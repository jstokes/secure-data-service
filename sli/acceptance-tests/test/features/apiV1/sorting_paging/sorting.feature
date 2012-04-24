Feature: Sort and page API results

Background: 
	Given I am logged in using "demo" "demo1234" to realm "SLI"
	

Scenario: Check default limit of 50
  Given format "application/json"
  When I navigate to GET "/v1/students"
  Then I should receive a collection with 50 elements

Scenario: Sorting a collection of student school association links by entryGradeLevel, ascending
	Given format "application/json"
		And parameter "sortBy" is "entryGradeLevel"
		And parameter "sortOrder" is "ascending"
	When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations"
	Then I should receive a collection
		And the link at index 0 should point to an entity with id "094b9681-4fbc-4d59-a05d-87f6f5d0b759"
		And the link at index 1 should point to an entity with id "7a238370-415e-4fc0-9c5e-565cb8394d13"
		And the link at index 2 should point to an entity with id "4e044247-4cc0-49fa-900d-80064614060c"


Scenario: Sorting a collection of entities obtained via a hop using a (nested) field, descending
    Given format "application/json"
    And parameter "sortBy" is "name.firstName"
    And parameter "sortOrder" is "descending"
    When I navigate to GET "/v1/educationOrganizations/<'Gotham City School District ed-org' ID>/staffEducationOrgAssignmentAssociations/staff"
    Then I should receive a return code of 200
    And I should receive a collection
    And the link at index 0 should have "name.firstName" equal to "Sample"
    And the link at index 1 should have "name.firstName" equal to "Johnny"

Scenario: Sorting a collection of full student school association entities
	Given format "application/json"
		And parameter "sortBy" is "entryGradeLevel"
		And parameter "sortOrder" is "descending"
	When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations"
	Then I should receive a collection
		And the link at index 0 should have "entryGradeLevel" equal to "Third grade"
		And the link at index 1 should have "entryGradeLevel" equal to "Sixth grade"
		And the link at index 2 should have "entryGradeLevel" equal to "Second grade"

Scenario: Validate PII cannot be sorted against
    Given format "application/json"
    And parameter "sortBy" is "name.firstName"
    And parameter "sortOrder" is "ascending"
    When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations/students"
    Then I should receive a return code of 400

Scenario: Sorting a collection of staff through a hop (from an ed-org)
    Given format "application/json"
    And parameter "sortBy" is "name.firstName"
    And parameter "sortOrder" is "ascending"
    When I navigate to GET "/v1/educationOrganizations/<'Gotham City School District ed-org' ID>/staffEducationOrgAssignmentAssociations/staff"
    Then I should receive a return code of 200
    And I should receive a collection
    And the link at index 0 should have "name.firstName" equal to "Jane"
    And the link at index 1 should have "name.firstName" equal to "John"

Scenario: Paging request the first two results from an API request
    Given format "application/json"
		And parameter "sortBy" is "entryGradeLevel"
		And parameter "sortOrder" is "ascending"
	When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations"
	Then I should receive a collection with 3 elements
 	Given parameter "offset" is "0"
		And parameter "limit" is "2"
	When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations"
	Then I should receive a collection with 2 elements
		And the link at index 0 should point to an entity with id "094b9681-4fbc-4d59-a05d-87f6f5d0b759"
		And the link at index 1 should point to an entity with id "7a238370-415e-4fc0-9c5e-565cb8394d13"
		And the header "TotalCount" equals 3
		And the a next link exists with offset equal to 2 and limit equal to 2
		And the a previous link should not exist

Scenario: Paging request the first two results from an API request via a hop
    Given format "application/json"
		And parameter "sortBy" is "name.firstName"
		And parameter "sortOrder" is "ascending"
	When I navigate to GET "/v1/educationOrganizations/<'Gotham City School District ed-org' ID>/staffEducationOrgAssignmentAssociations/staff"
	Then I should receive a collection with 4 elements
 	Given parameter "offset" is "0"
		And parameter "limit" is "1"
	When I navigate to GET "/v1/educationOrganizations/<'Gotham City School District ed-org' ID>/staffEducationOrgAssignmentAssociations/staff"
	Then I should receive a collection with 1 elements
		And the link at index 0 should point to an entity with id "f0e41d87-92d4-4850-9262-ed2f2723159b"
		And the header "TotalCount" equals 4
		And the a next link exists with offset equal to 1 and limit equal to 1
		And the a previous link should not exist

Scenario: Request the last and middle page of results from a API request
    Given format "application/json"
		And parameter "sortBy" is "entryGradeLevel"
		And parameter "sortOrder" is "ascending"
		And parameter "offset" is "1"
		And parameter "limit" is "2"
	When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations"
	Then I should receive a collection with 2 elements
		And the link at index 0 should point to an entity with id "7a238370-415e-4fc0-9c5e-565cb8394d13"
		And the link at index 1 should point to an entity with id "4e044247-4cc0-49fa-900d-80064614060c"
		And the header "TotalCount" equals 3
		And the a previous link exists with offset equal to 0 and limit equal to 2
		And the a next link should not exist
	Given parameter "offset" is "1"
		And parameter "limit" is "1"
	When I navigate to GET "/v1/schools/<'Krypton Middle School' ID>/studentSchoolAssociations"
	Then I should receive a collection with 1 elements
			And the link at index 0 should point to an entity with id "7a238370-415e-4fc0-9c5e-565cb8394d13"
			And the header "TotalCount" equals 3
			And the a previous link exists with offset equal to 0 and limit equal to 1
			And the a next link exists with offset equal to 2 and limit equal to 1
            
Scenario Outline: Confirm ability to use different operators with numbers
    Given format "application/json"
      And parameter "sequenceOfCourse" <operator> "2"
     When I navigate to GET "/v1/sections"
     Then I should receive a return code of 200
      And I should receive a collection with <entities returned> elements
    Examples:
        | operator  | entities returned |
        | "<="      | 25                |
        | ">"       | 10                |
        | "<"       | 23                |
        | ">="      | 12                |
        | "!="      | 33                |
        | "="       | 2                 |
