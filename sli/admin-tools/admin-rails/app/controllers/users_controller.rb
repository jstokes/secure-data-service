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

  # GET /users
  # GET /users.json
  def index
    check = Check.get ""
    @loginUserId=check["external_id"]
    @users = User.all
    respond_to do |format|
      format.html # index.html.erb
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
        @user_id = user.uid.gsub(/\./, "\\\\\\\\."); #escape dot in uid for javascript
      end
    end
    
    respond_to do |format|
      format.js
    end
  end
  
  # GET /apps/new
  # GET /apps/new.json
  def new
    check = Check.get ""
    @user = User.new
     @edorgs = {"" => "", check["edOrg"] => 2} 
     @roles ={"Sandbox Administrator" => 1, "Application Developer" => 2, "Ingestion User" => 3}
    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @user }
    end
  end
  
  # GET /users/1/edit
  # GET /users/1/edit.json
  def edit
    @users = User.all
    respond_to do |format|
      format.html # index.html.erb
      #format.json { render json: @users }
    end
  end
end