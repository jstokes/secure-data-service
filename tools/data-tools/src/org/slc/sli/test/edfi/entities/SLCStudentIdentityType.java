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
 * Changed to contain only a required StudentUniqueStateId.
 * 
 * <p>Java class for SLC-StudentIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentUniqueStateId" type="{http://ed-fi.org/0100}UniqueStateIdentifier"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentIdentityType", propOrder = {
    "studentUniqueStateId"
})
public class SLCStudentIdentityType {

    @XmlElement(name = "StudentUniqueStateId", required = true)
    protected String studentUniqueStateId;

    /**
     * Gets the value of the studentUniqueStateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudentUniqueStateId() {
        return studentUniqueStateId;
    }

    /**
     * Sets the value of the studentUniqueStateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentUniqueStateId(String value) {
        this.studentUniqueStateId = value;
    }

}
