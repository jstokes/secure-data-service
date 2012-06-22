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


package org.slc.sli.api.security.context.resolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slc.sli.api.client.constants.EntityNames;
import org.slc.sli.api.client.constants.v1.ParameterConstants;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit Tests
 *
 * @author pghosh
 *
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class NodeDateFilterTest {
    @InjectMocks
    @Spy
    NodeDateFilter nodeFilter = new NodeDateFilter(); //class under test

    @Mock
    private AssociativeContextHelper mockHelper;

    @Mock
    private Repository<Entity> mockRepo;

    @Before
    public void setup() {
    }

    @Test
    public void testIsFirstDateBeforeSecondDate() {
        assertTrue("Should be true", nodeFilter.isFirstDateBeforeSecondDate("2012-02-03", "2012-08-03"));

        assertFalse("Should be true", nodeFilter.isFirstDateBeforeSecondDate("2012-02-03", ""));
        assertFalse("Should be false", nodeFilter.isFirstDateBeforeSecondDate("2012-06-03", "2012-05-12"));
        assertFalse("Should be false", nodeFilter.isFirstDateBeforeSecondDate("", "2012-05-12"));
        assertFalse("Should be false", nodeFilter.isFirstDateBeforeSecondDate("2012-06-03", "somevalue"));
    }

    @Test
    public void testFilterIds() {
        List<Entity> studentSchoolAssociations = getStudentSchoolAssociations();

        when(mockHelper.getFilterDate(anyString(), any(Calendar.class))).thenReturn("2012-04-03");

        when(mockHelper.getReferenceEntities(eq(EntityNames.STUDENT_SCHOOL_ASSOCIATION),
                eq(ParameterConstants.STUDENT_ID), any(List.class))).thenReturn(studentSchoolAssociations);

        when(mockHelper.getReferenceEntities(eq(EntityNames.STUDENT_SECTION_ASSOCIATION),
                eq(ParameterConstants.STUDENT_ID), any(List.class))).thenReturn(studentSchoolAssociations);

        List<String> ids = new ArrayList<String>();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        ids.add("4");
        ids.add("5");
        ids.add("6");

        nodeFilter.setParameters("2000", "exitWithdrawDate");
        List<Entity> returnedEntities = nodeFilter.filterEntities(studentSchoolAssociations, "studentId");
        List<String> returnedIds = getReturnedIds(returnedEntities, "studentId");
        assertNotNull("Should not be null", returnedIds);
        assertEquals("Should match", 5, returnedIds.size());
        assertTrue("Should be true", returnedIds.contains("1"));
        assertTrue("Should be true", returnedIds.contains("3"));
        assertTrue("Should be true", returnedIds.contains("4"));
        assertTrue("Should be true", returnedIds.contains("5"));
        assertTrue("Should be true", returnedIds.contains("6"));

        nodeFilter.setParameters("0", "endDate");
        returnedEntities = nodeFilter.filterEntities(studentSchoolAssociations, "studentId");
        List<String> returnStudentIds = getReturnedIds(returnedEntities, "studentId");
        assertNotNull("Should not be null", returnStudentIds);
        assertEquals("Should match", 5, returnStudentIds.size());
        assertTrue("Should be true", returnStudentIds.contains("1"));
        assertTrue("Should be true", returnStudentIds.contains("2"));
        assertTrue("Should be true", returnStudentIds.contains("3"));
        assertTrue("Should be true", returnStudentIds.contains("4"));
        assertTrue("Should be true", returnStudentIds.contains("5"));
    }

    private List<Entity> getStudentSchoolAssociations() {
        List<Entity> list = new ArrayList<Entity>();

        list.add(createEntity("studentId", "1", "exitWithdrawDate", "2012-08-03"));
        list.add(createEntity("studentId", "2", "exitWithdrawDate", "2012-01-03"));
        list.add(createEntity("studentId", "3", "someKey", "7"));
        list.add(createEntity("studentId", "4", "exitWithdrawDate", ""));
        list.add(createEntity("studentId", "5", "endDate", "2012-08-03"));
        list.add(createEntity("studentId", "6", "endDate", "2012-01-03"));

        return list;
    }

    private Entity createEntity(String key1, String value1,
                                String key2, String value2) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put(key1, value1);
        body.put(key2, value2);

        Entity mockEntity = mock(Entity.class);
        when(mockEntity.getBody()).thenReturn(body);

        return mockEntity;
    }
    private List<String> getReturnedIds(List<Entity> entityList, String refId) {
        List<String> ids = new ArrayList<String>();
        for (Entity e : entityList) {
            ids.add(e.getBody().get(refId).toString());
        }
        return ids;
    }
}
