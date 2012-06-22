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


package org.slc.sli.api.resources.v1.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.client.constants.ResourceConstants;
import org.slc.sli.api.client.constants.ResourceNames;
import org.slc.sli.api.client.constants.v1.ParameterConstants;
import org.slc.sli.api.representation.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.util.ResourceTestUtil;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.entity.ProgramResource;
import org.slc.sli.api.resources.v1.entity.StaffResource;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.test.WebContextTestExecutionListener;

/**
 * Unit tests for the resource representing a staffProgramAssociation
 * @author jtully
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StaffProgramAssociationResourceTest {
    @Autowired
    StaffResource staffResource;
    @Autowired
    ProgramResource programResource;
    @Autowired
    StaffProgramAssociationResource staffProgramAssociationResource; //class under test

    @Autowired
    private SecurityContextInjector injector;

    private UriInfo uriInfo;
    private HttpHeaders httpHeaders;

    @Before
    public void setup() throws Exception {
        uriInfo = ResourceTestUtil.buildMockUriInfo(null);

        // inject administrator security context for unit testing
        injector.setAccessAllAdminContextWithElevatedRights();

        List<String> acceptRequestHeaders = new ArrayList<String>();
        acceptRequestHeaders.add(HypermediaType.VENDOR_SLC_JSON);

        httpHeaders = mock(HttpHeaders.class);
        when(httpHeaders.getRequestHeader("accept")).thenReturn(acceptRequestHeaders);
        when(httpHeaders.getRequestHeaders()).thenReturn(new MultivaluedMapImpl());
    }

    private Map<String, Object> createTestEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("staffId", "1001");
        entity.put("programId", "2001");
        entity.put(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID, 1234);
        return entity;
    }

    private Map<String, Object> createTestUpdateEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("staffId", "1001");
        entity.put("programId", "2003");
        entity.put(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID, 1234);
        return entity;
    }

    private Map<String, Object> createTestSecondaryEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("staffId", "1002");
        entity.put("programId", "2002");
        entity.put(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID, 5678);
        return entity;
    }

    @Test
    public void testCreate() {
        Response response = staffProgramAssociationResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());

        String id = ResourceTestUtil.parseIdFromLocation(response);
        assertNotNull("ID should not be null", id);
    }

    @Test
    public void testRead() {
        //create one entity
        Response createResponse = staffProgramAssociationResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);
        Response response = staffProgramAssociationResource.read(id, httpHeaders, uriInfo);

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity", results.size() == 1);
        } else {
            fail("Response entity not recognized: " + response);
        }
    }

    @Test
    public void testDelete() {
        //create one entity
        Response createResponse = staffProgramAssociationResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        //delete it
        Response response = staffProgramAssociationResource.delete(id, httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        try {
            @SuppressWarnings("unused")
            Response getResponse = staffProgramAssociationResource.read(id, httpHeaders, uriInfo);
            fail("should have thrown EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            return;
        } catch (Exception e) {
            fail("threw wrong exception: " + e);
        }
    }

    @Test
    public void testUpdate() {
        //create one entity
        Response createResponse = staffProgramAssociationResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        //update it
        Response response = staffProgramAssociationResource.update(id, new EntityBody(createTestUpdateEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        //try to get it
        Response getResponse = staffProgramAssociationResource.read(id, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), getResponse.getStatus());
        EntityResponse entityResponse = (EntityResponse) getResponse.getEntity();
        EntityBody body = (EntityBody) entityResponse.getEntity();
        assertNotNull("Should return an entity", body);
        assertEquals(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID + " should be 1234", body.get(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID), 1234);
        assertEquals("staffId should be Updated Reading Recovery", body.get("staffId"), "1001");
        assertEquals("programId should be Updated Reading Recovery", body.get("programId"), "2003");
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testReadAll() {
        //create two entities
        staffProgramAssociationResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        staffProgramAssociationResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        //read everything
        Response response = staffProgramAssociationResource.readAll(0, 100, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        List<EntityBody> results = (List<EntityBody>) entityResponse.getEntity();
        assertNotNull("Should return entities", results);
        assertTrue("Should have at least two entities", results.size() >= 2);
    }

    @Test
    public void testReadCommaSeparatedResources() {
        Response response = staffProgramAssociationResource.read(getIDList(ResourceNames.STAFF_PROGRAM_ASSOCIATIONS), httpHeaders, uriInfo);
        assertEquals("Status code should be 200", Status.OK.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        List<EntityBody> results = (List<EntityBody>) entityResponse.getEntity();
        assertEquals("Should get 2 entities", results.size(), 2);

        EntityBody body1 = results.get(0);
        assertNotNull("Should not be null", body1);
        assertEquals(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID + " should be 1234", body1.get(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID), 1234);
        assertNotNull("Should include links", body1.get(ResourceConstants.LINKS));

        EntityBody body2 = results.get(1);
        assertNotNull("Should not be null", body2);
        assertEquals(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID + " should be 5678", body2.get(ParameterConstants.STAFF_PROGRAM_ASSOCIATION_ID), 5678);
        assertNotNull("Should include links", body2.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetStaffProgramAssociationStaff() {
        Response createResponse = staffResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("StaffResource")), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = programResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("ProgramResource")), httpHeaders, uriInfo);
        String programId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StaffProgramAssociationResource", "StaffResource", staffId, "ProgramResource", programId);
        createResponse = staffProgramAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        Response response = staffProgramAssociationResource.getStaffProgramAssociationStaff(id, 0, 100, httpHeaders, uriInfo);
        EntityBody body = ResourceTestUtil.assertions(response);
        assertEquals("Entity type should match", "staff", body.get("entityType"));
        assertEquals("ID should match", staffId, body.get("id"));
    }

    @Test
    public void testGetStaffProgramAssociationPrograms() {
        Response createResponse = staffResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("StaffResource")), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = programResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("ProgramResource")), httpHeaders, uriInfo);
        String programId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StaffProgramAssociationResource", "StaffResource", staffId, "ProgramResource", programId);
        createResponse = staffProgramAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        Response response = staffProgramAssociationResource.getStaffProgramAssociationPrograms(id, 0, 100, httpHeaders, uriInfo);
        EntityBody body = ResourceTestUtil.assertions(response);
        assertEquals("Entity type should match", "program", body.get("entityType"));
        assertEquals("ID should match", programId, body.get("id"));
    }

    private String getIDList(String resource) {
        //create more resources
        Response createResponse1 = staffProgramAssociationResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        Response createResponse2 = staffProgramAssociationResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        return ResourceTestUtil.parseIdFromLocation(createResponse1) + "," + ResourceTestUtil.parseIdFromLocation(createResponse2);
    }
}
