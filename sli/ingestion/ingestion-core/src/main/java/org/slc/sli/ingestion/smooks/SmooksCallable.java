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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.milyn.SmooksException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import org.slc.sli.common.util.tenantdb.TenantContext;
import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.FileProcessStatus;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.model.Metrics;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.Stage;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.reporting.CoreMessageCode;
import org.slc.sli.ingestion.util.LogUtil;

/**
 * The Smooks of the future..
 *
 * @author dduran
 *
 */
public class SmooksCallable implements Callable<Boolean> {

    private static final Logger LOG = LoggerFactory.getLogger(SmooksCallable.class);

    private SliSmooksFactory sliSmooksFactory;

    private final NewBatchJob newBatchJob;
    private final IngestionFileEntry fe;
    private final Stage stage;

    public SmooksCallable(NewBatchJob newBatchJob, IngestionFileEntry fe, Stage stage, BatchJobDAO batchJobDAO,
            SliSmooksFactory sliSmooksFactory) {
        this.newBatchJob = newBatchJob;
        this.fe = fe;
        this.stage = stage;
        this.sliSmooksFactory = sliSmooksFactory;
    }

    @Override
    public Boolean call() throws Exception {
        return runSmooksFuture();
    }

    public boolean runSmooksFuture() {
        TenantContext.setJobId(newBatchJob.getId());
        TenantContext.setTenantId(newBatchJob.getTenantId());
        TenantContext.setBatchProperties(newBatchJob.getBatchProperties());

        LOG.info("Starting SmooksCallable for: " + fe.getFileName());
        Metrics metrics = Metrics.newInstance(fe.getFileName());
        stage.addMetrics(metrics);

        FileProcessStatus fileProcessStatus = new FileProcessStatus();

        // actually do the processing
        processFileEntry(fileProcessStatus);

        processMetrics(metrics, fileProcessStatus);

        LOG.info("Finished SmooksCallable for: " + fe.getFileName());

        TenantContext.setJobId(null);
        TenantContext.setTenantId(null);
        TenantContext.setBatchProperties(null);
        return (fe.getReportStats().hasErrors());
    }

    public void processFileEntry(FileProcessStatus fileProcessStatus) {

        if (fe.getFileType() != null) {
            FileFormat fileFormat = fe.getFileType().getFileFormat();
            if (fileFormat == FileFormat.EDFI_XML) {

                doHandling(fileProcessStatus);

            } else {
                throw new IllegalArgumentException("Unsupported file format: " + fe.getFileType().getFileFormat());
            }
        } else {
            throw new IllegalArgumentException("FileType was not provided.");
        }
    }

    private void doHandling(FileProcessStatus fileProcessStatus) {
        try {

            generateNeutralRecord(fileProcessStatus);

        } catch (IOException e) {
            LogUtil.error(LOG,
                    "Error generating neutral record: Could not instantiate smooks, unable to read configuration file",
                    e);
            fe.getMessageReport().error(fe.getReportStats(), CoreMessageCode.CORE_0016);
        } catch (SAXException e) {
            LogUtil.error(LOG, "Could not instantiate smooks, problem parsing configuration file", e);
            fe.getMessageReport().error(fe.getReportStats(), CoreMessageCode.CORE_0017);
        }
    }

    void generateNeutralRecord(FileProcessStatus fileProcessStatus) throws IOException, SAXException {

        // create instance of Smooks (with visitors already added)
        SliSmooks smooks = sliSmooksFactory.createInstance(fe, fe.getMessageReport(), fe.getReportStats());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(fe.getFile()));
        try {
            // filter fileEntry inputStream, converting into NeutralRecord entries as we go
            smooks.filterSource(new StreamSource(inputStream));

            populateRecordCountsFromSmooks(smooks, fileProcessStatus, fe);

        } catch (SmooksException se) {
            LogUtil.error(LOG, "smooks exception - encountered problem with " + fe.getFile().getName(), se);
            fe.getMessageReport().error(fe.getReportStats(), CoreMessageCode.CORE_0018);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void populateRecordCountsFromSmooks(SliSmooks smooks, FileProcessStatus fileProcessStatus,
            IngestionFileEntry ingestionFileEntry) {

        SmooksEdFiVisitor edFiVisitor = smooks.getSmooksEdFiVisitor();

        int recordsPersisted = edFiVisitor.getRecordsPerisisted();
        Map<String, Long> duplicateCounts = edFiVisitor.getDuplicateCounts();

        fileProcessStatus.setTotalRecordCount(recordsPersisted);
        fileProcessStatus.setDuplicateCounts(duplicateCounts);

        LOG.debug("Parsed and persisted {} records to staging db from file: {}.", recordsPersisted,
                ingestionFileEntry.getFileName());
    }

    private void processMetrics(Metrics metrics, FileProcessStatus fileProcessStatus) {
        metrics.setDuplicateCounts(fileProcessStatus.getDuplicateCounts());
        metrics.setRecordCount(fileProcessStatus.getTotalRecordCount());
        metrics.setErrorCount(fe.getReportStats().getErrorCount());
    }
}
