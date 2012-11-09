Feature: Developer Enablement
	As a developer I want to be able to enable my application for specific states and districts.
	As an admin I want to be able to approve the applications that developers enabled.
	
Background:
	Given I have an open web browser

    Scenario: Application editing can handle > 50 edorgs at a time.
        Given the large list of edorgs is loaded
        Given I am a valid SLI Developer "slcdeveloper" from the "SLI" hosted directory
        When I hit the Application Registration Tool URL
        And I was redirected to the "Simple" IDP Login page
        And I submit the credentials "slcdeveloper" "slcdeveloper1234" for the "Simple" login page
        Then I am redirected to the Application Registration Tool page
        And I see the list of (only) my applications
        And I clicked on the button Edit for the application "Testing App"
        Then I can see the on-boarded states
        When I select the "Mega State"
        Then I see all of the pages of Districts
        When I enable the first page of Districts
        Then the first page of districts are enabled
        When I click to the last page
        And I enable the last page of Districts
        Then the last page of districts are enabled
        When I click on the first page of Districts
        Then the first page of districts are enabled
        When I click on Save
        Then the "Testing App" is enabled for Districts
        Given I have replaced the edorg data

Scenario: App Developer or Vendor enabling application for a District
Given I am a valid SLI Developer "slcdeveloper" from the "SLI" hosted directory
When I hit the Application Registration Tool URL
	And I was redirected to the "Simple" IDP Login page
	And I submit the credentials "slcdeveloper" "slcdeveloper1234" for the "Simple" login page
Then I am redirected to the Application Registration Tool page
	And I see the list of (only) my applications
	And I clicked on the button Edit for the application "Testing App"
  Then I can see the on-boarded states
When I select a state
  Then I see all of the Districts
  Then I check the Districts
When I click on Save
Then the "Testing App" is enabled for Districts

Scenario: District Admin authorizing application for their district
Given I log in as a valid SLI Operator "sunsetadmin" from the "SLI" hosted directory
When I hit the Admin Application Authorization Tool
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "sunsetadmin" "sunsetadmin1234" for the "Simple" login page
	Then I am redirected to the Admin Application Authorization Tool
	Then I see the newly enabled application

Scenario: App Developer or Vendor disabling application for a District, part 2
Given I am a valid SLI Developer "slcdeveloper" from the "SLI" hosted directory
When I hit the Application Registration Tool URL
	And I was redirected to the "Simple" IDP Login page
	And I submit the credentials "slcdeveloper" "slcdeveloper1234" for the "Simple" login page
Then I am redirected to the Application Registration Tool page
	And I see the list of (only) my applications
	And I clicked on the button Edit for the application "Testing App"
Then I can see the on-boarded states
When I select a state
  Then I see all of the Districts
  Then I uncheck the Districts
When I click on Save
  Then the "Testing App" is enabled for Districts

Scenario: District Admin no longers see apps disabled for their district 
Given I log in as a valid SLI Operator "sunsetadmin" from the "SLI" hosted directory
When I hit the Admin Application Authorization Tool
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "sunsetadmin" "sunsetadmin1234" for the "Simple" login page
	Then I am redirected to the Admin Application Authorization Tool
	Then I don't see the newly disabled application

@sandbox @wip
Scenario: App Developer or Vendor disabling application for a District, part 3
Given I am a valid SLI Developer "developer" from the "SLI" hosted directory
When I hit the Application Registration Tool URL
	And I was redirected to the "Simple" IDP Login page
	And I submit the credentials "developer" "developer1234" for the "Simple" login page
Then I am redirected to the Application Registration Tool page
	And I see the list of (only) my applications
	And I clicked on the button Edit for the application "Testing App"
  Then I can see the on-boarded states
When I select a state
  Then I see all of the Districts
  Then I check the Districts
When I click on Save
Then the "Testing App" is enabled for Districts
Then I log out
Then I log in as a valid SLI Operator "sunsetadmin" from the "SLI" hosted directory
When I hit the Admin Application Authorization Tool
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "sunsetadmin" "sunsetadmin1234" for the "Simple" login page
	Then I am redirected to the Admin Application Authorization Tool
	Then I see the newly enabled application is approved
