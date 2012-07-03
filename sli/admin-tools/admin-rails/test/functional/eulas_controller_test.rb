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
require 'mocha'

class EulasControllerTest < ActionController::TestCase
  setup do
    @request.env['SERVER_PROTOCOL']='HTTP/1.1'
  end
 
  test "should respond to post for accept/reject" do
    Eula.stubs(:accepted?).returns(true)
    ApplicationHelper.stubs(:send_user_verification_email).returns(true)
    get :create
    assert_template :finish

    ApplicationHelper.stubs(:send_user_verification_email).returns(false)
    get :create
    assert_template :account_error

    Eula.stubs(:accepted?).returns(false)
    ApplicationHelper.stubs(:remove_user_account)
    get :create
    assert_redirected_to APP_CONFIG['redirect_slc_url']
  end

  test "should show eula" do
    assert_response :success
  end

  test "should check for valid session before rendering eula" do 
    Session.stubs(:valid?).returns(false)
    assert_raise(ActionController::RoutingError) { get :show}

    Session.stubs(:valid?).returns(true)
    assert_nothing_raised { get :show }
  end
end
