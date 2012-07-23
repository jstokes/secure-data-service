Feature: I want to verify that 403 error pages are handled correctly 

Scenario: Make bad saml assertion with application/json content type 
  Given I am using "application/json" as my content type
  And I make a post request to sso/post
  Then I get back a json-formatted 403 error page

Scenario: Make bad saml assertion with text/html content type 
  Given I am using "text/html" as my content type
  And I make a post request to sso/post
  Then I get back a html-formatted 403 error page
