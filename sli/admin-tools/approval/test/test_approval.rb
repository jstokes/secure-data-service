testdir = File.dirname(__FILE__)
$LOAD_PATH << testdir + "/../lib"
require 'approval'
require 'test/unit'

class MockEmailer 
	def send_approval_email(email_address, first, last)
		@last_call = [email_address, first, last]
	end
end

class TestApprovalEngine < Test::Unit::TestCase
	def setup
		# define two basic users 
		@jd_email = "jdoe@example.com"
		@jd_emailtoken = "0102030405060708090A0B0C0D0E0F"
		@jd_user = {
			:first      => "John",
			:last       => "Doe", 
			:email      => @jd_email,
			:password   => "secret", 
			:vendor     => "Acme Inc.",
			:emailtoken => @jd_emailtoken,
			:status     => "submitted"	
		}

		@td_email = "tdoe@example.com"
		@td_user = @jd_user.clone
		@td_user[:email] = @td_email
	end

	def regular_workflow(is_sandbox)
		@ldap = LDAPStorage.new("ldap.slidev.org", 389, "ou=DevTest,dc=slidev,dc=org", "cn=DevLDAP User, ou=People,dc=slidev,dc=org", "Y;Gtf@w{")
		@mock_emailer = MockEmailer.new

		ApprovalEngine.init(@ldap, @mock_emailer, is_sandbox)
		@ldap.get_user_groups(@jd_email).each do |g|
			@ldap.remove_user_group(@jd_email, g)
		end
		@ldap.get_user_groups(@td_email).each do |g|
			@ldap.remove_user_group(@td_email, g)
		end

		ApprovalEngine.remove_user(@jd_email)
		ApprovalEngine.remove_user(@td_email)

		td_emailtoken = ApprovalEngine.add_disabled_user(@td_user)
		jd_emailtoken = ApprovalEngine.add_disabled_user(@jd_user)
		assert(ApprovalEngine.user_exists?(@jd_email))
		user = @ldap.read_user(@jd_email)
		assert(user)
		assert(user[:email] == @jd_email)
		assert(user[:status] == ApprovalEngine::STATE_SUBMITTED)

		ApprovalEngine.verify_email(jd_emailtoken)
		if !is_sandbox
			assert(@ldap.read_user(@jd_email)[:status] == ApprovalEngine::STATE_PENDING)
			ApprovalEngine.change_user_status(@jd_email, ApprovalEngine::ACTION_APPROVE)
		end
		assert(@ldap.read_user(@jd_email)[:status] == ApprovalEngine::STATE_APPROVED)

		if is_sandbox
			groups = @ldap.get_user_groups(@jd_email)
			assert(@ldap.get_user_groups(@jd_email).sort == ApprovalEngine::SANDBOX_ROLES.sort)
		else 
			assert(@ldap.get_user_groups(@jd_email).sort == ApprovalEngine::PRODUCTION_ROLES.sort)
		end 

		# move the other user to rejected state 
		if !is_sandbox
			ApprovalEngine.verify_email(td_emailtoken)
			ApprovalEngine.change_user_status(@td_email, ApprovalEngine::ACTION_REJECT)
			assert(@ldap.read_user(@td_email)[:status] == ApprovalEngine::STATE_REJECTED)
			roles = ApprovalEngine.get_roles(@td_email) 
			assert(roles == [], "Expected empty roles but got #{roles}")
		end 

		ApprovalEngine.remove_user(@jd_email)
		assert(!ApprovalEngine.user_exists?(@jd_email))
		assert(ApprovalEngine.get_roles(@jd_email) == [])
		ApprovalEngine.remove_user(@td_email)
		users = ApprovalEngine.get_users().select { |u| !![@jd_email, @td_email].index(u[:email]) }
		assert(users == [], "Expected empty array got #{users}.")
	end
	

	def test_production
		regular_workflow(true)
	end

	def test_sandbox
		regular_workflow(false)
	end
end
