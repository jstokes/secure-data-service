/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

package org.slc.sli.ingestion.landingzone.validation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.slc.sli.ingestion.reporting.AbstractMessageReport;
import org.slc.sli.ingestion.reporting.ReportStats;
import org.slc.sli.ingestion.reporting.Source;
import org.slc.sli.ingestion.reporting.impl.BaseMessageCode;
import org.slc.sli.ingestion.reporting.impl.ZipFileSource;
import org.slc.sli.ingestion.validation.Validator;

/**
 * Zip file validator.
 *
 * @author okrook
 *
 */
public class ZipFileValidator implements Validator<File> {

    private static final Logger LOG = LoggerFactory.getLogger(ZipFileValidator.class);
    private static final String STAGE_NAME = "Zip File Validation";

    // how long to wait for the file to become valid before giving up
    private Long zipfileTimeout;

    // how often to check if the file is has become valid
    private Long zipfilePollInterval;

    @Override
    public boolean isValid(File zipFile, AbstractMessageReport report, ReportStats reportStats, Source source) {
        FileInputStream fis = null;
        ZipArchiveInputStream zis = null;

        boolean isValid = false;

        boolean done = false;
        long clockTimeout = System.currentTimeMillis() + zipfileTimeout;

        // we know more of our source
        LOG.info("Validating " + zipFile.getAbsolutePath());

        while (!done) {

            try {
                fis = new FileInputStream(zipFile);
                zis = new ZipArchiveInputStream(new BufferedInputStream(fis));

                ArchiveEntry ze;

                while ((ze = zis.getNextEntry()) != null) {

                    if (isDirectory(ze)) {
                        report.error(reportStats, new ZipFileSource(zipFile), BaseMessageCode.BASE_0010, zipFile.getName());
                        return false;
                    }

                    if (ze.getName().endsWith(".ctl")) {
                        isValid = true;
                    }
                }

                // no manifest (.ctl file) found in the zip file
                if (!isValid) {
                    report.error(reportStats, new ZipFileSource(zipFile), BaseMessageCode.BASE_0009, zipFile.getName());
                }

                done = true;

            } catch (UnsupportedZipFeatureException ex) {
                // Unsupported compression method
                report.error(reportStats, new ZipFileSource(zipFile), BaseMessageCode.BASE_0022, zipFile.getName());
                done = true;
                return false;

            } catch (FileNotFoundException ex) {
                // DE1618 Gluster may have lost track of the file, or it has been deleted from under
                // us
                String message = zipFile.getAbsolutePath()
                        + " cannot be found. If the file is not processed, please resubmit.";
                LOG.error(message, ex);
                report.error(reportStats, new ZipFileSource(zipFile), BaseMessageCode.BASE_0020, zipFile.getName());
                done = true;
                return false;

            } catch (IOException ex) {
                LOG.warn("Caught IO exception processing " + zipFile.getAbsolutePath());
                if (System.currentTimeMillis() >= clockTimeout) {
                    // error reading zip file
                    report.error(reportStats, new ZipFileSource(zipFile), BaseMessageCode.BASE_0021, zipFile.getName());
                    LOG.error("Unable to validate " + zipFile.getAbsolutePath(), ex);
                    done = true;
                    return false;
                } else {
                    try {
                        LOG.info("Waiting for " + zipFile.getAbsolutePath() + "to move.");
                        Thread.sleep(zipfilePollInterval);
                    } catch (InterruptedException e) {
                        // Restore the interrupted status
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                IOUtils.closeQuietly(zis);
                IOUtils.closeQuietly(fis);
            }
        }

        return isValid;
    }

    public Long getZipfileTimeout() {
        return zipfileTimeout;
    }

    public void setZipfileTimeout(Long zipfileTimeout) {
        this.zipfileTimeout = zipfileTimeout;
    }

    public Long getZipfilePollInterval() {
        return zipfilePollInterval;
    }

    public void setZipfilePollInterval(Long zipfilePollInterval) {
        this.zipfilePollInterval = zipfilePollInterval;
    }

    private static boolean isDirectory(ArchiveEntry zipEntry) {

        if (zipEntry.isDirectory()) {
            return true;
        }

        // UN: This check is to ensure that any zipping utility which does not pack a directory
        // entry
        // is verified by checking for a filename with '/'. Example: Windows Zipping Tool.
        if (zipEntry.getName().contains("/")) {
            return true;
        }

        return false;
    }

    @Override
    public String getStageName() {
        return STAGE_NAME;
    }
}
