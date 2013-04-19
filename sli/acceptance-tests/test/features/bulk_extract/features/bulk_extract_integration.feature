Feature: A bulk extract is triggered, retrived through the api, and validated

Scenario: Trigger a bulk extract on ingested data and retrieve the extract through the api
   Given I trigger a bulk extract

   And I am a valid 'service' user with an authorized long-lived token "92FAD560-D2AF-4EC1-A2CC-F15B460E1E43"
   And in my list of rights I have BULK_EXTRACT
   When I make a call to the bulk extract end point "/bulk/extract/tenant"
   When the return code is 200 I get expected tar downloaded
   Then I check the http response headers
   When I decrypt and save the extracted file
   And I verify that an extract tar file was created for the tenant "Midgar"
   And there is a metadata file in the extract
   And the extract contains a file for each of the following entities:
   |  entityType                            |
   |  assessment                            |              
   |  attendance                            |
   |  cohort                                |
   |  competencyLevelDescriptor             |
   |  course                                |
   |  courseOffering                        |
   |  courseTranscript                      |
   |  disciplineIncident                    |
   |  disciplineAction                      |
   |  educationOrganization                 |
   |  grade                                 |
   |  gradebookEntry                        |
   |  gradingPeriod                         |
   |  graduationPlan                        |
   |  learningObjective                     |
   |  learningStandard                      |
   |  parent                                |
   |  program                               |
   |  reportCard                            |
   |  school                                |
   |  section                               |
   |  session                               |
   |  staff                                 |
   |  staffCohortAssociation                |
   |  staffEducationOrganizationAssociation |
   |  staffProgramAssociation               |
   |  student                               |
   |  studentAcademicRecord                 |
   |  studentAssessment                     |
   |  studentCohortAssociation              |
   |  studentCompetency                     |
   |  studentCompetencyObjective            |
   |  studentDisciplineIncidentAssociation  |
   |  studentProgramAssociation             |
   |  studentGradebookEntry                 |
   |  studentSchoolAssociation              |
   |  studentSectionAssociation             |
   |  studentParentAssociation              |
   |  teacher                               |
   |  teacherSchoolAssociation              |
   |  teacherSectionAssociation             |

    Scenario: Un-Authorized user cannot use the endpoint
        Given I am logged in using "linda.kim" "balrogs" to realm "IL"
        When I make a call to the bulk extract end point "/bulk/extract/tenant"
        Then I should receive a return code of 403   


Scenario: Validate the Last-Modified header is in a valid http date format
    #Retrieve file information
    When I retrieve the path to and decrypt the extract file for the tenant "Midgar" and application with id "19cca28d-7357-4044-8df9-caad4b1c8ee4"
    When I know the file-length of the extract file

    #Make a head call to retrieve last-modified information
    Given I am a valid 'service' user with an authorized long-lived token "92FAD560-D2AF-4EC1-A2CC-F15B460E1E43"
    And in my list of rights I have BULK_EXTRACT
    When I make bulk extract API head call
    Then I get back a response code of "200"
    Then I have all the information to make a custom bulk extract request

    #Make a bulk extract request with correct If-Unmodified-Since time based on last-modified
    When the If-Unmodified-Since header field is set to "BEFORE"
    And I make a custom bulk extract API call
    Then I get back a response code of "412"