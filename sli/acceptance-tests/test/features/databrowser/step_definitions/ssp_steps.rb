Then /^I can search for <Type> with a <Field> and get a <Result>$/ do |table|
  table.hashes.each do |hash|
    select = @driver.find_element(:tag_name, "select")
    all_options = select.find_elements(:tag_name, "option")
    all_options.each do |option|
      if option.attribute("value") == hash["Type"]
        option.click
        break
      end
    end
    @driver.find_element(:id, "search_id").clear
    @driver.find_element(:id, "search_id").send_keys(hash["Field"])
    @driver.find_element(:css, "input[type='submit']").click
    errors = @driver.find_elements(:id, "flash")
    if hash["Result"] == "Pass"
      assert(errors.size == 0, "Should not be a flash error for #{hash["Type"]}/#{hash["Field"]}")
    else
      assert(errors.size == 1, "There should be an error message for #{hash["Type"]}/#{hash["Field"]}")
    end   
  end
end

When /^I go to the students page$/ do
  url = @driver.current_url.gsub /home/, 'students'
  puts url
  @driver.get(url)
end

When /^I click on the First Name column$/ do
  @first_element = @driver.find_element(:xpath, "//tbody/tr[0]/td[2]")
  @driver.find_element(:xpath, "//tr/th[2]").click
end

Then /^the order of the contents should change$/ do
  assert(@first_element != element, "The elements should have been sorted")
end

When /^I click again$/ do
  step "When I click on the First Name column"
end

Then /^the contents should reverse$/ do
  step "Then the order of the contents should change"
end

Then /^I should see (\d+) students$/ do |arg1|
  assert(@driver.find_elements(:xpath, "//tbody/tr") == arg1, "There should be #{arg1} students")
end

When /^I scroll to the bottom$/ do
  pending # express the regexp above with the code you wish you had
end

When /^wait for (\d+) seconds$/ do |arg1|
  pending # express the regexp above with the code you wish you had
end

Then /^I should see more students$/ do
  pending # express the regexp above with the code you wish you had
end