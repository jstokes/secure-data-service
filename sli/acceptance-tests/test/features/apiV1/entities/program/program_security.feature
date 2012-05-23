@security
@RALLY_US209
Feature: Security for Program CRUD
  As a product owner, I want to validate that my program entity is properly secured up to current SLI standards

  Scenario Outline: Authorized user tries to hit the program URL directly
    Given I am user <User> in IDP "IL"
    When I make an API call to get the program <Program>
    Then I receive a JSON response
    Examples:
	|User     |Program          |
	|"ckoch"  |"ACC-TEST-PROG-2"|
	|"rrogers"|"ACC-TEST-PROG-1"|

  Scenario Outline: Unauthorized authenticated user tries to hit the program URL directly
    Given I am user <User> in IDP "IL"
    When I make an API call to get the program <Program>
    Then I get a message that I am not authorized
    Examples:
	|User     |Program          |
	|"llogan" |"ACC-TEST-PROG-2"|
	|"cgray"  |"ACC-TEST-PROG-2"|
