=begin

Copyright 2012 Shared Learning Collaborative, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end

require_relative '../../../utils/sli_utils.rb'
require_relative '../../entities/common.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<(.+)>$/ do |template|
  id = template
  id = "74cf790e-84c4-4322-84b8-fca7206f1085" if template == "MARVIN MILLER STUDENT ID"
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^selector "(\([^\"]*\))"$/ do |selector|
  steps "Given parameter \"selector\" is \":#{selector}\""
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

###############################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
###############################################################################

Then /^in the response body I should see the following fields:$/ do |table|
  table.cells_rows.each do |field|
    assert(@result.has_key?(field.value(0)), "Response does not contain the field #{field}\nFields return: #{@result.keys}")
  end
end

Then /^in the response body I should see the following fields only:$/ do |table|
  num_of_fields = table.cells_rows.size
  assert(@result.keys.size == num_of_fields,
         "Number of fields returned doesn't match: received #{@result.keys.size}, expected #{num_of_fields}\nFields return: #{@result.keys}")
  table.cells_rows.each do |field|
    assert(@result.has_key?(field.value(0)),
           "Response does not contain the field #{field}\nFields return: #{@result.keys}")
  end
end

Then /^in "([^\"]*)" I should see the following fields:$/ do |key, table|
  assert(@result.has_key?(key), "Response does not contain #{key}")
  @result["#{key}"].each do |row|
    table.cells_rows.each do |field|
      assert(row.has_key?(field.value(0)),
             "#{key} does not contain the field #{field}\nFields return: #{row.keys}")
    end
  end
end

Then /^in "([^\"]*)" I should see the following fields only:$/ do |key, table|
  assert(@result.has_key?(key), "Response does not contain #{key}")
  @result["#{key}"].each do |row|
    num_of_fields = table.cells_rows.size
    assert(row.keys.size == num_of_fields,
           "Number of fields returned doesn't match: received #{row.keys.size}, expected #{num_of_fields}\nFields return: #{row.keys}")
    table.cells_rows.each do |field|
      assert(row.has_key?(field.value(0)),
             "#{key} does not contain the field #{field}\nFields return: #{row.keys}")
    end
  end
end

