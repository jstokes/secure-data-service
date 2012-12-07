=begin

Copyright 2012 Shared Learning Collaborative, LLC

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
require_relative '../../utils/common_stepdefs.rb'
require_relative '../../apiV1/utils/api_utils.rb'
require 'socket'

############################### Before Scenario Do ################################

Before('@clearIndexer') do |scenario|
  # Import tenant just in case it's empty in later extracts
  step 'I import into tenant collection'
end

###############################  After Scenario Do ###############################
# Clear Elastic Search Indexer
# Clear student and section collection from mongo
After('@clearIndexer') do |scenario|
  # we might not need to clear indexer, as extract command will clear it
  step 'I DELETE to clear the Indexer'
  #undo the import in Before scenario
  step 'I clear the tenants that I previously imported'
end
###################################################################################

Given /^I send a command to start the extractor to extract now$/ do
  hostname = PropLoader.getProps['elastic_search_host']
  port = PropLoader.getProps['elastic_search_remote_command_port']
  socket = TCPSocket.open(hostname, port)
  socket.write("extract")
  socket.close  
end

Given /^I DELETE to clear the Indexer$/ do
  # we don't need a token for search calls
  @sessionId = ""
  @format = "application/json;charset=utf-8"
  url = PropLoader.getProps['elastic_search_address'] 
  restHttpDeleteAbs(url)
  assert(@res != nil, "Response from rest-client POST is nil")
  puts @res
end

Given /^I flush the Indexer$/ do
  @format = "application/json;charset=utf-8"
  url = PropLoader.getProps['elastic_search_address'] + "/_flush"
  restHttpPostAbs(url)
  assert(@res != nil, "Response from rest-client POST is nil")
  puts @res
end

# Current not being used
Given /^I import some student data$/ do
  entityData = {
      "student1" => {
      "birthData" => {
        "birthDate" => "1994-04-04"
      },
      "sex" => "Male",
      "studentUniqueStateId" => "123456",
      "economicDisadvantaged" => false,
      "name" => {
        "firstName" => "Alex",
        "middleName" => "John",
        "lastSurname" => "Doe"
      }
    },
    "student2" => {
      "birthData" => {
        "birthDate" => "1994-04-04"
      },
      "sex" => "Male",
      "studentUniqueStateId" => "9876",
      "economicDisadvantaged" => true,
      "name" => {
        "firstName" => "Matt",
        "middleName" => "Steve",
        "lastSurname" => "Moe"
      }
    }
  }
  entityData.each do |key, value|
    @fields = value
    step "format \"application/vnd.slc+json\""
    step "I navigate to POST \"/students\""
    step "I should receive a return code of 201"
  end 
end

Given /^I drop Invalid Files to Inbox Directory$/ do
  @srcFileName = "02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a_student_1.json"
  src = "test/features/search/test_data/" + @srcFileName
  fileCopy(src)
end

Given /^I drop Valid Files to Inbox Directory$/ do
  @srcFileName = "02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a_student_2.json"
  src = "test/features/search/test_data/" + @srcFileName
  fileCopy(src)
end

Then /^I drop another Valid File to the Inbox Directory$/ do
  @srcFileName = "02f7abaa9764db2fa3c1ad852247cd4ff06b2c0a_student_3.json"
  src = "test/features/search/test_data/" + @srcFileName
  fileCopy(src)
end

Then /^I should see the file has been prcoessed$/ do
  destPath = PropLoader.getProps['elastic_search_inbox'] + "/" + @srcFileName
  current = 0
  finished = false
  while (!finished && current < 5)
    if (!File.exists?(destPath))
      finished = true
    end
    sleep 1  
    current +=1
  end
  assert(finished, "#{destPath} exists in Inbox")
end

Given /^I should see the file has not been processed$/ do
  destPath = PropLoader.getProps['elastic_search_inbox'] + "/" + @srcFileName
  current = 0
  finished = false
  while (!finished || current < 2)
    if (File.exists?(destPath))
      finished = true
    end
    sleep 1  
    current +=1
  end
  assert(finished, "#{destPath} still exists in Inbox")
end

Then /^Indexer should have "(.*?)" entities$/ do |numEntities|
  verifyElasticSearchCount(numEntities)
end

Given /^I check that Elastic Search is non\-empty$/ do
  verifyElasticSearchCount()
end

Given /^I search in Elastic Search for "(.*?)" in tenant "(.*?)"$/ do |query, tenant|
  url = PropLoader.getProps['elastic_search_address'] + "/" + convertTenantIdToDbName(tenant) + "/_search?q=" + query
  restHttpGetAbs(url)
  assert(@res != nil, "Response from rest-client GET is nil")
end

Given /^"(.*?)" hit is returned$/ do |expectedHits|
  result = JSON.parse(@res.body)
  hits = result["hits"]["total"]
  assert(expectedHits.to_i == hits.to_i, "Expected: #{expectedHits} Actual: #{hits}")
end

Given /^I search in API for "(.*?)"$/ do |query|
  url = PropLoader.getProps["api_server_url"] + "/api/rest/v1/search/students?q=" + query
  restHttpGetAbs(url)
  assert(@res != nil, "Response from rest-client GET is nil")  
  @result = JSON.parse(@res.body)
end

Given /^I see the following fields:$/ do |table|
  @table = table
  json = JSON.parse(@res.body)
  arrayOfHits = json["hits"]["hits"]
  verifyElementsOnResponse(arrayOfHits, @table)   
end

Then /^I see the following search results at index (\d+):$/ do |index, table|
  json = JSON.parse(@res.body)
  @table = table
  verifyElementsOnResponseAtIndex(index, json, @table)
end

Then /^no search results are returned$/ do
  response = JSON.parse(@res.body)
  assert(response == [], "Error: Unauthorized student data is accessible")  
end

Given /^I import into tenant collection$/ do
  tenants = ["Midgar", "Hyrule"]
  dbname = PropLoader.getProps["api_database_name"]
  db = Mongo::Connection.new(PropLoader.getProps["DB_HOST"])[dbname]
  tenants.each do |tenant|
    doc = generateTenantDoc(tenant)
    db['tenant'].insert(doc) if db['tenant'].find_one({"body.tenantId" => tenant}).nil?
  end
end

Given /^I clear the tenants that I previously imported$/ do
  tenants = ["Midgar", "Hyrule"]
  dbname = PropLoader.getProps["api_database_name"]
  db = Mongo::Connection.new(PropLoader.getProps["DB_HOST"])[dbname]
  tenants.each do |tenant|
    id = convertTenantIdToDbName(tenant)
    db['tenant'].remove("_id" => id)
  end
end

############ sorting-step utils for pagination ##########
Then /^the header "([^\"]*)" equals (\d+)$/ do |header, value|
  value = convert(value)
  header.downcase!
  headers = @res.raw_headers
  headers.should_not == nil
  assert(headers[header])
  headers[header].should_not == nil
  resultValue = headers[header]
  resultValue.should be_a Array
  resultValue.length.should == 1
  singleValue = convert(resultValue[0])
  singleValue.should == value
end

Then /^the a next link exists with offset equal to (\d+) and limit equal to (\d+)$/ do |start, max|
  links = @res.raw_headers["link"];
  links.should be_a Array
  found_link = false
  links.each do |link|
    if /rel=next/.match link
      assert(Regexp.new("offset=" + start).match(link), "offset is not correct: #{link}")
      assert(Regexp.new("limit=" + max).match(link), "limit is not correct: #{link}")
      found_link = true
    end
  end
  found_link.should == true
end

Then /^the a previous link exists with offset equal to (\d+) and limit equal to (\d+)$/ do |start, max|
  links = @res.raw_headers["link"];
  links.should be_a Array
  found_link = false
  links.each do |link|
    if /rel=prev/.match link
      assert(Regexp.new("offset=" + start).match(link), "offset is not correct: #{link}")
      assert(Regexp.new("limit=" + max).match(link), "limit is not correct: #{link}")
      found_link = true
    end
  end
  found_link.should == true
end



###### End of sorting-step utils for pagination ##########

def fileCopy(sourcePath, destPath = PropLoader.getProps['elastic_search_inbox'])
  assert(destPath != nil, "Destination path is nil")
  assert(sourcePath != nil, "Source path is nil")
  
  if !File.directory?(destPath)
    FileUtils.mkdir_p(destPath)
  end
  FileUtils.cp sourcePath, destPath  
end

def verifyElementsOnResponseAtIndex(index, arrayOfElements, table)
  response = arrayOfElements.at(index.to_i)
  table.hashes.each do |row|
    field = row["Field"]
    currentRes = response
    value = nil
    while (field.include? ".")
      delimiter = field.index('.') + 1
      length = field.length - delimiter      
      current = field[0..delimiter-2]        
      field = field[delimiter,length]  
      currentRes = currentRes[current]   
    end          
    value = currentRes[field]
    assert(value == row["Value"], "Expected #{row["Value"]} Actual #{value}" )
  end
end

def verifyElementsOnResponse(arrayOfElements, table)
  arrayOfElements.each do |response|
    table.hashes.each do |row|
      field = row["Field"]
      currentRes = response
      value = nil
      while (field.include? ".")
        delimiter = field.index('.') + 1
        length = field.length - delimiter      
        current = field[0..delimiter-2]        
        field = field[delimiter,length]  
        currentRes = currentRes[current]   
      end          
      value = currentRes[field]
      assert(value == row["Value"], "Expected #{row["Value"]} Actual #{value}" )
    end
  end
end

def verifyElasticSearchCount(numEntities = -1)
  # set sessionId to be non-nil
  @sessionId = ""
  max = 10
  done = false
  numTries = 0
  indexCount = 0
  sleep 2
  while (numTries < max && !done)
    url = PropLoader.getProps['elastic_search_address'] + "/_count"
    restHttpGetAbs(url)
    assert(@res != nil, "Response from rest-client POST is nil")
    puts @res
    result = JSON.parse(@res.body)
    indexCount = result["count"]
    if (indexCount == nil)
      indexCount = 0
    end
    if (indexCount.to_i == numEntities.to_i || (numEntities == -1 && indexCount.to_i > 0))
      done = true
    else
      numTries+=1
      sleep 1
    end
  end
  assert(done, "Expected #{numEntities} Actual #{indexCount}")
end

def generateTenantDoc(tenantName)
  doc = {
    "_id" => convertTenantIdToDbName(tenantName),
    "type" => "tenant",
    "body" => {
      "tenantId" => tenantName,
      "dbName" => convertTenantIdToDbName(tenantName)
      },
    "metaData" => {
        "tenantId" => tenantName
      }
  }
end


