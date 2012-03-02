package org.slc.sli.api.resources.v1;

/**
 * Constants used in URI requests.
 *
 *
 * @author kmyers
 *
 */
public class ParameterConstants {

    /**
     * An indication not to start from the first result.
     */
    public static final String OFFSET = "offset";

    /**
     * Maximum number of results to display at one time.
     */
    public static final String LIMIT = "limit";

    /**
     * An indication not to start from the first result.
     */
    public static final String DEFAULT_OFFSET = "0";

    /**
     * Maximum number of results to display at one time.
     */
    public static final String DEFAULT_LIMIT = "50";

    /**
     * Number of links to traverse when presenting a high-level document.
     */
    public static final String EXPAND_DEPTH = "expandDepth";

    /**
     * Query parameter for fields to include.
     */
    public static final String INCLUDE_FIELDS = "includeFields";

    /**
     * Query parameter for fields to exclude.
     */
    public static final String EXCLUDE_FIELDS = "excludeFields";

    /**
     * Entity/Association IDs
     */
    public static final String ASSESSMENT_ID = "assessmentId";
    public static final String BELL_SCHEDULE_ID = "bellScheduleId";
    public static final String COURSE_ID = "courseId";
    public static final String DISCIPLINE_INCIDENT_ID = "disciplineIncidentId";
    public static final String PARENT_ID = "parentId";
    public static final String PROGRAM_ID = "programId";
    public static final String SCHOOL_ID = "schoolId";
    public static final String SECTION_ID = "sectionId";
    public static final String SESSION_ID = "sessionId";
    public static final String STAFF_ID = "staffId";
    public static final String STUDENT_ID = "studentId";
    public static final String TEACHER_ID = "teacherId";
    public static final String COHORT_ID = "cohortId";
    public static final String EDUCATION_ORGANIZATION_ID = "educationOrganizationId";
    public static final String ATTENDANCE_ID = "attendanceId";
    public static final String SCHOOL_SESSION_ASSOCIATION_ID = "schoolSessionAssociationId";
    public static final String TEACHER_SCHOOL_ASSOCIATION_ID = "teacherSchoolAssociationId";
    public static final String TEACHER_SCHOOL_ASSOC_ID = "teacherSchoolAssociationId";
    public static final String TEACHER_SECTION_ASSOCIATION_ID = "teacherSectionAssociationId";
    public static final String STAFF_EDUCATION_ORGANIZATION_ID = "staffEducationOrganizationId";
    public static final String STUDENT_SECTION_ASSOCIATION_ID = "studentSectionAssociationId";
    public static final String STUDENT_SCHOOL_ASSOCIATION_ID = "studentSchoolAssociationId";
    public static final String SESSION_COURSE_ASSOCIATION_ID = "sessionCourseAssociationId";
    public static final String STUDENT_ASSESSMENT_ASSOCIATION_ID = "studentAssessmentAssociationId";
    public static final String SECTION_ASSESSMENT_ASSOCIATION_ID = "sectionAssessmentAssociationId";

}
