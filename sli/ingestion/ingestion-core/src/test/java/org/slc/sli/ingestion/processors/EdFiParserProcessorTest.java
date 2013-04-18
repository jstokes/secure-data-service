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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.Location;

import junit.framework.Assert;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.ingestion.ActionVerb;
import org.slc.sli.ingestion.FileEntryWorkNote;
import org.slc.sli.ingestion.FileType;
import org.slc.sli.ingestion.NeutralRecord;
import org.slc.sli.ingestion.ReferenceHelper;
import org.slc.sli.ingestion.WorkNote;
import org.slc.sli.ingestion.landingzone.IngestionFileEntry;
import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.da.BatchJobDAO;
import org.slc.sli.ingestion.parser.impl.RecordMetaImpl;
import org.slc.sli.ingestion.processors.EdFiParserProcessor.ParserState;
import org.slc.sli.ingestion.reporting.impl.JobSource;
import org.slc.sli.ingestion.reporting.impl.SimpleReportStats;
import org.slc.sli.ingestion.util.XsdSelector;



/**
 * EdFiParserProcessor unit tests.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/processor-test.xml" })
public class EdFiParserProcessorTest extends CamelTestSupport {


    @InjectMocks
    @Spy
    DummyEdFiParserProcessor processor = new DummyEdFiParserProcessor();

    private XsdSelector xsdSelector = new XsdSelector();

    @Autowired
    private  ReferenceHelper helper ;

    @Mock
    private ProducerTemplate producer;

    @Mock
    protected BatchJobDAO batchJobDAO;

    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        NewBatchJob job = Mockito.mock(NewBatchJob.class);
        Mockito.when(job.getTenantId()).thenReturn("tenantId");
        Mockito.when(batchJobDAO.findBatchJobById(Mockito.anyString())).thenReturn(job);

        processor.setXsdSelector(xsdSelector);
        processor.setProducer(producer);
        processor.setBatchJobDAO(batchJobDAO);
        processor.setBatchSize(2);
        Mockito.doNothing()
                .when(processor)
                .parse(Mockito.any(InputStream.class), Mockito.any(Resource.class),
                        Mockito.any(SimpleReportStats.class), Mockito.any(JobSource.class));
    }

    @Test
    public void testProcess() throws Exception {
        init();

        Exchange exchange = createFileEntryExchange();

        Resource xsd = Mockito.mock(Resource.class);
        Map<String, Resource> xsdList = new HashMap<String, Resource>();
        FileEntryWorkNote workNote = (FileEntryWorkNote) exchange.getIn().getMandatoryBody(WorkNote.class);
        xsdList.put(workNote.getFileEntry().getFileType().getName(), xsd);
        xsdSelector.setXsdList(xsdList);

        processor.process(exchange);

        Mockito.verify(processor, Mockito.times(1)).parse(Mockito.any(InputStream.class), Mockito.any(Resource.class),
                Mockito.any(SimpleReportStats.class), Mockito.any(JobSource.class));
    }

    @Test
    public void testVisitAndSend() throws Exception {
        init();
        Exchange exchange = createFileEntryExchange();

        processor.setUpState(exchange, exchange.getIn().getBody(FileEntryWorkNote.class));

        RecordMetaImpl meta = new RecordMetaImpl("student", "student");
        Location loc = Mockito.mock(Location.class);
        Mockito.when(loc.getLineNumber()).thenReturn(1);
        Mockito.when(loc.getColumnNumber()).thenReturn(1);
        meta.setSourceStartLocation(loc);
        meta.setSourceEndLocation(loc);

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("studentUniqueStateId", "studentId");

        processor.visit(meta, body);

        ParserState state = processor.getState();
        List<NeutralRecord> records = state.getDataBatch();

        Assert.assertNotNull(records);
        Assert.assertEquals(1, records.size());
        Assert.assertEquals("studentId", records.get(0).getAttributes().get("studentUniqueStateId"));
        Assert.assertEquals("student", records.get(0).getRecordType());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReferenceMap() throws Exception {
        init();

       //helper.setMapFile( "deleteHelper/referenceExceptionMap.json" );

        ActionVerb action = ActionVerb.DELETE;
        RecordMetaImpl meta = new RecordMetaImpl("GradeReference", "GradeReference", false, action);
        Location loc = Mockito.mock(Location.class);
        Mockito.when(loc.getLineNumber()).thenReturn(1);
        Mockito.when(loc.getColumnNumber()).thenReturn(1);
        meta.setSourceStartLocation(loc);
        meta.setSourceEndLocation(loc);

        /*
         * building GradeReference
         */
        Map<String, Object> att = new HashMap<String, Object>();
        att.put("StudentReference",new HashMap<String, Object>());
        (( Map<String,Object>)att.get("StudentReference")).put( "StudentIdenity", new HashMap<String, Object>());
        (( Map<String,Object>)(( Map<String,Object>)att.get("StudentReference")).get("StudentIdenity")).
                put("StudentUniqueStateId", "G-800000025");

        att.put("SectionReference",new HashMap<String, Object>());
        (( Map<String,Object>)att.get("SectionReference")).put( "EducationalOrgIdentity", new HashMap<String, Object>());
        (( Map<String,Object>)(( Map<String,Object>)att.get("SectionReference")).get("EducationalOrgIdentity")).
                put("StateOrganizationId", "Daybreak Central High");


        att.put("SchoolYear", "2011-2012");
        Assert.assertFalse(att.containsKey("StudentSectionAssociationReference"));

        helper.mapAttributes(att, "GradeReference");
        Assert.assertTrue(att.containsKey("StudentSectionAssociationReference"));
        Assert.assertTrue(att.get("StudentSectionAssociationReference") instanceof Map );
        Assert.assertTrue( ((Map<String,Object>)att.get("StudentSectionAssociationReference")).
                containsKey("StudentSectionAssociationIdentity"));

    }

    private Exchange createFileEntryExchange() throws IOException {
        IngestionFileEntry ife = Mockito.mock(IngestionFileEntry.class);
        InputStream is = Mockito.mock(InputStream.class);
        Mockito.when(ife.getFileStream()).thenReturn(is);
        FileType type = FileType.XML_STUDENT_PARENT_ASSOCIATION;
        Mockito.when(ife.getFileType()).thenReturn(type);
        FileEntryWorkNote workNote = new FileEntryWorkNote("batchJobId", ife, false);

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setBody(workNote);

        return exchange;
    }

    private class DummyEdFiParserProcessor extends EdFiParserProcessor {
        public void setUpState(Exchange exchange, FileEntryWorkNote workNote) {
            super.prepareState(exchange, workNote);
        }

        public ParserState getState() {
            return state.get();
        }
    }

}
