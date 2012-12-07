//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * New SLC identity type for StudentObjectiveAssessment. Contains an embedded StudentAssessmentReference and an embedded ObjectiveAssessmentReference.
 * 
 * <p>Java class for SLC-StudentObjectiveAssessmentIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentObjectiveAssessmentIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentAssessmentReference" type="{http://ed-fi.org/0100}SLC-StudentAssessmentReferenceType"/>
 *         &lt;element name="ObjectiveAssessmentReference" type="{http://ed-fi.org/0100}SLC-ObjectiveAssessmentReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentObjectiveAssessmentIdentityType", propOrder = {
    "studentAssessmentReference",
    "objectiveAssessmentReference"
})
public class SLCStudentObjectiveAssessmentIdentityType {

    @XmlElement(name = "StudentAssessmentReference", required = true)
    protected SLCStudentAssessmentReferenceType studentAssessmentReference;
    @XmlElement(name = "ObjectiveAssessmentReference", required = true)
    protected SLCObjectiveAssessmentReferenceType objectiveAssessmentReference;

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
