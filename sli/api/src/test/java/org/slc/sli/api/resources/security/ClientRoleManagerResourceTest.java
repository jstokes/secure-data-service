package org.slc.sli.api.resources.security;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClientRoleManagerResourceTest {
    @Autowired
    private ClientRoleManagerResource resource;
    
    @Autowired
    private SecurityContextInjector injector;
    
    private EntityService service;
    private EntityBody mapping;
    
    @Before
    public void setUp() throws Exception {
        
        injector.setAdminContext();

        mapping = new EntityBody();
        mapping.put("id", "123567324");
        mapping.put("realm_name", "Waffles");

        service = mock(EntityService.class);
        
        resource.setService(service);
        
        when(service.update("-1", mapping)).thenReturn(true);
        when(service.update("1234", mapping)).thenReturn(true);
        when(service.get("-1")).thenReturn(null);
        when(service.get("1234")).thenReturn(mapping);
    }
    
    @After
    public void tearDown() throws Exception {
        service = null;
    }
    
    @Test
    public void testAddClientRole() throws Exception {
        try {
            resource.updateClientRole("-1", null);
            assertFalse(false);
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
        assertTrue(resource.updateClientRole("1234", mapping));
    }
    
    @Test
    public void testGetMappings() throws Exception {
        assertNotNull(resource.getMappings("1234"));
        assertNull(resource.getMappings("-1"));
    }
}
