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

require_relative 'odin'

# Arg is assumed to be scenario name. If no name is provided, use what's specified in config.yml.
scenario = nil
if ARGV.length > 0
  if File.file?("scenarios/" + ARGV[0])
    scenario = ARGV[0]
  else
    puts "Specified scenario (\"#{ARGV[0]}\") does not exist.\n"
  end
end

o = Odin.new
o.generate( scenario )
