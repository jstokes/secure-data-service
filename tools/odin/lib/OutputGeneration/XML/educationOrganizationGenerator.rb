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
require_relative "../../Shared/data_utility.rb"

Dir["#{File.dirname(__FILE__)}/../EntityClasses/*.rb"].each { |f| load(f) }

# event-based education organization interchange generator
class EducationOrganizationGenerator < InterchangeGenerator

  # initialization will define the header and footer for the education organization interchange
  # writes header to education organization interchange
  # leaves file handle open for event-based writing of ed-fi entities
  def initialize
    @header = <<-HEADER
<?xml version="1.0"?>
<InterchangeEducationOrganization xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://ed-fi.org/0100"
xsi:schemaLocation="http://ed-fi.org/0100 ../../sli/edfi-schema/src/main/resources/edfiXsd-SLI/SLI-Interchange-EducationOrganization.xsd ">
HEADER
    @footer = <<-FOOTER
</InterchangeEducationOrganization>
FOOTER
    @handle = File.new("generated/InterchangeEducationOrganization.xml", 'w')
    @handle.write(@header)
  end

  # creates and writes state education agency to interchange
  def create_state_education_agency(rand, id)
    @handle.write SeaEducationOrganization.new(id, rand).render
  end

  # creates and writes local education agency to interchange
  def create_local_education_agency(rand, id, parent_id)
    @handle.write LeaEducationOrganization.new(id, parent_id, rand).render
  end

  # creates and writes school to interchange
  def create_school(rand, id, parent_id, type)
    @handle.write SchoolEducationOrganization.new(rand, id, parent_id, type).render
  end

  # creates and writes course to interchange
  def create_course(rand, id, ed_org_id)
    @handle.write Course.new(id.to_s, prng).render
  end

  # creates and writes program to interchange
  def create_program(rand, id)
    @handle.write Program.new(id.to_s, prng).render
  end

  # writes footer and closes education organization interchange file handle
  def close
    @handle.write(@footer)
    @handle.close()
  end
end
