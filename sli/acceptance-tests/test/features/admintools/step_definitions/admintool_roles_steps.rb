require "selenium-webdriver"

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'

Given /^I am not authenticated to SLI IDP$/ do
  @driver.manage.delete_all_cookies
end

When /^I navigate to the SLI Default Roles Admin Page$/ do
  url = PropLoader.getProps['admintools_server_url']
  @driver.get url
end

Given /^I am authenticated to SEA\/LEA IDP as user "([^"]*)" with pass "([^"]*)"$/ do |arg1, arg2|
  url = PropLoader.getProps['sea_idp_server_url']+"/UI/Login"
  @driver.get url
  @driver.find_element(:id, "IDToken1").send_keys arg1
  @driver.find_element(:id, "IDToken2").send_keys arg2
  @driver.find_element(:name, "Login.Submit").click
  begin
    @driver.switch_to.alert.accept
  rescue
  end
end

Then /^I should be redirected to the SLI Default Roles Admin Page$/ do
  assertWithWait("Failed to navigate to the Admintools Role page")  {@driver.page_source.index("Default SLI Roles") != nil}
end

Given /^I have tried to access the SLI Default Roles Admin Page$/ do
  url = PropLoader.getProps['admintools_server_url']
  @driver.get url
end

Given /^I am user "([^"]*)"$/ do |arg1|
  #No code needed for this step
end

Given /^"([^"]*)" is valid "([^"]*)" user$/ do |arg1, arg2|
  #No code needed for this step
end

Then /^I am now authenticated to SLI IDP$/ do
  #No code needed, this is tested implicitly by accessing the admin roles page
end

Given /^"([^"]*)" is invalid "([^"]*)" user$/ do |arg1, arg2|
  #No code needed for this step
end

Then /^I am informed that authentication has failed$/ do
  errorBox = @driver.find_element(:class, "error-message")
  assert(errorBox != nil, webdriverDebugMessage(@driver,"Could not find error message div"))
end

Then /^I do not have access to the SLI Default Roles Admin Page$/ do
  @driver.get PropLoader.getProps['admintools_server_url']
  assert(@driver.page_source.index("Default SLI Roles") == nil, webdriverDebugMessage(@driver,"Navigated to the Admintools Role page with no credentials"))
end

Given /^I have a Role attribute equal to "([^"]*)"$/ do |arg1|
  #No code needed, this is done durring the IDP configuration
end

Then /^I should get a message that I am not authorized$/ do
  assertWithWait("Could not find Not Authorized in page title")  {@driver.page_source.index("Forbidden")!= nil}
end

Given /^I have navigated to the SLI Default Roles Admin Page$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I click on the Logout link$/ do
  pending # express the regexp above with the code you wish you had
end

Then /^I am no longer authenticated to SLI IDP$/ do
  pending # express the regexp above with the code you wish you had
end
