Feature: A bulk extract is triggered and staff and teacher entities are verified

Scenario Outline: Verify staff, and teacher entities were correctly extracted
   When I get the path to the extract file for the tenant "<tenant>" and application with id "<appId>" for the lea "1b223f577827204a1c7e9c851dba06bea6b031fe_id"
   And a "<entity>" extract file exists
   And a the correct number of "<entity>" was extracted from the database
   And a "<entity>" was extracted with all the correct fields
   And I log into "inBloom Dashboards" with a token of "<user>", a "<role>" for "<edorg>" for "<realm>" in tenant "<tenant>", that lasts for "300" seconds
   Then a "<entity>" was extracted in the same format as the api
   
	Examples:
    | entity                                | user       | role             | realm       | tenant | edorg      | appId                                |
	| staff                                 | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |
	| teacher                               | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  | 19cca28d-7357-4044-8df9-caad4b1c8ee4 |

  Scenario Outline: Verify educationOrganization and school entities were correctly extracted for the public extract
    When I get the path to the public extract file for the tenant "<tenant>" and application with id "<appId>"
    And a "<entity>" extract file exists
    And a the correct number of "<entity>" was extracted from the database
    And a "<entity>" was extracted with all the correct fields
    And I log into "inBloom Dashboards" with a token of "<user>", a "<role>" for "<edorg>" for "<realm>" in tenant "<tenant>", that lasts for "300" seconds
    Then a "<entity>" was extracted in the same format as the api

  Examples:
    | entity                                | user       | role             | realm       | tenant | edorg        |   appId |
    | educationOrganization                 | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  |   19cca28d-7357-4044-8df9-caad4b1c8ee4 |
    | school                                | jstevenson | IT Administrator | IL-Daybreak | Midgar | IL-DAYBREAK  |   19cca28d-7357-4044-8df9-caad4b1c8ee4 |