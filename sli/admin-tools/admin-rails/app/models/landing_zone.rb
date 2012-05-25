class LandingZone
  
  def self.possible_edorgs
    if APP_CONFIG["is_sandbox"]
      edOrgs = []
      edOrgs << EducationOrganization.new(:stateUniqueId => 'IL', :nameOfInstitution => "High-Level Ed-Org from Sample Dataset 1")
      edOrgs << EducationOrganization.new(:stateUniqueId => 'IL-SUNSET', :nameOfInstitution => "High-Level Ed-Org from Sample Dataset 2")
      return edOrgs
    else
      []
    end
  end
  
  def self.provision(edorg_id, tenant, uid)
    Rails.logger.debug "entered provision: edorg_id = #{edorg_id}, tenant = #{tenant}, uid = #{uid}"

    user_info = APP_LDAP_CLIENT.read_user(uid)
    if(!user_info)
      raise ProvisioningError.new "User does not exist in LDAP"
    end

    result = OnBoarding.create(:stateOrganizationId => edorg_id, :tenantId => tenant)
    if !result.valid?
      raise ProvisioningError.new "Could not provision landing zone"
    end
    @landingzone = result.attributes[:landingZone]
    @server = result.attributes[:serverName]
    Rails.logger.info "landing zone is #{@landingzone}, server is #{@server}"
    
    user_info[:homedir] = result.attributes[:landingZone]
    user_info[:edorg] = edorg_id
    if APP_CONFIG["is_sandbox"]
      user_info[:tenant] = tenant
    end

    begin
      APP_LDAP_CLIENT.update_user_info(user_info)
    rescue => e
      Rails.logger.error "Could not update ldap for user #{uid} with #{user_info}.\nError: #{e.message}."
    end 
    
    # TODO: also check email address for being valid
    if(user_info[:emailAddress] != nil && user_info[:emailAddress].length != 0)
      # TODO: move this out to a template and not hardcode
      email = {
        :email_addr => user_info[:emailAddress],
        :name       => "#{user_info[:first]} #{user_info[:last]}",
        :subject    => "Landing Zone Provisioned",
        :content    => "Welcome!\n\n" <<
          "Your landing zone is now provisioned. Here is the information you'll need to access it\n\n" <<
          "Ed-Org: #{edorg_id}\n" <<
          "Server: #{result.attributes[:serverName]}\n" <<
          "LZ Directory: #{result.attributes[:landingZone]}\n\n" <<
          "Sftp to the LZ directory on the server using your ldap credentials.\n\n" <<
          "Thank you,\n" <<
          "SLC Operator\n"
      }
  
      APP_EMAILER.send_approval_email email
      @emailWarningMessage = ""
    else
      @emailWarningMessage = "Unfortunately, your account does not have an email address and " << 
                             "therefore we cannot send an email to you.  Please use this page " <<
                             "as reference or contact the SLC Operator for your landing zone details."
    end
    
    {:landingzone => @landingzone, :server => @server, :emailWarning => @emailWarningMessage}
  end

end

class ProvisioningError < StandardError
  
  def initialize(message)
    @message = message
  end
  
  def to_s
    @message
  end
end
