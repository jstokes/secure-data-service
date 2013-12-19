@RALLY_US4816
Feature: Odin Data Set Ingestion Correctness and Fidelity
Background: I have a landing zone route configured
Given I am using odin data store 

Scenario: Post Odin Sample Data Set
Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
  And I post "OdinSampleDataSet.zip" file as the payload of the ingestion job
  And the "Midgar" tenant db is empty
  When zip file is scp to ingestion landing zone
  And a batch job for file "OdinSampleDataSet.zip" is completed in database
  And I should not see an error log file created
  And I should not see a warning log file created

Scenario: Verify the sli verification script confirms everything ingested correctly
    Given the edfi manifest that was generated in the 'generated' directory
    And the tenant is 'Midgar'
    Then the sli-verify script completes successfully

Scenario: Verify specific staff document for IL Charter School Educator ingested correctly: Populated Database
  When I can find a "staff" with "body.teacherUniqueStateId" "chartereducator" in tenant db "Midgar"
    Then the "staff" entity "type" should be "teacher"

Scenario: Verify specific staff document for IL Charter School Admin ingested correctly: Populated Database
  When I can find a "staff" with "body.staffUniqueStateId" "charteradmin" in tenant db "Midgar"
    Then the "staff" entity "type" should be "staff"

Scenario: Verify specific staff document for IL Charter School Leader ingested correctly: Populated Database
  When I can find a "staff" with "body.staffUniqueStateId" "charterleader" in tenant db "Midgar"
    Then the "staff" entity "type" should be "staff"

Scenario: Verify specific staff document for IL Charter School Viewer ingested correctly: Populated Database
  When I can find a "staff" with "body.staffUniqueStateId" "charterviewer" in tenant db "Midgar"
    Then the "staff" entity "type" should be "staff"

Scenario: Verify superdoc studentSchoolAssociation references ingested correctly: Populated Database
  When Examining the studentSchoolAssociation collection references
    Then the document references "educationOrganization" "_id" with "body.schoolId"
    And the document references "student" "_id" with "body.studentId"
    And the document references "student" "schools._id" with "body.schoolId"
    And the document references "student" "schools.entryDate" with "body.entryDate"
    #And the document references "student" "schools.entryGradeLevel" with "body.entryGradeLevel"

Scenario: Verify staffEducationOrganizationAssociation references ingested correctly: Populated Database
  When Examining the staffEducationOrganizationAssociation collection references
    Then the document references "educationOrganization" "_id" with "body.educationOrganizationReference"
     And the document references "staff" "_id" with "body.staffReference"

Scenario: Verify teacherSchoolAssociation references ingested correctly: Populated Database
  When Examining the teacherSchoolAssociation collection references
    Then the document references "educationOrganization" "_id" with "body.schoolId"
     And the document references "staff" "_id" with "body.teacherId"

Scenario: Verify the charter school and Education Service Center ingested correctly: Populated Database
  When I can find a "educationOrganization" with "body.stateOrganizationId" "IL-CHARTER-SCHOOL" in tenant db "Midgar"
  Then the "educationOrganization" entity "type" should be "educationOrganization"
  And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "IL-CHARTER-SCHOOL"
    | field                               | value                                       |
    | body.stateOrganizationId            | IL-CHARTER-SCHOOL                           |
    | body.parentEducationAgencyReference | 884daa27d806c2d725bc469b273d840493f84b4d_id |
    | body.organizationCategories         | School                                      |
  And there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "4"
    | field                               | value                       |
    | body.stateOrganizationId            | 4                           |
    | body.organizationCategories         | Education Service Center    |
