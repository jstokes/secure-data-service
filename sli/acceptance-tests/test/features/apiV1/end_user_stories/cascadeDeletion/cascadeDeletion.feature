@RALLY_DE87
@RALLY_US209
Feature: As an SLI application, I want to be able to delete an entity and trigger a cascade deletion
    This means any entity referencing the deleted entity should also be deleted

Background:
    Given I am logged in using "rrogers" "rrogers1234" to realm "IL"
    Given format "application/vnd.slc+json"

Scenario: Delete a school and confirm deletion of related entities, associations, and their cascading entities and associations
    When I navigate to GET "/<SECTION URI>/<SECTION ID>"
    Then I should receive a return code of 200
     And I should receive a link named "getSchool" with URI "/<SCHOOL URI>/<SCHOOL ID>"
    When I navigate to GET "/<TEACHER SECTION ASSOCIATION URI>/<TEACHER SECTION ASSOCIATION ID>"
    Then I should receive a return code of 200
     And I should receive a link named "getSection" with URI "/<SECTION URI>/<SECTION ID>"
     And I should receive a link named "getTeacher" with URI "/<TEACHER URI>/<TEACHER ID>"
    When I navigate to DELETE "/<SCHOOL URI>/<SCHOOL ID>"
    Then I should receive a return code of 204
    When I navigate to GET "/<SCHOOL URI>/<SCHOOL ID>"
    Then I should receive a return code of 404
    When I navigate to GET "/<SECTION URI>/<SECTION ID>"
    Then I should receive a return code of 404
    When I navigate to GET "/<TEACHER SECTION ASSOCIATION URI>/<TEACHER SECTION ASSOCIATION ID>"
    Then I should receive a return code of 404
    When I navigate to GET "/<TEACHER URI>/<TEACHER ID>"
    Then I should receive a return code of 200
