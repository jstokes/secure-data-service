package org.slc.sli.validation;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.domain.Entity;

/**
 * Tests sample Avro schema for Students.
 *
 * @author Sean Melody <smelody@wgen.net>
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class AvroSchemaTest {

    @Autowired
    private EntitySchemaRegistry schemaReg;

    @SuppressWarnings("unchecked")
    @Test
    public void testValidStudent() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/student_fixture.json"));
        String student;
        while ((student = reader.readLine()) != null) {
            ObjectMapper oRead = new ObjectMapper();
            Map<String, Object> obj = oRead.readValue(student, Map.class);
            mapValidation((Map<String, Object>) obj.get("body"), "student");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testValidSchool() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/school_fixture.json"));
        String school;
        while ((school = reader.readLine()) != null) {
            ObjectMapper oRead = new ObjectMapper();
            Map<String, Object> obj = oRead.readValue(school, Map.class);
            mapValidation((Map<String, Object>) obj.get("body"), "school");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testValidAssessment() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("src/test/resources/assessment_fixture.json"));
        String assessment;
        while ((assessment = reader.readLine()) != null) {
            ObjectMapper oRead = new ObjectMapper();
            Map<String, Object> obj = oRead.readValue(assessment, Map.class);
            mapValidation((Map<String, Object>) obj.get("body"), "assessment");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testValidStudentAssessmentAssociation() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(
                "src/test/resources/studentassessmentassociation_fixture.json"));
        String studentassessmentassoc;
        while ((studentassessmentassoc = reader.readLine()) != null) {
            ObjectMapper oRead = new ObjectMapper();
            Map<String, Object> obj = oRead.readValue(studentassessmentassoc, Map.class);
            mapValidation((Map<String, Object>) obj.get("body"), "studentAssessmentAssociation");
        }
    }

    private void mapValidation(Map<String, Object> obj, String schemaName) {
        AvroEntityValidator validator = new AvroEntityValidator();
        validator.setSchemaRegistry(schemaReg);

        Entity e = mock(Entity.class);
        when(e.getBody()).thenReturn(obj);
        when(e.getType()).thenReturn(schemaName);

        try {
            assertTrue(validator.validate(e));
        } catch (EntityValidationException ex) {
            for (ValidationError err : ex.getValidationErrors()) {
                System.err.println(err);
            }
            fail();
        }
    }
}
