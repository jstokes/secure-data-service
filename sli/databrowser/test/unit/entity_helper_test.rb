require 'test_helper'
require 'entities_helper'


class EntityHelperTest < ActionView::TestCase
  include EntitiesHelper
  
  test "simple value generator with nil" do
    v = value_for_simple_view(VIEW_CONFIG['teacher'].first, nil)
    assert_nil(v)
  end
  
  test "simple value generator with nil type" do
    v = value_for_simple_view(nil, @teacher_fixtures['one'])
    assert_nil(v)
  end

  test "simple value generator with valid" do
    v = value_for_simple_view(VIEW_CONFIG['teacher'].first, @teacher_fixtures['one'])
    assert_not_nil(v)
    assert_equal(v, "Linda")
  end
  
  test "simple value generator with nested type" do
    v = value_for_simple_view(VIEW_CONFIG['teacher'][1], @teacher_fixtures['one'])
    assert_not_nil(v)
  end
  
  test "build simple hash with invalid type" do
    assert_nil(build_simple_hash(nil, @teacher_fixtures['one']))
  end
  
  test "build simple hash with invalid hash" do
    assert_nil(build_simple_hash(VIEW_CONFIG['teacher'], nil))
  end
  
  test "build simple hash with valid data" do
    assert_not_nil(build_simple_hash(VIEW_CONFIG['teacher'], @teacher_fixtures['one']))
  end
end