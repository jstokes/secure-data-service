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

require 'timeout'

require_relative '../lib/EntityCreation/work_order_processor.rb'
require_relative '../lib/Shared/demographics.rb'
require_relative '../lib/OutputGeneration/XML/studentParentInterchangeGenerator.rb'
require_relative '../lib/OutputGeneration/XML/enrollmentGenerator.rb'

describe "WorkOrderProcessor" do
  describe "#build" do
    context 'With a simple work order' do
      let(:work_order) {StudentWorkOrder.new(42, :KINDERGARTEN, 2001, {'id' => 64, 'sessions' => [{'year' => 2001}, 
                                                                                                  {'year' => 2002}]})}

      it "will generate the right number of entities for the student generator" do
        studentParent = double
        studentParent.should_receive(:<<).with(an_instance_of(Student)).once
        WorkOrderProcessor.new({:studentParent => studentParent}).build(work_order)
      end

      it "will generate the right number of entities for the enrollment generator" do
        enrollment = double
        enrollment.should_receive(:<<).with(an_instance_of(StudentSchoolAssociation)).twice
        WorkOrderProcessor.new({:enrollment => enrollment}).build(work_order)
      end

      it "will generate a StudentSchoolAssociation with the correct information" do
        enrollment = double
        ssas = []
        enrollment.stub(:<<) do |ssa|
          ssas << ssa
          ssa.studentId.should eq(42)
          ssa.schoolStateOrgId.should eq(64)
        end
        WorkOrderProcessor.new({:enrollment => enrollment}).build(work_order)
        ssas[0].startYear.should eq(2001) and ssas[0].startGrade.should eq("Kindergarten")
        ssas[1].startYear.should eq(2002) and ssas[1].startGrade.should eq("First grade")
      end

    end
  end
end

describe "gen_work_orders" do
  context "with a world with 20 students in 4 schools" do
    let(:world) {{'seas' => [{'id' => 'sea1'}], 'leas' => [{'id' => 'lea1'}], 
                  'elementary' => [{'id' => 0, 'students' => {2011 => {:KINDERGARTEN => 5}, 2012 => {:FIRST_GRADE => 5}}, 'sessions' => [{}]},
                                   {'id' => 1, 'students' => {2011 => {:KINDERGARTEN => 5}, 2012 => {:FIRST_GRADE => 5}}, 'sessions' => [{}]}],
                  'middle' => [{'id' => 2, 'students' => {2011 => {:SEVENTH_GRADE => 5}, 2012 => {:EIGTH_GRADE => 5}}, 'sessions' => [{}]}],
                  'high' => [{'id' => 3, 'students' => {2011 => {:NINTH_GRADE => 5}, 2012 => {:TENTH_GRADE => 5}}, 'sessions' => [{}]}]}}
    let(:work_orders) {WorkOrderProcessor.gen_work_orders world}

    it "will create a work order for each student" do
      work_orders.count.should eq(20)
    end

    it "will put the students in the right schools" do
      work_orders.each_with_index{|work_order, index|
        work_order.edOrg['id'].should eq(index/5)
      }
    end

    it "will give students the correct entry grade" do
      work_orders.select{|wo| wo.initial_grade == :KINDERGARTEN}.count.should eq(10)
      work_orders.select{|wo| wo.initial_grade == :SEVENTH_GRADE}.count.should eq(5)
      work_orders.select{|wo| wo.initial_grade == :NINTH_GRADE}.count.should eq(5)
    end

    it "will give students the correct entry year" do
      work_orders.select{|wo| wo.initial_year == 2011}.count.should eq(20)
    end

    it "will generate unique student ids" do
      work_orders.map{|wo| wo.id}.to_set.count.should eq(20)
    end

  end
  
  context "with an infinitely large school" do
    let(:world) {{'high' => [{'id' => "Zeno High", 'students' => {2001 => {:KINDERGARTEN => 1.0/0}}, 'sessions' => [{}]}]}}

    it "will lazily create work orders in finite time" do
      Timeout::timeout(5){
        WorkOrderProcessor.gen_work_orders(world).take(100).length.should eq(100)
      }
    end
  end

end
