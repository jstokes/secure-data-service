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


package org.slc.sli.test.utils;

/**
 * File Type enumerator.
 *
 */
public enum InterchangeType {
    
    STUDENT("Student"),
    STUDENT_PARENT_ASSOCIATION("Parent"),
    EDUCATION_ORGANIZATION("EducationOrganization"),
    EDUCATION_ORG_CALENDAR("EducationOrgCalendar"),
    MASTER_SCHEDULE("MasterSchedule"),
    STAFF_ASSOCIATION("StaffAssociation"),
    STUDENT_ENROLLMENT("StudentEnrollment"),
    ASSESSMENT_METADATA("AssessmentMetadata"),
    STUDENT_ASSESSMENT("StudentAssessment"),
    STUDENT_ATTENDANCE("Attendance"),
    STUDENT_GRADES("StudentGrades");

    private String name;

    InterchangeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
