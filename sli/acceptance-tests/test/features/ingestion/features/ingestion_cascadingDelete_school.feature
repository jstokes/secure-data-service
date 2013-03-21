@RALLY_US3033
Feature: Safe Deletion and Cascading Deletion 

Background: I have a landing zone route configured
Given I am using local data store

Scenario: delete "Jets School", AssignmentSchoolReference is wiped out in the DisciplineAction, ResponsibilitySchoolReference still points to "Sharks School"; delete "Sharks School", DisciplineAction is deleted.
Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
  And I post "prep_cascading_deletion_school.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName                            |
     | student                                   |
     | staff                                     |
     | educationOrganization                     |
     | disciplineAction                          |
     | disciplineIncident                        |
When zip file is scp to ingestion landing zone
  And a batch job for file "prep_cascading_deletion_school.zip" is completed in database
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName                            |              count|
     | student                                   |					1|
     | staff                                     |                  1|
     | educationOrganization                     |					5|
     | disciplineAction                          |					1|
     | disciplineIncident                        |					1|
    And I check to find if record is in collection:
       | collectionName                        | expectedRecordCount | searchParameter                  | searchValue                                  | searchType   |
       | disciplineAction                      | 1                   | body.responsibilitySchoolId      | 0a7994b6ca3cb7bf46c9015373a9d29d003999db_id  | string       |
       | disciplineAction                      | 1                   | body.assignmentSchoolId          | 0d430e252dd56a6a3e9c855d336601d4eaa09842_id  | string       |    
       | disciplineAction                      | 1                   | body.studentId                   | c6fcb4deb579ad0131c2664393d40b4319d8e215_id  | string       |         
       | disciplineAction                      | 1                   | body.staffId                     | b9bad00837eeea5bdbfe2475cf6011a3e4330c5c_id  | string       |         
       | disciplineAction                      | 1                   | body.disciplineIncidentId        | f96dbcb9c71d4b738c3f5d5200f199c45b00bf8c_id  | string       |          
    And I should see "Processed 9 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I post "cascading_deletion_jets_school.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "cascading_deletion_jets_school.zip" is completed in database
    And a batch job log has been created
	Then I should see following map of entry counts in the corresponding collections:
     | collectionName                            |              count|
     | student                                   |					1|
     | staff                                     |                  1|
     | educationOrganization                     |					4|
     | disciplineAction                          |					1|
     | disciplineIncident                        |					1|  
    And I check to find if record is in collection:
     | collectionName                       | expectedRecordCount | searchParameter                   | searchValue                                  | searchType   |
    # | disciplineAction                      | 0                   | body.responsibilitySchoolId      | 0a7994b6ca3cb7bf46c9015373a9d29d003999db_id  | string       |
     | disciplineAction                      | 1                   | body.assignmentSchoolId          | 0d430e252dd56a6a3e9c855d336601d4eaa09842_id  | string       |         
     | disciplineAction                      | 1                   | body.studentId                   | c6fcb4deb579ad0131c2664393d40b4319d8e215_id  | string       |         
     | disciplineAction                      | 1                   | body.staffId                     | b9bad00837eeea5bdbfe2475cf6011a3e4330c5c_id  | string       |         
     | disciplineAction                      | 1                   | body.disciplineIncidentId        | f96dbcb9c71d4b738c3f5d5200f199c45b00bf8c_id  | string       |         
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I post "cascading_deletion_sharks_school.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "cascading_deletion_sharks_school.zip" is completed in database
    And a batch job log has been created
	Then I should see following map of entry counts in the corresponding collections:
     | collectionName                            |              count|
     | student                                   |					1|
     | staff                                     |                  1|
     | educationOrganization                     |					3|
     #| disciplineAction                          |					0|
     | disciplineIncident                        |					1|  
    And I check to find if record is in collection:
     | collectionName                        | expectedRecordCount | searchParameter                  | searchValue                                  | searchType   |
  #   | disciplineAction                      | 0                   | body.responsibilitySchoolId      | 0d430e252dd56a6a3e9c855d336601d4eaa09842_id  | string       |
  #   | disciplineAction                      | 0                   | body.assignmentSchoolId          | 0a7994b6ca3cb7bf46c9015373a9d29d003999db_id  | string       |        
  #   | disciplineAction                      | 0                   | body.studentId                   | c6fcb4deb579ad0131c2664393d40b4319d8e215_id  | string       |         
  #   | disciplineAction                      | 0                   | body.staffId                     | b9bad00837eeea5bdbfe2475cf6011a3e4330c5c_id  | string       |         
  #   | disciplineAction                      | 0                   | body.disciplineIncidentId        | f96dbcb9c71d4b738c3f5d5200f199c45b00bf8c_id  | string       |         
    And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created

