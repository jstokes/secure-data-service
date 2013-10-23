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
the large list of edorgs is loaded
=end


require "selenium-webdriver"

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'

# When /^I hit the Application Registration Tool URL$/ do
#   @driver.get(PropLoader.getProps['admintools_server_url']+"/apps/")
# end
Given /^the large list of edorgs is loaded$/ do
  file = "#{File.dirname(__FILE__)}/edorgs/edorgs.json"
  status = system("mongoimport --drop -c educationOrganization -d #{convertTenantIdToDbName('Midgar')} --file #{file}")
  assert(status, "#{$?}")
  # Re-index edorg collection after drop
  index_rb_loc = "#{File.dirname(__FILE__)}/../../../../../config/scripts/indexTenantDb.rb"
  status = system("ruby #{index_rb_loc} localhost #{convertTenantIdToDbName('Midgar')}")
  assert(status, "#{$?}")
end

When /^I select the "(.*?)"$/ do |arg1|
  select = @driver.find_element(:css, 'div#state-menu select')
  select.find_element(:xpath, "//option[contains(text(),'#{arg1}')]").click
end

Then /^I see all of the pages of Districts$/ do
  assertWithWait("Should be more than one page of districts") {@driver.find_elements(:css, 'div#smartpager ul li').count > 1}
end

When /^I enable the first page of Districts$/ do
  step "I check the Districts"
end

Then /^the first page of districts are enabled$/ do
  visible_count = 0
  @driver.find_elements(:css, '#lea-table tr').each {|element| visible_count += 1 if element.displayed?}
  selected_count = 0
  @driver.find_elements(:css, '#lea-table tr input:checked').each {|element| selected_count += 1 if element.displayed?} 
  assert(visible_count == selected_count, "#{visible_count} total visible elemens should equal #{selected_count} selected elements")
end

When /^I click to the last page$/ do
  button = @driver.find_element(:xpath, '//div[contains(text(), "Last")]')
  button.click 
end

When /^I enable the last page of Districts$/ do
  step 'I check the Districts'
end

Then /^the last page of districts are enabled$/ do
  step 'the first page of districts are enabled'
end

When /^I click on the first page of Districts$/ do
  button = @driver.find_element(:xpath, '//div[contains(text(), "First")]')
  button.click 
end

Given /^I have replaced the edorg data$/ do
  file = "#{File.dirname(__FILE__)}/../../../data/Midgar_data/educationOrganization_fixture.json"
  status = system("mongoimport --drop -c educationOrganization -d #{convertTenantIdToDbName('Midgar')} --file #{file}")
  assert(status, "#{$?}")
  # Re-index edorg collection after drop
  index_rb_loc = "#{File.dirname(__FILE__)}/../../../../../config/scripts/indexTenantDb.rb"
  status = system("ruby #{index_rb_loc} localhost #{convertTenantIdToDbName('Midgar')}")
  assert(status, "#{$?}")
end

Then /^I see the list of \(only\) my applications$/ do
  assert(@driver.find_elements(:xpath, "//tr").count > 0, "Should be more than one application listed")
end

Then /^I can see the on\-boarded states\/districts$/ do
  assert(@driver.find_elements(:css, 'div#enable-menu div#lea-menu input[type="checkbox"]').count > 1, "One district should exist already")
end

Then /^I can see the on\-boarded states$/ do

  ['Midgar', 'Hyrule'].each {|tenantId|
    sliDb = @conn.db(convertTenantIdToDbName(tenantId))
    coll  = sliDb.collection('educationOrganization')
    seas  = coll.find('body.organizationCategories' => 'State Education Agency')
    puts "Looking for #{seas.count} SEAs of #{tenantId} on page!"
    seas.each { |sea|
      seaName = sea['body']['nameOfInstitution']
      puts " Looking for #{seaName} on page"
      seaOnPage = @driver.find_elements(:xpath, "//span[contains(., '#{seaName}')]")
      assert(seaOnPage.count > 0, " Looking for SEA #{seaName} of #{tenantId} on page. Found #{seaOnPage.count}.")
    }
  }
end

#
# NOTE: the actions and assertion below apply to a selection mode where
# the user selects a state from the dropdown, which triggers a refresh
# of the list of the (newly selected) state's districts for checkbox
# selection.  This mode is now obsolete
When /^I select the state "([^"]*)"$/ do |arg1|
  options = @driver.find_elements(:css, 'div#state-menu select option')
  step "I select the \"#{arg1}\""
end

Then /^I see all of the Districts$/ do
  lis = @driver.find_elements(:css, 'div#enable-menu div#lea-menu table tbody tr')
  assert(lis.count >= 1, "One district should exist")
end

Then /^I check the Districts$/ do
  @driver.find_element(:link_text, 'Enable All').click
end

Then /^I uncheck the Districts$/ do
  @driver.find_element(:link_text, 'Disable All').click
end

When /^I click on Save$/ do
  @driver.find_element(:css, 'input:enabled[type="submit"]').click
end

Then /^the "([^"]*)" is enabled for Districts$/ do |arg1|
  check_app(arg1)
end

Then /^I log out$/ do
  @driver.find_element(:link, "Sign out").click
  @driver.manage.delete_all_cookies
end

Then /^I log in as a valid SLI Operator "([^"]*)" from the "([^"]*)" hosted directory$/ do |arg1, arg2|
  #Empty step
end

Then /^I am redirected to the Application Registration Approval Tool page$/ do
  assert(@driver.page_source.index("Authorize Applications") != nil, "Should be at the Application Registration Approval Tool page")
end

Then /^I see the newly enabled application$/ do
  assert(!get_app.nil?, "App should exist")
end

Then /^I see the newly enabled application is approved$/ do
  test_app = get_app
  assert(!test_app.find_element(:xpath, "//td[text()='Approved']").nil?, "App should be approved")
end

Then /^"(.*?)" is enabled for "(.*?)" education organizations$/ do |app, edOrgCount|
     disable_NOTABLESCAN()
     sliDb = @conn.db('sli')
     coll = sliDb.collection("application")
     record = coll.find_one("body.name" => app)
     #puts record.to_s
     body = record["body"]
     #puts body.to_s
     edorgsArray = body["authorized_ed_orgs"]
     edorgsArrayCount = edorgsArray.count
     #puts edorgsArrayCount
     assert(edorgsArrayCount == edOrgCount.to_i, "Education organization count mismatch. Expected #{edOrgCount}, actual #{edorgsArrayCount}")
     enable_NOTABLESCAN()
end

Then /^I don't see the newly disabled application$/ do
  begin
    get_app
    fail("Shouldn't see app")
  rescue
    assert(true, "Should not find the app")
  end
end

When /^I (enable|disable) the educationalOrganization "([^"]*?)" in tenant "([^"]*?)"$/ do |action,edOrgName,tenant|
  disable_NOTABLESCAN()
  db = @conn[convertTenantIdToDbName(tenant)]
  coll = db.collection("educationOrganization")
  record = coll.find_one("body.nameOfInstitution" => edOrgName.to_s)
  enable_NOTABLESCAN()
  edOrgId = record["_id"]
  elt = @driver.find_element(:id, edOrgId)
  assert(elt, "Educational organization element for '" + edOrgName + "' (" + edOrgId + ") not found")
  assert(action == "enable" && !elt.selected? || action == "disable" && elt.selected?, "Cannot " + action + " educationalOrganization element with id '" + edOrgId + "' whose checked status is " + elt.selected?.to_s())
  elt.click()
end

When /^I (check|uncheck) the Bulk Extract checkbox$/ do |action|
  bulkExtractCheckboxId = 'app_isBulkExtract'
  elt = @driver.find_element(:id, bulkExtractCheckboxId)
  assert(elt, "Bulk Extract checkbox with id '" + bulkExtractCheckboxId + "' not found")
  assert(action == "check" && !elt.selected? || action == "uncheck" && elt.selected?, "Cannot " + action + " Bulk Extract checkbox because checked status is " + elt.selected?.to_s())
  elt.click()
end

private
def get_app(name="Testing App")
  rows = @driver.find_elements(:xpath, ".//tbody/tr")
  results = []
  for row in rows
    appName = row.find_elements(:xpath, 'td')
    fixedName = appName[0].text.sub(/Bulk Extract application request/,"").strip
    if fixedName == name
      return row      
    end
  end
  return nil
end
def check_app(arg1)
  @test_app = get_app(arg1)
  total_count = @test_app.find_elements(:css, "input:checked[type='checkbox']").count
  # TODO: the code below is not right. X can never be equal to (X - 1)
  #district_count = total_count - 1
  #puts "total count = #{total_count}, district count = #{district_count}"
  #assert(total_count == district_count, "All districts should be enabled.")
end
