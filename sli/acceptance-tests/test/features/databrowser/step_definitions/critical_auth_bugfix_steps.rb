require_relative '../../utils/sli_utils.rb'

Given /^Another user has authenticated to SLI previously$/ do
  # This should be done as part of the Rake task config
end

When /^I access the API resource "([^"]*)" with no authorization headers present$/ do |arg1|
  url = PropLoader.getProps['api_server_url']+"/api/rest"+arg1
  @res = RestClient.get(url){|response, request, result| response }
end
