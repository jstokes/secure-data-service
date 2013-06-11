@RALLY_US5764
Feature: Match IDP roles with the ingested inBloom Roles during Authentication
         As an EdOrg Admin, I would like my users to authenticate against the edOrg+roles that I ingested,
         so that they are assigned correct roles by the inBloom system.

Background: 
  Given I have an open web browser
  And the testing device app key has been created

Scenario: As a staff member, I can log in and see data for myself, if at least one role with no end date matches
  When I navigate to the API authorization endpoint with my client ID
  Then I select "Illinois Daybreak School District 4529" from the dropdown and click go
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "linda.kim" "linda.kim1234" for the "Simple" login page
      Then I should receive a json response containing my authorization code
    When I navigate to the API token endpoint with my client ID, secret, authorization code, and redirect URI
      Then I should receive a json response containing my authorization token
      And I should be able to use the token to make valid API calls

  Given format "application/json"
    When I navigate to GET "/v1/home"
      Then I should get and store the link named "self"
      And I should extract the "staff" id from the "self" URI

Scenario: As a staff member, I can log in and see data for myself, if at least one role with valid end date matches
  When I navigate to the API authorization endpoint with my client ID
  Then I select "Illinois Daybreak School District 4529" from the dropdown and click go
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "mgonzales" "mgonzales1234" for the "Simple" login page
      Then I should receive a json response containing my authorization code
    When I navigate to the API token endpoint with my client ID, secret, authorization code, and redirect URI
      Then I should receive a json response containing my authorization token
      And I should be able to use the token to make valid API calls

  Given format "application/json"
    When I navigate to GET "/v1/home"
      Then I should get and store the link named "self"
      And I should extract the "staff" id from the "self" URI

Scenario: As a staff member, I cannot log in, if all of my roles are expired
  When I navigate to the API authorization endpoint with my client ID
  Then I select "Illinois Daybreak School District 4529" from the dropdown and click go
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "cgray" "cgray1234" for the "Simple" login page
      Then I should receive a response page with http error code 403

Scenario: As a staff member, I cannot log in, if none of my roles match
  When I navigate to the API authorization endpoint with my client ID
  Then I select "Illinois Daybreak School District 4529" from the dropdown and click go
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "rbraverman" "rbraverman1234" for the "Simple" login page
      Then I should receive a response page with http error code 403

Scenario: As a staff member, I cannot log in, if I have no roles in the database
  When I navigate to the API authorization endpoint with my client ID
  Then I select "Illinois Daybreak School District 4529" from the dropdown and click go
    And I was redirected to the "Simple" IDP Login page
    And I submit the credentials "msmith" "msmith1234" for the "Simple" login page
      Then I should receive a response page with http error code 403
