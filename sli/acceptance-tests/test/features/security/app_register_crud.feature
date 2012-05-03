
Feature: Application Registration Entity
As an OAuth application developer for SLI, I want to create a registration entity in SLI so that it can authenticate with SLI

Scenario: CRUD operations on Applications

	Given I am logged in using "developer" "developer1234" to realm "SLI"
	When I navigate to POST "/apps"
	Then I should receive a return code of 201
     And I should receive an ID for the newly created application
	When I navigate to GET "/apps/<New App ID>"
	Then I should receive a return code of 200
     And I should receive the data for the specified application entry
	 And it should be "PENDING"
	When an operator approves the "/apps/<New App ID>" application
     And I navigate to PUT "/apps/<New App ID>"
     Then I should receive a return code of 204
	When I navigate to DELETE "/apps/<New App ID>"
	Then I should receive a return code of 204
     And I should no longer be able to get that application's data

Scenario: Deny creation when specifying invalid fields

	Given I am logged in using "developer" "developer1234" to realm "SLI"
	When I POST an application specifying an invalid field
	Then I should receive a return code of 400

Scenario Outline: Deny access when logging in as invalid user

	Given I am logged in using <User> <Password> to realm <Realm>
	When I navigate to GET "/apps/<Testing App>"
	Then I should receive a return code of 403
	Examples:
	| User       | Password       | Realm |
	| "baduser"  | "baduser1234"  | "SLI" |
	#| "badadmin" | "badadmin1234" | "IL"  |

Scenario Outline: Deny creation when user specifying auto-generated field

	Given I am logged in using "developer" "developer1234" to realm "SLI"
	When I POST an application specifying the auto-generated field <Field> 
	Then I should receive a return code of 400
	Examples:
	| Field           |
	| "client_id"     |
	| "client_secret" |

Scenario Outline: Deny update when user updating read-only auto-generated field

	Given I am logged in using "developer" "developer1234" to realm "SLI"
	When I PUT an application updating the auto-generated field <Field> 
	Then I should receive a return code of 400
	Examples:
	| Field           |
	| "client_id"     |
	| "client_secret" |

@sandbox 
Scenario: CRUD operations on Applications In Sandbox as a Developer
	Given I am logged in using "developer" "developer1234" to realm "SLI"
	When I navigate to POST "/apps"
	Then I should receive a return code of 201
     And I should receive an ID for the newly created application
	When I navigate to GET "/apps/<New App ID>"
	Then I should receive a return code of 200
     And I should receive the data for the specified application entry
	 And it should be "APPROVED"
    When I navigate to PUT "/apps/<New App ID>"
     Then I should receive a return code of 204
	When I navigate to DELETE "/apps/<New App ID>"
	Then I should receive a return code of 204
     And I should no longer be able to get that application's data

Scenario: CRUD operations on Applications In production as an Operator
	Given I am logged in using "operator" "operator1234" to realm "SLI"
	When I navigate to POST "/apps"
	Then I should receive a return code of 400
	When I navigate to GET "/apps/"
	 Then I should receive a return code of 200
     And I should only see "PENDING" and "APPROVED" applications
    When I navigate to PUT "/apps/<Testing App>" to update an application to "APPROVED"
     Then I should receive a return code of 400
    When I navigate to PUT "/apps/<Testing App>" to update an application's name
     Then I should receive a return code of 400
	When I navigate to DELETE "/apps/<Testing App>"
	Then I should receive a return code of 400

@sandbox @wip
Scenario: CRUD operations on Applications In production as an Operator
	Given I am logged in using "operator" "operator1234" to realm "SLI"
	When I navigate to POST "/apps"
	Then I should receive a return code of 400
	When I navigate to GET "/apps/"
	 Then I should receive a return code of 200
     And I should only see "APPROVED" applications
    When I navigate to PUT "/apps/<Testing App>" to update an application's name
     Then I should receive a return code of 400
	When I navigate to DELETE "/apps/<Testing App>"
	Then I should receive a return code of 400

