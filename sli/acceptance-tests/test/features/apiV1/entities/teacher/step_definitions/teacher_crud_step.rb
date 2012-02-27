require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../common.rb'

# transform <Place Holder Id>
Transform /^<.+>$/ do |template|
  id = template
  id = @newId.to_s                            if template == "<'newly created teacher' ID>"
  id = "fa45033c-5517-b14b-1d39-c9442ba95782" if template == "<'Macey' ID>";
  id = "738543275"                            if template == "<'Macey Home State' ID>"
  id = "344cf68d-50fd-8dd7-e8d6-ed9df76c219c" if template == "<'Belle' ID>"
  id = "11111111-1111-1111-1111-111111111111" if template == "<Unknown>"
  id = "824643f7-174b-4a50-9383-c9a6f762c49d" if template == "<'Christian' ID>"
  id = "a249d5d9-f149-d348-9b10-b26d68e7cb9c" if template == "<'Illiana' ID>"
  id
end

# transform /path/<Place Holder Id>
Transform /^(\/[\w-]+\/)(<.+>)$/ do |uri, template|
  uri + Transform(template)
end

# transform /path/<Place Holder Id>/targets
Transform /^(\/[\w-]+\/)(<.+>)\/targets$/ do |uri, template|
  Transform(uri + template) + "/targets"
end

# Transform /^teacher "([^"]*)"$/ do |step_arg|
#   id = "/teachers/fa45033c-5517-b14b-1d39-c9442ba95782" if step_arg == "Macey"
#   id = "/teachers/344cf68d-50fd-8dd7-e8d6-ed9df76c219c" if step_arg == "Belle"
#   id = "/teachers/824643f7-174b-4a50-9383-c9a6f762c49d" if step_arg == "Christian"
#   id = "/teachers/a249d5d9-f149-d348-9b10-b26d68e7cb9c" if step_arg == "Illiana"
#   id = "/teachers/0e43f14f-ead4-b09f-1ed5-ee5c7e3eeb8c" if step_arg == "Daphne"
#   id = "/teachers/d34fd432-a41c-67ce-1a86-5c2dc6957faf" if step_arg == "Harding"
#   id = "/teachers/eb424dcc-6cff-a69b-c1b3-2b1fc86b2c94" if step_arg == "Simone"
#   id = "/teachers/f44e0c1f-908c-8432-960a-67f650206b88" if step_arg == "Micah"
#   id = "/teachers/e24b24aa-2556-994b-d1ed-6e6f71d1be97" if step_arg == "Quemby"
#   id = "/teachers/8f403e29-2a65-643e-6fac-5ccb53000db2" if step_arg == "Bert"
#   id = "/teachers/11111111-1111-1111-1111-111111111111" if step_arg == "Invalid"
#   id = "/teachers/2B5AB1CC-F082-46AA-BE47-36A310F6F5EA" if step_arg == "Unknown"
#   id = "/teacher/11111111-1111-1111-1111-111111111111"  if step_arg == "WrongURI"
#   id = "/teachers"                                      if step_arg == "NoGUID" or step_arg == nil
#   id
# end


Given /^the "name" is "([^\"]+)" "([^\"]+)" "([^\"]+)"$/ do |first, mid, last|
  first.should_not == nil
  last.should_not == nil
  if !defined? @data
    @data = {}
  end
  @data["name"] = { "firstName" => first, "middleName" => mid, "lastSurname" => last };
end

Given /^the "([^\"]+)" is "([^\"]+)"$/ do |key, value|
  if !defined? @data
    @data = {}
  end
  value = convert(value) if key != "staffUniqueStateId"
  @data[key] = value
end

Given /^the "([^\"]+)" status is "([^\"]+)"$/ do |key, value|
  step "the \"#{key}\" is \"#{value}\""
end

When /^I set the "([^\"]*)" status to "([^\"]*)"$/ do |key, value|
  #step "the \"#{key}\" is \"#{value}\""
  value = convert(value)
  @result[key] = value
end

When /^I navigate to POST "([^\"]+)"$/ do |url|
  data = prepareData(@format, @data)
  restHttpPost(url, data)
  assert(@res != nil, "Response from rest-client POST is nil")
end

When /^I navigate to PUT "([^\"]*)"$/ do |url|
  @result = {} if !defined? @result 
  data = prepareData(@format, @result)
  restHttpPut(url, data)
  assert(@res != nil, "Response from rest-client PUT is nil")
end


Then /^the "name" should be "([^\"]*)" "([^\"]*)" "([^\"]*)"$/ do |first, mid, last|
  assert(@result["name"] != nil, "Name is nil")
  @result["name"]["firstName"].should == first
  @result["name"]["lastSurname"].should == last
  if last || last.len > 0
    @result["name"]["middleName"].should == mid
  else
    @result["name"]["middleName"].should == nil
  end
end

Then /^the "([^\"]*)" should be "([^\"]*)"$/ do |key, value|
  value = convert(value) if key != "staffUniqueStateId"
  @result[key].should_not == nil
  @result[key].should == value
end

Given /^the "([^\"]+)" status should be "([^\"]+)"$/ do |key, value|
  step "the \"#{key}\" should be \"#{value}\""
end




