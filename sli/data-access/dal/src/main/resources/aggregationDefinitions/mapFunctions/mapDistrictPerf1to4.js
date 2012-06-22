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



mapDistrictPerf1to4 = function() {
    
    //initialize result to all zeroes
    var level1 = level2 = level3 = level4 = 0;
    //increment variable representing this record's performance level
    switch(parseInt(this.body.performanceLevel))
    {
        case 1: level1++; break;
        case 2: level2++; break;
        case 3: level3++; break;
        case 4: level4++; break;
        default: break;
    }
    
    //create new result
    var values = {level1:level1,level2:level2,level3:level3,level4:level4};
    //new array to track associated districts (cannot hold duplicates)
    var districts = new Array();
    
    //list schools for student
    db.studentSchoolAssociation.find({"body.studentId":this.body.studentId}).forEach(
        function (ssa) {
            //list districts for school
            db.educationOrganizationSchoolAssociation.find({"body.schoolId":ssa.body.schoolId}).forEach(
                function (eosa) {
                    //register district as associated
                    districts[eosa.body.educationOrganizationId] = eosa.body.educationOrganizationId;
                }
            );
        }
    );
    
    //for each associated district
    for( districtId in districts) {    
        emit({"districtId":districtId,"assessmentType":aggregation_name}, values); 
    }
};

db.system.js.save({ "_id" : "mapDistrictPerf1to4" , "value" : mapDistrictPerf1to4 })