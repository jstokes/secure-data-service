@smoke @RALLY_US209
Feature: As an SLI application, I want to be able to perform CRUD operations on association resources.
  This means I want to be able to perform CRUD on all association entities
  and verify that the correct links are made available.

  Background: Logged in as IT Admin Rick Rogers
    Given I am logged in using "rrogers" "rrogers1234" to realm "IL"
    And format "application/vnd.slc+json"

    Scenario Outline: CRUD round trip for an association entity
      # Create
      Given a valid association json document for <ASSOC TYPE>
      When I navigate to POST "/<ASSOC URI>"
      Then I should receive a return code of 201
      And I should receive a new ID for the association I just created
      # Read
      When I navigate to GET "/<ASSOC URI>/<NEWLY CREATED ASSOC ID>"
      Then I should receive a return code of 200
      And the response should contain the appropriate fields and values
      And "entityType" should be "<ASSOC TYPE>"
      And "<UPDATE FIELD>" should be "<OLD VALUE>"
      # Update
      When I set the "<UPDATE FIELD>" to "<NEW VALUE>"
      And I navigate to PUT "/<ASSOC URI>/<NEWLY CREATED ASSOC ID>"
      Then I should receive a return code of 204
      And I navigate to GET "/<ASSOC URI>/<NEWLY CREATED ASSOC ID>"
      And "<UPDATE FIELD>" should be "<NEW VALUE>"
      # Delete
      When I navigate to DELETE "/<ASSOC URI>/<NEWLY CREATED ASSOC ID>"
      Then I should receive a return code of 204
      And I navigate to GET "/<ASSOC URI>/<NEWLY CREATED ASSOC ID>"
      And I should receive a return code of 404

    Examples:
      | ASSOC TYPE                             | ASSOC URI                                | UPDATE FIELD             | OLD VALUE              | NEW VALUE              |
      | courseOffering                         | courseOfferings                          | localCourseTitle         | German 101 - Intro     | German 111 - Intro     |
      | staffCohortAssociation                 | staffCohortAssociations                  | endDate                  | 2012-03-29             | 2013-03-29             |
      | staffEducationOrganizationAssociation  | staffEducationOrgAssignmentAssociations  | beginDate                | 2011-01-13             | 2011-02-15             |
      | staffProgramAssociation                | staffProgramAssociations                 | endDate                  | 2012-12-31             | 2012-02-01             |
      | studentAssessmentAssociation           | studentAssessments                       | retestIndicator          | 1st Retest             | 2nd Retest             |
      | studentCohortAssociation               | studentCohortAssociations                | beginDate                | 2012-02-29             | 2011-12-01             |
      | studentDisciplineIncidentAssociation   | studentDisciplineIncidentAssociations    | studentParticipationCode | Reporter               | Witness                |
      | studentParentAssociation               | studentParentAssociations                | livesWith                | true                   | false                  |
      | studentProgramAssociation              | studentProgramAssociations               | reasonExited             | Refused services       | Expulsion              |
      | studentSchoolAssociation               | studentSchoolAssociations                | entryGradeLevel          | First grade            | Third grade            |
      | studentSectionAssociation              | studentSectionAssociations               | homeroomIndicator        | true                   | false                  |
      | studentTranscriptAssociation           | courseTranscripts                        | finalLetterGradeEarned   | A                      | B                      |
      | teacherSchoolAssociation               | teacherSchoolAssociations                | programAssignment        | Regular Education      | Special Education      |
      | teacherSectionAssociation              | teacherSectionAssociations               | classroomPosition        | Teacher of Record      | Assistant Teacher      |

    Scenario Outline: Read All as State level Staff
     Given my contextual access is defined by table:
    |Context                | Ids                                |
    |schools                |b1bd3db6-d020-4651-b1b8-a8dba688d9e1|
    |educationOrganizations |b1bd3db6-d020-4651-b1b8-a8dba688d9e1|
    |staff                  |85585b27-5368-4f10-a331-3abcaf3a3f4c|
      And parameter "limit" is "0"
      When I navigate to GET "/<ASSOC URI>"
      Then I should receive a return code of 200
      And I should receive a collection of "<COUNT>" entities
      And each entity's "entityType" should be "<ASSOC TYPE>"
      And uri was rewritten to <REWRITE URI>

    Examples:
      | ASSOC TYPE                             | ASSOC URI                                | COUNT | REWRITE URI |
      | courseOffering                         | courseOfferings                          | 0     |"/schools/@ids/courseOfferings"|
      | staffCohortAssociation                 | staffCohortAssociations                  | 3     |"/staff/@ids/staffCohortAssociations"|
      | staffEducationOrganizationAssociation  | staffEducationOrgAssignmentAssociations  | 1     |"/staff/@ids/staffEducationOrgAssignmentAssociations"|
      | staffProgramAssociation                | staffProgramAssociations                 | 3     |"/staff/@ids/staffProgramAssociations"|
      | studentAssessmentAssociation           | studentAssessments                       | 0     |"/schools/@ids/studentSchoolAssociations/students/studentAssessments"|
      | studentCohortAssociation               | studentCohortAssociations                | 9     |"/staff/@ids/staffCohortAssociations/cohorts/studentCohortAssociations"|
      | studentDisciplineIncidentAssociation   | studentDisciplineIncidentAssociations    | 0     |"/staff/@ids/disciplineIncidents/studentDisciplineIncidentAssociations"|
      | studentParentAssociation               | studentParentAssociations                | 0     |"/schools/@ids/studentSchoolAssociations/students/studentParentAssociations"|
      | studentProgramAssociation              | studentProgramAssociations               | 10    |"/staff/@ids/staffProgramAssociations/programs/studentProgramAssociations"|
      | studentSchoolAssociation               | studentSchoolAssociations                | 0     |"/schools/@ids/studentSchoolAssociations"|
      | studentSectionAssociation              | studentSectionAssociations               | 0     |"/schools/@ids/sections/studentSectionAssociations"|
      | studentTranscriptAssociation           | courseTranscripts                        | 0     |"/schools/@ids/studentSchoolAssociations/students/courseTranscripts"|
      | teacherSchoolAssociation               | teacherSchoolAssociations                | 0     |"/schools/@ids/teacherSchoolAssociations"|
      | teacherSectionAssociation              | teacherSectionAssociations               | 0     |"/schools/@ids/teacherSchoolAssociations/teachers/teacherSectionAssociations"|

    Scenario Outline: Read All as School level Teacher
    	Given I am logged in using "linda.kim" "linda.kim1234" to realm "IL"
    And format "application/vnd.slc+json"
    And my contextual access is defined by table:
    |Context                | Ids                                |
    |schools	                |ec2e4218-6483-4e9c-8954-0aecccfd4731|
    |educationOrganizations	|ec2e4218-6483-4e9c-8954-0aecccfd4731|
    |staff	                  |67ed9078-431a-465e-adf7-c720d08ef512|
    |teachers               |67ed9078-431a-465e-adf7-c720d08ef512|
    |sections |706ee3be-0dae-4e98-9525-f564e05aa388,f048354d-dbcb-0214-791d-b769f521210d,ceffbb26-1327-4313-9cfc-1c3afd38122e,7847b027-687d-46f0-bc1a-36d3c16956aa|
      And parameter "limit" is "0"
      When I navigate to GET "/<ASSOC URI>"
      Then I should receive a return code of 200
      And I should receive a collection of "<COUNT>" entities
      And each entity's "entityType" should be "<ASSOC TYPE>"
      And uri was rewritten to <REWRITE URI>

    Examples:
      | ASSOC TYPE                             | ASSOC URI                                | COUNT | REWRITE URI |
      | courseOffering                         | courseOfferings                          | 10    |"/schools/@ids/courseOfferings"|
      | staffCohortAssociation                 | staffCohortAssociations                  | 0     |"/staff/@ids/staffCohortAssociations"|
      | staffEducationOrganizationAssociation  | staffEducationOrgAssignmentAssociations  | 0     |"/educationOrganizations/@ids/staffEducationOrgAssignmentAssociations"|
      | staffProgramAssociation                | staffProgramAssociations                 | 0     |"/staff/@ids/staffProgramAssociations"|
      | studentAssessmentAssociation           | studentAssessments                       | 3     |"/sections/@ids/studentSectionAssociations/students/studentAssessments"|
      | studentCohortAssociation               | studentCohortAssociations                | 0     |"/staff/@ids/staffCohortAssociations/cohorts/studentCohortAssociations"|
      | studentDisciplineIncidentAssociation   | studentDisciplineIncidentAssociations    | 0     |"/staff/@ids/disciplineIncidents/studentDisciplineIncidentAssociations"|
      | studentParentAssociation               | studentParentAssociations                | 2     |"/sections/@ids/studentSectionAssociations/students/studentParentAssociations"|
      | studentProgramAssociation              | studentProgramAssociations               | 0     |"/staff/@ids/staffProgramAssociations/programs/studentProgramAssociations"|
      | student                                | students                                 | 29    |"/sections/@ids/studentSectionAssociations/students"|
      | studentSchoolAssociation               | studentSchoolAssociations                | 58    |"/sections/@ids/studentSectionAssociations/students/studentSchoolAssociations"|
      | studentSectionAssociation              | studentSectionAssociations               | 31    |"/sections/@ids/studentSectionAssociations"|
      | studentTranscriptAssociation           | courseTranscripts                        | 1     |"/sections/@ids/studentSectionAssociations/students/courseTranscripts"|
      | teacher                                | teachers                                 | 1     |"/schools/@ids/teacherSchoolAssociations/teachers"|
      | teacherSchoolAssociation               | teacherSchoolAssociations                | 1     |"/teachers/@ids/teacherSchoolAssociations"|
      | teacherSectionAssociation              | teacherSectionAssociations               | 4     |"/teachers/@ids/teacherSectionAssociations"|

    Scenario Outline: Unhappy paths: invalid or inaccessible references
      # Log in as a user with less accessible data
      Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
      And format "application/vnd.slc+json"
      # Read with invalid reference
      When I navigate to GET "/<ASSOC URI>/<INVALID REFERENCE>"
      Then I should receive a return code of 404
      # Update with invalid reference
      Given a valid association json document for <ASSOC TYPE>
      When I set the "<UPDATE FIELD>" to "<NEW VALUE>"
      And I navigate to PUT "/<ASSOC URI>/<INVALID REFERENCE>"
      Then I should receive a return code of 404
      # Update by setting end points to invalid references
      When I navigate to GET "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      And I set the "<END POINT 1 FIELD>" to "<INVALID REFERENCE>"
      And I navigate to PUT "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      Then I should receive a return code of 403
      And the error message should indicate "<BAD REFERENCE>"
      When I navigate to GET "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      And I set the "<END POINT 2 FIELD>" to "<INVALID REFERENCE>"
      And I navigate to PUT "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      Then I should receive a return code of 403
      And the error message should indicate "<BAD REFERENCE>"
      # Delete with invalid reference
      When I navigate to DELETE "/<ASSOC URI>/<INVALID REFERENCE>"
      Then I should receive a return code of 404
      # Create with inaccessible endpoint 1
      Given a valid association json document for <ASSOC TYPE>
      When I set the "<END POINT 1 FIELD>" to "<INACCESSIBLE END POINT 1 ID>"
      When I navigate to POST "/<ASSOC URI>"
      Then I should receive a return code of 403
      # Create with inaccessible endpoint 2
      Given a valid association json document for <ASSOC TYPE>
      When I set the "<END POINT 2 FIELD>" to "<INACCESSIBLE END POINT 2 ID>"
      When I navigate to POST "/<ASSOC URI>"
      Then I should receive a return code of 403
      # Update wby setting end points to inaccessible references
      When I navigate to GET "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      And I set the "<END POINT 1 FIELD>" to "<INACCESSIBLE END POINT 1 ID>"
      And I navigate to PUT "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      Then I should receive a return code of 403
      When I navigate to GET "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      And I set the "<END POINT 2 FIELD>" to "<INACCESSIBLE END POINT 2 ID>"
      And I navigate to PUT "/<ASSOC URI>/<ASSOC ID FOR UPDATE>"
      Then I should receive a return code of 403

    Examples:
      | ASSOC TYPE                             | ASSOC URI                                | ASSOC ID FOR UPDATE                    | UPDATE FIELD             | NEW VALUE              | END POINT 1 FIELD     | END POINT 2 FIELD              | INACCESSIBLE END POINT 1 ID            | INACCESSIBLE END POINT 2 ID            |
      | courseOffering                         | courseOfferings                          | <b360e8e8-54d1-4b00-952a-b817f91035ed> | localCourseCode          | LCCGR2                 | sessionId             | courseId                       | <389b0caa-dcd2-4e84-93b7-daa4a6e9b18e> | <e31f7583-417e-4c42-bd55-0bbe7518edf8> |
      | staffCohortAssociation                 | staffCohortAssociations                  | <b4e31b1a-8e55-8803-722c-14d8087c0712> | beginDate                | 2011-02-01             | staffId               | cohortId                       | <04f708bc-928b-420d-a440-f1592a5d1073> | <b1bd3db6-d020-4651-b1b8-a8dba688d9e1> |
      | staffEducationOrganizationAssociation  | staffEducationOrgAssignmentAssociations  | <0966614a-6c5d-4345-b451-7ec991823ac5> | beginDate                | 2011-02-15             | staffReference        | educationOrganizationReference | <04f708bc-928b-420d-a440-f1592a5d1073> | <b2c6e292-37b0-4148-bf75-c98a2fcc905f> |
      | staffProgramAssociation                | staffProgramAssociations                 | <9bf7591b-8fd5-11e1-86ec-0021701f543f> | endDate                  | 2012-02-01             | staffId               | programId                      | <04f708bc-928b-420d-a440-f1592a5d1073> | <9e909dfc-ba61-406d-bbb4-c953e8946f8b> |
      | studentAssessmentAssociation           | studentAssessments                       | <e85b5aa7-465a-6dd3-8ffb-d02461ed79f8> | retestIndicator          | 2nd Retest             | studentId             | assessmentId                   | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <abc16592-7d7e-5d27-a87d-dfc7fcb12346> |
      | studentCohortAssociation               | studentCohortAssociations                | <b40c5b02-8fd5-11e1-86ec-0021701f543f> | beginDate                | 2011-12-01             | studentId             | cohortId                       | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <b1bd3db6-d020-4651-b1b8-a8dba688d9e1> |
      | studentDisciplineIncidentAssociation   | studentDisciplineIncidentAssociations    | <0e26de6c-225b-9f67-9625-5113ad50a03b> | studentParticipationCode | Witness                | studentId             | disciplineIncidentId           | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <0e26de79-22aa-5d67-9201-5113ad50a03b> |
      | studentParentAssociation               | studentParentAssociations                | <c5aa1969-492a-5150-8479-71bfc4d57f1e> | livesWith                | false                  | studentId             | parentId                       | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <cb7a932f-2d44-800c-cd5a-cdb25a29fc75> |
      | studentProgramAssociation              | studentProgramAssociations               | <b3f68907-8fd5-11e1-86ec-0021701f543f> | reasonExited             | Expulsion              | studentId             | programId                      | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <9e909dfc-ba61-406d-bbb4-c953e8946f8b> |
      | studentSchoolAssociation               | studentSchoolAssociations                | <e7e0e926-874e-4d05-9177-9776d44c5fb5> | entryGradeLevel          | Third grade            | studentId             | schoolId                       | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <0f464187-30ff-4e61-a0dd-74f45e5c7a9d> |
      | studentSectionAssociation              | studentSectionAssociations               | <4ae72560-3518-4576-a35e-a9607668c9ad> | homeroomIndicator        | false                  | studentId             | sectionId                      | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <a47eb9aa-1c97-4c8e-9d0a-45689a66d4f8> |
      | studentTranscriptAssociation           | courseTranscripts                        | <f11a2a30-d4fd-4400-ae18-353c00d581a2> | finalLetterGradeEarned   | B                      | studentId             | courseId                       | <737dd4c1-86bd-4892-b9e0-0f24f76210be> | <e31f7583-417e-4c42-bd55-0bbe7518edf8> |
      | teacherSchoolAssociation               | teacherSchoolAssociations                | <26a4a0fc-fad4-45f4-a00d-285acd1f83eb> | programAssignment        | Special Education      | teacherId             | schoolId                       | <04f708bc-928b-420d-a440-f1592a5d1073> | <0f464187-30ff-4e61-a0dd-74f45e5c7a9d> |
      | teacherSectionAssociation              | teacherSectionAssociations               | <32b86a2a-e55c-4689-aedf-4b676f3da3fc> | classroomPosition        | Assistant Teacher      | teacherId             | sectionId                      | <04f708bc-928b-420d-a440-f1592a5d1073> | <a47eb9aa-1c97-4c8e-9d0a-45689a66d4f8> |