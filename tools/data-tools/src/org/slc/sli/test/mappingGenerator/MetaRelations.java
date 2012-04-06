package org.slc.sli.test.mappingGenerator;

import java.util.HashMap;
import java.util.Map;

import org.slc.sli.test.edfi.entities.relations.CohortMeta;
import org.slc.sli.test.edfi.entities.relations.CourseMeta;
import org.slc.sli.test.edfi.entities.relations.LeaMeta;
import org.slc.sli.test.edfi.entities.relations.SchoolMeta;
import org.slc.sli.test.edfi.entities.relations.SeaMeta;
import org.slc.sli.test.edfi.entities.relations.SectionMeta;
import org.slc.sli.test.edfi.entities.relations.SessionMeta;
import org.slc.sli.test.edfi.entities.relations.StaffMeta;
import org.slc.sli.test.edfi.entities.relations.StudentMeta;
import org.slc.sli.test.edfi.entities.relations.TeacherMeta;
import org.slc.sli.test.edfi.entities.relations.ProgramMeta;

public final class MetaRelations {

    // knobs to control number of entities to create
    private static final int TOTAL_SEAS = 1;
    private static final int LEAS_PER_SEA = 1;
    private static final int STAFF_PER_SEA = 3;
    private static final int SCHOOLS_PER_LEA = 2;
    private static final int COURSES_PER_SCHOOL = 2;
    private static final int SESSIONS_PER_SCHOOL = 1;
    private static final int SECTIONS_PER_COURSE_SESSION = 1;
    private static final int TEACHERS_PER_SCHOOL = 1;
    private static final int STUDENTS_PER_SCHOOL = 25;
    private static final int PROGRAMS_PER_SCHOOL = 2;
    private static final int FREE_STANDING_COHORT_PER_SCHOOL = 2;
    private static final int FREE_STANDING_COHORT_SIZE = 4;
    private static final int INV_PROB_SECTION_HAS_PROGRAM = 10;

    // publicly accessible structures for the "meta-skeleton" entities populated by "buildFromSea()"
    public static final Map<String, SeaMeta> SEA_MAP = new HashMap<String, SeaMeta>();
    public static final Map<String, LeaMeta> LEA_MAP = new HashMap<String, LeaMeta>();
    public static final Map<String, SchoolMeta> SCHOOL_MAP = new HashMap<String, SchoolMeta>();
    public static final Map<String, CourseMeta> COURSE_MAP = new HashMap<String, CourseMeta>();

    public static final Map<String, SessionMeta> SESSION_MAP = new HashMap<String, SessionMeta>();

    public static final Map<String, SectionMeta> SECTION_MAP = new HashMap<String, SectionMeta>();

    public static final Map<String, StaffMeta> STAFF_MAP = new HashMap<String, StaffMeta>();
    public static final Map<String, TeacherMeta> TEACHER_MAP = new HashMap<String, TeacherMeta>();

    public static final Map<String, StudentMeta> STUDENT_MAP = new HashMap<String, StudentMeta>();

    public static final Map<String, ProgramMeta> PROGRAM_MAP = new HashMap<String, ProgramMeta>();

    public static final Map<String, CohortMeta> COHORT_MAP = new HashMap<String, CohortMeta>();

    /**
     * The top level call to start the XML generation process is
     * to 'buildSeas'
     */
    public static void buildFromSea() {

        long startTime = System.currentTimeMillis();

        buildSeas();

        System.out.println("Time taken to build entity relations: " + (System.currentTimeMillis() - startTime));

    }

    /**
     * Looping over all SEAs, build LEAs for each SEA
     */
    private static void buildSeas() {

        for (int idNum = 0; idNum < TOTAL_SEAS; idNum++) {

            SeaMeta seaMeta = new SeaMeta("sea" + idNum);

            SEA_MAP.put(seaMeta.id, seaMeta);

            Map<String, StaffMeta> staffForSea = buildStaffForSea(seaMeta);
            buildLeasForSea(seaMeta, staffForSea);
        }
    }

    /**
     * Create staff relations for each sea
     */
    private static Map<String, StaffMeta> buildStaffForSea(SeaMeta seaMeta) {

        Map<String, StaffMeta> staffInSchoolMap = new HashMap<String, StaffMeta>(STAFF_PER_SEA);
        for (int idNum = 0; idNum < STAFF_PER_SEA; idNum++) {

            StaffMeta staffMeta = new StaffMeta("staff" + idNum, seaMeta);

            STAFF_MAP.put(staffMeta.id, staffMeta);
            staffInSchoolMap.put(staffMeta.id, staffMeta);
        }
        return staffInSchoolMap;
    }

    /**
     * Looping over all LEAs, build Schools for each LEA
     *
     * @param seaMeta
     */
    private static void buildLeasForSea(SeaMeta seaMeta,
            Map<String, StaffMeta> staffForSea) {

        for (int idNum = 0; idNum < LEAS_PER_SEA; idNum++) {

            LeaMeta leaMeta = new LeaMeta("lea" + idNum, seaMeta);

            LEA_MAP.put(leaMeta.id, leaMeta);

            buildSchoolsForLea(leaMeta, staffForSea);
        }
    }

    /**
     * For each School, generate:
     * - teachers
     * - courses
     * - sessions
     * - sections
     * And correlate sections with teachers.
     *
     * @param leaMeta
     */
    private static void buildSchoolsForLea(LeaMeta leaMeta, Map<String, StaffMeta> staffForSea) {

        for (int idNum = 0; idNum < SCHOOLS_PER_LEA; idNum++) {

            SchoolMeta schoolMeta = new SchoolMeta("sch" + idNum, leaMeta);

            SCHOOL_MAP.put(schoolMeta.id, schoolMeta);

            buildAndRelateEntitiesWithSchool(schoolMeta, staffForSea);
        }
    }

    private static void buildAndRelateEntitiesWithSchool(SchoolMeta schoolMeta, Map<String, StaffMeta> staffForSea) {

        Map<String, TeacherMeta> teachersForSchool = buildTeachersForSchool(schoolMeta);

        Map<String, StudentMeta> studentsForSchool = buildStudentsForSchool(schoolMeta);

        Map<String, CourseMeta> coursesForSchool = buildCoursesForSchool(schoolMeta);

        Map<String, SessionMeta> sessionsForSchool = buildSessionsForSchool(schoolMeta);

        Map<String, ProgramMeta> programForSchool = buildProgramsForSchool(schoolMeta);

        Map<String, SectionMeta> sectionsForSchool = buildSectionsForSchool(schoolMeta, coursesForSchool,
                    sessionsForSchool, programForSchool);
        
        Map<String, CohortMeta> freeStandingCohortsForSchool = buildFreeStandingCohortsForSchool(schoolMeta);

        addSectionsToTeachers(sectionsForSchool, teachersForSchool);

        addStudentsToSections(sectionsForSchool, studentsForSchool);
            
        addStudentsToPrograms(sectionsForSchool, studentsForSchool, programForSchool);

        addStaffToPrograms(programForSchool, staffForSea);

        addStaffStudentToFreeStandingCohorts(freeStandingCohortsForSchool, studentsForSchool, staffForSea);

    }

    /**
     * Generate the teachers for this school.
     * teachersInSchoolMap is used later in this class.
     * TEACHER_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @return
     */
    private static Map<String, TeacherMeta> buildTeachersForSchool(SchoolMeta schoolMeta) {

        Map<String, TeacherMeta> teachersInSchoolMap = new HashMap<String, TeacherMeta>(TEACHERS_PER_SCHOOL);
        for (int idNum = 0; idNum < TEACHERS_PER_SCHOOL; idNum++) {

            TeacherMeta teacherMeta;
            if (idNum == 0) {
                // hardcode first teacher as he is set up in ny idp
                teacherMeta = TeacherMeta.create("wadama", schoolMeta);
            } else {
                teacherMeta = TeacherMeta.createWithChainedId("teacher" + idNum, schoolMeta);
            }

            // it's useful to return the objects created JUST for this school
            // add to both maps here to avoid loop in map.putAll if we merged maps later
            teachersInSchoolMap.put(teacherMeta.id, teacherMeta);
            TEACHER_MAP.put(teacherMeta.id, teacherMeta);
        }

        return teachersInSchoolMap;
    }

    /**
     * Generate the students for this school.
     * studentsInSchoolMap is used later in this class.
     * STUDENT_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @return
     */
    private static Map<String, StudentMeta> buildStudentsForSchool(SchoolMeta schoolMeta) {

        Map<String, StudentMeta> studentsInSchoolMap = new HashMap<String, StudentMeta>(STUDENTS_PER_SCHOOL);
        for (int idNum = 0; idNum < STUDENTS_PER_SCHOOL; idNum++) {

            StudentMeta studentMeta = new StudentMeta("student" + idNum, schoolMeta);

            // it's useful to return the objects created JUST for this school
            // add to both maps here to avoid loop in map.putAll if we merged maps later
            studentsInSchoolMap.put(studentMeta.id, studentMeta);
            STUDENT_MAP.put(studentMeta.id, studentMeta);
        }

        return studentsInSchoolMap;
    }

    /**
     * Generate the courses for the school.
     * coursesForSchool is used later in this class.
     * COURSE_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @return
     */
    private static Map<String, CourseMeta> buildCoursesForSchool(SchoolMeta schoolMeta) {

        Map<String, CourseMeta> coursesForSchool = new HashMap<String, CourseMeta>(COURSES_PER_SCHOOL);

        for (int idNum = 0; idNum < COURSES_PER_SCHOOL; idNum++) {

            CourseMeta courseMeta = new CourseMeta("course" + idNum, schoolMeta);

            // it's useful to return the objects created JUST for this school
            // add to both maps here to avoid loop in map.putAll if we merged maps later
            coursesForSchool.put(courseMeta.id, courseMeta);
            COURSE_MAP.put(courseMeta.id, courseMeta);
        }

        return coursesForSchool;
    }

    /**
     * Generate the sessions for the school.
     * sessionsForSchool is used later in this class.
     * SESSION_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @return
     */
    private static Map<String, SessionMeta> buildSessionsForSchool(SchoolMeta schoolMeta) {

        Map<String, SessionMeta> sessionsForSchool = new HashMap<String, SessionMeta>(SESSIONS_PER_SCHOOL);

        for (int idNum = 0; idNum < SESSIONS_PER_SCHOOL; idNum++) {

            SessionMeta sessionMeta = new SessionMeta("session" + idNum, schoolMeta);

            // it's useful to return the objects created JUST for this school
            // add to both maps here to avoid loop in map.putAll if we merged maps later
            sessionsForSchool.put(sessionMeta.id, sessionMeta);
            SESSION_MAP.put(sessionMeta.id, sessionMeta);
        }

        return sessionsForSchool;
    }

    /**
     * Generate the sections for this school.
     * sectionMapForSchool is used later in this class.
     * SECTION_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @param coursesForSchool
     * @param sessionsForSchool
     * @param programsForSchool
     * @return
     */
    private static Map<String, SectionMeta> buildSectionsForSchool(SchoolMeta schoolMeta,
            Map<String, CourseMeta> coursesForSchool, Map<String, SessionMeta> sessionsForSchool,
            Map<String, ProgramMeta> programsForSchool) {

        Map<String, SectionMeta> sectionMapForSchool = new HashMap<String, SectionMeta>();
        
        Object[] programMetas = programsForSchool.values().toArray();
        int programCounter = 0;

        for (SessionMeta sessionMeta : sessionsForSchool.values()) {

            for (CourseMeta courseMeta : coursesForSchool.values()) {

                for (int idNum = 0; idNum < SECTIONS_PER_COURSE_SESSION; idNum++) {

                    // program reference in section is optional; will create one program reference 
                    // for every inverse-probability-section-has-program section
                    ProgramMeta programMeta = null;
                    if(sectionMapForSchool.size() % INV_PROB_SECTION_HAS_PROGRAM == 0) {
                        programMeta = (ProgramMeta) programMetas[programCounter];
                        programCounter = (programCounter + 1) % programMetas.length;
                    }

                    SectionMeta sectionMeta = new SectionMeta("section" + idNum, schoolMeta, courseMeta, sessionMeta, programMeta);

                    // it's useful to return the objects created JUST for this school
                    // add to both maps here to avoid loop in map.putAll if we merged maps later
                    sectionMapForSchool.put(sectionMeta.id, sectionMeta);
                    SECTION_MAP.put(sectionMeta.id, sectionMeta);
                }
            }
        }

        return sectionMapForSchool;
    }

    /**
     * Generate the programs for this school.
     * programMapForSchool is used later in this class.
     * PROGRAM_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @return
     */
    private static Map<String, ProgramMeta> buildProgramsForSchool(SchoolMeta schoolMeta) {

        Map<String, ProgramMeta> programMapForSchool = new HashMap<String, ProgramMeta>();

        for (int idNum = 0; idNum < PROGRAMS_PER_SCHOOL; idNum++) {

            ProgramMeta programMeta = new ProgramMeta("prg" + idNum, schoolMeta);

            // it's useful to return the objects created JUST for this school
            // add to both maps here to avoid loop in map.putAll if we merged maps later
            programMapForSchool.put(programMeta.id, programMeta);
            PROGRAM_MAP.put(programMeta.id, programMeta);
            
            buildCohortsForProgram(programMeta, schoolMeta);
        }

        return programMapForSchool;
    }

    /**
     * Generate a cohort for this program.
     * COHORT_MAP is used to actually generate the XML.
     *
     * @param schoolMeta
     * @param programMeta
     * @return
     */
    private static void buildCohortsForProgram(ProgramMeta programMeta, SchoolMeta schoolMeta) {
        CohortMeta cohortMeta = new CohortMeta("coh", programMeta);
        COHORT_MAP.put(cohortMeta.id, cohortMeta);
        programMeta.cohortIds.add(cohortMeta.id);
        return;
    }

    /**
     * Generate free-standing (non-program-affiliated cohorts for school.
     * 
     * freeStandingCohortsForSchool is used later in this class
     * COHORT_MAP is used to actually generate the XML.
     * 
     * @param schoolMeta
     */
    private static Map<String, CohortMeta> buildFreeStandingCohortsForSchool(SchoolMeta schoolMeta) {

        Map<String, CohortMeta> freeStandingCohortsForSchool = new HashMap<String, CohortMeta>(FREE_STANDING_COHORT_PER_SCHOOL);
        for(int idNum = 0; idNum < FREE_STANDING_COHORT_PER_SCHOOL; idNum++) {
            CohortMeta cohortMeta = new CohortMeta("coh" + idNum, schoolMeta);
            freeStandingCohortsForSchool.put(cohortMeta.id, cohortMeta);
            COHORT_MAP.put(cohortMeta.id, cohortMeta);
        }
        return freeStandingCohortsForSchool;
        
    }
    
    /**
     * Correlates teachers and sections on a 'per school' basis.
     *
     * @param sectionsForSchool
     * @param teachersForSchool
     */
    private static void addSectionsToTeachers(Map<String, SectionMeta> sectionsForSchool,
            Map<String, TeacherMeta> teachersForSchool) {

        Object[] teacherMetas = teachersForSchool.values().toArray();
        int teacherCounter = 0;

        // each section needs to be referenced by a TeacherMeta
        for (SectionMeta sectionMeta : sectionsForSchool.values()) {

            // loop through the teachers we have in this school and assign them to sections
            if (teacherCounter >= teacherMetas.length) {
                teacherCounter = 0;
            }
            ((TeacherMeta) teacherMetas[teacherCounter]).sectionIds.add(sectionMeta.id);
            teacherCounter++;
        }
    }

    /**
     * Correlates students and sections on a 'per school' basis.
     *
     * @param sectionsForSchool
     * @param studentsForSchool
     */
    private static void addStudentsToSections(Map<String, SectionMeta> sectionsForSchool,
            Map<String, StudentMeta> studentsForSchool) {

        Object[] sectionMetas = sectionsForSchool.values().toArray();
        int sectionCounter = 0;

        // each section needs to be referenced by a TeacherMeta
        for (StudentMeta studentMeta : studentsForSchool.values()) {

            // loop through the sections we have in this school and assign students to them
            if (sectionCounter >= sectionMetas.length) {
                sectionCounter = 0;
            }
            studentMeta.sectionIds.add(((SectionMeta) sectionMetas[sectionCounter]).id);
            sectionCounter++;
        }
    }

    /**
     * Correlates students and program on a 'per school' basis.
     * Student S is correlated with a program P iff there exists a section X s.t. S is  
     * correlated with X and X is correlated with P. 
     *
     * @param sectionsForSchool
     * @param studentsForSchool
     */
    private static void addStudentsToPrograms(Map<String, SectionMeta> sectionsForSchool,
            Map<String, StudentMeta> studentsForSchool,
            Map<String, ProgramMeta> programsForSchool) {

        for (StudentMeta studentMeta : studentsForSchool.values()) {
            for (String sectionId : studentMeta.sectionIds) {
                SectionMeta sectionMeta = sectionsForSchool.get(sectionId);
                if(sectionMeta != null && sectionMeta.programId != null && programsForSchool.containsKey(sectionMeta.programId)) {
                    ProgramMeta programMeta = programsForSchool.get(sectionMeta.programId);
                    programMeta.studentIds.add(studentMeta.id);
                }
            }
        }

        // for each cohort in the program, add all the students in it to the cohort too. 
        for (ProgramMeta programMeta : programsForSchool.values()) {
            for(String cohortId : programMeta.cohortIds) {
                CohortMeta cohortMeta = COHORT_MAP.get(cohortId);
                if(cohortMeta != null) {
                    cohortMeta.studentIds.addAll(programMeta.studentIds);
                }
            }
        }
    }

    /**
     * Correlates staff and program on a 'per school' basis.
     *
     * @param programsForSchool
     */
    private static void addStaffToPrograms(Map<String, ProgramMeta> programsForSchool, 
            Map<String, StaffMeta> staffForSea) {
        Object[] staffMetas = staffForSea.values().toArray();
        int staffCounter = 0;

        // each program needs to be referenced by a StaffMeta
        for (ProgramMeta programMeta : programsForSchool.values()) {

            // loop through the sections we have in this school and assign students to them
            if (staffCounter >= staffMetas.length) {
                staffCounter = 0;
            }
            String staffId = ((StaffMeta) staffMetas[staffCounter]).id;
            programMeta.staffIds.add(staffId);
            staffCounter++;
        }

        // for each cohort in the program, add all the staff in it to the cohort too. 
        for (ProgramMeta programMeta : programsForSchool.values()) {
            for(String cohortId : programMeta.cohortIds) {
                CohortMeta cohortMeta = COHORT_MAP.get(cohortId);
                if(cohortMeta != null) {
                    cohortMeta.staffIds.addAll(programMeta.staffIds);
                }
            }
        }
    }

    /**
     * Assign staff and student to the school's free-standing cohorts
     * 
     * @param freeStandingCohortsForSchool
     * @param studentsForSchool
     * @param staffForSea
     */
    private static void addStaffStudentToFreeStandingCohorts(Map<String, CohortMeta> freeStandingCohortsForSchool,
                                                             Map<String, StudentMeta> studentsForSchool,
                                                             Map<String, StaffMeta> staffForSea) {
        Object[] studentIds = studentsForSchool.keySet().toArray();
        Object[] staffIds = staffForSea.keySet().toArray();
        int studentIdsIndx = 0; 
        int staffIdsIndx = 0;
        
        for (CohortMeta cohortMeta : freeStandingCohortsForSchool.values()) {
            for(int idNum = 0; idNum < FREE_STANDING_COHORT_SIZE; idNum++) {
                cohortMeta.studentIds.add((String) studentIds[studentIdsIndx]);
                studentIdsIndx = (studentIdsIndx + 1) % studentIds.length;
            }
            cohortMeta.staffIds.add((String) staffIds[staffIdsIndx]);
            staffIdsIndx = (staffIdsIndx + 1) % staffIds.length;
        }
    }
}
