Feature: A bulk extract is triggered and simple one-to-one entities are verified

Scenario Outline: Verify simple entities were correctly extracted for LEA
   When I get the path to the extract file for the tenant "<tenant>" and application with id "<appId>" for the lea "1b223f577827204a1c7e9c851dba06bea6b031fe_id"
   And a "<entity>" extract file exists
   And a the correct number of "<entity>" was extracted from the database
   And a "<entity>" was extracted with all the correct fields
   And I log into "inBloom Dashboards" with a token of "<user>", a "<role>" for "<edorg>" for "<realm>" in tenant "<tenant>", that lasts for "300" seconds
   Then a "<entity>" was extracted in the same format as the api
   
	Examples:
    | entity                                | user       | role             | realm       | tenant | edorg        | appId                                |
    | attendance                            | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	# should be uncommented when endpoint is available
	| courseTranscript                      | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| disciplineIncident                    | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| disciplineAction                      | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| parent                                | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| staffEducationOrganizationAssociation | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| staffCohortAssociation                | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| staffProgramAssociation               | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentCompetency                     | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | studentGradebookEntry                 | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| studentSchoolAssociation              | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| teacherSchoolAssociation              | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |

Scenario Outline: Verify simple entities were correctly extracted for the public extract
  When I get the path to the public extract file for the tenant "<tenant>" and application with id "<appId>"
  And a "<entity>" extract file exists
  And a the correct number of "<entity>" was extracted from the database
  And a "<entity>" was extracted with all the correct fields
   And I log into "inBloom Dashboards" with a token of "<user>", a "<role>" for "<edorg>" for "<realm>" in tenant "<tenant>", that lasts for "300" seconds
  Then a "<entity>" was extracted in the same format as the api

  Examples:
  | entity                                | user       | role             | realm       | tenant | edorg        | appId                                |
  | studentCompetencyObjective            | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
  | competencyLevelDescriptor             | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
  | learningObjective                     | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
  | learningStandard                      | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |

Scenario Outline: Verify simple entities were correctly extracted for low-level or Generic edOrg
   When I get the path to the extract file for the tenant "<tenant>" and application with id "<appId>" for the edOrg "352e8570bd1116d11a72755b987902440045d346_id"
   And a "<entity>" extract file exists
   And the correct number of "<entity>" "<expectedCount>" was extracted from the database
   And a "<entity>" was extracted with all the correct fields
   And I log into "inBloom Dashboards" with a token of "<user>", a "<role>" for "<edorg>" for "<realm>" in tenant "<tenant>", that lasts for "300" seconds
   Then a "<entity>" was extracted in the same format as the api
   
   Examples:
    | entity                                | expectedCount | | user       | role             | realm       | tenant | edorg        | appId                                |
    | attendance                            | 22      | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    #  courseTranscript should be excluded from the extract due to no records match
    #| courseTranscript                      | dbCount | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    # disciplineIncident should be excluded from the extract due to no records match
    #| disciplineIncident                    | dbCount | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    # disciplineAction should be excluded from the extract due to no records match
    #| disciplineAction                      | dbCount | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    # graduationPlan should be excluded from the extract due to no records match
    #| graduationPlan                        | dbCount | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | parent                                | 9       | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | staffEducationOrganizationAssociation | 4       | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | staffCohortAssociation                | 2       | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    # staffProgramAssociation should be excluded from the extract due to no records match
    #| staffProgramAssociation               | dbCount | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | studentCompetency                     |  32     | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | studentGradebookEntry                 | 100     | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | studentSchoolAssociation              | 55      | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | teacherSchoolAssociation              | 1       | | jstevenson | IT Administrator | IL-Daybreak | Midgar | South Daybreak Elementary  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
