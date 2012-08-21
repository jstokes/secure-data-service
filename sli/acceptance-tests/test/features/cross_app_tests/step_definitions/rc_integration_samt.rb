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
require 'approval'
require "mongo"
require 'rumbster'
require 'digest'

require_relative '../../utils/sli_utils.rb'
require_relative '../../utils/selenium_common.rb'

SEA_ADMINISTRATOR_EMAIL = 'testuser0.wgen@gmail.com'
EMAIL_PASSWORD = 'liferaywgen'

Before do
  @explicitWait = Selenium::WebDriver::Wait.new(:timeout => 60)
  @do_not_run_after = true
end

When /^I navigate to the user account management page$/ do
  samt_url = PropLoader.getProps['admintools_server_url']+PropLoader.getProps['samt_app_suffix']
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
    link = @driver.find_element(:xpath, "//a[@id='#{fullname}_delete']")
  rescue
  end
  unless(link.nil?)
    link.click
    begin
      @driver.switch_to.alert.accept
    rescue
    end
    sleep(3)
  end
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
  first_name = @user_full_name.split(" ", 2)[0]
  content = check_email_rc(first_name) do
    @driver.get(PropLoader.getProps["admintools_server_url"] + "/forgot_passwords")
    @driver.find_element(:id, "user_id").clear
    @driver.find_element(:id, "user_id").send_keys @user_email
    @driver.find_element(:id, "submit").click
  end

  reset_password_link = nil
  content.split("\n").each do |line|
    if(/#{PropLoader.getProps["admintools_server_url"]}/.match(line))
      reset_password_link = line
    end
  end
  reset_password_link = reset_password_link.strip[0..-("<br>".size + 1)]
  puts "reset password link = #{reset_password_link}"
  @driver.get(reset_password_link)

  @welcome_email_content = check_email_rc(first_name) do
    @driver.find_element(:id, "new_account_password_new_pass").clear
    @driver.find_element(:id, "new_account_password_new_pass").send_keys password
    @driver.find_element(:id, "new_account_password_confirmation").clear
    @driver.find_element(:id, "new_account_password_confirmation").send_keys password
    if(@mode == "sandbox")
      @driver.find_element(:id, "terms_and_conditions").click
    end
    @driver.find_element(:id, "submitForgotPasswordButton").click
  end
  puts @welcome_email_content
end

Then /^I can log in with my username "(.*?)" and password "(.*?)"$/ do |username, password|
  @driver.get(PropLoader.getProps["admintools_server_url"])
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
  #assertWithWait("Failed to navigate to the #{pageTitle} page")  {@driver.page_source.index("#{pageTitle}") != nil}
  sleep(3)
  begin
    assertText(pageTitle)
  rescue Exception => e
    puts @driver.page_source
    raise e
  end
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

def check_email_rc(subject_substring = nil, content_substring)
  imap_host = 'imap.gmail.com'
  imap_port = 993
  imap_user = @user_email
  imap_password = EMAIL_PASSWORD
  imap = Net::IMAP.new(imap_host, imap_port, true)
  imap.login(imap_user, imap_password)
  imap.examine('INBOX')
  not_so_distant_past = Date.today.prev_day.prev_day
  not_so_distant_past_imap_date = "#{not_so_distant_past.day}-#{Date::ABBR_MONTHNAMES[not_so_distant_past.month]}-#{not_so_distant_past.year}"
  messages_before = imap.search(['SINCE', not_so_distant_past_imap_date])
  imap.disconnect

  yield

  retry_attempts = 30
  retry_attempts.times do
    sleep 1
    imap = Net::IMAP.new(imap_host, imap_port, true, nil, false)
    imap.login(imap_user, imap_password)
    imap.examine('INBOX')

    messages_after = imap.search(['SINCE', not_so_distant_past_imap_date])
    messages_new = messages_after - messages_before
    messages_before = messages_after
    unless(messages_new.empty?)
      messages = imap.fetch(messages_new, ["BODY[HEADER.FIELDS (SUBJECT)]", "BODY[TEXT]"])
      messages.each do |message|
        content = message.attr["BODY[TEXT]"]
        subject = message.attr["BODY[HEADER.FIELDS (SUBJECT)]"]
        if((content_substring.nil? || (!content.nil? && content.include?(content_substring))) &&
            (subject_substring.nil? || (!subject.nil? && subject.include?(subject_substring))))
          return content
        end
      end
    end
    imap.disconnect
  end
  fail("timed out getting email with subject substring = #{subject_substring}, content substring = #{content_substring}")
end