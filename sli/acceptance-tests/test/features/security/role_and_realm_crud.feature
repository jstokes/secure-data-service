@smoke @RALLY_US176 @RALLY_US174 @RALLY_US5156
Feature: Custom Role Mapping functions and Realm Listing functions
As an administrator tool application, I should have access to API calls to perform CRUD operations to allow custom role mapping
As any SLI application, I can access an API resource that only returns a list of realms, even while unauthenticated

Scenario: Read an existing realm
    Given I am logged in using "sunsetrealmadmin" "sunsetrealmadmin1234" to realm "SLI"
    When I GET a list of realms
    Then I should receive a return code of 200
    And I should see a list of "1" valid realm objects
    And I should only see the realm "IL-Sunset"
    When I GET a specific realm "IL-Sunset"
    Then I should receive a return code of 200
    And I should see a valid object returned

Scenario: Update an existing realm
    Given the sli securityEvent collection is empty
    And I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    When I PUT to change the realm "Fake Realm" to change field "name" to "Endless"
    Then I should receive a return code of 204
    And a security event matching "^Realm .* updated" should be in the sli db

Scenario: Deny altering or deletion of realm not yours
    Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    When I DELETE the realm "Another Fake Realm"
    Then I should receive a return code of 403
    When I PUT to change the realm "Another Fake Realm" to change field "name" to "Endless"
    Then I should receive a return code of 403

Scenario: Delete an existing realm
    Given the sli securityEvent collection is empty
    And I am logged in using "anotherfakerealmadmin" "anotherfakerealmadmin1234" to realm "SLI"
    When I DELETE the realm "Another Fake Realm"
    Then I should receive a return code of 204
    And a security event matching "^Realm .* deleted" should be in the sli db

Scenario: Create new realms
    Given the sli securityEvent collection is empty
    And I am logged in using "anotherfakerealmadmin" "anotherfakerealmadmin1234" to realm "SLI"
    When I POST a new realm called "Realm1"
    Then I should receive a return code of 201
    And I should receive a new ID for my new realm of "Realm1"

     
    #Test that a default custom role doc was created for the new realm
    When I GET the custom role doc for realm "Realm1"
    Then I should receive a return code of 200
    And I should see only one custom role document
    #Test multi creation
    When I POST a new realm called "Chicken"
    Then I should receive a return code of 201
    And I should receive a new ID for my new realm of "Chicken"
    When I POST a new realm called "Waffles"
    Then I should receive a return code of 201
    And I should receive a new ID for my new realm of "Waffles"
    When I GET a list of realms
    Then I should receive a return code of 200
    And I should see a list of "3" valid realm objects
    When I GET a list of custom role docs
    Then I should receive a return code of 200
    And I should see a list of "3" valid custom role objects

Scenario: Deny creation of a new custom role doc when one already exists for this realm/tenant
    Given the sli securityEvent collection is empty
    And I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    When I POST a new custom role document for realm "Fake Realm"
    Then I should receive a return code of 400
    And a security event matching "^Failed to create custom role" should be in the sli db

Scenario: Delete a custom role doc
    Given the sli securityEvent collection is empty
    And I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    When I DELETE the custom role doc for realm "Fake Realm"
    Then I should receive a return code of 204
    And a security event matching "^Deleted role with id" should be in the sli db

Scenario: Create a custom role doc
    Given the sli securityEvent collection is empty
    Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    When I POST a new custom role document for realm "Fake Realm"
    Then I should receive a return code of 201
    And I should receive a new ID for my new custom role document
    And a security event matching "^Created custom role with id" should be in the sli db

     
Scenario: Update a custom role doc    
    Given the sli securityEvent collection is empty
    Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    And I am editing the custom role document for realm "Fake Realm"
    When I add a role "Foo" in group "Educator"
    Then I should receive a return code of 204

     
Scenario: Deny the same role being listed in two different groups
    Given the sli securityEvent collection is empty
    And I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    And I am editing the custom role document for realm "Fake Realm"
    #When I add a role "Foo" in group "Educator"
    #Then I should receive a return code of 204
    When I add a role "Foo" in group "Leader"
    Then I should receive a return code of 400
    And a security event matching "^Failed to create custom role" should be in the sli db
    
Scenario: Deny the same role being listed twice in one group
    Given the sli securityEvent collection is empty
    And I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    And I am editing the custom role document for realm "Fake Realm"
    When I add a role "Bar" in group "Educator"
    Then I should receive a return code of 204
    When I add a role "Bar" in group "Educator"
    Then I should receive a return code of 400
    And a security event matching "^Failed to create custom role" should be in the sli db

Scenario: Deny a right being listed twice in one group
    Given the sli securityEvent collection is empty
    And I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    And I am editing the custom role document for realm "Fake Realm"
    When I add a right "WRITE_GENERAL" in group "Educator"
    Then I should receive a return code of 204
    When I add a right "WRITE_GENERAL" in group "Educator"
    Then I should receive a return code of 400
    And a security event matching "^Failed to create custom role" should be in the sli db

Scenario: Deny creating a new role with a realm I do not have access to
    Given the sli securityEvent collection is empty
    Given I am logged in using "fakerealmadmin" "fakerealmadmin1234" to realm "SLI"
    When I POST a new custom role document for realm "IL-Sunset"
    Then I should receive a return code of 403
    And a security event matching "^Failed to create custom role" should be in the sli db
  
@sandbox
Scenario: Sandbox developer creating a custom role doc
    Given the sli securityEvent collection is empty
    And I am logged in using "anothersandboxdeveloper" "anothersandboxdeveloper1234" to realm "SLI" 
    When I POST a new custom role document for realm "Sandbox"
    Then I should receive a return code of 201
    And a security event matching "^Created custom role with id" should be in the sli db

@sandbox
Scenario: Sandbox developer creating a duplicate custom role doc
    Given the sli securityEvent collection is empty
    And I am logged in using "anothersandboxdeveloper" "anothersandboxdeveloper" to realm "SLI" 
    When I POST a new custom role document for realm "Sandbox"
    Then I should receive a return code of 400
    And a security event matching "^Failed to create custom role" should be in the sli db

@sandbox
Scenario: Delete a sandbox custom role doc
    Given the sli securityEvent collection is empty
    And I am logged in using "sandboxdeveloper" "sandboxdeveloper1234" to realm "SLI" 
    When I PUT a new group "Foo" with role "Foo" and right "READ_GENERAL"
    Then I should receive a return code of 204
    And a security event matching "^Updated role with id" should be in the sli db

@sandbox
Scenario: Sandbox developer confirming that his data was not affected by delete
    Given I am logged in using "anothersandboxdeveloper" "anothersandboxdeveloper1234" to realm "SLI" 
    When I GET the custom role doc for realm "Sandbox"
    Then I should see that my custom role document is the default with realm "Sandbox"

  Scenario: CRUD opeations on Realm cause SecurityEvents to be logged
    Given the sli securityEvent collection is empty
    And I am logged in using "anotherfakerealmadmin" "anotherfakerealmadmin1234" to realm "SLI"
    When I POST a new realm called "SecEventRealm"
    Then I should receive a return code of 201
    And I should receive a new ID for my new realm of "SecEventRealm"
    And a security event "Realm [SecEventRealm] created!" should be created for these targetEdOrgs ONLY
      | targetEdOrg |
      | VIRGON      |
    When I PUT to change the realm "SecEventRealm" to change field "name" to "Updated SecEventRealm"
    Then I should receive a return code of 204
    And a security event "Realm [SecEventRealm/Updated SecEventRealm] updated!" should be created for these targetEdOrgs ONLY
      | targetEdOrg |
      | VIRGON      |
    When I DELETE the realm "SecEventRealm"
    Then I should receive a return code of 204
    And a security event "Realm [Updated SecEventRealm] deleted!" should be created for these targetEdOrgs ONLY
      | targetEdOrg |
      | VIRGON      |
