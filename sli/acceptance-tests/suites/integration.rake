############################################################
# Cross App Tests start
############################################################
desc "Run cross application testing"
task :crossAppTests => [:appInit] do
  runTests("test/features/cross_app_tests")
end
############################################################
# Cross App Tests end
############################################################
