//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Encapsulates the possible attributes that can be used to lookup the identity of the Student Competency Objectives.
 * 
 * <p>Java class for SLC-StudentCompetencyObjectiveIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentCompetencyObjectiveIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentCompetencyObjectiveId" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentCompetencyObjectiveIdentityType", propOrder = {
    "studentCompetencyObjectiveId"
})
public class SLCStudentCompetencyObjectiveIdentityType {

    @XmlElement(name = "StudentCompetencyObjectiveId", required = true)
    protected String studentCompetencyObjectiveId;

    /**
     * Gets the value of the studentCompetencyObjectiveId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudentCompetencyObjectiveId() {
        return studentCompetencyObjectiveId;
    }

    /**
     * Sets the value of the studentCompetencyObjectiveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentCompetencyObjectiveId(String value) {
        this.studentCompetencyObjectiveId = value;
    }

}
