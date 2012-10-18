package org.slc.sli.search.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slc.sli.search.config.IndexConfigStore;
import org.slc.sli.search.entity.IndexEntity;
import org.slc.sli.search.process.impl.IncrementalListenerImpl;
import org.slc.sli.search.transform.IndexEntityConverter;

/**
 * Test class for the Sarje incremental update listener
 * 
 * @author dwu
 *
 */
public class IncrementalListenerTest {
    
    private String opLogInsert;
    private String opLogUpdate;
    private String opLogDelete;
    
    private final IncrementalListenerImpl listener = new IncrementalListenerImpl();
    private final IndexEntityConverter indexEntityConverter = new IndexEntityConverter();;
    
    @Before
    public void init() throws Exception {
        
        indexEntityConverter.setDecrypt(false);
        indexEntityConverter.setIndexConfigStore(new IndexConfigStore("index-config-test.json"));
        listener.setIndexEntityConverter(indexEntityConverter);
        
        // read in test oplog messages
        File inFile = new File(getClass().getClassLoader().getResource("studentOpLog.json").getFile());
        BufferedReader br = new BufferedReader(new FileReader(inFile));
        opLogInsert = br.readLine();
        //System.out.println(opLogInsert);
        opLogUpdate = br.readLine();
        //System.out.println(opLogUpdate1);
        opLogDelete = br.readLine();
    }
    
    
    /**
     * Test oplog insert -> index entity conversion
     */
    @Test
    public void testInsert() throws Exception {
        
        // convert to index entity
        IndexEntity entity = listener.convertToEntity(opLogInsert);
        
        // check result
        Assert.assertEquals("index", entity.getActionValue());
        Assert.assertEquals("4ef33d4356e3e757e5c3662e6a79ddbfd8b31866_id", entity.getId());
        Assert.assertEquals("student", entity.getType());
        Assert.assertEquals("midgar", entity.getIndex());
        Map<String, Object> name = (Map<String, Object>) entity.getBody().get("name"); 
        Assert.assertEquals(name.get("firstName"), "ESTRING:oF9iD6JYVIXWiLxhlEY5Rw==");
        Assert.assertEquals(name.get("lastSurname"), "ESTRING:B8eYiF6KTM4Fab9/A1lHsQ==");
    }
    
    
    /**
     * Test oplog update -> index entity conversion
     * Updates the entire body and metadata
     * 
     * @throws Exception
     */
    @Test
    public void testUpdate() throws Exception {
        
        // convert to index entity
        IndexEntity entity = listener.convertToEntity(opLogUpdate);
        
        // check result
        Assert.assertEquals(entity.getActionValue(), "index");
        Assert.assertEquals(entity.getId(), "067198fd6da91e1aa8d67e28e850f224d6851713_id");
        Assert.assertEquals(entity.getType(), "student");
        Assert.assertEquals(entity.getIndex(), "midgar");
        Map<String, Object> name = (Map<String, Object>) entity.getBody().get("name"); 
        // updated name
        Assert.assertEquals(name.get("lastSurname"), "ESTRING:eQhKVMY2pD1swnuIyLvSxA==");
        Assert.assertEquals(name.get("firstName"), "ESTRING:xctp43ByzulEIH6YylKuGQ==");
    }
    
    /**
     * Test oplog delete -> index entity conversion
     */
    @Ignore // TODO re-enable when delete functionality is in place
    @Test
    public void testDelete() throws Exception {
    
        // convert to index entity
        IndexEntity entity = listener.convertToEntity(opLogDelete);
        
        // check result
        Assert.assertEquals(entity.getActionValue(), "delete");
        Assert.assertEquals(entity.getId(), "4ef33d4356e3e757e5c3662e6a79ddbfd8b31866_id");
        Assert.assertEquals(entity.getType(), "student");
        Assert.assertEquals(entity.getIndex(), "midgar");
        
    }
    
    /**
     * Test filtering of oplog info
     */
    public void testFilter() {
        
    }
    
}