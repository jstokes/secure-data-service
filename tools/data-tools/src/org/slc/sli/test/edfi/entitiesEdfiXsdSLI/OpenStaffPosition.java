//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents an open staff position that is seeking to be filled by an education organization.
 * 
 * <p>Java class for OpenStaffPosition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OpenStaffPosition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="EmploymentStatus" type="{http://ed-fi.org/0100}EmploymentStatusType"/>
 *         &lt;element name="StaffClassification" type="{http://ed-fi.org/0100}StaffClassificationType"/>
 *         &lt;element name="PositionTitle" type="{http://ed-fi.org/0100}PositionTitle" minOccurs="0"/>
 *         &lt;element name="RequisitionNumber" type="{http://ed-fi.org/0100}RequisitionNumber"/>
 *         &lt;element name="ProgramAssignment" type="{http://ed-fi.org/0100}ProgramAssignmentType" minOccurs="0"/>
 *         &lt;element name="InstructionalGradeLevels" type="{http://ed-fi.org/0100}GradeLevelsType" minOccurs="0"/>
 *         &lt;element name="AcademicSubjects" type="{http://ed-fi.org/0100}AcademicSubjectsType" minOccurs="0"/>
 *         &lt;element name="DatePosted" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DatePostingRemoved" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="PostingResult" type="{http://ed-fi.org/0100}PostingResultType" minOccurs="0"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpenStaffPosition", propOrder = {
    "employmentStatus",
    "staffClassification",
    "positionTitle",
    "requisitionNumber",
    "programAssignment",
    "instructionalGradeLevels",
    "academicSubjects",
    "datePosted",
    "datePostingRemoved",
    "postingResult",
    "educationOrganizationReference"
})
public class OpenStaffPosition
    extends ComplexObjectType
{

    @XmlElement(name = "EmploymentStatus", required = true)
    protected EmploymentStatusType employmentStatus;
    @XmlElement(name = "StaffClassification", required = true)
    protected StaffClassificationType staffClassification;
    @XmlElement(name = "PositionTitle")
    protected String positionTitle;
    @XmlElement(name = "RequisitionNumber", required = true)
    protected String requisitionNumber;
    @XmlElement(name = "ProgramAssignment")
    protected ProgramAssignmentType programAssignment;
    @XmlElement(name = "InstructionalGradeLevels")
    protected GradeLevelsType instructionalGradeLevels;
    @XmlElement(name = "AcademicSubjects")
    protected AcademicSubjectsType academicSubjects;
    @XmlElement(name = "DatePosted", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String datePosted;
    @XmlElement(name = "DatePostingRemoved")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String datePostingRemoved;
    @XmlElement(name = "PostingResult")
    protected PostingResultType postingResult;
    @XmlElement(name = "EducationOrganizationReference", required = true)
    protected EducationalOrgReferenceType educationOrganizationReference;

    /**
     * Gets the value of the employmentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link EmploymentStatusType }
     *     
     */
    public EmploymentStatusType getEmploymentStatus() {
        return employmentStatus;
    }

    /**
     * Sets the value of the employmentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmploymentStatusType }
     *     
     */
    public void setEmploymentStatus(EmploymentStatusType value) {
        this.employmentStatus = value;
    }

    /**
     * Gets the value of the staffClassification property.
     * 
     * @return
     *     possible object is
     *     {@link StaffClassificationType }
     *     
     */
    public StaffClassificationType getStaffClassification() {
        return staffClassification;
    }

    /**
     * Sets the value of the staffClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaffClassificationType }
     *     
     */
    public void setStaffClassification(StaffClassificationType value) {
        this.staffClassification = value;
    }

    /**
     * Gets the value of the positionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPositionTitle() {
        return positionTitle;
    }

    /**
     * Sets the value of the positionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPositionTitle(String value) {
        this.positionTitle = value;
    }

    /**
     * Gets the value of the requisitionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    /**
     * Sets the value of the requisitionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequisitionNumber(String value) {
        this.requisitionNumber = value;
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

    /**
     * Gets the value of the datePosted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatePosted() {
        return datePosted;
    }

    /**
     * Sets the value of the datePosted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatePosted(String value) {
        this.datePosted = value;
    }

    /**
     * Gets the value of the datePostingRemoved property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatePostingRemoved() {
        return datePostingRemoved;
    }

    /**
     * Sets the value of the datePostingRemoved property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatePostingRemoved(String value) {
        this.datePostingRemoved = value;
    }

    /**
     * Gets the value of the postingResult property.
     * 
     * @return
     *     possible object is
     *     {@link PostingResultType }
     *     
     */
    public PostingResultType getPostingResult() {
        return postingResult;
    }

    /**
     * Sets the value of the postingResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostingResultType }
     *     
     */
    public void setPostingResult(PostingResultType value) {
        this.postingResult = value;
    }

    /**
     * Gets the value of the educationOrganizationReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getEducationOrganizationReference() {
        return educationOrganizationReference;
    }

    /**
     * Sets the value of the educationOrganizationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setEducationOrganizationReference(EducationalOrgReferenceType value) {
        this.educationOrganizationReference = value;
    }

}
