@RALLY_US5394
Feature: Public & Global Entity Security
As a user of the system, I want to be able to access data classified as public and global from any context, as long as I am given the READ_PUBLIC right.
However, i should be forced to have context to the entity if I am attempting to update it and have been granted the WRITE_PUBLIC right.

Scenario Outline: Seeing data for global entities regardless of context
Given I am logged in using "rbraverman" "rbraverman1234" to realm "IL"
When I make a call to get <Entity Type>
Then I should receive a return code of 200
And I should see the entity in the response
Examples:
| Entity Type                |
|"assessment"                |
|"course"                    |
|"courseOffering"            |
|"educationOrganization"     |
|"graduationPlan"            |
|"gradingPeriod"             |
|"learningObjective"         |
|"learningStandard"          |
|"program"                   |
|"school"                    |
|"section"                   |
|"session"                   |
|"studentCompetencyObjective"|

Scenario Outline: Seeing data for public entities if only given the READ_PUBLIC right
# Mr. Jackson is an Agg Viewer
Given I am logged in using "jjackson" "jjackson1234" to realm "IL"
When I make a call to get <Entity Type>
Then I should receive a return code of 200
And I should see the entity in the response
Examples:
| Entity Type                |
|"assessment"                |
|"course"                    |
|"courseOffering"            |
|"educationOrganization"     |
|"graduationPlan"            |
|"gradingPeriod"             |
|"learningObjective"         |
|"learningStandard"          |
|"program"                   |
|"school"                    |
|"section"                   |
|"session"                   |
|"studentCompetencyObjective"|
  
  Scenario Outline: Being denied updating a public global entity if outside my context
Given I am logged in using "akopel" "akopel1234" to realm "IL"
When I make a call to delete <Entity Type> which I don't have context to
Then I should receive a return code of 403
# Check entity still exists
When I make a call to get <Entity Type>
Then I should receive a return code of 200
And I should see the entity in the response
# NOTE: only certain entities have this context restriction:
# assessments, learning standards, learning objectives and competency level decriptors are exempt from this rule
Examples:
| Entity Type                |
|"course"                    |
|"courseOffering"            |
|"educationOrganization"     |
|"graduationPlan"            |
|"gradingPeriod"             |
|"program"                   |
|"school"                    |
|"section"                   |
|"session"                   |
|"studentCompetencyObjective"|

  Scenario: Field level filtering on shared super-types (like address)
    Given I am logged in using "cgray" "cgray" to realm "IL"
    When I navigate to GET "/v1/schools/6756e2b9-aba1-4336-80b8-4a5dde3c63fe"
    Then field "address" should exist in the response
    When I navigate to GET "/v1/staff/e9ca4497-e1e5-4fc4-ac7b-24bad1f2998b"
    Then field "address" should exist in the response
    When I navigate to GET "/v1/staff/e9ca4497-e1e5-4fc4-ac7b-24badbad998b"
    Then field "address" should not be visible