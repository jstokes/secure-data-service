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
Then I should see Admin link
And I click on Admin
Then I should be on the admin page
And under System Tools, I click on "Register Application"
And I switch to the iframe
Then I am redirected to the Application Registration Tool page
And I have clicked to the button New
And I am redirected to a new application page
When I entered the name "NotTheAppYoureLookingFor" into the field titled "Name"
And I entered the name "Best.  Description.  Ever." into the field titled "Description"
And I entered the name "0.0" into the field titled "Version"
And I make my app an installed app
And I click on the button Submit
And I switch to the iframe
Then I am redirected to the Application Registration Tool page
And the application "NotTheAppYoureLookingFor" is listed in the table on the top
And the client ID and shared secret fields are present
And I clicked on the button Edit for the application "NotTheAppYoureLookingFor"
Then I can see the on-boarded states
When I select a state
Then I see all of the Districts
Then I check the Districts
When I click on Save
Then the "NotTheAppYoureLookingFor" is enabled for Districts

#Full Window App
And I exit out of the iframe
And I click on Admin
Then I should be on the admin page
And under System Tools, I click on "Register Application"
And I switch to the iframe
Then I am redirected to the Application Registration Tool page
And I have clicked to the button New
And I am redirected to a new application page
When I entered the name "Schlemiel" into the field titled "Name"
And I entered the name "Yes, I totally made Schlemiel the painter's algorithm for SLI'" into the field titled "Description"
And I entered the name "1.0" into the field titled "Version"
And I entered the name "http://www.google.com" into the field titled "Application_URL"
And I entered the name "http://wwww.google.com" into the field titled "Redirect_URI"
And I select the app display method to "Full Window App" 
And I click on the button Submit
Then I am redirected to the Application Registration Tool page
And the application "Schlemiel" is listed in the table on the top
And the client ID and shared secret fields are present
And I clicked on the button Edit for the application "Schlemiel"
Then I can see the on-boarded states
When I select a state
Then I see all of the Districts
Then I check the Districts
When I click on Save
Then the "Schlemiel" is enabled for Districts
And I exit out of the iframe
And I click on log out
Then I will be redirected to realm selector web page
When I click on the "Sandbox" realm in "Sandbox"
Then I should be redirected to the impersonation page
And I should see that I "<DEVELOPER_SB_EMAIL>" am logged in

#Educator can see non-installed Apps
And I want to select "cgray" from the "SmallDatasetUsers" in automatic mode
Then I should be on Portal home page
And under My Applications, I see the following apps: "SLC Dashboards;Schlemiel"
And under My Applications, I click on "Schlemiel"
Then my current url is "http://www.google.com/"	