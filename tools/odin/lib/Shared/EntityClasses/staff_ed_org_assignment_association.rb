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

require_relative '../data_utility.rb'
require_relative 'baseEntity.rb'
require_relative 'enum/StaffClassificationType.rb'

# creates staff education organization assignment association
class StaffEducationOrgAssignmentAssociation < BaseEntity

  attr_accessor :staff_id, :ed_org_id, :classification, :title, :begin_date

  def initialize(staff, ed_org_id, classification, title, begin_date)
    @staff_id       = DataUtility.get_staff_unique_state_id(staff)
    @ed_org_id      = ed_org_id
    @classification = StaffClassificationType.to_string(classification)
    @title          = title
    @begin_date     = begin_date
  end
end
