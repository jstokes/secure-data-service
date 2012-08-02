@RALLY_US3041
Feature: Test exposure of aggregates to the api

Background: 
    Given I am logged in using "rrogers" "rrogers1234" to realm "IL"
    And format "application/json"

  Scenario Outline: As an administrator, I want to view the highest ever test score aggregates for my school 
    When I navigate to GET "/v1/schools/<ENTITY_ID>?includeAggregates=true"
    Then I see the embedded proficiency count for <LEVEL> is <COUNT>

                Examples:
                        | ENTITY_ID                             | LEVEL | COUNT |                       
                        | ec2e4218-6483-4e9c-8954-0aecccfd4731  | E     | 296   | 
                        | ec2e4218-6483-4e9c-8954-0aecccfd4731  | S     | 35    | 
                        | ec2e4218-6483-4e9c-8954-0aecccfd4731  | W     | 0     | 

