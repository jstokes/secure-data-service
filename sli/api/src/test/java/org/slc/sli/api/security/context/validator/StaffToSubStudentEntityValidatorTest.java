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

package org.slc.sli.api.security.context.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.resources.SecurityContextInjector;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.api.security.roles.SecureRoleRightAccessImpl;
import org.slc.sli.api.test.WebContextTestExecutionListener;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.domain.NeutralQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
@TestExecutionListeners({ WebContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class })
public class StaffToSubStudentEntityValidatorTest {

    @Autowired
    private StaffToSubStudentEntityValidator validator;

    @Autowired
    private SecurityContextInjector injector;

    private PagingRepositoryDelegate<Entity> mockRepo;
    private StaffToStudentValidator staffToStudentValidator;
    private Set<String> studentIds;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        studentIds = new HashSet<String>();
        mockRepo = Mockito.mock(PagingRepositoryDelegate.class);
        staffToStudentValidator = Mockito.mock(StaffToStudentValidator.class);

        String user = "fake staff";
        String fullName = "Fake Staff";
        List<String> roles = Arrays.asList(SecureRoleRightAccessImpl.IT_ADMINISTRATOR);

        Entity entity = Mockito.mock(Entity.class);
        Mockito.when(entity.getType()).thenReturn("staff");
        Mockito.when(entity.getEntityId()).thenReturn("1");
        injector.setCustomContext(user, fullName, "DERPREALM", roles, entity, "123");

        validator.setRepo(mockRepo);
        validator.setStaffToStudentValidator(staffToStudentValidator);
    }

    @After
    public void tearDown() {
        mockRepo = null;
        staffToStudentValidator = null;
        studentIds.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testCanValidateStaffToAttendance() throws Exception {
        assertTrue(validator.canValidate(EntityNames.ATTENDANCE, false));
    }

    @Test
    public void testCanValidateStaffToCourseTranscript() throws Exception {
        assertTrue(validator.canValidate(EntityNames.COURSE_TRANSCRIPT, false));
    }

    @Test
    public void testCanValidateStaffToDisciplineAction() throws Exception {
        assertTrue(validator.canValidate(EntityNames.DISCIPLINE_ACTION, false));
    }

    @Test
    public void testCanValidateStaffToStudentAcademicRecord() throws Exception {
        assertTrue(validator.canValidate(EntityNames.STUDENT_ACADEMIC_RECORD, false));
    }

    @Test
    public void testCanValidateStaffToStudentAssessment() throws Exception {
        assertTrue(validator.canValidate(EntityNames.STUDENT_ASSESSMENT_ASSOCIATION, false));
    }

    @Test
    public void testCanValidateStaffToStudentDisciplineIncident() throws Exception {
        assertTrue(validator.canValidate(EntityNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATION, false));
    }

    @Test
    public void testCanValidateStaffToStudentGradebookEntry() throws Exception {
        assertTrue(validator.canValidate(EntityNames.STUDENT_GRADEBOOK_ENTRY, false));
    }

    @Test
    public void testCanValidateStaffToStudentSchoolAssociation() throws Exception {
        assertTrue(validator.canValidate(EntityNames.STUDENT_SCHOOL_ASSOCIATION, false));
    }

    @Test
    public void testCanValidateStaffToStudentSectionAssociation() throws Exception {
        assertTrue(validator.canValidate(EntityNames.STUDENT_SECTION_ASSOCIATION, false));
    }

    @Test
    public void testCanNotValidateOtherEntities() throws Exception {
        assertFalse(validator.canValidate(EntityNames.STUDENT, false));
    }

    @Test
    public void testCanGetAccessToAttendance() throws Exception {
        Set<String> studentIds = new HashSet<String>();
        Set<String> attendances = new HashSet<String>();
        Map<String, Object> attendance1 = buildAttendanceForStudent("student123", "school123");
        Entity attendanceEntity1 = new MongoEntity("attendance", attendance1);
        attendances.add(attendanceEntity1.getEntityId());
        studentIds.add("student123");

        Mockito.when(mockRepo.findAll(Mockito.eq(EntityNames.ATTENDANCE), Mockito.any(NeutralQuery.class))).thenReturn(
                Arrays.asList(attendanceEntity1));

        Mockito.when(staffToStudentValidator.validate(EntityNames.STUDENT, studentIds)).thenReturn(true);
        assertTrue(validator.validate(EntityNames.ATTENDANCE, attendances));
    }

    @Test
    public void testCanNotGetAccessToAttendance() throws Exception {
        Set<String> attendances = new HashSet<String>();
        Map<String, Object> attendance1 = buildAttendanceForStudent("student123", "school123");
        Entity attendanceEntity1 = new MongoEntity("attendance", attendance1);
        attendances.add(attendanceEntity1.getEntityId());
        studentIds.add("student123");

        Mockito.when(mockRepo.findAll(Mockito.eq(EntityNames.ATTENDANCE), Mockito.any(NeutralQuery.class))).thenReturn(
                Arrays.asList(attendanceEntity1));

        Mockito.when(staffToStudentValidator.validate(EntityNames.STUDENT, studentIds)).thenReturn(false);
        assertFalse(validator.validate(EntityNames.ATTENDANCE, attendances));
    }

    @Test
    public void testCanGetAccessToCurrentStudentSchoolAssociation() throws Exception {
        Map<String, Object> goodStudentSchoolAssociation = buildStudentSchoolAssociation("student123", "school123",
                new DateTime().plusHours(1));
        Entity association = new MongoEntity(EntityNames.STUDENT_SCHOOL_ASSOCIATION, goodStudentSchoolAssociation);
        Mockito.when(
                mockRepo.findAll(Mockito.eq(EntityNames.STUDENT_SCHOOL_ASSOCIATION), Mockito.any(NeutralQuery.class)))
                .thenReturn(Arrays.asList(association));
        Set<String> associations = new HashSet<String>();
        associations.add(association.getEntityId());

        studentIds.add("student123");
        Mockito.when(staffToStudentValidator.validate(EntityNames.STUDENT, studentIds)).thenReturn(true);

        assertTrue(validator.validate(EntityNames.STUDENT_SCHOOL_ASSOCIATION, associations));
    }

    @Test
    public void testCanGetAccessToStudentSchoolAssociationWithoutExitWithdrawDate() throws Exception {
        Map<String, Object> goodStudentSchoolAssociation = buildStudentSchoolAssociation("student123", "school123");
        Entity association = new MongoEntity(EntityNames.STUDENT_SCHOOL_ASSOCIATION, goodStudentSchoolAssociation);
        Mockito.when(
                mockRepo.findAll(Mockito.eq(EntityNames.STUDENT_SCHOOL_ASSOCIATION), Mockito.any(NeutralQuery.class)))
                .thenReturn(Arrays.asList(association));
        Set<String> associations = new HashSet<String>();
        associations.add(association.getEntityId());

        studentIds.add("student123");
        Mockito.when(staffToStudentValidator.validate(EntityNames.STUDENT, studentIds)).thenReturn(true);

        assertTrue(validator.validate(EntityNames.STUDENT_SCHOOL_ASSOCIATION, associations));
    }

    @Test
    public void testDeniedAccessToExpiredStudentSchoolAssociation() throws Exception {
        Map<String, Object> badStudentSchoolAssociation = buildStudentSchoolAssociation("student123", "school123",
                new DateTime().minusDays(1));
        Entity association = new MongoEntity(EntityNames.STUDENT_SCHOOL_ASSOCIATION, badStudentSchoolAssociation);
        Mockito.when(
                mockRepo.findAll(Mockito.eq(EntityNames.STUDENT_SCHOOL_ASSOCIATION), Mockito.any(NeutralQuery.class)))
                .thenReturn(Arrays.asList(association));
        Set<String> associations = new HashSet<String>();
        associations.add(association.getEntityId());
        assertFalse(validator.validate(EntityNames.STUDENT_SCHOOL_ASSOCIATION, associations));
    }

    @Test
    public void testCanGetAccessToCurrentStudentSectionAssociation() throws Exception {
        Map<String, Object> goodStudentSectionAssociation = buildStudentSectionAssociation("student123", "section123",
                new DateTime().plusDays(1));
        Entity association = new MongoEntity(EntityNames.STUDENT_SECTION_ASSOCIATION, goodStudentSectionAssociation);
        Mockito.when(
                mockRepo.findAll(Mockito.eq(EntityNames.STUDENT_SECTION_ASSOCIATION), Mockito.any(NeutralQuery.class)))
                .thenReturn(Arrays.asList(association));
        Set<String> associations = new HashSet<String>();
        associations.add(association.getEntityId());

        studentIds.add("student123");
        Mockito.when(staffToStudentValidator.validate(EntityNames.STUDENT, studentIds)).thenReturn(true);

        assertTrue(validator.validate(EntityNames.STUDENT_SECTION_ASSOCIATION, associations));
    }

    @Test
    public void testCanGetAccessToStudentSectionAssociationWithoutEndDate() throws Exception {
        Map<String, Object> goodStudentSectionAssociation = buildStudentSectionAssociation("student123", "section123");
        Entity association = new MongoEntity(EntityNames.STUDENT_SECTION_ASSOCIATION, goodStudentSectionAssociation);
        Mockito.when(
                mockRepo.findAll(Mockito.eq(EntityNames.STUDENT_SECTION_ASSOCIATION), Mockito.any(NeutralQuery.class)))
                .thenReturn(Arrays.asList(association));
        Set<String> associations = new HashSet<String>();
        associations.add(association.getEntityId());

        studentIds.add("student123");
        Mockito.when(staffToStudentValidator.validate(EntityNames.STUDENT, studentIds)).thenReturn(true);

        assertTrue(validator.validate(EntityNames.STUDENT_SECTION_ASSOCIATION, associations));
    }

    @Test
    public void testDeniedAccessToExpiredStudentSectionAssociation() throws Exception {
        Map<String, Object> badStudentSectionAssociation = buildStudentSchoolAssociation("student123", "section123",
                new DateTime().minusDays(1));
        Entity association = new MongoEntity(EntityNames.STUDENT_SECTION_ASSOCIATION, badStudentSectionAssociation);
        Mockito.when(
                mockRepo.findAll(Mockito.eq(EntityNames.STUDENT_SECTION_ASSOCIATION), Mockito.any(NeutralQuery.class)))
                .thenReturn(Arrays.asList(association));
        Set<String> associations = new HashSet<String>();
        associations.add(association.getEntityId());
        assertFalse(validator.validate(EntityNames.STUDENT_SECTION_ASSOCIATION, associations));
    }

    private Map<String, Object> buildAttendanceForStudent(String studentId, String schoolId) {
        Map<String, Object> attendance = new HashMap<String, Object>();
        attendance.put("studentId", studentId);
        attendance.put("schoolId", schoolId);
        List<Map<String, Object>> schoolYearAttendance = new ArrayList<Map<String, Object>>();
        Map<String, Object> onlyYear = new HashMap<String, Object>();
        onlyYear.put("schoolYear", "2011-2012");
        onlyYear.put("attendanceEvent", new ArrayList<Map<String, Object>>());
        attendance.put("schoolYearAttendance", schoolYearAttendance);
        return attendance;
    }

    private Map<String, Object> buildStudentSchoolAssociation(String studentId, String schoolId) {
        Map<String, Object> association = new HashMap<String, Object>();
        association.put("studentId", studentId);
        association.put("schoolId", schoolId);
        association.put("entryDate", validator.getDateTimeString(DateTime.now().minusDays(3)));
        return association;
    }

    private Map<String, Object> buildStudentSchoolAssociation(String studentId, String schoolId,
            DateTime exitWithdrawDate) {
        Map<String, Object> association = new HashMap<String, Object>();
        association.put("studentId", studentId);
        association.put("schoolId", schoolId);
        association.put("entryDate", validator.getDateTimeString(exitWithdrawDate.minusDays(3)));
        association.put("exitWithdrawDate", validator.getDateTimeString(exitWithdrawDate));
        return association;
    }

    private Map<String, Object> buildStudentSectionAssociation(String studentId, String sectionId) {
        Map<String, Object> association = new HashMap<String, Object>();
        association.put("studentId", studentId);
        association.put("sectionId", sectionId);
        association.put("beginDate", validator.getDateTimeString(DateTime.now().minusDays(3)));
        return association;
    }

    private Map<String, Object> buildStudentSectionAssociation(String studentId, String sectionId, DateTime endDate) {
        Map<String, Object> association = new HashMap<String, Object>();
        association.put("studentId", studentId);
        association.put("sectionId", sectionId);
        association.put("beginDate", validator.getDateTimeString(endDate.minusDays(3)));
        association.put("endDate", validator.getDateTimeString(endDate));
        return association;
    }
}
