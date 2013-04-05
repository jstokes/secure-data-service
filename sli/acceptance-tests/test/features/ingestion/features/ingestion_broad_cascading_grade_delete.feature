@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

@wip
Scenario: Delete Grade with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "yearlyTranscript" records like below in "Midgar" tenant. And I save this query as "grade"
        |field                                     |value                                                                                 |
        |grade._id                                 |861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id|
    Then there exist "1" "yearlyTranscript" records like below in "Midgar" tenant. And I save this query as "reportCard"
        |field                                     |value                                                                                 |
        |reportCard.body.grades                    |861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id|
    And I save the collection counts in "Midgar" tenant
    And I post "BroadGradeDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "BroadGradeDelete.zip" is completed in database
	And I should see "records considered for processing: 1" in the resulting batch job file
	And I should see "records ingested successfully: 0" in the resulting batch job file
	And I should see "records deleted successfully: 1" in the resulting batch job file
	And I should see "records failed processing: 0" in the resulting batch job file
	And I should see "records not considered for processing: 0" in the resulting batch job file
	And I should see "All records processed successfully." in the resulting batch job file
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
    And I re-execute saved query "grade" to get "0" records
    And I re-execute saved query "reportCard" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection |delta|
        |grade       |   -1|
        #|recordHash  |    -1|
    And I should not see "861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id" in the "Midgar" database

Scenario: Delete Grade without cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "yearlyTranscript" records like below in "Midgar" tenant. And I save this query as "grade"
        |field                                     |value                                                                                 |
        |grade._id                                 |861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id|
    Then there exist "1" "yearlyTranscript" records like below in "Midgar" tenant. And I save this query as "reportCard"
        |field                                     |value                                                                                 |
        |reportCard.body.grades                    |861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id|
    And I save the collection counts in "Midgar" tenant
    And I post "BroadGradeDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "BroadGradeDelete.zip" is completed in database

    And I should see "records considered for processing: 1" in the resulting batch job file
    And I should see "records ingested successfully: 0" in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "records not considered for processing: 0" in the resulting batch job file
    And I should see "All records processed successfully." in the resulting batch job file
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
    And I should not see a warning log file created
    And I re-execute saved query "grade" to get "0" records
    And I re-execute saved query "reportCard" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection |delta|
        |grade       |   -1|
        #|recordHash  |    -1|
    And I should not see "861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id" in the "Midgar" database


