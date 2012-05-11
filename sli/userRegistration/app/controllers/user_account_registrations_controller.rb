require 'rest-client'
require 'json'
class UserAccountRegistrationsController < ApplicationController
    before_filter :check_for_cancel, :only => [:create, :update]
  
  # GET /user_account_registrations/new
  # GET /user_account_registrations/new.json
  def new
    @user_account_registration = UserAccountRegistration.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @user_account_registration }
    end
  end


  # POST /user_account_registrations
  # POST /user_account_registrations.json
  def create
    @user_account_registration = UserAccountRegistration.new(params[:user_account_registration])
    @user_account_registration.errors.clear
    if @user_account_registration.valid? ==false
       redirectPage=false
      else
        response=UserAccountRegistrationsHelper.register_user(@user_account_registration)
        puts("^^^^^^^^^^^#{response}")
        redirectPage=response["redirect"]
        @user_account_registration.errors.add(:email,response["error"])
        session[:guuid]=response["guuid"]
      end
    respond_to do |format|
        if redirectPage==true
            
            format.html  { redirect_to("/eula")}
            format.json  { render :json => @user_account_registration,
                                   action: "/eulas"}
        else
            format.html { render action: "new" }
            format.json { render json: @user_account_registration.errors, status: :unprocessable_entity }
        end
    end
  end

private

    #redirect cancel
   def check_for_cancel
       if params[:commit] == "Cancel"
         redirect_to  APP_CONFIG['redirect_slc_url']
       end
     end

end
