require 'selenium-webdriver'

When /^I click on student "([^"]*)"$/ do |name|
  clickOnStudent(name)
end

When /^I view its student profile$/ do

  csiContent = @explicitWait.until{@driver.find_element(:class, "csi")}
  studentInfo = csiContent.find_element(:class, "studentInfo")
  table_cells = studentInfo.find_elements(:xpath, ".//div[@class='field']/span")
  @info = Hash.new
  sName = csiContent.find_element(:xpath, ".//div[@class='colMain']/h1") 
  
  @info["Name"] = sName.text
  puts sName.text
  
for i in 0..table_cells.length-1
  if table_cells[i].text.length > 0 
    puts table_cells[i].text
      if i % 2 == 0
        key = table_cells[i].text
      else
        @info[key]= table_cells[i].text
      end
   end
 end
end

Then /^their name shown in profile is "([^"]*)"$/ do |expectedStudentName|
   containsName = @info["Name"] == expectedStudentName
   assert(containsName, "Actual name is :" + @info["Name"]) 
end

When /^their nickname shown in profile is "([^"]*)"$/ do |expectedNickName|
  csiContent = @driver.find_element(:class, "csi")
  nickName = csiContent.find_element(:xpath,".//div[@class='colMain']/small")
  assert(nickName.text == expectedNickName, "Actual displayed nickname is: " + nickName.text)
end

Then /^their id shown in proflie is "([^"]*)"$/ do |studentId|
  assert(@info["ID"] == studentId, "Actual ID is: " + @info["ID"])
end

Then /^their grade is "([^"]*)"$/ do |studentGrade|
 assert(@info["Grade"] == studentGrade, "Actual Grade is: " + @info["Grade"])
end

Then /^the lozenges include "([^"]*)"$/ do |lozenge|
  csiContent = @driver.find_element(:class, "csi")
  labelFound = false
  
  all_lozenge = csiContent.find_elements(:xpath, ".//div[contains(@class,'lozenge-widget')]")
  all_lozenge.each do |lozengeElement|
    if lozengeElement.attribute("innerHTML").to_s.include?(lozenge)
      labelFound = true
    end
  end
  
  assert(labelFound == true)
end

Then /^the teacher is "([^"]*)"$/ do |teacherName|
  assert(@info["Teacher"] == teacherName, "Actual teacher is :" + @info["Teacher"]) 
end

When /^the class is "([^"]*)"$/ do |className|
  assert(@info["Class"] == className, "Actual class is :" + @info["Class"]) 
end

When /^the lozenges count is "([^"]*)"$/ do |lozengesCount|
  csiContent = @driver.find_element(:class, "csi")
  labelFound = false

  all_lozenges = csiContent.find_elements(:xpath, ".//div[contains(@class,'lozenge-widget')]")

  assert(lozengesCount.to_i == all_lozenges.length, "Actual lozenges count is:" + all_lozenges.length.to_s)
end

Then /^there are "([^"]*)" student enrollment history entries$/ do |numEntries|
  rows = getEnrollmentHistoryEntries()
  assert(numEntries == (rows.length-1).to_s, "Actual number of enrollment history entries: " + (rows.length-1).to_s)
end

# the order of expected enrollment history is: schoolYear, school, gradeLevel, entryDate, transfer, exitWithdrawDate, exitWithdrawType
Then /^Student Enrollment History in row "([^"]*)" includes "([^"]*)"$/ do |rowIndex, expectedEnrollment|
  found = verifyEnrollmentHistoryEntryExists(rowIndex, expectedEnrollment)
  assert(found, "Enrollment is not found")
end

Then /^Student Enrollment History includes "([^"]*)"$/ do |expectedEnrollment|
  rows = getEnrollmentHistoryEntries()
  found = false
  for i in (1..rows.length-1)
    found = verifyEnrollmentHistoryEntryExists(i, expectedEnrollment)
    if (found)
      break
    end
  end
  assert(found, "Enrollment is not found")
end

When /^I see a header on the page that has the text "([^"]*)"$/ do |expectedText|
  header = @explicitWait.until{@driver.find_element(:class, "div_main")}
  logo = header.find_elements(:tag_name,"img")
  assert(logo.length == 1, "Header logo img is not found")
  headerText = header.find_element(:class, "header_right")
  
  assert(headerText.attribute("innerHTML").to_s.lstrip.rstrip.include?(expectedText), "Header text is not found")
end

When /^I see a footer on the page that has the text "([^"]*)"$/ do |expectedText|
  footer = @driver.find_element(:class, "div_footer")
  assert(footer.attribute("innerHTML").to_s.lstrip.rstrip.include?(expectedText), "Footer text is not found")
end

def clickOnStudent(name)
  los = @explicitWait.until{@driver.find_element(:class, "ui-jqgrid-bdiv")}
  
  @driver.find_element(:link, name).click
end

def getEnrollmentHistoryEntries()
  enrollmentPanel = getPanel("Enrollment History", "Overview")
 
  enrollmentTable = enrollmentPanel.find_element(:xpath, "//div[@class='ui-jqgrid-bdiv']")
  rows = enrollmentTable.find_elements(:tag_name, "tr")
  
  assert(rows.length > 1, "Is the enrollment history missing?")
  return rows
end

def verifyEnrollmentHistoryEntryExists(rowIndex, expectedEnrollment)
  expectedArray = expectedEnrollment.split(';')
  rowIndex = rowIndex.to_i
  assert(expectedArray.length == 8, "Missing expected enrollment history element, actual # of elements: " + expectedArray.length.to_s )

  rows = getEnrollmentHistoryEntries()
  
  # rowIndex > 0 as index 0 is the header
  assert(rowIndex <= rows.length-1 && rowIndex > 0, "Invalid rowIndex")
   
  found = true
  schoolYear = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'schoolYear')]")
  school = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'nameOfInstitution')]")
  gradeLevel = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'entryGradeLevel')]")
  entryDate = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'entryDate')]")
  entryType = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'entryType')]")
  transfer = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'transfer')]")
  exitWithdrawDate = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'exitWithdrawDate')]")
  exitWithdrawType = rows[rowIndex].find_element(:xpath, "td[contains(@aria-describedby,'exitWithdrawType')]")
    
  enrollmentArray = [ schoolYear, school, gradeLevel, entryDate, entryType, transfer, exitWithdrawDate, exitWithdrawType ] 
    
  for j in (0..expectedArray.length-1)
    if (enrollmentArray[j].text != expectedArray[j])
      found = false
      #puts "Discrency - Actual: " + enrollmentArray[j].text + " Expected: " + expectedArray[j]
    end
  end  
  return found  
end

