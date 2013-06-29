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
package org.slc.sli.api.security.context.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralQuery;

/**
 * JUnit for student access to sections
 *
 * @author nbrown
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
@SuppressWarnings("unchecked")
public class StudentToSectionValidatorTest {
    @Autowired
    private StudentToSectionValidator underTest;

    @Autowired
    private SecurityContextInjector injector;

    private PagingRepositoryDelegate<Entity> repo = mock(PagingRepositoryDelegate.class);

    @Before
    public void setUp() {
        underTest.setRepo(repo);
    }

    private void makeStudentContext() {
        injector.setStudentContext(makeRiver());
    }

    private Entity makeRiver() {
        Entity river = mock(Entity.class);
        when(river.getEntityId()).thenReturn("riverTam");
        Map<String, Object> section1 = new HashMap<String, Object>();
        section1.put("_id", "quantumPhysics");
        Map<String, Object> section2 = new HashMap<String, Object>();
        section2.put("_id", "history");
        Map<String, Object> section3 = new HashMap<String, Object>();
        section3.put("_id", "dance");
        Map<String, List<Map<String, Object>>> denormalized = new HashMap<String, List<Map<String,Object>>>();
        denormalized.put("section", Arrays.asList(section1, section2, section3));
        when(river.getDenormalizedData()).thenReturn(denormalized);
        return river;
    }

    private Entity makeSimon() {
        Entity simon = mock(Entity.class);
        when(simon.getEntityId()).thenReturn("simonTam");
        Map<String, Object> section1 = new HashMap<String, Object>();
        section1.put("_id", "medicine");
        Map<String, List<Map<String, Object>>> denormalized = new HashMap<String, List<Map<String,Object>>>();
        denormalized.put("section", Arrays.asList(section1));
        when(simon.getDenormalizedData()).thenReturn(denormalized);
        return simon;
    }

    private void makeParentContext() {
        Entity r = makeRiver();
        Entity s = makeSimon();
        Entity e = mock(Entity.class);
        when(e.getEntityId()).thenReturn("mrTam");
        when(e.getType()).thenReturn("parent");
        when(repo.findAll(eq("student"), any(NeutralQuery.class))).thenReturn(Arrays.asList(r, s));
        injector.setStudentContext(e);
    }

    @Test
    public void testAsStudent() {
        makeStudentContext();
        assertTrue(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("quantumPhysics"))));
        assertTrue(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("quantumPhysics", "history", "dance"))));
        assertFalse(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("remedialMath"))));
        assertFalse(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("remedialMath", "quantumPhysics"))));
    }

    @Test
    public void testAsParent() {
        makeParentContext();
        assertTrue(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("quantumPhysics"))));
        assertFalse(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("remedialMath"))));
        assertFalse(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("remedialMath", "quantumPhysics"))));
        assertTrue(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("medicine"))));
        assertTrue(underTest.validate(EntityNames.SECTION, new HashSet<String>(Arrays.asList("quantumPhysics", "history", "dance", "medicine"))));
    }

}
