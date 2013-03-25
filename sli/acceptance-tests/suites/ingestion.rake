############################################################
# Ingestion tests start
############################################################
desc "Run Concurrent Tenant Ingestion Test"
task :ingestionConcurrentTenantTest do
  runTests("test/features/ingestion/features/ingestion_concurrentTenant.feature")
end

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

desc "Run Ingestion LearningObjective Tests"
task :ingestionLearningObjectiveTest do
  runTests("test/features/ingestion/features/ingestion_learningObjective.feature")
end

desc "Run Ingestion Tenant Test"
task :ingestionTenantTest do
  runTests("test/features/ingestion/features/ingestion_tenant.feature")
end

desc "Run Ingestion Negative Tests"
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


desc "Run idNamespace Test"
task :ingestionidNamespaceTest do
  runTests("test/features/ingestion/features/ingestion_idNamespace.feature")
end


desc "Run Ingestion Batch Job Tests"
task :ingestionBatchJobTest do
  runTests("test/features/ingestion/features/ingestion_BatchJob.feature")
end


desc "Run Ingestion Delta Tests"
task :ingestionDeltaTest do
  runTests("test/features/ingestion/features/ingestion_delta.feature")
end

desc "Run Ingestion XSD Validation Test"
task :ingestionXsdValidationTest do
  runTests("test/features/ingestion/features/xsd_validation.feature")
end

desc "Run Index Validation Test" 
task :ingestionIndexValidationTest do 
  runTests("test/features/ingestion/features/ingestion_index_validation.feature") 
end

desc "Run Ingestion Ignore ID Reference Resolution Test"
task :ingestionIgnoreIDRefsTest do
  runTests("test/features/ingestion/features/ingestion_ignoreIDRefs.feature")
end

desc "Run Ingestion Error Report Interface Test"
task :ingestionErrorReportTest do
  runTests("test/features/ingestion/features/ingestion_ErrorReport.feature")
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
end

desc "Run Ingestion Parallel Job Tests"
task :ingestionParallelTests do
 runTests("test/features/ingestion/features/ingestion_acceptance_parallel_test.feature")
end

desc "Run Ingestion for Dashboard Sad Path Tests"
task :ingestionDashboardSadPathTest do
  runTests("test/features/ingestion/features/ingestion_dashboardSadPath.feature")
end

desc "Run Blacklist Validation Tests"
task :ingestionBlacklistValidationTests do
  runTests("test/features/ingestion/features/blacklistValidation_testing.feature")
end

desc "Run Ingestion Offline Tool Simple Test"
task :ingestionOfflineSimpleTest do
  runTests("test/features/ingestion/features/offline_validation.feature")
end

desc "Run Tenant Purge Test"
task :ingestionTenantPurgeTests do
  Rake::Task["realmInit"].invoke
  Rake::Task["importPurgeData"].invoke
  runTests("test/features/ingestion/features/tenant_purge.feature")
end

desc "Run XXE Injection Protection Test"
task :ingestionXXETest do
  runTests("test/features/ingestion/features/xxe_testing.feature")
end

desc "Run CompetencyLevelDescriptor Ingestion Test"
task :ingestionCompetencyLevelDescriptorTest do
  runTests("test/features/ingestion/features/ingestion_competencyLevelDescriptor.feature")
end

desc "Run bad control File Test"
task :ingestionBadControlFileTest do
  runTests("test/features/ingestion/features/ingestion_badCtlFile.feature")
end

desc "Run Dry Run Test"
task :ingestionDryRunTest do
  runTests("test/features/ingestion/features/ingestion_dry_run.feature")
end

desc "Run Course Update Test"
task :ingestionCourseUpdateTest do
  runTests("test/features/ingestion/features/ingestion_courseUpdate.feature")
end

desc "Run Complex Object Array Id Reference Resolution Test"
task :ingestionComplexObjectArrayIdRefTest do
  runTests("test/features/ingestion/features/ingestion_complexObjectArrayIdResolution.feature")
end

desc "Run Sandbox Data Test"
task :ingestionSandboxDataTest do
  runTests("test/features/ingestion/features/ingestion_sandbox_data_test.feature")
end

desc "Run Medium Sample Data Set Test"
task :ingestionMediumSampleDataSet do
  runTests("test/features/ingestion/features/ingestion_MediumSampleDataSet.feature")
end

desc "Run Ingestion Smoke Tests"
task :ingestionSmokeTests do
  @tags = ["~@wip", "@smoke", "~@sandbox"]
  Rake::Task["ingestionAcceptanceSdsTest"].invoke
end

desc "Run Small Sample Data Set Test"
task :ingestionSmallSampleDataSet do
  runTests("test/features/ingestion/features/ingestion_SmallSampleDataSet.feature")
end

desc "Run Ingestion HealthCheck Test"
task :ingestionHealthCheckTest do
  runTests("test/features/ingestion/features/ingestion_healthCheck.feature")
end

desc "Run Ingestion Error/Warning Limitation Testing"
task :ingestionErrorWarnCountTest do
  runTests("test/features/ingestion/features/ingestion_error_warning_count.feature")
end

desc "Run Preloading Test"
task :ingestionPreloading do
  runTests("test/features/ingestion/features/ingestion_preload_data.feature")
end

desc "Run Ingestion Order Agnostic Lists in Key Fields Testing"
task :ingestionOrderAgnosticListTest do
  testHash = Hash[
    "staff" => "staff_fixture.json",
    "program" => "program_fixture.json",
    "staffProgramAssociation" => "ingestion/orderAgnosticList_fixture.json"
  ]
  setMultipleFixtureFiles(testHash)
  runTests("test/features/ingestion/features/ingestion_orderAgnosticList.feature")
end

desc "Run Partial Ingestion of transformed Test"
task :partialTransformedIngestionTest do
  runTests("test/features/ingestion/features/ingestion_TransformedPartialIngestion.feature")
end

desc "Run Partial Ingestion Test"
task :partialIngestionTest do
  runTests("test/features/ingestion/features/ingestion_partial_test.feature")
end

desc "Ingestion Index Validation Test"
task :IngestionIndexValidationTest do
  runTests("test/features/ingestion/features/ingestion_indexValidation.feature") if PropLoader.getProps['ingestion_index_validation']
end

desc "Ingestion Jefferson County Onbarding Test"
task :ingestionJeffCoOnboardingTest do
  runTests("test/features/ingestion/features/ingestion_JeffCoOnboarding.feature")
end

desc "Ingestion Superdoc Tests"
task :ingestionSuperDocTests do
  runTests("test/features/superdoc/denormalization_ingestion.feature")
end

desc "Ingestion Subdoc Tests"
task :ingestionSubdocTests do
  runTests("test/features/ingestion/features/ingestion_subdoc.feature")
end

desc "Ingestion Db Spinup Test"
task :ingestionDbSpinupTest do
  runTests("test/features/ingestion/features/ingestion_db_spinup.feature")
end

desc "UniqueSectionCode Length Test"
task :ingestionUniqueSectionCodeTest do
  runTests("test/features/ingestion/features/ingestion_uniqueSectionCode.feature")
end

desc "Revert Data Test"
task :ingestionRevertDataTest do
  runTests("test/features/ingestion/features/ingestion_revert.feature")
end

desc "Odin Data Set Ingestion Tests"
task :ingestionOdinDataSet do
  runTests("test/features/ingestion/features/ingestion_OdinDataSet.feature")
end

desc "Duplicate Detection Test"
task :ingestionDupDetectTest do
  runTests("test/features/ingestion/features/ingestion_dupdetect.feature")
end

desc "Fail Fast on Missing Index Test"
task :ingestionJobFailOnMissingIndexTest do
  runTests("test/features/ingestion/features/ingestion_jobFailOnMissingIndex.feature")
end

desc "Run Ingestion SecurityEvent Test"
task :ingestionSecurityEventTest do
  runTests("test/features/ingestion/features/ingestion_securityEvent.feature")
end

desc "Run Ingestion Propagated Data Set Test"
task :ingestionPropagatedTest do
  runTests("test/features/ingestion/features/ingestion_PropagatedDataSet.feature")
end

desc "Run Ingestion Deletion Tests"
  task :ingestionDeletionTests => [
        :ingestionCascadingDeletionBroadAssessmentFamilyTest,
        :ingestionCascadingDeletionBroadAssessmentItemTest,
        :ingestionCascadingDeletionBroadAssessmentPeriodDescriptorTest,
        :ingestionCascadingDeletionBroadAssessmentTest,
        :ingestionCascadingDeletionBroadAttendanceEventTest,
        :ingestionCascadingDeletionBroadCalendarDateTest,
        :ingestionCascadingDeletionBroadCohortTest,
        :ingestionCascadingDeletionBroadCompetencyLevelDescriptorInStudentGradesTest,
        :ingestionCascadingDeletionBroadCompetencyLevelDescriptorTest,
        :ingestionCascadingDeletionBroadCourseOfferingTest,
        :ingestionCascadingDeletionBroadCourseTest,
        :ingestionCascadingDeletionBroadCourseTranscriptTest,
        :ingestionCascadingDeletionBroadDisciplineActionTest,
        :ingestionCascadingDeletionBroadDisciplineIncidentTest,
        :ingestionCascadingDeletionBroadGradebookEntryTest,
        :ingestionCascadingDeletionBroadGradeTest,
        :ingestionCascadingDeletionBroadGradingPeriodTest,
        :ingestionCascadingDeletionBroadGraduationPlanTest,
        :ingestionCascadingDeletionBroadLearningObjectiveFromStudentGradesTest,
        :ingestionCascadingDeletionBroadLearningObjectiveTest,
        :ingestionCascadingDeletionBroadLearningStandardTest,
        :ingestionCascadingDeletionBroadLEATest,
        :ingestionCascadingDeletionBroadObjectiveAssessmentTest,
        :ingestionCascadingDeletionBroadParentTest,
        :ingestionCascadingDeletionBroadProgramTest,
        :ingestionCascadingDeletionBroadReportCardTest,
        :ingestionCascadingDeletionBroadSchoolTest,
        :ingestionCascadingDeletionBroadSEATest,
        :ingestionCascadingDeletionBroadSectionTest,
        :ingestionCascadingDeletionBroadSessionTest,
        :ingestionCascadingDeletionBroadStaffCohortAssociationTest,
        :ingestionCascadingDeletionBroadStaffEducationOrgAssignmentAssociationTest,
        :ingestionCascadingDeletionBroadStaffProgramAssociationTest,
        :ingestionCascadingDeletionBroadStaffTest,
        :ingestionCascadingDeletionBroadStudentAcademicRecordTest,
        :ingestionCascadingDeletionBroadStudentAssessmentTest,
        :ingestionCascadingDeletionBroadStudentCohortAssociationTest,
        :ingestionCascadingDeletionBroadStudentCompetencyObjectiveTest,
        :ingestionCascadingDeletionBroadStudentCompetencyTest,
        :ingestionCascadingDeletionBroadStudentDisciplineIncidentAssociationTest,
        :ingestionCascadingDeletionBroadStudentFromInterchangeStudentParentTest,
        :ingestionCascadingDeletionBroadStudentGradebookEntryTest,
        :ingestionCascadingDeletionBroadStudentParentAssociationTest,
        :ingestionCascadingDeletionBroadStudentProgramAssociationTest,
        :ingestionCascadingDeletionBroadStudentSchoolAssociationTest,
        :ingestionCascadingDeletionBroadStudentSectionAssociationTest,
        :ingestionCascadingDeletionBroadTeacherSchoolAssociationTest,
        :ingestionCascadingDeletionBroadTeacherSectionAssociationTest,
        :ingestionCascadingDeletionBroadTeacherTest,
        :ingestionCascadingDeletionSchoolTest,
        :ingestionCascadingDeletionSectionTest,
        :ingestionCascadingDeletionStudentTest,
        :ingestionCascadingDeletionTeacherTest,
        :ingestionLeafStudentDelete,
  ] do

  displayFailureReport()
  if $SUCCESS
    puts "Completed All Tests"
  else
    raise "Tests have failed"
  end
end

desc "Run Leaf Student Deletion Test"
task :ingestionLeafStudentDelete do
  runTests("test/features/ingestion/features/ingestion_leaf_student_delete.feature")
end

task :ingestionCascadingDeletionStudentTest do
  runTests("test/features/ingestion/features/ingestion_cascadingDelete_student.feature")
end

task :ingestionCascadingDeletionSectionTest do
  runTests("test/features/ingestion/features/ingestion_cascadingDelete_section.feature")
end

task :ingestionCascadingDeletionSchoolTest do
  runTests("test/features/ingestion/features/ingestion_cascadingDelete_school.feature")
end

task :ingestionCascadingDeletionBroadSEATest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_SEA_delete.feature")
end

task :ingestionCascadingDeletionBroadLEATest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_LEA_delete.feature")
end

task :ingestionCascadingDeletionBroadProgramTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_program_delete.feature")
end


task :ingestionCascadingDeletionBroadSchoolTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_school_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentFromInterchangeStudentParentTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentFromInterchangeStudentParent_delete.feature")
end

task :ingestionCascadingDeletionBroadSectionTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_section_delete.feature")
end

task :ingestionCascadingDeletionBroadAssessmentTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_assessment_delete.feature")
end

task :ingestionCascadingDeletionBroadAssessmentItemFromAssessmentMetadataTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_assessmentItemFromAssessmentMetadata_delete.feature")
end

task :ingestionCascadingDeletionBroadAssessmentFamilyTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_assessmentFamily_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentAssessmentTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentAssessment_delete.feature")
end

task :ingestionCascadingDeletionBroadObjectiveAssessmentTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_objectiveAssessment_delete.feature")
end

task :ingestionCascadingDeletionBroadAssessmentPeriodDescriptorTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_AssessmentPeriodDescriptor_delete.feature")
end

task :ingestionCascadingDeletionBroadLearningObjectiveTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_LearningObjective_delete.feature")
end

task :ingestionCascadingDeletionBroadLearningStandardTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_LearningStandard_delete.feature")
end

task :ingestionCascadingDeletionBroadCourseTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_course_delete.feature")
end

task :ingestionCascadingDeletionBroadStaffTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_staff_delete.feature")
end

task :ingestionCascadingDeletionBroadStaffEducationOrgAssignmentAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_StaffEducationOrgAssignmentAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadTeacherTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_teacher_delete.feature")
end

task :ingestionCascadingDeletionBroadStaffProgramAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_StaffProgramAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadTeacherSchoolAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_TeacherSchoolAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadTeacherSectionAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_TeacherSectionAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadCourseOfferingTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_CourseOffering_delete.feature")
end

task :ingestionCascadingDeletionBroadCompetencyLevelDescriptorTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_competencyLevelDescriptor_delete.feature")
end

task :ingestionCascadingDeletionBroadParentTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_parent_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentParentAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentParentAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadSessionTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_session_delete.feature")
end

task :ingestionCascadingDeletionBroadGradingPeriodTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_gradingPeriod_delete.feature")
end

task :ingestionCascadingDeletionBroadCalendarDateTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_calendarDate_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentProgramAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentProgramAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadAttendanceEventTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_attendanceEvent_delete.feature")
end

task :ingestionCascadingDeletionTeacherTest do
  runTests("test/features/ingestion/features/ingestion_cascadingDelete_teacher.feature")
end

task :ingestionCascadingDeletionBroadStudentSchoolAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentSchoolAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentSectionAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentSectionAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadGraduationPlanTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_graduationPlan_delete.feature")
end

task :ingestionCascadingDeletionBroadDisciplineIncidentTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_disciplineIncident_delete.feature")
end

task :ingestionCascadingDeletionBroadDisciplineActionTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_disciplineAction_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentDisciplineIncidentAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentDisciplineIncidentAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadCohortTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_cohort_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentCohortAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentCohortAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadStaffCohortAssociationTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_staffCohortAssociation_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentCompetencyObjectiveTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentCompetencyObjective_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentCompetencyTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentCompetency_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentAcademicRecordTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentAcademicRecord_delete.feature")
end

task :ingestionCascadingDeletionBroadGradeTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_grade_delete.feature")
end

task :ingestionCascadingDeletionBroadCourseTranscriptTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_courseTranscript_delete.feature")
end

task :ingestionCascadingDeletionBroadReportCardTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_reportCard_delete.feature")
end

task :ingestionCascadingDeletionBroadGradebookEntryTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_gradebookEntry_delete.feature")
end

task :ingestionCascadingDeletionBroadStudentGradebookEntryTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_studentGradebookEntry_delete.feature")
end

task :ingestionCascadingDeletionBroadCompetencyLevelDescriptorInStudentGradesTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_competencyLevelDescriptorInStudentGrades_delete.feature")
end

task :ingestionCascadingDeletionBroadLearningObjectiveFromStudentGradesTest do
  runTests("test/features/ingestion/features/ingestion_broad_cascading_learningObjectiveFromStudentGrades_delete.feature")
end

############################################################
# Ingestion tests end
############################################################

############################################################
# Ingestion Offline Tool tests start
############################################################
desc "Run Ingestion Offline Tool Acceptances Tests"
task :ingestionOfflineToolTests => [:ingestionOfflineSimpleTest] do
  displayFailureReport()
  if $SUCCESS
    puts "Completed All Tests"
  else
    raise "Tests have failed"
  end
end
############################################################
# Ingestion Offline Tool tests end
############################################################

