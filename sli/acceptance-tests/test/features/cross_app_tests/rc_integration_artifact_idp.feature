@RALLY_US5960
@rc
Feature:  RC Integration Tests

  Background:
    Given I have an open web browser

  Scenario: Realm Admin Logins to create realm that supports artifact binding
    When I navigate to the Portal home page
    When I see the realm selector I authenticate to "inBloom"
    And I was redirected to the "Simple" IDP Login page
    When I submit the credentials "<SECONDARY_EMAIL>" "<SECONDARY_EMAIL_PASS>" for the "Simple" login page
    Then I should be on Portal home page
    And under System Tools, I click on "Realm Management"
    And I should be redirected back to the realm listing page
    When I click on the Add new realm button
    Then I should see that I am on the new realm page
    And all of the input fields should be blank
    And I should enter "Daybreak Artifact Test Realm" into the Display Name field
    And I enter "<CI_ARTIFACT_IDP_ID_URL>" in the IDP URL field
    And I enter "<CI_ARTIFACT_IDP_REDIRECT_URL>" in the Redirect Endpoint field
    And I enter "<CI_ARTIFACT_IDP_ARTIFACT_RESOLUTION_URL>" in the Artifact Resolution Endpoint field
    And I enter "<CI_ARTIFACT_SOURCE_ID>" in the Source Id field
    And I should enter "RC-Artifact-IL-Daybreak" into Realm Identifier
    And I should click the "Save" button
    And I should receive a notice that the realm was successfully "created"
    And the realm "Daybreak Artifact Test Realm" will exist
#    And I click on log out

  Scenario: Users can log into the newly created realm
  #Assumes dashboard and databrowser have already been approved
    When I navigate to the Portal home page
    When I see the realm selector I authenticate to "Daybreak Artifact Test Realm"
    And I was redirected to the "<CI_ARTIFACT_IDP_TYPE>" IDP Login page
    When I submit the credentials "jstevenson" "jstevenson1234" for the "<CI_ARTIFACT_IDP_TYPE>" login page
    Then I should be on Portal home page
    Then I should see "inBloom Dashboards"
    And I should see "inBloom Data Browser"
#    And I click on log out
