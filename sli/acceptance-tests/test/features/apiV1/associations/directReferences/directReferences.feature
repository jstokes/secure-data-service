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
    Then I should receive a link named <target link name> with URI "/<URI OF REFERENCED ENTITY>/<REFERRED ENTITY ID>"
    When I navigate to GET "/<URI OF REFERENCED ENTITY>/<REFERRED ENTITY ID>"
    Then I should receive a return code of 200
     And "entityType" should be <target entity type>
     And I should receive a link named <source link name> with URI "/<URI OF ENTITIES THAT REFER TO TARGET>"
    When I navigate to GET "/<URI OF ENTITIES THAT REFER TO TARGET>"
    Then I should receive a return code of 200
     And each entity's "entityType" should be <source entity type>
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
     And I set the <reference field> to "<INVALID REFERENCE>"
     And I navigate to PUT "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a return code of 400
    When I set the <reference field> to <new valid value>
     And I navigate to PUT "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then I should receive a return code of 204
    When I navigate to GET "/<REFERRING COLLECTION URI>/<REFERRING ENTITY ID>"
    Then <reference field> should be <new valid value>
     And I should receive a link named <target link name> with URI "/<URI OF REFERENCED ENTITY>/<NEW VALID VALUE>"
    When I navigate to GET "/<URI OF REFERENCED ENTITY>/<NEW VALID VALUE>"
    Then "id" should be "<NEW VALID VALUE>"
     And "entityType" should be <target entity type>
    Examples:
        | source entity type                      | source expose name                       | reference field                  | target entity type      | target expose name       | target link name           | source link name                            | testing ID                             | reference value                        | new valid value                        |
        | "studentAcademicRecord"                 | "studentAcademicRecords"                 | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentAcademicRecords"                 | "56afc8d4-6c91-48f9-8a51-de527c1131b7" | "5738d251-dd0b-4734-9ea6-417ac9320a15" | "ae479bef-eb57-4c2e-8896-84983b1d4ed2" |
        | "studentAcademicRecord"                 | "studentAcademicRecords"                 | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getStudentAcademicRecords"                 | "56afc8d4-6c91-48f9-8a51-de527c1131b7" | "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92" | "62101257-592f-4cbe-bcd5-b8cd24a06f73" |
        | "attendance"                            | "attendances"                            | "studentId"                      | "student"               | "students"               | "getStudent"               | "getAttendances"                            | "530f0704-c240-4ed9-0a64-55c0308f91ee" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "5738d251-dd0b-4734-9ea6-417ac9320a15" |
        #| "educationOrganization"                 | "educationOrganizations"                 | "parentEducationAgencyReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getEducationOrganizations"                 | "bd086bae-ee82-4cf2-baf9-221a9407ea07" | "b1bd3db6-d020-4651-b1b8-a8dba688d9e1" | "67ce204b-9999-4a11-aabe-000000000002" |
        | "gradebookEntry"                        | "gradebookEntries"                       | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getGradebookEntries"                       | "e49dc00c-182d-4f22-98c5-3d35b5f6d993" | "706ee3be-0dae-4e98-9525-f564e05aa388" | "15ab6363-5509-470c-8b59-4f289c224107" |
        | "school"                                | "schools"                                | "parentEducationAgencyReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getSchools"                                | "8cc0a1ac-ccb5-dffc-1d74-32964722179b" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" |
        | "schoolSessionAssociation"              | "schoolSessionAssociations"              | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getSchoolSessionAssociations"              | "7a1f5ae5-ee79-f9e5-eca8-10c32f390a8c" | "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb" | "ec2e4218-6483-4e9c-8954-0aecccfd4731" |
        | "schoolSessionAssociation"              | "schoolSessionAssociations"              | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getSchoolSessionAssociations"              | "7a1f5ae5-ee79-f9e5-eca8-10c32f390a8c" | "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92" | "62101257-592f-4cbe-bcd5-b8cd24a06f73" |
        | "section"                               | "sections"                               | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getSections"                               | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "ec2e4218-6483-4e9c-8954-0aecccfd4731" | "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb" |
        | "section"                               | "sections"                               | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getSections"                               | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "1cb50f82-7200-441a-a1b6-02d6532402a0" | "62101257-592f-4cbe-bcd5-b8cd24a06f73" |
        | "section"                               | "sections"                               | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getSections"                               | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "80931a47-c515-432b-999b-27537898b457" | "e31f7583-417e-4c42-bd55-0bbe7518edf8" |
        | "sectionAssessmentAssociation"          | "sectionAssessmentAssociations"          | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getSectionAssessmentAssociations"          | "0cb7e99b-fc18-ecdb-830a-99abe8ee26be" | "ceffbb26-1327-4313-9cfc-1c3afd38122e" | "8ed12459-eae5-49bc-8b6b-6ebe1a56384f" |
        | "sectionAssessmentAssociation"          | "sectionAssessmentAssociations"          | "assessmentId"                   | "assessment"            | "assessments"            | "getAssessment"            | "getSectionAssessmentAssociations"          | "0cb7e99b-fc18-ecdb-830a-99abe8ee26be" | "67ce204b-9999-4a11-bfea-000000004682" | "dd916592-7d7e-5d27-a87d-dfc7fcb757f6" |
        | "sessionCourseAssociation"              | "courseOfferings"                        | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getCourseOfferings"                        | "67bfb073-bd36-0771-8d5a-bba7ba03f8fb" | "ddf01d82-9293-49ba-b16e-0fe5b4f4804d" | "75de6e74-a2e0-47da-ad1e-df1c833af92c" |
        | "sessionCourseAssociation"              | "courseOfferings"                        | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getCourseOfferings"                        | "67bfb073-bd36-0771-8d5a-bba7ba03f8fb" | "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92" | "c549e272-9a7b-4c02-aff7-b105ed76c904" |
        | "staffEducationOrganizationAssociation" | "staffEducationOrgAssignmentAssociations" | "staffReference"                 | "staff"                 | "staff"                  | "getStaff"                 | "getStaffEducationOrgAssignmentAssociations" | "0966614a-6c5d-4345-b451-7ec991823ac5" | "45d6c371-e7f1-4fa8-899a-e9f2309cbe4e" | "527406fd-0f6c-43b7-9dab-fab504c87c7f" |
        | "staffEducationOrganizationAssociation" | "staffEducationOrgAssignmentAssociations" | "educationOrganizationReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getStaffEducationOrgAssignmentAssociations" | "0966614a-6c5d-4345-b451-7ec991823ac5" | "bd086bae-ee82-4cf2-baf9-221a9407ea07" | "b2c6e292-37b0-4148-bf75-c98a2fcc905f" |
        | "studentAssessmentAssociation"          | "studentAssessments"                   | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentAssessments"          | "1e0ddefb-6b61-4f7d-b8c3-33bb5676115a" | "7afddec3-89ec-402c-8fe6-cced79ae3ef5" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentAssessmentAssociation"          | "studentAssessments"                   | "assessmentId"                   | "assessment"            | "assessments"            | "getAssessment"            | "getStudentAssessments"          | "1e0ddefb-6b61-4f7d-b8c3-33bb5676115a" | "6a53f63e-deb8-443d-8138-fc5a7368239c" | "dd9165f2-65fe-4e27-a8ac-bec5f4b757f6" |
#mark
        | "studentSchoolAssociation"              | "studentSchoolAssociations"               | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentSchoolAssociations"              | "122a340e-e237-4766-98e3-4d2d67786572" | "714c1304-8a04-4e23-b043-4ad80eb60992" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentSchoolAssociation"              | "studentSchoolAssociations"               | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getStudentSchoolAssociations"              | "122a340e-e237-4766-98e3-4d2d67786572" | "eb3b8c35-f582-df23-e406-6947249a19f2" | "fdacc41b-8133-f12d-5d47-358e6c0c791c" |
        
        | "studentSectionAssociation"             | "studentSectionAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentSectionAssociations"             | "4efb4b14-bc49-f388-0000-0000c9355702" | "4efb3a11-bc49-f388-0000-0000c93556fb" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentSectionAssociation"             | "studentSectionAssociations"              | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getStudentSectionAssociations"             | "4efb4b14-bc49-f388-0000-0000c9355702" | "4efb4292-bc49-f388-0000-0000c9355701" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        
        | "studentSectionGradebookEntry"          | "studentGradebookEntries"               | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentGradebookEntries"         | "2713b97a-5632-44a5-8e04-031074bcb326" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "7a86a6a7-1f80-4581-b037-4a9328b9b650" |
        | "studentSectionGradebookEntry"          | "studentGradebookEntries"               | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getStudentGradebookEntries"         | "2713b97a-5632-44a5-8e04-031074bcb326" | "706ee3be-0dae-4e98-9525-f564e05aa388" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        | "studentSectionGradebookEntry"          | "studentGradebookEntries"               | "gradebookEntryId"               | "gradebookEntry"        | "gradebookEntries"       | "getGradebookEntry"        | "getStudentGradebookEntries"         | "2713b97a-5632-44a5-8e04-031074bcb326" | "008fd89d-88a2-43aa-8af1-74ac16a29380" | "e49dc00c-182d-4f22-98c5-3d35b5f6d993" |
        
        | "studentTranscriptAssociation"          | "courseTranscripts"                    | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getCourseTranscripts"          | "09eced61-edd9-4826-a7bc-137ffecda877" | "53777181-3519-4111-9210-529350429899" | "93d33f0b-0f2e-43a2-b944-7d182253a79a" |
        | "studentTranscriptAssociation"          | "courseTranscripts"                    | "studentId"                      | "student"               | "students"               | "getStudent"               | "getCourseTranscripts"          | "09eced61-edd9-4826-a7bc-137ffecda877" | "e1af7127-743a-4437-ab15-5b0dacd1bde0" | "714c1304-8a04-4e23-b043-4ad80eb60992" |
        
        | "studentParentAssociation"              | "studentParentAssociations"               | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentParentAssociations"              | "3722a00a-b6d3-4003-84c7-71cbad22dbae" | "fe7719d2-7e7d-457f-843f-76734db99388" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentParentAssociation"              | "studentParentAssociations"               | "parentId"                       | "parent"                | "parents"                | "getParent"                | "getStudentParentAssociations"              | "3722a00a-b6d3-4003-84c7-71cbad22dbae" | "0e950fce-4e47-4000-836d-c5f566fe2d74" | "eb4d7e1b-7bed-890a-cddf-cdb25a29fc2d" |
        
        | "teacherSchoolAssociation"              | "teacherSchoolAssociations"               | "teacherId"                      | "teacher"               | "teachers"               | "getTeacher"               | "getTeacherSchoolAssociations"              | "52c1f410-602e-46b6-9b40-77bf55d77568" | "244520d2-8c6b-4a1e-b35e-d67819ec0211" | "fa45033c-5517-b14b-1d39-c9442ba95782" |
        | "teacherSchoolAssociation"              | "teacherSchoolAssociations"               | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getTeacherSchoolAssociations"              | "52c1f410-602e-46b6-9b40-77bf55d77568" | "0f464187-30ff-4e61-a0dd-74f45e5c7a9d" | "eb3b8c35-f582-df23-e406-6947249a19f2" |
        
        | "teacherSectionAssociation"             | "teacherSectionAssociations"              | "teacherId"                      | "teacher"               | "teachers"               | "getTeacher"               | "getTeacherSectionAssociations"             | "660315c1-cf34-4904-b9f8-b5fb678c62d4" | "eb424dcc-6cff-a69b-c1b3-2b1fc86b2c94" | "244520d2-8c6b-4a1e-b35e-d67819ec0211" |
        | "teacherSectionAssociation"             | "teacherSectionAssociations"              | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getTeacherSectionAssociations"             | "660315c1-cf34-4904-b9f8-b5fb678c62d4" | "4efb4262-bc49-f388-0000-0000c9355700" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        
#Patrick
        | "studentDisciplineIncidentAssociation"  | "studentDisciplineIncidentAssociations"   | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentDisciplineIncidentAssociations"  | "0e26de6c-225b-9f67-9224-5113ad50a03b" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "41df2791-b33c-4b10-8de6-a24963bbd3dd" |
        | "studentDisciplineIncidentAssociation"  | "studentDisciplineIncidentAssociations"   | "disciplineIncidentId"           | "disciplineIncident"    | "disciplineIncidents"    | "getDisciplineIncident"    | "getStudentDisciplineIncidentAssociations"  | "0e26de6c-225b-9f67-9224-5113ad50a03b" | "0e26de79-222a-5e67-9201-5113ad50a03b" | "0e26de79-22ea-5d67-9201-5113ad50a03b" |
        | "cohort"                                | "cohorts"                                 | "educationOrgId"                 | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getCohorts"                                | "b408635d-8fd5-11e1-86ec-0021701f543f" | "b1bd3db6-d020-4651-b1b8-a8dba688d9e1" | "6756e2b9-aba1-4336-80b8-4a5dde3c63fe" |
        | "studentCohortAssociation"              | "studentCohortAssociations"               | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentCohortAssociations"              | "b40e2fc7-8fd5-11e1-86ec-0021701f543f" | "9f4019ca-dd53-4027-b11c-fc151268fafd" | "ace1dc53-8c1d-4c01-b922-c3ebb7ff5be8" |
        | "studentCohortAssociation"              | "studentCohortAssociations"               | "cohortId"                       | "cohort"                | "cohorts"                | "getCohort"                | "getStudentCohortAssociations"              | "b40c5b02-8fd5-11e1-86ec-0021701f543f" | "b408635d-8fd5-11e1-86ec-0021701f543f" | "b40926af-8fd5-11e1-86ec-0021701f543f" |
        | "studentProgramAssociation"             | "studentProgramAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentProgramAssociations"             | "b3f68907-8fd5-11e1-86ec-0021701f543f" | "0f0d9bac-0081-4900-af7c-d17915e02378" | "dd4068df-0bea-4280-bbac-fbc736eea54d" |
        | "studentProgramAssociation"             | "studentProgramAssociations"              | "programId"                      | "program"               | "programs"               | "getProgram"               | "getStudentProgramAssociations"             | "b3f6fe38-8fd5-11e1-86ec-0021701f543f" | "9b8c3aab-8fd5-11e1-86ec-0021701f543f" | "9b8cafdc-8fd5-11e1-86ec-0021701f543f" |
