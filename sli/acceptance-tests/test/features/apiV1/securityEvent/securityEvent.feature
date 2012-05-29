Feature: As an SLI application, I want to be able to read securityEvent entities
SEA Admins should be able to see all securityEvents related to their SEA and having securityEvent.role as SEA Administrator
LEA Admins should be able to see all securityEvents related to their LEA and having securityEvent.role as LEA Administrator
SLI Operators should be able to see all securityEvents
Non Admins should not be able to see any securityEvents
  
Scenario: Read all entities as SEA Admin
    Given I am logged in using "iladmin" "iladmin1234" to realm "SLI"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive a collection of "2" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"
    And each entity's "targetEdOrg" should be "IL"
    And each entity's "roles" should contain "SEA Administrator"

Scenario: Read all entities as LEA Admin
    Given I am logged in using "sunsetadmin" "sunsetadmin1234" to realm "SLI"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive a collection of "2" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"
    And each entity's "targetEdOrg" should be "IL-SUNSET"
    And each entity's "roles" should contain "LEA Administrator"

Scenario: Read all entities as SLC Operator
    Given I am logged in using "operator" "operator1234" to realm "SLI"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive a collection of "6" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"

Scenario: Read all entities as SLC Operator
    Given I am logged in using "linda.kim" "linda.kim1234" to realm "IL"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 403

