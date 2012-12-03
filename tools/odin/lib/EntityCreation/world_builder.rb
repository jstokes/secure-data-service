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

require 'date'
require 'logger'

require_relative "../OutputGeneration/XML/educationOrganizationGenerator.rb"
require_relative "../OutputGeneration/XML/educationOrgCalendarGenerator.rb"
require_relative "../OutputGeneration/XML/masterScheduleGenerator.rb"
require_relative "../Shared/EntityClasses/enum/GradeLevelType.rb"
require_relative "../Shared/EntityClasses/enum/GradingPeriodType.rb"
require_relative "../Shared/EntityClasses/schoolEducationOrganization.rb"
require_relative "../Shared/data_utility.rb"
require_relative "../Shared/date_interval.rb"
require_relative "../Shared/date_utility.rb"

# World Builder
# -> intent is to create 'scaffolding' that represents a detailed, time-sensitive view of the world
#
# generation strategies:
# (1) create world using number of students + time information (begin year, number of years)
# (2) create world using number of schools  + time information (begin year, number of years) [not supported]
class WorldBuilder
  def initialize(prng, yaml)
    small_batch_size  = 500
    medium_batch_size = 5000
    large_batch_size  = 25000

    $stdout.sync = true
    @log = Logger.new($stdout)
    @log.level = Logger::INFO
    @prng = prng
    @scenarioYAML = yaml

    @breakdown = Hash.new
    @edOrgs = Hash.new
    @edOrgs["seas"]       = []
    @edOrgs["leas"]       = []
    @edOrgs["elementary"] = []
    @edOrgs["middle"]     = []
    @edOrgs["high"]       = []

    @breakdown = Hash.new

    initialize_interchanges
  end

  # Builds the initial snapshot of the world
  # -> generates SEA(s), LEAs, Schools, Course [Education Organization interchange]
  # -> Session, GradingPeriod, CalendarDate [Education Organization Calendar interchange]
  # -> generates Staff, Teacher, StaffEdOrgAssignmentAssociation, TeacherSchoolAssociation [Staff Association interchange]
  # -> generates CourseOffering [Master Schedule interchange]
  # -> out of scope: Program, StaffProgramAssociation, StudentProgramAssociation, Cohort, StaffCohortAssociation, StudentCohortAssociation
  # -> returns education organization structure built
  def build()
    if !@scenarioYAML["studentCount"].nil?
      build_world_from_students
    #elsif !@scenarioYAML["schoolCount"].nil?
    #  build_world_from_edOrgs()
    #  --> not supported yet	
    else
      @log.error "studentCount or schoolCount must be set for a world to be created --> Exiting..."
    end

    finalize_interchanges
    return @edOrgs
  end

  # Builds world using the specified number of students as the driving criteria
  # -> ignores schoolCount, if specified in yml file
  # -> creates elementary, middle, and high schools from number of students
  # -> walks back up ed-fi graph to create LEAs and SEA(s) from created schools
  def build_world_from_students
    num_students = @scenarioYAML["studentCount"]
    @log.info "Creating world from initial number of students: #{num_students}"

    # create grade breakdown from total number of students
    # populate education organization structure using breakdown (number of students per grade)
    # update structure with time information (create sessions using begin year and number of years)
    # expand school enrollment information using previously created session information
    # use time information to create master schedule (course offerings, sections)
    # finally, write interchanges
    compute_grade_breakdown(num_students)
    create_edOrgs_using_breakdown
    begin_year, num_years = add_time_information_to_edOrgs
    expand_student_counts_using_time_information(begin_year, num_years)
    create_master_schedule
    write_interchanges
  end

  # iterates through the set of ordered grade levels (Kindergarten through 12th grade) and uses
  # the default percentages (as defined in yaml configuration file) for minimum and maximum per grade
  # to compute the number of students that will initially populate the specified grade level.
  def compute_grade_breakdown(num_students)
    students_so_far = 0
    GradeLevelType::get_ordered_grades.each do |grade|
      num_students_this_grade = get_num_students_per_grade(num_students)
      num_students_this_grade = 0                              if students_so_far >= num_students
      num_students_this_grade = num_students - students_so_far if grade == :TWELFTH_GRADE and num_students_this_grade != 0
      @breakdown[grade]       = num_students_this_grade
      students_so_far += num_students_this_grade
      end
    end

  # randomly select the number of students for the current grade by using the minimum percentage,
  # maximum percentage, and total number of students
  def get_num_students_per_grade(num_students)
    min = @scenarioYAML["MINIMUM_GRADE_PERCENTAGE"]
    max = @scenarioYAML["MAXIMUM_GRADE_PERCENTAGE"]

    if min.nil?
      @log.error "MINIMUM_GRADE_PERCENTAGE must be set for a world to be created --> Exiting..."
      abort
    end
    if max.nil?
      @log.error "MAXIMUM_GRADE_PERCENTAGE must be set for a world to be created --> Exiting..."
      abort
    end

    ((random_on_interval(min, max) / 100) * num_students).round
  end

  # Uses the breakdown hash to bucketize number of students according to type of school
  # they would be enrolled at, and then computes the number of schools required to hold those
  # students according to national averages (average students per elementary school, ...).
  def create_edOrgs_using_breakdown
    num_elementary_school_students = 0
    num_middle_school_students     = 0
    num_high_school_students       = 0

    GradeLevelType::elementary.each { |level| num_elementary_school_students += @breakdown[level] }
    GradeLevelType::middle.each     { |level| num_middle_school_students     += @breakdown[level] }
    GradeLevelType::high.each       { |level| num_high_school_students       += @breakdown[level] }

    num_schools   = 0
    num_schools   = create_schools("elementary", num_schools, num_elementary_school_students)
    num_schools   = create_schools("middle", num_schools, num_middle_school_students)
    num_schools   = create_schools("high", num_schools, num_high_school_students)
    num_districts = update_schools_with_districts(num_schools)
    update_districts_with_states(num_districts)

    # re-shuffle breakdown so that it is representative of actual student distribution (this is primarly for scaled down scenarios)
    update_breakdown_based_on_student_distribution(@scenarioYAML["beginYear"])

    # choose the feeder schools
    WorldBuilder.choose_feeders(@edOrgs['elementary'], @edOrgs['middle'], @edOrgs['high'])
  end

  # go through education organizations and make sure that the @breakdown instance variable is representative of the world
  # that was built
  # -> divergence will occur when scenarios have been specified that are relatively 'small' (less than 500 students)
  # -> occurs because creation of education organizations assembles students into 'waves' (thought of as cohorts)
  def update_breakdown_based_on_student_distribution(year)
    new_breakdown = Hash.new
    @edOrgs["elementary"].each do |school|
      students_by_grade = school["students"][year]
      students_by_grade.each do |grade, num_students|
        new_breakdown[grade] = 0 if !new_breakdown.has_key?(grade)
        new_breakdown[grade] += num_students
      end
    end
    @edOrgs["middle"].each do |school|
      students_by_grade = school["students"][year]
      students_by_grade.each do |grade, num_students|
        new_breakdown[grade] = 0 if !new_breakdown.has_key?(grade)
        new_breakdown[grade] += num_students
      end
    end
    @edOrgs["high"].each do |school|
      students_by_grade = school["students"][year]
      students_by_grade.each do |grade, num_students|
        new_breakdown[grade] = 0 if !new_breakdown.has_key?(grade)
        new_breakdown[grade] += num_students
      end
    end
    @breakdown = new_breakdown
  end

  # uses the total number of students, as well as range of [min, max] to compute students at a school of type 'tag', and then
  # populates the edOrgs structure according to that tag (which should be "elementary", "middle", or "high").
  def create_schools(tag, num_schools, total_num_students)
    if tag == "elementary"
      avg_num_students = @scenarioYAML["AVERAGE_ELEMENTARY_SCHOOL_NUM_STUDENTS"]
    elsif tag == "middle"
      avg_num_students = @scenarioYAML["AVERAGE_MIDDLE_SCHOOL_NUM_STUDENTS"]
    elsif tag == "high"
      avg_num_students = @scenarioYAML["AVERAGE_HIGH_SCHOOL_NUM_STUDENTS"]
    end
    avg_num_students_threshold = @scenarioYAML["AVERAGE_NUM_STUDENTS_THRESHOLD"]
    min                        = avg_num_students - (avg_num_students * avg_num_students_threshold)
    max                        = avg_num_students + (avg_num_students * avg_num_students_threshold)
    school_counter             = num_schools
    student_counter            = 0
    while student_counter < total_num_students
      current_students = random_on_interval(min, max).round
      school_counter  += 1
      if current_students > (total_num_students - student_counter)        
        current_students = (total_num_students - student_counter)
      end
      student_counter += current_students
      
      edOrg              = Hash.new
      edOrg["id"]        = school_counter
      edOrg["parent"]    = nil
      #edOrg["programs"]  = []
      edOrg["sessions"]  = []
      edOrg["staff"]     = create_staff_for_school()
      edOrg["offerings"] = Hash.new
      edOrg["students"]  = Hash.new
      begin_year         = @scenarioYAML["beginYear"]
      edOrg["students"][begin_year] = assemble_students_into_waves(tag, current_students)
      @edOrgs[tag]       << edOrg
    end
    return school_counter
  end

  # assembles students into waves by iterating through each grade and 'allocating' a section in each grade
  # until the specified number of students are exhausted.
  # -> if a small number of students are specified, it is likely that a single wave will exist
  # -> for larger numbers of students, many waves will be created
  def assemble_students_into_waves(type, num_students)
    if type == "elementary"
      min = @scenarioYAML["MINIMUM_ELEMENTARY_STUDENTS_PER_SECTION"]
      max = @scenarioYAML["MAXIMUM_ELEMENTARY_STUDENTS_PER_SECTION"]
      return get_students_per_grade(GradeLevelType.elementary, num_students, min, max)
    elsif type == "middle"
      min = @scenarioYAML["MINIMUM_MIDDLE_SCHOOL_STUDENTS_PER_SECTION"]
      max = @scenarioYAML["MAXIMUM_MIDDLE_SCHOOL_STUDENTS_PER_SECTION"]
      return get_students_per_grade(GradeLevelType.middle, num_students, min, max)
    elsif type == "high"
      min = @scenarioYAML["MINIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
      max = @scenarioYAML["MAXIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
      return get_students_per_grade(GradeLevelType.high, num_students, min, max)
    end
  end

  # takes the specified grades and uses the total number of students, as well as the minimum and maximum
  # number of students per section, to assemble 'waves' of students by grade
  def get_students_per_grade(grades, num_students, min, max)
    students_per_grade = Hash.new
    grades.each do |grade|
      students_per_grade[grade] = 0
    end
    while num_students > 0
      grades.each do |grade|
        current_students = random_on_interval(min, max)
        if num_students < current_students
          current_students = num_students
        end
        students_per_grade[grade] += current_students
        num_students              -= current_students
      end
    end
    students_per_grade
  end

  # updates the populated edOrgs arrays for elementary, middle, and high schools by determining how many schools are to be
  # contained in a given district (looks at yaml configuration file for average number of schools per district and threshold),
  # and then actually going and updating the schools to reference back to newly created local education agencies
  def update_schools_with_districts(num_schools)
    avg_num_schools_per_district = @scenarioYAML["AVERAGE_NUM_SCHOOLS_PER_DISTRICT"]
    avg_num_schools_threshold    = @scenarioYAML["AVERAGE_SCHOOLS_THRESHOLD"]
    min_num_schools_per_district = avg_num_schools_per_district - (avg_num_schools_per_district * avg_num_schools_threshold)
    max_num_schools_per_district = avg_num_schools_per_district + (avg_num_schools_per_district * avg_num_schools_threshold)
    all_schools      = (1..num_schools).to_a
    district_counter = num_schools
    school_counter   = 0
    while all_schools.size > 0
      district_counter += 1
      num_schools_in_this_district = random_on_interval(min_num_schools_per_district, max_num_schools_per_district).round
      
      if num_schools_in_this_district > (num_schools - school_counter)
        num_schools_in_this_district = (num_schools - school_counter)
      end

      schools_in_this_district = all_schools.pop(num_schools_in_this_district)      
      update_schools_with_district_id(district_counter, schools_in_this_district)
      
      edOrg             = Hash.new
      edOrg["id"]       = district_counter
      edOrg["parent"]   = nil
      #edOrg["programs"] = []
      edOrg["sessions"] = []
      edOrg["staff"]    = create_staff_for_local_education_agency()
      @edOrgs["leas"]   << edOrg
      school_counter    += num_schools_in_this_district
    end
    return district_counter
  end

  # actually does the work to iterate through the edOrgs structure (specifically the arrays
  # contained in the tags: "elementary", "middle", and "high" schools), finding edOrgs whose
  # unique "id" is contained within "schools_in_this_district", and sets the "parent" attribute
  # of those matching edOrgs to the district id.
  def update_schools_with_district_id(district_id, schools_in_this_district)
    # check in elementary schools
    # check in middle schools
    # check in high schools
    @edOrgs["elementary"].each { |edOrg| edOrg["parent"] = district_id if schools_in_this_district.include?(edOrg["id"]) }
    @edOrgs["middle"].each     { |edOrg| edOrg["parent"] = district_id if schools_in_this_district.include?(edOrg["id"]) }
    @edOrgs["high"].each       { |edOrg| edOrg["parent"] = district_id if schools_in_this_district.include?(edOrg["id"]) }
  end

  # updates the populated edOrgs arrays for leas (local education agencies) by determining how many districts are to be
  # contained in a given state. current implementation assumes:
  # - single tier for local education agencies
  # - all local education agencies are contained within a single state
  # 
  # future implementation should create more 'layers' within the local education agency 'tier'
  # future implementation should look at yaml for average number of districts in a state and create multiple 
  #  state education agencies, as needed
  def update_districts_with_states(num_districts)
    state_id          = num_districts + 1
    edOrg             = Hash.new
    edOrg["id"]       = state_id
    edOrg["courses"]  = create_courses()
    edOrg["staff"]    = create_staff_for_state_education_agency()
    #edOrg["programs"] = []
    @edOrgs["seas"]  << edOrg
    
    @edOrgs["leas"].each do |edOrg| 
      edOrg["parent"] = state_id
    end
  end

  # creates staff members at the state education agency level
  def create_staff_for_state_education_agency()
    members = []
    members
  end

  # creates staff members at the local education agency level
  def create_staff_for_local_education_agency()
    members = []
    members
  end  

  # creates staff members at the school level
  def create_staff_for_school()
    members = []
    members
  end

  def build_world_from_edOrgs()
  	num_schools = @scenarioYAML["schoolCount"]
    @log.info "Creating world from initial number of schools: #{num_schools}"
    # NOT CURRENTLY SUPPORTED
    # update structure with time information
    add_time_information_to_edOrgs()
  end

  # creates sessions for each local education agency
  # -> uses 'beginYear' and 'numYears' properties specified in yaml configuration file (defaults to current year and 1, respectively, if not specified)
  # -> iterates from begin year to (begin year + num years), creating Sessions, Grading Periods, and Calendar Dates
  # -> each Session stores a date interval (contains start date, end date, number of instructional days, and holidays for that interval)
  def add_time_information_to_edOrgs()
    begin_year   = @scenarioYAML["beginYear"]
    num_years    = @scenarioYAML["numYears"]
    
    if begin_year.nil?
      this_year = Date.today.year
      @log.info "Property: beginYear --> not set for scenario. Using default: #{this_year}"
      begin_year = this_year
    else
      @log.info "Property: beginYear --> Set in configuration: #{begin_year}"
    end

    if num_years.nil?
      @log.info "Property: numYears --> not set for scenario. Using default: 1"
      num_years = 1
    else
      @log.info "Property: numYears  --> Set in configuration: #{num_years}"
    end

    # loop over years updating infrastructure and population
    @log.info "iterating between #{begin_year} and #{(begin_year + num_years - 1)} to create session information"
    for year in begin_year..(begin_year+num_years-1) do
      # create session for LEAs
      # -> create grading period(s) for session
      # -> create calendar date(s) for grading period
      # -> [not implemented] iterate through schools and determine with some probability if school overrides existing lea session
      @log.info "creating session information for school year: #{year}-#{year+1}"
      @edOrgs["leas"].each_index do |index|
        edOrg = @edOrgs["leas"][index]
        state_organization_id = DataUtility.get_local_education_agency_id(edOrg["id"])
        start_date = DateUtility.random_school_day_on_interval(@prng, Date.new(year, 8, 25), Date.new(year, 9, 10))
        interval   = DateInterval.create_using_start_and_num_days(@prng, start_date, 180)

        session             = Hash.new
        session["term"]     = :YEAR_ROUND
        session["year"]     = year
        session["name"]     = year.to_s + "-" + (year+1).to_s + " " + SchoolTerm.to_string(:YEAR_ROUND) + " session: " + state_organization_id
        session["interval"] = interval
        session["edOrgId"]  = state_organization_id
        @edOrgs["leas"][index]["sessions"] << session
      end
    end

    # update each of the schools already in existence with the sessions that were just created at the local education agency level
    # -> suggest implementing school 'extending' inherited sessions in these functions rather than above
    update_schools_with_sessions("elementary")
    update_schools_with_sessions("middle")
    update_schools_with_sessions("high")
    return begin_year, num_years
  end

  # iterate through schools of matching 'type' and set the sessions to be used
  def update_schools_with_sessions(type)
    @log.info "updating #{type} schools to include inherited sessions"
    @edOrgs[type].each_index do |index|
      school = @edOrgs[type][index]
      @edOrgs[type][index]["sessions"] = get_sessions_to_be_used_by_school(school)
    end
  end

  # uses the breakdown structure (maps each grade => number of students in that grade) to update student enrollment
  # counts at each school over the course of the simulation
  # -> this method will update the @breakdown structure as it walks through years
  # -> this method will embed enrollment information for each school on the school itself
  def expand_student_counts_using_time_information(begin_year, num_years)
    for year in (begin_year + 1)..(begin_year + num_years - 1) do
      # this method operates on @breakdown structure
      shuffle_students_forward

      # update students counts for each school
      update_students_within_school("elementary", year)
      update_students_within_school("middle", year)
      update_students_within_school("high", year)

      # after students have been shuffled...
      # -> update education organizations to include students that graduated and incoming kindergarten students
      assign_incoming_students("elementary", year)
      assign_incoming_students("middle", year)
      assign_incoming_students("high", year)
    end
  end

  # uses the specified education organization 'type' to determine which grade to analyze in the @breakdown instance variable,
  # and if incoming students are present, assigns them across education organizations of specified 'type'
  def assign_incoming_students(type, year)
    if type == "elementary"
      grade = :KINDERGARTEN
      min   = @scenarioYAML["MINIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
      max   = @scenarioYAML["MAXIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
    elsif type == "middle"
      grade = :SIXTH_GRADE
      min = @scenarioYAML["MINIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
      max = @scenarioYAML["MAXIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
    elsif type == "high"
      grade = :NINTH_GRADE
      min = @scenarioYAML["MINIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
      max = @scenarioYAML["MAXIMUM_HIGH_SCHOOL_STUDENTS_PER_SECTION"]
  end
  
    if @breakdown[grade] > 0 && min >= 0 && max >= min
      # actually perform split
      students_to_be_split = @breakdown[grade]
      students_so_far      = 0
      @log.info "year #{year}-#{year+1} --> there are #{students_to_be_split} #{GradeLevelType.get(grade)} students to be split across #{@edOrgs[type].size} schools"

      while students_to_be_split > 0
        @edOrgs[type].each_index do |index|
          num_students_assigned_to_this_school = random_on_interval(min, max)

          if students_to_be_split == 0
            num_students_assigned_to_this_school = 0
          elsif students_to_be_split < num_students_assigned_to_this_school
            num_students_assigned_to_this_school = students_to_be_split
          end
          students_so_far      += num_students_assigned_to_this_school
          students_to_be_split -= num_students_assigned_to_this_school

          @edOrgs[type][index]["students"][year][grade] += num_students_assigned_to_this_school

          if students_to_be_split == 0
            break
          end
        end
      end
    end
  end

  # iterates through education organizations of specified 'type' and ripples student counts forward for the specified 'year'
  def update_students_within_school(type, year)
    @edOrgs[type].each_index do |index|
      school = @edOrgs[type][index]
      students_by_year = school["students"]
      if school["students"][year].nil?
        @edOrgs[type][index]["students"][year] = shuffle_students_forward_at_school(school["students"][year - 1])
      end
    end
  end

  # ripples number of students forward to next year
  # -> allows 1st graders to become 2nd graders, 2nd graders to become 3rd graders, ...
  # -> actually performs work by iterating through grades in reverse order (12 -> 11 -> 10 ...)
  # -> number of 12th graders who graduated becomes number of incoming kindergarteners --> keeps number of students in world consistent
  # -> no current support for students who failed current grade to be held back
  def shuffle_students_forward
    grades          = GradeLevelType.get_ordered_grades.reverse
    kindergarteners = @breakdown[:TWELFTH_GRADE]
    grades.each do |grade|
      if grade != :TWELFTH_GRADE
        @breakdown[GradeLevelType.increment(grade, 1)] = @breakdown[grade]
      end
    end
    @breakdown[:KINDERGARTEN] = kindergarteners
  end

  # ripples students forward to next year at current school
  def shuffle_students_forward_at_school(students_per_grade)
    new_breakdown = Hash.new
    grades        = GradeLevelType.get_ordered_grades.reverse
    grades.each do |grade|
      if students_per_grade.has_key?(grade) and students_per_grade.has_key?(GradeLevelType.increment(grade, 1))
        new_breakdown[GradeLevelType.increment(grade, 1)] = students_per_grade[grade]
        if grade == :KINDERGARTEN or grade == :SIXTH_GRADE or grade == :NINTH_GRADE
          new_breakdown[grade] = 0
        end
      end
    end
    new_breakdown
  end
  
  # create master schedule interchange
  # -> create course offerings
  # -> sections will be created later
  def create_master_schedule()
    # creating course offerings does not require any randomization --> only logic
    # -> computes intersection of courses to be offered at school with school's sessions
    offering_id = 0
    offering_id = create_course_offerings_for_school("elementary", offering_id)
    offering_id = create_course_offerings_for_school("middle", offering_id)
    offering_id = create_course_offerings_for_school("high", offering_id)
  end

  # iterate through schools of 'type' (should be "elementary", "middle", or "high" schools)
  # -> create course offerings by
  # --> getting set of sessions to be used
  # --> getting set of courses  to be used
  # -> iterate through sessions
  # -> iterate through courses
  # --> use {session, course} pair to create course offering at current school
  def create_course_offerings_for_school(type, num_offerings)
    offering_id = num_offerings
    @edOrgs[type].each_index do |index|
      school    = @edOrgs[type][index]
      sessions  = get_sessions_to_be_used_by_school(school)
      courses   = get_courses_to_be_used_by_school(type, school)
      sessions.each do |session|
        offerings    = []
        current_year = session["year"]
        courses.each do |grade, current_courses| 
          current_courses.each do |course|
            # increment course offering unique id
            offering_id                += 1
            # create map for session information
            local_session              = Hash.new
            local_session["name"]      = session["name"]
            local_session["ed_org_id"] = session["edOrgId"]
            # create course offering container
            offering = Hash.new
            offering["id"]        = offering_id
            if type == "elementary"
              offering["ed_org_id"] = DataUtility.get_elementary_school_id(school["id"])
            elsif type == "middle"
              offering["ed_org_id"] = DataUtility.get_middle_school_id(school["id"])
            elsif type == "high"
              offering["ed_org_id"] = DataUtility.get_high_school_id(school["id"])
            end            
            offering["session"]   = local_session
            offering["course"]    = course
            offering["grade"]     = grade
            
            # add course offering into 'offerings' (course offerings for current year)
            offerings << offering
          end
        end
        @edOrgs[type][index]["offerings"][current_year] = offerings
      end
    end
    offering_id
  end

  # gets the sequence of sessions to be used by the school when creating course offerings
  # -> if school has its own sessions, use those
  # -> if not, traverse up leas (and eventually seas) hierarchy until sessions are found
  # -> traverses until sessions are found or sea is reached (will not have a parent so while loop will be escaped)
  # -> if no education organization in the school's lineage has published sessions, an empty array will be returned
  def get_sessions_to_be_used_by_school(school)
    sessions = []
    if school["sessions"].nil? or school["sessions"].size == 0
      parent = get_parent_for_school(school)
      while !parent.nil? and sessions.size == 0
        if !parent["sessions"].nil? and parent["sessions"].size > 0
          sessions = parent["sessions"]
        else
          parent = get_parent_for_local_education_agency(parent)
        end
      end
    else
      # school has published its own sessions --> use those
      sessions = school["sessions"]
      # please note: right now, sessions of LEAs are stored on schools
      # -> there is no way to distinguish between a session published by the LEA or a school that is using its own
      # -> this will be mitigated in the future by writing sessions as they come into existence (in add_time method)
      # -> for now, school["sessions"] is used, but this does not imply that the school has actually published its own sessions
    end
    sessions
  end

  # finds the direct parent (local education agency) for a school
  # -> looks in @edOrgs structure under "leas"
  def get_parent_for_school(school)
    parent_id = school["parent"]
    parent    = @edOrgs["leas"].detect {|lea| lea["id"] == parent_id}
    parent
  end

  # finds the parent (local education agency or state education agency) for a local education agency
  # -> looks in @edOrgs structure under "leas"
  # -> looks in @edOrgs structure under "seas" (if no LEA parent was found)
  # this is done because local education agencies can nest a potentially unlimited number of times
  def get_parent_for_local_education_agency(local_education_agency)
    parent_id = local_education_agency["parent"]
    parent    = @edOrgs["leas"].detect {|lea| lea["id"] == parent_id}
    # if parent is nil --> no parent found under "leas"
    parent    = @edOrgs["seas"].detect {|sea| sea["id"] == parent_id} if parent.nil?
    parent
  end

  # gets the set of courses to be used by the school when creating course offerings
  # -> traverses up to state education agency to find course catalog
  # -> future implementations should traverse until a set of courses is found (allow districts to extend state-level course catalog)
  def get_courses_to_be_used_by_school(type, school)
    # get school's immediate parent
    # traverse up local education agencies until no parent can be found
    # check in state education agencies for parent that failed to be in leas
    # if found, return state education agency
    parent_id = school["parent"]
    parent    = @edOrgs["leas"].detect {|lea| lea["id"] == parent_id}
    while parent != nil
      parent_id = parent["parent"]
      parent    = @edOrgs["leas"].detect {|lea| lea["id"] == parent_id}
    end
    # parent_id should now be populated with id of the state education agency
    state_education_agency = @edOrgs["seas"].detect {|sea| sea["id"] == parent_id}

    courses = Hash.new
    if type == "elementary"
      GradeLevelType.elementary.each do |grade|
        # get the current set of courses that the state education agency has published
        # add state education agency id to courses --> makes life easier when creating course offering
        current_courses              = state_education_agency["courses"][grade]
        current_courses.each { |element| element["ed_org_id"] = DataUtility.get_state_education_agency_id(state_education_agency["id"]) }
        courses[grade]               = current_courses
      end
    elsif type == "middle"
      GradeLevelType.middle.each do |grade|
        # get the current set of courses that the state education agency has published
        # add state education agency id to courses --> makes life easier when creating course offering
        current_courses              = state_education_agency["courses"][grade]
        current_courses.each { |element| element["ed_org_id"] = DataUtility.get_state_education_agency_id(state_education_agency["id"]) }
        courses[grade]               = current_courses
      end
    elsif type == "high"
      GradeLevelType.high.each do |grade|
        # get the current set of courses that the state education agency has published
        # add state education agency id to courses --> makes life easier when creating course offering
        current_courses              = state_education_agency["courses"][grade]
        current_courses.each { |element| element["ed_org_id"] = DataUtility.get_state_education_agency_id(state_education_agency["id"]) }
        courses[grade]               = current_courses
      end
    end
    courses
  end

  def initialize_interchanges()
    @education_organization_writer = EducationOrganizationGenerator.new(@scenarioYAML, File.new("generated/InterchangeEducationOrganization.xml", 'w'))

    @education_org_calendar_writer = EducationOrgCalendarGenerator.new(@scenarioYAML, File.new("generated/InterchangeEducationOrgCalendar.xml", 'w'))
    @master_schedule_writer        = MasterScheduleGenerator.new(@scenarioYAML, File.new("generated/InterchangeMasterSchedule.xml", 'w'))

    @education_organization_writer.start
    @education_org_calendar_writer.start
    @master_schedule_writer.start
  end

  # writes ed-fi xml interchanges
  def write_interchanges
    write_education_organization_interchange()
    write_education_org_calendar_interchange()
    write_master_schedule_interchange()
  end

  # close all file handles used for writing ed-fi xml interchanges
  def finalize_interchanges
    @education_organization_writer.finalize
    @education_org_calendar_writer.finalize
    @master_schedule_writer.finalize
  end

  # writes ed-fi xml interchange: education organization
  # entities:
  # - StateEducationAgency
  # - LocalEducationAgency
  # - School [Elementary, Middle, High]
  # - Course
  # - [not yet implemented] Program
  def write_education_organization_interchange()
    # write state education agencies
    @edOrgs["seas"].each do |edOrg|
      @education_organization_writer.create_state_education_agency(@prng, edOrg["id"])
      write_courses(DataUtility.get_state_education_agency_id(edOrg["id"]), edOrg["courses"])
    end
    # write local education agencies
    @edOrgs["leas"].each do |edOrg|
      @education_organization_writer.create_local_education_agency(@prng, edOrg["id"], edOrg["parent"])
    end
    # write schools
    @edOrgs["elementary"].each do |edOrg|
      @education_organization_writer.create_school(@prng, edOrg["id"], edOrg["parent"], "elementary")
    end
    @edOrgs["middle"].each do |edOrg|
      @education_organization_writer.create_school(@prng, edOrg["id"], edOrg["parent"], "middle")
    end
    @edOrgs["high"].each do |edOrg|
      @education_organization_writer.create_school(@prng, edOrg["id"], edOrg["parent"], "high")
    end
  end

  # writes ed-fi xml interchange: education organization calendar
  # entities:
  # - Session
  # - GradingPeriod
  # - CalendarDate
  def write_education_org_calendar_interchange
    # write sessions at the district level
    @edOrgs["leas"].each do |ed_org|
      # get sessions currently stored on local education agency
      # iterate through sessions
      sessions  = ed_org["sessions"]
      ed_org_id = DataUtility.get_local_education_agency_id(ed_org["id"])
      sessions.each do |session|
        interval  = session["interval"]
        year      = session["year"]

        # create calendar date(s) using interval
        # create grading period(s) using interval, school year, and state organization id
        calendar_dates  = create_calendar_dates(interval)        
        grading_periods = create_grading_periods(interval, year, ed_org_id)

        # create and write session
        name      = session["name"]
        term      = session["term"]
        @education_org_calendar_writer.create_session(name, year, term, interval, ed_org_id, grading_periods)

        # write grading period(s)
        grading_periods.each do |grading_period|
          type      = grading_period["type"]
          year      = grading_period["year"]
          interval  = grading_period["interval"]
          ed_org_id = grading_period["ed_org_id"]
          @education_org_calendar_writer.create_grading_period(type, year, interval, ed_org_id, calendar_dates)
        end

        # write calendar date(s)
        calendar_dates.each do |calendar_date|
          @education_org_calendar_writer.create_calendar_date(calendar_date["date"], calendar_date["event"])
        end
      end
    end
  end

  # writes ed-fi xml interchange: master schedule
  # entities:
  # - CourseOffering
  # - [not done] Section
  def write_master_schedule_interchange
    write_course_offerings("elementary")
    write_course_offerings("middle")
    write_course_offerings("high")
  end

  # writes the course offerings for each 'type' of school ("elementary", "middle", or "high" school)
  # -> iterates through schools of 'type'
  # -> if the school has course offerings
  # --> assembles course offering and passes to master schedule interchange writer
  def write_course_offerings(type)
    @edOrgs[type].each do |school|
      if !school["offerings"].nil? and school["offerings"].size > 0
        school["offerings"].each do |year, course_offerings|
          course_offerings.each do |course_offering|
            id        = course_offering["id"]
            ed_org_id = course_offering["ed_org_id"]
            session   = course_offering["session"]
            course    = course_offering["course"]
            grade     = course_offering["grade"]
          
            if GradeLevelType.is_elementary_school_grade(grade)
              title   = GradeLevelType.get(grade)
            else
              title   = GradeLevelType.get(grade) + " " + course["title"]
            end
          
            @master_schedule_writer.create_course_offering(id, title, ed_org_id, session, course)
          end
        end
      end
    end
  end

  # creates an array of calendar dates that represent the specified interval
  # interval specifies:
  # - start date
  # - end date
  # - holidays
  # - num of instructional days (not used)
  def create_calendar_dates(interval)
    calendar_dates = []
    begin_date     = interval.get_begin_date
    end_date       = interval.get_end_date
    holidays       = interval.get_holidays
    (begin_date..end_date).step(1) do |date|
      if holidays.include?(date)
        calendar_date          = Hash.new
        calendar_date["date"]  = date
        calendar_date["event"] = :HOLIDAY
        calendar_dates << calendar_date
      else
        if date.wday != 0 and date.wday != 6
          calendar_date          = Hash.new
          calendar_date["date"]  = date
          calendar_date["event"] = :INSTRUCTIONAL_DAY
          calendar_dates << calendar_date
        end
      end
    end
    calendar_dates
  end

  # creates an array of grading periods that occur over the specified interval
  # -> currently, only a single grading period is generated (for whole session)
  # -> future implementations should take an additional variable that specifies breakdown into multiple grading periods
  def create_grading_periods(interval, year, ed_org_id)
    grading_periods              = []
    grading_period               = Hash.new
    grading_period["type"]       = :END_OF_YEAR
    grading_period["year"]       = year
    grading_period["interval"]   = interval
    grading_period["ed_org_id"]  = ed_org_id
    grading_periods              << grading_period
    grading_periods
  end

  # creates courses at the state education agency by populating a 'course catalog'
  # initially assumes a very simple course model
  # -> each grade contains Science, Math, English, and History
  # -> no honors or multiple course paths
  def create_courses
    courses = Hash.new
    course_counter = 0
    GradeLevelType::get_ordered_grades.each do |grade|
      current_grade_courses = Array.new
      if !@scenarioYAML[grade.to_s + "_COURSES"].nil?
        @scenarioYAML[grade.to_s + "_COURSES"].each do |course|
          current_grade_course = Hash.new
          course_counter += 1
          current_grade_course["id"] = course_counter
          current_grade_course["title"] = course
          current_grade_courses << current_grade_course
        end
      else
        current_grade_course = Hash.new
        course_counter += 1
        current_grade_course["id"] = course_counter
        current_grade_course["title"] = GradeLevelType.get(grade)
        current_grade_courses << current_grade_course
      end
      courses[grade] = current_grade_courses
    end
    courses
  end

  # writes the courses at the state education agency to the education organization interchange
  def write_courses(edOrgId, courses)
    courses.each do |key, value|
      grade = GradeLevelType.get(key)
      value.each do |course|
        id = DataUtility.get_course_unique_id(course["id"])
        if GradeLevelType.is_elementary_school_grade(key)
          title = grade
        else
          title = grade + " " + course["title"]
        end
        @education_organization_writer.create_course(@prng, id, title, edOrgId)
      end
    end
  end

  # computes a random number on the interval [min, max]
  # does NOT round
  def random_on_interval(min, max)
    min + @prng.rand(max - min)
  end

  def self.choose_feeders(elem, mid, high)
    hs_cycle = high.cycle
    mid.each{|school|
      hs = hs_cycle.next
      school['feeds_to'] = [hs['id']]
    }
    mid_cycle = mid.cycle
    elem.each{|school|
      ms = mid_cycle.next
      school['feeds_to'] = [ms['id'], ms['feeds_to'][0]]
    }
  end
end
