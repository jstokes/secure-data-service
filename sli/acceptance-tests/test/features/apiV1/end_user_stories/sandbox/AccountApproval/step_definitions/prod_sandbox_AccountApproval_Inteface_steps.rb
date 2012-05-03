require "selenium-webdriver"

require_relative '../../../../../utils/sli_utils.rb'
require_relative '../../../../../utils/selenium_common.rb'




Given /^I am authenticated to SLI IDP as user "([^"]*)" with pass "([^"]*)"$/ do |arg1, arg2|
  url =PropLoader.getProps['admintools_server_url']+"/account_managements"
  @driver.get url
  assertWithWait("Failed to navigate to the SLI IDP to authenticate")  {@driver.find_element(:id, "IDToken1")}
  @driver.find_element(:id, "IDToken1").send_keys arg1
  @driver.find_element(:id, "IDToken2").send_keys arg2
  @driver.find_element(:name, "Login.Submit").click
  begin
    @driver.switch_to.alert.accept
  rescue
  end
end

Given /^LDAP server has been setup and running$/ do
  @ldap = LDAPStorage.new(PropLoader.getProps['ldap.hostname'], 389, "ou=DevTest,dc=slidev,dc=org", "cn=DevLDAP User, ou=People,dc=slidev,dc=org", "Y;Gtf@w{")
end

Given /^there are accounts in requests pending in the system$/ do
  clear_all()
  user_info = {
      :first => "Loraine",
      :last => "Plyler", 
       :email => "jdoe"+String(rand(4))+"@example.com",
       :password => "secret", 
       :emailtoken => "token",
       :vendor => "Macro Corp",
       :status => "pending"
   }
  @ldap.create_user(user_info)
end


When /^I hit the Admin Application Account Approval page$/ do
  url = PropLoader.getProps['admintools_server_url']
  if @account_approval_env == "sandbox"
    url = url + "/account_managements?env=sandbox"
  else
    url = url +"/account_managements?reset=true"
  end
  @driver.get url
  begin
    @driver.switch_to.alert.accept
  rescue
  end
end

Then /^I see a table with headings of "([^"]*)" and "([^"]*)" and "([^"]*)" and "([^"]*)" and "([^"]*)"$/ do |vendor, user_name, last_update, status, action|
 vendor_header= @driver.find_element(:id,"vendor")
 assert(vendor_header.text==vendor,"didnt find #{vendor} in the heading")
 user_name_header= @driver.find_element(:id,"user_name")
 assert(user_name_header.text==user_name,"didnt find #{user_name} in the heading")
 last_update_header= @driver.find_element(:id,"last_update")
 assert(last_update_header.text==last_update,"didnt find #{last_update} in the heading")
 status_header= @driver.find_element(:id,"status")
 assert(status_header.text==status,"didnt find #{status} in the heading")
 action_header= @driver.find_element(:id,"action")
 assert(action_header.text==action,"didnt find #{action} in the heading")
end

Then /^on the next line there is vendor name in the "([^"]*)" column$/ do |arg1|

  vendors=@driver.find_elements(:xpath,"//td[@class='vendor']")
  assert(vendors.length>0,"didnt find vendor name in #{arg1} column")
end

Then /^User Name in the "([^"]*)" column$/ do |arg1|
  user_names=@driver.find_elements(:xpath,"//td[@class='user_name']")
  assert(user_names.length>0,"didnt find User name in #{arg1} column")
end

Then /^last update date in the "([^"]*)" column$/ do |arg1|
  last_updates=@driver.find_elements(:xpath,"//td[@class='last_update']")
  assert(last_updates.length>0,"didnt find Last Update in #{arg1} column")
end

Then /^status in the "([^"]*)" column$/ do |arg1|
  status_elements=@driver.find_elements(:xpath,"//td[@class='account_management_table_pendingStatus']")
  assert(status_elements.length>0,"didnt find status in #{arg1} column")
end

Then /^the "([^"]*)" column has (\d+) buttons "([^"]*)", "([^"]*)", "([^"]*)", and "([^"]*)"$/ do |arg1,arg2,approve_button, reject_button, disable_button, enable_button|
 approve_buttons=@driver.find_elements(:xpath,"//input[@value='Approve']")
 assert(approve_buttons.length>0,"didnt find #{approve_button} in action column")
 reject_buttons=@driver.find_elements(:xpath,"//input[@value='Reject']")
 assert(reject_buttons.length>0,"didnt find #{reject_button} in action column")
 disable_buttons=@driver.find_elements(:xpath,"//input[@value='Disable']")
 assert(disable_buttons.length>0,"didnt find #{disable_button} in action column")
 enable_buttons=@driver.find_elements(:xpath,"//input[@value='Enable']")
 assert(enable_buttons.length>0,"didnt find #{enable_button} in action column")
end

Given /^there is a "([^"]*)" production account request for vendor "([^"]*)"$/ do |status,vendor|
  clear_all()
  user_info = {
      :first => "Loraine",
      :last => "Plyler", 
       :email => "jdoe"+String(rand(4))+"@example.com",
       :password => "secret", 
       :emailtoken => "token",
       :vendor => vendor,
       :status => status
   }
  @ldap.create_user(user_info)
end

Then /^I see one account with name "([^"]*)"$/ do |user_name|
  user=@driver.find_element(:id,"username."+user_name)
  assert(user.text==user_name,"didnt find the account with name #{user_name}")
  @user_name=user_name
end

Then /^his account status is "([^"]*)"$/ do |arg1|
  status=@driver.find_element(:id,"status."+@user_name)
  assert(status.text==arg1,"user account status is not #{arg1}")
end

When /^I click the "([^"]*)" button$/ do |button_name|
  @driver.find_element(:id,button_name.downcase+"_button_"+@user_name).click
end

When /^I am asked "([^"]*)"$/ do |arg1|
  begin
        @driver.switch_to.alert
      rescue
      end
end

When /^I click on Ok$/ do
  @driver.switch_to.alert.accept
end

Then /^his account status changed to "([^"]*)"$/ do |arg1|
  status=@driver.find_element(:id,"status."+@user_name)
  assert(status.text==arg1,"user account status is not #{arg1}")
end


Given /^there is an pending sandbox account  for vendor "([^"]*)"$/ do |arg1|
 @account_approval_env="sandbox"
end

Given /^there is an approved sandbox account  for vendor "([^"]*)"$/ do |arg1|
  @account_approval_env="sandbox"
end

def clear_all
  users=@ldap.read_users()
  if users.length>0
  users.each do |user|
  @ldap.delete_user(user[:email])
  end
  end
end

