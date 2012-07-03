/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/*global $ contextRootPath*/
var instHierarchy=SLC.dataProxy.getData('populationWidget')['root'];
var courseSectionData;
    
$(document).ready( function() {
	var selectedPopulation=SLC.dataProxy.getData('populationWidget')['selectedPopulation'];
	populateInstHierarchy();
	$("#dbrd_btn_pw_go").click(function() {
		var edOrgIndex = $("#edOrgSelect").val();
		var schoolIndex = $("#schoolSelect").val();
		var courseIndex = $("#courseSelect").val();
		var sectionSelect = $("#sectionSelect").val();
		if (sectionSelect != -1) 
		    location.href = contextRootPath + "/service/list/" + courseSectionData[courseIndex].sections[sectionSelect].id;
	    else if (courseIndex != -1) {}
		else if (schoolIndex != -1)
			location.href = contextRootPath + "/service/layout/school/" + instHierarchy[edOrgIndex].schools[schoolIndex].id;
		else if (edOrgIndex != -1)
			location.href = contextRootPath + "/service/layout/district/" + instHierarchy[edOrgIndex].id;	
		
    });
});

// When a dropdown is unavailable the preceeding dropdowns should be unavailable
// set dropdown option to -1, empty dropdown options , clear dropdown text, disable the dropdown.
function clearSelections(currentSelect) {
	var selects = { "edOrg":1, "school":2, "course":3, "section":4 };
	switch(selects[currentSelect]) {
		case 1: $("#edOrgSelect").val(-1);
				$("#edOrgSelectMenu .optionText").html("");
				$("#edOrgSelectMenu .dropdown-menu").html("");
		case 2: $("#schoolSelect").val(-1);
				$("#schoolSelectMenu .optionText").html("");
				$("#schoolSelectMenu .dropdown-toggle").addClass("disabled");
				$("#schoolSelectMenu .dropdown-menu").html("");
		case 3: $("#courseSelect").val(-1);
				$("#courseSelectMenu .optionText").html("");
				$("#courseSelectMenu .dropdown-toggle").addClass("disabled"); 
				$("#courseSelectMenu .dropdown-menu").html("");
		case 4: $("#sectionSelect").val(-1);
				$("#sectionSelectMenu .optionText").html("");
				$("#sectionSelectMenu .dropdown-toggle").addClass("disabled");
				$("#sectionSelectMenu .dropdown-menu").html("");
	}
}

function getCourseSectionData() {
	var edOrgIndex = $("#edOrgSelect").val();
	var schoolIndex = $("#schoolSelect").val();
	SLC.dataProxy.load('populationCourseSection', instHierarchy[edOrgIndex].schools[schoolIndex].id, function(panel) {	
	    courseSectionData = SLC.dataProxy.getData('populationCourseSection')['root'];
	});
}

function populateInstHierarchy() {
	
	clearSelections("edOrg");
	SLC.util.setDropDownOptions("edOrg", null,  instHierarchy, "name", "", true, function() {
		clearSelections("school");
		populateSchoolMenu();
	});
}

function populateSchoolMenu() {
	var edOrgIndex = $("#edOrgSelect").val();
	clearSelections("school");
	SLC.util.setDropDownOptions("school", null, instHierarchy[edOrgIndex].schools, "nameOfInstitution", "", true, function() {
		clearSelections("course");
		getCourseSectionData();
		populateCourseMenu();
	});
}

function populateCourseMenu(){
	var edOrgIndex = $("#edOrgSelect").val();
	var schoolIndex = $("#schoolSelect").val();
	clearSelections("course");
	SLC.util.setDropDownOptions("course", null, courseSectionData, "courseTitle", "", true, function() {
		clearSelections("section");
		populateSectionMenu();
	});
}

function populateSectionMenu(){
	
	var edOrgIndex = $("#edOrgSelect").val();
	var schoolIndex = $("#schoolSelect").val();
	var courseIndex = $("#courseSelect").val();
	clearSelections("section");
	SLC.util.setDropDownOptions("section", null, courseSectionData[courseIndex].sections, "sectionName", "", false, function() {
	});
}
