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

require 'stomp'
require 'json'
require 'thread'

module Eventbus

  class MongoHelper
    JOB_COLLECTION = "jobdefinitions"
    CONFIG = {
        :mongo_host           => "127.0.0.1",
        :mongo_port           => 27017,
        :mongo_db             => "eventbus",
        :mongo_job_collection => "jobs",
        :poll_interval        => 5
    }

    #TODO: move this out to a property
    MONGO_HOME = "/usr/local"
    MONGO_EXEC = "#{MONGO_HOME}/bin/mongo"
    MONGOIMPORT_EXEC = "#{MONGO_HOME}/bin/mongoimport"

    def initialize(config = CONFIG)
      @active_config = config
    end

    # remove the entire database
    def removeDatabase
      connection_str = "#{@active_config[:mongo_host]}:#{@active_config[:mongo_port]}/#{@active_config[:mongo_db]}"
      status, stdout, stderr = systemu "#{MONGO_EXEC} --eval \"db.dropDatabase()\" #{connection_str}"
    end

    # Load a fixture file into the mongodb
    def setFixture(collectionName, fixtureFilePath, dropExistingCollection=true)
      dropOption = (dropExistingCollection) ? "--drop":""
      command = "#{MONGOIMPORT_EXEC} #{dropOption} -d #{@active_config[:mongo_db]} -c #{collectionName} -h #{@active_config[:mongo_host]} --file #{fixtureFilePath}"
      status, stdout, stderr = systemu command

      #sh cmd do |success, exit_code|
      #    assert success, "Failure loading fixture data #{fixtureFilePath}: #{exit_code.exitstatus}"
      #end
    end
  end
end
