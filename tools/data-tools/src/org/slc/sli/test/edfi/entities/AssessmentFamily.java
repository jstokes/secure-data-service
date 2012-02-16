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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents a logical grouping or association of assessments that share a common purpose, heritage, or content standard.  There may be hierarchies of Assessment Families. Characteristics (e.g., Academic Subject) specified for AssessmentFamilies, by convention, are inherited by the Assessments associated with the AssessmentFamily.
 * 
 * <p>Java class for AssessmentFamily complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssessmentFamily">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="AssessmentFamilyTitle" type="{http://ed-fi.org/0100}AssessmentTitle"/>
 *         &lt;element name="AssessmentFamilyIdentificationCode" type="{http://ed-fi.org/0100}AssessmentIdentificationCode" maxOccurs="unbounded"/>
 *         &lt;element name="AssessmentCategory" type="{http://ed-fi.org/0100}AssessmentCategoryType" minOccurs="0"/>
 *         &lt;element name="AcademicSubject" type="{http://ed-fi.org/0100}AcademicSubjectType" minOccurs="0"/>
 *         &lt;element name="GradeLevelAssessed" type="{http://ed-fi.org/0100}GradeLevelType" minOccurs="0"/>
 *         &lt;element name="LowestGradeLevelAssessed" type="{http://ed-fi.org/0100}GradeLevelType" minOccurs="0"/>
 *         &lt;element name="ContentStandard" type="{http://ed-fi.org/0100}ContentStandardType" minOccurs="0"/>
 *         &lt;element name="Version" type="{http://ed-fi.org/0100}Version" minOccurs="0"/>
 *         &lt;element name="RevisionDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="Nomenclature" type="{http://ed-fi.org/0100}Nomenclature" minOccurs="0"/>
 *         &lt;element name="AssessmentPeriods" type="{http://ed-fi.org/0100}AssessmentPeriodDescriptorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AssessmentFamilyReference" type="{http://ed-fi.org/0100}AssessmentFamilyReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssessmentFamily", propOrder = {
    "assessmentFamilyTitle",
    "assessmentFamilyIdentificationCode",
    "assessmentCategory",
    "academicSubject",
    "gradeLevelAssessed",
    "lowestGradeLevelAssessed",
    "contentStandard",
    "version",
    "revisionDate",
    "nomenclature",
    "assessmentPeriods",
    "assessmentFamilyReference"
})
public class AssessmentFamily
    extends ComplexObjectType
{

    @XmlElement(name = "AssessmentFamilyTitle", required = true)
    protected String assessmentFamilyTitle;
    @XmlElement(name = "AssessmentFamilyIdentificationCode", required = true)
    protected List<AssessmentIdentificationCode> assessmentFamilyIdentificationCode;
    @XmlElement(name = "AssessmentCategory")
    protected AssessmentCategoryType assessmentCategory;
    @XmlElement(name = "AcademicSubject")
    protected AcademicSubjectType academicSubject;
    @XmlElement(name = "GradeLevelAssessed")
    protected GradeLevelType gradeLevelAssessed;
    @XmlElement(name = "LowestGradeLevelAssessed")
    protected GradeLevelType lowestGradeLevelAssessed;
    @XmlElement(name = "ContentStandard")
    protected ContentStandardType contentStandard;
    @XmlElement(name = "Version")
    protected Integer version;
    @XmlElement(name = "RevisionDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar revisionDate;
    @XmlElement(name = "Nomenclature")
    protected String nomenclature;
    @XmlElement(name = "AssessmentPeriods")
    protected List<AssessmentPeriodDescriptorType> assessmentPeriods;
    @XmlElement(name = "AssessmentFamilyReference")
    protected AssessmentFamilyReferenceType assessmentFamilyReference;

    /**
     * Gets the value of the assessmentFamilyTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssessmentFamilyTitle() {
        return assessmentFamilyTitle;
    }

    /**
     * Sets the value of the assessmentFamilyTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssessmentFamilyTitle(String value) {
        this.assessmentFamilyTitle = value;
    }

    /**
     * Gets the value of the assessmentFamilyIdentificationCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assessmentFamilyIdentificationCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssessmentFamilyIdentificationCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssessmentIdentificationCode }
     * 
     * 
     */
    public List<AssessmentIdentificationCode> getAssessmentFamilyIdentificationCode() {
        if (assessmentFamilyIdentificationCode == null) {
            assessmentFamilyIdentificationCode = new ArrayList<AssessmentIdentificationCode>();
        }
        return this.assessmentFamilyIdentificationCode;
    }

    /**
     * Gets the value of the assessmentCategory property.
     * 
     * @return
     *     possible object is
     *     {@link AssessmentCategoryType }
     *     
     */
    public AssessmentCategoryType getAssessmentCategory() {
        return assessmentCategory;
    }

    /**
     * Sets the value of the assessmentCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssessmentCategoryType }
     *     
     */
    public void setAssessmentCategory(AssessmentCategoryType value) {
        this.assessmentCategory = value;
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
     * Gets the value of the gradeLevelAssessed property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getGradeLevelAssessed() {
        return gradeLevelAssessed;
    }

    /**
     * Sets the value of the gradeLevelAssessed property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setGradeLevelAssessed(GradeLevelType value) {
        this.gradeLevelAssessed = value;
    }

    /**
     * Gets the value of the lowestGradeLevelAssessed property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getLowestGradeLevelAssessed() {
        return lowestGradeLevelAssessed;
    }

    /**
     * Sets the value of the lowestGradeLevelAssessed property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setLowestGradeLevelAssessed(GradeLevelType value) {
        this.lowestGradeLevelAssessed = value;
    }

    /**
     * Gets the value of the contentStandard property.
     * 
     * @return
     *     possible object is
     *     {@link ContentStandardType }
     *     
     */
    public ContentStandardType getContentStandard() {
        return contentStandard;
    }

    /**
     * Sets the value of the contentStandard property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentStandardType }
     *     
     */
    public void setContentStandard(ContentStandardType value) {
        this.contentStandard = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVersion(Integer value) {
        this.version = value;
    }

    /**
     * Gets the value of the revisionDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getRevisionDate() {
        return revisionDate;
    }

    /**
     * Sets the value of the revisionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevisionDate(Calendar value) {
        this.revisionDate = value;
    }

    /**
     * Gets the value of the nomenclature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomenclature() {
        return nomenclature;
    }

    /**
     * Sets the value of the nomenclature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomenclature(String value) {
        this.nomenclature = value;
    }

    /**
     * Gets the value of the assessmentPeriods property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assessmentPeriods property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssessmentPeriods().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssessmentPeriodDescriptorType }
     * 
     * 
     */
    public List<AssessmentPeriodDescriptorType> getAssessmentPeriods() {
        if (assessmentPeriods == null) {
            assessmentPeriods = new ArrayList<AssessmentPeriodDescriptorType>();
        }
        return this.assessmentPeriods;
    }

    /**
     * Gets the value of the assessmentFamilyReference property.
     * 
     * @return
     *     possible object is
     *     {@link AssessmentFamilyReferenceType }
     *     
     */
    public AssessmentFamilyReferenceType getAssessmentFamilyReference() {
        return assessmentFamilyReference;
    }

    /**
     * Sets the value of the assessmentFamilyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssessmentFamilyReferenceType }
     *     
     */
    public void setAssessmentFamilyReference(AssessmentFamilyReferenceType value) {
        this.assessmentFamilyReference = value;
    }

}
