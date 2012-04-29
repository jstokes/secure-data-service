class AccountManagementsController < ApplicationController
  
  # GET /account_managements
  # GET /account_managements.json
  def index
    #use global variable to avoid save data to database before connect to LDAP is implemented
     @account_managements=$account_managements
    if @account_managements==nil
      counters = (0...20).to_a
      @account_managements=Array.new()
      counters.each do |counter|
        account_management = AccountManagement.new()
        account_management.name="test name " +String(counter+1)
        account_management.vendor="test vendor "+String(counter+1)
        if counter%4==0
          account_management.approvalDate="2012-01-01"
          account_management.status="Approved"
        elsif counter%4==1
          account_management.approvalDate=""
          account_management.status="Pending"
        elsif counter%4==2
          account_management.approvalDate=""
          account_management.status="Rejected"
        else
          account_management.approvalDate=""
          account_management.status="Disabled"
        end
        account_management.email="test"+String(counter+1)+"@test.com"
        @account_managements[counter]=account_management
      end
      end

    # sort the @account_managements based on status
   
    @account_managements=sort(@account_managements)

    #update the global variable $account_managements
    $account_managements=@account_managements
    
    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @account_managements }
    end
    end
  

  # GET /account_managements/1
  # GET /account_managements/1.json
  def show
    @account_management = AccountManagement.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @account_management }
    end
  end

  # GET /account_managements/new
  # GET /account_managements/new.json
  def new
    @account_management = AccountManagement.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render json: @account_management }
    end
  end

  # GET /account_managements/1/edit
  def edit
    @account_management = AccountManagement.find(params[:id])
  end

  # POST /account_managements
  # POST /account_managements.json
  def create
    commit = params["commit"]
    email = params["email"]
    @account_managements=$account_managements
    
      @account_managements.each do |account_management|
        if account_management.email==email
          @account_managements.delete(account_management)
          if commit=="Approve"
          account_management.status="Approved"
          account_management.approvalDate=Time.now.strftime("%Y-%m-%d")
          elsif commit=="Reject"
            account_management.status="Rejected"
          account_management.approvalDate=""
          elsif commit =="Disable"
            account_management.status="Disabled"
          account_management.approvalDate=""
          end
          @account_managements.push(account_management)
        end
      end
      @notice='Account was successfully updated.'
      @account_managements=sort(@account_managements)
    
    respond_to do |format|
     
        format.html { render "index"}
        #format.json { head :ok }
     
    end
    
  # redirect_to action: "index", notice: 'Account was successfully updated.'
    
  end
  

  # PUT /account_managements/1
  # PUT /account_managements/1.json
  def update
    @account_management = AccountManagement.find(params[:id])

    respond_to do |format|
      if @account_management.update_attributes(params[:account_management])
        format.html { redirect_to @account_management, notice: 'Account management was successfully updated.' }
        format.json { head :ok }
      else
        format.html { render action: "edit" }
        format.json { render json: @account_management.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /account_managements/1
  # DELETE /account_managements/1.json
  def destroy
    @account_management = AccountManagement.find(params[:id])
    @account_management.destroy

    respond_to do |format|
      format.html { redirect_to account_managements_url }
      format.json { head :ok }
    end
  end
  
  def sort(account_managements)
     pending_account_managements,approved_account_managements,rejected_account_managements,disabled_account_managements=Array.new(),Array.new(),Array.new(),Array.new()
    account_managements.each do |account_management|
      if account_management.status =="Pending"
      pending_account_managements.push(account_management)
      elsif account_management.status =="Approved"
      approved_account_managements.push(account_management)
      elsif account_management.status =="Rejected"
      rejected_account_managements.push(account_management)
      elsif account_management.status =="Disabled"
      disabled_account_managements.push(account_management)
      end
    end
    account_managements=pending_account_managements.concat(approved_account_managements).concat(rejected_account_managements).concat(disabled_account_managements)
  end
  
end
