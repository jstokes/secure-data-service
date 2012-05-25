require 'json'
require 'mongo'
require 'rest-client'
require 'rexml/document'
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../../utils/api_utils.rb'

Transform /^<([^"]*)>$/ do |human_readable_id|
  id = "assessments"                                if human_readable_id == "ASSESSMENT URI"
  id = "teachers"                                   if human_readable_id == "TEACHER URI"
  id = "students"                                   if human_readable_id == "STUDENT URI"
  id = "sections"                                   if human_readable_id == "SECTION URI"
  id = "sectionAssessmentAssociations"              if human_readable_id == "SECTION ASSESSMENT ASSOC URI"
  id = "studentSectionAssociations"                 if human_readable_id == "STUDENT SECTION ASSOC URI"
  id = "studentAssessments"			                    if human_readable_id == "STUDENT ASSESSMENT ASSOC URI"
  id = "teacherSectionAssociations"                 if human_readable_id == "TEACHER SECTION ASSOC URI"
  
  #sections
  id = "ceffbb26-1327-4313-9cfc-1c3afd38122e"       if human_readable_id == "'8th Grade English - Sec 6' ID"
  id = "58c9ef19-c172-4798-8e6e-c73e68ffb5a3"       if human_readable_id == "'Algebra II' ID"
  
  #students
  id = "0636ffd6-ad7d-4401-8de9-40538cf696c8" if human_readable_id == "'Preston Muchow' ID"
  id = "f7094bd8-46fc-4204-9fa2-a383fb71bdf6" if human_readable_id == "'Mayme Borc' ID"
  id = "6bfbdd9a-441a-490a-9f83-716785b61829" if human_readable_id == "'Malcolm Costillo' ID"
  id = "891faebe-bc84-4e0c-b7f3-195637cd981e" if human_readable_id == "'Tomasa Cleaveland' ID"
  id = "ffee781b-22b1-4015-81ff-3289ceb2c113" if human_readable_id == "'Merry Mccanse' ID"
  id = "5dd72fa0-98fe-4017-ab32-0bd33dc03a81" if human_readable_id == "'Samantha Scorzelli' ID"
  id = "5738d251-dd0b-4734-9ea6-417ac9320a15" if human_readable_id == "'Matt Sollars' ID"
  id = "32932b97-d466-4d3c-9ebe-d58af010a87c" if human_readable_id == "'Dominic Brisendine' ID"
  id = "6f60028a-f57a-4c3d-895f-e34a63abc175" if human_readable_id == "'Lashawn Taite' ID"
  id = "4f81fd4c-c7c5-403e-af91-6a2a91f3ad04" if human_readable_id == "'Oralia Merryweather' ID"
  id = "766519bf-31f2-4140-97ec-295297bc045e" if human_readable_id == "'Dominic Bavinon' ID"
  id = "e13b1a7a-81d6-474c-b751-a6af054dbd6a" if human_readable_id == "'Rudy Bedoya' ID"
  id = "a17bd536-7502-4a4d-9d1f-538792b86795" if human_readable_id == "'Verda Herriman' ID"
  id = "7062c584-e229-4763-bf40-aec36bf112e7" if human_readable_id == "'Alton Maultsby' ID"
  id = "b1312b46-0a6b-4c6d-b73a-8cd7981e260e" if human_readable_id == "'Felipe Cianciolo' ID"
  id = "e0c2e40a-a472-4e78-9736-5ed0cbc16018" if human_readable_id == "'Lyn Consla' ID"
  id = "7ac04245-d931-447c-b8b2-aeef63fa3a4e" if human_readable_id == "'Felipe Wierzbicki' ID"
  id = "5714e819-0323-4281-b8d6-83604d3e95e8" if human_readable_id == "'Gerardo Giaquinto' ID"
  id = "2ec521f4-38e9-4982-8300-8df4eed2fc52" if human_readable_id == "'Holloran Franz' ID"
  id = "f11f341c-709b-4c8e-9b08-da9ff89ec0a9" if human_readable_id == "'Oralia Simmer' ID"
  id = "e62933f0-4226-4895-8fe3-aaffd5556032" if human_readable_id == "'Lettie Hose' ID"
  id = "903ea314-8212-4e9f-92b7-a18a25059804" if human_readable_id == "'Gerardo Saltazor' ID"
  id = "0fb8e0b4-8f84-48a4-b3f0-9ba7b0513dba" if human_readable_id == "'Lashawn Aldama' ID"
  id = "37edd9ae-3ac2-4bba-a8d8-be57461cd6de" if human_readable_id == "'Alton Ausiello' ID"
  id = "1d3e77f6-5f07-47c2-8086-b5aa6f4d703e" if human_readable_id == "'Marco Daughenbaugh' ID"
  id = "dbecaa89-29e6-41e1-8099-f80e29baf48e" if human_readable_id == "'Karrie Rudesill' ID"
  id = "414106a9-6156-47e3-a477-4bd4dda7e21a" if human_readable_id == "'Damon Iskra' ID"
  id = "e2d8ba15-953c-4cf7-a593-dbb419014901" if human_readable_id == "'Gerardo Rounsaville' ID"
    
  #teachers
  id = "67ed9078-431a-465e-adf7-c720d08ef512"       if human_readable_id == "'Linda Kim' ID"
  id = "e24b24aa-2556-994b-d1ed-6e6f71d1be97"       if human_readable_id == "'Ms. Smith' ID"
  
  #assessments
  id = "67ce204b-9999-4a11-bfea-000000004682"       if human_readable_id == "'Math Assessment' ID"
  id = "dd916592-7d7e-5d27-a87d-dfc7fcb757f6"       if human_readable_id == "'SAT' ID"
  id = "e85b5aa7-465a-6dd3-8ffb-d02461ed79f8"       if human_readable_id == "'Most Recent SAT Student Assessment Association' ID"
  id = "e5e13e61-01aa-066b-efe0-710f7a0e8755"       if human_readable_id == "'Most Recent Math Student Assessment Association' ID"
  id = "dd9165f2-65fe-4e27-a8ac-bec5f4b757f6"       if human_readable_id == "'Grade 2 BOY DIBELS' ID"
  id = "dd916592-7dfe-4e27-a8ac-bec5f4b757b7"       if human_readable_id == "'Grade 2 MOY READ2' ID"
  id = "dd9165f2-65fe-4e27-a8ac-bec5f4b757f6"       if human_readable_id == "'Grade 2 BOY READ2' ID"
  id = "1e0ddefb-875a-ef7e-b8c3-33bb5676115a"       if human_readable_id == "'Most Recent Student Assessment Association' ID"
  
  #teacher section associations
  id = "12f25c0f-75d7-4e45-8f36-af1bcc342871"       if human_readable_id == "'Teacher Ms. Jones and Section Algebra II' ID"
  
  id
end

Given /^I am a valid SEA\/LEA end user "([^"]*)" with password "([^"]*)"$/ do |user, pass|
  @user = user
  @passwd = pass
end

Given /^I have a Role attribute returned from the "([^"]*)"$/ do |arg1|
# No code needed, this is done during the IDP configuration
end

Given /^the role attribute equals "([^"]*)"$/ do |arg1|
# No code needed, this is done during the IDP configuration
end

Given /^I am authenticated on "([^"]*)"$/ do |arg1|
  idpRealmLogin(@user, @passwd, arg1)
  assert(@sessionId != nil, "Session returned was nil")
end

When /^I navigate to URI "([^"]*)" with filter sorting and pagination$/ do |href|
  @filterSortPaginationHref=href
end

When /^filter by "([^"]*)" = "([^"]*)"$/ do |key, value|
  if @filterSortPaginationHref.include? "?"
    @filterSortPaginationHref = @filterSortPaginationHref+"&"+URI.escape(key)+"="+URI.escape(value)
  else
    @filterSortPaginationHref = @filterSortPaginationHref+"?"+URI.escape(key)+"="+URI.escape(value)
  end
end

When /^I submit the sorting and pagination request$/ do
  step "I navigate to GET \"#{@filterSortPaginationHref}\""
  assert(@result != nil, "Response contains no data")
end

Then /^I should have a list of (\d+) "([^"]*)" entities$/ do |size, entityType|
  assert(@result != nil, "Response contains no data")
  if @result.is_a?(Hash)
    assert(@result["entityType"] == entityType)
  else
    assert(@result.is_a?(Array), "Response contains #{@result.class}, expected Array")

    @ids = Array.new
    @result.each do |entity|
      assert(entity["entityType"] == entityType)
      @ids.push(entity["id"])
    end
  end
end

Then /^I should have an entity with ID "([^"]*)"$/ do |entityId|
  found = false
  @ids.each do |id|
    if entityId == id
      found = true
    end
  end
  
  assert(found, "Entity id #{entityId} was not found")
end

Then /^the field "([^"]*)" should be "([^"]*)"$/ do |field, value|
  assert(@result != nil, "Response contains no data")
  val = @result.clone
  field.split(".").each do |part|
    is_num?(part) ? val = val[part.to_i] : val = val[part]
  end
   
  assert(val == value, "the #{field} is #{val}, not expected #{value}")
end

Then /^there are "([\d]*)" "([^"]*)"$/ do |count, collection|
  assert(@result[collection].length == convert(count), "Expected #{count} #{collection}, received #{@result[collection].length}")
  @col = @result[collection]
end

Then /^for the level at position "([\d]*)"$/ do |offset|
  @offset = convert(offset)
end

Then /^the key "([^"]*)" has value "([^"]*)"$/ do |key,value|
  val = @col[@offset].clone
  key.split(".").each do |part|
    is_num?(part) ? val = val[part.to_i] : val = val[part]
  end
  if is_num?(value)
    assert(val == value.to_i, "Expected value: #{value}, but received #{val}")
  else
    assert(val == value, "Expected value: #{value}, but received #{val}")
  end
end

def is_num?(str)
  Integer(str)
rescue ArgumentError
  false
else
  true
end

