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


require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
require 'uri'
include REXML
require_relative '../../../../utils/sli_utils.rb'

###############################################################################
# TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM TRANSFORM
###############################################################################

Transform /^\/(<[^"]*>)$/ do |human_readable_id|

  #general
  id = "/v1/" + @entityUri                               if human_readable_id == "<ENTITY URI>"
  #return the translated value
  id
end

###############################################################################
# GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN GIVEN
###############################################################################


Given /^entity URI "([^"]*)"$/ do |arg1|
  @entityUri = arg1
end

Then /^I should not have any duplicate links/ do

  linkHash = Hash.new

  @result.each do |res|
    linkList = res["links"]
    notFound = true
    linkHash.clear
     linkList.each do |link|
        rel = link["rel"]
        href = link["href"]
        if linkHash[rel].nil?
           linkHash[rel] = href
        else
          notFound = false
        end
        assert(notFound,"Response contains duplicate link for " + rel +"With links "+ href +"And "+ linkHash[rel])
      end
  end

end

