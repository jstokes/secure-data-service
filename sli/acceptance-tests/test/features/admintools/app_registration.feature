@no_ingestion_hooks

Feature: Application Registration
  As a super-admin
  In order to allow the onboarding of new applications to SLI
  I want to be able to create new application keys

Background:
  Given I have an open browser

Scenario: A district-level administrator attempts to manage applications
  Given I am a valid district-level administrator
   When I attempt to manage applications
   Then I should not be allowed to access the page

Scenario: An operator denies a pending application
  Given a developer has registered a new application
    And I am a valid inBloom operator
    And I am managing my applications
    And I see the pending application
   When I deny the application
   Then I no longer see the application

Scenario: An operator denies an approved application
  Given a developer has registered a new application
    And I am a valid inBloom operator
    And I am managing my applications
    And I see the pending application
    And I approve the application
    And I see the approved application
  When I deny the application
  Then I no longer see the application

Scenario: A developer cancels creation of a new application
  Given I am a valid inBloom developer
    And I am managing my applications
    And I want to create a new application
   When I cancel out of the new application form
   Then I am managing my applications again

Scenario: A developer does not provide required information for an application
  Given I am a valid inBloom developer
    And I am managing my applications
   When I submit an application for registration without inputting any information
   Then I should see validation errors for:
    | Name            |
    | Description     |
    | Version         |
    | Application url |
    | Redirect uri    |

Scenario: A developer does not need to provide application and redirect URLs for an installed application
  Given I am a valid inBloom developer
    And I am managing my applications
   When I submit an application for registration marked as "Installed"
   Then I should not see validation errors for:
    | Application url |
    | Redirect uri    |

Scenario: A developer must provide correctly formatted URLs where needed
  Given I am a valid inBloom developer
    And I am managing my applications
   When I submit an application for registration with improperly formatted URLs
   Then I should see validation errors for:
    | Administration url |
    | Image url          |
    | Application url    |
    | Redirect uri       |

@sandbox
Scenario: A sandbox app developer can add a new application
  Given I am a valid sandbox developer
    And I have an open browser
    And I am managing my applications
   When I submit a new application for registration
   Then the application should get registered

# TODO: Determine if this is a legitimate use case; can there be multiple developer's in the same sandbox tenancy?
@wip @sandbox
Scenario: The other app developer in my tenancy can also modify and delete my apps
  Given I have an open web browser
  And my LDAP server has been setup and running
    Given there is a "Application Developer" with tenancy "developer-email@slidev.org" and in "STANDARD-SEA"
    Then I can navigate to app registration page with that user
	    And I am redirected to the Application Registration Tool page
    Then I clicked on the button Edit for the application "NewApp"
        Then every field except the shared secret and the app ID became editable
        And I can update the version to "100" 
        Then I clicked Save
        Then I am redirected to the Application Registration Tool page
    And I can delete "NewApp"