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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SLC-AcademicWeekIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-AcademicWeekIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EducationalOrgReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *         &lt;element name="WeekIdentifier" type="{http://ed-fi.org/0100}WeekIdentifier"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-AcademicWeekIdentityType", propOrder = {
    "educationalOrgReference",
    "weekIdentifier"
})
public class SLCAcademicWeekIdentityType {

    @XmlElement(name = "EducationalOrgReference", required = true)
    protected SLCEducationalOrgReferenceType educationalOrgReference;
    @XmlElement(name = "WeekIdentifier", required = true)
    protected String weekIdentifier;

    /**
     * Gets the value of the educationalOrgReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getEducationalOrgReference() {
        return educationalOrgReference;
    }

    /**
     * Sets the value of the educationalOrgReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setEducationalOrgReference(SLCEducationalOrgReferenceType value) {
        this.educationalOrgReference = value;
    }

    /**
     * Gets the value of the weekIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeekIdentifier() {
        return weekIdentifier;
    }

    /**
     * Sets the value of the weekIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeekIdentifier(String value) {
        this.weekIdentifier = value;
    }

}
