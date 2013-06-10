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

require_relative 'Enum.rb'

# Enumerates the types of school terms available.
# From SLI-Ed-Fi-Core.xsd:
# <xs:simpleType name="SLC-TermType">
#   <xs:annotation>
#     <xs:documentation>The type of the session during the school year (e.g., Fall Semester).</xs:documentation>
#   </xs:annotation>
#   <xs:restriction base="xs:token">
#     <xs:enumeration value="Fall Semester"/>
#     <xs:enumeration value="Spring Semester"/>
#     <xs:enumeration value="Summer Semester"/>
#     <xs:enumeration value="First Trimester"/>
#     <xs:enumeration value="Second Trimester"/>
#     <xs:enumeration value="Third Trimester"/>
#     <xs:enumeration value="Year Round"/>
#     <xs:enumeration value="MiniTerm"/>
#     <xs:enumeration value="Term 0"/>
#     <xs:enumeration value="Term 1"/>
#     <xs:enumeration value="Term 2"/>
#     <xs:enumeration value="Term 3"/>
#     <xs:enumeration value="Term 4"/>
#     <xs:enumeration value="Term 5"/>
#     <xs:enumeration value="Term 6"/>
#     <xs:enumeration value="Term 7"/>
#     <xs:enumeration value="Term 8"/>
#   </xs:restriction>
# </xs:simpleType>
class SchoolTerm
  include Enum

  SchoolTerm.define :FALL_SEMESTER, "Fall Semester"
  SchoolTerm.define :FIRST_TRIMESTER, "First Trimester"
  SchoolTerm.define :MINI_TERM, "MiniTerm"
  SchoolTerm.define :SECOND_TRIMESTER, "Second Trimester"
  SchoolTerm.define :SPRING_SEMESTER, "Spring Semester"
  SchoolTerm.define :SUMMER_SEMESTER, "Summer Semester"
  SchoolTerm.define :THIRD_TRIMESTER, "Third Trimester"
  SchoolTerm.define :YEAR_ROUND, "Year Round"
  SchoolTerm.define :TERM_0, "Term 0"
  SchoolTerm.define :TERM_1, "Term 1"
  SchoolTerm.define :TERM_2, "Term 2"
  SchoolTerm.define :TERM_3, "Term 3"
  SchoolTerm.define :TERM_4, "Term 4"
  SchoolTerm.define :TERM_5, "Term 5"
  SchoolTerm.define :TERM_6, "Term 6"
  SchoolTerm.define :TERM_7, "Term 7"
  SchoolTerm.define :TERM_8, "Term 8"
end
