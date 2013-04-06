@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

@wip
Scenario: Delete Student School Association with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "studentSchoolAssociation" records like below in "Midgar" tenant. And I save this query as "studentSchoolAssociation"
        |field                  |value                                      |
        |_id                    |c5c25df986e6e8fcf3fd9fde80cecbbeb662de42_id|
    Then there exist "1" "student" records like below in "Midgar" tenant. And I save this query as "student"
        |field                  |value                                      |
        |_id                    |908404e876dd56458385667fa383509035cd4312_id|
        |schools._id            |a13489364c2eb015c219172d561c62350f0453f3_id|
    And I save the collection counts in "Midgar" tenant
    And I post "BroadStudentSchoolAssociationDelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadStudentSchoolAssociationDelete.zip" is completed in database
	And I should see "records considered for processing: 1" in the resulting batch job file
    And I should see "records ingested successfully: 0" in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "records not considered for processing: 0" in the resulting batch job file
    And I should see "All records processed successfully." in the resulting batch job file
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I re-execute saved query "studentSchoolAssociation" to get "0" records
	And I re-execute saved query "student" to get "0" records
	And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                |delta|
        |studentSchoolAssociation   |   -1|
        #|recordHash                 |   -1|
	And I should not see "c5c25df986e6e8fcf3fd9fde80cecbbeb662de42_id" in the "Midgar" database

Scenario: Delete Orphan Student School Association with cascade = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "studentSchoolAssociation" records like below in "Midgar" tenant. And I save this query as "studentSchoolAssociation"
        |field                  |value                                      |
        |_id                    |c5c25df986e6e8fcf3fd9fde80cecbbeb662de42_id|
    Then there exist "1" "student" records like below in "Midgar" tenant. And I save this query as "student"
        |field                  |value                                      |
        |_id                    |908404e876dd56458385667fa383509035cd4312_id|
        |schools._id            |a13489364c2eb015c219172d561c62350f0453f3_id|
    And I save the collection counts in "Midgar" tenant
    And I post "OrphanStudentSchoolAssociationDelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "OrphanStudentSchoolAssociationDelete.zip" is completed in database
	And I should see "records considered for processing: 1" in the resulting batch job file
    And I should see "records ingested successfully: 0" in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "records not considered for processing: 0" in the resulting batch job file
    And I should see "All records processed successfully." in the resulting batch job file
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I re-execute saved query "studentSchoolAssociation" to get "0" records
	And I re-execute saved query "student" to get "0" records
	And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                |delta|
        |studentSchoolAssociation   |   -1|
        #|recordHash                 |   -1|
	And I should not see "c5c25df986e6e8fcf3fd9fde80cecbbeb662de42_id" in the "Midgar" database
