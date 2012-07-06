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

package org.slc.sli.ingestion.processors;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import org.slc.sli.common.util.logging.LogLevelType;
import org.slc.sli.common.util.logging.SecurityEvent;
import org.slc.sli.dal.TenantContext;
import org.slc.sli.ingestion.BatchJobStageType;
import org.slc.sli.ingestion.BatchJobStatusType;
import org.slc.sli.ingestion.FaultType;
import org.slc.sli.ingestion.FaultsReport;
import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.landingzone.ControlFile;
import org.slc.sli.ingestion.landingzone.ControlFileDescriptor;
import org.slc.sli.ingestion.landingzone.LandingZone;
import org.slc.sli.ingestion.landingzone.LocalFileSystemLandingZone;
import org.slc.sli.ingestion.landingzone.validation.IngestionException;
import org.slc.sli.ingestion.landingzone.validation.SubmissionLevelException;
import org.slc.sli.ingestion.model.Error;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.ResourceEntry;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;
import org.slc.sli.ingestion.tenant.TenantDA;
import org.slc.sli.ingestion.util.BatchJobUtils;
import org.slc.sli.ingestion.util.LogUtil;
import org.slc.sli.ingestion.util.spring.MessageSourceHelper;

/**
 * Transforms body from ControlFile to ControlFileDescriptor type.
 *
 * @author okrook
 *
 */
@Component
public class ControlFilePreProcessor implements Processor, MessageSourceAware {

    private static final Logger LOG = LoggerFactory.getLogger(ControlFilePreProcessor.class);

    public static final BatchJobStageType BATCH_JOB_STAGE = BatchJobStageType.CONTROL_FILE_PREPROCESSOR;

    @Autowired
    private BatchJobDAO batchJobDAO;

    @Autowired
    private TenantDA tenantDA;

    @Value("${sli.ingestion.tenant.deriveTenants}")
    private boolean deriveTenantId;

    private MessageSource messageSource;

    /**
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        processUsingNewBatchJob(exchange);
    }

    private void processUsingNewBatchJob(Exchange exchange) throws Exception {

        Stage stage = Stage.createAndStartStage(BATCH_JOB_STAGE);

        String batchJobId = exchange.getIn().getHeader("BatchJobId", String.class);
        String controlFileName = "control_file";

        FaultsReport errorReport = new FaultsReport();

        // TODO handle invalid control file (user error)
        // TODO handle IOException or other system error
        NewBatchJob newBatchJob = null;
        File fileForControlFile = null;
        try {
            fileForControlFile = exchange.getIn().getBody(File.class);
            controlFileName = fileForControlFile.getName();

            newBatchJob = getOrCreateNewBatchJob(batchJobId, fileForControlFile);

            ControlFile controlFile = parseControlFile(newBatchJob, fileForControlFile);

            ControlFileDescriptor controlFileDescriptor = createControlFileDescriptor(newBatchJob, controlFile);

            setExchangeHeaders(exchange, controlFileDescriptor, newBatchJob);

            auditSecurityEvent(controlFile);

        } catch (SubmissionLevelException exception) {
            String id = "null";
            if (newBatchJob != null) {
                id = newBatchJob.getId();
                if (newBatchJob.getResourceEntries().size() == 0) {
                    LOG.info(MessageSourceHelper.getMessage(messageSource, "CTLFILEPROC_WRNG_MSG1"));
                    errorReport.warning(MessageSourceHelper.getMessage(messageSource, "CTLFILEPROC_WRNG_MSG1"), this);
                }
            }
            handleExceptions(exchange, id, exception, controlFileName);
        } catch (Exception exception) {
            String id = "null";
            if (newBatchJob != null) {
                id = newBatchJob.getId();
            }
            handleExceptions(exchange, id, exception, controlFileName);
        } finally {
            if (newBatchJob != null) {
                BatchJobUtils.stopStageAndAddToJob(stage, newBatchJob);
                batchJobDAO.saveBatchJob(newBatchJob);
                BatchJobUtils.writeWarningssWithDAO(newBatchJob.getId(), fileForControlFile.getName(), BATCH_JOB_STAGE,
                        errorReport, batchJobDAO);
            }
        }
    }

    private NewBatchJob getOrCreateNewBatchJob(String batchJobId, File cf) {
        NewBatchJob job = null;
        if (batchJobId != null) {
            job = batchJobDAO.findBatchJobById(batchJobId);
        } else {
            job = createNewBatchJob(cf);
        }
        return job;
    }

    private ControlFile parseControlFile(NewBatchJob newBatchJob, File fileForControlFile) throws IOException,
            IngestionException {
        File lzFile = new File(newBatchJob.getTopLevelSourceId());
        LandingZone topLevelLandingZone = new LocalFileSystemLandingZone(lzFile);

        ControlFile controlFile = ControlFile.parse(fileForControlFile, topLevelLandingZone, messageSource);

        newBatchJob.setTotalFiles(controlFile.getFileEntries().size());
        createResourceEntryAndAddToJob(controlFile, newBatchJob);

        TenantContext.setTenantId(newBatchJob.getTenantId());
        // determine whether to override the tenantId property with a LZ derived value
        if (deriveTenantId) {
            // derive the tenantId property from the landing zone directory with a mongo lookup
            setTenantIdFromDb(controlFile, lzFile.getAbsolutePath());
        }
        return controlFile;
    }

    private ControlFileDescriptor createControlFileDescriptor(NewBatchJob newBatchJob, ControlFile controlFile) {
        File sourceFile = new File(newBatchJob.getSourceId());
        LandingZone resolvedLandingZone = new LocalFileSystemLandingZone(sourceFile);
        ControlFileDescriptor controlFileDescriptor = new ControlFileDescriptor(controlFile, resolvedLandingZone);
        return controlFileDescriptor;
    }

    /**
     * Handles errors associated with the control file.
     *
     * @param exchange
     *            Camel exchange.
     * @param batchJobId
     *            String representing current batch job id.
     * @param exception
     *            Exception thrown during control file parsing.
     * @param controlFileName
     */
    private void handleExceptions(Exchange exchange, String batchJobId, Exception exception, String controlFileName) {
        exchange.getIn().setHeader("BatchJobId", batchJobId);
        exchange.getIn().setHeader("ErrorMessage", exception.toString());
        exchange.getIn().setHeader("IngestionMessageType", MessageType.ERROR.name());
        LogUtil.error(LOG, "Error processing batch job " + batchJobId, exception);
        if (batchJobId != null) {
            Error error = Error.createIngestionError(batchJobId, controlFileName, BATCH_JOB_STAGE.getName(), null,
                    null, null, FaultType.TYPE_ERROR.getName(), null, exception.getMessage());
            batchJobDAO.saveError(error);

            // TODO: we should be creating WorkNote at the very first point of processing.
            // this will require some routing changes
            WorkNote workNote = WorkNote.createSimpleWorkNote(batchJobId);
            exchange.getIn().setBody(workNote, WorkNote.class);
        }
    }

    private void setExchangeHeaders(Exchange exchange, ControlFileDescriptor controlFileDescriptor, NewBatchJob newJob) {
        exchange.getIn().setHeader("BatchJobId", newJob.getId());
        exchange.getIn().setBody(controlFileDescriptor, ControlFileDescriptor.class);
        exchange.getIn().setHeader("IngestionMessageType", MessageType.BATCH_REQUEST.name());
    }

    private NewBatchJob createNewBatchJob(File controlFile) {
        NewBatchJob newJob = NewBatchJob.createJobForFile(controlFile.getName());
        newJob.setSourceId(controlFile.getParentFile().getAbsolutePath() + File.separator);
        newJob.setStatus(BatchJobStatusType.RUNNING.getName());
        LOG.info("Created job [{}]", newJob.getId());
        return newJob;
    }

    private void createResourceEntryAndAddToJob(ControlFile cf, NewBatchJob newJob) {
        ResourceEntry resourceEntry = new ResourceEntry();
        resourceEntry.setResourceId(cf.getFileName());
        resourceEntry.setExternallyUploadedResourceId(cf.getFileName());
        resourceEntry.setResourceName(newJob.getSourceId() + cf.getFileName());
        resourceEntry.setResourceFormat(FileFormat.CONTROL_FILE.getCode());
        resourceEntry.setTopLevelLandingZonePath(newJob.getTopLevelSourceId());
        newJob.getResourceEntries().add(resourceEntry);
    }

    /**
     * Derive the tenantId using a database look up based on the LZ path
     * and override the property on the ControlFile with he derived value.
     *
     * Throws an IngestionException if a tenantId could not be resolved.
     */
    private void setTenantIdFromDb(ControlFile cf, String lzPath) throws IngestionException {
        lzPath = new File(lzPath).getAbsolutePath();
        // TODO add user facing error report for no tenantId found
        String tenantId = tenantDA.getTenantId(lzPath);
        if (tenantId != null) {
            cf.getConfigProperties().put("tenantId", tenantId);
        } else {
            throw new IngestionException("Could not find tenantId for landing zone: " + lzPath);
        }
    }

    private void auditSecurityEvent(ControlFile controlFile) {
        byte[] ipAddr = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            ipAddr = addr.getAddress();

        } catch (UnknownHostException e) {
            LogUtil.error(LOG, "Error getting local host", e);
        }
        List<String> userRoles = Collections.emptyList();
        SecurityEvent event = new SecurityEvent(controlFile.getConfigProperties().getProperty("tenantId"), // Alpha
                                                                                                           // MH
                "", // user
                "", // targetEdOrg
                "processUsingNewBatchJob", // Alpha MH (actionUri)
                "Ingestion", // Alpha MH (appId)
                "", // origin
                ipAddr[0] + "." + ipAddr[1] + "." + ipAddr[2] + "." + ipAddr[3], // executedOn
                "", // Alpha MH (Credential - N/A for ingestion)
                "", // userOrigin
                new Date(), // Alpha MH (timeStamp)
                ManagementFactory.getRuntimeMXBean().getName(), // processNameOrId
                this.getClass().getName(), // className
                LogLevelType.TYPE_INFO, // Alpha MH (logLevel)
                userRoles, "Ingestion process started."); // Alpha MH (logMessage)

        audit(event);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
