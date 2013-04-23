@RALLY_US2194
Feature: Blacklist Validation Ingestion Testing

Background: I have a landing zone route configured
    Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone


Scenario: Post a zip file where the second record has a bad attribute should fail and process previous records
  Given I post "BlacklistInputs.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
        | educationOrganization       |
        | course                      |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 30 seconds for ingestion to complete
  And a batch job for file "BlacklistInputs.zip" is completed in database
  And I should see "CORE_0006" in the resulting error log file
  And I should not see a warning log file created
  And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter               | searchValue                     |
     | course                      | 1                   | body.courseTitle              | CourseWithRelaxedBlacklist      |
  
