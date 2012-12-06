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
 * Academic distinctions earned by or awarded to the student
 * 
 * <p>Java class for AcademicHonor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AcademicHonor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AcademicHonorsType" type="{http://ed-fi.org/0100}AcademicHonorsType"/>
 *         &lt;element name="HonorsDescription" type="{http://ed-fi.org/0100}HonorsDescription" minOccurs="0"/>
 *         &lt;element name="HonorAwardDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AcademicHonor", propOrder = {
    "academicHonorsType",
    "honorsDescription",
    "honorAwardDate"
})
public class AcademicHonor {

    @XmlElement(name = "AcademicHonorsType", required = true)
    protected AcademicHonorsType academicHonorsType;
    @XmlElement(name = "HonorsDescription")
    protected String honorsDescription;
    @XmlElement(name = "HonorAwardDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String honorAwardDate;

    /**
     * Gets the value of the academicHonorsType property.
     * 
     * @return
     *     possible object is
     *     {@link AcademicHonorsType }
     *     
     */
    public AcademicHonorsType getAcademicHonorsType() {
        return academicHonorsType;
    }

    /**
     * Sets the value of the academicHonorsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcademicHonorsType }
     *     
     */
    public void setAcademicHonorsType(AcademicHonorsType value) {
        this.academicHonorsType = value;
    }

    /**
     * Gets the value of the honorsDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHonorsDescription() {
        return honorsDescription;
    }

    /**
     * Sets the value of the honorsDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHonorsDescription(String value) {
        this.honorsDescription = value;
    }

    /**
     * Gets the value of the honorAwardDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHonorAwardDate() {
        return honorAwardDate;
    }

    /**
     * Sets the value of the honorAwardDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHonorAwardDate(String value) {
        this.honorAwardDate = value;
    }

}
