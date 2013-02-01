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

# creates course
class Course < BaseEntity

  attr_accessor :id, :grade, :title, :edOrg, :course_level, :grades_offered, :subject_area,
  :description, :gpa_appl, :defined_by, :career_path, :learning_objectives, :competency_levels
  def initialize(id, grade, title, edOrg)
    @rand = Random.new(id)
    @id = id
    @grade = grade
    @title = title
    @edOrg = edOrg
    if optional?
      @course_level = {:level => choose([
        "Basic or remedial",
        "Enriched or advanced",
        "General or regular",
        "Honors"
        ]), :level_char => choose([
        "Core Subject",
        "Correspondence",
        "Honors",
        "Magnet",
        "Remedial",
        "Basic",
        "General",
        "Other"
        ])}

      @grades_offered = grade

      @subject_area = AcademicSubjectType.to_string(choose(AcademicSubjectType.get_academic_subjects(GradeLevelType.get_key(grade))))

      @description = ("this is a course for " + grade)

      @gpa_appl = choose([
        "Applicable",
        "Not Applicable",
        "Weighted"
      ])

      @defined_by = choose([
        "LEA",
        "National Organization",
        "SEA",
        "School"
      ])

      @career_path = choose([
        "Arts, A/V Technology and Communications",
        "Education and Training",
        "Science, Technology, Engineering and Mathematics"
      ])

      num_objectives = (@@scenario["NUM_LEARNING_OBJECTIVES_PER_SUBJECT_AND_GRADE"] or 2)

      @learning_objectives = LearningObjective.build_learning_objectives(num_objectives, @subject_area, grade)

      @competency_levels = (@@scenario["COMPETENCY_LEVEL_DESCRIPTORS"] or []).collect{|competency_level_descriptor| competency_level_descriptor['code_value']}
      @competency_levels = [1, 2, 3] if @competency_levels.empty?
    end
  end

end
