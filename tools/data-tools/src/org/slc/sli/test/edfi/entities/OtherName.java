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
 * Other names (e.g., alias, nickname, previous legal name) associated with a person.
 * 
 * <p>Java class for OtherName complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OtherName">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PersonalTitlePrefix" type="{http://ed-fi.org/0100}PersonalTitlePrefixType" minOccurs="0"/>
 *         &lt;element name="FirstName" type="{http://ed-fi.org/0100}FirstName"/>
 *         &lt;element name="MiddleName" type="{http://ed-fi.org/0100}MiddleName" minOccurs="0"/>
 *         &lt;element name="LastSurname" type="{http://ed-fi.org/0100}LastSurname"/>
 *         &lt;element name="GenerationCodeSuffix" type="{http://ed-fi.org/0100}GenerationCodeSuffixType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="OtherNameType" use="required" type="{http://ed-fi.org/0100}OtherNameType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtherName", propOrder = {
    "personalTitlePrefix",
    "firstName",
    "middleName",
    "lastSurname",
    "generationCodeSuffix"
})
public class OtherName {

    @XmlElement(name = "PersonalTitlePrefix")
    protected PersonalTitlePrefixType personalTitlePrefix;
    @XmlElement(name = "FirstName", required = true)
    protected String firstName;
    @XmlElement(name = "MiddleName")
    protected String middleName;
    @XmlElement(name = "LastSurname", required = true)
    protected String lastSurname;
    @XmlElement(name = "GenerationCodeSuffix")
    protected GenerationCodeSuffixType generationCodeSuffix;
    @XmlAttribute(name = "OtherNameType", required = true)
    protected OtherNameType otherNameType;

    /**
     * Gets the value of the personalTitlePrefix property.
     * 
     * @return
     *     possible object is
     *     {@link PersonalTitlePrefixType }
     *     
     */
    public PersonalTitlePrefixType getPersonalTitlePrefix() {
        return personalTitlePrefix;
    }

    /**
     * Sets the value of the personalTitlePrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonalTitlePrefixType }
     *     
     */
    public void setPersonalTitlePrefix(PersonalTitlePrefixType value) {
        this.personalTitlePrefix = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the lastSurname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastSurname() {
        return lastSurname;
    }

    /**
     * Sets the value of the lastSurname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastSurname(String value) {
        this.lastSurname = value;
    }

    /**
     * Gets the value of the generationCodeSuffix property.
     * 
     * @return
     *     possible object is
     *     {@link GenerationCodeSuffixType }
     *     
     */
    public GenerationCodeSuffixType getGenerationCodeSuffix() {
        return generationCodeSuffix;
    }

    /**
     * Sets the value of the generationCodeSuffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenerationCodeSuffixType }
     *     
     */
    public void setGenerationCodeSuffix(GenerationCodeSuffixType value) {
        this.generationCodeSuffix = value;
    }

    /**
     * Gets the value of the otherNameType property.
     * 
     * @return
     *     possible object is
     *     {@link OtherNameType }
     *     
     */
    public OtherNameType getOtherNameType() {
        return otherNameType;
    }

    /**
     * Sets the value of the otherNameType property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherNameType }
     *     
     */
    public void setOtherNameType(OtherNameType value) {
        this.otherNameType = value;
    }

}
