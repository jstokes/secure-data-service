package org.slc.sli.test.generators.interchange;

import java.util.Collection;
import java.util.List;

import org.slc.sli.test.edfi.entities.Program;
import org.slc.sli.test.edfi.entities.Course;
import org.slc.sli.test.edfi.entities.InterchangeEducationOrganization;
import org.slc.sli.test.edfi.entities.LocalEducationAgency;
import org.slc.sli.test.edfi.entities.School;
import org.slc.sli.test.edfi.entities.StateEducationAgency;
import org.slc.sli.test.edfi.entities.meta.CourseMeta;
import org.slc.sli.test.edfi.entities.meta.LeaMeta;
import org.slc.sli.test.edfi.entities.meta.ProgramMeta;
import org.slc.sli.test.edfi.entities.meta.SchoolMeta;
import org.slc.sli.test.edfi.entities.meta.SeaMeta;
import org.slc.sli.test.generators.ProgramGenerator;
import org.slc.sli.test.generators.CourseGenerator;
import org.slc.sli.test.generators.LocalEducationAgencyGenerator;
import org.slc.sli.test.generators.SchoolGenerator;
import org.slc.sli.test.generators.StateEducationAgencyGenerator;
import org.slc.sli.test.mappingGenerator.MetaRelations;
import org.slc.sli.test.mappingGenerator.StateEdFiXmlGenerator;

/**
 * Generates all Education Organizations contained in the variables:
 * - seaMap
 * - leaMap
 * - schoolMap
 * - courseMap
 * as created by the call to MetaRelations.buildFromSea() in StateEdFiXmlGenerator
 *
 * @author dduran
 *
 */
public class InterchangeEdOrgGenerator {

    /**
     * Sets up a new Education Organization Interchange and populates it
     *
     * @return
     */
    public static InterchangeEducationOrganization generate() {

        InterchangeEducationOrganization interchange = new InterchangeEducationOrganization();
        List<Object> interchangeObjects = interchange
                .getStateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation();

        addEntitiesToInterchange(interchangeObjects);

        return interchange;
    }

    /**
     * Generates the individual entities that can be Educational Organizations
     *
     * @param interchangeObjects
     */
    private static void addEntitiesToInterchange(List<Object> interchangeObjects) {

        generateStateEducationAgencies(interchangeObjects, MetaRelations.SEA_MAP.values());

        generateLocalEducationAgencies(interchangeObjects, MetaRelations.LEA_MAP.values());

        generateSchools(interchangeObjects, MetaRelations.SCHOOL_MAP.values());

        generateCourses(interchangeObjects, MetaRelations.COURSE_MAP.values());

        generatePrograms(interchangeObjects, MetaRelations.PROGRAM_MAP.values());

    }

    /**
     * Loops all SEAs and, using an SEA Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param seaMetas
     */
    private static void generateStateEducationAgencies(List<Object> interchangeObjects, Collection<SeaMeta> seaMetas) {
        long startTime = System.currentTimeMillis();

        for (SeaMeta seaMeta : seaMetas) {

            StateEducationAgency sea;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                sea = null;
            } else {
                sea = StateEducationAgencyGenerator.generateLowFi(seaMeta.id);
            }

            interchangeObjects.add(sea);
        }

        System.out.println("generated " + seaMetas.size() + " StateEducationAgency objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    /**
     * Loops all LEAs and, using an LEA Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param leaMetas
     */
    private static void generateLocalEducationAgencies(List<Object> interchangeObjects, Collection<LeaMeta> leaMetas) {
        long startTime = System.currentTimeMillis();

        for (LeaMeta leaMeta : leaMetas) {

            LocalEducationAgency lea;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                lea = null;
            } else {
                lea = LocalEducationAgencyGenerator.generateLowFi(leaMeta.id, leaMeta.seaId);
            }

            interchangeObjects.add(lea);
        }

        System.out.println("generated " + leaMetas.size() + " LocalEducationAgency objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    /**
     * Loops all schools and, using a School Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param schoolMetas
     */
    private static void generateSchools(List<Object> interchangeObjects, Collection<SchoolMeta> schoolMetas) {
        long startTime = System.currentTimeMillis();

        for (SchoolMeta schoolMeta : schoolMetas) {

            School school;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                school = null;
            } else {
                school = SchoolGenerator.generateLowFi(schoolMeta.id, schoolMeta.leaId);
            }

            interchangeObjects.add(school);
        }

        System.out.println("generated " + schoolMetas.size() + " School objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    /**
     * Loops all courses and, using a Course Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param courseMetas
     */
    private static void generateCourses(List<Object> interchangeObjects, Collection<CourseMeta> courseMetas) {
        long startTime = System.currentTimeMillis();

        for (CourseMeta courseMeta : courseMetas) {

            Course course;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                course = null;
            } else {
                course = CourseGenerator.generateLowFi(courseMeta.id, courseMeta.schoolId);
            }

            interchangeObjects.add(course);
        }

        System.out.println("generated " + courseMetas.size() + " Course objects in: "
                + (System.currentTimeMillis() - startTime));
    }

    /**
     * Loops all programs and, using a Program Generator, populates interchange data.
     *
     * @param interchangeObjects
     * @param programMetas
     */
    private static void generatePrograms(List<Object> interchangeObjects, Collection<ProgramMeta> programMetas) {
        for (ProgramMeta programMeta : programMetas) {

            Program program;

            if ("medium".equals(StateEdFiXmlGenerator.fidelityOfData)) {
                program = null;
            } else {
                program = ProgramGenerator.generateLowFi(programMeta.id);
            }

            interchangeObjects.add(program);
        }
    }
}
