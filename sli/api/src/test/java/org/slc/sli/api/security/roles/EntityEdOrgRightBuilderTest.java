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

package org.slc.sli.api.security.roles;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.security.context.EdOrgOwnershipArbiter;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.Entity;

/**
 * Test EntityEdOrgRightBuilder class.
 * @author - tshewchuk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class EntityEdOrgRightBuilderTest {

    @Autowired
    @InjectMocks
    private EntityEdOrgRightBuilder entityEdOrgRightBuilder;

    private EdOrgOwnershipArbiter edOrgOwnershipArbiter;

    private Map<String, Collection<GrantedAuthority>> edOrgRights;

    private final GrantedAuthority READ_PUBLIC = new GrantedAuthorityImpl("READ_PUBLIC");
    private final GrantedAuthority READ_GENERAL = new GrantedAuthorityImpl("READ_GENERAL");
    private final GrantedAuthority READ_RESTRICTED = new GrantedAuthorityImpl("READ_RESTRICTED");
    private final GrantedAuthority AGGREGATE_READ = new GrantedAuthorityImpl("AGGREGATE_READ");
    private final GrantedAuthority WRITE_PUBLIC = new GrantedAuthorityImpl("WRITE_PUBLIC");
    private final GrantedAuthority WRITE_GENERAL = new GrantedAuthorityImpl("WRITE_GENERAL");
    private final GrantedAuthority WRITE_RESTRICTED = new GrantedAuthorityImpl("WRITE_RESTRICTED");
    private final GrantedAuthority AGGREGATE_WRITE = new GrantedAuthorityImpl("AGGREGATE_WRITE");

    @Before
    public void setup() {
        edOrgOwnershipArbiter = Mockito.mock(EdOrgOwnershipArbiter.class);
        entityEdOrgRightBuilder.setEdOrgOwnershipArbiter(edOrgOwnershipArbiter);
        edOrgRights = createEdOrgRights();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildEntityEdOrgRights() {

        Set<String> edOrgs = new HashSet<String>();
        edOrgs.add("edOrg1");
        edOrgs.add("edOrg2");
        edOrgs.add("edOrg3");
        edOrgs.add("edOrg4");

        Entity entity = Mockito.mock(Entity.class);
        Mockito.when(entity.getType()).thenReturn("student");

        Mockito.when(edOrgOwnershipArbiter.determineHierarchicalEdorgs(Matchers.anyList(), Matchers.anyString())).thenReturn(edOrgs);

        Collection<GrantedAuthority> grantedAuthorities = entityEdOrgRightBuilder.buildEntityEdOrgRights(edOrgRights, entity, false);

        Assert.assertEquals(5, grantedAuthorities.size());
        Assert.assertTrue(grantedAuthorities.contains(READ_PUBLIC));
        Assert.assertTrue(grantedAuthorities.contains(READ_GENERAL));
        Assert.assertTrue(grantedAuthorities.contains(READ_RESTRICTED));
        Assert.assertTrue(grantedAuthorities.contains(WRITE_GENERAL));
        Assert.assertTrue(grantedAuthorities.contains(WRITE_RESTRICTED));
        Assert.assertFalse(grantedAuthorities.contains(AGGREGATE_READ));
        Assert.assertFalse(grantedAuthorities.contains(WRITE_PUBLIC));
        Assert.assertFalse(grantedAuthorities.contains(AGGREGATE_WRITE));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildEntityEdOrgRightsWithNoEntityEdOrgs() {
        EdOrgOwnershipArbiter edOrgOwnershipArbiter = Mockito.mock(EdOrgOwnershipArbiter.class);
        entityEdOrgRightBuilder.setEdOrgOwnershipArbiter(edOrgOwnershipArbiter);


        Entity entity = Mockito.mock(Entity.class);
        Mockito.when(entity.getType()).thenReturn("student");

        Mockito.when(edOrgOwnershipArbiter.determineHierarchicalEdorgs(Matchers.anyList(), Matchers.anyString())).thenReturn(new HashSet<String>());

        Collection<GrantedAuthority> grantedAuthorities = entityEdOrgRightBuilder.buildEntityEdOrgRights(edOrgRights, entity, false);

        Assert.assertTrue(grantedAuthorities.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildEntityEdOrgRightsWithNoEntityEdOrgsNoMatchingEdOrgs() {
        EdOrgOwnershipArbiter edOrgOwnershipArbiter = Mockito.mock(EdOrgOwnershipArbiter.class);
        entityEdOrgRightBuilder.setEdOrgOwnershipArbiter(edOrgOwnershipArbiter);

        Set<String> edOrgs = new HashSet<String>();
        edOrgs.add("edOrg1");
        edOrgs.add("edOrg6");
        edOrgs.add("edOrg7");

        Entity entity = Mockito.mock(Entity.class);
        Mockito.when(entity.getType()).thenReturn("student");

        Mockito.when(edOrgOwnershipArbiter.determineHierarchicalEdorgs(Matchers.anyList(), Matchers.anyString())).thenReturn(edOrgs);

        Collection<GrantedAuthority> grantedAuthorities = entityEdOrgRightBuilder.buildEntityEdOrgRights(edOrgRights, entity, false);

        Assert.assertTrue(grantedAuthorities.isEmpty());
    }


    private Map<String, Collection<GrantedAuthority>> createEdOrgRights() {
        Map<String, Collection<GrantedAuthority>> edOrgRights = new HashMap<String, Collection<GrantedAuthority>>();
        Collection<GrantedAuthority> authorities2 = new HashSet<GrantedAuthority>(Arrays.asList(READ_PUBLIC,
                READ_GENERAL, READ_RESTRICTED, WRITE_GENERAL));
        Collection<GrantedAuthority> authorities3 = new HashSet<GrantedAuthority>(Arrays.asList(READ_RESTRICTED,
                WRITE_RESTRICTED));
        Collection<GrantedAuthority> authorities4 = new HashSet<GrantedAuthority>(Arrays.asList(READ_PUBLIC,
                WRITE_GENERAL));
        Collection<GrantedAuthority> authorities5 = new HashSet<GrantedAuthority>(Arrays.asList(READ_PUBLIC,
                AGGREGATE_READ, WRITE_PUBLIC, AGGREGATE_WRITE));
        edOrgRights.put("edOrg2", authorities2);
        edOrgRights.put("edOrg3", authorities3);
        edOrgRights.put("edOrg4", authorities4);
        edOrgRights.put("edOrg5", authorities5);

        return edOrgRights;
    }

}
