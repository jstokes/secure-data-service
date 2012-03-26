
Given /^I am logged in using "([^\"]*)" "([^\"]*)" to realm "([^\"]*)"$/ do |user, pass, realm|
  @user = user
  @passwd = pass
  @realm = realm
  idpRealmLogin(@user, @passwd, @realm)
  assert(@sessionId != nil, "Session returned was nil")
end

Given /^format "([^\"]*)"$/ do |fmt|
  ["application/json", "application/xml", "text/plain", "application/vnd.slc.full+json", "application/vnd.slc+json"].should include(fmt)
  @format = fmt
end

Then /^I should receive a return code of (\d+)$/ do |arg1|

  ###### Remove eventually
  if(@res.code == 403 && Integer(arg1) != 403)
    url = PropLoader.getProps['api_server_url']+"/api/rest/system/session/debug"
    tempResponse = RestClient.get(url, {:accept => "application/json", :sessionId => @sessionId}){|response, request, result| response }
    puts "#############################################", tempResponse.body, "##########################################################"
  end

  assert(@res.code == Integer(arg1), "Return code was not expected: "+@res.code.to_s+" but expected "+ arg1)
end

Then /^I should receive an ID for the newly created ([\w-]+)$/ do |entity|
  headers = @res.raw_headers
  assert(headers != nil, "Headers are nil")
  assert(headers['location'] != nil, "There is no location link from the previous request")
  s = headers['location'][0]
  @newId = s[s.rindex('/')+1..-1]
  assert(@newId != nil, "After POST, #{entity} ID is nil")
end

When /^I navigate to GET "([^\"]*)"$/ do |uri|
  if defined? @queryParams
    uri = uri + "?#{@queryParams.join('&')}"
  end
  restHttpGet(uri)
  assert(@res != nil, "Response from rest-client GET is nil")
  assert(@res.body != nil, "Response body is nil")
  contentType = contentType(@res)
  jsonTypes = ["application/json", "application/vnd.slc.full+json", "application/vnd.slc+json"].to_set
  if jsonTypes.include? contentType
    @result = JSON.parse(@res.body)
    assert(@result != nil, "Result of JSON parsing is nil")
  elsif /application\/xml/.match contentType
    doc = Document.new @res.body
    @result = doc.root
    puts @result
  else
    @result = {}
  end
end

When /^I navigate to DELETE "([^"]*)"$/ do |uri|
  restHttpDelete(uri)
  assert(@res != nil, "Response from rest-client DELETE is nil")
end

Then /^I should receive a link named "([^"]*)" with URI "([^"]*)"$/ do |rel, href|
  assert(@result.has_key?("links"), "Response contains no links")
  found = false
  @result["links"].each do |link|
    if link["rel"] == rel && link["href"] =~ /#{Regexp.escape(href)}$/
      found = true
    end
  end
  assert(found, "Link not found rel=#{rel}, href ends with=#{href}")
end

When /^I PUT the entity to "([^"]*)"$/ do |url|
  data = prepareData(@format, @result)
  restHttpPut(url, data)
  assert(@res != nil, "Response from rest-client PUT is nil")
  assert(@res.body == nil || @res.body.length == 0, "Response body from rest-client PUT is not nil")
end

When /^I POST the entity to "([^"]*)"$/ do |url|
  data = prepareData(@format, @result)
  restHttpPost(url, data)
  assert(@res != nil, "Response from rest-client POST is nil")
end

