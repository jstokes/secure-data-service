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


package org.slc.sli.test.generators;

import org.slc.sli.test.edfi.entities.EducationalOrgIdentityType;
import org.slc.sli.test.edfi.entities.EducationalOrgReferenceType;
import org.slc.sli.test.edfi.entities.GradeLevelType;
import org.slc.sli.test.edfi.entities.Ref;
import org.slc.sli.test.edfi.entities.ReferenceType;
import org.slc.sli.test.edfi.entities.StudentIdentityType;
import org.slc.sli.test.edfi.entities.StudentReferenceType;
import org.slc.sli.test.edfi.entities.StudentSchoolAssociation;
import org.slc.sli.test.edfi.entities.meta.relations.MetaRelations;



public class StudentSchoolAssociationGenerator {

    public static StudentSchoolAssociation generateLowFi(String studentId, String schoolId) {

    	StudentSchoolAssociation ssa = new StudentSchoolAssociation();
    	
		if (MetaRelations.StudentSchoolAssociation_Ref) {
			String graduationPlan = schoolId + "-gPlan0";
			Ref gPlan = new Ref(graduationPlan);
			ReferenceType refType = new ReferenceType();
			refType.setRef(gPlan);
			ssa.setGraduationPlanReference(refType);
		}
    	
        StudentIdentityType sit = new StudentIdentityType();
        sit.setStudentUniqueStateId(studentId);
        StudentReferenceType srt = new StudentReferenceType();
        srt.setStudentIdentity(sit);
        ssa.setStudentReference(srt);

        EducationalOrgIdentityType eoit = new EducationalOrgIdentityType();
//        eoit.getStateOrganizationIdOrEducationOrgIdentificationCode().add(schoolId);
        eoit.setStateOrganizationId(schoolId);
        EducationalOrgReferenceType eor = new EducationalOrgReferenceType();
        eor.setEducationalOrgIdentity(eoit);
        ssa.setSchoolReference(eor);

        ssa.setEntryGradeLevel(GradeLevelType.FIFTH_GRADE);
        ssa.setSchoolYear("2011-2012");
        ssa.setEntryDate("2011-09-01");

        return ssa;
    }
}
