//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 01:12:00 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides alternative references for sessions at an education organization. Use XML IDREF to reference a section record that is included in the interchange
 * 
 * <p>Java class for SessionReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SessionReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="SessionIdentity" type="{http://ed-fi.org/0100}SessionIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SessionReferenceType", propOrder = {
    "sessionIdentity"
})
public class SessionReferenceType
    extends ReferenceType
{

    @XmlElement(name = "SessionIdentity")
    protected SessionIdentityType sessionIdentity;

    /**
     * Gets the value of the sessionIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SessionIdentityType }
     *     
     */
    public SessionIdentityType getSessionIdentity() {
        return sessionIdentity;
    }

    /**
     * Sets the value of the sessionIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SessionIdentityType }
     *     
     */
    public void setSessionIdentity(SessionIdentityType value) {
        this.sessionIdentity = value;
    }

}
