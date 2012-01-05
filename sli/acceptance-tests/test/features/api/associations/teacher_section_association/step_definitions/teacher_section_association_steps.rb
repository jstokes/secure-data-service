require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../../utils/sli_utils.rb'

Transform /([^\/]*)\/<([^>]*)>$/ do |arg1, arg2|
  uri = arg1+"\/eb424dcc-6cff-a69b-c1b3-2b1fc86b2c94" if arg2 == "Ms. Jones' ID"
  uri = arg1+"\/e24b24aa-2556-994b-d1ed-6e6f71d1be97" if arg2 == "Ms. Smith's ID"
  uri = arg1+"\/58c9ef19-c172-4798-8e6e-c73e68ffb5a3" if arg2 == "Algebra II ID"
  uri = arg1+"\/1e1cdb04-2094-46b7-8140-e3e481013480" if arg2 == "Chem I ID"
  uri = arg1+"\/5c4b1a9c-2fcd-4fa0-b21c-f867cf4e7431" if arg2 == "Physics II ID"
  uri = arg1+"\/4efb4262-bc49-f388-0000-0000c9355700" if arg2 == "Biology III ID"
  uri = arg1+"\/12f25c0f-75d7-4e45-8f36-af1bcc342871" if arg2 == "Association between Ms. Jones and Algebra II ID"
  uri = arg1+"" if arg2 == ""
  uri = arg1+arg2 if uri == nil
  uri
end

Transform /^href ends with "([^"]*)<([^"]*)>"$/ do |arg1, arg2|
  uri = arg1+"122a340e-e237-4766-98e3-4d2d67786572" if arg2 == "Alfonso at Apple Alternative Elementary School ID"
  uri = arg1+"53ec02fe-570b-4f05-a351-f8206b6d552a" if arg2 == "Gil at Apple Alternative Elementary School ID"
  uri = arg1+"4ef1498f-5dfc-4604-83c3-95e81146b59a" if arg2 == "Sybill at Apple Alternative Elementary School ID"
  uri = arg1+"6de7d3b6-54d7-48f0-92ad-0914fe229016" if arg2 == "Alfonso at Yellow Middle School ID"
  uri = arg1+"714c1304-8a04-4e23-b043-4ad80eb60992" if arg2 == "Alfonso's ID"
  uri = arg1+"eb3b8c35-f582-df23-e406-6947249a19f2" if arg2 == "Apple Alternative Elementary School ID"
  uri = arg1+"eb424dcc-6cff-a69b-c1b3-2b1fc86b2c94" if arg2 == "Ms. Jones' ID"
  uri = arg1+"58c9ef19-c172-4798-8e6e-c73e68ffb5a3" if arg2 == "Algebra II ID"
  uri = arg1+"12f25c0f-75d7-4e45-8f36-af1bcc342871" if arg2 == "Association between Ms. Jones and Algebra II ID"
  uri = arg1+"" if arg2 == ""
  uri = arg1+arg2 if uri == nil
  uri
end

Given /^I am logged in using "([^"]*)" "([^"]*)"$/ do |arg1, arg2|
  @user = arg1
  @passwd = arg2
end

Given /^that I have admin access$/ do
  idpLogin(@user,@passwd)
  assert(@sessionId != nil, "Session returned was nil")
end

Given /^format "([^"]*)"$/ do |fmt|
  @format = fmt
end

Given /^"([^"]*)" is "([^"]*)"$/ do |key, value|
  if !defined? @fields
    @fields = {}
  end
  @fields[key] = value
end

Then /^I should receive a return code of (\d+)$/ do |code|
  assert(@res.code == Integer(code), "Return code was not expected: #{@res.code.to_s} but expected #{code}\nbody was #{@res}")
end

Then /^I should receive a link where rel is "([^"]*)" and (href ends with "[^"]*")$/ do |rel, href|
  assert(@data != nil, "Response contains no data")
  assert(@data.is_a?(Hash), "Response contains #{@data.class}, expected Hash")
  assert(@data.has_key?("links"), "Response contains no links")
  found = false
  @data["links"].each do |link|
    if link["rel"] == rel && link["href"] =~ /#{Regexp.escape(href)}$/
      found = true
    end
  end
  assert(found, "Link not found rel=#{rel}, href ends with=#{href}")
end

Then /^"([^"]*)" should equal "([^"]*)"$/ do |key, value|
  assert(@data != nil, "Response contains no data")
  assert(@data.is_a?(Hash), "Response contains #{@data.class}, expected Hash")
  assert(@data.has_key?(key), "Response does not contain key #{key}")
  assert(@data[key].to_s == value, "Expected #{key} to equal #{value}, received #{@data[key]}")
end

When /^I navigate to POST "([^"]*)"$/ do |uri|
  if @format == "application/json"
    dataH = @fields
    data=dataH.to_json
  elsif @format == "application/xml"
    assert(false, "application/xml is not supported")
  else
    assert(false, "Unsupported MIME type")
  end
  restHttpPost(uri, data)
  assert(@res != nil, "Response from rest-client POST is nil")
end

When /^I navigate to GET "([^"]*)"$/ do |uri|
  restHttpGet(uri)
  assert(@res != nil, "Response from rest-client GET is nil")
  if @format == "application/json"
    begin
      @data = JSON.parse(@res.body);
    rescue
      @data = nil
    end
  elsif @format == "application/xml"
    assert(false, "XML not supported yet")
  else
    assert(false, "Unsupported MediaType")
  end
end

When /^Teacher is <Ms. Smith's ID>$/ do 
  step "\"teacherId\" is \"e24b24aa-2556-994b-d1ed-6e6f71d1be97\""
end

When /^Section is <Algebra II>$/ do
  step "\"sectionId\" is \"58c9ef19-c172-4798-8e6e-c73e68ffb5a3\""
end

Given /^the BeginDate is "([^"]*)"$/ do |arg1|
  step "\"beginDate\" is \"" + arg1 + "\""
end

Given /^the EndDate is "([^"]*)"$/ do |arg1|
  step "\"endDate\" is \"" + arg1 + "\""
end

Given /^the ClassroomPosition is "([^"]*)"$/ do |arg1|
  position = arg1 
  position = "TEACHER_OF_RECORD" if arg1 == "teacher of record"
  step "\"classroomPosition\" is \"#{position}\""
end

Then /^I should receive a ID for the newly created teacher\-section\-association$/ do
  headers = @res.raw_headers
  headers.should_not be_nil
  headers['location'].should_not be_nil
  s = headers['location'][0]
  @newSectionId = s[s.rindex('/')+1..-1]
  @newSectionId.should_not be_nil
end

Then /^the EndDate should be "([^"]*)"$/ do |arg1|
  step "\"endDate\" is \"#{arg1}\""
end

Then /^the BeginDate should be "([^"]*)"$/ do |arg1|
  step "\"beginDate\" is \"#{arg1}\""
end

Then /^the ClassroomPosition should be "([^"]*)"$/ do |arg1|
  position = arg1
  position = "teacher of record" if arg1 == "TEACHER_OF_RECORD"
  step "\"classroomPosition\" is \"#{position}\""
end

When /^I navigate to Teacher Section Associations for Teacher "([^"]*)" and Section "([^"]*)"$/ do |teacherarg, sectionarg| 
  id = "None"
  id = "12f25c0f-75d7-4e45-8f36-af1bcc342871" if teacherarg == "Ms. Jones" and sectionarg == "Algebra II"
  id = "da3eff9f-d26e-49f6-8b8b-1af59241c91a" if teacherarg == "Ms. Smith" and sectionarg == "Physics II"
  step "I navigate to GET \"/teacher-section-associations/#{id}\""
end

Then /^I should receive a link named "([^"]*)" with URI \/([^\/]*)\/(<[^>]*>)$/ do |name, type, entity|
  step "I should receive a link where rel is \"#{name}\" and href ends with \"#{type}\/#{entity}\""
end

Then /^HighlyQualifiedTeacher should be true$/ do
  step "\"highlyQualifiedTeacher\" should equal \"true\""
end

Then /^ClassroomPosition should be "([^"]*)"$/ do |arg1|
  position = arg1
  position = "TEACHER_OF_RECORD" if arg1 == "teacher of record"
  step "\"classroomPosition\" should equal \"#{position}\""
end

When /^I navigate to Teacher Section Associations for the Teacher "Ms. Jones"$/ do 
  step "I navigate to GET \"/teacher-section-associations/eb424dcc-6cff-a69b-c1b3-2b1fc86b2c94\""
end

Then /^I should receive a collection of (\d+) teacher\-section\-association links that resolve to$/ do |arg1|
  if @format == "application/json" or @format == "application/vnd.slc+json"
    dataH=JSON.parse(@res.body)
    @collectionLinks = []
    counter=0
    @ids = Array.new
    dataH.each do|link|
      if link["link"]["rel"]=="self"
        counter=counter+1
        @ids.push(link["id"])
      # puts @ids
      end
    end
    assert(counter==Integer(arg1), "Expected response of size #{arg1}, received #{counter}")
  elsif @format == "application/xml"
    assert(false, "application/xml is not supported")
  else
    assert(false, "Unsupported MIME type")
  end
end

Then /^the collection should contain a link named "([^"]*)" with URI (\/[^\/]*\/<[^>]*>)$/ do |rel,href|
  found =false
  @ids.each do |id|
    uri = "/teacher-section-associations/"+id
    restHttpGet(uri)
    assert(@res != nil, "Response from rest-client GET is nil")
    if @format == "application/json" or @format == "application/vnd.slc+json"
      dataH=JSON.parse(@res.body)
      dataH["links"].each do|link|
        if link["rel"]==rel and link["href"].include? href
        found =true
        end
      end
    elsif @format == "application/xml"
      assert(false, "application/xml is not supported")
    else
      assert(false, "Unsupported MIME type")
    end
  end
  assert(found, "didnt receive link named #{rel} with URI #{href}")
end

Then /^everything in the collection should contain a link named "([^"]*)" with URI (\/[^\/]*\/<[^>]*>)$/ do |rel,href|
  @ids.each do |id|
    uri = "/teacher-section-associations/"+id
    restHttpGet(uri)
    assert(@res != nil, "Response from rest-client GET is nil")
    if @format == "application/json" or @format == "application/vnd.slc+json"
      dataH=JSON.parse(@res.body)
      dataH["links"].each do|link|
        if link["rel"] == rel
          assert((link["href"].include? href), "didnt receive link named #{rel} with URI #{href}")
        end
      end
    elsif @format == "application/xml"
      assert(false, "application/xml is not supported")
    else
      assert(false, "Unsupported MIME type")
    end
  end
end

When /^I navigate to Teacher Section Association for the Section "Chem I"$/ do 
  step "I navigate to GET \"/teacher-section-associations/1e1cdb04-2094-46b7-8140-e3e481013480\""
end

When /^I set the ClassroomPosition to assistant teacher$/ do
  step "\"classroomPosition\" is \"ASSISTANT_TEACHER\""
end

When /^I navigate to PUT \/([^\/]*\/[^\/]*)$/ do |uri|
  @data.update(@fields)
  restHttpPut("/"+uri, @data.to_json)
end

When /^I navigate to DELETE Teacher Section Associations for Teacher "([^"]*)" and Section "([^"]*)"$/ do |teacherarg, sectionarg|
  id = "None"
  id = "da3eff9f-d26e-49f6-8b8b-1af59241c91a" if teacherarg == "Ms. Smith" and sectionarg == "Physics II"
  restHttpDelete("/teacher-section-associations/"+id)
  assert(@res != nil, "Response from rest-client DELETE is nil")
end

