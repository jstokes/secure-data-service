Feature: K-3 student list view

As a SEA/LEA user, I want to see the K-3 student list view
on SLI, so I could see elementary school students results

@integration
Scenario: Check K-3 Student Name
Given I have an open web browser
Given the server is in "live" mode
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go
When I login as "rbraverman" "rbraverman1234"
When I select ed org "Daybreak School District 4529"
When I select school "South Daybreak Elementary"
And I select course "1st Grade Homeroom"
And I select section "Mrs. Braverman's Homeroom #38"
Then I should only see one view named "Early Literacy View"
Then I see a list of 25 students
And the list includes: "Mi-Ha Tran"
And I click on student "Lauretta Seip"
And I view its student profile
And their grade is "1"
And Tab has a title named "Elementary School Overview"
