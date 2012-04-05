@wip
Feature: ACT Assessment Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "actAssessmentMetadata.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | assessment                  |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count |
     | assessment                  | 1     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                                | searchValue                                      |
     | assessment                  | 1                   | body.assessmentFamilyHierarchyName             | ACT                                              |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-English                                      |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-English-Usage                                |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-English-Rhetorical                           |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Mathematics                                  |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Math-Pre-Algebra                             |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Math-Algebra                                 |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Math-Plane-Geometry                          |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Reading                                      |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Reading-SocialStudies                        |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Reading-Arts                                 |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Science                                      |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Writing                                      |

  And I should see "Processed 14 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "actAssessmentMetadata.xml records considered: 14" in the resulting batch job file
  And I should see "actAssessmentMetadata.xml records ingested successfully: 13" in the resulting batch job file
  And I should see "actAssessmentMetadata.xml records failed: 0" in the resulting batch job file
  
  