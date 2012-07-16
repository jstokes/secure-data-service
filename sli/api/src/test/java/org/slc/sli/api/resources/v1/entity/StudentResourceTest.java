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
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.representation.EntityResponse;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.util.ResourceTestUtil;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.association.StudentAssessmentAssociationResource;
import org.slc.sli.api.resources.v1.association.StudentCohortAssociationResource;
import org.slc.sli.api.resources.v1.association.StudentParentAssociationResource;
import org.slc.sli.api.resources.v1.association.StudentProgramAssociationResource;
import org.slc.sli.api.resources.v1.association.StudentSchoolAssociationResource;
import org.slc.sli.api.resources.v1.association.StudentSectionAssociationResource;
import org.slc.sli.api.resources.v1.association.StudentTranscriptAssociationResource;
import org.slc.sli.api.service.EntityNotFoundException;
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
 * Unit tests for the resource representing a Student
 * @author srichards
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StudentResourceTest {

    @Autowired
    StudentResource studentResource; //class under test

    @Autowired
    ReportCardResource reportCardResource;

    @Autowired
    CohortResource cohortResource;

    @Autowired
    StudentCohortAssociationResource studentCohortAssn;

    @Autowired
    ProgramResource programResource;

    @Autowired
    AssessmentResource assessmentResource;

    @Autowired
    ParentResource parentResource;

    @Autowired
    AttendanceResource attendanceResource;

    @Autowired
    SchoolResource schoolResource;

    @Autowired
    SectionResource sectionResource;

    @Autowired
    CourseResource courseResource;

    @Autowired
    StudentProgramAssociationResource studentProgramAssociationResource;

    @Autowired
    StudentAssessmentAssociationResource studentAssessmentAssociationResource;

    @Autowired
    StudentParentAssociationResource studentParentAssociationResource;

    @Autowired
    StudentSchoolAssociationResource studentSchoolAssociationResource;

    @Autowired
    StudentSectionAssociationResource studentSectionAssociationResource;

    @Autowired
    StudentTranscriptAssociationResource studentTranscriptAssociationResource;

    @Autowired
    EducationOrganizationResource educationOrganizationResource;

    @Autowired
    private SecurityContextInjector injector;

    private UriInfo uriInfo;
    private HttpHeaders httpHeaders;
    private final String firstStudentId = "1234";
    private final String secondStudentId = "2345";
    private final String secondName = "Dua";
    private final String uniqueStateId = "9876";
    private final String cohortId = "3456";
    private final String cohortAssociationId = "4567";
    private final String cohortAssnBeginDate = "2012-02-02";
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
        entity.put(ParameterConstants.STUDENT_ID, firstStudentId);
        entity.put(StudentResource.UNIQUE_STATE_ID, uniqueStateId);
        entity.put(StudentResource.NAME, "Unua");
        entity.put(StudentResource.SEX, "Female");
        entity.put(StudentResource.BIRTH_DATA, "1999-01-01");
        entity.put(StudentResource.HISPANIC_LATINO_ETHNICITY, "true");
        return entity;
    }

    private Map<String, Object> createTestUpdateEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STUDENT_ID, firstStudentId);
        entity.put(StudentResource.UNIQUE_STATE_ID, uniqueStateId);
        entity.put(StudentResource.NAME, secondName);
        entity.put(StudentResource.SEX, "Female");
        entity.put(StudentResource.BIRTH_DATA, "1999-01-01");
        entity.put(StudentResource.HISPANIC_LATINO_ETHNICITY, "true");
        return entity;
    }

    private Map<String, Object> createTestSecondaryEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STUDENT_ID, secondStudentId);
        entity.put(StudentResource.UNIQUE_STATE_ID, uniqueStateId);
        entity.put(StudentResource.NAME, "Tria");
        entity.put(StudentResource.SEX, "Male");
        entity.put(StudentResource.BIRTH_DATA, "1999-12-31");
        entity.put(StudentResource.HISPANIC_LATINO_ETHNICITY, "false");
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
        entity.put(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID, cohortAssociationId);
        entity.put(StudentCohortAssociationResource.BEGIN_DATE, cohortAssnBeginDate);
        return entity;
    }

    @Test
    public void testCreate() {
        Response response = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());

        String id = ResourceTestUtil.parseIdFromLocation(response);
        assertNotNull("ID should not be null", id);
    }

    @Test
    public void testRead() {
        //create one entity
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);
        Response response = studentResource.read(id, httpHeaders, uriInfo);

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

    @Test(expected = EntityNotFoundException.class)
    public void testDelete() {
        //create one entity
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        //delete it
        Response response = studentResource.delete(id, httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        @SuppressWarnings("unused")
        Response getResponse = studentResource.read(id, httpHeaders, uriInfo);
    }

    @Test
    public void testUpdate() {
        //create one entity
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = ResourceTestUtil.parseIdFromLocation(createResponse);

        //update it
        Response response = studentResource.update(id, new EntityBody(createTestUpdateEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        //try to get it
        Response getResponse = studentResource.read(id, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), getResponse.getStatus());
        EntityResponse entityResponse = (EntityResponse) getResponse.getEntity();
        EntityBody body = (EntityBody) entityResponse.getEntity();
        assertNotNull("Should return an entity", body);
        assertEquals(ParameterConstants.STUDENT_ID + " should be " + firstStudentId, firstStudentId, body.get(ParameterConstants.STUDENT_ID));
        assertEquals(StudentResource.NAME + " should be " + secondName, secondName, body.get(StudentResource.NAME));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testReadAll() {
        //create two entities
        studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        studentResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        //read everything
        Response response = studentResource.readAll(0, 100, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        List<EntityBody> results = (List<EntityBody>) entityResponse.getEntity();
        assertNotNull("Should return entities", results);
        assertTrue("Should have at least two entities", results.size() >= 2);
    }

    @Test
    public void testReadCommaSeparatedResources() {
        Response response = studentResource.read(getIDList(ResourceNames.STUDENTS), httpHeaders, uriInfo);
        assertEquals("Status code should be 200", Status.OK.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        List<EntityBody> results = (List<EntityBody>) entityResponse.getEntity();
        assertEquals("Should get 2 entities", 2, results.size());

        EntityBody body1 = results.get(0);
        assertNotNull("Should not be null", body1);
        assertEquals(ParameterConstants.STUDENT_ID + " should be " + firstStudentId, firstStudentId, body1.get(ParameterConstants.STUDENT_ID));
        assertNotNull("Should include links", body1.get(ResourceConstants.LINKS));

        EntityBody body2 = results.get(1);
        assertNotNull("Should not be null", body2);
        assertEquals(ParameterConstants.STUDENT_ID + " should be " + secondStudentId, secondStudentId, body2.get(ParameterConstants.STUDENT_ID));
        assertNotNull("Should include links", body2.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetCohortAssociations() {
        //create one entity
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Response res = educationOrganizationResource.create(new EntityBody(), httpHeaders, uriInfo);
        String edOrgId = ResourceTestUtil.parseIdFromLocation(res);

        createResponse = cohortResource.create(new EntityBody(createTestCohortEntity(edOrgId)), httpHeaders, uriInfo);
        String targetId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestCohortAssociationEntity();
        map.put(ParameterConstants.COHORT_ID, targetId);
        map.put(ParameterConstants.STUDENT_ID, studentId);

        createResponse = studentCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        //String associationId = parseIdFromLocation(createResponse);

        Response response = studentResource.getStudentCohortAssociations(studentId, httpHeaders, uriInfo);

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
        assertEquals(StudentCohortAssociationResource.BEGIN_DATE + " should be " + cohortAssnBeginDate, cohortAssnBeginDate, body.get(StudentCohortAssociationResource.BEGIN_DATE));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetAssociatedCohorts() {
        //create one entity
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Response res = educationOrganizationResource.create(new EntityBody(), httpHeaders, uriInfo);
        String edOrgId = ResourceTestUtil.parseIdFromLocation(res);

        createResponse = cohortResource.create(new EntityBody(createTestCohortEntity(edOrgId)), httpHeaders, uriInfo);
        String cohortId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestCohortAssociationEntity();
        map.put(ParameterConstants.STUDENT_ID, studentId);
        map.put(ParameterConstants.COHORT_ID, cohortId);

        createResponse = studentCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentCohortAssociationCohorts(studentId, httpHeaders, uriInfo);

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

    private Map<String, Object> createTestStudentProgramAssociationEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("beginDate", "2012-01-01");
        return entity;
    }

    @Test
    public void testGetStudentProgramAssociations() {
        //create one entity
        Response createResponse = programResource.create(new EntityBody(createTestProgramEntity()), httpHeaders, uriInfo);
        String programId = ResourceTestUtil.parseIdFromLocation(createResponse);

        createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStudentProgramAssociationEntity();
        map.put("programId", programId);
        map.put("studentId", studentId);

        createResponse = studentProgramAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentProgramAssociations(studentId, httpHeaders, uriInfo);

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
    public void testGetStudentProgramAssociationProgram() {
        //create one entity
        Response createResponse = programResource.create(new EntityBody(createTestProgramEntity()), httpHeaders, uriInfo);
        String programId = ResourceTestUtil.parseIdFromLocation(createResponse);

        createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStudentProgramAssociationEntity();
        map.put("programId", programId);
        map.put("studentId", studentId);

        createResponse = studentProgramAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentProgramAssociationPrograms(studentId, httpHeaders, uriInfo);

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

    @Test
    public void testGetStudentAssessmentAssociations() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = assessmentResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("AssessmentResource")), httpHeaders, uriInfo);
        String assessmentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentAssessmentAssociationResource", "StudentResource", studentId, "AssessmentResource", assessmentId);
        studentAssessmentAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentAssessmentAssociations(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentAssessmentAssociationsAssessments() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = assessmentResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("AssessmentResource")), httpHeaders, uriInfo);
        String assessmentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentAssessmentAssociationResource", "StudentResource", studentId, "AssessmentResource", assessmentId);
        studentAssessmentAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentAssessmentAssociationsAssessments(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentParentAssociations() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = parentResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("ParentResource")), httpHeaders, uriInfo);
        String parentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentParentAssociationResource", "StudentResource", studentId, "ParentResource", parentId);
        studentParentAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentParentAssociations(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentParentAssociationCourses() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = parentResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("ParentResource")), httpHeaders, uriInfo);
        String parentId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentParentAssociationResource", "StudentResource", studentId, "ParentResource", parentId);
        studentParentAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentParentAssociationCourses(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentsAttendance() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        Map<String, Object> map = ResourceTestUtil.createTestEntity("AttendanceResource");
        map.put(ParameterConstants.STUDENT_ID, studentId);
        createResponse = attendanceResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentsAttendance(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentSchoolAssociations() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = schoolResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("SchoolResource")), httpHeaders, uriInfo);
        String schoolId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentSchoolAssociationResource", "StudentResource", studentId, "SchoolResource", schoolId);
        studentSchoolAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentSchoolAssociations(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentSchoolAssociationSchools() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = schoolResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("SchoolResource")), httpHeaders, uriInfo);
        String schoolId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentSchoolAssociationResource", "StudentResource", studentId, "SchoolResource", schoolId);
        studentSchoolAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentSchoolAssociationSchools(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentSectionAssociations() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = sectionResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("SectionResource")), httpHeaders, uriInfo);
        String sectionId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentSectionAssociationResource", "StudentResource", studentId, "SectionResource", sectionId);
        studentSectionAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentSectionAssociations(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentSectionAssociationSections() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = sectionResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("SectionResource")), httpHeaders, uriInfo);
        String sectionId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentSectionAssociationResource", "StudentResource", studentId, "SectionResource", sectionId);
        studentSectionAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentSectionAssociationSections(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
     public void testGetStudentTranscriptAssociations() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = courseResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("CourseResource")), httpHeaders, uriInfo);
        String courseId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentTranscriptAssociationResource", "StudentResource", studentId, "CourseResource", courseId);
        studentTranscriptAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentTranscriptAssociations(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetStudentTranscriptAssociationCourses() {
        Response createResponse = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        createResponse = courseResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity("CourseResource")), httpHeaders, uriInfo);
        String courseId = ResourceTestUtil.parseIdFromLocation(createResponse);

        Map<String, Object> map = ResourceTestUtil.createTestAssociationEntity(
                "StudentTranscriptAssociationResource", "StudentResource", studentId, "CourseResource", courseId);
        studentTranscriptAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getStudentTranscriptAssociationCourses(studentId, httpHeaders, uriInfo);
        ResourceTestUtil.assertions(response);
    }

    @Test
    public void testGetReportCards() {
        final String studentResourceName = "StudentResource";
        final String reportCardResourceName = "ReportCardResource";
        Response createResponse = studentResource.create(new EntityBody(
                ResourceTestUtil.createTestEntity(studentResourceName)), httpHeaders, uriInfo);
        String studentId = ResourceTestUtil.parseIdFromLocation(createResponse);
        Map<String, Object> map = ResourceTestUtil.createTestEntity(reportCardResourceName);
        map.put(ParameterConstants.STUDENT_ID, studentId);
        reportCardResource.create(new EntityBody(map), httpHeaders, uriInfo);

        Response response = studentResource.getReportCards(studentId, httpHeaders, uriInfo);
        EntityBody body = ResourceTestUtil.assertions(response);
        assertEquals(studentId, body.get(ParameterConstants.STUDENT_ID));
    }

    private String getIDList(String resource) {
        //create more resources
        Response createResponse1 = studentResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        Response createResponse2 = studentResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        return ResourceTestUtil.parseIdFromLocation(createResponse1) + "," + ResourceTestUtil.parseIdFromLocation(createResponse2);
    }
}
