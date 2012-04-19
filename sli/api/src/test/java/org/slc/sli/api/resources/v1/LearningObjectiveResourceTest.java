package org.slc.sli.api.resources.v1;

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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.uri.UriBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.v1.entity.LearningObjectiveResource;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.common.constants.ResourceConstants;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.ParameterConstants;

/**
 * Unit tests for the resource representing an learningObjective
 *
 * @author dliu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class LearningObjectiveResourceTest {

    @Autowired
    LearningObjectiveResource learningObjResource; // class under test

    @Autowired
    private SecurityContextInjector injector;

    private UriInfo uriInfo;
    private HttpHeaders httpHeaders;

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
        entity.put("academicSubject", "Reading");
        entity.put("objectiveGradeLevel", "Eighth grade");
        entity.put(ParameterConstants.LEARNINGOBJECTIVE_ID, 1234);
        return entity;
    }

    private Map<String, Object> createTestUpdateEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("academicSubject", "Writing");
        entity.put("objectiveGradeLevel", "Fifth grade");
        entity.put(ParameterConstants.LEARNINGOBJECTIVE_ID, 1234);
        return entity;
    }

    private Map<String, Object> createTestSecondaryEntity() {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("academicSubject", "Math");
        entity.put("objectiveGradeLevel", "Ninth grade");
        entity.put(ParameterConstants.LEARNINGOBJECTIVE_ID, 5678);
        return entity;
    }

    @Test
    public void testCreate() {
        Response response = learningObjResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        assertEquals("Status code should be 201", Status.CREATED.getStatusCode(), response.getStatus());

        String id = parseIdFromLocation(response);
        assertNotNull("ID should not be null", id);
    }

    @Test
    public void testRead() {
        // create one entity
        Response createResponse = learningObjResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = parseIdFromLocation(createResponse);
        Response response = learningObjResource.read(id, httpHeaders, uriInfo);

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
        // create one entity
        Response createResponse = learningObjResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = parseIdFromLocation(createResponse);

        // delete it
        Response response = learningObjResource.delete(id, httpHeaders, uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        try {
            @SuppressWarnings("unused")
            Response getResponse = learningObjResource.read(id, httpHeaders, uriInfo);
            fail("should have thrown EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            return;
        } catch (Exception e) {
            fail("threw wrong exception: " + e);
        }
    }

    @Test
    public void testUpdate() {
        // create one entity
        Response createResponse = learningObjResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        String id = parseIdFromLocation(createResponse);

        // update it
        Response response = learningObjResource.update(id, new EntityBody(createTestUpdateEntity()), httpHeaders,
                uriInfo);
        assertEquals("Status code should be NO_CONTENT", Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // try to get it
        Response getResponse = learningObjResource.read(id, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), getResponse.getStatus());
        EntityBody body = (EntityBody) getResponse.getEntity();
        assertNotNull("Should return an entity", body);
        assertEquals(ParameterConstants.LEARNINGOBJECTIVE_ID + " should be 1234",
                body.get(ParameterConstants.LEARNINGOBJECTIVE_ID), 1234);
        assertEquals("academicSubject should be Writing", body.get("academicSubject"), "Writing");
        assertNotNull("Should include links", body.get(ResourceConstants.LINKS));
    }

    @Test
    public void testReadAll() {
        // create two entities
        learningObjResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        learningObjResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders, uriInfo);

        // read everything
        Response response = learningObjResource.readAll(0, 100, httpHeaders, uriInfo);
        assertEquals("Status code should be OK", Status.OK.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        List<EntityBody> results = (List<EntityBody>) response.getEntity();
        assertNotNull("Should return entities", results);
        assertTrue("Should have at least two entities", results.size() >= 2);
    }

    @Test
    public void testReadCommaSeparatedResources() {
        Response response = learningObjResource.read(getIDList(ResourceNames.LEARNINGOBJECTIVES), httpHeaders, uriInfo);
        assertEquals("Status code should be 200", Status.OK.getStatusCode(), response.getStatus());

        @SuppressWarnings("unchecked")
        List<EntityBody> results = (List<EntityBody>) response.getEntity();
        assertEquals("Should get 2 entities", results.size(), 2);

        EntityBody body1 = results.get(0);
        assertNotNull("Should not be null", body1);
        assertEquals(ParameterConstants.LEARNINGOBJECTIVE_ID + " should be 1234",
                body1.get(ParameterConstants.LEARNINGOBJECTIVE_ID), 1234);
        assertNotNull("Should include links", body1.get(ResourceConstants.LINKS));

        EntityBody body2 = results.get(1);
        assertNotNull("Should not be null", body2);
        assertEquals(ParameterConstants.LEARNINGOBJECTIVE_ID + " should be 5678",
                body2.get(ParameterConstants.LEARNINGOBJECTIVE_ID), 5678);
        assertNotNull("Should include links", body2.get(ResourceConstants.LINKS));
    }

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

        when(mock.getQueryParameters(true)).thenReturn(new MultivaluedMapImpl());
        when(mock.getRequestUri()).thenReturn(new UriBuilderImpl().replaceQuery(queryString).build(new Object[] {}));
        return mock;
    }

    private String getIDList(String resource) {
        // create more resources
        Response createResponse1 = learningObjResource.create(new EntityBody(createTestEntity()), httpHeaders, uriInfo);
        Response createResponse2 = learningObjResource.create(new EntityBody(createTestSecondaryEntity()), httpHeaders,
                uriInfo);

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
