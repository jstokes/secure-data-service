class SessionResource < ActiveResource::Base
  cattr_accessor :auth_id, :url_type
  Rails.logger.debug { "Session ID: #{@auth_id}" }
  Rails.logger.debug { "Entity Type: #{@url_type}" }
  
  class << self
    
    def headers
      @headers ||= {"sessionId" => self.auth_id}
    end
    
    ## Remove format from the url.
     def element_path(id, prefix_options = {}, query_options = nil)
       prefix_options, query_options = split_options(prefix_options) if query_options.nil?
       something = "#{prefix(prefix_options)}#{self.url_type}/#{id}#{query_string(query_options)}"
       Rails.logger.debug { "element_path: #{something}" }
       something
     end

     ## Remove format from the url.
     def collection_path(prefix_options = {}, query_options = nil)
       prefix_options, query_options = split_options(prefix_options) if query_options.nil?
       something = "#{prefix(prefix_options)}#{self.url_type}#{query_string(query_options)}"
       Rails.logger.debug { "collection_path: #{something}" }
       something
     end
     
      ## Remove format from the url.
      def custom_method_collection_url(method_name, options = {})
        prefix_options, query_options = split_options(options)
        something = "#{prefix(prefix_options)}#{self.url_type}/#{method_name}#{query_string(query_options)}"
        Rails.logger.debug {"custom_method_collection_url: #{something}"}
        something
      end
  end
end