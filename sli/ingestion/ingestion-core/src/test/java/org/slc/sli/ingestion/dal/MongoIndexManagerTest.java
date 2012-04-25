package org.slc.sli.ingestion.dal;

import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.dal.repository.MongoRepository;
import org.slc.sli.ingestion.NeutralRecord;

/**
 *
 * @author tke
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class MongoIndexManagerTest {

    @Autowired
    MongoIndexManager manager;
    String rootDir = "mongoIndexes";
    String batchJobId = "testBatchJob";
    MongoRepository<NeutralRecord> repository;


    @Test
    public void testCreateIndex() {
        Map<String, List<IndexDefinition>> res = manager.getCollectionIndexes();
        Assert.assertEquals(1, res.size());
        Assert.assertEquals(1, res.get("student").size());
        DBObject indexKeys = res.get("student").get(0).getIndexKeys();
        Assert.assertEquals(3, indexKeys.keySet().size());

        Assert.assertTrue(indexKeys.containsField("body.sex"));
        Assert.assertTrue(indexKeys.containsField("body.name"));
        Assert.assertTrue(indexKeys.containsField("body.birthDate"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetIndex() throws NoSuchFieldException {
        repository = Mockito.mock(MongoRepository.class);

        Mockito.when(repository.collectionExists(Mockito.anyString())).thenReturn(false);

        manager.ensureIndex(repository);
        Mockito.verify(repository, Mockito.times(1)).ensureIndex(Mockito.any(IndexDefinition.class), Mockito.anyString());
    }

}
