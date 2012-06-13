package org.slc.sli.shtick;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.slc.sli.api.client.Entity;

public class StandardLevel2ClientTest {

    private static final String BASE_URL = "http://local.slidev.org:8080/api/rest/v1";
    private static final Map<String, String> EMPTY_QUERY_ARGS = Collections.emptyMap();

    private void doGetStudents(final Level1Client inner) {
        final Level2Client client = new StandardLevel2Client(BASE_URL, inner);
        try {
            final List<Entity> students = client.getStudents(TestingConstants.TESTING_TOKEN, EMPTY_QUERY_ARGS);
            assertNotNull(students);
            assertEquals(50, students.size());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final SLIDataStoreException e) {
            fail(e.getMessage());
        }
    }

    private void doGetStudentsById(final Level1Client inner) {
        final Level2Client client = new StandardLevel2Client(BASE_URL, inner);
        // One identifier.
        try {
            final List<Entity> students = client.getStudentsById(TestingConstants.TESTING_TOKEN,
                    TestingConstants.TEST_STUDENT_ID, EMPTY_QUERY_ARGS);

            assertNotNull(students);
            assertEquals(1, students.size());
            final Entity student = students.get(0);
            assertNotNull(student);
            assertEquals(TestingConstants.TEST_STUDENT_ID, student.getId());
            assertEquals("student", student.getEntityType());
            final Map<String, Object> data = student.getData();
            assertNotNull(data);
            assertEquals("Male", data.get("sex"));
            assertEquals(Boolean.FALSE, data.get("economicDisadvantaged"));
            assertEquals("100000005", data.get("studentUniqueStateId"));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final SLIDataStoreException e) {
            fail(e.getMessage());
        }
        // Two identifiers.
        try {
            final List<Entity> students = client.getStudentsById(TestingConstants.TESTING_TOKEN,
                    TestingConstants.TEST_STUDENT_ID + "," + TestingConstants.TEST_STUDENT_ID_ONE + ","
                            + TestingConstants.TEST_STUDENT_ID_TWO, EMPTY_QUERY_ARGS);

            assertNotNull(students);
            assertEquals(3, students.size());
            // We have observed that multiple-id GET does not preserve ordering, so we use a map.
            final Map<String, Entity> studentMap = new HashMap<String, Entity>();
            for (final Entity student : students) {
                studentMap.put(student.getId(), student);
            }
            {
                final Entity student = studentMap.get(TestingConstants.TEST_STUDENT_ID);
                assertNotNull(student);
                assertEquals(TestingConstants.TEST_STUDENT_ID, student.getId());
                assertEquals("student", student.getEntityType());
                final Map<String, Object> data = student.getData();
                assertNotNull(data);
                assertEquals("Male", data.get("sex"));
                assertEquals(Boolean.FALSE, data.get("economicDisadvantaged"));
                assertEquals("100000005", data.get("studentUniqueStateId"));
            }
            {
                final Entity student = studentMap.get(TestingConstants.TEST_STUDENT_ID_ONE);
                assertNotNull(student);
                assertEquals(TestingConstants.TEST_STUDENT_ID_ONE, student.getId());
                assertEquals("student", student.getEntityType());
                final Map<String, Object> data = student.getData();
                assertNotNull(data);
                assertEquals("Female", data.get("sex"));
                assertEquals(null, data.get("economicDisadvantaged"));
                assertEquals("900000011", data.get("studentUniqueStateId"));
            }
            {
                final Entity student = studentMap.get(TestingConstants.TEST_STUDENT_ID_TWO);
                assertNotNull(student);
                assertEquals(TestingConstants.TEST_STUDENT_ID_TWO, student.getId());
                assertEquals("student", student.getEntityType());
                final Map<String, Object> data = student.getData();
                assertNotNull(data);
                assertEquals("Female", data.get("sex"));
                assertEquals(null, data.get("economicDisadvantaged"));
                assertEquals("900000011", data.get("studentUniqueStateId"));
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final SLIDataStoreException e) {
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

}
