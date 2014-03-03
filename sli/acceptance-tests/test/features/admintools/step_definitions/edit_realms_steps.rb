=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

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

# TODO: Delete this file when all ugly steps have been replaced

When /^I hit the realm editing URL$/ do
  @url = Property['admintools_server_url'] + "/realm_management"
  @driver.get @url
end

When /^I should see that I am on the "([^"]*)" edit page$/ do |realmName|
  message = "Realm Management For #{realmName}"
  assertWithWait("Should show '#{message}' message") do
    @driver.page_source.index(message) != nil
   end
end

When /^I should enter "([^"]*)" into the Display Name field$/ do |newRealmName|
  @driver.find_element(:id, "realm_name").clear
  @driver.find_element(:id, "realm_name").send_keys newRealmName
end

Then /^I copy the idp information$/ do
  $redirectEndpointElem =  @driver.find_element(:name, 'realm[idp][redirectEndpoint]').attribute('VALUE')
  $idpIdElem =  @driver.find_element(:name, 'realm[idp][id]').attribute('VALUE')
end

Then /^I should update the idp to captured information$/ do
  @driver.find_element(:name, 'realm[idp][id]').clear
  @driver.find_element(:name, 'realm[idp][id]').send_keys $idpIdElem
  @driver.find_element(:name, 'realm[idp][redirectEndpoint]').clear
  @driver.find_element(:name, 'realm[idp][redirectEndpoint]').send_keys $redirectEndpointElem
end

Then /^I should enter "(.*?)" into Realm Identifier$/ do |identifier|
  @driver.find_element(:name, 'realm[uniqueIdentifier]').clear
  @driver.find_element(:name, 'realm[uniqueIdentifier]').send_keys identifier
end

When /^I should click the "([^"]*)" button$/ do |buttonText|
  @driver.find_element(:xpath, ".//input[@value='#{buttonText}']").click
end

Then /^I should be redirected back to the realm listing page$/ do
  #No code necessary, this step is just to make Gherkin read happy
end

Then /^I should receive a notice that the realm was successfully "([^"]*)"$/ do |action|
  message = "Realm was successfully updated." if action == "updated"
  message = "Realm was successfully deleted." if action == "deleted"
  message = "Realm was successfully created." if action == "created"
  assertWithWait("Should give successful #{action} notice") do
    notice = (@driver.find_element(:id, "notice")).text
    notice.index(message) != nil
  end
end

Then /^I should see that I am on the new realm page$/ do
  assertWithWait("Should be on new realm page"){@driver.page_source.index("Manage Realm") != nil}
end

Then /^all of the input fields should be blank$/ do
  textFields = @driver.find_elements(:xpath, ".//input[@type='text']")
  textFields.each do |cur|
    assert(cur.text.length == 0, "Input fields should not contain text")
  end
end

When /^I click on the Add new realm button$/ do
  @driver.find_element(:link_text, "Add new").click
end

When /^I see the realms for "([^"]*)"$/ do |uid|
  title = (@driver.find_elements(:xpath, "//html/body/div/h1"))[0].text
  expectedTitle = "Realms for #{uid}"
  assert(title == expectedTitle, "Page title in body/div/h1 mismatch: got '" + title + "' expected '" + expectedTitle + "'")
end

When /^I click the "(.*?)" edit button$/ do |arg1|
  realm_row = @driver.find_element(:xpath, "//td[text()='#{arg1}']/..")
  realm_row.find_element(:link, "Edit").click
end

When /^I click the "(.*?)" delete button and confirm deletion$/ do |arg1|
  realm_row = @driver.find_element(:xpath, "//td[text()='#{arg1}']/..")
  realm_row.find_element(:link, "Delete Realm").click
  alert = @driver.switch_to().alert()
  assert(alert.text.index("WARNING: DELETING REALM WILL PREVENT ANY USER ASSOCIATED WITH THIS REALM FROM AUTHENTICATING ON inBloom AND WILL RESET ROLE MAPPING") != nil, "Popup message was not expected")
  alert.accept()
end

And /^the realm "(.*?)" will exist$/ do |arg1|
  assertWithWait("Realm #{arg1} was not found on page") {@driver.find_element(:id, "realms").text.index(arg1) != nil}
end
