package org.slc.sli.ingestion.processors;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.landingzone.ControlFile;
import org.slc.sli.ingestion.landingzone.ControlFileDescriptor;
import org.slc.sli.ingestion.landingzone.LandingZone;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.model.da.BatchJobMongoDA;
import org.slc.sli.ingestion.queues.MessageType;

/**
 * Transforms body from ControlFile to ControlFileDescriptor type.
 *
 * @author okrook
 *
 */
public class ControlFilePreProcessor implements Processor {

    private LandingZone landingZone;

    Logger log = LoggerFactory.getLogger(ZipFileProcessor.class);

    public ControlFilePreProcessor(LandingZone lz) {
        this.landingZone = lz;
    }

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {

        String batchJobId = exchange.getIn().getHeader("BatchJobId", String.class);
        
        // TODO handle invalid control file (user error)
        // TODO handle IOException or other system error
        try {
            File controlFile = exchange.getIn().getBody(File.class);

            System.out.println("batchJobId: "+batchJobId);
            if (batchJobId == null) {
                batchJobId = NewBatchJob.createId(controlFile.getName());
                exchange.getIn().setHeader("BatchJobId", batchJobId);
                log.info("Created job [{}]", batchJobId);
            }
//            BatchJobDAO batchJobDAO = new BatchJobMongoDA();
//            NewBatchJob newJob = batchJobDAO.findBatchJobById(batchJobId);
            NewBatchJob newJob = new NewBatchJob();
            BatchJobDAO batchJobDAO = new BatchJobMongoDA();
            try {
                newJob = batchJobDAO.findBatchJobById(batchJobId);
                System.out.println(newJob.getStatus());
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            Stage stage = new Stage();
            stage.setStageName("ControlFilePreProcessor");
            newJob.startStage(stage);

            // JobLogStatus.startStage(batchJobId, stageName)
            
            ControlFile cf = ControlFile.parse(controlFile);

            newJob.stopStage(stage);
            newJob.getStages().add(stage);
            batchJobDAO.saveBatchJob(newJob);

            // set headers for ingestion routing
            exchange.getIn().setBody(new ControlFileDescriptor(cf, landingZone), ControlFileDescriptor.class);
            exchange.getIn().setHeader("IngestionMessageType", MessageType.BATCH_REQUEST.name());

            // TODO May not need this ... JobLogStatus.completeStage(batchJobId, stageName)

        } catch (Exception exception) {
            exchange.getIn().setHeader("ErrorMessage", exception.toString());
            exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
            log.error("Exception:",  exception);
            if (batchJobId != null) {
                BatchJobMongoDA.logBatchStageError(batchJobId, BatchJobStageType.CONTROL_FILE_PREPROCESSING, FaultType.TYPE_ERROR.getName(), null, exception.toString());
            }
        }

    }

}
