require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
include REXML
require_relative '../../../utils/sli_utils.rb'


# transform <Place Holder Id>
Transform /^<.+>$/ do |template|
  id = "67ed9078-431a-465e-adf7-c720d08ef512" if template == "<'linda.kim' ID>"
  id = "0e26de79-222a-4d67-9301-5113ad50a03b" if template == "<'demo' ID>";
  id = "c13cf9a6-6779-4de6-8b48-f3207952bfb8" if template == "<'aggregator' ID>"
  id = "319fc71c-6bc8-4ca7-b5ce-92e0c9ca763d" if template == "<'baduser' ID>";
  id
end

# transform /path/<Place Holder Id>
Transform /^(\/[\w-]+\/)(<.+>)$/ do |uri, template|
  uri + Transform(template)
end

# transform /v1/entity/<Place Holder Id>
Transform /^(\/[\w-]+\/)([\w-]+\/)(<.+>)$/ do |version, uri, template|
  version + uri + Transform(template)
end

# transform /path/<Place Holder Id>
Transform /^(\/[\w-]+\/)([\w-]+\/)(<.+>)(\/[\w-]+)$/ do |version, uri, template, assoc|
  version + uri + Transform(template) + assoc
end

# transform /path/<Place Holder Id>
Transform /^(\/[\w-]+\/)([\w-]+\/)(<.+>)(\/[\w-]+)(\/[\w-]+)$/ do |version, uri, template, assoc, entity|
  version + uri + Transform(template) + assoc + entity
end

