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
 * Reflects important characteristics of the student's home situation: Displaced Homemaker, Homeless, Immigrant, Migratory, Military Parent, Pregnant Teen, Single Parent, Unaccompanied Youth		
 * 			
 * 
 * <p>Java class for StudentCharacteristic complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentCharacteristic">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Characteristic" type="{http://ed-fi.org/0100}StudentCharacteristicType"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="DesignatedBy" type="{http://ed-fi.org/0100}DesignatedBy" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentCharacteristic", propOrder = {
    "characteristic",
    "beginDate",
    "endDate",
    "designatedBy"
})
public class StudentCharacteristic {

    @XmlElement(name = "Characteristic", required = true)
    protected StudentCharacteristicType characteristic;
    @XmlElement(name = "BeginDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String beginDate;
    @XmlElement(name = "EndDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String endDate;
    @XmlElement(name = "DesignatedBy")
    protected String designatedBy;

    /**
     * Gets the value of the characteristic property.
     * 
     * @return
     *     possible object is
     *     {@link StudentCharacteristicType }
     *     
     */
    public StudentCharacteristicType getCharacteristic() {
        return characteristic;
    }

    /**
     * Sets the value of the characteristic property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentCharacteristicType }
     *     
     */
    public void setCharacteristic(StudentCharacteristicType value) {
        this.characteristic = value;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginDate(String value) {
        this.beginDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the designatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDesignatedBy() {
        return designatedBy;
    }

    /**
     * Sets the value of the designatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesignatedBy(String value) {
        this.designatedBy = value;
    }

}
