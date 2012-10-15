#!/usr/bin/env ruby

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


=begin

This script is used to trigger the ingestion of a file.
It is meant to be executed by ProFTPD using the mod_exec module hooked on the completion of the 'STOR' commmand (file upload finished).
The 'stomp' rubygem is required to issue the trigger command via ActiveMQ stomp protocol.
Configuration of this script is necessary based on the deployment environment (see below, "Configure based on deployment")

example invocation:
ruby publish_file_uploaded.rb STOR /home/ingestion/tenant/file.zip

=end

require 'rubygems'
require 'stomp'

#   Configure based on deployment
########################################
ACTIVEMQ_HOST = 'localhost'
ACTIVEMQ_PORT = 61613
ACTIVEMQ_QUEUE = 'ingestion.landingZone'
########################################

operation = ARGV[0]
if operation == 'STOR'

  path = ARGV[1]

  client = Stomp::Client.new '', '', ACTIVEMQ_HOST, ACTIVEMQ_PORT, false

  client.publish ACTIVEMQ_QUEUE, 'File upload completed.', { :persistent => true, :filePath => path }

  puts 'Sent message to ACTIVEMQ_QUEUE: #{ACTIVEMQ_QUEUE} file has been uploaded: #{path}'
  client.close
end
