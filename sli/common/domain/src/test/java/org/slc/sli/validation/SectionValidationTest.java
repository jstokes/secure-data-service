package org.slc.sli.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.domain.Entity;

/**
 * JUnit for validating section
 *
 * @author nbrown
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class SectionValidationTest {

    @Autowired
    private EntityValidator validator;

    @Autowired
    private DummyEntityRepository repo;

    @Before
    public void init() {
        repo.clean();
        repo.addEntity("school", "42", ValidationTestUtils.makeDummyEntity("school", "42"));
        repo.addEntity("session", "MySessionId", ValidationTestUtils.makeDummyEntity("session", "MySessionId"));
        repo.addEntity("course", "MyCourseId", ValidationTestUtils.makeDummyEntity("course", "MyCourseId"));
        repo.addEntity("program", "program1", ValidationTestUtils.makeDummyEntity("program", "program1"));
        //repo.addEntity("program", "program2", ValidationTestUtils.makeDummyEntity("program", "program2"));
    }

    private Entity goodSection() {
        final Map<String, Object> goodSection = new HashMap<String, Object>();
        goodSection.put("uniqueSectionCode", "Math101");
        goodSection.put("sequenceOfCourse", 1);
        goodSection.put("educationalEnvironment", "Classroom");
        goodSection.put("mediumOfInstruction", "Face-to-face instruction");
        goodSection.put("populationServed", "Regular Students");
        Map<String, Object> credit = new HashMap<String, Object>();
        credit.put("credit", 1.5);
        credit.put("creditType", "Semester hour credit");
        credit.put("creditConversion", 2.5);
        goodSection.put("availableCredit", credit);
        goodSection.put("schoolId", "42");
        goodSection.put("sessionId", "MySessionId");
        goodSection.put("courseId", "MyCourseId");
//        List<String> programs = new ArrayList<String>();
//        programs.add("program1");
//        programs.add("program2");
//        goodSection.put("programReference", programs);
        goodSection.put("programReference", "program1");

        return new Entity() {

            @Override
            public String getType() {
                return "section";
            }

            @Override
            public String getEntityId() {
                return "id";
            }

            @Override
            public Map<String, Object> getBody() {
                return goodSection;
            }

            @Override
            public Map<String, Object> getMetaData() {
                return new HashMap<String, Object>();
            }
        };
    }

    @Test
    public void testSectionValidation() {
        Entity goodSection = goodSection();
        assertTrue(validator.validate(goodSection));
    }

    @Test
    @ExpectedException(value = EntityValidationException.class)
    public void testBadSectionValidation() {
        Entity goodSection = goodSection();
        goodSection.getBody().put("schoolId", "INVALID");
        goodSection.getBody().put("sessionId", "INVALID");
        goodSection.getBody().put("courseId", "INVALID");
        validator.validate(goodSection);
    }

    @Test
    public void testMinimumSection() {
        Entity minSection = goodSection();
        minSection.getBody().remove("educationalEnvironment");
        minSection.getBody().remove("mediumOfInstruction");
        minSection.getBody().remove("populationServed");
        minSection.getBody().remove("availableCredit");
        assertTrue(validator.validate(minSection));
    }

    @Test
    public void testMissingRequiredFields() {
        Entity missingSectionCode = goodSection();
        missingSectionCode.getBody().remove("uniqueSectionCode");
        try {
            assertFalse(validator.validate(missingSectionCode));
        } catch (EntityValidationException e) {
            List<ValidationError> errors = e.getValidationErrors();
            ValidationError error = errors.get(0);
            assertEquals(ValidationError.ErrorType.REQUIRED_FIELD_MISSING, error.getType());
        }

    }
}
