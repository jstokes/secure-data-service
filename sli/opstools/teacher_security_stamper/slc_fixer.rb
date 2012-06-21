require 'rubygems'
require 'mongo'
require 'benchmark'
require 'set'
require 'date'
require 'logger'

class SLCFixer
  attr_accessor :db, :log

  def initialize(db, logger = nil, grace_period = 2000)
    @db = db
    @basic_options = {:timeout => false, :batch_size => 100}
    #@log = logger || Logger.new(STDOUT)
    @log = Logger.new(STDOUT)
    @log.level = Logger::ERROR

    @teacher_ids = {}
    @db['staff'].find({type: "teacher"}, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) do |cursor|
      cursor.each do |teacher|
        @teacher_ids[teacher['_id']] = teacher['metaData']['tenantId']
      end
    end

    @studentId_to_teachers = {}
    @studentId_to_tenant = {}

    @current_date = Date.today.to_s
    @grace_date = (Date.today - grace_period).to_s
  end

  def start
    time = Time.now
    @threads = []
    Benchmark.bm(20) do |x|
      @threads << Thread.new {x.report('students')    {stamp_students}}
    end

    @threads.each do |th|
      th.join
    end

    @threads = []
    Benchmark.bm(20) do |x|
      @threads << Thread.new {x.report('section')    {stamp_sections}}
      @threads << Thread.new {x.report('program')    {stamp_programs}}
      @threads << Thread.new {x.report('cohort')     {stamp_cohorts}}
      @threads << Thread.new {x.report('parent')     {stamp_parents}}
      @threads << Thread.new {x.report('assessments')     {stamp_assessments}}
      @threads << Thread.new {x.report('disciplines')     {stamp_disciplines}}
      @threads << Thread.new {x.report('grades')     {stamp_grades}}
      @threads << Thread.new {x.report('other')     {stamp_other}}
      @threads << Thread.new {x.report('misc')     {stamp_misc}}
      @threads << Thread.new {x.report('student_school_association')     {stamp_student_school_association}}
      @threads << Thread.new {x.report('teacher')     {stamp_teacher}}
      @threads << Thread.new {x.report('studentSectionGradebookEntry')     {stamp_gradebook}}
    end

    @threads.each do |th|
      th.join
    end

    finalTime = Time.now - time
    puts "\t Final time is #{finalTime} secs"
  end

  def stamp_students

    @log.info "Stamping students"
    @db['student'].find({}, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |student|
        studentId = student['_id']
        tenantId = student['metaData']['tenantId']
        @studentId_to_tenant[studentId] = tenantId

        teacherIds = []
        teacherIds += find_teachers_for_student_through_section(studentId)
        teacherIds += find_teachers_for_student_through_program(studentId)
        teacherIds += find_teachers_for_student_through_cohort(studentId)
        teacherIds = teacherIds.flatten
        teacherIds = teacherIds.uniq
        @studentId_to_teachers[studentId] = teacherIds

        @db['student'].update({'_id' => studentId, 'metaData.tenantId' => tenantId},
                              {'$set' => {'metaData.teacherContext' => teacherIds}})
      }
    }
  end

  def find_teachers_for_student_through_section(studentId)
    teachers = []
    @db['studentSectionAssociation'].find({'body.studentId'=> studentId, 
                                            '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @grace_date}} ]
                                          }, @basic_options) { |ssa_cursor|
      ssa_cursor.each { |ssa|
        @db['teacherSectionAssociation'].find({'body.sectionId'=> ssa['body']['sectionId'], 
                                                '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @grace_date}} ]
                                              }, @basic_options) { |tsa_cursor|
          tsa_cursor.each { |tsa|
            #@log.debug "teacherSectionAssoc->teacherId #{tsa['body']['teacherId'].to_s}"
            #@log.debug "found assoc - #{tsa['_id']} #{tsa['body']['endDate']}"
            teachers.push tsa['body']['teacherId']
          }
        }
      }
    }
    #@log.debug "section teachers for #{studentId}: #{teachers.to_s}"
    teachers
  end

  def find_teachers_for_student_through_cohort(studentId)
    teachers = []
    @db['studentCohortAssociation'].find({'body.studentId'=> studentId,
                                           '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]
                                         }, @basic_options) { |stu_assoc_cursor|
      stu_assoc_cursor.each { |stu_assoc|
        #@log.debug "stuCohortAssoc->cohortId #{stu_assoc['body']['cohortId'].to_s}"
        #@log.debug "found assoc - #{stu_assoc['_id']} #{stu_assoc['body']['endDate']}"
        @db['staffCohortAssociation'].find({'body.cohortId'=> stu_assoc['body']['cohortId'],
                                              #'$or'=> [ {'body.studentRecordAccess'=> {'$exists'=> false}}, {'body.studentRecordAccess'=> true} ],
                                              'body.studentRecordAccess'=> true,
                                              '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]
                                           }, @basic_options) { |staff_assoc_cursor|
          staff_assoc_cursor.each { |staff_assoc|
            #@log.debug "staffCohortAssoc->staffid #{staff_assoc['body']['staffId'].to_s}"
            #@log.debug "found assoc - #{staff_assoc['_id']} #{staff_assoc['body']['endDate']}"
            staff_assoc['body']['staffId'].each { |id|
              if @teacher_ids.has_key? id
                teachers.push staff_assoc['body']['staffId']
              end
            }
          }
        }
      }
    }
    #@log.debug "cohort teachers for #{studentId}: #{teachers.to_s}"
    teachers
  end

  def find_teachers_for_student_through_program(studentId)
    teachers = []
    @db['studentProgramAssociation'].find({'body.studentId'=> studentId,
                                            '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]
                                          }, @basic_options) { |stu_assoc_cursor|
      stu_assoc_cursor.each { |stu_assoc|
        #@log.debug "stuProgramAssoc->programId #{stu_assoc['body']['programId'].to_s}"
        #@log.debug "found assoc - #{stu_assoc['_id']} #{stu_assoc['body']['endDate']}"
        @db['staffProgramAssociation'].find({'body.programId'=> {'$in'=> [stu_assoc['body']['programId']]},
                                              #'$or'=> [ {'body.studentRecordAccess'=> {'$exists'=> false}}, {'body.studentRecordAccess'=> true} ],
                                              'body.studentRecordAccess'=> true,
                                              '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]
                                            }, @basic_options) { |staff_assoc_cursor|
          staff_assoc_cursor.each { |staff_assoc|
            #@log.debug "staffProgramAssoc->staffid #{staff_assoc['body']['staffId']}"
            #@log.debug "found assoc - #{staff_assoc['_id']} #{staff_assoc['body']['endDate']}"
            staff_assoc['body']['staffId'].each { |id|
              if @teacher_ids.has_key? id
                teachers.push staff_assoc['body']['staffId']
              end
            }
          }
        }
      }
    }
    #@log.debug "program teachers for #{studentId}: #{teachers.to_s}"
    teachers
  end

  def stamp_sections
    @log.info "Stamping sections and associations"

    section_to_teachers = {}
    section_assoc_to_teachers = {}
    section_to_tenant = {}
    section_to_session = {}

    @db['studentSectionAssociation'].find({}, @basic_options) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentSectionAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        @db['studentSectionAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
         
        section_assoc_to_teachers[assoc['_id']] = teachers
        section_id = assoc['body']['sectionId']
        section_to_tenant[section_id] ||= assoc['metaData']['tenantId']
        section_to_teachers[section_id] ||= []
        section_to_teachers[section_id] += teachers
        section_to_teachers[section_id] = section_to_teachers[section_id].flatten
        section_to_teachers[section_id] = section_to_teachers[section_id].uniq
      }
    }

    @db['grade'].find({}, @basic_options) { |cursor|
      cursor.each { |grade|
        teachers = section_assoc_to_teachers[grade['body']['studentSectionAssociationId']].flatten.uniq
        @db[:grade].update(make_ids_obj(grade), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }

    @db['studentCompetency'].find({}, @basic_options) { |cursor|
      cursor.each { |grade|
        teachers = section_assoc_to_teachers[grade['body']['studentSectionAssociationId']].flatten.uniq
        @db[:studentCompetency].update(make_ids_obj(grade), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }

    @db['teacherSectionAssociation'].find({}, @basic_options) { |cursor|
      cursor.each { |assoc|
        section_id = assoc['body']['sectionId']
        teacher_id = assoc['body']['teacherId']
        section_to_tenant[section_id] ||= assoc['metaData']['tenantId']
        section_to_teachers[section_id] ||= []
        section_to_teachers[section_id].push teacher_id
        section_to_teachers[section_id] = section_to_teachers[section_id].flatten
        section_to_teachers[section_id] = section_to_teachers[section_id].uniq

        teachers = section_to_teachers[section_id]
        @db['teacherSectionAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }
  
    section_to_teachers.each { |section, teachers|
      @db['section'].update({'_id'=> section, 'metaData.tenantId'=> section_to_tenant[section]}, {'$set' => {'metaData.teacherContext' => teachers}})
    }

    @db[:gradebookEntry].find({}, {fields: ['body.sectionId', 'metaData.tenantId']}.merge(@basic_options)) do |cursor|
      cursor.each do |item|
        teachers = section_to_teachers[item['body']['sectionId']].flatten.uniq
        @db[:gradebookEntry].update(make_ids_obj(item), {'$set' => {'metaData.teacherContext' => teachers}})
      end
    end


    #Session related stuff
    #Map section to session
    course_to_sections = {}
    @db[:section].find({}, @basic_options) do |cursor|
      cursor.each do |item|
        section_id = item['_id']
        section_to_session[section_id] = item['body']['sessionId']

        course_id = item['body']['courseId']
        course_to_sections[course_id] ||= []
        course_to_sections[course_id].push section_id
      end
    end

    section_to_session.each { |section, session|
      @db['session'].update({'_id'=> session}, {'$set' => {'metaData.teacherContext' => section_to_teachers[section]}})
      @db['schoolSessionAssociation'].update({'body.sessionId'=> session}, {'$set' => {'metaData.teacherContext' => section_to_teachers[section]}})
      @db['courseOffering'].update({'body.sessionId'=> session}, {'$set' => {'metaData.teacherContext' => section_to_teachers[section]}})
      @db['courseOffering'].update({'body.sessionId'=> session}, {'$set' => {'metaData.teacherContext' => section_to_teachers[section]}})
    }

    course_to_sections.each { |course_id, section_ids|
      teachers = []
      section_ids.each { |section| teachers << section_to_teachers[section] }
      teachers = teachers.flatten
      teachers = teachers.uniq
      @db['course'].update({'_id'=>course_id, 'metaData.tenantId'=>section_to_tenant[section_ids.first]}, {'$set' => {'metaData.teacherContext' => teachers}})
    }

  end

  def stamp_cohorts
    @log.info "Stamping cohorts and associations"

    cohort_to_teachers = {}
    cohort_to_tenant = {}

    @db['studentCohortAssociation'].find({}, {fields: ['_id', 'body.studentId', 'body.cohortId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentCohortAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        @db['studentCohortAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})

        cohort_id = assoc['body']['cohortId']
        cohort_to_tenant[cohort_id] ||= assoc['metaData']['tenantId']
        cohort_to_teachers[cohort_id] ||= []
        cohort_to_teachers[cohort_id] += teachers
        cohort_to_teachers[cohort_id] = cohort_to_teachers[cohort_id].flatten
        cohort_to_teachers[cohort_id] = cohort_to_teachers[cohort_id].uniq
      }
    }

    cohort_to_teachers.each { |cohort, teachers|
      @db['cohort'].update({'_id'=> cohort, 'metaData.tenantId'=> cohort_to_tenant[cohort]}, {'$set' => {'metaData.teacherContext' => teachers}})
    }

    @db['staffCohortAssociation'].find({}, @basic_options) { |cursor|
      cursor.each { |assoc|
        teachers = []
        assoc['body']['cohortId'].each { |cohort| teachers += cohort_to_teachers[cohort] }
        teachers = teachers.flatten
        teachers = teachers.uniq
        @db['staffCohortAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }
  end

  def stamp_parents
    @log.info "Stamping parents and associations"

    parent_to_teachers = {}
    parent_to_tenant = {}
    @db['studentParentAssociation'].find({}, {fields: ['_id', 'body.studentId', 'body.parentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        teachers = [] if teachers == nil
        @db['studentParentAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
        
        parent_id = assoc['body']['parentId']
        parent_to_tenant[parent_id] ||= assoc['metaData']['tenantId']
        parent_to_teachers[parent_id] ||= []
        parent_to_teachers[parent_id] += teachers
      }
    }

    parent_to_teachers.each { |parent_id, teachers|
      @db['parent'].update({'_id' => parent_id, 'metaData.tenantId' => parent_to_tenant[parent_id]}, {'$set' => {'metaData.teacherContext' => teachers}})
    }
  end

  def stamp_programs
    @log.info "Stamping programs and associations"

    program_to_teachers = {}
    program_to_tenant = {}

    @db['studentProgramAssociation'].find({}, {fields: ['_id', 'body.studentId', 'body.programId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentProgramAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        @db['studentProgramAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})

        program_id = assoc['body']['programId']
        program_to_tenant[program_id] ||= assoc['metaData']['tenantId']
        program_to_teachers[program_id] ||= []
        program_to_teachers[program_id] += teachers
      }
    }

    program_to_teachers.each { |program, teachers|
      teachers = teachers.flatten
      teachers = teachers.uniq
      @db['program'].update({'_id'=> program, 'metaData.tenantId'=> program_to_tenant[program]}, {'$set' => {'metaData.teacherContext' => teachers}})
    }

    @db['staffProgramAssociation'].find({}, @basic_options) { |cursor|
      cursor.each { |assoc|
        teachers = []
        assoc['body']['programId'].each { |program| teachers += program_to_teachers[program] }
        teachers = teachers.flatten
        teachers = teachers.uniq
        @db['staffProgramAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }
  end
  
  def stamp_attendance
    @log.info "Stamping attendance"
    @db['attendance'].find({}, {fields: ['body.studentId', 'metaData.tenantId']}.merge(@basic_options)) do |cursor|
      cursor.each do |attendance|
        teachers = @studentId_to_teachers[attendance['body']['studentId']].flatten.uniq
        @db['attendance'].update(make_ids_obj(attendance), {'$set' => {'metaData.teacherContext' => teachers}})
      end
    end
  end

  def stamp_grades

  end
  
  def stamp_misc
    @log.info "Stamping staffEducationOrganizationAssociation"
    @db['staffEducationOrganizationAssociation'].find({}, @basic_options) do |cursor|
      cursor.each do |assoc|
        @db['staffEducationOrganizationAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => assoc['body']['staffReference']}})
      end
    end
  end
  
  def stamp_disciplines
    @log.info "Stamping disciplineAction and disciplineIncident"
    @db['disciplineAction'].find({}, {fields: ['body.studentId', 'body.staffId', 'body.disciplineIncidentId', 'metaData.tenantId']}.merge(@basic_options)) do |cursor|
      cursor.each do |action|
        teachers = []
        action['body']['staffId'].each {|id| teachers << id}
        action['body']['studentId'].each {|id| teachers += @studentId_to_teachers[id]}
        teachers = teachers.flatten.uniq
        @db['disciplineAction'].update(make_ids_obj(action), {'$set' => {'metaData.teacherContext' => teachers}})
        action['body']['disciplineIncidentId'].each do |id|
          @db['disciplineIncident'].update({'_id' => id, 'metaData.tenantId' => action['metaData']['tenantId']}, {'$set' => {'metaData.teacherContext' => teachers}})
        end
      end
    end

    #TODO look more closely at these relationships and make sure they are stamped correctly.
    @log.info "Stamping studentDisciplineIncidentAssociation"
    @db['studentDisciplineIncidentAssociation'].find({}, @basic_options) do |cursor|
      cursor.each do |assoc|
        @db['studentDisciplineIncidentAssociation'].update(make_ids_obj(assoc), {"$set" => {'metaData.teacherContext' => @studentId_to_teachers[assoc['body']['studentId']].flatten.uniq}})
      end
    end
  end

  def stamp_assessments
    @log.info "Stamping assessment associations"
    assessment_to_teachers = {}
    assessment_to_tenant = {}

    @db['studentAssessmentAssociation'].find({}, {fields: ['_id', 'body.studentId', 'body.assessmentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentAssessmentAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        @db['studentAssessmentAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})

        assessment_id = assoc['body']['assessmentId']
        assessment_to_tenant[assessment_id] ||= assoc['metaData']['tenantId']
        assessment_to_teachers[assessment_id] ||= []
        assessment_to_teachers[assessment_id] += teachers unless teachers.nil?
      }
    }

    #not stamping assessments because they are public
    #assessment_to_teachers.each { |assessment, teachers|
    #  teachers = teachers.flatten
    #  teachers = teachers.uniq
    #  @db['assessment'].update({'_id'=> assessment, 'metaData.tenantId'=> assessment_to_tenant[assessment]}, {'$set' => {'metaData.teacherContext' => teachers}})
    #}
    # TODO sectionAssesmentAssociation?
  end

  def stamp_student_school_association
    @log.info "Stamping student school associations"

    @db['studentSchoolAssociation'].find({}, {fields: ['_id', 'body.studentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        @db['studentSchoolAssociation'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }
  end

  def stamp_other
    stamp_direct_student_association :reportCard
    stamp_direct_student_association :studentAcademicRecord
    stamp_direct_student_association :attendance
  end

  # Stamp non-association types that reference students through studentId
  def stamp_direct_student_association(type)
    @log.info "Stamping #{type}"
    @db[type].find({}, {fields: ['body.studentId', 'metaData.tenantId']}.merge(@basic_options)) do |cursor|
      cursor.each do |item|
        teachers = @studentId_to_teachers[item['body']['studentId']].flatten.uniq
        @db[type].update(make_ids_obj(item), {'$set' => {'metaData.teacherContext' => teachers}})
      end
    end
  end

  def stamp_gradebook
    @log.info "Stamping student section gradebook entry"

    @db['studentSectionGradebookEntry'].find({}, {fields: ['_id', 'body.studentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        @db['studentSectionGradebookEntry'].update(make_ids_obj(assoc), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }
  end

  def stamp_teacher
    @log.info "Stamping teachers and staff and associations"

    school_to_teachers = {}
    teacher_to_schools = {}
    teacher_to_tenant = {}
    assoc_to_teacher = {}
    @db['teacherSchoolAssociation'].find({'$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]},
                                         {fields: ['_id', 'body.teacherId', 'body.schoolId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        school_id = assoc['body']['schoolId']
        teacher_id = assoc['body']['teacherId']
        school_to_teachers[school_id] ||= []
        school_to_teachers[school_id].push teacher_id
        teacher_to_schools[teacher_id] ||= []
        teacher_to_schools[teacher_id].push school_id

        tenant_id = assoc['metaData']['tenantId']
        teacher_to_tenant[teacher_id] = tenant_id

        assoc_to_teacher[assoc['_id']] = teacher_id
      }
    }

    # tag teachers
    @db['staff'].find({'type'=>'teacher'}, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |teacher|
        teacher_id = teacher['_id']
        teachers = [teacher_id]

        schools = teacher_to_schools[teacher_id]
        unless schools.nil?
          schools.each { |school| teachers += school_to_teachers[school] }
          teachers = teachers.flatten
          teachers = teachers.uniq
        end

        #@db['teacher'].update({'_id'=> teacher_id, 'metaData.tenantId'=> teacher_to_tenant[teacher_id]}, {'$set' => {'metaData.teacherContext' => teachers}})
        @db['staff'].update(make_ids_obj(teacher), {'$set' => {'metaData.teacherContext' => teachers}})
      }
    }

    # tag teacherSchoolAssociations
    assoc_to_teacher.each { |assoc_id,teacher_id|
      teachers = [teacher_id]
      schools = teacher_to_schools[teacher_id]
      unless schools.nil?
        schools.each { |school| teachers += school_to_teachers[school] }
        teachers = teachers.flatten
        teachers = teachers.uniq
      end
      @db['teacherSchoolAssociation'].update({'_id'=> assoc_id, 'metaData.tenantId'=> teacher_to_tenant[teacher_id]}, {'$set' => {'metaData.teacherContext' => teachers}})
    }

    # tag non-teacher staff
    #@db['staffAssociation'].find({'$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]},
    #                                     {fields: ['_id', 'body.teacherId', 'body.schoolId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
    #  cursor.each { |assoc|
    #    school_id = assoc['body']['schoolId']
  end

  private
  def make_ids_obj(record)
    obj = {}
    obj['_id'] = record['_id']
    obj['metaData.tenantId'] = record['metaData']['tenantId']
    obj
  end
end
