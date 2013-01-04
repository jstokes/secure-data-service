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

require_relative 'Enum.rb'

# Enumerates the types of administration environments. From Ed-Fi-Core.xsd:
# <xs:simpleType name="AdministrationEnvironmentType">
#   <xs:annotation>
#     <xs:documentation>The environment in which the test was administered.  For example:
#     Electronic
#     Classroom
#     Testing Center
#     ...
#     </xs:documentation>
#   </xs:annotation>
#   <xs:restriction base="xs:token">
#     <xs:enumeration value="Classroom"/>
#     <xs:enumeration value="School"/>
#     <xs:enumeration value="Testing Center"/>
#   </xs:restriction>
# </xs:simpleType>
class AdministrationEnvironmentType
  include Enum

  AdministrationEnvironmentType.define :CLASSROOM, "Classroom"
  AdministrationEnvironmentType.define :SCHOOL, "School"
  AdministrationEnvironmentType.define :TESTING_CENTER, "Testing Center"
end
