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
 * <p>Java class for SLC-DiplomaReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-DiplomaReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DiplomaIdentity" type="{http://ed-fi.org/0100}SLC-DiplomaIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-DiplomaReferenceType", propOrder = {
    "diplomaIdentity"
})
public class SLCDiplomaReferenceType {

    @XmlElement(name = "DiplomaIdentity", required = true)
    protected SLCDiplomaIdentityType diplomaIdentity;

    /**
     * Gets the value of the diplomaIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCDiplomaIdentityType }
     *     
     */
    public SLCDiplomaIdentityType getDiplomaIdentity() {
        return diplomaIdentity;
    }

    /**
     * Sets the value of the diplomaIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCDiplomaIdentityType }
     *     
     */
    public void setDiplomaIdentity(SLCDiplomaIdentityType value) {
        this.diplomaIdentity = value;
    }

}
