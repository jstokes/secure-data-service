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

# Enumerates the types of attendance event categories. From Ed-Fi-Core.xsd:
# <xs:simpleType name="AttendanceEventCategoryType">
#   <xs:annotation>
#     <xs:documentation>A code categorizing the attendance event (e.g., excused absence, unexcused absence)</xs:documentation>
#   </xs:annotation>
#   <xs:restriction base="xs:token">
#     <xs:enumeration value="In Attendance"/>
#     <xs:enumeration value="Absence"/>
#     <xs:enumeration value="Excused Absence"/>
#     <xs:enumeration value="Unexcused Absence"/>
#     <xs:enumeration value="Tardy"/>
#     <xs:enumeration value="Early departure"/>
#   </xs:restriction>
# </xs:simpleType>
class AttendanceEventCategory
  include Enum

  AttendanceEventCategory.define :ABSENT, "Absence"
  AttendanceEventCategory.define :EARLY_DEPARTURE, "Early departure"
  AttendanceEventCategory.define :EXCUSED_ABSENCE, "Excused Absence"
  AttendanceEventCategory.define :PRESENT, "In Attendance"
  AttendanceEventCategory.define :TARDY, "Tardy"
  AttendanceEventCategory.define :UNEXCUSED_ABSENCE, "Unexcused Absence"
end
