//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents subtests that assess specific learning objectives.
 * 
 * <p>Java class for SLC-ObjectiveAssessment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-ObjectiveAssessment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="IdentificationCode" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *         &lt;element name="MaxRawScore" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="AssessmentPerformanceLevel" type="{http://ed-fi.org/0100}AssessmentPerformanceLevel" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PercentOfAssessment" type="{http://ed-fi.org/0100}percent" minOccurs="0"/>
 *         &lt;element name="Nomenclature" type="{http://ed-fi.org/0100}Nomenclature" minOccurs="0"/>
 *         &lt;element name="AssessmentItemReference" type="{http://ed-fi.org/0100}ReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}SLC-LearningObjectiveReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LearningStandardReference" type="{http://ed-fi.org/0100}SLC-LearningStandardReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ObjectiveAssessmentReference" type="{http://ed-fi.org/0100}ObjectiveAssessmentReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-ObjectiveAssessment", propOrder = {
    "identificationCode",
    "maxRawScore",
    "assessmentPerformanceLevel",
    "percentOfAssessment",
    "nomenclature",
    "assessmentItemReference",
    "learningObjectiveReference",
    "learningStandardReference",
    "objectiveAssessmentReference"
})
public class SLCObjectiveAssessment
    extends ComplexObjectType
{

    @XmlElement(name = "IdentificationCode", required = true)
    protected String identificationCode;
    @XmlElement(name = "MaxRawScore")
    protected Integer maxRawScore;
    @XmlElement(name = "AssessmentPerformanceLevel")
    protected List<AssessmentPerformanceLevel> assessmentPerformanceLevel;
    @XmlElement(name = "PercentOfAssessment")
    protected Integer percentOfAssessment;
    @XmlElement(name = "Nomenclature")
    protected String nomenclature;
    @XmlElement(name = "AssessmentItemReference")
    protected List<ReferenceType> assessmentItemReference;
    @XmlElement(name = "LearningObjectiveReference")
    protected List<SLCLearningObjectiveReferenceType> learningObjectiveReference;
    @XmlElement(name = "LearningStandardReference")
    protected List<SLCLearningStandardReferenceType> learningStandardReference;
    @XmlElement(name = "ObjectiveAssessmentReference")
    protected List<ObjectiveAssessmentReferenceType> objectiveAssessmentReference;

    /**
     * Gets the value of the identificationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificationCode() {
        return identificationCode;
    }

    /**
     * Sets the value of the identificationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificationCode(String value) {
        this.identificationCode = value;
    }

    /**
     * Gets the value of the maxRawScore property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxRawScore() {
        return maxRawScore;
    }

    /**
     * Sets the value of the maxRawScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxRawScore(Integer value) {
        this.maxRawScore = value;
    }

    /**
     * Gets the value of the assessmentPerformanceLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assessmentPerformanceLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssessmentPerformanceLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssessmentPerformanceLevel }
     * 
     * 
     */
    public List<AssessmentPerformanceLevel> getAssessmentPerformanceLevel() {
        if (assessmentPerformanceLevel == null) {
            assessmentPerformanceLevel = new ArrayList<AssessmentPerformanceLevel>();
        }
        return this.assessmentPerformanceLevel;
    }

    /**
     * Gets the value of the percentOfAssessment property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPercentOfAssessment() {
        return percentOfAssessment;
    }

    /**
     * Sets the value of the percentOfAssessment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPercentOfAssessment(Integer value) {
        this.percentOfAssessment = value;
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
     * Gets the value of the assessmentItemReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assessmentItemReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssessmentItemReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     * 
     * 
     */
    public List<ReferenceType> getAssessmentItemReference() {
        if (assessmentItemReference == null) {
            assessmentItemReference = new ArrayList<ReferenceType>();
        }
        return this.assessmentItemReference;
    }

    /**
     * Gets the value of the learningObjectiveReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningObjectiveReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningObjectiveReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCLearningObjectiveReferenceType }
     * 
     * 
     */
    public List<SLCLearningObjectiveReferenceType> getLearningObjectiveReference() {
        if (learningObjectiveReference == null) {
            learningObjectiveReference = new ArrayList<SLCLearningObjectiveReferenceType>();
        }
        return this.learningObjectiveReference;
    }

    /**
     * Gets the value of the learningStandardReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningStandardReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningStandardReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCLearningStandardReferenceType }
     * 
     * 
     */
    public List<SLCLearningStandardReferenceType> getLearningStandardReference() {
        if (learningStandardReference == null) {
            learningStandardReference = new ArrayList<SLCLearningStandardReferenceType>();
        }
        return this.learningStandardReference;
    }

    /**
     * Gets the value of the objectiveAssessmentReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objectiveAssessmentReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjectiveAssessmentReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectiveAssessmentReferenceType }
     * 
     * 
     */
    public List<ObjectiveAssessmentReferenceType> getObjectiveAssessmentReference() {
        if (objectiveAssessmentReference == null) {
            objectiveAssessmentReference = new ArrayList<ObjectiveAssessmentReferenceType>();
        }
        return this.objectiveAssessmentReference;
    }

}
