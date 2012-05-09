Feature: Dashboard User Login Authentication

As a SEA/LEA user, I want to use the SLI IDP Login to authenticate 
on SLI, so I could use the Dashboard application.

Scenario: Valid user login

Given I have an open web browser
Given the server is in "live" mode
#hitting static URL
When I access "/static/html/test.html" 
Then I can see "Static HTML page"
When I navigate to the Dashboard home page
Then I should be redirected to the Realm page
When I select "Sunset School District 4526" and click go
When I login as "linda.kim" "linda.kim1234"
Then I should be redirected to the Dashboard landing page
#hitting denied URL
When I access "/simon"
Then I am informed that "the page that you were looking for could not be found"

Scenario: Invalid user login

Given I have an open web browser
Given the server is in "live" mode
When I navigate to the Dashboard home page
And was redirected to the Realm page
When I select "New York Realm" and click go
And was redirected to the SLI-IDP login page
When I login as "InvalidJohnDoe" "demo1234"
Then I am informed that "Authentication failed"

@wip
Scenario: Login with cookie

Given I have an open web browser
Given the server is in "live" mode
When I navigate to the Dashboard home page
Then I should be redirected to the Realm page
Then I add a cookie for linda.kim
When I navigate to the Dashboard home page
Then I should be redirected to the Dashboard landing page

@wip
Scenario: user in IDP but not in mongo
Given I have an open web browser
Given the server is in "live" mode
When I access "/static/html/test.html" 
Then I can see "Static HTML page"
When I navigate to the Dashboard home page
Then I should be redirected to the Realm page
When I select "Sunset School District 4526" and click go
When I login as "mario.sanchez" "mario.sanchez"
#TODO there is a bug in the code right now
Then I am informed that "Invalid User"
