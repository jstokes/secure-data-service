Feature: Dashboard User Login Using IDP

As a teacher in a school district, I want to use the SLI IDP Login to authenticate on SLI, and I can see specific students retrieved from the API.

Scenario: Authenticate against IDP and navigate to studentlist page.

Given I have an open web browser
Given the server is in "live" mode
When I navigate to the Dashboard home page
When I select "Illinois Realm" and click go
When I login as "linda.kim" "linda.kim1234"
When I click on the Dashboard page
When I select ed org "No Ed-Org"
When I select school "Daybreak High School"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec3"
And I wait for "2" seconds
Then The students who have an ELL lozenge exist in the API
