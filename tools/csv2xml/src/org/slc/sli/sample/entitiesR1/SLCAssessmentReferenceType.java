//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Changed to use a required SLC identity type.
 * 
 * <p>Java class for SLC-AssessmentReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-AssessmentReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="AssessmentIdentity" type="{http://ed-fi.org/0100}SLC-AssessmentIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-AssessmentReferenceType", propOrder = {
    "assessmentIdentity"
})
public class SLCAssessmentReferenceType
    extends ReferenceType
{

    @XmlElement(name = "AssessmentIdentity")
    protected SLCAssessmentIdentityType assessmentIdentity;

    /**
     * Gets the value of the assessmentIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCAssessmentIdentityType }
     *     
     */
    public SLCAssessmentIdentityType getAssessmentIdentity() {
        return assessmentIdentity;
    }

    /**
     * Sets the value of the assessmentIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCAssessmentIdentityType }
     *     
     */
    public void setAssessmentIdentity(SLCAssessmentIdentityType value) {
        this.assessmentIdentity = value;
    }

}
