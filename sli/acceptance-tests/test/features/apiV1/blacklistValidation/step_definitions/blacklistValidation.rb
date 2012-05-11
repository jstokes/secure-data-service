require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../utils/sli_utils.rb'
require_relative '../../utils/api_utils.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|

  #assessment data
  id = 6                                        if human_readable_id == "ENTITY COUNT"
  id = "df9165f2-653e-df27-f86c-bfc5f4b7577d"   if human_readable_id == "ENTITY ID"
  id = "df9165f2-65fe-de27-a82c-bfc5f4b7577c"   if human_readable_id == "ENTITY ID FOR UPDATE"
  id = "df9165f2-65be-de27-a8ec-bec5f4b7577b"   if human_readable_id == "ENTITY ID FOR DELETE"
  id = "learningObjective"                      if human_readable_id == "ENTITY TYPE"
  id = "learningObjectives"                     if human_readable_id == "ENTITY URI"
  
  #update related field data
  id = "academicSubject"                          if human_readable_id == "UPDATE FIELD"
  id = "Writing"                                  if human_readable_id == "UPDATE FIELD EXPECTED VALUE" 
  id = "Mathematics"                              if human_readable_id == "UPDATE FIELD NEW VALID VALUE" 
  
  #general
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"
  id = "self"                                   if human_readable_id == "SELF LINK NAME" 
  id = @newId                                   if human_readable_id == "NEWLY CREATED ENTITY ID"
  id = "Validation failed"                      if human_readable_id == "VALIDATION"
  
  #other
  id = ""                                       if human_readable_id == "BLANK"
  
  #return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^an entity with a \\u0000 character for a "([^"]*)"$/ do |arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test\u0000",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with a \\u0001 character for a "([^"]*)"$/ do |arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test\u0001",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with a \\u0010 character for a "([^"]*)"$/ do |arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test\u0010",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with a \\u003c character for a "([^"]*)"$/ do |arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test\u003c",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with a '>' character for a "([^"]*)"$/ do |arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test>",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with a '<' character for a "([^"]*)"$/ do |arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test>",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with the text '(.*)' for a "([^"]*)"$/ do |word,arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test " + word,
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff"
  }
end

Given /^an entity with the text '(.*)' in the description field for a "([^"]*)"$/ do |word,arg1|
  @fields = {
    "academicSubject" => "Mathematics",
    "objective" => "Math Test",
    "objectiveGradeLevel" => "Fifth grade",
    "description" => "Some stuff" + word
  }
end






