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
require 'equivalent-xml'
require_relative 'spec_helper'
require_relative '../lib/odin.rb'

describe "Odin" do
  describe "#validate" do
    it "generates valid XML for the default scenario" do
      odin = Odin.new
      odin.generate( nil )
      odin.validate().should be true
    end
  end

  describe "#baseline" do
    it "will compare the output to baseline" do
      odin = Odin.new
      odin.generate( nil )
      doc = Nokogiri.XML( File.open( File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudent.xml" ) )
      baseline = Nokogiri.XML( File.open( File.new "#{File.dirname(__FILE__)}/test_data/baseline/InterchangeStudent.xml" ) )

      doc.should be_equivalent_to(baseline)
    end
  end

  describe "#md5"
  it "generates the same data for each run" do
    odin = Odin.new
    odin.generate( nil )

    sha1 = odin.md5()
    4.times do
      odin.generate( nil )
      odin.md5().should eq sha1
    end
  end

  context "with a 10 student configuration" do
    let(:odin) {Odin.new}
    before {odin.generate "10students"}
    let(:student) {File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudent.xml"}

    describe "#generate" do
      it "will generate lists of 10 students" do
        student.readlines.select{|l| l.match("<Student>")}.length.should eq(10)
      end
    end
  end

  context "validate 10 student configuration matches baseline" do
    let(:odin) {Odin.new}
    before {odin.generate "10students"}
    let(:student) {File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudent.xml"}

  end

  context "with a 10001 student configuration" do
    let(:odin) {Odin.new}
    before {odin.generate "10001students"}
    let(:student) {File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudent.xml"}

    describe "#generate" do
      it "will generate lists of 10001 students" do
        student.readlines.select{|l| l.match("<Student>")}.length.should eq(10001)
      end
    end
  end

end
