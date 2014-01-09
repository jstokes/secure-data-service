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

###############################################################################
# BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE
###############################################################################

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^I have a connection to Mongo$/ do
  host = Property['ingestion_db']
  port = Property['ingestion_db_port']

  @conn = Mongo::Connection.new(host, port)

  # HACK TO WORK AROUND MONGOS DROP DATABASE BUG
  port2 = Property['rcingest02_port']
  port3 = Property['rcingest03_port']
  port4 = Property['rcapi01_port']
  port5 = Property['rcapi02_port']
  @conn2 = Mongo::Connection.new(host, port2) if port2
  @conn3 = Mongo::Connection.new(host, port3) if port3
  @conn4 = Mongo::Connection.new(host, port4) if port4
  @conn5 = Mongo::Connection.new(host, port5) if port5
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

When /^I drop a control file to purge tenant data as "([^\"]*)" with password "([^\"]*)" to "([^\"]*)"$/ do |user, pass, server|
  steps %Q{
    Given a landing zone
    And I drop the file "Purge.zip" into the landingzone
    And I check for the file "job*.log" every "30" seconds for "600" seconds
  }
end

When /^I get the database name$/ do
  if (@mode == "SANDBOX")
    @tenant_name = Property['sandbox_tenant']
  else
    @tenant_name = Property['tenant']
  end

  @tenant_db_name = convertTenantIdToDbName(@tenant_name)

end

###############################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
###############################################################################

Then /^my tenant database should be cleared$/ do
  step "I get the database name"
  tenant_db = @conn.db(@tenant_db_name)
  coll_names = tenant_db.collection_names
  coll_to_skip = ["system.indexes",
                  "system.js",
                  "system.profile",
                  "system.namespaces",
                  "system.users",
                  "tenant",
                  "securityEvent",
                  "realm",
                  "application",
                  "roles",
                  "deltas",
                  "customRole"]
  disable_NOTABLESCAN
  coll_names.each do |coll|
    assert(tenant_db["#{coll}"].count == 0, "#{coll} is not empty.") if !coll_to_skip.include?(coll)
  end
  enable_NOTABLESCAN
end

Then /^I will drop the whole database$/ do
  attempts = 0
  begin
    attempts += 1
    res = @conn.drop_database(@tenant_db_name)
    puts "Attempted to drop database #{attempts} times: #{res.to_a}"
    raise "Could not drop database" if (attempts > 15)
  end while @conn.database_names.include?(@tenant_db_name)

  # HACK TO WORK AROUND MONGOS DROP DATABASE BUG
  if @conn2
    res = @conn2.drop_database(@tenant_db_name)
    puts "Attempted to drop database from second mongos: #{res.to_a}"
  end
  if @conn3
    res = @conn3.drop_database(@tenant_db_name)
    puts "Attempted to drop database from third mongos: #{res.to_a}"
  end
  if @conn4
    res = @conn4.drop_database(@tenant_db_name)
    puts "Attempted to drop database from fourth mongos: #{res.to_a}"
  end
  if @conn5
    res = @conn5.drop_database(@tenant_db_name)
    puts "Attempted to drop database from fifth mongos: #{res.to_a}"
  end
  #/ HACK TO WORK AROUND MONGOS DROP DATABASE BUG

  tenant_dropped = false
  if (!@conn.database_names.include?(@tenant_db_name) || @conn.db(@tenant_db_name).collection_names.empty?)
    tenant_dropped = true
  end
  assert(tenant_dropped, "Tenant DB not dropped.")
end

Then /^I flush all mongos instances$/ do
  # if one connection is to a mongos, then they all should be.
  if @conn['admin'].command({:serverStatus => true})['process'] == 'mongos'
    step "I have a connection to Mongo"
    [@conn, @conn2, @conn3, @conn4, @conn5].each do |mon|
      if mon != nil
        result = mon['admin'].command({:flushRouterConfig => true})
        puts "Flushed #{mon.host_port} with result: #{result}"
      end
    end
    step "I close all open Mongo connections"
  else
    puts "Not running against mongos. Skipping."
  end
end

Then /^I will clean my tenants recordHash documents from ingestion_batch_job db$/ do
  host = Property['ingestion_batchjob_db']
  port = Property['ingestion_batchjob_db_port']
  batchJobconn = Mongo::Connection.new(host, port)
  batchJobDb = batchJobconn.db(Property['ingestion_batchjob_database_name'])
  batchJobDb['recordHash'].remove("t" => @tenant_name)
end

Then /^I clean my tenant's landing zone$/ do
  begin
    if RUN_ON_RC
      steps %Q{
        Given I am using local data store
        And I am using default landing zone
        And I am using the tenant "<SANDBOX_TENANT>"
        And I use the landingzone user name "<DEVELOPER_SB_EMAIL>" and password "<DEVELOPER_SB_EMAIL_PASS>" on landingzone server "<LANDINGZONE>" on port "<LANDINGZONE_PORT>"
   }
      puts "SFTP Connection info: #{@lz_url}, #{@lz_username}, #{@lz_password}, #{@lz_port_number}, #{@landing_zone_path}"
      Net::SFTP.start(@lz_url, @lz_username, {:password => @lz_password, :port => @lz_port_number}) do |sftp|
        clear_remote_lz(sftp)
      end
    end
  rescue SystemExit, Interrupt
    raise
  rescue Exception => e
    puts "Error cleaning out Landing Zone.  Continuing regardless."
    puts "#{e}"
    puts e.backtrace.join("\n")
  end
end

Then /^I clean up the (production|sandbox) tenant's bulk extract file entries in the database$/ do |environment|
  if environment.downcase == 'sandbox'
    tenant = Property['sandbox_tenant']
  else
    tenant = Property['tenant']
  end
  sli_db = @conn.db(Property['sli_database_name'])
  sli_db['bulkExtractFiles'].remove('body.tenantId' => tenant)
  assert(sli_db['application'].find('body.tenantId' => tenant).count == 0, "Bulk extract file entries for tenant '#{tenant}' have not been deleted.")
end

Then /^I will drop the tenant document from the collection$/ do
  sli_db = @conn.db(Property['sli_database_name'])
  sli_db['tenant'].remove("body.tenantId" => @tenant_name)
  assert(sli_db['tenant'].find("body.tenantId" => @tenant_name).count == 0, "Tenant document not dropped.")
end

Then /^I will delete the realm for this tenant from the collection$/ do
  sli_db = @conn.db(Property['sli_database_name'])
  sli_db['realm'].remove("body.uniqueIdentifier" => "RC-IL-Daybreak")
  assert(sli_db['realm'].find("body.uniqueIdentifier" => "RC-IL-Daybreak").count == 0, "Realm document not deleted.")
  if RUN_ON_RC
     sli_db['realm'].remove("body.uniqueIdentifier" => "RC-IL-Charter-School")
     assert(sli_db['realm'].find("body.uniqueIdentifier" => "RC-IL-Charter-School").count == 0, "Realm document not deleted.")
     sli_db['realm'].remove("body.uniqueIdentifier" => "RC-Artifact-IL-Daybreak")
     assert(sli_db['realm'].find("body.uniqueIdentifier" => "RC-Artifact-IL-Daybreak").count == 0, "Realm document not deleted.")
     sli_db['realm'].remove("body.uniqueIdentifier" => "RC-Post-Encrypt-IL-Daybreak")
     assert(sli_db['realm'].find("body.uniqueIdentifier" => "RC-Post-Encrypt-IL-Daybreak").count == 0, "Realm document not deleted.")
  end
end

Then /^I will delete the applications "([^\"]*)" from the collection$/ do |apps|
  app_names = apps.split(",")
  sli_db = @conn.db(Property['sli_database_name'])
  app_names.each do |name|
    sli_db['application'].remove("body.name" => name)
    assert(sli_db['application'].find("body.name" => name).count == 0, "The application '#{name}' is not deleted.")
  end
end


Then /^I close all open Mongo connections$/ do
  @conn.close if @conn != nil
  @conn2.close if @conn2 != nil
  @conn3.close if @conn3 != nil
  @conn4.close if @conn4 != nil
  @conn5.close if @conn5 != nil
end
