require 'simplecov'
require 'simplecov-rcov'

SimpleCov.formatter = SimpleCov::Formatter::RcovFormatter
SimpleCov.start 'rails'

ENV["RAILS_ENV"] = "test"
require File.expand_path('../../config/environment', __FILE__)
require 'rails/test_help'
require 'active_resource/http_mock'

class ActiveSupport::TestCase
  # Setup all fixtures in test/fixtures/*.(yml|csv) for all tests in alphabetical order.
  #
  # Note: You'll currently still have to declare fixtures explicitly in integration tests
  # -- they do not yet inherit this setting
  # fixtures :all

  # Add more helper methods to be used by all tests here...
  def load_fixture(name)
    path = File.join(Rails.root, "test", "fixtures", "#{name}.yml")
    return nil unless File.exists?(path)
    YAML::load(File.open path)
  end
  
  setup do
    @student_fixtures = load_fixture("entities")
    @teacher_fixtures = load_fixture("teachers")
    @school_fixtures = load_fixture("schools")
    # @realm_fixtures = load_fixture("realms")
    ActiveResource::HttpMock.respond_to do |mock|
      mock.get "/api/rest/v1/home/", {"Accept" => "application/vnd.slc+json"}, [].to_json
      mock.get "/api/rest/v1/students/", {"Accept" => "application/vnd.slc+json"}, [@student_fixtures['one'], @student_fixtures['two']].to_json
      mock.get "/api/rest/v1/teachers/", {"Accept" => "application/vnd.slc+json"}, [@teacher_fixtures['one'], @teacher_fixtures['two']].to_json
      mock.get "/api/rest/v1/students/11111111-1111-1111-1111-111111111111/", {"Accept" => "application/vnd.slc+json"}, @student_fixtures['one'].to_json
      mock.get "/api/rest/v1/teachers/?teacherUniqueStateId=11111111-1111-1111-1111-111111111111", {"Accept" => "application/vnd.slc+json"}, [@teacher_fixtures['one'], @teacher_fixtures['two']].to_json
      
      mock.get "/api/rest/v1/teacher-school-associations/11111111-1111-1111-1111-111111111111/", {"Accept" => "application/vnd.slc+json"}, [@teacher_fixtures['one'], @teacher_fixtures['two']].to_json
            
      mock.get "/api/rest/system/session/check", {"Accept" => "application/json"}, {'full_name' => "Peter Griffin"}.to_json
      mock.get "/api/rest/system/session/check", {"Accept" => "application/json", "sessionId" => "Waffles"}, {'full_name' => "Peter Griffin"}.to_json
    end
  end
end
