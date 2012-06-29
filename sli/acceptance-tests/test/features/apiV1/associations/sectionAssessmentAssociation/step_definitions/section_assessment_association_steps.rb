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

  #section assessment association data
  id = 4                                        if human_readable_id == "ASSOCIATION COUNT"
  id = 1                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 1"
  id = 1                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 2"
  id = 1                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 1"
  id = 1                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 2"
  id = "627f6922-983c-4616-874a-def76a87ba70"   if human_readable_id == "ASSOCIATION ID"
  id = "0cb7e99b-fc18-ecdb-830a-99abe8ee26be"   if human_readable_id == "ASSOCIATION ID FOR UPDATE"
  id = "627f6922-983c-4616-874a-def76a87ba70"   if human_readable_id == "ASSOCIATION ID FOR DELETE"
  id = "getSectionAssessmentAssociations"       if human_readable_id == "ASSOCIATION LINK NAME"
  id = "sectionAssessmentAssociation"           if human_readable_id == "ASSOCIATION TYPE"
  id = "sectionAssessmentAssociations"          if human_readable_id == "ASSOCIATION URI"
  
  #section related data
  id = "sectionId"                              if human_readable_id == "ENDPOINT1 FIELD"
  id = "8ed12459-eae5-49bc-8b6b-6ebe1a56384f"   if human_readable_id == "ENDPOINT1 ID"          or human_readable_id == "ENDPOINT1 FIELD EXPECTED VALUE"
  id = "getSection"                             if human_readable_id == "ENDPOINT1 LINK NAME" 
  id = "getSections"                            if human_readable_id == "ENDPOINT1 RESOLUTION LINK NAME" 
  id = "section"                                if human_readable_id == "ENDPOINT1 TYPE" 
  id = "sections"                               if human_readable_id == "ENDPOINT1 URI" 
  
  #assessment related data
  id = "assessmentId"                           if human_readable_id == "ENDPOINT2 FIELD"
  id = "dd916592-7d7e-5d27-a87d-dfc7fcb757f6"   if human_readable_id == "ENDPOINT2 ID"          or human_readable_id == "ENDPOINT2 FIELD EXPECTED VALUE"
  id = "getAssessment"                          if human_readable_id == "ENDPOINT2 LINK NAME" 
  id = "getAssessments"                         if human_readable_id == "ENDPOINT2 RESOLUTION LINK NAME" 
  id = "assessment"                             if human_readable_id == "ENDPOINT2 TYPE" 
  id = "assessments"                            if human_readable_id == "ENDPOINT2 URI" 
  
  #update related field data
  id = "sectionId"                              if human_readable_id == "UPDATE FIELD"
  id = "ceffbb26-1327-4313-9cfc-1c3afd38122e"   if human_readable_id == "UPDATE FIELD EXPECTED VALUE"
  id = "7295e51e-cd51-4901-ae67-fa33966478c7"   if human_readable_id == "UPDATE FIELD NEW VALID VALUE"
  
  #general
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"
  id = "a47eb9aa-1c97-4c8e-9d0a-45689a66d4f8"   if human_readable_id == "INACCESSIBLE REFERENCE 1"
  id = "dd916592-7d3e-4f27-a8ac-bec5f4b757f6"   if human_readable_id == "INACCESSIBLE REFERENCE 2"
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
    "sectionId" => "7295e51e-cd51-4901-ae67-fa33966478c7",
    "assessmentId" => "6a53f63e-deb8-443d-8138-fc5a7368239c",
    "entityType" => "sectionAssessmentAssociation"
  }
end
