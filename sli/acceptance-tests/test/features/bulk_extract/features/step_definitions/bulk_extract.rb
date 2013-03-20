require_relative '../../../ingestion/features/step_definitions/ingestion_steps.rb'
require_relative '../../../ingestion/features/step_definitions/clean_database.rb'

TRIGGER_SCRIPT = File.expand_path(PropLoader.getProps['bulk_extract_script'])
OUTPUT_DIRECTORY = PropLoader.getProps['bulk_extract_output_directory']
DATABASE_NAME = PropLoader.getProps['sli_database_name']
DATABASE_HOST = PropLoader.getProps['bulk_extract_db']
DATABASE_PORT = PropLoader.getProps['bulk_extract_port']
ENCRYPTED_ENTITIES = ['student', 'parent']
ENCRYPTED_FIELDS = ['loginId', 'studentIdentificationCode','otherName','sex','address','electronicMail','name','telephone','birthData']

require 'zip/zip'

############################################################
# Given
############################################################
Given /^I trigger a bulk extract$/ do

puts "Running: sh #{TRIGGER_SCRIPT}"
puts runShellCommand("sh #{TRIGGER_SCRIPT}")

end

Given /^the extraction zone is empty$/ do
    assert(Dir.exists?(OUTPUT_DIRECTORY), "Bulk Extract output directory #{OUTPUT_DIRECTORY} does not exist")
    puts OUTPUT_DIRECTORY
    FileUtils.rm_rf("#{OUTPUT_DIRECTORY}/.", secure: true)
end

############################################################
# When
############################################################

When /^I retrieve the path to the extract file for the tenant "(.*?)"$/ do |tenant|
  @conn = Mongo::Connection.new(DATABASE_HOST, DATABASE_PORT)
  @sliDb = @conn.db(DATABASE_NAME)
  @coll = @sliDb.collection("bulkExtractFiles")

  match =  @coll.find_one("body.tenantId" => tenant)

  assert(match !=nil, "Database was not updated with bulk extract file location")

  @filePath = match['body']['path']
  @tenant = tenant

end

When /^I verify that an extract zip file was created for the tenant "(.*?)"$/ do |tenant|

	puts "Extract FilePath: #{@filePath}"

	assert(File.exists?(@filePath), "Extract file was not created or Output Directory was not found")
end

When /^there is a metadata file in the extract$/ do
    extractFile = Zip::ZipFile.open(@filePath, Zip::ZipFile::CREATE)
    metadataFile = extractFile.find_entry("metadata.txt")
	assert(metadataFile!=nil, "Cannot find metadata file in extract")
	extractFile.close()
end

When /^the extract contains a file for each of the following entities:$/ do |table|
    extractFile = Zip::ZipFile.open(@filePath, Zip::ZipFile::CREATE)

	table.hashes.map do |entity|
	 collFile = extractFile.find_entry(entity['entityType'] + ".json")
     puts entity
	 assert(collFile!=nil, "Cannot find #{entity['entityType']}.json file in extracts")
	end

	assert((extractFile.size-1)==table.hashes.size, "Expected " + table.hashes.size.to_s + " extract files, Actual:" + (extractFile.size-1).to_s)
    extractFile.close()
end

When /^a "(.*?)" extract file exists$/ do |collection|
    extractFile = Zip::ZipFile.open(@filePath, Zip::ZipFile::CREATE)
	collFile = extractFile.find_entry(collection + ".json")
	assert(collFile!=nil, "Cannot find #{collection}.json file in extracts")
    extractFile.close()
end

When /^a the correct number of "(.*?)" was extracted from the database$/ do |collection|
	@tenantDb = @conn.db(convertTenantIdToDbName(@tenant)) 
	count = @tenantDb.collection(collection).count()

	extractFile = Zip::ZipFile.open(@filePath, Zip::ZipFile::CREATE)
	records = JSON.parse(extractFile.read(collection + ".json"))

	puts "Counts Expected: " + count.to_s + " Actual: " + records.size.to_s
	assert(records.size == count,"Counts off Expected: " + count.to_s + " Actual: " + records.size.to_s)
end

When /^a "(.*?)" was extracted with all the correct fields$/ do |collection|
	extractFile = Zip::ZipFile.open(@filePath, Zip::ZipFile::CREATE)
	records = JSON.parse(extractFile.read(collection + ".json"))
	uniqueRecords = Hash.new
	records.each do |jsonRecord|
		assert(uniqueRecords[jsonRecord['id']] == nil, "Record was extracted twice \nJSONRecord:\n" + jsonRecord.to_s)
		uniqueRecords[jsonRecord['id']] = 1

		mongoRecord = getMongoRecordFromJson(jsonRecord)
		assert(mongoRecord != nil, "MongoRecord not found: " + mongoRecord.to_s)

		compareRecords(mongoRecord, jsonRecord)
	end
end

############################################################
# Functions
############################################################

def getMongoRecordFromJson(jsonRecord)
	@tenantDb = @conn.db(convertTenantIdToDbName(@tenant)) 
	collection = jsonRecord['entityType']
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
	assert(mongoRecord['_id']==jsonRecord['id'], "Record Ids do not match for records \nMONGORecord:\n" + mongoRecord.to_s + "\nJSONRecord:\n" + jsonRecord.to_s)
	assert(mongoRecord['type']==jsonRecord['entityType'], "Record types do not match for records \nMONGORecord:\n" + mongoRecord.to_s + "\nJSONRecord:\n" + jsonRecord.to_s)
	jsonRecord.delete('id')
	jsonRecord.delete('entityType')

    if (ENCRYPTED_ENTITIES.include?(mongoRecord['type'])) 
        compareEncryptedRecords(mongoRecord, jsonRecord)
    else
	    puts "\nMONGORecord:\n" + mongoRecord['body'].to_s + "\nJSONRecord:\n" + jsonRecord.to_s
	    assert(mongoRecord['body'].eql?(jsonRecord), "Record bodies do not match for records \nMONGORecord:\n" + mongoRecord['body'].to_s + "\nJSONRecord:\n" + jsonRecord.to_s )
    end
end

def compareEncryptedRecords(mongoRecord, jsonRecord)
    assert(get_nested_keys(mongoRecord['body']).eql?(get_nested_keys(jsonRecord)), "Record fields do not match for records \nMONGORecord:\n" + get_nested_keys(mongoRecord['body']).to_s + "\nJSONRecord:\n" + get_nested_keys(mongoRecord['body']).to_s)

    removeEncryptedFields(mongoRecord['body'])
    removeEncryptedFields(jsonRecord)

    assert(mongoRecord['body'].eql?(jsonRecord), "Record bodies do not match for records \nMONGORecord:\n" + mongoRecord['body'].to_s + "\nJSONRecord:\n" + jsonRecord.to_s )
end

def removeEncryptedFields(record)
    record.reject!{ |key,value| !ENCRYPTED_FIELDS.include?(key)}
end


def get_nested_keys(hash, keys=Array.new)
  hash.each do |k, v|
   keys << k
   case v
    when Array
      v.each {|vv| (Hash.try_convert(vv)!=nil)?get_nested_keys(vv, keys): keys }
    when Hash
      get_nested_keys(v,keys)
    end
   end
   keys
end


