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
 * Provides references for account code descriptors during interchange. Use XML IDREF to reference a course record that is included in the interchange.  To lookup when already loaded, specify either CodeValue OR Description OR ShortDescription plus the account code type (fund, function, etc.).
 * 
 * <p>Java class for AccountCodeDescriptorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountCodeDescriptorType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="AccountCodeType" type="{http://ed-fi.org/0100}AccountCodeType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="CodeValue" type="{http://ed-fi.org/0100}CodeValue"/>
 *           &lt;element name="ShortDescription" type="{http://ed-fi.org/0100}ShortDescription"/>
 *           &lt;element name="Description" type="{http://ed-fi.org/0100}Description"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountCodeDescriptorType", propOrder = {
    "accountCodeType",
    "codeValue",
    "shortDescription",
    "description"
})
public class AccountCodeDescriptorType
    extends ReferenceType
{

    @XmlElement(name = "AccountCodeType")
    protected String accountCodeType;
    @XmlElement(name = "CodeValue")
    protected String codeValue;
    @XmlElement(name = "ShortDescription")
    protected String shortDescription;
    @XmlElement(name = "Description")
    protected String description;

    /**
     * Gets the value of the accountCodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountCodeType() {
        return accountCodeType;
    }

    /**
     * Sets the value of the accountCodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountCodeType(String value) {
        this.accountCodeType = value;
    }

    /**
     * Gets the value of the codeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * Sets the value of the codeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeValue(String value) {
        this.codeValue = value;
    }

    /**
     * Gets the value of the shortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
