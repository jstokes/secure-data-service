//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for SLC-StudentAcademicRecordReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentAcademicRecordReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentAcademicRecordIdentity" type="{http://ed-fi.org/0100}SLC-StudentAcademicRecordIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentAcademicRecordReferenceType", propOrder = {
    "studentAcademicRecordIdentity"
})
public class SLCStudentAcademicRecordReferenceType {

    @XmlElement(name = "StudentAcademicRecordIdentity", required = true)
    protected SLCStudentAcademicRecordIdentityType studentAcademicRecordIdentity;

    /**
     * Gets the value of the studentAcademicRecordIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentAcademicRecordIdentityType }
     *     
     */
    public SLCStudentAcademicRecordIdentityType getStudentAcademicRecordIdentity() {
        return studentAcademicRecordIdentity;
    }

    /**
     * Sets the value of the studentAcademicRecordIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentAcademicRecordIdentityType }
     *     
     */
    public void setStudentAcademicRecordIdentity(SLCStudentAcademicRecordIdentityType value) {
        this.studentAcademicRecordIdentity = value;
    }

}
