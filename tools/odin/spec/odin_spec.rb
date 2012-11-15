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

require_relative '../odin.rb'

describe "Odin" do
  describe "#validate" do
    it "generates valid XML for the default scenario" do
      odin = Odin.new
      odin.generate( nil )
      odin.validate().should be true
    end
  end

  describe "#sha1"
  it "generates the same data for each run" do
    odin = Odin.new
    odin.generate( nil )

    sha1 = odin.sha1()
    4.times do
      odin.generate( nil )
      odin.sha1().should eq sha1
    end
  end
end