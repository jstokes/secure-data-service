Feature: Transformed Assessment Ingestion Test

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: Post a zip file containing all configured interchanges as a payload of the ingestion job: Clean Database
Given I post "assessmentMetaData.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName              |
     | assessment                  |
     | studentAssessmentAssociation|
     | learningStandard            |
When zip file is scp to ingestion landing zone
  And a batch job log has been created
Then I should see following map of entry counts in the corresponding collections:
     | collectionName              | count |
     | assessment                  | 5     |
     | studentAssessmentAssociation| 3     |
     | learningStandard            | 6     |
   And I check to find if record is in collection:
     | collectionName              | expectedRecordCount | searchParameter                                | searchValue                                      |
     | assessment                  | 3                   | body.assessmentFamilyHierarchyName             | DIBELS.DIBELS Next.DIBELS Next Kindergarten      |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | BOY                                              |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | MOY                                              |
     | assessment                  | 1                   | body.assessmentPeriodDescriptor.codeValue      | EOY                                              |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Writing                                      |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Math                                         |
     | assessment                  | 1                   | body.objectiveAssessment.identificationCode    | SAT-Critical Reading                             |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Arithmetic         |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Algebra            |
     | assessment                  | 1                   | body.objectiveAssessment.objectiveAssessments.identificationCode    | SAT-Math-Geometry           |
     | studentAssessmentAssociation| 2                   | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Writing          |
     | studentAssessmentAssociation| 2                   | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math             |
     | studentAssessmentAssociation| 2                   | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Critical Reading |
     | studentAssessmentAssociation| 2                   | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math-Arithmetic  |
     | studentAssessmentAssociation| 2                   | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math-Algebra     |
     | studentAssessmentAssociation| 2                   | body.studentObjectiveAssessments.objectiveAssessment.identificationCode    | SAT-Math-Geometry    |
     | learningStandard            | 6                   | body.subjectArea                               | ELA                                              |
     | assessment                  | 1                   | body.assessmentItem.0.identificationCode       | AssessmentItem-1 |
     | assessment                  | 1                   | body.assessmentItem.1.identificationCode       | AssessmentItem-2 |    
     | assessment                  | 1                   | body.assessmentItem.2.identificationCode       | AssessmentItem-3 |
     | assessment                  | 1                   | body.assessmentItem.3.identificationCode       | AssessmentItem-4 |
     

  And I should see "Processed 15 records." in the resulting batch job file
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
  And I should see "basicStandards.xml records considered: 6" in the resulting batch job file
  And I should see "basicStandards.xml records ingested successfully: 6" in the resulting batch job file
  And I should see "basicStandards.xml records failed: 0" in the resulting batch job file
  