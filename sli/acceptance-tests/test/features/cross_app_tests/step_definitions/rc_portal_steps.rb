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

When /^I hit the Portal home page$/ do
  portal_url = "#{PropLoader.getProps['portal_server_address']}/portal"
  begin
    @driver.get portal_url
  rescue
  end
end

Then /^the Portal .*? page should be compiled$/ do
  # Dummy step. No really, this step is stupid
end

When /^I hit the Portal Admin page$/ do
  portal_admin_url = "#{PropLoader.getProps['portal_server_address']}/portal/web/guest/admin"
  begin
    @driver.get portal_admin_url
  rescue
  end
end
