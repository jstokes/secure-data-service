<#-- The body of List of Student view -->
<#-- Used by: StudentListContentController.studentListContent() -->
<#-- required objects in the model map: 
     viewConfig: the view config for the list of students. Should be ViewConfig object
     assessments: contains assessment information for the list of students. Should be AssessmentResolver object
     students: contains the list of students to be displayed. Should be StudentResolver object. 
  -->

<table id="studentList"> 

<#-- draw header -->
<tr class="listHeader">
<#list viewConfig.getDisplaySet() as displaySet>
  <th colspan=${displaySet.getField()?size}}>${displaySet.getDisplayName()}</th>
</#list>
</tr>
<tr class="listHeader">
<#list viewConfig.getDisplaySet() as displaySet>
  <#list displaySet.getField() as field>
    <th>${field.getDisplayName()}</th>
  </#list>
</#list>
</tr>

<#-- draw body --> 
<#list students.list() as student>

<tr class="listRow">
<#list viewConfig.getDisplaySet() as displaySet>
  <#list displaySet.getField() as field>
    <td class="${field.getValue()}">
      <#-- try out each resolver to see if this data point can be resolved using it -->
      <#assign dataPointId = field.getValue()>
      
      <#-- assessment results -->
      <#if assessments.canResolve(dataPointId)>
        <#if field.getVisual()?? && (field.getVisual()?length > 0)>
          <#include "widget/" + field.getVisual() + ".ftl">
        <#else>
          ${assessments.get(dataPointId, student)}
        </#if>
        
      <#-- student info -->
      <#elseif students.canResolve(dataPointId)>
        ${students.get(dataPointId, student)}
        
      <#else>
        <#-- No resolver found. Report an error. -->
        Cannot resolve this field. Check your view config xml.
      </#if>
    </td>
  </#list>
</#list>
</tr>

</#list>

</table>


