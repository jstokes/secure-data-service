@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

# Cascaded deletes are disabled for now.
@wip
Scenario: Delete AttendanceEvent with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    And a query on attendance of for studentId "908404e876dd56458385667fa383509035cd4312_id", schoolYear "2001-2002" and date "2001-09-13" on the "Midgar" tenant has a count of "1"
	Then there exist "1" "attendance" records like below in "Midgar" tenant. And I save this query as "attendance"
	|field                                                           |value                                       |
	|_id                                                             |94de66549a6b58f96463ff0d59b34817aa1fead6_id |


    And I check the number of elements in array of collection:
    | collectionName   |field  |value                                       |expectedRecordCount|searchContainer       |
    | attendance       |_id    |94de66549a6b58f96463ff0d59b34817aa1fead6_id | 22                | body.attendanceEvent |

	And I save the collection counts in "Midgar" tenant
    And I post "BroadAttendanceEventDelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadAttendanceEventDelete.zip" is completed in database
	And I should see "records considered for processing: 1" in the resulting batch job file
	And I should see "records ingested successfully: 0" in the resulting batch job file
	And I should see "records deleted successfully: 1" in the resulting batch job file
	And I should see "records failed processing: 0" in the resulting batch job file
	And I should see "records not considered for processing: 0" in the resulting batch job file
	And I should see "All records processed successfully." in the resulting batch job file
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I re-execute saved query "attendance" to get "0" records
	And I see that collections counts have changed as follows in tenant "Midgar"
	|collection                        |delta     |
	|attendance                        |         0|
	|recordHash                        |         0|
	And a query on attendance of for studentId "908404e876dd56458385667fa383509035cd4312_id", schoolYear "2001-2002" and date "2001-09-13" on the "Midgar" tenant has a count of "0"
    And I check the number of elements in array of collection:
    | collectionName   |field  |value                                       |expectedRecordCount|searchContainer       |
    | attendance       |_id    |94de66549a6b58f96463ff0d59b34817aa1fead6_id | 21                | body.attendanceEvent |

Scenario: Delete AttendanceEvent with cascade = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "attendance" records like below in "Midgar" tenant. And I save this query as "attendance"
        |field                                     |value                                                                                 |
        |_id                                       |94de66549a6b58f96463ff0d59b34817aa1fead6_id                                           |
        |body.attendanceEvent.date                 |2001-09-13                                                                            |

    And I check the number of elements in array of collection:
    | collectionName   |field  |value                                       |expectedRecordCount|searchContainer       |
    | attendance       |_id    |94de66549a6b58f96463ff0d59b34817aa1fead6_id | 22                | body.attendanceEvent |

    And I save the collection counts in "Midgar" tenant
    And I post "SafeAttendanceEventDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "SafeAttendanceEventDelete.zip" is completed in database
    And a batch job log has been created
    And I should see "Processed 1 records." in the resulting batch job file
	  And I should see "records deleted successfully: 1" in the resulting batch job file
	  And I should see "records failed processing: 0" in the resulting batch job file
    And I should not see an error log file created
   	And I should not see a warning log file created
    And I re-execute saved query "attendance" to get "0" records 
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|
        | attendance                                |         0|
        | attendanceEvent                           |        -1|
        | recordHash                                |        -1|     

    And I check the number of elements in array of collection:
    | collectionName   |field  |value                                       |expectedRecordCount|searchContainer       |
    | attendance       |_id    |94de66549a6b58f96463ff0d59b34817aa1fead6_id | 21                | body.attendanceEvent |     
    
Scenario: Delete Orphan AttendanceEvent with cascade = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "attendance" records like below in "Midgar" tenant. And I save this query as "attendance"
        |field                                     |value                                                                                 |
        |_id                                       |71ca8f7bf0738fdd72ff09858365ef87b4bbb178_id                                           |
        |body.attendanceEvent.date                 |2001-12-25                                                                            |

    And I check the number of elements in array of collection:
    | collectionName   |field  |value                                       |expectedRecordCount|searchContainer      |
    | attendance       |_id    |71ca8f7bf0738fdd72ff09858365ef87b4bbb178_id | 1                | body.attendanceEvent |

    And I save the collection counts in "Midgar" tenant
    And I post "OrphanAttendanceEventDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "OrphanAttendanceEventDelete.zip" is completed in database
	  And I should see "Processed 1 records." in the resulting batch job file
		And I should see "records deleted successfully: 1" in the resulting batch job file
	  And I should see "records failed processing: 0" in the resulting batch job file
    And I should not see an error log file created
	  And I should not see a warning log file created
    And I re-execute saved query "attendance" to get "0" records 
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|
        | attendance                                |        -1|
        | attendanceEvent                           |        -1|         
        | recordHash                                |        -1| 
    And I should not see "71ca8f7bf0738fdd72ff09858365ef87b4bbb178_id" in the "Midgar" database
    And a query on attendance of for studentId "71ca8f7bf0738fdd72ff09858365ef87b4bbb178_id", schoolYear "2001-2002" and date "2001-09-13" on the "Midgar" tenant has a count of "0"

# Don't quite know the purpose of this scenario, since parent does not contain reference to AttendanceEvent.
@wip
Scenario: Delete Orphan AttendanceEvent Reference with cascade = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
    Then there exist "1" "parent" records like below in "Midgar" tenant. And I save this query as "parent"
        |field                                     |value                                              |
        |_id                                       |1a6c5e20162dc782e85e7752ad9d4f7f9cf0fe8c_id        |
    And I save the collection counts in "Midgar" tenant
    And I post "OrphanAttendanceEventDelete.zip" file as the payload of the ingestion job
    When zip file is scp to ingestion landing zone
    And a batch job for file "OrphanAttendanceEventDelete.zip" is completed in database
	  And I should see "Processed 1 records." in the resulting batch job file
		And I should see "records deleted successfully: 1" in the resulting batch job file
	  And I should see "records failed processing: 0" in the resulting batch job file
    And I should not see an error log file created
	  And I should not see a warning log file created
    And I re-execute saved query "parent" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|
        | parent                                    |        -1|       
        | recordHash                                |        -1|   	
