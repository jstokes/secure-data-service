Feature: As an SLI application, I want entity references displayed as usable links with exlicit link names
This means that when links are requested in a GET request, a link should be present that represents each reference field
and that the links are valid

Background: Nothing yet
    Given I am logged in using "demo" "demo1234" to realm "SLI"

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
#       | "application/xml"          |              |

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
        | "studentAcademicRecord"                 | "studentAcademicRecords"                 | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentAcademicRecords"                 | "3a0cc576-fe7f-40bd-b86c-ca861244db12" | "4efb3a11-bc49-f388-0000-0000c93556fb" | "7a86a6a7-1f80-4581-b037-4a9328b9b650" |
        | "studentAcademicRecord"                 | "studentAcademicRecords"                 | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getStudentAcademicRecords"                 | "3a0cc576-fe7f-40bd-b86c-ca861244db12" | "bbea7ac0-a016-4ece-bb1b-47b1fc251d56" | "389b0caa-dcd2-4e84-93b7-daa4a6e9b18e" |
        | "attendance"                            | "attendances"                            | "studentId"                      | "student"               | "students"               | "getStudent"               | "getAttendances"                            | "4beb72d4-0f76-4071-92b4-61982dba7a7b" | "7a86a6a7-1f80-4581-b037-4a9328b9b650" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "educationOrganization"                 | "educationOrganizations"                 | "parentEducationAgencyReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getEducationOrganizations"                 | "67ce204b-9999-4a11-aabe-000000000001" | "67ce204b-9999-4a11-aabe-000000000000" | "67ce204b-9999-4a11-aabe-000000000002" |
        | "gradebookEntry"                        | "gradebookEntries"                       | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getGradebookEntries"                       | "008fd89d-88a2-43aa-8af1-74ac16a29380" | "706ee3be-0dae-4e98-9525-f564e05aa388" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        | "school"                                | "schools"                                | "parentEducationAgencyReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getSchools"                                | "67ce204b-9999-4a11-aaab-000000000002" | "1d303c61-88d4-404a-ba13-d7c5cc324bc5" | "67ce204b-9999-4a11-aabe-000000000002" |
        | "schoolSessionAssociation"              | "schoolSessionAssociations"              | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getSchoolSessionAssociations"              | "67ce204b-9999-4a11-aacd-000000000001" | "67ce204b-9999-4a11-aaab-000000000008" | "67ce204b-9999-4a11-aaab-000000000002" |
        | "schoolSessionAssociation"              | "schoolSessionAssociations"              | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getSchoolSessionAssociations"              | "67ce204b-9999-4a11-aacd-000000000001" | "67ce204b-9999-4a11-aacb-000000000000" | "bbea7ac0-a016-4ece-bb1b-47b1fc251d56" |
        | "section"                               | "sections"                               | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getSections"                               | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" | "eb3b8c35-f582-df23-e406-6947249a19f2" | "fdacc41b-8133-f12d-5d47-358e6c0c791c" |
        | "section"                               | "sections"                               | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getSections"                               | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" | "389b0caa-dcd2-4e84-93b7-daa4a6e9b18e" | "bbea7ac0-a016-4ece-bb1b-47b1fc251d56" |
        | "section"                               | "sections"                               | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getSections"                               | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" | "53777181-3519-4111-9210-529350429899" | "53777181-3519-4111-9210-529350429899" |
        | "sectionAssessmentAssociation"          | "sectionAssessmentAssociations"          | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getSectionAssessmentAssociations"          | "df3e6d22-1c3f-460a-870a-49aba80bfb18" | "eb4d7e1b-7bed-890a-d574-cdb25a29fc2d" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        | "sectionAssessmentAssociation"          | "sectionAssessmentAssociations"          | "assessmentId"                   | "assessment"            | "assessments"            | "getAssessment"            | "getSectionAssessmentAssociations"          | "df3e6d22-1c3f-460a-870a-49aba80bfb18" | "6a53f63e-deb8-443d-8138-fc5a7368239c" | "dd9165f2-65fe-4e27-a8ac-bec5f4b757f6" |
        | "sessionCourseAssociation"              | "courseOfferings"              			 | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getCourseOfferings"              			| "9ff65bb1-ef8b-4588-83af-d58f39c1bf68" | "93d33f0b-0f2e-43a2-b944-7d182253a79a" | "53777181-3519-4111-9210-529350429899" |
        | "sessionCourseAssociation"              | "courseOfferings"              			 | "sessionId"                      | "session"               | "sessions"               | "getSession"               | "getCourseOfferings"              			| "9ff65bb1-ef8b-4588-83af-d58f39c1bf68" | "389b0caa-dcd2-4e84-93b7-daa4a6e9b18e" | "bbea7ac0-a016-4ece-bb1b-47b1fc251d56" |
        | "staffEducationOrganizationAssociation" | "staffEducationOrgAssignmentAssociations" | "staffReference"                 | "staff"                 | "staff"                  | "getStaff"                 | "getStaffEducationOrgAssignmentAssociations" | "c3c19cba-2ae1-4c9f-8497-40d273db5bdf" | "269be4c9-a806-4051-a02d-15a7af3ffe3e" | "0e26de79-222a-4d67-9201-5113ad50a03b" |
        | "staffEducationOrganizationAssociation" | "staffEducationOrgAssignmentAssociations" | "educationOrganizationReference" | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getStaffEducationOrgAssignmentAssociations" | "c3c19cba-2ae1-4c9f-8497-40d273db5bdf" | "4f0c9368-8488-7b01-0000-000059f9ba56" | "67ce204b-9999-4a11-aabe-000000000002" |
        | "studentAssessmentAssociation"          | "studentAssessments"          			 | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentAssessments"         		 	| "1e0ddefb-6b61-4f7d-b8c3-33bb5676115a" | "7afddec3-89ec-402c-8fe6-cced79ae3ef5" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentAssessmentAssociation"          | "studentAssessments"          			 | "assessmentId"                   | "assessment"            | "assessments"            | "getAssessment"            | "getStudentAssessments"          			| "1e0ddefb-6b61-4f7d-b8c3-33bb5676115a" | "6a53f63e-deb8-443d-8138-fc5a7368239c" | "dd9165f2-65fe-4e27-a8ac-bec5f4b757f6" |
        | "studentSchoolAssociation"              | "studentSchoolAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentSchoolAssociations"              | "122a340e-e237-4766-98e3-4d2d67786572" | "714c1304-8a04-4e23-b043-4ad80eb60992" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentSchoolAssociation"              | "studentSchoolAssociations"              | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getStudentSchoolAssociations"              | "122a340e-e237-4766-98e3-4d2d67786572" | "eb3b8c35-f582-df23-e406-6947249a19f2" | "fdacc41b-8133-f12d-5d47-358e6c0c791c" |
        | "studentSectionAssociation"             | "studentSectionAssociations"             | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentSectionAssociations"             | "4efb4b14-bc49-f388-0000-0000c9355702" | "4efb3a11-bc49-f388-0000-0000c93556fb" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentSectionAssociation"             | "studentSectionAssociations"             | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getStudentSectionAssociations"             | "4efb4b14-bc49-f388-0000-0000c9355702" | "4efb4292-bc49-f388-0000-0000c9355701" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        | "studentSectionGradebookEntry"          | "studentGradebookEntries"         		 | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentGradebookEntries"         		| "2713b97a-5632-44a5-8e04-031074bcb326" | "74cf790e-84c4-4322-84b8-fca7206f1085" | "7a86a6a7-1f80-4581-b037-4a9328b9b650" |
        | "studentSectionGradebookEntry"          | "studentGradebookEntries"         		 | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getStudentGradebookEntries"         		| "2713b97a-5632-44a5-8e04-031074bcb326" | "706ee3be-0dae-4e98-9525-f564e05aa388" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        | "studentSectionGradebookEntry"          | "studentGradebookEntries"         		 | "gradebookEntryId"               | "gradebookEntry"        | "gradebookEntries"       | "getGradebookEntry"        | "getStudentGradebookEntries"         		| "2713b97a-5632-44a5-8e04-031074bcb326" | "008fd89d-88a2-43aa-8af1-74ac16a29380" | "e49dc00c-182d-4f22-98c5-3d35b5f6d993" |
        | "studentTranscriptAssociation"          | "courseTranscripts"          			 | "courseId"                       | "course"                | "courses"                | "getCourse"                | "getCourseTranscripts"          			| "09eced61-edd9-4826-a7bc-137ffecda877" | "53777181-3519-4111-9210-529350429899" | "93d33f0b-0f2e-43a2-b944-7d182253a79a" |
        | "studentTranscriptAssociation"          | "courseTranscripts"          			 | "studentId"                      | "student"               | "students"               | "getStudent"               | "getCourseTranscripts"          			| "09eced61-edd9-4826-a7bc-137ffecda877" | "e1af7127-743a-4437-ab15-5b0dacd1bde0" | "714c1304-8a04-4e23-b043-4ad80eb60992" |
        | "studentParentAssociation"              | "studentParentAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentParentAssociations"              | "3722a00a-b6d3-4003-84c7-71cbad22dbae" | "fe7719d2-7e7d-457f-843f-76734db99388" | "74cf790e-84c4-4322-84b8-fca7206f1085" |
        | "studentParentAssociation"              | "studentParentAssociations"              | "parentId"                       | "parent"                | "parents"                | "getParent"                | "getStudentParentAssociations"              | "3722a00a-b6d3-4003-84c7-71cbad22dbae" | "0e950fce-4e47-4000-836d-c5f566fe2d74" | "eb4d7e1b-7bed-890a-cddf-cdb25a29fc2d" |
        | "teacherSchoolAssociation"              | "teacherSchoolAssociations"              | "teacherId"                      | "teacher"               | "teachers"               | "getTeacher"               | "getTeacherSchoolAssociations"              | "52c1f410-602e-46b6-9b40-77bf55d77568" | "244520d2-8c6b-4a1e-b35e-d67819ec0211" | "fa45033c-5517-b14b-1d39-c9442ba95782" |
        | "teacherSchoolAssociation"              | "teacherSchoolAssociations"              | "schoolId"                       | "school"                | "schools"                | "getSchool"                | "getTeacherSchoolAssociations"              | "52c1f410-602e-46b6-9b40-77bf55d77568" | "0f464187-30ff-4e61-a0dd-74f45e5c7a9d" | "eb3b8c35-f582-df23-e406-6947249a19f2" |
        | "teacherSectionAssociation"             | "teacherSectionAssociations"             | "teacherId"                      | "teacher"               | "teachers"               | "getTeacher"               | "getTeacherSectionAssociations"             | "660315c1-cf34-4904-b9f8-b5fb678c62d4" | "eb424dcc-6cff-a69b-c1b3-2b1fc86b2c94" | "244520d2-8c6b-4a1e-b35e-d67819ec0211" |
        | "teacherSectionAssociation"             | "teacherSectionAssociations"             | "sectionId"                      | "section"               | "sections"               | "getSection"               | "getTeacherSectionAssociations"             | "660315c1-cf34-4904-b9f8-b5fb678c62d4" | "4efb4262-bc49-f388-0000-0000c9355700" | "58c9ef19-c172-4798-8e6e-c73e68ffb5a3" |
        | "studentDisciplineIncidentAssociation"  | "studentDisciplineIncidentAssociations"  | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentDisciplineIncidentAssociations"  | "0e26de6c-225b-9f67-8e23-5113ad50a03b" | "e0e99028-6360-4247-ae48-d3bb3ecb606a" | "e1af7127-743a-4437-ab15-5b0dacd1bde0" |
        | "studentDisciplineIncidentAssociation"  | "studentDisciplineIncidentAssociations"  | "disciplineIncidentId"           | "disciplineIncident"    | "disciplineIncidents"    | "getDisciplineIncident"    | "getStudentDisciplineIncidentAssociations"  | "0e26de6c-225b-9f67-8e23-5113ad50a03b" | "0e26de79-22aa-5d67-9201-5113ad50a03b" | "0e26de79-226a-5d67-9201-5113ad50a03b" |
        | "cohort"                                | "cohorts"                                | "educationOrgId"                 | "educationOrganization" | "educationOrganizations" | "getEducationOrganization" | "getCohorts"                                | "7e9915ed-ea6f-4e6b-b8b0-aeae20a25826" | "2d7583b1-f8ec-45c9-a6da-acc4e6fde458" | "0a922b8a-7a3b-4320-8b34-0f7749b8b062" |
        | "studentCohortAssociation"              | "studentCohortAssociations"              | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentCohortAssociations"              | "4cfe2c95-09d5-4ba7-a09c-c7aabb8902c3" | "714c1304-8a04-4e23-b043-4ad80eb60992" | "7a86a6a7-1f80-4581-b037-4a9328b9b650" |
        | "studentCohortAssociation"              | "studentCohortAssociations"              | "cohortId"                       | "cohort"                | "cohorts"                | "getCohort"                | "getStudentCohortAssociations"              | "530acadc-f4b2-4dd4-a087-5700e9890548" | "7e9915ed-ea6f-4e6b-b8b0-aeae20a25826" | "a6929135-4782-46f1-ab01-b4df2e6ad093" |
        | "studentProgramAssociation"             | "studentProgramAssociations"             | "studentId"                      | "student"               | "students"               | "getStudent"               | "getStudentProgramAssociations"             | "7ac82cbd-44e7-4bff-aee9-83d4457da4ab" | "714c1304-8a04-4e23-b043-4ad80eb60992" | "c7146300-5bb9-4cc6-8b95-9e401ce34a03" |
        | "studentProgramAssociation"             | "studentProgramAssociations"             | "programId"                      | "program"               | "programs"               | "getProgram"               | "getStudentProgramAssociations"             | "0676342d-ad60-494f-9c2e-2371e18ae4e3" | "e8d33606-d114-4ee4-878b-90ac7fc3df16" | "cb292c7d-3503-414a-92a2-dc76a1585d79" |