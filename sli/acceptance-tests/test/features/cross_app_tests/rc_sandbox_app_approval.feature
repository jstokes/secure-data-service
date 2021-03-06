@RALLY_US4835
@rc
@sandbox
Feature:  RC Integration Tests

Background:
Given I have an open web browser

Scenario: App developer Registers, Approves, and Enables a new Installed app and Full window web app. Educators and IT Administrators can see the apps.
#Installed App
When I navigate to the Portal home page
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "<DEVELOPER_SB_EMAIL>" "<DEVELOPER_SB_EMAIL_PASS>" for the "Simple" login page    
Then I should be on Portal home page
And under System Tools, I click on "Apps"
Then I am redirected to the Application Registration Tool page
And I have clicked to the button New
And I am redirected to a new application page
When I entered the name "NotTheAppYoureLookingFor" into the field titled "Name"
And I entered the name "Best.  Description.  Ever." into the field titled "Description"
And I entered the name "0.0" into the field titled "Version"
And I make my app an installed app
And I check Bulk Extract
And I click on the button Submit
Then I am redirected to the Application Registration Tool page
And the application "NotTheAppYoureLookingFor" is listed in the table on the top
And the client ID and shared secret fields are present
And I clicked on the button Edit for the application "NotTheAppYoureLookingFor"
When I enable the educationalOrganization "Standard State Education Agency" in sandbox
When I click on Save
And my new apps client ID is present
And my new apps shared secret is present
Then I am redirected to the Application Registration Tool page
Then "NotTheAppYoureLookingFor" is enabled for "5" education organizations

#Full Window App
And I have clicked to the button New
And I am redirected to a new application page
When I entered the name "Schlemiel" into the field titled "Name"
And I entered the name "Yes, I totally made Schlemiel the painter's algorithm for SLI'" into the field titled "Description"
And I entered the name "1.0" into the field titled "Version"
And I entered the name "https://inbloom.org" into the field titled "Application_URL"
And I entered the name "https://inbloom.org" into the field titled "Redirect_URI"
And I select the app display method to "Full Window App" 
And I click on the button Submit
Then I am redirected to the Application Registration Tool page
And the application "Schlemiel" is listed in the table on the top
And the client ID and shared secret fields are present
And I clicked on the button Edit for the application "Schlemiel"
When I enable the educationalOrganization "Standard State Education Agency" in sandbox
When I click on Save
Then I am redirected to the Application Registration Tool page
Then "Schlemiel" is enabled for "5" education organizations

#Temperary comment out this part of the test due to logout function does not work,
#so there is no logout link/button in Admin rails

#And I click on log out
#Then I should be redirected to the impersonation page
#And I should see that I "<DEVELOPER_SB_EMAIL>" am logged in
#
##Educator can see non-installed Apps
#And I want to select "cgray" from the "SmallDatasetUsers" in automatic mode
#Then I should be on Portal home page
#And under My Applications, I see the following apps: "inBloom Dashboards;Schlemiel"
#And under My Applications, I click on "Schlemiel"
#Then my current url is "https://inbloom.org/"
#
##assert all edOrgs explicitly authorized
##bulk extract app is "NotTheAppYoureLookingFor", SSDS expected count is 5 (SEA, LEA & 3 schools)
#Then there are "5" edOrgs for the "NotTheAppYoureLookingFor" application in the applicationAuthorization collection
## non-bulk-extract app is "Schlemiel", SSDS expected count is 5 (SEA, LEA & 3 schools)
#Then there are "5" edOrgs for the "Schlemiel" application in the applicationAuthorization collection
