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
 * This entity represents identified learning objectives for courses in specific grades.
 * 
 * <p>Java class for LearningObjective complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LearningObjective">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="LearningObjectiveId" type="{http://ed-fi.org/0100}LearningStandardId" minOccurs="0"/>
 *         &lt;element name="Objective" type="{http://ed-fi.org/0100}Objective"/>
 *         &lt;element name="Description" type="{http://ed-fi.org/0100}Description" minOccurs="0"/>
 *         &lt;element name="AcademicSubject" type="{http://ed-fi.org/0100}AcademicSubjectType"/>
 *         &lt;element name="ObjectiveGradeLevel" type="{http://ed-fi.org/0100}GradeLevelType"/>
 *         &lt;element name="LearningStandardReference" type="{http://ed-fi.org/0100}LearningStandardReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}LearningObjectiveReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LearningObjective", propOrder = {
    "learningObjectiveId",
    "objective",
    "description",
    "academicSubject",
    "objectiveGradeLevel",
    "learningStandardReference",
    "learningObjectiveReference"
})
public class LearningObjective
    extends ComplexObjectType
{

    @XmlElement(name = "LearningObjectiveId")
    protected LearningStandardId learningObjectiveId;
    @XmlElement(name = "Objective", required = true)
    protected String objective;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "AcademicSubject", required = true)
    protected AcademicSubjectType academicSubject;
    @XmlElement(name = "ObjectiveGradeLevel", required = true)
    protected GradeLevelType objectiveGradeLevel;
    @XmlElement(name = "LearningStandardReference")
    protected List<LearningStandardReferenceType> learningStandardReference;
    @XmlElement(name = "LearningObjectiveReference")
    protected List<LearningObjectiveReferenceType> learningObjectiveReference;

    /**
     * Gets the value of the learningObjectiveId property.
     * 
     * @return
     *     possible object is
     *     {@link LearningStandardId }
     *     
     */
    public LearningStandardId getLearningObjectiveId() {
        return learningObjectiveId;
    }

    /**
     * Sets the value of the learningObjectiveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link LearningStandardId }
     *     
     */
    public void setLearningObjectiveId(LearningStandardId value) {
        this.learningObjectiveId = value;
    }

    /**
     * Gets the value of the objective property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Sets the value of the objective property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjective(String value) {
        this.objective = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     * Gets the value of the objectiveGradeLevel property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getObjectiveGradeLevel() {
        return objectiveGradeLevel;
    }

    /**
     * Sets the value of the objectiveGradeLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setObjectiveGradeLevel(GradeLevelType value) {
        this.objectiveGradeLevel = value;
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
     * {@link LearningStandardReferenceType }
     * 
     * 
     */
    public List<LearningStandardReferenceType> getLearningStandardReference() {
        if (learningStandardReference == null) {
            learningStandardReference = new ArrayList<LearningStandardReferenceType>();
        }
        return this.learningStandardReference;
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
     * {@link LearningObjectiveReferenceType }
     * 
     * 
     */
    public List<LearningObjectiveReferenceType> getLearningObjectiveReference() {
        if (learningObjectiveReference == null) {
            learningObjectiveReference = new ArrayList<LearningObjectiveReferenceType>();
        }
        return this.learningObjectiveReference;
    }

}
