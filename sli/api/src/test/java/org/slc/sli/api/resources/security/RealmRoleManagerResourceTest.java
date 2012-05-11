package org.slc.sli.api.resources.security;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

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
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.test.WebContextTestExecutionListener;

/**
 * Simple test for ClientRoleManagerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@DirtiesContext
public class RealmRoleManagerResourceTest {
    @Autowired
    private RealmRoleManagerResource resource;

    @Autowired
    private SecurityContextInjector injector;

    private EntityService service;
    private EntityBody mapping;
    private EntityBody realm2;

    @Before
    public void setUp() throws Exception {

        injector.setRealmAdminContext();

        mapping = new EntityBody();
        mapping.put("id", "123567324");
        mapping.put("realm_name", "Waffles");
        mapping.put("edOrg", "fake-ed-org");
        mapping.put("mappings", new HashMap<String, String>());

        EntityBody realm2 = new EntityBody();
        realm2.put("id", "other-realm");
        realm2.put("name", "Other Realm");
        realm2.put("mappings", new HashMap<String, String>());
        realm2.put("edOrg", "another-fake-ed-org");

        service = mock(EntityService.class);

        resource.setService(service);

        when(service.update("-1", mapping)).thenReturn(true);
        when(service.update("1234", mapping)).thenReturn(true);
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
            UriInfo uriInfo = null;
            resource.updateClientRole("-1", null, uriInfo);
            assertFalse(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
        UriInfo uriInfo = null;
        Response res = resource.updateClientRole("1234", mapping, uriInfo);
        Assert.assertEquals(204, res.getStatus());
    }

    @Test
    public void testGetMappingsFound() throws Exception {
        Response res = resource.getMappings("1234");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertNotNull(res.getEntity());
    }

    @Test
    public void testGetMappingsNotFound() throws Exception {
        Response res = resource.getMappings("-1");
        Assert.assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
        Assert.assertNull(res.getEntity());
    }

    @Test
    public void testUpdateOtherEdOrgRealm() {
        EntityBody temp = new EntityBody();
        temp.put("foo", "foo");
        UriInfo uriInfo = null;
        Response res = resource.updateClientRole("other-realm", temp, uriInfo);
        Assert.assertEquals(403, res.getStatus());
    }
}
