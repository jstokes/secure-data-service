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


require_relative '../../utils/sli_utils.rb'

Given /^my role is Educator$/ do
  restHttpGet('/system/session/check', 'application/json')
  check = JSON.parse(@res.body)
  check['sliRoles'].should include('Educator')
end

When /^I make an API call to access teachers$/ do
  restHttpGet('/v1/teachers', 'application/json')
  @staff = JSON.parse(@res.body)
end

When /^I make an API call to access staff$/ do
  restHttpGet('/v1/staff', 'application/json')
  @staff = JSON.parse(@res.body)
end

Then /^I get a response$/ do
  @staff.should_not be_nil, 'Should have a valid list of entities.'
end

Then /^the response does not includes the protected fields$/ do
  restHttpGet('/v1/home', 'application/json')
  home = JSON.parse(@res.body)
  self_link = home['links'].detect {|link| link['rel'] == 'self'}
  my_id = self_link['href'].split('/').last if self_link

  @staff.each do |staff|
    next if staff['id'] == my_id
    assert(!staff.has_key?('birthDate'), 'Shouldn\'t see fields like BirthDay')
    staff['telephone'].each do |phone|
      type = phone['telephoneNumberType']
      assert(phone == nil || type == 'Work', 'Should not see non-work telephone')
    end
    staff['electronicMail'].each do |email|
      type = email['emailAddressType']
      assert(email == nil || type == 'Work', 'Should not see non-work email')
    end
  end
end

Given /^my role is Leader$/ do
  @format = "application/json"
  restHttpGet("/system/session/check", @format)
  check = JSON.parse(@res.body)
  check['sliRoles'].should include('Leader')
end

Then /^the response includes the protected fields$/ do
  restHttpGet('/v1/home', 'application/json')
  home = JSON.parse(@res.body)
  self_link = home['links'].detect {|link| link['rel'] == 'self'}
  my_id = self_link['href'].split('/').last if self_link

  found_protected = false
  @staff.each do |staff|
    next if staff['id'] == my_id
    found_protected = true if staff.has_key?('birthDate')
  end
  found_protected.should be_true, 'Should have staff with protected fields (birthDate)'

  found_protected = false
  @staff.each do |staff|
    next if staff['id'] == my_id
    staff['telephone'].each do |phone|
      type = phone['telephoneNumberType']
      found_protected = true if phone != nil && type != 'Work'
    end
  end
  found_protected.should be_true, 'Should have staff with protected fields (telephone)'

  found_protected = false
  @staff.each do |staff|
    next if staff['id'] == my_id
    staff['electronicMail'].each do |email|
      type = email['emailAddressType']
      found_protected = true if email != nil && type != 'Work'
    end
  end
  found_protected.should be_true, 'Should have staff with protected fields (email)'
end

Then /^I should see my restricted information$/ do
  assert(@staff.has_key?('birthDate'), 'Should see their own birthday')
end

When /^I make an API call to access myself$/ do
  #In this context, myself equals "Rebecca Braverman"
  restHttpGet('/v1/teachers/bcfcc33f-f4a6-488f-baee-b92fbd062e8d', 'application/json')
  @staff = JSON.parse(@res.body)
end

When /^I update association "(.*?)" "(.*?)" to set studentAccessFlag to "(.*?)"$/ do |type, id, boolean|
  @format = 'application/json'
  flag = (boolean == "true")
  restHttpPatch("/v1/#{type}/#{id}", {"studentRecordAccess"=>flag}.to_json)
  @res.code.should == 204
end

