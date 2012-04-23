//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 01:12:00 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity holds the score and or performance levels earned for an objective assessment by a student.
 * 
 * <p>Java class for StudentObjectiveAssessment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentObjectiveAssessment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="ScoreResults" type="{http://ed-fi.org/0100}ScoreResult" maxOccurs="unbounded"/>
 *         &lt;element name="PerformanceLevels" type="{http://ed-fi.org/0100}PerformanceLevelDescriptorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="StudentTestAssessmentReference" type="{http://ed-fi.org/0100}ReferenceType"/>
 *         &lt;element name="ObjectiveAssessmentReference" type="{http://ed-fi.org/0100}ObjectiveAssessmentReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentObjectiveAssessment", propOrder = {
    "scoreResults",
    "performanceLevels",
    "studentTestAssessmentReference",
    "objectiveAssessmentReference"
})
public class StudentObjectiveAssessment
    extends ComplexObjectType
{

    @XmlElement(name = "ScoreResults", required = true)
    protected List<ScoreResult> scoreResults;
    @XmlElement(name = "PerformanceLevels")
    protected List<PerformanceLevelDescriptorType> performanceLevels;
    @XmlElement(name = "StudentTestAssessmentReference", required = true)
    protected ReferenceType studentTestAssessmentReference;
    @XmlElement(name = "ObjectiveAssessmentReference", required = true)
    protected ObjectiveAssessmentReferenceType objectiveAssessmentReference;

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
     * Gets the value of the studentTestAssessmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceType }
     *     
     */
    public ReferenceType getStudentTestAssessmentReference() {
        return studentTestAssessmentReference;
    }

    /**
     * Sets the value of the studentTestAssessmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceType }
     *     
     */
    public void setStudentTestAssessmentReference(ReferenceType value) {
        this.studentTestAssessmentReference = value;
    }

    /**
     * Gets the value of the objectiveAssessmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectiveAssessmentReferenceType }
     *     
     */
    public ObjectiveAssessmentReferenceType getObjectiveAssessmentReference() {
        return objectiveAssessmentReference;
    }

    /**
     * Sets the value of the objectiveAssessmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectiveAssessmentReferenceType }
     *     
     */
    public void setObjectiveAssessmentReference(ObjectiveAssessmentReferenceType value) {
        this.objectiveAssessmentReference = value;
    }

}
