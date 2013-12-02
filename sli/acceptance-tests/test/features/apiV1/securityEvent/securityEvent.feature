@RALLY_US2492
@RALLY_US3235
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
    And I should receive a collection of "4" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"
    And each securityEvent's targetEdOrgList should contain at least one of "<IL>"
    And each entity's "roles" should contain "SEA Administrator"

Scenario: Read all entities as LEA Admin
    Given I am logged in using "sunsetadmin" "sunsetadmin1234" to realm "SLI"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive a collection of "7" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"
    And each securityEvent's targetEdOrgList should contain at least one of "<IL_SUNSET_LONGWOOD>"
    And each entity's "roles" should contain "LEA Administrator"

Scenario: Read all entities as SLC Operator
    Given I am logged in using "operator" "operator1234" to realm "SLI"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive a collection of "20" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"

Scenario: Read all entities as NonAdmin User
    Given I am logged in using "linda.kim" "linda.kim1234" to realm "IL"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 403

Scenario: Setup admin delegation as LEA Admin(SecurityEventDelegation is disabled)
    Given I am logged in using "sunsetadmin" "sunsetadmin1234" to realm "SLI"
    When I POST a new admin delegation
    Then I should receive a return code of 201
    
Scenario: Enable delegation of SecurityEvents as LEA Admin
    Given I am logged in using "sunsetadmin" "sunsetadmin1234" to realm "SLI"
    And I have a valid admin delegation entity
    And I change "viewSecurityEventsEnabled" to true
    When I PUT to admin delegation
    Then I should receive a return code of 204


Scenario: Read delegation of SecurityEvents as SEA Admin
    Given I am logged in using "iladmin" "iladmin1234" to realm "SLI"
    Then I should get my delegations
    And I should see that "viewSecurityEventsEnabled" is "true" for district "IL-SUNSET's ID"

Scenario: Read securityEvents as SEA Admin. 11 Events. Sunsetadmin(4),  Iladmin(4) and Longwoodadmin(3)    
    Given I am logged in using "iladmin" "iladmin1234" to realm "SLI"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to GET "<ENTITY URI>"
    Then I should receive a return code of 200
    And I should receive a collection of "11" entities
    And each entity's "entityType" should be "<ENTITY TYPE>"
    Then each securityEvent's targetEdOrgList should contain at least one of "<IL_SUNSET_LONGWOOD>"

Scenario: IT Administrators can see security events for their edOrgs
  Given I am logged in using "jwashington" "jwashington1234" to realm "IL"
  And format "application/vnd.slc+json"
  And parameter "limit" is "0"
  When I navigate to GET "<ENTITY URI>"
  Then I should receive a return code of 200
  And I should receive a collection of "7" entities
  And each entity's "entityType" should be "<ENTITY TYPE>"
  And each securityEvent's targetEdOrgList should contain at least one of "<IL_SUNSET_OR_IL_LONGWOOD_OR_SUNSET_CENTRAL>"

Scenario: IT Administrator makes an HTTP HEAD request to a security event in their purview
    Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to HEAD "/securityEvent/07623f03-126e-427d-9ed4-29562388reak"
    Then I should receive a return code of 200

Scenario: IT Administrator makes an HTTP HEAD request to the security event resource (collection)
    Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to HEAD "/securityEvent/"
    Then I should receive a return code of 200

Scenario: IT Administrator makes an HTTP HEAD request to a security event not in their purview
    Given I am logged in using "jwashington" "jwashington1234" to realm "IL"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to HEAD "/securityEvent/07623f03-126e-427d-9ed4-29562388reak"
    Then I should receive a return code of 404

Scenario: Non-admin user (without SECURITY_EVENT_VIEW right) makes an unsuccessful HTTP HEAD request to the security event resource (collection)
    Given I am logged in using "cgray" "cgray1234" to realm "IL"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to HEAD "/securityEvent/"
    Then I should receive a return code of 403

@DE2991
Scenario Outline: DE2990 no spurious HTTP methods are allowed and DE2991 GET by id does not bypass security
    Given I am logged in using "<user>" "<password>" to realm "<realm>"
    And format "application/vnd.slc+json"
    And parameter "limit" is "0"
    When I navigate to <requestMethod> "/securityEvent/<id>"
    Then I should receive a return code of <returnCode>

 Examples:
| requestMethod  | id                                   | user           | password        | realm     | returnCode |
# DE2991 - Authorized user can GET security events by ID
| GET            | 268406bf-83b5-4d43-9b56-c428e0998cb2 | sunsetadmin    | sunsetadmin1234 | SLI       | 200        |
# DE2991 - Unauthorized user can not get security events by ID
| GET            | 268406bf-83b5-4d43-9b56-c428e0998cb2 | jstevenson     | jstevenson1234  | IL        | 404        |
# DE2990 - None can DELETE events through security API
| DELETE         | 07623f03-126e-427d-9ed4-29562388cdcc | operator       | operator1234    | SLI       | 403        |
# DE2990 - None can PATCH events through security API
| PATCH          | 07623f03-126e-427d-9ed4-29562388cdcc | operator       | operator1234    | SLI       | 403        |
| OPTIONS        |                                      | operator       | operator1234    | SLI       | 403        |

@DE2990
Scenario: None can PUT events through security API
    Given I am logged in using "operator" "operator1234" to realm "SLI"
    And format "application/vnd.slc+json"
    When I navigate to GET "/securityEvent/07623f03-126e-427d-9ed4-29562388cdcc"
    Then I should receive a return code of 200
    When I create an empty json object
    And I navigate to PUT "/securityEvent/07623f03-126e-427d-9ed4-29562388cdcc"
    Then I should receive a return code of 403

@DE2990
Scenario: None can POST events through security API
    Given I am logged in using "operator" "operator1234" to realm "SLI"
    And format "application/vnd.slc+json"
    When I create an empty json object
    And I navigate to POST "/securityEvent"
    Then I should receive a return code of 403

Scenario: IT Administrator creates, views, updates, and deletes a custom entity for a security event
    Given I am logged in using "jstevenson" "jstevenson1234" to realm "IL"
    And format "application/vnd.slc+json"
    When I create an empty json object
    And "key1" is "value1"
    And "key2" is "value2"
    And I navigate to POST "/securityEvent/07623f03-126e-427d-9ed4-29562388reak/custom"
    Then I should receive a return code of 201
    When I navigate to GET "/securityEvent/07623f03-126e-427d-9ed4-29562388reak/custom"
    Then I should receive a return code of 200
    And the "key1" should be "value1"
    And the "key2" should be "value2"
    When I create an empty json object
    And "key3" is "value3"
    And "key4" is "value4"
    And I navigate to PUT "/securityEvent/07623f03-126e-427d-9ed4-29562388reak/custom"
    Then I should receive a return code of 204
    When I navigate to DELETE "/securityEvent/07623f03-126e-427d-9ed4-29562388reak/custom"
    Then I should receive a return code of 204
    When I navigate to GET "/securityEvent/07623f03-126e-427d-9ed4-29562388reak/custom"
    Then I should receive a return code of 404

Scenario: Security prevents an IT Administrator from adding a custom entity for a security event for which they have no context
    Given I am logged in using "jwashington" "jwashington1234" to realm "IL"
    And format "application/vnd.slc+json"
    When I create an empty json object
    And "key1" is "value1"
    And "key2" is "value2"
    And I navigate to POST "/securityEvent/07623f03-126e-427d-9ed4-29562388reak/custom"
    Then I should receive a return code of 403
