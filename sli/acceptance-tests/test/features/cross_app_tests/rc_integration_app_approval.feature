@RALLY_US4835
@rc
Feature:  RC Integration Tests

Background:
Given I have an open web browser

Scenario: Realm Admin Logins to create realm
When I navigate to the Portal home page
When I see the realm selector I authenticate to "inBloom"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "<SECONDARY_EMAIL>" "<SECONDARY_EMAIL_PASS>" for the "Simple" login page
Then I should be on Portal home page
Then I should see Admin link
And I click on Admin
Then I should be on the admin page
And under System Tools, I click on "Manage Realm"
And I switch to the iframe
And I should see that I am on the new realm page
And all of the input fields should be blank
And I should enter "Daybreak Test Realm" into the Display Name field
And I enter "<CI_IDP_Redirect_URL>" in the IDP URL field
And I enter "<CI_IDP_Redirect_URL>" in the Redirect Endpoint field
And I should enter "RC-IL-Daybreak" into Realm Identifier
And I should click the "Save" button
And I switch to the iframe
And I should receive a notice that the realm was successfully "created"
#Then I see the realms for "Daybreak School District 4529 (IL-DAYBREAK)"
And the realm "Daybreak Test Realm" will exist
And I exit out of the iframe
And I click on log out

Scenario: User cannot access Bootstrapped Apps before approval
When I navigate to the Portal home page
When I selected the realm "Daybreak Test Realm"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "jstevenson" "jstevenson1234" for the "Simple" login page    
Then I should be on Portal home page
Then I should not see "inBloom Dashboards"
And I click on Admin
And I should be on the admin page
And I should not see "inBloom Data Browser"
And I click on log out

@wip
Scenario: Charter School Realm Admin Logins to create realm
When I navigate to the Portal home page
When I see the realm selector I authenticate to "inBloom"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "<CHARTER_EMAIL>" "<CHARTER_EMAIL_PASS>" for the "Simple" login page
Then I should be on Portal home page
Then I should see Admin link
And I click on Admin
Then I should be on the admin page
And under System Tools, I click on "Manage Realm"
And I switch to the iframe
And I should see that I am on the new realm page
And all of the input fields should be blank
And I should enter "Charter School Test Realm" into the Display Name field
And I enter "<CI_CHARTER_IDP_Redirect_URL>" in the IDP URL field
And I enter "<CI_CHARTER_IDP_Redirect_URL>" in the Redirect Endpoint field
And I should enter "RC-IL-Charter-School" into Realm Identifier
And I should click the "Save" button
And I switch to the iframe
And I should receive a notice that the realm was successfully "created"
Then I see the realms for "IL-CHARTER-SCHOOL (IL-CHARTER-SCHOOL)"
And the realm "Charter School Test Realm" will exist
And I exit out of the iframe
And I click on log out

@wip
Scenario: Charter School User cannot access Bootstrapped Apps before approval
When I navigate to the Portal home page
When I selected the realm "Charter School Test Realm"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "charteradmin" "charteradmin1234" for the "Simple" login page
Then I should be on Portal home page
Then I should not see "inBloom Dashboards"
And I click on Admin
And I should be on the admin page
And I should not see "inBloom Data Browser"
And I click on log out

Scenario: SEA approves Dashboard and Databrowser
When I navigate to the Portal home page
When I see the realm selector I authenticate to "inBloom"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "<PRIMARY_EMAIL>" "<PRIMARY_EMAIL_PASS>" for the "Simple" login page
Then I should be on Portal home page
Then I should see Admin link
And I click on Admin
Then I should be on the admin page
And under System Tools, I click on "Authorize Applications"
And I switch to the iframe
Then I am redirected to the Admin Application Authorization Tool
#Authorize the Dashboard
And I see an application "inBloom Dashboards" in the table
And in Status it says "0 EdOrg(s), auto-authorized"
And I click on the "Edit Authorizations" button next to it
And I authorize the educationalOrganization "Standard State Education Agency"
And I click Update
Then there are "199" edOrgs for the "inBloom Dashboards" application in the applicationAuthorization collection for the production tenant
And I check to find if record is in sli db collection:
  | collectionName      | expectedRecordCount | searchParameter       | searchValue                               |
  | securityEvent       | 1                   | body.logMessage       | Application granted access to EdOrg data! |
And there are "199" educationalOrganizations in the targetEdOrgList
And I see an application "inBloom Dashboards" in the table
And in Status it says "199 EdOrg(s), auto-authorized"

#Authorize the Databrowser
And I see an application "inBloom Data Browser" in the table
And in Status it says "0 EdOrg(s), auto-authorized"
And I click on the "Edit Authorizations" button next to it
And I authorize the educationalOrganization "Standard State Education Agency"
And I click Update
Then there are "199" edOrgs for the "inBloom Data Browser" application in the applicationAuthorization collection for the production tenant
And I check to find if record is in sli db collection:
  | collectionName      | expectedRecordCount | searchParameter       | searchValue                               |
  | securityEvent       | 1                   | body.logMessage       | Application granted access to EdOrg data! |
And there are "199" educationalOrganizations in the targetEdOrgList
And I see an application "inBloom Data Browser" in the table
And in Status it says "199 EdOrg(s), auto-authorized"
#Authorized the new Web-App
And I exit out of the iframe
And I click on log out

Scenario: Sessions are shared between Dashboard and Databrowser apps
When I navigate to the Portal home page
When I select "Daybreak Test Realm" and click go
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "jstevenson" "jstevenson1234" for the "Simple" login page
Then I should be on Portal home page
When I navigate to the dashboard page
And I am redirected to the dashboard home page
When I navigate to the databrowser page
Then I do not see any login pages
Then I am redirected to the databrowser home page
And I click on the logout link
Then I should see a message that I was logged out
And I should forced to reauthenticate to gain access
When I navigate to the dashboard home page
Then I should forced to reauthenticate to gain access

@wip
Scenario: Charter School Sessions are shared between Dashboard and Databrowser apps
When I navigate to the Portal home page
When I select "Charter School Test Realm" and click go
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "charteradmin" "charteradmin1234" for the "Simple" login page
Then I should be on Portal home page
When I navigate to the dashboard page
And I am redirected to the dashboard home page
When I navigate to the databrowser page
Then I do not see any login pages
Then I am redirected to the databrowser home page
And I click on the logout link
Then I should see a message that I was logged out
And I should forced to reauthenticate to gain access
When I navigate to the dashboard home page
Then I should forced to reauthenticate to gain access

Scenario: User sees non-installed Developer App
When I navigate to the Portal home page
When I selected the realm "Daybreak Test Realm"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "linda.kim" "linda.kim1234" for the "Simple" login page
Then I should be on Portal home page
And under My Applications, I see the following apps: "inBloom Dashboards"

@wip
Scenario: Charter School User sees non-installed Developer App
When I navigate to the Portal home page
When I selected the realm "Charter School Test Realm"
And I was redirected to the "Simple" IDP Login page
When I submit the credentials "chartereducator" "chartereducator1234" for the "Simple" login page
Then I should be on Portal home page
And under My Applications, I see the following apps: "inBloom Dashboards"


