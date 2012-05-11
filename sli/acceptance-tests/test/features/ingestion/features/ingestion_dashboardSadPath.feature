Feature: Dashboard Sad Path Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store

Scenario: Post a zip file containing all data for Illinois Daybreak as a payload of the ingestion job: Clean Database
Given I am using preconfigured Ingestion Landing Zone for "IL-Daybreak"
    And I post "DashboardSadPath_IL_Daybreak.zip" file as the payload of the ingestion job
    And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
        | studentSchoolAssociation    |
        | course                      |
        | educationOrganization       |
        | section                     |
        | studentSectionAssociation   |
        | staff                       |
        |staffEducationOrganizationAssociation|
        | teacherSchoolAssociation    |
        | teacherSectionAssociation   |
        | session                     |
        | assessment                  |
        | studentAssessmentAssociation|
        | gradebookEntry              |
        | studentTranscriptAssociation|
        | studentSectionGradebookEntry|
        | parent                      |
        | studentParentAssociation    |
        | attendance                  |
        | program                     |
        | staffProgramAssociation     |
        | studentProgramAssociation   |
        | cohort                      |
        | staffCohortAssociation      |
        | studentCohortAssociation    |
        | learningStandard            |
        | disciplineIncident          |
        | disciplineAction            |
		| studentDisciplineIncidentAssociation|
When zip file is scp to ingestion landing zone
  And a batch job log has been created
  And I should see "Processed 161 records." in the resulting batch job file

Scenario: Post a zip file containing all data for Illinois Sunset as a payload of the ingestion job: Append Database
Given I am using preconfigured Ingestion Landing Zone for "IL-Sunset"
  And I post "DashboardSadPath_IL_Sunset.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
  And a batch job log has been created