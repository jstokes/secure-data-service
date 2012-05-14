require 'selenium-webdriver'

When /^I click on student "([^"]*)"$/ do |name|
  clickOnStudent(name)
end

When /^I view its student profile$/ do

  csiContent = @explicitWait.until{@driver.find_element(:class, "csi")}
  studentInfo = csiContent.find_element(:class, "studentInfo")
  table_cells = studentInfo.find_elements(:tag_name,"tr")
  @info = Hash.new
  sName = csiContent.find_element(:xpath, ".//div[@class='colMain']/h1") 
  
  @info["Name"] = sName.text
  puts sName.text
  
  for i in 0..table_cells.length-1
   th = table_cells[i].find_element(:tag_name,"th") 
   td = table_cells[i].find_element(:tag_name,"td") 
   puts th.text[0..th.text.length-2]
   puts td.text
   key = th.text[0..th.text.length-2]
   @info[key]= td.text
  end
end

Then /^their name shown in profile is "([^"]*)"$/ do |expectedStudentName|
   containsName = @info["Name"] == expectedStudentName
   assert(containsName, "Actual name is :" + @info["Name"]) 
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
  
  all_lozenge = csiContent.find_elements(:xpath, ".//span[contains(@class,'lozenge-widget')]")
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

  all_lozenges = csiContent.find_elements(:xpath, ".//span[contains(@class,'lozenge-widget')]")

  assert(lozengesCount.to_i == all_lozenges.length, "Actual lozenges count is:" + all_lozenges.length.to_s)
end

When /^I see a header on the page that has the text "([^"]*)"$/ do |expectedText|
  header = @explicitWait.until{@driver.find_element(:id, "sli_banner")}
  logo = header.find_elements(:tag_name,"img")
  assert(logo.length == 1, "Header logo img is not found")
  headerText = header.find_element(:class, "header_right")
  
  assert(headerText.attribute("innerHTML").to_s.lstrip.rstrip.include?(expectedText), "Header text is not found")
end

When /^I see a footer on the page that has the text "([^"]*)"$/ do |expectedText|
  footer = @driver.find_element(:id, "sli_footer")
  assert(footer.attribute("innerHTML").to_s.lstrip.rstrip.include?(expectedText), "Footer text is not found")
end

def clickOnStudent(name)
  los = @explicitWait.until{@driver.find_element(:class, "ui-jqgrid-bdiv")}
  
  @driver.find_element(:link, name).click
end