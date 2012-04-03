Feature: Negative Ingestion Testing

Background: I have a landing zone route configured
    Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post an empty zip file should fail
  Given I post "emptyFile.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Processed 0 records." in the resulting batch job file
  And I should see "File student.xml: Empty file" in the resulting batch job file
  And I should see "student.xml records considered: 0" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "student.xml records failed: 0" in the resulting batch job file

Scenario: Post a zip file where the first record has an incorrect enum for an attribute value
  Given I post "valueTypeNotMatchAttributeType.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Record 1: Enumeration mismatch for field <sex> (provided: [Boy], expected: [[Female, Male]])" in the resulting error log file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "student.xml records considered: 2" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "student.xml records failed: 1" in the resulting batch job file

Scenario: Post a zip file where the first record has a bad attribute should fail on that record and proceed
  Given I post "firstRecordHasIncorrectAttribute.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Record 1: Missing or empty field <studentUniqueStateId>" in the resulting error log file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "Processed 2 records." in the resulting batch job file
  And I should see "student.xml records considered: 2" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "student.xml records failed: 1" in the resulting batch job file

Scenario: Post a zip file where the second record has a bad attribute should fail and process previous records
  Given I post "secondRecordHasIncorrectAttribute.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Record 2: Missing or empty field <studentUniqueStateId>" in the resulting error log file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "Processed 2 records." in the resulting batch job file
  And I should see "student.xml records considered: 2" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "student.xml records failed: 1" in the resulting batch job file

@wip
Scenario: Post a zip file where the first record has an undefined attribute should fail on that record and proceed
  Given I post "firstRecordHasMoreAttributes.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Record 1: Unknown Field <FullName>" in the resulting error log file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "Processed 2 records." in the resulting batch job file
  And I should see "student.xml records considered: 2" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "student.xml records failed: 1" in the resulting batch job file

Scenario: Post a zip file where the first record has a missing attribute should fail on that record and proceed
  Given I post "firstRecordMissingAttribute.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Record 1: Missing or empty field <firstName>" in the resulting error log file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "Processed 2 records." in the resulting batch job file
  And I should see "student.xml records considered: 2" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "student.xml records failed: 1" in the resulting batch job file

Scenario: Post a zip file where the the edfi input is malformed XML
  Given I post "malformedXML.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
#	And I should see "Input file was malformed" in the resulting error log file
  And I should see "Processed 0 records." in the resulting batch job file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file
  And I should see "student.xml records considered: 0" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "student.xml records failed: 0" in the resulting batch job file

#not sure if this is a valid failure or not
@wip
Scenario: Post a zip file where the the edfi input is missing a declaration line
  Given I post "noDeclarationLine.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
#	And I should see "Input file is missing declaration line" in the resulting error log file
  And I should see "Processed 0 records." in the resulting batch job file
  And I should see "Not all records were processed completely due to errors." in the resulting batch job file

Scenario: Post a zip file where the the edfi input has no records
  Given I post "noRecord.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
  When zip file is scp to ingestion landing zone
  And I am willing to wait upto 5 seconds for ingestion to complete
  And a batch job log has been created
  And I should see "Processed 0 records." in the resulting batch job file
  And I should see "All records processed successfully." in the resulting batch job file
  And I should see "student.xml records considered: 0" in the resulting batch job file
  And I should see "student.xml records ingested successfully: 0" in the resulting batch job file
  And I should see "student.xml records failed: 0" in the resulting batch job file


