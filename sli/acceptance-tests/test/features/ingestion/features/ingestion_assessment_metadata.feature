@RALLY_US1390 @RALLY_US632
Feature: Transformed Assessment Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "CommonCoreStandards/grade12English.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | learningStandard            |
     | learningObjective           |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
And I post "CommonCoreStandards/grade12Math.zip" file as the payload of the ingestion job
When zip file is scp to ingestion landing zone
  And a batch job log has been created
And I post "assessmentMetaData.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | assessment                  |
     | studentAssessmentAssociation|
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count |
     | assessment                  | 5     |
     | studentAssessmentAssociation| 4     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                                | searchValue                                      |  searchType |
     | assessment                  | 3                   | body.assessmentFamilyHierarchyName             | READ2.READ 2.0.READ 2.0 Kindergarten      | string |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | BOY                                              | string |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | MOY                                              | string |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | EOY                                              | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Writing                                      | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Math                                         | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Critical Reading                             | string |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Arithmetic         | string |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Algebra            | string |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Geometry           | string |
	 | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-English          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Reading          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Mathematics      | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Science          | string |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | ACT-Writing          | string |   
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-1 | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | False            | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-2 | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | True             | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-3 | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | True             | string |
     | assessment                  | 1                   | body.assessmentItem.identificationCode         | AssessmentItem-4 | string |
     | assessment                  | 1                   | body.assessmentItem.itemCategory               | True-False       | string |
     | assessment                  | 1                   | body.assessmentItem.maxRawScore                | 5                | integer |
     | assessment                  | 1                   | body.assessmentItem.correctResponse            | False            | string |
     | studentAssessmentAssociation | 2                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Writing          | string |
     | studentAssessmentAssociation | 2                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math             | string |
     | studentAssessmentAssociation | 2                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Critical Reading | string |
     | studentAssessmentAssociation | 2                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math-Arithmetic  | string |
     | studentAssessmentAssociation | 2                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math-Algebra     | string |
     | studentAssessmentAssociation | 2                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math-Geometry    | string |
     | studentAssessmentAssociation | 1                  | body.studentAssessmentItems.assessmentItem.identificationCode | AssessmentItem-3    | string |
     | studentAssessmentAssociation | 1                  | body.studentAssessmentItems.assessmentResponse                | True                | string |
     | studentAssessmentAssociation | 1                  | body.studentAssessmentItems.assessmentItem.identificationCode | AssessmentItem-4    | string |
     | studentAssessmentAssociation | 1                  | body.studentAssessmentItems.assessmentResponse                | True                | string |
     
  And I should see "Processed 16 records." in the resulting batch job file
  And I should not see an error log file created
  And I should see "dibelsAssessmentMetadata.xml records considered: 3" in the resulting batch job file
  And I should see "dibelsAssessmentMetadata.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "dibelsAssessmentMetadata.xml records failed: 0" in the resulting batch job file
  And I should see "satAssessmentMetadata.xml records considered: 1" in the resulting batch job file
  And I should see "satAssessmentMetadata.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "satAssessmentMetadata.xml records failed: 0" in the resulting batch job file
  And I should see "actAssessmentMetadata.xml records considered: 1" in the resulting batch job file
  And I should see "actAssessmentMetadata.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "actAssessmentMetadata.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeStudent.xml records considered: 1" in the resulting batch job file
  And I should see "InterchangeStudent.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "InterchangeStudent.xml records failed: 0" in the resulting batch job file
  And I should see "InterchangeStudentAssessment.xml records considered: 3" in the resulting batch job file
  And I should see "InterchangeStudentAssessment.xml records ingested successfully: 3" in the resulting batch job file
  And I should see "InterchangeStudentAssessment.xml records failed: 0" in the resulting batch job file
  And I should see "actStudentAssessment.xml records considered: 1" in the resulting batch job file
  And I should see "actStudentAssessment.xml records ingested successfully: 1" in the resulting batch job file
  And I should see "actStudentAssessment.xml records failed: 0" in the resulting batch job file
  And I should see "basicStandards.xml records considered: 6" in the resulting batch job file
  And I should see "basicStandards.xml records ingested successfully: 6" in the resulting batch job file
  And I should see "basicStandards.xml records failed: 0" in the resulting batch job file
  
