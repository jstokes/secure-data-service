testdir = File.dirname(__FILE__)
$LOAD_PATH << testdir + "/../lib"

require 'mongo'
require 'thread'
require 'eventbus'

module Eventbus
  class OpLogReader
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
        db = Mongo::Connection.new("localhost", 27017).db("local")
        coll = db['oplog.rs']
        @cursor = Mongo::Cursor.new(coll, :timeout => false, :tailable => true)
      rescue Exception => e
        puts "exception occurred when connecting to mongo for oplog: #{e}"
        puts "retrying connection in 5 seconds"
        sleep 5
        retry
      end
    end
  end

  class OpLogThrottler
    def initialize
      @oplog_queue = Queue.new
      @collection_filter_lock = Mutex.new
      set_collection_filter([])
    end

    def run
      Thread.new do
        sleep 5
        counter = 0
        begin
          loop do
            @oplog_queue.pop(true)
            counter = counter + 1
          end
        rescue
          # no more oplog in oplog queue
        end
        puts "ignore first oplog reading: #{counter} oplogs detected"

        loop do
          sleep 5
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
    def initialize
      oplog_throttler = Eventbus::OpLogThrottler.new
      oplog_reader = OpLogReader.new

      oplog_reader.read_oplogs do |incoming_oplog_message|
        oplog_throttler.push(incoming_oplog_message)
      end

      agent_config = {
          :node_name => Socket.gethostname,
          :publish_queue_name => "/queue/listener",
          :subscribe_queue_name => "/topic/agent"
      }

      messaging_service = Eventbus::MessagingService.new(agent_config) do |incoming_configuration_message|
        collection_filter = incoming_configuration_message['collection_filter']
        if(collection_filter != nil)
          oplog_throttler.set_collection_filter(collection_filter)
        end
      end

      oplog_throttler.run do |message|
        messaging_service.publish(message)
      end
    end
  end
end
