//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for SLC-StudentObjectiveAssessment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentObjectiveAssessment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="ScoreResults" type="{http://ed-fi.org/0100}ScoreResult" maxOccurs="unbounded"/>
 *         &lt;element name="PerformanceLevels" type="{http://ed-fi.org/0100}PerformanceLevelDescriptorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="StudentAssessmentReference" type="{http://ed-fi.org/0100}SLC-StudentAssessmentReferenceType"/>
 *         &lt;element name="ObjectiveAssessmentReference" type="{http://ed-fi.org/0100}SLC-ObjectiveAssessmentReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentObjectiveAssessment", propOrder = {
    "scoreResults",
    "performanceLevels",
    "studentAssessmentReference",
    "objectiveAssessmentReference"
})
public class SLCStudentObjectiveAssessment
    extends ComplexObjectType
{

    @XmlElement(name = "ScoreResults", required = true)
    protected List<ScoreResult> scoreResults;
    @XmlElement(name = "PerformanceLevels")
    protected List<PerformanceLevelDescriptorType> performanceLevels;
    @XmlElement(name = "StudentAssessmentReference", required = true)
    protected SLCStudentAssessmentReferenceType studentAssessmentReference;
    @XmlElement(name = "ObjectiveAssessmentReference", required = true)
    protected SLCObjectiveAssessmentReferenceType objectiveAssessmentReference;

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
     * Gets the value of the studentAssessmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentAssessmentReferenceType }
     *     
     */
    public SLCStudentAssessmentReferenceType getStudentAssessmentReference() {
        return studentAssessmentReference;
    }

    /**
     * Sets the value of the studentAssessmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentAssessmentReferenceType }
     *     
     */
    public void setStudentAssessmentReference(SLCStudentAssessmentReferenceType value) {
        this.studentAssessmentReference = value;
    }

    /**
     * Gets the value of the objectiveAssessmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCObjectiveAssessmentReferenceType }
     *     
     */
    public SLCObjectiveAssessmentReferenceType getObjectiveAssessmentReference() {
        return objectiveAssessmentReference;
    }

    /**
     * Sets the value of the objectiveAssessmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCObjectiveAssessmentReferenceType }
     *     
     */
    public void setObjectiveAssessmentReference(SLCObjectiveAssessmentReferenceType value) {
        this.objectiveAssessmentReference = value;
    }

}
