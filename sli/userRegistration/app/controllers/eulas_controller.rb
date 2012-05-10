class EulasController < ApplicationController
  
  SUBJECT = "SLI Account Verification Request"
  
  URL=APP_CONFIG['api_base']
  URL_HEADER = {
      "Content-Type" => "application/json",
      "content_type" => "json",
      "accept" => "application/json"
      }

  # GET /eula 
  def show
    if !Session.valid?(session)
      not_found
    end
    
    respond_to do |format|
      format.html 
    end
  end
  
  def get_email_info(guid)
    url = URL + "/" + guid
    res = RestClient.get(url, URL_HEADER){|response, request, result| response }
    
    if (res.code==200)
        jsonDocument = JSON.parse(res.body)
        return {
            "email_address" => jsonDocument["userName"],
            "first_name" => jsonDocument["firstName"],
            "last_name" => jsonDocument["lastName"],
        }
    else 
        return {
            "email_address" => "NONE",
            "first_name" => "NONE",
            "last_name" => "NONE",
        }
    end
  end
  
  def email_user_account_verification
    guid = session[:guuid]
    
    user_email_info = get_email_info guid
    
    email_conf = {
      :host => APP_CONFIG["email_host"],
      :port => APP_CONFIG["email_port"],
      :sender_name => APP_CONFIG["email_sender_name"],
      :sender_email_addr => APP_CONFIG["email_sender_address"],
    }
    puts(user_email_info["email_address"])
    emailtoken=ApplicationHelper.get_email_token(user_email_info["email_address"])
    
    message = "Your SLI account has been created pending email verification.\n" <<
      "\n\nPlease visit the following link to confirm your account:\n" <<
      "\n\n" + APP_CONFIG["validate_base"] + "/#{emailtoken}\n\n"
    
    email = Emailer.new email_conf
    email.send_approval_email(
      user_email_info["email_address"], 
      user_email_info["first_name"], 
      user_email_info["last_name"],
      SUBJECT, 
      message
    )
  end

  def create
    if Eula.accepted?(params)
      email_user_account_verification()
      render :finish
    else 
      gUID= session[:guuid]
      res = RestClient.get(URL+"/"+gUID, URL_HEADER){|response, request, result| response }
      if (res.code==200)
            jsonDocument = JSON.parse(res.body)
            puts(jsonDocument)
            puts(jsonDocument["userName"])
            ApplicationHelper.remove_user(jsonDocument["userName"])
            res = RestClient.delete(URL+"/"+gUID, URL_HEADER){|response, request, result| response }
        end
      redirect_to APP_CONFIG['redirect_slc_url']
    end
  end
end
