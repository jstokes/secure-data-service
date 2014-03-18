=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

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

require 'json'
require 'mongo'
require 'rest-client'
require 'rexml/document'
include REXML
require_relative '../../apiV1/utils/api_utils.rb'
require_relative '../../utils/sli_utils.rb'



###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|

  id = "staff"                                   if human_readable_id == "STAFF URI"
  id = "students"                                if human_readable_id == "STUDENT URI"
  id = "educationOrganizations"                  if human_readable_id == "EDORG URI"
  id = @newId                                    if human_readable_id == "New Entity ID"
  id = "85585b27-5368-4f10-a331-3abcaf3a3f4c"    if human_readable_id == "'Rick Rogers' ID"
  id = "67ed9078-431a-465e-adf7-c720d08ef512"    if human_readable_id == "'Linda Kim' ID"
  id = "0e26de7923423525d62015113ad50a03b_id"    if human_readable_id == "STUDENT ID"
    
  #return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################


$edorgData = {
    "organizationCategories" => [
      "State Education Agency"
    ],
    "address" => [
      {
        "addressType" => "Physical",
        "streetNumberName" => "111 District Office Way",
        "apartmentRoomSuiteNumber" => "100",
        "city" => "NY",
        "stateAbbreviation" => "NY",
        "postalCode" => "12345",
        "nameOfCounty" => "Wake"
      }
    ],
    "parentEducationAgencyReference" => "b1bd3db6-d020-4651-b1b8-a8dba688d9e1",
    "stateOrganizationId" => "NEW SCHOOL",
    "nameOfInstitution" => "New School for Testing"
  }

Given /^a valid entity json document for a "([^"]*)"$/ do |arg1|
  if arg1 == "educationOrganization"
    @fields = $edorgData
  else 
    assert(false,"Unexpected entity type: " + arg1)
  end
end

Given /^I drop the "([^\"]*)" collection$/ do |collection|
  DbClient.new.open {|db| db.remove_all collection}
end

###############################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
###############################################################################

Then /^there should be (\d+) records in the "([^\"]*)" collection$/ do |count, collection|
  DbClient.new.for_sli.open do |db|
    db.count(collection).should == count.to_i
  end
end

Then /^"([^\"]*)" field is "([^\"]*)" for all records$/ do |field, value|
  @entity_collection.find.each do |record|
    assert(record[field].to_s == value.to_s,
           "For #{record["_id"]}, expected #{field} to be #{value}, actual value: #{record[field].to_s}")
  end
end

Then /^"([^\"]*)" should exist$/ do |arg1|
  @result = @res if !defined? @result
  assert(@result.has_key?(arg1), "Expected '#{arg1}' field should exist")
end

Then /^"([^\"]*)" should not exist$/ do |arg1|
  @result = @res if !defined? @result
  assert(@result.has_key?(arg1) == false, "Expected '#{arg1}' field should not exist")
end

Then /^the entity should have a version of "([^\"]*)" in the database$/ do |arg1|
  @result = @res if !defined? @result
  DbClient.new(:tenant=>'Midgar').open do |db|
    db.count('educationOrganization',{'_id' => @newId, 'metaData.tenantId' => 'Midgar'}).should == 1
  end
end





