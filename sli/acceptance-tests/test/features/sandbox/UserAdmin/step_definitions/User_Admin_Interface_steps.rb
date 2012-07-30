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

require_relative '../../../utils/sli_utils.rb'
require_relative '../../../utils/selenium_common.rb'

Before do 
   @explicitWait = Selenium::WebDriver::Wait.new(:timeout => 60)
   @db = Mongo::Connection.new.db(PropLoader.getProps['api_database_name'])
end



Given /^LDAP server has been setup and running$/ do
  @ldap = LDAPStorage.new(PropLoader.getProps['ldap_hostname'], PropLoader.getProps['ldap_port'], 
                          PropLoader.getProps['ldap_base'], PropLoader.getProps['ldap_admin_user'], 
                          PropLoader.getProps['ldap_admin_pass'])
  @email_sender_name= "Administrator"
  @email_sender_address= "noreply@slidev.org"
  @email_conf = {
    :host =>  PropLoader.getProps['email_smtp_host'],
    :port => PropLoader.getProps['email_smtp_port'],
    :sender_name => @email_sender_name,
    :sender_email_addr => @email_sender_address
  }
end

When /^I navigate to the sandbox user account management page$/ do
  samt_url = PropLoader.getProps['admintools_server_url']+PropLoader.getProps['samt_app_suffix']
  @driver.get samt_url
end


Then /^I am redirected to "(.*?)" page$/ do |pageTitle|
  assertWithWait("Failed to navigate to the #{pageTitle} page")  {@driver.page_source.index("#{pageTitle}") != nil}
end

Then /^I will be redirected to "(.*?)" login page$/ do |idpType|
  step "I was redirected to the \"#{idpType}\" IDP Login page"
end

When /^I hit the sandbox user account management app list all users page$/ do
  step "I navigate to the sandbox user account management page"
end

Then /^I see a table with headings of "(.*?)" and "(.*?)" and "(.*?)" and "(.*?)" and "(.*?)" and "(.*?)"$/ do |fullName, email, role,edorg, dateCreated, actions|
  check_heading(fullName)
  check_heading(email)
  check_heading(role)
  check_heading(edorg)
  check_heading(dateCreated)
  check_heading(actions)
end

Then /^I see a user with Full Name is "(.*?)" in the table$/ do | fullName|
 fullName = fullName.gsub("hostname",Socket.gethostname)
 fullName_element = @driver.find_element(:xpath,"//tr[td='#{fullName}']")
 assert(fullName_element!=nil,"can not find user with full name is #{fullName}")
  @userFullName = fullName
end

Then /^I see "(.*?)" has "(.*?)" and "(.*?)" role$/ do |full_name, role1, role2|
    roles=[ role1, role2 ]
    roles.sort! { |a,b| a.downcase <=> b.downcase }
    step "I see a user with Full Name is \"#{full_name}\" in the table"
    roles_element = @driver.find_element(:id,"#{full_name}_role")
    displayed_roles = roles_element.text().split(",")
    displayed_roles.each do |str|
        str.strip!
    end
    displayed_roles.sort! { |a,b| a.downcase <=> b.downcase }
    assert(roles.size == displayed_roles.size, "roles size do not match")
    for idx in 0 ... roles.size
        assert(roles[idx] == displayed_roles[idx], "user roles do not match #{roles[idx]} #{displayed_roles[idx]}")
    end
end

Then /^the user "(.*?)" is "(.*?)"$/ do |key, value|
  element = @driver.find_element(:id,"#{@userFullName}_#{key}")
  assert(element.text==value,"the user #{key} is #{element.text} but expected #{value}")
end

Then /^I will get an error message that "(.*?)"$/ do |message|
  assertText(message)
end

Given /^There is a sandbox user with "(.*?)" and "(.*?)" in LDAP Server$/ do |fullName, role|
  @test_env = "sandbox"
  new_user = create_new_user(fullName, role) 
  idpRealmLogin("sandboxoperator", nil)
  sessionId = @sessionId
  format = "application/json"
  restHttpDelete("/users/#{new_user['uid']}", format, sessionId)
  restHttpPost("/users", new_user.to_json, format, sessionId)
  
end

Given /^There is a sandbox user with "(.*?)", "(.*?)", "(.*?)", and "(.*?)" in LDAP Server$/ do |full_name, role, addition_roles, email|
  @test_env = "sandbox"
  new_user=create_new_user(full_name, role, addition_roles)
  new_user['email']=email.gsub("hostname", Socket.gethostname)
  new_user['uid']=new_user['email']

  idpRealmLogin("sandboxoperator", nil)
  sessionId = @sessionId
  format = "application/json"

  restHttpDelete("/users/#{new_user['uid']}", format, sessionId)
  restHttpPost("/users", new_user.to_json, format, sessionId)
  @user_full_name="#{new_user['firstName']} #{new_user['lastName']}"
  @user_unique_id=new_user['uid']
end

When /^I click the "(.*?)" link for "(.*?)"$/ do |button_name, user_name|
  user_name=user_name.gsub("hostname", Socket.gethostname)
  @driver.find_element(:xpath, "//button[@id='#{user_name}_#{button_name}']/a").click
end 

Then /^the (.*?) field is prefilled with "(.*?)"$/ do |field_name, value|
  field=getField(field_name)
  value=value.gsub("hostname", Socket.gethostname) 
  assert(field.attribute("value") == "#{value}", "#{value} do not match what's displayed #{field.attribute("value")}")
end

Then /^the Role combobox is populated with (.*?)$/ do |primary_role|
  drop_down = @driver.find_element(:id, "user_primary_role")
  option = drop_down.find_element(:xpath, ".//option[text()=#{primary_role}]")
  assert(option.attribute("selected")=="true", "#{primary_role} does not match what's expected: #{option.text()}")
end

Then /^the Role checkbox is checked with "(.*?)"$/ do |additional_role| 
  roles = additional_role.split(",")
  roles.each do |str|
    str.strip!
  end
  checkboxes=@driver.find_elements(:xpath, "//form/fieldset/div/div/label/input[@type=\"checkbox\"]")
  checkboxes.each do |checkbox|
    value = checkbox.attribute("value")
    if checkbox.attribute("checked")
      assert((roles.include? value), "Checkbox #{value} should not be checked!") 
    else 
      assert((roles.include? value) == false, "Checkbox #{value} should be checked!")
    end
  end
end

Then /^I can update the (.*?) field to "(.*?)"$/ do |field_name, new_value|
  field=getField(field_name)
  field.clear
  value=localize(new_value)
  field.send_keys value 
  if field_name == "\"Full Name\"" 
    @user_full_name=value
  end
end 

Then /^the user has "(.*?)" updated to "(.*?)"$/ do |table_header, new_value| 
  value=localize(new_value);
  tr=@driver.find_element(:id, @user_unique_id)
  td=tr.find_element(:id, "#{@user_full_name}_#{table_header.downcase.gsub(" ", "_")}")
  assert(td.text()==value, "#{table_header} not updated! Expecting: #{new_value}, got: #{td.text()}")
end

Then /^the user still has (.*?) as (.*?)$/ do |table_header, new_value|
    step "the user has #{table_header} updated to #{new_value.gsub("hostname_", "")}"
end 

Then /^I can change the Role from the dropdown to (.*?)$/ do |primary_role|
    step "I can select #{primary_role} from a choice between a \"Ingestion User\", \"Application Developer\" and \"Sandbox Administrator\" Role"
end

Then /^I can add additional Role "(.*?)"$/ do |optional_role|
  checkboxes=@driver.find_elements(:xpath, "//form/fieldset/div/div/label/input[@type=\"checkbox\"]")
  checkboxes.each do |checkbox|
    value = checkbox.attribute("value")
    if optional_role == value && checkbox.attribute("checked") != "true"
        sleep 1
        checkbox.click
    end
    if optional_role != value && checkbox.attribute("checked") == "true"
        sleep 1
        checkbox.click
    end
  end
end

Then /^the user now has roles "(.*?)" and "(.*?)"$/ do |role1, role2| 
  roles = [ role1, role2 ]
  roles.sort!
  tr=@driver.find_element(:id, @user_unique_id)
  td=tr.find_element(:id, "#{@user_full_name}_role")
  displayed = td.text().split(",")
  displayed.each do |str|
    str.strip!
  end
  displayed.sort!
  assert(roles.size == displayed.size, "roles size do not match")
  for idx in 0 ... roles.size
    assert(roles[idx] == displayed[idx], "user roles do not match #{roles[idx]} #{displayed[idx]}")
  end
end 

When /^I click on "(.*?)" icon$/ do |buttonName|
  @driver.find_element(:xpath, "//button[@id='#{@userFullName}_#{buttonName}']/a").click
end


Then /^I am asked to confirm the delete action$/ do
   #do nothing
end

When /^I confirm the delete action$/ do
  begin
    @driver.switch_to.alert.accept
  rescue
  end
  sleep(3)
end

Then /^that user is removed from LDAP$/ do
 if @test_env == "sandbox"
   idpRealmLogin("sandboxoperator", nil)
  else
  idpRealmLogin("operator", nil)
  end
  sessionId = @sessionId
  format = "application/json"
  restHttpGet("/users",format,sessionId)
  notFound = true
  result = JSON.parse(@res.body)
  result.each do |user|
  if user["fullName"] == @userFullName
  notFound = false 
  end
  puts user["fullName"]
  end
  assert(notFound,"the user #{@userFullName} is not removed from LDAP") 
end

Then /^the user entry is removed from the table$/ do
element =nil
  begin
   element = @driver.find_element(:xpath,"//tr[td='#{@userFullName}']")
  rescue
  end
  assert(element==nil,"the user #{@userFullName} is not removed from the table")
end

Then /^I see my Full Name is "(.*?)" in the table$/ do |fullName|
  step "I see a user with Full Name is \"#{fullName}\" in the table"
end

Then /^the "(.*?)" button is disabled$/ do |buttonName|
delete_button=nil
 # begin
  delete_button = @driver.find_element(:xpath,"//button[@id='#{@userFullName}_#{buttonName}' and @disabled = 'disabled']")
#  rescue
 # end
  assert(delete_button!=nil,"the #{buttonName} button is not disabled")
end

When /^I click on (.*?) button$/ do |buttonName|
  @driver.find_element(:xpath, "//a[text()=#{buttonName}]").click
end

Given /^the testing user does not already exists in LDAP$/ do
  idpRealmLogin("sandboxoperator", nil)
  sessionId = @sessionId
  format = "application/json"
  restHttpDelete("/users/"+Socket.gethostname+"_testuser@testwgen.net", format, sessionId)
end
    
When /^I have entered Full Name and Email into the required fields$/ do
  @driver.find_element(:name, 'user[fullName]').send_keys "Sandbox AcceptanceTests"
  @driver.find_element(:name, 'user[email]').send_keys Socket.gethostname+"_testuser@testwgen.net"
end

Then /^I can select "(.*?)" from a choice between a (.*?), (.*?) and (.*?) Role$/ do |role, choice1, choice2, choice3| 
    drop_down = @driver.find_element(:id, "user_primary_role")
    drop_down.click
    for i in [ choice1, choice2, choice3 ] do
        option = drop_down.find_element(:xpath, ".//option[text()=#{i}]")
        assert(option != nil)
    end
   
    drop_down.send_keys "#{role}.chr" 
    #option = dropDown.find_element(:xpath, ".//option[text()=#{role}]")
    #option.send_keys "\r"
end

Then /^I can also check "(.*?)" Role$/ do |r|
    @driver.find_element(:id, "#{r.downcase.gsub(" ", "_")}_role").click 
end
 
When /^I click (.*?) link$/ do |link|
  @driver.find_element(:xpath, "//a[text()=#{link}]").click
end 

When /^I click button "(.*?)"$/ do |not_in_use|
  @driver.find_element(:name, "commit").click
end

Then /^a "(.*?)" message is displayed$/ do |message|
    assertText(message)
end

def check_heading(heading_name)
heading_element=@driver.find_element(:xpath, "//tr[th='#{heading_name}']")
assert(heading_element!=nil,"can not find heading name #{heading_name}")
end

def assertText(text)
  @explicitWait.until{@driver.find_element(:tag_name,"body")}
  body = @driver.find_element(:tag_name, "body")
  assert(body.text.include?(text), "Cannot find the text \"#{text}\"")
end

def build_user(uid,firstName, lastName,groups,tenant,edorg)
new_user = {
      "uid" => uid,
      "groups" => groups,
      "firstName" => firstName,
      "lastName" => lastName,
      "password" => "#{uid}1234",
      "email" => "testuser@wgen.net",
      "tenant" => tenant,
      "edorg" => edorg,
      "homeDir" => "/dev/null"
  }
  append_hostname(new_user)
  
end
  
def append_hostname(user )
  oldUid = user["uid"]
  newUid = oldUid+"_"+Socket.gethostname
  user.merge!({"uid" => newUid})
  return user
end

def localize(value) 
  value=Socket.gethostname+"_"+value
end

def getField(field_name) 
  label=@driver.find_element(:xpath, "//label[text()=#{field_name}]")
  id=label.attribute("for")
  field=@driver.find_element(:id, "#{id}")
end

def create_new_user(fullName, role, addition_roles=nil)
  firstName = fullName.split(" ")[0]
  lastName = fullName.split(" ")[1].gsub("hostname",Socket.gethostname)
  groups = Array.new
  groups.push(role)
  if addition_roles != nil 
    more_roles = addition_roles.split(",")
    more_roles.each do |str|
        groups.push(str.strip)
    end
  end
     
  uid=firstName.downcase+"_"+lastName.downcase
  new_user=build_user(uid,firstName,lastName,groups,"sandboxadministrator@slidev.org","")
end

