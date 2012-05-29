require "selenium-webdriver"
require 'json'

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'
require 'date'

Given /^I am a valid SLI Developer "([^"]*)" from the "([^"]*)" hosted directory$/ do |arg1, arg2|
  # No code needed, done as configuration
end

Given /^I am a valid SLC Operator "([^"]*)" from the "([^"]*)" hosted directory$/ do |arg1, arg2|
  # No code needed, done as configuration
end

When /^I hit the Application Registration Tool URL$/ do
  @driver.get(PropLoader.getProps['admintools_server_url']+"/apps/")
end

Then /^I am redirected to the Application Approval Tool page$/ do
  assertWithWait("Failed to navigate to the Admintools App Registration Approval page")  {@driver.page_source.index("Application Registration Approval") != nil}
end

Then /^I am redirected to the Application Registration Tool page$/ do
  assertWithWait("Failed to navigate to the Admintools App Registration page")  {@driver.page_source.index("New Application") != nil}
end

Then /^I see all of the applications that are registered to SLI$/ do
  assertWithWait("Failed to find applications table") {@driver.find_element(:id, "applications")}
end

Then /^application "([^"]*)" does not have an edit link$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  edit = appsTable.find_elements(:xpath, ".//tr/td[text()='#{app}']/../td/a[text()='Edit']")
  assert(edit.length == 0, "Should not see an edit link")
end

Then /^I see all the applications registered on SLI$/ do
  appsTable = @driver.find_element(:id, "applications")
  trs = appsTable.find_elements(:xpath, ".//tr/td[text()='APPROVED']")
  assert(trs.length > 10, "Should see a significant number of approved applications")
end

Then /^I see all the applications pending registration$/ do
  appsTable = @driver.find_element(:id, "applications")
  trs = appsTable.find_elements(:xpath, ".//tr/td[text()='PENDING']")
  assert(trs.length == 1, "Should see a pending application")
end

Then /^application "([^"]*)" is pending approval$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  trs  = appsTable.find_elements(:xpath, ".//tr/td[text()='#{app}']/../td[text()='PENDING']")
  assert(trs.length > 0, "#{app} is pending")
end

Then /^the pending apps are on top$/ do
  appsTable = @driver.find_element(:id, "applications")
  tableHeadings = appsTable.find_elements(:xpath, ".//tr/th")
  index = 0
  tableHeadings.each do |arg|
    index = tableHeadings.index(arg) + 1 if arg.text == "Status"    
  end
  trs = appsTable.find_elements(:xpath, ".//tr/td/form/div/input[@value='Approve']/../../../..")
  assert(trs.length > 10, "Should see many applications")

  last_status = nil
  trs.each do |row|
    td = row.find_element(:xpath, ".//td[#{index}]")
    if last_status == nil
      last_status = td.text
      assert(last_status == 'PENDING', "First element should be PENDING, got #{last_status}")
    end
    if last_status == 'APPROVED'
      assert(td.text != 'PENDING', "Once we find approved apps, we should no longer see any PENDING ones.")
    end
    last_status = td.text
  end
end


When /^I click on 'Approve' next to application "([^"]*)"$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  y_button  = appsTable.find_elements(:xpath, ".//tr/td/form/div/input[@value='Approve']")[0]
  assert(y_button != nil, "Found Y button")
  y_button.click
end

When /^I click on 'Deny' next to application "([^"]*)"$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  y_button  = appsTable.find_elements(:xpath, ".//tr/td/form/div/input[@value='Deny']")[0]
  assert(y_button != nil, "Found X button")
  y_button.click
end

Then /^I get a dialog asking if I want to continue$/ do
  @driver.switch_to.alert
end

Then /^application "([^"]*)" is registered$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  trs  = appsTable.find_elements(:xpath, ".//tr/td[text()='#{app}']/../td[text()='APPROVED']")
  assert(trs.length > 0, "No more pending applications")
end

Then /^application "([^"]*)" is not registered$/ do |app|
	# no-op - in next step we verify it was removed from list
end

Then /^application "([^"]*)" is removed from the list$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  tds  = appsTable.find_elements(:xpath, ".//tr/td[text()='#{app}']")
  assert(tds.length == 0, "#{app} isn't in list")
end

Then /^the 'Approve' button is disabled for application "([^"]*)"$/ do |app|
  appsTable = @driver.find_element(:id, "applications")
  y_button  = appsTable.find_elements(:xpath, ".//tr/td[text()='#{app}']/../td/form/div/input[@value='Approve']")[0]
  assert(y_button.attribute("disabled") == 'true', "Y button is disabled")
end

Then /^those apps are sorted by the Last Update column$/ do
  appsTable = @driver.find_element(:id, "applications")
  tableHeadings = appsTable.find_elements(:xpath, ".//tr/th")
  index = 0
  tableHeadings.each do |arg|
    index = tableHeadings.index(arg) + 1 if arg.text == "Last Update"    
  end
  tableRows = appsTable.find_elements(:xpath, ".//tr/td/a[text()='Edit']/../..")
  lastDate = nil
  tableRows.each do |row|
    td = row.find_element(:xpath, ".//td[#{index}]")
    date = Date.parse(td.text)
    if lastDate == nil
      lastDate = date
    end
    assert(date <= lastDate, "Last Update column should be sorted")
    lastDate = date
  end
end

Given /^I am a valid IT Administrator "([^"]*)" from the "([^"]*)" hosted directory$/ do |arg1, arg2|
  # No code needed, done as configuration
end

Then /^I receive a message that I am not authorized$/ do
  assertWithWait("Failed to find forbidden message")  {@driver.page_source.index("Forbidden") != nil}
end

Then /^I have clicked to the button New$/ do
  @driver.find_element(:xpath, '//a[text()="New Application"]').click
end

When /^I entered the name "([^"]*)" into the field titled "([^"]*)"$/ do |arg1, arg2|
  @driver.find_element(:id, "app_#{arg2.downcase}").send_keys arg1
end

Then /^I am redirected to a new application page$/ do
  assertWithWait("Failed to navigate to the New Applicaation page")  {@driver.page_source.index("New Application") != nil}
end

When /^I have entered data into the other required fields except for the shared secret and the app id which are read\-only$/ do
  @driver.find_element(:name, 'app[description]').send_keys "Blah"
  @driver.find_element(:name, 'app[application_url]').send_keys "https://blah.com"
  @driver.find_element(:name, 'app[administration_url]').send_keys "https://blah.com"
  @driver.find_element(:name, 'app[redirect_uri]').send_keys "https://blah.com"
  @driver.find_element(:name, 'app[version]').send_keys "0.9"
  @driver.find_element(:name, 'app[image_url]').send_keys "http://blah.com"
  @driver.find_element(:name, 'app[vendor]').send_keys "Cucumber"
  @driver.find_element(:css, 'input[id="app_installed"]').click
  list = @driver.find_element(:css, 'input[disabled="disabled"]')
  assert(list, "Should have disabled fields.")
  
end

When /^I click on the button Submit$/ do
  @driver.find_element(:name, 'commit').click
end

Then /^the application "([^"]*)" is listed in the table on the top$/ do |app|
  value = @driver.find_element(:id, 'notice').text
  assert(value =~ /successfully created/, "Should have valid flash message")
  assertWithWait("Couldn't locate #{app} at the top of the page") {@driver.find_element(:xpath, "//tbody/tr[1]/td[text()='#{app}']")}
end

Then /^a client ID is created for the new application that can be used to access SLI$/ do
  assertWithWait("Should have located a client id") {@driver.find_element(:xpath, '//tr[3]').find_element(:name, 'app[client_id]')}
end

Then /^the client ID and shared secret fields are Pending$/ do
  @driver.find_element(:xpath, "//tbody/tr[1]/td[1]").click
  client_id = @driver.find_element(:xpath, '//tbody/tr[2]/td/dl/dd[1]').text
  puts client_id
  assert(client_id == 'Pending', "Expected 'Pending', got #{client_id}")
end

Then /^the Registration Status field is Pending$/ do
  td = @driver.find_element(:xpath, "//tbody/tr[1]/td[4]")
  assert(td.text == 'Pending', "Expected 'Pending', got #{td.text}")
end

When /^I click on the row of application named "([^"]*)" in the table$/ do |arg1|
  @driver.find_element(:xpath, "//tr/td[text()='#{arg1}']").click
end

Then /^the row expands$/ do
  #No code needed, this should be tested by later stepdefs when they see that the application details are there
end

Then /^I see the details of "([^"]*)"$/ do |arg1|
  step "the client ID and shared secret fields are Pending"
end

Then /^all the fields are read only$/ do
  #Nothing needed
end

Then /^I clicked on the button Edit for the application "([^"]*)"$/ do |arg1|
  row = @driver.find_element(:xpath, "//tr/td[text()='#{arg1}']/..")
  assert(row)
  @id = row.attribute('id')
  @driver.find_element(:xpath, "//tr/td[text()='#{arg1}']/../td/a[text()='In Progress']").click
end

Then /^the row of the app "([^"]*)" expanded$/ do |arg1|
 invisible = @driver.find_elements(:css, "tr[display='none']").count
 visible = @driver.find_elements(:css, "tr.odd").count
 assert(invisible == visible -1)
end

Then /^every field except the shared secret and the app ID became editable$/ do
  @form = @driver.find_element(:id, "edit_app_#{@id}")
  editable = @form.find_elements(:css, "input").count
  uneditable = @form.find_elements(:css, "input[disabled='disabled']").count
  assert(uneditable == 2, "Found #{uneditable} elements")
end

Then /^I have edited the field named "([^"]*)" to say "([^"]*)"$/ do |arg1, arg2|
  @form = @driver.find_element(:css, "form")
  field = @form.find_element(:name, "app[#{arg1.gsub(' ', '_').downcase}]")
  field.clear
  field.send_keys arg2
end

When /^I clicked Save$/ do
  @form.find_element(:name, 'commit').click
end

Then /^the info for "([^"]*)" was updated$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

Then /^I the field named "([^"]*)" still says "([^"]*)"$/ do |arg1, arg2|
  @driver.find_element(:xpath, "//tbody/tr[1]/td[1]").click
  data = @driver.find_element(:xpath, "//tbody/tr[2]/td/dl/dt[text()=\"#{arg1}\"]")
  value = data.find_element(:xpath, "following-sibling::*[1]").text
  assertWithWait("#{arg1} should be #{arg2}") {value == arg2}
end

Then /^I have clicked on the button 'Deny' for the application named "([^"]*)"$/ do |arg1|
  list = @driver.find_element(:xpath, "//tr/td[text()='#{arg1}']")
  assert(list)
  @id = list.attribute('id')
  @driver.find_element(:xpath, "//tr/td[text()='#{arg1}']/../td/a[text()='Delete']").click
    
end

Then /^I got warning message saying 'You are trying to remove this application from SLI\. By doing so, you will prevent any active user to access it\. Do you want to continue\?'$/ do
  @driver.switch_to.alert
end

When /^I click 'Yes'$/ do
  @driver.switch_to.alert.accept
end

Then /^the application named "([^"]*)" is removed from the SLI$/ do |arg1|
    assertWithWait("Shouldn't see a NewApp") {!@driver.find_element(:xpath, "//tr[2]").attribute('id') != @id}
end

Then /^the previously generated client ID can no longer be used to access SLI$/ do
  pending # express the regexp above with the code you wish you had
end

Given /^I am a valid App Developer$/ do
  #Nothing
end

Then /^I see the list of my registered applications only$/ do
  appsTable = @driver.find_element(:id, "applications")
  trs = appsTable.find_elements(:xpath, ".//tr/td[text()='APPROVED']")
  assert(trs.length > 0, "Should see at least one of my apps")
end

Then /^the application is registered$/ do
  appsTable = @driver.find_element(:id, "applications")
  trs  = appsTable.find_elements(:xpath, ".//tr/td[text()='NewApp']/../td[text()='APPROVED']")
  assert(trs.length > 0, "No more pending applications")
end

Then /^I can see the client ID and shared secret$/ do
  appsTable = @driver.find_element(:id, "applications")
  app  = appsTable.find_element(:xpath, "//tr/td[text()='NewApp']/..")
  id = app.attribute('id')
  form = @driver.find_element(:id, "edit_app_#{id}")
  assert("Client ID should be visible", form.find_element(:name, 'app[client_id]').attribute('value') != "Pending")
  assert("Client Secret should be visible", form.find_element(:name, 'app[client_secret]').attribute('value') != "Pending")
end

Then /^the Registration Status field is Registered$/ do
  appsTable = @driver.find_element(:id, "applications")
  trs  = appsTable.find_elements(:xpath, ".//tr/td[text()='NewApp']/../td[text()='APPROVED']")
  assert(trs.length > 0, "No more pending applications")
end

