@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

Scenario: Delete Program with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And I post "prep_cascading_deletion.zip" file as the payload of the ingestion job
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
       | recordHash                                |
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
       | studentObjectiveAssessment                |
       | studentParentAssociation                  |
       | studentProgramAssociation                 |
       | studentSchoolAssociation                  |
       | studentSectionAssociation                 |
       | studentGradebookEntry                     |
       | courseTranscript                          |
       | teacher                                   |
       | teacherSchoolAssociation                  |
       | teacherSectionAssociation                 |
       | yearlyTranscript                          |
    When zip file is scp to ingestion landing zone
    Then a batch job for file "prep_cascading_deletion.zip" is completed in database
    And a batch job log has been created
    And I should not see an error log file created
	And I should not see a warning log file created
    And I post "cascading_deletion_program.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "cascading_deletion_program.zip" is completed in database
    And a batch job log has been created
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	#And I should not see "983dd657325009aefa88a234fa18bdb1e11c82a8_id" in the "Midgar" database