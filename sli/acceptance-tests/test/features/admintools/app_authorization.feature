@wip
Feature: Application Authorization Tool
As an SEA or LEA  Administrator / Operator, I want to be able to allow specific applications access to my district(s) data

	Scenario: District Super Administrator logs in to the authorization tool
	
	Given I have an open web browser
	Given I am a valid District Super Administrator for <District>
	When I hit the Admin Application Authorization Tool
	And I was redirected to the "OpenAM" IDP Login page
	When I submit the credentials "cyclon" "cyclon1234" for the "OpenAM" login page
	Then I am redirected to the Admin Application Authorization Tool
	And in the upper right corner I see my name
	And I see a label in the middle <my state -> my district>
	And I see the list of all available apps on SLI
	And the authorized apps for my district are colored green
	And the unauthorized are colored red
	And are sorted by ‘Authorized’
	And I see the Name, Version, Vendor and Status of the apps

	Scenario: Non SLI-hosted valid user tries to access the Application Authorization Tool
	
	Given I am a valid SEA/LEA user
	When I hit the Application Authorization Tool
	And the login process is initiated
	And I pass my valid username and password
	Then I get message that I am not authorized
	And I am not logged on the application

	Scenario: Approve application
	
	Given I am an authenticated District Super Administrator for <District>
	And I am logged to the Application Authorization Tool
	And I see an application in the table
	And in Status it says Not Approved
	And I click on the Approve button next to it
	And I am asked ‘Do you really want this application to access the district’s data’
	When I click on Yes
	Then the application is authorized to use data of <District>
	And is put on the top of the table
	And the Status becomes Approved
	And it is colored green
	And the Approve button next to it is disabled
	And the Deny button next to it is enabled
	
	Scenario: Deny application
	
	Given I am an authenticated District Super Administrator for <District>
	And I am logged to the Application Authorization Tool
	And I see an application in the table
	And in Status it says Approved
	And I click on the Deny button next to it
	And I am asked ‘Do you really want deny access to this application of the district’s data’
	When I click on Yes
	Then the application is denied to use data of <District>
	And it is put on the bottom of the table
	And the Status becomes Not Approved
	And it is colored red
	And the Approve button next to it is enabled
	And the Deny button next to it is disabled
	
	Scenario: Authenticated user (Educator) tries to access a resource through DB within a district that denied Data Browser
	
	# It is very critical that we expand this Gherkin properly when we will implement the non-Educator context mapping!
	Given I am an authenticated end user (Educator) from <district>
	And the Data Browser is denied access for <district>
	When I try to access any resource through the DB (even the home-link) page
	Then I get message that I am not authorized