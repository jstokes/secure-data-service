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

require 'digest/md5'
require 'digest/sha1'
require 'logger'
require "rexml/document"
require 'yaml'

require_relative 'validator.rb'
require_relative 'util.rb'
require_relative 'world_builder.rb'

class Odin
  def initialize
    $stdout.sync = true
    @log = Logger.new($stdout)
    @log.level = Logger::INFO
  end

  def generate( scenario )

    Dir["#{File.dirname(__FILE__)}/interchangeGenerators/*.rb"].each { |f| load(f) }

    configYAML = YAML.load_file(File.join(File.dirname(__FILE__),'config.yml'))

    if ( scenario.nil? )
      scenario = configYAML['scenario']
    end

    scenarioYAML = YAML.load_file(File.join(File.dirname(__FILE__), 'scenarios', scenario ))

    prng = Random.new(configYAML['seed'])
    Dir.mkdir('generated') if !Dir.exists?('generated')

    time = Time.now
    pids = []
    
    # Create a snapshot of the world
    edOrgs = WorldBuilder.new.build(prng, scenarioYAML)
    @log.info "edOrgs: #{edOrgs}"
    
    # Process the work_order(s)
    pids << fork {  StudentParentGenerator.new.write(prng, scenarioYAML)           }
    pids << fork {  MasterScheduleGenerator.new.write(prng, scenarioYAML)       }
    Process.waitall

    finalTime = Time.now - time
    @log.info "Total generation time: #{finalTime} secs"

    genCtlFile

  end

  def validate()
    valid = true
    Dir["#{File.dirname(__FILE__)}/generated/*.xml"].each { |f|

      valid = valid && validate_file(f)

    }
    return valid
  end

  # Generates a MD5 hash of the generated xml files.
  def md5()
    hashes = []
    Dir["#{File.dirname(__FILE__)}/generated/*.xml"].each { |f|
      hashes.push( Digest::MD5.hexdigest( f ))
    }
    
    return Digest::MD5.hexdigest( hashes.to_s )
  end
end

