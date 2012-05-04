package org.slc.sli.ingestion.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slc.sli.common.util.performance.Profiled;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.dal.NeutralRecordMongoAccess;
import org.slc.sli.ingestion.measurement.ExtractBatchJobIdToContext;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;
import org.slc.sli.ingestion.util.BatchJobUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Creates music sheets of work items that can be distributed to pit nodes.
 *
 * @author smelody
 * @author shalka
 */
@Component
public class MaestroOutboundProcessor implements Processor {

    public static final BatchJobStageType BATCH_JOB_STAGE = BatchJobStageType.MAESTRO_MUSIC_SHEET_CREATION;
    private static final Logger LOG = LoggerFactory.getLogger(MaestroOutboundProcessor.class);

    @Autowired
    private NeutralRecordMongoAccess neutralRecordMongoAccess;

    @Autowired
    private BatchJobDAO batchJobDAO;
        
    @Override
    @ExtractBatchJobIdToContext
    @Profiled
    public void process(Exchange exchange) throws Exception {
        String batchJobId = exchange.getIn().getHeader("BatchJobId", String.class);
        if (batchJobId == null) {
            handleNoBatchJobIdInExchange(exchange);
        } else {
            handleMusicSheetCreation(exchange, batchJobId);
        }
    }

    private void handleMusicSheetCreation(Exchange exchange, String batchJobId) {
        Stage stage = Stage.createAndStartStage(BATCH_JOB_STAGE);

        NewBatchJob newJob = null;
        try {
            newJob = batchJobDAO.findBatchJobById(batchJobId);
            boolean hasErrors = false;
            
            long attendanceCount = neutralRecordMongoAccess.getRecordRepository().getCollection("attendance").count();
            long assessmentCount = neutralRecordMongoAccess.getRecordRepository().getCollection("studentAssessmentAssociation").count();
            long studentSchoolCount = neutralRecordMongoAccess.getRecordRepository().getCollection("studentSchoolAssociation").count();
            long studentSectionCount = neutralRecordMongoAccess.getRecordRepository().getCollection("studentSectionAssociation").count();
            long studentDisciplineCount = neutralRecordMongoAccess.getRecordRepository().getCollection("studentDisciplineIncidentAssociation").count();
            LOG.warn("iii - Found attendances: {}", attendanceCount);
            LOG.warn("iii - Found assessments: {}", assessmentCount);
            LOG.warn("iii - Found student school associations: {}", studentSchoolCount);
            LOG.warn("iii - Found student section associations: {}", studentSectionCount);
            LOG.warn("iii - Found student discipline associations: {}", studentDisciplineCount);

            setExchangeHeaders(exchange, hasErrors);
        } catch (Exception exception) {
            handleProcessingExceptions(exchange, batchJobId, exception);
        } finally {
            if (newJob != null) {
                BatchJobUtils.stopStageAndAddToJob(stage, newJob);
                batchJobDAO.saveBatchJob(newJob);
            }
        }
    }
    
    private void handleNoBatchJobIdInExchange(Exchange exchange) {
        exchange.getIn().setHeader("ErrorMessage", "No BatchJobId specified in exchange header.");
        exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        LOG.error("Error:", "No BatchJobId specified in " + this.getClass().getName() + " exchange message header.");
    }

    private void handleProcessingExceptions(Exchange exchange, String batchJobId, Exception exception) {
        exchange.getIn().setHeader("ErrorMessage", exception.toString());
        exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        LOG.error("Exception:", exception);
        if (batchJobId != null) {
            Error error = Error.createIngestionError(batchJobId, null, BATCH_JOB_STAGE.getName(), null, null, null,
                    FaultType.TYPE_ERROR.getName(), null, exception.toString());
            batchJobDAO.saveError(error);
        }
    }

    private void setExchangeHeaders(Exchange exchange, boolean hasError) {
        if (hasError) {
            exchange.getIn().setHeader("hasErrors", hasError);
            exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        } else {
            //Keep the current processing for now
            exchange.getIn().setHeader("IngestionMessageType", MessageType.DATA_TRANSFORMATION.name());
        }
    }
}
