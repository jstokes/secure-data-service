@RALLY_US4214
Feature: As an SLI API, I want to be able to provide granular access to data.
  This means the user is able to request all the data within a school year range.

  Background: Use JSON format
    Given format "application/json"

Scenario Outline: check that elastic search functions as expected
	 Given I am logged in using "akopel" "akopel1234" to realm "IL"
    When I navigate to GET "/v1.4/<Entity URI>"
    Then I should receive a return code of 200
    And I should receive a collection with <COUNT> elements  
    

    Examples:
     | Entity URI | COUNT|
    |search/assessments?q=2003|1|
    |search/assessments?q=%222003%22|1|
    |search/assessments?q=209999|0|
    |search/assessments?q=%22209999%22|0|
    |search/assessments?q=Writing%202011|7|
    |search/assessments?q=%22Writing%202011%22|0|
    |search/assessments?q=Writing&q=2011|3|
    |search/assessments?q=%22BadTest111%22|0|
    |search/assessments?q=BadTest111|0|
   
     
  Scenario Outline: Result of elastic search should contain correct unicode encoding
    Given I am logged in using "akopel" "akopel1234" to realm "IL"
    And parameter "limit" is "0"
    When I navigate to GET "/v1.1/<Entity URI>"
    Then I should receive a return code of 200
    And  I should have entity with "<ENTITY ID>" and "<DESC>"
  Examples:
    | Entity URI | ENTITY ID | DESC |
    |search?q=cri%20tri%20geo|dd9165f2-653e-6f27-a82c-bfc5f4c757bc|Use congruence and similarity criteria for triangles to solve problems and to prove relationships in geometric figures.★|
