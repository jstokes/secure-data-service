Feature: Negative Ingestion Testing

Background: I have a landing zone route configured
    Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a Zip File containing a control file with checksum error 
  Given I want to ingest locally provided data "ChecksumError.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName                          |
        | student                                 |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 30 seconds for ingestion to complete
  And a batch job for file "ChecksumError.zip" is completed in database
  Then I should see following map of entry counts in the corresponding collections:
        | collectionName                          | count     |
        | student                                 | 0         |

Then I should see "BASE_0006" in the resulting error log file
  And I should see "Processed 0 records." in the resulting batch job file
