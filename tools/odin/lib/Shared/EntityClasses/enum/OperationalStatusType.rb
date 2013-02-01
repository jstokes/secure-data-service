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

# Enumerates the types of operational statuses. From Ed-Fi-Core.xsd:
# <xs:simpleType name="OperationalStatusType">
#   <xs:annotation>
#     <xs:documentation>The current operational status of the education organization (e.g., active, inactive).  </xs:documentation>
#   </xs:annotation>
#   <xs:restriction base="xs:token">
#     <xs:enumeration value="Active"/>
#     <xs:enumeration value="Added"/>
#     <xs:enumeration value="Changed Agency"/>
#     <xs:enumeration value="Closed"/>
#     <xs:enumeration value="Continuing"/>
#     <xs:enumeration value="Future"/>
#     <xs:enumeration value="Inactive"/>
#     <xs:enumeration value="New"/>
#     <xs:enumeration value="Reopened"/>
#   </xs:restriction>
# </xs:simpleType>
class OperationalStatusType
  include Enum

  OperationalStatusType.define :ACTIVE, "Active"
  OperationalStatusType.define :ADDED, "Added"
  OperationalStatusType.define :CHANGED_AGENCY, "Changed Agency"
  OperationalStatusType.define :CLOSED, "Closed"
  OperationalStatusType.define :CONTINUING, "Continuing"
  OperationalStatusType.define :FUTURE, "Future"
  OperationalStatusType.define :INACTIVE, "Inactive"
  OperationalStatusType.define :NEW, "New"
  OperationalStatusType.define :REOPENED, "Reopened"
end
