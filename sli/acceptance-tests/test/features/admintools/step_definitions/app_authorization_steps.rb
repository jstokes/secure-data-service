require "selenium-webdriver"
require 'json'

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'

When /^I hit the Admin Application Authorization Tool$/ do
  #XXX - Once the API is ready, remove the ID
  @driver.get(PropLoader.getProps['admintools_server_url']+"/application_authorizations/")
end

When /^I was redirected to the "([^"]*)" IDP Login page$/ do |arg1|
  assertWithWait("Failed to navigate to the IDP Login page")  {@driver.find_element(:id, "IDToken1")}
end

Then /^I am redirected to the Admin Application Authorization Tool$/ do
  assertWithWait("Failed to navigate to the Admintools App Registration page")  {@driver.page_source.index("application_authorizations") != nil}
end

Then /^in the upper right corner I see my name$/ do
  assertWithWait("Failed to find name in upper right corner") {@driver.page_source.index("Sunset Admin") != nil}
end

Then /^I see a label in the middle "([^"]*)"/ do |arg1|
  #We're changing how the ID is referenced, so the label for the time-being isn't going to be accurate
  #assert(@driver.page_source.index(arg1) != nil)
end

Then /^I see the list of all available apps on SLI$/ do
  @appsTable = @driver.find_element(:id, "Authorized Apps Table")
  assert(@appsTable != nil  )
end

Then /^the authorized apps for my district are colored green$/ do
  approved = @appsTable.find_elements(:xpath, ".//tr/td[text()='Approved']")
  approved.each do |currentStatus|
    assert(currentStatus.attribute(:id) == "approvedStatus", "App is not the right color, should be green")
  end
end

Then /^the unauthorized are colored red$/ do
  notApproved = @appsTable.find_elements(:xpath, ".//tr/td[text()='Not Approved']")
  notApproved.each do |currentStatus|
    assert(currentStatus.attribute(:id) == "notApprovedStatus", "App is not the right color, should be red")
  end
end

Then /^are sorted by 'Status'$/ do
  tableHeadings = @appsTable.find_elements(:xpath, ".//tr/th")
  index = 0
  tableHeadings.each do |arg|
    index = tableHeadings.index(arg) + 1 if arg.text == "Status"    
  end
  rows = @appsTable.find_elements(:xpath, ".//tr/..")
  inApprovedSection = true
  rows.each do |curRow| 
    td = curRow.find_element(:xpath, ".//td[#{index}]")
    assert(inApprovedSection || (!inApprovedSection && td.text != "Approved"), "Encountered an app with a 'Approved' status after one with a 'Not Approved' status")
    if td.text == "Not Approved"
      inApprovedSection = false
    end
  end
end

Then /^I see the Name, Version, Vendor and Status of the apps$/ do
  expectedHeadings = ["Name", "Version", "Vendor", "Status", ""]
  tableHeadings = @appsTable.find_elements(:xpath, ".//tr/th")
  actualHeadings = []
  tableHeadings.each do |heading|
    actualHeadings.push(heading.text)
  end    
  assert(expectedHeadings.sort == actualHeadings.sort, "Headings are different, found #{actualHeadings.inspect} but expected #{expectedHeadings.inspect}")
end

Given /^I am a valid SEA\/LEA user$/ do
end

When /^I hit the Application Authorization Tool$/ do
  pending # express the regexp above with the code you wish you had
end

When /^the login process is initiated$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I pass my valid username and password$/ do
  pending # express the regexp above with the code you wish you had
end

Then /^I get message that I am not authorized$/ do
  isForbidden = @driver.find_element(:xpath, './/body/title[text()="Not Authorized (403)"]')
  assert(isForbidden != nil)
end

Then /^I do not get message that I am not authorized$/ do
  isForbidden = nil
  begin
    isForbidden = @driver.find_element(:xpath, './/body/title[text()="Not Authorized (403)"]')
  rescue Exception => e
    #expected
    assert(isForbidden == nil)
  else
    assert(isForbidden == nil)
  end
end

Then /^I am not logged into the application$/ do
  step "I hit the Admin Application Authorization Tool"
end


Given /^I am logged into the Application Authorization Tool$/ do
end

Given /^I see an application "([^"]*)" in the table$/ do |arg1|
  @appName = arg1
  apps = @driver.find_elements(:xpath, ".//tr/td[text()='#{arg1}']/..")
  assert(apps != nil)
end

Given /^in Status it says "([^"]*)"$/ do |arg1|
  headings = @driver.find_elements(:xpath, ".//tr/th")
  index = 0
  headings.each do |heading|
    if heading.text == "Status"
      index = headings.index(heading) + 1
    end
  end
  
  @appRow = @driver.find_element(:xpath, ".//tr/td[text()='#{@appName}']/..")
  actualStatus = @appRow.find_element(:xpath, ".//td[#{index}]").text
  assert(actualStatus == arg1, "Expected status of #{@appName} to be #{arg1} instead it's #{actualStatus}")
end

Given /^I click on the "([^"]*)" button next to it$/ do |arg1|
  inputs = @appRow.find_elements(:xpath, ".//td/form/input")
  inputs.each do |cur|
    if cur.attribute(:value) == arg1
      cur.click
      break
    end
  end
end

Given /^I am asked 'Do you really want this application to access the district's data'$/ do
      begin
        @driver.switch_to.alert
      rescue
      end
end

When /^I click on Ok$/ do
  @driver.switch_to.alert.accept
end

Then /^the application is authorized to use data of "([^"]*)"$/ do |arg1|
  row = @driver.find_element(:xpath, ".//tr/td[text()='#{@appName}']/..")
  assert(row != nil)
end

Then /^is put on the top of the table$/ do
  @row = @driver.find_element(:xpath, ".//tr/td/..")
  assert(@row.find_element(:xpath, ".//td[1]").text == @appName, "The approved application should have moved to the top")
end

Then /^the Status becomes "([^"]*)"$/ do |arg1|
  assertWithWait("Status should have switched to #{arg1}"){  @row.find_element(:xpath, ".//td[4]").text == arg1} 
end

Then /^it is colored "([^"]*)"$/ do |arg1|
  status = @row.find_element(:xpath, ".//td[4]")
  if arg1 == "green"
    assert(status.attribute(:id) == "approvedStatus", "Should be colored green, instead ID is #{status.attribute(:id)}")
  elsif arg1 == "red"
    assert(status.attribute(:id) == "notApprovedStatus", "Should be colored red, instead ID is #{status.attribute(:id)}")
  end
end

Then /^the Approve button next to it is disabled$/ do
  @inputs = @row.find_elements(:xpath, ".//td/form/input")
  @inputs.each do |input|
    if input.attribute(:value) == "Approve"
      assert(input.attribute(:disabled) == "true", "Approve button should be disabled")
    end
  end
end

Then /^the Deny button next to it is enabled$/ do
  @inputs = @row.find_elements(:xpath, ".//td/form/input")
  @inputs.each do |input|
    if input.attribute(:value) == "Deny"
      assert(input.attribute(:disabled) == "false", "Deny button should be enabled")
    end
  end
end

Given /^in Status it says Approved$/ do
  pending # express the regexp above with the code you wish you had
end

Given /^I am asked 'Do you really want deny access to this application of the district's data'$/ do
      begin
        @driver.switch_to.alert
      rescue
      end
end

Then /^the application is denied to use data of "([^"]*)"$/ do |arg1|
  row = @driver.find_element(:xpath, ".//tr/td[text()='#{@appName}']/..")
  assert(row != nil)
end

Then /^it is put on the bottom of the table$/ do
  @row = @driver.find_element(:xpath, ".//tr/td[text()='#{@appName}']/..")
end

Then /^the Approve button next to it is enabled$/ do
  @inputs = @row.find_elements(:xpath, ".//td/form/input")
  @inputs.each do |input|
    if input.attribute(:value) == "Approve"
      assert(input.attribute(:disabled) == "false", "Approve button should be enabled")
    end
  end
end

Then /^the Deny button next to it is disabled$/ do
  @inputs = @row.find_elements(:xpath, ".//td/form/input")
  @inputs.each do |input|
    if input.attribute(:value) == "Deny"
      assert(input.attribute(:disabled) == "true", "Deny button should be disabled")
    end
  end
end

Given /^I am an authenticated end user \(Educator\) from <district>$/ do
  pending # express the regexp above with the code you wish you had
end

Given /^the Data Browser is denied access for <district>$/ do
  pending # express the regexp above with the code you wish you had
end

When /^I try to access any resource through the DB \(even the home\-link\) page$/ do
  pending # express the regexp above with the code you wish you had
end

Given /^I am a valid District Super Administrator for "([^"]*)"$/ do |arg1|
  #No code needed
end

Given /^I am an authenticated District Super Administrator for "([^"]*)"$/ do |arg1|
  step "I have an open web browser"
  step "I hit the Admin Application Authorization Tool"
  step "I was redirected to the \"OpenAM\" IDP Login page"
  step "I submit the credentials \"sunsetadmin\" \"sunsetadmin1234\" for the \"OpenAM\" login page"
  step "I am redirected to the Admin Application Authorization Tool"
end

Given /^I am an authenticated end user "" from "([^"]*)"$/ do |arg1|
    @driver.get(PropLoader.getProps['databrowser_server_url'])
end


