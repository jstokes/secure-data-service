Feature: Session Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "Session2.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | session                     |
     | sessionSchoolAssociation    |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count  |
     | session                     | 10     |
     | sessionSchoolAssociation	  | 10     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                             | searchValue             | searchType           |
     | session                     | 1                   | body.sessionName                            | Fall 2011 Able School   | string               |
     | session                     | 10                  | body.schoolYear                             | 1997-1998               | string               |
     | session                     | 25                  | body.term                                   | Fall Semester           | string               |
     | session                     | 5                   | body.beginDate                              | 2011-09-06              | string               |
     | session                     | 5                   | body.endDate                                | 2011-12-16              | string               |
     | session                     | 0                   | body.endDate                                | 2011-12-23              | string               |
     | session                     | 5                   | body.totalInstructionalDays                 | 75                      | integer              |
     | session                     | 0                   | body.totalInstructionalDays                 | 80                      | integer              |
     | sessionSchoolAssociation    | 50                  | body.gradingPeriod.gradingPeriod		        | Second Six Weeks        | string               |
     | sessionSchoolAssociation    | 50                  | body.gradingPeriod.beginDate		  	        | 2010-10-04              | string               |
     | sessionSchoolAssociation    | 50                  | body.gradingPeriod.endDate		   		        | 2010-11-07              | string               |
  And I should see "Processed 50 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "Session1.xml records considered: 50" in the resulting batch job file
  And I should see "Session1.xml records ingested successfully: 50" in the resulting batch job file
  And I should see "Session1.xml records failed: 0" in the resulting batch job file

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Populated Database
Given I post "Session2.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName               | count |
     | session                      | 10    |
     | sessionSchoolAssociation	   | 10    |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                             | searchValue             | searchType           |
     | session                     | 1                   | body.sessionName                            | Fall 2011 Able School   | string               |
     | session                     | 10                  | body.schoolYear                             | 1997-1998               | string               |
     | session                     | 25                  | body.term                                   | Fall Semester           | string               |
     | session                     | 5                   | body.beginDate                              | 2011-09-06              | string               |
     | session                     | 0                   | body.endDate                                | 2011-12-16              | string               |
     | session                     | 5                   | body.endDate                                | 2011-12-23              | string               |
     | session                     | 0                   | body.totalInstructionalDays                 | 75                      | integer              |
     | session                     | 5                   | body.totalInstructionalDays                 | 80                      | integer              |
     | sessionSchoolAssociation    | 50                  | body.gradingPeriod.gradingPeriod		        | Second Six Weeks	      | string               |
     | sessionSchoolAssociation    | 50                  | body.gradingPeriod.beginDate		  	        | 2010-10-04              | string               |
     | sessionSchoolAssociation    | 50                  | body.gradingPeriod.endDate		   		        | 2010-11-07              | string               |
  And I should see "Processed 50 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "Session2.xml records considered: 50" in the resulting batch job file
  And I should see "Session2.xml records ingested successfully: 50" in the resulting batch job file
  And I should see "Session2.xml records failed: 0" in the resulting batch job file
