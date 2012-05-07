Feature: Display simple assessment contents

As a SEA/LEA user, I want to be able to select different views in my dashboard
application, that will change the subset of information that is displayed.

Background:
  Given I have an open web browser
  Given the server is in "live" mode

Scenario: Displaying simple ISAT reading and writing results for all students
    When I navigate to the Dashboard home page
    When I select "Sunset School District 4526" and click go
    When I login as "linda.kim" "linda.kim1234"
      When I select <edOrg> "Daybreak School District 4529"
        And I select <school> "East Daybreak Junior High"
        And I select <course> "8th Grade English"
        And I select <section> "8th Grade English - Sec 6"
      And I select view "Middle School ELA View"
	
	 #Most recent reading
     And the scale score for assessment "ISAT Reading" for student "Matt Sollars" is "350"
     #Highest ever writing 
     And the scale score for assessment "ISAT Writing" for student "Matt Sollars" is "220"
     