require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
require 'yaml'
require_relative 'common_stepdefs'
include REXML

$SLI_DEBUG=ENV['DEBUG'] if ENV['DEBUG']

def assert(bool, message = 'assertion failure')
  raise message unless bool
end

# Function idpLogin
# Inputs: (String) user = Username to login to the IDP with
# Inputs: (String) passwd = Password associated with the username
# Output: sets @sessionId, a string containing the session from the IDP that can be referenced throughout the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that logs in to the IDP using the supplied credentials
#              and sets the @sessionId variable for use in later stepdefs throughout the scenario
#              It is suggested you assert the @sessionId before returning success from the calling function
def idpLogin(user, passwd)
  url = PropLoader.getProps['sli_idp_server_url']+"/identity/authenticate?username="+user+"&password="+passwd
  res = RestClient.get(url){|response, request, result| response }
  @sessionId = res.body[res.body.rindex('=')+1..-2]
  puts(@sessionId) if $SLI_DEBUG
end

# Function idpRealmLogin
# Inputs: (Enum/String) realm = ("sli" "idp1" or "idp2") Which IDP you want to login with
# Inputs: (String) user = Username to login to the IDP with
# Inputs: (String) passwd = Password associated with the username
# Output: sets @sessionId, a string containing the session from the IDP that can be referenced throughout the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that logs in to the specified IDP using the supplied credentials
#              and sets the @sessionId variable for use in later stepdefs throughout the scenario
#              It is suggested you assert the @sessionId before returning success from the calling function
def idpRealmLogin(user, passwd, realm="sli")
  realmType = 'sli_idp_server_url' # Default case
  realmType = 'sea_idp_server_url' if realm == "idp1"
  realmType = 'lea_idp_server_url' if realm == "idp2"
  url = PropLoader.getProps[realmType]+"/identity/authenticate?username="+user+"&password="+passwd
  res = RestClient.get(url){|response, request, result| response }
  @sessionId = res.body[res.body.rindex('=')+1..-2]
  puts(@sessionId) if $SLI_DEBUG
end

# Function restHttpPost
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Inputs: (Object) data = Data object of type @format that you want to create
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Opt. Input: (bool) passAsRequestParm = Pass the sessionId as a request parameter instead of a header.
#                               Defaults to false (pass in the header) Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using POST to create a new object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpPost(id, data, format = @format, sessionId = @sessionId, passAsRequestParm = false)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into POST was nil")

  urlHeader = makeUrlAndHeaders('post',passAsRequestParm,id,sessionId,format)
  @res = RestClient.post(urlHeader[:url], data, urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

# Function restHttpGet
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Opt. Input: (bool) passAsRequestParm = Pass the sessionId as a request parameter instead of a header.
#                               Defaults to false (pass in the header) Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using GET to retrieve an existing object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpGet(id, format = @format, sessionId = @sessionId, passAsRequestParm = false)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into GET was nil")

  urlHeader = makeUrlAndHeaders('get',passAsRequestParm,id,sessionId,format)
  @res = RestClient.get(urlHeader[:url], urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

# Function restHttpPut
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Inputs: (Object) data = Data object of type @format that you want to update
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Opt. Input: (bool) passAsRequestParm = Pass the sessionId as a request parameter instead of a header.
#                               Defaults to false (pass in the header) Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using PUT to update an existing object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpPut(id, data, format = @format, sessionId = @sessionId, passAsRequestParm = false)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into PUT was nil")

  urlHeader = makeUrlAndHeaders('put',passAsRequestParm,id,sessionId,format)
  @res = RestClient.put(urlHeader[:url], data, urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

# Function restHttpDelete
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Opt. Input: (bool) passAsRequestParm = Pass the sessionId as a request parameter instead of a header.
#                               Defaults to false (pass in the header) Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using DELETE to remove an existing object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpDelete(id, format = @format, sessionId = @sessionId, passAsRequestParm = false)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into DELETE was nil")

  urlHeader = makeUrlAndHeaders('delete',passAsRequestParm,id,sessionId,format)
  @res = RestClient.delete(urlHeader[:url], urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

def makeUrlAndHeaders(verb,passAsRequestParm,id,sessionId,format)
  if(verb == 'put' || verb == 'post')
    headers = {:content_type => format}
  else
    headers = {:accept => format}
  end

  if passAsRequestParm
    #See if other request params exist in the URL
    sessionParm = "?sessionId="+sessionId
    sessionParm = "&sessionId="+sessionId if id.rindex("?") != nil

    url = PropLoader.getProps['api_server_url']+"/api/rest"+id+sessionParm
  else
    url = PropLoader.getProps['api_server_url']+"/api/rest"+id
    headers.store(:sessionId, sessionId)
  end
  puts(url, headers) if $SLI_DEBUG

  return {:url => url, :headers => headers}
end

##############################################################################
##############################################################################
###### After hook(s) #########################################################

After do |scenario|

  if @sessionId
    #This After hook block is to logout a user if @sessionId exists
    url = PropLoader.getProps['sli_idp_server_url']+"/identity/logout?subjectid="+@sessionId
    @res = RestClient.get(url, {}){|response, request, result| response }
    puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
  end

end

##############################################################################
##############################################################################
### Step Def Util methods ###

def convert(value)
  if /^true$/.match value
    true;
  elsif /^false$/.match value
    false;
  elsif /^\d+\.\d+$/.match value
    Float(value)
  elsif /^\d+$/.match value
    Integer(value)
  else
    value
  end
end

def prepareData(format, hash)
  if format == "application/json"
    hash.to_json
  elsif format == "application/xml"
    raise "XML not implemented"
  else
    assert(false, "Unsupported MIME type")
  end
end

def contentType(response)
  headers = @res.raw_headers
  assert(headers != nil, "Headers are nil")
  assert(headers['content-type'] != nil, "There is no content-type set in the response")
  headers['content-type'][0]
end

#return boolean
def findLink(id, type, rel, href)
  found = false
  uri = type+id
  restHttpGet(uri)
  assert(@res != nil, "Response from rest-client GET is nil")
  assert(@res.code == 200, "Return code was not expected: #{@res.code.to_s} but expected 200")
  if @format == "application/json" or @format == "application/vnd.slc+json"
    dataH=JSON.parse(@res.body)
    dataH["links"].each do |link|
      if link["rel"]==rel and link["href"].include? href
        found = true
        break
      end
    end
  elsif @format == "application/xml"
    assert(false, "application/xml is not supported")
  else
    assert(false, "Unsupported MIME type")
  end
  return found
end


########################################################################
########################################################################

def runShellCommand(command)
  `#{command}`
end

########################################################################
########################################################################
# Property Loader class

class PropLoader
  @@yml = YAML.load_file(File.join(File.dirname(__FILE__),'properties.yml'))
  @@modified=false

  def self.getProps
    self.updateHash() unless @@modified
    return @@yml
  end

  private

  def self.updateHash()
    @@yml.each do |key, value|
      @@yml[key] = ENV[key] if ENV[key]
    end
    @@modified=true
  end
end

module DataProvider
  def self.getValidRealmData()
    return {
       "state" => "bliss",
       "idp" => "http://path.to.nowhere",
       "realm" => "a_new_realm",
       "mappings"=> {"role"=>[{"sliRoleName"=>"Educator","clientRoleName"=>["Math teacher","Sci Teacher","Enforcer of Conformity"]},{"sliRoleName"=>"Leader","clientRoleName"=>["Fearless Leader","Imperator","First Consul"]}]}
    }
  end
end

module CreateEntityHash
  def CreateEntityHash.createBaseStudent()
    data = Hash[
        "studentUniqueStateId" => "123456",
        "name" => Hash[
          "firstName" => "fname",
          "lastSurname" => "lname",
          "middleName" => "mname"],
        "sex" => "Male",
        "birthData" => Hash[
          "birthDate" => "2012-01-01"
          ],
        "learningStyles" => Hash[
          "visualLearning" => 30,
          "auditoryLearning" => 40,
          "tactileLearning" => 30
          ]
       ]

    return data
  end

  def CreateEntityHash.createBaseSchool()
    data = Hash[
        "nameOfInstitution" => "school name",
        "stateOrganizationId" => "12345678",
        "gradesOffered" => ["First grade", "Second grade"],
        "address"=>[],
        "organizationCategories" => ["School"],
        "schoolCategories" => ["Elementary School"],
        ]
    return data
  end
end
