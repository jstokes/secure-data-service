package org.slc.sli.api.init;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import org.slc.sli.dal.repository.EntityRepository;
import org.slc.sli.domain.Entity;

/**
 * Simple test for RoleInitializer
 */
public class RoleInitializerTest {
    
    private RoleInitializer roleInitializer;
    private EntityRepository mockRepo;
    
    @Before
    public void setUp() throws Exception {
        mockRepo = mock(EntityRepository.class);
        roleInitializer = new RoleInitializer();
        roleInitializer.setRepository(mockRepo);
    }
    
    @Test
    public void testAllRolesCreated() throws Exception {
        when(mockRepo.findAll("roles")).thenReturn(new ArrayList<Entity>());
        
        assertTrue(roleInitializer.buildRoles() == 5);
        
    }
}
