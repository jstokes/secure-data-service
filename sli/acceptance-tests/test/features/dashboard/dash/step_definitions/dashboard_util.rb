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
require_relative '../../../utils/sli_utils.rb'

Given /^the district "([^\"]*)" has dissallowed use of the dashboard$/ do |district|
  disable_NOTABLESCAN()
  dissallowDashboard(district, "Midgar")
  dissallowDashboard(district, "Hyrule")
  enable_NOTABLESCAN()
end

def dissallowDashboard(district, tenantName)
  conn = Mongo::Connection.new(PropLoader.getProps['DB_HOST'])
  db = conn[PropLoader.getProps['api_database_name']]
  appColl = db.collection("application")
  dashboardId = appColl.find_one({"body.name" => "SLC Dashboards"})["_id"]
  puts("The dashboard id is #{dashboardId}") if ENV['DEBUG']
  
  dbTenant = conn[convertTenantIdToDbName(tenantName)]
  appAuthColl = dbTenant.collection("applicationAuthorization")
  edOrgColl = dbTenant.collection("educationOrganization")

  edOrg = edOrgColl.find_one({"body.stateOrganizationId" => district})
  if edOrg != nil
    puts("Found edOrg: #{edOrg.inspect}") if ENV['DEBUG']
    edOrgId = edOrg["_id"]
    edOrgTenant = edOrg["metaData"]["tenantId"]
    existingAppAuth = appAuthColl.find_one({"body.authId" => edOrgId})
    if existingAppAuth != nil
      existingAppAuth["body"]["appIds"].delete(dashboardId)
      puts("About to update #{existingAppAuth.inspect}") if ENV['DEBUG']
      appAuthColl.update({"body.authId" => edOrgId}, existingAppAuth)
    else
      puts("District #{edOrgId} already denies all apps") if ENV['DEBUG']
    end
  else
    puts("District #{district} not found") if ENV['DEBUG']
  end
end
