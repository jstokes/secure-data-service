@RALLY_US4305
@RALLY_US4306

Feature: As a student or staff I want to use apps that access the inBloom API

  Background: None

  Scenario: As a student, I want access to my data to use educational apps
  # Log in via simple-idp and authenticate student credentials
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    When I navigate to GET "/v1/home"
    Then I should validate all the HATEOS links

  Scenario: I check the response body fields of specific student API endpoints
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    And I am using api version "v1"
    And I am accessing data about myself, "matt.sollars"
   When I verify the following response body fields in "/students/067198fd6da91e1aa8d67e28e850f224d6851713_id":
    | field                                                 | value                                       |
    | id                                                    | 067198fd6da91e1aa8d67e28e850f224d6851713_id |
    | entityType                                            | student                                     |
    | studentUniqueStateId                                  | 800000025                                   |
    | sex                                                   | Female                                      |
    | oldEthnicity                                          | White, Not Of Hispanic Origin               |
    | profileThumbnail                                      | 800000025 thumb                             |
    | limitedEnglishProficiency                             | NotLimited                                  |
    #| schoolFoodServicesEligibility                         | Full price                                  |
    | displacementStatus                                    | Status BBB                                  |
    | hispanicLatinoEthnicity                               | false                                       |
    #| studentCharacteristics.beginDate                      | 20013-04-20                                 |
    | name.firstName                                        | Matt                                        |
    | name.middleName                                       | Aida                                        |
    | name.lastSurname                                      | Sollars                                     |
    | studentIdentificationCode.0.identificationCode        | abcde                                       |
    | studentIdentificationCode.0.identificationSystem      | District                                    |
    | studentIdentificationCode.0.assigningOrganizationCode | School                                      |
    | address.0.streetNumberName                            | 707 Elm Street                              |
    | electronicMail.0.emailAddress                         | 4859@fakemail.com                           |
    | telephone.0.telephoneNumber                           | (115)555-5072                               |
    | homeLanguages.0.language                              | Cambodian (Khmer)                           |
    | studentIndicators.0.indicator                         | Indicator 1                                 |
    | otherName.0.lastSurname                               | Charles                                     |
    | languages.0.language                                  | Norwegian                                   |
    | programParticipations.0.program                       | Adult/Continuing Education                  |
    | section504Disabilities.0                              | Sensory Impairment                          |
    | cohortYears.0.schoolYear                              | 2011-2012                                   |
    | race.0                                                | Black - African American                    |
    | disabilities.0.disability                             | Deaf-Blindness                              |
    | studentCharacteristics.0.endDate                      | 2014-08-01                                  |
    | studentCharacteristics.0.designatedBy                 | Teacher                                     |
    | studentCharacteristics.0.characteristic               | Unaccompanied Youth                         |
  When I verify the following response body fields in "/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentParentAssociations/parents?sortBy=parentUniqueStateId":
    | field                                                 | value                                       |
    | 0.address.0.streetNumberName                          | 707 Elm Street                              |
    | 0.electronicMail.0.emailAddress                       | 9008@fakemail.com                           |
    | 0.loginId                                             | 9008@fakemail.com                           |
    | 0.name.firstName                                      | Brenton                                     |
    | 0.otherName.0.firstName                               | Brice                                       |
    | 0.parentUniqueStateId                                 | 800000025-dad                               |
    | 0.sex                                                 | Male                                        |
    | 0.telephone.0.telephoneNumber                         | (512)555-2418                               |
    | 1.parentUniqueStateId                                 | 800000025-mom                               |
  #Fields in assessment domain
  Then I verify the following response body fields exist in "/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentAssessments":
    | field                                                                           |
    | 0.administrationDate                                                            |
    | 0.administrationEndDate                                                         |
    | 0.administrationEnvironment                                                     |
    | 0.administrationLanguage.language                                               |
    | 0.administrationLanguage.identificationSystem                                   |
    | 0.gradeLevelWhenAssessed                                                        |
    | 0.linguisticAccommodations.0                                                    |
    | 0.performanceLevelDescriptors.0.0.codeValue                                     |
    | 0.reasonNotTested                                                               |
    | 0.retestIndicator                                                               |
    | 0.scoreResults.0.result                                                         |
    | 0.scoreResults.0.assessmentReportingMethod                                      |
    | 0.serialNumber                                                                  |
    | 0.specialAccommodations                                                         |
    | 0.studentAssessmentItems.0.assessmentItem.correctResponse                       |
    | 0.studentAssessmentItems.0.assessmentItem.identificationCode                    |
    | 0.studentAssessmentItems.0.assessmentItem.itemCategory                          |
    | 0.studentAssessmentItems.0.assessmentItem.maxRawScore                           |
    | 0.studentAssessmentItems.0.assessmentItem.nomenclature                          |
    | 0.studentAssessmentItems.0.assessmentItem.learningStandards                     |
    | 0.studentAssessmentItems.0.assessmentItemResult                                 |
    | 0.studentAssessmentItems.0.assessmentResponse                                   |
    | 0.studentAssessmentItems.0.rawScoreResult                                       |
    | 0.studentAssessmentItems.0.responseIndicator                                    |
    | 0.studentAssessmentItems.0.responseIndicator                                    |
    | 0.studentObjectiveAssessments.0.objectiveAssessment.assessmentPerformanceLevel  |
    | 0.studentObjectiveAssessments.0.objectiveAssessment.identificationCode          |
    | 0.studentObjectiveAssessments.0.objectiveAssessment.maxRawScore                 |
    | 0.studentObjectiveAssessments.0.objectiveAssessment.nomenclature                |
    | 0.studentObjectiveAssessments.0.objectiveAssessment.percentOfAssessment         |
    | 0.studentObjectiveAssessments.0.objectiveAssessment.learningObjectives          |

  Then I verify the following response body fields exist in "/students/067198fd6da91e1aa8d67e28e850f224d6851713_id/studentAssessments/assessments":
    | field                                                                              |
    | 0.academicSubject                                                                  |
    | 0.assessmentCategory                                                               |
    | 0.assessmentFamilyHierarchyName                                                    |
    | 0.assessmentForm                                                                   |
    | 0.assessmentIdentificationCode                                                     |
    | 0.assessmentPeriodDescriptor.beginDate                                             |
    | 0.assessmentPeriodDescriptor.codeValue                                             |
    | 0.assessmentPeriodDescriptor.description                                           |
    | 0.assessmentPeriodDescriptor.endDate                                               |
    | 0.assessmentPeriodDescriptor.shortDescription                                      |
    | 0.assessmentTitle                                                                  |
    | 0.contentStandard                                                                  |
    | 0.gradeLevelAssessed                                                               |
    | 0.lowestGradeLevelAssessed                                                         |
    | 0.maxRawScore                                                                      |
    | 0.nomenclature                                                                     |
    | 0.objectiveAssessment.0.assessmentPerformanceLevel                                 |
    | 0.objectiveAssessment.0.identificationCode                                         |
    | 0.objectiveAssessment.0.maxRawScore                                                |
    | 0.objectiveAssessment.0.nomenclature                                               |
    | 0.objectiveAssessment.0.percentOfAssessment                                        |
    | 0.objectiveAssessment.0.learningObjectives                                         |
    | 0.objectiveAssessment.0.assessmentPerformanceLevel.0.assessmentReportingMethod                             |
    | 0.objectiveAssessment.0.assessmentPerformanceLevel.0.maximumScore                                          |
    | 0.objectiveAssessment.0.assessmentPerformanceLevel.0.minimumScore                                          |
    | 0.objectiveAssessment.0.assessmentPerformanceLevel.0.performanceLevelDescriptor                            |
    | 0.objectiveAssessment.0.objectiveAssessments.0.identificationCode                                         |
    | 0.objectiveAssessment.0.objectiveAssessments.0.assessmentItem.0.correctResponse    |
    | 0.objectiveAssessment.0.objectiveAssessments.0.assessmentItem.0.identificationCode |
    | 0.objectiveAssessment.0.objectiveAssessments.0.assessmentItem.0.itemCategory       |
    | 0.objectiveAssessment.0.objectiveAssessments.0.assessmentItem.0.maxRawScore        |
    | 0.objectiveAssessment.0.objectiveAssessments.0.assessmentItem.0.nomenclature       |
    | 0.objectiveAssessment.0.objectiveAssessments.0.assessmentItem.0.learningStandards  |
    | 0.revisionDate                                                                     |
    #| 0.version                                                                          |
  Then I verify the following response body fields exist in "/learningObjectives?limit=1":
    | field                                        |
    | 0.academicSubject                            |
    | 0.description                                |
    | 0.learningObjectiveId.contentStandardName    |
    | 0.learningObjectiveId.identificationCode     |
    | 0.objective                                  |
    | 0.objectiveGradeLevel                        |
  Then I verify the following response body fields exist in "/learningStandards/c772fbb0f9b9210d1f2a1bfcd53018b205c46da6_id":
    | field                                        |
    | contentStandard                            |
    #| courseTitle                                |
    | description                                |
    | gradeLevel                                 |
    | learningStandardId.contentStandardName     |
    | learningStandardId.identificationCode      |
    | subjectArea                                |

  @wip
  Scenario: Student should NOT have access to certain fields in API entity response bodies
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    And I am using api version "v1"
    And I am accessing data about myself, "matt.sollars"
   When I verify the following response body fields are NOT visible in "/students/067198fd6da91e1aa8d67e28e850f224d6851713_id":
    | field                                                 | value                                       |
    | id                                                    | 067198fd6da91e1aa8d67e28e850f224d6851713_id |

  @student_public
  Scenario: Student cannot POST public entities
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    And I am using api version "v1"
    When I POST and validate the following entities:
      | entity                        | type                       | returnCode |
      | newProgram                    | program                    | 403        |
      | newSection                    | section                    | 403        |
      | newLearningObjective          | learningObjective          | 403        |
      | newLearningStandard           | learningStandard           | 403        |
      | newCourseOffering             | courseOffering             | 403        |
      | newCompetencyLevelDescriptor  | competencyLevelDescriptor  | 403        |
      | newSession                    | session                    | 403        |
      | newSEACourse                  | course                     | 403        |
      | newStudentCompetencyObjective | studentCompetencyObjective | 403        |
      | newEducationOrganization      | educationOrganization      | 403        |
      | newGradingPeriod              | gradingPeriod              | 403        |
      | newAssessment                 | assessment                 | 403        |

  @student_patch
  Scenario: Student cannot PATCH public entities
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    And I am using api version "v1"
    Then I PATCH entities and check return code
      | Endpoint                    | Id                                          | Field                  | ReturnCode |
      | programs                    | 36980b1432275aae32437bb367fb3b66c5efc90e_id | programType            | 403        |
      | sections                    | 8d9ad6c3b870e8775016fff99fbd9c74920de8d5_id | repeatIdentifier       | 403        |
      | learningObjectives          | a39aa7089c0e0b8a271ed7caad97b8d319f7d236_id | academicSubject        | 403        |
      | learningStandards           | c772fbb0f9b9210d1f2a1bfcd53018b205c46da6_id | subjectArea            | 403        |
      | courseOfferings             | 7e2dc97f5868cf7fe5ec8a279facd9574b29af6a_id | localCourseTitle       | 403        |
      | competencyLevelDescriptor   | c91ae4718903d20289607c3c4335759e652ad569_id | description            | 403        |
      | sessions                    | 3327329ef80b7419a48521818d65743234d6e5fb_id | sessionName            | 403        |
      | courses                     | d875eac3c6117f5448437c192ac1ea7c3cc977dd_id | courseDescription      | 403        |
      | studentCompetencyObjectives | b7080a7f753939752b693bca21fe60375d15587e_id | objective              | 403        |
      | educationOrganizations      | 1b223f577827204a1c7e9c851dba06bea6b031fe_id | shortNameOfInstitution | 403        |
      | gradingPeriods              | 5db742ef357941df75afdfcdf78b12191d5898ef_id | endDate                | 403        |
      | assessments                 | 8e47092935b521fb6aba9fdec94a4f961f04cd45_id | identificationCode     | 403        |

  @wip
  Scenario: Student cannot POST private entities
    Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
    And format "application/json"
    And I am using api version "v1"
    When I POST and validate the following entities:
      | entity             | type         | returnCode |
      | newDaybreakStudent | staffStudent | 403        |

   @wip
   Scenario: Verify Rewrites for Base Level entities for Students
     Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "cegray" with password "cegray1234"
     And my contextual access is defined by the table:
       | Context                | Ids                                         |
       | educationOrganizations | 772a61c687ee7ecd8e6d9ad3369f7883409f803b_id |
       | schools                | 772a61c687ee7ecd8e6d9ad3369f7883409f803b_id |
       | sections               | fb23953d3b55349847fe558e4909a265fab3b6a0_id,ac4aede7e0113d1c003f3da487fc079e124f129d_id,02ffe06e27e313e46e852c1a457ecb25af2cd950_id,6b687d24b9a2b10c664e2248bd8e689a482e47e2_id |
       | students               | 92164cd19ebdbe17cfdcd0e1d177877cdc9a40ef_id |
     And format "application/json"
     When I navigate to the base level URI <Entity> I should see the rewrite in the format of <URI>:
       | Entity                       | URI                                                                            |
       | /assessments                 | /search/assessments                                                            |
       | /attendances                 | /students/@ids/attendances                                                     |
       | /cohorts                     | /students/@ids/studentCohortAssociations/cohorts                               |
       | /competencyLevelDescriptor   | /search/competencyLevelDescriptor                                              |
       | /courseOfferings             | /schools/@ids/courseOfferings                                                  |
       | /courses                     | /schools/@ids/courseOfferings/courses                                          |
       | /courseTranscripts           | /students/@ids/studentAcademicRecords/courseTranscripts                        |
       | /educationOrganizations      | /schools/@ids                                                                  |
       | /gradebookEntries            | /sections/@ids/gradebookEntries                                                |
       | /grades                      | /students/@ids/studentSectionAssociations/grades                               |
       | /gradingPeriods              | /schools/@ids/sessions/gradingPeriods                                          |
       | /graduationPlans             | /schools/@ids/graduationPlans                                                  |
       | /learningObjectives          | /search/learningObjectives                                                     |
       | /learningStandards           | /search/learningStandards                                                      |
       | /parents                     | /students/@ids/studentParentAssociations/parents                               |
       | /programs                    | /students/@ids/studentProgramAssociations/programs                             |
       | /reportCards                 | /students/@ids/reportCards                                                     |
       | /schools                     | /schools/@ids                                                                  |
       | /sections                    | /sections/@ids                                                                 |
       | /sessions                    | /schools/@ids/sessions                                                         |
       | /staff                       | /educationOrganizations/@ids/staffEducationOrgAssignmentAssociations           |
       | /studentAcademicRecords      | /students/@ids/studentAcademicRecords                                          |
       | /studentAssessments          | /students/@ids/studentAssessments                                              |
       | /studentCohortAssociations   | /students/@ids/studentCohortAssociations                                       |
       | /studentCompetencies         | /students/@ids/studentSectionAssociations/studentCompetencies                  |
       | /studentCompetencyObjectives | /educationOrganizations/@ids/studentCompetencyObjectives                       |
       | /studentGradebookEntries     | /students/@ids/studentGradebookEntries                                         |
       | /studentParentAssociations   | /students/@ids/studentParentAssociations                                       |
       | /studentProgramAssociations  | /students/@ids/studentProgramAssociations                                      |
       | /students                    | /sections/@ids/studentSectionAssociations/students                             |
       | /studentSchoolAssociations   | /students/@ids/studentSchoolAssociations                                       |
       | /studentSectionAssociations  | /students/@ids/studentSectionAssociations                                      |
       | /teachers                    | /sections/@ids/teacherSectionAssociations/teachers                             |
       | /teacherSchoolAssociations   | /schools/@ids/teacherSchoolAssociations                                        |
       | /teacherSectionAssociations  | /sections/@ids/teacherSectionAssociations                                      |
       | /yearlyAttendances           | /students/@ids/yearlyAttendances                                               |


Scenario: Student has access to entities via API entpoints
Given I log in to realm "Illinois Daybreak Students" using simple-idp as "student" "student.m.sollars" with password "student.m.sollars1234"
  And format "application/json"
  And I am using api version "v1"
 When I validate I have access to entities via the API access pattern "/v1/Entity/Id":
    | entity                    | id                                          |
    | students                  | 067198fd6da91e1aa8d67e28e850f224d6851713_id |
    | parents                   | 5f8989384287747b1960d16edd95ff2bb318e3bd_id |
    | parents                   | 7f5b783a051b72820eab5f8188c45ade72869f0f_id |
    | parents                   | 5f8989384287747b1960d16edd95ff2bb318e3bd_id,7f5b783a051b72820eab5f8188c45ade72869f0f_id |
    | studentParentAssociations | 067198fd6da91e1aa8d67e28e850f224d6851713_idc43bbfa3df05d4fd2d78a9edfee8fd63fbcf495a_id  |
    | studentParentAssociations | 067198fd6da91e1aa8d67e28e850f224d6851713_ide2f8c24b3e1ab8ead6e134d661a464d0f90e4c8e_id  |

