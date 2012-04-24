require 'selenium-webdriver'

And /^there are "([^"]*)" Tabs$/ do |tabCount|
  allTabs = @driver.find_element(:id, "tabs")
  tab = allTabs.find_elements(:tag_name, "li")
  assert(tabCount.to_i == tab.length, "Actual Tab Count: " + tab.length.to_s)
end

Given /^Tab "([^"]*)" is titled "([^"]*)"$/ do |tabIndex, tabTitle|
  allTabs = @driver.find_element(:id, "tabs")
  tab = allTabs.find_elements(:tag_name, "li")
  title = tab[tabIndex.to_i-1].find_element(:tag_name, "a")
  assert(title.text == tabTitle, "Actual Title: " + title.text)
end

When /^in Tab ID "([^"]*)", there is "([^"]*)" Panels$/ do |tabId, panelCount|
  element = @driver.find_element(:id, "page-tab" + tabId)
  tabs = element.find_elements(:class, "panel")
  assert(tabs.length == panelCount.to_i, "Actual # of Panels: " + tabs.length.to_s)
end

Then /^in "([^"]*)" tab, there are "([^"]*)" Panels$/ do |tabName, panelCount|
  tabId = getTabId(tabName)
  element = @driver.find_element(:id, "page-tab" + tabId)
  tabs = element.find_elements(:class, "panel")
  assert(tabs.length == panelCount.to_i, "Actual # of Panels: " + tabs.length.to_s)
end

When /^Tab has a title named "([^"]*)"$/ do |tabTitle|
  getTabId(tabTitle)
end

def getTabId(tabTitle)
  allTabs = @driver.find_element(:id, "tabs")
  tabs = allTabs.find_elements(:tag_name, "li")
 
  found = false;
  id = nil;
  tabs.each do |tab|
    title = tab.find_element(:tag_name, "a") 
    link = title.attribute("href")
    if (title.text == tabTitle && link =~ /page-tab(\d+)/)
      found = true
      id = $1
    end
  end
  assert(found == true, "Tab title was not found: " + tabTitle)
  return id
end
