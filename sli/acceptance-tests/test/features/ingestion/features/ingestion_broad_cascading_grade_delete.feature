@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

Scenario: Delete Grade with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	#And I should see child entities of entityType "grade" with id "861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id" in the "Midgar" database
    Then there exist "1" "yearlyTranscript" records like below in "Midgar" tenant. And I save this query as "grade"
        |field                                     |value                                                                                 |
        |grade._id                                 |861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id|
    Then there exist "1" "yearlyTranscript" records like below in "Midgar" tenant. And I save this query as "reportCard"
        |field                                     |value                                                                                 |
        |reportCard.body.grades                    |861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id|
    And I post "BroadGradeDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "BroadGradeDelete.zip" is completed in database
    And a batch job log has been created
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
    #And I should not see "861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id" in the "Midgar" database
	#And I should not see any entity mandatorily referring to "861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id" in the "Midgar" database
	#And I should see entities optionally referring to "861efe5627b2c10ac01441b9afd26903398585bc_id091159fdc8200451fc5482189b29f7b9749ba775_id" be updated in the "Midgar" database
    And I re-execute saved query "grade" to get "0" records
    And I re-execute saved query "reportCard" to get "0" records
