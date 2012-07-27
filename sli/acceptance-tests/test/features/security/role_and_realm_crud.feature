@smoke @RALLY_US176 @RALLY_US174
Feature: Custom Role Mapping functions and Realm Listing functions
As an administrator tool application, I should have access to API calls to perform CRUD operations to allow custom role mapping
As any SLI application, I can access an API resource that only returns a list of realms, even while unauthenticated

@wip
Scenario Outline: Deny access to users not using SLI Adminstrator credentials

	Given I am logged in using <Username> <Password> to realm "SLI"
	When I try to access the URI "/realm" with operation <Operation>
	Then I should be denied access
	Examples:
	| Username        | Password            | Operation |
	| "leader"        | "leader1234"        | "POST"    |
	| "educator"      | "educator1234"      | "GET"     |
	
@wip
Scenario Outline: Deny access to users using non-allowed methods

	Given I am logged in using <Username> <Password> to realm "SLI"
	When I try to access the URI "/realm" with operation <Operation>
	Then I should receive a return code of 405
  Examples:
  | Username        | Password            | Operation |
  | "leader"        | "leader1234"        | "PUT"     |
  | "administrator" | "administrator1234" | "DELETE"  |

@wip
Scenario: Deny access to users using SLI Administrator credentials from non-SLI realms

	Given I am logged in using "badadmin" "badadmin1234" to realm "IL"
	When I try to access the URI "/realm" with operation "GET"
	Then I should be denied access

Scenario: Read a list of realms

	Given I am logged in using "sunsetrealmadmin" "sunsetrealmadmin1234" to realm "SLI"
  When I GET a list of realms
  Then I should receive a return code of 200
  And I should see a list of valid realm objects

Scenario: Read an existing realm

Given I am logged in using "sunsetrealmadmin" "sunsetrealmadmin1234" to realm "SLI"
When I GET a specific realm "IL-Sunset"
Then I should receive a return code of 200
And I should see a valid object returned

Scenario: Update an existing realm

	Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
	When I PUT to change the realm "Fake Realm" to change field "name" to "Endless"
	Then I should receive a return code of 204

Scenario: Delete an existing realm

	Given I am logged in using "anotherfakerealmadmin" "anotherfakerealmadmin1234" to realm "SLI"
	When I DELETE the realm "Another Fake Realm"
	Then I should receive a return code of 204
	
Scenario: Create a new realm

  Given I am logged in using "anotherfakerealmadmin" "anotherfakerealmadmin1234" to realm "SLI"
  When I POST a new realm
  Then I should receive a return code of 201
     And I should receive a new ID for my new realm

Scenario: Deny creation of a new custom role doc when one already exists for this realm/tenant
  Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
  When I POST a new custom role document with realm "Fake Realm"
  Then I should receive a return code of 400

	
@wip
Scenario: Deny mappings from non-SLI Default roles to custom roles

	Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
	When I add a mapping between non-existent role "Governator" and custom role "blah" for realm "Fake Realm"
	Then I should receive a return code of 400

@wip
Scenario: Deny mapping the same custom role to the default role twice

  Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
  When I add a mapping between default role "Aggregate Viewer" and custom role "Observer" for realm "Fake Realm"
  Then I should receive a return code of 204
  When I add a mapping between default role "Aggregate Viewer" and custom role "Observer" for realm "Fake Realm"
  Then I should receive a return code of 400
  
@workin-it
Scenario: Deny the same role being listed in two different groups

  Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
  When I add a role "Foo" in group "Educator"
  Then I should receive a return code of 204
  When I add a role "Foo" in group "Leader"
  Then I should receive a return code of 400


Scenario: Deny the same role being listed twice in one group

	
Scenario: Deny a right being listed twice in one group

Scenario: Deny creating a new role document when one already exists for that realm/tenant

Scenario: Deny creating a new role with a realm I do not have access to

	
