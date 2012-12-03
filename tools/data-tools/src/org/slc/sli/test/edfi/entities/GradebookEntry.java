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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents a teacherís assignment, homework, or classroom assessment to be recorded in a gradebook.
 * 
 * <p>Java class for GradebookEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GradebookEntry">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="GradebookEntryType" type="{http://ed-fi.org/0100}GradebookEntryType"/>
 *         &lt;element name="DateAssigned" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Description" type="{http://ed-fi.org/0100}Description" minOccurs="0"/>
 *         &lt;element name="LearningStandardReference" type="{http://ed-fi.org/0100}LearningStandardReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}LearningObjectiveReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SectionReference" type="{http://ed-fi.org/0100}SectionReferenceType"/>
 *         &lt;element name="GradingPeriodReference" type="{http://ed-fi.org/0100}GradingPeriodReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GradebookEntry", propOrder = {
    "gradebookEntryType",
    "dateAssigned",
    "description",
    "learningStandardReference",
    "learningObjectiveReference",
    "sectionReference",
    "gradingPeriodReference"
})
public class GradebookEntry
    extends ComplexObjectType
{

    @XmlElement(name = "GradebookEntryType", required = true)
    protected String gradebookEntryType;
    @XmlElement(name = "DateAssigned", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String dateAssigned;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "LearningStandardReference")
    protected List<LearningStandardReferenceType> learningStandardReference;
    @XmlElement(name = "LearningObjectiveReference")
    protected List<LearningObjectiveReferenceType> learningObjectiveReference;
    @XmlElement(name = "SectionReference", required = true)
    protected SectionReferenceType sectionReference;
    @XmlElement(name = "GradingPeriodReference")
    protected GradingPeriodReferenceType gradingPeriodReference;

    /**
     * Gets the value of the gradebookEntryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGradebookEntryType() {
        return gradebookEntryType;
    }

    /**
     * Sets the value of the gradebookEntryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGradebookEntryType(String value) {
        this.gradebookEntryType = value;
    }

    /**
     * Gets the value of the dateAssigned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateAssigned() {
        return dateAssigned;
    }

    /**
     * Sets the value of the dateAssigned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateAssigned(String value) {
        this.dateAssigned = value;
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

    /**
     * Gets the value of the sectionReference property.
     * 
     * @return
     *     possible object is
     *     {@link SectionReferenceType }
     *     
     */
    public SectionReferenceType getSectionReference() {
        return sectionReference;
    }

    /**
     * Sets the value of the sectionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SectionReferenceType }
     *     
     */
    public void setSectionReference(SectionReferenceType value) {
        this.sectionReference = value;
    }

    /**
     * Gets the value of the gradingPeriodReference property.
     * 
     * @return
     *     possible object is
     *     {@link GradingPeriodReferenceType }
     *     
     */
    public GradingPeriodReferenceType getGradingPeriodReference() {
        return gradingPeriodReference;
    }

    /**
     * Sets the value of the gradingPeriodReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradingPeriodReferenceType }
     *     
     */
    public void setGradingPeriodReference(GradingPeriodReferenceType value) {
        this.gradingPeriodReference = value;
    }

}
