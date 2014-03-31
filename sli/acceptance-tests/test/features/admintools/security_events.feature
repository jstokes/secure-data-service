@security_event @no_ingestion_hooks

Feature:
  As an inBloom operator
  I want security events (access and denials) to be logged
  So that I can monitor the security of my inBloom instance

Background:
  Given I have an open browser

Scenario: Non-SLI-hosted valid user tries to access the Application Authorization Tool
  Given I am a valid non-SLI hosted user with no roles
   When I attempt to manage application authorizations
   Then I should see the error header for 403 Forbidden
    And I should see an error message indicating that "No valid role mappings exist"
    And I should see security events for:
      | Successful login             |
      | No valid role mappings exist |

Scenario: Valid SLC operator successfully accesses the default administration page
  Given I am a valid inBloom developer
   When I attempt to go to the default administration page
   Then I should be on the default administration page
   Then I should see security events for:
      | Successful login         |
      | logged successfully into |

Scenario: Invalid SLI IDP user attempts to access the default admin page
  Given I am an unknown user
   When I attempt to go to the default administration page
   Then I should not be on the default administration page
    And I should see security events for:
      | Failed login |

Scenario: Valid SLI user who does not have any roles
  Given I am a valid SLI hosted user with no roles
   When I attempt to go to the default administration page
   Then I should not be on the default administration page
    But I should see an error message indicating that "User account is in invalid mode"
    And I should see security events for:
      | Failed login |

Scenario Outline: Valid inBloom operator attempts to access restricted pages
  Given I am a valid SLC Operator
   When I attempt to go to the <page> page
   Then I should see an error message indicating that "you don't have access to this page"
  Examples:
    | page                       |
    | application authorizations |
    | custom roles               |

Scenario: Valid inBloom operator attempts to access accessible pages
  Given I am a valid SLC Operator
  When I attempt to go to the applications page
  Then I should not see an error message indicating that "you don't have access to this page"

Scenario: Valid Super Administrator attempts to access restricted pages
  Given I am a valid Super Administrator
  When I attempt to go to the custom roles page
  Then I should see an error message indicating that "you don't have access to this page"
