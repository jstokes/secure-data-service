@RALLY_US0025
@RALLY_US149
@RALLY_US0609
@RALLY_US1689
@RALLY_US1690
@RALLY_US1691
@RALLY_US1804
@RALLY_US1876
@RALLY_US1889
@RALLY_US1929
@RALLY_US1964
@RALLY_US2033
@RALLY_US2081
@RALLY_DE85
@RALLY_DE87
@RALLY_DE621
@RALLY_US3122
@RALLY_US3202
@RALLY_US3200
@RALLY_US4162
@RALLY_US4136
@RALLY_US4080
@RALLY_US4116
@RALLY_DE1934
@RALLY_US4391
Feature: Acceptance Storied Data Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store

@smoke @integration @IL-Daybreak
Scenario: Post a zip file containing all data for Illinois Daybreak as a payload of the ingestion job: Clean Database
Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And I post "StoriedDataSet_IL_Daybreak.zip" file as the payload of the ingestion job
    And the following collections are empty in datastore:
        | collectionName                        |
        | assessment                            |
        | attendance                            |
        | calendarDate                          |
        | cohort                                |
        | competencyLevelDescriptor             |
        | course                                |
        | courseOffering                        |
        | disciplineAction                      |
        | disciplineIncident                    |
        | educationOrganization                 |
        | grade                                 |
        | gradebookEntry                        |
        | gradingPeriod                         |
        | graduationPlan                        |
        | learningObjective                     |
        | learningStandard                      |
        | parent                                |
        | program                               |
        | reportCard                            |
        | section                               |
        | session                               |
        | staff                                 |
        | staffCohortAssociation                |
        | staffEducationOrganizationAssociation |
        | staffProgramAssociation               |
        | student                               |
        | studentAcademicRecord                 |
        | studentAssessmentAssociation          |
        | studentCohortAssociation              |
        | studentCompetency                     |
        | studentCompetencyObjective            |
        | studentDisciplineIncidentAssociation  |
        | studentGradebookEntry                 |
        | studentParentAssociation              |
        | studentProgramAssociation             |
        | studentSchoolAssociation              |
        | studentSectionAssociation             |
        | courseTranscript                      |
        | teacherSchoolAssociation              |
        | teacherSectionAssociation             |
  When zip file is scp to ingestion landing zone
  And a batch job for file "StoriedDataSet_IL_Daybreak.zip" is completed in database

Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | assessment                  | 19    |
        | attendance                  | 75    |
        | calendarDate                | 556   |
        | cohort                      | 3     |
        | competencyLevelDescriptor   | 6     |
        | course                      | 95    |
        | courseOffering              | 95    |
        | disciplineAction            | 2     |
        | disciplineIncident          | 2     |
        | educationOrganization       | 5     |
        | grade                       | 4     |
        | gradebookEntry              | 12    |
        | gradingPeriod               | 17    |
        | graduationPlan              | 4     |
        | learningObjective           | 198   |
        | learningStandard            | 1499  |
        | parent                      | 9     |
        | program                     | 2     |
        | reportCard                  | 2     |
        | section                     | 97    |
        | session                     | 22    |
        | staff                       | 14    |
        | staffCohortAssociation      | 3     |
        | staffEducationOrganizationAssociation| 10 |
        | staffProgramAssociation     | 7     |
        | student                     | 78    |
        | studentAcademicRecord       | 117   |
        | studentAssessmentAssociation| 203   |
        | studentCohortAssociation    | 6     |
        | studentCompetency           | 59    |
        | studentCompetencyObjective  | 4     |
        | studentDisciplineIncidentAssociation| 4|
        | studentGradebookEntry       | 315   |
        | studentParentAssociation    | 9     |
        | studentProgramAssociation   | 6     |
        | studentSchoolAssociation    | 167   |
        | studentSectionAssociation   | 297   |
        | courseTranscript            | 196   |
        | teacherSchoolAssociation    | 3     |
        | teacherSectionAssociation   | 11    |
    And I check to find if record is in collection:
       | collectionName              | expectedRecordCount | searchParameter          | searchValue                | searchType           |
       | assessment                  | 1                   | body.assessmentItem.0.correctResponse          | False            | string  |
       | assessment                  | 1                   | body.assessmentItem.0.identificationCode       | AssessmentItem-1 | string  |
       | assessment                  | 1                   | body.assessmentItem.0.itemCategory             | True-False       | string  |
       | assessment                  | 1                   | body.assessmentItem.0.maxRawScore              | 5                | integer |
       | assessment                  | 1                   | body.assessmentItem.1.correctResponse          | True             | string  |
       | assessment                  | 1                   | body.assessmentItem.1.identificationCode       | AssessmentItem-2 | string  |
       | assessment                  | 1                   | body.assessmentItem.1.itemCategory             | True-False       | string  |
       | assessment                  | 1                   | body.assessmentItem.1.maxRawScore              | 5                | integer |
       | assessment                  | 1                   | body.assessmentItem.2.correctResponse          | True             | string  |
       | assessment                  | 1                   | body.assessmentItem.2.identificationCode       | AssessmentItem-3 | string  |
       | assessment                  | 1                   | body.assessmentItem.2.itemCategory             | True-False       | string  |
       | assessment                  | 1                   | body.assessmentItem.2.maxRawScore              | 5                | integer |
       | assessment                  | 1                   | body.assessmentItem.3.correctResponse          | False            | string  |
       | assessment                  | 1                   | body.assessmentItem.3.identificationCode       | AssessmentItem-4 | string  |
       | assessment                  | 1                   | body.assessmentItem.3.itemCategory             | True-False       | string  |
       | assessment                  | 1                   | body.assessmentItem.3.maxRawScore              | 5                | integer |
       | attendance                  | 11                  | body.schoolYearAttendance.attendanceEvent.event | Tardy         | string     |
       | attendance                  | 75                  | body.schoolYearAttendance.attendanceEvent.event | In Attendance | string     |
       | attendance                  | 75                  | body.schoolYearAttendance.schoolYear            | 2011-2012     | string     |
       | cohort                      | 1                   | body.cohortIdentifier      | ACC-TEST-COH-1             | string               |
       | cohort                      | 1                   | body.cohortIdentifier      | ACC-TEST-COH-2             | string               |
       | cohort                      | 1                   | body.cohortIdentifier      | ACC-TEST-COH-3             | string               |
       | course                      | 1                   | body.courseTitle     | 1st Grade Homeroom         | string               |
       | disciplineAction            | 1                   | body.disciplineDate      | 2011-03-04                 | string               |
       | disciplineAction            | 1                   | body.disciplineDate      | 2011-04-04                 | string               |
       | disciplineIncident          | 1                   | body.incidentIdentifier  | Disruption                 | string               |
       | disciplineIncident          | 1                   | body.incidentIdentifier  | Tardiness                  | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId      | IL                         | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId      | IL-DAYBREAK                | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId      | South Daybreak Elementary  | string               |
       | graduationPlan              | 1                   | metaData.externalId                            | GP-ADVANCED      | string  |
       | graduationPlan              | 1                   | metaData.externalId                            | GP-MINIMUM       | string  |
       | graduationPlan              | 1                   | metaData.externalId                            | GP-STANDARD      | string  |
       | graduationPlan              | 3                   | body.educationOrganizationId                   | 1b223f577827204a1c7e9c851dba06bea6b031fe_id | string  |
       | graduationPlan              | 2                   | body.graduationPlanType                        | Minimum                                     | string  |
       | program                     | 1                   | body.programId      | ACC-TEST-PROG-1            | string               |
       | program                     | 1                   | body.programId      | ACC-TEST-PROG-2            | string               |
       | staff                       | 1                   | body.staffUniqueStateId        | cgray                      | string               |
       | staff                       | 1                   | body.staffUniqueStateId  | rbraverman                 | string               |
       | staff                       | 2                   | body.name.verification   | Drivers license            | string               |
       | staff                       | 2                   | body.race                | White                      | string               |
       | student                     | 1                   | body.studentUniqueStateId      | 100000000                  | string               |
       | student                     | 1                   | body.studentUniqueStateId      | 800000012                  | string               |
       | student                     | 1                   | body.studentUniqueStateId      | 800000025                  | string               |
       | student                     | 1                   | body.studentUniqueStateId      | 900000024                  | string               |
       | studentAssessmentAssociation | 10                 | body.studentAssessmentItems.assessmentItemResult              | Incorrect           | string |
       | studentAssessmentAssociation | 10                 | body.studentAssessmentItems.assessmentResponse                | False               | string |
       | studentAssessmentAssociation | 24                 | body.studentAssessmentItems.assessmentItemResult              | Correct             | string |
       | studentAssessmentAssociation | 24                 | body.studentAssessmentItems.assessmentResponse                | True                | string |
       | studentAssessmentAssociation | 25                 | body.studentAssessmentItems.assessmentItem.identificationCode | AssessmentItem-3    | string |
       | studentAssessmentAssociation | 25                 | body.studentAssessmentItems.assessmentItem.identificationCode | AssessmentItem-4    | string |
       | studentParentAssociation     | 2                  | body.contactRestrictions                                      | NO CONTACT ALLOWED  | string |
       | studentParentAssociation     | 3                  | body.contactPriority                                          | 1                   | integer|
    And I should see "Processed 4261 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "InterchangeStudent.xml records considered: 78" in the resulting batch job file
    And I should see "InterchangeStudent.xml records ingested successfully: 78" in the resulting batch job file
    And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records considered: 108" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records ingested successfully: 108" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records considered: 595" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records ingested successfully: 595" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records considered: 192" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records ingested successfully: 192" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records considered: 45" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records ingested successfully: 45" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records considered: 496" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records ingested successfully: 496" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records considered: 709" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records ingested successfully: 709" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-READ2.xml records considered: 2" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-READ2.xml records ingested successfully: 2" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-READ2.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-StateTest.xml records considered: 2" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-StateTest.xml records ingested successfully: 2" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-StateTest.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-ACT.xml records considered: 1" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-ACT.xml records ingested successfully: 1" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-ACT.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-StateAssessments.xml records considered: 12" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-StateAssessments.xml records ingested successfully: 12" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-StateAssessments.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CCS-English.xml records considered: 1024" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CCS-English.xml records ingested successfully: 1024" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CCS-English.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CCS-Math.xml records considered: 574" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CCS-Math.xml records ingested successfully: 574" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CCS-Math.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim6thgrade.xml records considered: 2" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim6thgrade.xml records ingested successfully: 2" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim6thgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim7thgrade.xml records considered: 3" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim7thgrade.xml records ingested successfully: 3" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim7thgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim8thgrade.xml records considered: 112" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim8thgrade.xml records ingested successfully: 112" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Lkim8thgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman1stgrade.xml records considered: 4" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman1stgrade.xml records ingested successfully: 4" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman1stgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman3rdgrade.xml records considered: 3" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman3rdgrade.xml records ingested successfully: 3" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman3rdgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman4thgrade.xml records considered: 2" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman4thgrade.xml records ingested successfully: 2" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman4thgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman5thgrade.xml records considered: 2" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman5thgrade.xml records ingested successfully: 2" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Rbraverman5thgrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Cgray-ACT.xml records considered: 25" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Cgray-ACT.xml records ingested successfully: 25" in the resulting batch job file
    And I should see "InterchangeStudentAssessment-Cgray-ACT.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-ACT.xml records considered: 1" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-ACT.xml records ingested successfully: 1" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-ACT.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAttendance.xml records considered: 75" in the resulting batch job file
    And I should see "InterchangeAttendance.xml records ingested successfully: 75" in the resulting batch job file
    And I should see "InterchangeAttendance.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records considered: 18" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records ingested successfully: 18" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentProgram.xml records considered: 6" in the resulting batch job file
    And I should see "InterchangeStudentProgram.xml records ingested successfully: 6" in the resulting batch job file
    And I should see "InterchangeStudentProgram.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentCohort.xml records considered: 12" in the resulting batch job file
    And I should see "InterchangeStudentCohort.xml records ingested successfully: 12" in the resulting batch job file
    And I should see "InterchangeStudentCohort.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentDiscipline.xml records considered: 8" in the resulting batch job file
    And I should see "InterchangeStudentDiscipline.xml records ingested successfully: 8" in the resulting batch job file
    And I should see "InterchangeStudentDiscipline.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CommonCore.xml records considered: 99" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CommonCore.xml records ingested successfully: 99" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata-CommonCore.xml records failed: 0" in the resulting batch job file

@smoke
Scenario: Check the collections: Clean Database
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue                             | searchType           |
     | session                     | 1                   | body.sessionName            | Spring 2010 East Daybreak Junior High   | string               |
     | session                     | 4                   | body.schoolYear             | 2009-2010                               | string               |
     | session                     | 13                  | body.term                   | Fall Semester                           | string               |
     | session                     | 3                   | body.beginDate              | 2011-09-06                              | string               |
     | session                     | 3                   | body.endDate                | 2011-05-16                              | string               |
     | session                     | 1                   | body.endDate                | 2002-12-16                              | string               |
     | session                     | 22                  | body.totalInstructionalDays | 75                                      | integer              |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | program                     | 1                   | body.programId              | ACC-TEST-PROG-1         | string               |
     | program                     | 1                   | body.programId              | ACC-TEST-PROG-2         | string               |
     | program                     | 0                   | body.programId              | ACC-TEST-PROG-3         | string               |
     | program                     | 1                   | body.programType            | Regular Education       | string               |
     | program                     | 1                   | body.programType            | Remedial Education      | string               |
     | program                     | 1                   | body.programSponsor         | State Education Agency  | string               |
     | program                     | 1                   | body.programSponsor         | Local Education Agency  | string               |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | cohort                      | 1                   | body.cohortIdentifier       | ACC-TEST-COH-1          | string               |
     | cohort                      | 1                   | body.cohortIdentifier       | ACC-TEST-COH-2          | string               |
     | cohort                      | 2                   | body.cohortScope            | District                | string               |
     | cohort                      | 1                   | body.cohortScope            | Statewide               | string               |
     | cohort                      | 1                   | body.academicSubject        | English                 | string               |
     | cohort                      | 1                   | body.academicSubject        | Mathematics             | string               |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | disciplineAction            | 1                   | body.disciplineDate         | 2011-03-04              | string               |
     | disciplineAction            | 0                   | body.disciplineDate         | 2011-05-04              | string               |
     | disciplineAction            | 2                   | body.disciplineActionLength | 74                      | integer              |
And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | studentProgramAssociation   | 3                   | body.beginDate              | 2011-01-01              | string               |
     | studentProgramAssociation   | 3                   | body.beginDate              | 2011-03-01              | string               |
     | studentProgramAssociation   | 1                   | body.endDate                | 2011-12-31              | string               |
     | studentProgramAssociation   | 1                   | body.endDate                | 2011-12-01              | string               |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | staffProgramAssociation     | 4                   | body.studentRecordAccess    | true                    | boolean              |
     | staffProgramAssociation     | 3                   | body.studentRecordAccess    | false                   | boolean              |
     | staffProgramAssociation     | 2                   | body.beginDate              | 2011-01-01              | string               |
     | staffProgramAssociation     | 4                   | body.beginDate              | 2011-01-05              | string               |
     | staffProgramAssociation     | 1                   | body.beginDate              | 2011-06-01              | string               |
     | staffProgramAssociation     | 4                   | body.endDate                | 2012-02-15              | string               |
And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | studentCohortAssociation    | 1                   | body.beginDate              | 2011-02-01              | string               |
     | studentCohortAssociation    | 1                   | body.beginDate              | 2011-04-01              | string               |
     | studentCohortAssociation    | 1                   | body.endDate                | 2012-01-15              | string               |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | disciplineIncident          | 1                   | body.incidentIdentifier     | Disruption              | string               |
     | disciplineIncident          | 1                   | body.incidentIdentifier     | Tardiness               | string               |
     | disciplineIncident          | 2                   | body.incidentLocation       | On School               | string               |
     | disciplineIncident          | 1                   | body.incidentDate           | 2011-02-01              | string               |
 And I check to find if record is in collection:
     | collectionName    | expectedRecordCount   | searchParameter              | searchValue              | searchType            |
     | gradebookEntry    | 2                     | body.dateAssigned            | 2011-09-15               | string                |
     | gradebookEntry    | 3                     | body.dateAssigned            | 2011-09-29               | string                |
     | gradebookEntry    | 3                     | body.dateAssigned            | 2011-10-13               | string                |
     | gradebookEntry    | 2                     | body.dateAssigned            | 2011-10-27               | string                |
     | gradebookEntry    | 1                     | body.gradebookEntryType      | Unit test                | string                |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | staffCohortAssociation      | 3                   | body.studentRecordAccess    | true                    | boolean              |
     | staffCohortAssociation      | 1                   | body.beginDate              | 2011-01-01              | string               |
     | staffCohortAssociation      | 1                   | body.beginDate              | 2011-07-01              | string               |
     | staffCohortAssociation      | 1                   | body.endDate                | 2012-02-15              | string               |
And I check to find if record is in collection:
     | collectionName                          | expectedRecordCount | searchParameter                     | searchValue          | searchType           |
     | studentDisciplineIncidentAssociation    | 2                   | body.studentParticipationCode       | Perpetrator          | string               |
     | studentDisciplineIncidentAssociation    | 1                   | body.studentParticipationCode       | Witness              | string               |
     | studentDisciplineIncidentAssociation    | 1                   | body.studentParticipationCode       | Victim               | string               |
 And I check to find if record is in collection:
       | collectionName                | expectedRecordCount | searchParameter                       | searchValue             | searchType           |
       | courseTranscript              | 196                 | body.courseAttemptResult              | Pass                    | string               |
       | courseTranscript              | 10                  | body.finalNumericGradeEarned          | 90                      | integer              |
       | courseTranscript              | 4                   | body.finalNumericGradeEarned          | 87                      | integer              |
       | courseTranscript              | 2                   | body.finalNumericGradeEarned          | 82                      | integer              |
       | courseTranscript              | 33                  | body.finalLetterGradeEarned           | B                       | string               |
       | courseTranscript              | 60                  | body.gradeLevelWhenTaken              | Tenth grade             | string               |
       | studentAcademicRecord         | 100                 | body.cumulativeCreditsAttempted.credit| 5                       | integer              |
And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                              | searchValue      |  searchType           |
     | assessment                  | 1                   | body.assessmentItem.identificationCode       | AssessmentItem-1 |   string              |
     | assessment                  | 1                   | body.assessmentItem.identificationCode       | AssessmentItem-2 |   string              |
     | assessment                  | 1                   | body.assessmentItem.identificationCode       | AssessmentItem-3 |   string              |
     | assessment                  | 1                   | body.assessmentItem.identificationCode       | AssessmentItem-4 |   string              |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue              | searchType           |
     | courseOffering              | 1                   | body.localCourseCode        | 3rd Grade Homeroom       | string               |
     | courseOffering              | 1                   | body.localCourseCode        | Government-4             | string               |
And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                                 | searchValue   | searchType           |
     | attendance                  | 0                   | body.schoolYearAttendance.attendanceEvent.date  | 2011-09-01    | string               |
     | attendance                  | 75                  | body.schoolYearAttendance.attendanceEvent.date  | 2011-11-10    | string               |
 And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                                  | searchValue                                      |searchType           |
     | assessment                  | 1                   | body.assessmentFamilyHierarchyName               | AP.AP Eng.AP-Eng-and-Literature                  |string               |
     | assessment                  | 1                   | body.assessmentFamilyHierarchyName               | AP.AP Eng.AP-Lang-and-Literature                 |string               |
     | studentAssessmentAssociation| 0                   | body.performanceLevelDescriptors.0.1.description | Extremely well qualified                         |string               |
#    | studentSchoolAssociation     | 7                   | body.classOf                                     | 2011-2012    |
And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                                                     | searchValue                                      |searchType           |
     | assessment                  | 1                   | body.assessmentFamilyHierarchyName                                  | ACT                                              |string               |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode                         | ACT-English                                      |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-English-Usage                                |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-English-Rhetorical                           |string               |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode                         | ACT-Mathematics                                  |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-Math-Pre-Algebra                             |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-Math-Algebra                                 |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-Math-Plane-Geometry                          |string               |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode                         | ACT-Reading                                      |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-Reading-SocialStudies                        |string               |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | ACT-Reading-Arts                                 |string               |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode                         | ACT-Science                                      |string               |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode                         | ACT-Writing                                      |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode                        | ACT-English                  |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.objectiveAssessment.objectiveAssessments.0.identificationCode | ACT-English-Usage            |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.objectiveAssessment.objectiveAssessments.1.identificationCode | ACT-English-Rhetorical       |string               |
     | studentAssessmentAssociation| 12                  | body.studentObjectiveAssessments.scoreResults.0.result                                         | 15                           |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.scoreResults.0.assessmentReportingMethod                      | Scale score                  |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode                        | ACT-English-Usage            |string               |
     | studentAssessmentAssociation| 6                   | body.studentObjectiveAssessments.scoreResults.0.result                                         | 10                           |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.scoreResults.0.assessmentReportingMethod                      | Scale score                  |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode                        | ACT-English-Rhetorical       |string               |
     | studentAssessmentAssociation| 9                   | body.studentObjectiveAssessments.scoreResults.0.result                                         | 8                            |string               |
     | studentAssessmentAssociation| 25                  | body.studentObjectiveAssessments.scoreResults.0.assessmentReportingMethod                      | Scale score                  |string               |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter               | searchValue     |searchType           |
     | parent                      | 1                   | body.parentUniqueStateId      | 9870036500      |string               |
     | parent                      | 1                   | body.parentUniqueStateId      | 6473283635      |string               |
     | parent                      | 1                   | body.parentUniqueStateId      | 0798132465      |string               |
     | parent                      | 1                   | body.parentUniqueStateId      | 3597672174      |string               |
   And I check to find if record is in collection:
     | collectionName                        | expectedRecordCount | searchParameter               | searchValue     |searchType           |
     | staffEducationOrganizationAssociation |          9          | body.beginDate                | 1967-08-13      | string              |
     | staffEducationOrganizationAssociation |          1          | body.beginDate                | 2000-01-01      | string              |
   And I check to find if record is in collection:
     #check to make sure we're actually resolving references
     | collectionName | expectedRecordCount | searchParameter | searchValue | searchType |
     | cohort                       | 1 | body.programId.0 | 983dd657325009aefa88a234fa18bdb1e11c82a8_id | string |
     | educationOrganization        | 2 | body.programReference.0 | a50802f02c7e771d979f7d5b3870c500014e6803_id | string |
     | educationOrganization        | 1 | body.programReference.0 | 983dd657325009aefa88a234fa18bdb1e11c82a8_id | string |
     | section                      | 1 | body.courseOfferingId | f987fc599bd78f69c57d4176163633bde1ffc3cb_id | string |
     | section                      | 1 | body.programReference.0 | a50802f02c7e771d979f7d5b3870c500014e6803_id | string |
     | staffProgramAssociation      | 3 | body.programId | a50802f02c7e771d979f7d5b3870c500014e6803_id | string|
     | studentAcademicRecord        | 1 | body.reportCards.0 | 0021f99fa3d1b5ff3231a9b75a8bb37e87af210c_id | string |
     | studentProgramAssociation    | 6 | body.programId | a50802f02c7e771d979f7d5b3870c500014e6803_id | string|
     | studentCompetency            | 1 | body.objectiveId.studentCompetencyObjectiveId | 028d7f8e25584d3353c9691e6aab89156029dde8_id | string |
 
@smoke
Scenario: Verify deterministic ids generated: Clean Database
  And I check that ids were generated properly:
    | collectionName                       | deterministicId                             | field                             | value                                |
    | competencyLevelDescriptor            | fb623d47656476ad67d8b698ee19d3a1932fd2ea_id | body.codeValue                    | Barely Competent 4                   |
    | educationOrganization                | b64ee2bcc92805cdd8ada6b7d8f9c643c9459831_id | body.stateOrganizationId  | IL                                   |
    | assessment                           | d50118aaad960b54a8b2afc7268d01d13842cb58_id | body.assessmentIdentificationCode.ID  | ACT                              |
    | educationOrganization                | 1b223f577827204a1c7e9c851dba06bea6b031fe_id | body.stateOrganizationId  | IL-DAYBREAK                          |
    | educationOrganization                | a13489364c2eb015c219172d561c62350f0453f3_id | body.stateOrganizationId  | Daybreak Central High                |
    | student                              | 067198fd6da91e1aa8d67e28e850f224d6851713_id | body.studentUniqueStateId         | 800000025                            |
    | staff                                | dfec28d34c75a4d307d1e85579e26a81630f6a47_id | body.staffUniqueStateId           | jstevenson                           |
    | staff                                | 6757c28005c30748f3bbda02882bf59bc81e0d71_id | body.staffUniqueStateId           | linda.kim                            |
    | cohort                               | e097d0f6e1e3d40d58930052eae2d7074eaa901a_id | body.cohortIdentifier     | ACC-TEST-COH-2                       |
    | cohort                               | e097d0f6e1e3d40d58930052eae2d7074eaa901a_id | body.educationOrgId       | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
    | studentCohortAssociation             | e097d0f6e1e3d40d58930052eae2d7074eaa901a_idbc542a3d675b570fe46b6fe54ec46cf9e7cb710c_id | body.studentId            | c20c4b37f887348b67a02091dc10ee6b27fbd1ce_id |
    | studentCohortAssociation             | e097d0f6e1e3d40d58930052eae2d7074eaa901a_idbc542a3d675b570fe46b6fe54ec46cf9e7cb710c_id | body.cohortId             | e097d0f6e1e3d40d58930052eae2d7074eaa901a_id |
    | studentCohortAssociation             | e097d0f6e1e3d40d58930052eae2d7074eaa901a_idbc542a3d675b570fe46b6fe54ec46cf9e7cb710c_id | body.beginDate            | 2011-04-01                           |
    | studentAssessmentAssociation         | 9b38ee8562b14f3201aff4995bac9bbafc3336a0_idfee2cbf123fe2305dd0741ea674742eef3b25386_id | body.studentId            | 9b38ee8562b14f3201aff4995bac9bbafc3336a0_id |
    | studentAssessmentAssociation         | 9b38ee8562b14f3201aff4995bac9bbafc3336a0_idfee2cbf123fe2305dd0741ea674742eef3b25386_id | body.assessmentId         | 8be1b9e5f8b4274b0e0fd49ffe0e199297e0cb30_id |
    | studentAssessmentAssociation         | 0f037add13a1b0590b9e7f19bd9edf8c38e0e1ac_id88754d3ae3a4166f26bd90e9d3f242f23b047121_id | body.assessmentId         | d50118aaad960b54a8b2afc7268d01d13842cb58_id |
    | studentAssessmentAssociation         | 9b38ee8562b14f3201aff4995bac9bbafc3336a0_idfee2cbf123fe2305dd0741ea674742eef3b25386_id | body.administrationDate   | 2011-10-01                           |
    | studentCompetency                    | a899667c35703b07c8005ff17abc4f2d0d7b4f21_id | body.competencyLevel.codeValue    | 777                                  |
    | studentCompetency                    | a899667c35703b07c8005ff17abc4f2d0d7b4f21_id | body.studentSectionAssociationId    | 5593b94891e8ba3f7005993e3847df6aaaa3a064_idc377c9c4b343dda726e837f442a171c570a460cd_id |
    | studentCompetency                    | a899667c35703b07c8005ff17abc4f2d0d7b4f21_id | body.objectiveId.learningObjectiveId    | 9e4b630c63a6f2e284de84aae8e9e1846b33bf1f_id                                  |
    | studentCompetencyObjective           | 028d7f8e25584d3353c9691e6aab89156029dde8_id | body.studentCompetencyObjectiveId | SCO-K-1                              |
    | course                               | a42a8a8deaaf4fa04448d602ea96c0e2f74c6521_id | body.uniqueCourseId  | State-History-II-G7-50 |
    | course                               | a42a8a8deaaf4fa04448d602ea96c0e2f74c6521_id | body.schoolId  | 772a61c687ee7ecd8e6d9ad3369f7883409f803b_id |
    | reportCard                           | 8f3a05e77f7d902f963b73b5ec072ced1583fbda_id | body.studentId | 067198fd6da91e1aa8d67e28e850f224d6851713_id                       |
    | reportCard                           | 8f3a05e77f7d902f963b73b5ec072ced1583fbda_id | body.gradingPeriodId | 5b68a0b672e485892f496987c05e9c32d95f7067_id                       |
# disciplineAction
    | disciplineAction                     | 70b8c1f4b77823bf5ede69389e13b0487f32e720_id | body.responsibilitySchoolId          | a13489364c2eb015c219172d561c62350f0453f3_id |
    | disciplineAction                     | 70b8c1f4b77823bf5ede69389e13b0487f32e720_id | body.disciplineActionIdentifier      | cap0-lea0-sch1-da0                   |
# disciplineIncident
    | disciplineIncident                   | 950c9f3ec3c8866d10794a7c053d7745c80f6b91_id | body.schoolId                        | a13489364c2eb015c219172d561c62350f0453f3_id |
    | disciplineIncident                   | 950c9f3ec3c8866d10794a7c053d7745c80f6b91_id | body.incidentIdentifier              | Disruption                           |
# grade
    | grade                                | 0e7ebb7c78f4447b9ece45cdde992153b9ecbb4b_id | body.studentSectionAssociationId     | 5593b94891e8ba3f7005993e3847df6aaaa3a064_idc377c9c4b343dda726e837f442a171c570a460cd_id |
    | grade                                | 0e7ebb7c78f4447b9ece45cdde992153b9ecbb4b_id | body.gradingPeriodId                 | c29dd49f05474ddc05a21a9bce9cb452ea783a98_id |
# gradebookEntry
    | gradebookEntry                       | 135963f2abd3320ae508546fbff31f37e10b949e_idbbfd4364e569b963aa25dbe015c5f09db96342cb_id | body.sectionId                       | 135963f2abd3320ae508546fbff31f37e10b949e_id |
    | gradebookEntry                       | 135963f2abd3320ae508546fbff31f37e10b949e_idbbfd4364e569b963aa25dbe015c5f09db96342cb_id | body.gradebookEntryType              | Unit test                                 |
    | gradebookEntry                       | 135963f2abd3320ae508546fbff31f37e10b949e_idbbfd4364e569b963aa25dbe015c5f09db96342cb_id | body.dateAssigned                    | 2011-10-13                           |
# studentAcademicRecord
    | studentAcademicRecord                | a1e159796736acfe35a3dda1ece214dc380a2714_id | body.studentId                       | 2474c3b2906eab72c1ee4b06a5c4ebf02d02aace_id |
    | studentAcademicRecord                | a1e159796736acfe35a3dda1ece214dc380a2714_id | body.sessionId                       | a2f899c4b31e2dc11a5a5ab202d4590bb0a33c8b_id |
# staffEducationOrganizationAssociation
    | staffEducationOrganizationAssociation | 5a000d037de00063995e84fdc3d0f91d9afb4b65_id | body.staffReference                 | e4320d0bef725998faa8579a987ada80f254e7be_id |
    | staffEducationOrganizationAssociation | 5a000d037de00063995e84fdc3d0f91d9afb4b65_id | body.educationOrganizationReference | b64ee2bcc92805cdd8ada6b7d8f9c643c9459831_id |
    | staffEducationOrganizationAssociation | 5a000d037de00063995e84fdc3d0f91d9afb4b65_id | body.staffClassification            | LEA System Administrator                       |
    | staffEducationOrganizationAssociation | 5a000d037de00063995e84fdc3d0f91d9afb4b65_id | body.beginDate                      | 1967-08-13                           |
     | studentDisciplineIncidentAssociation | e2449a1a6d0e37f388ce871d066a4705aabac16c_id | body.studentId              | 6578f984876bbf6f884c1be2ef415dbf4441db89_id |
     | studentDisciplineIncidentAssociation | e2449a1a6d0e37f388ce871d066a4705aabac16c_id | body.disciplineIncidentId    | 71c6e7baacd2d0367a04c056fa365a468dead7b4_id |
# staffProgramAssociation
    | staffProgramAssociation               | 1c0ea205ed43afc88096ce626f22bd07a30d2729_id | body.staffId                        | 948bd23862b59e1468aa5dfafbec95ea6570e0e4_id |
    | staffProgramAssociation               | 1c0ea205ed43afc88096ce626f22bd07a30d2729_id | body.programId                      | a50802f02c7e771d979f7d5b3870c500014e6803_id |
    | staffProgramAssociation               | 1c0ea205ed43afc88096ce626f22bd07a30d2729_id | body.beginDate                      | 2011-06-01                           |
# staffCohortAssociation
    | staffCohortAssociation               | 77d027fa7ebb00aac5b2887c9ffc2f1a19b8d8cd_id | body.staffId                        | c9302118115a8e2f01492914ea22c4176447b6b6_id |
    | staffCohortAssociation               | 77d027fa7ebb00aac5b2887c9ffc2f1a19b8d8cd_id | body.cohortId                       | e5c71e5eed5b9ded0b4df10ad97860a80cbd4d6c_id |
    | staffCohortAssociation               | 77d027fa7ebb00aac5b2887c9ffc2f1a19b8d8cd_id | body.beginDate                      | 2011-01-01                                  |
# teacherSchoolAssociation
    | teacherSchoolAssociation             | 68bd8fc5cd433b27d98b8b73dd94e8e0d932c22c_id | body.teacherId                      | b49545f9d443dfbf93358851c903a9923f6af4dd_id |
    | teacherSchoolAssociation             | 68bd8fc5cd433b27d98b8b73dd94e8e0d932c22c_id | body.programAssignment              | Regular Education                    |
    | teacherSchoolAssociation             | 68bd8fc5cd433b27d98b8b73dd94e8e0d932c22c_id | body.schoolId                       | a13489364c2eb015c219172d561c62350f0453f3_id |
# courseOffering
    | courseOffering                       | a6c96dcc34fc021f685b6d082c7759b070731f93_id | body.schoolId                       | 772a61c687ee7ecd8e6d9ad3369f7883409f803b_id |
    | courseOffering                       | a6c96dcc34fc021f685b6d082c7759b070731f93_id | body.sessionId                      | f1f768e7ec6f9936b2414372dcb046a3bca7ad93_id |
    | courseOffering                       | a6c96dcc34fc021f685b6d082c7759b070731f93_id | body.localCourseCode                | Pre-Algebra I                          |
   | courseTranscript                     | b40e7c315873a891873e4eb8b9036f47ac553d28_id | body.studentAcademicRecordId            | 1272719cf8247946b9ef689bf1860b27e7df7828_id                                 |
   | courseTranscript                     | b40e7c315873a891873e4eb8b9036f47ac553d28_id | body.courseId                | 28ef7ffd6361d977db1c8f66c461d4597913a16e_id                                 |
   | courseTranscript                     | b40e7c315873a891873e4eb8b9036f47ac553d28_id | body.courseAttemptResult            | Pass                                 |
   | studentParentAssociation             | 067198fd6da91e1aa8d67e28e850f224d6851713_id482360640e4db1dc0dd3755e699b25cfc9abf4a9_id | body.studentId            | 067198fd6da91e1aa8d67e28e850f224d6851713_id |
   | studentParentAssociation             | 067198fd6da91e1aa8d67e28e850f224d6851713_id482360640e4db1dc0dd3755e699b25cfc9abf4a9_id | body.parentId             | 93616529c9acb1f9a5a88b8bf735d8a4277d6f08_id |
   | studentSchoolAssociation             | b0fa95fe87c80a76598fdedd181cce8044c44f0f_id | body.studentId            | 0c93f4ca943a22e75b979fb468e7dc949c479bb9_id  |
   | studentSchoolAssociation             | b0fa95fe87c80a76598fdedd181cce8044c44f0f_id | body.schoolId            | 772a61c687ee7ecd8e6d9ad3369f7883409f803b_id |
   | studentSchoolAssociation             | b0fa95fe87c80a76598fdedd181cce8044c44f0f_id | body.entryDate            | 2011-09-01                              |
   | section                                | 84432d70656e1ab68df27cf2584282da351ab684_id | studentSectionAssociation.body.beginDate            | 2011-09-01                              |
   | section                                | 84432d70656e1ab68df27cf2584282da351ab684_id | studentSectionAssociation.body.studentId            | 6578f984876bbf6f884c1be2ef415dbf4441db89_id |
   | section                                | 84432d70656e1ab68df27cf2584282da351ab684_id | studentSectionAssociation.body.sectionId            | 84432d70656e1ab68df27cf2584282da351ab684_id |
   | section                                | 84432d70656e1ab68df27cf2584282da351ab684_id | studentSectionAssociation._id | 84432d70656e1ab68df27cf2584282da351ab684_id2f7176f215be612c37c2c1745ec01eba6cd9b87a_id  |
   | teacherSectionAssociation            | 135963f2abd3320ae508546fbff31f37e10b949e_id107eb8696c809b0bce7431b362b49c32a46ea72f_id | body.teacherId            | 6757c28005c30748f3bbda02882bf59bc81e0d71_id |
   | teacherSectionAssociation            | 135963f2abd3320ae508546fbff31f37e10b949e_id107eb8696c809b0bce7431b362b49c32a46ea72f_id | body.sectionId            | 135963f2abd3320ae508546fbff31f37e10b949e_id |
    | program                              | a50802f02c7e771d979f7d5b3870c500014e6803_id | body.programId            | ACC-TEST-PROG-1                      |
    | calendarDate                         | a4785ee1380871b68888ec317c39c9e8ef7e1346_id | body.date                 | 2010-10-13                           |
    | calendarDate                         | 356b451105c8cd5678f69eb7c3dce42d5ef4c873_id | body.date                 | 2010-10-14                           |
    | studentProgramAssociation            | a50802f02c7e771d979f7d5b3870c500014e6803_id98ae5d5377bee52764848bb05f5284ba72ef65e2_id | body.studentId            | c20c4b37f887348b67a02091dc10ee6b27fbd1ce_id |
    | studentProgramAssociation            | a50802f02c7e771d979f7d5b3870c500014e6803_idfbdb2bd12da6fa64d2e74242c20c2235cd3f04d4_id | body.programId            | a50802f02c7e771d979f7d5b3870c500014e6803_id |
    | studentProgramAssociation            | a50802f02c7e771d979f7d5b3870c500014e6803_idcf81759eafe33b0f1280caa1ea1922fc578ef9c7_id | body.educationOrganizationId | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
    | parent                               | aae71d23ffacfef68aa2eaa357c7259445daa0fe_id | body.parentUniqueStateId  | 3597672174             |
    | section                              | 92451eba2195a4cffcb0b55fe6d6ac8b13faa9ad_id | body.uniqueSectionCode    | Drama I - Sec 5f09 |
    | section                              | 92451eba2195a4cffcb0b55fe6d6ac8b13faa9ad_id | body.schoolId             | 772a61c687ee7ecd8e6d9ad3369f7883409f803b_id |
    | gradingPeriod                        | a6c7aac9afe6bd86b0b8c8116caa8edb35e2a0ba_id | body.beginDate                           | 2012-03-05                           |
    | gradingPeriod                        | a6c7aac9afe6bd86b0b8c8116caa8edb35e2a0ba_id | body.gradingPeriodIdentity.gradingPeriod | Fifth Six Weeks                      |
    | gradingPeriod                        | a6c7aac9afe6bd86b0b8c8116caa8edb35e2a0ba_id | body.gradingPeriodIdentity.schoolId      | 352e8570bd1116d11a72755b987902440045d346_id |
    | gradingPeriod                        | a6c7aac9afe6bd86b0b8c8116caa8edb35e2a0ba_id | body.calendarDateReference		        | 085e5a5fcc6c175e66eed7b8edcc2ed1b3b38ba0_id |
# session
    | session                              | 1e217f65c48cda4f5009cb1518cb33ddd51637e0_id | body.sessionName                     | Fall 2007 South Daybreak Elementary    |
    | session                              | 1e217f65c48cda4f5009cb1518cb33ddd51637e0_id | body.schoolId                        | 352e8570bd1116d11a72755b987902440045d346_id |
    | attendance                           | 0e4cf9728e804e6ab0c09432d58e3f5bdd3622c1_id | body.studentId                       | 366e15c0213a81f653cdcf524606edeed3f80f99_id |
    | attendance                           | 0e4cf9728e804e6ab0c09432d58e3f5bdd3622c1_id | body.schoolId                        | a13489364c2eb015c219172d561c62350f0453f3_id |
    | graduationPlan | 7f5c42b2ff7edf0bfa0b877eab43df47985cd99c_id | body.educationOrganizationId | 1b223f577827204a1c7e9c851dba06bea6b031fe_id |
    | graduationPlan | 7f5c42b2ff7edf0bfa0b877eab43df47985cd99c_id | body.graduationPlanType      | Minimum       |
    | learningObjective                    | e7ca691a652808cedd4fc8abd1275c94f9679e56_id | body.objective                       | The Revolutionary Period |
    | learningObjective                    | e7ca691a652808cedd4fc8abd1275c94f9679e56_id | body.academicSubject                 | Social Studies |
    | learningObjective                    | e7ca691a652808cedd4fc8abd1275c94f9679e56_id | body.objectiveGradeLevel             | Third grade |
    | learningStandard                     | 84a2dbad54ca44b613728cdfbe92d2e9a3bbcd9f_id | body.learningStandardId.identificationCode | 9DB2617F615743cfA8D225346AC4CB4D |


@integration @IL-Sunset
Scenario: Post a zip file containing all data for Illinois Sunset as a payload of the ingestion job: Append Database
Given I am using preconfigured Ingestion Landing Zone for "Midgar-Sunset"
   And I post "StoriedDataSet_IL_Sunset.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
  And a batch job for file "StoriedDataSet_IL_Sunset.zip" is completed in database
Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | assessment                  | 19    |
        | attendance                  | 75    |
        | cohort                      | 3     |
        | course                      | 96    |
        | courseOffering              | 96    |
        | disciplineAction            | 2     |
        | disciplineIncident          | 2     |
        | educationOrganization       | 7     |
        | grade                       | 4     |
        | gradebookEntry              | 12    |
        | parent                      | 9     |
        | program                     | 2     |
        | reportCard                  | 2     |
        | section                     | 100   |
        | session                     | 23    |
        | staff                       | 21    |
        | staffCohortAssociation      | 3     |
        | staffEducationOrganizationAssociation| 16 |
        | staffProgramAssociation     | 7     |
        | student                     | 183   |
        | studentAssessmentAssociation| 203   |
        | studentCohortAssociation    | 6     |
        | studentDisciplineIncidentAssociation| 4|
        | studentGradebookEntry       | 315   |
        | studentParentAssociation    | 9     |
        | studentProgramAssociation   | 6     |
        | studentSchoolAssociation    | 272   |
        | studentSectionAssociation   | 402   |
        | courseTranscript            | 196   |
        | teacherSchoolAssociation    | 4     |
        | teacherSectionAssociation   | 14    |
    And I check to find if record is in collection:
       | collectionName              | expectedRecordCount | searchParameter          | searchValue                | searchType           |
       | student                     | 1                   | body.studentUniqueStateId      | 1000000000                 | string               |
       | staff                       | 1                   | body.staffUniqueStateId  | manthony                   | string               |
       | course                      | 1                   | body.courseTitle      | A.P. Calculus              | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId | Sunset Central High School | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId | IL-SUNSET                  | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId | IL                         | string               |
    And I should see "Processed 342 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "InterchangeStudent.xml records considered: 105" in the resulting batch job file
    And I should see "InterchangeStudent.xml records ingested successfully: 105" in the resulting batch job file
    And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records considered: 3" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records ingested successfully: 3" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records considered: 3" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records ingested successfully: 3" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records considered: 4" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records ingested successfully: 4" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records considered: 17" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records ingested successfully: 17" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records considered: 210" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records ingested successfully: 210" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records failed: 0" in the resulting batch job file


@integration @NY-NYC
Scenario: Post a zip file containing all data for New York as a payload of the ingestion job: Append Database
Given I am using preconfigured Ingestion Landing Zone for "Hyrule-NYC"
  And I post "StoriedDataSet_NY.zip" file as the payload of the ingestion job
      And the following collections are empty in datastore:
        | collectionName                      |
        | student                             |
        | studentSchoolAssociation            |
        | course                              |
        | educationOrganization               |
        | section                             |
        | studentSectionAssociation           |
        | staff                               |
        |staffEducationOrganizationAssociation|
        | teacherSchoolAssociation            |
        | teacherSectionAssociation           |
        | session                             |
        | assessment                          |
        | studentAssessmentAssociation        |
        | gradebookEntry                      |
        | courseTranscript                    |
        | studentGradebookEntry               |
        | parent                              |
        | studentParentAssociation            |
        | attendance                          |
        | program                             |
        | staffProgramAssociation             |
        | studentProgramAssociation           |
        | cohort                              |
        | staffCohortAssociation              |
        | studentCohortAssociation            |
        | studentCompetency                   |
        | studentCompetencyObjective          |
        | learningStandard                    |
        | learningObjective                   |
        | disciplineIncident                  |
        | disciplineAction                    |
        | studentDisciplineIncidentAssociation|
        | grade                               |
        | gradingPeriod                       |
        | calendarDate                        |
        | reportCard                          |
        | courseOffering                      |
        | studentAcademicRecord               |
        | graduationPlan                      |
When zip file is scp to ingestion landing zone
  And a batch job for file "StoriedDataSet_NY.zip" is completed in database
Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | assessment                  | 0     |
        | attendance                  | 0     |
        | cohort                      | 0     |
        | course                      | 8     |
        | courseOffering              | 8     |
        | disciplineAction            | 1     |
        | disciplineIncident          | 2     |
        | educationOrganization       | 7     |
        | grade                       | 0     |
        | gradebookEntry              | 0     |
        | parent                      | 0     |
        | program                     | 0     |
        | reportCard                  | 0     |
        | section                     | 16    |
        | session                     | 4     |
        | staff                       | 37    |
        | staffCohortAssociation      | 0     |
        | staffEducationOrganizationAssociation| 21 |
        | staffProgramAssociation     | 0     |
        | student                     | 8     |
        | studentAssessmentAssociation| 0     |
        | studentCohortAssociation      | 0     |
        | studentDisciplineIncidentAssociation| 4|
        | studentGradebookEntry       | 0     |
        | studentParentAssociation    | 0     |
        | studentProgramAssociation   | 0     |
        | studentSchoolAssociation    | 8     |
        | studentSectionAssociation   | 8     |
        | courseTranscript            | 0     |
        | teacherSchoolAssociation    | 16    |
        | teacherSectionAssociation   | 16    |
    And I check to find if record is in collection:
       | collectionName              | expectedRecordCount | searchParameter          | searchValue                | searchType           |
       | student                     | 1                   | body.studentUniqueStateId| 100000006                  | string               |
       | staff                       | 1                   | body.staffUniqueStateId  | jcarlyle                   | string               |
       | section                     | 1                   | body.uniqueSectionCode   | Mason201-Sec1              | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId | 1000000111                 | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId | NY-Parker                  | string               |
       | educationOrganization       | 1                   | body.stateOrganizationId | NY                         | string               |
    And I should see "Processed 726 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "InterchangeStudent.xml records considered: 8" in the resulting batch job file
    And I should see "InterchangeStudent.xml records ingested successfully: 8" in the resulting batch job file
    And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records considered: 15" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records ingested successfully: 15" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records considered: 566" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records ingested successfully: 566" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records considered: 24" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records ingested successfully: 24" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records considered: 90" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records ingested successfully: 90" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records considered: 16" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records ingested successfully: 16" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentDiscipline.xml records considered: 7" in the resulting batch job file
    And I should see "InterchangeStudentDiscipline.xml records ingested successfully: 7" in the resulting batch job file
    And I should see "InterchangeStudentDiscipline.xml records failed: 0" in the resulting batch job file


Scenario: Post an append zip file containing append data for Illinois Daybreak as a payload of the ingestion job: Append Database
Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
  And I post "StoriedDataSet_IL_Daybreak_Append.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
  And a batch job for file "StoriedDataSet_IL_Daybreak_Append.zip" is completed in database
Then I should see following map of entry counts in the corresponding collections:
     | collectionName                       | count |
     | assessment                           | 23    |
     | attendance                           | 75    |
     | cohort                               | 4     |
     | courseOffering                       | 97    |
     | disciplineAction                     | 3     |
     | disciplineIncident                   | 5     |
     | gradebookEntry                       | 13    |
     | parent                               | 12    |
     | program                              | 4     |
     | staffProgramAssociation              | 16    |
     | student                              | 185   |
     | studentAcademicRecord                | 121   |
     | studentAssessmentAssociation         | 204   |
     | studentCohortAssociation             | 6     |
     | studentDisciplineIncidentAssociation | 5     |
     | studentParentAssociation             | 11    |
     | studentProgramAssociation            | 9     |
     | courseTranscript                     | 200   |
  And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | assessment                  | 1                   | body.assessmentFamilyHierarchyName               | AP.AP Eng.AP-Eng-and-Literature      |string                  |
     | assessment                  | 1                   | body.assessmentFamilyHierarchyName               | AP.AP Eng.AP-Lang-and-Literature     |string                  |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | False            | string |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | False            | string |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | True             | string |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | True             | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-1 | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-2 | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-3 | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-4 | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode      | AssessmentItem-1 |string                  |
     | assessment                  | 1                   | body.assessmentItem.identificationCode      | AssessmentItem-2 |string                  |
     | assessment                  | 1                   | body.assessmentItem.identificationCode      | AssessmentItem-3 |string                  |
     | assessment                  | 1                   | body.assessmentItem.identificationCode      | AssessmentItem-4 |string                  |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | BOY                                              | string |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | EOY                                              | string |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | MOY                                              | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-English          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Mathematics      | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Reading          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Science          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Writing          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Critical Reading                             | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Math                                         | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Writing                                      | string |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Algebra            | string |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Arithmetic         | string |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Geometry           | string |
     | assessment                  | 3                   | body.assessmentFamilyHierarchyName             | READ2.READ 2.0.READ 2.0 Kindergarten                 | string |
     | attendance                  | 75                  | body.schoolYearAttendance.attendanceEvent.date | 2011-09-06      |string               |
     | cohort                      | 1                   | body.academicSubject        | English                 | string               |
     | cohort                      | 1                   | body.academicSubject        | Mathematics             | string               |
     | cohort                      | 1                   | body.cohortIdentifier       | ACC-TEST-COH-1          | string               |
     | cohort                      | 1                   | body.cohortIdentifier       | ACC-TEST-COH-2          | string               |
     | cohort                      | 1                   | body.cohortScope            | Statewide               | string               |
     | cohort                      | 2                   | body.academicSubject        | Social Studies          | string               |
     | cohort                      | 2                   | body.cohortIdentifier       | ACC-TEST-COH-3          | string               |
     | cohort                      | 3                   | body.cohortScope            | District                | string               |
     | courseOffering              | 0                   | body.localCourseTitle       | 3rd Grade Homeroom        | string               |
     | courseOffering              | 1                   | body.localCourseTitle       | Government-4A             | string               |
     | courseOffering              | 2                   | body.localCourseTitle       | Government-4              | string               |
     | disciplineAction            | 1                   | body.disciplineDate         | 2011-03-04              | string               |
     | disciplineAction            | 1                   | body.disciplineDate         | 2011-04-04              | string               |
     | disciplineAction            | 1                   | body.disciplineDate         | 2011-05-04              | string               |
     | disciplineAction            | 3                   | body.disciplineActionLength | 74                      | integer              |
     | disciplineIncident          | 1                   | body.incidentIdentifier     | Bullying                | string               |
     | disciplineIncident          | 1                   | body.incidentIdentifier     | Hazing                  | string               |
     | disciplineIncident          | 1                   | body.incidentIdentifier     | Disruption              | string               |
     | disciplineIncident          | 1                   | body.incidentIdentifier     | Tardiness               | string               |
     | disciplineIncident          | 2                   | body.weapons                | Non-Illegal Knife       | string               |
     | disciplineIncident          | 2                   | body.incidentDate           | 2011-02-01              | string               |
     | disciplineIncident          | 3                   | body.incidentLocation       | On School               | string               |
     | gradebookEntry              | 2                   | body.dateAssigned           | 2011-10-27              | string               |
     | gradebookEntry              | 4                   | body.dateAssigned           | 2011-10-13              | string               |
     | gradebookEntry              | 4                   | body.gradebookEntryType     | Quiz                    | string               |
     | parent                      | 1                   | body.parentUniqueStateId      | 2521899635      |string                  |
     | parent                      | 1                   | body.parentUniqueStateId      | 3152281864      |string                  |
     | program                     | 1                   | body.programId              | ACC-TEST-PROG-1         | string               |
     | program                     | 1                   | body.programId              | ACC-TEST-PROG-2         | string               |
     | program                     | 1                   | body.programId              | ACC-TEST-PROG-3         | string               |
     | program                     | 1                   | body.programId              | ACC-TEST-PROG-4         | string               |
     | program                     | 1                   | body.programSponsor         | State Education Agency  | string               |
     | program                     | 1                   | body.programType            | Remedial Education      | string               |
     | program                     | 3                   | body.programSponsor         | Local Education Agency  | string               |
     | program                     | 3                   | body.programType            | Regular Education       | string               |
     | staffCohortAssociation      | 1                   | body.beginDate              | 2011-01-01              | string               |
     | staffCohortAssociation      | 1                   | body.beginDate              | 2012-02-15              | string               |
     | staffCohortAssociation      | 1                   | body.endDate                | 2012-02-15              | string               |
     | staffCohortAssociation      | 2                   | body.beginDate              | 2011-07-01              | string               |
     | staffCohortAssociation      | 5                   | body.studentRecordAccess    | true                    | boolean              |
     | staffProgramAssociation     | 12                  | body.studentRecordAccess    | true                    | boolean              |
     | staffProgramAssociation     | 4                   | body.studentRecordAccess    | false                   | boolean              |
     | staffProgramAssociation     | 6                   | body.beginDate              | 2011-01-01              | string               |
     | staffProgramAssociation     | 2                   | body.beginDate              | 2011-01-02              | string               |
     | staffProgramAssociation     | 4                   | body.beginDate              | 2011-01-05              | string               |
     | staffProgramAssociation     | 1                   | body.beginDate              | 2011-05-02              | string               |
     | staffProgramAssociation     | 1                   | body.beginDate              | 2011-06-01              | string               |
     | staffProgramAssociation     | 2                   | body.beginDate              | 2011-06-02              | string               |
     | staffProgramAssociation     | 9                   | body.endDate                | 2012-02-15              | string               |
     | studentAcademicRecord         | 104                 | body.cumulativeCreditsAttempted.credit| 5                       | integer              |
     | studentAssessmentAssociation | 10                  | body.studentAssessmentItems.assessmentResponse                | False               | string |
     | studentAssessmentAssociation | 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | ACT-English-Rhetorical   | string |
     | studentAssessmentAssociation | 25                  | body.studentAssessmentItems.assessmentResponse                | True                | string |
     | studentAssessmentAssociation | 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | ACT-Math-Algebra            | string |
     | studentAssessmentAssociation | 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | ACT-Math-Pre-Algebra    | string |
     | studentAssessmentAssociation | 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | ACT-Mathematics             | string |
     | studentAssessmentAssociation | 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | ACT-Reading-Arts            | string |
     | studentAssessmentAssociation | 25                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | ACT-Writing                       | string |
     | studentAssessmentAssociation | 26                  | body.studentAssessmentItems.assessmentItem.identificationCode | AssessmentItem-3    | string |
     | studentAssessmentAssociation | 26                  | body.studentAssessmentItems.assessmentItem.identificationCode | AssessmentItem-4    | string |
     | studentAssessmentAssociation| 8                   | body.performanceLevelDescriptors.0.1.description | Extremely well qualified             |string                  |
     | studentCohortAssociation    | 1                   | body.beginDate              | 2011-02-01              | string               |
     | studentCohortAssociation    | 1                   | body.beginDate              | 2011-03-01              | string               |
     | studentCohortAssociation    | 1                   | body.endDate                | 2011-12-31              | string               |
     | studentDisciplineIncidentAssociation    | 1                   | body.studentParticipationCode       | Witness              | string               |
     | studentDisciplineIncidentAssociation    | 2                   | body.studentParticipationCode       | Victim               | string               |
     | studentDisciplineIncidentAssociation    | 2                   | body.studentParticipationCode       | Perpetrator          | string               |
     | studentProgramAssociation   | 1                   | body.endDate                | 2011-12-31              | string               |
     | studentProgramAssociation   | 1                   | body.endDate                | 2012-02-15              | string               |
     | studentProgramAssociation   | 2                   | body.beginDate              | 2011-05-01              | string               |
     | studentProgramAssociation   | 3                   | body.beginDate              | 2011-01-01              | string               |
     | studentProgramAssociation   | 4                   | body.beginDate              | 2011-03-01              | string               |
     | studentProgramAssociation   | 6                   | body.endDate                | 2012-04-12              | string               |
     | courseTranscript            | 200                 | body.courseAttemptResult              | Pass                    | string               |
     | courseTranscript            | 10                  | body.finalNumericGradeEarned          | 90                      | integer              |
     | courseTranscript  | 3                   | body.finalNumericGradeEarned          | 82                      | integer              |
     | courseTranscript  | 36                  | body.finalLetterGradeEarned           | B                       | string               |
     | courseTranscript  | 5                   | body.finalNumericGradeEarned          | 87                      | integer              |
     | courseTranscript  | 64                  | body.gradeLevelWhenTaken              | Tenth grade             | string               |
  When I find a record in "assessment" under "body.assessmentItem" where "identificationCode" is "AssessmentItem-1"
  Then the field "learningStandards" is an array of size 2
  And "learningStandards" contains a reference to a "learningStandard" where "body.learningStandardId.identificationCode" is "G-C.4"
  When I find a record in "assessment" under "body.assessmentItem" where "identificationCode" is "AssessmentItem-2"
  Then the field "learningStandards" is an array of size 2
  And "learningStandards" contains a reference to a "learningStandard" where "body.learningStandardId.identificationCode" is "G-SRT.3"
  When I find a record in "assessment" under "body.assessmentItem" where "identificationCode" is "AssessmentItem-3"
  Then the field "learningStandards" is an array of size 1
  And "learningStandards" contains a reference to a "learningStandard" where "body.learningStandardId.identificationCode" is "G-SRT.5"
  When I find a record in "assessment" under "body.assessmentItem" where "identificationCode" is "AssessmentItem-4"
  Then the field "learningStandards" is an array of size 1
  And "learningStandards" contains a reference to a "learningStandard" where "body.learningStandardId.identificationCode" is "G-SRT.6"
  And I should see "Processed 117 records." in the resulting batch job file
  And I should see "Program2.xml records considered: 4" in the resulting batch job file
  And I should see "Program2.xml records ingested successfully: 4" in the resulting batch job file
  And I should see "Program2.xml records failed: 0" in the resulting batch job file
  And I should see "Cohort2.xml records considered: 1" in the resulting batch job file
  And I should see "Cohort2.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "Cohort2.xml records failed: 0" in the resulting batch job file
  And I should see "DisciplineAction2.xml records considered: 2" in the resulting batch job file
  And I should see "DisciplineAction2.xml records ingested successfully: 2" in the resulting batch job file
  And I should see "DisciplineAction2.xml records failed: 0" in the resulting batch job file
  And I should see "StudentProgramAssociation2.xml records considered: 9" in the resulting batch job file
  And I should see "StudentProgramAssociation2.xml records ingested successfully: 9" in the resulting batch job file
  And I should see "StudentProgramAssociation2.xml records failed: 0" in the resulting batch job file
  And I should see "Staff2.xml records considered: 9" in the resulting batch job file
  And I should see "Staff2.xml records ingested successfully: 9" in the resulting batch job file
  And I should see "Staff2.xml records failed: 0" in the resulting batch job file
  And I should see "StudentCohortAssociation2.xml records considered: 1" in the resulting batch job file
  And I should see "StudentCohortAssociation2.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "StudentCohortAssociation2.xml records failed: 0" in the resulting batch job file
  And I should see "GradeBookEntry2.xml records considered: 1" in the resulting batch job file
  And I should see "GradeBookEntry2.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "GradeBookEntry2.xml records failed: 0" in the resulting batch job file
  And I should see "DisciplineIncident2.xml records considered: 2" in the resulting batch job file
  And I should see "DisciplineIncident2.xml records ingested successfully: 2" in the resulting batch job file
  And I should see "DisciplineIncident2.xml records failed: 0" in the resulting batch job file
  And I should see "StaffCohortAssociation2.xml records considered: 2" in the resulting batch job file
  And I should see "StaffCohortAssociation2.xml records ingested successfully: 2" in the resulting batch job file
  And I should see "StaffCohortAssociation2.xml records failed: 0" in the resulting batch job file
  And I should see "Discipline2.xml records considered: 2" in the resulting batch job file
  And I should see "Discipline2.xml records ingested successfully: 2" in the resulting batch job file
  And I should see "Discipline2.xml records failed: 0" in the resulting batch job file
  And I should see "CourseTranscript2.xml records considered: 8" in the resulting batch job file
  And I should see "CourseTranscript2.xml records ingested successfully: 8" in the resulting batch job file
  And I should see "CourseTranscript2.xml records failed: 0" in the resulting batch job file
  And I should see "CourseOffering.xml records considered: 2" in the resulting batch job file
  And I should see "CourseOffering.xml records ingested successfully: 2" in the resulting batch job file
  And I should see "CourseOffering.xml records failed: 0" in the resulting batch job file
  And I should see "actAssessment_CCSMapping.xml records considered: 1" in the resulting batch job file
  And I should see "actAssessment_CCSMapping.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "actAssessment_CCSMapping.xml records failed: 0" in the resulting batch job file
  And I should see "Grade_12_Math_CCS_G_SRT.xml records considered: 12" in the resulting batch job file
  And I should see "Grade_12_Math_CCS_G_SRT.xml records ingested successfully: 12" in the resulting batch job file
  And I should see "Grade_12_Math_CCS_G_SRT.xml records failed: 0" in the resulting batch job file
  And I should see "Grade_12_Math_CCS_G_C.xml records considered: 7" in the resulting batch job file
  And I should see "Grade_12_Math_CCS_G_C.xml records ingested successfully: 7" in the resulting batch job file
  And I should see "Grade_12_Math_CCS_G_C.xml records failed: 0" in the resulting batch job file
  And I should see "Grade_12_English_CCS_RI_11_12.xml records considered: 17" in the resulting batch job file
  And I should see "Grade_12_English_CCS_RI_11_12.xml records ingested successfully: 17" in the resulting batch job file
  And I should see "Grade_12_English_CCS_RI_11_12.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeAssessmentMetadata-AP-Eng.xml records considered: 6" in the resulting batch job file
  And I should see "InterchangeAssessmentMetadata-AP-Eng.xml records ingested successfully: 6" in the resulting batch job file
  And I should see "InterchangeAssessmentMetadata-AP-Eng.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeStudentAssessment-CgrayAP-English.xml records considered: 11" in the resulting batch job file
  And I should see "InterchangeStudentAssessment-CgrayAP-English.xml records ingested successfully: 11" in the resulting batch job file
  And I should see "InterchangeStudentAssessment-CgrayAP-English.xml records failed: 0" in the resulting batch job file
  And I should see "StudentAssessmentItem_ACTAssessmentItem_Mapping.xml records considered: 1" in the resulting batch job file
  And I should see "StudentAssessmentItem_ACTAssessmentItem_Mapping.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "StudentAssessmentItem_ACTAssessmentItem_Mapping.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeStudent.xml records considered: 12" in the resulting batch job file
  And I should see "InterchangeStudent.xml records ingested successfully: 12" in the resulting batch job file
  And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeEducationOrgCalendar.xml records considered: 3" in the resulting batch job file
  And I should see "InterchangeEducationOrgCalendar.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "InterchangeEducationOrgCalendar.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records considered: 3" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file

Scenario: Concurrent job processing
Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
        | studentSchoolAssociation    |
        | course                      |
        | educationOrganization       |
        | section                     |
        | studentSectionAssociation   |
        | staff                       |
        | staffEducationOrganizationAssociation|
        | teacherSchoolAssociation    |
        | teacherSectionAssociation   |
        | session                     |
        | assessment                  |
        | studentAssessmentAssociation|
        | gradebookEntry              |
        | courseTranscript            |
        | studentGradebookEntry       |
        | parent                      |
        | studentParentAssociation    |
        | attendance                  |
        | program                     |
        | staffProgramAssociation     |
        | studentProgramAssociation   |
        | cohort                      |
        | staffCohortAssociation      |
        | studentCohortAssociation    |
        | studentCompetency           |
        | studentCompetencyObjective  |
        | learningStandard            |
        | learningObjective           |
        | disciplineIncident          |
        | disciplineAction            |
        | studentDisciplineIncidentAssociation|
        | grade                       |
        | gradingPeriod               |
        | calendarDate                |
        | reportCard                  |
        | courseOffering              |
        | studentAcademicRecord       |
    And I am using preconfigured Ingestion Landing Zone for "Hyrule-NYC"
    And the following collections are empty in datastore:
        | collectionName              |
        | assessment                  |
        | attendance                  |
        | calendarDate                |
        | cohort                      |
        | course                      |
        | courseOffering              |
        | disciplineAction            |
        | disciplineIncident          |
        | educationOrganization       |
        | grade                       |
        | gradebookEntry              |
        | gradingPeriod               |
        | learningObjective           |
        | learningStandard            |
        | parent                      |
        | program                     |
        | reportCard                  |
        | section                     |
        | session                     |
        | staff                       |
        | staffCohortAssociation      |
        | staffProgramAssociation     |
        | student                     |
        | studentAcademicRecord       |
        | studentAssessmentAssociation|
        | studentCohortAssociation    |
        | studentCompetency           |
        | studentCompetencyObjective  |
        | studentDisciplineIncidentAssociation|
        | studentGradebookEntry       |
        | studentParentAssociation    |
        | studentProgramAssociation   |
        | studentSchoolAssociation    |
        | studentSectionAssociation   |
        | courseTranscript            |
        | teacherSchoolAssociation    |
        | teacherSectionAssociation   |
        |staffEducationOrganizationAssociation|
    And I post "StoriedDataSet_IL_Daybreak.zip" file as the payload of the ingestion job for "Midgar-Daybreak"
    And I post "StoriedDataSet_NY.zip" file as the payload of the ingestion job for "Hyrule-NYC"

When zip file is scp to ingestion landing zone for "Midgar-Daybreak"
  And zip file is scp to ingestion landing zone for "Hyrule-NYC"
  And a batch job for file "StoriedDataSet_IL_Daybreak.zip" is completed in database
  And a batch job for file "StoriedDataSet_NY.zip" is completed in database

Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | assessment                  | 0     |
        | attendance                  | 0     |
        | calendarDate                | 556   |
        | cohort                      | 0     |
        | course                      | 8     |
        | courseOffering              | 8     |
        | disciplineAction            | 1     |
        | disciplineIncident          | 2     |
        | educationOrganization       | 7     |
        | grade                       | 0     |
        | gradebookEntry              | 0     |
        | gradingPeriod               | 6     |
        | learningObjective           | 0     |
        | learningStandard            | 0     |
        | parent                      | 0     |
        | program                     | 0     |
        | reportCard                  | 0     |
        | section                     | 16    |
        | session                     | 4     |
        | staff                       | 37    |
        | staffCohortAssociation      | 0     |
        | staffEducationOrganizationAssociation| 21 |
        | staffProgramAssociation     | 0     |
        | student                     | 8     |
        | studentAcademicRecord       | 0     |
        | studentAssessmentAssociation| 0     |
        | studentCohortAssociation    | 0     |
        | studentCompetency           | 0     |
        | studentCompetencyObjective  | 0     |
        | studentDisciplineIncidentAssociation| 4 |
        | studentGradebookEntry       | 0     |
        | studentParentAssociation    | 0     |
        | studentProgramAssociation   | 0     |
        | studentSchoolAssociation    | 8     |
        | studentSectionAssociation   | 8     |
        | courseTranscript            | 0     |
        | teacherSchoolAssociation    | 16    |
        | teacherSectionAssociation   | 16    |


@IL-Daybreak
Scenario: Post a zip file containing new entities and deltas for existing entities. Validate updates and inserts.
    Given I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | gradebookEntry              | 12    |
        | studentGradebookEntry       | 315   |
        | studentCompetency           | 59    |
        | grade                       | 4     |
        | reportCard                  | 2     |
        | staffCohortAssociation      | 3     |
        | staffProgramAssociation     | 7     |
    And I check to find if record is in collection:
        | collectionName              | expectedRecordCount | searchParameter                | searchValue             | searchType           |
        | gradebookEntry              | 0                   | body.dateAssigned              | 2011-09-27              | string               |
        | studentGradebookEntry       | 0                   | body.letterGradeEarned         | Q                       | string               |
        | studentCompetency           | 0                   | body.competencyLevel.codeValue | 99                      | string               |
        | grade                       | 0                   | body.letterGradeEarned         | U                       | string               |
        | reportCard                  | 0                   | body.gpaGivenGradingPeriod     | 1.1                     | double               |
        | staffCohortAssociation      | 1                   | body.beginDate                 | 2011-01-01              | string               |
        | staffCohortAssociation      | 0                   | body.beginDate                 | 2011-01-02              | string               |
        | staffCohortAssociation      | 1                   | body.beginDate                 | 2010-01-15              | string               |
        | staffProgramAssociation     | 2                   | body.beginDate                 | 2011-01-01              | string               |
        | staffProgramAssociation     | 0                   | body.endDate                   | 2012-03-16              | string               |
        | staffProgramAssociation     | 4                   | body.beginDate                 | 2011-01-05              | string               |
        | staffProgramAssociation     | 1                   | body.beginDate                 | 2011-06-01              | string               |
        | staffProgramAssociation     | 0                   | body.beginDate                 | 2011-12-31              | string               |
        | staffProgramAssociation     | 4                   | body.endDate                   | 2012-02-15              | string               |
        | staffProgramAssociation     | 4                   | body.studentRecordAccess       | true                    | boolean              |
        | staffProgramAssociation     | 3                   | body.studentRecordAccess       | false                   | boolean              |
    When I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And I post "StoriedDataSet_IL_Daybreak_Deltas.zip" file as the payload of the ingestion job
    And zip file is scp to ingestion landing zone
    And a batch job for file "StoriedDataSet_IL_Daybreak_Deltas.zip" is completed in database
    Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | gradebookEntry              | 13    |
        | studentGradebookEntry       | 316   |
        | studentCompetency           | 59    |
        | grade                       | 4     |
        | reportCard                  | 2     |
        | staffCohortAssociation      | 4     |
        | staffProgramAssociation     | 11    |
    And I check to find if record is in collection:
        | collectionName              | expectedRecordCount | searchParameter                | searchValue             | searchType           |
        | gradebookEntry              | 1                   | body.dateAssigned              | 2011-09-27              | string               |
        | studentGradebookEntry       | 1                   | body.letterGradeEarned         | Q                       | string               |
        | grade                       | 1                   | body.letterGradeEarned         | U                       | string               |
        | reportCard                  | 1                   | body.gpaGivenGradingPeriod     | 1.1                     | double               |
        | staffCohortAssociation      | 1                   | body.beginDate                 | 2011-01-01              | string               |
        | staffCohortAssociation      | 1                   | body.beginDate                 | 2011-01-02              | string               |
        | staffCohortAssociation      | 1                   | body.beginDate                 | 2010-01-15              | string               |
        | staffProgramAssociation     | 2                   | body.beginDate                 | 2011-01-01              | string               |
        | staffProgramAssociation     | 2                   | body.endDate                   | 2012-03-16              | string               |
        | staffProgramAssociation     | 4                   | body.beginDate                 | 2011-01-05              | string               |
        | staffProgramAssociation     | 1                   | body.beginDate                 | 2011-06-01              | string               |
        | staffProgramAssociation     | 4                   | body.beginDate                 | 2011-12-31              | string               |
        | staffProgramAssociation     | 8                   | body.endDate                   | 2012-02-15              | string               |
        | staffProgramAssociation     | 10                  | body.studentRecordAccess       | true                    | boolean              |
        | staffProgramAssociation     | 1                   | body.studentRecordAccess       | false                   | boolean              |
