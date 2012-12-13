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
require_relative '../lib/Shared/util.rb'

# specifications for ODIN
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
      for f in Dir.entries(File.new "#{File.dirname(__FILE__)}/../generated") do
        if (f.end_with?(".xml") || f.end_with?(".ctl"))
          doc = Nokogiri.XML( File.open( File.new "#{File.dirname(__FILE__)}/../generated/#{f}" ) )

          # ensure there are no extra generated files without a corresponding baseline
          # as a hedge against adding files without a corresponding baseline.
          File.exists?("#{File.dirname(__FILE__)}/test_data/baseline/#{f}").should be_true

          baseline = Nokogiri.XML( File.open( File.new "#{File.dirname(__FILE__)}/test_data/baseline/#{f}" ) )
          #doc.should be_equivalent_to(baseline)
          EquivalentXml.equivalent?(doc, baseline) { |n1, n2, result| 
            puts "\t failed to reproduce baseline document: #{f}" if result == false
            result.should be_true
          }
        end
      end
      # ensure there are no missing generated files.
      for f in Dir.entries(File.new "#{File.dirname(__FILE__)}/test_data/baseline") do
        if (f.end_with?(".xml") || f.end_with?(".ctl"))
          File.exists?("#{File.dirname(__FILE__)}/../generated/#{f}").should be TRUE
        end
      end
    end
  end

  describe "#md5" do
    it "generates the same data for each run" do
      odin = Odin.new
      odin.generate( nil )

      sha1 = odin.md5()
      4.times do
        odin.generate( nil )
        odin.md5().should eq sha1
      end
    end
  end

  context "with a 10 student configuration" do
    let(:odin) {Odin.new}
    before {odin.generate "10students"}
    let(:student) {File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudentParent.xml"}
    let(:ctlFile) {File.new "#{File.dirname(__FILE__)}/../generated/ControlFile.ctl"}
    let(:lines) {ctlFile.readlines}
    let(:zipFile) {File.new "#{File.dirname(__FILE__)}/../generated/OdinSampleDataSet.zip"}
    
    before(:each) do
      @interchanges = Hash.new
      lines.each do |line|
        @interchanges[line.split(',')[1]] = line
      end
    end
    
    describe "#generate" do
      it "will generate lists of 10 students" do
        student.readlines.select{|l| l.match("<Student>")}.length.should eq(10)
      end
      
      it "will generate a valid control file with 8 interchanges" do     
        @interchanges.length.should eq(9)
      end
      
      it "will generate a valid control file with Student as a type" do
        @interchanges["StudentParent"].should match(/StudentParent.xml/)
      end
      
      it "will generate a valid control file with EducationOrganization as a type" do
        @interchanges["EducationOrganization"].should match(/EducationOrganization.xml/)
      end
      
      it "will generate a valid control file with EducationOrgCalendar as a type" do
        @interchanges["EducationOrgCalendar"].should match(/EducationOrgCalendar.xml/)
      end
      
      it "will generate a valid control file with MasterSchedule as a type" do
        @interchanges["MasterSchedule"].should match(/MasterSchedule.xml/)
      end

      it "will generate a valid control file with StaffAssociation as a type" do
        @interchanges["StaffAssociation"].should match(/StaffAssociation.xml/)
      end

      it "will generate a valid control file with StaffAssociation as a type" do
        @interchanges["StudentEnrollment"].should match(/StudentEnrollment.xml/)
      end
      
      it "will generate a zip file of the included interchanges" do
        # Make sure the zipfile exists in the dir we expect
        zipDir = "#{File.dirname(__FILE__)}/../generated"
        zipFile.should_not be_nil
        
        # Unzip the file in odin/generated/
        genDataUnzip(zipDir, "OdinSampleDataSet.zip", "OdinSampleDataSet") 
        # Verify the dumb number of files matches expected values
        # --> always add 2 files to your expected count for . and ..      
        #Dir.entries(File.new "#{zipDir}/OdinSampleDataSet").length.should eq(10)   
        # --> not sure this is a valid test.. what are we really testing with this? we already check the number of interchanges above.

        # Re-zip the data to prep for Acceptance Tests
        # --> this also feels wrong
        # --> acceptance testings can zip the generated/ directory (or OdinSampleDataSet/ sub-directory).. so why are we doing this?
        genDataZip("#{zipDir}/OdinSampleDataSet", "OdinSampleDataSet.zip", "#{zipDir}/OdinSampleDataSet")        
      end
      
    end
  end

  context "validate 10 student configuration matches baseline" do
    let(:odin) {Odin.new}
    before {odin.generate "10students"}
    let(:student) {File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudentParent.xml"}

  end

  context "with a 10001 student configuration" do
    let(:odin) {Odin.new}
    before {odin.generate "10001students"}
    let(:student) {File.new "#{File.dirname(__FILE__)}/../generated/InterchangeStudentParent.xml"}

    describe "#generate" do
      it "will generate lists of 10001 students" do
        student.readlines.select{|l| l.match("<Student>")}.length.should eq(10001)
      end
    end
  end

end
