//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Changed to contain only Date and an embedded EducationOrgReference.
 * 
 * <p>Java class for SLC-CalendarDateIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-CalendarDateIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EducationOrgReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-CalendarDateIdentityType", propOrder = {
    "date",
    "educationOrgReference"
})
public class SLCCalendarDateIdentityType {

    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "EducationOrgReference", required = true)
    protected SLCEducationalOrgReferenceType educationOrgReference;

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the educationOrgReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getEducationOrgReference() {
        return educationOrgReference;
    }

    /**
     * Sets the value of the educationOrgReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setEducationOrgReference(SLCEducationalOrgReferenceType value) {
        this.educationOrgReference = value;
    }

}
