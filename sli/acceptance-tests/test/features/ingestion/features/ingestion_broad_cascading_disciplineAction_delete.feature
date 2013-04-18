@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

@wip
Scenario: Delete Discipline Action with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    And I should see child entities of entityType "disciplineAction" with id "26f0088394f533ab3bf8d2fe0b830144f0774a61_id" in the "Midgar" database	
    And I post "BroadDisciplineActionDelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadDisciplineActionDelete.zip" is completed in database
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I should not see "26f0088394f533ab3bf8d2fe0b830144f0774a61_id" in the "Midgar" database
	And I should not see any entity mandatorily referring to "26f0088394f533ab3bf8d2fe0b830144f0774a61_id" in the "Midgar" database
	And I should see entities optionally referring to "26f0088394f533ab3bf8d2fe0b830144f0774a61_id" be updated in the "Midgar" database

  Scenario: Delete DisciplineAction with cascade = false, force = true
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "disciplineAction" records like below in "Midgar" tenant. And I save this query as "disciplineAction"
      |field                                                                |value                                      |
      |_id                                                                  |26f0088394f533ab3bf8d2fe0b830144f0774a61_id|
    And I save the collection counts in "Midgar" tenant
    And I post "ForceDisciplineActionDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "ForceDisciplineActionDelete.zip" is completed in database
    And a batch job log has been created
    And I should see "Processed 1 records." in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "All records processed successfully" in the resulting batch job file
    And I should not see an error log file created
    And I should not see a warning log file created
    And I re-execute saved query "disciplineAction" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
      | collection                                |     delta|
      | disciplineAction                          |        -1|
      | recordHash                                |        -1|

  Scenario: Delete DisciplineAction Reference with cascade = false, force = true
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "disciplineAction" records like below in "Midgar" tenant. And I save this query as "disciplineAction"
      |field                                                                |value                                      |
      |_id                                                                  |26f0088394f533ab3bf8d2fe0b830144f0774a61_id|
    And I save the collection counts in "Midgar" tenant
    And I post "ForceDisciplineActionRefDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "ForceDisciplineActionRefDelete.zip" is completed in database
    And a batch job log has been created
    And I should see "Processed 1 records." in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "All records processed successfully" in the resulting batch job file
    And I should not see an error log file created
    And I should not see a warning log file created
    And I re-execute saved query "disciplineAction" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
      | collection                                |     delta|
      | disciplineAction                          |        -1|
      | recordHash                                |        -1|