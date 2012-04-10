require_relative '../../../utils/sli_utils.rb'
require_relative '../../dash/step_definitions/selenium_common_dash.rb'

Given /^the sampleApp is deployed on sampleApp server$/ do
  @appPrefix = "oauth2-sample/students"
end

When /^I navigate to the sampleApp home page$/ do
  url = PropLoader.getProps['sampleApp_server_address']
  url = url + @appPrefix
  puts url
  @driver.get url
  # There's a redirect to the realm page, so this assert should fail
  # assert(@driver.current_url == url, "Failed to navigate to "+url)
end

Then /^I should be redirected to the Realm page$/ do
  assert(@driver.current_url.include?("/api/oauth/authorize"))

end

When /^I select "([^"]*)" and click go$/ do |arg1|
  sleep(1)
  
 realm_select = @driver.find_element(:name=> "realmId")

  
  options = realm_select.find_elements(:tag_name=>"option")
  options.each do |e1|
    if (e1.text == arg1)
      e1.click()
      break
    end
  end
  clickButton("go", "id")
  
end

When /^I login as "([^"]*)" "([^"]*)"/ do | username, password |
    sleep(1)
    wait = Selenium::WebDriver::Wait.new(:timeout => 5) # explicit wait for at most 5 sec
    wait.until{@driver.find_element(:id, "IDToken1")}.send_keys username
    @driver.find_element(:id, "IDToken2").send_keys password
    @driver.find_element(:name, "Login.Submit").click
end

When /^I go to List of Students$/ do
  url = PropLoader.getProps['sampleApp_server_address']
  url = url + @appPrefix
  puts url
  @driver.get url
end

Then /^the page should include a table with header "([^"]*)"$/ do |name|
  sleep(10)
  puts @driver.page_source
  headerName=@driver.find_element(:id, "header.Student")
  headerName.text.should == name
end

Then /^I should see student "([^"]*)" in the student list$/ do |studentName|
  sleep(10)
  puts @driver.page_source
  name=@driver.find_element(:id, "name."+studentName)
  name.text.should == studentName
end

Given /^I am authenticated to SLI as "([^"]*)" "([^"]*)"$/ do |user, pass|
  url = PropLoader.getProps['sampleApp_server_address']
  url = url + PropLoader.getProps[@appPrefix]
  
  #url = "http://localhost:8080/dashboard"
  @driver.get(url)
  @driver.manage.timeouts.implicit_wait = 30
  @driver.find_element(:name, "j_username").clear
  @driver.find_element(:name, "j_username").send_keys user
  @driver.find_element(:name, "j_password").clear
  @driver.find_element(:name, "j_password").send_keys pass
  @driver.find_element(:name, "submit").click
end