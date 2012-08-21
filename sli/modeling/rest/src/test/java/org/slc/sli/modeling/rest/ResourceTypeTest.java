package org.slc.sli.modeling.rest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for ResourceType class.
 *
 * @author wscott
 *
 */
public class ResourceTypeTest {

    private ResourceType resourceType; // class under test

    private static final String ID = "id";
    private static final String VERB = Method.NAME_HTTP_GET;
    private static final ArrayList<Response> RESPONSES = new ArrayList<Response>(0);
    private static final ArrayList<Representation> REPRESENTATIONS = new ArrayList<Representation>(0);
    private static final ArrayList<Documentation> DOC = new ArrayList<Documentation>(0);
    private static final Request REQUEST = new Request(DOC, new ArrayList<Param>(0), REPRESENTATIONS);
    private static final Method METHOD = new Method(ID, VERB, DOC, REQUEST, RESPONSES);
    private static final ArrayList<String> TYPE = new ArrayList<String>(0);
    private static final String QUERY_TYPE = "queryType";
    private static final String PATH = "path";
    private static final ArrayList<Param> PARAMS = new ArrayList<Param>(0);
    private static final ArrayList<Method> METHODS = new ArrayList<Method>(0);
    private static final ArrayList<Resource> RESOURCES = new ArrayList<Resource>(0);
    private static final Resource RESOURCE = new Resource(ID, TYPE, QUERY_TYPE, PATH, DOC, PARAMS, METHODS, RESOURCES);

    @Before
    public void setup() throws Exception {
        resourceType = new ResourceType(ID, DOC, PARAMS, METHOD, RESOURCE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullId() {
        new ResourceType(null, DOC, PARAMS, METHOD, RESOURCE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullMethod() {
        new ResourceType(ID, DOC, PARAMS, null, RESOURCE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullParams() {
        new ResourceType(ID, DOC, null, METHOD, RESOURCE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullResource() {
        new ResourceType(ID, DOC, PARAMS, METHOD, null);
    }

    @Test
    public void testGetId() {
        assertEquals(ID, resourceType.getId());
    }

    @Test
    public void testGetMethod() {
        assertEquals(METHOD, resourceType.getMethod());
    }

    @Test
    public void testGetParams() {
        assertEquals(PARAMS, resourceType.getParams());
    }

    @Test
    public void testGetResource() {
        assertEquals(RESOURCE, resourceType.getResource());
    }

}
