package org.slc.sli.ingestion.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.common.util.performance.Profiled;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.Job;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.measurement.ExtractBatchJobIdToContext;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.Metrics;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;
import org.slc.sli.ingestion.transformation.TransformationFactory;
import org.slc.sli.ingestion.transformation.Transmogrifier;
import org.slc.sli.ingestion.util.BatchJobUtils;

/**
 * Camel processor for transformation of data.
 *
 * @author dduran
 *
 */
@Component
public class TransformationProcessor implements Processor {

    public static final BatchJobStageType BATCH_JOB_STAGE = BatchJobStageType.TRANSFORMATION_PROCESSOR;

    private static final Logger LOG = LoggerFactory.getLogger(TransformationProcessor.class);

    @Autowired
    private TransformationFactory transformationFactory;

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
            handleNoBatchJobId(exchange);
        } else {
            processTransformations(workNote, exchange);
        }
    }

    private void processTransformations(WorkNote workNote, Exchange exchange) {
        Stage stage = Stage.createAndStartStage(BATCH_JOB_STAGE);

        Metrics metrics = Metrics.createAndStart(workNote.getBatchJobId() + "-"
                + workNote.getIngestionStagedEntity().getCollectionNameAsStaged());
        stage.getMetrics().add(metrics);

        String batchJobId = workNote.getBatchJobId();
        NewBatchJob newJob = null;
        try {
            newJob = batchJobDAO.findBatchJobById(batchJobId);

            performDataTransformations(workNote, newJob);

        } catch (Exception e) {
            handleProcessingExceptions(exchange, batchJobId, e);
        } finally {
            metrics.stopMetric();

            BatchJobUtils.stopStageChunkAndAddToJob(stage, newJob);
            batchJobDAO.saveBatchJob(newJob);
        }
    }

    /**
     * Invokes transformations strategies
     *
     * @param workNote
     *            TODO
     * @param job
     */
    void performDataTransformations(WorkNote workNote, Job job) {
        LOG.info("performing data transformation BatchJob: {}", job);

        Transmogrifier transmogrifier = transformationFactory.createTransmogrifier(workNote, job);

        transmogrifier.executeTransformations();

    }

    private void handleNoBatchJobId(Exchange exchange) {
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

    public TransformationFactory getTransformationFactory() {
        return transformationFactory;
    }

    public void setTransformationFactory(TransformationFactory transformationFactory) {
        this.transformationFactory = transformationFactory;
    }

}
