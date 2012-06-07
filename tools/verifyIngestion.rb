require 'mongo'
require 'json'

#Talk to maestro's mongos
mongohost = 'nxmaestro.slidev.org'

expected={"900K" => {
  "assessment"=>4,
  "attendance"=>15700,
  "calendarDate"=>0,
  "cohort"=>0,
  "course"=>25,
  "courseOffering"=>0,
  "courseSectionAssociation"=>0,
  "disciplineAction"=>0,
  "disciplineIncident"=>0,
  "educationOrganization"=>27,
  "educationOrganizationAssociation"=>0,
  "educationOrganizationSchoolAssociation"=>0,
  "gradebookEntry"=>0,
  "gradingPeriod"=>0,
  "learningObjective"=>0,
  "parent"=>0,
  "program"=>0,
  "school"=>0,
  "schoolSessionAssociation"=>25,
  "section"=>3125,
  "sectionAssessmentAssociation"=>0,
  "sectionSchoolAssociation"=>0,
  "session"=>25,
  "sessionCourseAssociation"=>0,
  "staff"=>902,
  "staffCohortAssociation"=>0,
  "staffEducationOrganizationAssociation"=>2,
  "staffProgramAssociation"=>0,
  "student"=>15700,
  "studentAcademicRecord"=>0,
  "studentAssessmentAssociation"=>235500,
  "studentCohortAssociation"=>0,
  "studentDisciplineIncidentAssociation"=>0,
  "studentParentAssociation"=>0,
  "studentProgramAssociation"=>0,
  "studentSchoolAssociation"=>15700,
  "studentSectionAssociation"=>314000,
  "studentSectionGradebookEntry"=>0,
  "studentTranscriptAssociation"=>0,
  "teacher"=>0,
  "teacherSchoolAssociation"=>900,
  "teacherSectionAssociation"=>3125
  },
    
  "8M" => {
  "assessment"=>163,
  "attendance"=>30544,#3420928
  "calendarDate"=>184,
  "cohort"=>92,
  "course"=>100,
  "courseOffering"=>0,
  "courseSectionAssociation"=>0,
  "disciplineAction"=>44022,
  "disciplineIncident"=>44022,
  "educationOrganization"=>48,
  "educationOrganizationAssociation"=>135,
  "educationOrganizationSchoolAssociation"=>0,
  "gradebookEntry"=>0,
  "gradingPeriod"=>736,
  "learningObjective"=>393,
  "parent"=>45904,
  "program"=>47,
  "school"=>46,
  "schoolSessionAssociation"=>0, 
  "section"=>30912,
  "sectionAssessmentAssociation"=>0,
  "sectionSchoolAssociation"=>0,
  "session"=>92,
  "sessionCourseAssociation"=>0,
  "staff"=>135,
  "staffCohortAssociation"=>92,
  "staffEducationOrganizationAssociation"=>135,
  "staffProgramAssociation"=>47,
  "student"=>30544,
  "studentAcademicRecord"=>0,
  "studentAssessmentAssociation"=>305440,
  "studentCohortAssociation"=>32983,
  "studentDisciplineIncidentAssociation"=>44022,
  "studentParentAssociation"=>45904,
  "studentProgramAssociation"=>26267,
  "studentSchoolAssociation"=>30544,
  "studentSectionAssociation"=>427616,
  "studentSectionGradebookEntry"=>0,
  "studentTranscriptAssociation"=>0,
  "teacher"=>2484,
  "teacherSchoolAssociation"=>2484,
  "teacherSectionAssociation"=>30912
  }
}

# Print total counts
expected.each do |set,collections|
  total=0
  collections.each do |name,count|
    total+=count
  end
  printf "%s %d\n",set,total
end

# Check that user specified the set
if ARGV.count() < 1
  puts "must specify the type of batch"
  exit
end


if !expected.has_key?(ARGV[0])
  puts "Unsupported batch \e[31m#{ARGV[0]}\e[0m"
  puts "Supported: "
  expected.each_key {|key| puts key}
  exit
end

connection = Mongo::Connection.new( mongohost, 27017)
db = connection.db("sli")

printf "\e[35m%-40s %s\n","Collection","Expected(Actual)\e[0m"
puts "---------------------------------------------------------"

expected[ARGV[0]].each do |collectionName,expectedCount|
  coll = db.collection(collectionName)

  count = coll.count()

  color="\e[32m"
  if expectedCount != count
    color="\e[31m"
  end
  printf "#{color}%-40s %d(%d)\e[0m\n",collectionName,count,expectedCount

end

puts "ALL DONE"
