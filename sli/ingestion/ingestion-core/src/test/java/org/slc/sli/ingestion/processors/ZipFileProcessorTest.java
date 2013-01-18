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

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.ingestion.FileFormat;
import org.slc.sli.ingestion.IngestionTest;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.ResourceEntry;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.queues.MessageType;

/**
 * ZipFileProcessor unit tests.
 *
 * @author ablum
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class ZipFileProcessorTest {

    @Autowired
    @InjectMocks
    private ZipFileProcessor zipProc;

    @Mock
    private BatchJobDAO mockedBatchJobDAO;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHappyZipFileProcessor() throws Exception {

        Exchange preObject = new DefaultExchange(new DefaultCamelContext());

        preObject.getIn().setBody(IngestionTest.getFile("zip/ValidZip.zip"));

        String batchJobId = NewBatchJob.createId("ValidZip.zip");
        NewBatchJob mockedJob = new NewBatchJob(batchJobId, "SLI");
        File zipFile = IngestionTest.getFile("zip/ValidZip.zip");
        createResourceEntryAndAddToJob(zipFile, mockedJob);
        mockedJob.setSourceId("zip");
        Mockito.when(mockedBatchJobDAO.findBatchJobById(Matchers.eq(batchJobId))).thenReturn(mockedJob);
        preObject.getIn().setHeader("BatchJobId", batchJobId);

        zipProc.process(preObject);

        Assert.assertNotNull(preObject.getIn().getBody());
        Assert.assertEquals("ControlFile.ctl", preObject.getIn().getHeader("ResourceId"));
    }

    @Test
    public void testNoControlFileZipFileProcessor() throws Exception {

        Exchange preObject = new DefaultExchange(new DefaultCamelContext());

        preObject.getIn().setBody(IngestionTest.getFile("zip/NoControlFile.zip"));

        String batchJobId = NewBatchJob.createId("NoControlFile.zip");
        NewBatchJob mockedJob = new NewBatchJob(batchJobId, "SLI");
        File zipFile = IngestionTest.getFile("zip/NoControlFile.zip");
        createResourceEntryAndAddToJob(zipFile, mockedJob);
        mockedJob.setSourceId("zip");
        Mockito.when(mockedBatchJobDAO.findBatchJobById(Matchers.eq(batchJobId))).thenReturn(mockedJob);
        preObject.getIn().setHeader("BatchJobId", batchJobId);

        zipProc.process(preObject);

        Assert.assertNotNull(preObject.getIn().getBody());
        Assert.assertTrue((Boolean) preObject.getIn().getHeader("hasErrors"));
        Assert.assertEquals(preObject.getIn().getHeader("IngestionMessageType") , MessageType.BATCH_REQUEST.name());
    }

    private void createResourceEntryAndAddToJob(File zipFile, NewBatchJob newJob) throws IOException {
        ResourceEntry resourceName = new ResourceEntry();
        resourceName.setResourceName(zipFile.getCanonicalPath());
        resourceName.setResourceId(zipFile.getName());
        resourceName.setExternallyUploadedResourceId(zipFile.getName());
        resourceName.setResourceFormat(FileFormat.ZIP_FILE.getCode());
        newJob.getResourceEntries().add(resourceName);
    }
}
