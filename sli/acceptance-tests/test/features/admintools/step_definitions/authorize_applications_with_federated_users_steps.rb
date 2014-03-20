=begin

Copyright 2012-2014 inBloom, Inc. and its affiliates.

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

require_relative 'capybara_setup.rb'
require 'selenium-webdriver'

require 'pry'

When /^I navigate to the Custom Role Mapping page$/ do
  browser.visit path_for('')
  login_to_the_inbloom_realm

  browser.page.should have_link("Custom Roles")
end

Then /^I should get to the correct Custom Role Mapping Page$/ do
  browser.page.click_link 'Custom Roles'
  # Click on the Custom Roles for item 'Illinois Daybreak School District 4529'
  browser.page.find(:xpath, "//tr[td[contains(.,'Illinois Daybreak School District 4529')]]/td/a", :text => 'Custom Roles').click

  browser.page.should have_text('Custom Roles for Illinois Daybreak School District 4529')
  browser.page.should have_button('resetToDefaultsButton')
end

When /^I click on the Reset to Defaults button$/ do
  browser.page.click_button('resetToDefaultsButton')

  browser.alert_popup_message.should eql('Resetting to default roles will remove any existing role mapping and will restore the default roles.  This operation cannot be undone.  Are you sure you want to reset?')
  browser.confirm_popup
end

Then /^The page should be reset back to default$/ do
  numChecked = 0

  # Check for correct default group titles should be the same as titles
  ["Educator","Leader","Aggregate Viewer","IT Administrator", "Student", "Parent"].each do |role|
    results = browser.find(:xpath, "//td/div[text()='#{role}']")
    moreResults = browser.find(:xpath, "//td/div/span[text()='#{role}']")

    results.text.should eql(moreResults.text)
  end

  # Check IT Administrator is the only Admin Role
  browser.all("input[type='checkbox']").each do |checkbox|
    if checkbox.checked? == true
      numChecked += 1
    end
  end

  numChecked.should == 1
  browser.find(:xpath, '/html/body/div/div[2]/div[2]/div/table/tbody/tr[3]/td[4]/input').should be_checked
end

# This step might need to be refactored
When /^I add a new role group$/ do
  browser.click_button('Add Role Group')
  browser.fill_in('Enter group name', :with => 'Application Authorizer')
  browser.fill_in('addRoleInput', :with => 'Application Authorizer')
  browser.click_on('Add')
  browser.select('APP_AUTHORIZE', :from => 'addRightSelect')
  browser.click_on('addRightButton')
  browser.select('READ_GENERAL', :from => 'addSelfRightSelect')
  browser.click_on('addSelfRightButton')
  browser.click_on('Save')
end

# Possible refactor will be required
Then /^I should see the new role group$/ do
  browser.within '#custom_roles' do
    @new_custom_role = browser.find(:xpath, '/html/body/div/div[2]/div[2]/div/table/tbody/tr[7]')

    browser.within @new_custom_role do
      browser.find('.groupTitle').text.should == 'Application Authorizer'
      browser.find('.role').text.should == 'Application Authorizer'
      browser.find('.right').text.should == 'APP_AUTHORIZE'
      browser.find('.self-right').text.should == 'READ_GENERAL'
    end
  end
end

And /^I create new application "([^"]*)"$/ do |app_name|
  fill_in_application_fields(:name => app_name,
                             :description => 'new application',
                             :version => '0.9',
                             :application_url => 'https://example.com',
                             :administration_url => 'https://example.com',
                             :redirect_uri => 'https://example.com',
                             :image_url => 'https://example.com')
  browser.check('app[installed]')
  browser.click_button 'Register'
end

Then /^Application "([^"]*)" should be created$/ do |app_name|
  browser.page.should have_css('#notice')
  browser.page.should have_content(app_name)
  browser.reset_session!
end
