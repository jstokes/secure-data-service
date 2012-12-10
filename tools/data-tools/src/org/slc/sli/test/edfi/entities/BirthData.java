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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The set of elements that capture relevant data regarding a person's birth, including birth date and place of birth.
 * 
 * <p>Java class for BirthData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BirthData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BirthDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="CityOfBirth" type="{http://ed-fi.org/0100}City" minOccurs="0"/>
 *         &lt;element name="StateOfBirthAbbreviation" type="{http://ed-fi.org/0100}StateAbbreviationType" minOccurs="0"/>
 *         &lt;element name="CountryOfBirthCode" type="{http://ed-fi.org/0100}CountryCodeType" minOccurs="0"/>
 *         &lt;element name="DateEnteredUS" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="MultipleBirthStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BirthData", propOrder = {
    "birthDate",
    "cityOfBirth",
    "stateOfBirthAbbreviation",
    "countryOfBirthCode",
    "dateEnteredUS",
    "multipleBirthStatus"
})
public class BirthData {

    @XmlElement(name = "BirthDate", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String birthDate;
    @XmlElement(name = "CityOfBirth")
    protected String cityOfBirth;
    @XmlElement(name = "StateOfBirthAbbreviation")
    protected StateAbbreviationType stateOfBirthAbbreviation;
    @XmlElement(name = "CountryOfBirthCode")
    protected CountryCodeType countryOfBirthCode;
    @XmlElement(name = "DateEnteredUS")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String dateEnteredUS;
    @XmlElement(name = "MultipleBirthStatus")
    protected Boolean multipleBirthStatus;

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(String value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the cityOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityOfBirth() {
        return cityOfBirth;
    }

    /**
     * Sets the value of the cityOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityOfBirth(String value) {
        this.cityOfBirth = value;
    }

    /**
     * Gets the value of the stateOfBirthAbbreviation property.
     * 
     * @return
     *     possible object is
     *     {@link StateAbbreviationType }
     *     
     */
    public StateAbbreviationType getStateOfBirthAbbreviation() {
        return stateOfBirthAbbreviation;
    }

    /**
     * Sets the value of the stateOfBirthAbbreviation property.
     * 
     * @param value
     *     allowed object is
     *     {@link StateAbbreviationType }
     *     
     */
    public void setStateOfBirthAbbreviation(StateAbbreviationType value) {
        this.stateOfBirthAbbreviation = value;
    }

    /**
     * Gets the value of the countryOfBirthCode property.
     * 
     * @return
     *     possible object is
     *     {@link CountryCodeType }
     *     
     */
    public CountryCodeType getCountryOfBirthCode() {
        return countryOfBirthCode;
    }

    /**
     * Sets the value of the countryOfBirthCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCodeType }
     *     
     */
    public void setCountryOfBirthCode(CountryCodeType value) {
        this.countryOfBirthCode = value;
    }

    /**
     * Gets the value of the dateEnteredUS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateEnteredUS() {
        return dateEnteredUS;
    }

    /**
     * Sets the value of the dateEnteredUS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateEnteredUS(String value) {
        this.dateEnteredUS = value;
    }

    /**
     * Gets the value of the multipleBirthStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMultipleBirthStatus() {
        return multipleBirthStatus;
    }

    /**
     * Sets the value of the multipleBirthStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMultipleBirthStatus(Boolean value) {
        this.multipleBirthStatus = value;
    }

}
