class RealmsController < ApplicationController
  # GET /realms
  # GET /realms.json
  def index
    @realms = Realm.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @realms }
    end
  end

  # # GET /realms/1
  # # GET /realms/1.json
  # def show
  #   @realm = Realm.find(params[:id])
  # 
  #   respond_to do |format|
  #     format.html # show.html.erb
  #     format.json { render json: @realm }
  #   end
  # end
  # 
  # # GET /realms/new
  # # GET /realms/new.json
  # def new
  #   @realm = Realm.new
  # 
  #   respond_to do |format|
  #     format.html # new.html.erb
  #     format.json { render json: @realm }
  #   end
  # end
  # 
  # # GET /realms/1/edit
  # def edit
  #   @realm = Realm.find(params[:id])
  # end
  # 
  # # POST /realms
  # # POST /realms.json
  # def create
  #   @realm = Realm.new(params[:realm])
  # 
  #   respond_to do |format|
  #     if @realm.save
  #       format.html { redirect_to @realm, notice: 'Realm was successfully created.' }
  #       format.json { render json: @realm, status: :created, location: @realm }
  #     else
  #       format.html { render action: "new" }
  #       format.json { render json: @realm.errors, status: :unprocessable_entity }
  #     end
  #   end
  # end
  # 
  # # PUT /realms/1
  # # PUT /realms/1.json
  # def update
  #   @realm = Realm.find(params[:id])
  # 
  #   respond_to do |format|
  #     if @realm.update_attributes(params[:realm])
  #       format.html { redirect_to @realm, notice: 'Realm was successfully updated.' }
  #       format.json { head :ok }
  #     else
  #       format.html { render action: "edit" }
  #       format.json { render json: @realm.errors, status: :unprocessable_entity }
  #     end
  #   end
  # end
  # 
  # # DELETE /realms/1
  # # DELETE /realms/1.json
  # def destroy
  #   @realm = Realm.find(params[:id])
  #   @realm.destroy
  # 
  #   respond_to do |format|
  #     format.html { redirect_to realms_url }
  #     format.json { head :ok }
  #   end
  # end
end
