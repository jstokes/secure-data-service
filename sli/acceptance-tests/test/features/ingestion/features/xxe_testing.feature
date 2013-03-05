@RALLY_US2191
Feature: Negative XXE Injection Testing

Background: I have a landing zone route configured
    Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file with an external entity that attempts to access a local file
  Given I post "XXETest.zip" file as the payload of the ingestion job
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 30 seconds for ingestion to complete
  And a batch job for file "XXETest.zip" is completed in database
  And a batch job log has been created
  And I should see "InterchangeEducationOrganization.xml records considered: 0" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "InterchangeEducationOrganization.xml records failed xsd validation: 1" in the resulting batch job file
  And I should see "All records processed successfully" in the resulting batch job file
  And I should see "Processed 0 records." in the resulting batch job file
  #And I should see "BASE_0017" in the resulting error log file for "InterchangeEducationOrganization.xml"
  And I should not see a warning log file created

