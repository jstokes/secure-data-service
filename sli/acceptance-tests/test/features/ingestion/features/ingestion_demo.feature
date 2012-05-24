@RALLY_US149
@RALLY_US0812
@RALLY_US0813
@RALLY_US0814
@RALLY_US0815
@RALLY_US0817
@RALLY_US0818
@RALLY_US0819
@RALLY_US1085
@RALLY_US1086
@RALLY_US1241
@RALLY_US1243
Feature: Demo Data Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
    And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "DemoData.zip" file as the payload of the ingestion job
    And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
        | studentSchoolAssociation    |
        | course                      |
        | educationOrganization       |
        | section                     |
        | studentSectionAssociation   |
        | staff                       |
        | teacherSchoolAssociation    |
        | teacherSectionAssociation   |
        | session                     |
        | assessment                  |
        | studentAssessmentAssociation|
        | studentTranscriptAssociation|
        | studentAcademicRecord       |
        | studentSectionGradebookEntry|
        | parent                      |
        | studentParentAssociation    |
        | graduationPlan              |
        | studentAcademicRecord       |
When zip file is scp to ingestion landing zone
#    And "30" seconds have elapsed
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | student                     | 94    |
        | studentSchoolAssociation    | 123   |
        | course                      | 15    |
        | educationOrganization       | 8     |
        | section                     | 25    |
        | studentSectionAssociation   | 210   |
        | staff                       | 4     |
        | teacherSchoolAssociation    | 6     |
        | teacherSectionAssociation   | 20    |
        | session                     | 8     |
        | assessment                  | 17    |
        | studentAssessmentAssociation| 104   |
        | parent                      | 9     |
        | studentParentAssociation    | 9     |
        | studentSectionGradebookEntry| 135   |
        | graduationPlan              | 3     |
        | studentAcademicRecord       | 86    |
        | studentTranscriptAssociation| 87    |
    And I check to find if record is in collection:
       | collectionName              | expectedRecordCount | searchParameter          | searchValue                | searchType           |
       | student                     | 1                   | metaData.externalId      | 530425896                  | string               |
       | student                     | 1                   | metaData.externalId      | 784204643                  | string               |
       | staff                       | 1                   | metaData.externalId      | cgray                      | string               |
       | course                      | 1                   | metaData.externalId      | 1st Grade Homeroom         | string               |
       | educationOrganization       | 1                   | metaData.externalId      | South Daybreak Elementary  | string               |
       | educationOrganization       | 1                   | metaData.externalId      | IL-DAYBREAK                | string               |
       | educationOrganization       | 1                   | metaData.externalId      | IL                         | string               |
    And I should see "Processed 971 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "InterchangeStudent.xml records considered: 94" in the resulting batch job file
    And I should see "InterchangeStudent.xml records ingested successfully: 94" in the resulting batch job file
    And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records considered: 23" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records ingested successfully: 23" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records considered: 8" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records ingested successfully: 8" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records considered: 25" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records ingested successfully: 25" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records considered: 30" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records ingested successfully: 30" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records considered: 338" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records ingested successfully: 338" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata.xml records considered: 17" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata.xml records ingested successfully: 17" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment.xml records considered: 104" in the resulting batch job file
    And I should see "InterchangeStudentAssessment.xml records ingested successfully: 104" in the resulting batch job file
    And I should see "InterchangeStudentAssessment.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records considered: 314" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records ingested successfully: 314" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records considered: 18" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records ingested successfully: 18" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records failed: 0" in the resulting batch job file

    
Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Populated Database
Given I post "DemoData.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
    And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | student                     | 94    |
        | studentSchoolAssociation    | 123   |
        | course                      | 15    |
        | educationOrganization       | 8     |
        | section                     | 25    |
        | studentSectionAssociation   | 210   |
        | staff                       | 4     |
        | teacherSchoolAssociation    | 6     |
        | teacherSectionAssociation   | 20    |
        | session                     | 8     |
        | assessment                  | 17    |
        | studentAssessmentAssociation| 104   |
        | studentTranscriptAssociation| 87    |
        | studentAcademicRecord       | 86    |
        | parent                      | 9     |
        | studentParentAssociation    | 9     |
        | studentSectionGradebookEntry| 135   |
        | graduationPlan              | 3     |
    And I check to find if record is in collection:
       | collectionName              | expectedRecordCount | searchParameter          | searchValue                | searchType           |
       | student                     | 1                   | metaData.externalId      | 530425896                  | string               |
       | student                     | 1                   | metaData.externalId      | 784204643                  | string               |
       | staff                       | 1                   | metaData.externalId      | cgray                      | string               |
       | course                      | 1                   | metaData.externalId      | 1st Grade Homeroom         | string               |
       | educationOrganization       | 1                   | metaData.externalId      | South Daybreak Elementary  | string               |
       | educationOrganization       | 1                   | metaData.externalId      | IL-DAYBREAK                | string               |
       | educationOrganization       | 1                   | metaData.externalId      | IL                         | string               |
    And I should see "Processed 971 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "InterchangeStudent.xml records considered: 94" in the resulting batch job file
    And I should see "InterchangeStudent.xml records ingested successfully: 94" in the resulting batch job file
    And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records considered: 23" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records ingested successfully: 23" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records considered: 8" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records ingested successfully: 8" in the resulting batch job file
    And I should see "InterchangeEducationOrgCalendar.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records considered: 25" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records ingested successfully: 25" in the resulting batch job file
    And I should see "InterchangeMasterSchedule.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records considered: 30" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records ingested successfully: 30" in the resulting batch job file
    And I should see "InterchangeStaffAssociation.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records considered: 338" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records ingested successfully: 338" in the resulting batch job file
    And I should see "InterchangeStudentEnrollment.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata.xml records considered: 17" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata.xml records ingested successfully: 17" in the resulting batch job file
    And I should see "InterchangeAssessmentMetadata.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentAssessment.xml records considered: 104" in the resulting batch job file
    And I should see "InterchangeStudentAssessment.xml records ingested successfully: 104" in the resulting batch job file
    And I should see "InterchangeStudentAssessment.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records considered: 314" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records ingested successfully: 314" in the resulting batch job file
    And I should see "InterchangeStudentGrade.xml records failed: 0" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records considered: 18" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records ingested successfully: 18" in the resulting batch job file
    And I should see "InterchangeStudentParent.xml records failed: 0" in the resulting batch job file
