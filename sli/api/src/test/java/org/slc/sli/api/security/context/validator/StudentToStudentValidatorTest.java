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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.common.constants.EntityNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.Entity;

/**
 * JUnit for student to student
 *
 * @author nbrown
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StudentToStudentValidatorTest {

    @Autowired
    private StudentToStudentValidator underTest;

    @Autowired
    private SecurityContextInjector injector;

    @Before
    public void setUp() throws Exception {
        Entity e = mock(Entity.class);
        when(e.getEntityId()).thenReturn("riverTam");
        when(e.getType()).thenReturn(EntityNames.STUDENT);
        injector.setStudentContext(e);
    }

    @Test
    public void testStudentCanAccessHerself() {
        Set<String> idsToValidate = new HashSet<String>(Arrays.asList("riverTam"));
        assertTrue(underTest.validate("student", idsToValidate).containsAll(idsToValidate));
    }

    @Test
    public void testStudentCannotAccessOthers() {
        Set<String> idsToValidate = new HashSet<String>(Arrays.asList("simonTam"));
        assertFalse(underTest.validate("student", idsToValidate).containsAll(idsToValidate));
    }

    @Test
    public void testHeterogeneousList() {
        Set<String> idsToValidate = new HashSet<String>(Arrays.asList("simonTam", "riverTam"));
        assertFalse(underTest.validate("student", idsToValidate).containsAll(idsToValidate));
    }

}
