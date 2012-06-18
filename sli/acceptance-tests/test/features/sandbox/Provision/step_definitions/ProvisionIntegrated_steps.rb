require "selenium-webdriver"
require 'approval'
require "mongo" 
require 'rumbster'

require_relative '../../../utils/sli_utils.rb'
require_relative '../../../utils/selenium_common.rb'

Before do 
   @explicitWait = Selenium::WebDriver::Wait.new(:timeout => 60)
   @db = Mongo::Connection.new.db(PropLoader.getProps['api_database_name'])
   @edorgId =  "Test Ed Org"
end


Transform /^<([^"]*)>$/ do |human_readable_id|
  id = @email                                       if human_readable_id == "USERID"
  id = "test1234"                                   if human_readable_id == "PASSWORD"
  id = @edorgId                                     if human_readable_id == "EDORG_NAME"
  id = "devldapuser@slidev.org"                     if human_readable_id == "DEVELOPER_EMAIL"
  id
end


Given /^LDAP server has been setup and running$/ do
  ldap_base=PropLoader.getProps['ldap_base']
  @ldap = LDAPStorage.new(PropLoader.getProps['ldap_hostname'], 389, ldap_base, "cn=DevLDAP User, ou=People,dc=slidev,dc=org", "Y;Gtf@w{")
   @email_sender_name= "Administrator"
     @email_sender_address= "noreply@slidev.org"
      email_conf = {
       :host => 'mon.slidev.org',
       :port => 3000,
       :sender_name => @email_sender_name,
       :sender_email_addr => @email_sender_address
     }
  
  ApprovalEngine.init(@ldap,Emailer.new(email_conf),nil,true)
end

Given /^there is an account in ldap for vendor "([^"]*)"$/ do |vendor|
@vendor = vendor

end

Given /^the account has a tenantId "([^"]*)"$/ do |tenantId|
#@email="devldapuser_#{Socket.gethostname}@slidev.org"
@email="devldapuser@slidev.org"
@tenantId = tenantId
removeUser(@email)
sleep(1)

  user_info = {
      :first => "Provision",
      :last => "test",
       :email => @email,
       :emailAddress => @email,
       :password => "test1234",
       :emailtoken => "token",
       :vendor => @vendor,
       :status => "submitted",
       :homedir => "changeit",
       :uidnumber => "500",
       :gidnumber => "500",
       :tenantId => @tenantId
   }

  ApprovalEngine.add_disabled_user(user_info)
  ApprovalEngine.change_user_status(@email, ApprovalEngine::ACTION_ACCEPT_EULA)
  user_info = ApprovalEngine.get_user(@email)
  ApprovalEngine.verify_email(user_info[:emailtoken])
  #ApprovalEngine.change_user_status(@email,"approve",true)
  #clear_edOrg()
  #clear_tenant()
end

Given /^there is no corresponding tenant in mongo$/ do
  clear_tenant
end

Given /^there is no corresponding ed\-org in mongo$/ do
  clear_edOrg
end

When /^the developer provision a "(.*?)" Landing zone with edorg is "(.*?)"$/ do |env,edorgId|
  step "I provision with \"#{env}\" high\-level ed\-org to \"#{edorgId}\""
  end
  
Then /^a tenantId "([^"]*)" created in Mongo$/ do |tenantId|
 tenant_coll=@db["tenant"]
 assert( tenant_coll.find("body.tenantId" => tenantId).count >0 ,"the tenantId #{tenantId} is not created in mongo")
end

Then /^the directory structure for the landing zone is stored for tenant in mongo$/ do
 tenant_coll=@db["tenant"]
 tenant_entity = tenant_coll.find("body.tenantId" => @tenantId)
 puts tenant_entity
end

Then /^the user gets a success message$/ do
  pending # express the regexp above with the code you wish you had
end


When /^the developer is authenticated to Simple IDP as user "([^"]*)" with pass "([^"]*)"$/ do |user, pass|
  step "I submit the credentials \"#{user}\" \"#{pass}\" for the \"Simple\" login page"
end


When /^the developer go to the provisioning application web page$/ do
  url =PropLoader.getProps['admintools_server_url']+"/landing_zone"
  @driver.get url
end

When /^I provision with "([^"]*)" high\-level ed\-org to "([^"]*)"$/ do |env,edorgName|
  if(env=="sandbox")
  @driver.find_element(:id, "custom").click
  end
  @driver.find_element(:id, "custom_ed_org").send_keys edorgName
  @driver.find_element(:id, "provisionButton").click
  @edorgName=edorgName
end

Then /^I get the success message$/ do
  assertWithWait("No success message") {@driver.find_element(:id, "successMessage") != nil}
end

Then /^an ed\-org is created in Mongo with the "([^"]*)" is "([^"]*)"$/ do |key1, value1|
step "I am logged in using \"operator\" \"operator1234\" to realm \"SLI\""
  uri="/v1/educationOrganizations"
  uri=uri+"?"+URI.escape(key1)+"="+URI.escape(value1)
  restHttpGet(uri)
  assert(@res.length>0,"didnt see a top level ed org with #{key1} is #{value1}")
end

Then /^a request to provision a landing zone is made$/ do
   # this request is made by landing zone app in admin tools
end

Then /^the directory structure for the landing zone is stored in ldap$/ do
  user=@ldap.read_user(@email)
  assert(user[:homedir].include?(@edorgName),"the landing zone path is not stored in ldap")
end

def removeUser(email)
  if ApprovalEngine.user_exists?(email)
  ApprovalEngine.remove_user(email)
  end
end
def clear_edOrg
   edOrg_coll=@db["educationOrganization"]
   edOrg_coll.remove("body.stateOrganizationId"=>@edorgId)
   assert(edOrg_coll.find("body.stateOrganizationId"=>@edorgId).count==0,"edorg with stateOrganizationId #{@edorgId} still exist in mongo")
end

def clear_tenant
   tenant_coll=@db["tenant"]
   tenant_coll.remove("body.tenantId"=>@tenantId)
   assert(tenant_coll.find('body.tenantId'=> @tenantId).count()==0,"tenant with tenantId #{@tenantId} still exist in mongo")
end
