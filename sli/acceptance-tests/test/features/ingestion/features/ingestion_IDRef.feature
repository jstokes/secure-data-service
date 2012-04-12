Feature: Session Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "studentAssessment_Valid.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | studentAssessment           |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count    |
     | studentAssessment           | 1000     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter             |
     | studentAssessment           | 1000                | body.studentAssessment      |
     | studentAssessment           | 1000                | body.StudentReference       |
     | studentAssessment           | 1000                | body.AssessmentReference    |
  And I should see "Processed 1000 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "InterchangeStudentAssessment_Valid.xml records considered: 1000" in the resulting batch job file
  And I should see "InterchangeStudentAssessment_Valid.xml records ingested successfully: 1000" in the resulting batch job file
  And I should see "InterchangeStudentAssessment_Valid.xml records failed: 0" in the resulting batch job file
  And I should see "studentAssessment_Valid.xml: Resolved 2000 references to 110 Ed-Fi references for 1000 Ed-Fi entities" in the resulting batch job file

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "studentAssessment_MissingIDRef.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | studentAssessment           |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count    |
     | studentAssessment           | 0        |
  And I should see "Processed 0 records." in the resulting batch job file
  And I should see an error log file created
  And I should see "InterchangeStudentAssessment_MissingIDRef.xml records considered: 1000" in the resulting batch job file
  And I should see "InterchangeStudentAssessment_MissingIDRef.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "InterchangeStudentAssessment_MissingIDRef.xml records failed: 1000" in the resulting batch job file
  And I should see "Error resolving references in XML file studentAssessment_MissingIDRef.xml: Unresolved reference, id=" in the resulting batch job file
  And I should see "STU-1" in the resulting batch job file
  And I should see "Cannot resolve references in XML file studentAssessment_MissingIDRef.xml" in the resulting batch job file

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "studentAssessment_MalformedXML.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | studentAssessment           |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count    |
     | studentAssessment           | 0        |
  And I should see "Processed 0 records." in the resulting batch job file
  And I should see an error log file created
  And I should see "InterchangeStudentAssessment_MalformedXML.xml records considered: 1000" in the resulting batch job file
  And I should see "InterchangeStudentAssessment_MalformedXML.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "InterchangeStudentAssessment_MalformedXML.xml records failed: 1000" in the resulting batch job file
  And I should see "Error parsing XML file studentAssessment_MalformedXML.xml: The element type " in the resulting batch job file
  And I should see "StudentReference" in the resulting batch job file
  And I should see "must be terminated by the matching end-tag " in the resulting batch job file
  And I should see "</StudentReference>" in the resulting batch job file
  And I should see "Cannot extract references from XML file studentAssessment_MalformedXML.xml" in the resulting batch job file

