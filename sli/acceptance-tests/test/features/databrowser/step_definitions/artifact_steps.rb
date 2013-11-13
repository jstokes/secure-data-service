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

Then /^I should see a login failed message$/ do
  assertWithWait('Could not find login failed error message') {@driver.find_element(:css, '.form-error').text == 'Login has failed. Double-check your username and password.'}
end

Then /^I get an error message that says "([^"]*)"$/ do | message |
  assertWithWait('Could not find error message') {@driver.find_element(:tag_name, 'h3').text == message}
end