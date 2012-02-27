require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../common.rb'


Transform /^([^"]*)<([^"]*)>$/ do |arg1, arg2|
  id = arg1+"dd9165f2-65fe-4e27-a8ac-bec5f4b757f6" if arg2 == "'Mathematics Achievement Assessment Test' ID"
  id = arg1+"29f044bd-1449-4fb7-8e9a-5e2cf9ad252a" if arg2 == "'Mathematics Assessment 2' ID"
  id = arg1+"542b0b38-ea57-4d81-aa9c-b55a629a3bd6" if arg2 == "'Mathematics Assessment 3' ID"
  id = arg1+"6c572483-fe75-421c-9588-d82f1f5f3af5" if arg2 == "'Writing Advanced Placement Test' ID"
  id = arg1+"df897f7a-7ac4-42e4-bcbc-8cc6fd88b91a" if arg2 == "'Writing Assessment II' ID"
  id = arg1+"11111111-1111-1111-1111-111111111111" if arg2 == "'NonExistentAssessment' ID" or arg2 == "'WrongURI' ID"
  id = arg1+@newId                                 if arg2 == "'newly created assessment' ID"
  id = arg1                                        if arg2 == "'NoGUID' ID" 
  id
end

Transform /^([^"]*)<([^"]*)>\/targets$/ do |arg1, arg2|
  id = arg1+"6c572483-fe75-421c-9588-d82f1f5f3af5/targets" if arg2 == "'Writing Advanced Placement Test' ID"
  id
end

Given /^"([^"]*)" is "([^"]*)"$/ do |key, value|
  @data = {} if !defined? @data
  if key == "assessmentIdentificationCode"
    @data[key] = [Hash["identificationSystem"=>"School","ID"=>value]]
  elsif key == "version"
    @data[key] = Integer(value)
  else
    @data[key] = value
  end
end

Then /^"([^"]*)" should be "([^"]*)"$/ do |key, value|
  if key == "assessmentIdentificationCode"
    assert(@result[key][0]['ID'] == value, "Expected value of #{value} but received #{@result[key][0]['ID']} in the response body")
  elsif key == "version"
    assert(@result[key] == Integer(value),"Expected value of #{value} but received #{@result[key]}")
  else
    assert(@result[key] == value, "Expected value of #{value} but received #{@result[key]}")
  end
end

When /^I set the "([^"]*)" to "([^"]*)"$/ do |key, value|
  @result[key] = value
end


When /^I navigate to POST "([^"]*)"$/ do |arg1|
  if @format == "application/json"
    data = @data.to_json 
  elsif @format == "application/xml"
    builder = Builder::XmlMarkup.new(:indent=>2)   
  else
    assert(false, "Unsupported MIME type")
  end

  restHttpPost(arg1, data)    
  assert(@res != nil, "Response from rest-client POST is nil")

end


When /^I navigate to PUT "([^"]*<[^"]*>)"$/ do |url|
  data = prepareData(@format, @result)
  restHttpPut(url, data)
  assert(@res != nil, "Response from rest-client PUT is nil")
  assert(@res.body == nil || @res.body.length == 0, "Response body from rest-client PUT is not nil")
end


When /^I attempt to update "([^"]*<[^"]*>)"$/ do |url|
  @result = {}
  data = prepareData(@format, @result)
  restHttpPut(url, data)
  assert(@res != nil, "Response from rest-client PUT is nil")
end
