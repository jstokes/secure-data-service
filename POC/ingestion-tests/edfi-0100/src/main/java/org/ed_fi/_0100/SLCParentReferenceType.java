//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;


/**
 * Changed to use a required SLC identity type.
 * 
 * <p>Java class for SLC-ParentReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-ParentReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ParentIdentity" type="{http://ed-fi.org/0100}SLC-ParentIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-ParentReferenceType", propOrder = {
    "parentIdentity"
})
public class SLCParentReferenceType
    implements Locatable
{

    @XmlElement(name = "ParentIdentity", required = true)
    protected SLCParentIdentityType parentIdentity;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    /**
     * Gets the value of the parentIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCParentIdentityType }
     *     
     */
    public SLCParentIdentityType getParentIdentity() {
        return parentIdentity;
    }

    /**
     * Sets the value of the parentIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCParentIdentityType }
     *     
     */
    public void setParentIdentity(SLCParentIdentityType value) {
        this.parentIdentity = value;
    }

    public Locator sourceLocation() {
        return locator;
    }

    public void setSourceLocation(Locator newLocator) {
        locator = newLocator;
    }

}
