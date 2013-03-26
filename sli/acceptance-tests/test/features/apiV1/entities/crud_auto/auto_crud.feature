@smoke @RALLY_US209 @RALLY_DE87
Feature: As an SLI application, I want to be able to perform CRUD operations on various resources
This means I want to be able to perform CRUD on all entities.
and verify that the correct links are made available.

Background: Nothing yet
    Given I am logged in using "rrogers" "rrogers1234" to realm "IL"
      And format "application/vnd.slc+json"

        Scenario: CRUD operations on an entity
        Then I perform CRUD for each resource available



        Scenario: CRUD operations on invalid entities
        #Read invalid
        Then I navigate to GET with invalid id for each resource available
        #Update Invalid
        Then I navigate to PUT with invalid id for each resource available
        #Delete Invalid
        Then I navigate to DELETE with invalid id for each resource available

    Scenario: Get All Entities as State Staff
    Given my contextual access is defined by table:
    |Context                | Ids                                |
    |schools                |b1bd3db6-d020-4651-b1b8-a8dba688d9e1|
    |educationOrganizations |b1bd3db6-d020-4651-b1b8-a8dba688d9e1|
    |staff                  |85585b27-5368-4f10-a331-3abcaf3a3f4c|
    Given the expected rewrite results are defined by table:
    | Entity Type                              | Entity Resource URI                      | Count | Rewrite URI|
    | assessment                              | assessments                              | 18    |/search/assessments|
    | attendance                              | attendances                              | 0     |/schools/@ids/studentSchoolAssociations/students/attendances|
    | cohort                                  | cohorts                                  | 2     |/staff/@ids/staffCohortAssociations/cohorts|
    | course                                  | courses                                  | 92    |/search/courses| 
    | disciplineAction                        | disciplineActions                        | 2     |/staff/@ids/disciplineActions|
    | disciplineIncident                      | disciplineIncidents                      | 0     |/staff/@ids/disciplineIncidents|
    | educationOrganization                   | educationOrganizations                   | 1     |/staff/@ids/staffEducationOrgAssignmentAssociations/educationOrganizations|
    | gradebookEntry                          | gradebookEntries                         | 0     |/schools/@ids/sections/gradebookEntries|
    | learningObjective                       | learningObjectives                       | 5     |/search/learningObjectives|
    | learningStandard                        | learningStandards                        | 14    |/search/learningStandards|
    | parent                                  | parents                                  | 0     |/schools/@ids/studentSchoolAssociations/students/studentParentAssociations/parents|
    | program                                 | programs                                 | 2     |/staff/@ids/staffProgramAssociations/programs|
    | studentProgramAssociation               | studentProgramAssociations               | 6    |/staff/@ids/staffProgramAssociations/programs/studentProgramAssociations|
    | courseTranscript                        | courseTranscripts                        | 0     |/schools/@ids/studentSchoolAssociations/students/studentAcademicRecords/courseTranscripts|
    | staffEducationOrganizationAssociation   | staffEducationOrgAssignmentAssociations  | 1     |/staff/@ids/staffEducationOrgAssignmentAssociations|
    | studentCohortAssociation                | studentCohortAssociations                | 4     |/staff/@ids/staffCohortAssociations/cohorts/studentCohortAssociations|
    | teacherSectionAssociation               | teacherSectionAssociations               | 0     |/schools/@ids/teacherSchoolAssociations/teachers/teacherSectionAssociations|
    | studentSchoolAssociation                | studentSchoolAssociations                | 0     |/schools/@ids/studentSchoolAssociations|
    | teacherSchoolAssociation                | teacherSchoolAssociations                | 0     |/schools/@ids/teacherSchoolAssociations|
    | studentSectionAssociation               | studentSectionAssociations               | 0     |/schools/@ids/sections/studentSectionAssociations|
    | staffCohortAssociation                  | staffCohortAssociations                  | 2     |/staff/@ids/staffCohortAssociations|
    | studentAssessment                       | studentAssessments                       | 0     |/schools/@ids/studentSchoolAssociations/students/studentAssessments|
    | competencyLevelDescriptor               | competencyLevelDescriptor                | 2     |/search/competencyLevelDescriptor|
    | staffProgramAssociation                 | staffProgramAssociations                 | 3     |/staff/@ids/staffProgramAssociations|
    | studentDisciplineIncidentAssociation    | studentDisciplineIncidentAssociations    | 0     |/staff/@ids/disciplineIncidents/studentDisciplineIncidentAssociations|
    | studentParentAssociation                | studentParentAssociations                | 0     |/schools/@ids/studentSchoolAssociations/students/studentParentAssociations|
    | courseOffering                          | courseOfferings                          | 138    |/search/courseOfferings|
    | graduationPlan                          | graduationPlans                          | 5     |/graduationPlans|
    | school                                  | schools                                  | 0     |/staff/@ids/staffEducationOrgAssignmentAssociations/schools|
    | section                                 | sections                                 | 0     |/schools/@ids/sections|
    | session                                 | sessions                                 | 29    |/search/sessions|
    | staff                                   | staff                                    | 4     |/educationOrganizations/@ids/staffEducationOrgAssignmentAssociations/staff|
    | student                                 | students                                 | 0     |/schools/@ids/studentSchoolAssociations/students|
    | studentAcademicRecord                   | studentAcademicRecords                   | 0     |/schools/@ids/studentSchoolAssociations/students/studentAcademicRecords|
    | studentGradebookEntry                   | studentGradebookEntries                  | 0     |/schools/@ids/studentSchoolAssociations/students/studentGradebookEntries|
    | teacher                                 | teachers                                 | 0     |/schools/@ids/teacherSchoolAssociations/teachers|
    | grade                                   | grades                                   | 0     |/schools/@ids/sections/studentSectionAssociations/grades|
    | studentCompetencie                      | studentCompetencies                      | 0     |/schools/@ids/sections/studentSectionAssociations/studentCompetencies|
    | gradingPeriod                           | gradingPeriods                           | 3     |/search/gradingPeriods|
    | reportCard                              | reportCards                              | 0     |/schools/@ids/studentSchoolAssociations/students/reportCards|
    | studentCompetencyObjective              | studentCompetencyObjectives              | 1     |/search/studentCompetencyObjectives    |    
    Then the staff queries and rewrite rules work

 @Teacher_crud
    Scenario: CRUD operations requiring explicit associations on an entity as an IT Admin Teacher
    Given I am logged in using "cgrayadmin" "cgray1234" to realm "IL"
      And format "application/vnd.slc+json"
      Then I perform POST for each resource available in the order defined by table:
      | Entity Resource                         |
      | educationOrganizations                  |
      | schools                                 |
      | staff                                   |
      | staffEducationOrgAssignmentAssociations |
      | gradingPeriods                           |
      | sessions                                |
      | students                                |
      | courses                                  |
      | courseOfferings                          |
      | sections                                |
      | programs                                |
      | teachers                                |
      | assessments                              |
      | attendances                              |
      | cohorts                                  |
      | disciplineActions                        |
      | disciplineIncidents                      |
      | gradebookEntries                          |
      | learningObjectives                       |
      | learningStandards                        |
      | parents                                  |
      | studentProgramAssociations               |
      | studentCohortAssociations                |
      | studentSchoolAssociations                |
      | teacherSchoolAssociations                |
      | studentSectionAssociations               |
      | staffCohortAssociations                  |
      | studentAssessments                       |
      | competencyLevelDescriptor               |
      | studentDisciplineIncidentAssociations    |
      | studentParentAssociations                |
      | graduationPlans                          |
      | studentAcademicRecords                   |
#      | studentGradebookEntries                   |
      | courseTranscripts                        |
      | grades                                   |
      | studentCompetencies                       |
      | reportCards                              |
#      | studentCompetencyObjectives              |
      And I perform PUT,GET and Natural Key Update for each resource available
      And I perform DELETE for each resource availabel in the order defined by table:
        | Entity Resource                         |
#        | studentCompetencyObjectives              |
        | reportCards                              |
        | studentCompetencies                       |
        | grades                                   |
        | gradingPeriods                           |
        | courseTranscripts                        |
#        | studentGradebookEntries                   |
        | studentAcademicRecords                   |
        | graduationPlans                          |
        | parents                                  |
        | competencyLevelDescriptor               |
        | studentAssessments                       |
        | learningStandards                        |
        | learningObjectives                       |
        | gradebookEntries                          |
        | disciplineActions                        |
        | disciplineIncidents                      |
        | cohorts                                  |
        | attendances                              |
        | assessments                              |
        | teachers                                |
        | programs                                |
        | studentSchoolAssociations                |
        | students                                |
        | courses                                  |
        | staffEducationOrgAssignmentAssociations |
        | staff                                   |
        | teacherSchoolAssociations                |
        | schools                                 |
        | educationOrganizations                 |

    Scenario: Get All Entities as School Teacher
    Given I am logged in using "cgray" "cgray1234" to realm "IL"
     And format "application/vnd.slc+json"
    And my contextual access is defined by table:
    | Context                | Ids                                                                          |
    | schools                | 92d6d5a0-852c-45f4-907a-912752831772,6756e2b9-aba1-4336-80b8-4a5dde3c63fe    |
    | educationOrganizations | 92d6d5a0-852c-45f4-907a-912752831772,6756e2b9-aba1-4336-80b8-4a5dde3c63fe    |
    | staff                  | e9ca4497-e1e5-4fc4-ac7b-24bad1f2998b                                         |
    | teachers               | e9ca4497-e1e5-4fc4-ac7b-24bad1f2998b                                         |
    | sections               | 15ab6363-5509-470c-8b59-4f289c224107_id,47b5adbf-6fd0-4f07-ba5e-39612da2e234_id |
    Given the expected rewrite results are defined by table:
    | Entity Type                              | Entity Resource URI                      | Count | Rewrite URI|
    | assessment                              | assessments                              | 17    |/assessments|
    | attendance                              | attendances                              | 4     |/sections/@ids/studentSectionAssociations/students/attendances|
    | cohort                                  | cohorts                                  | 1     |/staff/@ids/staffCohortAssociations/cohorts|
    | course                                  | courses                                  | 26    |/schools/@ids/courses|
    | disciplineAction                        | disciplineActions                        | 0     |/staff/@ids/disciplineActions|
    | disciplineIncident                      | disciplineIncidents                      | 0     |/staff/@ids/disciplineIncidents|
    | school                                  | educationOrganizations                   | 2     |/teachers/@ids/teacherSchoolAssociations/schools|
    | gradebookEntry                          | gradebookEntries                         | 1     |/sections/@ids/gradebookEntries|
    | learningObjective                       | learningObjectives                       | 5     |/learningObjectives|
    | learningStandard                        | learningStandards                        | 15    |/learningStandards|
    | parent                                  | parents                                  | 2     |/sections/@ids/studentSectionAssociations/students/studentParentAssociations/parents|
    | program                                 | programs                                 | 0     |/staff/@ids/staffProgramAssociations/programs|
    | studentProgramAssociation               | studentProgramAssociations               | 0     |/staff/@ids/staffProgramAssociations/programs/studentProgramAssociations    |
    | courseTranscript                        | courseTranscripts                        | 2     |/sections/@ids/studentSectionAssociations/students/courseTranscripts    |
    | staffEducationOrganizationAssociation   | staffEducationOrgAssignmentAssociations  | 3     |/educationOrganizations/@ids/staffEducationOrgAssignmentAssociations    |
    | studentCohortAssociation                | studentCohortAssociations                | 1     |/staff/@ids/staffCohortAssociations/cohorts/studentCohortAssociations    |
    | teacherSectionAssociation               | teacherSectionAssociations               | 2     |/teachers/@ids/teacherSectionAssociations    |
    | studentSchoolAssociation                | studentSchoolAssociations                | 59    |/sections/@ids/studentSectionAssociations/students/studentSchoolAssociations    |
    | teacherSchoolAssociation                | teacherSchoolAssociations                | 2     |/teachers/@ids/teacherSchoolAssociations    |
    | studentSectionAssociation               | studentSectionAssociations               | 25    |/sections/@ids/studentSectionAssociations    |
    | staffCohortAssociation                  | staffCohortAssociations                  | 1     |/staff/@ids/staffCohortAssociations    |
    | studentAssessment                       | studentAssessments                       | 1     |/sections/@ids/studentSectionAssociations/students/studentAssessments    |
    | competencyLevelDescriptor               | competencyLevelDescriptor                | 0     |/competencyLevelDescriptor    |
    | staffProgramAssociation                 | staffProgramAssociations                 | 0     |/staff/@ids/staffProgramAssociations    |
    | studentDisciplineIncidentAssociation    | studentDisciplineIncidentAssociations    | 0     |/staff/@ids/disciplineIncidents/studentDisciplineIncidentAssociations    |
    | studentParentAssociation                | studentParentAssociations                | 2     |/sections/@ids/studentSectionAssociations/students/studentParentAssociations    |
    | courseOffering                          | courseOfferings                          | 26    |/schools/@ids/courseOfferings    |
    | graduationPlan                          | graduationPlans                          | 5     |/graduationPlans    |
    | school                                  | schools                                  | 2     |/teachers/@ids/teacherSchoolAssociations/schools|
    | section                                 | sections                                 | 2     |/teachers/@ids/teacherSectionAssociations/sections|
    | session                                 | sessions                                 | 6     |/educationOrganizations/@ids/sessions|
    | staff                                   | staff                                    | 3     |/educationOrganizations/@ids/staffEducationOrgAssignmentAssociations/staff|
    | student                                 | students                                 | 25    |/sections/@ids/studentSectionAssociations/students|
    | studentAcademicRecord                   | studentAcademicRecords                   | 3     |/sections/@ids/studentSectionAssociations/students/studentAcademicRecords|
    | studentGradebookEntry                   | studentGradebookEntries                  | 2     |/sections/@ids/studentSectionAssociations/students/studentGradebookEntries|
    | teacher                                 | teachers                                 | 3     |/schools/@ids/teacherSchoolAssociations/teachers|
    | grade                                   | grades                                   | 1     |/sections/@ids/studentSectionAssociations/grades|
    | studentCompetency                       | studentCompetencies                      | 2     |/sections/@ids/studentSectionAssociations/studentCompetencies|
    | gradingPeriod                           | gradingPeriods                           | 2     |/schools/@ids/sessions/gradingPeriods|
    | reportCard                              | reportCards                              | 3     |/sections/@ids/studentSectionAssociations/students/reportCards|
    | studentCompetencyObjective              | studentCompetencyObjectives              | 0     |/educationOrganizations/@ids/studentCompetencyObjectives    |




