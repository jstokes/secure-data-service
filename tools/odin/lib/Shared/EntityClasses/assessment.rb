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

require_relative 'assessment_item'

require_relative 'baseEntity'
class Assessment < BaseEntity

  attr_accessor :id, :assessmentTitle, :assessmentIdentificationCode, :year_of, :gradeLevelAssessed,
    :assessmentFamilyReference, :assessment_items

  def initialize(id, year_of = 2012, gradeLevelAssessed = nil, num_items = 0, assessmentFamilyReference = nil)
    @id = id
    @year_of = year_of
    @gradeLevelAssessed = gradeLevelAssessed
    @assessmentTitle = @id
    @assessmentIdentificationCode = { code: @id, assessmentIdentificationSystemType: 'State' }

    @assessment_items = (1..num_items).map{|i| AssessmentItem.new(i, self.to_hash)}
    @assessmentFamilyReference = assessmentFamilyReference
  end

  def to_hash
    {:id=>@id, :year=>@year_of, :grade=>@gradeLevelAssessed, :family=>@assessmentFamilyReference}
  end

end
