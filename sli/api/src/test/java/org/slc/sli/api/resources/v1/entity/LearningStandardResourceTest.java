package org.slc.sli.api.resources.v1.entity;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.slc.sli.api.config.BasicDefinitionStore;

/**
 * Unit tests for LearningStandardResource
 * 
 * @author Ryan Farris <rfarris@wgen.net>
 * 
 */
@RunWith(JUnit4.class)
public class LearningStandardResourceTest {
    LearningStandardResource learningStandardResource = new LearningStandardResource(new BasicDefinitionStore());
    
    @Test
    public void testReadAll() {
        Response res = learningStandardResource.readAll(0, 0, null, null);
        assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void testRead() {
        Response res = learningStandardResource.read(null, null, null);
        assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void testCreate() {
        Response res = learningStandardResource.create(null, null, null);
        assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    }
    
    @Test
    public void testDelete() {
        Response res = learningStandardResource.delete(null, null, null);
        assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    }

    @Test
    public void testUpdate() {
        Response res = learningStandardResource.update(null, null, null, null);
        assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    }
}
