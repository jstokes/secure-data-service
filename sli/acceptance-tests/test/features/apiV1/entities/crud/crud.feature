@smoke @RALLY_US209 @RALLY_DE87
Feature: As an SLI application, I want to be able to perform CRUD operations on various resources
This means I want to be able to perform CRUD on all entities.
and verify that the correct links are made available.

Background: Nothing yet
    Given I am logged in using "rrogers" "rrogers1234" to realm "IL"
      And format "application/vnd.slc+json"


        Scenario Outline: CRUD operations on an entity
       Given entity URI <Entity Resource URI>
        # Create
       Given a valid entity json document for a <Entity Type>
        When I navigate to POST "/<ENTITY URI>"
        Then I should receive a return code of 201
         And I should receive a new entity URI
        # Read
        When I navigate to GET "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        Then I should receive a return code of 200
         And the response should contain the appropriate fields and values
         And "entityType" should be <Entity Type>
         And I should receive a link named "self" with URI "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        # Update
        When I set the <Update Field> to <Updated Value>
         And I navigate to PUT "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        Then I should receive a return code of 204
         And I navigate to GET "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
         And <Update Field> should be <Updated Value>
        # Delete
        When I navigate to DELETE "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        Then I should receive a return code of 204
         And I navigate to GET "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
         And I should receive a return code of 404

Examples:
| Entity Type                    | Entity Resource URI       | Update Field             | Updated Value                                |
| "assessment"                   | "assessments"             | "assessmentTitle"        | "Advanced Placement Test - Subject: Writing" |
| "attendance"                   | "attendances"             | "studentId"              | "274f4c71-1984-4607-8c6f-0a91db2d240a"       |
| "cohort"                       | "cohorts"                 | "cohortDescription"      | "frisbee golf team"                          |
| "course"                       | "courses"                 | "courseDescription"      | "Advanced Linguistic Studies"                |
| "disciplineAction"             | "disciplineActions"       | "disciplineDate"         | "2012-03-18"                                 |
| "disciplineIncident"           | "disciplineIncidents"     | "incidentTime"           | "01:02:15"                                   |
| "educationOrganization"        | "educationOrganizations"  | "nameOfInstitution"      | "Bananas School District"                    |
| "gradebookEntry"               | "gradebookEntries"        | "gradebookEntryType"     | "Homework"                                   |
| "learningObjective"            | "learningObjectives"      | "description"            | "Mathematics Objective"                      |
| "learningStandard"             | "learningStandards"       | "gradeLevel"             | "Ninth grade"                                |
| "parent"                       | "parents"                 | "parentUniqueStateId"    | "ParentID102"                                |
| "program"                      | "programs"                | "programSponsor"         | "State Education Agency"                     |
| "school"                       | "schools"                 | "nameOfInstitution"      | "Yellow Middle School"                       |
| "section"                      | "sections"                | "sequenceOfCourse"       | "2"                                          |
| "session"                      | "sessions"                | "totalInstructionalDays" | "43"                                         |
| "staff"                        | "staff"                   | "sex"                    | "Female"                                     |
| "student"                      | "students"                | "sex"                    | "Female"                                     |
| "studentAcademicRecord"        | "studentAcademicRecords"  | "sessionId"              | "abcff7ae-1f01-46bc-8cc7-cf409819bbce"       |
| "studentGradebookEntry"        | "studentGradebookEntries" | "diagnosticStatement"    | "Finished the quiz in 5 hours"               |
| "teacher"                      | "teachers"                | "highlyQualifiedTeacher" | "false"                                      |
| "grade"                        | "grades"                  | "gradeType"              | "Mid-Term Grade"                             |
| "studentCompetency"            | "studentCompetencies"     | "diagnosticStatement"    | "advanced nuclear thermodynamics"            |
| "reportCard"                   | "reportCards"             | "numberOfDaysAbsent"     | "17"                                         |


        Scenario Outline: CRUD operations on invalid entities
    Given entity URI <Entity Resource URI>
    #Read invalid
     When I navigate to GET "/<ENTITY URI>/<INVALID REFERENCE>"
     Then I should receive a return code of 404
    #Update Invalid
    Given a valid entity json document for a <Entity Type>
     When I set the <Update Field> to <Updated Value>
     When I navigate to PUT "/<ENTITY URI>/<INVALID REFERENCE>"
     Then I should receive a return code of 404
    #Delete Invalid
     When I navigate to DELETE "/<ENTITY URI>/<INVALID REFERENCE>"
     Then I should receive a return code of 404

Examples:
| Entity Type                    | Entity Resource URI       | Update Field             | Updated Value                                |
| "assessment"                   | "assessments"             | "assessmentTitle"        | "Advanced Placement Test - Subject: Writing" |
| "attendance"                   | "attendances"             | "schoolYearAttendance"   | "[]"                                         |
| "cohort"                       | "cohorts"                 | "cohortDescription"      | "frisbee golf team"                          |
| "course"                       | "courses"                 | "courseDescription"      | "Advanced Linguistic Studies"                |
| "disciplineAction"             | "disciplineActions"       | "disciplineDate"         | "2012-03-18"                                 |
| "disciplineIncident"           | "disciplineIncidents"     | "incidentTime"           | "01:02:15"                                   |
| "educationOrganization"        | "educationOrganizations"  | "nameOfInstitution"      | "Bananas School District"                    |
| "gradebookEntry"               | "gradebookEntries"        | "gradebookEntryType"     | "Homework"                                   |
| "learningObjective"            | "learningObjectives"      | "academicSubject"        | "Mathematics"                                |
| "learningStandard"             | "learningStandards"       | "gradeLevel"             | "Ninth grade"                                |
| "parent"                       | "parents"                 | "parentUniqueStateId"    | "ParentID102"                                |
| "program"                      | "programs"                | "programSponsor"         | "State Education Agency"                     |
| "school"                       | "schools"                 | "nameOfInstitution"      | "Yellow Middle School"                       |
| "section"                      | "sections"                | "sequenceOfCourse"       | "2"                                          |
| "session"                      | "sessions"                | "totalInstructionalDays" | "43"                                         |
| "staff"                        | "staff"                   | "sex"                    | "Female"                                     |
| "student"                      | "students"                | "sex"                    | "Female"                                     |
| "studentAcademicRecord"        | "studentAcademicRecords"  | "sessionId"              | "67ce204b-9999-4a11-aacb-000000000003"       |
| "studentGradebookEntry"        | "studentGradebookEntries" | "diagnosticStatement"    | "Finished the quiz in 5 hours"               |
| "teacher"                      | "teachers"                | "highlyQualifiedTeacher" | "false"                                      |
| "grade"                        | "grades"                  | "gradeType"              | "Mid-Term Grade"                             |
| "studentCompetency"            | "studentCompetencies"     | "diagnosticStatement"    | "advanced nuclear thermodynamics"            |
| "gradingPeriod"                | "gradingPeriods"          | "endDate"                | "2015-10-15"                                 |
| "reportCard"                   | "reportCards"             | "numberOfDaysAbsent"     | "17"                                         |

    Scenario Outline: Get All Entities as State Staff
    Given entity URI <Entity Resource URI>
    Given parameter "limit" is "0"
     When I navigate to GET "/<ENTITY URI>"
     Then I should receive a return code of <Code>
      And I should receive a collection of "<Entity Count>" entities
      And each entity's "entityType" should be <Entity Type>

Examples:
| Entity Type                    | Entity Resource URI       | Code | Entity Count |
| "assessment"                   | "assessments"             |  200 | 17 |
| "attendance"                   | "attendances"             |  200 | 0 |
| "cohort"                       | "cohorts"                 |  200 | 3 |
| "course"                       | "courses"                 |  200 | 0 |
| "disciplineAction"             | "disciplineActions"       |  200 | 2 |
| "disciplineIncident"           | "disciplineIncidents"     |  200 | 0 |
| "educationOrganization"        | "educationOrganizations"  |  200 | 1 |
| "gradebookEntry"               | "gradebookEntries"        |  200 | 0 |
| "learningObjective"            | "learningObjectives"      |  200 | 5 |
| "learningStandard"             | "learningStandards"       |  200 | 14 |
| "parent"                       | "parents"                 |  200 | 0 |
| "program"                      | "programs"                |  200 | 2 |
| "school"                       | "schools"                 |  200 | 27 |
| "section"                      | "sections"                |  200 | 0 |
| "session"                      | "sessions"                |  200 | 0 |
| "staff"                        | "staff"                   |  200 | 3 |
| "student"                      | "students"                |  200 | 0 |
| "studentAcademicRecord"        | "studentAcademicRecords"  |  200 | 0 |
| "studentGradebookEntry"        | "studentGradebookEntries" |  200 | 0 |
| "teacher"                      | "teachers"                |  200 | 0 |
| "grade"                        | "grades"                  |  200 | 0 |
| "studentCompetency"            | "studentCompetencies"     |  200 | 0 |
| "gradingPeriod"                | "gradingPeriods"          |  200 | 0 |
| "reportCard"                   | "reportCards"             |  200 | 0 |

    Scenario Outline: CRUD operations on an entity as an IT Admin Teacher
    Given I am logged in using "cgrayadmin" "cgray1234" to realm "IL"
      And format "application/vnd.slc+json"
       Given entity URI <Entity Resource URI>
        # Create
       Given a valid entity json document for a <Entity Type>
        When I navigate to POST "/<ENTITY URI>"
        Then I should receive a return code of 201
         And I should receive a new entity URI
        # Read
        When I navigate to GET "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        Then I should receive a return code of 200
         And the response should contain the appropriate fields and values
         And "entityType" should be <Entity Type>
         And I should receive a link named "self" with URI "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        # Update
        When I set the <Update Field> to <Updated Value>
         And I navigate to PUT "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        Then I should receive a return code of 204
         And I navigate to GET "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
         And <Update Field> should be <Updated Value>
        # Delete
        When I navigate to DELETE "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
        Then I should receive a return code of 204
         And I navigate to GET "/<ENTITY URI>/<NEWLY CREATED ENTITY ID>"
         And I should receive a return code of 404

Examples:
| Entity Type                    | Entity Resource URI       | Update Field             | Updated Value                                |
| "assessment"                   | "assessments"             | "assessmentTitle"        | "Advanced Placement Test - Subject: Writing" |
| "attendance"                   | "attendances"             | "studentId"              | "2fab099f-47d5-4099-addf-69120db3b53b"       |
| "cohort"                       | "cohorts"                 | "cohortDescription"      | "frisbee golf team"                          |
| "course"                       | "courses"                 | "courseDescription"      | "Advanced Linguistic Studies"                |
| "disciplineAction"             | "disciplineActions"       | "disciplineDate"         | "2012-03-18"                                 |
| "disciplineIncident"           | "disciplineIncidents"     | "incidentTime"           | "01:02:15"                                   |
| "educationOrganization"        | "educationOrganizations"  | "nameOfInstitution"      | "Bananas School District"                    |
| "gradebookEntry"               | "gradebookEntries"        | "gradebookEntryType"     | "Homework"                                   |
| "learningObjective"            | "learningObjectives"      | "academicSubject"        | "Mathematics"                                |
| "learningStandard"             | "learningStandards"       | "gradeLevel"             | "Ninth grade"                                |
| "parent"                       | "parents"                 | "parentUniqueStateId"    | "ParentID102"                                |
| "program"                      | "programs"                | "programSponsor"         | "State Education Agency"                     |
| "school"                       | "schools"                 | "nameOfInstitution"      | "Yellow Middle School"                       |
| "section"                      | "sections"                | "sequenceOfCourse"       | "2"                                          |
| "session"                      | "sessions"                | "totalInstructionalDays" | "43"                                         |
| "staff"                        | "staff"                   | "sex"                    | "Female"                                     |
| "student"                      | "students"                | "sex"                    | "Female"                                     |
| "studentAcademicRecord"        | "studentAcademicRecords"  | "sessionId"              | "abcff7ae-1f01-46bc-8cc7-cf409819bbce"       |
| "studentGradebookEntry" | "studentGradebookEntries" | "diagnosticStatement"    | "Finished the quiz in 5 hours"               |
| "teacher"                      | "teachers"                | "highlyQualifiedTeacher" | "false"                                      |
| "grade"                        | "grades"                  | "gradeType"              | "Mid-Term Grade"                             |
| "studentCompetency"            | "studentCompetencies"     | "diagnosticStatement"    | "advanced nuclear thermodynamics"            |
| "reportCard"                   | "reportCards"             | "numberOfDaysAbsent"     | "17"                                         |

    Scenario Outline: Get All Entities as School Teacher
    
    Given I am logged in using "linda.kim" "linda.kim1234" to realm "IL"
     And format "application/vnd.slc+json"
    Given entity URI <Entity Resource URI>
    Given parameter "limit" is "0"
     When I navigate to GET "/<ENTITY URI>"
     Then I should receive a return code of 200
      And I should receive a collection of "<Entity Count>" entities
      And each entity's "entityType" should be <Entity Type>

Examples:
| Entity Type                    | Entity Resource URI       | Entity Count |
| "assessment"                   | "assessments"             | 17 |
| "attendance"                   | "attendances"             | 1 |
| "cohort"                       | "cohorts"                 | 0 |
| "course"                       | "courses"                 | 10|
| "disciplineAction"             | "disciplineActions"       | 0 |
| "disciplineIncident"           | "disciplineIncidents"     | 0 |
| "school"                       | "educationOrganizations"  | 1 |
| "gradebookEntry"               | "gradebookEntries"        | 3 |
| "learningObjective"            | "learningObjectives"      | 5 |
| "learningStandard"             | "learningStandards"       | 14 |
| "parent"                       | "parents"                 | 2 |
| "program"                      | "programs"                | 2 |
| "school"                       | "schools"                 | 27 |
| "section"                      | "sections"                | 4 |
| "session"                      | "sessions"                | 0 |
| "staff"                        | "staff"                   | 1 |
| "student"                      | "students"                | 31 |
| "studentAcademicRecord"        | "studentAcademicRecords"  | 1 |
| "studentGradebookEntry"        | "studentGradebookEntries" | 4 |
| "teacher"                      | "teachers"                | 1 |
| "grade"                        | "grades"                  | 0 |
| "studentCompetency"            | "studentCompetencies"     | 0 |
| "gradingPeriod"                | "gradingPeriods"          | 0 |
| "reportCard"                   | "reportCards"             | 1 |
