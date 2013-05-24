@RALLY_US5096
Feature: Odin Data Set Ingestion Correctness and Fidelity
  Background: I have a landing zone route configured
    Given I am using odin data store
    And I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"

  Scenario: Post Odin Sample Data Set
    Given the landing zone for tenant "Midgar" edOrg "Daybreak" is reinitialized
    And the tenant database for "Midgar" does not exist
    And I post "OdinSampleDataSet.zip" file as the payload of the ingestion job
    And the following collections are empty in datastore:
      | collectionName                            |
      | assessment                                |
      | assessmentFamily                          |
      | assessmentPeriodDescriptor                |
      | attendance                                |
      | calendarDate                              |
      | cohort                                    |
      | competencyLevelDescriptor                 |
      | course                                    |
      | courseOffering                            |
      | courseSectionAssociation                  |
      | disciplineAction                          |
      | disciplineIncident                        |
      | educationOrganization                     |
      | educationOrganizationAssociation          |
      | educationOrganizationSchoolAssociation    |
      | grade                                     |
      | gradebookEntry                            |
      | gradingPeriod                             |
      | graduationPlan                            |
      | learningObjective                         |
      | learningStandard                          |
      | parent                                    |
      | program                                   |
      | reportCard                                |
      | school                                    |
      | schoolSessionAssociation                  |
      | section                                   |
      | sectionAssessmentAssociation              |
      | sectionSchoolAssociation                  |
      | session                                   |
      | sessionCourseAssociation                  |
      | staff                                     |
      | staffCohortAssociation                    |
      | staffEducationOrganizationAssociation     |
      | staffProgramAssociation                   |
      | student                                   |
      | studentAcademicRecord                     |
      | studentAssessment                         |
      | studentCohortAssociation                  |
      | studentCompetency                         |
      | studentCompetencyObjective                |
      | studentDisciplineIncidentAssociation      |
      | studentParentAssociation                  |
      | studentProgramAssociation                 |
      | studentSchoolAssociation                  |
      | studentSectionAssociation                 |
      | studentGradebookEntry                     |
      | courseTranscript                          |
      | teacher                                   |
      | teacherSchoolAssociation                  |
      | teacherSectionAssociation                 |
    When zip file is scp to ingestion landing zone
    And a batch job for file "OdinSampleDataSet.zip" is completed in database
    #Then  I should see "Processed 202109 records." in the resulting batch job file
    Then I should not see an error log file created
    And I should not see a warning log file created
