Feature: high school college-ready student list view

As a SEA/LEA user, I want to see the high school student list view
on SLI, so I could see high school students results

Scenario: Check table headers
Given I have an open web browser
Given the server is in "test" mode
And I am authenticated to SLI as "cgray" password "cgray"
When I access "/studentlist"
And I wait for "1" seconds
When I select ed org "Daybreak School District 4529"
When I select school "Daybreak Central High"
And I select course "American Literature"
And I select section "Sec 145"
And I wait for "2" seconds
And I select user view "IL_9-12"
And I wait for "4" seconds
Then the table includes header "ACT;SAT;PSAT/NMSQT;Reading Test Scores (Highest);Writing Test Scores (Highest)"
