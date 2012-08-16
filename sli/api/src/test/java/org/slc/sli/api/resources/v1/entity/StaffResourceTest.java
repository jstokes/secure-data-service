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


package org.slc.sli.api.resources.v1.entity;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.constants.ResourceConstants;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.representation.EntityResponse;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.util.ResourceTestUtil;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.association.StaffCohortAssociationResource;
import org.slc.sli.api.resources.v1.association.StaffEducationOrganizationAssociationResource;
import org.slc.sli.api.resources.v1.association.StaffProgramAssociationResource;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the resource representing a Staff
 * @author srichards
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StaffResourceTest {

    @Autowired
    StaffResource staffResource; //class under test

    @Autowired
    EducationOrganizationResource edOrgResource;

    @Autowired
    StaffEducationOrganizationAssociationResource staffEdOrgAssn;

    @Autowired
    CohortResource cohortResource;

    @Autowired
    StaffCohortAssociationResource staffCohortAssn;

    @Autowired
    ProgramResource programResource;

    @Autowired
    StaffProgramAssociationResource staffProgramAssociationResource;

    @Autowired
    EducationOrganizationResource educationOrganizationResource;

    @Autowired
    private SecurityContextInjector injector;

    private UriInfo uriInfo;
    private HttpHeaders httpHeaders;

    private final String firstStaffId = "1234";
    //private final String secondStaffId = "5678";
    private final String edOrgId = "2345";
    private final String edOrgAssociationId = "3456";
    private final String cohortId = "4567";
    private final String cohortAssociationId = "5678";
    private final String cohortAssnBeginDate = "2012-02-02";
    //private final String secondName = "Dua";
    private final String uniqueStateId = "9876";
    private final String staffClassification = "orange";
    private final String cohortType = "Unua Type";

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
        entity.put(ParameterConstants.STAFF_ID, firstStaffId);
        entity.put(StaffResource.UNIQUE_STATE_ID, uniqueStateId);
        entity.put(StaffResource.NAME, "Unua");
        entity.put(StaffResource.SEX, "Female");
        entity.put(StaffResource.HISPANIC_LATINO_ETHNICITY, "true");
        entity.put(StaffResource.EDUCATION_LEVEL, "PhD");
        return entity;
    }

    private Map<String, Object> createTestEdOrgEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.EDUCATION_ORGANIZATION_ID, edOrgId);
        entity.put("organizationCategories", "State Education Agency");
        entity.put("nameOfInstitution", "Primero Test Institution");
       return entity;
    }

    private Map<String, Object> createTestEdOrgAssociationEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STAFF_EDUCATION_ORGANIZATION_ID, edOrgAssociationId);
        entity.put(StaffEducationOrganizationAssociationResource.STAFF_CLASSIFICATION, staffClassification);
        entity.put(StaffEducationOrganizationAssociationResource.BEGIN_DATE, "2012-01-01");
        return entity;
    }

    private Map<String, Object> createTestCohortEntity(String edOrgId) {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(CohortResource.COHORT_IDENTIFIER, cohortId);
        entity.put(CohortResource.COHORT_TYPE, cohortType);
        entity.put(CohortResource.EDUCATION_ORGANIZATION_ID, edOrgId);
        return entity;
    }

    private Map<String, Object> createTestCohortAssociationEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STAFF_COHORT_ASSOCIATION_ID, cohortAssociationId);
        entity.put(StaffCohortAssociationResource.BEGIN_DATE, cohortAssnBeginDate);
        return entity;
    }


    @Test
    public void testGetEdOrgAssociations() {
        //create one entity
        Response createResponse = staffResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);

        createResponse = edOrgResource.create(new EntityBody(createTestEdOrgEntity()), httpHeaders, uriInfo);
        String targetId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestEdOrgAssociationEntity();
        map.put(StaffEducationOrganizationAssociationResource.EDUCATION_ORGANIZATION_REFERENCE, targetId);
        map.put(StaffEducationOrganizationAssociationResource.STAFF_REFERENCE, staffId);

        createResponse = staffEdOrgAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        //String associationId = parseIdFromLocation(createResponse);

        Response response = staffResource.getStaffEducationOrganizationAssociations(staffId, httpHeaders, uriInfo);

        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        EntityBody body = null;
        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
            body = (EntityBody) responseEntityObj;
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity; actually have " + results.size(), results.size() == 1);
            body = results.get(0);
        } else {
            fail("Response entity not recognized: " + response);
            return;
        }

        assertNotNull("Should return an entity", body);
        assertEquals(StaffEducationOrganizationAssociationResource.STAFF_CLASSIFICATION + " should be " + staffClassification, staffClassification, body.get(StaffEducationOrganizationAssociationResource.STAFF_CLASSIFICATION));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetAssociatedEdOrgs() {
        //create one entity
        Response createResponse = staffResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);

        createResponse = edOrgResource.create(new EntityBody(createTestEdOrgEntity()), httpHeaders, uriInfo);
        String targetId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestEdOrgAssociationEntity();
        map.put(StaffEducationOrganizationAssociationResource.EDUCATION_ORGANIZATION_REFERENCE, targetId);
        map.put(StaffEducationOrganizationAssociationResource.STAFF_REFERENCE, staffId);

        createResponse = staffEdOrgAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        //String associationId = parseIdFromLocation(createResponse);

        Response response = staffResource.getStaffEducationOrganizationAssociationEducationOrganizations(staffId, httpHeaders, uriInfo);

        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        EntityBody body = null;
        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
            body = (EntityBody) responseEntityObj;
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity; actually have " + results.size(), results.size() == 1);
            body = results.get(0);
        } else {
            fail("Response entity not recognized: " + response);
            return;
        }

        assertNotNull("Should return an entity", body);
        assertEquals("organizationCategories should be State Education Agency", "State Education Agency", body.get("organizationCategories"));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetCohortAssociations() {
        //create one entity
        Response createResponse = staffResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Response res = educationOrganizationResource.create(new EntityBody(), httpHeaders, uriInfo);
        String edOrgId = ResourceTestUtil.parseIdFromLocation(res);

        createResponse = cohortResource.create(new EntityBody(createTestCohortEntity(edOrgId)), httpHeaders, uriInfo);
        String targetId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestCohortAssociationEntity();
        map.put(ParameterConstants.COHORT_ID, targetId);
        map.put(ParameterConstants.STAFF_ID, staffId);

        createResponse = staffCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        //String associationId = parseIdFromLocation(createResponse);

        Response response = staffResource.getStaffCohortAssociations(staffId, httpHeaders, uriInfo);

        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        EntityBody body = null;
        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
            body = (EntityBody) responseEntityObj;
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity; actually have " + results.size(), results.size() == 1);
            body = results.get(0);
        } else {
            fail("Response entity not recognized: " + response);
            return;
        }

        assertNotNull("Should return an entity", body);
        assertEquals(StaffCohortAssociationResource.BEGIN_DATE + " should be " + cohortAssnBeginDate, cohortAssnBeginDate, body.get(StaffCohortAssociationResource.BEGIN_DATE));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetAssociatedCohorts() {
        //create one entity
        Response createResponse = staffResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Response res = educationOrganizationResource.create(new EntityBody(), httpHeaders, uriInfo);
        String edOrgId = ResourceTestUtil.parseIdFromLocation(res);

        createResponse = cohortResource.create(new EntityBody(createTestCohortEntity(edOrgId)), httpHeaders, uriInfo);
        String cohortId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestCohortAssociationEntity();
        map.put(ParameterConstants.STAFF_ID, staffId);
        map.put(ParameterConstants.COHORT_ID, cohortId);

        createResponse = staffCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = staffResource.getStaffCohortAssociationCohorts(staffId, httpHeaders, uriInfo);

        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        EntityBody body = null;
        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
            body = (EntityBody) responseEntityObj;
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity; actually have " + results.size(), results.size() == 1);
            body = results.get(0);
        } else {
            fail("Response entity not recognized: " + response);
            return;
        }

        assertNotNull("Should return an entity", body);
        assertEquals(CohortResource.COHORT_TYPE + " should be " + cohortType, cohortType, body.get(CohortResource.COHORT_TYPE));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    private Map<String, Object> createTestProgramEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("programId", "1001");
       return entity;
    }

    private Map<String, Object> createTestStaffProgramAssociationEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("beginDate", "2012-01-01");
        return entity;
    }

    @Test
    public void testGetStaffProgramAssociations() {
        //create one entity
        Response createResponse = programResource.create(new EntityBody(createTestProgramEntity()), httpHeaders, uriInfo);
        String programId = ResourceTestUtil.parseIdFromLocation(createResponse);

        createResponse = staffResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStaffProgramAssociationEntity();
        map.put("programId", programId);
        map.put("staffId", staffId);

        createResponse = staffProgramAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = staffResource.getStaffProgramAssociations(staffId, httpHeaders, uriInfo);

        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        EntityBody body = null;
        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
            body = (EntityBody) responseEntityObj;
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity", results.size() == 1);
            body = results.get(0);
        } else {
            fail("Response entity not recognized: " + response);
            return;
        }

        assertNotNull("Should return an entity", body);
        assertEquals("beginDate should be 2012-01-01", "2012-01-01", body.get("beginDate"));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetStaffProgramAssociationProgram() {
        //create one entity
        Response createResponse = programResource.create(new EntityBody(createTestProgramEntity()), httpHeaders, uriInfo);
        String programId = ResourceTestUtil.parseIdFromLocation(createResponse);

        createResponse = staffResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String staffId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStaffProgramAssociationEntity();
        map.put("programId", programId);
        map.put("staffId", staffId);

        createResponse = staffProgramAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = staffResource.getStaffProgramAssociationPrograms(staffId, httpHeaders, uriInfo);

        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        Object responseEntityObj = null;

        if (response.getEntity() instanceof EntityResponse) {
            EntityResponse resp = (EntityResponse) response.getEntity();
            responseEntityObj = resp.getEntity();
        } else {
            fail("Should always return EntityResponse: " + response);
        }

        EntityBody body = null;
        if (responseEntityObj instanceof EntityBody) {
            assertNotNull(responseEntityObj);
            body = (EntityBody) responseEntityObj;
        } else if (responseEntityObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<EntityBody> results = (List<EntityBody>) responseEntityObj;
            assertTrue("Should have one entity", results.size() == 1);
            body = results.get(0);
        } else {
            fail("Response entity not recognized: " + response);
            return;
        }

        assertNotNull("Should return an entity", body);
        assertEquals("studentUniqueStateId should be 1001", "1001", body.get("programId"));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }
}
