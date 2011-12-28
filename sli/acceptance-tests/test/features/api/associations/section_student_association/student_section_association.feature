@wip
Feature: In order to manage sections and students
As a SLI application I want to use this association to get the course section(s) a student is assigned to, or all the student(s) a section has.  
I must be able to perform CRUD functions on student-section association.

This is the data I am assuming for these tests
Section:  Biology II - C
Student: Jane Doe

Background: 
	Given I am logged in using "demo" "demo1234"

Scenario: Create a student-section-association
Given format "application/vnd.slc+json"
	And "sectionId" is Section "Biology II - C"
	And "studentId" is Student "Jane Doe"
When I navigate to POST "/student-section-associations"
Then I should receive a return code of 201
	And I should receive a ID for the newly created student-section-association

Scenario: Read a student-section-association
Given format "application/vnd.slc+json"
When I navigate to GET Student Section Association for Student "Jane Doe" and Section "Biology II - C"
Then I should receive a return code of 200
	And I should receive 1 student-section-assoications
	And I should receive a link named "getStudent" with URI for Student "Jane Doe"
	And I should receive a link named "getSection" with URI for Section "Biology II - C"
	And the begin date should be "09/15/2011"
	And the end date should be "12/15/2011"
	And the sequence of course should be 1
	And the Repeat Identifier should be 1

Scenario: Reading a student-section-association for a student
Given format "application/vnd.slc+json"
When I navigate to GET Student Section Associations for the Student "Jane Doe"
Then I should receive a return code of 200
	And I should receive 4 student-section-associations
	And I should receive a link named "getStudent" with URI for Student "Jane Doe"
	And I should receive a link named "getSection" with URI for Section "Foreign Language - A"
	And I should receive a link named "getSection" with URI for Section "Biology II - C"
	And I should receive a link named "getSection" with URI for Section "Physics I - B"
	And I should receive a link named "getSection" with URI for Section "Chemistry I - A"

Scenario: Reading a student-section-association for a section
Given  format "application/vnd.slc+json"
When I navigate to GET Student Section Associations for the Section "Chemistry I - A"
Then I should receive a return code of 200
	And I should receive 3 student-section-associations
	And I should receive a link named "getStudent" with URI for Student "Jane Doe"
	And I should receive a link named "getStudent" with URI for Student "Albert Wright"
	And I should receive a link named "getStudent" with URI for Student "Kevin Smith"
	And I should receive a link named "getSection" with URI for Section "Chemistry I - A"

Scenario: Update a student-section-association 
Given  format "application/vnd.slc+json"
And I navigate to GET Student Section Associations for the Section "Biology II - C" and Student "Jane Doe"
	#And Medium of Instruction is "Center-Based"
When I set the Medium of Instruction to "Correspondence Instruction"
	And I navigate to PUT /student-section-associations/<the previous association ID>
Then I should get a return code of 2xx
	And I navigate to GET /student-section-associations/<the previous association ID>
	#And the Medium of Instruction should be "Correspondence Instruction"

Scenario: Delete a student-section-association
Given format "application/vnd.slc+json"
And I navigate to DELETE /student-section-associations/<the previous association Id>
Then I should get a return code of 2xx
	And I navigate to GET /student-section-associations/<the previous association Id>
	And I should receive a return code of 404

