Feature: SAT Scores 

Background: None

@wip
# ==========================================================
# MOVE THIS TO V1 WHEN COMPLETE, MAKE SURE TESTS PASS
# ==========================================================
Scenario Outline:  As a teacher for my class I want to get the most recent values SAT including Critical Reading, Writing, Mathematics
	Given  I am a valid SEA/LEA end user <Username> with password <Password>
    And I have a Role attribute returned from the "SEA/LEA IDP"
    And the role attribute equals <AnyDefaultSLIRole>
    And I am authenticated on "SEA/LEA IDP"
    
     Given format "application/json"
	When I navigate to GET "/teachers/<'Ms. Jones' ID>"
	Then I should receive a link named "getTeacherSectionAssociations" with URI "/teacher-section-associations/<'Ms. Jones' ID>"
		And I should receive a link named "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
		And I should receive a link named "self" with URI "/teachers/<'Ms. Jones' ID>"
		
	When I navigate to "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
	Then I should receive a collection of 2 section links 
		And I should find section with uniqueSectionCode is "Section I" 
		And I should find section with uniqueSectionCode is "Section II"  with <'ImportantSection' ID>

When I navigate to "getAssessments" with URI "/section-assessment-associations/<'ImportantSection' ID>/targets" with filter sorting and pagination
		And filter by  "assessmentFamilyHierarchyName" = "SAT" 
		And "sort-by" = "assessmentPeriodDescriptor.beginDate"
		 And "sort-order" = "descending" 
		 And "start-index" = "0" 
		 And "max-results" = "1"
	     Then  I should receive a collection of 1 assessment link
	        And I should find Assessment with "<'Most recent SAT' ID>"
        
	When I navigate to GET "/assessments/<'Most recent SAT' ID>"
	    Then I should receive 1 assessment  
		     And  the "assessmentTitle" is "SAT"
		     And the "assessmentCategory" is "College Addmission Test"
		     And the "gradeLevelAssessed" is "Twelfth grade"
		     And the "lowestGradeLevelAssessed" is "Eleventh grade"
		     And the "assessmentFamilyHierarchyName" is "SAT"
		     And the "maxRawScore" is 2400
		     And the "minRawScore" is 600
		     And the assessment has an array of 3 objectiveAssessments
		     And the first one is "objectiveAssessment.identificationCode" = "SAT-Writing"
		     And the first one is "objectiveAssessment.percentOfAssessment" = 33
		     And the first one is "objectiveAssessment.maxRawScore" = 800
		     And the second one is"objectiveAssessment.identificationCode" = "SAT-Math"
		     And the second one is  "objectiveAssessment.percentofAssessment" = "33"
		     And the second one is "objectiveAssessment.maxRawScore" = 800
		     And the third one is "objectiveAssessment.identificationCode" = "SAT-Critical Reading"
		     And the third one is "objectiveAssessment.percentofAssessment" = "33"
		     And the third one is "objectiveAssessment.maxRawScore" = 800
	  
	 When I navigate to GET "/student-section-association/<'ImportantSection' ID>/targets"
		Then I should receive a collection of 5 student links
	 
	 Given I loop through the collection of student links      
	 When I navigate to GET "/student-assessment-associations/<'SAT' ID>"
	     Then I get a collection of 20 student-assessment-associations links 
	     When I filter by studentId is <'Current_student' ID>
	         Then I get 1 student-assessment-association
			    	 And the "administrationDate" is "2011/05/10"
			     And the "gradeLevelWhenAssessed" is "Twelfth Grade"
			     And the "scoreResults.assessmentReportingResultType" is "Scale score"	
			     And the "scoreResults.result" is "2060" 
			     And the "scoreResults.assessmentReportingResultType" is "Percentile"	
			     And the "scoreResults.result" is "92" 
				And the "studentObjectiveAssessment.objectiveAssessment" has the 3 entries
				 And the first one is "studentObjectiveAssessment.objectiveAssessment.identificationCode = "SAT-Writing"
				 And the first one is "studentObjectiveAssessment.scoreResults.assessmentReportingResultType" is "Scale score"	
			     And the first one is "studentObjectiveAssessment.scoreResults.result" is "680"
			     And the first one is "studentObjectiveAssessment.scoreResults.assessmentReportingResultType" is "Percentile score"	
			     And the first one is "studentObjectiveAssessment.scoreResults.result" is "80"
			     And the second one is "studentObjectiveAssessment.objectiveAssessment.identificationCode = "SAT-Math"
				 And the second one is "studentObjectiveAssessment.scoreResults.assessmentReportingResultType" is "Scale score"	
			     And the second one is "studentObjectiveAssessment.scoreResults.result" is "680"
			     And the second one is "studentObjectiveAssessment.scoreResults.assessmentReportingResultType" is "Percentile score"	
			     And the second one is "studentObjectiveAssessment.scoreResults.result" is "80"
			     And the  third one is"studentObjectiveAssessment.objectiveAssessment.identificationCode = "SAT-CriticalReading"
				 And the third one is "studentObjectiveAssessment.scoreResults.assessmentReportingResultType" is "Scale score"	
			     And the third one is "studentObjectiveAssessment.scoreResults.result" is "680"
			     And the third one is "studentObjectiveAssessment.scoreResults.assessmentReportingResultType" is "Percentile score"	
			     And the third one is "studentObjectiveAssessment.scoreResults.result" is "80"

Examples:
| Username        | Password            | AnyDefaultSLIRole  |
#| "educator"      | "educator1234"      | "Educator"         |
| "administrator" | "administrator1234" | "IT Administrator" |
#| "leader"        | "leader1234"        | "Leader"           |


# negative security case docuemented in another file.
