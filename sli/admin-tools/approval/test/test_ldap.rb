require 'test/unit'
require "socket"

testdir = File.dirname(__FILE__)
$LOAD_PATH << testdir + "/../lib"
require 'approval'

class TestLdap < Test::Unit::TestCase

  # Note: Socket.gethostname is used to ensure uniqueness when testers are 
  # running this simultaneously on different machines
  Jd_email = "jdoe@example.com_#{Socket.gethostname}"
  Jd_emailtoken = "0102030405060708090A0B0C0D0E0F"
  User_info = {
    :first      => "John",
    :last       => "Doe", 
    :email      => Jd_email,
    :password   => "secret", 
    :vendor     => "Acme Inc.",
    :emailtoken => Jd_emailtoken,
    :status     => "submitted",
    :homedir    => 'test',
    :uidnumber  => '456',
    :gidnumber  => '123',
    :tenant     => 'testtenant',
    :edorg      => 'testedorg'
  }

  # Note: Socket.gethostname is used to ensure uniqueness when testers are 
  # running this simultaneously on different machines
  Td_email = "tdoe@example.com_#{Socket.gethostname}"
  Td_user_info = User_info.clone 
  Td_user_info[:first] = "Tom"
  Td_user_info[:email] = Td_email 

  Ldap_generated_keys = [:created, :updated]
  All_keys = [:first, :last, :email, :password, :vendor, :emailtoken,
    :status, :homedir, :uidnumber, :gidnumber, :tenant, :edorg]

  Email1 = "dog_cat_hamster_platypus_elephant"
  Email2 = "mouse_snake_lion_cat_horse"
  Email3 = "car_truck_horse_camel"
  Email4 = "python_mongoose_anteater_cow"
  Email5 = "pig_cat_cow_porcupine"

  def setup
    @ldap = LDAPStorage.new("ldap.slidev.org", 389, "ou=DevTest,dc=slidev,dc=org", "cn=DevLDAP User, ou=People,dc=slidev,dc=org", "Y;Gtf@w{")

    @ldap.delete_user(Jd_email)
    @ldap.delete_user(Td_email)

    @ldap.remove_user_group(Td_email, "testgroup")
    @ldap.remove_user_group(Jd_email, "testgroup")

    @ldap.delete_user(Email1)
    @ldap.delete_user(Email2)
    @ldap.delete_user(Email3)
    @ldap.delete_user(Email4)
    @ldap.delete_user(Email5)

    all_testusers = @ldap.search_users "*#{Socket.gethostname}*"
    all_testusers.each do |u|
      @ldap.delete_user u[:email]
    end    
  end

  def assert_equal_user_info(expected, actual)
    All_keys.each do |x| 
      if x.to_s.downcase != "password"
        assert_equal expected[x], actual[x]
      end
    end
  end

  def test_keys
    # to make sure that new tests are added when a new key is added to user_info
    result = @ldap.create_user(User_info)
    found_user = @ldap.read_user(Jd_email)

    keys = [].concat(All_keys).concat(Ldap_generated_keys)
    unaccounted = found_user.keys.reject{|x| keys.include? x}
    assert unaccounted.empty?
  end

  def test_create_read_delete
    result = @ldap.create_user(User_info)
    found_user = @ldap.read_user(Jd_email)
    assert_equal_user_info(User_info, found_user)

    found_user.each do |attribute, values|
      # values.each do |value|
      #   puts "      --->#{value}"
      # end
    end

    @ldap.create_user(Td_user_info)
    @ldap.add_user_group(Jd_email, "testgroup")
    @ldap.add_user_group(Td_email, "testgroup")
    @ldap.add_user_group(Jd_email, "abcgroup")
    @ldap.add_user_group(Td_email, "abcgroup")
    
    jd_user_groups = @ldap.get_user_groups(Jd_email)
    assert jd_user_groups.include?("testgroup")
    assert jd_user_groups.include?("abcgroup")
  end
  
  def test_update_user_info
    result = @ldap.create_user(User_info)
    found_user = @ldap.read_user(User_info[:email])
    assert_equal_user_info(User_info, found_user)
    #update the fields except for email, which uniquely id a user
    to_update = {
      :first      => "#{found_user[:first]}UpdateTest",
      :last       => "#{found_user[:last]}UpdateTest", 
      :email      => found_user[:email],
      :password   => "#{found_user[:password]}UpdateTest", 
      :vendor     => "#{found_user[:vendor]}UpdateTest",
      :emailtoken => "#{found_user[:emailtoken]}UpdateTest",
      :status     => "#{found_user[:status]}UpdateTest",
      :homedir    => "#{found_user[:homedir]}/updateTest",
      :uidnumber  => "#{found_user[:uidnumber]}890",
      :gidnumber  => "#{found_user[:gidnumber]}890"
    }
    @ldap.update_user_info(to_update)
    updated_found_user = @ldap.read_user(User_info[:email])
    
    allow_updating = [
      :first,
      :last,
      :password, 
      :vendor,
      :emailtoken,
      :homedir
    ]

    to_update.keys.each do |key|
      if allow_updating.include? key
        assert_equal to_update[key], updated_found_user[key]
      else
        assert_equal found_user[key], updated_found_user[key]
      end
    end
  end

  def build_user_info(email)
    new_user_info = User_info.clone
    new_user_info[:email] = "#{email}_#{Socket.gethostname}"
    new_user_info
  end

  # TODO: enable this test when 
  def test_search_users
    @ldap.create_user(build_user_info Email1)
    @ldap.create_user(build_user_info Email2)
    @ldap.create_user(build_user_info Email3)
    @ldap.create_user(build_user_info Email4)
    @ldap.create_user(build_user_info Email5)
    
    search_result = @ldap.search_users "*cat*"
    assert_equal 3, search_result.size
    
    search_result = @ldap.search_users "*cow*"
    assert_equal 2, search_result.size
    
    search_result = @ldap.search_users "*pig*"
    assert_equal 1, search_result.size
    
    search_result = @ldap.search_users "*blah blah blah*"
    assert_equal 0, search_result.size
  end
end


# any_email = "anything@example.com" 
# puts "User #{jd_email} exists: #{ldap.user_exists?(jd_email)}"
# puts "User #{any_email} exists: #{ldap.user_exists?(any_email)}"

# new_user = user_info.clone 
# new_user[:status] = "pending"
# ldap.update_status(new_user)
# found_user = ldap.read_user(jd_email)
# puts "Changed status: #{found_user[:status]}"

# found_user = ldap.read_user_emailtoken(jd_emailtoken)
# puts "#{found_user}"

# found_user = ldap.read_user_emailtoken("nothing")
# if !found_user
# 	puts "Did not find arbitrary emailtoken."
# end


# puts "Fetching all users:"
# all_users = ldap.read_users
# all_users.each do |e|
# 	puts e[:email]
# end 

# puts "-----------------------------\nFetching pending users:"
# pending_users = ldap.read_users("pending")
# pending_users.each do |e|
# 	puts e[:email]
# end

# found_user = true 
# ldap.delete_user(jd_email)
# found_user = ldap.read_user_emailtoken("nothing")
# if !found_user
# 	puts "Did not find delete user."
# end
# ldap.delete_user(td_email)

