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
 * Changed to use a required SLC identity type.
 * 
 * <p>Java class for SLC-GradingPeriodReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-GradingPeriodReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GradingPeriodIdentity" type="{http://ed-fi.org/0100}SLC-GradingPeriodIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-GradingPeriodReferenceType", propOrder = {
    "gradingPeriodIdentity"
})
public class SLCGradingPeriodReferenceType {

    @XmlElement(name = "GradingPeriodIdentity", required = true)
    protected SLCGradingPeriodIdentityType gradingPeriodIdentity;

    /**
     * Gets the value of the gradingPeriodIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCGradingPeriodIdentityType }
     *     
     */
    public SLCGradingPeriodIdentityType getGradingPeriodIdentity() {
        return gradingPeriodIdentity;
    }

    /**
     * Sets the value of the gradingPeriodIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCGradingPeriodIdentityType }
     *     
     */
    public void setGradingPeriodIdentity(SLCGradingPeriodIdentityType value) {
        this.gradingPeriodIdentity = value;
    }

}
