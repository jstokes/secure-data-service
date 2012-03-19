require_relative '../../../utils/sli_utils.rb'

When /^the view configuration file has a AssessmentFamilyHierarchy like "([^"]*)"$/ do |arg1|
  # configuration has been setup to use correct assessmentFamilyHierarchy
end

Then /^I should see his\/her most recent ISAT Writing Perf\. level is "([^"]*)"$/ do |perfLevel|
  level = @driver.find_element(:id, @studentName+".ISAT Writing for Grade 8.Mastery level")
  level.should_not be nil
  level.text.should == perfLevel
end

Then /^I should see his\/her Perf\.level for DIBELS Next for most recent window is "([^"]*)"$/ do |perfLevel|
  level = @driver.find_element(:id, @studentName+".DIBELS_NEXT.Mastery level")
  level.should_not be nil
  level.text.should == perfLevel
end