package org.slc.sli.ingestion.util;

import static org.slc.sli.ingestion.model.da.BatchJobMongoDA.logIngestionError;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.ingestion.BatchJob;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.Job;

/**
 * Utilities for BatchJob
 *
 * @author dduran
 *
 */
public class BatchJobUtils {

    private static final Logger LOG = LoggerFactory.getLogger(BatchJobUtils.class);

    public static final String TIMESTAMPPATTERN = "yyyy-MM-dd:HH-mm-ss";

    private static InetAddress localhost;
    static {
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given camel exchange, return batch job using state manager specified in system properties.
     * This should be refactored to be an interface with different implementations.
     *
     * @param exchange
     * @return
     */
    public static Job getBatchJobUsingStateManager(Exchange exchange) {

        Job batchJob = null;

        if ("mongodb".equals(System.getProperty("state.manager"))) {

            // Get the batch job ID from the exchange
            String batchJobId = exchange.getIn().getBody(String.class);
            LOG.info("pulling BatchJob {} from mongodb", batchJobId);

            // TODO usually we will want to startStage(batchJobId, this.getClass().getName()) before
            // getting the job from the db

            // TODO: get batch job from db based on jobId. something like:
            // batchJob = dataAccess.getBatchJob(batchJobId);

        } else {
            LOG.info("pulling BatchJob from camel exchange");
            batchJob = exchange.getIn().getBody(BatchJob.class);

        }
        return batchJob;
    }

    /**
     * Save BatchJob using state maanger specified in system properties.
     * This should be refactored to be an interface with different implementations.
     *
     * @param job
     */

    public static void saveBatchJobUsingStateManager(Job job) {

        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("saving BatchJob {} to mongodb", job.getId());
            // TODO usually we will want to set the stage stopTimeStamp before saving the job to db
            // TODO: save batch job to db

        } else {
            // nothing
            LOG.info("camel exchange references updated BatchJob {}", job.getId());
        }
    }

    /**
     * Create a batch job with startTimeStamp of now in the db
     *
     * @param batchJobId
     */
    public static void createBatchJob(String batchJobId) {
        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("start a db managed batch job");
            // TODO: create a batch job with startTime now in the db

        }
    }

    /**
     * Complete a batch job setting stopTimeStamp to now in the db
     *
     * @param batchJobId
     */
    public static void completeBatchJob(String batchJobId) {
        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("complete a db managed batch job");
            // TODO: update a batch job with with status "completed" and startTime now in the db

        }
    }

    /**
     * Create a stage in the db with startTimeStamp of now
     *
     * @param batchJobId
     * @param stageName
     */
    public static void beginStage(Job job, String stageName) {
        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("started a stage in the db managed batch job");
            // TODO: create a stage field with status "running" and startTime of now in the db
        }
    }

    /**
     * Update the stage field in the db to have stopTimeStamp of now
     *
     * @param batchJob
     * @param stageName
     */
    public static void endStage(Job job, String stageName) {
        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("stopped the stage in the db managed batch job");
            // TODO: update the stage status to "completed" and the stopTimeStamp to now in the db

        }
    }

    /**
     * Create a metric with startTime of current system time
     *
     * @param batchJobId
     * @param stageName
     * @param fileId
     */
    public static void startMetric(String batchJobId, String stageName, String fileId) {
        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("created a metric");
            // TODO: create a metric with startTimeStamp of now in the db

        }
    }

    /**
     * Update the metric in the db with the current time as stopTime and the specified record and
     * error counts
     *
     * @param batchJobId
     * @param stageName
     * @param fileId
     * @param recordCount
     * @param errorCount
     */
    public static void stopMetric(String batchJobId, String stageName, String fileId, int recordCount, int errorCount) {
        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("stopped a metric");
            // TODO: update the metric with the stopTimeStamp of now and counts as specified in the
            // db

        }
    }

    public static String getHostAddress() {
        return localhost.getHostAddress();
    }

    public static String getHostName() {
        return localhost.getHostName();
    }

    public static String getTimeStamp() {
        return getTimeStamp(TIMESTAMPPATTERN);
    }

    public static String getTimeStamp(String pattern) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(cal.getTime());
    }

    // TODO: enumerate severity for errors

    /**
     * Write a batch job error using state manager system properties.
     * This should be refactored to be an interface with different implementations.
     *
     * @param batchJobId
     * @param stageName
     * @param fileId
     * @param severity
     * @param errorDetail
     */
    public static void logError(String batchJobId, BatchJobStageType stage, String fileId, String severity,
            String errorDetail) {

        if ("mongodb".equals(System.getProperty("state.manager"))) {

            LOG.info("creating a error in the db");
            // TODO: create an error document in the db

            logIngestionError(batchJobId, stage.getName(), fileId, null, null, null, severity, "generic", errorDetail);
        }
    }

    public static void logBatchError(String batchJobId, String severity, String errorDetail) {
        logError(batchJobId, null, null, severity, errorDetail);
    }

    public static void logBatchStageError(String batchJobId, BatchJobStageType stage, String severity,
            String errorDetail) {
        logError(batchJobId, stage, null, severity, errorDetail);
    }

}
