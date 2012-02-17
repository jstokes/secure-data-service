Feature: As a teacher I want to get DIBELS Composite Score and Reading Level

Background: None

@wip
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
    And I should find section with uniqueSectionCode is "Section I" 
    And I should find section with uniqueSectionCode is "Section II"  with <'ImportantSection' ID>
                
    When I navigate to "getAssessments" with URI "/section-assessment-associations/<'ImportantSection' ID>/targets" 
    And I "filter by "AssessmentFamilyHierarchyName" = "DIBELS Next" 
    And I "sort" by "AssessmentPeriodDescriptor.BeginDate"
    And I use "sort_order"="descending" 
    And I set the "page_size"  = "1" 
    And I get the "page"="1"
    Then  I should receive a collection of 1 assessment link
    And after resolution, I should receive an "Assessment" with ID "<'Grade 2 MOY DIBELS' ID>"
        
    When I navigate to  GET "/assessments/<'Grade 2 MOY DIBELS' ID>"
    Then I should receive 1 assessment  
    And the "AssessmentTitle" is "DIBELS-MOY"
    And the "AssessmentCategory" is "Benchmark Test"
    And the "AssessmentSubjectType" is "Reading"
    And the "GradeLevelAssessed" is "Second Grade"
    And the "LowestGradeLevelAssessed" is "Second Grade"
    And the "AssessmentPerformanceLevel" has the 3 levels
    And the "AssessmentPerformanceLevel.PerformanceLevel.Description" is "At or Above Benchmark"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Code" is "Level 1"
    And the "AssessmentPerformanceLevel.MinimumScore" is "190"
    And the "AssessmentPerformanceLevel.MaximumScore" is "380"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Description" is "Below Benchmark"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Code" is "Level 2"
    And the "AssessmentPerformanceLevel.MaximumScore" is "189"
    And the "AssessmentPerformanceLevel.MinimumScore" is "145"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Description" is "Well Below Benchmark"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Code" is "Level 3"
    And the "AssessmentPerformanceLevel.MaximumScore" is "144"
    And the "AssessmentPerformanceLevel.MinimumScore" is "13"
    And the "AssessmentFamilyHierarchyName" is "DIBELS Next"
    And the "MaxRawScore" is "380"
    And the "MinRawScore" is "13"
    And the "AssessmentPeriodDescriptor.BeginDate" is "2012/01/01"
    And the "AssessmentPeriodDescriptor.EndDate" is "2012/02/01"
        
    When I navigate to GET "/student-section-association/<'Important_Section' ID>/targets"
    Then I should receive a collection of 5 student links
    And after resolution, I should receive a "Student"" with ID <'John Doe' ID>
    And after resolution, I should receive a "Student" with ID  <'Sean Deer' ID>
    And after resolution, I should receive a "Student" with ID  <'Suzy Queue' ID>
    And after resolution, I should receive a "Student" with ID  <'Mary Line' ID>
    And after resolution, I should receive a "Student" with ID  <'Dong Steve' ID>
     
    Given I loop through the collection of student links
    When I navigate to GET "/student-assessment-associations/<'Grade 2 MOY DIBELS' ID>"
    And filter by <'Current_student' ID> 
    And "sort_by" ="studentAssessmentAssociation.administrationDate"
    And "sort_order"="descending"
    And set the "page_size"  = "1" 
    And get the "page"="1"
    Then I should receive a collection of 1 assessment link 
    And after resolution, I should receive a "student-assessment-association" with ID "<'Most Recent Assessment Association' ID>"
         
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

@wip
Scenario Outline:  (paging/sorting) As a teacher, for my class, I want to get the most recent values of the following attributes: DIBELSCompositeScore, ReadingInstructionalLevel, PerformanceLevel
    Given  I am valid SEA/LEA end user <Username> with password <Password>
    And I have a Role attribute returned from the "SEA/LEA IDP"
    And the role attribute equals <AnyDefaultSLIRole>
    And I am authenticated on "SEA/LEA IDP"

    When I navigate to GET "/tteachers/<'Ms. Jones' ID>"
    Then I should receive a link named "getTeacherSectionAssociations" with URI "teacher-section-associations/<'Ms. Jones' ID>"
    And I should receive a link named "getSections" with URI "teacher-section-associations/<'Ms. Jones' ID>/targets"
    And I should receive a link named "self" with URI "/teachers/<'Ms. Jones' ID>"
        
    When I navigate to "getSections" with URI "teacher-section-associations/<'Ms. Jones' ID>/targets"
    Then I should receive a collection of 2 section links 
    And I should find section with uniqueSectionCode is "Section I" 
    And I should find section with uniqueSectionCode is "Section II"  with <'ImportantSection' ID>
                
    When I navigate to "getStudents" with URI "/student-section-associations/<'ImportantSection' ID>/targets"
    Then I should receive a collection of 5 student links
    And after resolution, I should receive a "Student"" with ID <'John Doe' ID>
    And after resolution, I should receive a "Student" with ID  <'Sean Deer' ID>
    And after resolution, I should receive a "Student" with ID  <'Suzy Queue' ID>
    And after resolution, I should receive a "Student" with ID  <'Mary Line' ID>
    And after resolution, I should receive a "Student" with ID  <'Dong Steve' ID>
              
    When I navigate to each student     
    Then I navigate to "getAssessments" with URI "/student-assessment-associations/<'Each Student' ID>/targets" 
    And filter by assessmentFamily is "DIBELS Next"
    And  filter by assessmentTitle is "Grade 2 MOY DIBELS"
    Then I should receive 1 assessment with ID <'Grade 2 MOY DIBELS' ID>
    And the "AssessmentCategory" is "Benchmark Test"
    And the "AssessmentSubjectType" is "Reading"
    And the "GradeLevelAssessed" is "Second Grade"
    And the "LowestGradeLevelAssessed" is "Second Grade"
    And the "AssessmentPerformanceLevel" has the 3 levels
    And the "AssessmentPerformanceLevel.PerformanceLevel.Description" is "At or Above Benchmark"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Code" is "Level 1"
    And the "AssessmentPerformanceLevel.MinimumScore" is "190"
    And the "AssessmentPerformanceLevel.MaximumScore" is "380"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Description" is "Below Benchmark"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Code" is "Level 2"
    And the "AssessmentPerformanceLevel.MaximumScore" is "189"
    And the "AssessmentPerformanceLevel.MinimumScore" is "145"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Description" is "Well Below Benchmark"
    And the "AssessmentPerformanceLevel.PerformanceLevel.Code" is "Level 3"
    And the "AssessmentPerformanceLevel.MaximumScore" is "144"
    And the "AssessmentPerformanceLevel.MinimumScore" is "13"
    And the "AssessmentFamilyHierarchyName" is "DIBELS.DIBELS Next.DIBELS Next Grade 2"
    And the "MaxRawScore" is "380"
    And the "MinRawScore" is "13"
    And the "AssessmentPeriodDescriptor.BeginDate" is "2012/01/01"
    And the "AssessmentPeriodDescriptor.EndDate" is "2012/02/01"
             
    When I navigate to "getStudentAssessmentAssociations" with URI "/student-assessment-associations/<'Grade 2 MOY DIBELS' ID>"
    And filter by studentId is <'Each Student' ID>
    And "sort_by" ="administrationDate"
    And "sort_order"="descending" 
    And set the "page_size"  = "1" 
    And get the "page"="1"
    Then I get the first URI "/student-assessment-associations/<'Most Recent Assessment Association' ID>"
         
    When I navigate to "/student-assessment-associations/<'Most Recent Assessment Association' ID>"
    Then I receive a 1 student-assessment-association 
    And the "AdministrationDate" is "2012/01/10"
    And the "AdministrationEndDate" is "2012/01/15"
    And the "GradeLevelWhenAssessed" is "Second Grade" 
    And the "PerformanceLevelDescriptor.Code" is "Level 2"
    And the "PerformanceLevelDescriptor.Code" is "Below Benchmark"
    And the "ScoreResults.AssessmentReportingResultType" is "ScaleScore"    
    And the "ScoreResults.Result" is "120"     
    
Examples:
| Username        | Password            | AnyDefaultSLIRole  |
| "educator"      | "educator1234"      | "Educator"         |
| "administrator" | "administrator1234" | "IT Administrator" |
| "leader"        | "leader1234"        | "Leader"           |


Scenario Outline:  As a AggregateViewer I should not see personally identifiable information data
    Given I am a valid SEA/LEA end user <username> with password <password>
    And I have a Role attribute returned from the "SEA/LEA IDP"
    And the role attribute equals "Aggregate Viewer"
    And I am authenticated on "SLI Realm"

    When I navigate to GET "/teachers/<'Ms. Smith' ID>"
    Then I should receive a return code of 403
        
    When I navigate to GET "/teacher-section-associations/<'Teacher Ms. Jones and Section Algebra II' ID>/targets"
    Then I should receive a return code of 403

    When I navigate to GET "/student-section-associations/<'Algebra II' ID>/targets"
    Then I should receive a return code of 403
         
    When I navigate to GET "/students/<'Jane Doe' ID>"      
    Then I should receive a return code of 403
    
    When I navigate to GET "/sections/<'Algebra II' ID>"      
    Then I should receive a return code of 403
        
    When I navigate to GET "/assessments/<'Grade 2 BOY DIBELS' ID>"
    Then I should receive a return code of 403

Examples:
| Username         | Password             | AnyDefaultSLIRole  |
| "aggregateViewer"| "aggregate1234"      | "AggregateViewer"  |
     
