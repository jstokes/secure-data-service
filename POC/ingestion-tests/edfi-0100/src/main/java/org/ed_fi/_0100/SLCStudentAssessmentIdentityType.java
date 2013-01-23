//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * New SLC identity type for StudentAssessment. Contains keyfields: AdministrationDate, an embedded StudentReference and an embedded AssessmentReference.
 * 
 * <p>Java class for SLC-StudentAssessmentIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentAssessmentIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AdministrationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *         &lt;element name="AssessmentReference" type="{http://ed-fi.org/0100}SLC-AssessmentReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentAssessmentIdentityType", propOrder = {
    "administrationDate",
    "studentReference",
    "assessmentReference"
})
public class SLCStudentAssessmentIdentityType {

    @XmlElement(name = "AdministrationDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar administrationDate;
    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;
    @XmlElement(name = "AssessmentReference", required = true)
    protected SLCAssessmentReferenceType assessmentReference;

    /**
     * Gets the value of the administrationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAdministrationDate() {
        return administrationDate;
    }

    /**
     * Sets the value of the administrationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAdministrationDate(XMLGregorianCalendar value) {
        this.administrationDate = value;
    }

    /**
     * Gets the value of the studentReference property.
     * 
     * @return
     *     possible object is
     *     {@link StudentReferenceType }
     *     
     */
    public StudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentReferenceType }
     *     
     */
    public void setStudentReference(StudentReferenceType value) {
        this.studentReference = value;
    }

    /**
     * Gets the value of the assessmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCAssessmentReferenceType }
     *     
     */
    public SLCAssessmentReferenceType getAssessmentReference() {
        return assessmentReference;
    }

    /**
     * Sets the value of the assessmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCAssessmentReferenceType }
     *     
     */
    public void setAssessmentReference(SLCAssessmentReferenceType value) {
        this.assessmentReference = value;
    }

}
