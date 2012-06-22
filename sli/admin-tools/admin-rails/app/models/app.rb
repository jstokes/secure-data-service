class App < SessionResource
  self.format = ActiveResource::Formats::JsonFormat
  validates_presence_of [:description, :application_url, :name, :redirect_uri, :vendor], :message => "must not be blank"
  validates_format_of :version, :with => /^[A-Za-z0-9\.]{1,25}$/, :message => "must contain only alphanumeric characters and periods and be less than 25 characters long"
  
  def self.all_but_admin
    apps = App.all
    apps.delete_if { |app| app.respond_to? :endpoints }
    apps
  end
  
  def pending?
    self.registration.status == "PENDING" ? true : false
  end
  
  def in_progress?
    if self.bootstrap
      return false
    end
    progress = true
    self.authorized_ed_orgs.each { |ed_org| progress = false if !ed_org.to_i != 0 }
    progress
  end
  
  schema do 
    string "client_secret", "redirect_uri", "description", "image_url"
    string "name", "client_id", "application_url", "administration_url"
    string "version", "behavior"
    boolean "is_admin", "license_acceptance", "installed", "bootstrap"
    time "created", "updated"
    string "authorized_ed_orgs", "vendor"

  end
  

  class Registration < SessionResource
    schema do
      string "status"
    end
  end
end


