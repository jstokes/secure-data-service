//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents any type of list of designated students for tracking, analysis, or intervention.
 * 
 * <p>Java class for Cohort complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Cohort">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="CohortIdentifier" type="{http://ed-fi.org/0100}CohortIdentifier"/>
 *         &lt;element name="CohortDescription" type="{http://ed-fi.org/0100}CohortDescription" minOccurs="0"/>
 *         &lt;element name="CohortType" type="{http://ed-fi.org/0100}CohortType"/>
 *         &lt;element name="CohortScope" type="{http://ed-fi.org/0100}CohortScopeType" minOccurs="0"/>
 *         &lt;element name="AcademicSubject" type="{http://ed-fi.org/0100}AcademicSubjectType" minOccurs="0"/>
 *         &lt;element name="EducationOrgReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="ProgramReference" type="{http://ed-fi.org/0100}ProgramReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cohort", propOrder = {
    "cohortIdentifier",
    "cohortDescription",
    "cohortType",
    "cohortScope",
    "academicSubject",
    "educationOrgReference",
    "programReference"
})
public class Cohort
    extends ComplexObjectType
{

    @XmlElement(name = "CohortIdentifier", required = true)
    protected String cohortIdentifier;
    @XmlElement(name = "CohortDescription")
    protected String cohortDescription;
    @XmlElement(name = "CohortType", required = true)
    protected CohortType cohortType;
    @XmlElement(name = "CohortScope")
    protected CohortScopeType cohortScope;
    @XmlElement(name = "AcademicSubject")
    protected AcademicSubjectType academicSubject;
    @XmlElement(name = "EducationOrgReference", required = true)
    protected EducationalOrgReferenceType educationOrgReference;
    @XmlElement(name = "ProgramReference")
    protected List<ProgramReferenceType> programReference;

    /**
     * Gets the value of the cohortIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCohortIdentifier() {
        return cohortIdentifier;
    }

    /**
     * Sets the value of the cohortIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCohortIdentifier(String value) {
        this.cohortIdentifier = value;
    }

    /**
     * Gets the value of the cohortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCohortDescription() {
        return cohortDescription;
    }

    /**
     * Sets the value of the cohortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCohortDescription(String value) {
        this.cohortDescription = value;
    }

    /**
     * Gets the value of the cohortType property.
     * 
     * @return
     *     possible object is
     *     {@link CohortType }
     *     
     */
    public CohortType getCohortType() {
        return cohortType;
    }

    /**
     * Sets the value of the cohortType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CohortType }
     *     
     */
    public void setCohortType(CohortType value) {
        this.cohortType = value;
    }

    /**
     * Gets the value of the cohortScope property.
     * 
     * @return
     *     possible object is
     *     {@link CohortScopeType }
     *     
     */
    public CohortScopeType getCohortScope() {
        return cohortScope;
    }

    /**
     * Sets the value of the cohortScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link CohortScopeType }
     *     
     */
    public void setCohortScope(CohortScopeType value) {
        this.cohortScope = value;
    }

    /**
     * Gets the value of the academicSubject property.
     * 
     * @return
     *     possible object is
     *     {@link AcademicSubjectType }
     *     
     */
    public AcademicSubjectType getAcademicSubject() {
        return academicSubject;
    }

    /**
     * Sets the value of the academicSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcademicSubjectType }
     *     
     */
    public void setAcademicSubject(AcademicSubjectType value) {
        this.academicSubject = value;
    }

    /**
     * Gets the value of the educationOrgReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getEducationOrgReference() {
        return educationOrgReference;
    }

    /**
     * Sets the value of the educationOrgReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setEducationOrgReference(EducationalOrgReferenceType value) {
        this.educationOrgReference = value;
    }

    /**
     * Gets the value of the programReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProgramReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProgramReferenceType }
     * 
     * 
     */
    public List<ProgramReferenceType> getProgramReference() {
        if (programReference == null) {
            programReference = new ArrayList<ProgramReferenceType>();
        }
        return this.programReference;
    }

}
