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

class StudentAssessment < BaseEntity
  attr_accessor :studentId, :assessment, :date, :score
  def initialize(student_id, assessment_title, date)
    @studentId = student_id
    @assessment = {assessmentTitle: assessment_title}
    @date = date
    @score = student_id.hash % 100
  end
end
