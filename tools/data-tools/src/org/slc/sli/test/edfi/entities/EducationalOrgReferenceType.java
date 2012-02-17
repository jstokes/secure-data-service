//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.02.12 at 04:54:37 PM EST
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides alternative references for Educational Organization (ESC, LEA, school) reference during interchange. Use XML IDREF to reference an educational organization record that is included in the interchange
 *
 * <p>Java class for EducationalOrgReferenceType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EducationalOrgReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="EducationalOrgIdentity" type="{http://ed-fi.org/0100}EducationalOrgIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EducationalOrgReferenceType", propOrder = {
    "educationalOrgIdentity"
})
public class EducationalOrgReferenceType
    extends ReferenceType
{

    @XmlElement(name = "EducationalOrgIdentity")
    protected EducationalOrgIdentityType educationalOrgIdentity;

    /**
     * Gets the value of the educationalOrgIdentity property.
     *
     * @return
     *     possible object is
     *     {@link EducationalOrgIdentityType }
     *
     */
    public EducationalOrgIdentityType getEducationalOrgIdentity() {
        return educationalOrgIdentity;
    }

    /**
     * Sets the value of the educationalOrgIdentity property.
     *
     * @param value
     *     allowed object is
     *     {@link EducationalOrgIdentityType }
     *
     */
    public void setEducationalOrgIdentity(EducationalOrgIdentityType value) {
        this.educationalOrgIdentity = value;
    }

}
