#bulk extract
Dir["./test/features/bulk_extract/features/step_definitions/*.rb"].each {|file| require file}

When /^the operator triggers a bulk extract for tenant "(.*?)"$/ do |tenant|

command  = "sh #{TRIGGER_SCRIPT}"
if (PROPERTIES_FILE !=nil && PROPERTIES_FILE != "")
  command = command + " -Dsli.conf=#{PROPERTIES_FILE}" 
  puts "Using extra property: -Dsli.conf=#{PROPERTIES_FILE}"
end
if (KEYSTORE_FILE !=nil && KEYSTORE_FILE != "")
  command = command + " -Dsli.encryption.keyStore=#{KEYSTORE_FILE}" 
  puts "Using extra property: -Dsli.encryption.keyStore=#{KEYSTORE_FILE}"
end
if (JAR_FILE !=nil && JAR_FILE != "")
  command = command + " -f#{JAR_FILE}" 
  puts "Using extra property:  -f#{JAR_FILE}"
end

command = command + " -t#{tenant}"
puts "Running: #{command} "
puts runShellCommand(command)

end

Then /^I get the client ID and shared secret for the app$/ do
  @driver.find_element(:xpath, "//tbody/tr[1]/td[1]").click
  @client_id = @driver.find_element(:xpath, '//tbody/tr[2]/td/dl/dd[1]').text
  @shared_secret = @driver.find_element(:xpath, '//tbody/tr[2]/td/dl/dd[2]').text  
  puts "client_id: " + client_id
  puts "Shared Secret ID: " + shared_secret  
  assert(client_id != '', "Expected non empty client Id, got #{client_id}")
  assert(shared_secret != '', "Expected non empty shared secret Id, got #{shared_secret}")
end

Then /^I authorize a session with the app$/ do |user|
  puts API_URL+"/api/oauth/authorize?response_type=code&client_id=#{@client_id}"
  @driver.get(API_URL+"/api/oauth/authorize?response_type=code&client_id=#{@client_id}")
end

Then /^ I capture the authorization and start a session$/ do
  auth_response = JSON.parse(@driver.get_body_text)['authorization_code']

  @driver.get(API_URL+"api/oauth/token?response_type=code&client_id=#{@client_id}&client_secret=#{@shared_secret}&code=#{auth_response}&redirect_uri=http://device")
  @auth_token = JSON.parse(@driver.get_body_text)['access_token']

end 

Then /^I validate the bulk extract file is correct$/ do
  
end
