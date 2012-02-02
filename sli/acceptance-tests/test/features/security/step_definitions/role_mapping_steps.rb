require 'json'

require_relative '../../utils/sli_utils.rb'

Transform /^realm "([^"]*)"$/ do |arg1|
  id = "eb4d7e1b-7bed-890a-d574-8da22127fd2d" if arg1 == "SLI"
  id = "eb4d7e1b-7bed-890a-d974-8da22127fd2d" if arg1 == "Fake Realm"
  id = "eb4d7e1b-7bed-890a-d5b4-8da22127fd2d" if arg1 == "Another Realm"
end

Given /^I have not yet authenticated$/ do
  @sessionId = nil
end

When /^I make a call to get the list of realms$/ do
  restHttpGet("/pub/realms","application/json")
  assert(@res != nil, "Response from rest-client GET is nil")
end

Then /^I should see a response that contains the list of realms$/ do
  assert(@res.code == 200, "Return code was not expected: "+@res.code.to_s+" but expected 200")
  @result = JSON.parse(@res.body)
  assert(@result != nil, "Result of JSON parsing is nil")
end

Then /^I should see a URL for each realm that links to their IDP$/ do
  @result.each do |item|
    assert(item["idp"] != nil, "Realm "+item["state"]+" URL was not found.")
  end
end

Then /^I should not see any data about any realm's role\-mapping$/ do
  @result.each do |item|
    assert(item["mappings"] == nil, "Realm "+item["state"]+" Role mapping info was found.")
  end
end

When /^I try to access the URI "([^"]*)" with operation "([^"]*)"$/ do |arg1, arg2|
  @format = "application/json"
  data = "{\"mappings\": {},\"name\": \"Aggregate Viewer\",\"rights\": [\"AGGREGATE_READ\",\"READ_GENERAL\"]}"

  restHttpPost(arg1, data) if arg2 == "POST"
  restHttpGet(arg1) if arg2 == "GET"
  restHttpPut(arg1, data) if arg2 == "PUT"
  restHttpDelete(arg1) if arg2 == "DELETE"
end

Then /^I should be denied access$/ do
  assert(@res.code == 403, "Return code was not expected: "+@res.code.to_s+" but expected 403")
end

Then /^I should see a valid object returned$/ do
  result = JSON.parse(@res.body)
  assert(result != nil, "Result of JSON parsing is nil")
end

When /^I POST a new realm$/ do
  data = {}
  data["state"] = "Test State"
  data["idp"]  =  "http://devdanil.slidev.org:8080/idp"
  dataFormatted = prepareData("application/json", data)
  restHttpPost("/realm", dataFormatted)
  assert(result != nil, "Response from rest-client POST is nil")
end

Then /^I should receive a new ID for my new realm$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I GET a list of realms$/ do
  restHttpGet("/realm")
  assert(@res != nil, "Response from rest-client GET is nil")
end

Then /^I should see a list of valid realm objects$/ do
  assert(@res.code == 200, "Return code was not expected: "+@res.code.to_s+" but expected 200")
  result = JSON.parse(@res.body)
  assert(result != nil, "Result of JSON parsing is nil")

  result.each do |item|
    assert(item["idp"] != nil, "Realm "+item["state"]+" URL was not found.")
  end
end

When /^I GET a specific (realm "[^"]*")$/ do |arg1|
  restHttpGet("/realm/" + arg1)
  assert(@res != nil, "Response from rest-client GET is nil")
end

When /^I PUT to change the (realm "[^"]*") to add a mapping between default role "([^"]*)" to role "([^"]*)"$/ do |arg1, arg2, arg3|
  restHttpGet("/realm/" + arg1) 
  assert(@res != nil, "Response from rest-client GET is nil")
  data = json.parse(@res.body)
  curMappings = data["mappings"]
  curMappings[arg2].push arg3
  data["mappings"] = curMappings
  dataFormatted = prepareData("application/json", data)
  restHttpPut("/realm/" + arg1, dataFormatted)
  assert(@res != nil, "Response from rest-client PUT is nil")
  assert(@res.code == 204, "Return code was not expected, got " + @res.code + " but expected 204")
end

When /^I DELETE the (realm "[^"]*")$/ do |arg1|
  restHttpDelete("/realm/" + arg1)
  assert(@res != nil, "Response from rest-client DELETE is nil")
end

When /^I add a mapping between default role "([^"]*)" and custom role "([^"]*)" for realm "([^"]*)"$/ do |arg1, arg2, arg3|
  pending # express the regexp above with the code you wish you had
end
