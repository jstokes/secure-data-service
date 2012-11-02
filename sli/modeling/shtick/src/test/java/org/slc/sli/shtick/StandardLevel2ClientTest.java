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

package org.slc.sli.shtick;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class StandardLevel2ClientTest {

    private static final String BASE_URL = "http://local.slidev.org:8080/api/rest/v1";
    private static final Map<String, Object> EMPTY_QUERY_ARGS = Collections.emptyMap();

    private void doGetStudents(final Level1Client inner) {
        final Level2Client client = new StandardLevel2Client(BASE_URL, inner);
        try {
            final Map<String, Object> queryArgs = new HashMap<String, Object>();
            queryArgs.put("limit", 1000);
            final List<Entity> students = client.getStudents(TestingConstants.KIM_TOKEN, queryArgs);
            assertNotNull(students);
            final Map<String, Entity> studentMap = new HashMap<String, Entity>();
            for (final Entity student : students) {
                studentMap.put(student.getId(), student);
            }
            {
                final Entity student = studentMap.get(TestingConstants.TEST_STUDENT_KIM_ID);
                assertNotNull(student);
                assertEquals(TestingConstants.TEST_STUDENT_KIM_ID, student.getId());
                assertEquals("student", student.getType());
                final Map<String, Object> data = student.getData();
                assertNotNull(data);
                assertEquals("Male", data.get("sex"));
                final Object name = data.get("name");
                assertTrue(name instanceof Map);
                @SuppressWarnings("unchecked")
                final Map<String, Object> nameMap = (Map<String, Object>) name;
                assertEquals("Preston", nameMap.get("firstName"));
                assertEquals("Muchow", nameMap.get("lastSurname"));
                assertEquals("800000019", data.get("studentUniqueStateId"));
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final StatusCodeException e) {
            fail(e.getMessage());
        }
    }

    private void doGetStudentsById(final Level1Client inner) {
        final Level2Client client = new StandardLevel2Client(BASE_URL, inner);
        // One identifier.
        try {
            final List<String> studentIds = new LinkedList<String>();
            studentIds.add(TestingConstants.TEST_STUDENT_KIM_ID);
            final List<Entity> students = client.getStudentsById(TestingConstants.KIM_TOKEN, studentIds,
                    EMPTY_QUERY_ARGS);

            assertNotNull(students);
            assertEquals(1, students.size());
            final Entity student = students.get(0);
            assertNotNull(student);
            assertEquals(TestingConstants.TEST_STUDENT_KIM_ID, student.getId());
            assertEquals("student", student.getType());
            final Map<String, Object> data = student.getData();
            assertNotNull(data);
            assertEquals("Male", data.get("sex"));
            assertEquals("800000019", data.get("studentUniqueStateId"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final StatusCodeException e) {
            fail(e.getMessage());
        }
    }

    private void doGetStudentsWithBrokenToken(final Level1Client inner) {
        final Level2Client client = new StandardLevel2Client(BASE_URL, inner);
        try {
            final Map<String, Object> queryArgs = new HashMap<String, Object>();
            queryArgs.put("limit", 1000);
            final List<Entity> students = client.getStudents(TestingConstants.BROKEN_TOKEN, queryArgs);
            assertNotNull(students);
            final Map<String, Entity> studentMap = new HashMap<String, Entity>();
            for (final Entity student : students) {
                studentMap.put(student.getId(), student);
            }
            {
                final Entity student = studentMap.get(TestingConstants.TEST_STUDENT_KIM_ID);
                assertNotNull(student);
                assertEquals(TestingConstants.TEST_STUDENT_KIM_ID, student.getId());
                assertEquals("student", student.getType());
                final Map<String, Object> data = student.getData();
                assertNotNull(data);
                assertEquals("Male", data.get("sex"));
                final Object name = data.get("name");
                assertTrue(name instanceof Map);
                @SuppressWarnings("unchecked")
                final Map<String, Object> nameMap = (Map<String, Object>) name;
                assertEquals("Preston", nameMap.get("firstName"));
                assertEquals("Muchow", nameMap.get("lastSurname"));
                assertEquals("800000019", data.get("studentUniqueStateId"));
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final StatusCodeException e) {
            fail(e.getMessage());
        }
    }

    @Before
    public void setup() {
    }

    // @Test
    public void testDeleteRequest() {

    }

    @Test
    public void testGetStudentsByIdUsingJson() {
        doGetStudentsById(new JsonLevel1Client());
    }

    @Ignore("Problem with the plurality of XML documents.")
    public void testGetStudentsByIdUsingStAX() {
        doGetStudentsById(new StAXLevel1Client());
    }

    @Test
    public void testGetStudentsUsingJson() {
        doGetStudents(new JsonLevel1Client());
    }

    @Test
    public void testGetStudentsUsingStAX() {
        doGetStudents(new StAXLevel1Client());
    }

    @Ignore("Problem with invalid autorization token.")
    public void testGetStudentsWithBrokenTokenUsingJson() {
        doGetStudentsWithBrokenToken(new JsonLevel1Client());
    }

    @Ignore("Problem with invalid autorization token.")
    public void testGetStudentsWithBrokenTokenUsingStAX() {
        doGetStudentsWithBrokenToken(new StAXLevel1Client());
    }

    @Test
    public void testStudentSectionAssociations() {
        final Level1Client inner = new JsonLevel1Client();
        final Level2Client client = new StandardLevel2Client(BASE_URL, inner);
        try {
            final Map<String, Object> queryArgs = new HashMap<String, Object>();
            queryArgs.put("limit", 1000);
            final List<Entity> studentSchoolAssociations = client.getStudentSchoolAssociations(
                    TestingConstants.KIM_TOKEN, queryArgs);
            assertNotNull(studentSchoolAssociations);
            final Map<String, Entity> associationMap = new HashMap<String, Entity>();
            for (final Entity studentSchoolAssociation : studentSchoolAssociations) {
                @SuppressWarnings("unused")
                final Map<String, Object> data = studentSchoolAssociation.getData();
                associationMap.put(studentSchoolAssociation.getId(), studentSchoolAssociation);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final StatusCodeException e) {
            fail(e.getMessage());
        }
    }
}
