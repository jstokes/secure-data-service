//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.31 at 09:35:49 AM EDT 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents the analysis or scoring of a student's response on an assessment. The analysis results in a value that represents a student's performance on a set of items on a test.
 * 
 * <p>Java class for StudentAssessment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentAssessment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="AdministrationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="AdministrationEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="SerialNumber" type="{http://ed-fi.org/0100}IdentificationCode" minOccurs="0"/>
 *         &lt;element name="AdministrationLanguage" type="{http://ed-fi.org/0100}LanguageItemType" minOccurs="0"/>
 *         &lt;element name="AdministrationEnvironment" type="{http://ed-fi.org/0100}AdministrationEnvironmentType" minOccurs="0"/>
 *         &lt;element name="SpecialAccommodations" type="{http://ed-fi.org/0100}SpecialAccommodationsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LinguisticAccommodations" type="{http://ed-fi.org/0100}LinguisticAccommodationsType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RetestIndicator" type="{http://ed-fi.org/0100}RetestIndicatorType" minOccurs="0"/>
 *         &lt;element name="ReasonNotTested" type="{http://ed-fi.org/0100}ReasonNotTestedType" minOccurs="0"/>
 *         &lt;element name="ScoreResults" type="{http://ed-fi.org/0100}ScoreResult" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GradeLevelWhenAssessed" type="{http://ed-fi.org/0100}GradeLevelType" minOccurs="0"/>
 *         &lt;element name="PerformanceLevels" type="{http://ed-fi.org/0100}PerformanceLevelDescriptorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *         &lt;element name="AssessmentReference" type="{http://ed-fi.org/0100}AssessmentReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentAssessment", propOrder = {
    "administrationDate",
    "administrationEndDate",
    "serialNumber",
    "administrationLanguage",
    "administrationEnvironment",
    "specialAccommodations",
    "linguisticAccommodations",
    "retestIndicator",
    "reasonNotTested",
    "scoreResults",
    "gradeLevelWhenAssessed",
    "performanceLevels",
    "studentReference",
    "assessmentReference"
})
public class StudentAssessment
    extends ComplexObjectType
{

    @XmlElement(name = "AdministrationDate", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String administrationDate;
    @XmlElement(name = "AdministrationEndDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String administrationEndDate;
    @XmlElement(name = "SerialNumber")
    protected String serialNumber;
    @XmlElement(name = "AdministrationLanguage")
    protected LanguageItemType administrationLanguage;
    @XmlElement(name = "AdministrationEnvironment")
    protected AdministrationEnvironmentType administrationEnvironment;
    @XmlElement(name = "SpecialAccommodations")
    protected List<SpecialAccommodationsType> specialAccommodations;
    @XmlElement(name = "LinguisticAccommodations")
    protected List<LinguisticAccommodationsType> linguisticAccommodations;
    @XmlElement(name = "RetestIndicator")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String retestIndicator;
    @XmlElement(name = "ReasonNotTested")
    protected ReasonNotTestedType reasonNotTested;
    @XmlElement(name = "ScoreResults")
    protected List<ScoreResult> scoreResults;
    @XmlElement(name = "GradeLevelWhenAssessed")
    protected GradeLevelType gradeLevelWhenAssessed;
    @XmlElement(name = "PerformanceLevels")
    protected List<PerformanceLevelDescriptorType> performanceLevels;
    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;
    @XmlElement(name = "AssessmentReference", required = true)
    protected AssessmentReferenceType assessmentReference;

    /**
     * Gets the value of the administrationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdministrationDate() {
        return administrationDate;
    }

    /**
     * Sets the value of the administrationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdministrationDate(String value) {
        this.administrationDate = value;
    }

    /**
     * Gets the value of the administrationEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdministrationEndDate() {
        return administrationEndDate;
    }

    /**
     * Sets the value of the administrationEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdministrationEndDate(String value) {
        this.administrationEndDate = value;
    }

    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Gets the value of the administrationLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link LanguageItemType }
     *     
     */
    public LanguageItemType getAdministrationLanguage() {
        return administrationLanguage;
    }

    /**
     * Sets the value of the administrationLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link LanguageItemType }
     *     
     */
    public void setAdministrationLanguage(LanguageItemType value) {
        this.administrationLanguage = value;
    }

    /**
     * Gets the value of the administrationEnvironment property.
     * 
     * @return
     *     possible object is
     *     {@link AdministrationEnvironmentType }
     *     
     */
    public AdministrationEnvironmentType getAdministrationEnvironment() {
        return administrationEnvironment;
    }

    /**
     * Sets the value of the administrationEnvironment property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdministrationEnvironmentType }
     *     
     */
    public void setAdministrationEnvironment(AdministrationEnvironmentType value) {
        this.administrationEnvironment = value;
    }

    /**
     * Gets the value of the specialAccommodations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialAccommodations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialAccommodations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SpecialAccommodationsType }
     * 
     * 
     */
    public List<SpecialAccommodationsType> getSpecialAccommodations() {
        if (specialAccommodations == null) {
            specialAccommodations = new ArrayList<SpecialAccommodationsType>();
        }
        return this.specialAccommodations;
    }

    /**
     * Gets the value of the linguisticAccommodations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linguisticAccommodations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinguisticAccommodations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinguisticAccommodationsType }
     * 
     * 
     */
    public List<LinguisticAccommodationsType> getLinguisticAccommodations() {
        if (linguisticAccommodations == null) {
            linguisticAccommodations = new ArrayList<LinguisticAccommodationsType>();
        }
        return this.linguisticAccommodations;
    }

    /**
     * Gets the value of the retestIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetestIndicator() {
        return retestIndicator;
    }

    /**
     * Sets the value of the retestIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetestIndicator(String value) {
        this.retestIndicator = value;
    }

    /**
     * Gets the value of the reasonNotTested property.
     * 
     * @return
     *     possible object is
     *     {@link ReasonNotTestedType }
     *     
     */
    public ReasonNotTestedType getReasonNotTested() {
        return reasonNotTested;
    }

    /**
     * Sets the value of the reasonNotTested property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReasonNotTestedType }
     *     
     */
    public void setReasonNotTested(ReasonNotTestedType value) {
        this.reasonNotTested = value;
    }

    /**
     * Gets the value of the scoreResults property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scoreResults property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScoreResults().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScoreResult }
     * 
     * 
     */
    public List<ScoreResult> getScoreResults() {
        if (scoreResults == null) {
            scoreResults = new ArrayList<ScoreResult>();
        }
        return this.scoreResults;
    }

    /**
     * Gets the value of the gradeLevelWhenAssessed property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getGradeLevelWhenAssessed() {
        return gradeLevelWhenAssessed;
    }

    /**
     * Sets the value of the gradeLevelWhenAssessed property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setGradeLevelWhenAssessed(GradeLevelType value) {
        this.gradeLevelWhenAssessed = value;
    }

    /**
     * Gets the value of the performanceLevels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the performanceLevels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerformanceLevels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PerformanceLevelDescriptorType }
     * 
     * 
     */
    public List<PerformanceLevelDescriptorType> getPerformanceLevels() {
        if (performanceLevels == null) {
            performanceLevels = new ArrayList<PerformanceLevelDescriptorType>();
        }
        return this.performanceLevels;
    }

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
     * Gets the value of the assessmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link AssessmentReferenceType }
     *     
     */
    public AssessmentReferenceType getAssessmentReference() {
        return assessmentReference;
    }

    /**
     * Sets the value of the assessmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssessmentReferenceType }
     *     
     */
    public void setAssessmentReference(AssessmentReferenceType value) {
        this.assessmentReference = value;
    }

}
