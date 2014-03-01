@track_entities
Feature: Realm Management
  As a realm administrator
  In order to manage security realms for my SDS instance
  I can add new realms, edit realms, and delete realms

Background:
  Given I have an open browser

Scenario: A realm administrator can create a realm
  Given I am a valid realm administrator
    And I am managing my realms
   When I add a new realm
   Then I see the new realm listed

Scenario: A realm administrator can edit a realm
  Given I am a valid realm administrator
    And I am managing my realms
    And I add a new realm
   When I edit that realm
    And I modify the realm name
   Then I see the edited realm listed

Scenario: A realm administrator can delete a realm
  Given I am a valid realm administrator
    And I am managing my realms
    And I add a new realm
   When I delete that realm
   Then I do not see the realm listed

Scenario: A realm administrator attempts to create a realm with missing data
  Given I am a valid realm administrator
    And I am managing my realms
   When I try to add a new realm without inputting any data
   Then I should see validation errors for:
      | Realm Identifier  |
      | Display Name      |
      | IDP URL           |
      | Redirect Endpoint |

Scenario: A realm administrator attempts to create realm with display name and identifiers that are too short
  Given I am a valid realm administrator
    And I am managing my realms
   When I try to add a new realm with invalid values for:
     | Realm Identifier |
     | Display Name     |
   Then I should see minimum length validation errors for:
     | Realm Identifier  | 5      |
     | Display Name      | 5      |

Scenario: A realm administrator attempts to create realm with artifact endpoint but not IDP source ID
  Given I am a valid realm administrator
    And I am managing my realms
   When I try to add a new realm with valid values for:
     | Artifact Resolution Endpoint |
   Then I should see co-dependent validation errors for:
     | IDP Source ID | Artifact Resolution Endpoint |

Scenario: A realm administrator attempts to create a realm with IDP source ID but not artifact endpoint
  Given I am a valid realm administrator
    And I am managing my realms
   When I try to add a new realm with valid values for:
     | IDP Source ID |
   Then I should see co-dependent validation errors for:
     | Artifact Resolution Endpoint | IDP Source ID |
