/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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


package org.slc.sli.api.resources.security;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.util.ResourceTestUtil;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.test.WebContextTestExecutionListener;

import javax.ws.rs.core.Response.Status;

/**
 * Simple test for RealmResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@DirtiesContext
public class RealmResourceTest {
    @Autowired
    private RealmResource resource;

    @Autowired
    private SecurityContextInjector injector;

    private EntityService service;
    private EntityBody mapping;
    //private EntityBody realm2;

    Map<String, Object> idp = new HashMap<String, Object>();

    @Before
    public void setUp() throws Exception {

        injector.setRealmAdminContext();

        mapping = new EntityBody();
        mapping.put("id", "123567324");
        mapping.put("realm_name", "Waffles");
        mapping.put("edOrg", "fake-ed-org");
        mapping.put("mappings", new HashMap<String, Object>());

        idp.put("id", "fakerealm");
        idp.put("redirectEndpoint", "fakeRedirectEndpoint");
        idp.put("artifactResolutionEndpoint", "fakeArtifactEndpoint");

        mapping.put("idp", idp);
        
        EntityBody realm2 = new EntityBody();
        realm2.put("id", "other-realm");
        realm2.put("name", "Other Realm");
        realm2.put("mappings", new HashMap<String, Object>());
        realm2.put("edOrg", "another-fake-ed-org");

        service = mock(EntityService.class);

        resource.setService(service);

        when(service.update("-1", mapping, false)).thenReturn(true);
        when(service.update("1234", mapping, false)).thenReturn(true);
        when(service.get("-1")).thenReturn(null);
        when(service.get("1234")).thenReturn(mapping);
        when(service.get("other-realm")).thenReturn(realm2);
    }

    @After
    public void tearDown() throws Exception {
        service = null;
    }

    @Test
    public void testAddClientRole() throws Exception {
        try {

            resource.updateRealm("-1", null, null);
            assertFalse(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
        UriInfo uriInfo = ResourceTestUtil.buildMockUriInfo("");
        Response res = resource.updateRealm("1234", mapping, uriInfo);
        Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), res.getStatus());

        idp.put(RealmResource.SOURCE_ID, "ccf4f3895f6e37896e7511ed1d991b1d96f04ac1");
        res = resource.updateRealm("1234", mapping, uriInfo);
        Assert.assertEquals(204, res.getStatus());
        idp.remove(RealmResource.SOURCE_ID);
    }

    @Test
    public void testvalidateArtifactResolution() {
        Response res = resource.validateArtifactResolution(null, null);
        Assert.assertNull(res);

        res = resource.validateArtifactResolution("",  "");
        Assert.assertNull(res);

        res = resource.validateArtifactResolution("testEndpoint",  "ccf4f3895f6e37896e7511ed1d991b1d96f04ac1");
        Assert.assertNull(res);

        res = resource.validateArtifactResolution("",  "ccf4f3895f6e37896e7511ed1d991b1d96f04ac1");
        Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), res.getStatus());

        res = resource.validateArtifactResolution("testEndpoint",  "");
        Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), res.getStatus());

        res = resource.validateArtifactResolution(null,  "ccf4f3895f6e37896e7511ed1d991b1d96f04ac1");
        Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), res.getStatus());

        res = resource.validateArtifactResolution("tesetEndpoint",  null);
        Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), res.getStatus());

        res = resource.validateArtifactResolution("tesetEndpoint",  "ccf4f3895f6e37896e7511ed1d991b1d96f");
        Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), res.getStatus());
    }


    @Test
    public void testGetMappingsFound() throws Exception {
        Response res = resource.readRealm("1234");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertNotNull(res.getEntity());
    }

    @Test
    public void testGetMappingsNotFound() throws Exception {
        Response res = resource.readRealm("-1");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertNull(res.getEntity());
    }

    @Test
    public void testUpdateOtherEdOrgRealm() {
        EntityBody temp = new EntityBody();
        temp.put("foo", "foo");
        UriInfo uriInfo = null;
        Response res = resource.updateRealm("other-realm", temp, uriInfo);
        Assert.assertEquals(403, res.getStatus());
    }
}
