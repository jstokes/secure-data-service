@rc
Feature:  RC Integration LEA SAMT Tests

Background:
Given I have an open web browser

Scenario: SEA Admin logs into SAMT and creates an LEA Admin with Realm Admin and ingestion_user roles.
  When I navigate to the user account management page
  Then I will be redirected to the realm selector web page
  When I select the realm "Shared Learning Collaborative"
  Then I am redirected to "Simple" login page
  When I submit the credentials "testuser0.wgen@gmail.com" "test1234" for the "Simple" login page
  Then I delete the user "RCTestLeaAdminFN RCTestLeaAdminLN" if exists
  Then I click on "Add User" button
  And I am redirected to the "Add a User" page
  And I can directly update the "Full Name" field to "RCTestLeaAdminFN RCTestLeaAdminLN"
  And I can directly update the "Email" field to "testuser1.wgen@gmail.com"

  And I can select "LEA Administrator" from a choice of "SEA Administrator, LEA Administrator, Ingestion User, Realm Administrator" Role
  And I can also check "Realm Administrator" Role
  And I can also check "Ingestion User" Role
  And I can change the EdOrg dropdown to "RCTestEdOrg"

  When I click button "Save"
  Then I am redirected to the "Admin Account Management" page
  And the "Success" message is displayed
  And the newly created user has "Tenant" updated to "RCTestTenant"
  And the newly created user has "EdOrg" updated to "RCTestEdOrg"

  Then I set my password to "test1234"