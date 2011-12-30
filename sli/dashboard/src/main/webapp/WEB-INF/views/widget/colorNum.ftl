<#-- widget that displays an assessment data point, with color depending on performance level -->
<#-- Used by: student list -->
<#-- required objects in the model map: 
     field: config info about the data to be displayed
     assessments: contains assessment results for the list of students. Should be AssessmentResolver object
     student: a Student object
     assessmentInfo: assessment meta-data, including cutpoints
  -->

<#-- figure out color css class based on perf level and number of levels -->


${assessments.get(dataPointId, student)}