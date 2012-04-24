package org.slc.sli.ingestion.processors;

import java.util.HashSet;
import java.util.Set;

import junitx.util.PrivateAccessor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.ingestion.model.NewBatchJob;
import org.slc.sli.ingestion.model.da.BatchJobDAO;

/**
 *
 * @author npandey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class PurgeProcessorTest {

    @InjectMocks
    @Autowired
    private PurgeProcessor purgeProcessor;

    @Mock
    private BatchJobDAO mockBatchJobDAO;

    @Mock
    private MongoTemplate mongoTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNoTenantId() throws Exception {

        String batchJobId = "123";

        Exchange ex = Mockito.mock(Exchange.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(ex.getIn()).thenReturn(message);
        Mockito.when(message.getHeader("BatchJobId", String.class)).thenReturn(batchJobId);

        NewBatchJob job = new NewBatchJob();
        Mockito.when(mockBatchJobDAO.findBatchJobById(batchJobId)).thenReturn(job);

        Logger log = Mockito.mock(org.slf4j.Logger.class);
        PrivateAccessor.setField(purgeProcessor, "logger", log);

        purgeProcessor.process(ex);
        Mockito.verify(log, Mockito.atLeastOnce()).error("TenantId missing. No purge operation performed.");
    }

    @Test
    public void testPurging() throws Exception {
        String batchJobId = "123";

        Exchange ex = Mockito.mock(Exchange.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(ex.getIn()).thenReturn(message);
        Mockito.when(message.getHeader("BatchJobId", String.class)).thenReturn(batchJobId);

        NewBatchJob job = new NewBatchJob();
        job.setProperty("tenantId", "SLI");
        Mockito.when(mockBatchJobDAO.findBatchJobById(batchJobId)).thenReturn(job);

        Set<String> collectionNames = new HashSet<String>();
        collectionNames.add("student");
        collectionNames.add("teacher");

        Mockito.when(mongoTemplate.getCollectionNames()).thenReturn(collectionNames);

        purgeProcessor.process(ex);

        Mockito.verify(mongoTemplate, Mockito.atLeastOnce()).remove(Mockito.any(Query.class), Mockito.eq("student"));
    }

    @Test
    public void testPurgingSystemCollections() throws Exception {
        String batchJobId = "123";

        Exchange ex = Mockito.mock(Exchange.class);
        Message message = Mockito.mock(Message.class);
        Mockito.when(ex.getIn()).thenReturn(message);
        Mockito.when(message.getHeader("BatchJobId", String.class)).thenReturn(batchJobId);

        NewBatchJob job = new NewBatchJob();
        job.setProperty("tenantId", "SLI");
        Mockito.when(mockBatchJobDAO.findBatchJobById(batchJobId)).thenReturn(job);

        Set<String> collectionNames = new HashSet<String>();
        collectionNames.add("system.js");
        collectionNames.add("system.indexes");

        Mockito.when(mongoTemplate.getCollectionNames()).thenReturn(collectionNames);

        purgeProcessor.process(ex);

        Mockito.verify(mongoTemplate, Mockito.never()).remove(Mockito.any(Query.class), Mockito.eq("system.js"));
    }

}
