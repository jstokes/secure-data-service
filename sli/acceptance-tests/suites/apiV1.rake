############################################################
# API V1 tests start
############################################################
task :apiV1EntityTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/entities/crud")
end

task :apiV1AssociationTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/crud/assoc_crud.feature")
  runTests("test/features/apiV1/associations/links/assoc_links.feature")
end

desc "Run V1 check for duplicate links"
task :apiV1DuplicateLinkTest => [:realmInit] do
  # Import the data once, none of these tests edit the data
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/entities/Links/duplicate_link_test.feature")
end

task :apiV1QueryingTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/querying/querying.feature")
end

desc "Run V1 XML Tests"
task :v1XMLTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/xml/xml.feature")
end

desc "Run V1 Program Security Tests"
task :v1ProgramSecurityTests => [:realmInit] do
  runTests("test/features/apiV1/entities/program/program_security.feature")
end

desc "Run V1 Discipline Incident Security Tests"
task :v1DisciplineIncidentSecurityTests => [:realmInit] do
  runTests("test/features/apiV1/entities/disciplineIncident/disciplineIncident_security.feature")
end

desc "Run V1 Staff Secuity Tests"
task :v1StaffSecurityTests => [:realmInit] do
  runTests("test/features/security/staff_security.feature")
end

desc "Run V1 Discipline Action Secuity Tests"
task :v1DisciplineActionSecurityTests => [:realmInit] do
  runTests("test/features/apiV1/entities/disciplineAction/disciplineAction_security.feature")
end

desc "Run V1 Parent Security Tests"
task :v1ParentSecurityTests => [:realmInit] do
  runTests("test/features/apiV1/entities/parent/parent_security.feature")
end

desc "Run V1 Attendance Security Tests"
task :v1AttendanceSecurityTests => [:realmInit] do
  runTests("test/features/apiV1/entities/attendance/attendance_security.feature")
end

desc "Run V1 Cohort Security Tests"
task :v1CohortSecurityTests => [:realmInit] do
  runTests("test/features/apiV1/entities/cohort/cohort_security.feature")
end

desc "Run V1 Student Discipline Incident Association Tests"
task :v1StudentDisciplineIncidentAssociationTests => [:realmInit] do
  setFixture("student", "student_fixture.json")
  setFixture("disciplineIncident", "disciplineIncident_fixture.json")
  setFixture("studentDisciplineIncidentAssociation", "studentDisciplineIncidentAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/studentDisciplineIncidentAssociation")
end

desc "Run V1 Cascade Deletion Tests"
task :v1CascadeDeletionTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/cascadeDeletion")
end

desc "Run V1 Direct References Tests"
task :v1DirectReferencesTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferences")
end

desc "Run V1 Direct Reference Collections Tests"
task :v1DirectReferenceCollectionsTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferenceCollections")
end

desc "Run V1 Student Parent Association Tests"
task :v1StudentParentAssociationTests => [:realmInit] do
  setFixture("student", "student_fixture.json")
  setFixture("parent", "parent_fixture.json")
  setFixture("studentParentAssociation", "studentParentAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/studentParentAssociation")
end

desc "Run V1 Student Transcript Association Tests"
task :v1StudentTranscriptAssociationTests => [:realmInit] do
  setFixture("student", "student_fixture.json")
  setFixture("course", "course_fixture.json")
  setFixture("studentTranscriptAssociation", "studentTranscriptAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/studentTranscriptAssociation")
end

desc "Run V1 Teacher School Association Tests"
task :v1TeacherSchoolAssociationTests => [:realmInit] do
  setFixture("staff", "staff_fixture.json")
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("teacherSchoolAssociation", "teacherSchoolAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/teacherSchoolAssociation")
end

desc "Run V1 Teacher Section Association Tests"
task :v1TeacherSectionAssociationTests => [:realmInit] do
  setFixture("staff", "staff_fixture.json")
  setFixture("section", "section_fixture.json")
  setFixture("teacherSectionAssociation", "teacherSectionAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/teacherSectionAssociation")
end

desc "Run V1 Student Assessment Association Tests"
task :v1StudentAssessmentAssociationTests => [:realmInit] do
  #drop data, re add fixture data
  setFixture("student", "student_fixture.json")
  setFixture("assessment", "assessment_fixture.json")
  setFixture("studentAssessmentAssociation", "studentAssessmentAssociation_fixture.json")
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/studentAssessmentAssociation")
end

desc "Run V1 Student Section Association Tests"
task :v1StudentSectionAssociationTests => [:realmInit] do
  #drop data, re add fixture data
  setFixture("student", "student_fixture.json")
  setFixture("section", "section_fixture.json")
  setFixture("studentSectionAssociation", "studentSectionAssociation_fixture.json")
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/studentSectionAssociation")
end

desc "Run V1 Staff Education Organization Association Tests"
task :v1StaffEdOrgAssociationTests => [:realmInit] do
  setFixture("staff", "staff_fixture.json")
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("staffEducationOrganizationAssociation", "staffEducationOrganizationAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/staffEducationOrganizationAssociation")
end

desc "Run V1 Staff Program Association Tests"
task :v1StaffProgramAssociationTests => [:realmInit] do
  setFixture("staff", "staff_fixture.json")
  setFixture("program", "program_fixture.json")
  setFixture("staffProgramAssociation", "staffProgramAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/staffProgramAssociation")
end

desc "Run V1 Student Program Association Tests"
task :v1StudentProgramAssociationTests => [:realmInit] do
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("student", "student_fixture.json")
  setFixture("program", "program_fixture.json")
  setFixture("staffProgramAssociation", "staffProgramAssociation_fixture.json")
  setFixture("studentProgramAssociation", "studentProgramAssociation_fixture.json")
  runTests("test/features/apiV1/associations/legacy_tests/studentProgramAssociation")
end

desc "Run V1 School Session Association Tests"
task :v1SchoolSessionAssociationTests => [:realmInit] do
  #drop data, re add fixture data
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("session", "session_fixture.json")
  setFixture("schoolSessionAssociation", "schoolSessionAssociation_fixture.json")
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/schoolSessionAssociation")
end

desc "Run V1 Student School Association Tests"
task :v1StudentSchoolAssociationTests => [:realmInit] do
  #drop data, re add fixture data
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("student", "student_fixture.json")
  setFixture("studentSchoolAssociation", "studentSchoolAssociation_fixture.json")
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/studentSchoolAssociation")
end

desc "Run V1 Course Offering Tests"
task :v1CourseOfferingTests => [:realmInit] do
  #drop data, re add fixture data
  setFixture("session", "session_fixture.json")
  setFixture("course", "course_fixture.json")
  setFixture("courseOffering", "sessionCourseAssociation_fixture.json")
  setFixture("section", "section_fixture.json")
  setFixture("studentSectionAssociation", "studentSectionAssociation_fixture.json")
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/courseOffering")
end

desc "Run V1 Staff Cohort Association Tests"
task :v1StaffCohortAssociationTests => [:realmInit] do
  #drop data, re add fixture data
  setFixture("staff", "staff_fixture.json")
  setFixture("cohort", "cohort_fixture.json")
  setFixture("staffCohortAssociation", "staffCohortAssociation_fixture.json")
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/staffCohortAssociation")
end

desc "Run V1 Student Cohort Association Tests"
task :v1StudentCohortAssociationTests => [:realmInit] do
  #drop data, re add fixture data
  Rake::Task["importSandboxData"].execute
  #run test
  runTests("test/features/apiV1/associations/legacy_tests/studentCohortAssociation")
end

desc "Run V1 Common Core Standards reference traversal Tests"
task :v1CCSTests => [:realmInit] do
  setFixture("learningStandard", "learningStandard_fixture.json")
  runFixtureAndTests("test/features/apiV1/end_user_stories/commonCoreStandards/API/api_ccs.feature","learningObjective","learningObjective_fixture.json")
end

desc "Run V1 Home URI Tests"
task :v1homeUriTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/home_uri")
end

desc "Run User Admin CRUD Tests"
task :userAdminCrudTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/user_admin")
end

desc "Run V1 Hierachy Traversal Tests"
task :v1HierarchyTraversalTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/hierarchyTraversal")
end

desc "Run V1 Validation Tests"
task :v1ValidationTests => [:realmInit] do
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("staff", "staff_fixture.json")
  setFixture("student", "student_fixture.json")
  setFixture("section", "section_fixture.json")
  setFixture("studentSectionAssociation", "studentSectionAssociation_fixture.json")
  setFixture("teacherSectionAssociation", "teacherSectionAssociation_fixture.json")
  runTests("test/features/apiV1/validation/validation.feature")
end

desc "Run V1 White List Validation Tests"
task :v1WhiteListValidationTests => [:realmInit] do
  setFixture("educationOrganization", "educationOrganization_fixture.json")
  setFixture("staff", "staff_fixture.json")
  setFixture("student", "student_fixture.json")
  runTests("test/features/apiV1/validation/whitelist_validation.feature")
end

desc "Run Sorting and Paging Tests"
task :v1SortingAndPagingTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/sorting_paging")
end

desc "Run Encryption Tests"
task :v1EncryptionTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/encryption")
end

desc "Run Target Tests"
task :v1TargetTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/targets")
end

desc "Run List Tests"
task :v1ListTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/lists")
end

desc "Run V1 Assessment User Story Tests"
task :v1EndUserStoryAssessmentTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/assessments/assessment.feature")
end

desc "Run V1 Custom entity User Story Tests"
task :v1EndUserStoryCustomEntityTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/CustomEntities/CustomEntities.feature")
end

desc "Run V1 Student Optional Fields Tests"
task :v1StudentOptionalFieldsTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/optional_fields/student_optional_fields.feature")
end

desc "Run V1 Single Student View Tests"
task :v1SingleStudentViewTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/optional_fields/single_student_view.feature")
end

desc "Run V1 Blacklist/Whitelist input Tests"
task :v1BlacklistValidationTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/blacklistValidation/blacklistValidation.feature")
end

desc "Run V1 SecurityEvent Tests"
task :v1SecurityEventTests => [:realmInit] do
  setFixture("securityEvent", "securityEvent_fixture.json")
  runTests("test/features/apiV1/securityEvent/securityEvent.feature")
end

desc "Run V1 Comma-Separated List Order Tests"
task :v1CommaSeparatedListOrderTests => [:realmInit] do
  setFixture("student", "student_fixture.json")
  runTests("test/features/apiV1/comma_separated_list/comma_separated_list_ordering.feature")
end

desc "Run API Smoke Tests"
task :apiSmokeTests do
  @tags = ["~@wip", "@smoke", "~@sandbox"]
  Rake::Task["apiV1EntityTests"].invoke
  Rake::Task["apiV1AssociationTests"].invoke
  Rake::Task["securityTests"].invoke
  Rake::Task["apiMegaTests"].invoke
end

desc "Run API Performance Tests"
task :apiPerformanceTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/performance/performance.feature")
end

############################################################
# API V1 tests end
############################################################

############################################################
# Security tests start
############################################################
desc "Run Security Tests"
task :securityTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/security")
end

desc "Run Security MegaTest"
task :apiMegaTests => [:realmInit, :importSecuredData] do
    runTests("test/features/apiV1/entities/student_security")
end
############################################################
# Security tests end
############################################################
