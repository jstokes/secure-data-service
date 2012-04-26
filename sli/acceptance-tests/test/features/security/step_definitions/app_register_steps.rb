require_relative '../../utils/sli_utils.rb'

Transform /^([^"]*)<([^"]*)>$/ do |arg1, arg2|
  id = arg1+"deb9a9d2-771d-40a1-bb9c-7f93b44e51df" if arg2 == "Testing App"
  id = arg1+@newId       if arg2 == "New App ID"
  #id = step_arg if id == nil
  id
end

When /^I navigate to POST "([^"]*)"$/ do |arg1|
  @format = "application/json"
  dataObj = DataProvider.getValidAppData()
  dataObj.delete "registered"
  data = prepareData("application/json", dataObj)


  restHttpPost(arg1, data)

  assert(@res != nil, "Response from POST operation was null")
end

Then /^I should receive the data for the specified application entry$/ do
  result = JSON.parse(@res.body)
  assert(result != nil, "Result of JSON parsing is nil")
  @client_secret = result["client_secret"]
  @client_id = result["client_id"]
  @registration = result["registration"]
end

When /^I navigate to PUT "([^"]*)"$/ do |arg1|
  @format = "application/json"
  dataObj = DataProvider.getValidAppData()
  dataObj["description"] = "New and Improved"
  dataObj["client_secret"] = @client_secret
  dataObj["client_id"] = @client_id
  dataObj["registration"]  = @registration
  data = prepareData("application/json", dataObj)

  restHttpPut(arg1, data)

  assert(@res != nil, "Response from PUT operation was null")
end

Then /^I should no longer be able to get that application's data'$/ do
  @format = "application/json"
  restHttpGet("/apps/#{@newId}")
  
  assert(@res != nil, "Response from PUT operation was null")
  assert(@res.code == 404, "Return code was not expected: "+@res.code.to_s+" but expected 404")
end

When /^I POST an application specifying an invalid field$/ do
  @format = "application/json"
  dataObj = DataProvider.getValidAppData()
  dataObj["foo"] = "A Bar App"
  data = prepareData("application/json", dataObj)

  restHttpPost("/apps/", data)

  assert(@res != nil, "Response from POST operation was null")
end

When /^I POST an application specifying the auto\-generated field "([^"]*)"$/ do |arg1|
  @format = "application/json"
  dataObj = DataProvider.getValidAppData()
  dataObj[arg1] = "fm67sH6vZZ"
  data = prepareData("application/json", dataObj)

  restHttpPost("/apps/", data)

  assert(@res != nil, "Response from POST operation was null")
end

When /^I PUT an application updating the auto\-generated field "([^"]*)"$/ do |arg1|
  @format = "application/json"
  uri = "/apps/deb9a9d2-771d-40a1-bb9c-7f93b44e51df"
  restHttpGet(uri)
  result = JSON.parse(@res.body)
  assert(result != nil, "Result of JSON parsing is nil")
  
  prev_client_secret = result["client_secret"]
  prev_client_id = result["client_id"] 

  dataObj = DataProvider.getValidAppData()
  dataObj["client_secret"] = prev_client_secret 
  dataObj["client_id"] = prev_client_id 
  dataObj["client_secret"] = "thisIsAReallyBadLongClientSecretYarightorlyyarly" if arg1 == "client_secret"
  dataObj["client_id"] = "badclientId" if arg1 == "client_id"

  data = prepareData("application/json", dataObj)

  restHttpPut(uri, data)

  assert(@res != nil, "Response from PUT operation was null")
end

Then /^it should be "([^"]*)"$/ do |arg1|
  app = JSON.parse(@res.body)
  assert(app["registered"] === arg1, "Registered field should be #{arg1}, not #{app["registered"]}")
end

Then /^I should only see "([^"]*)" applications$/ do |arg1|
  apps = JSON.parse(@res.body)
  apps.each do |app|
    assert(app["registered"] === arg1, "App #{app["name"]} should be #{arg1}")
  end
end

When /^I navigate to PUT "([^"]*)" to update an application's name$/ do |arg1|
  @format = "application/json"
  restHttpGet(arg1)
  result = JSON.parse(@res.body)
  assert(result != nil, "Result of JSON parsing is nil")
  
  prev_client_secret = result["client_secret"]
  prev_client_id = result["client_id"] 

  dataObj = DataProvider.getValidAppData()
  dataObj["name"] = "Waffle App"
  data = prepareData("application/json", dataObj)

  restHttpPut(uri, data)

  assert(@res != nil, "Response from PUT operation was null")
end