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
 * Academic distinctions earned by or awarded to the
 * 				student
 * 			
 * 
 * <p>Java class for academicHonor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="academicHonor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="academicHonorsType" type="{}academicHonorsType"/>
 *         &lt;element name="honorsDescription" type="{}honorsDescription" minOccurs="0"/>
 *         &lt;element name="honorAwardDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "academicHonor", propOrder = {
    "academicHonorsType",
    "honorsDescription",
    "honorAwardDate"
})
public class AcademicHonor {

    @XmlElement(required = true)
    protected AcademicHonorsType academicHonorsType;
    protected String honorsDescription;
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
