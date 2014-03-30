############################################################
# API V1 tests start
############################################################

desc "Run API V1 Yearly Transcript Tests"
task :apiV1YearlyTranscriptTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/container_doc/yearly_transcript.feature")
end

desc "Run API V1 Granular Access Tests"
task :apiV1GranularAccessTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/granular_access")
end

task :apiVersionTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/api_versions/apiVersions.feature")
end

task :longLivedSessionTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/long_lived_session")
end

task :apiV1EntityTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/entities/crud/attendance_events_crud.feature")
  runTests("test/features/apiV1/entities/crud/class_period_crud.feature")
  runTests("test/features/apiV1/entities/crud/bell_schedule_crud.feature")
  runTests("test/features/apiV1/entities/crud/admin_security.feature")
  runTests("test/features/apiV1/entities/crud/section_crud.feature")
  runTests("test/features/apiV1/entities/crud/crud.feature")
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/entities/crud_auto")
  runTests("test/features/apiV1/search/api_search.feature")
end

task :apiV1AssociationTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/crud/assoc_crud.feature")
  runTests("test/features/apiV1/associations/links/assoc_links.feature")
end

desc "Run API SuperDoc Tests"
task :apiSuperDocTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/superdoc/denormalization_api.feature")
end

desc "Run API PATCH Tests"
task :apiPatchTests => :realmInit do
  # Import the data once, none of these tests edit the data
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/patch/api_patch.feature")
  runTests("test/features/apiV1/patch/api_patch_teacher.feature")
end

desc "Run V1 Selectors Tests"
task :v1SelectorTests => :realmInit do
  # Import the data once, none of these tests edit the data
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/selectors")
end

desc "Run V1 check for duplicate links"
task :apiV1DuplicateLinkTest => :realmInit do
  # Import the data once, none of these tests edit the data
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/entities/Links")
end

desc "Run API querying tests"
task :apiV1QueryingTests => :realmInit do
  DB_NAME = convertTenantIdToDbName(ENV['DB_NAME'] ? ENV['DB_NAME'] : "Hyrule")
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/querying/querying.feature")
  runTests("test/features/apiV1/querying/access_denied_security_events.feature")
  DB_NAME = convertTenantIdToDbName(ENV['DB_NAME'] ? ENV['DB_NAME'] : "Midgar")
end

desc "Run API querying tests"
task :apiV1NTSQueryingTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/querying/no_table_scan.feature")
end

desc "Run V1 XML Tests"
task :v1XMLTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/xml")
end

desc "Run V1 Staff Secuity Tests"
task :v1StaffSecurityTests => :realmInit do
  runTests("test/features/security/staff_security.feature")
end

desc "Run V1 Cascade Deletion Tests"
task :v1NoCascadeDeletionTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/noCascadeDeletion/noCascadeDeletion.feature")
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/noCascadeDeletion/noCascadeDeletion_teacher.feature")
end

desc "Run V1 Direct References Tests"
task :v1DirectReferencesTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferences/directReferences.feature")
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferences/directReferences_teacher.feature")
end

desc "Run V1 Direct References Teacher Tests"
task :v1DirectReferencesTeacherTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferences/directReferences_teacher.feature")
end

desc "Run V1 Direct Reference Collections Tests"
task :v1DirectReferenceCollectionsTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferenceCollections/directReferenceCollections.feature")
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/associations/directReferenceCollections/directReferenceCollections_teacher.feature")
end

desc 'Run V1 Home URI Tests'
task :v1homeUriTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/home_uri")
end

desc "Run User Admin CRUD Tests"
task :userAdminCrudTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/user_admin")
end

desc "Run V1 Hierachy Traversal Tests"
task :v1HierarchyTraversalTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/hierarchyTraversal")
end

desc "Run V1 Validation Tests"
task :v1ValidationTests => :realmInit do
  # maybe I need studentSchoolAssociation and teacherSchoolAssociation
  set_fixtures(
    %w( educationOrganization staff staffEducationOrganizationAssociation student section ), 'test/data/Midgar_data'
  )
  runTests("test/features/apiV1/validation/validation.feature")
end

desc "Run V1 Teacher Validation Tests"
task :v1TeacherValidationTests => :realmInit do
  set_fixtures(
    %w( educationOrganization staff staffEducationOrganizationAssociation student section ), 'test/data/Midgar_data'
  )
  runTests("test/features/apiV1/validation/teacher_validation.feature")
end

desc "Run Sorting and Paging Tests"
task :v1SortingAndPagingTests => :realmInit do
  Rake::Task["importSandboxData"].execute  
  runTests("test/features/apiV1/sorting_paging")
end

desc "Run Encryption Tests"
task :v1EncryptionTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/encryption")
end

desc "Run Target Tests"
task :v1TargetTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/targets")
end

desc "Run List Tests"
task :v1ListTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/lists")
end

desc "Run Tests for new endpoints"
task :v1NewEndpointTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/endpoints/endpoints.feature")
end

desc "Run Tests for list-attendance endpoint"
task :v1ListAttendanceEndpointTests do
  runTests("test/features/apiV1/endpoints/listAttendancesEndpoint.feature")
end

desc "Run V1 Custom entity User Story Tests"
task :v1EndUserStoryCustomEntityTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/end_user_stories/CustomEntities/CustomEntities.feature")
end

desc "Run V1 Student Optional Fields Tests"
task :v1StudentOptionalFieldsTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/optional_fields/student_optional_fields.feature")
end

desc "Run V1 Single Student View Tests"
task :v1SingleStudentViewTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/optional_fields/single_student_view.feature")
end

desc "Run V1 Blacklist/Whitelist input Tests"
task :v1BlacklistValidationTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/blacklistValidation/blacklistValidation.feature")
end

desc "Run V1 SecurityEvent Tests"
task :v1SecurityEventTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  set_fixture("securityEvent", "securityEvent_fixture.json")
  runTests("test/features/apiV1/securityEvent/securityEvent.feature")
end

desc "Run V1 Comma-Separated List Order Tests"
task :v1CommaSeparatedListOrderTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  set_fixture("student", "Midgar_data/student_fixture.json")
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

desc "Run API Multiple Parent Tests"
task :apiOdinMultipleParentTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  allLeaAllowApp("Mobile App")
  authorizeEdorg("Mobile App")
  runTests("test/features/apiV1/integration/multiple_parents.feature")
end

desc "Run API Federated Apps Tests"
task :apiOdinFederatedAppsTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  allLeaAllowApp("Mobile App")
  authorizeEdorg("Mobile App")
  runTests("test/features/apiV1/integration/federated_apps.feature")
end

desc "Run API Performance Tests"
task :apiPerformanceTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/apiV1/performance/performance.feature")
end

desc "Run API JMeter Tests"
task :apiJMeterTests do
  runTests("test/features/apiV1/jmeter/jmeterPerformance.feature")
end

desc "Run Odin API Generation Task"
task :apiOdinGenerate do
  runTests("test/features/odin/generate_api_data.feature")
end

desc "Run Odin API Student Data Generation Task"
task :apiOdinSecurityGenerate do
  runTests("test/features/odin/generate_api_security_data.feature")
end

desc "Run ODIN API Contextual Roles Data Generation Task"
task :apiOdinContextualRolesGenerate do
  runTests("test/features/odin/generate_api_contextual_roles.feature")
end

desc "Run API Odin Ingestion Tests"
task :apiOdinIngestion do
  runTests("test/features/ingestion/features/ingestion_OdinAPIData.feature")
end

desc "Run API Odin Ingestion Tests"
task :apiOdinSecurityIngestion do
  runTests("test/features/ingestion/features/ingestion_OdinSecurityData.feature")
end

desc "Run API Odin Ingestion Test"
task :apiOdinContextualRolesIngestion do
  runTests("test/features/ingestion/features/ingestion_OdinContextualRoles.feature")
end

desc "Run API Odin Assessment Integration Tests"
task :apiOdinSuperAssessment => :realmInit do
  allLeaAllowApp("Mobile App")
  authorizeEdorg("Mobile App")
# This is to extract assessment, learningStandard, etc. into Elastic Search  
  Rake::Task["runSearchBulkExtract"].execute
  runTests("test/features/apiV1/integration/super_assessment.feature")
  runTests("test/features/apiV1/integration/search_assessment.feature")
end

desc "Run API Odin Assessment Search Tests"
task :apiOdinSearchAssessment do
  Rake::Task["runSearchBulkExtract"].execute
  runTests("test/features/apiV1/end_user_stories/assessments/searchAssessment.feature")
end

desc "Set up app for api odin tests"
task :apiOdinSetupAPIApp => :realmInit do
  allLeaAllowApp("Mobile App")
  authorizeEdorg("Mobile App")
end

desc "Set up api for odin tests"
task :apiOdinSetupAPI => [:realmInit, :apiOdinSetupAPIApp] do
  Rake::Task["runSearchBulkExtract"].execute
  runTests("test/features/apiV1/integration/parent_student_token_generator.feature")
end

desc "Run API Odin Student Integration Tests"
task :apiOdinStudentLogin => [:apiOdinSetupAPI] do
  runTests("test/features/apiV1/integration/student_login.feature")
  runTests("test/features/apiV1/integration/student_endpoints.feature")
  runTests("test/features/apiV1/integration/student_staff_endpoints.feature")
  runTests("test/features/apiV1/integration/student_path_security.feature")
  runTests("test/features/apiV1/integration/student_validator_security.feature")
  runTests("test/features/apiV1/integration/student_other_student_fields.feature")
  runTests("test/features/apiV1/integration/student_crud_operations.feature")
end

desc "Run API Odin Parent Integration Tests"
task :apiOdinParentLogin => [:apiOdinSetupAPI] do
  runTests("test/features/apiV1/integration/parent_login.feature")
  runTests("test/features/apiV1/integration/parent_public.feature")
  runTests("test/features/apiV1/integration/parent_endpoints.feature")
  runTests("test/features/apiV1/integration/parent_staff_endpoints.feature")
  runTests("test/features/apiV1/integration/parent_path_security.feature")
  runTests("test/features/apiV1/integration/parent_validator_security.feature")
  runTests("test/features/apiV1/integration/parent_other_student_fields.feature")
  runTests("test/features/apiV1/integration/parent_crud_operations.feature")
end

desc "Run contextual roles acceptance tests"
task :apiContextualRolesTests => [:apiOdinContextualRolesGenerate, :apiOdinContextualRolesIngestion, :runSearchBulkExtract] do
  runTests("test/features/apiV1/contextual_roles/classPeriod_bellSchedule.feature")
  runTests("test/features/apiV1/contextual_roles/custom_entities.feature")
  runTests("test/features/apiV1/contextual_roles/matchRoles.feature")
  runTests("test/features/apiV1/contextual_roles/other_entities_crud.feature")
  runTests("test/features/apiV1/contextual_roles/student_delete.feature")
  runTests("test/features/apiV1/contextual_roles/student_gets.feature")
  runTests("test/features/apiV1/contextual_roles/student_patch.feature")
  runTests("test/features/apiV1/contextual_roles/student_post.feature")
  runTests("test/features/apiV1/contextual_roles/student_put.feature")
  set_fixture 'calendarDate', 'Midgar_data/calendarDate_fixture.json'
  set_fixtures %w(staff educationOrganization)
  set_fixture 'staffEducationOrganizationAssociation',  'staffEducationOrganizationAssociation_fixture_contextual_roles.json'
  runTests("test/features/apiV1/contextual_roles/classPeriod_bellSchedule_crud.feature")
  display_failure_report
end

desc "Run API V1 Elastic Search Limits Tests"
task :apiV1SearchLimitTests => :realmInit do
  Rake::Task["ingestionSmallSampleDataSet"].execute
  Rake::Task["runSearchBulkExtract"].execute
  runTests("test/features/apiV1/search/search_limits.feature")
  display_failure_report
end

############################################################
# API V1 tests end
############################################################

############################################################
# Security tests start
############################################################
desc "Run Security Tests"
task :securityTests => :realmInit do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/security")
end

desc "Run Security MegaTest"
task :apiMegaTests => :realmInit do
    DB_NAME = convertTenantIdToDbName(ENV['DB_NAME'] ? ENV['DB_NAME'] : "Security")
    Rake::Task["importSecuredData"].execute
    runTests("test/features/apiV1/entities/student_security")
    DB_NAME = convertTenantIdToDbName(ENV['DB_NAME'] ? ENV['DB_NAME'] : "Midgar")
end
############################################################
# Security tests end
############################################################