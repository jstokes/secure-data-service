require 'test_helper'
require "mocha"

class UserAccountValidationTest < ActiveSupport::TestCase    
  def test_validate_account_ok
    ApplicationHelper.expects(:verify_email).with("1234567890").once()
    assert_equal UserAccountValidation::ACCOUNT_VERIFICATION_COMPLETE, UserAccountValidation.validate_account("1234567890")
  end
  
  def test_account_duplicate
    ApplicationHelper.expects(:verify_email).with("1234567890").raises("Could not find user for email id 1234567890.")
    assert_equal UserAccountValidation::INVALID_VERIFICATION_CODE, UserAccountValidation.validate_account("1234567890")
  end
  
  def test_account_already_verified
    ApplicationHelper.expects(:verify_email).with("1234567890").raises("Current status 'approved' does not allow transition 'verify_email'.")
    assert_equal UserAccountValidation::ACCOUNT_PREVIOUSLY_VERIFIED, UserAccountValidation.validate_account("1234567890")
  end
  
  def test_account_already_misc_exception
    ApplicationHelper.expects(:verify_email).with("1234567890").raises("La Marche des Hobos")
    assert_equal UserAccountValidation::UNEXPECTED_VERIFICATION_ERROR, UserAccountValidation.validate_account("1234567890")
  end

end


