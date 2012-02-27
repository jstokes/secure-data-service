Feature: As an SLI application, I want to be able to manage sections
    As a client application using SLI
    I want to have create, read, update, and delete functionality for a section.

Background: Logged in as a super-user and using the small data set
    Given I am logged in using "demo" "demo1234"
    Given I have access to all sections

#### Happy Path 
Scenario Outline: Create a new section
    Given format "<format>"
      And the "uniqueSectionCode" is "SpanishB09"
      And the "sequenceOfCourse" is "1"
      And the "educationalEnvironment" is "Off-school center"
      And the "mediumOfInstruction" is "Independent study"
      And the "populationServed" is "Regular Students"
      And the "schoolId" is "<'APPLE ELEMENTARY (SCHOOL)' ID>"
      And the "sessionId" is "<'FALL 2011 (SESSION)' ID>"
      And the "courseId" is "<'FRENCH 1 (COURSE)' ID>"
    When I navigate to POST "/v1/sections/" 
    Then I should receive a return code of 201
      And I should receive an ID for the newly created section
    When I navigate to GET "/v1/sections/<'newly created section' ID>"
     Then the "uniqueSectionCode" should be "SpanishB09"
      And the "sequenceOfCourse" should be "1"
      And the "educationalEnvironment" should be "Off-school center"
      And the "mediumOfInstruction" should be "Independent study"
      And the "populationServed" should be "Regular Students"
      And the "schoolId" should be "<'APPLE ELEMENTARY (SCHOOL)' ID>"
      And the "sessionId" should be "<'FALL 2011 (SESSION)' ID>"
      And the "courseId" should be "<'FRENCH 1 (COURSE)' ID>"
    Examples:
    		| format                     |
    		| application/json 			 |

Scenario Outline: Read a section by id
    Given format "<format>"
    When I navigate to GET "/v1/sections/<'chemistryF11' ID>"
    Then I should receive a return code of 200
    	And the "sequenceOfCourse" should be "1"
     	And the "educationalEnvironment" should be "Classroom"
     	And the "mediumOfInstruction" should be "Virtual_on_line_distance_learning"
     	And the "populationServed" should be "Bilingual_students"
        And I should receive a link named "getTeacherSectionAssociations" with URI "/v1/sections/<'chemistryF11' ID>/teacherSectionAssociations"
	    And I should receive a link named "getStudentSectionAssociations" with URI "/v1/sections/<'chemistryF11' ID>/studentSectionAssociations"
   	    And I should receive a link named "getTeachers" with URI "/v1/sections/<'chemistryF11' ID>/teacherSectionAssociations/teachers"
	    And I should receive a link named "getStudents" with URI "/v1/sections/<'chemistryF11' ID>/studentSectionAssociations/students"
	    And I should receive a link named "self" with URI "/v1/sections/<'chemistryF11' ID>"
    Examples:
    		| format                 |
    		| application/vnd.slc+json       |

Scenario Outline: Update an existing section
    Given format "<format>"
      When I navigate to GET "/v1/sections/<'biologyF09' ID>"
   Then I should receive a return code of 200
     And the "sequenceOfCourse" should be "1"
  When I set the "sequenceOfCourse" to "2"
    When I navigate to PUT "/v1/sections/<'biologyF09' ID>"
    Then I should receive a return code of 204
    When I navigate to GET "/v1/sections/<'biologyF09' ID>"
     And the "sequenceOfCourse" should be "2"
    Examples:
		| format                 |
		| application/json       |
        
Scenario Outline: Delete an existing section
    Given format "<format>"
    When I navigate to DELETE "/v1/sections/<'biologyF09' ID>"
    Then I should receive a return code of 204
    When I navigate to GET "/v1/sections/<'biologyF09' ID>"
    Then I should receive a return code of 404
    Examples:
		| format                 |
		| application/json       |
    
    
###Links
@wip
Scenario: Section Resource links to teacher section association
   Given format "application/vnd.slc+json"
   When I navigate to GET /v1/sections/<'biology567' ID>
   Then I should receive a return code of 200
      And I should receive a link named "getTeacherSectionAssociations" with URI "/v1/sections/<'biology567' ID>/teacherSectionAssociations"
	  And I should receive a link named "getStudentSectionAssociations" with URI "/v1/sections/<'biology567' ID>/studentSectionAssociations"
   	  And I should receive a link named "getTeachers" with URI "/v1/sections/<'biology567' ID>/teacherSectionAssociations/teachers"
	  And I should receive a link named "getStudents" with URI "/v1/sections/<'biology567' ID>/studentSectionAssociations/students"
	  And I should receive a link named "self" with URI "/v1/sections/<'biology567' ID>"
    
### Error handling
Scenario: Attempt to read a non-existing section
    Given format "application/json"
    When I navigate to GET "/v1/sections/<'Invalid' ID>"
    Then I should receive a return code of 404      

Scenario: Attempt to delete a non-existing section
    Given format "application/json"
    When I navigate to DELETE "/v1/sections/<'Invalid' ID>"
    Then I should receive a return code of 404      

Scenario: Update a non-existing section
    Given format "application/json"
    When I navigate to PUT "/v1/sections/<'Invalid' ID>"
    Then I should receive a return code of 404      
    
Scenario: Fail when asking for an unsupported format "text/plain"
    Given format "text/plain"
    When I navigate to GET "/v1/sections/<'physicsS08' ID>"
    Then I should receive a return code of 406
    
Scenario: Fail if going to the wrong URI
    Given format "application/json"
    When I navigate to GET "/v1/section/<'physicsS08' ID>"
    Then I should receive a return code of 404
    
 Scenario: Attempt to read the base section resource with no GUID
    Given format "application/json"
    When I navigate to GET "/sections/"
    Then I should receive a return code of 405