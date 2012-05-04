require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../../utils/sli_utils.rb'
require_relative '../../../utils/api_utils.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^<([^"]*)>$/ do |human_readable_id|

  # values to support the test for whether or not to display links
  id = "educationOrganizations"                                if human_readable_id == "URI FOR ENTITY THAT CAN RETURN LINKS"
  id = "a189b6f2-cc17-4d66-8b0d-0478dcf0cdfb"   if human_readable_id == "ID OF ENTITY THAT CAN RETURN LINKS"

  #values to support direct entity reference tests
  id = @referring_collection_expose_name        if human_readable_id == "REFERRING COLLECTION URI"
  id = @testing_id                              if human_readable_id == "REFERRING ENTITY ID"
  id = @referred_collection_expose_name         if human_readable_id == "URI OF REFERENCED ENTITY"
  id = @reference_value                         if human_readable_id == "REFERRED ENTITY ID"
  id = @referring_field                         if human_readable_id == "REFERENCE FIELD"
  id = @new_valid_value                         if human_readable_id == "NEW VALID VALUE"

  #query URI
  id = @referring_collection_expose_name + "?" + @referring_field + "=" + @reference_value                    if human_readable_id == "URI OF ENTITIES THAT REFER TO TARGET"

  #general
  id = "11111111-1111-1111-1111-111111111111"   if human_readable_id == "INVALID REFERENCE"
  id = "self"                                   if human_readable_id == "SELF LINK NAME"
  id = @newId                                   if human_readable_id == "NEWLY CREATED ASSOCIATION ID"
  id = "Validation failed"                      if human_readable_id == "VALIDATION"

  #return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################

Given /^referring collection "([^"]*)" exposed as "([^"]*)"$/ do |referring_collection, referring_collection_expose_name|
  @referring_collection = referring_collection
  @referring_collection_expose_name = referring_collection_expose_name
end

Given /^referring field "([^"]*)" with value "([^"]*)"$/ do |referring_field, reference_value|
  @referring_field = referring_field
  @reference_value = reference_value
end

Given /^referred collection "([^"]*)" exposed as "([^"]*)"$/ do |referred_collection, referred_collection_expose_name|
  @referred_collection = referred_collection
  @referred_collection_expose_name = referred_collection_expose_name
end

Given /^the link from references to referred entity is "([^"]*)"$/ do |link_from_references|
  @link_from_references = link_from_references
end

Given /^the link from referred entity to referring entities is "([^"]*)"$/ do |link_from_referred|
  @link_from_referred = link_from_referred
end

Given /^the ID to use for testing is "([^"]*)"$/ do |testing_id|
  @testing_id = testing_id
end

Given /^the ID to use for testing a valid update is "([^"]*)"$/ do |new_valid_value|
  @new_valid_value = new_valid_value
end

###############################################################################
# WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN WHEN
###############################################################################

When /^I request "([^"]*)"$/ do |links|
  @should_get_links = (links=="links")
end

###############################################################################
# THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN THEN
###############################################################################

Then /^the response should contain links if I requested them$/ do
  assert(@result != nil, "Response contains no data")
  assert(@result.is_a?(Hash), "Response contains #{@result.class}, expected Hash")
  has_links = @result.has_key?('links')
  if (@should_get_links)
    assert(has_links==true, "Links were requested in response, but they were not included.")
  else
    assert(has_links==false, "Links were not requested in response, but they were included.")
  end
end