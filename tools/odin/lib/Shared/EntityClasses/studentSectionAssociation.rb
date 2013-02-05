=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

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

class StudentSectionAssociation < BaseEntity

  attr_accessor :student, :section, :ed_org_id, :begin_date

  def initialize(student, section, ed_org_id, begin_date, grade)
    @student    = student
    @section    = section
    @ed_org_id  = DataUtility.get_school_id(ed_org_id, GradeLevelType.school_type(grade))
    @begin_date = begin_date
  end
end
