require 'rest-client'
require 'json'
require_relative '../../../utils/sli_utils.rb'
require_relative '../../entities/common.rb'

# transform <Place Holder Id>
Transform /^<(.+)>$/ do |template|
  id = template
  id = "9d970849-0116-499d-b8f3-2255aeb69552" if template == "'Dawn Elementary School' ID"
  id = "b1bd3db6-d020-4651-b1b8-a8dba688d9e1" if template == "'Illinois State Ed-org' ID"
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

Given /^query criteria is "([^"]*)"$/ do |arg1|
  @query_criteria = arg1
end

Then /^I should receive a collection$/ do
  assert(@result != nil, "Response contains no data")
  assert(@result.is_a?(Array), "Expected array of links")
end

Then /^the link at index (\d+) should point to an entity with id "([^\"]*)"$/ do |index, id|
  index = convert(index)
  @result[index].should_not == nil
  @result[index]["id"].should == id
end

Then /^the link at index (\d+) should have "([^\"]*)" equal to "([^\"]*)"$/ do |index, field, id|
  index = convert(index)
  @result[index].should_not == nil
  fieldValue = @result[index];
  field.split("\.").each do |f| 
    fieldValue = fieldValue[f]
  end
  fieldValue.should == id
end

Then /^I should receive a collection with (\d+) elements$/ do |count|;
  count = convert(count)
  assert(@result != nil, "Response contains no data")
  assert(@result.is_a?(Array), "Expected array of links")
  @result.length.should == count
end

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

Then /^the a previous link should not exist$/ do
  links = @res.raw_headers["link"];
  links.should be_a Array
  found_link = false
  links.each do |link|
    if /rel=prev/.match link
      found_link = true
    end
  end
  found_link.should == false
end

Then /^the a next link should not exist$/ do
  links = @res.raw_headers["link"];
  links.should be_a Array
  found_link = false
  links.each do |link|
    if /rel=next/.match link
      found_link = true
    end
  end
  found_link.should == false
end

