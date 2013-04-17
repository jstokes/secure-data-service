@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

@wip
Scenario: Delete Student Discipline Incident Association with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	And I should see child entities of entityType "studentDisciplineIncidentAssociation" with id "908404e876dd56458385667fa383509035cd4312_ide92b633193053439c0e72a9254a35fb3a145b6d9_id" in the "Midgar" database
    And I post "BroadStudentDisciplineIncidentAssociationDelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadStudentDisciplineIncidentAssociationDelete.zip" is completed in database
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I should not see "908404e876dd56458385667fa383509035cd4312_ide92b633193053439c0e72a9254a35fb3a145b6d9_id" in the "Midgar" database	
	And I should not see any entity mandatorily referring to "908404e876dd56458385667fa383509035cd4312_ide92b633193053439c0e72a9254a35fb3a145b6d9_id" in the "Midgar" database
	And I should see entities optionally referring to "908404e876dd56458385667fa383509035cd4312_ide92b633193053439c0e72a9254a35fb3a145b6d9_id" be updated in the "Midgar" database

  Scenario: Delete StudentDisciplineIncidentAssociation Reference with cascade = false, force = true
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "student" records like below in "Midgar" tenant. And I save this query as "studentDisciplineInciden"
      |field                                                    |value                                                                                 |
      |studentDisciplineIncidentAssociation._id                 |908404e876dd56458385667fa383509035cd4312_ide92b633193053439c0e72a9254a35fb3a145b6d9_id|
    And I save the collection counts in "Midgar" tenant
    And I post "ForceStudentDisciplineIncidentAssociationDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "ForceStudentDisciplineIncidentAssociationDelete.zip" is completed in database
    And a batch job log has been created
    And I should see "Processed 1 records." in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "All records processed successfully" in the resulting batch job file
    And I should not see an error log file created
    And I should not see a warning log file created
    And I re-execute saved query "studentDisciplineInciden" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
      | collection                                |     delta|
      | studentDisciplineIncidentAssociation      |        -1|
#      | recordHash                                |        -1|

  Scenario: Delete StudentDisciplineIncidentAssociation Reference with cascade = false, force = true
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "student" records like below in "Midgar" tenant. And I save this query as "studentDisciplineInciden"
      |field                                                    |value                                                                                 |
      |studentDisciplineIncidentAssociation._id                 |908404e876dd56458385667fa383509035cd4312_ide92b633193053439c0e72a9254a35fb3a145b6d9_id|
    And I save the collection counts in "Midgar" tenant
    And I post "ForceStudentDisciplineIncidentAssociationRefDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "ForceStudentDisciplineIncidentAssociationRefDelete.zip" is completed in database
    And a batch job log has been created
    And I should see "Processed 1 records." in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "All records processed successfully" in the resulting batch job file
    And I should not see an error log file created
    And I should not see a warning log file created
    And I re-execute saved query "studentDisciplineInciden" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
      | collection                                |     delta|
      | studentDisciplineIncidentAssociation      |        -1|
#      | recordHash                                |        -1|