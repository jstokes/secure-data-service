Feature:  Student Profile View

As a teacher in a school district, I want to click on a student and be directed to their profile page.

Background:
Given I have an open web browser

Scenario: View student's profile 
Given the server is in "live" mode
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go
When I login as "linda.kim" "linda.kim1234"
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
#Lozenges check
And the following students have "ELL" lozenges: "Matt Sollars;Odysseus Merrill;Hoyt Hicks;Brielle Klein;Patricia Harper"
# Lozenges check on LOS
Then there is no lozenges for student "Jeanette Graves"
#And I see a header on the page that has the text "Logout"
#And I see a footer on the page that has the text "Copyright"
And I click on student "Kimberley Pennington"
And I view its student profile
And their name shown in profile is "Kimberley Yuli Pennington Jr"
And their id shown in proflie is "437680177"
And their grade is "8"
And the teacher is "Mrs Linda Kim"
And the class is "8th Grade English - Sec 6"
And the lozenges count is "1"
And the lozenges include "ELL"
#And I see a header on the page that has the text "Logout"
#And I see a footer on the page that has the text "Copyright"
#Display hide tabs based on grades
And there are "7" Tabs
And Tab has a title named "Middle School Overview"
And Tab has a title named "Attendance and Discipline"
And Tab has a title named "Assessments"
And Tab has a title named "Grades and Credits"
And Tab has a title named "Advanced Academics"
And Tab has a title named "ELL"
#Check the District tab
And Tab has a title named "Daybreak District"
#Check Enrollment
And Student Enrollment History includes " ;East Daybreak Junior High;8;2012-01-01; ; ; ; ;"
And Student Enrollment History includes " ; ;8;2012-01-01; ; ;2012-01-02; ;"
And Student Enrollment History includes " ; ;8;2012-01-01; ; ;2012-01-02; ;"


Scenario: Student with no grade level
Given the server is in "live" mode
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go
When I login as "linda.kim" "linda.kim1234"
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
And I click on student "Marvin Miller"
And I view its student profile
And their name shown in profile is "Marvin Miller Jr"
And their id shown in proflie is "453827070"
And their grade is "!"
And the teacher is "Mrs Linda Kim"
And the class is "8th Grade English - Sec 6"
And the lozenges count is "0"
#Display hide tabs based on grades
#And there are "8" Tabs
And Tab has a title named "Elementary School Overview"
And Tab has a title named "Middle School Overview"
And Tab has a title named "High School Overview"
#Check the District tab
And Tab has a title named "Daybreak District"
#Check Enrollment
And Student Enrollment History includes " ;East Daybreak Junior High;8;2012-01-01; ; ;2012-01-02; ;"
And Student Enrollment History includes " ; ;8;2012-01-01; ; ;2012-01-02; ;"
	
@wip
Scenario: View a student with other name
Given I am authenticated to SLI as "lkim" password "lkim"
When I click on the Dashboard page

	
