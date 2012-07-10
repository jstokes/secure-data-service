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
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../../utils/api_utils.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|

  #student section association data
  id = 265                                      if human_readable_id == "ASSOCIATION COUNT"
  id = 2                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 1"
  id = 25                                       if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 2"
  id = 25                                       if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 1"
  id = 2                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 2"
  id = "4ae72560-3518-4576-a35e-a9607668c9ad"   if human_readable_id == "ASSOCIATION ID"
  id = "4ae72560-3518-4576-a35e-a9607668c9ad"   if human_readable_id == "ASSOCIATION ID FOR UPDATE"
  id = "4ae72560-3518-4576-a35e-a9607668c9ad"   if human_readable_id == "ASSOCIATION ID FOR DELETE"
  id = "getStudentSectionAssociations"          if human_readable_id == "ASSOCIATION LINK NAME"
  id = "studentSectionAssociation"              if human_readable_id == "ASSOCIATION TYPE"
  id = "studentSectionAssociations"             if human_readable_id == "ASSOCIATION URI"
  id = "7ba1a2e9-989c-4b00-b5e0-9bf3b72c909d"	if human_readable_id == "ASSOCIATION ID FOR GRADE"
  id = 2										if human_readable_id == "GRADE COUNT"
  id = "grade"									if human_readable_id == "GRADE TYPE"
  id = "grades"									if human_readable_id == "GRADE URI"
  
  #student related data
  id = "studentId"                              if human_readable_id == "ENDPOINT1 FIELD"
  id = "27fea52e-94ab-462c-b80f-7e868f6919d7"   if human_readable_id == "ENDPOINT1 ID"          or human_readable_id == "ENDPOINT1 FIELD EXPECTED VALUE"
  id = "getStudent"                             if human_readable_id == "ENDPOINT1 LINK NAME" 
  id = "getStudents"                            if human_readable_id == "ENDPOINT1 RESOLUTION LINK NAME" 
  id = "student"                                if human_readable_id == "ENDPOINT1 TYPE" 
  id = "students"                               if human_readable_id == "ENDPOINT1 URI" 
  
  #section related data
  id = "sectionId"                              if human_readable_id == "ENDPOINT2 FIELD"
  id = "8ed12459-eae5-49bc-8b6b-6ebe1a56384f"   if human_readable_id == "ENDPOINT2 ID"          or human_readable_id == "ENDPOINT2 FIELD EXPECTED VALUE"
  id = "getSection"                             if human_readable_id == "ENDPOINT2 LINK NAME" 
  id = "getSections"                            if human_readable_id == "ENDPOINT2 RESOLUTION LINK NAME" 
  id = "section"                                if human_readable_id == "ENDPOINT2 TYPE" 
  id = "sections"                               if human_readable_id == "ENDPOINT2 URI" 
  
  #update related field data
  id = "homeroomIndicator"                       if human_readable_id == "UPDATE FIELD"
  id = "true"                           if human_readable_id == "UPDATE FIELD EXPECTED VALUE"
  id = "false"   if human_readable_id == "UPDATE FIELD NEW VALID VALUE"
  
  #general
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"
  id = "eb4d7e1b-7bed-890a-d9f4-5d8aa9fbfc2d"   if human_readable_id == "INACCESSIBLE REFERENCE 1"
  id = "a47eb9aa-1c97-4c8e-9d0a-45689a66d4f8"   if human_readable_id == "INACCESSIBLE REFERENCE 2"
  id = "self"                                   if human_readable_id == "SELF LINK NAME" 
  id = @newId                                   if human_readable_id == "NEWLY CREATED ASSOCIATION ID"
  id = "Validation failed"                      if human_readable_id == "VALIDATION"
  
  #return the translated value
  id
end


###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^a valid association json document for a "([^"]*)"$/ do |arg1|
  @fields = {
    "studentId" => "0636ffd6-ad7d-4401-8de9-40538cf696c8",
    "sectionId" => "ceffbb26-1327-4313-9cfc-1c3afd38122e",
    "repeatIdentifier" => "Repeated, counted in grade point average",
    "beginDate" => "2011-12-01",
    "endDate" => "2012-01-01",
    "entityType" => "studentSectionAssociation"
  }
end
