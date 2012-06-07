class AppsController < ApplicationController
  before_filter :check_rights

  # Let us add some docs to this confusing controller.
  # NOTE this controller is performing two actions:
  # It allows developers to create new apps.
  # It also allows slc operators approve an app for use in the SLC.
  def check_rights
    unless is_developer? or is_operator?
      raise ActiveResource::ForbiddenAccess, caller
    end
  end

  # GET /apps
  # GET /apps.json
  def index
    @title = "Application Registration Tool"
    @apps = App.all.sort { |a,b| b.metaData.updated <=> a.metaData.updated }
    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @apps }
    end
  end

  # GET /apps/1
  # GET /apps/1.json
  def show
    @app = App.find(params[:id])
  
    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @app }
    end
  end

  # GET /apps/new
  # GET /apps/new.json
  def new
    @title = "New Application"
    @app = App.new
      
    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @app }
    end
  end

  def approve
    @app = App.find(params[:id])
    respond_to do |format|
      reg = @app.attributes["registration"]
      reg.status = "APPROVED"
      if @app.update_attribute("registration", reg)
        format.html { redirect_to apps_path, notice: 'App was successfully updated.' }
        format.json { head :ok }
      else
        format.html { render action: "edit" }
        format.json { render json: @app.errors, status: :unprocessable_entity }
      end
    end
  end

  def unregister
    @app = App.find(params[:id])
    respond_to do |format|
      reg = @app.attributes["registration"]
      if reg.status == 'PENDING'
        reg.status = 'DENIED'
      else
        reg.status = "UNREGISTERED"
      end
      if @app.update_attribute("registration", reg)
        format.html { redirect_to apps_path, notice: 'App was successfully updated.' }
        format.json { head :ok }
      else
        format.html { render action: "edit" }
        format.json { render json: @app.errors, status: :unprocessable_entity }
      end
    end
  end

  # # GET /apps/1/edit
  # def edit
  #   @app = App.find(params[:id])
  # end

  # POST /apps
  # POST /apps.json
  def create
    # if operator?
    #       redirect_to apps_path, notice: "Only developers can create new applications" and return
    #     end
    #ugg...can't figure out why rails nests the app_behavior attribute outside the rest of the app
    params[:app][:behavior] = params[:app_behavior]
    params[:app][:authorized_ed_orgs] = params[:authorized_ed_orgs]
    params[:app][:authorized_ed_orgs] = [] if params[:app][:authorized_ed_orgs] == nil

    @app = App.new(params[:app])
    logger.debug{"Application is valid? #{@app.valid?}"}
    @app.is_admin = boolean_fix @app.is_admin
    @app.installed = boolean_fix @app.installed

    respond_to do |format|
      if @app.save
        logger.debug {"Redirecting to #{apps_path}"}
        format.html { redirect_to apps_path, notice: 'App was successfully created.' }
        format.json { render json: @app, status: :created, location: @app }
        # format.js
      else
        format.html { render action: "new" }
        format.json { render json: @app.errors, status: :unprocessable_entity }
        # format.js
      end
    end
  end

  # PUT /apps/1
  # PUT /apps/1.json
  def update
    @app = App.find(params[:id])
    logger.debug {"App found (Update): #{@app.attributes}"}

    params[:app][:is_admin] = boolean_fix params[:app][:is_admin]
    params[:app][:installed] = boolean_fix params[:app][:installed]
    params[:app][:authorized_ed_orgs] = params[@app.name.gsub(" ", "_") + "_authorized_ed_orgs"]
    params[:app][:authorized_ed_orgs] = [] if params[:app][:authorized_ed_orgs] == nil

    #ugg...can't figure out why rails nests the app_behavior attribute outside the rest of the app
    params[:app][:behavior] = params[:app_behavior]

    respond_to do |format|
      if @app.update_attributes(params[:app])
          format.html { redirect_to apps_path, notice: 'App was successfully updated.' }
          format.json { head :ok }
      else
        format.html { render action: "edit" }
        format.json { render json: @app.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /apps/1
  # DELETE /apps/1.json
  def destroy
    @app = App.find(params[:id])
    @app.destroy
  
    respond_to do |format|
      format.js
      # format.html { redirect_to apps_url }
      # format.json { head :ok }
    end
  end

  def operator?
    !session[:roles].include? "Application Developer"
  end

  private
  def boolean_fix (parameter)
    case parameter
    when "1"
      parameter = true
    when "0"
      parameter = false
    end
  end
end
