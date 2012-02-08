module EntitiesHelper
  
  def localize_url(url)
    url.gsub( APP_CONFIG['api_base'], "http://#{request.host_with_port}/entities") unless url.nil?
  end
  
  def build_links(hash)
  	html = ""
  	if hash.is_a?(Array)
  		html << '<ul>'
  		hash.each do |link|
  			html << '<li>' << link_to( t(link["rel"]), localize_url(link["href"]) ) << '</li>'
  		end
  		html << '</ul>'
  	else
  		html << link_to( t(hash["rel"]), localize_url(hash["href"]) )
  	end
  	html
  end 

  def display_hash (hash = {}, indent = 0)
  	html = ""
  	return if hash.empty?
  	if indent != 0
  		html << "<dl>"
  	end
  	hash.each do |key, value|
  		case key
  		when 'links', 'link'
  			html << "<dt><b>#{t(key)}:</b></dt>"
  			html << "<dd>#{build_links value}</dd>"
  		when 'id'
  			# do not print id
  		else
  			html << "<dt><b>#{t(key)}:</b></dt>"
  			html << "<dd>"
  			if value.is_a?(Hash)
  				display_hash(value, indent+1)
  			elsif value.is_a?(Array)
  				value.each do |item| 
  					if item.is_a? Hash
  						html << display_hash(item, indent+1)
  					else
  						html << value.join(', ')
  						break
  					end
  				end
  			else
  				html << value.to_s
  			end
  			html << "</dd>"
  		end	
  	end
  	if indent != 0
  		html << "</dl>"
  	end
  	html
  end
  
end
