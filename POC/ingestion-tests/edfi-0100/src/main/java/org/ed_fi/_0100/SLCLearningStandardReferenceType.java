//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 * <p>Java class for SLC-LearningStandardReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-LearningStandardReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LearningStandardIdentity" type="{http://ed-fi.org/0100}SLC-LearningStandardIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-LearningStandardReferenceType", propOrder = {
    "learningStandardIdentity"
})
public class SLCLearningStandardReferenceType {

    @XmlElement(name = "LearningStandardIdentity", required = true)
    protected SLCLearningStandardIdentityType learningStandardIdentity;

    /**
     * Gets the value of the learningStandardIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCLearningStandardIdentityType }
     *     
     */
    public SLCLearningStandardIdentityType getLearningStandardIdentity() {
        return learningStandardIdentity;
    }

    /**
     * Sets the value of the learningStandardIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCLearningStandardIdentityType }
     *     
     */
    public void setLearningStandardIdentity(SLCLearningStandardIdentityType value) {
        this.learningStandardIdentity = value;
    }

}
