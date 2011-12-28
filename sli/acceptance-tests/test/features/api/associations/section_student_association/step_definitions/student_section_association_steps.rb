require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../common.rb'

#transform to section or student uri
Transform /^(Section|Student) "([^"]*)"$/ do |type, id|
  puts "called with type = #{type} and id = #{id}"
  if type == "Student"
    uri = "4efb3a11-bc49-f388-0000-0000c93556fb" if id == "Jane Doe"
    uri = "4efb3a5e-bc49-f388-0000-0000c93556fc" if id == "Albert Wright"
    uri = "4efb3a7f-bc49-f388-0000-0000c93556fd" if id == "Kevin Smith"
  elsif type == "Section"
    uri = "4efb4262-bc49-f388-0000-0000c9355700" if id == "Biology II - C"
    uri = "4efb4292-bc49-f388-0000-0000c9355701" if id == "Foreign Language - A"
    uri = "4efb4243-bc49-f388-0000-0000c93556ff" if id == "Physics I - B"
    uri = "4efb4238-bc49-f388-0000-0000c93556fe" if id == "Chemistry I - A"
  end
  uri
end

#transform to student section association by student AND section
Transform /^Student Section Association for Student "([^"]*)" and Section "([^"]*)"$/ do |student, section|
  puts "called with student:#{student} and section #{section}"
  uri = "4efb4b14-bc49-f388-0000-0000c9355702" if student == "Jane Doe" and section == "Foreign Language - A"
  uri = "4efb4b2f-bc49-f388-0000-0000c9355703" if student == "Jane Doe" and section == "Physics I - B"
  uri = "4efb4b38-bc49-f388-0000-0000c9355704" if student == "Jane Doe" and section == "Chemistry I - A"
  uri = "4efb5272-bc49-f388-0000-0000c9355705" if student == "Albert Wright" and section == "Chemistry I - A"
  uri = "4efb4b14-bc49-f388-0000-0000c9355702" if student == "Kevin Smith" and section == "Chemistry I - A"
  uri = "4efb7614-8488-7b01-0000-000059f9ba55" if student == "Albert Wright" and section == "Foreign Language - A"
  uri
end

#transform to student section associations by student OR section
Transform /^Student Section Associations for the (Student|Section) "([^"]*)"$/ do |type, id|
  puts "called with type = #{type} and id = #{id}"
  Transform type + ' "' + id + '"'
end

#vnd.slc+json format is not ready for testing
#remove this transform to switch to new format
Transform /^application\/vnd\.slc\+json$/ do |args|
  puts 'transform to application/json was called'
  "application/json"
end

#Givens

Given /^"([^"]*)" is ((Student|Section) "[^"]*")$/ do |key, value, student_or_section|
  puts "key = #{key}"
  puts "student_or_section = #{student_or_section}"
  puts "value = #{value}"
  
  step '"' + key + '" is "' + value + '"'
end

#Whens

When /^I navigate to (GET|DELETE|PUT) (Student Section Association for Student "[^"]*" and Section "[^"]*")$/ do |method, uri|
  puts "uri = #{uri}"
  puts "method = #{method}"
  step 'I navigate to ' + method +  ' "/student-section-associations/' + uri + '"'
end

When /^I navigate to GET (Student Section Associations for the (Student|Section) "[^"]*")$/ do |uri, student_or_section|
  puts "uri = #{uri}"
  step 'I navigate to GET "/student-section-associations/' + uri + '"'
end
