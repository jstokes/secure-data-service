Feature: In order to retrive session details from the API

Scenario: Authenticate with valid session ID and get the session debug context successfully
        
    Given I login with "demo" and "demo1234"
    And I get an authentication session ID from the gatekeeper
	When I GET the url "/system/session/debug" using that session ID
    Then I should receive a return code of "200"
    And I should see the session debug context in the response body

Scenario: Deny access when request session debug context without session ID

	When I GET the url "/system/session/debug" using a blank session ID
    Then I should receive a return code of "401"

Scenario: Deny access when request session debug context with invalid session ID

	When I GET the url "/system/session/debug" using an invalid session ID
    Then I should receive a return code of "401"

Scenario: Access the session check resource with valid authentication session ID

    Given I login with "demo" and "demo1234"
    And I get an authentication session ID from the gatekeeper
	When I GET the url "/system/session/check" using that session ID
    Then I should receive a return code of "200"
    And I should see the authenticated object in the response body

Scenario: Access the session check resource without authentication session ID

	When I GET the url "/system/session/check" using a blank session ID
    Then I should receive a return code of "200"
    And I should see the non-authenticated object in the response body

Scenario: Access the session check resource with invalid authentication session ID
        
	When I GET the url "/system/session/check" using an invalid session ID
    Then I should receive a return code of "200"
    And I should see the non-authenticated object in the response body

