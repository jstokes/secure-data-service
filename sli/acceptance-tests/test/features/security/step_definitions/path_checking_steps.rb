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

require_relative '../../utils/sli_utils.rb'

Given /^my contextual access is defined by table:$/ do |table|
  # table is a Cucumber::Ast::Table
  @ctx={}
  table.hashes.each do |hash|
    @ctx[hash["Context"]]=hash["Ids"]
  end
end

When /^I call "(.*?)" using ID "(.*?)"$/ do |arg1, arg2|
  pending # express the regexp above with the code you wish you had
end

Then /^the executed URI should be "(.*?)"$/ do |arg1|
  version = "v1"
  root = expectedUri.match(/\/(.+?)\/|$/)[1]
  expected = version+expectedUri
  actual = @headers["x-executedpath"][0]

  #First, make sure the paths of the URIs are the same
  expectedPath = expected.gsub("@ids", "[^/]+")
  assert(actual.match(expectedPath), "Rewriten URI path didn't match, expected:#{expectedPath}, actual:#{actual}")

  #Then, validate the list of ids are the same
  ids = []
  if @ctx.has_key? root
    idsString = actual.match(/v1\/[^\/]*\/([^\/]*)\/?/)[1]
    actualIds = idsString.split(",")
    expectedIds = @ctx[root].split(",")
    
    assert(actualIds.length == expectedIds.length,"Infered Context IDs not equal: expected:#{expectedIds.inspect}, actual:#{actualIds.inspect}")
    expectedIds.each do |id|
      assert(actualIds.include?(id),"Infered Context IDs not equal: expected:#{expectedIds.inspect}, actual:#{actualIds.inspect}")
    end
  end
end
