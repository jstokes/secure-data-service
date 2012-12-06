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
 * Changed to use embedded an StudentReference and SectionReference rather than IdentityTypes, and added required BeginDate.
 * 
 * <p>Java class for SLC-StudentSectionAssociationIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentSectionAssociationIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}SLC-StudentReferenceType"/>
 *         &lt;element name="SectionReference" type="{http://ed-fi.org/0100}SLC-SectionReferenceType"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentSectionAssociationIdentityType", propOrder = {
    "studentReference",
    "sectionReference",
    "beginDate"
})
public class SLCStudentSectionAssociationIdentityType {

    @XmlElement(name = "StudentReference", required = true)
    protected SLCStudentReferenceType studentReference;
    @XmlElement(name = "SectionReference", required = true)
    protected SLCSectionReferenceType sectionReference;
    @XmlElement(name = "BeginDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar beginDate;

    /**
     * Gets the value of the studentReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentReferenceType }
     *     
     */
    public SLCStudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentReferenceType }
     *     
     */
    public void setStudentReference(SLCStudentReferenceType value) {
        this.studentReference = value;
    }

    /**
     * Gets the value of the sectionReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCSectionReferenceType }
     *     
     */
    public SLCSectionReferenceType getSectionReference() {
        return sectionReference;
    }

    /**
     * Sets the value of the sectionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCSectionReferenceType }
     *     
     */
    public void setSectionReference(SLCSectionReferenceType value) {
        this.sectionReference = value;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBeginDate(XMLGregorianCalendar value) {
        this.beginDate = value;
    }

}
