//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Changed type of StateEducationAgencyReference to SLC reference type.
 * 
 * <p>Java class for SLC-EducationServiceCenter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-EducationServiceCenter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}SLC-EducationOrganization">
 *       &lt;sequence>
 *         &lt;element name="StateEducationAgencyReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-EducationServiceCenter", propOrder = {
    "stateEducationAgencyReference"
})
@XmlRootElement(name = "EducationServiceCenter") 
public class SLCEducationServiceCenter
    extends SLCEducationOrganization
{

    @XmlElement(name = "StateEducationAgencyReference")
    protected EducationalOrgReferenceType stateEducationAgencyReference;

    /**
     * Gets the value of the stateEducationAgencyReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getStateEducationAgencyReference() {
        return stateEducationAgencyReference;
    }

    /**
     * Sets the value of the stateEducationAgencyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setStateEducationAgencyReference(EducationalOrgReferenceType value) {
        this.stateEducationAgencyReference = value;
    }

}
