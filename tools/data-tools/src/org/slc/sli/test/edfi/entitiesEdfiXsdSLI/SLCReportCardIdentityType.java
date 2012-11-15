//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SLC-ReportCardIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-ReportCardIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}SLC-StudentReferenceType"/>
 *         &lt;element name="GradingPeriodReference" type="{http://ed-fi.org/0100}SLC-GradingPeriodReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-ReportCardIdentityType", propOrder = {
    "studentReference",
    "gradingPeriodReference"
})
public class SLCReportCardIdentityType {

    @XmlElement(name = "StudentReference", required = true)
    protected SLCStudentReferenceType studentReference;
    @XmlElement(name = "GradingPeriodReference")
    protected SLCGradingPeriodReferenceType gradingPeriodReference;

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
     * Gets the value of the gradingPeriodReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCGradingPeriodReferenceType }
     *     
     */
    public SLCGradingPeriodReferenceType getGradingPeriodReference() {
        return gradingPeriodReference;
    }

    /**
     * Sets the value of the gradingPeriodReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCGradingPeriodReferenceType }
     *     
     */
    public void setGradingPeriodReference(SLCGradingPeriodReferenceType value) {
        this.gradingPeriodReference = value;
    }

}
