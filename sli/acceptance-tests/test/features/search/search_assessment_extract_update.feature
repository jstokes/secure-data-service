@RALLY_US5430 
@RALLY_US5423
Feature:  Send bulk update command to Elastic Search

Scenario: Bulk extract and update for assessment
Given I import into tenant collection
Given I send a command to start the extractor to extract now
And I check that Elastic Search is non-empty
When I do elastic search for assessment in Midgar tenant
Then I should see below records in response
       | _index                                       | _type        | _id                                          | Field                                              | Value                                         |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentTitle                                    | READ2-MOY 2                                   |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentIdentificationCode.identificationSystem  | School                                        |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | academicSubject                                    | Reading                                       |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentCategory                                 | Benchmark test                                |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | gradeLevelAssessed                                 | Second grade                                  |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | lowestGradeLevelAssessed                           | Second grade                                  |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPerformanceLevel.minimumScore            | 145                                           |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPerformanceLevel.maximumScore            | 189                                           |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | contentStandard                                    | LEA Standard                                  |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPeriodDescriptor.beginDate               | 2012-01-01                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPeriodDescriptor.endDate                 | 2012-02-01                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPeriodDescriptor.description             | assessment                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.identificationCode             | 2003-First grade Assessment 2.OA-0 Sub        |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.maxRawScore                    | 50                                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.percentOfAssessment            | 50                                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.nomenclature                   | Nomenclature                                  |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.assessmentPerformanceLevel.minimumScore| 0                                     |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.assessmentPerformanceLevel.maximumScore| 50                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.identificationCode                  | 2003-First grade Assessment 2#4               |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.maxRawScore                         | 10                                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.itemCategory                        | True-False                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.correctResponse                     | false                                         |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentFamilyHierarchyName                      | 2003 Standard.2003 First grade Standard       |

When I update some assessment records in mongo
And I send a command to start the extractor to update "Midgar" tenant now
And I do elastic search for assessment in Midgar tenant
Then I should see below records in response
       | _index                                       | _type        | _id                                          | Field                                              | Value                                         |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentTitle                                    | READ2-MOY 2                                   |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentIdentificationCode.identificationSystem  | LEA                                           |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | academicSubject                                    | Reading                                       |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentCategory                                 | Benchmark test                                |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | gradeLevelAssessed                                 | Second grade                                  |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | lowestGradeLevelAssessed                           | First grade                                   |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPerformanceLevel.minimumScore            | 100                                           |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPerformanceLevel.maximumScore            | 200                                           |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | contentStandard                                    | School Standard                               |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPeriodDescriptor.beginDate               | 2013-01-01                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPeriodDescriptor.endDate                 | 2013-02-01                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | e33ce38ad4136e409b426b1ffe7781d09aed2aec_id  | assessmentPeriodDescriptor.description             | updated_assessment                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.identificationCode             | 2004-First grade Assessment 2.OA-0 Sub        |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.maxRawScore                    | 60                                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.percentOfAssessment            | 60                                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.nomenclature                   | updated-Nomenclature                          |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.assessmentPerformanceLevel.minimumScore| 10                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | objectiveAssessment.assessmentPerformanceLevel.maximumScore| 60                                    |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.identificationCode                  | 2004-First grade Assessment 2#4               |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.maxRawScore                         | 20                                            |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.itemCategory                        | Multiple Choice                               |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentItem.correctResponse                     | A                                             |
       | 02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a     | assessment   | 49da176bc1b8025d5a6c2855cebfec421a418541_id  | assessmentFamilyHierarchyName                      | 2003 First grade Standard                     |
      