require 'mongo'
require 'fileutils'
require_relative '../../utils/sli_utils.rb'

def generate(scenario="10students")
  # run bundle exec rake in the Odin directory
  puts "Shell command will be bundle exec ruby driver.rb #{scenario}"
  FileUtils.cd @odin_working_path
  runShellCommand("bundle exec ruby driver.rb #{scenario}")
  FileUtils.cd @at_working_path
end

############################################################
# STEPS: BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE
############################################################
Before do
  @at_working_path = Dir.pwd
  @odin_working_path = "#{File.dirname(__FILE__)}" + "/../../../../../../tools/odin/"
end

############################################################
# STEPS: GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
############################################################
Given /^I am using the odin working directory$/ do
  @odin_working_path = "#{File.dirname(__FILE__)}" + "/../../../../../../tools/odin/"
end

############################################################
# STEPS: WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
############################################################
When /^I generate the 10 student data set in the (.*?) directory$/ do |gen_dir|
  @gen_path = "#{@odin_working_path}#{gen_dir}/"
  puts "Calling generate function for 10 students scenario"
  generate("10students")   
end

When /^I generate the 10001 student data set in the (.*?) directory$/ do |gen_dir|
  @gen_path = "#{@odin_working_path}#{gen_dir}/"
  puts "Calling generate function for 10001 students scenario"
  generate("10001students")    
end

When /^I generate the jmeter api performance data set in the (.*?) directory$/ do |gen_dir|
  @gen_path = "#{@odin_working_path}#{gen_dir}/"
  puts "Calling generate function for jmeter api performance scenario"
  generate("jmeter_api_performance")   
end

When /^I zip generated data under filename (.*?) to the new (.*?) directory$/ do |zip_file, new_dir|
  @zip_path = "#{@gen_path}#{new_dir}/"
  FileUtils.mkdir_p(@zip_path)
  FileUtils.chmod(0777, @zip_path)
  runShellCommand("zip -j #{@zip_path}#{zip_file} #{@gen_path}*.xml #{@gen_path}*.ctl")
end

When /^I copy generated data to the new (.*?) directory$/ do |new_dir|
  @zip_path = "#{@gen_path}#{new_dir}"
  Dir["#{@gen_path}*.xml"].each do |f|
    FileUtils.cp(f, @zip_path)
    puts "Copied file #{f} to #{@zip_path}"
  end
  Dir["#{@gen_path}*.ctl"].each {|f| FileUtils.cp(f, @zip_path)}
end

############################################################
# STEPS: THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
############################################################
Then /^I should see ([0-9]+) xml interchange files$/ do |file_count|
  count = Dir["#{@gen_path}*xml"].length
  file_count = file_count.to_i
  puts "Expected to see #{file_count} files, found #{count}"
  raise "Did not find expected number of Interchange files (found #{count}, expected #{file_count})" if file_count.to_i != count
end

Then /^I should see (.*?) has been generated$/ do |filename|
  raise "#{filename} does not exist" if !File.exist?("#{@gen_path}#{filename}")
end

Given /^the edfi manifest that was generated in the '([^\']*)' directory$/ do |dir|
  @manifest = "#{@odin_working_path}#{dir}/manifest.json"
end

Given /^the tenant is '([^\']*)'$/ do |tenant_id|
  @tenant_id = tenant_id
end

Then /^the sli\-verify script completes successfully$/ do
  results = `bundle exec ruby #{@odin_working_path}sli-verify.rb #{@tenant_id} #{@manifest}`
  assert(results.include?("All expected entities found\n"), "verification script failed, results are #{results}")
end
