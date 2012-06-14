# encoding:utf-8
require File.expand_path("../test_helper", __FILE__)

class BinaryTest < Test::Unit::TestCase
  def setup
    @data = ("THIS IS BINARY " * 50).unpack("c*")
  end

  def test_do_not_display_binary_data
    binary = BSON::Binary.new(@data)
    assert_equal "<BSON::Binary:#{binary.object_id}>", binary.inspect
  end
end
