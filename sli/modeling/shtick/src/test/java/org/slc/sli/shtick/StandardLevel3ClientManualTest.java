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
import org.slc.sli.shtick.pojo.SexType;
import org.slc.sli.shtick.pojo.SimpleName;
import org.slc.sli.shtick.pojo.Student;
import org.slc.sli.shtick.pojo.UniqueStateIdentifier;
import org.slc.sli.shtick.pojomanual.BirthDataManual;
import org.slc.sli.shtick.pojomanual.NameManual;
import org.slc.sli.shtick.pojomanual.StudentManual;

public class StandardLevel3ClientManualTest {

    private static final String BASE_URL = "http://local.slidev.org:8080/api/rest/v1";
    private static final Map<String, Object> EMPTY_QUERY_ARGS = Collections.emptyMap();

    private void doGetStudents(final Level2Client inner) {
        final Level3ClientManual client = new StandardLevel3ClientManual(BASE_URL, inner);
        try {
            final Map<String, Object> queryArgs = new HashMap<String, Object>();
            queryArgs.put("limit", 1000);
            final List<StudentManual> students = client.getStudents(TestingConstants.ROGERS_TOKEN, queryArgs);
            assertNotNull(students);
            final Map<String, StudentManual> studentMap = new HashMap<String, StudentManual>();
            for (@SuppressWarnings("unused")
            final StudentManual student : students) {
                studentMap.put(student.getId(), student);
            }
            {
                final StudentManual student = studentMap.get(TestingConstants.TEST_STUDENT_ID);
                assertNotNull(student);
                // FIXME:
                // assertEquals(TestingConstants.TEST_STUDENT_ID, student.getId());
                // assertEquals("Male", student.getSex());
                // final Name name = student.getName();
                // assertEquals("Garry", name.getFirstName());
                // assertEquals("Kinsel", name.getLastSurname());
                // assertEquals(Boolean.FALSE, student.getEconomicDisadvantaged());
                // assertEquals("100000005", student.getStudentUniqueStateId());
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final StatusCodeException e) {
            fail(e.getMessage());
        }
    }

    private void doGetStudentsById(final Level2Client inner) {
        final Level3ClientManual client = new StandardLevel3ClientManual(BASE_URL, inner);
        // One identifier.
        try {
            final List<String> studentIds = new LinkedList<String>();
            studentIds.add(TestingConstants.TEST_STUDENT_ID);
            final List<StudentManual> students = client.getStudentsById(TestingConstants.ROGERS_TOKEN, studentIds,
                    EMPTY_QUERY_ARGS);

            assertNotNull(students);
            assertEquals(1, students.size());
            final StudentManual student = students.get(0);
            assertNotNull(student);
            // FIXME
            // assertEquals(TestingConstants.TEST_STUDENT_ID, student.getId());
            // assertEquals("Male", student.getSex());
            // assertEquals(Boolean.FALSE, student.getEconomicDisadvantaged());
            // assertEquals("100000005", student.getStudentUniqueStateId());
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
                final Entity student = studentMap.get(TestingConstants.TEST_STUDENT_ID);
                assertNotNull(student);
                assertEquals(TestingConstants.TEST_STUDENT_ID, student.getId());
                assertEquals("student", student.getType());
                final Map<String, Object> data = student.getData();
                assertNotNull(data);
                assertEquals("Male", data.get("sex"));
                final Object name = data.get("name");
                assertTrue(name instanceof Map);
                @SuppressWarnings("unchecked")
                final Map<String, Object> nameMap = (Map<String, Object>) name;
                assertEquals("Garry", nameMap.get("firstName"));
                assertEquals("Kinsel", nameMap.get("lastSurname"));
                assertEquals(Boolean.FALSE, data.get("economicDisadvantaged"));
                assertEquals("100000005", data.get("studentUniqueStateId"));
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
    public void testPostStudent() {
        try {
            doPostStudentUsingJson(new StandardLevel2Client(BASE_URL, new JsonLevel1Client()));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (StatusCodeException e) {
            e.printStackTrace();
            fail("Status Code Returned : " + e.getStatusCode());
        }
    }

    private void doPostStudentUsingJson(Level2Client inner) throws IOException, StatusCodeException {
        final Level3ClientManual client = new StandardLevel3ClientManual(BASE_URL, inner);
        StudentManual student = new StudentManual(new HashMap<String, Object>());

        NameManual name = new NameManual(new HashMap<String, Object>());
        name.setFirstName(new SimpleName("Jeff"));
        name.setMiddleName(new SimpleName("Allen"));
        name.setLastSurname(new SimpleName("Stokes"));
        student.setName(name);

        student.setUniqueStateIdentifier(new UniqueStateIdentifier("1234-STUDENT"));
        student.setSex(SexType.MALE);

        BirthDataManual birthData = new BirthDataManual(new HashMap<String, Object>());
        birthData.setBirthDate("1988-12-01");
        student.setBirthData(birthData);

        student.setHispanicLatinoEthnicity(false);
        client.postStudent(TestingConstants.ROGERS_TOKEN, student);
    }

    @Ignore
    public void testGetStudentsByIdUsingJson() {
        doGetStudentsById(new StandardLevel2Client(BASE_URL, new JsonLevel1Client()));
    }

    @Ignore
    public void testGetStudentsByIdUsingStAX() {
        doGetStudentsById(new StandardLevel2Client(BASE_URL, new StAXLevel1Client()));
    }

    @Ignore
    public void testGetStudentsUsingJson() {
        doGetStudents(new StandardLevel2Client(BASE_URL, new JsonLevel1Client()));
    }

    @Ignore("Need POJO wrapping Entity")
    public void testGetStudentsUsingStAX() {
        doGetStudents(new StandardLevel2Client(BASE_URL, new StAXLevel1Client()));
    }

    @Ignore("Problem with invalid autorization token.")
    public void testGetStudentsWithBrokenTokenUsingJson() {
        doGetStudentsWithBrokenToken(new JsonLevel1Client());
    }

    @Ignore("Problem with invalid autorization token.")
    public void testGetStudentsWithBrokenTokenUsingStAX() {
        doGetStudentsWithBrokenToken(new StAXLevel1Client());
    }
}
