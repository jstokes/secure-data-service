package org.slc.sli.ingestion;

/**
 * File Type enumerator.
 *
 * @author okrook
 *
 */
public enum FileType {

    XML_EDUCATION_ORGANIZATION("EducationOrganization", FileFormat.EDFI_XML),
    XML_EDUCATION_ORG_CALENDAR("EducationOrgCalendar", FileFormat.EDFI_XML),
    XML_STAFF_ASSOCIATION("StaffAssociation", FileFormat.EDFI_XML),
    //XML_STUDENT("Student", FileFormat.EDFI_XML),
    XML_STUDENT_ATTENDANCE("Attendance", FileFormat.EDFI_XML),
    XML_STUDENT_ENROLLMENT("StudentEnrollment", FileFormat.EDFI_XML),
    XML_MASTER_SCHEDULE("MasterSchedule", FileFormat.EDFI_XML),
    XML_ASSESSMENT_METADATA("AssessmentMetadata", FileFormat.EDFI_XML),
    XML_STUDENT_ASSESSMENT("StudentAssessment", FileFormat.EDFI_XML),
    XML_STUDENT_GRADES("StudentGrades", FileFormat.EDFI_XML),
    XML_STUDENT_PARENT_ASSOCIATION("StudentParent", FileFormat.EDFI_XML),
    XML_STUDENT_PROGRAM("StudentProgram", FileFormat.EDFI_XML),
    XML_STUDENT_COHORT("StudentCohort", FileFormat.EDFI_XML),
    XML_STUDENT_DISCIPLINE("StudentDiscipline", FileFormat.EDFI_XML),

    CSV_STUDENT("Student", FileFormat.CSV),
    CSV_SCHOOL("School", FileFormat.CSV),
    CSV_STATE_EDUCATION_AGENCY("StateEducationAgency", FileFormat.CSV),
    CSV_LOCAL_EDUCATION_AGENCY("LocalEducationAgency", FileFormat.CSV),
    CSV_COURSE("Course", FileFormat.CSV),
    CSV_STAFF("Staff", FileFormat.CSV),
    CSV_SECTION("Section", FileFormat.CSV),
    CSV_SESSION("Session", FileFormat.CSV),
    CSV_TEACHER("Teacher", FileFormat.CSV),
    CSV_STUDENT_SCHOOL_ASSOCIATION("StudentSchoolAssociation", FileFormat.CSV),
    CSV_STUDENT_SECTION_ASSOCIATION("StudentSectionAssociation", FileFormat.CSV),
    CSV_TEACHER_SCHOOL_ASSOCIATION("TeacherSchoolAssociation", FileFormat.CSV),
    CSV_TEACHER_SECTION_ASSOCIATION("TeacherSectionAssociation", FileFormat.CSV),
    CSV_LOCATION("Location", FileFormat.CSV),
    CSV_OBJECTIVE_ASSESSMENT("ObjectiveAssessment", FileFormat.CSV);

    private final String name;
    private final FileFormat fileFormat;

    FileType(String name, FileFormat fileFormat) {
        this.name = name;
        this.fileFormat = fileFormat;
    }

    public String getName() {
        return name;
    }

    public FileFormat getFileFormat() {
        return fileFormat;
    }

    public static FileType findByNameAndFormat(String name, FileFormat fileFormat) {
        if (name != null) {
            for (FileType ft : FileType.values()) {
                if (ft.getName().toLowerCase().equals(name.toLowerCase()) && ft.getFileFormat().equals(fileFormat)) {
                    return ft;
                }
            }
        }
        return null;
    }


}
