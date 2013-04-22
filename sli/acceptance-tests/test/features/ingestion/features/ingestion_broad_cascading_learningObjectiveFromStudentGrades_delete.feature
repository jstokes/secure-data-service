@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

Background: I have a landing zone route configured
Given I am using local data store

#learningObjective	gradebookEntry 	learningObjectives	0	N	learningObjective missing!		
#learningObjective  learningObjective parentLearningObjective  0   1  learningObjective missing!
#learningObjective  studentCompetency objectiveId.learningObjectiveId 1  1  learningObjective missing!

@wip
Scenario: Delete LearningObjective with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	Then there exist "1" "assessment" records like below in "Midgar" tenant. And I save this query as "assessment"
	|field                                             |value                                                |
	|objectiveAssessment.body.learningObjectives       |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	#Then there exist "1" "section" records like below in "Midgar" tenant. And I save this query as "gradebookEntry"
	#|field                                             |value                                                |
	#|gradebookEntry.body.learningObjectives            |03060bce612f6f8992060b766c3d96a33481f810_id          |	
	And I save the collection counts in "Midgar" tenant
    And I post "BroadLearningObjectiveDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadLearningObjectiveDeleteInGrade.zip" is completed in database
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I should not see "1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id" in the "Midgar" database	

		
Scenario: Safe Delete LearningObjective with Cascade = false, Force = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	Then there exist "1" "assessment" records like below in "Midgar" tenant. And I save this query as "assessment"
	|field                                             |value                                                |
	|objectiveAssessment.body.learningObjectives       |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |

	And I save the collection counts in "Midgar" tenant
    And I post "SafeLearningObjectiveDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "SafeLearningObjectiveDeleteInGrade.zip" is completed in database
    And I should see "records deleted successfully: 0" in the resulting batch job file
    And I should see "records failed processing: 1" in the resulting batch job file
	And I should see "Not all records were processed completely due to errors." in the resulting batch job file
	And I should see "Processed 1 records." in the resulting batch job file
    And I should see "CORE_0066" in the resulting error log file for "InterchangeStudentGrade.xml"
   	And I should not see a warning log file created
    And I re-execute saved query "learningObjective" to get "1" records
    And I re-execute saved query "assessment" to get "1" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|    
        | learningObjective                         |         0| 
        | recordHash                                |      	  0|


Scenario: Safe Delete LearningObjective by Reference with Cascade = false, Force = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	Then there exist "1" "assessment" records like below in "Midgar" tenant. And I save this query as "assessment"
	|field                                             |value                                                |
	|objectiveAssessment.body.learningObjectives       |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |

	And I save the collection counts in "Midgar" tenant
    And I post "SafeLearningObjectiveRefDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "SafeLearningObjectiveRefDeleteInGrade.zip" is completed in database
    And I should see "records deleted successfully: 0" in the resulting batch job file
    And I should see "records failed processing: 1" in the resulting batch job file
	And I should see "Not all records were processed completely due to errors." in the resulting batch job file
	And I should see "Processed 1 records." in the resulting batch job file
    And I should see "CORE_0066" in the resulting error log file for "InterchangeStudentGrade.xml"
   	And I should not see a warning log file created
    And I re-execute saved query "learningObjective" to get "1" records
    And I re-execute saved query "assessment" to get "1" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|    
        | learningObjective                         |         0| 
        | recordHash                                |      	  0|
        
        
Scenario: Delete Orphan LearningObjective with cascade = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |4436e808461d2ccde93e64ebc66cd5036117d01b_id          |
	And I save the collection counts in "Midgar" tenant
    And I post "OrphanLearningObjectiveDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "OrphanLearningObjectiveDeleteInGrade.zip" is completed in database
    And I should see "Processed 1 records." in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should not see an error log file created
   	And I should not see a warning log file created
    And I re-execute saved query "learningObjective" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|
        | learningObjective                         |        -1| 
        | recordHash                                |      	 -1|
	And I should not see "4436e808461d2ccde93e64ebc66cd5036117d01b_id" in the "Midgar" database

	
Scenario: Delete Orphan LearningObjective Ref with cascade = false
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |4436e808461d2ccde93e64ebc66cd5036117d01b_id          |
	And I save the collection counts in "Midgar" tenant
    And I post "OrphanLearningObjectiveRefDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "OrphanLearningObjectiveRefDeleteInGrade.zip" is completed in database
    And I should see "Processed 1 records." in the resulting batch job file
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should not see an error log file created
   	And I should not see a warning log file created
    And I re-execute saved query "learningObjective" to get "0" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|
        | learningObjective                         |        -1| 
        | recordHash                                |      	 -1|
	And I should not see "4436e808461d2ccde93e64ebc66cd5036117d01b_id" in the "Midgar" database
	
	
Scenario: Delete LearningObjective with default settings (Confirm that by default cascade = false, force = true and log violations = true)
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	Then there exist "1" "assessment" records like below in "Midgar" tenant. And I save this query as "assessment"
	|field                                             |value                                                |
	|objectiveAssessment.body.learningObjectives       |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	And I save the collection counts in "Midgar" tenant
    And I post "ForceLearningObjectiveDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "ForceLearningObjectiveDeleteInGrade.zip" is completed in database
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "child records deleted successfully: 0" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "records not considered for processing: 0" in the resulting batch job file
    And I should see "All records processed successfully." in the resulting batch job file
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "CORE_0066" in the resulting warning log file for "InterchangeStudentGrade.xml"
    And I re-execute saved query "learningObjective" to get "0" records
    And I re-execute saved query "assessment" to get "1" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|    
        | learningObjective                         |        -1| 
        | recordHash                                |      	 -1|
 
        
Scenario: Delete LearningObjective Ref with default settings (Confirm that by default cascade = false, force = true and log violations = true)
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "learningObjective" records like below in "Midgar" tenant. And I save this query as "learningObjective"
	|field                                             |value                                                |
	|_id                                               |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	Then there exist "1" "assessment" records like below in "Midgar" tenant. And I save this query as "assessment"
	|field                                             |value                                                |
	|objectiveAssessment.body.learningObjectives       |1b0d13e233ef61ffafb613a8cc6930dfc0d29b92_id          |
	And I save the collection counts in "Midgar" tenant
    And I post "ForceLearningObjectiveRefDeleteInGrade.zip" file as the payload of the ingestion job
  	When zip file is scp to ingestion landing zone
    And a batch job for file "ForceLearningObjectiveRefDeleteInGrade.zip" is completed in database
    And I should see "records deleted successfully: 1" in the resulting batch job file
    And I should see "child records deleted successfully: 0" in the resulting batch job file
    And I should see "records failed processing: 0" in the resulting batch job file
    And I should see "records not considered for processing: 0" in the resulting batch job file
    And I should see "All records processed successfully." in the resulting batch job file
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "CORE_0066" in the resulting warning log file for "InterchangeStudentGrade.xml"
    And I re-execute saved query "learningObjective" to get "0" records
    And I re-execute saved query "assessment" to get "1" records
    And I see that collections counts have changed as follows in tenant "Midgar"
        | collection                                |     delta|    
        | learningObjective                         |        -1| 
        | recordHash                                |      	 -1|