Feature: Smooks Verification

Background: I have a landing zone route configured
Given I am using local data store
    And I am using preconfigured Ingestion Landing Zone

Scenario: Assessment and StudentAssessment Verification
    Given I post "smooksVer_Assess_Student_Assess.zip" file as the payload of the ingestion job
    And the following collections are empty in datastore:
        | collectionName              |
        | student                     |
        | assessment                  |
        | studentAssessmentAssociation|
    When zip file is scp to ingestion landing zone
    And I am willing to wait upto 20 seconds for ingestion to complete
    And a batch job log has been created
    Then I should see following map of entry counts in the corresponding collections:
        | collectionName              | count |
        | student                     | 1     |
        | assessment                  | 1     |
        | studentAssessmentAssociation| 1     |
    And I find a(n) "assessment" record where "metaData.externalId" is equal to "Grade 8 2011 ISAT Writing"
    And verify the following data in that document:
       | searchParameter                                                          | searchValue                           | searchType           |
       | body.assessmentTitle                                                     | Grade 8 2011 ISAT Writing             | string               |
       | body.assessmentIdentificationCode.0.ID                                   | Grade 8 2011 ISAT Writing             | string               |
       | body.assessmentIdentificationCode.0.identificationSystem                 | Test Contractor                       | string               |
       | body.assessmentCategory                                                  | State summative assessment 3-8 general| string               |
       | body.academicSubject                                                     | Writing                               | string               |
       | body.gradeLevelAssessed                                                  | Eighth grade                          | string               |
       | body.lowestGradeLevelAssessed                                            | Fifth grade                           | string               |
       | body.assessmentPerformanceLevel.0.performanceLevelDescriptor.0.description | 1                               | string               |
       | body.assessmentPerformanceLevel.1.performanceLevelDescriptor.0.description | 2                               | string               |
       | body.assessmentPerformanceLevel.2.performanceLevelDescriptor.0.description | 3                               | string               |
       | body.assessmentPerformanceLevel.3.performanceLevelDescriptor.0.description | 4                               | string               |
       | body.assessmentPerformanceLevel.0.assessmentReportingMethod              | Scale score                               | string               |
       | body.assessmentPerformanceLevel.1.assessmentReportingMethod              | Scale score                               | string               |
       | body.assessmentPerformanceLevel.2.assessmentReportingMethod              | Scale score                               | string               |
       | body.assessmentPerformanceLevel.3.assessmentReportingMethod              | Scale score                               | string               |
       | body.assessmentPerformanceLevel.0.minimumScore                           | 120                                       | integer               |
       | body.assessmentPerformanceLevel.1.minimumScore                           | 221                                       | integer               |
       | body.assessmentPerformanceLevel.2.minimumScore                           | 246                                       | integer               |
       | body.assessmentPerformanceLevel.3.minimumScore                           | 288                                       | integer               |
       | body.assessmentPerformanceLevel.0.maximumScore                           | 221                                       | integer               |
       | body.assessmentPerformanceLevel.1.maximumScore                           | 246                                       | integer               |
       | body.assessmentPerformanceLevel.2.maximumScore                           | 288                                       | integer               |
       | body.assessmentPerformanceLevel.3.maximumScore                           | 410                                       | integer               |
       | body.contentStandard                                                     | State Standard                            | string               |
       | body.assessmentForm                                                      | Multiple Choice                           | string               |
       | body.version                                                             | 1                                         | integer               |
       | body.revisionDate                                                        | 2011-03-12                                | string               |
       | body.maxRawScore                                                         | 450                                       | integer               |
       | body.assessmentFamilyHierarchyName                                       | ISAT.ISAT Writing for Grades 3-8.ISAT Writing for Grade 8 | string               |
       
       
    And I should see "Processed 6 records." in the resulting batch job file
    And I should not see an error log file created
    And I should see "student.xml records considered: 1" in the resulting batch job file
    And I should see "student.xml records ingested successfully: 1" in the resulting batch job file
    And I should see "student.xml records failed: 0" in the resulting batch job file
    And I should see "assess.xml records considered: 4" in the resulting batch job file
    And I should see "assess.xml records ingested successfully: 4" in the resulting batch job file
    And I should see "assess.xml records failed: 0" in the resulting batch job file
    And I should see "stu_assess.xml records considered: 1" in the resulting batch job file
    And I should see "stu_assess.xml records ingested successfully: 1" in the resulting batch job file
    And I should see "stu_assess.xml records failed: 0" in the resulting batch job file
    
    And I find a(n) "studentAssessmentAssociation" record where "metaData.externalId" is equal to "{administrationDate=2011-05-01, studentId=1, assessmentId=Grade 8 2011 ISAT Writing}"
    And verify the following data in that document:
       | searchParameter                                                          | searchValue                           | searchType           |
       | body.administrationDate                                                  | 2011-05-01                            | string               |
       | body.administrationEndDate                                               | 2011-05-01                            | string               |
       | body.serialNumber                                                        | 0                                     | string               |
       | body.administrationLanguage                                              | English                               | string               |
       | body.administrationEnvironment                                           | Testing Center                        | string               |
       | body.specialAccommodations.0                                             | Extra time                            | string               |
       | body.specialAccommodations.1                                             | Dyslexia Bundled                      | string               |
       | body.linguisticAccommodations.0                                          | English Dictionary                    | string               |
       | body.linguisticAccommodations.1                                          | Linguistic Simplification             | string               |
       | body.retestIndicator                                                     | Primary Administration                | string               |
       | body.reasonNotTested                                                     | Medical waiver                        | string               |
       | body.scoreResults.0.assessmentReportingMethod                            | Scale score                           | string               |
       | body.scoreResults.0.result                                               | 320                                   | string               |
       | body.scoreResults.1.assessmentReportingMethod                            | Percentile                            | string               |
       | body.scoreResults.1.result                                               | 94.45                                 | string               |
       | body.scoreResults.2.assessmentReportingMethod                            | Other                                 | string               |
       | body.scoreResults.2.result                                               | 1233L                                 | string               |
       | body.gradeLevelWhenAssessed                                              | Eighth grade                          | string               |
       | body.performanceLevelDescriptors.0.0.description                           | Above Benchmark                       | string               |
       | body.performanceLevelDescriptors.0.1.codeValue                             | 1                                     | string               |
    