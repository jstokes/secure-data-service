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

require_relative '../../ingestion/rc_test/step_definition/rc_integration_ingestion.rb'

Before do
  RUN_ON_RC = ENV['RUN_ON_RC'] ? true : false
end

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<(.+)>$/ do |template|
  id = template
  #TODO: do we need to add the other emails?
  id = PropLoader.getProps['email_imap_registration_user_email'] if template == "SEA ADMIN"
  #TODO: externalize this password to properties
  id = "test1234"                                                if template == "SEA ADMIN PASSWORD"
  # return the transformed value
  id
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

When /^I drop a control file to purge tenant data as "([^\"]*)" with password "([^\"]*)" to "([^\"]*)"$/ do |user, pass, server|
  #TODO
  if RUN_ON_RC
    steps %Q{
      Given I am using local data store
      Given I am using default landing zone
      Given I use the landingzone user name "#{user}" and password "#{pass}" on landingzone server "#{server}-lz.slidev.org" on port "443"
      And I drop the file "Purge.zip" into the landingzone
      Then a batch job log has been created
      And I should not see an error log file created
      }
  else
    steps %Q{
      Given I have a local configured landing zone for my tenant
      And I drop the file "Purge.zip" into the landingzone
      Then a batch job log has been created
      And I should not see an error log file created
    }
  end
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

Then /^my tenant database should be cleared$/ do
  #TODO
end
