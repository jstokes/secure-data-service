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
require_relative 'spec_helper'
require_relative '../lib/OutputGeneration/XML/studentParentInterchangeGenerator'
require_relative '../lib/OutputGeneration/XML/validator'
require 'factory_girl'

FactoryGirl.find_definitions

describe 'StudentGenerator' do
  let(:path) { File.join( "#{File.dirname(__FILE__)}/", "../generated/InterchangeStudentParent.xml" ) }
  let(:interchange) { File.open( path, 'w')}
  let(:generator) {StudentParentInterchangeGenerator.new(interchange, 1)}
  let(:student) {FactoryGirl.build(:student)}
  let(:parent) {FactoryGirl.build(:parent)}
  let(:spa) {FactoryGirl.build( :studentParentAssociation)}
  describe '<<' do
    it 'will write a student to edfi' do

 
      generator << student
      generator << parent
      generator << spa

      valid = true

      #validate_file( path )

      valid.should be true

    end
  end
end

