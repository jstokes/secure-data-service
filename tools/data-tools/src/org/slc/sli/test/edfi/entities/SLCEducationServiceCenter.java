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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents a regional, multi-services public agency authorized by State law to develop, manage, and provide services, programs, or other options support (e.g., construction, food services, and technology services) to LEAs.
 * 
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
