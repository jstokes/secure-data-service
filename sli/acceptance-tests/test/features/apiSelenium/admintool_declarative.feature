Feature: Admin Tool Declarative Administrative Permissions

As a SLI Operator/Administrator, I want the SLI Default Roles Admin Page to be
protected so only I can get information about the default roles in SLI and their permissions.
 
Scenario: Go to SLI Default Roles Admin Page, with a SLI IT Administrator role when authenticated to SLI IDP
Given I have an open web browser
And I am authenticated to SLI IDP
And I have a Role attribute equal to "SLI IT Administrator"
When I navigate to the SLI Default Roles Admin Page
Then I should be redirected to the SLI Default Roles Admin Page
 
Scenario: Go to SLI Default Roles Admin Page, with a role other than SLI IT Administrator when authenticated to SLI IDP
Given I have an open web browser
And I am authenticated to SLI IDP
And I have a Role attribute equal to "Leader"
When I navigate to the SLI Default Roles Admin Page
Then I should get a message that I am not authorized
 
Scenario: Go to SLI Default Roles Admin Page, with a SLI IT Administrator role when authenticated to SEA/LEA IDP
Given I have an open web browser
And I am authenticated to SEA/LEA IDP
And I have a Role attribute equal to "SLI IT Administrator"
When I navigate to the SLI Default Roles Admin Page
Then I should get a message that I am not authorized
 
Scenario: Go to SLI Default Roles Admin Page, with a role other than SLI IT Administrator when authenticated to SEA/LEA IDP
Given I have an open web browser
And I am authenticated to SEA/LEA IDP
And I have a Role attribute equal to "Leader"
When I navigate to the SLI Default Roles Admin Page
Then I should get a message that I am not authorized
 
Scenario: Valid SLI IDP user login to SLI Default Roles Admin Page
Given I have an open web browser 
And I am not authenticated to SLI IDP
And I have tried to access the SLI Default Roles Admin Page
And I was redirected to the Realm page
And I choose realm "Shared Learning Initiative" in the drop-down list
And I was redirected to the SLI IDP Login page
And I am user "demo"
And "demo" is valid "SLI IDP" user
And I have a Role attribute equal to "SLI IT Administrator"
When I enter "demo" in the username text field
And I enter "demo1234" in the password text field
And I click the Go button
Then I am now authenticated to SLI IDP
And I should be redirected to the SLI Default Roles Admin Page

Scenario: Invalid SLI IDP user login to SLI Default Roles Admin Page
 
Given I have an open web browser
And I am not authenticated to SLI IDP
And I have tried to access the SLI Default Roles Admin Page
And I was redirected to the Realm page
And I choose my realm
And I was redirected to the SLI IDP Login page
And I am user "InvalidJohnDoe"
And "InvalidJohnDoe" is invalid "SLI IDP" user
When I enter "InvalidJohnDoe" in the username text field
And I enter "badpass" in the password text field
And I click the Go button
Then I am informed that authentication has failed
And I do not have access to the SLI Default Roles Admin Page