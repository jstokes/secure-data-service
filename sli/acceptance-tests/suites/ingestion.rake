############################################################
# Ingestion tests start
############################################################
desc "Run Common Core Standards Tests"
task :ingestionCommonCoreTest do
  runTests("test/features/ingestion/features/ingestion_common_core_standards.feature")
end

desc "Run Ingestion Demo Tests"
task :ingestionDemoDataTest do
  runTests("test/features/ingestion/features/ingestion_demo.feature")
end

desc "Run Ingestion Acceptance SDS Tests"
task :ingestionAcceptanceSdsTest do
  runTests("test/features/ingestion/features/ingestion_acceptance_SDS_test.feature")
end

desc "Run Ingestion Tenant Test"
task :ingestionTenantTest do
  runTests("test/features/ingestion/features/ingestion_tenant.feature")
end

desc "Run Ingestion Session Test"
task :ingestionSessionTest do
  runTests("test/features/ingestion/features/ingestion_session.feature")
end

desc "Run Ingestion Program Test"
task :ingestionProgramTest do
  runTests("test/features/ingestion/features/ingestion_program.feature")
end

desc "Run Ingestion StudentProgramAssociation Test"
task :ingestionStudentProgramAssociationTest do
  runTests("test/features/ingestion/features/ingestion_StudentProgramAssociation.feature")
end

desc "Run Ingestion StaffProgramAssociation Test"
task :ingestionStaffProgramAssociationTest do
  runTests("test/features/ingestion/features/ingestion_StaffProgramAssociation.feature")
end

desc "Run Ingestion Cohort Test"
task :ingestionCohortTest do
  runTests("test/features/ingestion/features/ingestion_cohort.feature")
end

desc "Run Ingestion StudentCohortAssociation Test"
task :ingestionStudentCohortAssociationTest do
  runTests("test/features/ingestion/features/ingestion_StudentCohortAssociation.feature")
end

desc "Run Ingestion StaffCohortAssociation Test"
task :ingestionStaffCohortAssociationTest do
  runTests("test/features/ingestion/features/ingestion_StaffCohortAssociation.feature")
end

desc "Run Ingestion DisciplineIncident Test"
task :ingestionDisciplineIncidentTest do
  runTests("test/features/ingestion/features/ingestion_disciplineIncident.feature")
end

desc desc "Run Ingestion DisciplineAction Test"
task :ingestionDisciplineActionTest do
  runTests("test/features/ingestion/features/ingestion_disciplineAction.feature")
end

desc "Run Ingestion StudentDiciplineIncidentAssociation Test"
task :ingestionStudentDisciplineIncidentAssociationTest do
  runTests("test/features/ingestion/features/ingestion_StudentDisciplineIncidentAssociation.feature")
end

"Run Ingestion Negative Tests"
task :ingestionNegativeTests do
  runTests("test/features/ingestion/features/negative_testing.feature")
end

desc "Run Ingestion Smooks Verification Tests"
task :ingestionSmooksVerificationTests do
  runTests("test/features/ingestion/features/smooks_verification.feature")
end

desc "Run Ingestion Encryption Tests"
task :ingestionEncryptionTests do
  runTests("test/features/ingestion/features/encryption_testing.feature")
end

desc "Run Ingestion Daily Attendance Tests"
task :ingestionAttendanceTests do
  runTests("test/features/ingestion/features/ingestion_daily_attendance.feature")
end

desc "Run Ingestion Assessment Tests"
task :ingestionAssessmentTests do
  runTests("test/features/ingestion/features/ingestion_assessment_metadata.feature")
end

desc "Run Student Parents Tests"
task :ingestionStudentParentsTests do
  runTests("test/features/ingestion/features/ingestion_student_parents.feature")
end

desc "Run Student Transcript Association Test"
task :ingestionStudentTranscriptAssociationTests do
  runTests("test/features/ingestion/features/ingestion_StudentTranscriptAssociation.feature")
end

desc "Run idNamespace Test"
task :ingestionidNamespaceTest do
  runTests("test/features/ingestion/features/ingestion_idNamespace.feature")
end

desc "Run Ingestion Gradebook Entry Test"
task :ingestionGradebookEntryTests do
  runTests("test/features/ingestion/features/ingestion_GradebookEntry.feature")
end

desc "Run Ingestion Batch Job Tests"
task :ingestionBatchJobTest do
  runTests("test/features/ingestion/features/ingestion_BatchJob.feature")
end

desc "Run Ingestion XSD Validation Test"
task :ingestionXsdValidationTest do
  runTests("test/features/ingestion/features/xsd_validation.feature")
end

desc "Run Ingestion AP Assessment and StudentAssessment Tests"
task :ingestionAPAssessmentTests do
  runTests("test/features/ingestion/features/apAssessment.feature")
end

desc "Run Ingestion ACT Assessment Tests"
task :ingestionACTAssessmentTests do
  runTests("test/features/ingestion/features/ACTAssessment.feature")
end

desc "Run Ingestion Ed-Fi ID Reference Resolution Test"
task :ingestionIDReferenceResolutionTest do
  runTests("test/features/ingestion/features/ingestion_IDRef.feature")
end

# This task SHOULD NOT BE ADDED to the general ingestion test suite
desc "Run Ingestion Performance Tests"
task :ingestionPerformanceTest do
  runTests("test/features/ingestion/features/performance_testing.feature")
end

desc "Run Ingestion Database Performance Tests"
task :ingestionMongoDBPerformanceTest do
  runTests("test/features/ingestion/features/ingestion_mongo_performance.feature")
end

desc "Run Ingestion Index Tests"
task :ingestionIndexTest do
 runTests("test/features/ingestion/features/index_testing.feature")
end

desc "Run Ingestion Parallel Job Tests"
task :ingestionParallelTests do
 runTests("test/features/ingestion/features/ingestion_acceptance_parallel_test.feature")
end

desc "Run Ingestion for Dashboard Sad Path Tests"
task :ingestionDashboardSadPathTest do
  runTests("test/features/ingestion/features/ingestion_dashboardSadPath.feature")
end

desc "Run Ingestion Offline Tool Simple Test"
task :ingestionOfflineSimpleTest do
  runTests("test/features/ingestion/features/offline_validation.feature")
end

desc "Run Tenant Purge Test"
task :ingestionTenantPurgeTests do
  runTests("test/features/ingestion/features/tenant_purge.feature")
end
############################################################
# Ingestion tests end
############################################################
