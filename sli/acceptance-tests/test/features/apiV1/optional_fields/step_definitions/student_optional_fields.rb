require 'rest-client'
require 'json'
require_relative '../../../utils/sli_utils.rb'
require_relative '../../entities/common.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<(.+)>$/ do |template|
  id = template
  id = "706ee3be-0dae-4e98-9525-f564e05aa388" if template == "SECTION ID"
  id = "e7b6521b-7bed-890a-d4b5-c4b25a29fc7e" if template == "STUDENT SECTION ASSOC ID"
  id = "53777181-3519-4111-9210-529350429899" if template == "COURSE ID"
  id = "32930275-a9f3-4eaa-866f-7b35efc303ee" if template == "SCHOOL ID"
  id = "dd69083f-a053-4819-a3cd-a162cdc627d7" if template == "STUDENT_ID"
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^optional field "([^\"]*)"$/ do |field|
  if !defined? @queryParams
    @queryParams = [ "optionalFields=#{field}" ]
  else
    @fields = @queryParams[0].split("=")[1];
    @fields = @fields + ",#{field}"
    @queryParams[0] = "optionalFields=#{@fields}"
  end
end

#################################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
#################################################################################

When /^I look at the first one$/ do
  @col = @col[0]
end

When /^I go back up one level$/ do
  @col = @colStack.pop
end

#################################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
#################################################################################

Then /^I should find "([^\"]*)" in "([^\"]*)"$/ do |key, collection|
  assert(@result[0][collection] != nil, "Response contains no #{collection}")
  assert(@result[0][collection][key] != nil, "Response contains no #{key}")
  @col = @result[0][collection][key]
end

Then /^I should see "([^\"]*)" is "([^\"]*)" in it$/ do |key, value|
  assert(@col[key].to_s == value.to_s, "Expected #{value}, received #{@col[key]}")
end

Then /^I should find "([\d]*)" "([^\"]*)"$/ do |count, collection|
  if @result.is_a?(Array)
    @col = @result[0][collection]
  elsif @result.is_a?(Hash)
    @col = @result[collection]
  end
  assert(@col != nil, "Response contains no #{collection}")
  assert(@col.length == convert(count), "Expected #{count} #{collection}, received #{@col.length}")
end

Then /^I should find "([\d]*)" "([^\"]*)" in it$/ do |count, collection|
  if !defined? @col
    @col = @result[0][collection]
  else
    @col = @col[collection]
  end
  assert(@col != nil, "Response contains no #{collection}")
  assert(@col.length == convert(count), "Expected #{count} #{collection}, received #{@col.length}")
end

Then /^I should find "([^\"]*)" expanded in each of them$/ do |key|
  @col.each do |col|
    assert(col[key] != nil, "Response contains no #{key}")
  end
end

Then /^inside "([^\"]*)"$/ do |key|
  if !defined? @col
    if @result.is_a?(Array)
      @col = @result[0]
    elsif @result.is_a?(Hash)
      @col = @result
    end
  end
  if !defined? @colStack
    @colStack = []
  end
  @colStack.push @col
  @col = @col[key]
end

Then /^I should see the year "([\d]*)" in none of the attendance entries$/ do |year|
  bool = true
  @col.each do |col|
    if col["eventDate"].include? year.to_s
      bool = false
    end
  end
  assert(bool, "Found some attendance data in the year #{year}")
end

Then /^I should see the year "([\d]*)" in some of the attendance entries$/ do |year|
  bool = false
  @col.each do |col|
    if col["eventDate"].include? year.to_s
      bool = true
    end
  end
  assert(bool, "Cannot find attendance data in the year #{year}")
end