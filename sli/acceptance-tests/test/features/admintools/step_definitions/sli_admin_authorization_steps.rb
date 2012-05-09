require "selenium-webdriver"
require 'json'

require_relative '../../utils/sli_utils.rb'


Given /^I am a valid SLC Operator$/ do
  @user = 'sunsetoperator' # an :operator
  @pass = 'sunsetoperator1234'
end

Given /^I am a valid Super Administrator$/ do
  @user = 'sunsetadmin' # a :super_admin
  @pass = 'sunsetadmin1234'
end

When /^I try to authenticate on the Application Approval Tool$/ do
  # NOTE approval tool and registration tool share the same url
  step "I hit the Application Registration Tool URL"
  step "I login"
end

When /^I try to authenticate on the Application Registration Tool$/ do
  step "I hit the Application Registration Tool URL"
  step "I login"
end

When /^I try to authenticate on the Application Authorization Tool$/ do
  step "I hit the Admin Application Authorization Tool"
  step "I login"
end

When /^I try to authenticate on the Role Mapping Tool$/ do
  step "I navigate to the Complex-Configurable Role Mapping Page"
  step "I login"
end

And /^I login$/ do
  step "I get redirected to the IDP login page"
  step "I authenticate with username \"#@user\" and password \"#@pass\""
end

