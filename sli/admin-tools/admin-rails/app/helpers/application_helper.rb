require 'approval'

module ApplicationHelper


  REST_HEADER = {
    "Content-Type" => "application/json",
    "content_type" => "json",
    "accept" => "application/json"
  }

  API_BASE=APP_CONFIG["api_base"]+"/v1/userAccounts"
  UNKNOWN_EMAIL = {
    "email_address" => "UNKNOWN",
    "first_name" => "UNKNOWN",
    "last_name" => "UNKNOWN",
  }
  
  # Looks up the provided GUID (record) through the API, removes (deletes) that record,
  # and removes the associated user from the LDAP.
  #
  # Input Prameters:
  #   - guid : ID of record containing email address to send to
  #
  # Returns:
  #     Nothing
  #
  def self.remove_user_account(guid)
    if (guid.nil?)
      return
    end
    res = RestClient.get(API_BASE + "/" + guid, REST_HEADER){|response, request, result| response }
    if (res.code==200)
      jsonDocument = JSON.parse(res.body)
      remove_user(jsonDocument["userName"])
      res = RestClient.delete(API_BASE+"/"+guid, REST_HEADER){|response, request, result| response }
    end
  end

  # Gets the email address for the supplied GUID and sends them a confirmation-request
  # email.
  #
  # Input Prameters:
  #   - guid : ID of record containing email address to send to
  #
  # Returns:
  #     Nothing
  #
  def self.send_user_verification_email(validate_base, guid)
    
    user_email_info = get_email_info guid
    email_address = user_email_info["email_address"]
    email_token = get_email_token(email_address)
    if (email_token.nil?)
      return false
    end
    userEmailValidationLink = "#{APP_CONFIG['email_replace_uri']}/user_account_validation/#{email_token}"
    
    ApplicationMailer.verify_email(email_address,user_email_info['first_name'],userEmailValidationLink).deliver
    
    true
  end
  
  # Checks if the user account exists.
  # Input Parameters:
  #   - email - user id (email)
  #
  # Returns : true or false
  def self.user_exists?(email)
    ApprovalEngine.user_exists?(email)
  end
  
  # Returns a map containing values for email_address, first_name, and last_name.
  #
  # Input Parameters:
  #   - guid : identifier of record containing an email address
  #
  # Returns : first, last, and user name on associated record
  #
  def self.get_email_info(guid)
    
    url = API_BASE + "/" + guid
    res = RestClient.get(url, REST_HEADER){|response, request, result| response }

    if (res.code==200)
      jsonDocument = JSON.parse(res.body)
      return {
        "email_address" => jsonDocument["userName"],
        "first_name" => jsonDocument["firstName"],
        "last_name" => jsonDocument["lastName"],
      }
    else 
      return UNKNOWN_EMAIL
    end
  end
  
  # Add all relevant information for a new user to the backend.
  #
  # Input Parameters:
  #
  # user_info is a hash with the following fields:
  #   - first : first name
  #   - last  : last name
  #   - email : email address (also serves as the userid)
  #   - password : password used to log in
  #   - vendor : optional vendor name
  #
  # Returns: A hash string that has to be used for email verification
  # by embedding a link into a confirmation email.
  #
  # Input Example:
  #
  # user_info = {
  #     :first => "John",
  #     :last => "Doe",
  #     :email => "jdoe@example.com",
  #     :password => "secret",
  #     :vendor => "Acme Inc."
  # }
  # 
  def self.add_user(userAccountRegistration)
    new_user = {
      :first           => userAccountRegistration.firstName,
      :last            => userAccountRegistration.lastName,
      :email           => userAccountRegistration.email,
      :emailAddress    => userAccountRegistration.email,
      :password        => userAccountRegistration.password,
      :vendor          => userAccountRegistration.vendor,
      :status          => "submitted"
    }
    ApprovalEngine.add_disabled_user(new_user)
  end

  # Verify the email address against the backend.
  #
  # Input Parameter:
  #
  # email_hash : The email hash that was previously returned by the add_user
  # and included in a click through link that the user received in an email (as a query parameter).
  #
  def self.verify_email(emailtoken)
   # APP_LDAP_CLIENT.verify_email(emailtoken)
   ApprovalEngine.verify_email(emailtoken)
  end

  # Update the user information that was submitted via the add_user method.
  #
  # Input parameter: A subset of the "user_info" submitted to the "add_user" method.
  #
  def self.update_user_info(userAccountRegistration)
    new_user = {
      :first          => userAccountRegistration.firstName,
      :last           => userAccountRegistration.lastName,
      :email          => userAccountRegistration.email,
      :emailAddress   => userAccountRegistration.email,
      :password       => userAccountRegistration.password,
      :vendor         => userAccountRegistration.vendor,
      :status         => "submitted"
    }
    ApprovalEngine.update_user_info(new_user)
  end

  #get email token for a specific user
  def self.get_email_token(email_address)
    user_info= ApprovalEngine.get_user(email_address)
    if (user_info.nil?)
      return nil
    else
      return user_info[:emailtoken]
    end
  end

  # Returns an individual user via their email token or nil if the user does not exist.
  def self.get_user_with_emailtoken(email_token)
    return ApprovalEngine.get_user_emailtoken(email_token)
  end

  #remove user with address
  def self.remove_user(email_address)
    ApprovalEngine.remove_user(email_address)
  end

  def required?(obj, attr)
    target = (obj.class == Class) ? obj : obj.class
    target.validators_on(attr).map(&:class).include?(
        ActiveModel::Validations::PresenceValidator)
  end

end
class ErbBinding < OpenStruct
    def get_binding
      return binding()
    end
  end
