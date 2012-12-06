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
 * New SLC identity type for StudentCompetency. Contains an embedded LearningObjectiveReference, an embedded StudentCompetencyObjectiveReference, CodeValue and an embedded StudentSectionAssociationReference.
 * 
 * <p>Java class for SLC-StudentCompetencyIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentCompetencyIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}SLC-LearningObjectiveReferenceType"/>
 *           &lt;element name="StudentCompetencyObjectiveReference" type="{http://ed-fi.org/0100}SLC-StudentCompetencyObjectiveReferenceType"/>
 *         &lt;/choice>
 *         &lt;element name="CodeValue" type="{http://ed-fi.org/0100}CodeValue" minOccurs="0"/>
 *         &lt;element name="StudentSectionAssociationReference" type="{http://ed-fi.org/0100}SLC-StudentSectionAssociationReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentCompetencyIdentityType", propOrder = {
    "learningObjectiveReference",
    "studentCompetencyObjectiveReference",
    "codeValue",
    "studentSectionAssociationReference"
})
public class SLCStudentCompetencyIdentityType {

    @XmlElement(name = "LearningObjectiveReference")
    protected SLCLearningObjectiveReferenceType learningObjectiveReference;
    @XmlElement(name = "StudentCompetencyObjectiveReference")
    protected SLCStudentCompetencyObjectiveReferenceType studentCompetencyObjectiveReference;
    @XmlElement(name = "CodeValue")
    protected String codeValue;
    @XmlElement(name = "StudentSectionAssociationReference", required = true)
    protected SLCStudentSectionAssociationReferenceType studentSectionAssociationReference;

    /**
     * Gets the value of the learningObjectiveReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCLearningObjectiveReferenceType }
     *     
     */
    public SLCLearningObjectiveReferenceType getLearningObjectiveReference() {
        return learningObjectiveReference;
    }

    /**
     * Sets the value of the learningObjectiveReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCLearningObjectiveReferenceType }
     *     
     */
    public void setLearningObjectiveReference(SLCLearningObjectiveReferenceType value) {
        this.learningObjectiveReference = value;
    }

    /**
     * Gets the value of the studentCompetencyObjectiveReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentCompetencyObjectiveReferenceType }
     *     
     */
    public SLCStudentCompetencyObjectiveReferenceType getStudentCompetencyObjectiveReference() {
        return studentCompetencyObjectiveReference;
    }

    /**
     * Sets the value of the studentCompetencyObjectiveReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentCompetencyObjectiveReferenceType }
     *     
     */
    public void setStudentCompetencyObjectiveReference(SLCStudentCompetencyObjectiveReferenceType value) {
        this.studentCompetencyObjectiveReference = value;
    }

    /**
     * Gets the value of the codeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * Sets the value of the codeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeValue(String value) {
        this.codeValue = value;
    }

    /**
     * Gets the value of the studentSectionAssociationReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentSectionAssociationReferenceType }
     *     
     */
    public SLCStudentSectionAssociationReferenceType getStudentSectionAssociationReference() {
        return studentSectionAssociationReference;
    }

    /**
     * Sets the value of the studentSectionAssociationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentSectionAssociationReferenceType }
     *     
     */
    public void setStudentSectionAssociationReference(SLCStudentSectionAssociationReferenceType value) {
        this.studentSectionAssociationReference = value;
    }

}
