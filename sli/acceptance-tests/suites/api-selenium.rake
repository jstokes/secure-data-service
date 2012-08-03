############################################################
# API Selenium tests start
# NOTE: Do not add these tests to the production run yet!
#       While they can be run locally without issues,
#       Firefox cannot start on Jenkins, and will cause
#       all these tests to fail
############################################################
task :adminRightsTests do
  runTests("test/features/admintools/sli_admin_authorization.feature")
end

desc "Run Admin Tool Smoke Tests"
task :adminWebTests => [:realmInit] do
  Rake::Task["importSandboxData"].execute
  runTests("test/features/admintools")
end

desc "Run IDP Authentication Selenium Tests"
task :idpAuthTests => [:realmInit] do
  runTests("test/features/databrowser/idp_authentication.feature")
  displayFailureReport()
  if $SUCCESS
    puts "Completed All Tests"
  else
    raise "Tests have failed"
  end
end

desc "Run Sandbox Simple IDP Authentication Selenium Tests"
task :simpleIDPAuthTests => [:realmInit, :importSandboxData] do
  runTests("test/features/simple-idp")
  displayFailureReport()
  if $SUCCESS
    puts "Completed All Tests"
  else
    raise "Tests have failed"
  end
end

desc "Run Dataprowler Smoke Tests"
task :databrowserSmokeTests do 
  @tags = ["~@wip", "@smoke", "~@sandbox"]
  runTests("test/features/databrowser/databrowser_simple_detail_view.feature")
end

desc "Run Admin Tool Smoke Tests"
task :adminSmokeTests do 
  @tags = ["~@wip", "@smoke", "~@sandbox"]
  Rake::Task["realmInit"].execute
  Rake::Task["importSandboxData"].execute
  runTests("test/features/admintools/admin_smoke.feature")
end

############################################################
# API Selenium tests end
############################################################

