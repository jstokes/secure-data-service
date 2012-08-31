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

require 'rubygems'
require 'mongo'
require 'fileutils'

if __FILE__ == $0
  if ARGV.length == 0
    branch = "master"
  elsif ARGV.length == 1
    branch = ARGV[0]
  else
    puts "Usage: prompt>ruby #{$0} branchName"
    puts "Example: ruby #{$0} alpha.1"
    puts "If no branchName is specified, it defaults to master"
    exit(1)
  end
end

puts "*******NOTE******** Ingestion must have been built prior to running this script so that the ingestion/ingestion-validation/target directory exists!!!!"

curTime = Time.new
dirString = ""

dirs_to_zip = ["acceptance-tests", "opstools", "admin-tools/approval", "ingestion/ingestion-validation/target/"]

`cd ..;zip -r testBundle_#{curTime.strftime("%m%d%Y_%H%M%S")}_#{branch}.zip #{dirs_to_zip.join(" ")}`



