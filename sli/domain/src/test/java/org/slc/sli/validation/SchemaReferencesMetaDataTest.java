/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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


package org.slc.sli.validation;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import junit.framework.Assert;
import org.aspectj.apache.bcel.classfile.AnnotationDefault;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.slc.sli.validation.schema.SchemaReferencesMetaData;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/applicationContext-test.xml" })
public class SchemaReferencesMetaDataTest {
    @Autowired
    private SchemaReferencesMetaData schemaRefMetaData;

    public void aggregateForeignKeyReferences() {
        for(String type: schemaRefMetaData.getReferredEntityTypes()) {
            List<SchemaReferencePath> refs = schemaRefMetaData.getReferencesTo(type);
            //System.out.println(String.format("%30s, %-100s", type, refs));
        }
    }

    @Test
    public void testSchemaEntityReferences()
    {
        Assert.assertEquals(
                "Unexpected references found to assessment!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentAssessment.assessmentId", "assessment", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("objecti" +
                                        "veAssessment.assessmentId", "assessment", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("subdoc_studentObjectiveAssessment.objectiveAssessment.assessmentId", "assessment", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("assessmentItem.assessmentId", "assessment", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("subdoc_studentAssessmentItem.assessmentItem.assessmentId", "assessment", 0L, 1L,false,true, false) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("assessment"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to assessmentItem!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentAssessmentItem.assessmentItemId", "assessmentItem", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("assessmentItem"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to cohort!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("staffCohortAssociation.cohortId", "cohort", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCohortAssociation.cohortId", "cohort", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("cohort"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to course!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("courseOffering.courseId", "course", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("courseTranscript.courseId", "course", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("course"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to courseOffering!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("section.courseOfferingId", "courseOffering", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("courseOffering"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to disciplineIncident!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentDisciplineIncidentAssociation.disciplineIncidentId", "disciplineIncident", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("disciplineIncident"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to educationOrganization!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentCompetencyObjective.educationOrganizationId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("gradingPeriod.gradingPeriodIdentity.schoolId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("gradingPeriodIdentityType.schoolId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("calendarDate.educationOrganizationId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("school.parentEducationAgencyReference", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("localEducationAgency.parentEducationAgencyReference", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("localEducationAgency.localEducationAgencyReference", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("localEducationAgency.educationServiceCenterReference", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("course.schoolId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("staffEducationOrganizationAssociation.educationOrganizationReference", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("session.schoolId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("postSecondaryEvent.institutionId", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("stateEducationAgency.parentEducationAgencyReference", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("studentSpecialEdProgramAssociation.educationOrganizationId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("graduationPlan.educationOrganizationId", "educationOrganization", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("cohort.educationOrgId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentProgramAssociation.educationOrganizationId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCTEProgramAssociation.educationOrganizationId", "educationOrganization", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("educationOrganization.parentEducationAgencyReference", "educationOrganization", 0L, 1L,false,true, false) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("educationOrganization"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to gradebookEntry!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentGradebookEntry.gradebookEntryId", "gradebookEntry", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("gradebookEntry"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to gradingPeriod!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("grade.gradingPeriodId", "gradingPeriod", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("gradebookEntry.gradingPeriodId", "gradingPeriod", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("reportCard.gradingPeriodId", "gradingPeriod", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("gradingPeriod"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to graduationPlan!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentSchoolAssociation.graduationPlanId", "graduationPlan", 0L, 1L,false,true, false) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("graduationPlan"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to learningObjective!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentCompetency.objectiveId.learningObjectiveId", "learningObjective", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("learningObjective.parentLearningObjective", "learningObjective", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("studentCompetencyObjectiveReference.learningObjectiveId", "learningObjective", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("learningObjective"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to objectiveAssessment!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentObjectiveAssessment.objectiveAssessmentId", "objectiveAssessment", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("objectiveAssessment"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to parent!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentParentAssociation.parentId", "parent", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("parent"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to program!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("staffProgramAssociation.programId", "program", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentSpecialEdProgramAssociation.programId", "program", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentProgramAssociation.programId", "program", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCTEProgramAssociation.programId", "program", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("program"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to school!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("attendance.schoolId", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("courseOffering.schoolId", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("disciplineIncident.schoolId", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentSchoolAssociation.schoolId", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("disciplineAction.responsibilitySchoolId", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("disciplineAction.assignmentSchoolId", "school", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("teacherSchoolAssociation.schoolId", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("restraintEvent.SchoolReference", "school", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("section.schoolId", "school", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("school"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to section!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("grade.sectionId", "section", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("gradebookEntry.sectionId", "section", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentGradebookEntry.sectionId", "section", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentSectionAssociation.sectionId", "section", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("teacherSectionAssociation.sectionId", "section", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("section"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to session!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("courseOffering.sessionId", "session", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentAcademicRecord.sessionId", "session", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("section.sessionId", "session", 0L, 1L,false,true, false) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("session"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to staff!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("disciplineIncident.staffId", "staff", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("staffProgramAssociation.staffId", "staff", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("staffCohortAssociation.staffId", "staff", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("staffEducationOrganizationAssociation.staffReference", "staff", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("staff"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to student!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("attendance.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentSchoolAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("grade.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentAcademicRecord.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("reportCard.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentAssessment.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentGradebookEntry.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentSectionAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("courseTranscript.studentId", "student", 0L, 1L,false,true, false) ,
                                new SchemaReferencePath("studentDisciplineIncidentAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCohortAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("postSecondaryEvent.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("restraintEvent.studentReference", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentParentAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentSpecialEdProgramAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentProgramAssociation.studentId", "student", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCTEProgramAssociation.studentId", "student", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("student"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to studentAcademicRecord!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("courseTranscript.studentAcademicRecordId", "studentAcademicRecord", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("studentAcademicRecord"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to studentAssessment!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentObjectiveAssessment.studentAssessmentId", "studentAssessment", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentAssessmentItem.studentAssessmentId", "studentAssessment", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("studentAssessment"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to studentCompetencyObjective!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("studentCompetency.objectiveId.studentCompetencyObjectiveId", "studentCompetencyObjective", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCompetencyObjectiveReference.studentCompetencyObjectiveId", "studentCompetencyObjective", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("studentCompetencyObjective"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to studentSectionAssociation!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("grade.studentSectionAssociationId", "studentSectionAssociation", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentGradebookEntry.studentSectionAssociationId", "studentSectionAssociation", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("studentCompetency.studentSectionAssociationId", "studentSectionAssociation", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("studentSectionAssociation"))).size()
        );

        Assert.assertEquals(
                "Unexpected references found to teacher!",
                0,
                Sets.symmetricDifference(
                        Sets.newHashSet(
                                new SchemaReferencePath("teacherSchoolAssociation.teacherId", "teacher", 1L, 1L,false,false, true) ,
                                new SchemaReferencePath("teacherSectionAssociation.teacherId", "teacher", 1L, 1L,false,false, true) ),
                        Sets.newHashSet(schemaRefMetaData.getReferencesTo("teacher"))).size()
        );

    }
}
