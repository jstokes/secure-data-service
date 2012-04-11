Feature: Batchjob Datamodel Data Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
    And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing configured interchanges as a payload of the ingestion job: Clean Database
Given I post "BatchJob.zip" file as the payload of the ingestion job
    And the following collections are empty in batch job datastore:
        | collectionName              |
        | newBatchJob                 |

When zip file is scp to ingestion landing zone
  And a batch job log has been created

Then I should see following map of entry counts in the corresponding batch job db collections:
        | collectionName              | count |
        | newBatchJob                 | 1     |

 And I check to find if record is in batch job collection:
  | collectionName | expectedRecordCount | searchParameter                | searchValue             |searchType     |
  | newBatchJob    | 1                   | totalFiles                     | 1                       |integer        |
  # newBatchJob            | 1                                               | sourceId                                                            | nyc1_secure_landingzone          |string                          |
  # stages
  | newBatchJob    | 1                   | stages.0.stageName             | ZipFileProcessor        |string         |
  | newBatchJob    | 1                   | stages.0.status                | finished                |string         |
  | newBatchJob    | 1                   | stages.1.stageName             | ControlFilePreProcessor |string         |
  | newBatchJob    | 1                   | stages.1.status                | finished                |string         |
  #resources
  | newBatchJob    | 1                   | resourceEntries.0.resourceName | BatchJob.zip            |string         |
  | newBatchJob    | 1                   | resourceEntries.0.recordCount  | 0                       |integer        |
  | newBatchJob    | 1                   | resourceEntries.0.errorCount   | 0                       |integer        |

   And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "InterchangeEducationOrganization.xml records considered: 1" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records ingested successfully: 1" in the resulting batch job file
    And I should see "InterchangeEducationOrganization.xml records failed: 0" in the resulting batch job file
