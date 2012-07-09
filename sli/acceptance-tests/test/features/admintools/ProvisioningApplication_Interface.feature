@RALLY_US1106 @RALLY_US2217 @RALLY_US172
Feature: Developer/Admin has an interface to create a High Level Ed-Org and link it to the Landing Zone

Background:
Given LDAP and email server has been setup and running

Scenario: As a Admin I can define a High Level Ed-Org and Provision my Landing Zone
Given there is a production account in ldap for vendor "Macro Corp"
And I have an open web browser
When I go to the provisioning application
And I submit the credentials "<USER_ID>" "<USER_PASS>" for the "Simple" login page
#Then I can only enter a custom high-level ed-org
#When I set the custom high-level ed-org to "Test Ed Org"
When I click the Provision button
Then I get the success message

@sandbox
Scenario: As a developer I can define a High Level Ed-Org and Provision my Landing Zone on sandbox
Given there is a sandbox account in ldap for vendor "Macro Corp"
And I have an open web browser
When I go to the provisioning application
And I submit the credentials "<USER_ID>" "<USER_PASS>" for the "Simple" login page
Then I can select between the the high level ed-org of the sample data sets or enter a custom high-level ed-org
When I select the first sample data set
And I click the Provision button
Then I get the success message

Scenario: As a Admin I cannot provision my landing zone twice
Given there is a production account in ldap for vendor "Macro Corp"
And I have an open web browser
When I go to the provisioning application
And I submit the credentials "<USER_ID>" "<USER_PASS>" for the "Simple" login page
And I click the Provision button
Then I get the success message
When I go to the provisioning application
And I click the Provision button
Then I get a already provisioned message

