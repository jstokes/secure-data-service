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

require_relative '../../utils/sli_utils.rb'
require_relative '../../ingestion/features/step_definitions/ingestion_steps.rb'

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

When /^the developer selects to preload "(.*?)"$/ do |sample_data_set|
  if sample_data_set.downcase.include? "small"
    sample_data_set="small"
  else
    sample_data_set="medium"
  end
  @explicitWait.until{@driver.find_element(:id,"ed_org_from_sample").click}
  select = Selenium::WebDriver::Support::Select.new(@explicitWait.until{@driver.find_element(:id,"sample_data_select")})
  select.select_by(:value, sample_data_set)
  @explicitWait.until{@driver.find_element(:id,"provisionButton").click}
end

Then /^there are "(.*?)" edOrgs for the "(.*?)" application in the applicationAuthorization collection$/ do |expected_count, application|
   db = @conn.db("sli")
   coll = db.collection("application")
   record = coll.find_one("body.name" => application)
   appId = record["_id"]
   db = @conn[convertTenantIdToDbName(PropLoader.getProps['sandbox_tenant'])]
   coll = db.collection("applicationAuthorization")
   record = coll.find_one("body.applicationId" => appId.to_s)
   body = record["body"]
   edorgsArray = body["edorgs"]
   edorgsArrayCount = edorgsArray.count
   assert(edorgsArrayCount == expected_count.to_i, "Education organization count mismatch in applicationAuthorization collection. Expected #{expected_count}, actual #{edorgsArrayCount}")
end

When /^I (enable|disable) the educationalOrganization "([^"]*?)" in sandbox/ do |action,edOrgName|
  # Note: there should be no need to disable table scan since there is an index on educationOrganization.nameOfInstitution
  db = @conn[convertTenantIdToDbName(PropLoader.getProps['sandbox_tenant'])]
  coll = db.collection("educationOrganization")
  record = coll.find_one("body.nameOfInstitution" => edOrgName.to_s)
  edOrgId = record["_id"]
  elt = @driver.find_element(:id, edOrgId)
  assert(elt, "Educational organization element for '" + edOrgName + "' (" + edOrgId + ") not found")
  assert(action == "enable" && !elt.selected? || action == "disable" && elt.selected?, "Cannot " + action + " educationalOrganization element with id '" + edOrgId + "' whose checked status is " + elt.selected?.to_s())
  elt.click()
end
