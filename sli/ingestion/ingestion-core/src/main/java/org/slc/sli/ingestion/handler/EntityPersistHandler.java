/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.slc.sli.ingestion.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.MongoException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;

import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.common.util.datetime.DateTimeUtil;
import org.slc.sli.common.util.uuid.DeterministicUUIDGeneratorStrategy;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.slc.sli.ingestion.FileProcessStatus;
import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.AbstractReportStats;
import org.slc.sli.ingestion.reporting.CoreMessageCode;
import org.slc.sli.ingestion.transformation.SimpleEntity;
import org.slc.sli.ingestion.transformation.normalization.ComplexKeyField;
import org.slc.sli.ingestion.transformation.normalization.EntityConfig;
import org.slc.sli.ingestion.transformation.normalization.EntityConfigFactory;
import org.slc.sli.ingestion.util.spring.MessageSourceHelper;
import org.slc.sli.validation.EntityValidationException;
import org.slc.sli.validation.EntityValidator;
import org.slc.sli.validation.NoNaturalKeysDefinedException;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.ValidationError;
import org.slc.sli.validation.schema.AppInfo;
import org.slc.sli.validation.schema.INaturalKeyExtractor;
import org.slc.sli.validation.schema.NeutralSchema;

/**
 * Handles the persisting of Entity objects
 *
 * @author dduran
 *         Modified by Thomas Shewchuk (PI3 US811)
 *         - 2/1/2010 Added record DB lookup and update capabilities, and support for association
 *         entities.
 *
 */
public class EntityPersistHandler extends AbstractIngestionHandler<SimpleEntity, Entity> implements InitializingBean {

    public static final Logger LOG = LoggerFactory.getLogger(EntityPersistHandler.class);

    private Repository<Entity> entityRepository;
    private EntityConfigFactory entityConfigurations;
    private MessageSource messageSource;

    @Value("${sli.ingestion.mongotemplate.writeConcern}")
    private String writeConcern;

    @Value("${sli.ingestion.referenceSchema.referenceCheckEnabled}")
    private String referenceCheckEnabled;

    @Value("${sli.ingestion.totalRetries}")
    private int totalRetries;

    @Autowired
    private SchemaRepository schemaRepository;

    @Autowired
    private EntityValidator validator;

    @Autowired
    private INaturalKeyExtractor naturalKeyExtractor;

    @Autowired
    private DeterministicUUIDGeneratorStrategy deterministicUUIDGeneratorStrategy;

    @Override
    public void afterPropertiesSet() throws Exception {
        entityRepository.setWriteConcern(writeConcern);
        entityRepository.setReferenceCheck(referenceCheckEnabled);
    }

    /**
     * Persist entity in the data store.
     *
     * @param entity
     *            Entity to be persisted
     * @return Persisted entity
     * @throws EntityValidationException
     *             Validation Exception
     */
    private Entity persist(SimpleEntity entity) throws EntityValidationException {

        String collectionName = "";
        NeutralSchema schema = schemaRepository.getSchema(entity.getType());
        if (schema != null) {
            AppInfo appInfo = schema.getAppInfo();
            if (appInfo != null) {
                collectionName = appInfo.getCollectionType();
            }
        }

        if (entity.getEntityId() != null) {

            if (!entityRepository.updateWithRetries(collectionName, entity, totalRetries)) {
                // TODO: exception should be replace with some logic.
                throw new RuntimeException("Record was not updated properly.");
            }

            return entity;
        } else {
            return entityRepository.createWithRetries(entity.getType(), null, entity.getBody(), entity.getMetaData(),
                    collectionName, totalRetries);
        }
    }

    boolean update(String collectionName, Entity entity, List<Entity> failed, AbstractMessageReport report,
            AbstractReportStats reportStats) {
        boolean res = false;

        try {
            res = entityRepository.updateWithRetries(collectionName, entity, totalRetries);
            if (!res) {
                failed.add(entity);
            }
        } catch (MongoException e) {
            reportWarnings(e.getCause().getMessage(), collectionName, ((SimpleEntity) entity).getSourceFile(), report,
                    reportStats);
        }

        return res;
    }

    private List<Entity> persist(List<SimpleEntity> entities, AbstractMessageReport report,
            AbstractReportStats reportStats) {
        List<Entity> failed = new ArrayList<Entity>();
        List<Entity> queued = new ArrayList<Entity>();
        Map<List<Object>, SimpleEntity> memory = new HashMap<List<Object>, SimpleEntity>();
        String collectionName = getCollectionName(entities.get(0));
        EntityConfig entityConfig = entityConfigurations.getEntityConfiguration(entities.get(0).getType());

        for (SimpleEntity entity : entities) {
            if (entity.getEntityId() != null) {
                update(collectionName, entity, failed, report, reportStats);
            } else {
                preMatchEntity(memory, entityConfig, report, entity, reportStats);
            }
        }

        for (Map.Entry<List<Object>, SimpleEntity> entry : memory.entrySet()) {
            SimpleEntity entity = entry.getValue();
            LOG.debug("Processing: {}", entity.getType());
            try {
                validator.validate(entity);
                addTimestamps(entity);
                queued.add(entity);
            } catch (EntityValidationException e) {
                reportErrors(e.getValidationErrors(), entity, report, reportStats);
                failed.add(entity);
            }
        }

        try {
            LOG.info("Bulk insert of {} queued records into collection: {}", new Object[] { queued.size(),
                    collectionName });
            entityRepository.insert(queued, collectionName);
        } catch (Exception e) {
            // Assuming there would NOT be DuplicateKeyException at this point.
            // Because "queued" only contains new records(with no Id), and we don't have unique
            // indexes
            LOG.warn("Bulk insert failed --> Performing upsert for each record that was queued.");

            // Try to do individual upsert again for other exceptions
            for (Entity entity : queued) {
                update(collectionName, entity, failed, report, reportStats);
            }
        }

        return failed;
    }

    private void preMatchEntity(Map<List<Object>, SimpleEntity> memory, EntityConfig entityConfig,
            AbstractMessageReport report, SimpleEntity entity, AbstractReportStats reportStats) {

        NaturalKeyDescriptor naturalKeyDescriptor;
        try {
            naturalKeyDescriptor = naturalKeyExtractor.getNaturalKeyDescriptor(entity);
        } catch (NoNaturalKeysDefinedException e1) {
            LOG.error(e1.getMessage(), e1);
            return;
        }

        if (naturalKeyDescriptor.isNaturalKeysNotNeeded()) {
            String message = "Unable to find natural keys fields" + "       Entity     " + entity.getType() + "\n"
                    + "       Instance   " + entity.getRecordNumber();
            LOG.error(message);

            preMatchEntityWithNaturalKeys(memory, entityConfig, report, entity, reportStats);
        } else {
            // "new" style -> based on natural keys from schema
            String id = deterministicUUIDGeneratorStrategy.generateId(naturalKeyDescriptor);
            List<Object> keyValues = new ArrayList<Object>();
            keyValues.add(id);
            entity.setEntityId(id);
            memory.put(keyValues, entity);
        }
    }

    private void preMatchEntityWithNaturalKeys(Map<List<Object>, SimpleEntity> memory, EntityConfig entityConfig,
            AbstractMessageReport report, SimpleEntity entity, AbstractReportStats reportStats) {
        List<String> keyFields = entityConfig.getKeyFields();
        ComplexKeyField complexField = entityConfig.getComplexKeyField();
        if (keyFields.size() > 0) {
            List<Object> keyValues = new ArrayList<Object>();
            for (String field : keyFields) {
                try {
                    keyValues.add(PropertyUtils.getProperty(entity, field));
                } catch (Exception e) {
                    report.error(reportStats, CoreMessageCode.CORE_0008, null, field, entity.getType());
                }

                if (complexField != null) {
                    String propertyString = complexField.getListPath() + ".[0]." + complexField.getFieldPath();

                    try {
                        keyValues.add(PropertyUtils.getProperty(entity, propertyString));
                    } catch (Exception e) {
                        report.error(reportStats, CoreMessageCode.CORE_0008, null, field, entity.getType());
                    }
                }

            }
            memory.put(keyValues, entity);
        }
    }

    private String getCollectionName(Entity entity) {
        String collectionName = null;
        NeutralSchema schema = schemaRepository.getSchema(entity.getType());
        if (schema != null) {
            AppInfo appInfo = schema.getAppInfo();
            if (appInfo != null) {
                collectionName = appInfo.getCollectionType();
            }
        }
        return collectionName;
    }

    /**
     * Generic error reporting function.
     *
     * @param error
     *            List of errors to report.
     * @param entity
     *            Entity reporting errors.
     * @param errorReport
     *            Reference to error report logging error messages.
     */
    private void reportErrors(List<ValidationError> errors, SimpleEntity entity, AbstractMessageReport report,
            AbstractReportStats reportStats) {
        for (ValidationError err : errors) {
            report.error(reportStats, CoreMessageCode.CORE_0006, err.getType().name(), entity.getType(),
                    Long.toString(entity.getRecordNumber()), err.getFieldName(), err.getFieldValue(), Arrays.toString(err.getExpectedTypes()));
        }
    }

    /**
     * Generic warning reporting function.
     *
     * @param warningMessage
     *            Warning message reported by entity.
     * @param entity
     *            Entity reporting warning.
     * @param errorReport
     *            Reference to error report to log warning message in.
     */
    private void reportWarnings(String warningMessage, String type, String resourceId, AbstractMessageReport report,
            AbstractReportStats reportStats) {
        report.warning(reportStats, CoreMessageCode.CORE_0007, type, warningMessage);
    }

    protected String getFailureMessage(String code, Object... args) {
        return MessageSourceHelper.getMessage(messageSource, code, args);
    }

    public void setEntityRepository(Repository<Entity> entityRepository) {
        this.entityRepository = entityRepository;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public EntityConfigFactory getEntityConfigurations() {
        return entityConfigurations;
    }

    public void setEntityConfigurations(EntityConfigFactory entityConfigurations) {
        this.entityConfigurations = entityConfigurations;
    }

    private void addTimestamps(Entity entity) {
        Date now = DateTimeUtil.getNowInUTC();
        entity.getMetaData().put("created", now);
        entity.getMetaData().put("updated", now);
    }

    Entity doHandling(SimpleEntity entity, AbstractMessageReport report, AbstractReportStats reportStats) {
        return doHandling(entity, report, reportStats, null);
    }

    @Override
    protected Entity doHandling(SimpleEntity item, AbstractMessageReport report, AbstractReportStats reportStats,
            FileProcessStatus fileProcessStatus) {
        try {
            return persist(item);
        } catch (EntityValidationException ex) {
            reportErrors(ex.getValidationErrors(), item, report, reportStats);
        } catch (DuplicateKeyException ex) {
            reportWarnings(ex.getRootCause().getMessage(), item.getType(), item.getSourceFile(), report, reportStats);
        }
        return null;
    }

    @Override
    protected List<Entity> doHandling(List<SimpleEntity> items, AbstractMessageReport report,
            AbstractReportStats reportStats, FileProcessStatus fileProcessStatus) {
        // TODO Auto-generated method stub
        return persist(items, report, reportStats);
    }
}
