Feature: A bulk extract is triggered and superdoc and subdoc entities are verified

Scenario Outline: Verify simple entities were correctly extracted
   When I get the path to the extract file for the tenant "<tenant>" and application with id "<appId>"
   And a "<entity>" extract file exists
   And a the correct number of "<entity>" was extracted from the database
   And a "<entity>" was extracted with all the correct fields
   And I log into "inBloom Dashboards" with a token of "<user>", a "<role>" for "<realm>" in tenant "<tenant>", that lasts for "300" seconds
   Then a "<entity>" was extracted in the same format as the api
   
	Examples:
    | entity                                | user       | role             | realm       | tenant | appId                                |
	| assessment                            | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| cohort                                | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| grade                                 | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| gradebookEntry                        | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| program                               | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| reportCard                            | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| section                               | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| student                               | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentAcademicRecord                 | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentAssessment                     | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentCohortAssociation              | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentDisciplineIncidentAssociation  | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentParentAssociation              | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentProgramAssociation             | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentSectionAssociation             | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| teacherSectionAssociation             | jstevenson | IT Administrator | IL-Daybreak | Midgar | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
