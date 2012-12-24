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

require_relative "../data_utility.rb"
require_relative "baseEntity.rb"
require_relative "enum/GradeLevelType.rb"

# creates school
class SchoolEducationOrganization < BaseEntity

  attr_accessor :state_org_id, :grades, :parent_id, :programs

  def initialize(id, parent_id, type, programs = nil)
    @id = id
    if parent_id.kind_of? String
      @parent_id = parent_id
    else
      @parent_id = DataUtility.get_local_education_agency_id(parent_id)
    end
    @type      = type
    @grades    = []
    if @type == "elementary"
      if id.kind_of? String
        @state_org_id = id
      else
        @state_org_id = DataUtility.get_elementary_school_id(@id)
      end
      GradeLevelType.elementary.each { |level| @grades << GradeLevelType.to_string(level) }
    elsif @type == "middle"
      if id.kind_of? String
        @state_org_id = id
      else
        @state_org_id = DataUtility.get_middle_school_id(@id)
      end
      GradeLevelType.middle.each { |level| @grades << GradeLevelType.to_string(level) }
    else
      if id.kind_of? String
        @state_org_id = id
      else
        @state_org_id = DataUtility.get_high_school_id(@id)
      end
      GradeLevelType.high.each { |level| @grades << GradeLevelType.to_string(level) }
    end
    @programs = programs
  end

  def type
    if @type == "elementary"
      "Elementary School"
    elsif @type == "middle"
      "Middle School"
    elsif @type == "high"
      "High School"
    end 
  end
end
