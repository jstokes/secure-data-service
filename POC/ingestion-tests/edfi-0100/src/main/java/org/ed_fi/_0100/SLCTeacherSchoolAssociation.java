//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * TeacherSchoolAssociation record with key fields: TeacherReference (StaffUniqueStateId), SchoolReference (StateOrganizationId) and ProgramAssignment. Limited SchoolReference to a single instance. Changed types of TeacherReference and SchoolReference to SLC reference types.
 * 
 * <p>Java class for SLC-TeacherSchoolAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-TeacherSchoolAssociation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="TeacherReference" type="{http://ed-fi.org/0100}SLC-StaffReferenceType"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *         &lt;element name="ProgramAssignment" type="{http://ed-fi.org/0100}ProgramAssignmentType"/>
 *         &lt;element name="InstructionalGradeLevels" type="{http://ed-fi.org/0100}GradeLevelsType"/>
 *         &lt;element name="AcademicSubjects" type="{http://ed-fi.org/0100}AcademicSubjectsType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-TeacherSchoolAssociation", propOrder = {
    "teacherReference",
    "schoolReference",
    "programAssignment",
    "instructionalGradeLevels",
    "academicSubjects"
})
public class SLCTeacherSchoolAssociation
    extends ComplexObjectType
{

    @XmlElement(name = "TeacherReference", required = true)
    protected SLCStaffReferenceType teacherReference;
    @XmlElement(name = "SchoolReference", required = true)
    protected SLCEducationalOrgReferenceType schoolReference;
    @XmlElement(name = "ProgramAssignment", required = true)
    protected ProgramAssignmentType programAssignment;
    @XmlElement(name = "InstructionalGradeLevels", required = true)
    protected GradeLevelsType instructionalGradeLevels;
    @XmlElement(name = "AcademicSubjects", required = true)
    protected AcademicSubjectsType academicSubjects;

    /**
     * Gets the value of the teacherReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStaffReferenceType }
     *     
     */
    public SLCStaffReferenceType getTeacherReference() {
        return teacherReference;
    }

    /**
     * Sets the value of the teacherReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStaffReferenceType }
     *     
     */
    public void setTeacherReference(SLCStaffReferenceType value) {
        this.teacherReference = value;
    }

    /**
     * Gets the value of the schoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getSchoolReference() {
        return schoolReference;
    }

    /**
     * Sets the value of the schoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setSchoolReference(SLCEducationalOrgReferenceType value) {
        this.schoolReference = value;
    }

    /**
     * Gets the value of the programAssignment property.
     * 
     * @return
     *     possible object is
     *     {@link ProgramAssignmentType }
     *     
     */
    public ProgramAssignmentType getProgramAssignment() {
        return programAssignment;
    }

    /**
     * Sets the value of the programAssignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProgramAssignmentType }
     *     
     */
    public void setProgramAssignment(ProgramAssignmentType value) {
        this.programAssignment = value;
    }

    /**
     * Gets the value of the instructionalGradeLevels property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelsType }
     *     
     */
    public GradeLevelsType getInstructionalGradeLevels() {
        return instructionalGradeLevels;
    }

    /**
     * Sets the value of the instructionalGradeLevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelsType }
     *     
     */
    public void setInstructionalGradeLevels(GradeLevelsType value) {
        this.instructionalGradeLevels = value;
    }

    /**
     * Gets the value of the academicSubjects property.
     * 
     * @return
     *     possible object is
     *     {@link AcademicSubjectsType }
     *     
     */
    public AcademicSubjectsType getAcademicSubjects() {
        return academicSubjects;
    }

    /**
     * Sets the value of the academicSubjects property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcademicSubjectsType }
     *     
     */
    public void setAcademicSubjects(AcademicSubjectsType value) {
        this.academicSubjects = value;
    }

}
