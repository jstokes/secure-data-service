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

package org.slc.sli.ingestion.transformation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityMetadataKey;
import org.slc.sli.domain.Repository;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordEntity;
import org.slc.sli.ingestion.handler.Handler;
import org.slc.sli.ingestion.transformation.normalization.ComplexRefDef;
import org.slc.sli.ingestion.transformation.normalization.EntityConfig;
import org.slc.sli.ingestion.transformation.normalization.EntityConfigFactory;
import org.slc.sli.ingestion.transformation.normalization.IdNormalizer;
import org.slc.sli.ingestion.transformation.normalization.RefDef;
import org.slc.sli.ingestion.validation.DummyErrorReport;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.slc.sli.validation.SchemaRepository;
import org.slc.sli.validation.schema.AppInfo;
import org.slc.sli.validation.schema.NeutralSchema;

/**
 * EdFi to SLI data transformation
 *
 * @author okrook
 *
 */
public abstract class EdFi2SLITransformer implements Handler<NeutralRecord, List<SimpleEntity>> {

    private static final Logger LOG = LoggerFactory.getLogger(EdFi2SLITransformer.class);

    protected static final String METADATA_BLOCK = "metaData";

    private IdNormalizer idNormalizer;

    private EntityConfigFactory entityConfigurations;

    private Repository<Entity> entityRepository;

    @Autowired
    private SchemaRepository schemaRepository;

    @Override
    public List<SimpleEntity> handle(NeutralRecord item) {
        return handle(item, new DummyErrorReport());
    }

    @Override
    public List<SimpleEntity> handle(NeutralRecord item, ErrorReport errorReport) {
        resolveReferences(item, errorReport);

        if (errorReport.hasErrors()) {
            return Collections.emptyList();
        }

        List<SimpleEntity> transformed = transform(item, errorReport);

        if (errorReport.hasErrors()) {
            return Collections.emptyList();
        }

        if (transformed != null && !transformed.isEmpty()) {

            for (SimpleEntity entity : transformed) {

                if (entity.getMetaData() == null) {
                    entity.setMetaData(new HashMap<String, Object>());
                }

                entity.getMetaData().put(EntityMetadataKey.TENANT_ID.getKey(), item.getSourceId());

                try {
                    matchEntity(entity, errorReport);
                } catch (DataAccessResourceFailureException darfe) {
                    LOG.error("Exception in matchEntity", darfe);
                }

                if (errorReport.hasErrors()) {
                    return Collections.emptyList();
                }
            }
        } else {
            LOG.error("EdFi2SLI Transform has resulted in either a null or empty list of transformed SimpleEntities.");
        }

        return transformed;
    }

    protected void resolveReferences(NeutralRecord item, ErrorReport errorReport) {
        Entity entity = new NeutralRecordEntity(item);
        EntityConfig entityConfig = entityConfigurations.getEntityConfiguration(entity.getType());

        ComplexRefDef ref = entityConfig.getComplexReference();
        if (ref != null) {
            String collectionName = "";
            NeutralSchema schema = schemaRepository.getSchema(ref.getEntityType());
            if (schema != null) {
                AppInfo appInfo = schema.getAppInfo();
                if (appInfo != null) {
                    collectionName = appInfo.getCollectionType();
                }
            }

            idNormalizer.resolveReferenceWithComplexArray(entity, item.getSourceId(), ref.getValueSource(),
                    ref.getFieldPath(), collectionName, ref.getPath(), ref.getComplexFieldNames(), errorReport);
        }

        idNormalizer.resolveInternalIds(entity, item.getSourceId(), entityConfig, errorReport);
    }

    /**
     * Find a matched entity in the data store. If match is found the EntityID gets updated with the
     * ID from the data store.
     *
     * @param entity
     *            Entity to match
     * @param entityConfig
     *            Configuration for the entity
     * @param errorReport
     *            Error reporting
     */
    protected void matchEntity(SimpleEntity entity, ErrorReport errorReport) {
        EntityConfig entityConfig = entityConfigurations.getEntityConfiguration(entity.getType());

        Query query = createEntityLookupQuery(entity, entityConfig, errorReport);

        if (errorReport.hasErrors()) {
            return;
        }

        String collection = "";
        NeutralSchema schema = schemaRepository.getSchema(entity.getType());
        if (schema != null) {
            AppInfo appInfo = schema.getAppInfo();
            if (appInfo != null) {
                collection = appInfo.getCollectionType();
            }
        }

        @SuppressWarnings("deprecation")
        Iterable<Entity> match = entityRepository.findByQuery(collection, query, 0, 0);

        if (match != null && match.iterator().hasNext()) {
            // Entity exists in data store.
            Entity matched = match.iterator().next();
            entity.setEntityId(matched.getEntityId());
            entity.getMetaData().putAll(matched.getMetaData());
        }
    }

    /**
     * Create entity lookup query from EntityConfig fields
     *
     * @param entity
     *            : the entity to be looked up.
     * @param keyFields
     *            : the list of the fields with which to generate the filter
     * @param errorReport
     *            : error reporting
     * @return Look up filter
     *
     * @author tke
     */
    protected Query createEntityLookupQuery(SimpleEntity entity, EntityConfig entityConfig, ErrorReport errorReport) {
        Query query = new Query();

        String errorMessage = "ERROR: Invalid key fields for an entity\n";
        if (entityConfig.getKeyFields() == null || entityConfig.getKeyFields().size() == 0) {
            errorReport.fatal("Cannot find a match for an entity: No key fields specified", this);
        } else {
            errorMessage += "       Entity      " + entity.getType() + "\n" + "       Key Fields  "
                    + entityConfig.getKeyFields() + "\n";
            if (entityConfig.getReferences() != null && entityConfig.getReferences().size() > 0) {
                errorMessage += "     The following collections are referenced by the key fields:" + "\n";
                for (RefDef refDef : entityConfig.getReferences()) {
                    String collectionName = "";
                    NeutralSchema schema = schemaRepository.getSchema(refDef.getRef().getEntityType());
                    if (schema != null) {
                        AppInfo appInfo = schema.getAppInfo();
                        if (appInfo != null) {
                            collectionName = appInfo.getCollectionType();
                        }
                    }

                    errorMessage += "       collection = " + collectionName + "\n";
                }
            }
        }

        String tenantId = entity.getMetaData().get(EntityMetadataKey.TENANT_ID.getKey()).toString();
        query.addCriteria(Criteria.where(METADATA_BLOCK + "." + EntityMetadataKey.TENANT_ID.getKey()).is(tenantId));

        try {
            for (String field : entityConfig.getKeyFields()) {
                Object fieldValue = PropertyUtils.getProperty(entity, field);
                query.addCriteria(Criteria.where(field).is(fieldValue));
            }
        } catch (Exception e) {
            errorReport.error(errorMessage, this);
        }

        return query;
    }

    protected abstract List<SimpleEntity> transform(NeutralRecord item, ErrorReport errorReport);

    public IdNormalizer getIdNormalizer() {
        return idNormalizer;
    }

    public void setIdNormalizer(IdNormalizer idNormalizer) {
        this.idNormalizer = idNormalizer;
    }

    public EntityConfigFactory getEntityConfigurations() {
        return entityConfigurations;
    }

    public void setEntityConfigurations(EntityConfigFactory entityConfigurations) {
        this.entityConfigurations = entityConfigurations;
    }

    public Repository<Entity> getEntityRepository() {
        return entityRepository;
    }

    public void setEntityRepository(Repository<Entity> entityRepository) {
        this.entityRepository = entityRepository;
    }
}
