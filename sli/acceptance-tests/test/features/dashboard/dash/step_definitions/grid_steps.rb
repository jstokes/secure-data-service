# returns all trs of a grid in a particular panel
# excludes the header of the grid
def getGrid(panel)
  grid = @explicitWait.until{panel.find_element(:class,"ui-jqgrid-bdiv")}
  all_trs = panel.find_elements(:xpath,".//tr[contains(@class,'ui-widget-content')]")
  return all_trs
end

def getTdBasedOnAttribute(tr,attribute)
  assert(!tr.nil?, "Row is empty")
  searchText = "td[contains(@aria-describedby,'" + attribute + "')]"
  td = tr.find_element(:xpath, searchText)
  return td
end

def getTdsBasedOnAttribute(tr,attribute)
  assert(!tr.nil?, "Row is empty")
  searchText = "td[contains(@aria-describedby,'" + attribute + "')]"
  tds = tr.find_elements(:xpath, searchText)
  return tds
end

def getAttribute(tr, attribute)
  assert(!tr.nil?, "Row is empty")
  searchText = "td[contains(@aria-describedby,'" + attribute + "')]"
  value = tr.find_element(:xpath, searchText)
  return value.text
end

def getAttributes(tr, attribute)
  assert(!tr.nil?, "Row is empty")
  searchText = "td[contains(@aria-describedby,'" + attribute + "')]"
  values = []
  i = 0
  elements = tr.find_elements(:xpath, searchText)
  elements.each do |element|
    if (element.text.length > 0)
      values[i] = element.text
      i += 1
    end
  end
  return values
end

#Checks against entries in a grid
#use <empty> for empty cells
def checkGridEntries(panel, table, mapping)
  table.headers.each do |current|
    if (mapping[current] == nil)
      puts "Warning: No mapping found for header: " + current
      mapping[current] = current      
    end
  end
  
  grid = getGrid(panel)
  assert(table.rows.length == grid.length, "Expected entries: " + table.rows.length.to_s + " Actual: " + grid.length.to_s)
  table.hashes.each do |row|
    found = false
    grid.each do |tr|  
      table.headers.each do |header|
        if (mapping[header].kind_of?(Array))
          #example, fuel gauge tests or visualization
           td = getTdBasedOnAttribute(tr,mapping[header][0])
           value = row[header]
           verifier = mapping[header][1].downcase
           
           if (verifier == "fuelgauge")
            testFuelGauge(td, value)
           end
        else
          value = getAttribute(tr, mapping[header])
          if (value == row[header] || (value.strip == "" && row[header]=="<empty>"))
            found = true
          else
            found = false
            break
          end
        end
      end #table.headers.each
      if (found)
        break
      end #if
    end #grid.each
    if (!found)
      puts "Error: This is the entry that was not found:"
      outputRow(row)
      assert(found, "Entry was not found")
    end
  end #table.hashes.each
end

def outputRow(row)
  assert(row!=nil)
  keys = row.keys
  output = ""
  keys.each do |key|
    output += key + ": " + row[key] + ", "
  end
  puts output
end
