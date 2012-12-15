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

require_relative 'baseEntity.rb'
require_relative 'enum/GradeLevelType'
require_relative '../data_utility'

class StudentSchoolAssociation < BaseEntity
  attr_accessor :studentId, :schoolStateOrgId, :startYear, :startGrade, :gradPlan

  def initialize(studentId, schoolId, startYear, grade, gradPlan = nil)

    if startYear.nil?
      exit -1
    end

    if grade.nil?
      exit -1
    end

    @studentId = studentId
    @schoolStateOrgId = DataUtility.get_school_id(schoolId, GradeLevelType.school_type(grade))
    @startYear = startYear
    @startGrade = GradeLevelType.get(grade)
    @gradPlan = gradPlan
  end
  
end
