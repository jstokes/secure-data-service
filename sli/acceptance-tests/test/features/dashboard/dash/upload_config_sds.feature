Feature: Upload View Configurations

As a admin, I'm able to upload view configuration

Background:
Given I have an open web browser
Given that dashboard has been authorized for all ed orgs
When I navigate to the Dashboard home page

@integration @RALLY_US2276
Scenario: Invalid User Login
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "linda.kim" "linda.kim1234" for the "Simple" login page
 Then I should be redirected to the Dashboard landing page
When I enter the Configuration Area
Then I see an error

@integration @RALLY_US2276 @RALLY_US200
Scenario: Upload invalid config file
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "jstevenson" "jstevenson1234" for the "Simple" login page
 Then I should be redirected to the Dashboard landing page
When I enter the Configuration Area
Then I am authorized to the Configuration Area
And I paste Invalid json config into the text box
And click Save
Then I should be shown a failure message
And I reset custom config
And click Save
Then I should be shown a success message
And I logout
When I navigate to the Dashboard home page
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "linda.kim" "linda.kim1234" for the "Simple" login page
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
Then I see a list of 28 students
Then I should have a dropdown selector named "viewSelect"
And I should have a selectable view named "Default View"

@integration @RALLY_US2276 @RALLY_US200
Scenario: Upload valid config file
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "jstevenson" "jstevenson1234" for the "Simple" login page
 Then I should be redirected to the Dashboard landing page
When I enter the Configuration Area
Then I am authorized to the Configuration Area
And the title of the page is "inBloom"
And I paste Valid json config into the text box
And click Save
Then I should be shown a success message
And I logout
When I navigate to the Dashboard home page
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "linda.kim" "linda.kim1234" for the "Simple" login page
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
Then I see a list of 28 students
Then I should have a dropdown selector named "viewSelect"
#And I should have a selectable view named "Middle School ELA View"
Then I should see a table heading "Student"
Then I should see a table heading "Grade"
And I click on student "Matt Sollars"
#And there are "4" Tabs
#And Tab has a title named "Middle School Overview"
And I logout
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "manthony" "manthony1234" for the "Simple" login page
When I select ed org "Sunset School District 4526"
When I select school "Sunset Central High School"
When I select course "A.P. Calculus"
When I select section "A.P. Calculus Sec 201"
#Then I should only see one view named "College Ready ELA View"
Then I see a list of 3 students
Then I should only see one view named "College Ready ELA View"

@integration @RALLY_US2276
Scenario:  State IT admin upload
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "rrogers" "rrogers1234" for the "Simple" login page
 Then I should be redirected to the Dashboard landing page
When I enter the Configuration Area
Then I am authorized to the Configuration Area
And I logout
When I navigate to the Dashboard home page
When I select "Illinois Daybreak School District 4529" and click go
 And I was redirected to the "Simple" IDP Login page
 When I submit the credentials "akopel" "akopel1234" for the "Simple" login page
 Then I should be redirected to the Dashboard landing page
When I enter the Configuration Area
Then I am unauthorized to the Configuration Area

@integration @RALLY_US2276
Scenario: Upload for hide panel based on condition
When I select "Illinois Sunset School District 4526" and click go
When I submit the credentials "jwashington" "jwashington1234" for the "Simple" login page
Then I should be redirected to the Dashboard landing page
When I enter the Configuration Area
Then I am authorized to the Configuration Area
And I paste configuration to hide csi panel
And click Save
Then I should be shown a success message
When I navigate to the Dashboard home page
When I select ed org "Sunset School District 4526"
When I select school "Sunset Central High School"
When I select course "A.P. Calculus"
When I select section "A.P. Calculus Sec 201"
Then I should only see one view named "Default View"
And I click on student "Matt Forker"
And I view its student profile
And their name shown in profile is "Matt Forker"
And I click on the browser back button
And I click on student "Betty Davis"
And I cannot see csi panel in student profile
When I enter the Configuration Area
And I reset custom config
And click Save
Then I should be shown a success message
