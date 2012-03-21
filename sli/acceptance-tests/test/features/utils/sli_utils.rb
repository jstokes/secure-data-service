require File.expand_path('../common_stepdefs.rb', __FILE__)
require 'rubygems'
require 'bundler/setup'

require 'rest-client'
require 'json'
require 'builder'
require 'rexml/document'
require 'yaml'
include REXML

$SLI_DEBUG=ENV['DEBUG'] if ENV['DEBUG']

$SESSION_MAP = {"demo_SLI" => "e88cb6d1-771d-46ac-a207-2e58d7f12196",
                "jdoe_SLI" => "c88ab6d7-117d-46aa-a207-2a58d1f72796",
                "tbear_SLI" => "c77ab6d7-227d-46bb-a207-2a58d1f82896",
                "john_doe_SLI" => "a69ab2d7-137d-46ba-c281-5a57d1f22706",
                "ejane_SLI" => "4ab8b6d4-51ad-c67a-1b0a-25e8d1f12701",
                "linda.kim_SLI" => "4cf7a5d4-37a1-ca19-8b13-b5f95131ac85",
                "educator_SLI"=> "4cf7a5d4-37a1-ca11-8b13-b5f95131ac85",
                "leader_SLI"=> "4cf7a5d4-37a1-ca22-8b13-b5f95131ac85",
                "administrator_SLI"=> "4cf7a5d4-37a1-ca33-8b13-b5f95131ac85",
                "aggregator_SLI"=> "4cf7a5d4-37a1-ca44-8b13-b5f95131ac85",
                "baduser_SLI"=> "4cf7a5d4-37a1-ca55-8b13-b5f95131ac85",
                "nouser_SLI"=> "4cf7a5d4-37a1-ca66-8b13-b5f95131ac85",
                "teacher_SLI"=> "4cf7a5d4-37a1-ca77-8b13-b5f95131ac85",
                "prince_SLI"=> "4cf7a5d4-37a1-ca88-8b13-b5f95131ac85",
                "root_SLI"=> "4cf7a5d4-37a1-ca99-8b13-b5f95131ac85",
                "bigbro_SLI"=> "4cf7a5d4-37a1-ca00-8b13-b5f95131ac85"}

def assert(bool, message = 'assertion failure')
  raise message unless bool
end

# Function idpLogin
# Inputs: (String) user = Username to login to the IDP with
# Inputs: (String) passwd = Password associated with the username
# Output: sets @sessionId, a string containing the OAUTH session that can be referenced throughout the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that logs in to the IDP using the supplied credentials
#              and sets the @sessionId variable for use in later stepdefs throughout the scenario
#              It is suggested you assert the @sessionId before returning success from the calling function
def idpLogin(user, passwd)
  idpRealmLogin(user, passwd, "SLI")
end

# Function idpRealmLogin
# Inputs: (Enum/String) realm = ("SLI" "IL" or "NY") Which IDP you want to login with
# Inputs: (String) user = Username to login to the IDP with
# Inputs: (String) passwd = Password associated with the username
# Output: sets @sessionId, a string containing the OAUTH session that can be referenced throughout the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that logs in to the specified IDP using the supplied credentials
#              and sets the @sessionId variable for use in later stepdefs throughout the scenario
#              It is suggested you assert the @sessionId before returning success from the calling function
def idpRealmLogin(user, passwd, realm="SLI")
  token = $SESSION_MAP[user+"_"+realm]
  assert(token != nil, "Could not find session for user #{user} in realm #{realm}")
  @sessionId = token
  puts(@sessionId) if $SLI_DEBUG
end

# Function restHttpPost
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Inputs: (Object) data = Data object of type @format that you want to create
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using POST to create a new object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpPost(id, data, format = @format, sessionId = @sessionId)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into POST was nil")

  urlHeader = makeUrlAndHeaders('post',id,sessionId,format)
  @res = RestClient.post(urlHeader[:url], data, urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

# Function restHttpGet
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using GET to retrieve an existing object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpGet(id, format = @format, sessionId = @sessionId)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into GET was nil")

  urlHeader = makeUrlAndHeaders('get',id,sessionId,format)
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
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using PUT to update an existing object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpPut(id, data, format = @format, sessionId = @sessionId)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into PUT was nil")

  urlHeader = makeUrlAndHeaders('put',id,sessionId,format)
  @res = RestClient.put(urlHeader[:url], data, urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

# Function restHttpDelete
# Inputs: (String) id = URL of the desired resource (ex. /students/fe3425e53-f23-f343-53cab3453)
# Opt. Input: (String) format = defaults to @format that is generally set from the scenario step defs
#                               Can be manually overwritten
# Opt. Input: (String) sessionId = defaults to @sessionId that was created from the idpLogin() function
#                               Can be manually overwritten
# Output: sets @res, the HTML REST response that can be access throughout the remainder of the Gherkin scenario
# Returns: Nothing, see Output
# Description: Helper function that calls the REST API specified in id using DELETE to remove an existing object
#              It is suggested you assert the state of the @res response before returning success from the calling function
def restHttpDelete(id, format = @format, sessionId = @sessionId)
  # Validate SessionId is not nil
  assert(sessionId != nil, "Session ID passed into DELETE was nil")

  urlHeader = makeUrlAndHeaders('delete',id,sessionId,format)
  @res = RestClient.delete(urlHeader[:url], urlHeader[:headers]){|response, request, result| response }

  puts(@res.code,@res.body,@res.raw_headers) if $SLI_DEBUG
end

def makeUrlAndHeaders(verb,id,sessionId,format)
  if(verb == 'put' || verb == 'post')
    headers = {:content_type => format}
  else
    headers = {:accept => format}
  end

  headers.store(:Authorization, "bearer "+sessionId)

  url = PropLoader.getProps['api_server_url']+"/api/rest"+id
  puts(url, headers) if $SLI_DEBUG

  return {:url => url, :headers => headers}
end

##############################################################################
##############################################################################
###### After hook(s) #########################################################

# None remaining

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
  elsif format == "application/vnd.slc+json"
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
       "regionId" => "bliss",
       "idp" => {"id" => "http://path.to.nowhere", "redirectEndpoint" => "http://path.to.nowhere/somewhere/else"},
       "saml" => {"field" => []},
       "name" => "a_new_realm",
       "mappings"=> {"role"=>[{"sliRoleName"=>"Educator","clientRoleName"=>["Math teacher","Sci Teacher","Enforcer of Conformity"]},{"sliRoleName"=>"Leader","clientRoleName"=>["Fearless Leader","Imperator","First Consul"]}]}
    }
  end
  
  def self.getValidAppData()
    return {
      "client_type" => "PUBLIC",
      "scope" => "ENABLED",
      "redirect_uri" => "https://slidev.org",
      "description" => "Prints hello world.",
      "name" => "Hello World",
      "is_admin" => true,
      "administration_url" => "https://slidev.org/admin",
      "image_url" => "https://slidev.org/image",
      "application_url" => "https://slidev.org/image",
      "version" => "3.14",
      "developer_info" => { "license_acceptance" => true, "organization" => "Acme" } 
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

module EntityProvider
  def self.verify_entities_match(expected, response)
    if expected.is_a?(Hash) 
      expected.each { |key, value| verify_entities_match(value, response[key]) }
    elsif expected.is_a?(Array)
      assert( expected.size == response.size )
      expected.zip(response).each { |ex, res| verify_entities_match(ex, res) }
    else
      assert( expected == response )
    end
  end

  def self.get_new_entity(type)
    case type
    when 'attendance', 'v1attendance'
      { 
        "eventDate" => "2012-02-24", 
        "attendanceEventType" => "Daily Attendance", 
        "attendanceEventCategory" => "Tardy", 
        "studentId" => "7a86a6a7-1f80-4581-b037-4a9328b9b650"
      }
    end
  end

  def self.get_entity_uri(type)
    case type
    when 'attendance'
      "/attendances"
    when 'v1attendance'
      "/v1/attendances"
    end
  end

  def self.get_existing_entity(type)
    case type
    when 'attendance', 'v1attendance'
      { 
        "id" => "4beb72d4-0f76-4071-92b4-61982dba7a7b",
        "eventDate" => "2012-02-24", 
        "attendanceEventType" => "Daily Attendance", 
        "attendanceEventCategory" => "Tardy",
        "studentId" => "7a86a6a7-1f80-4581-b037-4a9328b9b650"
      }
    end
  end

  def self.get_updated_entity(type)
    case type
    when 'attendance', 'v1attendance'
      { 
        "id" => "4beb72d4-0f76-4071-92b4-61982dba7a7b",
        "eventDate" => "2012-02-24",
        "attendanceEventType" => "Daily Attendance", 
        "attendanceEventCategory" => "In Attendance",
        "studentId" => "7a86a6a7-1f80-4581-b037-4a9328b9b650"
      }
    end
  end
end
