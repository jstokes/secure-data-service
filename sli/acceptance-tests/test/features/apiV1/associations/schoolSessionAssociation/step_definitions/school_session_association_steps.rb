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

  #school session association data
  id = 3                                       if human_readable_id == "ASSOCIATION COUNT"
  id = 2                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 1"
  id = 2                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 2"
  id = 1                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 1"
  id = 1                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 2"
  id = "7a1f5ae5-ee79-f9e5-eca8-10c32f390a8c"   if human_readable_id == "ASSOCIATION ID"
  id = "7a1f5ae5-ee79-f9e5-eca8-10c32f390a8c"   if human_readable_id == "ASSOCIATION ID FOR UPDATE"
  id = "7a1f5ae5-ee79-f9e5-eca8-10c32f390a8c"   if human_readable_id == "ASSOCIATION ID FOR DELETE"
  id = "getSchoolSessionAssociations"           if human_readable_id == "ASSOCIATION LINK NAME"
  id = "schoolSessionAssociation"               if human_readable_id == "ASSOCIATION TYPE"
  id = "schoolSessionAssociations"              if human_readable_id == "ASSOCIATION URI"
  
  #school related data
  id = "schoolId"                               if human_readable_id == "ENDPOINT1 FIELD"
  id = "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb"   if human_readable_id == "ENDPOINT1 ID"          or human_readable_id == "ENDPOINT1 FIELD EXPECTED VALUE"
  id = "getSchool"                              if human_readable_id == "ENDPOINT1 LINK NAME" 
  id = "getSchools"                             if human_readable_id == "ENDPOINT1 RESOLUTION LINK NAME" 
  id = "school"                                 if human_readable_id == "ENDPOINT1 TYPE" 
  id = "schools"                                if human_readable_id == "ENDPOINT1 URI" 
  
  #session related data
  id = "sessionId"                              if human_readable_id == "ENDPOINT2 FIELD"
  id = "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92"   if human_readable_id == "ENDPOINT2 ID"          or human_readable_id == "ENDPOINT2 FIELD EXPECTED VALUE"
  id = "getSession"                             if human_readable_id == "ENDPOINT2 LINK NAME" 
  id = "getSessions"                            if human_readable_id == "ENDPOINT2 RESOLUTION LINK NAME" 
  id = "session"                                if human_readable_id == "ENDPOINT2 TYPE" 
  id = "sessions"                               if human_readable_id == "ENDPOINT2 URI" 
  
  #update related field data
  id = "sessionId"                              if human_readable_id == "UPDATE FIELD"
  id = "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92"   if human_readable_id == "UPDATE FIELD EXPECTED VALUE" 
  id = "c549e272-9a7b-4c02-aff7-b105ed76c904"   if human_readable_id == "UPDATE FIELD NEW VALID VALUE" 
  
  #general
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"
  id = "0f464187-30ff-4e61-a0dd-74f45e5c7a9d"   if human_readable_id == "INACCESSIBLE REFERENCE 1"
  id = "389b0caa-dcd2-4e84-93b7-daa4a6e9b18e"   if human_readable_id == "INACCESSIBLE REFERENCE 2"
  id = "self"                                   if human_readable_id == "SELF LINK NAME" 
  id = @newId                                   if human_readable_id == "NEWLY CREATED ASSOCIATION ID"
  id = "Validation failed"                      if human_readable_id == "VALIDATION"
  id = "Invalid reference. No association to referenced entity." if human_readable_id == "BAD REFERENCE"

  #return the translated value
  id
end


###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^a valid association json document for a "([^"]*)"$/ do |arg1|
  @fields = {
    "sessionId" => "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92",
    "schoolId" => "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb"
  }
end
