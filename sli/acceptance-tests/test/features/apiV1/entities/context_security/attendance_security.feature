@RALLY_US209
Feature: Security for Attendance CRUD
As a product owner, I want to validate that my attendance entity is properly secured up to current SLI standards

Scenario: Showing attendance event list link in Student's available links
Given I am user "linda.kim" in IDP "IL"
And I am assigned the Educator role in my IDP
And I teach the student "Marvin Miller"
When I make an API call to get the student "Marvin Miller"
Then I receive a JSON response
And I should see a link to get the list of its attendance events in the response labeled "attendances"

Scenario: Authorized user tries to hit the attendance events list URL directly
Given I am user "linda.kim" in IDP "IL"
And I am assigned the Educator role in my IDP
And I teach the student "Marvin Miller"
When I make an API call to get the student "Marvin Miller"'s attendance events list
Then I should receive a list containing the student "Marvin Miller"'s attendance events

Scenario: Unauthorized authenticated user tries to hit the attendance events list URL directly
Given I am user "linda.kim" in IDP "IL"
And I am assigned the Educator role in my IDP
And I do not teach the student "Delilah D. Sims"
And the sli securityEvent collection is empty
When I make an API call to get the student "Delilah D. Sims"'s attendance events list
Then I get a message that I am not authorized
#ContextValidator.java:288
And I should see a count of "1" in the security event collection
And a security event matching "Access Denied:Access to 6a98d5d3-d508-4b9c-aec2-59fce7e16825_id is not authorized" should be in the sli db
And I check to find if record is in sli db collection:
    | collectionName  | expectedRecordCount | searchParameter         | searchValue                              | searchType |
    | securityEvent   | 1                   | body.userEdOrg          | IL-DAYBREAK                              | string     |
    | securityEvent   | 1                   | body.targetEdOrgList    | 152901002                                | string     |

Scenario: Authorized user accessing a specific school year attendance events of a student
Given I am user "linda.kim" in IDP "IL"
And I am assigned the Educator role in my IDP
And I teach the student "Marvin Miller"
When I make an API call to get the specific attendance document "Marvin Miller Attendance events"
Then I should receive a JSON object of the attendance event

Scenario: Unauthorized user accessing a specific attendance event of a student
Given I am user "linda.kim" in IDP "IL"
And I am assigned the Educator role in my IDP
And I do not teach the student "Delilah D. Sims"
When I make an API call to get the specific attendance document "Delilah D. Sims Attendance events"
Then I get a message that I am not authorized
