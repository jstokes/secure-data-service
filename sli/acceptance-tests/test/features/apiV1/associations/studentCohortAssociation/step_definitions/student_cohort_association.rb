require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../../utils/api_utils.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |val|

  case val
    #student cohort association data
  when "ASSOCIATION COUNT" then 10
  when "ASSOCIATION COUNT FOR ENDPOINT 1" then 1
  when "ASSOCIATION COUNT FOR ENDPOINT 2" then 3
  when "RESOLUTION COUNT FOR ENDPOINT 1" then 1
  when "RESOLUTION COUNT FOR ENDPOINT 2" then 3
  when "ASSOCIATION ID" then "4cfe2c95-09d5-4ba7-a09c-c7aabb8902c3"
  when "ASSOCIATION ID FOR UPDATE" then "530acadc-f4b2-4dd4-a087-5700e9890548"
  when "ASSOCIATION ID FOR DELETE" then "88cadff8-d0fb-4a82-ab0a-594c74024c77"
  when "ASSOCIATION LINK NAME" then "getStudentCohortAssociations"
  when "ASSOCIATION TYPE" then "studentCohortAssociation"
  when "ASSOCIATION URI" then "studentCohortAssociations"
    
    #student related data
  when "ENDPOINT1 FIELD" then "studentId"
  when "ENDPOINT1 ID", "ENDPOINT1 FIELD EXPECTED VALUE" then "714c1304-8a04-4e23-b043-4ad80eb60992"
  when "ENDPOINT1 LINK NAME" then "getStudent"
  when "ENDPOINT1 RESOLUTION LINK NAME" then "getStudents"
  when "ENDPOINT1 TYPE" then "student"
  when "ENDPOINT1 URI" then "students"

    #cohort related data
  when "ENDPOINT2 FIELD" then "cohortId"
  when "ENDPOINT2 ID", "ENDPOINT2 FIELD EXPECTED VALUE" then "7e9915ed-ea6f-4e6b-b8b0-aeae20a25826"
  when "ENDPOINT2 LINK NAME" then "getCohort"
  when "ENDPOINT2 RESOLUTION LINK NAME" then "getCohorts"
  when "ENDPOINT2 TYPE" then "cohort"
  when "ENDPOINT2 URI" then "cohorts"

    #update related field data
  when "UPDATE FIELD" then "beginDate"
  when "UPDATE FIELD EXPECTED VALUE" then "2012-01-15"
  when "UPDATE FIELD NEW VALID VALUE" then "2012-03-07"

    #general
  when "INVALID REFERENCE" then "11111111-1111-1111-1111-111111111111"
  when "SELF LINK NAME" then "self"
  when "NEWLY CREATED ASSOCIATION ID" then @newId
  when "VALIDATION" then "Validation failed"
  end
end


###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^a valid association json document for a "([^"]*)"$/ do |arg1|
  @fields = {
    "studentId" => "e0e99028-6360-4247-ae48-d3bb3ecb606a",
    "cohortId" => "a6929135-4782-46f1-ab01-b4df2e6ad093",
    "beginDate" => "2012-02-29",
    "endDate" => "2012-03-29"
  }
end
