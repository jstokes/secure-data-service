
Feature:  Dashboard Tests For SDS Data

Background:
Given I have an open web browser
Given the server is in "live" mode
When I navigate to the Dashboard home page
When I select "Illinois Sunset School District 4526" and click go

@integration
Scenario: View Matt Sollars
When I login as "linda.kim" "linda.kim1234"
When I select ed org "Daybreak School District 4529"
When I select school "East Daybreak Junior High"
When I select course "8th Grade English"
When I select section "8th Grade English - Sec 6"
Then I see a list of 28 students
And the list includes: "Matt Sollars"
And the following students have "ELL" lozenges: "Matt Sollars;Alton Maultsby;Malcolm Costillo"
And there is no lozenges for student "Lettie Hose"
And the count for id "ATTENDANCE.AbsenceCount" for student "Matt Sollars" is "5"
And the class for id "ATTENDANCE.AbsenceCount" for student "Matt Sollars" is "countLevel2"
And the count for id "ATTENDANCE.AttendanceRate" for student "Matt Sollars" is "97"
And the class for id "ATTENDANCE.AttendanceRate" for student "Matt Sollars" is "perfLevel4"
And the count for id "ATTENDANCE.TardyCount" for student "Matt Sollars" is "0"
And the class for id "ATTENDANCE.TardyCount" for student "Matt Sollars" is "countLevel1"
And the count for id "ATTENDANCE.TardyRate" for student "Matt Sollars" is "0"
And the class for id "ATTENDANCE.TardyRate" for student "Matt Sollars" is "perfLevel5"
And I click on student "Matt Sollars"
And I view its student profile
And their name shown in profile is "Matt Joseph Sollars Jr"
And their id shown in proflie is "800000025"
And their grade is "8"
And the teacher is "Mrs Linda Kim"
And the class is "8th Grade English - Sec 6"
And the lozenges count is "1"
And the lozenges include "ELL"
And there are "7" Tabs
And Tab has a title named "Middle School Overview"
And Tab has a title named "Attendance and Discipline"
And Tab has a title named "Assessments"
And Tab has a title named "Grades and Credits"
And Tab has a title named "Advanced Academics"
And Tab has a title named "ELL"
And Tab has a title named "Daybreak District"
And I look at the panel "Contact Information"
And there are "1" email addresses
And the list of email address includes "m.sollars@gmail.com"
And there are "1" phone numbers
And the list of phone number includes "309-555-2449"
And the phone number "309-555-2449" is of type "Home"
And there are "0" addresses
And there are "6" student enrollment history entries
And Student Enrollment History includes " ;East Daybreak Junior High;8;2011-09-01; ; ; ; ;"
And Student Enrollment History includes " ;East Daybreak Junior High;7;2010-09-01;Next year school; ;2011-05-11;End of school year"
And Student Enrollment History includes " ;East Daybreak Junior High;6;2009-09-07;Transfer from a public school in the same local education agency; ;2010-05-11;End of school year"
And Student Enrollment History includes " ; ;5;2008-09-05;Next year school; ;2009-05-11;End of school year"
And Student Enrollment History includes " ; ;4;2007-09-12;Next year school; ;2008-05-10;End of school year"
And Student Enrollment History includes " ; ;3;2006-09-11;Transfer from a private, religiously-affiliated school in a different state; ;2007-05-09;Student is in a different public school in the same local education agency"

@integration 
Scenario: View Mi-Ha Tran
When I login as "rbraverman" "rbraverman1234"
When I select ed org "Daybreak School District 4529"
When I select school "South Daybreak Elementary"
And I select course "1st Grade Homeroom"
And I select section "Mrs. Braverman's Homeroom #38"
Then I see a list of 25 students
And the list includes: "Mi-Ha Tran"
And the following students have "ELL" lozenges: "Malcolm Haehn;Dara Nemecek;Lauretta Seip"
And I click on student "Mi-Ha Tran"
And I view its student profile
And their name shown in profile is "Mi-Ha Tran"
And their id shown in proflie is "100000017"
And their grade is "1"
And the teacher is "Ms Rebecca Braverman"
And the class is "Mrs. Braverman's Homeroom #38"
And the lozenges count is "0"
And there are "6" Tabs
And Tab has a title named "Elementary School Overview"
And Tab has a title named "Attendance and Discipline"
And Tab has a title named "Assessments"
And Tab has a title named "Grades and Credits"
And Tab has a title named "Advanced Academics"
And Tab has a title named "Daybreak District"
And I look at the panel "Contact Information"
And there are "3" email addresses
And the list of email address includes "ti.troung@gmail.com"
And the list of email address includes "ti.troung@unicef.com"
And the list of email address includes "ti.troung@yahoo.com"
And the order of the email addresses is "ti.troung@gmail.com;ti.troung@unicef.com;ti.troung@yahoo.com"
And there are "2" phone numbers
And the list of phone number includes "309-555-5210"
And the list of phone number includes "309-555-5341"
And the phone number "309-555-5210" is of type "Unlisted"
And the phone number "309-555-5341" is of type "Unlisted"
And the order of the phone numbers is "309-555-5210;309-555-5341"
And there are "2" addresses
And the list of address includes 
"""
34 Northshore Ave, Apt 9B
Daybreak, IL 75229
"""
And the list of address includes 
"""
82 N. Central Pkwy., Suite B
Daybreak, IL 75229
"""
And the order of the addressess is "34 Northshore Ave;82 N. Central Pkwy."
And there are "2" student enrollment history entries
And Student Enrollment History includes " ;South Daybreak Elementary;1;2011-09-05;Next year school; ; ; ;"
And Student Enrollment History includes " ;South Daybreak Elementary;K;2010-09-03;Original entry into a United States school; ;2011-05-11;End of school year"

@integration
Scenario: View Carmen Ortiz
When I login as "cgray" "cgray1234"
When I select ed org "Daybreak School District 4529"
When I select school "Daybreak Central High"
And I select course "American Literature"
And I select section "Sec 145"
Then I see a list of 25 students
And the list includes: "Carmen Ortiz"
And the following students have "ELL" lozenges: "Randolph Vanhooser;Kelvin Zahm;Johnathan Zenz"
And I click on student "Carmen Ortiz"
And I view its student profile
And their name shown in profile is "Carmen Daniella Ortiz"
And their id shown in proflie is "900000016"
And their grade is "11"
And the teacher is "!"
#And the class is "American Literature"
And the lozenges count is "0"
#And there are "6" Tabs
And Tab has a title named "High School Overview"
And Tab has a title named "Attendance and Discipline"
And Tab has a title named "Assessments"
And Tab has a title named "Grades and Credits"
And Tab has a title named "Advanced Academics"
#And Tab has a title named "Daybreak District"
And I look at the panel "Contact Information"
And there are "1" email addresses
And the list of email address includes "c.ortiz@gmail.com"
And there are "1" phone numbers
And the list of phone number includes "309-555-9372"
And the phone number "309-555-9372" is of type "Other"
And there are "0" addresses
And there are "9" student enrollment history entries
And Student Enrollment History includes " ;Daybreak Central High;11;2011-09-08;Next year school; ; ; ;"
And Student Enrollment History includes " ;Daybreak Central High;10;2010-09-08;Next year school; ;2011-05-11;End of school year"
And Student Enrollment History includes " ;Daybreak Central High;9;2009-09-08;Transfer from a public school in the same local education agency; ;2010-05-11;End of school year"
And Student Enrollment History includes " ; ;8;2008-09-11;Transfer from a public school in the same local education agency; ;2009-05-08;Exited"
And Student Enrollment History includes " ; ;7;2008-03-11;Transfer from a public school in the same local education agency; ;2008-05-08;End of school year"
And Student Enrollment History includes " ; ;7;2007-09-14;Next year school; ;2008-02-12;Student is in a different public school in the same local education agency"
And Student Enrollment History includes " ; ;6;2006-09-11;Next year school; ;2007-05-14;End of school year"
And Student Enrollment History includes " ; ;5;2005-09-09;Transfer from a private, religiously-affiliated school in the same local education agency; ;2006-05-15;Exited"
And Student Enrollment History includes " ; ;2;2002-09-12;Transfer from a school outside of the country; ;2003-04-12;Expelled or involuntarily withdrawn"

