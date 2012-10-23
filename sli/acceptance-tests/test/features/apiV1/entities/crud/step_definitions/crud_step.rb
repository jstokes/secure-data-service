=begin

Copyright 2012 Shared Learning Collaborative, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end


require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
require 'uri'
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../../utils/api_utils.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|

  #general
  id = @entityUri                               if human_readable_id == "ENTITY URI"
  id = @newId                                   if human_readable_id == "NEWLY CREATED ENTITY ID"
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"

  #return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################


Given /^entity URI "([^"]*)"$/ do |arg1|
  @entityUri = arg1
end


Given /^a valid entity json document for a "([^"]*)"$/ do |arg1|
@entityData = {
  "gradingPeriod" => {
    "gradingPeriodIdentity" => {
      "educationalOrgIdentity" => [{
        "stateOrganizationId" => "Daybreak Elementary School",
      }],
      "stateOrganizationId" => "Daybreak Elementary School",
      "gradingPeriod" => "First Six Weeks",
      "schoolYear" => "2011-2012"
    },
    "beginDate" => "2012-07-01",
    "endDate" => "2012-07-31",
    "totalInstructionalDays" => 20
  },
  "userAccount" => {
    "userName" => "bob3@bob.com",
    "firstName" => "Bob",
    "lastName" => "Roberts",
    "validated" => false,
    "environment" => "Sandbox"
  },

  "attendance" => {
    "entityType" => "attendance",
    "studentId" => "0c2756fd-6a30-4010-af79-488d6ef2735a",
    "schoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe",
    "schoolYearAttendance" => [{
      "schoolYear" => "2011-2012",
      "attendanceEvent" => [{
        "date" => "2011-09-16",
        "event" => "Tardy"
      }]
    }]
  },
  "studentAcademicRecord" => {
    "studentId" => "61161008-2560-480d-aadf-4b0264dc2ae3",
    "sessionId" => "d23ebfc4-5192-4e6c-a52b-81cee2319072"
  },
  "student" => {
    "birthData" => {
      "birthDate" => "1994-04-04"
    },
    "sex" => "Male",
    "studentUniqueStateId" => "123456",
    "economicDisadvantaged" => false,
    "name" => {
      "firstName" => "Mister",
      "middleName" => "John",
      "lastSurname" => "Doe"
    }
  },
  "cohort" => {
    "cohortIdentifier" => "ACC-TEST-COH-4",
    "cohortDescription" => "ultimate frisbee team",
    "cohortType" => "Extracurricular Activity",
    "cohortScope" => "Statewide",
    "academicSubject" => "Physical, Health, and Safety Education",
    "educationOrgId" => "92d6d5a0-852c-45f4-907a-912752831772",
    "programId" => ["9b8cafdc-8fd5-11e1-86ec-0021701f543f"]
  },
  "course" => {
    "courseTitle" => "Chinese 1",
    "numberOfParts" => 1,
    "courseCode" => [{
      "ID" => "C1",
      "identificationSystem" => "School course code",
      "assigningOrganizationCode" => "Bob's Code Generator"
    }],
    "courseLevel" => "Basic or remedial",
    "courseLevelCharacteristics" => ["Advanced Placement"],
    "gradesOffered" => ["Eighth grade"],
    "subjectArea" => "Foreign Language and Literature",
    "courseDescription" => "Intro to Chinese",
    "dateCourseAdopted" => "2001-01-01",
    "highSchoolCourseRequirement" => false,
    "courseDefinedBy" => "LEA",
    "minimumAvailableCredit" => {
      "credit" => 1.0
    },
    "maximumAvailableCredit" => {
      "credit" => 1.0
    },
    "careerPathway" => "Hospitality and Tourism",
    "schoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe",
    "uniqueCourseId" => "Chinese-1-10"
  },
  "courseOffering" => {
    "schoolId" => "67ce204b-9999-4a11-aaab-000000000008",
    "localCourseCode" => "LCCMA1",
    "sessionId" => "67ce204b-9999-4a11-aacb-000000000002",
    "localCourseTitle" => "Math 1 - Intro to Mathematics",
    "courseId" => "67ce204b-9999-4a11-aacc-000000000004"
  },
  "disciplineAction" => {
    "disciplineActionIdentifier" => "Discipline act XXX",
    "disciplines" => [[
        {"codeValue" => "Discp Act 3"},
        {"shortDescription" => "Disciplinary Action 3"},
        {"description" => "Long disciplinary Action 3"}
    ]],
    "disciplineDate" => "2012-01-28",
    "disciplineIncidentId" => ["0e26de79-7efa-5e67-9201-5113ad50a03b"],
    "studentId" => ["61161008-2560-480d-aadf-4b0264dc2ae3"],
    "responsibilitySchoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe",
    "assignmentSchoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe"
  },
  "disciplineIncident" => {
    "incidentIdentifier" => "Incident ID XXX",
    "incidentDate" => "2012-02-14",
    "incidentTime" => "01:00:00",
    "incidentLocation" => "On School",
    "behaviors" => [[
        {"shortDescription" => "Behavior 012 description"},
        {"codeValue" => "BEHAVIOR 012"}
    ]],
    "schoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe"
  },
  "educationOrganization" => {
    "organizationCategories" => ["State Education Agency"],
    "stateOrganizationId" => "SomeUniqueSchoolDistrict-2422883",
    "nameOfInstitution" => "Gotham City School District",
    "address" => [
              "streetNumberName" => "111 Ave C",
              "city" => "Chicago",
              "stateAbbreviation" => "IL",
              "postalCode" => "10098",
              "nameOfCounty" => "Wake"
              ]
  },
  "gradebookEntry" => {
    "gradebookEntryType" => "Quiz",
    "dateAssigned" => "2012-02-14",
    "sectionId" => "1d345e41-f1c7-41b2-9cc4-9898c82faeda"
  },
  "learningObjective" => {
    "academicSubject" => "Mathematics",
    "objective" => "Learn Mathematics",
    "objectiveGradeLevel" => "Fifth grade"
  },
  "learningStandard" => {
    "learningStandardId" => {
     "identificationCode" => "apiTestLearningStandard"},
    "description" => "a description",
    "gradeLevel" => "Ninth grade",
    "contentStandard"=>"State Standard",
    "subjectArea" => "English"
  },
  "program" => {
    "programId" => "ACC-TEST-PROG-3",
    "programType" => "Remedial Education",
    "programSponsor" => "Local Education Agency",
    "services" => [[
        {"codeValue" => "codeValue3"},
        {"shortDescription" => "Short description for acceptance test program 3"},
        {"description" => "This is a longer description of the services provided by acceptance test program 3. More detail could be provided here."}]]
  },
  "section" => {
    "uniqueSectionCode" => "SpanishB09",
    "sequenceOfCourse" => 1,
    "educationalEnvironment" => "Off-school center",
    "mediumOfInstruction" => "Independent study",
    "populationServed" => "Regular Students",
    "schoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe",
    "sessionId" => "d23ebfc4-5192-4e6c-a52b-81cee2319072",
    "courseOfferingId" => "00291269-33e0-415e-a0a4-833f0ef38189",
    "assessmentReferences" => ["29f044bd-1449-4fb7-8e9a-5e2cf9ad252a"]
  },
  "session" => {
    "sessionName" => "Spring 2012",
    "schoolYear" => "2011-2012",
    "term" => "Spring Semester",
    "beginDate" => "2012-01-01",
    "endDate" => "2012-06-31",
    "totalInstructionalDays" => 80,
    "gradingPeriodReference" => ["b40a7eb5-dd74-4666-a5b9-5c3f4425f130"],
    "schoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe"
  },
  "staff" => {
    "staffUniqueStateId" => "EMPLOYEE123456789",
    "sex" => "Male",
    "hispanicLatinoEthnicity" => false,
    "highestLevelOfEducationCompleted" => "Bachelor's",
    "name" => {
      "firstName" => "Teaches",
      "middleName" => "D.",
      "lastSurname" => "Students"
    }
  },
  "studentGradebookEntry" => {
    "gradebookEntryId" => "20120613-56b6-4d17-847b-2997b7227686",
    "letterGradeEarned" => "A",
    "sectionId" => "1d345e41-f1c7-41b2-9cc4-9898c82faeda",
    "studentId" => "2fab099f-47d5-4099-addf-69120db3b53b",
    "studentSectionAssociationId" => "49b277c3-4639-42c2-88ef-0f59dd5acba2",
    "numericGradeEarned" => 98,
    "dateFulfilled" => "2012-01-31",
    "diagnosticStatement" => "Finished the quiz in 5 minutes"
  },
  "assessment" => {
    "assessmentTitle" => "Writing Advanced Placement Test",
    "assessmentIdentificationCode" => [{
      "identificationSystem" => "School",
      "ID" => "01234B"
    }],
    "academicSubject" => "Mathematics",
    "assessmentCategory" => "Achievement test",
    "gradeLevelAssessed" => "Adult Education",
    "contentStandard" => "LEA Standard",
    "version" => 2
  },
  "parent" => {
    "parentUniqueStateId" => "ParentID101",
    "name" =>
    { "firstName" => "John",
      "lastSurname" => "Doe",
    }
  },
  "school" => {
    "shortNameOfInstitution" => "SCTS",
    "nameOfInstitution" => "School Crud Test School",
    "webSite" => "www.scts.edu",
    "stateOrganizationId" => "SomeUniqueSchool-24242342",
    "organizationCategories" => ["School"],
    "address" => [
      "addressType" => "Physical",
      "streetNumberName" => "123 Main Street",
      "city" => "Lebanon",
      "stateAbbreviation" => "KS",
      "postalCode" => "66952",
      "nameOfCounty" => "Smith County"
    ],
    "gradesOffered" => [
      "Kindergarten",
      "First grade",
      "Second grade",
      "Third grade",
      "Fourth grade",
      "Fifth grade"
    ]
  },
  "teacher" => {
    "birthDate" => "1954-08-31",
    "sex" => "Male",
    "yearsOfPriorTeachingExperience" => 32,
    "staffUniqueStateId" => "12345678",
    "highlyQualifiedTeacher" => true,
    "highestLevelOfEducationCompleted" => "Master's",
    "name" => {
      "firstName" => "Rafe",
      "middleName" => "Hairfire",
      "lastSurname" => "Esquith"
    }
  },
  "grade" => {
    "studentSectionAssociationId" => "00cbf81b-41df-4bda-99ad-a5717d3e81a1",
    "letterGradeEarned" => "B+",
    "gradeType" => "Final"
  },
  "studentCompetency" => {
     "competencyLevel" => [{
       "description" => "really hard competency"
     }],
     "objectiveId" => {
       "learningObjectiveId" => "dd9165f2-65be-6d27-a8ac-bdc5f46757b6"
     },
     "diagnosticStatement" => "passed with flying colors",
     "studentSectionAssociationId" => "00cbf81b-41df-4bda-99ad-a5717d3e81a1"
  },
  "reportCard" => {
      "grades" => ["ef42e2a2-9942-11e1-a8a9-68a86d21d918"],
      "studentCompetencyId" => ["3a2ea9f8-9acf-11e1-add5-68a86d83461b"],
      "gpaGivenGradingPeriod" => 3.14,
      "gpaCumulative" => 2.9,
      "numberOfDaysAbsent" => 15,
      "numberOfDaysInAttendance" => 150,
      "numberOfDaysTardy" => 10,
      "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
      "gradingPeriodId" => "ef72b883-90fa-40fa-afc2-4cb1ae17623b"
  },
  "graduationPlan" => {
       "creditsBySubject" => [{
            "subjectArea" => "English",
            "credits" => {
                "creditConversion" => 0,
                "creditType" => "Semester hour credit",
                "credit" => 6
             }
       }],
       "individualPlan" => false,
       "graduationPlanType" => "Minimum",
       "educationOrganizationId" => "67ce204b-9999-4a11-bfea-000000000009",
       "totalCreditsRequired" => {
            "creditConversion" => 0,
            "creditType" => "Semester hour credit",
            "credit" => 32
       }
    }
}
  @fields = @entityData[arg1]
end

When /^I create an association of type "([^"]*)"$/ do |type|
  @assocData = {
    "studentCohortAssocation" => {
       "cohortId" => @newId,
       "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
       "endDate" => "2020-01-15",
       "beginDate" => "2011-04-01"
    },
    "courseOffering" => {
      "localCourseCode" => "LCC7252GR2",
      "localCourseTitle" => "German 2 - Outro to German",
      "sessionId" => "0410354d-dbcb-0214-250a-404401060c93",
      "courseId" => @newId,
      "schoolId" => "92d6d5a0-852c-45f4-907a-912752831772"
    },
    "section" => {
       "educationalEnvironment" => "Classroom",
       "sessionId" => "0410354d-dbcb-0214-250a-404401060c93",
       "populationServed" => "Regular Students",
       "sequenceOfCourse" => 3,
       "uniqueSectionCode" => "Motorcycle Repair 101",
       "mediumOfInstruction" => "Independent study",
       "programReference" => [],
       "courseOfferingId" => @assocId,
       "schoolId" => "92d6d5a0-852c-45f4-907a-912752831772",
       "availableCredit" => nil
    },
    "studentDisciplineIncidentAssociation" => {
       "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
       "disciplineIncidentId" => @newId,
       "studentParticipationCode" => "Reporter"
    },
    "studentParentAssociation" => {
       "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
       "parentId" => @newId,
       "livesWith" => true,
       "primaryContactStatus" => true,
       "relation" => "Father",
       "contactPriority" => 0,
       "emergencyContactStatus" => true
    },
    "studentProgramAssociation" => {
       "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
       "programId" => @newId,
       "beginDate" => "2011-05-01",
       "educationOrganizationId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe"
    },
    "studentSectionAssociation" => {
      "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
      "sectionId" => @newId,
      "beginDate" => "2012-05-01"
    },
    "staffEducationOrganizationAssociation" => {
      "educationOrganizationReference" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe",
      "staffReference" => @newId,
      "beginDate" => "2000-01-01",
      "positionTitle" => "Hall monitor",
      "staffClassification" => "School Administrative Support Staff"
    },
    "studentSectionAssociation2" => {
      "studentId" => @newId,
      "sectionId" => "15ab6363-5509-470c-8b59-4f289c224107",
      "beginDate" => "2012-05-01"
    },
    "teacherSchoolAssociation" => {
      "schoolId" => "6756e2b9-aba1-4336-80b8-4a5dde3c63fe",
      "programAssignment" => "Regular Education",
      "teacherId" => @newId,
      "instructionalGradeLevels" => ["First grade"],
      "academicSubjects" => ["Composite"]
    }
  }
  @fields = @assocData[type]
end

When /^I POST the association of type "([^"]*)"$/ do |type|
  @assocUrl = {
    "studentCohortAssocation" => "studentCohortAssociations",
    "courseOffering" => "courseOfferings",
    "section" => "sections",
    "studentDisciplineIncidentAssociation" => "studentDisciplineIncidentAssociations",
    "studentParentAssociation" => "studentParentAssociations",
    "studentProgramAssociation" => "studentProgramAssociations",
    "studentSectionAssociation" => "studentSectionAssociations",
    "staffEducationOrganizationAssociation" => "staffEducationOrgAssignmentAssociations",
    "studentSectionAssociation2" => "studentSectionAssociations",
    "teacherSchoolAssociation" => "teacherSchoolAssociations"
  }
  if type != ""
    step "I navigate to POST \"/#{@assocUrl[type]}\""
    headers = @res.raw_headers
    assert(headers != nil, "Headers are nil")
    assert(headers['location'] != nil, "There is no location link from the previous request")
    s = headers['location'][0]
    @assocId = s[s.rindex('/')+1..-1]
  end
end

Then /^I should receive a new entity URI$/ do
  step "I should receive an ID for the newly created entity"
  assert(@newId != nil, "After POST, URI is nil")
end

Then /^the tenant ID of the entity should be "([^"]*)"$/ do |arg1|
  result = JSON.parse(@res.body)
  assert(result != nil, "Result of JSON parsing is nil")
  tenant = result["metaData"]["tenantId"]
  assert(tenant == arg1, "Tenant ID expected #{arg1} but was #{tenant}")
end

Given /^my contextual access is defined by table:$/ do |table|
  @ctx={}
  table.hashes.each do |hash|
  @ctx[hash["Context"]]=hash["Ids"]
  end
end

Then /^uri was rewritten to "(.*?)"$/ do |expectedUri|
  version = "v1"
  root = expectedUri.match(/\/(.+?)\/|$/)[1]
  expected = version+expectedUri
  actual = @headers["x-executedpath"][0]

  #First, make sure the paths of the URIs are the same
  expectedPath = expected.gsub("@ids", "[^/]*")
  assert(actual.match(expectedPath), "Rewriten URI path didn't match, expected:#{expectedPath}, actual:#{actual}")

  #Then, validate the list of ids are the same
  ids = []
  if @ctx.has_key? root
    idsString = actual.match(/v1\/[^\/]*\/([^\/]*)\//)[1]
    actualIds = idsString.split(",")
    expectedIds = @ctx[root].split(",")
    
    assert(actualIds.length == expectedIds.length,"Infered Context IDs not equal: expected:#{expectedIds.inspect}, actual:#{actualIds.inspect}")
    expectedIds.each do |id|
      assert(actualIds.include?(id),"Infered Context IDs not equal: expected:#{expectedIds.inspect}, actual:#{actualIds.inspect}")
    end
  end
end

And /^field "(.*?)" is removed from the json document$/ do |arg1|
  puts @fields.inspect 
  @fields.delete "beginDate" 
  puts @fields.inspect 
end