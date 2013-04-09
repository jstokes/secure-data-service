require_relative '../../../ingestion/features/step_definitions/ingestion_steps.rb'
require_relative '../../../apiV1/bulkExtract/stepdefs/balrogs_steps.rb'
require_relative '../../../ingestion/features/step_definitions/clean_database.rb'
require_relative '../../../utils/sli_utils.rb'

SCHEDULER_SCRIPT = File.expand_path(PropLoader.getProps['bulk_extract_scheduler_script'])
TRIGGER_SCRIPT_DIRECTORY = File.expand_path(PropLoader.getProps['bulk_extract_script_directory'])
CRON_OUTPUT_DIRECTORY = PropLoader.getProps['bulk_extract_cron_output_directory']
TRIGGER_SCRIPT = File.expand_path(PropLoader.getProps['bulk_extract_script'])
OUTPUT_DIRECTORY = PropLoader.getProps['bulk_extract_output_directory']
PROPERTIES_FILE = PropLoader.getProps['bulk_extract_properties_file']
KEYSTORE_FILE = PropLoader.getProps['bulk_extract_keystore_file']
JAR_FILE = PropLoader.getProps['bulk_extract_jar_loc']
DATABASE_NAME = PropLoader.getProps['sli_database_name']
DATABASE_HOST = PropLoader.getProps['bulk_extract_db']
DATABASE_PORT = PropLoader.getProps['bulk_extract_port']
ENCRYPTED_ENTITIES = ['student', 'parent']
COMBINED_ENTITIES = ['assessment', 'studentAssessment']

ENCRYPTED_FIELDS = ['loginId', 'studentIdentificationCode','otherName','sex','address','electronicMail','name','telephone','birthData']
MUTLI_ENTITY_COLLS = ['staff', 'educationOrganization']

require 'zip/zip'
require 'archive/tar/minitar'
require 'zlib'
require 'open3'
require 'openssl'
include Archive::Tar
require_relative '../../../ingestion/features/step_definitions/ingestion_steps.rb'
require_relative '../../../apiV1/bulkExtract/stepdefs/balrogs_steps.rb' #This is for the decryption step

############################################################
# Scheduler
############################################################
Given /^the current crontab is empty$/ do
    command = "crontab -l"
    result = runShellCommand(command)
    puts "Running: #{command} #{result}"
    command = "crontab -r"
    result = runShellCommand(command)
    puts "Running: #{command} #{result}"
    assert(result.length==0, "current crontab is not empty but #{result}")
end

Given /^the local bulk extract script path and the scheduling config path$/ do
    assert(Dir.exists?(TRIGGER_SCRIPT_DIRECTORY), "Bulk Extract script directory #{TRIGGER_SCRIPT_DIRECTORY} does not exist")
    @current_dir = Dir.pwd
    is_jenkins = @current_dir.include?"jenkins"
    puts "pwd: #{@current_dir}"
    @trigger_script_path = TRIGGER_SCRIPT_DIRECTORY
    @scheduling_config_path = File.dirname(__FILE__) + '/../../test_data/config/'

    if !is_jenkins
        @scheduling_config_path = File.dirname(__FILE__) + '/../../test_data/local/'
    end

    assert(Dir.exists?(@scheduling_config_path), "Bulk Extract scheduling config directory #{@scheduling_config_path} does not exist")

    puts "bulk extract script path: #{@trigger_script_path}"
    puts "bulk extract scheduling config path: #{@scheduling_config_path}"
end

And /^I clean up the cron extraction zone$/ do
    Dir.chdir
    puts "pwd: #{Dir.pwd}"
    if (Dir.exists?(CRON_OUTPUT_DIRECTORY))
        FileUtils.rm_rf CRON_OUTPUT_DIRECTORY
    end
    assert(!Dir.exists?(CRON_OUTPUT_DIRECTORY), "cron output directory #{CRON_OUTPUT_DIRECTORY} does exist")
    puts "CRON_OUTPUT_DIRECTORY: #{CRON_OUTPUT_DIRECTORY}"
    Dir.chdir(@current_dir)
end

Then /^I run the bulk extract scheduler script$/ do
    command  = "echo 'y' | #{SCHEDULER_SCRIPT} #{@trigger_script_path} #{@scheduling_config_path}"
    result = runShellCommand(command)
    puts "Running: #{command} #{result}"
    raise "Result of bulk extract scheduler script should include Installed new crontab but was #{result}" if !result.include?"Installed new crontab"
    command = "crontab -l"
    result = runShellCommand(command)
    Dir.chdir
    puts "pwd: #{Dir.pwd}"
end

When /^I am willing to wait upto (\d+) seconds for the bulk extract scheduler cron job to start and complete$/ do |limit|
    @maxTimeout = limit.to_i
    puts "Waited timeout for #{limit.to_i} seconds"
    intervalTime = 1
    @maxTimeout ? @maxTimeout : @maxTimeout = 900
    iters = (1.0*@maxTimeout/intervalTime).ceil
    iters.times do |i|
       if Dir.exists?(CRON_OUTPUT_DIRECTORY)
          puts "Bulk extract scheduler cron job took approx. #{(i+1)*intervalTime} seconds to start and complete"
          found = true
          break
       else
          sleep(intervalTime)
       end
    end

    assert(Dir.exists?(CRON_OUTPUT_DIRECTORY), "Timeout: cron job output directory #{CRON_OUTPUT_DIRECTORY} does not exist")

    outdir = Dir.new(CRON_OUTPUT_DIRECTORY)
    outdir.each do |filename|
       puts "Bulk extracted file by cron job: #{filename}" if filename!="." && filename!=".."
    end
    Dir.chdir(@current_dir)
end

And /^I clear crontab$/ do
    command = "crontab -r"
    result = runShellCommand(command)
    puts "Running: #{command} #{result}"
    assert(result.length==0, "current crontab is not empty but #{result}")
end


############################################################
# Given
############################################################
Given /^I trigger a bulk extract$/ do
command  = "#{TRIGGER_SCRIPT}"
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
puts "Running: #{command} "
puts runShellCommand(command)

end

Given /^the extraction zone is empty$/ do
    if (Dir.exists?(OUTPUT_DIRECTORY))
      puts OUTPUT_DIRECTORY
      FileUtils.rm_rf("#{OUTPUT_DIRECTORY}/.", secure: true)
    end
end

Given /^I have delta bulk extract files generated for today$/ do
  bulk_delta_file_entry = {
    _id: "Midgar_delta",
    body: {
      tenantId: "Midgar",
      isDelta: "true",
      path: "#{File.dirname(__FILE__)}/../../test_data/deltas/Midgar_delta_1.tar",
      date: Time.now
    },
    metaData: {
      updated: Time.now
    },
    type: "bulkExtractEntity"
  }
  @pre_generated = "#{File.dirname(__FILE__)}/../../test_data/deltas/Midgar_delta_1.tar"
  @conn ||= Mongo::Connection.new(DATABASE_HOST, DATABASE_PORT)
  @sliDb ||= @conn.db(DATABASE_NAME)
  @coll ||= @sliDb.collection("bulkExtractFiles")
  @coll.save(bulk_delta_file_entry)
end

############################################################
# When
############################################################

When /^I retrieve the path to the extract file for the tenant "(.*?)"$/ do |tenant|
  @conn = Mongo::Connection.new(DATABASE_HOST, DATABASE_PORT)
  @sliDb = @conn.db(DATABASE_NAME)
  @coll = @sliDb.collection("bulkExtractFiles")

  match =  @coll.find_one({"_id" => tenant, "body.tenantId" => tenant})

  assert(match !=nil, "Database was not updated with bulk extract file location")

  @filePath = match['body']['path']
  @unpackDir = File.dirname(@filePath) + '/unpack'
  @tenant = tenant

end

When /^I verify that an extract tar file was created for the tenant "(.*?)"$/ do |tenant|

	puts "Extract FilePath: #{@filePath}"

	assert(File.exists?(@filePath), "Extract file was not created or Output Directory was not found")
end

When /^I verify this tar file is the same as the pre-generated delta file$/ do
   puts "pre-generated file at: #{@pre_generated}"
   puts "served file from API at: #{@filePath}"
   assert(FileUtils.compare_file(@filePath, @pre_generated), "Delta file served from API is different from pre-generated file") 
end

When /^there is a metadata file in the extract$/ do
  Minitar.unpack(@filePath, @unpackDir)
	assert(File.exists?(@unpackDir + "/metadata.txt"), "Cannot find metadata file in extract")
end

When /^the extract contains a file for each of the following entities:$/ do |table|
  Minitar.unpack(@filePath, @unpackDir)

	table.hashes.map do |entity|
  exists = File.exists?(@unpackDir + "/" +entity['entityType'] + ".json.gz")
  assert(exists, "Cannot find #{entity['entityType']}.json file in extracts")
	end

  fileList = Dir.entries(@unpackDir)
	assert((fileList.size-3)==table.hashes.size, "Expected " + table.hashes.size.to_s + " extract files, Actual:" + (fileList.size-3).to_s)
end

When /^a "(.*?)" extract file exists$/ do |collection|
  exists = File.exists?(@unpackDir + "/" + collection + ".json.gz")
	assert(exists, "Cannot find #{collection}.json file in extracts")

end

When /^a the correct number of "(.*?)" was extracted from the database$/ do |collection|
  disable_NOTABLESCAN()
	@tenantDb = @conn.db(convertTenantIdToDbName(@tenant))

	case collection
	when "school"
	  count = @tenantDb.collection("educationOrganization").find({"type" => "school" } ).count()
	when "teacher"
	  count = @tenantDb.collection("staff").find({"type" => "teacher" } ).count()
	else
    parentCollection = subDocParent(collection)
	  if(parentCollection == nil)
      count = @tenantDb.collection(collection).count()
    else 
      count = @tenantDb.collection(parentCollection).aggregate([ {"$match" => {"#{collection}" => {"$exists" => true}}}, {"$unwind" => "$#{collection}"}]).size
    end
	end

	Zlib::GzipReader.open(@unpackDir + "/" + collection + ".json.gz") { |extractFile|
    records = JSON.parse(extractFile.read)
    puts "\nCounts Expected: " + count.to_s + " Actual: " + records.size.to_s + "\n"
    assert(records.size == count,"Counts off Expected: " + count.to_s + " Actual: " + records.size.to_s)
  }
  enable_NOTABLESCAN()
end

When /^a "(.*?)" was extracted with all the correct fields$/ do |collection|
  disable_NOTABLESCAN()
	Zlib::GzipReader.open(@unpackDir +"/" + collection + ".json.gz") { |extractFile|
	records = JSON.parse(extractFile.read)
	uniqueRecords = Hash.new
	records.each do |jsonRecord|
		assert(uniqueRecords[jsonRecord['id']] == nil, "Record was extracted twice \nJSONRecord:\n" + jsonRecord.to_s)
		uniqueRecords[jsonRecord['id']] = 1

		mongoRecord = getMongoRecordFromJson(jsonRecord)
		assert(mongoRecord != nil, "MongoRecord not found: " + mongoRecord.to_s)

		compareRecords(mongoRecord, jsonRecord)
	end
  enable_NOTABLESCAN()
}
end

When /^I log into "(.*?)" with a token of "(.*?)", a "(.*?)" for "(.*?)" in tenant "(.*?)", that lasts for "(.*?)" seconds/ do |client_appName, user, role, realm, tenant, expiration_in_seconds|

  disable_NOTABLESCAN()
  conn = Mongo::Connection.new(DATABASE_HOST, DATABASE_PORT)
  db = conn[DATABASE_NAME]
  appColl = db.collection("application")
  client_id = appColl.find_one({"body.name" => client_appName})["body"]["client_id"]
  conn.close
  enable_NOTABLESCAN()

  script_loc = File.dirname(__FILE__) + "/../../../../../../opstools/token-generator/generator.rb"
  out, status = Open3.capture2("ruby #{script_loc} -e #{expiration_in_seconds} -c #{client_id} -u #{user} -r \"#{role}\" -t \"#{tenant}\" -R \"#{realm}\"")
  match = /token is (.*)/.match(out)
  @sessionId = match[1]
  puts("The generated token is #{@sessionId}") if $SLI_DEBUG
end

############################################################
# Then
############################################################

Then  /^a "(.*?)" was extracted in the same format as the api$/ do |collection|
  Zlib::GzipReader.open(@unpackDir +"/" + collection + ".json.gz") { |extracts|
  collFile = JSON.parse(extracts.read)
  assert(collFile!=nil, "Cannot find #{collection}.json file in extracts")
  compareToApi(collection, collFile)
}
end

############################################################
# Functions
############################################################

def getMongoRecordFromJson(jsonRecord)
	@tenantDb = @conn.db(convertTenantIdToDbName(@tenant)) 
	case jsonRecord['entityType']
	when "stateEducationAgency", "localEducationAgency", "school"
	  collection = "educationOrganization"
	when "teacher"
	  collection = "staff"
	else
    collection = jsonRecord['entityType']
	end
	parent = subDocParent(collection)
	if (parent == nil)
        return @tenantDb.collection(collection).find_one("_id" => jsonRecord['id'])
    else #Collection is a subdoc, gets record from parent
    	superdoc = @tenantDb.collection(parent).find_one("#{collection}._id" => jsonRecord['id'])
    	superdoc[collection].each do |subdoc|
    		if (subdoc["_id"] == jsonRecord['id'])
    			return subdoc
    		end
    	end
    end
end

def	compareRecords(mongoRecord, jsonRecord)
	if !MUTLI_ENTITY_COLLS.include?(jsonRecord['entityType'])
	  assert(mongoRecord['type']==jsonRecord['entityType'], "Record types do not match for records \nMONGORecord:\n" + mongoRecord.to_s + "\nJSONRecord:\n" + jsonRecord.to_s)
	end
	jsonRecord.delete('id')
	jsonRecord.delete('entityType')

    if (ENCRYPTED_ENTITIES.include?(mongoRecord['type'])) 
        compareEncryptedRecords(mongoRecord, jsonRecord)
    elsif(mongoRecord['type'] == 'attendance')
      compareAttendances(mongoRecord, jsonRecord)
    elsif (!COMBINED_ENTITIES.include?(mongoRecord['type']))
	    assert(mongoRecord['body'].eql?(jsonRecord), "Record bodies do not match for records \nMONGORecord:\n" + mongoRecord['body'].to_s + "\nJSONRecord:\n" + jsonRecord.to_s )
    end
end

def compareAttendances(mongoRecord, jsonRecord)
      assert(mongoRecord['body']['attendanceEvent']==jsonRecord['schoolYearAttendance'][0]['attendanceEvent'], "Record types do not match for records \nMONGORecord:\n" + mongoRecord.to_s + "\nJSONRecord:\n" + jsonRecord.to_s)
      assert(mongoRecord['body']['schoolYear']==jsonRecord['schoolYearAttendance'][0]['schoolYear'], "Record types do not match for records \nMONGORecord:\n" + mongoRecord.to_s + "\nJSONRecord:\n" + jsonRecord.to_s)
      jsonRecord.delete('schoolYearAttendance')
      mongoRecord['body'].delete('schoolYear')
      mongoRecord['body'].delete('attendanceEvent')
      assert(mongoRecord['body'].eql?(jsonRecord), "Record bodies do not match for records \nMONGORecord:\n" + mongoRecord['body'].to_s + "\nJSONRecord:\n" + jsonRecord.to_s )
end

def compareEncryptedRecords(mongoRecord, jsonRecord)
    assert(get_nested_keys(mongoRecord['body']).eql?(get_nested_keys(jsonRecord)), 
      "Record fields do not match for records \nMONGORecord:\n" + get_nested_keys(mongoRecord['body']).to_s + "\nJSONRecord:\n" + get_nested_keys(jsonRecord).to_s)

    assert(removeEncryptedFields(mongoRecord['body']).eql?(removeEncryptedFields(jsonRecord)), 
      "Record bodies do not match for records \nMONGORecord:\n" + mongoRecord['body'].to_s + "\nJSONRecord:\n" + jsonRecord.to_s )
end

def removeEncryptedFields(record)
    record.delete_if{ |key,value| ENCRYPTED_FIELDS.include?(key)}
end


def get_nested_keys(hash, keys=Array.new)
  hash.each do |k, v|
   keys << k
   case v
    when Array
      v.each {|vv| (Hash.try_convert(vv)!=nil)?get_nested_keys(vv, keys): keys.sort }
    when Hash
      get_nested_keys(v,keys)
    end
   end
   keys.sort
end

def entityToUri(entity)
  
  uri = String.new(entity)

  case entity
  when "staff", "competencyLevelDescriptor"
  when "gradebookEntry", "studentGradebookEntry", "studentCompetency"
    uri[-1] = "ies" 
  when "staffEducationOrganizationAssociation"
    uri = "staffEducationOrgAssignmentAssociations"
  else
    uri += "s"
  end

  uri
end

def compareToApi(collection, collFile)
  found = false
    
  collFile.each do |extractRecord|
    
    id = extractRecord["id"]
      
    #Make API call and get JSON for the collection
    @format = "application/vnd.slc+json"
    uri = entityToUri(collection)
    restHttpGet("/v1/#{uri}/#{id}")
    assert(@res != nil, "Response from rest-client GET is nil")
    if @res.code == 200
      apiRecord = JSON.parse(@res.body)
      assert(apiRecord != nil, "Result of JSON parsing is nil")    
      apiRecord.delete("links")
      assert(extractRecord.eql?(apiRecord), "Extract record doesn't match API record.\nExtractRecord:\n" +extractRecord.to_s + "\nAPIRecord:\n" + apiRecord.to_s)
      found = true
    end
  end
    
  assert(found, "No API records for #{collection} were fetched successfully.")
end

