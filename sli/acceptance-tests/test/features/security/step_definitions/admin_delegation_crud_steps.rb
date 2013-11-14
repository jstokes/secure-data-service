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

require_relative '../../utils/sli_utils.rb'

Transform /^district "([^"]*)"$/ do |district|
  id = "b2c6e292-37b0-4148-bf75-c98a2fcc905f" if district == "IL-SUNSET"
  id = "b2c6e292-37b0-4148-bf75-c98a2fcc905f" if district == "IL-SUNSET's ID"
  id = "xd086bae-ee82-6ce2-bcf9-321a8407ba13" if district == "IL-LONGWOOD's ID"
  id = "xd086bae-ee82-6ce2-bcf9-321a8407ba13" if district == "IL-LONGWOOD"
  id = "b1bd3db6-d020-4651-b1b8-a8dba688d9e1" if district == "IL"
  id
end

When /^I POST a new admin delegation$/ do
  dataObj = DataProvider.getValidAdminDelegationData()
  data = prepareData("application/json", dataObj)
  @format = "application/json"


  restHttpPost("/adminDelegation", data)

  assert(@res != nil, "Response from POST operation was null")
  
end


When /^I do not have access to app authorizations for district "([^"]*)"$/ do |arg1|
  #No code necessary, should be handled already
end


Given /^I have a valid admin delegation entity$/ do
  @adminDelegation = DataProvider.getValidAdminDelegationData()
end

Given /^I have a valid admin delegation entity for longwood$/ do
  @adminDelegation = DataProvider.getValidAdminDelegationDataLongwood()
end

Given /^I change "([^"]*)" to true$/ do |field|
  @adminDelegation[field] = true
end

Given /^I change "([^"]*)" to false/ do |field|
  @adminDelegation[field] = false
end

When /^I PUT to admin delegation$/ do
  @format = "application/json"
  data = prepareData(@format, @adminDelegation)
  restHttpPut("/adminDelegation/myEdOrg", data)
  assert(@res != nil, "Response from PUT operation was nil")
end

When /^I have access to app authorizations for district "([^"]*)"$/ do |arg1|
  #No code necessary, should be handled already
end

Then /^I should update app authorizations for (district "[^"]*")$/ do |district|
  @format = "application/json"
  dataObj = {"appId" => "78f71c9a-8e37-0f86-8560-7783379d96f7", "authorized" => true, :edorgs => [district]}
  data = prepareData("application/json", dataObj)
  puts("The data is #{data}") if ENV['DEBUG']
  restHttpPut("/applicationAuthorization/78f71c9a-8e37-0f86-8560-7783379d96f7", data)
  assert(@res != nil, "Response from PUT operation was nil")
end

Then /^I should revoke all app authorizations for (district "[^"]*")$/ do |district|
  @format = "application/json"
  dataObj = {"appId" => "78f71c9a-8e37-0f86-8560-7783379d96f7", "authorized" => false, :edorgs => [district]}
  data = prepareData("application/json", dataObj)
  puts("The data is #{data}") if ENV['DEBUG']
  restHttpPut("/applicationAuthorization/78f71c9a-8e37-0f86-8560-7783379d96f7", data)
  assert(@res != nil, "Response from PUT operation was nil")
end

Then /^I should grant all app authorizations for (district "[^"]*")$/ do |district|
  @format = "application/json"
  dataObj = {"appId" => "78f71c9a-8e37-0f86-8560-7783379d96f7", "authorized" => true, :edorgs => [district]}
  data = prepareData("application/json", dataObj)
  puts("The data is #{data}") if ENV['DEBUG']
  restHttpPut("/applicationAuthorization/78f71c9a-8e37-0f86-8560-7783379d96f7?", data)
  assert(@res != nil, "Response from PUT operation was nil")
end

Then /^I should update one app authorization for district "([^"]*)"$/ do |district|
  step "I should update app authorizations for district \"#{district}\""
end

Then /^I should also update app authorizations for district "([^"]*)"$/ do |district|
  step "I should update app authorizations for district \"#{district}\""
end

Then /^I should get my delegations$/ do
  restHttpGet("/adminDelegation")
  assert(@res != nil, "Response from GET operation was nil")
end

Then /^I should see that "([^"]*)" is "([^"]*)" for (district "[^"]*")$/ do |field, value, district|
  list = JSON.parse(@res.body)
  foundIt = false
  puts "brandon"  
  puts district
  list.each do |cur|
    
    if cur["localEdOrgId"] == district  
      foundIt = true
      assert(cur[field].to_s == value, "Expected field #{field} to be #{value} was #{cur[field].to_s}")
    end
  end
  assert(foundIt, "Never found district #{district}")
end

When /^I navigate to GET applicationAuthorization with "([^"]*)"$/ do |app_id|
  restHttpGet("/applicationAuthorization/#{app_id}")
  assert(@res != nil, "Response from application authorization request is nil")
  @result = JSON.parse(@res.body)
  #puts @result
end

And /^There is an applicationAuthorization entity for application "([^"]*)"$/ do |app_id|
   found=false
   @result.each do |appAuthEntry|
     if appAuthEntry["appId"]==app_id
       found = true
       @result = appAuthEntry
       break;
     end
   end

   assert(found=true,"Application #{app_id} was not found in the results");

end

And /^There is a correct entry in applicationAuthorization edorg array for (district "[^"]*") for the application "([^"]*)"$/ do |edorg, app_id|
  edorgs = @result["edorgs"]
  found = false
  edorgs.each do |edorg_entry|
    if edorg_entry["authorizedEdorg"]== edorg
      found = true
      @edorg_hash = edorg_entry
      break
    end
  end
  assert(found==true,"The entry in applicationAuthorization edorg array for #{edorg} was incorrect");
  assert(@edorg_hash.size==4, "The entry in applicationAuthorization edorg array for #{edorg} was incorrect");
end

And /^The value of "([^"]*)" should be "([^"]*)"$/ do |field, value|
  if value == "nil"
    value_t = nil
  else
    value_t = value
  end
  assert(@edorg_hash.has_key?(field), "Does not have expected field #{field}")
  if @edorg_hash.has_key?(field)
    assert(@edorg_hash[field] == value_t, "Expected field #{field} to be #{value_t} was #{@edorg_hash[field].to_s}")
    @edorg_hash.delete(field)
  end
end


