require 'net/http'
require 'rexml/document'
require 'json'

@staff = {}

def extractStaff(ele, role)
   staffUniqueStateIds = ele.elements["StaffUniqueStateId"].text
   @staff[staffUniqueStateIds] = {}
   @staff[staffUniqueStateIds]["userId"] = staffUniqueStateIds
   firstName = ele.elements["Name/FirstName"].text
   lastName = ele.elements["Name/LastSurname"].text
   @staff[staffUniqueStateIds]["name"] = "#{firstName} #{lastName}"
   @staff[staffUniqueStateIds]["role"] = role
   @staff[staffUniqueStateIds]["association"] = ""
end


intputFileName = (ARGV[0].nil? ? "/Users/scole/git/SLI/sli/acceptance-tests/test/features/ingestion/test_data/MediumSampleDataSet/InterchangeStaffAssociation.xml" : ARGV[0])
outputFileName = ARGV[1]

# get the XML data as a string
xml_data = File.open(intputFileName, 'r')

# extract event information
doc = REXML::Document.new(xml_data)



#  <Teacher id="CAP0-D0-HSch0-teacher1">
#    <StaffUniqueStateId>CAP0-D0-HSch0-teacher1</StaffUniqueStateId>
#    <Name Verification="Birth certificate">
#      <FirstName>Clinton</FirstName>
#      <LastSurname>Fitzpatrick</LastSurname>
#    </Name>  
doc.elements.each('*/Teacher') do |ele|
	extractStaff(ele, "Educator")
end


#  <TeacherSchoolAssociation>
#    <TeacherReference ref="CAP0-D0-HSch0-teacher1"/>
# OR   <StaffIdentity>
#        <StaffUniqueStateId>cgray</StaffUniqueStateId>
#      </StaffIdentity>
#    <SchoolReference>
#      <EducationalOrgIdentity>
#        <StateOrganizationId>CAP0-D0-HSch0</StateOrganizationId>     
doc.elements.each('*/TeacherSchoolAssociation') do |ele|
   id = ele.elements["TeacherReference"].attributes["ref"]
   if id == nil
     id = ele.elements["TeacherReference/StaffIdentity/StaffUniqueStateId"].text
   end
   edorg = ele.elements["SchoolReference/EducationalOrgIdentity/StateOrganizationId"].text
   @staff[id]["association"] = edorg
end

#  <Staff id="CAP0-staff0">
#    <StaffUniqueStateId>CAP0-staff0</StaffUniqueStateId>
#    <Name Verification="Other official document">
#      <FirstName>Paulette</FirstName>
#      <LastSurname>Tucker</LastSurname>
doc.elements.each('*/Staff') do |ele|
   extractStaff(ele, "Leader")
end

#  <StaffEducationOrgAssignmentAssociation>
#    <StaffReference ref="CAP0-staff0"/>
#    <EducationOrganizationReference>
#      <EducationalOrgIdentity>
#        <StateOrganizationId>CAP0</StateOrganizationId>
doc.elements.each('*/StaffEducationOrgAssignmentAssociation') do |ele|
   id = ele.elements["StaffReference"].attributes["ref"]
   if id == nil
     id = ele.elements["StaffReference/StaffIdentity/StaffUniqueStateId"].text
   end
   edorg = ele.elements["EducationOrganizationReference/EducationalOrgIdentity/StateOrganizationId"].text
   @staff[id]["association"] = edorg
end

if outputFileName == nil
  print JSON.pretty_generate(@staff.values)
else
  out = File.open(outputFileName, 'w')
  out.write(JSON.pretty_generate(@staff.values))
end