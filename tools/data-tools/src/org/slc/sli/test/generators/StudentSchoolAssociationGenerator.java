package org.slc.sli.test.generators;

import org.slc.sli.test.edfi.entities.*;

public class StudentSchoolAssociationGenerator {

    public static StudentSchoolAssociation generateLowFi(String studentId, String schoolId) {
        StudentSchoolAssociation ssa = new StudentSchoolAssociation();

        StudentIdentityType sit = new StudentIdentityType();
        sit.setStudentUniqueStateId(studentId);
        StudentReferenceType srt = new StudentReferenceType();
        srt.setStudentIdentity(sit);
        ssa.setStudentReference(srt);

        EducationalOrgIdentityType eoit = new EducationalOrgIdentityType();
        eoit.getStateOrganizationIdOrEducationOrgIdentificationCode().add(schoolId);
        EducationalOrgReferenceType eor = new EducationalOrgReferenceType();
        eor.setEducationalOrgIdentity(eoit);
        ssa.setSchoolReference(eor);

        ssa.setEntryGradeLevel(GradeLevelType.FIFTH_GRADE);

        return ssa;
    }
}
