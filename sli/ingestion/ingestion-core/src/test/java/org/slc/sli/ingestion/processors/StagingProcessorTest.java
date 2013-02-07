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

package org.slc.sli.ingestion.processors;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.NeutralRecordWorkNote;
import org.slc.sli.ingestion.ResourceWriter;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.da.BatchJobDAO;

/**
 *
 * @author ablum
 *
 */
public class StagingProcessorTest {
    @InjectMocks
    StagingProcessor processor = new StagingProcessor();

    @Mock
    ResourceWriter<NeutralRecord> rwriter;

    @Mock
    protected BatchJobDAO batchJobDAO;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        NewBatchJob job = Mockito.mock(NewBatchJob.class);
        Mockito.when(job.getTenantId()).thenReturn("tenantId");
        Mockito.when(batchJobDAO.findBatchJobById(Mockito.anyString())).thenReturn(job);
    }

    @Test
    public void testProcess() throws Exception {
        NeutralRecord record1 = new NeutralRecord();
        record1.setRecordType("school");
        NeutralRecord record2 = new NeutralRecord();
        record2.setRecordType("student");
        List<NeutralRecord> records = new ArrayList<NeutralRecord>();
        records.add(record1);
        records.add(record2);

        NeutralRecordWorkNote workNote = new NeutralRecordWorkNote(records, "batchJobId", "tenantId", false);

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setBody(workNote);

        processor.process(exchange);

        Assert.assertEquals(false, exchange.getIn().getHeader("hasErrors"));

        List<NeutralRecord> verifyStudent = new ArrayList<NeutralRecord>();
        verifyStudent.add(record2);
        List<NeutralRecord> verifySchool = new ArrayList<NeutralRecord>();
        verifySchool.add(record1);

        Mockito.verify(rwriter, Mockito.times(1)).insertResources(verifyStudent, "student");
        Mockito.verify(rwriter, Mockito.times(1)).insertResources(verifySchool, "school");

    }
}
