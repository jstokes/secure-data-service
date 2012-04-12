package org.slc.sli.test.generators.interchange;

import java.util.Collection;
import java.util.List;

import org.slc.sli.test.edfi.entities.InterchangeStaffAssociation;
import org.slc.sli.test.edfi.entities.Staff;
import org.slc.sli.test.edfi.entities.StaffEducationOrgAssignmentAssociation;
import org.slc.sli.test.edfi.entities.StaffEducationOrgEmploymentAssociation;
import org.slc.sli.test.edfi.entities.Teacher;
import org.slc.sli.test.edfi.entities.TeacherSchoolAssociation;
import org.slc.sli.test.edfi.entities.TeacherSectionAssociation;
import org.slc.sli.test.edfi.entities.meta.StaffMeta;
import org.slc.sli.test.edfi.entities.meta.TeacherMeta;
import org.slc.sli.test.generators.StaffEdOrgAssignmentAssociationGenerator;
import org.slc.sli.test.generators.StaffEdOrgEmploymentAssociationGenerator;
import org.slc.sli.test.generators.StaffGenerator;
import org.slc.sli.test.generators.TeacherGenerator;
import org.slc.sli.test.generators.TeacherSchoolAssociationGenerator;
import org.slc.sli.test.generators.TeacherSectionAssociationGenerator;
import org.slc.sli.test.mappingGenerator.MetaRelations;
import org.slc.sli.test.mappingGenerator.StateEdFiXmlGenerator;

/**
 * Generates the Staff Association Interchange as derived from the associations
 * determined during the call to MetaRelations.buildFromSea() in StateEdFiXmlGenerator.
 *
 * @author dduran
 *
 */
public class InterchangeStaffAssociationGenerator {

    /**
     * Sets up a new Staff Association Interchange and populates it.
     *
     * @return
     */
    public static InterchangeStaffAssociation generate() {

        InterchangeStaffAssociation interchange = new InterchangeStaffAssociation();
        List<Object> interchangeObjects = interchange
                .getStaffOrStaffEducationOrgEmploymentAssociationOrStaffEducationOrgAssignmentAssociation();

        addEntitiesToInterchange(interchangeObjects);

        return interchange;
    }

    /**
     * Generate the individual Teacher Association entities.
     *
     * @param interchangeObjects
     */
    private static void addEntitiesToInterchange(List<Object> interchangeObjects) {

        generateTeachers(interchangeObjects, MetaRelations.TEACHER_MAP.values());

        generateTeacherSchoolAssoc(interchangeObjects, MetaRelations.TEACHER_MAP.values());

        generateTeacherSectionAssoc(interchangeObjects, MetaRelations.TEACHER_MAP.values());

        generateStaff(interchangeObjects, MetaRelations.STAFF_MAP.values());

        generateStaffEdOrgEmploymentAssoc(interchangeObjects, MetaRelations.STAFF_MAP.values());

        generateStaffEdOrgAssignmentAssoc(interchangeObjects, MetaRelations.STAFF_MAP.values());

    }

    /**
     * Loops all teachers and, using a Teacher Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param teacherMetas
     */
    private static void generateTeachers(List<Object> interchangeObjects, Collection<TeacherMeta> teacherMetas) {
        long startTime = System.currentTimeMillis();

        for (TeacherMeta teacherMeta : teacherMetas) {

            Teacher teacher;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                teacher = null;
            } else {
                teacher = TeacherGenerator.generateLowFi(teacherMeta.id);
            }

            interchangeObjects.add(teacher);
        }

        System.out.println("generated " + teacherMetas.size() + " Teacher objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    private static void generateTeacherSchoolAssoc(List<Object> interchangeObjects, Collection<TeacherMeta> teacherMetas) {
        long startTime = System.currentTimeMillis();

        int objGenCounter = 0;
        for (TeacherMeta teacherMeta : teacherMetas) {
            for (String schoolId : teacherMeta.schoolIds) {

                TeacherSchoolAssociation teacherSchool;

                if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                    teacherSchool = null;
                } else {
                    teacherSchool = TeacherSchoolAssociationGenerator.generateLowFi(teacherMeta, schoolId);
                }

                interchangeObjects.add(teacherSchool);

                objGenCounter++;
            }
        }

        System.out.println("generated " + objGenCounter + " TeacherSchoolAssociation objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    private static void generateTeacherSectionAssoc(List<Object> interchangeObjects,
            Collection<TeacherMeta> teacherMetas) {
        long startTime = System.currentTimeMillis();

        int objGenCounter = 0;
        for (TeacherMeta teacherMeta : teacherMetas) {
            for (String sectionId : teacherMeta.sectionIds) {

                TeacherSectionAssociation teacherSection;

                if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                    teacherSection = null;
                } else {
                    teacherSection = TeacherSectionAssociationGenerator.generateLowFi(teacherMeta, sectionId);
                }

                interchangeObjects.add(teacherSection);

                objGenCounter++;
            }
        }

        System.out.println("generated " + objGenCounter + " TeacherSectionAssociation objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    /**
     * Loops all staff and, using a Staff Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param teacherMetas
     */
    private static void generateStaff(List<Object> interchangeObjects, Collection<StaffMeta> staffMetas) {
        long startTime = System.currentTimeMillis();

        for (StaffMeta staffMeta : staffMetas) {

            Staff staff;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                staff = null;
            } else {
                staff = StaffGenerator.generateLowFi(staffMeta.id);
            }

            interchangeObjects.add(staff);
        }

        System.out.println("generated " + staffMetas.size() + " Staff objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    private static void generateStaffEdOrgEmploymentAssoc(List<Object> interchangeObjects,
            Collection<StaffMeta> staffMetas) {
        long startTime = System.currentTimeMillis();

        for (StaffMeta staffMeta : staffMetas) {

            StaffEducationOrgEmploymentAssociation staffEdOrgEmploymentAssoc;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                staffEdOrgEmploymentAssoc = null;
            } else {
                staffEdOrgEmploymentAssoc = StaffEdOrgEmploymentAssociationGenerator.generateLowFi(staffMeta);
            }

            interchangeObjects.add(staffEdOrgEmploymentAssoc);
        }

        System.out.println("generated " + staffMetas.size() + " StaffEducationOrgEmploymentAssociation objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    private static void generateStaffEdOrgAssignmentAssoc(List<Object> interchangeObjects,
            Collection<StaffMeta> staffMetas) {
        long startTime = System.currentTimeMillis();

        for (StaffMeta staffMeta : staffMetas) {

            StaffEducationOrgAssignmentAssociation staffEdOrgAssignmentAssoc;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                staffEdOrgAssignmentAssoc = null;
            } else {
                staffEdOrgAssignmentAssoc = StaffEdOrgAssignmentAssociationGenerator.generateLowFi(staffMeta);
            }

            interchangeObjects.add(staffEdOrgAssignmentAssoc);
        }

        System.out.println("generated " + staffMetas.size() + " StaffEducationOrgAssignmentAssociation objects in: "
                + (System.currentTimeMillis() - startTime));
    }
}
