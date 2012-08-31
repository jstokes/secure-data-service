//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents an open staff position that
 * 				is seeking to be
 * 				filled by an education organization.
 * 			
 * 
 * <p>Java class for openStaffPosition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="openStaffPosition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="employmentStatus" type="{http://slc-sli/ed-org/0.1}employmentStatusType"/>
 *         &lt;element name="staffClassification" type="{http://slc-sli/ed-org/0.1}staffClassificationType"/>
 *         &lt;element name="positionTitle" type="{http://slc-sli/ed-org/0.1}positionTitle" minOccurs="0"/>
 *         &lt;element name="requisitionNumber" type="{http://slc-sli/ed-org/0.1}requisitionNumber"/>
 *         &lt;element name="programAssignment" type="{http://slc-sli/ed-org/0.1}programAssignmentType" minOccurs="0"/>
 *         &lt;element name="instructionalGradeLevels" type="{http://slc-sli/ed-org/0.1}gradeLevelType" minOccurs="0"/>
 *         &lt;element name="academicSubjects" type="{http://slc-sli/ed-org/0.1}academicSubjectType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="datePosted" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="datePostingRemoved" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="postingResult" type="{http://slc-sli/ed-org/0.1}postingResultType" minOccurs="0"/>
 *         &lt;element name="educationOrganizationReference" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "openStaffPosition", propOrder = {
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
public class OpenStaffPosition {

    @XmlElement(required = true)
    protected EmploymentStatusType employmentStatus;
    @XmlElement(required = true)
    protected StaffClassificationType staffClassification;
    protected String positionTitle;
    @XmlElement(required = true)
    protected String requisitionNumber;
    protected ProgramAssignmentType programAssignment;
    protected GradeLevelType instructionalGradeLevels;
    protected List<AcademicSubjectType> academicSubjects;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String datePosted;
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String datePostingRemoved;
    protected PostingResultType postingResult;
    @XmlElement(required = true)
    protected String educationOrganizationReference;

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
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getInstructionalGradeLevels() {
        return instructionalGradeLevels;
    }

    /**
     * Sets the value of the instructionalGradeLevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setInstructionalGradeLevels(GradeLevelType value) {
        this.instructionalGradeLevels = value;
    }

    /**
     * Gets the value of the academicSubjects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the academicSubjects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcademicSubjects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcademicSubjectType }
     * 
     * 
     */
    public List<AcademicSubjectType> getAcademicSubjects() {
        if (academicSubjects == null) {
            academicSubjects = new ArrayList<AcademicSubjectType>();
        }
        return this.academicSubjects;
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
     *     {@link String }
     *     
     */
    public String getEducationOrganizationReference() {
        return educationOrganizationReference;
    }

    /**
     * Sets the value of the educationOrganizationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducationOrganizationReference(String value) {
        this.educationOrganizationReference = value;
    }

}
