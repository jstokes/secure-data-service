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
require 'open3'
require 'set'

require_relative '../../../utils/sli_utils.rb'
require_relative '../../../utils/selenium_common.rb'

Given /^the testing device app key has been created$/ do
  @oauthClientId = "EGbI4LaLaL"
  @oauthClientSecret = "iGdeAGCugi4VwZNtMJR062nNKjB7gRKUjSB0AcZqpn8Beeee"
  @oauthRedirectURI = "http://device"
end

Given /^I setup the data needed for the token generator$/ do
  @user_info = {
      "student.m.sollars" => {:unique_id => "800000025", :role => "Student", :student? => true, :edOrg => "East Daybreak Junior High"},
      "leader.m.sollars" => {:unique_id => "800000025", :role => "StudentLeader", :student? => true, :edOrg => "East Daybreak Junior High"},
      "cegray" => {:unique_id => "cgray", :role => "Student", :student? => true, :edOrg => "East Daybreak Junior High"},
      "carmen.ortiz" => {:unique_id => "900000016", :role => "Student", :student? => true, :edOrg => "Daybreak Central High"},
      "jstevenson" => {:unique_id => "jstevenson", :role => "IT Administrator", :student? => false, :edOrg => "IL-DAYBREAK"},
      "rrogers" => {:unique_id => "rrogers", :role => "IT Administrator", :student? => false, :edOrg => "STANDARD-SEA"},
      "cgray" => {:unique_id => "cgray", :role => "Educator", :student? => false, :edOrg => "Daybreak Central High"}
  }

  @realm_info = {
      "Illinois Daybreak Students" => {:unique_id => "IL-Daybreak-Students", :tenant => "Midgar"},
      "Illinois Daybreak School District 4529" => {:unique_id => "IL-Daybreak", :tenant => "Midgar"}
  }
end


Given /^I log in to realm "(.*?)" using simple-idp as "(.*?)" "(.*?)" with password "(.*?)"$/ do |realm, user_type, user, pass|
  step "the testing device app key has been created"
  step "I setup the data needed for the token generator"

  if ENV["use_token_gen"] == "true" and @user_info[user] && @realm_info[realm]
    @sessionId = get_token_from_generator @user_info[user][:unique_id], @user_info[user][:role], @realm_info[realm][:tenant], @realm_info[realm][:unique_id], @user_info[user][:edOrg], 600, @oauthClientId, @user_info[user][:student?]
  else
    step "I have an open web browser"
    step "I navigate to the API authorization endpoint with my client ID"
    step "I was redirected to the Realm page"
    step "I choose realm \"#{realm}\" in the drop\-down list"
    step "I click on the realm page Go button"

    step "I was redirected to the \"Simple\" IDP Login page"
    step "I submit the credentials \"#{user}\" \"#{pass}\" for the \"Simple\" login page"
    step "I should receive a json response containing my authorization code"

    step "I navigate to the API token endpoint with my client ID, secret, authorization code, and redirect URI"
    step "I should receive a json response containing my authorization token"
  end
  step "I should be able to use the token to make valid API calls"
end

Given /^I navigated to the Data Browser Home URL$/ do
  @driver.get PropLoader.getProps['databrowser_server_url']
end

Given /^I was redirected to the Realm page$/ do
  assertWithWait("Failed to navigate to Realm chooser") {@driver.title.index("Choose your realm") != nil}
end

When /^I choose realm "(.*?)" in the drop\-down list$/ do |arg1|
  select = Selenium::WebDriver::Support::Select.new(@driver.find_element(:tag_name, "select"))
  select.select_by(:text, arg1)
end

Given /^I click on the realm page Go button$/ do
  assertWithWait("Could not find the Go button")  { @driver.find_element(:id, "go") }
  @driver.find_element(:id, "go").click
end

Given /^I was redirected to the SLI IDP Login page$/ do
  assertWithWait("Was not redirected to the IDP login page")  { @driver.find_element(:name, "Login.Submit") }
end

Given /^I import the odin-local-setup application and realm data$/ do
  @ci_realm_store_path = File.dirname(__FILE__) + '/../../../../../../../tools/jmeter/odin-ci/'
  @local_realm_store_path = File.dirname(__FILE__) + '/../../../../../../../tools/jmeter/odin-local-setup/'
  #get current working dir
  current_dir = Dir.getwd
  # Get current server environment (ci or local) from properties.yml
  app_server = PropLoader.getProps['app_bootstrap_server']
  # Drop in ci specific app-auth fixture data
  if app_server == "ci"
    puts "\b\bDEBUG: We are setting CI environment app auth data"
    Dir.chdir(@ci_realm_store_path)
    `sh ci-jmeter-realm.sh`
  # Drop in local specific app-auth fixture data
  elsif app_server == "local"
    puts "\b\bDEBUG: We are setting LOCAL environment app auth data"
    Dir.chdir(@local_realm_store_path)
    `sh local-jmeter-realm.sh`
  else
    puts "\n\nWARNING: No App server context set, assuming CI environment.."
    Dir.chdir(@ci_realm_store_path)
    `sh ci-jmeter-realm.sh`
  end
  # restore back current dir
  Dir.chdir(current_dir)
end

When /^I navigate to the API authorization endpoint with my client ID$/ do
  @driver.get PropLoader.getProps['api_server_url'] + "/api/oauth/authorize?response_type=code&client_id=#{@oauthClientId}"
end

Then /^I should be redirected to the realm choosing page$/ do
  assertWithWait("Failed to navigate to Realm chooser") {@driver.title.index("Choose your realm") != nil}
end

When /^I select "(.*?)" drop the dropdown and click go$/ do |arg1|
  select = Selenium::WebDriver::Support::Select.new(@driver.find_element(:tag_name, "select"))
  select.select_by(:text, arg1)
  assertWithWait("Could not find the Go button")  { @driver.find_element(:id, "go") }
  @driver.find_element(:id, "go").click
end

Then /^I should receive a json response containing my authorization code$/ do
  assertWithWait("Could not find text 'authorization_code' on page") {@driver.page_source.include?("authorization_code")}

  @oauthAuthCode = @driver.page_source.match(/"authorization_code":"(?<Code>[^"]*)"/)[:Code]
  puts "DEBUG: @oauthAuthCode is #{@oauthAuthCode}"
end

When /^I navigate to the API token endpoint with my client ID, secret, authorization code, and redirect URI$/ do
  @driver.get PropLoader.getProps['api_server_url'] + "/api/oauth/token?response_type=code&client_id=#{@oauthClientId}" +
  "&client_secret=#{@oauthClientSecret}&code=#{@oauthAuthCode}&redirect_uri=#{@oauthRedirectURI}"
end

Then /^I should receive a json response containing my authorization token$/ do
  assertWithWait("Could not find text 'authorization_token' on page") {@driver.page_source.include?("access_token")}

  @sessionId = @driver.page_source.match(/"access_token":"(?<Token>[^"]*)"/)[:Token]
  puts "sessionId = #@sessionId"
end

Then /^I should be able to use the token to make valid API calls$/ do
  restHttpGet("/system/session/check", "application/json")
  assert(@res != nil, "Response from rest-client GET is nil")
  data = JSON.parse(@res.body)
  assert(data != nil, "Response body is nil")
  assert(data['authenticated'] == true,
  "Session debug context 'authentication.authenticated' is not true")
end


def get_token_from_generator(user, role, tenant, realm, edOrg, expiration_in_seconds, client_id, is_student)
  script_loc = File.dirname(__FILE__) + "/../../../../../../opstools/token-generator/generator.rb"
  student = "--student" if is_student
  out, status = Open3.capture2("ruby #{script_loc} -e #{expiration_in_seconds} -c #{client_id} -u #{user} -r \"#{role}\" -t \"#{tenant}\" -R \"#{realm}\" -E \"#{edOrg}\" #{student}")
  match = /token is (.*)/.match(out)
  sessionId = match[1]
  puts("The generated token is #{sessionId}") if $SLI_DEBUG
  return sessionId
end