package org.slc.sli.api.resources.v1.entity;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.util.ResourceConstants;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.api.resources.v1.association.StaffCohortAssociation;
import org.slc.sli.api.resources.v1.association.StudentCohortAssociation;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.sun.jersey.api.uri.UriBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Unit tests for the resource representing a cohort
 * @author srichards
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class CohortResourceTest {

    @Autowired
    CohortResource cohortResource; //class under test 

    @Autowired
    StaffResource staffResource; //class under test 

    @Autowired
    StaffCohortAssociation staffCohortAssn;

    @Autowired
    StudentResource studentResource; //class under test 

    @Autowired
    StudentCohortAssociation studentCohortAssn;

    @Autowired
    private SecurityContextInjector injector;
    
    private UriInfo uriInfo;
    private HttpHeaders httpHeaders;
    private final String firstCohortId = "1234";
    private final String secondCohortId = "2345";
    private final String staffId = "3456";
    private final String uniqueStateId = "4567";
    private final String staffAssociationId = "5678";
    private final String studentId = "6789";
    private final String studentAssociationId = "7890";
    private final String staffAssnBeginDate = "2012-03-03";
    private final String studentAssnBeginDate = "2012-04-04";
    private final String secondCohortType = "Dua Type";
    private final String edOrgId = "9876";
    
    @Before
    public void setup() throws Exception {
        uriInfo = buildMockUriInfo(null);
        
        // inject administrator security context for unit testing
        injector.setAdminContextWithElevatedRights();
        
        List<String> acceptRequestHeaders = new ArrayList<String>();
        acceptRequestHeaders.add(HypermediaType.VENDOR_SLC_JSON);
        
        httpHeaders = mock(HttpHeaders.class);
        when(httpHeaders.getRequestHeader("accept")).thenReturn(acceptRequestHeaders);
        when(httpHeaders.getRequestHeaders()).thenReturn(new MultivaluedMapImpl());
    }
    
    private Map<String, Object> createTestEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(CohortResource.COHORT_IDENTIFIER, firstCohortId);
        entity.put(CohortResource.COHORT_TYPE, "Unua Type");
        entity.put(CohortResource.EDUCATION_ORGANIZATION_ID, edOrgId);
        return entity;
    }
    
    private Map<String, Object> createTestUpdateEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(CohortResource.COHORT_IDENTIFIER, firstCohortId);
        entity.put(CohortResource.COHORT_TYPE, secondCohortType);
        entity.put(CohortResource.EDUCATION_ORGANIZATION_ID, edOrgId);
        return entity;
    }
    
    private Map<String, Object> createTestSecondaryEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(CohortResource.COHORT_IDENTIFIER, secondCohortId);
        entity.put(CohortResource.COHORT_TYPE, "Tria Type");
        entity.put(CohortResource.EDUCATION_ORGANIZATION_ID, edOrgId);
        return entity;
    }
    
    private Map<String, Object> createTestStaffEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STAFF_ID, staffId);
        entity.put(StaffResource.UNIQUE_STATE_ID, uniqueStateId);
        entity.put(StaffResource.NAME, "Unua");
        entity.put(StaffResource.SEX, "Female");
        entity.put(StaffResource.HISPANIC_LATINO_ETHNICITY, "true");
        entity.put(StaffResource.EDUCATION_LEVEL, "PhD");
        return entity;
    }
    
    private Map<String, Object> createTestStaffAssociationEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STAFF_COHORT_ASSOCIATION_ID, staffAssociationId);
        entity.put(StaffCohortAssociation.BEGIN_DATE, staffAssnBeginDate);
        return entity;
    }  

    private Map<String, Object> createTestStudentEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STUDENT_ID, studentId);
        entity.put(StudentResource.UNIQUE_STATE_ID, uniqueStateId);
        entity.put(StudentResource.NAME, "Unua");
        entity.put(StudentResource.SEX, "Female");
        entity.put(StudentResource.BIRTH_DATA, "1999-01-01");
        entity.put(StudentResource.HISPANIC_LATINO_ETHNICITY, "true");
        return entity;
    }
    
    private Map<String, Object> createTestStudentAssociationEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID, studentAssociationId);
        entity.put(StudentCohortAssociation.BEGIN_DATE, studentAssnBeginDate);
        return entity;
    }  

    @Test
    public void testCreate() {
        Response response = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());
            
        String id = parseIdFromLocation(response);
        assertNotNull("ID should not be null", id);
    }
    
    @Test
    public void testRead() {
        //create one entity
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = parseIdFromLocation(createResponse);
        Response response = cohortResource.read(id, httpHeaders, uriInfo);
        
        Object responseEntityObj = response.getEntity();
        
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
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = parseIdFromLocation(createResponse);
        
        //delete it
        Response response = cohortResource.delete(id, httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());
        
        try {
            @SuppressWarnings("unused")
            Response getResponse = cohortResource.read(id, httpHeaders, uriInfo);
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
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = parseIdFromLocation(createResponse);
        
        //update it
        Response response = cohortResource.update(id, new EntityBody(createTestUpdateEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());
          
        //try to get it
        Response getResponse = cohortResource.read(id, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), getResponse.getStatus());            
        EntityBody body = (EntityBody) getResponse.getEntity();
        assertNotNull("Should return an entity", body);            
        assertEquals(CohortResource.COHORT_IDENTIFIER + " should be " + firstCohortId, firstCohortId, body.get(CohortResource.COHORT_IDENTIFIER));
        assertEquals(CohortResource.COHORT_TYPE + " should be " + secondCohortType, secondCohortType, body.get(CohortResource.COHORT_TYPE));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }
    
    @Test
    public void testReadAll() {
        //create two entities
        cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        cohortResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);
        
        //read everything
        Response response = cohortResource.readAll(0, 100, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        List<EntityBody> results = (List<EntityBody>) response.getEntity();
        assertNotNull("Should return entities", results);
        assertTrue("Should have at least two entities", results.size() >= 2);
    }
    
    @Test
    public void testReadCommaSeparatedResources() {
        Response response = cohortResource.read(getIDList(ResourceNames.COHORTS), httpHeaders, uriInfo);
        assertEquals("Status code should be 200", Status.OK.getStatusCode(), response.getStatus());
        
        @SuppressWarnings("unchecked")
        List<EntityBody> results = (List<EntityBody>) response.getEntity();
        assertEquals("Should get 2 entities", 2, results.size());

        EntityBody body1 = results.get(0);
        assertNotNull("Should not be null", body1);
        assertEquals(CohortResource.COHORT_IDENTIFIER + " should be " + firstCohortId, firstCohortId, body1.get(CohortResource.COHORT_IDENTIFIER));
        assertNotNull("Should include links", body1.get(ResourceConstants.LINKS));
        
        EntityBody body2 = results.get(1);
        assertNotNull("Should not be null", body2);
        assertEquals(CohortResource.COHORT_IDENTIFIER + " should be " + secondCohortId, secondCohortId, body2.get(CohortResource.COHORT_IDENTIFIER));
        assertNotNull("Should include links", body2.get(ResourceConstants.LINKS));
    }
    
    @Test
    public void testGetStaffAssociations() {
        //create one entity
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String cohortId = parseIdFromLocation(createResponse);

        createResponse = staffResource.create(new EntityBody(createTestStaffEntity()), httpHeaders, uriInfo); 
        String targetId = parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStaffAssociationEntity();
        map.put(ParameterConstants.COHORT_ID, cohortId);
        map.put(ParameterConstants.STAFF_ID, targetId);

        createResponse = staffCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        //String associationId = parseIdFromLocation(createResponse);

        Response response = cohortResource.getStaffCohortAssociations(cohortId, httpHeaders, uriInfo);
        
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());            

        Object responseEntityObj = response.getEntity();

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
        assertEquals(StaffCohortAssociation.BEGIN_DATE + " should be " + staffAssnBeginDate, staffAssnBeginDate, body.get(StaffCohortAssociation.BEGIN_DATE));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetAssociatedStaff() {
        //create one entity
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String cohortId = parseIdFromLocation(createResponse);

        createResponse = staffResource.create(new EntityBody(createTestStaffEntity()), httpHeaders, uriInfo); 
        String staffId = parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStaffAssociationEntity();
        map.put(ParameterConstants.COHORT_ID, cohortId);
        map.put(ParameterConstants.STAFF_ID, staffId);

        createResponse = staffCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        
        Response response = cohortResource.getStaffCohortAssociationStaff(cohortId, httpHeaders, uriInfo);
        
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());            

        Object responseEntityObj = response.getEntity();

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
        assertEquals(StaffResource.UNIQUE_STATE_ID + " should be " + uniqueStateId, uniqueStateId, body.get(StaffResource.UNIQUE_STATE_ID));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }


/*    @Test
    public void testGetStudentAssociations() {
        //create one entity
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String cohortId = parseIdFromLocation(createResponse);

        createResponse = studentResource.create(new EntityBody(createTestStudentEntity()), httpHeaders, uriInfo); 
        String targetId = parseIdFromLocation(createResponse);

        Map<String, Object> map = createTestStudentAssociationEntity();
        map.put(ParameterConstants.COHORT_ID, cohortId);
        map.put(ParameterConstants.STUDENT_ID, targetId);

        createResponse = studentCohortAssn.create(new EntityBody(map), httpHeaders, uriInfo);
        //String associationId = parseIdFromLocation(createResponse);

        Response response = cohortResource.getStaffCohortAssociations(cohortId, httpHeaders, uriInfo);
        
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());            

        Object responseEntityObj = response.getEntity();

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
        assertEquals(StudentCohortAssociation.BEGIN_DATE + " should be " + studentAssnBeginDate, studentAssnBeginDate, body.get(StudentCohortAssociation.BEGIN_DATE));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testGetAssociatedStudents() {
        //create one entity
        Response createResponse = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String cohortId = parseIdFromLocation(createResponse);
    
        createResponse = studentResource.create(new EntityBody(createTestStudentEntity()), httpHeaders, uriInfo); 
        String studentId = parseIdFromLocation(createResponse);
    
        Map<String, Object> map = createTestStudentProgramAssociationEntity();
        map.put(ParameterConstants.COHORT_ID, cohortId);
        map.put(ParameterConstants.STUDENT_ID, studentId);
    
        createResponse = studentCohortAssociationResource.create(new EntityBody(map), httpHeaders, uriInfo);
    
        
        System.out.println( uriInfo.getQueryParameters(true) );
        System.out.println( uriInfo.getQueryParameters(true).get(ParameterConstants.OPTIONAL_FIELDS) );
        System.out.println("\n\n" + uriInfo + "\n\n");
        
        Response response = cohortResource.getStudentCohortAssociationStudent(cohortId, httpHeaders, uriInfo);
        
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());            
    
        Object responseEntityObj = response.getEntity();
    
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
        assertEquals("studentUniqueStateId should be 1001", "1001", body.get("studentUniqueStateId"));
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }
*/

    private UriInfo buildMockUriInfo(final String queryString) throws Exception {
        UriInfo mock = mock(UriInfo.class);
        when(mock.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
            
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return new UriBuilderImpl().path("absolute");
            }
        });
        when(mock.getBaseUriBuilder()).thenAnswer(new Answer<UriBuilder>() {
            
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return new UriBuilderImpl().path("base");
            }
        });
        when(mock.getRequestUriBuilder()).thenAnswer(new Answer<UriBuilder>() {
            
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return new UriBuilderImpl().path("request");
            }
        });
        
        when(mock.getQueryParameters(true)).thenAnswer(new Answer<MultivaluedMap<String, String>>() {
            @Override
            public MultivaluedMap<String, String> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return new MultivaluedMapImpl();
            }
        });
        
        when(mock.getRequestUri()).thenReturn(new UriBuilderImpl().replaceQuery(queryString).build(new Object[] {}));
        return mock;
    }
    
    private String getIDList(String resource) {
        //create more resources
        Response createResponse1 = cohortResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        Response createResponse2 = cohortResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);
        
        return parseIdFromLocation(createResponse1) + "," + parseIdFromLocation(createResponse2);
    }
    
    private static String parseIdFromLocation(Response response) {
        List<Object> locationHeaders = response.getMetadata().get("Location");
        assertNotNull(locationHeaders);
        assertEquals(1, locationHeaders.size());
        Pattern regex = Pattern.compile(".+/([\\w-]+)$");
        Matcher matcher = regex.matcher((String) locationHeaders.get(0));
        matcher.find();
        assertEquals(1, matcher.groupCount());
        return matcher.group(1);
    }
}
