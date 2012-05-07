package org.slc.sli.ingestion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Conducts the ingestion job
 * 
 * @author dkornishev
 * 
 */
public enum EdfiEntity {
    SELF("self",Collections.<EdfiEntity>emptyList()),

    ASSESSMENT_FAMILY ("assessmentFamily",Arrays.asList (SELF)),
    CALENDAR_DATE ("calendarDate",Collections.<EdfiEntity>emptyList()),
    CLASS_PERIOD ("classPeriod",Collections.<EdfiEntity>emptyList()),
    GRADUATION_PLAN ("graduationPlan",Collections.<EdfiEntity>emptyList()),
    LEARNING_STANDARD ("learningStandard",Collections.<EdfiEntity>emptyList()),
    LOCATION ("location",Collections.<EdfiEntity>emptyList()),
    PARENT ("parent",Collections.<EdfiEntity>emptyList()),
    PROGRAM ("program",Collections.<EdfiEntity>emptyList()),
    STAFF ("staff",Collections.<EdfiEntity>emptyList()),
    STUDENT ("student",Collections.<EdfiEntity>emptyList()),
    TEACHER ("teacher",Collections.<EdfiEntity>emptyList()),

    
    ACADEMIC_WEEK ("academicWeek",Arrays.asList (EdfiEntity.CALENDAR_DATE)),
    GRADING_PERIOD ("gradingPeriod",Arrays.asList (CALENDAR_DATE)),
    LEARNING_OBJECTIVE ("learningObjective",Arrays.asList (LEARNING_STANDARD,SELF)),
    STATE_EDUCATION_AGENCY ("stateEducationAgency",Arrays.asList (PROGRAM,SELF)),
    EDUCATION_SERVICE_CENTER ("educationServiceCenter",Arrays.asList (STATE_EDUCATION_AGENCY,PROGRAM,SELF)),
    LOCAL_EDUCATION_AGENCY ("localEducationAgency",Arrays.asList (STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,PROGRAM,SELF)),
    SCHOOL ("school",Arrays.asList (LOCAL_EDUCATION_AGENCY,LOCATION,CLASS_PERIOD,SELF)),
    DIPLOMA ("diploma",Arrays.asList (SCHOOL)),
    STUDENT_PARENT_ASSOCIATION ("studentParentAssociation",Arrays.asList (STUDENT,PARENT)),
    STUDENT_PROGRAM_ASSOCATION ("studentProgramAssocation",Arrays.asList (STUDENT,PROGRAM)),
    STAFF_PROGRAM_ASSOCATION ("staffProgramAssocation",Arrays.asList (STAFF,PROGRAM)),
    ASSESSMENT_ITEM ("assessmentItem",Arrays.asList (LEARNING_STANDARD)),


    BEHAVIOR_DESCRIPTOR ("behaviorDescriptor",Arrays.asList (STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    COHORT ("Cohort",Arrays.asList (PROGRAM,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    COURSE ("course",Arrays.asList (LEARNING_OBJECTIVE,LEARNING_STANDARD,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    DISCIPLINE_INCIDENT ("disciplineIncident",Arrays.asList (SCHOOL,STAFF)),
    OBJECTIVE_ASSESSMENT ("objectiveAssessment",Arrays.asList (LEARNING_OBJECTIVE,LEARNING_STANDARD,SELF)),
    SESSION ("session",Arrays.asList (GRADING_PERIOD,CALENDAR_DATE,ACADEMIC_WEEK,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    STAFF_EDUCATION_ORG_ASSIGNMENT_ASSOCATION ("staffEducationOrgAssignmentAssocation",Arrays.asList (STAFF,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    STUDENT_COMPETENCY_OBJECTIVE ("studentCompetencyObjective",Arrays.asList(STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    STUDENT_SCHOOL_ASSOCIATION ("studentSchoolAssociation",Arrays.asList (STUDENT,SCHOOL,GRADUATION_PLAN)),
    TEACHER_SCHOOL_ASSOCIATION ("teacherSchoolAssociation",Arrays.asList (TEACHER,SCHOOL)),

    
    COURSE_OFFERING ("courseOffering",Arrays.asList (SCHOOL,SESSION,COURSE)),
    DISCIPLINE_ACTION ("disciplineAction",Arrays.asList (STUDENT,DISCIPLINE_INCIDENT,STAFF,SCHOOL)),
    STUDENT_DISCIPLINE_INCIDENT_ASSOCIATION ("studentDisciplineIncidentAssociation",Arrays.asList (STUDENT,DISCIPLINE_INCIDENT)),

    SECTION ("section",Arrays.asList (COURSE_OFFERING,SCHOOL,SESSION,LOCATION,CLASS_PERIOD,PROGRAM)),

    STUDENT_SECTION_ASSOCIATION ("studentSectionAssociation",Arrays.asList (STUDENT,SECTION)),
    ASSESSMENT ("assessment",Arrays.asList (ASSESSMENT_ITEM,OBJECTIVE_ASSESSMENT,ASSESSMENT_FAMILY,SECTION)),
    ATTENDANCE_EVENT ("attendanceEvent",Arrays.asList (STUDENT,SECTION)),
    TEACHER_SECTION_ASSOCIATION ("teacherSectionAssociation",Arrays.asList (TEACHER,SECTION)),
    STUDENT_COMPETENCY ("studentCompetency",Arrays.asList (LEARNING_OBJECTIVE,STUDENT_COMPETENCY_OBJECTIVE,STUDENT_SECTION_ASSOCIATION)),
    
    GRADE ("grade",Arrays.asList (GRADING_PERIOD,STUDENT_SECTION_ASSOCIATION)),
    REPORT_CARD ("reportCard",Arrays.asList (GRADE,STUDENT_COMPETENCY,STUDENT,GRADING_PERIOD)),
    STUDENT_ASSESSMENT ("studentAssessment",Arrays.asList (STUDENT,ASSESSMENT)),

    STUDENT_ACADEMIC_RECORD ("studentAcademicRecord",Arrays.asList (STUDENT,SESSION,REPORT_CARD,DIPLOMA)),
    STUDENT_OBJECTIVE_ASSESSMENT ("studentObjectiveAssessment",Arrays.asList (OBJECTIVE_ASSESSMENT,STUDENT_ASSESSMENT)),
    
    COURSE_TRANSCRIPT ("courseTranscript",Arrays.asList (STUDENT_ACADEMIC_RECORD,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    DISCIPLINE_DESCRIPTOR ("disciplineDescriptor",Arrays.asList (STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    FEEDER_SCHOOL_ASSOCIATION ("feederSchoolAssociation",Arrays.asList (STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    OPEN_STAFF_POSITION ("openStaffPosition",Arrays.asList (STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    
    BELL_SCHEDULE ("bellSchedule",Collections.<EdfiEntity>emptyList()),
    COMPETENCY_LEVEL_DESCRIPTOR ("competencyLevelDescriptor",Collections.<EdfiEntity>emptyList()),
    CREDENTIAL_FIELD_DESCRIPTOR ("credentialFieldDescriptor",Collections.<EdfiEntity>emptyList()),
    PERFORMANCE_LEVEL_DESCRIPTOR ("performanceLevelDescriptor",Collections.<EdfiEntity>emptyList()),
    SERVICE_DESCRIPTOR ("serviceDescriptor",Collections.<EdfiEntity>emptyList()),

    GRADEBOOK_ENTRY ("gradebookEntry",Arrays.asList (LEARNING_OBJECTIVE,LEARNING_STANDARD,SECTION,GRADING_PERIOD)),
    LEAVE_EVENT ("leaveEvent",Arrays.asList (STAFF)),
    MEETING_TIME ("meetingTime",Arrays.asList (CLASS_PERIOD)),
 
    RESTRAINT_EVENT ("restraintEvent",Arrays.asList (STUDENT,SCHOOL,PROGRAM)),
    STAFF_COHORT_ASSOCIATION ("StaffCohortAssociation",Arrays.asList (STAFF,COHORT)),
    STUDENT_COHORT_ASSOCIATION ("StudentCohortAssociation",Arrays.asList (STUDENT,COHORT)),
    STAFF_EDUCATION_ORG_EMPLOYMENT_ASSOCIATION ("staffEducationOrgEmploymentAssociation",Arrays.asList (STAFF,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    STUDENT_GRADEBOOK_ENTRY ("studentGradebookEntry",Arrays.asList (STUDENT_SECTION_ASSOCIATION)),
    STUDENT_ASSESSMENT_ITEM ("studentAssessmentItem",Arrays.asList (STUDENT_OBJECTIVE_ASSESSMENT,SELF)),

    STUDENT_CTE_PROGRAM_ASSOCIATION ("studentCTEProgramAssociation",Arrays.asList (STUDENT,PROGRAM,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    STUDENT_SPECIAL_ED_PROGRAM_ASSOCIATION ("studentSpecialEdProgramAssociation",Arrays.asList (STUDENT,PROGRAM,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL)),
    STUDENT_TITLE_PART_A_PROGRAM_ASSOCIATION ("studentTitlePartAProgramAssociation",Arrays.asList (STUDENT,PROGRAM,STATE_EDUCATION_AGENCY,EDUCATION_SERVICE_CENTER,LOCAL_EDUCATION_AGENCY,SCHOOL));

    //*************************************************************************************
    private final String entityName;
    private final List<EdfiEntity> neededEntities;
    
    private EdfiEntity(String entityName, List<EdfiEntity> needs) {
        this.entityName = entityName;
        this.neededEntities = needs;
    }
    
    public String getEntityName() {
        return entityName;
    }
    
    public List<EdfiEntity> getNeededEntities() {
        return neededEntities;
    }
    
}
