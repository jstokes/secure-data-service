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
import javax.xml.bind.annotation.XmlType;


/**
 * New SLC identity type for MeetingTime.  Contains keyfields: embedded ClassPeriodReference and WeekNumber.
 * 
 * <p>Java class for SLC-MeetingTimeIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-MeetingTimeIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ClassPeriodReference" type="{http://ed-fi.org/0100}SLC-ClassPeriodReferenceType"/>
 *         &lt;element name="WeekNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-MeetingTimeIdentityType", propOrder = {
    "classPeriodReference",
    "weekNumber"
})
public class SLCMeetingTimeIdentityType {

    @XmlElement(name = "ClassPeriodReference", required = true)
    protected SLCClassPeriodReferenceType classPeriodReference;
    @XmlElement(name = "WeekNumber")
    protected int weekNumber;

    /**
     * Gets the value of the classPeriodReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCClassPeriodReferenceType }
     *     
     */
    public SLCClassPeriodReferenceType getClassPeriodReference() {
        return classPeriodReference;
    }

    /**
     * Sets the value of the classPeriodReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCClassPeriodReferenceType }
     *     
     */
    public void setClassPeriodReference(SLCClassPeriodReferenceType value) {
        this.classPeriodReference = value;
    }

    /**
     * Gets the value of the weekNumber property.
     * 
     */
    public int getWeekNumber() {
        return weekNumber;
    }

    /**
     * Sets the value of the weekNumber property.
     * 
     */
    public void setWeekNumber(int value) {
        this.weekNumber = value;
    }

}
