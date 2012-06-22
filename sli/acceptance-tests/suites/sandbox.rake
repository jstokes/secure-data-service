############################################################
# API Sandbox Tests start
############################################################
desc "Run Sandbox API tests"
task :apiSandboxTests do
  @tags = ["~@wip", "@sandbox"]
  Rake::Task["securityTests"].invoke
end

desc "Run Sandbox mode Tests"
task :adminSandboxTests do
  @tags = ["~@wip", "@sandbox"]
  Rake::Task["adminToolsTests"].invoke
end

desc "Run Sandbox Simple IDP tests"
task :simpleIdpSandboxTests do
  @tags = ["~@wip", "@sandbox"]
  Rake::Task["importSandboxData"].invoke
  runTests("test/features/simple_idp/SimpleIDP.feature")
end
############################################################
# API Sandbox Tests end
############################################################


############################################################
# Account Approval tests start
############################################################
desc "Run Account Approval acceptance tests"
task :accountApprovalInterfaceTests => [:realmInitNoPeople] do
    runTests("test/features/sandbox/AccountApproval/prod_sandbox_AccountApproval_Inteface.feature")
end

desc "Run Account Approval Sandbox acceptance tests"
task :accountApprovalSandboxTests do
  @tags = ["~@wip", "@sandbox"]
  Rake::Task["accountApprovalInterfaceTests"].invoke
  Rake::Task["accountApprovalTests"].invoke
end

desc "Run Account Approval Tests"
task :accountApprovalTests do
  runTests("test/features/sandbox/AccountApproval/prod_sandbox_AccountApproval.feature")
end
############################################################
# Account Approval tests end
############################################################

############################################################
# Onboarding tests start
############################################################

desc "Run Onboarding Integration Tests"
task :onboardingIntegrationSandboxTests do
  @tags = ["~@wip", "@sandbox"]
  runTests("test/features/sandbox/Integration/onboarding_integration.feature")
end
############################################################
# Onboarding tests end
############################################################

###########################################################
# Provisioning tests start
############################################################

desc "Run Provisioning Integration Sandbox Tests"
task :provisionIntegrationSandboxTests do
  @tags = ["~@wip", "@sandbox"]
  runTests("test/features/sandbox/Provision/provision_Integrated.feature")
end
############################################################
# Provisioning tests end
############################################################

###########################################################
# Tenant Metrics tests start
############################################################

desc "Tenant Metrics Tests"
task :tenantMetricsTests do
  @tags = ["~@wip"]
  Rake::Task["importTenantMetricsData"].invoke
  runTests("test/features/sandbox/TenantUsage/usage_analytics.feature")
end
############################################################
# Tenant Metrics tests end
############################################################



