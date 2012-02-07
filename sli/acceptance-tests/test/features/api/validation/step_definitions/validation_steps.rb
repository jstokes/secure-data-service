require 'rest-client'
require 'json'
require_relative '../../../utils/sli_utils.rb'

# transform <Place Holder Id>
Transform /^<(.+)>$/ do |template|
  id = template
  id = @newId.to_s                            if template == "'Previous School' ID"
  id = "344cf68d-50fd-8dd7-e8d6-ed9df76c219c" if template == "'Belle' ID"
  id = "eb3b8c35-f582-df23-e406-6947249a19f2" if template == "'Apple Alternative Elementary School' ID"
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

def createXlengthString(x)
  value = ""
  x.times {value << 'a'}
  value
end

Given /^I create a valid base level student object$/ do
  @result = CreateEntityHash.createBaseStudent()
end

Given /^I create a valid base level school object$/ do
  @result = CreateEntityHash.createBaseSchool()
end

Given /^I create a blank json object$/ do
  @result = Hash[]
end

When /^I navigate to POST "([^"]*)"$/ do |arg1|
  if @format == "application/json"
    data = @result.to_json
  elsif @format == "application/xml"
    data = @result
  else
    assert(false, "Unsupported MIME type")
  end
  restHttpPost(arg1, data)
  assert(@res != nil, "Response from rest-client POST is nil")
end

Given /^I create a student object with "([^"]*)" set to Guy$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
  @result['sex'] = 'Guy'
end

Given /^I create a student object with sex equal to "([^"]*)" instead of "([^"]*)"$/ do |arg1, arg2|
  @result = CreateEntityHash.createBaseStudent()
  @result['sex'] = arg1
end

Given /^I create a create a school object with "([^"]*)" set to a single map$/ do |arg1|
  @result = CreateEntityHash.createBaseSchool()
  @result[arg1] = Hash["streetNumberName" => "123 Elm Street",
                       'city'=>"New York",
                       "stateAbbreviation" => "NY",
                       "postalCode" => "12345"
                       ]
end

Given /^I create the same school object with "([^"]*)" as an array with the same map$/ do |arg1|
  @result = CreateEntityHash.createBaseSchool()
  @result[arg1] = [
                    Hash["streetNumberName" => "123 Elm Street",
                         'city'=>"New York",
                         "stateAbbreviation" => "NY",
                         "postalCode" => "12345"
                         ]
                  ]
end


Given /^I create a student object with "([^"]*)" set to an array of names$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
  @result[arg1] = [@result[arg1]]
end

Given /^I create the same student object with "([^"]*)" as a map with the same data$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
end

Given /^an SSA object is valid except for "([^"]*)"$/ do |arg1|
  @result = Hash[
                "studentId" => "714c1304-8a04-4e23-b043-4ad80eb60992",
                "entryGradeLevel" => "EIGHTH_GRADE"
                ]
  @result[arg1] = "11111111-1111-1111-1111-111111111111"
end

Given /^I create a student object with "learningStyles.([^"]*)" equal to a string$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
  @result["learningStyles"][arg1] = "no"
end

Given /^I create a student object with "([^"]*)" equal to a integer$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
  @result[arg1] = 12345678
end

Given /^I create a school object with "([^"]*)" equal to a (\d+) character string$/ do |arg1, arg2|
  @result = CreateEntityHash.createBaseSchool()
  @result[arg1] = createXlengthString(Integer(arg2))
end

Given /^I create a student object with "([^"]*)" set to a true string$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
  @result[arg1] = "true"
end

Given /^I create a student object with "([^"]*)" set to MM\-DD\-YYYY$/ do |arg1|
  @result = CreateEntityHash.createBaseStudent()
  @result['birthData'][arg1] = "01-01-2012"
end

When /^I navigate to PUT "([^\"]*)"$/ do |url|
  @result = {} if !defined? @result
  data = prepareData(@format, @result)
  restHttpPut(url, data)
  assert(@res != nil, "Response from rest-client PUT is nil")
end

When /^I create a blank request body object$/ do
  @result = {}
end

When /^I create a base school object$/ do
  @result = CreateEntityHash.createBaseSchool()
end

Then /^"([^\"]*)" should be "([^\"]*)"$/ do |key, value|
  @result[key].should_not == nil
  @result[key].should == value
  @result.delete(key)
end

Then /^"([^"]*)" should contain "([^"]*)" and "([^"]*)"$/ do |key, val1, val2|
  @result[key].should_not == nil
  assert(@result[key].find(val1) != nil, "School's gradesOffered does not contain #{val1}")
  assert(@result[key].find(val2) != nil, "School's gradesOffered does not contain #{val2}")
  @result.delete(key)
end

Then /^there should be no other contents in the response body other than links$/ do
  @result.delete('links')
  @result.delete('id')
  @result.delete('address')
  @result.delete('organizationCategories')
  @result.delete('schoolCategories')
  assert(@result == {}, "The response body still contains data that was previously there but *not* in the PUT data")
end

Then /^a collection of size (\d+)$/ do |arg1|
  assert(@result != nil, "Result collection was nill")
  assert(@result.is_a?(Array), "Result collection was not an Array")
  assert(@result.size == Integer(arg1), "Result collection was not of size #{arg1}")
end
