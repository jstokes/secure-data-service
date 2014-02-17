Feature: Generate API hybrid edOrg data using Odin data generator

@rc
Scenario: Generate an API data set with hybrid edOrgs for API testing using Odin generate tool
  When I generate the "hybrid_edorgs" data set
  When I update edOrg "IL-CHARTER-SCHOOL" in "InterchangeEducationOrganization.xml" with updated parent refs
  |  ParentReference |
  | STANDARD-SEA     |
  When I convert edorg "4" to a "Education Service Center" in "InterchangeEducationOrganization.xml"
  When I convert edorg "IL-CHARTER-SCHOOL" to a "School" in "InterchangeEducationOrganization.xml"

  Then I should see generated file <File>
| File  |
|ControlFile.ctl|
|InterchangeAssessmentMetadata.xml|
|InterchangeAttendance.xml|
|InterchangeEducationOrgCalendar.xml|
|InterchangeEducationOrganization.xml|
|InterchangeMasterSchedule.xml|
|InterchangeStaffAssociation.xml|
|InterchangeStudentAssessment.xml|
|InterchangeStudentCohort.xml|
|InterchangeStudentDiscipline.xml|
|InterchangeStudentEnrollment.xml|
|InterchangeStudentGrades.xml|
|InterchangeStudentParent.xml|
|InterchangeStudentProgram.xml|
|OdinSampleDataSet.zip|
|manifest.json|
  When I zip generated data under filename "OdinSampleDataSet.zip"
