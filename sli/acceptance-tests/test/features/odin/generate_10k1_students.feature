@RALLY_US5074
Feature: Generate 10k1 student sample data using Odin data generator

Given I am using the odin working directory

  Scenario: Generate a 10k1 student data set using Odin generate tool
    When I generate the 10001 student data set in the generated directory
    And I zip generated data under filename Odin10k1SampleDataSet.zip to the new Odin10k1SampleDataSet directory
    And I copy generated data to the new Odin10k1SampleDataSet directory

    Then I should see 11 xml interchange files
    And I should see InterchangeAssessmentMetadata.xml has been generated
    And I should see InterchangeAttendance.xml has been generated
    And I should see InterchangeEducationOrganization.xml has been generated
    And I should see InterchangeEducationOrgCalendar.xml has been generated
    And I should see InterchangeMasterSchedule.xml has been generated
    And I should see InterchangeStaffAssociation.xml has been generated
    And I should see InterchangeStudentEnrollment.xml has been generated
    And I should see InterchangeStudentAssessment.xml has been generated
    And I should see InterchangeStudentParent.xml has been generated
    And I should see InterchangeStudentProgram.xml has been generated
    And I should see InterchangeStudentCohort.xml has been generated
    And I should see ControlFile.ctl has been generated
    And I should see Odin10k1SampleDataSet/Odin10k1SampleDataSet.zip has been generated
