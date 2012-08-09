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

import java.util.List;

import org.slc.sli.test.edfi.entities.AcademicSubjectType;
import org.slc.sli.test.edfi.entities.AcademicSubjectsType;
import org.slc.sli.test.edfi.entities.EducationalOrgIdentityType;
import org.slc.sli.test.edfi.entities.EducationalOrgReferenceType;
import org.slc.sli.test.edfi.entities.GradeLevelType;
import org.slc.sli.test.edfi.entities.GradeLevelsType;
import org.slc.sli.test.edfi.entities.ProgramAssignmentType;
import org.slc.sli.test.edfi.entities.Ref;
import org.slc.sli.test.edfi.entities.StaffIdentityType;
import org.slc.sli.test.edfi.entities.StaffReferenceType;
import org.slc.sli.test.edfi.entities.TeacherSchoolAssociation;
import org.slc.sli.test.edfi.entities.meta.TeacherMeta;
import org.slc.sli.test.edfi.entities.meta.relations.MetaRelations;

public class TeacherSchoolAssociationGenerator {
    public TeacherSchoolAssociation generate(String staffId, List<String> stateOrgIds) {
        TeacherSchoolAssociation tsa = new TeacherSchoolAssociation();

        tsa.setTeacherReference(TeacherGenerator.getTeacherReference(staffId));

        EducationalOrgIdentityType eoit = new EducationalOrgIdentityType();

        for (String stateOrgId : stateOrgIds) {
//            eoit.getStateOrganizationIdOrEducationOrgIdentificationCode().add(stateOrgId);
            eoit.setStateOrganizationId(stateOrgId);
        }

        EducationalOrgReferenceType eor = new EducationalOrgReferenceType();
        eor.setEducationalOrgIdentity(eoit);
        tsa.getSchoolReference().add(eor);

        tsa.setProgramAssignment(ProgramAssignmentType.REGULAR_EDUCATION);

        GradeLevelsType glt = new GradeLevelsType();
        glt.getGradeLevel().add(GradeLevelType.EARLY_EDUCATION);
        tsa.setInstructionalGradeLevels(glt);

        AcademicSubjectsType ast = new AcademicSubjectsType();
        ast.getAcademicSubject().add(AcademicSubjectType.COMPUTER_AND_INFORMATION_SCIENCES);
        tsa.setAcademicSubjects(ast);
        return tsa;
    }

    public static TeacherSchoolAssociation generateLowFi(TeacherMeta teacherMeta, String schoolId) {

        TeacherSchoolAssociation teacherSchool = new TeacherSchoolAssociation();

        // construct and add the school references
        EducationalOrgIdentityType edOrgIdentity = new EducationalOrgIdentityType();
//        edOrgIdentity.getStateOrganizationIdOrEducationOrgIdentificationCode().add(schoolId);
        edOrgIdentity.setStateOrganizationId(schoolId);

        EducationalOrgReferenceType schoolRef = new EducationalOrgReferenceType();
        schoolRef.setEducationalOrgIdentity(edOrgIdentity);
   
        
        teacherSchool.getSchoolReference().add(schoolRef);

       
        // construct and add the teacher reference
        /*
        StaffIdentityType staffIdentity = new StaffIdentityType();
        staffIdentity.setStaffUniqueStateId(teacherMeta.id);

        StaffReferenceType teacherRef = new StaffReferenceType();
        teacherRef.setStaffIdentity(staffIdentity);

        teacherSchool.setTeacherReference(teacherRef);
        */
        
		if (MetaRelations.TeacherSchoolAssociation_Ref) {
			Ref teacherRefer = new Ref(teacherMeta.id);
			StaffReferenceType teacherRef = new StaffReferenceType();
			teacherRef.setRef(teacherRefer);
			teacherSchool.setTeacherReference(teacherRef);
		} else {
			StaffIdentityType staffIdentity = new StaffIdentityType();
			staffIdentity.setStaffUniqueStateId(teacherMeta.id);
			StaffReferenceType teacherRef = new StaffReferenceType();
			teacherRef.setStaffIdentity(staffIdentity);

			teacherSchool.setTeacherReference(teacherRef);
		}

        teacherSchool.setProgramAssignment(ProgramAssignmentType.REGULAR_EDUCATION);

        GradeLevelsType glt = new GradeLevelsType();
        glt.getGradeLevel().add(GradeLevelType.EARLY_EDUCATION);
        teacherSchool.setInstructionalGradeLevels(glt);

        AcademicSubjectsType ast = new AcademicSubjectsType();
        ast.getAcademicSubject().add(AcademicSubjectType.COMPUTER_AND_INFORMATION_SCIENCES);
        teacherSchool.setAcademicSubjects(ast);
        return teacherSchool;
    }
}
