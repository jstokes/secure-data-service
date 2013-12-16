@DE2300
Feature: As an SLI API, I want to be able to provide granular access to data.
  This means the user is able to request all the data within a school year range.

  Background: Use JSON format
    Given format "application/json"

  Scenario Outline: Ensure Normal Search Entities Can Be Queried with a Limit of 250
    Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
    And parameter "limit" is "250"
    When I navigate to GET "/v1.3/<Entity URI>"
    Then I should receive a return code of 200

  Examples:
    | Entity Type                 | Entity URI                         | Count |
    | course                      | search/courses                     | 95    |
    | session                     | search/sessions                    | 22    |
    | gradingPeriod               | search/gradingPeriods              | 17    |

  Scenario Outline: Ensure Extended Search Entities Can Be Queried with a Limit of 5000
    Given I am logged in using "ckoch" "ckoch1234" to realm "IL"
    And parameter "limit" is "5000"
    When I navigate to GET "/v1.3/<Entity URI>"
    Then I should receive a return code of 200
    And each entity's "entityType" should be "<Entity Type>"
    And I should receive a collection of "<Count>" entities

  Examples:
    | Entity Type                 | Entity URI                         | Count |
    | assessment                  | search/assessments                 | 19    |
    | competencyLevelDescriptor   | search/competencyLevelDescriptor   | 0     |
    | learningObjective           | search/learningObjectives          | 198   |
    | learningStandard            | search/learningStandards           | 1499  |
    | studentCompetencyObjective  | search/studentCompetencyObjectives | 4     |

  Scenario Outline: Ensure Normal Search Entities Cannot Be Queried with a Limit Greater Than 250
    Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
    And parameter "limit" is "500"
    When I navigate to GET "/v1.3/<Entity URI>"
    Then I should receive a return code of 412

  Examples:
    | Entity Type                 | Entity URI                         |
    | course                      | search/courses                     |
    | session                     | search/sessions                    |
    | gradingPeriod               | search/gradingPeriods              |

  Scenario Outline: Ensure Extended Search Entities Cannot Be Queried with a Limit Greater Than 5000
    Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
    And parameter "limit" is "6000"
    When I navigate to GET "/v1.3/<Entity URI>"
    Then I should receive a return code of 412

  Examples:
    | Entity Type                 | Entity URI                         |
    | assessment                  | search/assessments                 |
    | competencyLevelDescriptor   | search/competencyLevelDescriptor   |
    | learningObjective           | search/learningObjectives          |
    | learningStandard            | search/learningStandards           |
    | studentCompetencyObjective  | search/studentCompetencyObjectives |
