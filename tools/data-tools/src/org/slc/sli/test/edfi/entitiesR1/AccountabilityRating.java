//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * An accountability rating for a school or district.
 * 			
 * 
 * <p>Java class for accountabilityRating complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountabilityRating">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ratingTitle" type="{http://slc-sli/ed-org/0.1}ratingTitleType"/>
 *         &lt;element name="rating" type="{http://slc-sli/ed-org/0.1}rating"/>
 *         &lt;element name="ratingDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="SchoolYear" type="{http://slc-sli/ed-org/0.1}schoolYearType"/>
 *         &lt;element name="ratingOrganization" type="{http://slc-sli/ed-org/0.1}ratingOrganization" minOccurs="0"/>
 *         &lt;element name="ratingProgram" type="{http://slc-sli/ed-org/0.1}ratingProgramType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accountabilityRating", propOrder = {
    "ratingTitle",
    "rating",
    "ratingDate",
    "schoolYear",
    "ratingOrganization",
    "ratingProgram"
})
public class AccountabilityRating {

    @XmlElement(required = true)
    protected String ratingTitle;
    @XmlElement(required = true)
    protected String rating;
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String ratingDate;
    @XmlElement(name = "SchoolYear", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String schoolYear;
    protected String ratingOrganization;
    protected String ratingProgram;

    /**
     * Gets the value of the ratingTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatingTitle() {
        return ratingTitle;
    }

    /**
     * Sets the value of the ratingTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatingTitle(String value) {
        this.ratingTitle = value;
    }

    /**
     * Gets the value of the rating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRating(String value) {
        this.rating = value;
    }

    /**
     * Gets the value of the ratingDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatingDate() {
        return ratingDate;
    }

    /**
     * Sets the value of the ratingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatingDate(String value) {
        this.ratingDate = value;
    }

    /**
     * Gets the value of the schoolYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchoolYear() {
        return schoolYear;
    }

    /**
     * Sets the value of the schoolYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchoolYear(String value) {
        this.schoolYear = value;
    }

    /**
     * Gets the value of the ratingOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatingOrganization() {
        return ratingOrganization;
    }

    /**
     * Sets the value of the ratingOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatingOrganization(String value) {
        this.ratingOrganization = value;
    }

    /**
     * Gets the value of the ratingProgram property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatingProgram() {
        return ratingProgram;
    }

    /**
     * Sets the value of the ratingProgram property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatingProgram(String value) {
        this.ratingProgram = value;
    }

}
