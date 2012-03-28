Feature: Test database encryption.
    As a client application using SLI
    I want to have create, read, update, and delete functionality for a student.

Background: Logged in as a super-user and using the small data set
	Given I am logged in using "demo" "demo1234" to realm "SLI"

       
Scenario: Student data created via the API should be encrypted
    Given format "application/json"
		And no record exists in "student" with a "body.studentUniqueStateId" of "530425896"
    	And a fully populated student record
    When I navigate to POST "/students/" 
    Then I should receive a return code of 201
   		And I should receive an ID for the newly created student
    When I navigate to GET "/students/<'newly created student' ID>"
    Then the "name" should be "Rhonda" "Shannon" "Delgado"
    	And the "birthDate" should be "2006-07-02"
   		And the "sex" should be "Female"
   		And the "studentUniqueStateId" should be "530425896"
	When I find a mongo record in "student" with "body.studentUniqueStateId" equal to "530425896"
        # UNENCRYPTED FIELDS
        And the field "body.studentUniqueStateId" has value "530425896"
        And the field "body.hispanicLatinoEthnicity" has value "false"
        And the field "body.oldEthnicity" has value "Black, Not Of Hispanic Origin"
        And the field "body.race[0]" has value "Black - African American"
        And the field "body.economicDisadvantaged" has value "false"
        And the field "body.schoolFoodServicesEligibility" has value "Reduced price"
        And the field "body.studentCharacteristics[0].characteristic" has value "Parent in Military"
        And the field "body.studentCharacteristics[0].beginDate" has value "2000-10-01"
        And the field "body.limitedEnglishProficiency" has value "NotLimited"
        And the field "body.languages[0]" has value "English"
        And the field "body.homeLanguages[0]" has value "English"
        And the field "body.disabilities[0].disability" has value "Other Health Impairment"
        And the field "body.section504Disabilities[0]" has value "Medical Condition"
        And the field "body.displacementStatus" has value "Slightly to the right"
        And the field "body.learningStyles.visualLearning" has value "33"
        And the field "body.learningStyles.auditoryLearning" has value "33"
        And the field "body.learningStyles.tactileLearning" has value "33"
        And the field "body.cohortYears[0].schoolYear" has value "2010-2011"
        And the field "body.cohortYears[0].cohortYearType" has value "First grade"
        And the field "body.studentIndicators[0].indicatorName" has value "At risk"
        And the field "body.studentIndicators[0].indicator" has value "At risk"
        # ENCRYPTED FIELDS
        And the field "body.name.firstName" with value "Rhonda" is encrypted
        And the field "body.name.middleName" with value "Shannon" is encrypted
        And the field "body.name.lastSurname" with value "Delgado" is encrypted
        And the field "body.otherName[0].firstName" with value "Julie" is encrypted
        And the field "body.otherName[0].middleName" with value "Wren" is encrypted
        And the field "body.otherName[0].lastSurname" with value "Einstein" is encrypted
        And the field "body.otherName[0].otherNameType" with value "Nickname" is encrypted
        And the field "body.sex" with value "Female" is encrypted
        And the field "body.birthData.birthDate" with value "2006-07-02" is encrypted
        And the field "body.address[0].streetNumberName" with value "1234 Shaggy" is encrypted
        And the field "body.address[0].city" with value "Durham" is encrypted
        And the field "body.address[0].postalCode" with value "27701" is encrypted
        And the field "body.address[0].stateAbbreviation" with value "NC" is encrypted
        And the field "body.telephone[0].telephoneNumber" with value "919-555-8765" is encrypted
        And the field "body.electronicMail[0].emailAddress" with value "rsd@summer.nc.edu" is encrypted
        And the field "body.loginId" with value "rsd" is encrypted

Scenario: Sorting on PII across a hop should fail
	Given format "application/json"
		And parameter "sortBy" is "name.firstName"
		And parameter "sortOrder" is "descending"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400
	Given parameter "full-entities" is "true"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400
	Given parameter "sortBy" is "studentUniqueStateId"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 200



Scenario: Can query PII fields by exact (equals) matching
 	Given format "application/json"
 		And student Rhonda Delagio exists
 		And Ronda Delagio is associated with "<'Krypton Middle School' ID>".
        And parameter "name.firstName" is "Rhonda"
		And parameter "full-entities" is "true"
 	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
 	Then I should receive a return code of 200
 		And all students should have "name.firstName" equal to "Rhonda"

Scenario: Can query PII fields by exact (not equals) matching
	Given format "application/json"
		And student Rhonda Delagio exists
		And Ronda Delagio is associated with "<'Krypton Middle School' ID>".
 	Given parameter "name.firstName" is not "Rhonda"
		And parameter "full-entities" is "true"
 	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
 	Then I should receive a return code of 200
 		And no student should have "name.firstName" equal to "Rhonda"

Scenario: Can not query PII fields by non-exact matching
	Given format "application/json"
		And student Rhonda Delagio exists
		And Ronda Delagio is associated with "<'Krypton Middle School' ID>".
		And parameter "name.firstName" less than "Rhonda"
		And parameter "full-entities" is "true"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400
	Given parameter "name.firstName" greater than "Rhonda"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400
	Given parameter "name.firstName" greater than or equal to "Rhonda"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400
	Given parameter "name.firstName" less than or equal to "Rhonda"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400
	Given parameter "name.firstName" matches via regex "Rhonda"
	When I navigate to GET "/student-school-associations/<'Krypton Middle School' ID>/targets"
	Then I should receive a return code of 400