@RALLY_US4305
@RALLY_US4306

Feature: As a student or staff I want to use apps that access the inBloom API

  Background: None

  Scenario: As a student, for my section, I want to get the most recent Math assessment

  # Log in via simple-idp and authenticate student credentials
    Given I log in to realm "Illinois Daybreak School District 4529" using simple-idp as student "msollars" with password "msollars1234"
    And format "application/json"
    When I navigate to GET "/v1/home"
    Then I should validate all the HATEOS links
     #And I should GET a return code of "200" for all the "student" links
    #And I should extract the "student" id from the "self" URI
  @wip
  Scenario: I check the contest of specific api endpoints
    When I navigate to GET "/students/<my student id>"
    Then the response body "id" should match my "student" "id"
    And the response field "entityType" should be "teacher"
    And the response field "name.lastSurname" should be "Gray"
    And I should receive a link named "self" with URI "/teachers/<teacher id>"
    And I should get and store the link named "getTeacherSectionAssociations"
    And I should get and store the link named "getSections"
    And I should get and store the link named "getTeacherSchoolAssociations"
    And I should get and store the link named "getSchools"
    And I should get and store the link named "getStaffEducationOrgAssignmentAssociations"
    And I should get and store the link named "getEducationOrganizations"

    When I follow the HATEOS link named "<getTeacherSectionAssociations>"
    Then I should extract the "sectionId" from the response body to a list

    When I navigate to GET "/sections/<teacher section>"
    Then I should have a list of 12 "section" entities

    When I make a GET request to URI "/sections/@id/studentSectionAssociations/students/studentAssessments"
    Then I should have a list of 42 "studentAssessment" entities
    And I should extract the "id" from the response body to a list and save to "studentAssessments"

    When I navigate to GET "/v1/studentAssessments"
    Then I should have a list of 50 "studentAssessment" entities
    And I store the studentAssessments

    When I navigate to GET "/studentAssessments/<student assessment>"
    Then I should extract the "studentAssessment" id from the "self" URI
    And the response field "entityType" should be "studentAssessment"
    And the response field "administrationLanguage.language" should be "English"
    And the response field "administrationEnvironment" should be "Classroom"
    And the response field "retestIndicator" should be "Primary Administration"
    And the response field "<SOA.scoreResults.result>" should be "77"
    And the response field "<SOA.OA.identificationCode>" should be "2013-Eleventh grade Assessment 2.OA-0"
    And I sort the studentAssessmentItems
    And the response field "<SAI.AI.identificationCode>" should be "2013-Eleventh grade Assessment 2#1"
    And I should extract the student reference from studentAssessment
    And I should extract the assessment reference from studentAssessment

  # /assessments/{id}
  # assessment: d12f6eb0f1a2bc260a738db6c61ea5515badc1cb_id
    When I navigate to GET "/assessments/<assessment>"
    Then I should extract the "assessment" id from the "self" URI
    And the response field "<AIC.identificationSystem>" should be "State"
    And the response field "<AIC.ID>" should be "<assessment 1>"
    And the response field "<OA.nomenclature>" should be "Nomenclature"
    And the response field "<OA.identificationCode>" should be "<objective assessment>"
    And the response field "<OA.percentOfAssessment>" should be the number "50"
    And the response field "<OA.APL.PLD.codeValue>" should be "<code value>"
    And the response field "<OA.APL.assessmentReportingMethod>" should be "<reporting method>"
    And the response field "<OA.AP.minimumScore>" should be the number "0"
    And the response field "<OA.AP.maximumScore>" should be the number "50"
  # Sub-Objective Assessments
    And the response field "<OA.OAS.nomenclature>" should be "Nomenclature"
    And the response field "<OA.OAS.identificationCode>" should be "<sub objective assessment>"
    And the response field "<OA.OAS.percentOfAssessment>" should be the number "50"
    And the response field "<OA.OAS.APL.PLD.codeValue>" should be "<code value>"
    And the response field "<OA.OAS.APL.assessmentReportingMethod>" should be "<reporting method>"
    And the response field "<OA.OAS.APL.minimumScore>" should be the number "0"
    And the response field "<OA.OAS.APL.maximumScore>" should be the number "50"
    And I should extract the learningObjectives from "<OA.learningObjectives>"
    And I should extract the learningObjectives from "<OA.OAS.learningObjectives>"
    And I make sure "<OA.learningObjectives>" match "<OA.OAS.learningObjectives>"
    And the response field "<OA.maxRawScore>" should be the number "50"
    And the response field "<OA.OAS.AI.identificationCode>" should be "<assessment item 1>"
    And the response field "<OA.OAS.AI.correctResponse>" should be "<correct response>"
    And the response field "<OA.OAS.AI.itemCategory>" should be "<item category>"
    And the response field "<OA.OAS.AI.maxRawScore>" should be the number "10"
  # Assessment Family Hierarchy
    And the response field "assessmentFamilyHierarchyName" should be "<assessment family hierarchy>"
  # Assessment Period Descriptor
    And the response field "assessmentPeriodDescriptor.description" should be "<assessment period descriptor>"
    And the response field "assessmentPeriodDescriptor.codeValue" should be "<APD.codeValue>"
    And the response field "entityType" should be "assessment"
    And the response field "gradeLevelAssessed" should be "Eleventh grade"
    And the response field "assessmentTitle" should be "<assessment 1>"
    And I extract all the "assessment" links

    When I follow the links for assessment
    Then I should validate the "objectiveAssessment.0.learningObjectives" from "assessment" links map to learningObjectives

    When I navigate to GET "/v1/search/assessments?q=Sixth"
    Then I should have a list of 4 "assessment" entities
    When I navigate to GET "/v1/search/assessments?assessmentTitle=2013-Sixth%20grade%20Assessment%202"
    Then I should have a list of 1 "assessment" entities
    And the offset response field "assessmentTitle" should be "2013-Sixth grade Assessment 2"
    And the offset response field "gradeLevelAssessed" should be "Sixth grade"
    And the offset response field "<AIC.ID>" should be "2013-Sixth grade Assessment 2"
    And the offset response field "<AIC.identificationSystem>" should be "State"
    And the offset response field "assessmentPeriodDescriptor.description" should be "Beginning of Year 2013-2014 for Sixth grade"
    And the offset response field "assessmentPeriodDescriptor.codeValue" should be "BOY-6-2013"
    And the offset response field "assessmentFamilyHierarchyName" should be "2013 Standard.2013 Sixth grade Standard"

    When I navigate to GET "/v1/search/assessments?q=sub"
    Then I should have a list of 50 "assessment" entities

    When I navigate to GET "/v1/search/assessments?q=2014-ninth%20grade%20assessment%201&limit=100"
    Then I should have a list of 52 "assessment" entities

    When I navigate to GET "/v1/search/assessments?assessmentTitle=2013-Sixth%20grade%20Assessment%201"
    Then I should have a list of 1 "assessment" entities
    And the offset response field "assessmentTitle" should be "2013-Sixth grade Assessment 1"
    And the offset response field "<search.assessment.ID>" should be "2013-Sixth grade Assessment 1"
    And the offset response field "<search.assessment.ID.system>" should be "State"
    And the offset response field "assessmentPeriodDescriptor.description" should be "Beginning of Year 2013-2014 for Sixth grade"
    And the offset response field "assessmentPeriodDescriptor.codeValue" should be "BOY-6-2013"
  #And the response field "<search.assessment.ID.system>" should be "State"
  # assessmentPeriodDescriptorId = ac743445484ab8745f3921fea80bad59bf484593_id
  #And the response field "<search.APD.id>" should be valid
  # assessmentFamilyReference = 3391fabed45ea970b84a47ae545ab165b4370cc4_id
  # And the offset response field "assessmentFamilyHierarchyName" should be "2014 Standard.2014 Ninth grade Standard"
  # assessmentFamilyHierarchyName = 2014 Standard.2014 Ninth grade Standard

    When I navigate to GET "/v1/assessments?assessmentPeriodDescriptor.description=Beginning%20of%20Year%202013-2014%20for%20Sixth%20grade"
    Then I should have a list of 2 "assessment" entities
    And the offset response field "assessmentTitle" should be "2013-Sixth grade Assessment 2"
    And the offset response field "gradeLevelAssessed" should be "Sixth grade"
    And the offset response field "assessmentFamilyHierarchyName" should be "2013 Standard.2013 Sixth grade Standard"
    And the offset response field "assessmentPeriodDescriptor.description" should be "Beginning of Year 2013-2014 for Sixth grade"
    And the offset response field "assessmentPeriodDescriptor.codeValue" should be "BOY-6-2013"

    When I navigate to GET "/v1/assessments?assessmentFamilyHierarchyName=2013%20Standard.2013%20Sixth%20grade%20Standard"
    Then I should have a list of 2 "assessment" entities
    And the offset response field "assessmentTitle" should be "2013-Sixth grade Assessment 2"
    And the offset response field "gradeLevelAssessed" should be "Sixth grade"
    And the offset response field "assessmentFamilyHierarchyName" should be "2013 Standard.2013 Sixth grade Standard"
    And the offset response field "<search.assessment.ID>" should be "2013-Sixth grade Assessment 2"
    And the offset response field "assessmentPeriodDescriptor.description" should be "Beginning of Year 2013-2014 for Sixth grade"
    And the offset response field "assessmentPeriodDescriptor.codeValue" should be "BOY-6-2013"
    And the offset response field "<OA.identificationCode>" should be "2013-Sixth grade Assessment 2.OA-0"
    And the offset response field "<OA.OAS.AI.identificationCode>" should be "2013-Sixth grade Assessment 2#1"

  @student
  Scenario Outline: Student has access to stuff
    Given I log in to realm "Illinois Daybreak School District 4529" using simple-idp as student "msollars" with password "msollars1234"
    And format "application/json"
    When I navigate to GET "/v1/<Entity>/<Ids>"
    Then I should receive a return code of 200
  Examples:
    | Entity                    | Ids                                                                                                                                                                           |
    | students                  | 306404b837fd630ea53bc11391dd57985db5e48c_id                                                                                                                                   |
    | parents                   | a4ae754c75f6a59b13f5ecb7464c29bc0370fa56_id,4122df03e56fd8296006c55b1fb5a3a16d484726_id                                                                                       |
    | studentParentAssociations | 306404b837fd630ea53bc11391dd57985db5e48c_id1f8f744fbc0329f653c4b32a3e60d86cfb7f0264_id,306404b837fd630ea53bc11391dd57985db5e48c_id06820ec0ebba76b11bd39f3460dc862199399b1e_id |
