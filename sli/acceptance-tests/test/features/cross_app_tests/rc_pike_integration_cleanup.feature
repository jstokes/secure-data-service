@RALLY_US4835
@rc
Feature:  RC Integration CleanUp Tests

Background:
  Given I have an open web browser
  
Scenario: App developer deletes installed bulk extract app
  When I navigate to the Portal home page
  When I see the realm selector I authenticate to the developer realm
  And I was redirected to the "Simple" IDP Login page
  When I submit the credentials "<DEVELOPER_EMAIL>" "<DEVELOPER_EMAIL_PASS>" for the "Simple" login page
  Then I should be on Portal home page
  Then I should see Admin link
  And I click on Admin
  Then I should be on the admin page
  And under System Tools, I click on "Register Application"
  And I switch to the iframe
  Then I am redirected to the Application Registration Tool page
  Then I have clicked on the button 'Delete' for the application named "BulkExtractApp2"
  And I got warning message saying 'You are trying to remove this application from inBloom. By doing so, you will prevent any active user to access it. Do you want to continue?'
  When I click 'Yes'
  And I switch to the iframe
  Then the application named "BulkExtractApp2" is removed from the SLI

Scenario: App developer deletes installed non bulk extract app
  When I navigate to the Portal home page
  When I see the realm selector I authenticate to the developer realm
  And I was redirected to the "Simple" IDP Login page
  When I submit the credentials "<DEVELOPER_EMAIL>" "<DEVELOPER_EMAIL_PASS>" for the "Simple" login page
  Then I should be on Portal home page
  Then I should see Admin link
  And I click on Admin
  Then I should be on the admin page
  And under System Tools, I click on "Register Application"
  And I switch to the iframe
  Then I am redirected to the Application Registration Tool page
  Then I have clicked on the button 'Delete' for the application named "NotABulkExtractApp"
  And I got warning message saying 'You are trying to remove this application from inBloom. By doing so, you will prevent any active user to access it. Do you want to continue?'
  When I click 'Yes'
  And I switch to the iframe
  Then the application named "NotABulkExtractApp" is removed from the SLI

@wip
Scenario:  LEA deletes realm
  When I navigate to the Portal home page
  When I selected the realm "inBloom"
  And I was redirected to the "Simple" IDP Login page
  When I submit the credentials "<SECONDARY_EMAIL>" "<SECONDARY_EMAIL_PASS>" for the "Simple" login page
  Then I should be on Portal home page
  Then I should see Admin link
  And I click on Admin
  Then I should be on the admin page
  And under System Tools, I click on "Manage Realm"
  And I switch to the iframe
  Then I see the realms for "IL-DAYBREAK"
  When I click the "Daybreak Test Realm" delete button and confirm deletion
  And I switch to the iframe
  Then I see the realms for "IL-DAYBREAK"
  And I exit out of the iframe
  And I click on log out

@wip
Scenario:  Charter SchoolL LEA deletes realm
  When I navigate to the Portal home page
  When I selected the realm "inBloom"
  And I was redirected to the "Simple" IDP Login page
  When I submit the credentials "<CHARTER_EMAIL>" "<CHARTER_EMAIL_PASS>" for the "Simple" login page
  Then I should be on Portal home page
  Then I should see Admin link
  And I click on Admin
  Then I should be on the admin page
  And under System Tools, I click on "Manage Realm"
  And I switch to the iframe
  Then I see the realms for "IL-CHARTER-SCHOOL"
  When I click the "Charter School Test Realm" delete button and confirm deletion
  And I switch to the iframe
  Then I see the realms for "IL-CHARTER-SCHOOL"
  And I exit out of the iframe
  And I click on log out

@wip
Scenario: slcoperator deletes SEA,LEA
  When I navigate to the user account management page
  And I see the realm selector I authenticate to "inBloom"
  Then I am redirected to "Simple" login page
  When I submit the credentials "slcoperator" "slcoperator1234" for the "Simple" login page
  Then I delete the user "RCTestSeaAdminFN RCTestSeaAdminLN"
  Then I delete the user "RCTestLeaAdminFN RCTestLeaAdminLN"
  Then I delete the user "CharterSchoolLeaAdminFN CharterSchoolLeaAdminLN"
