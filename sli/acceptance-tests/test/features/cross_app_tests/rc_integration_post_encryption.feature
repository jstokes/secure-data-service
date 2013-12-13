@RALLY_US5960
@rc
Feature:  RC Integration Tests

  Background:
    Given I have an open web browser

  Scenario: Realm Admin Logins to create realm that supports encryption for post
    When I navigate to the Portal home page
    When I see the realm selector I authenticate to "inBloom"
    And I was redirected to the "Simple" IDP Login page
    When I submit the credentials "<SECONDARY_EMAIL>" "<SECONDARY_EMAIL_PASS>" for the "Simple" login page
    Then I should be on Portal home page
    Then I should see Admin link
    And I click on Admin
    Then I should be on the admin page
    And under System Tools, I click on "Manage Realm"
    And I switch to the iframe
    And I should be redirected back to the realm listing page
    When I click on the Add new realm button
    Then I should see that I am on the new realm page
    And all of the input fields should be blank
    And I should enter "Daybreak Post Encryption Test Realm" into the Display Name field
    And I enter "<POST_ENCRYPT_IDP_ID_URL>" in the IDP URL field
    And I enter "<POST_ENCRYPT_IDP_REDIRECT_URL>" in the Redirect Endpoint field
    And I should enter "RC-Post-Encrypt-IL-Daybreak" into Realm Identifier
    And I should click the "Save" button
    And I switch to the iframe
    And I should receive a notice that the realm was successfully "created"
    And the realm "Daybreak Post Encryption Test Realm" will exist
    And I exit out of the iframe
    And I click on log out

  Scenario: Users can log into the newly created realm
  #Assumes dashboard and databrowser have already been approved
    When I navigate to the Portal home page
    When I see the realm selector I authenticate to "Daybreak Post Encryption Test Realm"
    And I was redirected to the "<POST_ENCRYPT_IDP_TYPE>" IDP Login page
    When I submit the credentials "jstevenson" "jstevenson1234" for the "<POST_ENCRYPT_IDP_TYPE>" login page
    Then I should be on Portal home page
    Then I should see "inBloom Dashboards"
    And I click on Admin
    And I should be on the admin page
    And I should see "inBloom Data Browser"
    And I click on log out
