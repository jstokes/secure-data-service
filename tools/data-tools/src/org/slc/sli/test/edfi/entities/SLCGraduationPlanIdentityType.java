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
 * New SLC identity type for GraduationPlan. Contains keyfields: GraduationPlanType and an embedded EducationalOrgReference.
 * 
 * <p>Java class for SLC-GraduationPlanIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-GraduationPlanIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GraduationPlanType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EducationalOrgReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-GraduationPlanIdentityType", propOrder = {
    "graduationPlanType",
    "educationalOrgReference"
})
public class SLCGraduationPlanIdentityType {

    @XmlElement(name = "GraduationPlanType")
    protected String graduationPlanType;
    @XmlElement(name = "EducationalOrgReference")
    protected SLCEducationalOrgReferenceType educationalOrgReference;

    /**
     * Gets the value of the graduationPlanType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraduationPlanType() {
        return graduationPlanType;
    }

    /**
     * Sets the value of the graduationPlanType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraduationPlanType(String value) {
        this.graduationPlanType = value;
    }

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

}
