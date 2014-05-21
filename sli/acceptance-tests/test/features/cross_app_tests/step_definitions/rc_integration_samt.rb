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


require "selenium-webdriver"
require 'approval'
require "mongo"
require 'rumbster'
require 'digest'

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'
require_relative '../../utils/email.rb'

Transform /^<([^>]*)>$/ do |human_readable_text|

  case human_readable_text
    when "PRIMARY_EMAIL"
      value = Property['primary_email_imap_registration_user_email']
      @email_username = Property['primary_email_imap_registration_user']
      @email_password = Property['primary_email_imap_registration_pass']
    when "PRIMARY_EMAIL_PASS"
      value = Property['primary_email_imap_registration_pass']
    when "SECONDARY_EMAIL"
      value = Property['secondary_email_imap_registration_user_email']
      @email_username = Property['secondary_email_imap_registration_user']
      @email_password = Property['secondary_email_imap_registration_pass']
    when "SECONDARY_EMAIL_PASS"
      value = Property['secondary_email_imap_registration_pass']
    when "CHARTER_EMAIL"
      value = Property['charter_email_imap_registration_user_email']
      @email_username = Property['charter_email_imap_registration_user']
      @email_password = Property['charter_email_imap_registration_pass']
    when "CHARTER_EMAIL_PASS"
      value = Property['charter_email_imap_registration_pass']
    when "DEVELOPER_EMAIL"
      value = Property['developer_email_imap_registration_user_email']
      @email_username = Property['developer_email_imap_registration_user']
      @email_password = Property['developer_email_imap_registration_pass']
    when "DEVELOPER_EMAIL_PASS"
      value = Property['developer_email_imap_registration_pass']
    when "DEVELOPER_SB_EMAIL"
      value = Property['developer_sb_email_imap_registration_user_email']
      @email_username = Property['developer_sb_email_imap_registration_user']
      @email_password = Property['developer_sb_email_imap_registration_pass']
    when "DEVELOPER_SB_EMAIL_PASS"
      value = Property['developer_sb_email_imap_registration_pass']
    when "DEVELOPER2_SB_EMAIL"
      value = Property['developer2_sb_email_imap_registration_user_email']
      @email_username = Property['developer2_sb_email_imap_registration_user']
      @email_password = Property['developer2_sb_email_imap_registration_pass']
    when "DEVELOPER2_SB_EMAIL_PASS"
      value = Property['developer2_sb_email_imap_registration_pass']
    when "LANDINGZONE"
      value = Property['landingzone']
    when "LANDINGZONE_PORT"
      value = Property['landingzone_port']
    when "TENANT"
      puts "Tenant in Transform = #{Property[:tenant]}"
      value = Property['tenant']
    when "SANDBOX_TENANT"
      value = Property['sandbox_tenant']
    when "CI_IDP_Redirect_URL"
      value = Property[:ci_idp_redirect_url]
    when "MATT SOLLARS UNIQUE ID"
      value = "800000025"
    when "CARMEN ORTIZ UNIQUE ID"
      value = "900000016"
    when "BRANDON SUZUKI UNIQUE ID"
      value = "100000022"
    when "ZOE LOCUST UNIQUE ID"
      value = "900000009"
    when "REBECCA BRAVERMAN UNIQUE ID"
      value = "rbraverman"
    when "AMY KOPEL UNIQUE ID"
      value = "akopel"
    when 'CI_ARTIFACT_IDP_ID_URL'
      value = Property['ci_artifact_idp_id_url']
    when 'CI_ARTIFACT_IDP_REDIRECT_URL'
      value = Property['ci_artifact_idp_redirect_url']
    when 'CI_ARTIFACT_IDP_ARTIFACT_RESOLUTION_URL'
      value = Property['ci_artifact_idp_artifact_resolution_url']
    when 'CI_ARTIFACT_SOURCE_ID'
      value = Property['ci_artifact_source_id']
    when 'CI_ARTIFACT_IDP_TYPE'
      value = Property['ci_artifact_idp_type']
    when 'POST_ENCRYPT_IDP_ID_URL'
      value = Property['post_encrypt_idp_id_url']
    when 'POST_ENCRYPT_IDP_REDIRECT_URL'
      value = Property['post_encrypt_idp_redirect_url']
    when 'POST_ENCRYPT_IDP_TYPE'
      value = Property['post_encrypt_idp_type']
    when 'Pre-installed Bulk Extract App Name'
      value = Property['bulk_extract_testapp_name']
    when 'RC Server'
      value = Property['rc_env']
  end

 value
end

Before do
  @explicitWait = Selenium::WebDriver::Wait.new(:timeout => 60)
end

When /^I navigate to the user account management page$/ do
  samt_url = "#{Property[:admintools_server_url]}/users"
  @driver.get samt_url
end

Then /^I will be redirected to the realm selector web page$/ do
  assertWithWait("Failed to navigate to Realm chooser") {@driver.title.index("Choose your realm") != nil}
end

When /^I select the realm "([^"]*)"$/ do |realm|
  select = Selenium::WebDriver::Support::Select.new(@driver.find_element(:tag_name, "select"))
  select.select_by(:text, realm)
  @driver.find_element(:id, "go").click
end

Then /^I am redirected to "(.*?)" login page$/ do |idpType|
  step "I was redirected to the \"#{idpType}\" IDP Login page"
end

When /^I click on the (".*?") button$/ do |buttonName|
  @explicitWait.until{@driver.find_element(:xpath, "//a[text()=#{buttonName}]")}.click
end

Then /^I delete the user "(.*?)" if exists$/ do |fullname|
  begin
    links = @driver.find_elements(:xpath, "//a[@id='#{fullname}_delete']")
    puts "admin account management debug = #{links}\n"
  rescue
  end
  unless(links.nil?)
    links.each do |link|
      link.click
      begin
        @driver.switch_to.alert.accept
      rescue
      end
      sleep(3)
    end
  end
end

Then /^I delete the user "(.*?)"$/ do |fullname|
  @driver.find_element(:xpath, "//a[@id='#{fullname}_delete']").click
  begin
      @driver.switch_to.alert.accept
  rescue
  end
  sleep(3)
end

Then /^I can directly update the (.*?) field to "(.*?)"$/ do |field_name, new_value|
  field = getField(field_name)
  field.clear
  field.send_keys new_value
  if field_name == "\"Full Name\""
    @user_full_name = new_value
  end
  if field_name == "\"Email\""
    @user_email = new_value
  end
end

Then /^I set my password to "(.*?)"$/ do |password|
  @driver.get "#{Property["admintools_server_url"]}/reset_password"
  @driver.find_element(:id, "user_id").clear
  @driver.find_element(:id, "user_id").send_keys @user_email
  @driver.find_element(:id, "submit").click

  first_name = @user_full_name.split(" ", 2)[0]
  # content = check_email({:content_substring => first_name,
  #                        :subject_substring => "Reset Password",
  #                        :imap_username => @email_username,
  #                        :imap_password => @email_password}) do
  #   @driver.get(Property["admintools_server_url"] + "/forgot_passwords")
  #   @driver.find_element(:id, "user_id").clear
  #   @driver.find_element(:id, "user_id").send_keys @user_email
  #   @driver.find_element(:id, "submit").click
  # end


  @welcome_email_content = check_email({:content_substring => first_name,
                                        :subject_substring => 'Reset Password',
                                        :imap_username => @email_username,
                                        :imap_password => @email_password}) do
    reset_password_link = nil
    content.split("\n").each do |line|
      if(/#{Property["admintools_server_url"]}/.match(line))
        reset_password_link = line.strip
      end
    end
    puts "reset password link: #{reset_password_link}"
    @driver.get(reset_password_link)

    @driver.find_element(:id, "new_account_password_new_pass").clear
    @driver.find_element(:id, "new_account_password_new_pass").send_keys password
    @driver.find_element(:id, "new_account_password_confirmation").clear
    @driver.find_element(:id, "new_account_password_confirmation").send_keys password
    if(@mode == "SANDBOX")
      @driver.find_element(:id, "terms_and_conditions").click
    end
    @driver.find_element(:id, "submitForgotPasswordButton").click
  end
  puts @welcome_email_content
end

Then /^I can log in with my username "(.*?)" and password "(.*?)"$/ do |username, password|
  @driver.get(Property["admintools_server_url"])
  @driver.find_element(:id, "user_id").clear
  @driver.find_element(:id, "user_id").send_keys username
  @driver.find_element(:id, "password").clear
  @driver.find_element(:id, "password").send_keys password
  @driver.find_element(:id, "login_button").click
  actual_page_content = @driver.find_element(:tag_name, "body")
  expected_page_content = "Admin Tool"
  assert(actual_page_content.text.include?(expected_page_content), "Cannot find page id: #{expected_page_content}")
end

Then /^I am redirected to the "(.*?)" page$/ do |pageTitle|
  assertText(pageTitle)
end

Then /^I can select "(.*?)" from a choice of "(.*?)" Role$/ do |role, choices|
  drop_down = @explicitWait.until{@driver.find_element(:id, "user_primary_role")}
  #drop_down.click
  for i in choices.split(",")  do
    i.strip!
    option = @explicitWait.until{drop_down.find_element(:xpath, ".//option[text()=\"#{i}\"]")}
    assert(option != nil)
  end

  options = @explicitWait.until{drop_down.find_elements(:xpath, ".//option")}
  assert(options.size == choices.split(",").size, "Only has #{options.size} choices, but requirement has #{choices.split(",").size} chioces")

  select = Selenium::WebDriver::Support::Select.new(@explicitWait.until{@driver.find_element(:id, "user_primary_role")})
  select.select_by(:text, role)
end

Then /^the "(.*?)" message is displayed$/ do |message|
  assertText(message)
end

Then /^the newly created user has (.*?) updated to (.*?)$/ do |field_name, value|
  @user_unique_id=@user_email
  step "the user has #{field_name} updated to #{value}"
end
