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

require "selenium-webdriver"
require 'json'

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'

When /^I navigate to the Custom Role Mapping Page$/ do
  @driver.get PropLoader.getProps['admintools_server_url']+"/custom_roles"
end

Then /^I have navigated to my Custom Role Mapping Page$/ do
  assertWithWait("Failed to be redirected to Role mapping page")  {@driver.page_source.index("Custom") != nil}
end

When /^I got a warning message saying "(.*?)"$/ do |arg1|
  @driver.switch_to.alert
end

When /^I click 'OK' on the warning message$/ do
  @driver.switch_to.alert.accept
end

When /^I click on the Add Group button$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I type the name "(.*?)" in the Group name textbox$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

Then /^a new group is created titled "(.*?)"$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

Then /^the group "(.*?)" contains the roles "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

Then /^the group "(.*?)" contains the rights "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

When /^I hit the save button$/ do
  pending # express the regexp above with the code you wish you had
end

Then /^I am informed that I must have at least one role and right in the group$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I add the right "(.*?)" to the group "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

When /^I add the role "(.*?)" to the group "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

When /^I create a new role <Role> to the group <Group> that allows <User> to access the API$/ do |table|
  # table is a Cucumber::Ast::Table
  table.hashes.each do |hash|
    step "I edit the group #{hash["Group"]}"
    step "I add the role #{hash["Role"]} to the group #{hash["Group"]}"
    step "I hit the save button"
    #TODO add stuff to validate the new role is in the group
    step "the user #{hash["User"]} can now access the API with rights #{hash["Role"]}"
  end

  pending # express the regexp above with the code you wish you had
end

Then /^the user "(.*?)" can access the API with rights "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

When /^I remove the right "(.*?)" from the group "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

When /^I remove the role <Role> from the group <Group> that denies <User> access to the API$/ do |table|
  # table is a Cucumber::Ast::Table
  table.hashes.each do |hash|
    step "I edit the group #{hash["Group"]}"
    step "I remove the role #{hash["Role"]} from the group #{hash["Group"]}"
    step "I hit the save button"
    #TODO add stuff to validate the new role is in the group
    step "the user #{hash["User"]} can no longer access the API with rights #{hash["Role"]}"
  end
  
  pending # express the regexp above with the code you wish you had
end

Then /^That user can no longer access the API$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I remove the role "(.*?)" from the group "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

When /^I remove the group "(.*?)"$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

Then /^the group "(.*?)" no longer appears on the page$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

When /^I edit the rights for the group <Group> to include the duplicate right <Right>$/ do |table|
  # table is a Cucumber::Ast::Table
  pending # express the regexp above with the code you wish you had
end

Then /^I cannot find the right in the dropdown$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I edit the roles for the group <Group> to include the duplicate role <Role>$/ do |table|
  # table is a Cucumber::Ast::Table
  pending # express the regexp above with the code you wish you had
end

Then /^I am informed that "(.*?)"$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

When /^I click the cancel button$/ do
  pending # express the regexp above with the code you wish you had
end
