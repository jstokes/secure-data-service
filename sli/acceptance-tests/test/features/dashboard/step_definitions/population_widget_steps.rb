require 'selenium-webdriver'
# require_relative '../../utils/sli_utils.rb'


# idpLogin("demo", "demo1234")


Given /^I am logged in using "([^"]*)" "([^"]*)"$/ do |username, password|
  puts "login"
  $SLI_DEBUG=true
  @username = username
  @password = password
  localLogin(username, password)
end

Given /^I selected the "([^"]*)" application$/ do |appName|
  @driver.find_element(:link_text, appName).click
end

When /^I look in the school drop\-down$/ do
  @dropDownId = "schoolSelect"
  assertMissingField(@dropDownId)
end

Then /^I only see "([^"]*)"$/ do |listContent|
  select = @driver.find_element(:id, @dropDownId)
  all_options = select.find_elements(:tag_name, "option")
  matchCondition = true
  # If any list item has a value that is not in the list - set flag to false
  all_options.each do |option|
    if option.attribute("text") != listContent and 
      option.attribute("text") != "" then
      matchCondition = false
    end
  end
  assert(matchCondition, "School list has more then required string(s) " + listContent)
end

When /^I look in the course drop\-down$/ do
  @dropDownId = "courseSelect"
  assertMissingField(@dropDownId)
end

Then /^I see these values in the drop\-down: "([^"]*)"$/ do |listContent|
  puts "@dropDownId = " + @dropDownId
  desiredContentArray = listContent.split(";")
  select = @driver.find_element(:id, @dropDownId)
  all_options = select.find_elements(:tag_name, "option")
  matchCondition = true
  selectContent = ""
  # If any list item has a value that is not in the list - set flag to false
  all_options.each do |option|
    selectContent += option.attribute("text") + ";"
    puts "selectContent = " + selectContent
  end
  selectContentArray = selectContent.split(";")
  result = (desiredContentArray | selectContentArray) - (desiredContentArray & selectContentArray)
  assert(result == [""], "list content does not match required content: " + listContent)  
end

Then /^I see these values in the class drop\-down: "([^"]*)"$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

Then /^I see a list of (\d+) students$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

When /^I select school "([^"]*)"$/ do |optionToSelect|
  @dropDownId = "schoolSelect"
  puts "@dropDownId = " + @dropDownId
  selectOption(@dropDownId, optionToSelect)
  @dropDownId = "courseSelect"
end

When /^I select course "([^"]*)"$/ do |optionToSelect|
  @dropDownId = "courseSelect"
  puts "@dropDownId = " + @dropDownId
  selectOption(@dropDownId, optionToSelect)
  @dropDownId = "sectionSelect"
end

When /^I select section "([^"]*)"$/ do |optionToSelect|
  @dropDownId = "sectionSelect"
  puts "@dropDownId = " + @dropDownId
  selectOption(@dropDownId, optionToSelect)
end

Then /^the list includes: "([^"]*)"$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

