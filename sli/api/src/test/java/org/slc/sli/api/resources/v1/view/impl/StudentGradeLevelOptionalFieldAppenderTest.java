package org.slc.sli.api.resources.v1.view.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.v1.view.OptionalFieldAppender;
import org.slc.sli.api.service.MockRepo;
import org.slc.sli.api.test.WebContextTestExecutionListener;

/**
 * Unit tests
 * @author dwu
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StudentGradeLevelOptionalFieldAppenderTest {

    @Autowired
    private OptionalFieldAppender studentGradeLevelOptionalFieldAppender;

    @Autowired
    private SecurityContextInjector injector;

    @Autowired
    MockRepo repo;

    private static final String STUDENT_ID = "1234";
    private static final String SCHOOL_ID = "5555";
    
    @Before
    public void setup() {
        
        // inject administrator security context for unit testing
        injector.setAdminContextWithElevatedRights();

        repo.create("student", createTestStudentEntity(STUDENT_ID));

        repo.create("studentSchoolAssociation", createStudentSchoolAssoc(STUDENT_ID, SCHOOL_ID, "2011-09-01", "Eighth grade"));
        repo.create("studentSchoolAssociation", createStudentSchoolAssoc(STUDENT_ID, SCHOOL_ID, "2010-09-01", "Seventh grade"));
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testApplyOptionalField() {
        List<EntityBody> entities = new ArrayList<EntityBody>();
        entities.add(new EntityBody(createTestStudentEntity(STUDENT_ID)));

        // apply optional field logic
        entities = studentGradeLevelOptionalFieldAppender.applyOptionalField(entities, null);
        assertEquals("Should be 1", 1, entities.size());

        // check for grade level
        assertEquals("Should match", "Eighth grade", entities.get(0).get("gradeLevel"));
        assertEquals("Should match", SCHOOL_ID, entities.get(0).get("schoolId"));

    }

    private Map<String, Object> createTestStudentEntity(String id) {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("id", id);
        entity.put("field2", 2);
        entity.put("studentUniqueStateId", 1234);
        return entity;
    }

    private Map<String, Object> createStudentSchoolAssoc(String studentId, String schoolId, String entryDate, String entryGradeLevel) {
        
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("studentId", studentId);
        entity.put("schoolId", schoolId);
        entity.put("entryDate", entryDate);
        entity.put("entryGradeLevel", entryGradeLevel);

        return entity;
    }

}
