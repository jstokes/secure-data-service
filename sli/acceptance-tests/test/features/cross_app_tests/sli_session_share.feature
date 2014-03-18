@integration
Feature: Applications sharing SLI Sessions
As a user of SLI, I want to have my session shared across SLI Apps

Background:

Given that dashboard has been authorized for all ed orgs
 And that databrowser has been authorized for all ed orgs
 
Scenario: Session sharing between Databrowser & Dashboard

	Given I have an open web browser
	And I have navigated to the databrowser page
	And I was redirected to the realmchooser page
	And I selected the realm "Illinois Daybreak School District 4529"
	When I submit the credentials "jstevenson" "jstevenson1234" for the "Simple" login page
	Then I am redirected to the databrowser home page
	When I navigate to the dashboard page
	Then I do not see any login pages
	And I am redirected to the dashboard home page
	When I navigate to the databrowser page
	And I click on the logout link
	Then I should see a message that I was logged out
	And I should forced to reauthenticate to gain access
	When I navigate to the dashboard home page
	Then I should forced to reauthenticate to gain access