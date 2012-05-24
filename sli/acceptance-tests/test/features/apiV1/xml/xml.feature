@RALLY_US209
@RALLY_US210
Feature: As an SLI application, I want to be able to support XML.
  This means the API should support POST, PUT and GET with XML format.

Background: Nothing yet
  Given I am logged in using "rrogers" "rrogers1234" to realm "IL"
  And format "application/xml"

  Scenario Outline: Getting response from GET - Read
    When I navigate to GET "/v1/<ENTITY URI>/<ENTITY ID>"
    Then I should receive a return code of 200
    And I should receive an XML document
    And I should see "<entityType>" is "<ENTITY TYPE>"

  Examples:
    | ENTITY URI                  | ENTITY TYPE                | ENTITY ID                               |
    | assessments                 | assessment                 | dd916592-7d7e-5d27-a87d-dfc7fcb757f6    |
    | schools                     | school                     | 92d6d5a0-852c-45f4-907a-912752831772    |
    | students                    | student                    | 3a8860f9-ffb1-40f2-89b4-84f3ba369204    |
    | studentSectionAssociations  | studentSectionAssociation  | 4ae72560-3518-4576-a35e-a9607668c9ad    |
    | courseOfferings             | courseOffering             | 9ff65bb1-ef8b-4588-83af-d58f39c1bf68    |

  Scenario Outline: Getting response from GET - Read all
    Given parameter "limit" is "0"
    When I navigate to GET "/v1/<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive an XML document
    And I should receive <ENTITY COUNT> entities
    And I should see each entity's "<entityType>" is "<ENTITY TYPE>"

  Examples:
    | ENTITY URI                  | ENTITY TYPE                | ENTITY COUNT    |
    | assessments                 | assessment                 | 3               |
    | schools                     | school                     | 5               |
    | students                    | student                    | 82              |
    | studentSectionAssociations  | studentSectionAssociation  | 263             |
    | courseOfferings             | courseOffering             | 5               |


Scenario: Getting response from POST - Create (school)
  Given a valid XML document for a new school entity
  When I POST the entity to "/v1/schools"
  Then I should receive a return code of 201
  And I should receive an ID for the newly created entity
  When I navigate to GET "/v1/schools/<NEWLY CREATED ENTITY ID>"
  Then I should receive a return code of 200
  And I should see "<nameOfInstitution>" is "Apple Alternative Elementary School"
  And I should see "<organizationCategories><organizationCategories>" is "School"
  And I should find "<address><address>" under "/"
  And I should see "<address><address><streetNumberName>" is "123 Main Street"
#  And I should find 3 "<gradesOffered>" under "<school>"


Scenario: Getting response from PUT - Update (school)
  When I navigate to GET "/v1/schools/<SCHOOL ENTITY TO BE UPDATED>"
  Then I should see "<nameOfInstitution>" is "Sunset Central High School"
  When I change the name to "Updated School Name"
  And I PUT the entity to "/v1/schools/<SCHOOL ENTITY TO BE UPDATED>"
  Then I should receive a return code of 204
  When I navigate to GET "/v1/schools/<SCHOOL ENTITY TO BE UPDATED>"
  Then I should receive an XML document
  Then I should see "<nameOfInstitution>" is "Updated School Name"

Scenario: Applying optional fields
  Given optional field "attendances"
  And optional field "assessments"
  And optional field "gradebook"
  And optional field "transcript"
  And parameter "limit" is "0"
  When I navigate to GET "/v1/sections/<LINDA KIM SECTION ID>/studentSectionAssociations/students"
  Then I should receive an XML document
  And I should receive a return code of 200

  # assessments
  Then I should find 1 "<studentAssessments>" under "<student><studentAssessments>"
  And I should see "<entityType>" is "studentAssessmentAssociation" for the one at position 1
  And I should see "<studentId>" is "<MARVIN MILLER STUDENT ID>" for the one at position 1
  And I should find "<assessments>" under "<student><studentAssessments><studentAssessments>"
  And I should see "<student><studentAssessments><studentAssessments><assessments><entityType>" is "assessment"
  And I should see "<student><studentAssessments><studentAssessments><assessments><gradeLevelAssessed>" is "Twelfth grade"
  And I should find 3 "<studentObjectiveAssessments>" under "<student><studentAssessments><studentAssessments><studentObjectiveAssessments>"
  And I should find 2 "<scoreResults>" under "<student><studentAssessments><studentAssessments><studentObjectiveAssessments><studentObjectiveAssessments><scoreResults>"
  And I should see "<result>" is "80" for the one at position 2

  # gradebook
  Then I should find 3 "<studentGradebookEntries>" under "<student><studentGradebookEntries>"
  And I should see "<entityType>" is "studentSectionGradebookEntry" for the one at position 1
  And I should see "<letterGradeEarned>" is "A" for the one at position 1
  And I should see "<dateFulfilled>" is "2012-02-07" for the one at position 1
  And I should find "<gradebookEntries>" under "<student><studentGradebookEntries><studentGradebookEntries>"
  And I should see "<student><studentGradebookEntries><studentGradebookEntries><gradebookEntries><entityType>" is "gradebookEntry"
  And I should see "<student><studentGradebookEntries><studentGradebookEntries><gradebookEntries><dateAssigned>" is "2012-02-07"

  # transcript
  Then I should find "<transcript>" under "<student>"
  And I should find "<courseTranscripts>" under "<student><transcript>"
  And I should see "<student><transcript><courseTranscripts><courseTranscripts><entityType>" is "studentTranscriptAssociation"
  And I should see "<student><transcript><courseTranscripts><courseTranscripts><finalLetterGradeEarned>" is "B"
  And I should find 2 "<studentSectionAssociations>" under "<student><transcript><studentSectionAssociations>"
  And I should see "<entityType>" is "studentSectionAssociation" for the one at position 1
  And I should find "<sections>" under "<student><transcript><studentSectionAssociations><studentSectionAssociations>"
  And I should see "<student><transcript><studentSectionAssociations><studentSectionAssociations><sections><entityType>" is "section"
  And I should find "<sessions>" under "<student><transcript><studentSectionAssociations><studentSectionAssociations><sections>"
  And I should see "<student><transcript><studentSectionAssociations><studentSectionAssociations><sections><sessions><entityType>" is "session"
  And I should find "<courses>" under "<student><transcript><studentSectionAssociations><studentSectionAssociations><sections>"
  And I should see "<student><transcript><studentSectionAssociations><studentSectionAssociations><sections><courses><entityType>" is "course"

Scenario: Applying optional fields - single student view
  Given optional field "attendances"
  And optional field "assessments"
  And optional field "gradebook"
  And optional field "transcript"
  And parameter "limit" is "0"
  When I navigate to GET "/v1/students/<MARVIN MILLER STUDENT ID>"
  Then I should receive an XML document
  And I should receive a return code of 200

  # attendances
  Then I should find "<attendances>" under "/"
  And I should find 181 "<attendances>" under "<attendances><attendances>"
  And I should see "<date>" is "2011-09-07" for the one at position 2
  And I should see "<event>" is "In Attendance" for the one at position 2

  # assessments
  Then I should find "<studentAssessments>" under "/"
  And I should see "<studentAssessments><studentAssessments><entityType>" is "studentAssessmentAssociation"
  And I should see "<studentAssessments><studentAssessments><studentId>" is "<MARVIN MILLER STUDENT ID>"
  And I should see "<studentAssessments><studentAssessments><assessments><entityType>" is "assessment"
  And I should see "<studentAssessments><studentAssessments><assessments><gradeLevelAssessed>" is "Twelfth grade"
  And I should find 3 "<studentObjectiveAssessments><studentObjectiveAssessments>" under "<studentAssessments><studentAssessments>"
  And I should find 2 "<scoreResults><scoreResults>" under "<studentAssessments><studentAssessments><studentObjectiveAssessments><studentObjectiveAssessments>"
  And I should see "<result>" is "80" for the one at position 2

  # gradebook
  Then I should find 3 "<studentGradebookEntries>" under "<studentGradebookEntries>"
  And I should see "<entityType>" is "studentSectionGradebookEntry" for the one at position 1
  And I should see "<letterGradeEarned>" is "A" for the one at position 1
  And I should see "<dateFulfilled>" is "2012-02-07" for the one at position 1
  And I should find 1 "<gradebookEntries>" under "<studentGradebookEntries><studentGradebookEntries>"
  And I should see "<entityType>" is "gradebookEntry" for the one at position 1
  And I should see "<dateAssigned>" is "2012-02-07" for the one at position 1

  # transcript
  Then I should find "<transcript>" under "/"
  And I should find "<courseTranscripts>" under "<transcript>"
  And I should see "<transcript><courseTranscripts><courseTranscripts><entityType>" is "studentTranscriptAssociation"
  And I should see "<transcript><courseTranscripts><courseTranscripts><finalLetterGradeEarned>" is "B"
  And I should find 2 "<studentSectionAssociations><studentSectionAssociations>" under "<transcript>"
  And I should see "<entityType>" is "studentSectionAssociation" for the one at position 1
  And I should find "<sections>" under "<transcript><studentSectionAssociations><studentSectionAssociations>"
  And I should see "<transcript><studentSectionAssociations><studentSectionAssociations><sections><entityType>" is "section"
  And I should find "<sessions>" under "<transcript><studentSectionAssociations><studentSectionAssociations><sections>"
  And I should see "<transcript><studentSectionAssociations><studentSectionAssociations><sections><sessions><entityType>" is "session"
  And I should find "<courses>" under "<transcript><studentSectionAssociations><studentSectionAssociations><sections>"
  And I should see "<transcript><studentSectionAssociations><studentSectionAssociations><sections><courses><entityType>" is "course"
