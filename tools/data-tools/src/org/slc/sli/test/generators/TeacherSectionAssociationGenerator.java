package org.slc.sli.test.generators;

import java.util.Random;

import org.slc.sli.test.edfi.entities.ClassroomPositionType;
import org.slc.sli.test.edfi.entities.SectionIdentityType;
import org.slc.sli.test.edfi.entities.SectionReferenceType;
import org.slc.sli.test.edfi.entities.StaffIdentityType;
import org.slc.sli.test.edfi.entities.StaffReferenceType;
import org.slc.sli.test.edfi.entities.TeacherSectionAssociation;
import org.slc.sli.test.edfi.entities.meta.TeacherMeta;

public class TeacherSectionAssociationGenerator {
    private Random r = new Random();

    public TeacherSectionAssociation generate(String teacher, String school, String sectionCode) {

        TeacherSectionAssociation tsa = new TeacherSectionAssociation();

        tsa.setTeacherReference(TeacherGenerator.getTeacherReference(teacher));

        SectionIdentityType secit = new SectionIdentityType();
        secit.getStateOrganizationIdOrEducationOrgIdentificationCode().add(school);
        secit.setUniqueSectionCode(sectionCode);
        SectionReferenceType secrt = new SectionReferenceType();
        secrt.setSectionIdentity(secit);
        tsa.setSectionReference(secrt);

        tsa.setClassroomPosition(ClassroomPositionType.TEACHER_OF_RECORD);

        tsa.setHighlyQualifiedTeacher(r.nextBoolean());

        return tsa;
    }

    public static TeacherSectionAssociation generateLowFi(TeacherMeta teacherMeta, String sectionId) {

        TeacherSectionAssociation teacherSection = new TeacherSectionAssociation();

        // construct and add the teacher reference
        StaffIdentityType staffIdentity = new StaffIdentityType();
        staffIdentity.setStaffUniqueStateId(teacherMeta.id);

        StaffReferenceType teacherRef = new StaffReferenceType();
        teacherRef.setStaffIdentity(staffIdentity);

        teacherSection.setTeacherReference(teacherRef);

        teacherSection.setClassroomPosition(ClassroomPositionType.TEACHER_OF_RECORD);

        // construct and add the section references
        SectionIdentityType sectionIdentity = new SectionIdentityType();
        sectionIdentity.setUniqueSectionCode(sectionId);
        sectionIdentity.getStateOrganizationIdOrEducationOrgIdentificationCode().add(teacherMeta.schoolIds.get(0));

//        EducationOrgIdentificationCode edOrgIdCode = new EducationOrgIdentificationCode();
//        edOrgIdCode.setID(sectionId);
//        edOrgIdCode.setIdentificationSystem(EducationOrgIdentificationSystemType.SCHOOL);
//        sectionIdentity.getStateOrganizationIdOrEducationOrgIdentificationCode().add(edOrgIdCode);

        SectionReferenceType sectionRef = new SectionReferenceType();
        sectionRef.setSectionIdentity(sectionIdentity);

        teacherSection.setSectionReference(sectionRef);
        teacherSection.setClassroomPosition(ClassroomPositionType.TEACHER_OF_RECORD);

        return teacherSection;
    }
}
