Feature: K-3 student list view

As a SEA/LEA user, I want to see the K-4 student list view
on SLI, so I could see elementary school students results

 @RALLY_US200 @RALLY_US147 @RALLY_US198
Scenario: Check K-3 Student Name in Live
Given I have an open web browser
Given that dashboard has been authorized for all ed orgs
When I navigate to the Dashboard home page
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "rbraverman" "rbraverman1234" for the "Simple" login page
When I select ed org "Daybreak School District 4529"
When I select school "South Daybreak Elementary"
And I select course "1st Grade Homeroom"
And I select section "Mrs. Braverman's Homeroom #38"
Then I should only see one view named "Default View"
#Then I see a list of 20 students
And the list includes: "Mi-Ha Tran"
And I click on student "Lauretta Seip"
And I view its student profile
#Display Elementary School Tab
And their grade is "1"
And Tab has a title named "Elementary School Overview"