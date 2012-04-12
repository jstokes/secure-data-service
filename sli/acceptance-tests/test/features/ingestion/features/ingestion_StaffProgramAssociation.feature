Feature: StaffProgramAssociation Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "StaffProgramAssociation1.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | program                     |
     | staff                       |
     | staffProgramAssociation     |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count |
     | program                     | 3     |
     | staff                       | 3     |
     | staffProgramAssociation     | 3     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | staffProgramAssociation     | 1                   | body.programId              | ACC-TEST-PROG-1         | string               |
     | staffProgramAssociation     | 1                   | body.programId              | ACC-TEST-PROG-2         | string               |
     | staffProgramAssociation     | 2                   | body.programId              | ACC-TEST-PROG-3         | string               |
     | staffProgramAssociation     | 2                   | body.staffId                | linda.kim               | string               |
     | staffProgramAssociation     | 2                   | body.staffId                | rbraverman              | string               |
     | staffProgramAssociation     | 1                   | body.staffId                | cgray                   | string               |
     | staffProgramAssociation     | 1                   | body.studentRecordAccess    | true                    | string               |
     | staffProgramAssociation     | 2                   | body.studentRecordAccess    | false                   | string               |
     | staffProgramAssociation     | 2                   | body.beginDate              | 2011-01-01              | string               |
     | staffProgramAssociation     | 1                   | body.beginDate              | 2011-06-01              | string               |
     | staffProgramAssociation     | 1                   | body.endDate                | 2012-02-15              | string               |
  And I should see "Processed 9 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "Program1.xml records considered: 3" in the resulting batch job file
  And I should see "Program1.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "Program1.xml records failed: 0" in the resulting batch job file
  And I should see "Staff1.xml records considered: 3" in the resulting batch job file
  And I should see "Staff1.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "Staff1.xml records failed: 0" in the resulting batch job file
  And I should see "StaffProgramAssociation1.xml records considered: 3" in the resulting batch job file
  And I should see "StaffProgramAssociation1.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "StaffProgramAssociation1.xml records failed: 0" in the resulting batch job file

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Populated Database
Given I post "StaffProgramAssociation2.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count |
     | program                     | 3     |
     | staff                       | 3     |
     | staffProgramAssociation     | 4     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             | searchValue             | searchType           |
     | staffProgramAssociation     | 2                   | body.programId              | ACC-TEST-PROG-1         | string               |
     | staffProgramAssociation     | 2                   | body.programId              | ACC-TEST-PROG-2         | string               |
     | staffProgramAssociation     | 2                   | body.programId              | ACC-TEST-PROG-3         | string               |
     | staffProgramAssociation     | 2                   | body.staffId                | linda.kim               | string               |
     | staffProgramAssociation     | 2                   | body.staffId                | rbraverman              | string               |
     | staffProgramAssociation     | 2                   | body.staffId                | cgray                   | string               |
     | staffProgramAssociation     | 3                   | body.studentRecordAccess    | true                    | string               |
     | staffProgramAssociation     | 1                   | body.studentRecordAccess    | false                   | string               |
     | staffProgramAssociation     | 2                   | body.beginDate              | 2011-01-01              | string               |
     | staffProgramAssociation     | 2                   | body.beginDate              | 2011-06-01              | string               |
     | staffProgramAssociation     | 2                   | body.endDate                | 2012-02-15              | string               |
  And I should see "Processed 9 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "Program2.xml records considered: 3" in the resulting batch job file
  And I should see "Program2.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "Program2.xml records failed: 0" in the resulting batch job file
  And I should see "Staff2.xml records considered: 3" in the resulting batch job file
  And I should see "Staff2.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "Staff2.xml records failed: 0" in the resulting batch job file
  And I should see "StaffProgramAssociation2.xml records considered: 4" in the resulting batch job file
  And I should see "StaffProgramAssociation2.xml records ingested successfully: 4" in the resulting batch job file
  And I should see "StaffProgramAssociation2.xml records failed: 0" in the resulting batch job file
