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


require 'test_helper'
require "mocha"

class ChangePasswordsControllerTest < ActionController::TestCase
  setup do
    @change_password = ChangePassword.new(
        :old_pass => 'test1234',
        :new_pass => 'testabcd',
        :confirmation => 'testabcd'
    )
  end

  test "should get index" do
    get :index
    assert_response :success
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create change_password" do
    post :create, change_password: @change_password.attributes
    assert_response :success
  end


end
