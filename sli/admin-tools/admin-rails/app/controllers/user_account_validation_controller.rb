
class UserAccountValidationController < ApplicationController
  
  # GET /user_account_registrations/validate/1
  # GET /user_account_registrations/validate/1.json
  def show
    @validation_result = UserAccountValidation.validate_account params[:id]
    render :show
  end # end def show
end
