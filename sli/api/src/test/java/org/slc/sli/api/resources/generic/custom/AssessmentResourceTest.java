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

package org.slc.sli.api.resources.generic.custom;

import com.sun.jersey.api.uri.UriBuilderImpl;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.representation.EntityResponse;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.generic.representation.Resource;
import org.slc.sli.api.resources.generic.service.DefaultResourceService;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit Tests
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class AssessmentResourceTest {

    @Autowired
    private AssessmentResource assessmentResource;

    @Autowired
    private SecurityContextInjector injector;

    @Autowired
    private DefaultResourceService resourceService;

    private Resource assessments;
    private Resource learningStandards;
    private java.net.URI requestURI;
    private UriInfo uriInfo;

    private static final String BASE_URI = "http://some.net/api/rest/v1/assessments";

    @Before
    public void setup() throws Exception {
        // inject administrator security context for unit testing
        injector.setAdminContextWithElevatedRights();

        assessments = new Resource("v1", "assessments");
        learningStandards = new Resource("v1", "learningStandards");
    }

    private void setupMocks(String uri) throws URISyntaxException {
        requestURI = new java.net.URI(uri);

        MultivaluedMap map = new MultivaluedMapImpl();
        uriInfo = mock(UriInfo.class);
        when(uriInfo.getRequestUri()).thenReturn(requestURI);
        when(uriInfo.getQueryParameters()).thenReturn(map);
        when(uriInfo.getBaseUriBuilder()).thenAnswer(new Answer<UriBuilder>() {
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return new UriBuilderImpl().path("base");
            }
        });
    }

    @Test
    public void testGet() throws URISyntaxException {
        String learningStandardId = resourceService.postEntity(learningStandards, createLearningStandardEntity());
        String assessmentId = resourceService.postEntity(assessments, createAssessmentEntity(learningStandardId));

        setupMocks(BASE_URI + "/" + assessmentId + "/learningStandards");

        Response response = assessmentResource.get(uriInfo, assessmentId);
        assertEquals("Status code should be OK", Response.Status.OK.getStatusCode(), response.getStatus());

        EntityResponse entityResponse = (EntityResponse) response.getEntity();
        assertEquals("Should match", 1, ((List<EntityBody>) entityResponse.getEntity()).size());
    }

    private EntityBody createAssessmentEntity(String learningStandardId) {
        EntityBody assessmentItem = new EntityBody();
        assessmentItem.put("learningStandards", Arrays.asList(learningStandardId));

        EntityBody entity = new EntityBody();
        entity.put("assessmentTitle", "Test");
        entity.put("assessmentItem", Arrays.asList(assessmentItem));
        return entity;
    }

    private EntityBody createLearningStandardEntity() {
        EntityBody entity = new EntityBody();
        entity.put("somefield", "somevalue");
        return entity;
    }
}
