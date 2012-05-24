@RALLY_US209
@RALLY_US210
Feature: As a teacher I want to get DIBELS Composite Score and Reading Level

Background: None

@wip
    Scenario Outline:  (sorting) As a teacher, for my class, I want to get the most recent Math assessment
    Given I am a valid SEA/LEA end user <Username> with password <Password>
    And I have a Role attribute returned from the "SLI"
    And the role attribute equals <AnyDefaultSLIRole>
    And I am authenticated on "IL"

     Given format "application/json"
     When I navigate to GET "/<TEACHER URI>/<'Linda Kim' ID>"
      Then I should receive a link named "getTeacherSectionAssociations" with URI "/<TEACHER URI>/<'Linda Kim' ID>/<TEACHER SECTION ASSOC URI>"
        And I should receive a link named "getSections" with URI "/<TEACHER URI>/<'Linda Kim' ID>/<TEACHER SECTION ASSOC URI>/<SECTION URI>"
        And I should receive a link named "self" with URI "/<TEACHER URI>/<'Linda Kim' ID>"

     When I navigate to GET "/<TEACHER URI>/<'Linda Kim' ID>/<TEACHER SECTION ASSOC URI>/<SECTION URI>"
      Then I should have a list of 2 "section" entities
        And I should have an entity with ID "<'8th Grade English - Sec 6' ID>"

     When I navigate to URI "/<SECTION URI>/<'8th Grade English - Sec 6' ID>/<SECTION ASSESSMENT ASSOC URI>/<ASSESSMENT URI>" with filter sorting and pagination
        And filter by "assessmentTitle" = "Mathematics Achievement Assessment Test"
#        And filter by "sortBy" = "assessmentPeriodDescriptor.beginDate"
#        And filter by "sortOrder" = "descending"
        And filter by "offset" = "0"
        And filter by "limit" = "1"
        And I submit the sorting and pagination request
      Then I should have a list of 1 "assessment" entities
        And I should have an entity with ID "<'Math Assessment' ID>"

     When I navigate to GET "/<ASSESSMENT URI>/<'Math Assessment' ID>"
      Then I should have a list of 1 "assessment" entities
        And "assessmentTitle" should be "Mathematics Achievement Assessment Test"
        And "assessmentCategory" should be "Advanced Placement"
        And "academicSubject" should be "Mathematics"
        And "gradeLevelAssessed" should be "Eighth grade"

     When I navigate to GET "/<SECTION URI>/<'8th Grade English - Sec 6' ID>/<STUDENT SECTION ASSOC URI>/<STUDENT URI>"
    Then I should have a list of 28 "student" entities
        And I should have an entity with ID "<'Preston Muchow' ID>"
        And I should have an entity with ID "<'Mayme Borc' ID>"
        And I should have an entity with ID "<'Malcolm Costillo' ID>"
        And I should have an entity with ID "<'Tomasa Cleaveland' ID>"
        And I should have an entity with ID "<'Merry Mccanse' ID>"
        And I should have an entity with ID "<'Samantha Scorzelli' ID>"
        And I should have an entity with ID "<'Matt Sollars' ID>"
        And I should have an entity with ID "<'Dominic Brisendine' ID>"
        And I should have an entity with ID "<'Lashawn Taite' ID>"
        And I should have an entity with ID "<'Oralia Merryweather' ID>"
        And I should have an entity with ID "<'Dominic Bavinon' ID>"
        And I should have an entity with ID "<'Rudy Bedoya' ID>"
        And I should have an entity with ID "<'Verda Herriman' ID>"
        And I should have an entity with ID "<'Alton Maultsby' ID>"
        And I should have an entity with ID "<'Felipe Cianciolo' ID>"
        And I should have an entity with ID "<'Lyn Consla' ID>"
        And I should have an entity with ID "<'Felipe Wierzbicki' ID>"
        And I should have an entity with ID "<'Gerardo Giaquinto' ID>"
        And I should have an entity with ID "<'Holloran Franz' ID>"
        And I should have an entity with ID "<'Oralia Simmer' ID>"
        And I should have an entity with ID "<'Lettie Hose' ID>"
        And I should have an entity with ID "<'Gerardo Saltazor' ID>"
        And I should have an entity with ID "<'Lashawn Aldama' ID>"
        And I should have an entity with ID "<'Alton Ausiello' ID>"
        And I should have an entity with ID "<'Marco Daughenbaugh' ID>"
        And I should have an entity with ID "<'Karrie Rudesill' ID>"
        And I should have an entity with ID "<'Damon Iskra' ID>"
        And I should have an entity with ID "<'Gerardo Rounsaville' ID>"

     When I navigate to URI "/<ASSESSMENT URI>/<'Math Assessment' ID>/<STUDENT ASSESSMENT ASSOC URI>" with filter sorting and pagination
        And filter by "sortBy" = "administrationDate"
        And filter by "sortOrder" = "descending"
        And filter by "offset" = "0"
        And filter by "limit" = "1"
        And I submit the sorting and pagination request
      Then I should have a list of 1 "studentAssessmentAssociation" entities
        And I should have an entity with ID "<'Most Recent Student Assessment Association' ID>"

     When I navigate to GET "/<STUDENT ASSESSMENT ASSOC URI>/<'Most Recent Student Assessment Association' ID>"
      Then I should have a list of 1 "studentAssessmentAssociation" entities
        And "administrationDate" should be "2011-09-15"
        And "administrationEndDate" should be "2011-12-15"
        And "retestIndicator" should be "Primary Administration"

Examples:
| Username        | Password            | AnyDefaultSLIRole  |
| "rrogers"       | "rrogers1234"       | "IT Administrator" |
| "sbantu"        | "sbantu1234"        | "Leader"           |

    Scenario Outline:  (paging/sorting) As a teacher, for my class, I want to get the most recent values of the following attributes: DIBELSCompositeScore, ReadingInstructionalLevel, PerformanceLevel
    Given I am a valid SEA/LEA end user <Username> with password <Password>
    And I have a Role attribute returned from the "SLI"
    And the role attribute equals <AnyDefaultSLIRole>
    And I am authenticated on "IL"

    Given format "application/json"
     When I navigate to GET "/<TEACHER URI>/<'Linda Kim' ID>"
      Then I should receive a link named "getTeacherSectionAssociations" with URI "/<TEACHER URI>/<'Linda Kim' ID>/<TEACHER SECTION ASSOC URI>"
        And I should receive a link named "getSections" with URI "/<TEACHER URI>/<'Linda Kim' ID>/<TEACHER SECTION ASSOC URI>/<SECTION URI>"
        And I should receive a link named "self" with URI "/<TEACHER URI>/<'Linda Kim' ID>"

    When I navigate to GET "/<TEACHER URI>/<'Linda Kim' ID>/<TEACHER SECTION ASSOC URI>/<SECTION URI>"
    Then I should have a list of 2 "section" entities
        And I should have an entity with ID "<'8th Grade English - Sec 6' ID>"

    When I navigate to GET "/<SECTION URI>/<'8th Grade English - Sec 6' ID>/<STUDENT SECTION ASSOC URI>/<STUDENT URI>"
    Then I should have a list of 28 "student" entities
        And I should have an entity with ID "<'Preston Muchow' ID>"
        And I should have an entity with ID "<'Mayme Borc' ID>"
        And I should have an entity with ID "<'Malcolm Costillo' ID>"
        And I should have an entity with ID "<'Tomasa Cleaveland' ID>"
        And I should have an entity with ID "<'Merry Mccanse' ID>"
        And I should have an entity with ID "<'Samantha Scorzelli' ID>"
        And I should have an entity with ID "<'Matt Sollars' ID>"
        And I should have an entity with ID "<'Dominic Brisendine' ID>"
        And I should have an entity with ID "<'Lashawn Taite' ID>"
        And I should have an entity with ID "<'Oralia Merryweather' ID>"
        And I should have an entity with ID "<'Dominic Bavinon' ID>"
        And I should have an entity with ID "<'Rudy Bedoya' ID>"
        And I should have an entity with ID "<'Verda Herriman' ID>"
        And I should have an entity with ID "<'Alton Maultsby' ID>"
        And I should have an entity with ID "<'Felipe Cianciolo' ID>"
        And I should have an entity with ID "<'Lyn Consla' ID>"
        And I should have an entity with ID "<'Felipe Wierzbicki' ID>"
        And I should have an entity with ID "<'Gerardo Giaquinto' ID>"
        And I should have an entity with ID "<'Holloran Franz' ID>"
        And I should have an entity with ID "<'Oralia Simmer' ID>"
        And I should have an entity with ID "<'Lettie Hose' ID>"
        And I should have an entity with ID "<'Gerardo Saltazor' ID>"
        And I should have an entity with ID "<'Lashawn Aldama' ID>"
        And I should have an entity with ID "<'Alton Ausiello' ID>"
        And I should have an entity with ID "<'Marco Daughenbaugh' ID>"
        And I should have an entity with ID "<'Karrie Rudesill' ID>"
        And I should have an entity with ID "<'Damon Iskra' ID>"
        And I should have an entity with ID "<'Gerardo Rounsaville' ID>"

     When I navigate to URI "/<STUDENT URI>/<'Matt Sollars' ID>/<STUDENT ASSESSMENT ASSOC URI>/<ASSESSMENT URI>" with filter sorting and pagination
        And filter by "assessmentTitle" = "DIBELS-MOY"
        And I submit the sorting and pagination request
      Then I should have a list of 1 "assessment" entities
        And I should have an entity with ID "<'Grade 2 MOY DIBELS' ID>"

     When I navigate to GET "/<ASSESSMENT URI>/<'Grade 2 MOY DIBELS' ID>"
      Then I should have a list of 1 "assessment" entities
        And "assessmentTitle" should be "DIBELS-MOY"
        And "assessmentCategory" should be "Benchmark test"
        And "academicSubject" should be "Reading"
        And "gradeLevelAssessed" should be "Second grade"
        And "lowestGradeLevelAssessed" should be "Second grade"
        And "assessmentFamilyHierarchyName" should be "DIBELS Next"
        And "maxRawScore" should be "380"
        And "minRawScore" should be "13"
        And the field "assessmentPeriodDescriptor.beginDate" should be "2012-01-01"
        And the field "assessmentPeriodDescriptor.endDate" should be "2012-02-01"
        And there are "3" "assessmentPerformanceLevel"
        And for the level at position "0"
        And the key "minimumScore" has value "190"
        And the key "maximumScore" has value "380"
        And the key "assessmentReportingMethod" has value "Composite Score"
        And the key "performanceLevelDescriptor.0.codeValue" has value "Level 1"
        And the key "performanceLevelDescriptor.1.description" has value "At or Above Benchmark"
        And for the level at position "1"
        And the key "minimumScore" has value "145"
        And the key "maximumScore" has value "189"
        And the key "assessmentReportingMethod" has value "Composite Score"
        And the key "performanceLevelDescriptor.0.codeValue" has value "Level 2"
        And the key "performanceLevelDescriptor.1.description" has value "Below Benchmark"
        And for the level at position "2"
        And the key "minimumScore" has value "13"
        And the key "maximumScore" has value "144"
        And the key "assessmentReportingMethod" has value "Composite Score"
        And the key "performanceLevelDescriptor.0.codeValue" has value "Level 3"
        And the key "performanceLevelDescriptor.1.description" has value "Well Below Benchmark"

     When I navigate to URI "/<ASSESSMENT URI>/<'Grade 2 MOY DIBELS' ID>/<STUDENT ASSESSMENT ASSOC URI>" with filter sorting and pagination
        And filter by "sortBy" = "administrationDate"
        And filter by "sortOrder" = "descending"
        And filter by "offset" = "0"
        And filter by "limit" = "1"
        And I submit the sorting and pagination request
      Then I should have a list of 1 "studentAssessmentAssociation" entities
        And I should have an entity with ID "<'Most Recent Student Assessment Association' ID>"

     When I navigate to GET "/<STUDENT ASSESSMENT ASSOC URI>/<'Most Recent Student Assessment Association' ID>"
      Then I should have a list of 1 "studentAssessmentAssociation" entities
        And "administrationDate" should be "2012-01-10"
        And "administrationEndDate" should be "2012-01-15"
        And "gradeLevelWhenAssessed" should be "Second grade"
        And "retestIndicator" should be "1st Retest"
        And the field "performanceLevelDescriptors.0.1.description" should be "Below Benchmark"
        And the field "scoreResults.0.assessmentReportingMethod" should be "Scale score"
        And the field "scoreResults.0.result" should be "120"

Examples:
| Username        | Password            | AnyDefaultSLIRole  |
| "rrogers"       | "rrogers1234"       | "IT Administrator" |
#| "sbantu"        | "sbantu1234"        | "Leader"           |

@wip
Scenario Outline:  As a AggregateViewer I should not see personally identifiable information data
    Given I am a valid SEA/LEA end user <Username> with password <Password>
    And I have a Role attribute returned from the "SLI"
    And the role attribute equals <AnyDefaultSLIRole>
    And I am authenticated on "IL"

    When I navigate to GET "/<TEACHER URI>/<'Ms. Smith' ID>"
    Then I should receive a return code of 403

    When I navigate to GET "/<TEACHER SECTION ASSOC URI>/<'Teacher Ms. Jones and Section Algebra II' ID>/<TEACHER URI>"
    Then I should receive a return code of 403

    When I navigate to GET "/<STUDENT SECTION ASSOC URI>/<'Algebra II' ID>/<STUDENT URI>"
    Then I should receive a return code of 403

    When I navigate to GET "/<STUDENT URI>/<'Matt Sollars' ID>"      
    Then I should receive a return code of 403

    When I navigate to GET "/<SECTION URI>/<'Algebra II' ID>"      
    Then I should receive a return code of 403

    When I navigate to GET "/<ASSESSMENT URI>/<'Grade 2 BOY DIBELS' ID>"
    Then I should receive a return code of 403

Examples:
| Username         | Password             | AnyDefaultSLIRole  |
| "msmith"         | "msmith1234"         | "AggregateViewer"  |
