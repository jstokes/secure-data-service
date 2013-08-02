Feature: Missing Index Alert

  Background: I have a landing zone route configured
    Given I am using local data store

  Scenario: Ingest, delete index, and ingest again
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the tenant database for "Midgar" does not exist
    And the tenantIsReady flag for the tenant "Midgar" is reset
    And I post "TinyDataSet.zip" file as the payload of the ingestion job
    And zip file is scp to ingestion landing zone with name "TinyDataSet1.zip"
    And a batch job for file "TinyDataSet1.zip" is completed in database
    And I should not see an error log file created
    Then I should see following map of indexes in the corresponding collections:
      | collectionName                             | index                       |
      | section                                    | body.schoolId               |
      | student                                    | body.studentUniqueStateId   |
      | teacherSchoolAssociation                   | body.schoolId               |
   Then I remove the following indexes in the corresponding collections:
      | collectionName                             | index                       |
      | section                                    | body.schoolId               |
      | student                                    | body.studentUniqueStateId   |
      | teacherSchoolAssociation                   | body.schoolId               |
    And I post "TinyDataSet.zip" file as the payload of the ingestion job
    And zip file is scp to ingestion landing zone with name "TinyDataSet2.zip"
    And a batch job for file "TinyDataSet2.zip" is completed in database
    And I should see "INFO  Not all records were processed completely due to errors." in the resulting batch job file
    And I should see "CORE_0038" in the resulting error log file
    And I should not see a warning log file created
    When the tenant indexes are applied to the tenant "Midgar"
    #And the old files are removed from the "Midgar-Daybreak" landing zone

   And the landing zone is reinitialized
    And I post "TinyDataSet.zip" file as the payload of the ingestion job
    And zip file is scp to ingestion landing zone with name "TinyDataSet3.zip"
    And a batch job for file "TinyDataSet3.zip" is completed in database
    And I should see "InterchangeEducationOrganization.xml educationOrganization 1 deltas!" in the resulting batch job file
    And I should see "All records processed successfully." in the resulting batch job file
    And I should see "Processed 0 records." in the resulting batch job file
    And I should not see an error log file created
    And I should not see a warning log file created



