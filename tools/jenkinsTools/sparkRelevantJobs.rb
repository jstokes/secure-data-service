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


require 'mongo'
require 'rest-client'


################## Static Data ########################################

@jenkinsHostname = "jenkins.slidev.org"
@jenkinsMongoPort = 27017
@token = ""

@pathToTestMap = {
  "sli/acceptance-tests/test/features/api" => ["api", "odin-api"],
  "sli/acceptance-tests/test/features/simple_idp" => ["admin" , "databrowser"],
  "sli/acceptance-tests/test/features/ingestion" => ["ingestion"],
  "sli/acceptance-tests/test/features/admintools" => ["admin"],
  "sli/acceptance-tests/test/features/databrowser" => ["databrowser"],
  "sli/acceptance-tests/test/features/dash" => ["dashboard"],
  "sli/acceptance-tests/test/features/odin" => ["odin", "jmeter"],
  "sli/acceptance-tests/test/features/ingestion/features/ingestion_dashboardSadPath.feature" => ["ingestion", "dashboard"],
  "sli/acceptance-tests/test/features/ingestion/test_data/DashboardSadPath_IL_Daybreak" => ["ingestion", "dashboard"],
  "sli/acceptance-tests/test/features/ingestion/test_data/DashboardSadPath_IL_Sunset" => ["ingestion", "dashboard"],
  "sli/acceptance-tests/test/features/ingestion/test_data/DashboardSadPath_NY" => ["ingestion", "dashboard"],
  "sli/acceptance-tests/test/features/bulk_extract" => ["bulk-extract"],
  "sli/acceptance-tests/test/data/Midgar_data" => ["api", "odin-api" , "databrowser", "sdk"],
  "sli/acceptance-tests/test/data/Hyrule_data" => ["api", "odin-api" , "sdk"],
  "sli/acceptance-tests/test/data/unified_data" => ["dashboard", "sdk"],
  "sli/acceptance-tests/test/data/application_fixture.json" => ["api", "odin-api", "admin", "sdk"],
  "sli/acceptance-tests/test/data/realm_fixture.json" => ["api", "odin-api", "admin", "dashboard", "sdk"],
  "sli/acceptance-tests/test/data/oauth_authentication_tokens.json" => ["api", "odin-api"],
  "sli/acceptance-tests/suites/bulk-extract.rake" => ["bulk-extract"],
  "sli/acceptance-tests/suites/ingestion.rake" => ["ingestion"],
  "sli/acceptance-tests/suites/dashboard.rake" => ["dashboard"],
  "sli/api/" => ["api", "odin-api", "search-indexer", "jmeter", "admin", "sdk", "bulk-extract"],
  "sli/simple-idp" => ["api", "odin-api", "admin", "sdk"],
  "sli/SDK" => ["admin", "dashboard", "sdk"],
  "sli/data-access" => ["api", "odin-api", "ingestion", "bulk-extract"],
  "sli/domain" => ["api", "odin-api", "ingestion", "bulk-extract"],
  "sli/bulk-extract" => ["bulk-extract"],
  "sli/ingestion/ingestion-core" => ["ingestion", "odin"],
  "sli/ingestion/ingestion-base" => ["ingestion", "odin"],
  "sli/ingestion/ingestion-validation" => ["ingestion"],
  "sli/ingestion/ingestion-service" => ["ingestion", "odin"],
  "sli/admin-tools" => ["admin"],
  "sli/dashboard/src" => ["dashboard"],
  "sli/databrowser" => ["databrowser"],
  "sli/search-indexer" => ["search-indexer"],
  "tools/odin" => ["odin", "odin-api", "jmeter"]
}

@testIdToUrlMap = {
  "api" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20API%20Tests/buildWithParameters",
  "admin" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20Admin%20Tests/buildWithParameters",
  "ingestion" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20Ingestion%20Service%20Tests/buildWithParameters",
  "dashboard" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20Dashboard%20Tests/buildWithParameters",
  "databrowser" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20Databrowser%20Tests/buildWithParameters",
  "sdk" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20SDK%20Tests/buildWithParameters",
  "search-indexer" => "#{@jenkinsHostname}:8080/view/Components/job/Search-Indexer%20Tests/buildWithParameters",
  "odin" => "#{@jenkinsHostname}:8080/view/Components/job/Odin-DataGeneration-Tests/buildWithParameters",
  "jmeter" => "#{@jenkinsHostname}:8080/view/Components/job/NTS%20JMeter%20API%20Performance/buildWithParameters",
  "odin-api" => "#{@jenkinsHostname}:8080/view/Components/job/API_Odin_Tests/buildWithParameters",
  "bulk-extract" => "#{@jenkinsHostname}:8080/view/Components/job/Bulk-Extract%20Tests/buildWithParameters"
}

################## Helpers and Input Parsing ###########################

if __FILE__ == $0
  unless ARGV.length == 1
      puts "Usage: prompt>ruby " + $0 + " commitHash"
      puts "Example: ruby #{$0} 17570fe99fd6cfbdfd82d7121506ce5652548250"
      exit(1)
  end

  currHash = ARGV[0]
end

# returns true if the time stamp of firstHash is less than the timestamp of the secondHash
def checkCommitOrder(firstHash, secondHash)
  firstTime = `git show -s --format=format:"%ad" #{firstHash} --date=raw | awk '{printf $1;}'`
  secondTime = `git show -s --format=format:"%ad" #{secondHash} --date=raw | awk '{printf $1;}'`

  firstTime.to_i < secondTime.to_i
end

# returns array of files changed between two hashes
# returns empty array if timestamp of first hash > second hash
def getFilesChanged(firstHash, secondHash)
  unless checkCommitOrder(firstHash, secondHash)
    return []
  end
  output = `git diff --name-only #{firstHash} #{secondHash}`
  list = output.split(/\n/)

  list
end

def getLastHashFromMongo()
  conn = Mongo::Connection.new(@jenkinsHostname, @jenkinsMongoPort)
  db = conn.db("git_hash")
  coll = db['commit']

  coll.find_one("_id" => "last_used_commit")["commit_hash"]
end

def whichTestsToRun(hash)
  testsToRun = []
  mongoHash = getLastHashFromMongo()
  puts "Last commit hash in mongo is #{mongoHash}"
  changedFiles = getFilesChanged(mongoHash, hash)
  puts "Changed files: #{changedFiles}"
  keySet = @pathToTestMap.keys
  changedFiles.each do |file|
    keySet.each do |key|
      if file =~ /^#{key}/
        testsToRun.push @pathToTestMap[key]
        next
      end
    end
  end
  testsToRun.flatten.uniq
end

# given a test id to run, it will make the relevant api posts to jenkins to spart the appropriate test jobs
def sparkTest(test, hash)
  data = ""
  url = "http://jenkinsapi_user:test1234@#{@testIdToUrlMap[test]}?COMMIT_HASH=#{hash}"
  puts "Running #{test} test with url #{url}"
  res = RestClient.post(url, data, {:content_type => "application/json"} ){|response, request, result| response }

  puts(res.code,res.body,res.raw_headers)
end

# will update mongo to reflect the supplied git hash as the last used
def updateMongo(hash)
  conn = Mongo::Connection.new(@jenkinsHostname, @jenkinsMongoPort)
  db = conn.db("git_hash")
  coll = db['commit']
  currTime = Time.new

  coll.update({"_id" => "last_used_commit"}, {"$set" => {"commit_hash" => hash, "lastUpdate" => currTime}})

  puts "Newly persisted git hash in mongo: #{coll.find_one("_id" => "last_used_commit")["commit_hash"]}"


end

##################### Main Methods #########################################


whichTestsToRun(currHash).each do |test|
  sparkTest(test, currHash)
end

updateMongo(currHash)










