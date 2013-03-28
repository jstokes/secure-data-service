@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

Scenario: Delete Student Program Association with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And I post "BroadSetOfTypes.zip" file as the payload of the ingestion job
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
    Then a batch job for file "BroadSetOfTypes.zip" is completed in database
    And a batch job log has been created
    And I should not see an error log file created
	And I should not see a warning log file created
	And I should see child entities of entityType "studentProgramAssociation" with id "50dc8b12ef9184d88d8c304e635cf5a80d38bf79_id8f495c4527ad19c78a501a5646a2402d57cb6795_id" in the "Midgar" database
    And I post "BroadStudentProgramAssociationDelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadStudentProgramAssociationDelete.zip" is completed in database
    And a batch job log has been created
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	#And I should not see "50dc8b12ef9184d88d8c304e635cf5a80d38bf79_id8f495c4527ad19c78a501a5646a2402d57cb6795_id" in the "Midgar" database
	And I should not see any entity mandatorily referring to "908404e876dd56458385667fa383509035cd4312_id6ac27714bca705efbd6fd0eb6c0fd2c7317062e6_id" in the "Midgar" database
	And I should see entities optionally referring to "908404e876dd56458385667fa383509035cd4312_id6ac27714bca705efbd6fd0eb6c0fd2c7317062e6_id" be updated in the "Midgar" database	
	
	