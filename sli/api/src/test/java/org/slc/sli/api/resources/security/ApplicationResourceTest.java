package org.slc.sli.api.resources.security;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.oauth.SliClientDetailService;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * 
 * @author pwolf
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class })
@DirtiesContext
public class ApplicationResourceTest {
    
    @Autowired
    @InjectMocks private ApplicationResource resource;
    
    @Autowired
    @InjectMocks private SliClientDetailService detailsService;
    
    @Mock EntityService service;
    
    UriInfo uriInfo = null;
    
    private static final int STATUS_CREATED = 201;
    private static final int STATUS_DELETED = 204;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_FOUND = 200;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        uriInfo = buildMockUriInfo(null);
    }
    
    @Test
    public void testGoodCreate() {
        
        EntityBody app = getNewApp();
        
        // test create during dup check
        Mockito.when(
                service.list(Mockito.eq(0), Mockito.eq(1), Mockito.anyString()))
                .thenReturn(new ArrayList<String>());
        
        Response resp = resource.createApplication(app, uriInfo);
        assertEquals(STATUS_CREATED, resp.getStatus());
        assertTrue("Client id set", app.get("client_id").toString().length() == 10);
        assertTrue("Client secret set", app.get("client_secret").toString().length() == 48);
        
    }
    
    
    private EntityBody getNewApp() {
        EntityBody app = new EntityBody();
        app.put("client_type", "PUBLIC");
        app.put("redirect_uri", "https://slidev.org");
        app.put("description", "blah blah blah");
        app.put("name", "TestApp");
        app.put("scope", "ENABLED");
        return app;
    }
    
    @Test
    public void testGoodDelete() {
        String clientId = "1234567890";
        String uuid = "123";
        
        EntityBody toDelete = getNewApp();
        ArrayList<String> existingEntitiesIds = new ArrayList<String>();
        
        toDelete.put("client_id", clientId);
        toDelete.put("id", uuid);
        existingEntitiesIds.add(uuid);
        Mockito.when(
                service.list(0, 1, "client_id=" + clientId))
                .thenReturn(existingEntitiesIds);
        Response resp = resource.deleteApplication(clientId);
        assertEquals(STATUS_DELETED, resp.getStatus());
    }
    
    @Test
    public void testBadDelete() {
        String clientId = "9999999999";

        Mockito.when(
                service.list(0, 1, "client_id=" + clientId))
                .thenReturn(new ArrayList<String>());
        Response resp = resource.deleteApplication("9999999999");
        assertEquals(STATUS_NOT_FOUND, resp.getStatus());
    }
    
    @Test
    public void testGoodGet() {
        String clientId = "1234567890";
        String uuid = "123";
        
        EntityBody toGet = getNewApp();
        ArrayList<String> existingEntitiesIds = new ArrayList<String>();
        
        toGet.put("client_id", clientId);
        toGet.put("id", uuid);
        existingEntitiesIds.add(uuid);
        Mockito.when(
                service.list(0, 1, "client_id=" + clientId))
                .thenReturn(existingEntitiesIds);
        Response resp = resource.getApplication(clientId);
        assertEquals(STATUS_FOUND, resp.getStatus());
    }
    
    @Test
    public void testBadGet() {
        String clientId = "9999999999";

        Mockito.when(
                service.list(0, 1, "client_id=" + clientId))
                .thenReturn(new ArrayList<String>());
        Response resp = resource.getApplication("9999999999");
        assertEquals(STATUS_NOT_FOUND, resp.getStatus());
    }
    
    @Test
    public void testClientLookup() {
        String clientId = "1234567890";
        String uuid = "123";
        
        ArrayList<String> existingEntitiesIds = new ArrayList<String>();
        existingEntitiesIds.add(uuid);
        Mockito.when(
                service.list(0, 1, "client_id=" + clientId))
                .thenReturn(existingEntitiesIds);
        
        EntityBody mockApp = getNewApp();
        mockApp.put("client_id", clientId);
        mockApp.put("id", uuid);
        mockApp.put("client_secret", "ldkafjladsfjdsalfadsl");
        Mockito.when(service.get(uuid)).thenReturn(mockApp);
        
        ClientDetails details = detailsService.loadClientByClientId(clientId);
        assertNotNull(details);
        assertNotNull("Checking for client id", details.getClientId());
        assertNotNull("Checking for client secret", details.getClientSecret());
        assertNotNull("Checking for redirect uri", details.getWebServerRedirectUri());
        assertNotNull("Checking for scope", details.getScope());
    }
    
    @Test(expected = OAuth2Exception.class)
    public void testBadClientLookup() {
        String clientId = "1234567890";

        //return empty list
        Mockito.when(
                service.list(0, 1, "client_id=" + clientId))
                .thenReturn(new ArrayList<String>());
        detailsService.loadClientByClientId(clientId);
    }
    
    public UriInfo buildMockUriInfo(final String queryString) throws Exception {
        UriInfo mock = mock(UriInfo.class);
        when(mock.getBaseUri()).thenReturn(new URI("http://blah.org"));
        when(mock.getPath()).thenReturn("blah");
        return mock;
    }

    
}
