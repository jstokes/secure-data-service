//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.30 at 01:48:06 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The 10-digit telephone number, including the area code, for the organization
 * 
 * <p>Java class for InstitutionTelephone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InstitutionTelephone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TelephoneNumber" type="{http://ed-fi.org/0100}TelephoneNumber"/>
 *       &lt;/sequence>
 *       &lt;attribute name="InstitutionTelephoneNumberType" type="{http://ed-fi.org/0100}InstitutionTelephoneNumberType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstitutionTelephone", propOrder = {
    "telephoneNumber"
})
public class InstitutionTelephone {

    @XmlElement(name = "TelephoneNumber", required = true)
    protected String telephoneNumber;
    @XmlAttribute(name = "InstitutionTelephoneNumberType")
    protected InstitutionTelephoneNumberType institutionTelephoneNumberType;

    /**
     * Gets the value of the telephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the value of the telephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber(String value) {
        this.telephoneNumber = value;
    }

    /**
     * Gets the value of the institutionTelephoneNumberType property.
     * 
     * @return
     *     possible object is
     *     {@link InstitutionTelephoneNumberType }
     *     
     */
    public InstitutionTelephoneNumberType getInstitutionTelephoneNumberType() {
        return institutionTelephoneNumberType;
    }

    /**
     * Sets the value of the institutionTelephoneNumberType property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstitutionTelephoneNumberType }
     *     
     */
    public void setInstitutionTelephoneNumberType(InstitutionTelephoneNumberType value) {
        this.institutionTelephoneNumberType = value;
    }

}
