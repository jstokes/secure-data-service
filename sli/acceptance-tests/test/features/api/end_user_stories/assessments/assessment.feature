@wip
Feature: As a teacher I want to get DIBELS Composite Score and Reading Level

Background: None

Scenario Outline:  (sorting) As a teacher, for my class, I want to get the most recent DIBELS assessment
	Given  I am valid SEA/LEA end user <Username> with password <Password>
	And I have a Role attribute returned from the "SEA/LEA IDP"
	And the role attribute equals <AnyDefaultSLIRole>
	And I am authenticated on "SEA/LEA IDP"

	When I navigate to GET "/teachers/<'Ms. Jones' ID>"
	Then I should receive a link named "getTeacherSectionAssociations" with URI "/teacher-section-associations/<'Ms. Jones' ID>"
		And I should receive a link named "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
		And I should receive a link named "self" with URI "/teachers/<'Ms. Jones' ID>"
		
	When I navigate to "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
	Then I should receive a collection of 2 section links 
		And I should find section with sectionName is "Section I" 
		And I should find section with sectionName is "Section II"  with <'ImportantSection' ID>
				
	When I navigate to "getAssessments" with URI "/section-assessment-associations/<'ImportantSection' ID>/targets" 
		and filter by  "AssessmentFamilyHierarchyName" = "DIBELS Next" 
		and sort by AssessmentPeriodDescriptor.BeginDate, descending
	     Then  I should receive a collection of 1 assessment link
	        And after resolution, I should receive an "Assessment" with ID "<'Grade 2 MOY DIBELS' ID>"
        
	When I navigate to  GET "/assessments/<'Grade 2 MOY DIBELS' ID>"
	    Then I should receive 1 assessment  
		     And  the "AssessmentTitle" is "DIBELS-MOY"
		     And the "AssessmentCategory" is "Benchmark Test"
		     And the "AssessmentSubjectType" is "Reading"
		     And the "GradeLevelAssessed" is "Second Grade"
		     And the "LowestGradeLevelAssessed" is "Second Grade"
		     And the "AssessmentPerformanceLevel" has the three levels
				     PerformanceLevel= "At or Above Benchmark"
				     MinimumScore = "190"
				     MaximumScore = "380"
				     PerformanceLevel= "Below Benchmark"
				     MinimumScore = "189"
				     MaximumScore = "145"
				     PerformanceLevel= "Well Below Benchmark"
				     MinimumScore = "144"
				     MaximumScore = "13"
		     And the "AssessmentFamilyHierarchyName" is "DIBELS Next"
		     And the "MaxRawScore" is "380"
		     And the "MinRawScore" is "13"
		     And the "AssessmentPeriodDescriptor.BeginDate" = "2012/01/01"
		     And the "AssessmentPeriodDescriptor.EndDate" = "2012/02/01"
	    
	 When I navigate to GET "/student-assessment-associations/<'Grade 2 MOY DIBELS' ID>"
		 Then sort by administrationDate, descending
		 And get an ordered collection of URI "/student-assessment-associations/<'Most Recent Assessment Association' ID>"
	     
	  When I navigate to URI "/student-assessment-associations/<'Most Recent Assessment Association' ID>"
	     Then I get 1 student-assessment-association
			    	 And the "AdministrationDate" is "2012/01/10"
			     And the "AdministrationEndDate" is "2012/01/15"
			     And the "GradeLevelWhenAssessed" is "Second Grade"
			     And the "AssessmentFamily" is "DIBELS Next Grade 2"
			     And the "PerformanceLevel" is "Below Benchmark"
			     And the "ScoreResult" is "120"
			     And the "ScaleScore" is "120"   
			     
Examples:
| Username        | Password            | AnyDefaultSLIRole  |
| "educator"      | "educator1234"      | "Educator"         |
| "administrator" | "administrator1234" | "IT Administrator" |
| "leader"        | "leader1234"        | "Leader"           |

Scenario Outline:  (paging/sorting) As a teacher, for my class, I want to get the most recent values of the following attributes: DIBELSCompositeScore, ReadingInstructionalLevel, PerformanceLevel
	Given  I am valid SEA/LEA end user <Username> with password <Password>
	And I have a Role attribute returned from the "SEA/LEA IDP"
	And the role attribute equals <AnyDefaultSLIRole>
	And I am authenticated on "SEA/LEA IDP"

	When I navigate to GET "/teachers/<'Ms. Jones' ID>"
	Then I should receive a link named "getTeacherSectionAssociations" with URI "/teacher-section-associations/<'Ms. Jones' ID>"
		And I should receive a link named "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
		And I should receive a link named "self" with URI "/teachers/<'Ms. Jones' ID>"
		
	When I navigate to "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
	Then I should receive a collection of 2 section links 
		And I should find section with sectionName is "Section I" 
		And I should find section with sectionName is "Section II"  with <'ImportantSection' ID>
				
	When I navigate to "getStudents" with URI "/student-section-associations/<'ImportantSection' ID>/targets"
	Then I should receive a collection of 5 student links
		And after resolution, I should receive a "Student"" with ID <'John Doe' ID>
		And after resolution, I should receive a "Student" with ID  <'Sean Deer' ID>
		And after resolution, I should receive a "Student" with ID  <'Suzy Queue' ID>
		And after resolution, I should receive a "Student" with ID  <'Mary Line' ID>
	 	And after resolution, I should receive a "Student" with ID  <'Dong Steve' ID>
	 	 	
	When I navigate to each student 	
		 Then I navigate to "getAssessments" with URI "/student-assessment-associations/<'Each Student' ID>/targets" and filter by assessmentTitle is "DIBELS-MOY"
	    Then I should receive 1 assessment with ID "Grade 2 MOY DIBELS"
		    And the "AssessmentCategory" is "Benchmark Test"
		     And the "AssessmentSubjectType" is "Reading"
		     And the "GradeLevelAssessed" is "Second Grade"
		     And the "LowestGradeLevelAssessed" is "Second Grade"
		     And the "AssessmentPerformanceLevel" has the three levels
				     PerformanceLevel= "At or Above Benchmark"
				     MinimumScore = "190"
				     MaximumScore = "380"
				     PerformanceLevel= "Below Benchmark"
				     MinimumScore = "189"
				     MaximumScore = "145"
				     PerformanceLevel= "Well Below Benchmark"
				     MinimumScore = "144"
				     MaximumScore = "13"
		     And the "AssessmentFamilyHierarchyName" is "DIBELS Next"
		     And the "MaxRawScore" is "380"
		     And the "MinRawScore" is "13"
		     And the "AssessmentPeriodDescriptor.BeginDate" = "2012/01/01"
		     And the "AssessmentPeriodDescriptor.EndDate" = "2012/02/01"
		     
	    When I navigate to "getStudentAssessmentAssociations" with URI "/student-assessment-associations/<'Grade 2 MOY DIBELS' ID>"
	    	 And filter by studentId is <'Each Student' ID>
		 And sort by administrationDate, descending and set the page size to 1 and get the first URI "/student-assessment-associations/<'Most Recent Assessment Association' ID>"
		 
		 When I navigate to "/student-assessment-associations/<'Most Recent Assessment Association' ID>"
		 Then I receive a 1 student-assessment-association 
			    	 And the "AdministrationDate" is "2012/01/10"
			     And the "AdministrationEndDate" is "2012/01/15"
			     And the "GradeLevelWhenAssessed" is "Second Grade"
			     # the assessmentFamily is different from assessmentFamilyHierarchyName. 
			     And the "AssessmentFamily" is "DIBELS Next Grade 2"   
			     And the "PerformanceLevel" is "Below Benchmark"
			     And the "ScoreResult" is "120"	
			     And the "ScaleScore" is "120"     
Examples:
| Username        | Password            | AnyDefaultSLIRole  |
| "educator"      | "educator1234"      | "Educator"         |
| "administrator" | "administrator1234" | "IT Administrator" |
| "leader"        | "leader1234"        | "Leader"           |


Scenario Outline:  As a AggregateViewer I should not get DIBELS Composite Score and Reading Level
	Given  I am valid SEA/LEA end user <Username> with password <Password>
	And I have a Role attribute returned from the "SEA/LEA IDP"
	And the role attribute equals <AnyDefaultSLIRole>
	And I am authenticated on "SEA/LEA IDP"

	When I navigate to GET "/teachers/<'Ms. Jones' ID>"
	Then I should receive a link named "getTeacherSectionAssociations" with URI "/teacher-section-associations/<'Ms. Jones' ID>"
		And I should receive a link named "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
		And I should receive a link named "self" with URI "/teachers/<'Ms. Jones' ID>"
		
	When I navigate to "getSections" with URI "/teacher-section-associations/<'Ms. Jones' ID>/targets"
	Then I should receive a collection of 2 section links 
		And I should find section with sectionName is "Section I" 
		And I should find section with sectionName is "Section II"  with <'ImportantSection' ID>
				
	When I navigate to "getStudents" with URI "/student-section-associations/<'ImportantSection' ID>/targets"
	Then I should receive a collection of 5 student links
		And after resolution, I should receive a "Student"" with ID <'John Doe' ID>
		And after resolution, I should receive a "Student" with ID  <'Sean Deer' ID>
		And after resolution, I should receive a "Student" with ID  <'Suzy Queue' ID>
		And after resolution, I should receive a "Student" with ID  <'Mary Line' ID>
	 	And after resolution, I should receive a "Student" with ID  <'Dong Steve' ID>
	 	
	# this is to enable paging 	
	When I navigate to each student  	
	Then I should not receive a link named "getAssessments" with URI "/student-assessment-associations/<'Each Student' ID>/targets"
Examples:
| Username         | Password             | AnyDefaultSLIRole  |
| "aggregateViewer"| "aggregate1234"      | "AggregateViewer"         |
	 
