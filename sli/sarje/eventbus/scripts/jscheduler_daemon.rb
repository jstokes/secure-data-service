#!ruby 

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

localdir = File.dirname(__FILE__)
$LOAD_PATH << localdir + "/../lib"
require 'eventbus'
require 'yaml'

if __FILE__ == $0
    unless ARGV.length == 2
        puts "Usage: " + $0 + " config.yml environment "
        exit(1)
    end
    
    config = YAML::load( File.open( ARGV[0] ) )[ARGV[1]]

    # create instances of the queue listener and the job runner and 
    # and plug them into an Jobscheduler
    listener = Eventbus::OplogListener.new 
    jobrunner = Eventbus::HadoopJobRunner.new 
    active_config = config.update(:listener => listener, 
                                  :jobrunner => jobrunner)
    scheduler = Eventbus::JobScheduler.new(active_config)
    scheduler.join
end
