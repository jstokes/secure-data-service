
Feature: List Students in SampleApp

As a SEA/LEA user, I want to be able to see list of students.

Background:
    Given I have an open web browser
    Given the sampleApp is deployed on sampleApp server
	

Scenario: Teacher sees list of students
		When I navigate to the sampleApp home page
		Then I should be redirected to the Realm page
		When I select "Illinois Realm" and click go
		When I login as "cgray" "cgray1234"
		When I go to List of Students
		Then the page should include a table with header "Student"
		And I should see student "Swanson, Alec" in the student list


