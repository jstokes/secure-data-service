// This should get cleaned up. 
// Put these under its onwn namespace, at least. 

function populateInstHierarchy(){
    var y = "<select id=\"edOrgSelect\" onChange=\"populateSchoolMenu(this.value)\">"
    y += "<option value=\"-1\"></option>"
    var i = 0;
    for(i = 0;i<instHierarchy.length;i++){
        y += "<option value=\"" +i +"\">"+ instHierarchy[i].name + "</option>"
    }
    y += "</select>"
    document.getElementById("edorgDiv").innerHTML = y;
}

function populateSchoolMenu(edorgIndex){
    var temp = instHierarchy[edorgIndex].schools

    var y = "<select id=\"schoolSelect\" onChange=\"populateCourseMenu("+edorgIndex+",this.value)\">"
    y += "<option value=\"-1\"></option>"   
    var i = 0;
    for(i = 0;i<temp.length;i++){
        y += "<option value=\"" +i +"\">"+ temp[i].nameOfInstitution + "</option>"
    }
    y += "</select>"
    document.getElementById("schoolDiv").innerHTML = y;
}

function populateCourseMenu(edorgIndex,schoolIndex){
    var temp = instHierarchy[edorgIndex].schools[schoolIndex].courses
 
    var y = "<select id=\"courseSelect\" onChange=\"populateSectionMenu("+edorgIndex+","+schoolIndex+",this.value)\">"
    y += "<option value=\"\"></option>"
    var j = 0;
    for(j = 0;j < temp.length;j++){
        y += "<option value=\"" +j +"\">"+ temp[j].courseTitle + "</option>"
    }
    document.getElementById("courseDiv").innerHTML = y
}

function populateSectionMenu(edorgIndex,schoolIndex, courseIndex){
    var temp = instHierarchy[edorgIndex].schools[schoolIndex].courses[courseIndex].sections
    var y = "<select id=\"sectionSelect\" onChange=\"printStudentList("+edorgIndex+","+schoolIndex+","+courseIndex+", this.value, 0)\">"
    y += "<option value=\"\"></option>"
    var i = 0
    for(;i < temp.length;i++){
        y += "<option value=\"" +i +"\">"+ temp[i].sectionName + "</option>"
    }
    y += "</select>"
    document.getElementById("sectionDiv").innerHTML = y
}

function changeView(viewIndex) {
    var edOrgIndex = document.getElementById("edOrgSelect").value;
    var schoolIndex = document.getElementById("schoolSelect").value;
    var courseIndex = document.getElementById("courseSelect").value;
    var sectionIndex = document.getElementById("sectionSelect").value;
    var filterIndex = null;
    if (document.getElementById("studentFilterSelector")) {
        filterIndex = document.getElementById("studentFilterSelector").value;
    }
    printStudentList(edOrgIndex, schoolIndex, courseIndex, sectionIndex, viewIndex, filterIndex);
}

function filterStudents(filterIndex) {
    var edOrgIndex = document.getElementById("edOrgSelect").value;
    var schoolIndex = document.getElementById("schoolSelect").value;
    var courseIndex = document.getElementById("courseSelect").value;
    var sectionIndex = document.getElementById("sectionSelect").value;
    var viewIndex = document.getElementById("viewSelector").value;
    printStudentList(edOrgIndex, schoolIndex, courseIndex, sectionIndex, viewIndex, filterIndex);
}

function printStudentList(edorgIndex,schoolIndex, courseIndex, sectionIndex, viewIndex, filterIndex){
    var i = 0;
    filterIndex = filterIndex == null ? 0 : filterIndex;
    var temp = instHierarchy[edorgIndex].schools[schoolIndex].courses[courseIndex].sections[sectionIndex].studentUIDs;
    var sessionId = instHierarchy[edorgIndex].schools[schoolIndex].courses[courseIndex].sections[sectionIndex].sessionId;
    var courseId = instHierarchy[edorgIndex].schools[schoolIndex].courses[courseIndex].id;
    var sectionId = instHierarchy[edorgIndex].schools[schoolIndex].courses[courseIndex].sections[sectionIndex].id;
    // This is going to change when we figure out what the API should be. 
    var studentUIDs = temp.join(',');
    var studentContentUrl = "studentlistcontent?population=" + studentUIDs 
                            + "&viewIndex=" + viewIndex + "&filterIndex=" + filterIndex 
                            + "&username=" + "${username}" + "&sessionId=" + sessionId + "&courseId=" + courseId + "&sectionId=" + sectionId;
    
    // make ajax call and process response                        
    $("#studentDiv").load(studentContentUrl, function(responseText, textStatus, XMLHttpRequest) {
    
        DashboardUtil.checkAjaxError(XMLHttpRequest, studentContentUrl);
    
        document.getElementById("viewSelector").selectedIndex = viewIndex;
        if (document.getElementById("studentFilterSelector")) {
            document.getElementById("studentFilterSelector").selectedIndex = filterIndex;        
        }
    });
}

function studentProfilePopup(id){
	var studentProfileUrl = "studentprofile?id="+id;
	xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange=function()
	  {
	  if (xmlHttp.readyState==4 && xmlHttp.status==200)
	    {
		  var respText =xmlHttp.responseText;
		  document.getElementById("InnerStudentProfile").innerHTML = respText;
	    
	    }
	  };
	xmlHttp.open("GET", studentProfileUrl, true);
	xmlHttp.send();
	
	centerPopup();
	loadPopup();
}

