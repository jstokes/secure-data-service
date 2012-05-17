package org.slc.sli.ingestion.processors;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.Bytes;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.common.util.performance.Profiled;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.EntityMetadataKey;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordEntity;
import org.slc.sli.ingestion.Translator;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.slc.sli.ingestion.dal.NeutralRecordReadConverter;
import org.slc.sli.ingestion.handler.EntityPersistHandler;
import org.slc.sli.ingestion.handler.NeutralRecordEntityPersistHandler;
import org.slc.sli.ingestion.measurement.ExtractBatchJobIdToContext;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.Metrics;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;
import org.slc.sli.ingestion.transformation.SimpleEntity;
import org.slc.sli.ingestion.transformation.SmooksEdFi2SLITransformer;
import org.slc.sli.ingestion.util.BatchJobUtils;
import org.slc.sli.ingestion.validation.DatabaseLoggingErrorReport;
import org.slc.sli.ingestion.validation.ErrorReport;
import org.slc.sli.ingestion.validation.ProxyErrorReport;

/**
 * Ingestion Persistence Processor.
 *
 * Specific Ingestion Persistence Processor which provides specific SLI Ingestion instance
 * persistence behavior.
 * Persists data from Staged Database.
 *
 */
@Component
public class StagedDataPersistenceProcessor implements Processor {

    public static final BatchJobStageType BATCH_JOB_STAGE = BatchJobStageType.PERSISTENCE_PROCESSOR;

    private static final Logger LOG = LoggerFactory.getLogger(StagedDataPersistenceProcessor.class);

    @Autowired
    SmooksEdFi2SLITransformer transformer;

    // spring-loaded list of supported collections
    private Set<String> persistedCollections;

    private EntityPersistHandler entityPersistHandler;

    private NeutralRecordEntityPersistHandler obsoletePersistHandler;

    private NeutralRecordReadConverter neutralRecordReadConverter;

    @Autowired
    private NeutralRecordMongoAccess neutralRecordMongoAccess;

    @Autowired
    private BatchJobDAO batchJobDAO;

    /**
     * Camel Exchange process callback method
     *
     * @param exchange
     */
    @Override
    @ExtractBatchJobIdToContext
    @Profiled
    public void process(Exchange exchange) {

        WorkNote workNote = exchange.getIn().getBody(WorkNote.class);

        if (workNote == null || workNote.getBatchJobId() == null) {
            handleNoBatchJobIdInExchange(exchange);
        } else {
            processPersistence(workNote, exchange);
        }
    }

    private void processPersistence(WorkNote workNote, Exchange exchange) {
        Stage stage = Stage.createAndStartStage(BATCH_JOB_STAGE);

        String batchJobId = workNote.getBatchJobId();
        NewBatchJob newJob = null;
        try {
            newJob = batchJobDAO.findBatchJobById(batchJobId);
            LOG.info("processing persistence: {}", newJob);

            processWorkNote(workNote, newJob, stage);

        } catch (Exception exception) {
            handleProcessingExceptions(exception, exchange, batchJobId);
        } finally {
            if (newJob != null) {
                BatchJobUtils.stopStageAndAddToJob(stage, newJob);
                batchJobDAO.saveBatchJobStageSeparatelly(batchJobId, stage);
            }
        }
    }

    private void processWorkNote(WorkNote workNote, Job job, Stage stage) {

        long recordNumber = 0;
        long numFailed = 0;
        boolean persistedFlag = false;

        String collectionNameAsStaged = workNote.getIngestionStagedEntity().getCollectionNameAsStaged();
        String collectionToPersistFrom = getCollectionNameAfterTransform(job, collectionNameAsStaged);
        LOG.info("PERSISTING DATA IN COLLECTION: {} (staged as: {})", collectionToPersistFrom, collectionNameAsStaged);

        boolean noTransformationWasPerformed = collectionNameAsStaged.equals(collectionToPersistFrom);

        Map<String, Metrics> perFileMetrics = new HashMap<String, Metrics>();
        ErrorReport errorReportForCollection = createDbErrorReport(job.getId(), collectionNameAsStaged);

        try {

            int maxRecordNumberToPersist = workNote.getRangeMaximum() - workNote.getRangeMinimum();

            DBCursor cursor = getCollectionIterable(collectionToPersistFrom, job.getId(), workNote);
            Iterator<DBObject> dbObjectIterator = cursor.iterator();

            while (recordNumber <= maxRecordNumberToPersist && dbObjectIterator.hasNext()) {
                DBObject record = dbObjectIterator.next();

                numFailed = 0;
                
                recordNumber++;
                persistedFlag = false;

                NeutralRecord neutralRecord = neutralRecordReadConverter.convert(record);

                errorReportForCollection = createDbErrorReport(job.getId(), neutralRecord.getSourceFile());
                
                Metrics currentMetric = getOrCreateMetricForSourceFile(perFileMetrics, neutralRecord);

                // process NeutralRecord with old or new pipeline
                if (noTransformationWasPerformed) {
                    if (persistedCollections.contains(neutralRecord.getRecordType())) {
                        numFailed += processOldStyleNeutralRecord(neutralRecord, recordNumber, getTenantId(job),
                                errorReportForCollection);
                        persistedFlag = true;
                    }
                } else {
                    numFailed += processTransformableNeutralRecord(neutralRecord, getTenantId(job),
                            errorReportForCollection);
                    persistedFlag = true;
                }

                if (persistedFlag) {
                    currentMetric.setRecordCount(currentMetric.getRecordCount() + 1);
                }

                currentMetric.setErrorCount(currentMetric.getErrorCount() + numFailed);
                perFileMetrics.put(currentMetric.getResourceId(), currentMetric);
            }

        } catch (Exception e) {
            String fatalErrorMessage = "ERROR: Fatal problem saving records to database: \n" + "\tEntity\t"
                    + collectionNameAsStaged + "\n";
            errorReportForCollection.fatal(fatalErrorMessage, StagedDataPersistenceProcessor.class);
            LOG.error("Exception when attempting to ingest NeutralRecords in: " + collectionNameAsStaged + ".\n", e);
        } finally {

            Iterator<Metrics> it = perFileMetrics.values().iterator();
            while (it.hasNext()) {
                Metrics m = it.next();
                stage.getMetrics().add(m);
            }

        }
    }

    private long processTransformableNeutralRecord(NeutralRecord neutralRecord, String tenantId,
            ErrorReport errorReportForCollection) {
        long numFailed = 0;

        LOG.debug("processing transformable neutral record: {}", neutralRecord.getRecordType());

        // remove _transformed metadata from type. upcoming transformation is based on type.
        neutralRecord.setRecordType(neutralRecord.getRecordType().replaceFirst("_transformed", ""));

        // must set tenantId here, it is used by upcoming transformer.
        neutralRecord.setSourceId(tenantId);

        List<SimpleEntity> xformedEntities = transformer.handle(neutralRecord, errorReportForCollection);
        for (SimpleEntity xformedEntity : xformedEntities) {

            ErrorReport errorReportForNrEntity = new ProxyErrorReport(errorReportForCollection);

            if (xformedEntity.getType().equals("schoolSessionAssociation")) {

                persistSessionAndSchoolSessionAssociation(xformedEntity, errorReportForNrEntity);

            } else {

                entityPersistHandler.handle(xformedEntity, errorReportForNrEntity);

            }

            if (errorReportForNrEntity.hasErrors()) {
                numFailed++;
            }
        }

        return numFailed;
    }

    private void persistSessionAndSchoolSessionAssociation(SimpleEntity xformedEntity,
            ErrorReport errorReportForNrEntity) {
        SimpleEntity session = (SimpleEntity) xformedEntity.getBody().remove("session");

        Entity mongoSession = entityPersistHandler.handle(session, errorReportForNrEntity);
        xformedEntity.getBody().put("sessionId", mongoSession.getEntityId());

        entityPersistHandler.handle(xformedEntity, errorReportForNrEntity);

    }

    private long processOldStyleNeutralRecord(NeutralRecord neutralRecord, long recordNumber, String tenantId,
            ErrorReport errorReportForCollection) {
        long numFailed = 0;

        LOG.debug("processing old-style neutral record: {}", neutralRecord);

        NeutralRecordEntity nrEntity = Translator.mapToEntity(neutralRecord, recordNumber);
        nrEntity.setMetaDataField(EntityMetadataKey.TENANT_ID.getKey(), tenantId);

        ErrorReport errorReportForNrEntity = new ProxyErrorReport(errorReportForCollection);
        obsoletePersistHandler.handle(nrEntity, errorReportForNrEntity);

        if (errorReportForNrEntity.hasErrors()) {
            numFailed++;
        }

        return numFailed;
    }

    /**
     * returns a name of a collection which we should use in data persistence
     *
     * @param job
     *
     * @return collectionName
     */
    private String getCollectionNameAfterTransform(Job job, String collectionName) {

        String collectionNameTransformed = collectionName + "_transformed";

        boolean collectionExists = neutralRecordMongoAccess.getRecordRepository().collectionExistsForJob(
                collectionNameTransformed, job.getId());

        if (collectionExists) {
            if (neutralRecordMongoAccess.getRecordRepository().countForJob(collectionNameTransformed,
                    new NeutralQuery(), job.getId()) > 0) {
                LOG.info("FOUND TRANSFORMED COLLECTION WITH MORE THAN 0 RECORD = " + collectionNameTransformed);
                return (collectionNameTransformed);
            }
        }
        return collectionName;
    }

    private Metrics getOrCreateMetricForSourceFile(Map<String, Metrics> perFileMetrics, NeutralRecord neutralRecord) {
        Metrics currentMetric;
        if (perFileMetrics.containsKey(neutralRecord.getSourceFile())) {
            // metrics for this file is established
            currentMetric = perFileMetrics.get(neutralRecord.getSourceFile());
        } else {
            // establish new metrics
            currentMetric = Metrics.newInstance(neutralRecord.getSourceFile());
        }
        return currentMetric;
    }

    private DatabaseLoggingErrorReport createDbErrorReport(String batchJobId, String resourceId) {
        DatabaseLoggingErrorReport dbErrorReport = new DatabaseLoggingErrorReport(batchJobId, BATCH_JOB_STAGE,
                resourceId, batchJobDAO);
        return dbErrorReport;
    }

    private static String getTenantId(Job job) {
        // TODO this should be determined based on the sourceId
        String tenantId = job.getProperty("tenantId");
        if (tenantId == null) {
            tenantId = "SLI";
        }
        return tenantId;
    }

    private void handleNoBatchJobIdInExchange(Exchange exchange) {
        exchange.getIn().setHeader("ErrorMessage", "No BatchJobId specified in exchange header.");
        exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        LOG.error("Error:", "No BatchJobId specified in " + this.getClass().getName() + " exchange message header.");
    }

    private void handleProcessingExceptions(Exception exception, Exchange exchange, String batchJobId) {
        exchange.getIn().setHeader("ErrorMessage", exception.toString());
        exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        LOG.error("Exception:", exception);

        Error error = Error.createIngestionError(batchJobId, null, BATCH_JOB_STAGE.getName(), null, null, null,
                FaultType.TYPE_ERROR.getName(), "Exception", exception.getMessage());
        batchJobDAO.saveError(error);
    }

    public void setEntityPersistHandler(EntityPersistHandler entityPersistHandler) {
        this.entityPersistHandler = entityPersistHandler;
    }

    public NeutralRecordEntityPersistHandler getObsoletePersistHandler() {
        return obsoletePersistHandler;
    }

    public void setObsoletePersistHandler(NeutralRecordEntityPersistHandler obsoletePersistHandler) {
        this.obsoletePersistHandler = obsoletePersistHandler;
    }

    public Set<String> getPersistedCollections() {
        return persistedCollections;
    }

    public void setPersistedCollections(Set<String> persistedCollections) {
        this.persistedCollections = persistedCollections;
    }

    public NeutralRecordReadConverter getNeutralRecordReadConverter() {
        return neutralRecordReadConverter;
    }

    public void setNeutralRecordReadConverter(NeutralRecordReadConverter neutralRecordReadConverter) {
        this.neutralRecordReadConverter = neutralRecordReadConverter;
    }

    protected DBCursor getCollectionIterable(String collectionName, String jobId, WorkNote workNote) {
        DBCollection col = neutralRecordMongoAccess.getRecordRepository().getCollectionForJob(collectionName, jobId);

        DBCursor dbcursor = col.find();
        dbcursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);
        dbcursor.batchSize(1000);
        dbcursor.skip(workNote.getRangeMinimum());

        return dbcursor;
    }
}
