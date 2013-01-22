//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This is the base type from which all entity elements are extended.
 * 
 * <p>Java class for ComplexObjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ComplexObjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ComplexObjectType")
@XmlSeeAlso({
    Student.class,
    SLCAttendanceEvent.class,
    AssessmentFamily.class,
    SLCAssessment.class,
    AssessmentPeriodDescriptor.class,
    SLCObjectiveAssessment.class,
    SLCAssessmentItem.class,
    SLCLearningObjective.class,
    LearningStandard.class,
    SLCStudentSchoolAssociation.class,
    SLCCourseTranscript.class,
    SLCReportCard.class,
    SLCGrade.class,
    SLCSection.class,
    SLCCourseOffering.class,
    SLCCourse.class,
    Diploma.class,
    StudentAssessment.class,
    SLCStudentProgramAssociation.class,
    SLCStudentAssessment.class,
    SLCStudentObjectiveAssessment.class,
    SLCStudentAssessmentItem.class,
    SLCSession.class,
    SLCGradingPeriod.class,
    SLCCalendarDate.class,
    CompetencyLevelDescriptor.class,
    SLCGraduationPlan.class,
    SLCCohort.class,
    SLCStudentCohortAssociation.class,
    SLCStaffCohortAssociation.class,
    Parent.class,
    SLCStudentAcademicRecord.class,
    SLCStudentCompetency.class,
    SLCGradebookEntry.class,
    SLCStudentGradebookEntry.class,
    SLCStudentCompetencyObjective.class,
    Staff.class,
    SLCTeacherSchoolAssociation.class,
    SLCDisciplineIncident.class,
    SLCStudentDisciplineIncidentAssociation.class,
    SLCDisciplineAction.class,
    MeetingTime.class,
    AcademicWeek.class,
    ReportCard.class,
    StudentObjectiveAssessment.class,
    ClassPeriod.class,
    StudentCompetency.class,
    Cohort.class,
    BellSchedule.class,
    GradingPeriod.class,
    LeaveEvent.class,
    Budget.class,
    Payroll.class,
    CredentialFieldDescriptor.class,
    Course.class,
    AssessmentItem.class,
    StudentAssessmentItem.class,
    Program.class,
    Actual.class,
    CalendarDate.class,
    SLCBellSchedule.class,
    RestraintEvent.class,
    Grade.class,
    Location.class,
    StudentGradebookEntry.class,
    SLCEducationOrganization.class,
    AccountCodeDescriptor.class,
    StudentCompetencyObjective.class,
    Session.class,
    SLCAcademicWeek.class,
    DisciplineDescriptor.class,
    CourseOffering.class,
    ObjectiveAssessment.class,
    ServiceDescriptor.class,
    DisciplineIncident.class,
    GraduationPlan.class,
    OpenStaffPosition.class,
    PostSecondaryEvent.class,
    ContractedStaff.class,
    GradebookEntry.class,
    Assessment.class,
    PerformanceLevelDescriptor.class,
    CourseTranscript.class,
    EducationOrganization.class,
    LearningObjective.class,
    AttendanceEvent.class,
    BehaviorDescriptor.class,
    Account.class,
    StudentAcademicRecord.class,
    DisciplineAction.class,
    Section.class
})
public abstract class ComplexObjectType {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
