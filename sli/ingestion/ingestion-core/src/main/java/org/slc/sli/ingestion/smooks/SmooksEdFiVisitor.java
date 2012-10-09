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

package org.slc.sli.ingestion.smooks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.MongoException;

import org.milyn.container.ExecutionContext;
import org.milyn.delivery.sax.SAXElement;
import org.milyn.delivery.sax.SAXElementVisitor;
import org.milyn.delivery.sax.SAXText;
import org.milyn.delivery.sax.annotation.StreamResultWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.mongodb.UncategorizedMongoDbException;

import org.slc.sli.common.domain.NaturalKeyDescriptor;
import org.slc.sli.common.util.uuid.DeterministicUUIDGeneratorStrategy;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.ResourceWriter;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.util.NeutralRecordUtils;
import org.slc.sli.ingestion.validation.ErrorReport;

/**
 * Visitor that writes a neutral record or reports errors encountered.
 *
 * @author dduran
 *
 */
@StreamResultWriter
public final class SmooksEdFiVisitor implements SAXElementVisitor {

    // Logging
    private static final Logger LOG = LoggerFactory.getLogger(SmooksEdFiVisitor.class);

    /** Constant to write a log message every N records. */
    private static final int FLUSH_QUEUE_THRESHOLD = 10000;

    private static final int FIRST_INSTANCE = 1;

    private ResourceWriter<NeutralRecord> nrMongoStagingWriter;

    private final String beanId;
    private final String batchJobId;
    private final ErrorReport errorReport;
    private final IngestionFileEntry fe;
    private final String tenantId;

    private Map<String, Integer> occurences;
    private Map<String, List<NeutralRecord>> queuedWrites;
    private int recordsPerisisted;

    private DeterministicUUIDGeneratorStrategy deterministicUUIDGeneratorStrategy;

    private BatchJobDAO batchJobDAO;
    private Set<String> recordLevelDeltaEnabledEntities;

    private Map<String, Long> duplicateCounts = new HashMap<String, Long>();

    /**
     * Get records persisted to data store. If there are still queued writes waiting, flush the
     * queue by writing to data store before returning final count.
     *
     * @return Final number of records persisted to data store.
     */
    public int getRecordsPerisisted() {
        writeAndClearQueuedNeutralRecords();
        return recordsPerisisted;
    }

    private SmooksEdFiVisitor(String beanId, String batchJobId, ErrorReport errorReport, IngestionFileEntry fe,
            String tenantId, DeterministicUUIDGeneratorStrategy deterministicUUIDGeneratorStrategy) {
        this.beanId = beanId;
        this.batchJobId = batchJobId;
        this.errorReport = errorReport;
        this.fe = fe;
        this.occurences = new HashMap<String, Integer>();
        this.recordsPerisisted = 0;
        this.queuedWrites = new HashMap<String, List<NeutralRecord>>();
        this.deterministicUUIDGeneratorStrategy = deterministicUUIDGeneratorStrategy;
        this.tenantId = tenantId;
    }

    public static SmooksEdFiVisitor createInstance(String beanId, String batchJobId, ErrorReport errorReport,
            IngestionFileEntry fe, String tenantId,
            DeterministicUUIDGeneratorStrategy deterministicUUIDGeneratorStrategy) {
        return new SmooksEdFiVisitor(beanId, batchJobId, errorReport, fe, tenantId, deterministicUUIDGeneratorStrategy);
    }

    @Override
    public void visitAfter(SAXElement element, ExecutionContext executionContext) throws IOException {

        Throwable terminationError = executionContext.getTerminationError();
        if (terminationError == null) {
            NeutralRecord neutralRecord = getProcessedNeutralRecord(executionContext);

            if (!recordLevelDeltaEnabledEntities.contains(neutralRecord.getRecordType())) {
                queueNeutralRecordForWriting(neutralRecord);
            } else {
                if (!SliDeltaManager.isPreviouslyIngested(neutralRecord, batchJobDAO)) {
                    queueNeutralRecordForWriting(neutralRecord);

                } else {
                    String type = neutralRecord.getRecordType();
                    Long count = duplicateCounts.containsKey(type) ? duplicateCounts.get(type) : new Long(0);
                    duplicateCounts.put(type, new Long(count.longValue() + 1));
                }
            }

            if (recordsPerisisted % FLUSH_QUEUE_THRESHOLD == 0) {
                writeAndClearQueuedNeutralRecords();
            }
        } else {

            // Indicate Smooks Validation Failure
            LOG.error("Smooks validation failure at element " + element.getName().toString());

            if (errorReport != null) {
                errorReport.error(terminationError.getMessage(), SmooksEdFiVisitor.class);
            }
        }
    }

    /**
     * Adds the Neutral Record to the queue of Neutral Records waiting to be written.
     *
     * @param record
     *            Neutral Record to be written to data store.
     */
    private void queueNeutralRecordForWriting(NeutralRecord record) {
        if (!queuedWrites.containsKey(record.getRecordType())) {
            queuedWrites.put(record.getRecordType(), new ArrayList<NeutralRecord>());
        }
        queuedWrites.get(record.getRecordType()).add(record);
        this.recordsPerisisted++;
    }

    /**
     * Write all neutral records currently contained in the queue, and clear the queue.
     */
    private void writeAndClearQueuedNeutralRecords() {
        if (recordsPerisisted != 0) {
            for (Map.Entry<String, List<NeutralRecord>> entry : queuedWrites.entrySet()) {
                if (entry.getValue().size() > 0) {
                    try {
                        nrMongoStagingWriter.insertResources(entry.getValue(), entry.getKey(), batchJobId);
                        LOG.info("Persisted {} records of type {} ", entry.getValue().size(), entry.getKey());
                        queuedWrites.get(entry.getKey()).clear();
                    } catch (DataAccessResourceFailureException darfe) {
                        LOG.error("Exception processing record with entityPersistentHandler", darfe);
                    } catch (InvalidDataAccessApiUsageException ex) {
                        LOG.error("Exception processing record with entityPersistentHandler", ex);
                    } catch (InvalidDataAccessResourceUsageException ex) {
                        LOG.error("Exception processing record with entityPersistentHandler", ex);
                    } catch (MongoException me) {
                        LOG.error("Exception processing record with entityPersistentHandler", me);
                    } catch (UncategorizedMongoDbException ex) {
                        LOG.error("Exception processing record with entityPersistentHandler", ex);
                    }
                }
            }
        }
    }

    private NeutralRecord getProcessedNeutralRecord(ExecutionContext executionContext) {
        NeutralRecord neutralRecord = (NeutralRecord) executionContext.getBeanContext().getBean(beanId);
        neutralRecord.setBatchJobId(batchJobId);
        neutralRecord.setSourceFile(fe == null ? "" : fe.getFileName());

        if (this.occurences.containsKey(neutralRecord.getRecordType())) {
            int temp = this.occurences.get(neutralRecord.getRecordType()).intValue() + 1;
            this.occurences.put(neutralRecord.getRecordType(), temp);
            neutralRecord.setLocationInSourceFile(temp);
        } else {
            this.occurences.put(neutralRecord.getRecordType(), FIRST_INSTANCE);
            neutralRecord.setLocationInSourceFile(FIRST_INSTANCE);
        }

        // scrub empty strings in NeutralRecord (this is needed for the current way we parse CSV
        // files)
        neutralRecord.setAttributes(NeutralRecordUtils.scrubEmptyStrings(neutralRecord.getAttributes()));
        if (String.class.isInstance(neutralRecord.getLocalId())) {
            neutralRecord.setLocalId(((String) neutralRecord.getLocalId()).trim());
        }

        String entityType = neutralRecord.getRecordType();

        // Calculate deterministic id for educationOrganization and school
        // This is important because the edOrg id is currently used for stamping metaData
        // during ingestion. Therefore, the id needs to be known now, rather than
        // waiting till the entity is persisted in the DAL.

        if ("stateEducationAgency".equals(entityType) || "school".equals(entityType)
                || "localEducationAgency".equals(entityType)) {

            // Normally, NaturalKeyDescriptors are generated based on the sli.xsd, but in this
            // case, we need to generate one ahead of time (for context stamping), so it will
            // be built by hand in this case
            Map<String, String> naturalKeys = new HashMap<String, String>();
            String stateOrganizationId = (String) neutralRecord.getAttributes().get("stateOrganizationId");
            naturalKeys.put("stateOrganizationId", stateOrganizationId);

            NaturalKeyDescriptor descriptor = new NaturalKeyDescriptor(naturalKeys, tenantId,
                    neutralRecord.getRecordType());
            descriptor.setEntityType("educationOrganization");

            String deterministicId = deterministicUUIDGeneratorStrategy.generateId(descriptor);
            neutralRecord.setRecordId(deterministicId);
        }

        return neutralRecord;
    }

    public void setNrMongoStagingWriter(ResourceWriter<NeutralRecord> nrMongoStagingWriter) {
        this.nrMongoStagingWriter = nrMongoStagingWriter;
    }

    public void setBatchJobDAO(BatchJobDAO batchJobDAO) {
        this.batchJobDAO = batchJobDAO;
    }

    public void setRecordLevelDeltaEnabledEntities(Set<String> entities) {
        this.recordLevelDeltaEnabledEntities = entities;
    }

    /* we are not using the below visitor hooks */

    @Override
    public void visitBefore(SAXElement element, ExecutionContext executionContext) {
        // nothing
    }

    @Override
    public void onChildElement(SAXElement element, SAXElement childElement, ExecutionContext executionContext) {
        // nothing
    }

    @Override
    public void onChildText(SAXElement element, SAXText childText, ExecutionContext executionContext) {
        // nothing

    }

    public Map<String, Long> getDuplicateCounts() {
        return duplicateCounts;
    }

    public void setDuplicateCounts(Map<String, Long> duplicateCounts) {
        this.duplicateCounts = duplicateCounts;
    }


}
