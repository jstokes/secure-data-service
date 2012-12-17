=begin

Copyright 2012 Shared Learning Collaborative, LLC

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

require 'json'
require 'logger'

class EntityTracker
  attr_accessor :counts

  def initialize
    @counts = Hash.new(0)
  end

  def track(entity)
    @counts[entity.class.name] += 1
  end

  def display
    pattern = "%-45s%10i\n"
    "-------------------------------------------------------\n" +
    "Ed-fi entity counts:" +
    "-------------------------------------------------------\n" +
    @counts.sort.map{|type, count|
      pattern % ["#{type}:", count]
    }.inject(:+) +
    pattern % ["Total entity count:", @counts.values.inject(:+)] +
    "-------------------------------------------------------"
  end

  def count(entity_type)
    @counts[entity_type.name]
  end

  def clear
    @counts.clear
  end

end
