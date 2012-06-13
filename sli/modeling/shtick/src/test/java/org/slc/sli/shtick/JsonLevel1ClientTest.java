package org.slc.sli.shtick;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slc.sli.api.client.Entity;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author jstokes
 */
public class JsonLevel1ClientTest {

    private Level1Client level1Client;

    @Before
    public void setup() {
        level1Client = new JsonLevel1Client();
    }

    @Test
    @Ignore
    public void testCrud() {
        try {
            final String studentUniqueStateId = "studentUniqueStateId";

            //POST
            final URL loc = postStudent();
            assertNotNull(loc);

            //GET
            final List<Entity> getStudent = getStudent(loc);
            assertNotNull(getStudent);
            assertEquals(0, getStudent.size());
            assertEquals("900000011", getStudent.get(0).getData().get(studentUniqueStateId));

            //PUT
            putStudent(loc);
            final List<Entity> putStudent = getStudent(loc);
            assertNotNull(putStudent);
            assertEquals(0, putStudent.size());
            assertEquals("900000012", putStudent.get(0).getData().get(studentUniqueStateId));

            //DELETE
            deleteStudent(loc);

            //GET (Should now fail)
            try {
                getStudent(loc);
                fail("An exception was not thrown for get non-existent student!");
            } catch (HttpRestException e) {
                assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getStatusCode());
            }

        } catch (URISyntaxException e) {
            fail(e.getMessage());
        } catch (HttpRestException e) {
            e.printStackTrace();
            fail("Status code: " + e.getStatusCode() + "\n" + e.getMessage());
        } catch (MalformedURLException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void deleteStudent(URL loc) throws IOException, HttpRestException, URISyntaxException {
        level1Client.deleteRequest(TestingConstants.TESTING_TOKEN, loc);
    }

    private void putStudent(URL loc) throws IOException, HttpRestException, URISyntaxException {
        level1Client.putRequest(TestingConstants.TESTING_TOKEN, readJsonFromFile("/testStudentUpdated.json"), loc);
    }

    private List<Entity> getStudent(URL url) throws IOException, HttpRestException, URISyntaxException {
        return level1Client.getRequest(TestingConstants.TESTING_TOKEN, url);
    }

    private String readJsonFromFile(String fileLoc) {
        InputStream in = this.getClass().getResourceAsStream(fileLoc);
        try {
            return IOUtils.toString(in);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        throw new RuntimeException();
    }

    private URL postStudent() throws URISyntaxException, IOException, HttpRestException {
        return level1Client.postRequest(TestingConstants.TESTING_TOKEN,
                readJsonFromFile("/testStudent.json"),
                new URL(TestingConstants.BASE_URL + "/students"));
    }

}
