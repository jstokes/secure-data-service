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

class UsersController < ApplicationController

 @@EXISTING_EMAIL_MSG = "An account with this email already exists"
  
  SANDBOX_ADMINISTRATOR = "Sandbox Administrator"
  APPLICATION_DEVELOPER = "Application Developer"
  INGESTION_USER = "Ingestion User"

  # GET /users
  # GET /users.json
  def index
    get_login_id
    @users = User.all
    respond_to do |format|
      format.html
      #format.json { render json: @users }
    end
  end
 
 
  # DELETE /users/1
  # DELETE /users/1.json
  def destroy 
    @users = User.all
    @users.each do |user|
      if user.uid == params[:id]
        user.id = user.uid
        user.destroy
        @users.delete(user)
        
     # @user_id = user.uid.gsub(/\@/,"\\\\\\\\@").gsub(/\./, "\\\\\\\\."); #escape dot in uid for javascript
      #  logger.info("user id after escape is #{@user_id}")
      end
    end
    get_login_id
    respond_to do |format|
    #format.js
    format.html {render "index"}
    end
  end
  
  # GET /apps/new
  # GET /apps/new.json
  def new
    check = Check.get ""
    @user = User.new
   set_edorg_options
   set_role_options
    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @user }
    end
  end
  
  # POST /users
  # POST /users.json
  def create
    logger.info{"running create new user"}
    @user = User.new()
    create_update 
    @user.uid = params[:user][:email]   
    logger.info("the new user is #{@user.to_json}")
    logger.info("the new user validation is #{@user.valid?}")
    logger.info("the new user validation error is #{@user.errors.to_json}")
    @user.errors.clear
    if @user.valid? == false || validate_email==false
      resend = true
    else
    begin  
    @user.save
    rescue ActiveResource::ResourceConflict
      resend = true
      @user.errors[:email] << @@EXISTING_EMAIL_MSG
    end
    end
    
     respond_to do |format|
       if resend
         set_edorg_options
         set_role_options
         set_roles
         format.html {render "new"}
       else
        format.html { redirect_to "/users", notice: 'Success! You have added a new user' } 
       end
     end
    
  end
  
  # GET /users/1/edit
  # GET /users/1/edit.json
  def edit
    @users = User.all
    check = Check.get ""
    set_edorg_options
    set_role_options
   @users.each do |user|
      if user.uid == params[:id]
        @user = user
        @user.id = user.uid
        logger.info{"find updated user #{user.to_json}"}
      end
    
    end
    
    set_roles
    @user.errors.clear
     
     logger.info{"find updated user #{@user.to_json}"}
    
  #  respond_to do |format|
      
   #   format.html {render "users/edit"}
      #format.json { render json: @users }
  #  end
  end
  
  # PUT /users/1/
  def update
    
    logger.info{"running the update user now"}
    @users = User.all
    @users.each do |user|
      if user.uid = params[:id]
        @user = user
      end
    end
    create_update
    
    @user.createTime="2000-01-01"
    @user.modifyTime="2000-01-01"
    
    logger.info{"the updated user is #{@user.to_json}"}
    @user.errors.clear
    logger.info{"the updated user validate is #{@user.valid?}"}
    logger.info{"the updated user validation errors is #{@user.errors.to_json}"}
    if @user.valid? == false || validate_email==false
      resend = true
    else
    @user.save
    end

     respond_to do |format|
       if resend
         @user.id = @user.uid
         set_edorg_options
         set_role_options
         set_roles
         format.html { render "edit"}
       else
        format.html { redirect_to "/users", notice: 'Success! You have updated the user' }
        end
     end
  end
  
  def create_update
    check = Check.get ""
    groups = []
    groups << params[:user][:primary_role]
    groups << params[:user][:optional_role_1] if params[:user][:optional_role_1]!="0" && !groups.include?(params[:user][:optional_role_1])
    groups << params[:user][:optional_role_2] if params[:user][:optional_role_2]!="0" && !groups.include?(params[:user][:optional_role_2])
    params[:user][:firstName]= params[:user][:fullName].split(" ")[0]
    params[:user][:lastName] = params[:user][:fullName].gsub(params[:user][:firstName],"").lstrip if params[:user][:fullName] !=nil && params[:user][:fullName]!=""
    @user.fullName = params[:user][:fullName]
    @user.fullName = nil if @user.fullName == ""
    @user.firstName = params[:user][:firstName]
    @user.lastName = params[:user][:lastName]
    @user.lastName = " " if @user.lastName==""
    @user.email = params[:user][:email]
    @user.tenant = check["tenantId"]
    @user.edorg = params[:user][:edorg]
   
    @user.groups = groups
    @user.homeDir = "/dev/null"
    return @user
    
  end
  
  # GET /users/1
  # GET /users/1.json
  def show
     respond_to do |format|
        format.html { redirect_to "/users" }
     end
     end
  
  def get_login_id
    check = Check.get ""
    @loginUserId=check["external_id"]
  end
  
  def set_edorg_options
    check = Check.get ""
     @edorgs = {"(optional)" => "", "" => "" ,check["edOrg"] => check["edOrg"]}
  end
  
  def set_role_options
    @sandbox_roles ={"Sandbox Administrator" => "Sandbox Administrator", "Application Developer" => "Application Developer", "Ingestion User" => "Ingestion User"} 
  end
  
  def set_roles
    if @user.groups.include?(SANDBOX_ADMINISTRATOR)
       @user.primary_role = SANDBOX_ADMINISTRATOR
       if @user.groups.include?(APPLICATION_DEVELOPER)
         @user.optional_role_1 = APPLICATION_DEVELOPER
       end
        if @user.groups.include?(INGESTION_USER)
         @user.optional_role_2 = INGESTION_USER
       end
     elsif @user.groups.include?(APPLICATION_DEVELOPER)
       @user.primary_role = APPLICATION_DEVELOPER
        if @user.groups.include?(INGESTION_USER)
         @user.optional_role_2 = INGESTION_USER
        end
     elsif @user.groups.include?(INGESTION_USER)
       @user.primary_role = INGESTION_USER
     end
    
  end
  
  def validate_email
    # don't validate empty values here, otherwise we get duplicate error messages
    valid=true
    if not @user.email =~ /^[-a-z0-9_]+([\.]{0,1}[-a-z0-9_]+)*\@([a-z0-9]+([-]*[a-z0-9]+)*\.)*([a-z0-9]+([-]*[a-z0-9]+))+$/i
      @user.errors[:email] << "Please enter a valid email address"
      valid =false
    elsif @user.email.length > 160
      @user.errors[:email] << "Email address is too long"
      valid=false
      end
      return valid
      end
  
end