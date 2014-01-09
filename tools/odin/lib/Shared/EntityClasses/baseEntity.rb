=begin

Copyright 2012-2013 inBloom, Inc. and its affiliates.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end

require 'yaml'
require_relative '../demographics'

# base entity
class BaseEntity

  def self.initializeDemographics(demographics, choices)
    @@d = Demographics.new(demographics, choices)
  end

  def self.demographics
    @@d
  end

  def self.set_scenario(scenario)
    @@scenario = scenario
  end

  def rand(num)
    @rand.rand(num)
  end

  def choose(options)
    unless options.empty? 
      options[@rand.rand(options.size) - 1]
    end
  end

  def wChoose(distribution)
    r = @rand.rand weight_total(distribution)
    distribution.each do |element, weight|
      if r < weight
      return element
      end
      r -= weight
    end
  end

  def wChooseUsingRand(prng, distribution)
    r = prng.rand weight_total(distribution)
    distribution.each do |element, weight|
      if r < weight
      return element
      end
      r -= weight
    end
  end

  def weight_total(distribution)
    sum = 0
    distribution.each do |_, weight|
      sum +=weight
    end
    sum
  end
  
  def bit_choose
    @rand.rand(2) == 1
  end

  # True if a the scenario has set OPTIONAL_FIELD_LIKELYHOOD and a random percentage is less than that value; false otherwise
  def optional?
    @@scenario['OPTIONAL_FIELD_LIKELYHOOD'] && (@@scenario['OPTIONAL_FIELD_LIKELYHOOD'] > 0) && (@rand.rand() < @@scenario['OPTIONAL_FIELD_LIKELYHOOD'])
  end

  # Yield to the given block if a random percentage is less than the scenario's OPTIONAL_FIELD_LIKELYHOOD
  def optional(&blk)
    yield if optional?
  end

  def int_value(obj)
    in_byte = obj.to_s.bytes
    in_byte.inject(0) {|s, i| s+i}
  end
end
