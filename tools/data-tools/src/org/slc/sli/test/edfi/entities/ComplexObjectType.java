//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

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
    AttendanceEvent.class,
    CourseTranscript.class,
    ReportCard.class,
    Grade.class,
    Section.class,
    CourseOffering.class,
    Course.class,
    Diploma.class,
    StudentAssessment.class,
    AssessmentFamily.class,
    Assessment.class,
    AssessmentPeriodDescriptor.class,
    PerformanceLevelDescriptor.class,
    ObjectiveAssessment.class,
    AssessmentItem.class,
    LearningObjective.class,
    LearningStandard.class,
    RestraintEvent.class,
    ServiceDescriptor.class,
    StudentObjectiveAssessment.class,
    StudentAssessmentItem.class,
    Session.class,
    GradingPeriod.class,
    CalendarDate.class,
    AcademicWeek.class,
    Location.class,
    ClassPeriod.class,
    CompetencyLevelDescriptor.class,
    Program.class,
    GraduationPlan.class,
    Cohort.class,
    BellSchedule.class,
    MeetingTime.class,
    Parent.class,
    StudentAcademicRecord.class,
    StudentCompetency.class,
    GradebookEntry.class,
    StudentGradebookEntry.class,
    StudentCompetencyObjective.class,
    Staff.class,
    LeaveEvent.class,
    OpenStaffPosition.class,
    CredentialFieldDescriptor.class,
    DisciplineIncident.class,
    DisciplineAction.class,
    BehaviorDescriptor.class,
    DisciplineDescriptor.class,
    AccountCodeDescriptor.class,
    Budget.class,
    Payroll.class,
    PostSecondaryEvent.class,
    ContractedStaff.class,
    Actual.class,
    Account.class,
    EducationOrganization.class
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
