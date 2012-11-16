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

# Enumerates each grade level for when a student started at a given school. From Ed-Fi-Core.xsd: 
# <xs:simpleType name="GradeLevelType"> 
#   <xs:annotation>
#     <xs:documentation>The enumeration items for the set of grade levels.</xs:documentation>
#   </xs:annotation> 
#   <xs:restriction base="xs:token">
#     <xs:enumeration value="Adult Education"/> 
#     <xs:enumeration value="Early Education"/> 
#     <xs:enumeration value="Eighth grade"/>
#     <xs:enumeration value="Eleventh grade"/> 
#     <xs:enumeration value="Fifth grade"/> 
#     <xs:enumeration value="First grade"/> 
#     <xs:enumeration value="Fourth grade"/>
#     <xs:enumeration value="Grade 13"/> 
#     <xs:enumeration value="Infant/toddler"/> 
#     <xs:enumeration value="Kindergarten"/>
#     <xs:enumeration value="Ninth grade"/> 
#     <xs:enumeration value="Other"/>
#     <xs:enumeration value="Postsecondary"/> 
#     <xs:enumeration value="Preschool/Prekindergarten"/> 
#     <xs:enumeration value="Second grade"/>
#     <xs:enumeration value="Seventh grade"/> 
#     <xs:enumeration value="Sixth grade"/>
#     <xs:enumeration value="Tenth grade"/> 
#     <xs:enumeration value="Third grade"/>
#     <xs:enumeration value="Transitional Kindergarten"/> 
#     <xs:enumeration value="Twelfth grade"/> 
#     <xs:enumeration value="Ungraded"/> 
#   </xs:restriction>
# </xs:simpleType>
class GradeLevelType
  include Enum

  GradeLevelType.define :ADULT_EDUCATION, "Adult Education"
  GradeLevelType.define :EARLY_EDUCATION, "Early Education"
  GradeLevelType.define :EIGHTH_GRADE, "Eighth grade"
  GradeLevelType.define :ELEVENTH_GRADE, "Eleventh grade"
  GradeLevelType.define :FIFTH_GRADE, "Fifth grade"
  GradeLevelType.define :FIRST_GRADE, "First grade"
  GradeLevelType.define :FOURTH_GRADE, "Fourth grade"
  GradeLevelType.define :GRADE_13, "Grade 13"
  GradeLevelType.define :INFANT_TODDLER, "Infant/toddler"
  GradeLevelType.define :KINDERGARTEN, "Kindergarten"
  GradeLevelType.define :NINTH_GRADE, "Ninth grade"
  GradeLevelType.define :OTHER, "Other"
  GradeLevelType.define :POSTSECONDARY, "Postsecondary"
  GradeLevelType.define :PRESCHOOL_PREKINDERGARTEN, "Preschool/Prekindergarten"
  GradeLevelType.define :SECOND_GRADE, "Second grade"
  GradeLevelType.define :SEVENTH_GRADE, "Seventh grade"
  GradeLevelType.define :SIXTH_GRADE, "Sixth grade"
  GradeLevelType.define :TENTH_GRADE, "Tenth grade"
  GradeLevelType.define :THIRD_GRADE, "Third grade"
  GradeLevelType.define :TRANSITIONAL_KINDERGARTEN, "Transitional Kindergarten"
  GradeLevelType.define :TWELFTH_GRADE, "Twelfth grade"
  GradeLevelType.define :UNGRADED, "Ungraded"
end
