@US5481
Feature: Bulk Extraction Works
    
    Scenario: Authorized long-lived session token can use bulk extract
        Given I am a valid 'service' user with an authorized long-lived token "92FAD560-D2AF-4EC1-A2CC-F15B460E1E43"
        And in my list of rights I have BULK_EXTRACT
        When I make bulk extract API call
        Then I get expected zip downloaded        
    
    Scenario: Un-Authorized user cannot use the endpoint
        Given I am logged in using "linda.kim" "balrogs" to realm "IL"
        When I make bulk extract API call
        Then I should receive a return code of 403
