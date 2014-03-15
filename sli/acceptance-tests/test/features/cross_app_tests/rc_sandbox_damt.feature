@RALLY_US4835
@rc
@sandbox
Feature: RC Sandbox Integration - Developer Account Management Tool (DAMT)

  Background:
    Given I have an open web browser
    And I am running in Sandbox mode
    When I navigate to the Portal home page
    And I was redirected to the "Simple" IDP Login page

    Scenario: Creating a developer account using DAMT
      When I submit the credentials "<DEVELOPER_SB_EMAIL>" "<DEVELOPER_SB_EMAIL_PASS>" for the "Simple" login page
      Then I should be on Portal home page
      And I should see Admin link
      And I click on Admin
      Then the portal should be on the admin page
      And under System Tools, I click on "Manage Developer Accounts"
      #These two waits were added by the operator to improve test reliability
      And I wait for "5" seconds
      And I switch to the iframe
      And I wait for "2" seconds
      Then I am redirected to the "Sandbox Account Management" page

      Then I delete the user "RCTestDev PartTwo" if exists
      And I switch to the iframe
      Then I click on the "Add User" button
      And I switch to the iframe
      And I am redirected to the "Add a User" page
      And I can directly update the "Full Name" field to "RCTestDev PartTwo"
      And I can directly update the "Email" field to "<DEVELOPER2_SB_EMAIL>"
      And I can select "Sandbox Administrator" from a choice of "Sandbox Administrator, Application Developer, Ingestion User" Role
      And I can also check "Ingestion User" Role
      And I can also check "Application Developer" Role
      When I click button "Save"
      Then I am redirected to the "Sandbox Account Management" page
      And the "Success" message is displayed
      Then I set my password to "<DEVELOPER2_SB_EMAIL_PASS>"

    Scenario: Newly created sandbox developer can login and check available admin links
      When I submit the credentials "<DEVELOPER2_SB_EMAIL>" "<DEVELOPER2_SB_EMAIL_PASS>" for the "Simple" login page
      Then I should be on Portal home page
      And I should see Admin link
      And I click on AdmThen the portal should be on the admin pagedmin page
      And under System Tools, I see the following "Register Application;Create Custom Roles;Create Landing Zone;Change Password;Manage Developer Accounts"

    Scenario: Original developer change the role of new developer and confirm admin links
      When I submit the credentials "<DEVELOPER_SB_EMAIL>" "<DEVELOPER_SB_EMAIL_PASS>" for the "Simple" login page
      Then I should be on Portal home page
      And I should see Admin link
      And I cliThen the portal should be on the admin page on the admin page
      And under System Tools, I click on "Manage Developer Accounts"
      #These two waits were added by the operator to improve test reliability
      And I wait for "5" seconds
      And I switch to the iframe
      And I wait for "2" seconds
      Then I am redirected to the "Sandbox Account Management" page
      When I click the "edit" link for "RCTestDev PartTwo"
      Then I am redirected to "Update a User" page
      And the "Full Name" field is prefilled with "RCTestDev PartTwo"
      And the "Email" field is prefilled with "<DEVELOPER2_SB_EMAIL>"
      And the Role combobox is populated with "Sandbox Administrator"
      And the Role checkbox is checked with "Application Developer,Ingestion User"
      And I can select "Application Developer" from a choice of "Sandbox Administrator, Application Developer, Ingestion User" Role
      Then the Role combobox is populated with "Application Developer"
      And I can also check "Ingestion User" Role
      Then the Role checkbox is checked with ""
      When I click button "Update"
      Then I am redirected to the "Sandbox Account Management" page
      And the "Success" message is displayed
      When I exit out of the iframe
      And I click on log out
      Then I should be redirected to the impersonation page
      When I click on the simple-idp logout link
      Then I was redirected to the "Simple" IDP Login page

      # Switch account and verify admin links
      When I navigate to the Portal home page
      And I was redirected to the "Simple" IDP Login page
      When I submit the credentials "<DEVELOPER2_SB_EMAIL>" "<DEVELOPER2_SB_EMAIL_PASS>" for the "Simple" login page
      Then I should be on Portal home page
      And I should see Admin link
      Then the portal should be on the admin pageshould be on the admin page
      And under System Tools, I see the following "Register Application;Create Custom Roles;Change Password"
      #DE2242 causes this step to fail - And under System Tools, I shouldn't see the following "Create Landing Zone;Manage Developer Accounts"
      And under System Tools, I shouldn't see the following "Manage Developer Accounts"