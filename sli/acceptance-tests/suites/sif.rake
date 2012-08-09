############################################################
# SIF tests start
############################################################

desc "Import SIF Sandbox Test Data"
task :importSifSandboxData do
  Rake::Task["importSifBootstrapData"].execute
  setFixture("educationOrganization", "sif/sif_lea_fixture.json", "test/data", false)
  setFixture("educationOrganization", "sif/sif_educationOrganization_fixture.json", "test/data", false)
  setFixture("studentSchoolAssociation", "sif/sif_studentSchoolAssociation_fixture.json", "test/data", true)
  setFixture("student", "sif/sif_student_fixture.json", "test/data", true)
end

desc "Import SIF Bootstrap Test Data"
task :importSifBootstrapData do
  testHash = Hash[
    "staff" => "sif/sif_bootstrap_staff_fixture.json",
    "educationOrganization" => "sif/sif_bootstrap_educationOrganization_fixture.json",
    "staffEducationOrganizationAssociation" => "sif/sif_bootstrap_staffEducationOrganizationAssociation_fixture.json"
  ]
  setMultipleFixtureFiles(testHash)
end

desc "Run SIF Integrated Tests"
task :sifIntegratedTest => [:realmInit] do
  Rake::Task["importSifBootstrapData"].execute
  runTests("test/features/sif/features/sif_integrated.feature")
end

desc "Run SIF SchoolInfo Tests"
task :sifSchoolInfoTest => [:realmInit] do
  Rake::Task["importSifBootstrapData"].execute
  runTests("test/features/sif/features/sif_SchoolInfo.feature")
end

desc "Run SIF LEAInfo Tests"
task :sifLEAInfoTest => [:realmInit] do
  Rake::Task["importSifBootstrapData"].execute
  runTests("test/features/sif/features/sif_LEAInfo.feature")
end

desc "Run SIF SEAInfo Tests"
task :sifSEAInfoTest => [:realmInit] do
  Rake::Task["importSifBootstrapData"].execute
  runTests("test/features/sif/features/sif_SEAInfo.feature")
end

desc "Run SIF StudentSchoolEnrollment Tests"
task :sifStudentSchoolEnrollmentTest => [:realmInit] do
  Rake::Task["importSifBootstrapData"].execute
  runTests("test/features/sif/features/sif_StudentSchoolEnrollment.feature")
end

desc "Run SIF StudentLEARelationship Tests"
task :sifStudentLEARelationshipTest => [:realmInit] do
  Rake::Task["importSifBootstrapData"].execute
  runTests("test/features/sif/features/sif_StudentLEARelationship.feature")
end

############################################################
# SIF tests end
############################################################

