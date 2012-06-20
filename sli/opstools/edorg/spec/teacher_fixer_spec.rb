require File.dirname(__FILE__) + '/../teacher_fixer'

describe TeacherFixer do
  def insert_to_collection(collection, hash)
    new_hash = {:body => hash}
    @db[collection].insert(new_hash)
  end
  before(:all) do
    conn = Mongo::Connection.new('localhost', 27017)
    @db = conn['test']
    Object.send(:db, db)
  end
  describe "#fixing_students" do
    after(:each) do
      @db['student'].remove
      @db['staff'].remove
      @db['teacherSectionAssociation'].remove
      @db['studentSectionAssociation'].remove
    end
    it "should stamp students with section associations" do
      teacher_id = insert_to_collection @db['staff'], {:name => "Teacher"}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        section_id = insert_to_collection @db['section'], {:name => "Section #{id}"}
        insert_to_collection @db['teacherSectionAssociation'], {:sectionId => section_id, :teacherId => teacher_id}
        insert_to_collection @db['studentSectionAssociation'], {:studentId => id, :sectionId => section_id}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should include 'teacherId'
        student['metaData']['teacherId'].should eql teacher_id
      end
    end
    it "should stamp students with program assocations" do
      teacher_id = insert_to_collection @db['staff'], {:name => "Teacher"}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        program_id = insert_to_collection @db['program'], {:name => "program #{id}"}
        insert_to_collection @db['staffProgramAssociation'], {:programId => [program_id], :staffId => [teacher_id]}
        insert_to_collection @db['studentProgramAssociation'], {:programId => [program_id], :studentId => [teacher_id]}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should include 'teacherId'
        student['metaData']['teacherId'].should eql teacher_id
      end
    end
    it "should should stamp students with cohort associations" do
      teacher_id = insert_to_collection @db['staff'], {:body =>{:name => "Teacher"}}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        program_id = insert_to_collection @db['cohort'], {:name => "cohort #{id}"}
        insert_to_collection @db['staffCohortAssociation'], {:cohortId => [program_id], :staffId => [teacher_id]}
        insert_to_collection @db['studentCohortAssociation'], {:cohortId => [program_id], :studentId => [teacher_id]}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should include 'teacherId'
        student['metaData']['teacherId'].should eql teacher_id
      end
    end
    it "should respect end dates for student section associations" do
      teacher_id = insert_to_collection @db['staff'], {:name => "Teacher"}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        section_id = insert_to_collection @db['section'], {:name => "Section #{id}"}
        insert_to_collection @db['teacherSectionAssociation'], {:sectionId => section_id, :teacherId => teacher_id}
        insert_to_collection @db['studentSectionAssociation'], {:studentId => id, :sectionId => section_id, :endDate => Date.today - 2001}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should_not include 'teacherId'
        student['metaData']['teacherId'].should_not eql teacher_id
      end
    end
    it "should respect end dates for teacher section associations" do
      teacher_id = insert_to_collection @db['staff'], {:name => "Teacher"}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        section_id = insert_to_collection @db['section'], {:name => "Section #{id}"}
        insert_to_collection @db['teacherSectionAssociation'], {:sectionId => section_id, :teacherId => teacher_id, :endDate => Date.today - 2001}
        insert_to_collection @db['studentSectionAssociation'], {:studentId => id, :sectionId => section_id}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should_not include 'teacherId'
        student['metaData']['teacherId'].should_not eql teacher_id
      end
    end
    it "should respect end dates for student program associations" do
      teacher_id = insert_to_collection @db['staff'], {:name => "Teacher"}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        program_id = insert_to_collection @db['program'], {:name => "program #{id}"}
        insert_to_collection @db['staffProgramAssociation'], {:programId => [program_id], :staffId => [teacher_id]}
        insert_to_collection @db['studentProgramAssociation'], {:programId => [program_id], :studentId => [teacher_id], :endDate => Date.today - 2001}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should_not include 'teacherId'
        student['metaData']['teacherId'].should_not eql teacher_id
      end
    end
    it "should respect end dates for staff program associations" do
      teacher_id = insert_to_collection @db['staff'], {:name => "Teacher"}
      5.times do |i|
        student_id = insert_to_collection @db['student'], {:name => "Student #{i}"}
        program_id = insert_to_collection @db['program'], {:name => "program #{id}"}
        insert_to_collection @db['staffProgramAssociation'], {:programId => [program_id], :staffId => [teacher_id], :endDate => Date.today - 2001}
        insert_to_collection @db['studentProgramAssociation'], {:programId => [program_id], :studentId => [teacher_id]}
      end
      Object.send(:fix_students)
      
      @db['student'].find.each do |student| 
        student.should include 'metaData'
        student['metaData'].should_not include 'teacherId'
        student['metaData']['teacherId'].should_not eql teacher_id
      end
    end
  end
end
