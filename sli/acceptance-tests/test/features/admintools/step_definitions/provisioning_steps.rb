require "selenium-webdriver"
require "json"

require_relative "../../utils/sli_utils.rb"
require_relative "../../utils/selenium_common.rb"
require "date"

SAMPLE_DATA_SET1_CHOICE = "ed_org_sample-dataset-1-ed-org"
SAMPLE_DATA_SET2_CHOICE = "ed_org_sample-dataset-2-ed-org"
CUSTOM_DATA_SET_CHOICE = "custom"

Given /^there is a production account in ldap for vendor "([^"]*)"$/ do |vendor|
  @sandboxMode=false
end

When /^I provision$/ do
  pending # express the regexp above with the code you wish you had
end

Then /^a high\-level ed\-org is created in Mongo$/ do
  pending # express the regexp above with the code you wish you had
end

Then /^Landing\-Zone provisioining API is invoked with the high\-level ed\-org information$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I go to the provisioning application$/ do
  url = PropLoader.getProps['admintools_server_url']+"/landing_zone"
  @driver.get(url)
end

Then /^I can only enter a custom high\-level ed\-org$/ do
  assertWithWait("Sample data choice exists") {@driver.find_elements(:id, SAMPLE_DATA_SET1_CHOICE).empty? }
  assertWithWait("Sample data choice exists") {@driver.find_elements(:id, SAMPLE_DATA_SET2_CHOICE).empty? }
  assertWithWait("Custom data choice does not exist") {@driver.find_element(:id, CUSTOM_DATA_SET_CHOICE) != nil}
end

When /^I set the custom high\-level ed\-org to "([^"]*)"$/ do |arg1|
  @driver.find_element(:id, "custom_ed_org").send_keys arg1
end

When /^I select the first sample data set$/ do
  @driver.find_element(:id, SAMPLE_DATA_SET1_CHOICE).click
end

When /^I click the Provision button$/ do
  @driver.find_element(:id, "provisionButton").click
end

Then /^I get the success message$/ do
  assertWithWait("No success message") {@driver.find_element(:id, "successMessage") != nil}
end

Given /^there is a sandbox account in ldap for vendor "([^"]*)"$/ do |arg1|
  @sandboxMode=true
end

Then /^I can select between the the high level ed\-org of the sample data sets or enter a custom high\-level ed\-org$/ do
  assertWithWait("Sample data choice does not exist") {@driver.find_element(:id, SAMPLE_DATA_SET1_CHOICE) != nil}
  assertWithWait("Sample data choice does not exist") {@driver.find_element(:id, SAMPLE_DATA_SET2_CHOICE) != nil}
  assertWithWait("Custom data choice does not exist") {@driver.find_element(:id, CUSTOM_DATA_SET_CHOICE) != nil}
end
