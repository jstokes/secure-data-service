//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.10.02 at 05:28:16 PM EDT
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides alternative references for grading periods. Use XML IDREF to reference a program record that is included in the interchange
 *
 * <p>Java class for GradingPeriodReferenceType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="GradingPeriodReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="GradingPeriodIdentity" type="{http://ed-fi.org/0100}GradingPeriodIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GradingPeriodReferenceType", propOrder = {
    "gradingPeriodIdentity"
})
public class GradingPeriodReferenceType
    extends ReferenceType
{

    @XmlElement(name = "GradingPeriodIdentity")
    protected SLCGradingPeriodIdentityType gradingPeriodIdentity;

    /**
     * Gets the value of the gradingPeriodIdentity property.
     *
     * @return
     *     possible object is
     *     {@link GradingPeriodIdentityType }
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
     *     {@link GradingPeriodIdentityType }
     *
     */
    public void setGradingPeriodIdentity(SLCGradingPeriodIdentityType value) {
        this.gradingPeriodIdentity = value;
    }

}
