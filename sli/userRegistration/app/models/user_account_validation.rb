
require 'rest-client'
require 'json'

class UserAccountValidation
    
  # success message
  ACCOUNT_VERIFICATION_COMPLETE = {
    "status" => "Registration Complete!",
    "message" => "An administrator will email you when your account is ready."
  }
  
  INVALID_VERIFICATION_CODE = {
    "status" => "Account validation failed!",
    "message" => "Invalid account verification code."
  }
  
  # error condition for attemptint to re-verify verified account
  ACCOUNT_PREVIOUSLY_VERIFIED = {
    "status" => "Account validation failed!",
    "message" => "Account previously verified."
  }
  
  # error condition for attemptint to re-verify verified account
  UNEXPECTED_VERIFICATION_ERROR = {
    "status" => "Account validation failed!",
    "message" => "Unexpected verification error. Try again later."
  }
  
  REST_HEADER = {
    "Content-Type" => "application/json",
    "content-type" => "application/json",
    "accept" => "application/json"
  }
    
  def self.validate_account(guid)
    emailToken=guid
    user_info=ApplicationHelper.get_user_with_emailtoken(emailToken)
    url = APP_CONFIG['api_base'] + "?userName=" + user_info[:email]+"&environment="+APP_CONFIG["is_sandbox"]? "Sandbox":"Production"
    
    res = RestClient.get(url, REST_HEADER){|response, request, result| response }
    
    if (res.code == 200)
      parsedJsonHash = JSON.parse(res.body)
      if (parsedJsonHash["validated"] == true)
        return ACCOUNT_PREVIOUSLY_VERIFIED
      else
        parsedJsonHash["validated"] = true
        putRes = RestClient.put(url, parsedJsonHash.to_json, REST_HEADER){|response, request, result| response }
        if (putRes.code == 204)
          ApplicationHelper.verify_email(emailToken)
          return ACCOUNT_VERIFICATION_COMPLETE
        else
          return UNEXPECTED_VERIFICATION_ERROR
        end
      end
    elsif (res.code == 404)
      return INVALID_VERIFICATION_CODE
    else
      return UNEXPECTED_VERIFICATION_ERROR
    end 
  end
end
