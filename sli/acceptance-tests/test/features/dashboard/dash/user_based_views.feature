##Marking this feature as WIP for now, since this doesn't test what we want it to
##Dashboard currently looks at cohort year on student for filtering the available views
##There needs to be a refactor to look at student grade /students/{id}/studentWithGrade
##and data generation done for Charles Gray and Rebecca Braverman's students

@wip
Feature: User based view selection

As a SEA/LEA user, I want to be able to select different views in my dashboard
application, that will change the subset of information that is displayed.

Background:
  Given I have an open web browser
  Given the server is in "live" mode

Scenario: Check user has multiple views available
  When I navigate to the Dashboard home page
  When I select "Sunset School District 4526" and click go
  And I wait for "1" seconds
  When I login as "cgray" "cgray1234"
  And I wait for "2" seconds
  When I click on the Dashboard page
  When I select <edOrg> "Daybreak School District 4529"
    And I select <school> "Daybreak Central High"
    And I select <course> "American Literature"
    And I select <section> "Sec 145"
  Then I should have a dropdown selector named "viewSelector"
    And I should have a selectable view named "IL_3-8_ELA"
    And I should have a selectable view named "IL_9-12"


Scenario: Views are filtered based on student grades
  When I navigate to the Dashboard home page
  When I select "Sunset School District 4526" and click go
  And I wait for "1" seconds
  When I login as "cgray" "cgray1234"
  And I wait for "2" seconds
  When I click on the Dashboard page
  When I select <edOrg> "Daybreak School District 4529"
    And I select <school> "Daybreak Central High"
    And I select <course> "Writing about Government"
    And I select <section> "Sec 923"
  Then I should only see one view named "IL_9-12"

Scenario: Check changing view changes table headings
  When I navigate to the Dashboard home page
  When I select "Sunset School District 4526" and click go
  And I wait for "1" seconds
  When I login as "cgray" "cgray1234"
  And I wait for "2" seconds
  When I click on the Dashboard page
  When I select <edOrg> "Daybreak School District 4529"
    And I select <school> "Daybreak Central High"
    And I select <course> "American Literature"
    And I select <section> "Sec 145"
  When I select view "IL_3-8_ELA"
  Then I should see a table heading "ISAT Reading"
    And I should see a table heading "ISAT Writing (most recent)"
  When I select view "IL_9-12"
  Then I should see a table heading "Reading Test Scores (Highest)"
    And I should see a table heading "Writing Test Scores (Highest)"
    And I should see a table heading "AP Eng. Exam Scores"
    
Scenario: Different users have different views defined
  When I navigate to the Dashboard home page
  When I select "Sunset School District 4526" and click go
  And I wait for "1" seconds
  When I login as "rbraverman" "rbraverman1234"
  And I wait for "2" seconds
  When I click on the Dashboard page
  When I select <edOrg> "Daybreak School District 4529"
    And I select <school> "South Daybreak Elementary"
    And I select <course> "1st Grade Homeroom"
    And I select <section> "Mrs. Braverman's Homeroom #38"
  Then I should only see one view named "IL_K-3"