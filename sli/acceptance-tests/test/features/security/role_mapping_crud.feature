@wip
Feature: Custom Role Mapping functions and Realm Listing functions
As an administrator tool application, I should have access to API calls to perform CRUD operations to allow custom role mapping
As any SLI application, I can access an API resource that only returns a list of realms, even while unauthenticated

Scenario: Unauthenticated users can access a list of realms

	Given I have not yet authenticated
	When I make a call to get the list of realms
	Then I should see a response that contains the list of realms
	And I should see a URL for each realm that links to their IDP
	And I should not see any data about any realm's role-mapping'

Scenario Outline: Deny access to users not using SLI Adminstrator credentials

	Given I am a valid "sli" end user <Username> with password <Password>
	When I try to access the URI "/pub/roles/mapping/" with operation <Operation>
	Then I should be denied access
	Examples:
	| Username        | Password            | Operation |
	| "leader"        | "leader1234"        | POST      |
	| "educator"      | "educator1234"      | GET       |
	| "administrator" | "administrator1234" | DELETE    |

Scenario: Create a custom role mapping

	Given I am a valid "sli" end user "demo" with password "demo1234"
	When I POST a mapping between default role "Educator" and custom role "blah" for realm "SLI"
	Then I should see the POST operation is successful
	
Scenario: Read an existing role mapping

	Given I am a valid "sli" end user "demo" with password "demo1234"
	When I GET a list of role mappings for realm "SLI"
	Then I should see the GET operation is successful
	And I should see a valid object returned

Scenario: Update an existing role mapping

	Given I am a valid "sli" end user "demo" with password "demo1234"
	When I PUT to change the mapping between default role "Educator" and custom role "blah" to role "Blah" for realm "SLI"
	Then I should see the PUT operaion is successful
	
Scenario: Delete an existing role mapping

	Given I am a valid "sli" end user "demo" with password "demo1234"
	When I DELETE a mapping between the default role "Educator" and custom role "Blah"
	Then I should see the DELETE operation is successful

Scenario: Deny duplicated creations

	Given I am a valid "sli" end user "demo" with password "demo1234"
	When something
	Then something
	
Scenario: Deny duplicated deletions

	Given I am a valid "sli" end user "demo" with password "demo1234"
	When something
	Then something