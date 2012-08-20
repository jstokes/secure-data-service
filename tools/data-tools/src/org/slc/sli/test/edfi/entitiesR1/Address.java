//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 02:49:01 PM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The set of elements that describes an address,
 * 				including the street
 * 				address, city, state, and ZIP code.
 * 			
 * 
 * <p>Java class for address complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="address">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streetNumberName">
 *           &lt;simpleType>
 *             &lt;restriction base="{}streetNumberName">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="apartmentRoomSuiteNumber" type="{}apartmentRoomSuiteNumber" minOccurs="0"/>
 *         &lt;element name="buildingSiteNumber" type="{}buildingSiteNumber" minOccurs="0"/>
 *         &lt;element name="city" type="{}city"/>
 *         &lt;element name="stateAbbreviation" type="{}stateAbbreviationType"/>
 *         &lt;element name="postalCode" type="{}postalCode"/>
 *         &lt;element name="nameOfCounty" type="{}nameOfCounty" minOccurs="0"/>
 *         &lt;element name="countyFIPSCode" type="{}countyFIPSCode" minOccurs="0"/>
 *         &lt;element name="countryCode" type="{}countryCodeType" minOccurs="0"/>
 *         &lt;element name="latitude" type="{}coordinate" minOccurs="0"/>
 *         &lt;element name="longitude" type="{}coordinate" minOccurs="0"/>
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="closeDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="addressType" type="{}addressType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "address", propOrder = {
    "streetNumberName",
    "apartmentRoomSuiteNumber",
    "buildingSiteNumber",
    "city",
    "stateAbbreviation",
    "postalCode",
    "nameOfCounty",
    "countyFIPSCode",
    "countryCode",
    "latitude",
    "longitude",
    "openDate",
    "closeDate",
    "addressType"
})
public class Address {

    @XmlElement(required = true)
    protected String streetNumberName;
    protected String apartmentRoomSuiteNumber;
    protected String buildingSiteNumber;
    @XmlElement(required = true)
    protected String city;
    @XmlElement(required = true)
    protected StateAbbreviationType stateAbbreviation;
    @XmlElement(required = true)
    protected String postalCode;
    protected String nameOfCounty;
    protected String countyFIPSCode;
    protected CountryCodeType countryCode;
    protected String latitude;
    protected String longitude;
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String openDate;
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String closeDate;
    protected AddressType addressType;

    /**
     * Gets the value of the streetNumberName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreetNumberName() {
        return streetNumberName;
    }

    /**
     * Sets the value of the streetNumberName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreetNumberName(String value) {
        this.streetNumberName = value;
    }

    /**
     * Gets the value of the apartmentRoomSuiteNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApartmentRoomSuiteNumber() {
        return apartmentRoomSuiteNumber;
    }

    /**
     * Sets the value of the apartmentRoomSuiteNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApartmentRoomSuiteNumber(String value) {
        this.apartmentRoomSuiteNumber = value;
    }

    /**
     * Gets the value of the buildingSiteNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildingSiteNumber() {
        return buildingSiteNumber;
    }

    /**
     * Sets the value of the buildingSiteNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildingSiteNumber(String value) {
        this.buildingSiteNumber = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the stateAbbreviation property.
     * 
     * @return
     *     possible object is
     *     {@link StateAbbreviationType }
     *     
     */
    public StateAbbreviationType getStateAbbreviation() {
        return stateAbbreviation;
    }

    /**
     * Sets the value of the stateAbbreviation property.
     * 
     * @param value
     *     allowed object is
     *     {@link StateAbbreviationType }
     *     
     */
    public void setStateAbbreviation(StateAbbreviationType value) {
        this.stateAbbreviation = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the nameOfCounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfCounty() {
        return nameOfCounty;
    }

    /**
     * Sets the value of the nameOfCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfCounty(String value) {
        this.nameOfCounty = value;
    }

    /**
     * Gets the value of the countyFIPSCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountyFIPSCode() {
        return countyFIPSCode;
    }

    /**
     * Sets the value of the countyFIPSCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountyFIPSCode(String value) {
        this.countyFIPSCode = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link CountryCodeType }
     *     
     */
    public CountryCodeType getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCodeType }
     *     
     */
    public void setCountryCode(CountryCodeType value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatitude(String value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongitude(String value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the openDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenDate() {
        return openDate;
    }

    /**
     * Sets the value of the openDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenDate(String value) {
        this.openDate = value;
    }

    /**
     * Gets the value of the closeDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloseDate() {
        return closeDate;
    }

    /**
     * Sets the value of the closeDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloseDate(String value) {
        this.closeDate = value;
    }

    /**
     * Gets the value of the addressType property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getAddressType() {
        return addressType;
    }

    /**
     * Sets the value of the addressType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setAddressType(AddressType value) {
        this.addressType = value;
    }

}
