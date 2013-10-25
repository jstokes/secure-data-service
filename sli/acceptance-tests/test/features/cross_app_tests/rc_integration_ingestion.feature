@RALLY_US4835
@rc
Feature: Ingestion

    Background: SFTP into landing zone to drop the Small Data Set


        Scenario: Ingest Small Sample Dataset for End to End Testing

            Given a landing zone
            And I drop the file "SmallSampleDataSet.zip" into the landingzone
            Then the "SmallSampleDataSet.zip" should be ingested with the correct number of records
            And the landing zone should contain a file with the message "All records processed successfully."
            And I should not see an error log file created
            And I should not see a warning log file created
            And I should see following map of entry counts in the corresponding collections:
                 | collectionName                           |              count|
                 | assessment                               |                 19|
                 | assessmentFamily                         |                 38|
                 | assessmentPeriodDescriptor               |                  2|
                 | attendance                               |                 75|
                 | calendarDate                             |                558|
                 | cohort                                   |                  3|
                 | competencyLevelDescriptor                |                  0|
                 | course                                   |                 95|
                 | courseOffering                           |                 95|
                 | courseSectionAssociation                 |                  0|
                 | disciplineAction                         |                  2|
                 | disciplineIncident                       |                  2|
                 | educationOrganization                    |                  5|
                 | educationOrganizationAssociation         |                  0|
                 | educationOrganizationSchoolAssociation   |                  0|
                 | grade                                    |                  4|
                 | gradebookEntry                           |                 12|
                 | gradingPeriod                            |                 17|
                 | graduationPlan                           |                  0|
                 | learningObjective                        |                198|
                 | learningStandard                         |               1499|
                 | parent                                   |                  9|
                 | program                                  |                  2|
                 | recordHash                               |              10097|
                 | reportCard                               |                  2|
                 | schoolSessionAssociation                 |                  0|
                 | section                                  |                 97|
                 | sectionAssessmentAssociation             |                  0|
                 | sectionSchoolAssociation                 |                  0|
                 | session                                  |                 22|
                 | sessionCourseAssociation                 |                  0|
                 | staff                                    |                 14|
                 | staffCohortAssociation                   |                  3|
                 | staffEducationOrganizationAssociation    |                 13|
                 | staffProgramAssociation                  |                  7|
                 | student                                  |                 78|
                 | studentAcademicRecord                    |                117|
                 | studentAssessment                        |                203|
                 | studentCohortAssociation                 |                  6|
                 | studentCompetency                        |                 59|
                 | studentCompetencyObjective               |                  4|
                 | studentDisciplineIncidentAssociation     |                  4|
                 | studentObjectiveAssessment               |                300|
                 | studentParentAssociation                 |                  9|
                 | studentProgramAssociation                |                  6|
                 | studentSchoolAssociation                 |                167|
                 | studentSectionAssociation                |                297|
                 | studentGradebookEntry                    |                315|
                 | courseTranscript                         |                196|
                 | teacherSchoolAssociation                 |                  3|
                 | teacherSectionAssociation                |                 11|

        Scenario: Ingest Charter School Dataset
            Given a landing zone
            When I drop the file "CharterSchool.zip" into the landingzone
            And the most recent batch job for file "CharterSchool.zip" has completed successfully
            Then the landing zone should contain a file with the message "All records processed successfully."
            And I should not see an error log file created
            And I should not see a warning log file created

        Scenario: Ingest Additional edOrgs to test App Enable and Authorize
            Given a landing zone
            When I drop the file "AppEnableAndAuthorizeDataSet.zip" into the landingzone
            And the most recent batch job for file "AppEnableAndAuthorizeDataSet.zip" has completed successfully
            Then the landing zone should contain a file with the message "All records processed successfully."
            And I should not see an error log file created
            And I should not see a warning log file created


