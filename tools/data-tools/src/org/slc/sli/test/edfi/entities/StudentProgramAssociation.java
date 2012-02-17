//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.02.12 at 04:54:37 PM EST
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This association represents the program(s) that a student participates in or is served by.
 *
 *
 * <p>Java class for StudentProgramAssociation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StudentProgramAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *         &lt;element name="ProgramReference" type="{http://ed-fi.org/0100}ProgramReferenceType"/>
 *         &lt;element name="Services" type="{http://ed-fi.org/0100}ServiceDescriptorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ReasonExited" type="{http://ed-fi.org/0100}ReasonExitedType" minOccurs="0"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentProgramAssociation", propOrder = {
    "studentReference",
    "programReference",
    "services",
    "beginDate",
    "endDate",
    "reasonExited",
    "educationOrganizationReference"
})
@XmlSeeAlso({
    StudentSpecialEdProgramAssociation.class,
    StudentCTEProgramAssociation.class,
    StudentTitleIPartAProgramAssociation.class
})
public class StudentProgramAssociation {

    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;
    @XmlElement(name = "ProgramReference", required = true)
    protected ProgramReferenceType programReference;
    @XmlElement(name = "Services")
    protected List<ServiceDescriptorType> services;
    @XmlElement(name = "BeginDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar beginDate;
    @XmlElement(name = "EndDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar endDate;
    @XmlElement(name = "ReasonExited")
    protected ReasonExitedType reasonExited;
    @XmlElement(name = "EducationOrganizationReference", required = true)
    protected EducationalOrgReferenceType educationOrganizationReference;

    /**
     * Gets the value of the studentReference property.
     *
     * @return
     *     possible object is
     *     {@link StudentReferenceType }
     *
     */
    public StudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     *
     * @param value
     *     allowed object is
     *     {@link StudentReferenceType }
     *
     */
    public void setStudentReference(StudentReferenceType value) {
        this.studentReference = value;
    }

    /**
     * Gets the value of the programReference property.
     *
     * @return
     *     possible object is
     *     {@link ProgramReferenceType }
     *
     */
    public ProgramReferenceType getProgramReference() {
        return programReference;
    }

    /**
     * Sets the value of the programReference property.
     *
     * @param value
     *     allowed object is
     *     {@link ProgramReferenceType }
     *
     */
    public void setProgramReference(ProgramReferenceType value) {
        this.programReference = value;
    }

    /**
     * Gets the value of the services property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the services property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServices().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceDescriptorType }
     *
     *
     */
    public List<ServiceDescriptorType> getServices() {
        if (services == null) {
            services = new ArrayList<ServiceDescriptorType>();
        }
        return this.services;
    }

    /**
     * Gets the value of the beginDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Calendar getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBeginDate(Calendar value) {
        this.beginDate = value;
    }

    /**
     * Gets the value of the endDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEndDate(Calendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the reasonExited property.
     *
     * @return
     *     possible object is
     *     {@link ReasonExitedType }
     *
     */
    public ReasonExitedType getReasonExited() {
        return reasonExited;
    }

    /**
     * Sets the value of the reasonExited property.
     *
     * @param value
     *     allowed object is
     *     {@link ReasonExitedType }
     *
     */
    public void setReasonExited(ReasonExitedType value) {
        this.reasonExited = value;
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
