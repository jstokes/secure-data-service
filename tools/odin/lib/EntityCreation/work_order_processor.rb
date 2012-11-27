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

require_relative '../Shared/EntityClasses/student.rb'
require_relative '../Shared/EntityClasses/studentSchoolAssociation.rb'
require_relative '../Shared/EntityClasses/studentSectionAssociation.rb'

class WorkOrderProcessor

  def initialize(work_order, interchanges)
    @id = work_order[:id]
    @rand = Random.new(@id)
    @work_order = work_order
    @student_interchange = interchanges[:studentParent]
    @enrollment_interchange = interchanges[:enrollment]
  end

  def build
    s = Student.new(@id, @work_order[:birth_day_after])
    @student_interchange << s
    @work_order[:sessions].each{ |session|
      gen_enrollment(session)
    }
  end

  def gen_enrollment(session)
    school_id = session[:school]
    schoolAssoc = StudentSchoolAssociation.new(@id, school_id, @rand)
    @enrollment_interchange << schoolAssoc
    session[:sections].each{ |section|
      gen_section(section)
    }
  end

  def gen_section(section)
    sectionAssoc = StudentSectionAssociation.new(@id, section[:id], section[:edOrg], @rand)
    @enrollment_interchange << sectionAssoc
  end

end
