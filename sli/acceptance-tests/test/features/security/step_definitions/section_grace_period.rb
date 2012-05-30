require_relative '../../utils/sli_utils.rb'

Before do
  @gracePeriod = 2000
  time = Time.new
  @currentDate = time.day.to_s + "/" + time.month.to_s + "/" + time.year.to_s
end

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|

  # students
  id = "74cf790e-84c4-4322-84b8-fca7206f1085"    if human_readable_id == "'MARVIN MILLER'"
  id = "5738d251-dd0b-4734-9ea6-417ac9320a15"    if human_readable_id == "'MATT SOLLARS'"
  id = "e04118fd-5025-4d3b-b58d-3ed0d4f270a6"    if human_readable_id == "'CARMEN ORTIZ JR'"
  id = "bf88acdb-71f9-4c19-8de8-2cdc698936fe"    if human_readable_id == "'CHARLA CHRISTOFF'"
  id = "51db306f-4fa5-405b-b587-5fac7605e4b3"    if human_readable_id == "'STEVE DONG'"
  id = "e1dd7a73-5000-4293-9b6d-b5f02b7b3b34"    if human_readable_id == "'LUCRETIA NAGAI'"

  # schools
  id = "6756e2b9-aba1-4336-80b8-4a5dde3c63fe"    if human_readable_id == "'SUNSET CENTRAL HIGH'"
  id = "92d6d5a0-852c-45f4-907a-912752831772"    if human_readable_id == "'DAYBREAK CENTRAL HIGH'"
  id = "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb"    if human_readable_id == "'SOUTH DAYBREAK ELEMENTARY'"

  # sessions
  id = "377c734f-7c15-455f-9209-ac15b3118236"    if human_readable_id == "'FALL 2010'"
  id = "0410354d-dbcb-0214-250a-404401060c93"    if human_readable_id == "'FALL 2001'"

  # session end dates
  id = "2010-12-16"                              if human_readable_id == "'FALL 2010 END DATE'"
  id = "2001-12-16"                              if human_readable_id == "'FALL 2001 END DATE'"

  # student-school exit dates
  id = "2009-05-11"                              if human_readable_id == "'MATT SOLLARS SOUTH DAYBREAK EXIT DATE'"
  id = "2003-04-12"                              if human_readable_id == "'STEVE DONG SOUTH DAYBREAK EXIT DATE'"

  #return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^I teach "([^\"]*)"$/ do |studentId|
  # nothing to do here...id already transformed
end

Given /^I do not teach "([^\"]*)"$/ do |studentId|
  # nothing to do here...id already transformed
end

Given /^my school is "([^\"]*)"$/ do |schoolId|
  # nothing to do here...id already transformed
end

Given /^"([^\"]*)" studies in "([^\"]*)"$/ do |studentId, schoolId|
  # nothing to do here...id already transformed
end

Given /^I taught "([^\"]*)" in "([^\"]*)"$/ do |studentId, sessionId|
  # nothing to do here...id already transformed
end

Given /^"([^\"]*)" is within the grace period$/ do |sessionEndDate|
  endDateArr = sessionEndDate.split("-")
  endDate = endDateArr[2].to_s + "/" + endDateArr[1].to_s + "/" + endDateArr[0].to_s
  daysPassed = Date.parse(@currentDate).mjd - Date.parse(endDate).mjd
  assert(daysPassed <= 2000, "Session is not within grace period (2000 days)")
end

Given /^"([^\"]*)" is outside of the grace period$/ do |sessionEndDate|
  endDateArr = sessionEndDate.split("-")
  endDate = endDateArr[2].to_s + "/" + endDateArr[1].to_s + "/" + endDateArr[0].to_s
  daysPassed = Date.parse(@currentDate).mjd - Date.parse(endDate).mjd
  assert(daysPassed >= 2000, "Session is not outside grace period (2000 days)")
end

Given /^"([^\"]*)" is not enrolled in "([^\"]*)"$/ do |studentId, schoolId|
  # nothing to do here...id already transformed
end

Given /^"([^\"]*)" exited "([^\"]*)" on "([^\"]*)"$/ do |studentId, schoolId, exitDate|
  uri = "/v1/studentSchoolAssociations?studentId=#{studentId}&schoolId=#{schoolId}&sortBy=exitWithdrawDate&sortOrder=descending"
  step "I navigate to GET \"#{uri}\""
  assert(@result[0]["exitWithdrawDate"] == exitDate, "Expected #{exitDate}, received #{@result[0]["exitWithdrawDate"]}")
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

When /^I make an API call to get "([^\"]*)"$/ do |studentId|
  uri = "/v1/students/#{studentId}"
  puts "GET #{uri}"
  step "I navigate to GET \"#{uri}\""
end

###############################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
###############################################################################

Then /^I should see that "([^\"]*)" is "([^\"]*)" in the JSON response$/ do |key, value|
  assert(@result[key] == convert(value), "Expected #{value}, received #{@result[key]}")
end