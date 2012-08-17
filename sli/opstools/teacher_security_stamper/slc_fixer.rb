
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

require 'rubygems'
require 'mongo'
require 'benchmark'
require 'set'
require 'date'
require 'logger'

class SLCFixer
  attr_accessor :db, :log

  def initialize(db, forever = false, tenant = nil, grace_period = 2000)
    $stdout.sync = true
    @tenant = tenant
    @forever = forever
    @grace_period = grace_period
    @basic_query = tenant.nil? ? {} : {"metaData.tenantId" => tenant} 
    @db = db
    @basic_options = {:timeout => false, :batch_size => 100}
    @log = Logger.new($stdout)
    @log.level = Logger::INFO
  end

  def measure(lable, &block)
    Thread.current[:count] = 0
    Thread.current[:start_time] = Time.now
    time = Benchmark.measure {block.call()}
    @log.warn "%-15s#{time}" % lable
    total_time = Time.now - Thread.current[:start_time]
    @log.warn "#{lable}"
    @log.warn "\tTotal time: #{total_time} s"
    @log.warn "\tRPS: #{Thread.current[:count]/total_time}"
  end

  def start
    begin
      time = Time.now
      @teacher_ids = {}
      @studentId_to_teachers = {}
      @current_date = Date.today.to_s
      @grace_date = (Date.today - @grace_period).to_s
      @count = 0

      @threads = []

      # Needed to move this inside the start method from init, since in forever mode it woudl only init with the teachers currently in system
      @db['staff'].find({"metaData.tenantId" => @tenant, type: "teacher"}, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
        cursor.each { |teacher|
          @teacher_ids[teacher['_id']] = teacher['metaData']['tenantId']
        }
      }

      measure('students') {stamp_students}
      @threads << Thread.new {measure('sections') {stamp_sections}}
      @threads << Thread.new {measure('programs') {stamp_programs}}
      @threads << Thread.new {measure('cohorts') {stamp_cohorts}}
      @threads << Thread.new {measure('parents') {stamp_parents}}
      @threads << Thread.new {measure('assessments') {stamp_assessments}}
      @threads << Thread.new {measure('disciplines') {stamp_disciplines}}
      @threads << Thread.new {measure('other') {stamp_other}}
      @threads << Thread.new {measure('student-assoc') {stamp_student_associations}}
      @threads << Thread.new {measure('teacher') {stamp_teacher}}
      @threads << Thread.new {measure('gradebook') {stamp_gradebook}}
      @threads << Thread.new {measure('schools') {stamp_schools}}

      @threads.each { |th|
        th.join
      }

      finalTime = Time.now - time
      @log.info "\t Final time is #{finalTime} secs"
    end while @forever
  end

  def stamp_students

    @log.info "Stamping students"
    @db[:student].find(@basic_query, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |student|
        studentId = student['_id']
        tenantId = student['metaData']['tenantId']

        teacherIds = []
        teacherIds += find_teachers_for_student_through_section(studentId,tenantId)
        teacherIds += find_teachers_for_student_through_program(studentId,tenantId)
        teacherIds += find_teachers_for_student_through_cohort(studentId,tenantId)
        teacherIds.flatten!; teacherIds.uniq!
        @studentId_to_teachers[studentId] = teacherIds
        stamp_context(@db['student'], student, teacherIds)
      }
    }

    @studentId_to_teachers.delete_if {|_,v| v.nil?}
    @log.info "Finished: Stamping students"
  end

  def find_teachers_for_student_through_section(studentId, tenantId)
    teachers = []
    @db['studentSectionAssociation'].find({'metaData.tenantId' => tenantId, 'body.studentId'=> studentId,
                                            '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @grace_date}} ]
                                          }, @basic_options) { |ssa_cursor|
      ssa_cursor.each { |ssa|
        @db['teacherSectionAssociation'].find({'metaData.tenantId' => tenantId, 'body.sectionId'=> ssa['body']['sectionId'],
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

  def find_teachers_for_student_through_cohort(studentId, tenantId)
    teachers = []
    @db['studentCohortAssociation'].find({'metaData.tenantId' => tenantId, 'body.studentId'=> studentId,
                                           '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]
                                         }, @basic_options) { |stu_assoc_cursor|
      stu_assoc_cursor.each { |stu_assoc|
        #@log.debug "stuCohortAssoc->cohortId #{stu_assoc['body']['cohortId'].to_s}"
        #@log.debug "found assoc - #{stu_assoc['_id']} #{stu_assoc['body']['endDate']}"
        @db['staffCohortAssociation'].find({'metaData.tenantId' => tenantId, 'body.cohortId'=> stu_assoc['body']['cohortId'],
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

  def find_teachers_for_student_through_program(studentId, tenantId)
    teachers = []
    @db['studentProgramAssociation'].find({'metaData.tenantId' => tenantId, 'body.studentId'=> studentId,
                                            '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]
                                          }, @basic_options) { |stu_assoc_cursor|
      stu_assoc_cursor.each { |stu_assoc|
        #@log.debug "stuProgramAssoc->programId #{stu_assoc['body']['programId'].to_s}"
        #@log.debug "found assoc - #{stu_assoc['_id']} #{stu_assoc['body']['endDate']}"
        @db['staffProgramAssociation'].find({'metaData.tenantId' => tenantId, 'body.programId'=> {'$in'=> [stu_assoc['body']['programId']]},
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

    # teachers that have context to a student student also have context to:
    # studentSectionAssocs, sections, and teacherSectionAssocs

    @log.info "Stamping studentSecctionAssociations"
    section_to_teachers = {}
    @db[:studentSectionAssociation].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |ssa|
        teachers = @studentId_to_teachers[ssa['body']['studentId']]
        stamp_context(@db['studentSectionAssociation'],ssa,teachers)
        @log.info "Stamping grade for studentSectionAssociation##{ssa["_id"]}"
        @db[:grade].find({"metaData.tenantId" => ssa['metaData']['tenantId'], "body.studentSectionAssociationId" => ssa["_id"]}, @basic_options) { |cur| 
          cur.each { |grade|
            stamp_context(@db['grade'],grade,teachers)
          }
        }

        section_to_teachers[ssa['body']['sectionId']] ||= []
        section_to_teachers[ssa['body']['sectionId']] += teachers unless teachers.nil?
      }
    }
    @log.info "Finished: Stamping studentSecctionAssociations"
    section_to_teachers.each { |_,t| t.flatten!; t.uniq! }

    # push teacher listed in teacherSectionAssociation to have context to section
    @log.info "Stamping teacherSecctionAssociations"
    @db[:teacherSectionAssociation].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |tsa|
        section_to_teachers[tsa['body']['sectionId']] ||= []
        section_to_teachers[tsa['body']['sectionId']].push tsa['body']['teacherId']
        stamp_context(@db['teacherSectionAssociation'],tsa,section_to_teachers[tsa['body']['sectionId']])
      }
    }
    @log.info "Finished: Stamping teacherSecctionAssociations"

    @log.info "Stamping sections"
    @db[:section].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |sec|
        stamp_context(@db['section'],sec,section_to_teachers[sec['_id']])
      }
    }
    @log.info "Finished: Stamping sections"

    @log.info "Stamping gradebookEntries"
    @db[:gradebookEntry].find(@basic_query, {fields: ['body.sectionId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |item|
        stamp_context(@db[:gradebookEntry], item, section_to_teachers[item['body']['sectionId']])
      }
    }
    @log.info "Finished: Stamping gradebookEntries"

    #Session related entities below
    
    #iterates over section again to populate session and course offering context
    session_to_teachers = {}
    course_offering_to_teachers = {}
    @log.info "Iterating over sections"
    @db[:section].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |sec|
        teachers = section_to_teachers[sec['_id']]
        next if teachers.nil?

        course_offering_to_teachers[sec['body']['courseOfferingId']] ||= [] 
        course_offering_to_teachers[sec['body']['courseOfferingId']] +=  teachers

        session_to_teachers[sec['body']['sessionId']] ||= []
        session_to_teachers[sec['body']['sessionId']] += teachers
      }
    }
    @log.info "Finished Iterating over sections"
    session_to_teachers.each { |_,t| t.flatten!; t.uniq! }

    @log.info "Stamping schoolSessionAssociation"
    @db[:schoolSessionAssociation].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |ssa|
        stamp_context(@db['schoolSessionAssociation'], ssa, session_to_teachers[ssa['body']['sessionId']])
      }
    }
    @log.info "Finished: Stamping schoolSessionAssociation"

    gp_to_teachers = {}
    @log.info "Stamping sessions"
    @db[:session].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |ses|
        teachers = session_to_teachers[ses['_id']]

        stamp_context(@db['session'], ses, teachers)

        gp_id = ses['body']['gradingPeriodReference']
        unless gp_id.nil? || teachers.nil?
          gp_to_teachers[gp_id] ||= []
          gp_to_teachers[gp_id] += teachers
        end

        co_id = ses['body']['courseOfferingId']
        unless co_id.nil? || teachers.nil?
          course_offering_to_teachers[co_id] ||= []
          course_offering_to_teachers[co_id] += teachers
        end
      }
    }
    @log.info "Finished: Stamping sessions"
    gp_to_teachers.each { |_,t| t.flatten!; t.uniq! }
    course_offering_to_teachers.each { |_,t| t.flatten!; t.uniq! }

    @log.info "Stamping gradingPeriods"
    @db[:gradingPeriod].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |gp|
        stamp_context(@db['gradingPeriod'], gp, gp_to_teachers[gp['_id']])
      }
    }
    @log.info "Finished: Stamping gradingPeriods"

    #courseOfferings stamped with union of section->courseOffering and session->courseOffering
    course_to_teachers = {}
    @log.info "Stamping courseOffering"
    @db[:courseOffering].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |co|
        teachers = course_offering_to_teachers[co['_id']]
        stamp_context(@db['courseOffering'], co, teachers)

        course_id = co['body']['courseId']
        unless course_id.nil? || teachers.nil?
          course_to_teachers[course_id] ||= []
          course_to_teachers[course_id] += teachers
        end
      }
    }
    @log.info "Finished: Stamping courseOffering"
    course_to_teachers.each { |_,t| t.flatten!; t.uniq! }

    @log.info "Stamping course"
    @db[:course].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |course|
        stamp_context(@db['course'], course, course_to_teachers[course['_id']])
      }
    }
    @log.info "Finished: Stamping course"
  end

  def stamp_cohorts

    cohort_to_teachers = {}

    @log.info "Stamping studentCohortAssociation"
    @db['studentCohortAssociation'].find(@basic_query, {fields: ['_id', 'body.studentId', 'body.cohortId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentCohortAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        stamp_context(@db['studentCohortAssociation'], assoc, teachers)

        cohort_id = assoc['body']['cohortId']
        cohort_to_teachers[cohort_id] ||= []
        cohort_to_teachers[cohort_id] += teachers unless teachers.nil?
      }
    }
    @log.info "Finished: Stamping studentCohortAssociations"
    cohort_to_teachers.each { |_,t| t.flatten!; t.uniq! }

    @log.info "Stamping cohorts"
    @db[:cohort].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |cohort|
        stamp_context(@db['cohort'], cohort, cohort_to_teachers[cohort['_id']])
      }
    }
    @log.info "Finished: Stamping cohorts"

    @log.info "Stamping staffCohortAssociations"
    @db['staffCohortAssociation'].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |assoc|
        teachers = []
        assoc['body']['cohortId'].each { |cohort| teachers += cohort_to_teachers[cohort] unless cohort_to_teachers[cohort].nil? }
        teachers = teachers.flatten
        teachers = teachers.uniq
        stamp_context(@db['staffCohortAssociation'], assoc, teachers)
      }
    }
    @log.info "Finished: Stamping staffCohortAssociations"
  end

  def stamp_parents
    parent_to_teachers = {}
    @log.info "Stamping studentParentAssociations"
    @db[:studentParentAssociation].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']] || []
        stamp_context(@db['studentParentAssociation'], assoc, teachers)

        parent_id = assoc['body']['parentId']
        unless parent_id.nil? || teachers.nil?
          parent_to_teachers[parent_id] ||= []
          parent_to_teachers[parent_id] += teachers
        end
      }
    }
    @log.info "Finished: Stamping studentParentAssociations"
    parent_to_teachers.each { |_,t| t.flatten!; t.uniq! }

    @log.info "Stamping parents"
    @db[:parent].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |parent|
        stamp_context(@db['parent'], parent, parent_to_teachers[parent['_id']])
      }
    }
    @log.info "Finished: Stamping parents"
  end

  def stamp_programs

    program_to_teachers = {}

    @log.info "Stamping studentProgramAssociations"
    @db['studentProgramAssociation'].find(@basic_query, {fields: ['_id', 'body.studentId', 'body.programId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentProgramAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        stamp_context(@db['studentProgramAssociation'], assoc, teachers)

        program_id = assoc['body']['programId']
        program_to_teachers[program_id] ||= []
        program_to_teachers[program_id] += teachers unless teachers.nil?
      }
    }
    @log.info "Finished: Stamping studentProgramAssociations"

    program_to_teachers.each { |_,t| t.flatten!; t.uniq! }
    @log.info "Stamping programs"
    @db[:program].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |program|
        stamp_context(@db['program'], program, program_to_teachers[program['_id']])
      }
    }
    @log.info "Finished: Stamping programs"

    @log.info "Stamping staffProgramAssociations"
    @db['staffProgramAssociation'].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |assoc|
        teachers = []
        assoc['body']['programId'].each { |program| teachers += program_to_teachers[program] unless program_to_teachers[program].nil? }
        teachers = teachers.flatten
        teachers = teachers.uniq
        stamp_context(@db['staffProgramAssociation'], assoc, teachers)
      }
    }
    @log.info "Finished: Stamping staffProgramAssociations"
  end

  def stamp_attendance
    @log.info "Stamping attendance"
    @db['attendance'].find(@basic_query, {fields: ['body.studentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |attendance|
        teachers = @studentId_to_teachers[attendance['body']['studentId']].flatten.uniq
        teachers = teachers.flatten
        teachers = teachers.uniq
        stamp_context(@db['attendance'], attendance, teachers)
      }
    }
    @log.info "Finished: Stamping attendance"
  end

  def stamp_disciplines
    @log.info "Stamping disciplineActions and disciplineIncidents"
    @db['disciplineAction'].find(@basic_query, {fields: ['body.studentId', 'body.staffId', 'body.disciplineIncidentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |action|
        teachers = []
        action['body']['staffId'].each {|id| teachers << id}
        action['body']['studentId'].each {|id| teachers += @studentId_to_teachers[id] unless @studentId_to_teachers[id].nil?}
        teachers = teachers.flatten.uniq
        stamp_context(@db['disciplineAction'], action, teachers)
        action['body']['disciplineIncidentId'].each { |id|
          stamp_full_context(@db['disciplineIncident'], id, action['metaData']['tenantId'], teachers)
        }
      }
    }
    @log.info "Finished: Stamping disciplineActions and disciplineIncidents"

    #TODO look more closely at these relationships and make sure they are stamped correctly.
    @log.info "Stamping studentDisciplineIncidentAssociation"
    @db['studentDisciplineIncidentAssociation'].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |assoc|
        stamp_context(@db['studentDisciplineIncidentAssociation'], assoc, @studentId_to_teachers[assoc['body']['studentId']].flatten.uniq) if @studentId_to_teachers.has_key? assoc['body']['studentId']
        # @db['studentDisciplineIncidentAssociation'].update(make_ids_obj(assoc), {"$unset" => {"padding" => 1}, "$set" => {'metaData.teacherContext' => @studentId_to_teachers[assoc['body']['studentId']].flatten.uniq }}) unless @studentId_to_teachers[assoc['body']['studentId']].nil?
      }
    }
    @log.info "Finished: Stamping studentDisciplineIncidentAssociation"
  end

  def stamp_assessments
    @log.info "Stamping assessment associations"
    @db['studentAssessmentAssociation'].find(@basic_query, {fields: ['_id', 'body.studentId', 'body.assessmentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        #@log.debug "studentAssessmentAssociation #{assoc['_id']} teacherContext #{teachers.to_s}"
        stamp_context(@db['studentAssessmentAssociation'], assoc, teachers)
      }
    }
    @log.info "Finished: Stamping assessment associations"

    # TODO sectionAssesmentAssociation?
  end

  def stamp_student_associations
    @log.info "Stamping studentSchoolAssociations"
    @db['studentSchoolAssociation'].find(@basic_query, {fields: ['_id', 'body.studentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        stamp_context(@db['studentSchoolAssociation'], assoc, teachers)
      }
    }
    @log.info "Finished: Stamping studentSchoolAssociations"

    @log.info "Stamping studentTranscriptAssociations"
    @db['studentTranscriptAssociation'].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |assoc|
        student_id = assoc['body']['studentId']
        record_id = assoc['body']['studentAcademicRecordId']
        tenant_id = assoc['metaData']['tenantId']
        teachers = []
        teachers += @studentId_to_teachers[student_id] unless student_id.nil? || @studentId_to_teachers[student_id].nil?
        unless record_id.nil?
          @db['studentAcademicRecord'].find({'metaData.tenantId'=>tenant_id, '_id'=>record_id}, @basic_options) { |cursor|
            cursor.each { |record|
              student_id = record['body']['studentId']
              teachers += @studentId_to_teachers[student_id] unless student_id.nil? or @studentId_to_teachers[student_id].nil?
            }
          }
          teachers = teachers.flatten
          teachers = teachers.uniq
        end

        stamp_context(@db['studentTranscriptAssociation'], assoc, teachers)
      }
    }
    @studentId_to_teachers.each { |student,teachers|
      #TODO Add tenantId, remove multi
      @db['studentTranscriptAssociation'].update({'body.studentId'=> student}, {"$unset" => {"padding" => 1}, '$set' => {'metaData.teacherContext' => teachers}}, {:multi => true})
    }
    Thread.current[:count] += 1
    @log.info "Finished: Stamping studentTranscriptAssociations"
  end

  def stamp_other
    stamp_direct_student_association :reportCard
    stamp_direct_student_association :studentAcademicRecord
    stamp_direct_student_association :attendance
  end

  # Stamp non-association types that reference students through studentId
  def stamp_direct_student_association(type)
    @log.info "Stamping #{type}"
    @db[type].find(@basic_query, {fields: ['body.studentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |item|
        teachers = @studentId_to_teachers[item['body']['studentId']].flatten.uniq unless @studentId_to_teachers[item['body']['studentId']].nil?
        stamp_context(@db[type], item, teachers)
      }
    }
    @log.info "Finished: Stamping #{type}"
  end

  def stamp_gradebook
    @log.info "Stamping student section gradebook entry"

    @db['studentGradebookEntry'].find(@basic_query, {fields: ['_id', 'body.studentId', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |assoc|
        teachers = @studentId_to_teachers[assoc['body']['studentId']]
        stamp_context(@db['studentGradebookEntry'], assoc, teachers)
      }
    }
    @log.info "Finished: Stamping student section gradebook entry"
  end

  def stamp_teacher

    school_to_teachers = {}
    teacher_to_schools = {}
    teacher_to_tenant = {}
    assoc_to_teacher = {}
    @log.info "Iterating teachersSchoolAssociations"
    @db['teacherSchoolAssociation'].find({'$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ], "metaData.tenantId" => @tenant},
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
    @log.info "Finished: Iterating teachersSchoolAssociations"

    # tag teachers
    @log.info "Stamping teachers"
    @db['staff'].find({"metaData.tenantId" => @tenant,'type'=>'teacher'}, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |teacher|
        teacher_id = teacher['_id']
        teachers = [teacher_id]

        schools = teacher_to_schools[teacher_id]
        unless schools.nil?
          schools.each { |school| teachers += school_to_teachers[school] unless school_to_teachers[school].nil?}
          teachers = teachers.flatten
          teachers = teachers.uniq
        end

        stamp_context(@db['staff'], teacher, teachers)
      }
    }
    @log.info "Finished: Stamping teachers"

    # tag teacherSchoolAssociations
    @log.info "Finished: Stamping teacherSchoolAssociations"
    assoc_to_teacher.each { |assoc_id,teacher_id|
      teachers = [teacher_id]
      schools = teacher_to_schools[teacher_id]
      unless schools.nil?
        schools.each { |school| teachers += school_to_teachers[school] unless school_to_teachers[school].nil?}
        teachers = teachers.flatten
        teachers = teachers.uniq
      end
      stamp_full_context(@db['teacherSchoolAssociation'], assoc_id, teacher_to_tenant[teacher_id], teachers)
    }
    @log.info "Finished: Stamping teacherSchoolAssociations"

    # tag staff ed orgs
    # NOTE staff ed org tagging on basis that teachers are connected to ed orgs through teacherSchoolAssociation and staff through staffEdOrgAssociation
    staff_to_schools = {}
    staff_to_tenant = {}
    @log.info "Stamping staffEducationOrganizationAssociation"
    @db['staffEducationOrganizationAssociation'].find(@basic_query, @basic_options) { |cursor|
      cursor.each { |assoc|
        staff_id = assoc['body']['staffReference']
        ed_org_id = assoc['body']['educationOrganizationReference']
        tenant_id = assoc['metaData']['tenantId']
        stamp_context(@db['staffEducationOrganizationAssociation'], assoc, staff_id)
        if school_to_teachers.has_key? ed_org_id
          staff_to_schools[staff_id] ||= []
          staff_to_schools[staff_id].push ed_org_id
          staff_to_tenant[staff_id] = tenant_id
        end
      }
    }
    @log.info "Finished: Stamping staffEducationOrganizationAssociation"

    # tag non-teacher staff
    @log.info "Stamping non-teacher staff"
    staff_to_schools.each { |staff_id, schools|
      teachers = []
      schools.each { |school| teachers << school_to_teachers[school] }
      teachers = teachers.flatten
      teachers = teachers.uniq
      stamp_full_context(@db['staff'], staff_id, staff_to_tenant[staff_id], teachers)
    }
    @log.info "Finished: Stamping non-teacher staff"
  end

  def stamp_schools
    @log.info "Stamping schools"
    @db['educationOrganization'].find({"metaData.tenantId" => @tenant, 'type'=>'school'}, {fields: ['_id', 'metaData.tenantId']}.merge(@basic_options)) { |cursor|
      cursor.each { |school|
        teachers = {}
        @db['teacherSchoolAssociation'].find({'metaData.tenantId'=>school['metaData']['tenantId'], 'body.schoolId'=>school['_id'], '$or'=> [ {'body.endDate'=> {'$exists'=> false}}, {'body.endDate'=> {'$gte'=> @current_date}} ]},
                                             @basic_options) { |cursor|
          cursor.each { |tsa|
            teacher_id = tsa['body']['teacherId']
            teachers[teacher_id] = true
          }
        }
        stamp_context(@db['educationOrganization'], school, teachers.keys)
        #@log.debug "school #{school['_id']} teachers #{teachers.keys}"
      }
    }
    @log.info "Finished: Stamping schools"
  end

  private

  def make_ids_obj(record)
    obj = {}
    obj['_id'] = record['_id']
    if record.include? 'metaData' and record['metaData'].include? 'tenantId'
      obj['metaData.tenantId'] = record['metaData']['tenantId']
    else
      obj['metaData.tenantId'] = nil
    end
    obj
  end

  def stamp_context(collection, object, teachers)
    obj = make_ids_obj(object)
    stamp_full_context(collection, obj['_id'], obj['metaData.tenantId'], teachers)
  end

  def stamp_full_context(collection, id, tenant, teachers)
    #puts "stamping " + collection.name + ": " + id.to_s + " with tenant " + tenant + " for teachers " + teachers.to_s
    if tenant.nil?
      @log.warn "No tenant for #{collection.name}##{id}"
      # return
    end
    begin
      if !id.is_a? Array
        id = [id]
      end
      id.each { |i|
        collection.update({"_id" => i, "metaData.tenantId" => tenant}, {"$unset" => {"padding" => 1}, '$set' => {'metaData.teacherContext' => teachers}})
        @count += 1
        Thread.current[:count] += 1
        @log.info {"Stamping #{collection.name}"} if @count % 200 == 0
      }
    rescue Exception => e
      @log.warn e.message
    end
  end
end
