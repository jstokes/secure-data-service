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

testdir = File.dirname(__FILE__)
$LOAD_PATH << testdir + "/../lib"

require 'mongo'
require 'thread'
require 'eventbus'

module Eventbus
  class OpLogReader
    def initialize(config = {})
      @config = {
          :mongo_host => 'localhost',
          :mongo_port => 27017,
          :mongo_db => 'local',
          :mongo_oplog_collection =>'oplog.rs',
          :mongo_connection_retry => 5,
          :mongo_ignore_initial_read => true
      }.merge(config)
    end

    def read_oplogs
      @thread ||= Thread.new do
        connect_to_mongo_oplog
        loop do
          while not @cursor.closed?
            begin
              if doc = @cursor.next_document
                # only care about insert, update, delete
                yield doc if ["i", "u", "d"].include?(doc["op"])
              end
            rescue Exception => e
              puts e
              connect_to_mongo_oplog
            end
          end
        end
      end
    end

    def connect_to_mongo_oplog
      begin
        db = Mongo::Connection.new(@config[:mongo_host], @config[:mongo_port]).db(@config[:mongo_db])
        coll = db[@config[:mongo_oplog_collection]]
        @cursor = Mongo::Cursor.new(coll, :timeout => false, :tailable => true)
        if(@config[:mongo_ignore_initial_read])
          counter = 0
          while @cursor.has_next?
            @cursor.next_document
            counter = counter + 1
          end
          puts "ignore initial read counter = #{counter}"
        end
      rescue Exception => e
        puts "exception occurred when connecting to mongo for oplog: #{e}"
        puts "retrying connection in 5 seconds"
        sleep @config[:mongo_connection_retry]
        retry
      end
    end
  end

  class OpLogThrottler
    def initialize(config = {})
      @config = {
          :throttle_polling_period => 5
      }.merge(config)
      @oplog_queue = Queue.new
      @collection_filter_lock = Mutex.new
      set_collection_filter([])
    end

    def run
      Thread.new do
        loop do
          sleep @config[:throttle_polling_period]
          collection_changed = Set.new
          begin
            loop do
              message = @oplog_queue.pop(true)
              collection_changed << message["ns"]
            end
          rescue
            # no more oplog in oplog queue
          end
          puts "collection changed = #{collection_changed.to_a}"
          collection_filter = get_collection_filter
          collection_changed = collection_filter & collection_changed.to_a
          if(collection_changed != nil && collection_changed != [])
            message = {
                "collections" => collection_changed
            }
            yield message
          end
        end
      end
    end

    def push(oplog)
      @oplog_queue.push(oplog)
    end

    def set_collection_filter(collection_filter)
      @collection_filter_lock.synchronize {
        @collection_filter = collection_filter
      }
    end

    def get_collection_filter()
      collection_filter = []
      @collection_filter_lock.synchronize {
        collection_filter = @collection_filter
      }
      collection_filter
    end
  end

  class OpLogAgent
    attr_reader :threads

    def initialize(config = {})
      config = {
          :publish_queue_name => "/queue/listener",
          :subscribe_queue_name => "/topic/agent"
      }.merge(config)

      @threads = []

      oplog_throttler = Eventbus::OpLogThrottler.new(config)
      oplog_reader = OpLogReader.new(config)

      @threads << oplog_reader.read_oplogs do |incoming_oplog_message|
        oplog_throttler.push(incoming_oplog_message)
      end

      messaging_service = Eventbus::MessagingService.new(config) do |incoming_configuration_message|
        collection_filter = incoming_configuration_message['collection_filter']
        if(collection_filter != nil)
          oplog_throttler.set_collection_filter(collection_filter)
        end
      end

      @threads << oplog_throttler.run do |message|
        messaging_service.publish(message)
      end
    end
  end
end
