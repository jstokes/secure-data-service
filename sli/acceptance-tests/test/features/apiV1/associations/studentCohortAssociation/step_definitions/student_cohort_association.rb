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
  when "ASSOCIATION COUNT" then 13
  when "ASSOCIATION COUNT FOR ENDPOINT 1" then 1
  when "ASSOCIATION COUNT FOR ENDPOINT 2" then 3
  when "RESOLUTION COUNT FOR ENDPOINT 1" then 1
  when "RESOLUTION COUNT FOR ENDPOINT 2" then 3
  when "ASSOCIATION ID" then "b40c5b02-8fd5-11e1-86ec-0021701f543f"
  when "ASSOCIATION ID FOR UPDATE" then "b40c5b02-8fd5-11e1-86ec-0021701f543f"
  when "ASSOCIATION ID FOR DELETE" then "b40c5b02-8fd5-11e1-86ec-0021701f543f"
  when "ASSOCIATION LINK NAME" then "getStudentCohortAssociations"
  when "ASSOCIATION TYPE" then "studentCohortAssociation"
  when "ASSOCIATION URI" then "studentCohortAssociations"

    #student related data
  when "ENDPOINT1 FIELD" then "studentId"
  when "ENDPOINT1 ID", "ENDPOINT1 FIELD EXPECTED VALUE" then "41df2791-b33c-4b10-8de6-a24963bbd3dd"
  when "ENDPOINT1 LINK NAME" then "getStudent"
  when "ENDPOINT1 RESOLUTION LINK NAME" then "getStudents"
  when "ENDPOINT1 TYPE" then "student"
  when "ENDPOINT1 URI" then "students"

    #cohort related data
  when "ENDPOINT2 FIELD" then "cohortId"
  when "ENDPOINT2 ID", "ENDPOINT2 FIELD EXPECTED VALUE" then "b408635d-8fd5-11e1-86ec-0021701f543f"
  when "ENDPOINT2 LINK NAME" then "getCohort"
  when "ENDPOINT2 RESOLUTION LINK NAME" then "getCohorts"
  when "ENDPOINT2 TYPE" then "cohort"
  when "ENDPOINT2 URI" then "cohorts"

    #update related field data
  when "UPDATE FIELD" then "beginDate"
  when "UPDATE FIELD EXPECTED VALUE" then "2011-05-01"
  when "UPDATE FIELD NEW VALID VALUE" then "2012-03-07"

    #general
  when "INVALID REFERENCE" then "11111111-1111-1111-1111-111111111111"
  when "INACCESSIBLE REFERENCE 1" then "eb4d7e1b-7bed-890a-d9f4-5d8aa9fbfc2d"
  when "INACCESSIBLE REFERENCE 2" then "a50121a2-c566-401b-99a5-71eb5cab5f4f"
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
    "studentId" => "0f0d9bac-0081-4900-af7c-d17915e02378",
    "cohortId" => "b408d88e-8fd5-11e1-86ec-0021701f543f",
    "beginDate" => "2012-02-29",
    "endDate" => "2012-03-29"
  }
end
