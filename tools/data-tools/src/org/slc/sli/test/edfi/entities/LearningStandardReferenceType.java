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
 * Provides alternative references for Learning Standard reference during interchange. Use XML IDREF to reference a learning standard record that is included in the interchange
 * 
 * <p>Java class for LearningStandardReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LearningStandardReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="LearningStandardIdentity" type="{http://ed-fi.org/0100}LearningStandardIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LearningStandardReferenceType", propOrder = {
    "learningStandardIdentity"
})
public class LearningStandardReferenceType
    extends ReferenceType
{

    @XmlElement(name = "LearningStandardIdentity")
    protected LearningStandardIdentityType learningStandardIdentity;

    /**
     * Gets the value of the learningStandardIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link LearningStandardIdentityType }
     *     
     */
    public LearningStandardIdentityType getLearningStandardIdentity() {
        return learningStandardIdentity;
    }

    /**
     * Sets the value of the learningStandardIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link LearningStandardIdentityType }
     *     
     */
    public void setLearningStandardIdentity(LearningStandardIdentityType value) {
        this.learningStandardIdentity = value;
    }

}
