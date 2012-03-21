class App < SessionResource
  self.format = ActiveResource::Formats::JsonFormat
  schema do 
    string "client_secret", "redirect_uri", "description", "image_url"
    string "name", "client_id", "application_url", "administration_url"
    string "vendor", "version", "method"
    boolean "is_admin", "license_acceptance"
    time "created", "updated"
    string "organization", "client_type", "scope", "developer_info"
  end
  
  class DeveloperInfo < SessionResource
    schema do
      string "organization"
      boolean "license_acceptance"
    end
  end
end

