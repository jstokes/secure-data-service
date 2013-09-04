@RALLY_US4835
@rc
Feature:  RC Integration LEA SAMT Tests

Background:
Given I have an open web browser

Scenario: SEA Admin logs into SAMT and creates an LEA Admin with Realm Admin and ingestion_user roles.
  When I navigate to the Portal home page
  When I see the realm selector I authenticate to "inBloom" 
  #When I selected the realm "inBloom"
  And I was redirected to the "Simple" IDP Login page
  #When I navigate to the user account management page
  #Then I will be redirected to the realm selector web page
  #When I select the realm "inBloom"
  #Then I am redirected to "Simple" login page
  When I submit the credentials "<PRIMARY_EMAIL>" "<PRIMARY_EMAIL_PASS>" for the "Simple" login page
  Then I should be on Portal home page
  Then I should see Admin link  
  And I click on Admin
  Then I should be on the admin page
  And under System Tools, I click on "Manage Administrator Accounts"

  And I switch to the iframe
  Then I delete the user "RCTestLeaAdminFN RCTestLeaAdminLN" if exists
  And I switch to the iframe
  Then I click on the "Add User" button
  And I switch to the iframe
  And I am redirected to the "Add a User" page
  And I can directly update the "Full Name" field to "RCTestLeaAdminFN RCTestLeaAdminLN"
  And I can directly update the "Email" field to "<SECONDARY_EMAIL>"

  And I can select "LEA Administrator" from a choice of "SEA Administrator, LEA Administrator, Ingestion User, Realm Administrator" Role
  And I can also check "Realm Administrator" Role
  And I can also check "Ingestion User" Role
  And I can change the EdOrg dropdown to "IL-DAYBREAK"    
  
  When I click button "Save"
  Then I am redirected to the "Manage Administrator Accounts" page
  And the "Success" message is displayed
  And the newly created user has "Tenant" updated to "<TENANT>"
  And the newly created user has "EdOrg" updated to "IL-DAYBREAK"
  And I switch to the iframe
  Then I set my password to "<SECONDARY_EMAIL_PASS>"


Scenario: Create LEA Admin for Charter School

   When I navigate to the Portal home page
    When I see the realm selector I authenticate to "inBloom"
    And I was redirected to the "Simple" IDP Login page
    When I submit the credentials "<PRIMARY_EMAIL>" "<PRIMARY_EMAIL_PASS>" for the "Simple" login page
    Then I should be on Portal home page
    Then I should see Admin link
    And I click on Admin
    Then I should be on the admin page
    And under System Tools, I click on "Manage Administrator Accounts"

    And I switch to the iframe
    Then I delete the user "CharterSchoolLeaAdminFN CharterSchoolLeaAdminLN" if exists
    And I switch to the iframe
    Then I click on the "Add User" button
    And I switch to the iframe
    And I am redirected to the "Add a User" page
    And I can directly update the "Full Name" field to "CharterSchoolLeaAdminFN CharterSchoolLeaAdminLN"
    And I can directly update the "Email" field to "<CHARTER_EMAIL>"

    And I can select "LEA Administrator" from a choice of "SEA Administrator, LEA Administrator, Ingestion User, Realm Administrator" Role
    And I can also check "Realm Administrator" Role
    And I can also check "Ingestion User" Role
    And I can change the EdOrg dropdown to "IL Charter School"

    When I click button "Save"
    Then I am redirected to the "Manage Administrator Accounts" page
    And the "Success" message is displayed
    And the newly created user has "Tenant" updated to "<TENANT>"
    And the newly created user has "EdOrg" updated to "IL Charter School"
    And I switch to the iframe
    Then I set my password to "<CHARTER_EMAIL_PASS>"

  