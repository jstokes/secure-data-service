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
 * <p>Java class for SLC-AssessmentItemIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-AssessmentItemIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AssessmentItemIdentificationCode" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-AssessmentItemIdentityType", propOrder = {
    "assessmentItemIdentificationCode"
})
public class SLCAssessmentItemIdentityType {

    @XmlElement(name = "AssessmentItemIdentificationCode", required = true)
    protected String assessmentItemIdentificationCode;

    /**
     * Gets the value of the assessmentItemIdentificationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssessmentItemIdentificationCode() {
        return assessmentItemIdentificationCode;
    }

    /**
     * Sets the value of the assessmentItemIdentificationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssessmentItemIdentificationCode(String value) {
        this.assessmentItemIdentificationCode = value;
    }

}
