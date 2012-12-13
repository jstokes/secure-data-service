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
require 'active_support/core_ext/hash/deep_merge'

if (ARGV.size != 3)
  puts " How to use... "
  puts " ./schema-migration-prep.rb <XSD File> <Src Migration Config File> <Dst Migration Config File>"
  exit
end


def main
	modify_complex_types_schema()
	merge_migration_config()
end


def modify_complex_types_schema
	schemaFilename = ARGV[0]
	xsd = File.read(schemaFilename)

	xsd = update_version_numbers(xsd)
	xsd = add_new_field(xsd)
	xsd = remove_field(xsd)
	xsd = rename_field(xsd)

	File.open(schemaFilename, 'w') {|f| f.write(xsd)}
end



def update_version_numbers(xsd)
	# replace existing version numbers with 999999
	upversioned_xsd = xsd.gsub(%r{<sli:schemaVersion>\d*</sli:schemaVersion>}, '<sli:schemaVersion>999999</sli:schemaVersion>')
	return upversioned_xsd
end


def add_new_field(xsd)
	# add new field to staff
	insert_index = xsd.index("<xs:element name=\"staffUniqueStateId\" ")

	new_schema_field = "<xs:element name=\"favoriteSubject\" type=\"xs:string\"/>\n"
	xsd.insert(insert_index,new_schema_field)
	return xsd
end


def remove_field(xsd)

#<xs:element name="sex" type="sexType">
#				<xs:annotation>
#					<xs:documentation>A person's gender.</xs:documentation>
#					<xs:appinfo>
#						<sli:ReadEnforcement>READ_RESTRICTED</sli:ReadEnforcement>
#						<sli:WriteEnforcement>WRITE_RESTRICTED</sli:WriteEnforcement>
#					</xs:appinfo>
#				</xs:annotation>
#			</xs:element>


	return xsd
end


def rename_field(xsd)
	return xsd
end



def merge_migration_config()
	# Modify migration configuration

	src_migration_config_filename = ARGV[1]
	dst_migration_config_filename = ARGV[2]

	src_migration_config = File.read(src_migration_config_filename)
	dst_migration_config = File.read(dst_migration_config_filename)

	src_hash = JSON.parse src_migration_config
	dst_hash = JSON.parse dst_migration_config

	combined_hash = src_hash.deep_merge(dst_hash)
	
	new_dst_json = combined_hash.to_json

	File.open(dst_migration_config_filename, 'w') {|f| f.write(new_dst_json)}
end




# execute main 
main()


