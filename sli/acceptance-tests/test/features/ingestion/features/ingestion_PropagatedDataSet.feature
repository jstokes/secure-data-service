Feature: Propagated Dataset Testing

Background: I have a landing zone route configured
Given I am using local data store
  And I am using preconfigured Ingestion Landing Zone

Scenario: All required and optional fields in a dataset should ingest correctly
Given I post "PropagatedDataSet.zip" file as the payload of the ingestion job
  And the following collections are empty in datastore:
     | collectionName                        |
     | recordHash                            |
     | assessment                            |
     | attendance                            |
     | cohort                                |
     | competencyLevelDescriptor             |
#    | competencyLevelDescriptorType         |
     | course	                              |
     | courseOffering                        |
     | courseTranscript                      |
     | disciplineIncident                    |
     | educationOrganization                 |
     | grade                                 |
     | gradebookEntry                        |
     | gradingPeriod                         |
     | graduationPlan                        |
     | learningObjective                     |
     | learningStandard                      |
     | parent                                |
     | program                               |
     | reportCard                            |
     | section                               |
     | staff                                 |
     | staffCohortAssociation                |
     | staffEducationOrganizationAssociation |
     | staffProgramAssociation               |
     | student                               |
     | studentAcademicRecord                 |
     | studentAssessment                     |
     | studentCohortAssociation              |
     | studentCompetency                     |
     | studentCompetencyObjective            |
#    | studentCTEProgramAssociation          |
     | studentDisciplineIncidentAssociation  |
     | studentGradebookEntry                 |
     | studentParentAssociation              |
     | studentProgramAssociation             |
     | studentSchoolAssociation              |
     | studentSectionAssociation             |
#    | studentSpecialEdProgramAssociation    |
     | teacherSchoolAssociation              |
     | teacherSectionAssociation             |

When zip file is scp to ingestion landing zone
  And a batch job for file "PropagatedDataSet.zip" is completed in database

Then I should see following map of entry counts in the corresponding collections:
     | collectionName                        | count |
     | assessment                            | 4     |
     | attendance                            | 16    |
     | cohort                                | 32    |
#remove     | competencyLevelDescriptor             | 2     |
#    | competencyLevelDescriptorType         |       |
     | course	                              | 16    |
     | courseOffering                        | 32    |
     | courseTranscript                      | 32    |
     | disciplineIncident                    | 16    |
     | educationOrganization                 | 14    |
     | grade                                 | 64    |
     | gradebookEntry                        | 2     |
     | gradingPeriod                         | 64    |
     | graduationPlan                        | 16    |
     | learningObjective                     | 66    |
     | learningStandard                      | 16    |
     | parent                                | 25    |
     | program                               | 28    |
     | reportCard                            | 32    |
     | section                               | 64    |
     | staff                                 | 20    |
     | staffCohortAssociation                | 80    |
     | staffEducationOrganizationAssociation | 4     |
     | staffProgramAssociation               | 84    |
     | student                               | 16    |
     | studentAcademicRecord                 | 16    |
     | studentAssessment                     | 32    |
     | studentCohortAssociation              | 46    |
     | studentCompetency                     | 128   |
     | studentCompetencyObjective            | 4     |
#    | studentCTEProgramAssociation          |       |
#remove     | studentDisciplineIncidentAssociation  | 16    |
     | studentGradebookEntry                 | 2     |
     | studentParentAssociation              | 25    |
     | studentProgramAssociation             | 14    |
     | studentSchoolAssociation              | 16    |
     | studentSectionAssociation             | 32    |
#    | studentSpecialEdProgramAssociation    |       |
     | teacherSchoolAssociation              | 16    |
     | teacherSectionAssociation             | 64    |

   And I check the number of records in collection:
     | collectionName                        | expectedRecordCount | searchParameter                                                                                                                       | searchContainer |
     | assessment                            | 4                   | body.assessmentTitle                                                                                                                  | none |
     | assessment                            | 4                   | body.assessmentFamilyHierarchyName                                                                                                    | none |
     | assessment                            | 4                   | body.assessmentIdentificationCode                                                                                                     | body.assessmentIdentificationCode |
     | assessment                            | 4                   | body.assessmentIdentificationCode.ID                                                                                                  | body.assessmentIdentificationCode |
     | assessment                            | 4                   | body.assessmentIdentificationCode.identificationSystem                                                                                | body.assessmentIdentificationCode |
     | assessment                            | 4                   | body.assessmentIdentificationCode.assigningOrganizationCode                                                                           | body.assessmentIdentificationCode |
     | assessment                            | 4                   | body.assessmentCategory                                                                                                               | none |
     | assessment                            | 4                   | body.academicSubject                                                                                                                  | none |
     | assessment                            | 4                   | body.gradeLevelAssessed                                                                                                               | none |
     | assessment                            | 4                   | body.lowestGradeLevelAssessed                                                                                                         | none |
     | assessment                            | 8                   | body.assessmentPerformanceLevel                                                                                                       | body.assessmentPerformanceLevel |
#    | assessment                            |                     | body.assessmentPerformanceLevel.performanceLevelDescriptor                                                                            |
#    | assessment                            |                     | body.assessmentPerformanceLevel.performanceLevelDescriptor.codeValue                                                                  |
#    | assessment                            |                     | body.assessmentPerformanceLevel.performanceLevelDescriptor.description                                                                |
#    | assessment                            |                     | body.assessmentPerformanceLevel.performanceLevelDescriptor.performanceBaseConversion                                                  |
     | assessment                            | 8                   | body.assessmentPerformanceLevel.assessmentReportingMethod                                                                             | body.assessmentPerformanceLevel |
     | assessment                            | 8                   | body.assessmentPerformanceLevel.minimumScore                                                                                          | body.assessmentPerformanceLevel |
     | assessment                            | 8                   | body.assessmentPerformanceLevel.maximumScore                                                                                          | body.assessmentPerformanceLevel |
     | assessment                            | 4                   | body.contentStandard                                                                                                                  | none |
     | assessment                            | 4                   | body.assessmentForm                                                                                                                   | none |
     | assessment                            | 4                   | body.version                                                                                                                          | none |
     | assessment                            | 4                   | body.revisionDate                                                                                                                     | none |
     | assessment                            | 4                   | body.maxRawScore                                                                                                                      | none |
#    | assessment                            |                     | body.minRawScore                                                                                                                      |
     | assessment                            | 4                   | body.nomenclature                                                                                                                     | none |
     | assessment                            | 4                   | body.assessmentPeriodDescriptor                                                                                                       | none |
     | assessment                            | 4                   | body.assessmentPeriodDescriptor.codeValue                                                                                             | none |
     | assessment                            | 4                   | body.assessmentPeriodDescriptor.description                                                                                           | none |
     | assessment                            | 4                   | body.assessmentPeriodDescriptor.shortDescription                                                                                      | none |
     | assessment                            | 4                   | body.assessmentPeriodDescriptor.beginDate                                                                                             | none |
     | assessment                            | 4                   | body.assessmentPeriodDescriptor.endDate                                                                                               | none |
     | assessment                            | 8                   | body.objectiveAssessment                                                                                                              | body.objectiveAssessment |
     | assessment                            | 8                   | body.objectiveAssessment.identificationCode                                                                                           | body.objectiveAssessment |
     | assessment                            | 8                   | body.objectiveAssessment.maxRawScore                                                                                                  | body.objectiveAssessment |
     | assessment                            | 16                  | body.objectiveAssessment.assessmentPerformanceLevel                                                                                   | body.objectiveAssessment:assessmentPerformanceLevel |
     | assessment                            | 8                   | body.objectiveAssessment.percentOfAssessment                                                                                          | body.objectiveAssessment |
     | assessment                            | 8                   | body.objectiveAssessment.nomenclature                                                                                                 | body.objectiveAssessment |
#    | assessment                            |                     | body.objectiveAssessment.assessmentItem                                                                                               | |
     | assessment                            | 10                  | body.objectiveAssessment.learningObjectives                                                                                           | body.objectiveAssessment:learningObjectives |
     | assessment                            | 7                   | body.objectiveAssessment.objectiveAssessments                                                                                         | body.objectiveAssessment:objectiveAssessments |
     | assessment                            | 8                   | body.assessmentItem                                                                                                                   | body.assessmentItem |
     | assessment                            | 8                   | body.assessmentItem.identificationCode                                                                                                | body.assessmentItem |
     | assessment                            | 8                   | body.assessmentItem.itemCategory                                                                                                      | body.assessmentItem |
     | assessment                            | 8                   | body.assessmentItem.maxRawScore                                                                                                       | body.assessmentItem |
     | assessment                            | 8                   | body.assessmentItem.correctResponse                                                                                                   | body.assessmentItem |
     | assessment                            | 16                  | body.assessmentItem.learningStandards                                                                                                 | body.assessmentItem:learningStandards |
     | assessment                            | 8                   | body.assessmentItem.nomenclature                                                                                                      | body.assessmentItem |

     | attendance                            | 16                  | body.studentId                                                                                                                        | none |
     | attendance                            | 16                  | body.schoolId                                                                                                                         | none |
     | attendance                            | 16                  | body.schoolYearAttendance                                                                                                             | body.schoolYearAttendance |
     | attendance                            | 16                  | body.schoolYearAttendance.schoolYear                                                                                                  | body.schoolYearAttendance |
     | attendance                            | 64                  | body.schoolYearAttendance.attendanceEvent                                                                                             | body.schoolYearAttendance:attendanceEvent |
     | attendance                            | 64                  | body.schoolYearAttendance.attendanceEvent.date                                                                                        | body.schoolYearAttendance:attendanceEvent |
     | attendance                            | 64                  | body.schoolYearAttendance.attendanceEvent.event                                                                                       | body.schoolYearAttendance:attendanceEvent |
     | attendance                            | 1                   | body.schoolYearAttendance.attendanceEvent.reason                                                                                      | body.schoolYearAttendance:attendanceEvent |

     | cohort                                | 32                  | body.cohortIdentifier                                                                                                                 | none |
     | cohort                                | 32                  | body.cohortDescription                                                                                                                | none |
     | cohort                                | 32                  | body.cohortType                                                                                                                       | none |
     | cohort                                | 32                  | body.cohortScope                                                                                                                      | none |
     | cohort                                | 32                  | body.academicSubject                                                                                                                  | none |
     | cohort                                | 32                  | body.educationOrgId                                                                                                                   | none |
     | cohort                                | 16                  | body.programId                                                                                                                        | body.programId |

     | competencyLevelDescriptor             | 2                   | body.codeValue                                                                                                                        | none |
     | competencyLevelDescriptor             | 2                   | body.description                                                                                                                      | none |
     | competencyLevelDescriptor             | 2                   | body.performanceBaseConversion                                                                                                        | none |

#    | competencyLevelDescriptorType         |                     | body.codeValue                                                                                                                        |
#    | competencyLevelDescriptorType         |                     | body.description                                                                                                                      |

     | course                                | 16                  | body.courseTitle                                                                                                                      | none |
     | course                                | 16                  | body.numberOfParts                                                                                                                    | none |
     | course                                | 16                  | body.uniqueCourseId                                                                                                                   | none |
     | course                                | 16                  | body.courseCode                                                                                                                       | body.courseCode |
     | course                                | 16                  | body.courseCode.ID                                                                                                                    | body.courseCode |
     | course                                | 16                  | body.courseCode.identificationSystem                                                                                                  | body.courseCode |
     | course                                | 1                   | body.courseCode.assigningOrganizationCode                                                                                             | body.courseCode |
     | course                                | 16                  | body.courseLevel                                                                                                                      | none |
     | course                                | 368                 | body.courseLevelCharacteristics                                                                                                       | body.courseLevelCharacteristics |
     | course                                | 16                  | body.gradesOffered                                                                                                                    | body.gradesOffered |
     | course                                | 16                  | body.subjectArea                                                                                                                      | none |
     | course                                | 16                  | body.courseDescription                                                                                                                | none |
     | course                                | 16                  | body.dateCourseAdopted                                                                                                                | none |
     | course                                | 16                  | body.highSchoolCourseRequirement                                                                                                      | none |
     | course                                | 16                  | body.courseGPAApplicability                                                                                                           | none |
     | course                                | 16                  | body.courseDefinedBy                                                                                                                  | none |
     | course                                | 16                  | body.minimumAvailableCredit                                                                                                           | none |
     | course                                | 16                  | body.minimumAvailableCredit.credit                                                                                                    | none |
     | course                                | 16                  | body.minimumAvailableCredit.creditType                                                                                                | none |
     | course                                | 16                  | body.minimumAvailableCredit.creditConversion                                                                                          | none |
     | course                                | 16                  | body.maximumAvailableCredit                                                                                                           | none |
     | course                                | 16                  | body.maximumAvailableCredit.credit                                                                                                    | none |
     | course                                | 16                  | body.maximumAvailableCredit.creditType                                                                                                | none |
     | course                                | 16                  | body.maximumAvailableCredit.creditConversion                                                                                          | none |
     | course                                | 16                  | body.careerPathway                                                                                                                    | none |
     | course                                | 16                  | body.schoolId                                                                                                                         | none |

     | courseOffering                        | 32                  | body.localCourseCode                                                                                                                  | none |
     | courseOffering                        | 32                  | body.localCourseTitle                                                                                                                 | none |
     | courseOffering                        | 32                  | body.schoolId                                                                                                                         | none |
     | courseOffering                        | 32                  | body.courseId                                                                                                                         | none |
     | courseOffering                        | 32                  | body.sessionId                                                                                                                        | none |

     | courseTranscript                      | 32                  | body.courseAttemptResult                                                                                                              | none |
     | courseTranscript                      | 32                  | body.creditsAttempted                                                                                                                 | none |
     | courseTranscript                      | 32                  | body.creditsAttempted.credit                                                                                                          | none |
     | courseTranscript                      | 32                  | body.creditsAttempted.creditType                                                                                                      | none |
     | courseTranscript                      | 32                  | body.creditsAttempted.creditConversion                                                                                                | none |
     | courseTranscript                      | 32                  | body.creditsEarned                                                                                                                    | none |
     | courseTranscript                      | 32                  | body.creditsEarned.credit                                                                                                             | none |
     | courseTranscript                      | 32                  | body.creditsEarned.creditType                                                                                                         | none |
     | courseTranscript                      | 32                  | body.creditsEarned.creditConversion                                                                                                   | none |
     | courseTranscript                      | 32                  | body.additionalCreditsEarned                                                                                                          | body.additionalCreditsEarned |
     | courseTranscript                      | 32                  | body.additionalCreditsEarned.credit                                                                                                   | body.additionalCreditsEarned |
     | courseTranscript                      | 32                  | body.additionalCreditsEarned.additionalCreditType                                                                                     | body.additionalCreditsEarned |
     | courseTranscript                      | 32                  | body.gradeLevelWhenTaken                                                                                                              | none |
     | courseTranscript                      | 32                  | body.methodCreditEarned                                                                                                               | none |
     | courseTranscript                      | 32                  | body.finalLetterGradeEarned                                                                                                           | none |
     | courseTranscript                      | 1                   | body.finalNumericGradeEarned                                                                                                          | none |
     | courseTranscript                      | 32                  | body.courseRepeatCode                                                                                                                 | none |
#    | courseTranscript                      |                     | body.diagnosticStatement                                                                                                              |
#    | courseTranscript                      |                     | body.gradeType                                                                                                                        |
#    | courseTranscript                      |                     | body.performanceBaseConversion                                                                                                        |
     | courseTranscript                      | 32                  | body.courseId                                                                                                                         | none |
     | courseTranscript                      | 32                  | body.educationOrganizationReference                                                                                                   | body.educationOrganizationReference |
     | courseTranscript                      | 32                  | body.studentAcademicRecordId                                                                                                          | none |
     | courseTranscript                      | 32                  | body.studentId                                                                                                                        | none |

     | disciplineIncident                    | 16                  | body.incidentIdentifier                                                                                                               | none |
     | disciplineIncident                    | 16                  | body.incidentDate                                                                                                                     | none |
     | disciplineIncident                    | 16                  | body.incidentTime                                                                                                                     | none |
     | disciplineIncident                    | 16                  | body.incidentLocation                                                                                                                 | none |
     | disciplineIncident                    | 16                  | body.reporterDescription                                                                                                              | none |
     | disciplineIncident                    | 16                  | body.reporterName                                                                                                                     | none |
     | disciplineIncident                    | 17                  | body.behaviors                                                                                                                        | body.behaviors |
     | disciplineIncident                    | 15                  | body.behaviors.codeValue                                                                                                              | body.behaviors |
     | disciplineIncident                    | 1                   | body.behaviors.shortDescription                                                                                                       | body.behaviors |
     | disciplineIncident                    | 1                   | body.behaviors.description                                                                                                            | body.behaviors |
     | disciplineIncident                    | 16                  | body.secondaryBehaviors                                                                                                               | body.secondaryBehaviors |
     | disciplineIncident                    | 16                  | body.secondaryBehaviors.secondaryBehavior                                                                                             | body.secondaryBehaviors |
     | disciplineIncident                    | 16                  | body.secondaryBehaviors.behaviorCategory                                                                                              | body.secondaryBehaviors |
     | disciplineIncident                    | 16                  | body.weapons                                                                                                                          | body.weapons |
     | disciplineIncident                    | 16                  | body.reportedToLawEnforcement                                                                                                         | none |
     | disciplineIncident                    | 16                  | body.caseNumber                                                                                                                       | none |
     | disciplineIncident                    | 16                  | body.schoolId                                                                                                                         | none |
     | disciplineIncident                    | 16                  | body.staffId                                                                                                                          | none |
 
     | educationOrganization                 | 14                  | body.stateOrganizationId                                                                                                              | none |
     | educationOrganization                 | 8                   | body.educationOrgIdentificationCode                                                                                                   | body.educationOrgIdentificationCode |
     | educationOrganization                 | 8                   | body.educationOrgIdentificationCode.ID                                                                                                | body.educationOrgIdentificationCode |
     | educationOrganization                 | 8                   | body.educationOrgIdentificationCode.identificationSystem                                                                              | body.educationOrgIdentificationCode |
     | educationOrganization                 | 14                  | body.nameOfInstitution                                                                                                                | none |
     | educationOrganization                 | 14                  | body.shortNameOfInstitution                                                                                                           | none |
     | educationOrganization                 | 14                  | body.organizationCategories                                                                                                           | body.organizationCategories |
     | educationOrganization                 | 14                  | body.address                                                                                                                          | body.address |
     | educationOrganization                 | 14                  | body.address.streetNumberName                                                                                                         | body.address |
     | educationOrganization                 | 14                  | body.address.apartmentRoomSuiteNumber                                                                                                 | body.address |
     | educationOrganization                 | 14                  | body.address.buildingSiteNumber                                                                                                       | body.address |
     | educationOrganization                 | 14                  | body.address.city                                                                                                                     | body.address |
     | educationOrganization                 | 14                  | body.address.stateAbbreviation                                                                                                        | body.address |
     | educationOrganization                 | 14                  | body.address.postalCode                                                                                                               | body.address |
     | educationOrganization                 | 14                  | body.address.nameOfCounty                                                                                                             | body.address |
     | educationOrganization                 | 14                  | body.address.countyFIPSCode                                                                                                           | body.address |
#    | educationOrganization                 |                     | body.address.countryCode                                                                                                              |
     | educationOrganization                 | 14                  | body.address.latitude                                                                                                                 | body.address |
     | educationOrganization                 | 14                  | body.address.longitude                                                                                                                | body.address |
     | educationOrganization                 | 14                  | body.address.openDate                                                                                                                 | body.address |
     | educationOrganization                 | 14                  | body.address.closeDate                                                                                                                | body.address |
     | educationOrganization                 | 14                  | body.address.addressType                                                                                                              | body.address |
     | educationOrganization                 | 2                   | body.telephone                                                                                                                        | body.telephone |
     | educationOrganization                 | 2                   | body.telephone.telephoneNumber                                                                                                        | body.telephone |
     | educationOrganization                 | 2                   | body.telephone.institutionTelephoneNumberType                                                                                         | body.telephone |
     | educationOrganization                 | 8                   | body.webSite                                                                                                                          | none |
     | educationOrganization                 | 14                  | body.operationalStatus                                                                                                                | none |
     | educationOrganization                 | 2                   | body.accountabilityRatings                                                                                                            | body.accountabilityRatings |
     | educationOrganization                 | 2                   | body.accountabilityRatings.ratingTitle                                                                                                | body.accountabilityRatings |
     | educationOrganization                 | 2                   | body.accountabilityRatings.rating                                                                                                     | body.accountabilityRatings |
     | educationOrganization                 | 2                   | body.accountabilityRatings.ratingDate                                                                                                 | body.accountabilityRatings |
     | educationOrganization                 | 2                   | body.accountabilityRatings.ratingOrganization                                                                                         | body.accountabilityRatings |
     | educationOrganization                 | 2                   | body.accountabilityRatings.ratingProgram                                                                                              | body.accountabilityRatings |
     | educationOrganization                 | 20                  | body.programReference                                                                                                                 | body.programReference |
#    | educationOrganization                 |                     | body.agencyHierarchyName                                                                                                              |
     | educationOrganization                 | 12                  | body.parentEducationAgencyReference                                                                                                   | none |
     | educationOrganization                 | 176                 | body.gradesOffered                                                                                                                    | body.gradesOffered |
     | educationOrganization                 | 96                  | body.schoolCategories                                                                                                                 | body.schoolCategories |
     | educationOrganization                 | 8                   | body.schoolType                                                                                                                       | none |
     | educationOrganization                 | 8                   | body.charterStatus                                                                                                                    | none |
     | educationOrganization                 | 8                   | body.titleIPartASchoolDesignation                                                                                                     | none |
     | educationOrganization                 | 8                   | body.magnetSpecialProgramEmphasisSchool                                                                                               | none |
     | educationOrganization                 | 8                   | body.administrativeFundingControl                                                                                                     | none |

     | grade                                 | 64                  | body.letterGradeEarned                                                                                                                | none |
     | grade                                 | 64                  | body.numericGradeEarned                                                                                                               | none |
     | grade                                 | 64                  | body.diagnosticStatement                                                                                                              | none |
     | grade                                 | 64                  | body.gradeType                                                                                                                        | none |
     | grade                                 | 64                  | body.performanceBaseConversion                                                                                                        | none |
     | grade                                 | 64                  | body.studentSectionAssociationId                                                                                                      | none |
     | grade                                 | 64                  | body.gradingPeriodId                                                                                                                  | none |

     | gradebookEntry                        | 2                   | body.gradebookEntryType                                                                                                               | none |
     | gradebookEntry                        | 2                   | body.dateAssigned                                                                                                                     | none |
     | gradebookEntry                        | 2                   | body.description                                                                                                                      | none |
     | gradebookEntry                        | 2                   | body.sectionId                                                                                                                        | none |
     | gradebookEntry                        | 2                   | body.gradingPeriodId                                                                                                                  | none |

     | gradingPeriod                         | 64                  | body.gradingPeriodIdentity                                                                                                            | none |
     | gradingPeriod                         | 64                  | body.gradingPeriodIdentity.gradingPeriod                                                                                              | none |
     | gradingPeriod                         | 64                  | body.gradingPeriodIdentity.schoolYear                                                                                                 | none |
     | gradingPeriod                         | 64                  | body.gradingPeriodIdentity.schoolId                                                                                                   | none |
     | gradingPeriod                         | 64                  | body.beginDate                                                                                                                        | none |
     | gradingPeriod                         | 64                  | body.endDate                                                                                                                          | none |
     | gradingPeriod                         | 64                  | body.totalInstructionalDays                                                                                                           | none |
     | gradingPeriod                         | 64                  | body.calendarDateReference                                                                                                            | body.calendarDateReference |

     | graduationPlan                        | 16                  | body.graduationPlanType                                                                                                               | none |
     | graduationPlan                        | 1                   | body.individualPlan                                                                                                                   | none |
     | graduationPlan                        | 16                  | body.totalCreditsRequired                                                                                                             | none |
     | graduationPlan                        | 16                  | body.totalCreditsRequired.credit                                                                                                      | none |
     | graduationPlan                        | 1                   | body.totalCreditsRequired.creditType                                                                                                  | none |
     | graduationPlan                        | 1                   | body.totalCreditsRequired.creditConversion                                                                                            | none |
     | graduationPlan                        | 1                   | body.creditsBySubject                                                                                                                 | body.creditsBySubject |
     | graduationPlan                        | 1                   | body.creditsBySubject.subjectArea                                                                                                     | body.creditsBySubject |
     | graduationPlan                        | 1                   | body.creditsBySubject.credits                                                                                                         | body.creditsBySubject |
     | graduationPlan                        | 1                   | body.creditsBySubject.credits.credit                                                                                                  | body.creditsBySubject |
     | graduationPlan                        | 1                   | body.creditsBySubject.credits.creditType                                                                                              | body.creditsBySubject |
     | graduationPlan                        | 1                   | body.creditsBySubject.credits.creditConversion                                                                                        | body.creditsBySubject |
     | graduationPlan                        | 1                   | body.creditsByCourse                                                                                                                  | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.courseCode                                                                                                       | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.courseCode.ID                                                                                                    | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.courseCode.identificationSystem                                                                                  | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.courseCode.assigningOrganizationCode                                                                             | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.credits                                                                                                          | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.credits.credit                                                                                                   | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.credits.creditType                                                                                               | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.credits.creditConversion                                                                                         | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.creditsByCourse.gradeLevel                                                                                                       | body.creditsByCourse |
     | graduationPlan                        | 1                   | body.educationOrganizationId                                                                                                          | none |

     | learningObjective                     | 66                  | body.learningObjectiveId                                                                                                              | none |
     | learningObjective                     | 66                  | body.learningObjectiveId.identificationCode                                                                                           | none |
     | learningObjective                     | 66                  | body.learningObjectiveId.contentStandardName                                                                                          | none |
     | learningObjective                     | 66                  | body.objective                                                                                                                        | none |
     | learningObjective                     | 66                  | body.description                                                                                                                      | none |
     | learningObjective                     | 66                  | body.academicSubject                                                                                                                  | none |
     | learningObjective                     | 66                  | body.objectiveGradeLevel                                                                                                              | none |
     | learningObjective                     | 4                   | body.learningStandards                                                                                                                | body.learningStandards |
#     | learningObjective                     |                     | body.parentLearningObjective                                                                                                          |

     | learningStandard                      | 16                  | body.learningStandardId                                                                                                               | none |
     | learningStandard                      | 16                  | body.learningStandardId.identificationCode                                                                                            | none |
     | learningStandard                      | 16                  | body.learningStandardId.contentStandardName                                                                                           | none |
     | learningStandard                      | 16                  | body.description                                                                                                                      | none |
     | learningStandard                      | 16                  | body.contentStandard                                                                                                                  | none |
     | learningStandard                      | 16                  | body.gradeLevel                                                                                                                       | none |
     | learningStandard                      | 16                  | body.subjectArea                                                                                                                      | none |
     | learningStandard                      | 16                  | body.courseTitle                                                                                                                      | none |

     | parent                                | 25                  | body.parentUniqueStateId                                                                                                              | none |
     | parent                                | 25                  | body.name                                                                                                                             | none |
     | parent                                | 25                  | body.name.personalTitlePrefix                                                                                                         | none |
     | parent                                | 25                  | body.name.firstName                                                                                                                   | none |
     | parent                                | 25                  | body.name.middleName                                                                                                                  | none |
     | parent                                | 25                  | body.name.lastSurname                                                                                                                 | none |
     | parent                                | 5                   | body.name.generationCodeSuffix                                                                                                        | none |
     | parent                                | 1                   | body.name.maidenName                                                                                                                  | none |
     | parent                                | 25                  | body.name.verification                                                                                                                | none |
     | parent                                | 50                  | body.otherName                                                                                                                        | body.otherName |
     | parent                                | 50                  | body.otherName.personalTitlePrefix                                                                                                    | body.otherName |
     | parent                                | 50                  | body.otherName.firstName                                                                                                              | body.otherName |
     | parent                                | 50                  | body.otherName.middleName                                                                                                             | body.otherName |
     | parent                                | 50                  | body.otherName.lastSurname                                                                                                            | body.otherName |
     | parent                                | 14                  | body.otherName.generationCodeSuffix                                                                                                   | body.otherName |
     | parent                                | 50                  | body.otherName.otherNameType                                                                                                          | body.otherName |
     | parent                                | 25                  | body.sex                                                                                                                              | none |
     | parent                                | 32                  | body.address                                                                                                                          | body.address |
     | parent                                | 32                  | body.address.streetNumberName                                                                                                         | body.address |
     | parent                                | 32                  | body.address.apartmentRoomSuiteNumber                                                                                                 | body.address |
     | parent                                | 32                  | body.address.buildingSiteNumber                                                                                                       | body.address |
     | parent                                | 32                  | body.address.city                                                                                                                     | body.address |
     | parent                                | 32                  | body.address.stateAbbreviation                                                                                                        | body.address |
     | parent                                | 32                  | body.address.postalCode                                                                                                               | body.address |
     | parent                                | 32                  | body.address.nameOfCounty                                                                                                             | body.address |
     | parent                                | 32                  | body.address.countyFIPSCode                                                                                                           | body.address |
#    | parent                                |                     | body.address.countryCode                                                                                                              |
     | parent                                | 32                  | body.address.latitude                                                                                                                 | body.address |
     | parent                                | 32                  | body.address.longitude                                                                                                                | body.address |
     | parent                                | 32                  | body.address.openDate                                                                                                                 | body.address |
     | parent                                | 32                  | body.address.closeDate                                                                                                                | body.address |
     | parent                                | 32                  | body.address.addressType                                                                                                              | body.address |
     | parent                                | 50                  | body.telephone                                                                                                                        | body.telephone |
     | parent                                | 50                  | body.telephone.telephoneNumber                                                                                                        | body.telephone |
     | parent                                | 50                  | body.telephone.telephoneNumberType                                                                                                    | body.telephone |
     | parent                                | 50                  | body.telephone.primaryTelephoneNumberIndicator                                                                                        | body.telephone |
     | parent                                | 25                  | body.electronicMail                                                                                                                   | body.electronicMail |
     | parent                                | 25                  | body.electronicMail.emailAddress                                                                                                      | body.electronicMail |
     | parent                                | 25                  | body.electronicMail.emailAddressType                                                                                                  | body.electronicMail |
     | parent                                | 25                  | body.loginId                                                                                                                          | none |

     | program                               | 28                  | body.programId                                                                                                                        | none |
     | program                               | 28                  | body.programType                                                                                                                      | none |
     | program                               | 28                  | body.programSponsor                                                                                                                   | none |
     | program                               | 24                  | body.services                                                                                                                         | body.services |
     | program                               | 22                  | body.services.codeValue                                                                                                               | body.services |
     | program                               | 1                   | body.services.shortDescription                                                                                                        | body.services |
     | program                               | 1                   | body.services.description                                                                                                             | body.services |

     | reportCard                            | 64                  | body.grades                                                                                                                           | body.grades |
     | reportCard                            | 128                 | body.studentCompetencyId                                                                                                              | body.studentCompetencyId |
     | reportCard                            | 32                  | body.gpaGivenGradingPeriod                                                                                                            | none |
     | reportCard                            | 32                  | body.gpaCumulative                                                                                                                    | none |
     | reportCard                            | 32                  | body.numberOfDaysAbsent                                                                                                               | none |
     | reportCard                            | 32                  | body.numberOfDaysInAttendance                                                                                                         | none |
     | reportCard                            | 32                  | body.numberOfDaysTardy                                                                                                                | none |
     | reportCard                            | 32                  | body.studentId                                                                                                                        | none |
     | reportCard                            | 32                  | body.gradingPeriodId                                                                                                                  | none |

     | section                               | 64                  | body.uniqueSectionCode                                                                                                                | none |
     | section                               | 64                  | body.sequenceOfCourse                                                                                                                 | none |
     | section                               | 1                   | body.educationalEnvironment                                                                                                           | none |
     | section                               | 1                   | body.mediumOfInstruction                                                                                                              | none |
     | section                               | 1                   | body.populationServed                                                                                                                 | none |
     | section                               | 1                   | body.availableCredit                                                                                                                  | none |
     | section                               | 1                   | body.availableCredit.credit                                                                                                           | none |
     | section                               | 1                   | body.availableCredit.creditType                                                                                                       | none |
     | section                               | 1                   | body.availableCredit.creditConversion                                                                                                 | none |
     | section                               | 64                  | body.schoolId                                                                                                                         | none |
     | section                               | 64                  | body.sessionId                                                                                                                        | none |
     | section                               | 1                   | body.programReference                                                                                                                 | body.programReference |
     | section                               | 64                  | body.courseOfferingId                                                                                                                 | none |
#    | section                               |                     | body.assessmentReferences                                                                                                             |

     | staff                                 | 20                  | body.staffUniqueStateId                                                                                                               | none |
     | staff                                 | 2                   | body.staffIdentificationCode                                                                                                          | body.staffIdentificationCode |
     | staff                                 | 2                   | body.staffIdentificationCode.ID                                                                                                       | body.staffIdentificationCode |
     | staff                                 | 2                   | body.staffIdentificationCode.identificationSystem                                                                                     | body.staffIdentificationCode |
     | staff                                 | 2                   | body.staffIdentificationCode.assigningOrganizationCode                                                                                | body.staffIdentificationCode |
     | staff                                 | 20                  | body.name                                                                                                                             | none |
     | staff                                 | 20                  | body.name.personalTitlePrefix                                                                                                         | none |
     | staff                                 | 20                  | body.name.firstName                                                                                                                   | none |
     | staff                                 | 20                  | body.name.middleName                                                                                                                  | none |
     | staff                                 | 20                  | body.name.lastSurname                                                                                                                 | none |
     | staff                                 | 6                   | body.name.generationCodeSuffix                                                                                                        | none |
     | staff                                 | 2                   | body.name.maidenName                                                                                                                  | none |
     | staff                                 | 20                  | body.name.verification                                                                                                                | none |
     | staff                                 | 29                  | body.otherName                                                                                                                        | body.otherName |
     | staff                                 | 29                  | body.otherName.personalTitlePrefix                                                                                                    | body.otherName |
     | staff                                 | 29                  | body.otherName.firstName                                                                                                              | body.otherName |
     | staff                                 | 29                  | body.otherName.middleName                                                                                                             | body.otherName |
     | staff                                 | 29                  | body.otherName.lastSurname                                                                                                            | body.otherName |
     | staff                                 | 9                   | body.otherName.generationCodeSuffix                                                                                                   | body.otherName |
     | staff                                 | 29                  | body.otherName.otherNameType                                                                                                          | body.otherName |
     | staff                                 | 20                  | body.sex                                                                                                                              | none |
     | staff                                 | 20                  | body.birthDate                                                                                                                        | none |
     | staff                                 | 34                  | body.address                                                                                                                          | body.address |
     | staff                                 | 34                  | body.address.streetNumberName                                                                                                         | body.address |
     | staff                                 | 34                  | body.address.apartmentRoomSuiteNumber                                                                                                 | body.address |
     | staff                                 | 34                  | body.address.buildingSiteNumber                                                                                                       | body.address |
     | staff                                 | 34                  | body.address.city                                                                                                                     | body.address |
     | staff                                 | 34                  | body.address.stateAbbreviation                                                                                                        | body.address |
     | staff                                 | 34                  | body.address.postalCode                                                                                                               | body.address |
     | staff                                 | 34                  | body.address.nameOfCounty                                                                                                             | body.address |
     | staff                                 | 34                  | body.address.countyFIPSCode                                                                                                           | body.address |
#    | staff                                 |                     | body.address.countryCode                                                                                                              |
     | staff                                 | 34                  | body.address.latitude                                                                                                                 | body.address |
     | staff                                 | 34                  | body.address.longitude                                                                                                                | body.address |
     | staff                                 | 34                  | body.address.openDate                                                                                                                 | body.address |
     | staff                                 | 34                  | body.address.closeDate                                                                                                                | body.address |
     | staff                                 | 34                  | body.address.addressType                                                                                                              | body.address |
     | staff                                 | 20                  | body.telephone                                                                                                                        | body.telephone |
     | staff                                 | 20                  | body.telephone.telephoneNumber                                                                                                        | body.telephone |
     | staff                                 | 20                  | body.telephone.telephoneNumberType                                                                                                    | body.telephone |
     | staff                                 | 20                  | body.telephone.primaryTelephoneNumberIndicator                                                                                        | body.telephone |
     | staff                                 | 4                   | body.electronicMail                                                                                                                   | body.electronicMail |
     | staff                                 | 4                   | body.electronicMail.emailAddress                                                                                                      | body.electronicMail |
     | staff                                 | 4                   | body.electronicMail.emailAddressType                                                                                                  | body.electronicMail |
     | staff                                 | 20                  | body.hispanicLatinoEthnicity                                                                                                          | none |
     | staff                                 | 20                  | body.oldEthnicity                                                                                                                     | none |
     | staff                                 | 20                  | body.race                                                                                                                             | body.race |
     | staff                                 | 20                  | body.highestLevelOfEducationCompleted                                                                                                 | none |
     | staff                                 | 20                  | body.yearsOfPriorProfessionalExperience                                                                                               | none |
     | staff                                 | 20                  | body.yearsOfPriorTeachingExperience                                                                                                   | none |
     | staff                                 | 4                   | body.credentials                                                                                                                      | body.credentials |
     | staff                                 | 4                   | body.credentials.credentialType                                                                                                       | body.credentials |
     | staff                                 | 4                   | body.credentials.credentialField                                                                                                      | body.credentials |
     | staff                                 | 2                   | body.credentials.credentialField.codeValue                                                                                            | body.credentials |
     | staff                                 | 2                   | body.credentials.credentialField.description                                                                                          | body.credentials |
     | staff                                 | 4                   | body.credentials.level                                                                                                                | body.credentials |
     | staff                                 | 4                   | body.credentials.teachingCredentialType                                                                                               | body.credentials |
     | staff                                 | 4                   | body.credentials.credentialIssuanceDate                                                                                               | body.credentials |
     | staff                                 | 4                   | body.credentials.credentialExpirationDate                                                                                             | body.credentials |
     | staff                                 | 4                   | body.credentials.teachingCredentialBasis                                                                                              | body.credentials |
     | staff                                 | 2                   | body.loginId                                                                                                                          | none |
     | staff                                 | 1                   | body.teacherUniqueStateId                                                                                                             | none |
     | staff                                 | 16                  | body.highlyQualifiedTeacher                                                                                                           | none |

     | staffCohortAssociation                | 80                  | body.staffId                                                                                                                          | none |
     | staffCohortAssociation                | 80                  | body.cohortId                                                                                                                         | none |
     | staffCohortAssociation                | 80                  | body.beginDate                                                                                                                        | none |
     | staffCohortAssociation                | 80                  | body.endDate                                                                                                                          | none |
     | staffCohortAssociation                | 80                  | body.studentRecordAccess                                                                                                              | none |

     | staffEducationOrganizationAssociation | 4                   | body.staffReference                                                                                                                   | none |
     | staffEducationOrganizationAssociation | 4                   | body.educationOrganizationReference                                                                                                   | none |
     | staffEducationOrganizationAssociation | 4                   | body.staffClassification                                                                                                              | none |
     | staffEducationOrganizationAssociation | 1                   | body.positionTitle                                                                                                                    | none |
     | staffEducationOrganizationAssociation | 4                   | body.beginDate                                                                                                                        | none |
     | staffEducationOrganizationAssociation | 1                   | body.endDate                                                                                                                          | none |

     | staffProgramAssociation               | 84                  | body.staffId                                                                                                                          | none |
     | staffProgramAssociation               | 84                  | body.programId                                                                                                                        | none |
     | staffProgramAssociation               | 84                  | body.beginDate                                                                                                                        | none |
     | staffProgramAssociation               | 84                  | body.endDate                                                                                                                          | none |
     | staffProgramAssociation               | 84                  | body.studentRecordAccess                                                                                                              | none |

     | student 	                            | 16                  | body.studentUniqueStateId                                                                                                             | none |
     | student	                              | 1                   | body.studentIdentificationCode                                                                                                        | body.studentIdentificationCode |
     | student	                              | 1                   | body.studentIdentificationCode.identificationCode                                                                                     | body.studentIdentificationCode |
     | student	                              | 1                   | body.studentIdentificationCode.identificationSystem                                                                                   | body.studentIdentificationCode |
     | student	                              | 1                   | body.studentIdentificationCode.assigningOrganizationCode                                                                              | body.studentIdentificationCode |
     | student	                              | 16                  | body.name                                                                                                                             | none |
     | student	                              | 16                  | body.name.personalTitlePrefix                                                                                                         | none |
     | student	                              | 16                  | body.name.firstName                                                                                                                   | none |
     | student	                              | 16                  | body.name.middleName                                                                                                                  | none |
     | student	                              | 16                  | body.name.lastSurname                                                                                                                 | none |
     | student	                              | 3                   | body.name.generationCodeSuffix                                                                                                        | none |
     | student	                              | 1                   | body.name.maidenName                                                                                                                  | none |
     | student	                              | 16                  | body.name.verification                                                                                                                | none |
     | student	                              | 32                  | body.otherName                                                                                                                        | body.otherName |
     | student	                              | 32                  | body.otherName.personalTitlePrefix                                                                                                    | body.otherName |
     | student	                              | 32                  | body.otherName.firstName                                                                                                              | body.otherName |
     | student	                              | 32                  | body.otherName.middleName                                                                                                             | body.otherName |
     | student	                              | 32                  | body.otherName.lastSurname                                                                                                            | body.otherName |
     | student	                              | 12                  | body.otherName.generationCodeSuffix                                                                                                   | body.otherName |
     | student	                              | 32                  | body.otherName.otherNameType                                                                                                          | body.otherName |
     | student	                              | 16                  | body.sex                                                                                                                              | none |
     | student	                              | 16                  | body.birthData                                                                                                                        | none |
     | student	                              | 16                  | body.birthData.birthDate                                                                                                              | none |
     | student	                              | 1                   | body.birthData.cityOfBirth                                                                                                            | none |
     | student	                              | 1                   | body.birthData.stateOfBirthAbbreviation                                                                                               | none |
     | student	                              | 1                   | body.birthData.countryOfBirthCode                                                                                                     | none |
     | student	                              | 1                   | body.birthData.dateEnteredUS                                                                                                          | none |
     | student	                              | 1                   | body.birthData.multipleBirthStatus                                                                                                    | none |
     | student	                              | 24                  | body.address                                                                                                                          | body.address |
     | student	                              | 24                  | body.address.streetNumberName                                                                                                         | body.address |
     | student	                              | 24                  | body.address.apartmentRoomSuiteNumber                                                                                                 | body.address |
     | student	                              | 24                  | body.address.buildingSiteNumber                                                                                                       | body.address |
     | student	                              | 24                  | body.address.city                                                                                                                     | body.address |
     | student	                              | 24                  | body.address.stateAbbreviation                                                                                                        | body.address |
     | student	                              | 24                  | body.address.postalCode                                                                                                               | body.address |
     | student	                              | 24                  | body.address.nameOfCounty                                                                                                             | body.address |
     | student	                              | 24                  | body.address.countyFIPSCode                                                                                                           | body.address |
#    | student	                             |                     | body.address.countryCode                                                                                                              |
     | student 	                            | 24                  | body.address.latitude                                                                                                                 | body.address |
     | student	                              | 24                  | body.address.longitude                                                                                                                | body.address |
     | student	                              | 24                  | body.address.openDate                                                                                                                 | body.address |
     | student	                              | 24                  | body.address.closeDate                                                                                                                | body.address |
     | student	                              | 24                  | body.address.addressType                                                                                                              | body.address |
     | student	                              | 32                  | body.telephone                                                                                                                        | body.telephone |
     | student	                              | 32                  | body.telephone.telephoneNumber                                                                                                        | body.telephone |
     | student	                              | 32                  | body.telephone.telephoneNumberType                                                                                                    | body.telephone |
     | student	                              | 32                  | body.telephone.primaryTelephoneNumberIndicator                                                                                        | body.telephone |
     | student	                              | 32                  | body.electronicMail                                                                                                                   | body.electronicMail |
     | student	                              | 32                  | body.electronicMail.emailAddress                                                                                                      | body.electronicMail |
     | student	                              | 32                  | body.electronicMail.emailAddressType                                                                                                  | body.electronicMail |
     | student	                              | 16                  | body.profileThumbnail                                                                                                                 | none |
     | student	                              | 16                  | body.hispanicLatinoEthnicity                                                                                                          | none |
     | student	                              | 16                  | body.oldEthnicity                                                                                                                     | none |
     | student	                              | 32                  | body.race                                                                                                                             | body.race |
     | student	                              | 16                  | body.economicDisadvantaged                                                                                                            | none |
     | student	                              | 16                  | body.schoolFoodServicesEligibility                                                                                                    | none |
     | student	                              | 32                  | body.studentCharacteristics                                                                                                           | body.studentCharacteristics |
     | student	                              | 32                  | body.studentCharacteristics.characteristic                                                                                            | body.studentCharacteristics |
     | student	                              | 32                  | body.studentCharacteristics.beginDate                                                                                                 | body.studentCharacteristics |
     | student	                              | 32                  | body.studentCharacteristics.endDate                                                                                                   | body.studentCharacteristics |
     | student	                              | 1                   | body.studentCharacteristics.designatedBy                                                                                              | body.studentCharacteristics |
     | student	                              | 16                  | body.limitedEnglishProficiency                                                                                                        | none |
     | student	                              | 32                  | body.languages                                                                                                                        | body.languages |
     | student	                              | 32                  | body.homeLanguages                                                                                                                    | body.homeLanguages |
     | student	                              | 32                  | body.disabilities                                                                                                                     | body.disabilities |
     | student	                              | 32                  | body.disabilities.disability                                                                                                          | body.disabilities |
     | student	                              | 1                   | body.disabilities.disabilityDiagnosis                                                                                                 | body.disabilities |
     | student	                              | 2                   | body.disabilities.orderOfDisability                                                                                                   | body.disabilities |
     | student	                              | 16                  | body.section504Disabilities                                                                                                           | body.section504Disabilities |
     | student 	                            | 16                  | body.displacementStatus                                                                                                               | none |
     | student	                              | 32                  | body.programParticipations                                                                                                            | body.programParticipations |
     | student	                              | 32                  | body.programParticipations.program                                                                                                    | body.programParticipations |
     | student	                              | 32                  | body.programParticipations.beginDate                                                                                                  | body.programParticipations |
     | student	                              | 32                  | body.programParticipations.endDate                                                                                                    | body.programParticipations |
     | student	                              | 1                   | body.programParticipations.designatedBy                                                                                               | body.programParticipations |
     | student	                              | 16                  | body.learningStyles                                                                                                                   | none |
     | student	                              | 16                  | body.learningStyles.visualLearning                                                                                                    | none |
     | student	                              | 16                  | body.learningStyles.auditoryLearning                                                                                                  | none |
     | student	                              | 16                  | body.learningStyles.tactileLearning                                                                                                   | none |
     | student	                              | 32                  | body.cohortYears                                                                                                                      | body.cohortYears |
     | student	                              | 32                  | body.cohortYears.schoolYear                                                                                                           | body.cohortYears |
     | student	                              | 32                  | body.cohortYears.cohortYearType                                                                                                       | body.cohortYears |
     | student	                              | 32                  | body.studentIndicators                                                                                                                | body.studentIndicators |
     | student	                              | 1                   | body.studentIndicators.indicatorGroup                                                                                                 | body.studentIndicators |
     | student	                              | 32                  | body.studentIndicators.indicatorName                                                                                                  | body.studentIndicators |
     | student	                              | 32                  | body.studentIndicators.indicator                                                                                                      | body.studentIndicators |
     | student	                              | 32                  | body.studentIndicators.beginDate                                                                                                      | body.studentIndicators |
     | student	                              | 32                  | body.studentIndicators.endDate                                                                                                        | body.studentIndicators |
     | student	                              | 1                   | body.studentIndicators.designatedBy                                                                                                   | body.studentIndicators |
     | student	                              | 16                  | body.loginId                                                                                                                          | none |
#    | student	                             |                     | body.gradeLevel                                                                                                                       |
#    | student	                             |                     | body.schoolId                                                                                                                         |

     | studentAcademicRecord                 | 16                  | body.studentId                                                                                                                        | none |
     | studentAcademicRecord                 | 16                  | body.sessionId                                                                                                                        | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsEarned                                                                                                          | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsEarned.credit                                                                                                   | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsEarned.creditType                                                                                               | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsEarned.creditConversion                                                                                         | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsAttempted                                                                                                       | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsAttempted.credit                                                                                                | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsAttempted.creditType                                                                                            | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeCreditsAttempted.creditConversion                                                                                      | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeGradePointsEarned                                                                                                      | none |
     | studentAcademicRecord                 | 16                  | body.cumulativeGradePointAverage                                                                                                      | none |
     | studentAcademicRecord                 | 16                  | body.gradeValueQualifier                                                                                                              | none |
     | studentAcademicRecord                 | 16                  | body.classRanking                                                                                                                     | none |
     | studentAcademicRecord                 | 16                  | body.classRanking.classRank                                                                                                           | none |
     | studentAcademicRecord                 | 16                  | body.classRanking.totalNumberInClass                                                                                                  | none |
     | studentAcademicRecord                 | 16                  | body.classRanking.percentageRanking                                                                                                   | none |
     | studentAcademicRecord                 | 16                  | body.classRanking.classRankingDate                                                                                                    | none |
     | studentAcademicRecord                 | 16                  | body.academicHonors                                                                                                                   | body.academicHonors |
     | studentAcademicRecord                 | 16                  | body.academicHonors.academicHonorsType                                                                                                | body.academicHonors |
     | studentAcademicRecord                 | 16                  | body.academicHonors.honorsDescription                                                                                                 | body.academicHonors |
     | studentAcademicRecord                 | 16                  | body.academicHonors.honorAwardDate                                                                                                    | body.academicHonors |
     | studentAcademicRecord                 | 16                  | body.recognitions                                                                                                                     | body.recognitions |
     | studentAcademicRecord                 | 16                  | body.recognitions.recognitionType                                                                                                     | body.recognitions |
     | studentAcademicRecord                 | 16                  | body.recognitions.recognitionDescription                                                                                              | body.recognitions |
     | studentAcademicRecord                 | 16                  | body.recognitions.recognitionAwardDate                                                                                                | body.recognitions |
     | studentAcademicRecord                 | 16                  | body.projectedGraduationDate                                                                                                          | none |
     | studentAcademicRecord                 | 32                  | body.reportCards                                                                                                                      | body.reportCards |

     | studentAssessment                     | 32                  | body.administrationDate                                                                                                               | none |
     | studentAssessment                     | 32                  | body.administrationEndDate                                                                                                            | none |
     | studentAssessment                     | 32                  | body.serialNumber                                                                                                                     | none |
     | studentAssessment                     | 32                  | body.administrationLanguage                                                                                                           | none |
     | studentAssessment                     | 32                  | body.administrationEnvironment                                                                                                        | none |
     | studentAssessment                     | 23                  | body.specialAccommodations                                                                                                            | body.specialAccommodations |
     | studentAssessment                     | 27                  | body.linguisticAccommodations                                                                                                         | body.linguisticAccommodations |
     | studentAssessment                     | 32                  | body.retestIndicator                                                                                                                  | none |
     | studentAssessment                     | 32                  | body.reasonNotTested                                                                                                                  | none |
     | studentAssessment                     | 32                  | body.scoreResults                                                                                                                     | body.scoreResults |
     | studentAssessment                     | 32                  | body.scoreResults.result                                                                                                              | body.scoreResults |
     | studentAssessment                     | 32                  | body.scoreResults.assessmentReportingMethod                                                                                           | body.scoreResults |
     | studentAssessment                     | 32                  | body.gradeLevelWhenAssessed                                                                                                           | none |
#    | studentAssessment                     |                     | body.performanceLevelDescriptors                                                                                                      |
#    | studentAssessment                     |                     | body.performanceLevelDescriptors.codeValue                                                                                            |
#    | studentAssessment                     |                     | body.performanceLevelDescriptors.description                                                                                          |
#    | studentAssessment                     |                     | body.performanceLevelDescriptors.performanceBaseConversion                                                                            |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments                                                                                                      | body.studentObjectiveAssessments |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.scoreResults                                                                                         | body.studentObjectiveAssessments:scoreResults |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.scoreResults.result                                                                                  | body.studentObjectiveAssessments:scoreResults |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.scoreResults.assessmentReportingMethod                                                               | body.studentObjectiveAssessments:scoreResults |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.performanceLevelDescriptors                                                                          |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.performanceLevelDescriptors.codeValue                                                                |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.performanceLevelDescriptors.description                                                              |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.performanceLevelDescriptors.performanceBaseConversion                                                |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.objectiveAssessment                                                                                  | body.studentObjectiveAssessments |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.objectiveAssessment.identificationCode                                                               | body.studentObjectiveAssessments |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.objectiveAssessment.maxRawScore                                                                      | body.studentObjectiveAssessments |
     | studentAssessment                     | 38                  | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel                                                       | body.studentObjectiveAssessments:objectiveAssessment.assessmentPerformanceLevel |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.performanceLevelDescriptor                            |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.performanceLevelDescriptor.codeValue                  |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.performanceLevelDescriptor.description                |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.performanceLevelDescriptor.performanceBaseConversion  |
     | studentAssessment                     | 38                  | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.assessmentReportingMethod                             | body.studentObjectiveAssessments:objectiveAssessment.assessmentPerformanceLevel |
     | studentAssessment                     | 38                  | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.minimumScore                                          | body.studentObjectiveAssessments:objectiveAssessment.assessmentPerformanceLevel |
     | studentAssessment                     | 38                  | body.studentObjectiveAssessments.objectiveAssessment.assessmentPerformanceLevel.maximumScore                                          | body.studentObjectiveAssessments:objectiveAssessment.assessmentPerformanceLevel |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.objectiveAssessment.percentOfAssessment                                                              | body.studentObjectiveAssessments |
     | studentAssessment                     | 19                  | body.studentObjectiveAssessments.objectiveAssessment.nomenclature                                                                     | body.studentObjectiveAssessments |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem                                                                   |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem.identificationCode                                                |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem.itemCategory                                                      |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem.maxRawScore                                                       |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem.correctResponse                                                   |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem.learningStandards                                                 |
#    | studentAssessment                     |                     | body.studentObjectiveAssessments.objectiveAssessment.assessmentItem.nomenclature                                                      |
     | studentAssessment                     | 10                  | body.studentObjectiveAssessments.objectiveAssessment.learningObjectives                                                               | body.studentObjectiveAssessments:objectiveAssessment.learningObjectives |
     | studentAssessment                     | 11                  | body.studentObjectiveAssessments.objectiveAssessment.objectiveAssessments                                                             | body.studentObjectiveAssessments:objectiveAssessment.objectiveAssessments |
     | studentAssessment                     | 32                  | body.studentAssessmentItems                                                                                                           | body.studentAssessmentItems |
     | studentAssessment                     | 1                   | body.studentAssessmentItems.assessmentResponse                                                                                        | body.studentAssessmentItems |
     | studentAssessment                     | 1                   | body.studentAssessmentItems.responseIndicator                                                                                         | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItemResult                                                                                      | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.rawScoreResult                                                                                            | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItem                                                                                            | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItem.identificationCode                                                                         | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItem.itemCategory                                                                               | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItem.maxRawScore                                                                                | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItem.correctResponse                                                                            | body.studentAssessmentItems |
     | studentAssessment                     | 64                  | body.studentAssessmentItems.assessmentItem.learningStandards                                                                          | body.studentAssessmentItems:assessmentItem.learningStandards |
     | studentAssessment                     | 32                  | body.studentAssessmentItems.assessmentItem.nomenclature                                                                               | body.studentAssessmentItems |
     | studentAssessment                     | 32                  | body.studentId                                                                                                                        | none |
     | studentAssessment                     | 32                  | body.assessmentId                                                                                                                     | none |

     | studentCohortAssociation              | 46                  | body.studentId                                                                                                                        | none |
     | studentCohortAssociation              | 46                  | body.cohortId                                                                                                                         | none |
     | studentCohortAssociation              | 46                  | body.beginDate                                                                                                                        | none |
     | studentCohortAssociation              | 46                  | body.endDate                                                                                                                          | none |

     | studentCompetency                     | 128                 | body.objectiveId                                                                                                                      | none |
     | studentCompetency                     | 64                  | body.objectiveId.learningObjectiveId                                                                                                  | none |
     | studentCompetency                     | 64                  | body.objectiveId.studentCompetencyObjectiveId                                                                                         | none |
     | studentCompetency                     | 128                 | body.competencyLevel                                                                                                                  | none |
     | studentCompetency                     | 127                 | body.competencyLevel.codeValue                                                                                                        | none |
     | studentCompetency                     | 1                   | body.competencyLevel.description                                                                                                      | none |
     | studentCompetency                     | 128                 | body.diagnosticStatement                                                                                                              | none |
     | studentCompetency                     | 128                 | body.studentSectionAssociationId                                                                                                      | none |

     | studentCompetencyObjective            | 4                   | body.studentCompetencyObjectiveId                                                                                                     | none |
     | studentCompetencyObjective            | 4                   | body.objective                                                                                                                        | none |
     | studentCompetencyObjective            | 4                   | body.description                                                                                                                      | none |
     | studentCompetencyObjective            | 4                   | body.objectiveGradeLevel                                                                                                              | none |
     | studentCompetencyObjective            | 4                   | body.educationOrganizationId                                                                                                          | none |

#    | studentCTEProgramAssociation          |                     | body.studentId                                                                                                                        |
#    | studentCTEProgramAssociation          |                     | body.programId                                                                                                                        |
#    | studentCTEProgramAssociation          |                     | body.services                                                                                                                         |
#    | studentCTEProgramAssociation          |                     | body.services.codeValue                                                                                                               |
#    | studentCTEProgramAssociation          |                     | body.services.shortDescription                                                                                                        |
#    | studentCTEProgramAssociation          |                     | body.services.description                                                                                                             |
#    | studentCTEProgramAssociation          |                     | body.beginDate                                                                                                                        |
#    | studentCTEProgramAssociation          |                     | body.endDate                                                                                                                          |
#    | studentCTEProgramAssociation          |                     | body.reasonExited                                                                                                                     |
#    | studentCTEProgramAssociation          |                     | body.educationOrganizationId                                                                                                          |
#    | studentCTEProgramAssociation          |                     | body.cteProgram                                                                                                                       |
#    | studentCTEProgramAssociation          |                     | body.cteProgram.careerPathway                                                                                                         |
#    | studentCTEProgramAssociation          |                     | body.cteProgram.cipCode                                                                                                               |
#    | studentCTEProgramAssociation          |                     | body.cteProgram.primaryCTEProgramIndicator                                                                                            |
#    | studentCTEProgramAssociation          |                     | body.cteProgram.cteProgramCompletionIndicator                                                                                         |

     | studentDisciplineIncidentAssociation  | 16                  | body.studentId                                                                                                                        | none |
     | studentDisciplineIncidentAssociation  | 16                  | body.disciplineIncidentId                                                                                                             | none |
     | studentDisciplineIncidentAssociation  | 16                  | body.studentParticipationCode                                                                                                         | none |
     | studentDisciplineIncidentAssociation  | 3                   | body.behaviors                                                                                                                        | body.behaviors |
     | studentDisciplineIncidentAssociation  | 1                   | body.behaviors.codeValue                                                                                                              | body.behaviors |
     | studentDisciplineIncidentAssociation  | 1                   | body.behaviors.shortDescription                                                                                                       | body.behaviors |
     | studentDisciplineIncidentAssociation  | 1                   | body.behaviors.description                                                                                                            | body.behaviors |
     | studentDisciplineIncidentAssociation  | 1                   | body.secondaryBehaviors                                                                                                               | body.secondaryBehaviors |
     | studentDisciplineIncidentAssociation  | 1                   | body.secondaryBehaviors.secondaryBehavior                                                                                             | body.secondaryBehaviors |
     | studentDisciplineIncidentAssociation  | 1                   | body.secondaryBehaviors.behaviorCategory                                                                                              | body.secondaryBehaviors |

     | studentGradebookEntry                 | 2                   | body.gradebookEntryId                                                                                                                 | none |
     | studentGradebookEntry                 | 2                   | body.studentId                                                                                                                        | none |
     | studentGradebookEntry                 | 2                   | body.sectionId                                                                                                                        | none |
     | studentGradebookEntry                 | 2                   | body.dateFulfilled                                                                                                                    | none |
     | studentGradebookEntry                 | 2                   | body.letterGradeEarned                                                                                                                | none |
     | studentGradebookEntry                 | 2                   | body.numericGradeEarned                                                                                                               | none |
     | studentGradebookEntry                 | 2                   | body.diagnosticStatement                                                                                                              | none |
     | studentGradebookEntry                 | 2                   | body.studentSectionAssociationId                                                                                                      | none |

     | studentParentAssociation              | 25                  | body.studentId                                                                                                                        | none |
     | studentParentAssociation              | 25                  | body.parentId                                                                                                                         | none |
     | studentParentAssociation              | 25                  | body.relation                                                                                                                         | none |
     | studentParentAssociation              | 25                  | body.primaryContactStatus                                                                                                             | none |
     | studentParentAssociation              | 25                  | body.livesWith                                                                                                                        | none |
     | studentParentAssociation              | 25                  | body.emergencyContactStatus                                                                                                           | none |
     | studentParentAssociation              | 25                  | body.contactPriority                                                                                                                  | none |
     | studentParentAssociation              | 1                   | body.contactRestrictions                                                                                                              | none |

     | studentProgramAssociation             | 14                  | body.studentId                                                                                                                        | none |
     | studentProgramAssociation             | 14                  | body.programId                                                                                                                        | none |
     | studentProgramAssociation             | 4                   | body.services                                                                                                                         | body.services |
     | studentProgramAssociation             | 1                   | body.services.codeValue                                                                                                               | body.services |
     | studentProgramAssociation             | 1                   | body.services.shortDescription                                                                                                        | body.services |
     | studentProgramAssociation             | 2                   | body.services.description                                                                                                             | body.services |
     | studentProgramAssociation             | 14                  | body.beginDate                                                                                                                        | none |
     | studentProgramAssociation             | 14                  | body.endDate                                                                                                                          | none |
     | studentProgramAssociation             | 14                  | body.reasonExited                                                                                                                     | none |
     | studentProgramAssociation             | 14                  | body.educationOrganizationId                                                                                                          | none |

     | studentSchoolAssociation              | 16                  | body.studentId                                                                                                                        | none |
     | studentSchoolAssociation              | 16                  | body.schoolId                                                                                                                         | none |
     | studentSchoolAssociation              | 16                  | body.schoolYear                                                                                                                       | none |
     | studentSchoolAssociation              | 16                  | body.entryDate                                                                                                                        | none |
     | studentSchoolAssociation              | 16                  | body.entryGradeLevel                                                                                                                  | none |
     | studentSchoolAssociation              | 1                   | body.entryType                                                                                                                        | none |
     | studentSchoolAssociation              | 1                   | body.repeatGradeIndicator                                                                                                             | none |
     | studentSchoolAssociation              | 1                   | body.classOf                                                                                                                          | none |
     | studentSchoolAssociation              | 1                   | body.schoolChoiceTransfer                                                                                                             | none |
     | studentSchoolAssociation              | 1                   | body.exitWithdrawDate                                                                                                                 | none |
     | studentSchoolAssociation              | 1                   | body.exitWithdrawType                                                                                                                 | none |
     | studentSchoolAssociation              | 1                   | body.educationalPlans                                                                                                                 | body.educationalPlans |
     | studentSchoolAssociation              | 1                   | body.graduationPlanId                                                                                                                 | none |

     | studentSectionAssociation             | 32                  | body.studentId                                                                                                                        | none |
     | studentSectionAssociation             | 32                  | body.sectionId                                                                                                                        | none |
     | studentSectionAssociation             | 32                  | body.beginDate                                                                                                                        | none |
     | studentSectionAssociation             | 32                  | body.endDate                                                                                                                          | none |
     | studentSectionAssociation             | 32                  | body.homeroomIndicator                                                                                                                | none |
     | studentSectionAssociation             | 32                  | body.repeatIdentifier                                                                                                                 | none |

#    | studentSpecialEdProgramAssociation    |                     | body.studentId                                                                                                                        |
#    | studentSpecialEdProgramAssociation    |                     | body.programId                                                                                                                        |
#    | studentSpecialEdProgramAssociation    |                     | body.services                                                                                                                         |
#    | studentSpecialEdProgramAssociation    |                     | body.services.codeValue                                                                                                               |
#    | studentSpecialEdProgramAssociation    |                     | body.services.shortDescription                                                                                                        |
#    | studentSpecialEdProgramAssociation    |                     | body.services.description                                                                                                             |
#    | studentSpecialEdProgramAssociation    |                     | body.beginDate                                                                                                                        |
#    | studentSpecialEdProgramAssociation    |                     | body.endDate                                                                                                                          |
#    | studentSpecialEdProgramAssociation    |                     | body.reasonExited                                                                                                                     |
#    | studentSpecialEdProgramAssociation    |                     | body.educationOrganizationId                                                                                                          |
#    | studentSpecialEdProgramAssociation    |                     | body.ideaEligibility                                                                                                                  |
#    | studentSpecialEdProgramAssociation    |                     | body.educationalEnvironment                                                                                                           |
#    | studentSpecialEdProgramAssociation    |                     | body.specialEducationHoursPerWeek                                                                                                     |
#    | studentSpecialEdProgramAssociation    |                     | body.multiplyDisabled                                                                                                                 |
#    | studentSpecialEdProgramAssociation    |                     | body.medicallyFragile                                                                                                                 |
#    | studentSpecialEdProgramAssociation    |                     | body.lastEvaluationDate                                                                                                               |
#    | studentSpecialEdProgramAssociation    |                     | body.iepReviewDate                                                                                                                    |
#    | studentSpecialEdProgramAssociation    |                     | body.iepBeginDate                                                                                                                     |
#    | studentSpecialEdProgramAssociation    |                     | body.iepEndDate                                                                                                                       |

     | teacherSchoolAssociation              | 16                  | body.teacherId                                                                                                                        | none |
     | teacherSchoolAssociation              | 16                  | body.schoolId                                                                                                                         | none |
     | teacherSchoolAssociation              | 16                  | body.programAssignment                                                                                                                | none |
     | teacherSchoolAssociation              | 16                  | body.instructionalGradeLevels                                                                                                         | body.instructionalGradeLevels |
     | teacherSchoolAssociation              | 16                  | body.academicSubjects                                                                                                                 | body.academicSubjects |

     | teacherSectionAssociation             | 64                  | body.teacherId                                                                                                                        | none |
     | teacherSectionAssociation             | 64                  | body.sectionId                                                                                                                        | none |
     | teacherSectionAssociation             | 64                  | body.classroomPosition                                                                                                                | none |
     | teacherSectionAssociation             | 1                   | body.beginDate                                                                                                                        | none |
     | teacherSectionAssociation             | 1                   | body.endDate                                                                                                                          | none |
     | teacherSectionAssociation             | 1                   | body.highlyQualifiedTeacher                                                                                                           | none |


  And I should see "Processed 1204 records." in the resulting batch job file
  And I should not see an error log file created
  And I should not see a warning log file created