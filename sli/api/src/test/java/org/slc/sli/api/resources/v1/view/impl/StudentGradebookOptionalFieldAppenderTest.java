package org.slc.sli.api.resources.v1.view.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.resources.v1.view.OptionalFieldAppender;
import org.slc.sli.api.service.MockRepo;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Unit tests
 * @author srupasinghe
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StudentGradebookOptionalFieldAppenderTest {

    @Autowired
    private OptionalFieldAppender studentGradebookOptionalFieldAppender;

    @Autowired
    private SecurityContextInjector injector;

    @Autowired
    MockRepo repo;

    private static final String STUDENT_ID = "1234";
    private static final String SECTION_ID = "5555";
    private String gradebookEntryId1;
    private String gradebookEntryId2;

    @Before
    public void setup() {
        // inject administrator security context for unit testing
        injector.setAdminContextWithElevatedRights();

        repo.create("student", createTestStudentEntityWithSectionAssociation(STUDENT_ID, SECTION_ID));

        gradebookEntryId1 = repo.create("gradebookEntry", createGradebookEntry()).getEntityId();
        gradebookEntryId1 = repo.create("gradebookEntry", createGradebookEntry()).getEntityId();

        repo.create("studentSectionGradebookEntry", createStudentSectionGradebookEntry(STUDENT_ID, SECTION_ID, gradebookEntryId1));
        repo.create("studentSectionGradebookEntry", createStudentSectionGradebookEntry(STUDENT_ID, SECTION_ID, gradebookEntryId1));
    }

    @After
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testApplyOptionalField() {
        List<EntityBody> entities = new ArrayList<EntityBody>();
        entities.add(new EntityBody(createTestStudentEntityWithSectionAssociation(STUDENT_ID, SECTION_ID)));

        entities = studentGradebookOptionalFieldAppender.applyOptionalField(entities);

        //test should be updated as code is put in
        assertEquals("Should be 1", 1, entities.size());

        EntityBody body = (EntityBody) entities.get(0).get("gradebook");
        assertNotNull("Should not be null", body.get("studentSectionGradebookEntries"));
        assertNotNull("Should not be null", body.get("gradebookEntries"));

        List<EntityBody> studentSectionGradebookAssociations = (List<EntityBody>) body.get("studentSectionGradebookEntries");
        assertEquals("Should match", 2, studentSectionGradebookAssociations.size());
        assertEquals("Should match", STUDENT_ID, studentSectionGradebookAssociations.get(0).get("studentId"));
        assertEquals("Should match", SECTION_ID, studentSectionGradebookAssociations.get(0).get("sectionId"));

        List<EntityBody> gradebookEntries = (List<EntityBody>) body.get("gradebookEntries");
        assertEquals("Should match", 2, gradebookEntries.size());
        assertEquals("Should match", "Unit Tests", gradebookEntries.get(0).get("gradebookEntryType"));
        for (EntityBody e : gradebookEntries) {
            if (!((String) e.get("id")).equals(gradebookEntryId1)
                    && !((String) e.get("id")).equals(gradebookEntryId2)) {
                fail("AssessmentIds should match");
            }
        }
    }

    private Map<String, Object> createTestStudentEntityWithSectionAssociation(String id, String sectionId) {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("id", id);
        entity.put("field2", 2);
        entity.put("studentUniqueStateId", 1234);

        EntityBody body = new EntityBody();
        body.put("studentId", id);
        body.put("sectionId", sectionId);

        List<EntityBody> list = new ArrayList<EntityBody>();
        list.add(body);

        entity.put("studentSectionAssociation", list);

        return entity;
    }

    private Map<String, Object> createStudentSectionGradebookEntry(String studentId, String sectionId,
                                                                   String gradebookEntryId) {
        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("studentId", studentId);
        entity.put("sectionId", sectionId);
        entity.put("gradebookEntryId", gradebookEntryId);
        entity.put("letterGradeEarned", "A");
        entity.put("numericGradeEarned", "90");

        return entity;
    }

    private Map<String, Object> createGradebookEntry() {

        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("gradebookEntryType", "Unit Tests");
        entity.put("dateAssigned", "2012-03-22");

        return entity;
    }
}
