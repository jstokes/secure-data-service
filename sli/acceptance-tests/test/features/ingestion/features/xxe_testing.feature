@RALLY_US2191
Feature: Negative XXE Injection Testing

Background: I have a landing zone route configured
    Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file with an external entity that attempts to access a local file
  Given I post "XXETest.zip" file as the payload of the ingestion job
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 30 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "InterchangeEducationOrganization.xml records considered: 1" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records failed: 1" in the resulting batch job file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "Processed 1 records." in the resulting batch job file

  And I should see "There has been a data validation error when saving an entity" in the resulting error log file
  And I should see "     Entity     stateEducationAgency" in the resulting error log file
  And I should see "     Instance   0" in the resulting error log file
  And I should see "     Field      stateOrganizationId" in the resulting error log file

