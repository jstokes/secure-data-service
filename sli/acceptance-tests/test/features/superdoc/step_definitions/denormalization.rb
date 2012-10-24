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

require_relative '../../apiV1/associations/crud/step_definitions/assoc_crud.rb'
require_relative '../../ingestion/features/step_definitions/ingestion_steps.rb'

###############################################################################
# BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE BEFORE
###############################################################################

Before do
  @midgar_db_name = convertTenantIdToDbName('Midgar')
end

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|
  # General
  id = @newId                                 if human_readable_id == "NEW ID"
  # Student
  id = "74cf790e-84c4-4322-84b8-fca7206f1085" if human_readable_id == "MARVIN MILLER"
  # Section
  id = "ceffbb26-1327-4313-9cfc-1c3afd38122e" if human_readable_id == "8TH GRADE ENGLISH SEC 6"
  # Program
  id = "9b8c3aab-8fd5-11e1-86ec-0021701f543f" if human_readable_id == "ACC TEST PROG 2"
  # Session
  id = "1cb50f82-7200-441a-a1b6-02d6532402a0" if human_readable_id == "FALL 2011"
  # StudentSchoolAssociation
  id = "ec2e4218-6483-4e9c-8954-0aecccfd4731" if human_readable_id == "MARVIN MILLER EAST DB JR HI"

  # Return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^a valid json document for a ([^"]*)$/ do |entity|
  @fields = deep_copy($entity_data[entity])
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

When /^I look at "([^\"]*)" in the "([^\"]*)"$/ do |id, coll|
  conn = Mongo::Connection.new(PropLoader.getProps['ingestion_db'])
  mdb = conn.db(@midgar_db_name)
  @doc = mdb.collection(coll).find("_id" => id).to_a
  assert(!@doc.nil?, "Cannot find the document with _id=#{id} in #{coll}")
  assert(@doc.size == 1, "Number of entities returned != 1 (received #{@doc.size}")
end

###############################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
###############################################################################

Then /^I should receive a new ID$/ do
  step "I should receive a new ID for the association I just created"
end

Then /^I (should|should not) find "([^\"]*)" in "([^\"]*)"$/ do |should_or_not, id, field|
  should = should_or_not == "should"? true : false
  found = false
  sub_doc = @doc[0][field]
  unless sub_doc.nil?
    sub_doc.each do |row|
      if row["_id"] == id
        found = true
        break
      end
    end
  end
  assert(found == should, "Failed should / should not check")
end

###############################################################################
# DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA
###############################################################################

$entity_data = {
  "studentSectionAssociation" => {
    "studentId" => "74cf790e-84c4-4322-84b8-fca7206f1085",
    "sectionId" => "ceffbb26-1327-4313-9cfc-1c3afd38122e",
    "repeatIdentifier" => "Repeated, counted in grade point average",
    "beginDate" => "2011-12-01",
    "endDate" => "2012-01-01",
    "homeroomIndicator" => true
  },
  "studentProgramAssociation" => {
    "studentId" => "74cf790e-84c4-4322-84b8-fca7206f1085",
    "programId" => "9b8c3aab-8fd5-11e1-86ec-0021701f543f",
    "beginDate" => "2012-01-12",
    "endDate" => "2012-05-01",
    "reasonExited" => "Refused services",
    "educationOrganizationId" =>"ec2e4218-6483-4e9c-8954-0aecccfd4731"
  },
  "attendance" => {
    "entityType" => "attendance",
    "studentId" => "74cf790e-84c4-4322-84b8-fca7206f1085",
    "schoolId" => "ec2e4218-6483-4e9c-8954-0aecccfd4731",
    "schoolYearAttendance" => [{
      "schoolYear" => "2011-2012",
      "attendanceEvent" => [{
        "date" => "2011-09-16",
        "event" => "Tardy"
      }]
    }]
  },
}
