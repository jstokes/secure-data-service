class Realm < SessionResource

  self.site = "#{APP_CONFIG['api_base']}"
  self.collection_name = "realm"
  schema do
    string "uniqueIdentifier", "name"
    string "saml", "mappings"
    string "idp"
  end

  class IDP < SessionResource
      schema do
        string "id", "redirectEndpoint"
      end
    end
end
