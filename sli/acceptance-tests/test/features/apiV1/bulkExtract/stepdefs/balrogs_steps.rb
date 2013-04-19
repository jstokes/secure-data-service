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
require 'open3'
require_relative '../../../utils/sli_utils.rb'

$FAKE_FILE_TEXT = "This is a fake tar file"

Given /^I am a valid 'service' user with an authorized long\-lived token "(.*?)"$/ do |token|
  @sessionId=token
end

Given /^in my list of rights I have BULK_EXTRACT$/ do
  #  Explanatory step
end

Given /^I set up a fake tar file on the file system and in Mongo$/ do
  File.open("fake.tar", 'w') {|f| f.write($FAKE_FILE_TEXT)}
  puts("Tar file is in #{Dir.pwd}/fake.tar")
  @path = Dir.pwd + "/fake.tar"

  encrypt(@path, @path)

  time = Time.new
  
  db ||= Mongo::Connection.new(PropLoader.getProps['DB_HOST']).db('sli')
  appId = getAppId()
  src_coll = db["bulkExtractFiles"]
  @fake_tar_id = SecureRandom.uuid
  src_coll.insert({"_id" => @fake_tar_id, "body" => {"applicationId" => appId, "isDelta" => "false", "tenantId" => "Midgar", "date" => time.strftime("%a %b %d %H:%S:%M %Z %Y"), "path" => Dir.pwd + "/fake.tar"}})
end

Given /^I know the file length of the extract file$/ do
  @file_size = File.size(@path)
end

When /^I make API call to retrieve sampled bulk extract file$/ do
  restHttpGet("/bulk/extract")
end

When /^I make API call to retrieve sampled bulk extract file headers$/ do
  restHttpHead("/bulk/extract")
end

When /^I make API call to retrieve sampled bulk extract file headers with version "(.*?)"$/ do |version|
  restHttpHead("/" + version + "/bulk/extract")
end

When /^I make API call to bulk extract file headers with version "(.*?)"$/ do |version|
  restHttpHead("/" + version + "/bulk/extract/tenant")
end

When /^I make bulk extract API head call$/ do
  restHttpHead("/bulk/extract/tenant")
end

When /^I make bulk extract API call$/ do
  restHttpGet("/bulk/extract/tenant")
end

When /^I make a custom bulk extract API call$/ do
  restHttpCustomHeadersGet("/bulk/extract/tenant", @customHeaders)
end

When /^I make a concurrent ranged bulk extract API call and store the results$/ do
  t1=Thread.new{apiCall1()}
  t2=Thread.new{apiCall2()}
  t1.join
  t2.join

  @received_file = Dir.pwd + "/Final.tar"
  File.open(@received_file, "wb") do |outf|
    outf << @res2.body
    outf << @res1.body
  end
end

def apiCall1()
  @customHeaders = makeCustomHeader("101-543")
  @res1 = restHttpCustomHeadersGet("/bulk/extract/tenant", @customHeaders)
end

def apiCall2()
  @customHeaders = makeCustomHeader("0-100")
  @res2 = restHttpCustomHeadersGet("/bulk/extract/tenant", @customHeaders)
end

When /^I make API call to retrieve today's delta file$/ do
  today = Time.now
  restHttpGet("/bulk/deltas/#{today.strftime("%Y%m%d")}")
end

When /^I make API call to retrieve tomorrow's non existing delta files$/ do
  tomorrow = Time.now+24*3600
  restHttpGet("/bulk/deltas/#{tomorrow.strftime("%Y%m%d")}")
end

When /^I prepare the custom headers for byte range from "(.*?)" to "(.*?)"$/ do |from, to|
  if  (to == "end")
    to = ""
  end
  range = from + "-" + to
  @customHeaders = makeCustomHeader(range)
end

When /^I prepare the custom headers for multiple byte ranges "(.*?)"$/ do |ranges|
  @customHeaders = makeCustomHeader(ranges)
end

When /^I prepare the custom headers for the first "(.*?)" bytes$/ do |number_of_bytes|
  to = (number_of_bytes.to_i) -1
  @customHeaders = makeCustomHeader("0-" + to.to_s)
end

When /^I prepare the custom headers for the last "(.*?)" bytes$/ do |number_of_bytes|
  from = (@content_length.to_i - number_of_bytes.to_i)
  range = from.to_s + "-#{@content_length}"
  @customHeaders = makeCustomHeader(range)
end

When /^I prepare the custom headers with incorrect etag$/ do
  @customHeaders = makeCustomHeader("0-10", "xyz")
end

When /^the If-Match header field is set to "(.*?)"$/ do |value|
  if value == "FILENAME"
    @customHeaders = {:if_match => "\"#{@etag}\""}
  else
    @customHeaders = {:if_match => "\"#{value}\""}
  end

 end

 When /^the If-Unmodified-Since header field is set to "(.*?)"$/ do |value|
  date = Date.parse(@last_modified)
  if value == "BEFORE"
    @customHeaders = {:if_unmodified_since => "#{date.prev_day.httpdate})"}
  elsif value == "AFTER"
    @customHeaders = {:if_unmodified_since => "#{date.next_day.httpdate}"}
  else 
    assert(false)
  end

 end

When /^I save the extracted file$/ do
  @filePath = "extract/extract.tar"
  @unpackDir = File.dirname(@filePath) + '/unpack'
  if (!File.exists?("extract"))
      FileUtils.mkdir("extract")
  end
  if (File.exists?(@filePath)) 
      FileUtils.rm(@filePath)
      puts "Removed existing #{@filePath}"
  end
  if (File.exists?(@unpackDir))
      FileUtils.rm_r(@unpackDir)
      puts "Removed existing #{@unpackDir}"
  end

  File.open(@filePath, 'w') {|f| f.write(@res.body) }
end

When /^I decrypt and save the extracted file$/ do
  @filePath = "extract/extract.tar"
  @unpackDir = File.dirname(@filePath) + '/unpack'
  if (!File.exists?("extract"))
      FileUtils.mkdir("extract")
  end
  if (File.exists?(@filePath)) 
      FileUtils.rm(@filePath)
      puts "Removed existing #{@filePath}"
  end
  if (File.exists?(@unpackDir))
      FileUtils.rm_r(@unpackDir)
      puts "Removed existing #{@unpackDir}"
  end

  step "the response is decrypted"
  File.open(@filePath, 'w') {|f| f.write(@plain) }
end


When /^the return code is 404 I ensure there is no bulkExtractFiles entry for Midgar$/ do
  @db ||= Mongo::Connection.new(PropLoader.getProps['DB_HOST']).db('sli')
  @coll = "bulkExtractFiles";
  @src_coll = @db[@coll]

  assert(@res.code == 404,"The return code is #{@res.code}. Expected: 404")
  puts "@res.headers: #{@res.headers}"
  puts "@res.code: #{@res.code}"

  if @src_coll.count > 0
    ref_doc = @src_coll.find({"_id" => "Midgar"}).to_a
    assert(ref_doc.count == 0, "Return code was: "+@res.code.to_s+" but find #{@coll} document with _id #{"Midgar"}")
  end
end

When /^the return code is 503 I ensure there is a bulkExtractFiles entry for Midgar$/ do
  assert(@res.code == 503,"The return code is #{@res.code}. Expected: 503")
  puts "@res.headers: #{@res.headers}"
  puts "@res.code: #{@res.code}"

  if @src_coll.count > 0
    ref_doc = @src_coll.find({"_id" => "Midgar"}).to_a
    assert(ref_doc.count > 0, "Return code was: "+@res.code.to_s+" but find no #{@coll} document with _id #{"Midgar"}")
  end
end

When /^the return code is 200 I get expected tar downloaded$/ do
	puts "@res.headers: #{@res.headers}"
	puts "@res.code: #{@res.code}"
  assert(@res.code == 200,"The return code is #{@res.code}. Expected: 200")
	puts "@res.headers: #{@res.headers}"
	puts "@res.code: #{@res.code}"
	
	EXPECTED_CONTENT_TYPE = 'application/x-tar'
	@content_disposition = @res.headers[:content_disposition]
	@zip_file_name = @content_disposition.split('=')[-1].strip() if @content_disposition.include? '='
	@last_modified = @res.headers[:last_modified]
	
	puts "content-disposition: #{@content_disposition}"
	puts "download file name: #{@zip_file_name}"
	puts "last-modified: #{@last_modified}"
	
	assert(@res.headers[:content_type]==EXPECTED_CONTENT_TYPE, "Content Type must be #{EXPECTED_CONTENT_TYPE} was #{@res.headers[:content_type]}")
end

When /^the return code is 200$/ do
  assert(@res.code == 200,"The return code is #{@res.code}. Expected: 200")
	puts "@res.headers: #{@res.headers}"
	puts "@res.code: #{@res.code}"
	
	EXPECTED_CONTENT_TYPE = 'application/x-tar'
	@content_disposition = @res.headers[:content_disposition]
	@zip_file_name = @content_disposition.split('=')[-1].strip() if @content_disposition.include? '='
	@last_modified = @res.headers[:last_modified]
	
	puts "content-disposition: #{@content_disposition}"
	puts "download file name: #{@zip_file_name}"
	puts "last-modified: #{@last_modified}"
	
	assert(@res.headers[:content_type]==EXPECTED_CONTENT_TYPE, "Content Type must be #{EXPECTED_CONTENT_TYPE} was #{@res.headers[:content_type]}")
end

Then /^I get back a response code of "(.*?)"$/ do |response_code|
  puts "@res.headers: #{@res.headers}"
  puts "@res.code: #{@res.code}"
  assert(@res.code.to_i == response_code.to_i, "The return code is #{@res.code}. Expected: #{response_code}")
end

Then /^the content length in response header is "(.*?)"$/ do |length|
  content_length = @res.headers[:content_length]
  assert(content_length.to_i == length.to_i)
end

Then /^I store the file content$/ do
  @received_file = Dir.pwd + "/Final.tar"
  File.open(@received_file, "a") do |outf|
    outf << @res.body
  end
end

Then /^I process the file content$/ do
  file = File.open(@path, "rb")
  original_tar_contents = file.read

  res_content = @res.body.split("\r\n")
  @received_file = Dir.pwd + "/Final.tar"

  File.open(@received_file, "wb") do |outf|
    res_content.each { |content|
      if not ((content.include? "--MULTIPART_BYTERANGES") || (content.include? "Content-Range"))
        outf << content
      end
    }
  end
end

Then /^the file is decrypted$/ do
  file = File.open(@received_file, "rb")
  contents = file.read
  @final_content = decrypt(contents)
end

Then /^I see that the combined file matches the tar file$/ do
  assert(File.size(@received_file) == File.size(@path))
  File.delete(@received_file)
  @received_file = nil
end

Then /^I combine the overlapped parts$/ do
  #assuming chunks are in order
  chunk_content = @res.body
  chunk_length = @res.headers[:content_length]
  chunk_range = @res.headers[:content_range]
  range = chunk_range.split("bytes ")[1].split("/")[0]
  range_start = range.split("-")[0].to_i
  range_end = range.split("-")[1].to_i

  current_file_size = File.size(@received_file)
  if (range_end > current_file_size)
    range_start = current_file_size - range_start
  end

  nonoverlap_range = Range.new(range_start, range_end)
  nonoverlap_content = chunk_content[nonoverlap_range]
  File.open(@received_file, "a") do |outf|
    outf << nonoverlap_content
  end
end

Then /^I verify the bytes I have are correct$/ do
  chunk_length = @res.headers[:content_length]
  chunk_range = @res.headers[:content_range]
  range = chunk_range.split("bytes ")[1].split("/")[0]
  range_start = range.split("-")[0].to_i
  range_end = range.split("-")[1].to_i
  
  result = compareWithOriginalFile(@res.body, range_start, range_end)
  assert(result == true)
end

Then /^the file size is "(.*?)"$/ do |file_size|
  puts File.size(@received_file)
end

Then /^I verify I do not have the complete file$/ do
  assert(File.size(@received_file) != File.size(@path))
  File.delete(@received_file)
  @received_file = nil
end

Then /^I check the version of http response headers$/ do
  LATEST_API_VERSION = "v1.2"

  returned_version = @res.headers[:x_executedPath].split("/").first

  assert(returned_version==LATEST_API_VERSION, "Returned version is wrong. Actual: #{returned_version} Expected: #{LATEST_API_VERSION}")
end

Then /^I check the http response headers$/ do  

  if @zip_file_name == "sample-extract.tar"
    EXPECTED_LAST_MODIFIED = "Not Specified"
    assert(@res.headers[:last_modified].to_s==EXPECTED_LAST_MODIFIED, "Last Modified date is wrong! Actual: #{@res.headers[:last_modified]} Expected: #{EXPECTED_LAST_MODIFIED}" )
  elsif @res.code == 200
    @db ||= Mongo::Connection.new(PropLoader.getProps['DB_HOST']).db('sli')
    coll = "bulkExtractFiles";
    src_coll = @db[coll]
    raise "Could not find #{coll} collection" if src_coll.count == 0

    ref_doc = src_coll.find({"body.tenantId" => "Midgar"}).to_a
    raise "Could not find #{coll} document with tenant #{"Midgar"}" if ref_doc.count == 0

    puts "bulkExtractFiles record: #{ref_doc}"
    
    found = false
    ref_doc.each do |row|
      
      path = row['body']['path']
      assert(path != nil, "A mongo record doesn't have data for a bulk extract file's location")
      
      file_name = File.basename(path)
      
      if file_name == @zip_file_name

        dateFromMongo = row['body']['date'].to_datetime.to_time.to_s
        if dateFromMongo != nil
          @last_modified = DateTime.parse(@last_modified).to_time.to_s
          assert(@last_modified==dateFromMongo, "last-modified must be #{dateFromMongo} was #{@last_modified}")
        end
        
        found = true
        
      end
    end
    
    assert(found, "A bulk extract with #{@zip_file_name} was not found in the mongo database")
    
  end
end

Then /^the response is decrypted$/ do
  @plain = decrypt(@res.body)
end

Then /^I see that the response matches what I put in the fake tar file$/ do
  assert(@plain == $FAKE_FILE_TEXT, "Decrypted text in 'tar' file did not match, expected #{$FAKE_FILE_TEXT} received #{@plain}")
end


Then /^I have all the information to make a custom bulk extract request$/ do
  puts "@res.headers: #{@res.headers}"
  @last_modified = @res.headers[:last_modified]
  @accept_ranges = @res.headers[:accept_ranges]
  @etag = @res.headers[:etag]
  @content_range = @res.headers[:content_range]
  @content_length = @res.headers[:content_length]
  assert(@last_modified != nil, "Last-Modified header is empty")
  assert(@accept_ranges == "bytes", "Accept-Ranges header is not bytes")
  assert(@etag != nil, "ETag header is empty")
  assert(@content_length = @file_size, "Content-Length header is incorrect")
  assert(@content_range != nil, "Content-Range header is incorrect")
end

def getAppId()
  db ||= Mongo::Connection.new(PropLoader.getProps['DB_HOST']).db('sli')
  userSessionColl = db.collection("userSession")
  clientId = userSessionColl.find_one({"body.appSession.token" => @sessionId}) ["body"]["appSession"][0]["clientId"]
  appColl = db.collection("application")
  appId = appColl.find_one({"body.client_id" => clientId}) ["_id"]
  return appId
end

def encrypt(unEncryptedFilePath, decryptedFilePath)
  unEncryptedFile = File.open(unEncryptedFilePath, "rb")
  contents = unEncryptedFile.read

  public_key = OpenSSL::PKey::RSA.new File.read './test/features/bulk_extract/features/test-key.pub'

  cipher = OpenSSL::Cipher.new('AES-128-CBC')
  cipher.encrypt
  cipher.key = key = cipher.random_key
  cipher.iv = iv = cipher.random_iv
  encrypted_key = public_key.public_encrypt(key)
  encrypted_iv = public_key.public_encrypt(iv)

  encrypted_data = cipher.update(contents) # Encrypt the data.
  encrypted_data << cipher.final

  File.open(decryptedFilePath, "wb") do |outf|
    outf << encrypted_iv
    outf << encrypted_key
    outf << encrypted_data
  end
end

def decrypt(content)
  private_key = OpenSSL::PKey::RSA.new File.read './test/features/bulk_extract/features/test-key'
  assert(content.length >= 512)
  encryptediv = content[0,256] 
  encryptedsecret = content[256,256]
  encryptedmessage = content[512,content.length - 512]

  decrypted_iv = private_key.private_decrypt(encryptediv)
  decrypted_secret = private_key.private_decrypt(encryptedsecret)
 
  aes = OpenSSL::Cipher.new('AES-128-CBC')
  aes.decrypt
  aes.key = decrypted_secret
  aes.iv = decrypted_iv
  @decrypted = aes.update(encryptedmessage) + aes.final

  if $SLI_DEBUG 
    puts("Final is #{aes.final}")
    puts("IV is #{encryptediv}")
    puts("Decrypted iv type is #{decrypted_iv.class} and it is #{decrypted_iv}")
    puts("Encrypted message is #{encryptedmessage}")
    puts("Cipher is #{aes}")
    puts("Plain text length is #{@decrypted.length} and it is #{@decrypted}")
    puts "length #{content.length}"
  end
  return @decrypted
end

def compareWithOriginalFile(content, range_start, range_end)
  file = File.open(@path, "rb")
  file_contents = file.read
  range = Range.new(range_start, range_end)
  file_range_content = file_contents[range]
  if (file_range_content == content)
    return true
  else
    return false
  end
end

def makeCustomHeader(range, if_range = @etag, last_modified = @last_modified)
   header = {:if_range => if_range}
   header.store(:last_modified, last_modified)
   header.store(:range, "bytes=" + range)
   return header
end

After('@fakeTar') do 
#Given /^I remove the fake tar file and remove its reference in Mongo$/ do
  conn = Mongo::Connection.new(PropLoader.getProps['DB_HOST'])
  db ||= conn.db('sli')
  #path = Dir.pwd + "/fake.tar"
  src_coll = db["bulkExtractFiles"]
  src_coll.remove({"_id" => @fake_tar_id})
  File.delete(@path)
  if(@received_file != nil)
    File.delete(@received_file)
  end
  conn.close()
end
