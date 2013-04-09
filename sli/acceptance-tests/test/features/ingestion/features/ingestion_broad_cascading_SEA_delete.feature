@RALLY_US5180
Feature: Safe Deletion and Cascading Deletion

#Type	                Child Type	    Field	                        minOccurs	maxOccurs	Child Collection	
#SEA	                LEA          	parentEducationAgencyReference	1	        1	         educationOrganization		

Background: I have a landing zone route configured
Given I am using local data store

@wip
Scenario: Delete SEA with cascade
    Given I am using preconfigured Ingestion Landing Zone for "Midgar-Daybreak"
    And the "Midgar" tenant db is empty
    When the data from "test/features/ingestion/test_data/delete_fixture_data/" is imported
	Then there exist "1" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "educationOrganization"
	|field                                                           |value                                                |
	|_id                                                             |884daa27d806c2d725bc469b273d840493f84b4d_id          |
	Then there exist "2" "educationOrganization" records like below in "Midgar" tenant. And I save this query as "educationOrganization"
	|field                                                           |value                                                |
	|body.parentEducationAgencyReference                             |884daa27d806c2d725bc469b273d840493f84b4d_id          |
	Then there exist "3" "graduationPlan" records like below in "Midgar" tenant. And I save this query as "graduationPlan"
	|field                                                           |value                                               |
	|body.educationOrganizationId                                    |884daa27d806c2d725bc469b273d840493f84b4d_id          |
	Then there exist "1" "program" records like below in "Midgar" tenant. And I save this query as "program"
	|field                                                           |value                                                |
	|studentProgramAssociation.body.educationOrganizationId          |884daa27d806c2d725bc469b273d840493f84b4d_id          |
	Then there exist "136" "staffEducationOrganizationAssociation" records like below in "Midgar" tenant. And I save this query as "staffEducationOrganizationAssociation"
	|field                                                           |value                                                |
	|body.educationOrganizationReference                             |884daa27d806c2d725bc469b273d840493f84b4d_id          |
	Then there exist " 10" "student" records like below in "Midgar" tenant. And I save this query as "student"
	|field                                                           |value                                                |
	|schools.edOrgs                                                  |884daa27d806c2d725bc469b273d840493f84b4d_id          |
    And I save the collection counts in "Midgar" tenant
	And I post "BroadSEADelete.zip" file as the payload of the ingestion job
	When zip file is scp to ingestion landing zone
    And a batch job for file "BroadSEADelete.zip" is completed in database
	And I should see "records considered for processing: 1" in the resulting batch job file
	And I should see "records ingested successfully: 0" in the resulting batch job file
	And I should see "records deleted successfully: 1" in the resulting batch job file
	And I should see "records failed processing: 0" in the resulting batch job file
	And I should see "records not considered for processing: 0" in the resulting batch job file
	And I should see "All records processed successfully." in the resulting batch job file
	And I should see "Processed 1 records." in the resulting batch job file
    And I should not see an error log file created
	And I should not see a warning log file created
	And I re-execute saved query "educationOrganization" to get "0" records
	And I re-execute saved query "student" to get "0" records
	And I re-execute saved query "staffEducationOrganizationAssociation" to get "0" records
    And I re-execute saved query "program" to get "0" records
	And I re-execute saved query "graduationPlan" to get "0" records
	And I see that collections counts have changed as follows in tenant "Midgar"
	 |collection                        |delta     |
	 |educationOrganization             |        -1|
	 |staffEducationOrganizationAssociation |  -136|
	 |studentProgramAssociation         |       -14|
	 |recordHash                        |         0|
	And I should not see "884daa27d806c2d725bc469b273d840493f84b4d_id" in the "Midgar" database
