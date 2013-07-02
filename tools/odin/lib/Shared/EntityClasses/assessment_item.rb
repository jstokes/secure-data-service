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

require_relative 'baseEntity'
class AssessmentItem < BaseEntity

  attr_accessor :id, :identificationCode, :itemCategory, :maxRawScore, :correctResponse,
                :assessmentTitle, :assessment, :gradeLevelAssessed, :learningStandardReference, :academicSubject,
                :nomenclature
  def initialize(id, assessment)
    @id = id
    @rand = Random.new(id)
    @assessment = assessment.id
    @assessmentTitle = assessment.id
    @gradeLevelAssessed = assessment.gradeLevelAssessed
    @academicSubject = assessment.academicSubject
    @identificationCode = "#{@assessmentTitle}##{id}"
    @itemCategory = "True-False"
    @maxRawScore = 10
    @correctResponse = id.odd?
    @nomenclature = "nonenclature"
    @learningStandardReference = choose(LearningStandard.learning_standard_ids(
      @@scenario["NUM_LEARNING_STANDARDS_PER_SUBJECT_AND_GRADE"], assessment.subject, assessment.grade))
  end

end
