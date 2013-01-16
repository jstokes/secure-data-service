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

package org.slc.sli.ingestion.tool;

import java.io.File;
import java.io.IOException;

import org.slc.sli.ingestion.Resource;
import org.slc.sli.ingestion.handler.Handler;
import org.slc.sli.ingestion.landingzone.ControlFile;
import org.slc.sli.ingestion.landingzone.ControlFileDescriptor;
import org.slc.sli.ingestion.landingzone.FileResource;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.landingzone.validation.SubmissionLevelException;
import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.ReportStats;
import org.slc.sli.ingestion.reporting.Source;
import org.slc.sli.ingestion.reporting.impl.JobSource;
import org.slc.sli.ingestion.reporting.impl.SimpleReportStats;
import org.slc.sli.ingestion.validation.ComplexValidator;
import org.slc.sli.ingestion.validation.Validator;

/**
 * Validation Controller reads zip file or ctl file in a given directory and applies set of
 * pre-defined validators.
 *
 * @author mpatel
 */
public class ValidationController {

    private Handler<Resource, String> zipFileHandler;

    private Validator<IngestionFileEntry> complexValidator;

    private Validator<ControlFileDescriptor> controlFilevalidator;

    private AbstractMessageReport messageReport;

    /*
     * retrieve zip file or control file from the input directory and invoke
     * relevant validator
     */
    public void doValidation(File path) {
        ReportStats reportStats = new SimpleReportStats();
        Source source = new JobSource(path.getName(), null);

        if (path.isFile()) {
            if (path.getName().endsWith(".ctl")) {
                processControlFile(path.getParentFile().getAbsoluteFile(), path.getName(), reportStats, source);
            } else if (path.getName().endsWith(".zip")) {
                processZip(path, reportStats, source);
            } else {
                messageReport.error(reportStats, source, ValidationMessageCode.VALIDATION_0001);
            }
        } else {
            messageReport.error(reportStats, source, ValidationMessageCode.VALIDATION_0015);
        }
    }

    public void processValidators(ControlFile cfile, ReportStats reportStats, Source source) {
        boolean isValid = false;
        for (IngestionFileEntry ife : cfile.getFileEntries()) {
            if (ife.isValid()) {
                messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0002, ife.getFileName());
                isValid = complexValidator.isValid(ife, messageReport, reportStats, source);
                if (!isValid) {
                    messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0003, ife.getFileName());
                    continue;
                }
                messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0004, ife.getFileName());
            }
        }
    }

    public void processZip(File zipFile, ReportStats reportStats, Source source) {

        messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0005, zipFile.getAbsolutePath());

        FileResource zipFileResource = new FileResource(zipFile.getAbsolutePath());
        String ctlFile = zipFileHandler.handle(zipFileResource, messageReport, reportStats);

        if (!reportStats.hasErrors()) {
            processControlFile(zipFile, ctlFile, reportStats, source);
        }

        messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0006, zipFile.getAbsolutePath());
    }

    public void processControlFile(File parentDirectoryOrZipFile, String ctlFile, ReportStats reportStats, Source source) {
        messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0007, ctlFile);

        try {
            ControlFile cfile = ControlFile.parse(parentDirectoryOrZipFile.getAbsolutePath(), ctlFile, messageReport);

            ControlFileDescriptor cfd = new ControlFileDescriptor(cfile, parentDirectoryOrZipFile.getAbsolutePath());

            controlFilevalidator.isValid(cfd, messageReport, reportStats, source);

            processValidators(cfile, reportStats, source);
        } catch (IOException e) {
            messageReport.error(reportStats, source, ValidationMessageCode.VALIDATION_0008);
        } catch (SubmissionLevelException exception) {
            messageReport.error(reportStats, source, ValidationMessageCode.VALIDATION_0010, exception.getMessage());
        } finally {
            messageReport.info(reportStats, source, ValidationMessageCode.VALIDATION_0009, ctlFile);
        }
    }

    public Handler<Resource, String> getZipFileHandler() {
        return zipFileHandler;
    }

    public void setZipFileHandler(Handler<Resource, String> zipFileHandler) {
        this.zipFileHandler = zipFileHandler;
    }

    public ComplexValidator<IngestionFileEntry> getComplexValidator() {
        return (ComplexValidator<IngestionFileEntry>) complexValidator;
    }

    public void setComplexValidator(ComplexValidator<IngestionFileEntry> complexValidator) {
        this.complexValidator = complexValidator;
    }

    public Validator<ControlFileDescriptor> getControlFilevalidator() {
        return controlFilevalidator;
    }

    public void setControlFilevalidator(Validator<ControlFileDescriptor> controlFilevalidator) {
        this.controlFilevalidator = controlFilevalidator;
    }

    public AbstractMessageReport getMessageReport() {
        return messageReport;
    }

    public void setMessageReport(AbstractMessageReport messageReport) {
        this.messageReport = messageReport;
    }

}
