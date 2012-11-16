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

require_relative "./interchangeGenerator.rb"
Dir["#{File.dirname(__FILE__)}/../baseEntityClasses/*.rb"].each { |f| load(f) }
class MasterScheduleGenerator < InterchangeGenerator
  def initialize
    @header = <<-HEADER
<?xml version="1.0"?>
<InterchangeMasterSchedule xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://ed-fi.org/0100"
xsi:schemaLocation="http://ed-fi.org/0100 ../../sli/domain/src/main/resources/edfiXsd-SLI/SLI-Interchange-MasterSchedule.xsd ">
HEADER
    @footer = <<-FOOTER
</InterchangeMasterSchedule>
FOOTER
  end

  def write(prng, yamlHash)
    File.open("generated/InterchangeMasterSchedule.xml", 'w') do |f|
      f.write(@header)
      #This needs to map to courses

      for id in 0..yamlHash['numCourseOffering']-1 do
        f.write CourseOffering.new(id.to_s, prng).render
      end
      for id in 0..yamlHash['numBellSchedule']-1 do
        f.write BellSchedule.new(id.to_s, prng).render
      end

      f.write(@footer)
    end
  end

end