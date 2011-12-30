
Feature: In order to manage assessments
As a SLI Application I want to be able to manage the basic model of assessments. This includes performing CRUD on Assessment entity.
Also verify the correct links from assessment to student-assessment associations and family-assessment associations. An assessment can 
be used to measure differences in individuals or groups and changes in performance from one occasion to the next.


This is the data I am assuming for these tests
AssessmentTitle: Mathematics Achievement Assessment Test, Writing Advanced Placement Test
AcademicSubject: Mathematics, Writing, Foreign Language
GradeLevelAssessed: Adult
ContentStandard: School Standard
AssessmentCategory: Achievement  Test,  Advanced Placement Test

Background: Logged in as a super-user and using the small data set
  	Given I am logged in using "demo" "demo1234"
     Given I have access to all assessments

## JSON
Scenario: Create an assessment 
	Given format "application/json"
		And AssessmentTitle is "Mathematics Achievement Assessment Test"
		And AssessmentIdentificationCode is "01234B"
		And AcademicSubject is "MATHEMATICS"
		And AssessmentCategory is "ACHIEVEMENT_TEST"
		And GradeLevelAssessed is "ADULT_EDUCATION"
		And ContentStandard is "LEA_STANDARD"
		And Version is "1.2"
	When I navigate to POST assessment "Mathematics Test"
	Then I should receive a return code of 201
		And I should receive an ID for a newly created assessment
    When I navigate to GET /assessments/<'newly created assessment' ID>
    Then the AssessmentIdentificationCode is "01234B"
        And AcademicSubject is "MATHEMATICS"
        And AssessmentCategory is "ACHIEVEMENT_TEST"
        And GradeLevelAssessed is "ADULT_EDUCATION"
        And ContentStandard is "LEA_STANDARD"
        And Version is "1.2"

Scenario: Read an assessment by ID
	Given format "application/json"
	When I navigate to GET /assessments/<'Writing Assessment 1' ID>
	Then I should receive a return code of 200
		And the AssessmentTitle should be "Writing Advanced Placement Test" 
		And the AcademicSubject should be "ENGLISH_LANGUAGE_AND_LITERATURE"
		And the AssessmentCategory should be "ADVANCED_PLACEMENT_TEST"

Scenario: Update an assessment by ID
	Given format "application/json"
		When I navigate to GET /assessments/<'Writing Assessment 1' ID>
		Then I should receive a return code of 200
		And the AssessmentTitle should be "Writing Advanced Placement Test" 
	When I set the AssessmentTitle to "Advanced Placement Test - Subject: Writing"
		And I navigate to PUT /assessments/<'Writing Assessment 1' ID>
	Then I should receive a return code of 204
	When I navigate to GET /assessments/<'Writing Assessment 1' ID>
	Then I should receive a return code of 200
	And the AssessmentTitle should be "Advanced Placement Test - Subject: Writing"

Scenario: Delete an assessment by ID
	Given format "application/json"
	When I navigate to DELETE /assessments/<'Writing Assessment 2' ID>
	Then I should receive a return code of 204
	When I navigate to GET /assessments/<'Writing Assessment 2'  ID>
	Then I should receive a return code of 404

## LINKS
@wip
Scenario: Assessment entity links to StudentAssessment association and AssessmentFamily association
   Given format "application/json"
   When I navigate to /assessments/<'Mathematics Achievement Assessment Test' ID>
   Then I should receive a return code of 200
    And I should receive a link named "self" with URI /assessments/<'Mathematics Achievement Assessment Test' ID>
    And I should receive a link named "getStudentAssessmentAssociations" with URI /student-assessment-associations/<'Mathematics Achievement Assessment Test' ID>
	And I should receive a link named "getStudentAssessments" with URI /student-assessment-associations/<'Mathematics Achievement Assessment Test'' ID>/targets
	And I should receive a link named "getAssessmentFamily" with URI /assessment-family-associations/<'Mathematics Achievement Assessment Test' ID>

## ERROR HANDLING
Scenario: Attempt to read a non-existent assessment
   Given format "application/json"
   When I navigate to GET /assessments/<'NonExistentAssessment' ID>
   Then I should receive a return code of 404

Scenario: Attempt to update a non-existent assessment
   Given format "application/json"
   When I navigate to PUT /assessments/'NonExistentAssessment' ID>
   Then I should receive a return code of 404

Scenario: Attempt to delete a non-existent assessment
   Given format "application/json"
   When I navigate to DELETE /assessments/<'NonExistentAssessment' ID>
   Then I should receive a return code of 404
   
   
   
		
