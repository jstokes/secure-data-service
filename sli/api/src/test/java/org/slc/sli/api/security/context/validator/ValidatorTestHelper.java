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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.domain.Entity;

/**
 * Utility for unit testing context validators.
 */
@Component
public class ValidatorTestHelper {
    @Autowired
    private PagingRepositoryDelegate<Entity> repo;

    public final String STAFF_ID = "1";
    public final String ED_ORG_ID = "111";

    public String getBadDate() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime past = DateTime.now().minusYears(10);
        return past.toString(fmt);
    }

    public void generateStaffEdorg(String staffId, String edOrgId, boolean isExpired) {
        Map<String, Object> staffEdorg = new HashMap<String, Object>();
        staffEdorg.put(ParameterConstants.STAFF_REFERENCE, staffId);
        staffEdorg.put(ParameterConstants.EDUCATION_ORGANIZATION_REFERENCE, edOrgId);
        expireAssociation(isExpired, staffEdorg);
        repo.create(EntityNames.STAFF_ED_ORG_ASSOCIATION, staffEdorg);
    }

    public Entity generateEdorgWithParent(String parentId) {
        Map<String, Object> edorg = new HashMap<String, Object>();
        if (parentId != null) {
            edorg.put(ParameterConstants.PARENT_EDUCATION_AGENCY_REFERENCE, parentId);
        }
        return repo.create(EntityNames.EDUCATION_ORGANIZATION, edorg);
    }

    public Entity generateSection(String edorgId) {
        Map<String, Object> section = new HashMap<String, Object>();
        section.put(ParameterConstants.SCHOOL_ID, edorgId);
        return repo.create(EntityNames.SECTION, section);

    }

    public void generateSSA(String studentId, String sectionId, boolean isExpired) {
        Map<String, Object> ssaBody = new HashMap<String, Object>();
        ssaBody.put(ParameterConstants.SECTION_ID, sectionId);
        ssaBody.put(ParameterConstants.STUDENT_ID, studentId);
        expireAssociation(isExpired, ssaBody);
        repo.create(EntityNames.STUDENT_SECTION_ASSOCIATION, ssaBody);
    }

    private void expireAssociation(boolean isExpired, Map<String, Object> body) {
        if (isExpired) {
            body.put(ParameterConstants.END_DATE, getBadDate());
        }
    }

    public void generateTeacherSchool(String teacherId, String edorgId) {
        Map<String, Object> tsaBody = new HashMap<String, Object>();
        tsaBody.put(ParameterConstants.TEACHER_ID, teacherId);
        tsaBody.put(ParameterConstants.SCHOOL_ID, edorgId);

        repo.create(EntityNames.TEACHER_SCHOOL_ASSOCIATION, tsaBody);
    }

    public void generateTSA(String teacherId, String sectionId, boolean isExpired) {
        Map<String, Object> tsaBody = new HashMap<String, Object>();
        tsaBody.put(ParameterConstants.TEACHER_ID, teacherId);
        tsaBody.put(ParameterConstants.SECTION_ID, sectionId);
        expireAssociation(isExpired, tsaBody);
        repo.create(EntityNames.TEACHER_SECTION_ASSOCIATION, tsaBody);
    }

    public Entity generateCohort(String edOrgId) {
        Map<String, Object> cohortBody = new HashMap<String, Object>();
        cohortBody.put("educationOrgId", edOrgId);

        return repo.create(EntityNames.COHORT, cohortBody);
    }

    public void generateStaffCohort(String teacherId, String cohortId, boolean isExpired, boolean studentAccess) {
        Map<String, Object> staffCohort = new HashMap<String, Object>();
        staffCohort.put(ParameterConstants.STAFF_ID, teacherId);
        staffCohort.put(ParameterConstants.COHORT_ID, cohortId);
        expireAssociation(isExpired, staffCohort);
        staffCohort.put(ParameterConstants.STUDENT_RECORD_ACCESS, studentAccess);

        repo.create(EntityNames.STAFF_COHORT_ASSOCIATION, staffCohort);

    }

    public String generateStudentAndStudentSchoolAssociation(String studentId, String schoolId, boolean isExpired) {
        Map<String, Object> student = new HashMap<String, Object>();
        student.put("studentUniqueStateId", studentId);
        student.put("sex", "Female");
        Map<String, Object> birthDate = new HashMap<String, Object>();
        birthDate.put("birthDate", "2003-04-03");
        student.put("birthData", birthDate);
        student.put("hispanicLatinoEthnicity", false);

        Map<String, List<Map<String, Object>>> denormalizations = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> schools = new ArrayList<Map<String, Object>>();
        Map<String, Object> school = new HashMap<String, Object>();
        school.put("_id", schoolId);
        school.put("entryDate", DateTime.now().minusDays(3).toString(DateTimeFormat.forPattern("yyyy-MM-dd")));
        school.put("entryGradeLevel", "Fifth grade");
        if (isExpired) {
            school.put("exitWithdrawDate", getBadDate());
        }
        school.put("edOrgs", Arrays.asList(schoolId));
        schools.add(school);
        denormalizations.put("schools", schools);
        student.put("denormalization", denormalizations);

        Entity entity = repo.create(EntityNames.STUDENT, student);
        String createdStudentId = entity.getEntityId();

        Map<String, Object> association = new HashMap<String, Object>();
        association.put(ParameterConstants.STUDENT_ID, createdStudentId);
        association.put(ParameterConstants.SCHOOL_ID, schoolId);
        association.put("entryDate", DateTime.now().minusDays(3).toString(DateTimeFormat.forPattern("yyyy-MM-dd")));
        association.put("entryGradeLevel", "Fifth grade");
        expireStudentSchoolAssociation(isExpired, association);
        repo.create(EntityNames.STUDENT_SCHOOL_ASSOCIATION, association);

        return createdStudentId;
    }

    private void expireStudentSchoolAssociation(boolean isExpired, Map<String, Object> body) {
        if (isExpired) {
            body.put("exitWithdrawDate", DateTime.now().minusDays(2).toString(DateTimeFormat.forPattern("yyyy-MM-dd")));
        }
    }

    public void generateStudentCohort(String studentId, String cohortId, boolean isExpired) {
        Map<String, Object> studentCohort = new HashMap<String, Object>();
        studentCohort.put(ParameterConstants.STUDENT_ID, studentId);
        studentCohort.put(ParameterConstants.COHORT_ID, cohortId);
        expireAssociation(isExpired, studentCohort);

        repo.create(EntityNames.STUDENT_COHORT_ASSOCIATION, studentCohort);
    }

    public void generateStudentProgram(String studentId, String programId, boolean isExpired) {
        Map<String, Object> studentProgram = new HashMap<String, Object>();
        studentProgram.put(ParameterConstants.STUDENT_ID, studentId);
        studentProgram.put(ParameterConstants.PROGRAM_ID, programId);
        expireAssociation(isExpired, studentProgram);

        repo.create(EntityNames.STUDENT_PROGRAM_ASSOCIATION, studentProgram);
    }

    public Entity generateEdorgWithProgram(List<String> programIds) {
        Map<String, Object> edorgBody = new HashMap<String, Object>();
        edorgBody.put(ParameterConstants.PROGRAM_REFERENCE, programIds);
        return repo.create(EntityNames.EDUCATION_ORGANIZATION, edorgBody);
    }

    public Entity generateProgram() {
        return repo.create(EntityNames.PROGRAM, new HashMap<String, Object>());
    }

    public void generateStaffProgram(String teacherId, String programId, boolean isExpired, boolean studentAccess) {
        Map<String, Object> staffProgram = new HashMap<String, Object>();
        staffProgram.put(ParameterConstants.STAFF_ID, teacherId);
        staffProgram.put(ParameterConstants.PROGRAM_ID, programId);
        expireAssociation(isExpired, staffProgram);
        staffProgram.put(ParameterConstants.STUDENT_RECORD_ACCESS, studentAccess);

        repo.create(EntityNames.STAFF_PROGRAM_ASSOCIATION, staffProgram);

    }

    public Entity generateAssessment() {
        return repo.create(EntityNames.ASSESSMENT, new HashMap<String, Object>());
    }

    public Entity generateLearningObjective() {
        return repo.create(EntityNames.LEARNING_OBJECTIVE, new HashMap<String, Object>());
    }

    public Entity generateLearningStandard() {
        return repo.create(EntityNames.LEARNING_STANDARD, new HashMap<String, Object>());
    }

}
