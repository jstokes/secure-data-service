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

  #course offering data
  id = 94                                        if human_readable_id == "ASSOCIATION COUNT"
  id = 6                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 1"
  id = 1                                        if human_readable_id == "ASSOCIATION COUNT FOR ENDPOINT 2"
  id = 6                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 1"
  id = 1                                        if human_readable_id == "RESOLUTION COUNT FOR ENDPOINT 2"
  id = "b360e8e8-54d1-4b00-952a-b817f91035ed"   if human_readable_id == "ASSOCIATION ID"
  id = "b360e8e8-54d1-4b00-952a-b817f91035ed"   if human_readable_id == "ASSOCIATION ID FOR UPDATE"
  id = "b360e8e8-54d1-4b00-952a-b817f91035ed"   if human_readable_id == "ASSOCIATION ID FOR DELETE"
  id = "getCourseOfferings"			            if human_readable_id == "ASSOCIATION LINK NAME"
  id = "courseOffering"                         if human_readable_id == "ASSOCIATION TYPE"
  id = "courseOfferings"              			if human_readable_id == "ASSOCIATION URI"
  
  #session related data
  id = "sessionId"                               if human_readable_id == "ENDPOINT1 FIELD"
  id = "d23ebfc4-5192-4e6c-a52b-81cee2319072"   if human_readable_id == "ENDPOINT1 ID"          or human_readable_id == "ENDPOINT1 FIELD EXPECTED VALUE"
  id = "getSession"                              if human_readable_id == "ENDPOINT1 LINK NAME" 
  id = "getSessions"                             if human_readable_id == "ENDPOINT1 RESOLUTION LINK NAME" 
  id = "session"                                 if human_readable_id == "ENDPOINT1 TYPE" 
  id = "sessions"                                if human_readable_id == "ENDPOINT1 URI" 
  
  #course related data
  id = "courseId"                              if human_readable_id == "ENDPOINT2 FIELD"
  id = "52038025-1f18-456a-884e-d2e63f9a02f4"   if human_readable_id == "ENDPOINT2 ID"          or human_readable_id == "ENDPOINT2 FIELD EXPECTED VALUE"
  id = "getCourse"                             if human_readable_id == "ENDPOINT2 LINK NAME" 
  id = "getCourses"                            if human_readable_id == "ENDPOINT2 RESOLUTION LINK NAME" 
  id = "course"                                if human_readable_id == "ENDPOINT2 TYPE" 
  id = "courses"                               if human_readable_id == "ENDPOINT2 URI" 
  
  #update related field data
  id = "localCourseCode"                        if human_readable_id == "UPDATE FIELD"
  id = "LCCGR1"						    if human_readable_id == "UPDATE FIELD EXPECTED VALUE" 
  id = "LCCGR2"						    if human_readable_id == "UPDATE FIELD NEW VALID VALUE" 
  
  #general
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"
  id = "389b0caa-dcd2-4e84-93b7-daa4a6e9b18e"   if human_readable_id == "INACCESSIBLE REFERENCE 1"
  id = "e31f7583-417e-4c42-bd55-0bbe7518edf8"   if human_readable_id == "INACCESSIBLE REFERENCE 2"
  id = "self"                                   if human_readable_id == "SELF LINK NAME" 
  id = @newId                                   if human_readable_id == "NEWLY CREATED ASSOCIATION ID"
  id = "Validation failed"                      if human_readable_id == "VALIDATION"
  
  #other
  id = "41baa245-ceea-4336-a9dd-0ba868526b9b"  if human_readable_id == "'Algebra Alternative' ID"
  id = "389b0caa-dcd2-4e84-93b7-daa4a6e9b18e"  if human_readable_id == "'Fall 2011 Session' ID"
  
  #return the translated value
  id
end


###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^a valid association json document for a "([^"]*)"$/ do |arg1|
  @fields = {
     "schoolId" => "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb",
     "sessionId" => "fb0ac9e8-9e4e-48a0-95d2-ae07ee15ee92",
     "courseId" => "ddf01d82-9293-49ba-b16e-0fe5b4f4804d",
     "localCourseCode" => "LCCGR1",
     "localCourseTitle" => "German 1 - Intro to German"
  }
end
