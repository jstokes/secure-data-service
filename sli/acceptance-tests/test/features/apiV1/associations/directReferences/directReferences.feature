@RALLY_US209
@RALLY_US1803
@RALLY_US1804
@RALLY_US1961
@RALLY_DE85
@RALLY_DE87
Feature: As an SLI application, I want entity references displayed as usable links with exlicit link names
This means that when links are requested in a GET request, a link should be present that represents each reference field
and that the links are valid

Background: Nothing yet
    Given I am logged in using "rrogers" "rrogers1234" to realm "IL"

Scenario Outline: Control the presence of links by specifying an accept type format
   Given format <format>
     And I request <return links>
     And I navigate to GET "/<URI FOR ENTITY THAT CAN RETURN LINKS>/<ID OF ENTITY THAT CAN RETURN LINKS>"
    Then I should receive a return code of 200
     And the response should contain links if I requested them
    Examples:
        | format                     | return links |
        | "application/json"         | "links"      |
        | "application/vnd.slc+json" | "links"      |

Scenario Outline: Confirm all known reference fields generate two valid links that are implemented and update-able
   Given format "application/vnd.slc+json"
     And referring collection <source entity type> exposed as <source expose name>
     And referring field <reference field> with value <reference value>
     And referred collection <target entity type> exposed as <target expose name>
     And the link from references to referred entity is <target link name>
     And the link from referred entity to referring entities is <source link name>
     And the ID to use for testing is <testing ID>
     And the ID to use for testing a valid update is <new valid value>
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a link named <target link name>
    When I navigate to GET the link named <target link name>
    Then I should receive a return code of 200
     And "entityType" should be <target entity type>
     And I should receive a link named <source link name>
    When I navigate to GET the link named <source link name>
    Then I should receive a return code of 200
     And each entity's "entityType" should be <source entity type>
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
     And I set the <reference field> to "<INVALID REFERENCE>"
     And I navigate to PUT "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a return code of 403
    When I set the <reference field> to <new valid value>
     And I navigate to PUT "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a return code of 204
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then <reference field> should be <new valid value>
     And I should receive a link named <target link name>
    When I navigate to GET the link named <target link name>
    Then "id" should be "<NEW VALID VALUE>"
     And "entityType" should be <target entity type>
    Examples:
        | source entity type                      | source expose name                       | reference field                  | target entity type      | target expose name       | target link name           | source link name                            | testing ID                             | reference value                        | new valid value                        |
        | "reportCard"                            | "reportCards"                            | "studentId"                      | "student"               | "students"               | "getStudent"               | "getReportCards"                            | "cf0ca1c6-a9db-4180-bf23-8276c4e2624c" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "41df2791-b33c-4b10-8de6-a24963bbd3dd" |
        | "reportCard"                            | "reportCards"                            | "gradingPeriodId"                | "gradingPeriod"         | "gradingPeriods"         | "getGradingPeriod"         | "getReportCards"                            | "cf0ca1c6-a9db-4180-bf23-8276c4e2624c" | "b40a7eb5-dd74-4666-a5b9-5c3f4425f130" | "ef72b883-90fa-40fa-afc2-4cb1ae17623b" |
        | "grade"                                 | "grades"                                 | "studentSectionAssociationId"    | "studentSectionAssociation" | "studentSectionAssociations" | "getStudentSectionAssociation" | "getGrades"                     | "708c4e08-9942-11e1-a8a9-68a86d21d918" | "7ba1a2e9-989c-4b00-b5e0-9bf3b72c909d" | "205786da-f3a1-470d-86a1-2dabff9b2146" |
        | "grade"                                 | "grades"                                 | "gradingPeriodId"                | "gradingPeriod"         | "gradingPeriods"         | "getGradingPeriod"         | "getGrades"                                 | "708c4e08-9942-11e1-a8a9-68a86d21d918" | "b40a7eb5-dd74-4666-a5b9-5c3f4425f130" | "ef72b883-90fa-40fa-afc2-4cb1ae17623b" |
        | "studentCompetency"                     | "studentCompetencies"                    | "learningObjectiveId"            | "learningObjective"     | "learningObjectives"     | "getLearningObjective"     | "getStudentCompetencies"                    | "b57643e4-9acf-11e1-89a7-68a86d21d918" | "dd9165f2-65be-6d27-a8ac-bdc5f46757b6" | "dd9165f2-65fe-6d27-a8ec-bdc5f47757b7" |
        | "studentCompetency"                     | "studentCompetencies"                    | "studentSectionAssociationId"    | "studentSectionAssociation" | "studentSectionAssociations" | "getStudentSectionAssociation" | "getStudentCompetencies"        | "b57643e4-9acf-11e1-89a7-68a86d21d918" | "7ba1a2e9-989c-4b00-b5e0-9bf3b72c909d" | "205786da-f3a1-470d-86a1-2dabff9b2146" |
        | "studentAcademicRecord"                 | "studentAcademicRecords"                 | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentAcademicRecords"                 | "56afc8d4-6c91-48f9-8a51-de527c1131b7" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentAcademicRecord"                 | "studentAcademicRecords"                 | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getStudentAcademicRecords"                 | "56afc8d4-6c91-48f9-8a51-de527c1131b7" | "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92" | "62101257-592f-4cbe-bcd5-b8cd24a06f73" |
        | "attendance"                            | "attendances"                            | "studentId"                      | "student"               | "students"               | "getStudent"               | "getAttendances"                            | "530f0704-c240-4ed9-0a64-55c0308f91ee" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "5738d251-dd0b-4734-9ea6-417ac9320a15" |
        | "educationOrganization"                 | "educationOrganizations"                 | "parentEducationAgencyReference" | "educationOrganization" | "educationOrganizations" | "getParentEducationOrganization" | "getFeederEducationOrganizations"     | "b2c6e292-37b0-4148-bf75-c98a2fcc905f" | "b1bd3db6-d020-4651-b1b8-a8dba688d9e1" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" |
        | "gradebookEntry"                        | "gradebookEntries"                       | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getGradebookEntries"                       | "e49dc00c-182d-4f22-98c5-3d35b5f6d993" | "706ee3be-0dae-4e98-9525-f564e05aa388" | "15ab6363-5509-470c-8b59-4f289c224107" |
        | "school"                                | "schools"                                | "parentEducationAgencyReference" | "educationOrganization" | "educationOrganizations" | "getParentEducationOrganization" | "getFeederSchools"                    | "8cc0a1ac-ccb5-dffc-1d74-32964722179b" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" |
        | "section"                               | "sections"                               | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getSections"                               | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "ec2e4218-6483-4e9c-8954-0aecccfd4731" | "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb" |
        | "section"                               | "sections"                               | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getSections"                               | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "1cb50f82-7200-441a-a1b6-02d6532402a0" | "62101257-592f-4cbe-bcd5-b8cd24a06f73" |
        | "section"                               | "sections"                               | "courseOfferingId"               | "courseOffering"        | "courseOfferings"        | "getCourseOffering"        | "getSections"                               | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "01ba881f-ae39-4b76-920e-42bc7e8769d7" | "c5b80f7d-93c5-11e1-adcc-101f74582c4c" |
        | "studentGradebookEntry"                 | "studentGradebookEntries"                | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentGradebookEntries"                | "0f5e6f78-5434-f906-e51b-d63ef970ef8f" | "5738d251-dd0b-4734-9ea6-417ac9320a15" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentGradebookEntry"                 | "studentGradebookEntries"                | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getStudentGradebookEntries"                | "0f5e6f78-5434-f906-e51b-d63ef970ef8f" | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "8ed12459-eae5-49bc-8b6b-6ebe1a56384f" |
        | "studentGradebookEntry"                 | "studentGradebookEntries"                | "gradebookEntryId"               | "gradebookEntry"        | "gradebookEntries"       | "getGradebookEntry"        | "getStudentGradebookEntries"                | "0f5e6f78-5434-f906-e51b-d63ef970ef8f" | "0b980a49-56b6-4d17-847b-2997b7227686" | "e49dc00c-182d-4f22-98c5-3d35b5f6d993" |
        | "cohort"                                | "cohorts"                                | "educationOrgId"                 | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getCohorts"                                | "b408635d-8fd5-11e1-86ec-0021701f543f" | "b1bd3db6-d020-4651-b1b8-a8dba688d9e1" | "6756e2b9-aba1-4336-80b8-4a5dde3c63fe" |


Scenario Outline: Confirm all association generate one valid links that is implemented and update-able
   Given format "application/vnd.slc+json"
     And referring collection <source entity type> exposed as <source expose name>
     And referring field <reference field> with value <reference value>
     And referred collection <target entity type> exposed as <target expose name>
     And the link from references to referred entity is <target link name>
     And the link from referred entity to referring entities is <source link name>
     And the ID to use for testing is <testing ID>
     And the ID to use for testing a valid update is <new valid value>
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a link named <target link name>
    When I navigate to GET the link named <target link name>
    Then I should receive a return code of 200
     And "entityType" should be <target entity type>
    When I navigate to GET "/<URI OF ENTITIES THAT REFER TO TARGET>"
    Then I should receive a return code of 200
     And each entity's "entityType" should be <source entity type>
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
     And I set the <reference field> to "<INVALID REFERENCE>"
     And I navigate to PUT "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a return code of 403
    When I set the <reference field> to <new valid value>
     And I navigate to PUT "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a return code of 204
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then <reference field> should be <new valid value>
     And I should receive a link named <target link name>
    When I navigate to GET the link named <target link name>
    Then "id" should be "<NEW VALID VALUE>"
     And "entityType" should be <target entity type>
    Examples:
        | source entity type                      | source expose name                       | reference field                  | target entity type      | target expose name       | target link name           | source link name                            | testing ID                             | reference value                        | new valid value                        |
        | "studentDisciplineIncidentAssociation"  | "studentDisciplineIncidentAssociations"  | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentDisciplineIncidentAssociations"  | "0e26de6c-225b-9f67-9224-5113ad50a03b" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "41df2791-b33c-4b10-8de6-a24963bbd3dd" |
        | "studentDisciplineIncidentAssociation"  | "studentDisciplineIncidentAssociations"  | "disciplineIncidentId"           | "disciplineIncident"    | "disciplineIncidents"    | "getDisciplineIncident"    | "getStudentDisciplineIncidentAssociations"  | "0e26de6c-225b-9f67-9224-5113ad50a03b" | "0e26de79-222a-5e67-9201-5113ad50a03b" | "0e26de79-22ea-5d67-9201-5113ad50a03b" |
        | "courseOffering"                        | "courseOfferings"                        | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getCourseOfferings"                        | "b360e8e8-54d1-4b00-952a-b817f91035ed" | "52038025-1f18-456a-884e-d2e63f9a02f4" | "75de6e74-a2e0-47da-ad1e-df1c833af92c" |
        | "courseOffering"                        | "courseOfferings"                        | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getCourseOfferings"                        | "b360e8e8-54d1-4b00-952a-b817f91035ed" | "d23ebfc4-5192-4e6c-a52b-81cee2319072" | "c549e272-9a7b-4c02-aff7-b105ed76c904" |
        | "staffEducationOrganizationAssociation" | "staffEducationOrgAssignmentAssociations"| "educationOrganizationReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getStaffEducationOrgAssignmentAssociations"| "0966614a-6c5d-4345-b451-7ec991823ac5" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" | "b2c6e292-37b0-4148-bf75-c98a2fcc905f" |
        | "staffEducationOrganizationAssociation" | "staffEducationOrgAssignmentAssociations"| "staffReference"                 | "staff"                 | "staff"                  | "getStaff"                 | "getStaffEducationOrgAssignmentAssociations"| "0966614a-6c5d-4345-b451-7ec991823ac5" | "45d6c371-e7f1-4fa8-899a-e9f2309cbe4e" | "527406fd-0f6c-43b7-9dab-fab504c87c7f" |
        | "studentAssessmentAssociation"          | "studentAssessments"                     | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentAssessments"                     | "e5e13e61-01aa-066b-efe0-710f7a0e8755" | "5738d251-dd0b-4734-9ea6-417ac9320a15" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentAssessmentAssociation"          | "studentAssessments"                     | "assessmentId"                   | "assessment"            | "assessments"            | "getAssessment"            | "getStudentAssessments"                     | "e5e13e61-01aa-066b-efe0-710f7a0e8755" | "67ce204b-9999-4a11-bfea-000000004682" | "dd916592-7d7e-5d27-a87d-dfc7fcb757f6" |
        | "studentSchoolAssociation"              | "studentSchoolAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentSchoolAssociations"              | "925d3f23-001f-4173-883b-04cf04ed9ad4" | "5738d251-dd0b-4734-9ea6-417ac9320a15" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentSchoolAssociation"              | "studentSchoolAssociations"              | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getStudentSchoolAssociations"              | "925d3f23-001f-4173-883b-04cf04ed9ad4" | "ec2e4218-6483-4e9c-8954-0aecccfd4731" | "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb" |
        | "studentSectionAssociation"             | "studentSectionAssociations"             | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentSectionAssociations"             | "bac890d6-b580-4d9d-a0d4-8bce4e8d351a" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "5738d251-dd0b-4734-9ea6-417ac9320a15" |
        | "studentSectionAssociation"             | "studentSectionAssociations"             | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getStudentSectionAssociations"             | "bac890d6-b580-4d9d-a0d4-8bce4e8d351a" | "706ee3be-0dae-4e98-9525-f564e05aa388" | "7295e51e-cd51-4901-ae67-fa33966478c7" |
        | "studentTranscriptAssociation"          | "courseTranscripts"                      | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getCourseTranscripts"                      | "36aeeabf-ee9b-46e6-8039-13320bf15226" | "ddf01d82-9293-49ba-b16e-0fe5b4f4804d" | "75de6e74-a2e0-47da-ad1e-df1c833af92c" |
        | "studentTranscriptAssociation"          | "courseTranscripts"                      | "studentId"                      | "student"               | "students"               | "getStudent"               | "getCourseTranscripts"                      | "36aeeabf-ee9b-46e6-8039-13320bf15226" | "5738d251-dd0b-4734-9ea6-417ac9320a15" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentParentAssociation"              | "studentParentAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentParentAssociations"              | "c5aa1969-492a-5150-8479-71bfc4d57f1e" | "5738d251-dd0b-4734-9ea6-417ac9320a15" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentParentAssociation"              | "studentParentAssociations"              | "parentId"                       | "parent"                | "parents"                | "getParent"                | "getStudentParentAssociations"              | "c5aa1969-492a-5150-8479-71bfc4d57f1e" | "38ba6ea7-7e73-47db-99e7-d0956f83d7e9" | "eb4d7e1b-7bed-890a-cddf-cdb25a29fc2d" |
        | "teacherSchoolAssociation"              | "teacherSchoolAssociations"              | "teacherId"                      | "teacher"               | "teachers"               | "getTeacher"               | "getTeacherSchoolAssociations"              | "353dc847-51b5-44b8-a70f-a45c58fc8ee3" | "edce823c-ee28-4840-ae3d-74d9e9976dc5" | "67ed9078-431a-465e-adf7-c720d08ef512" |
        | "teacherSchoolAssociation"              | "teacherSchoolAssociations"              | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getTeacherSchoolAssociations"              | "353dc847-51b5-44b8-a70f-a45c58fc8ee3" | "6756e2b9-aba1-4336-80b8-4a5dde3c63fe" | "ec2e4218-6483-4e9c-8954-0aecccfd4731" |
        | "teacherSectionAssociation"             | "teacherSectionAssociations"             | "teacherId"                      | "teacher"               | "teachers"               | "getTeacher"               | "getTeacherSectionAssociations"             | "ff03a37c-3cbd-4f49-a7e7-ad306bfc9d6f" | "67ed9078-431a-465e-adf7-c720d08ef512" | "bcfcc33f-f4a6-488f-baee-b92fbd062e8d" |
        | "teacherSectionAssociation"             | "teacherSectionAssociations"             | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getTeacherSectionAssociations"             | "ff03a37c-3cbd-4f49-a7e7-ad306bfc9d6f" | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "47b5adbf-6fd0-4f07-ba5e-39612da2e234" |
        | "studentCohortAssociation"              | "studentCohortAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentCohortAssociations"              | "b40e2fc7-8fd5-11e1-86ec-0021701f543f" | "9f4019ca-dd53-4027-b11c-fc151268fafd" | "ace1dc53-8c1d-4c01-b922-c3ebb7ff5be8" |
        | "studentCohortAssociation"              | "studentCohortAssociations"              | "cohortId"                       | "cohort"                | "cohorts"                | "getCohort"                | "getStudentCohortAssociations"              | "b40c5b02-8fd5-11e1-86ec-0021701f543f" | "b408635d-8fd5-11e1-86ec-0021701f543f" | "b40926af-8fd5-11e1-86ec-0021701f543f" |
        | "studentProgramAssociation"             | "studentProgramAssociations"             | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentProgramAssociations"             | "b3f68907-8fd5-11e1-86ec-0021701f543f" | "0f0d9bac-0081-4900-af7c-d17915e02378" | "dd4068df-0bea-4280-bbac-fbc736eea54d" |
        | "studentProgramAssociation"             | "studentProgramAssociations"             | "programId"                      | "program"               | "programs"               | "getProgram"               | "getStudentProgramAssociations"             | "b3f6fe38-8fd5-11e1-86ec-0021701f543f" | "9b8c3aab-8fd5-11e1-86ec-0021701f543f" | "9b8cafdc-8fd5-11e1-86ec-0021701f543f" |


