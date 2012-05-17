Then /^the fuel gauge for "([^"]*)" in "([^"]*)" is "([^"]*)" with cutpoints "([^"]*)"$/ do |studentName, assessment, score, cutpointsArray|
  studentCell = getStudentCell(studentName)
  td = getTdBasedOnAttribute(studentCell, assessment)
  testFuelGauge(td, score, cutpointsArray)
end

def testFuelGauge(td, score, cutpointsArray)

  cutpoints = []
  colorCode = ["#eeeeee","#b40610", "#e58829","#dfc836", "#7fc124","#438746"]
  scoreValue = nil
  cutpoints = cutpointsArray.split(',')

  scoreValue = td.attribute("title")
  assert(score == scoreValue, "Expected: " + score + " but found: " + scoreValue)
  
  rects = td.find_elements(:tag_name,"rect")
  # use the 2nd rect
  filledPercentage = rects[1].attribute("width")
  color = rects[1].attribute("fill")
  index = 0
  
  cutpoints.each do |cutPoint|
    if (cutPoint.to_i <= score.to_i)
         index += 1
     else
      break;
    end
  end
  # we need to look at the previous index count to get the color
  colorIndex = 0
  if (index > 0 )
    colorIndex = index 
  end
  assert(color == colorCode[colorIndex], "Actual Color: " + color + " Expected Color: " + colorCode[colorIndex] + " at index " + index.to_s)
  
  expectedMaxPercentage = (index.to_f/4)*100
  expectedMinPercentage = 0
  if (index > 0)
    expectedMinPercentage = ((index-1).to_f/4)*100
  end
 
  puts "expected percentage range: " + expectedMinPercentage.to_s + " to " + expectedMaxPercentage.to_s + " Actual: " + filledPercentage
  assert((expectedMinPercentage <= filledPercentage.to_f && expectedMaxPercentage >= filledPercentage.to_f), "Actual Fuel Gauge Percentage is not within range")

end