//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * CourseOffering record with key fields: LocalCourseCode, SchoolReference and SessionReference.  Changed types of SchoolReference, SessionReference and CourseReference to SLC reference types.
 * 
 * <p>Java class for SLC-CourseOffering complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-CourseOffering">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="LocalCourseCode" type="{http://ed-fi.org/0100}LocalCourseCode"/>
 *         &lt;element name="LocalCourseTitle" type="{http://ed-fi.org/0100}CourseTitle" minOccurs="0"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *         &lt;element name="SessionReference" type="{http://ed-fi.org/0100}SLC-SessionReferenceType"/>
 *         &lt;element name="CourseReference" type="{http://ed-fi.org/0100}SLC-CourseReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-CourseOffering", propOrder = {
    "localCourseCode",
    "localCourseTitle",
    "schoolReference",
    "sessionReference",
    "courseReference"
})
@XmlRootElement public class SLCCourseOffering
    extends ComplexObjectType
{

    @XmlElement(name = "LocalCourseCode", required = true)
    protected String localCourseCode;
    @XmlElement(name = "LocalCourseTitle")
    protected String localCourseTitle;
    @XmlElement(name = "SchoolReference", required = true)
    protected SLCEducationalOrgReferenceType schoolReference;
    @XmlElement(name = "SessionReference", required = true)
    protected SLCSessionReferenceType sessionReference;
    @XmlElement(name = "CourseReference", required = true)
    protected SLCCourseReferenceType courseReference;

    /**
     * Gets the value of the localCourseCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalCourseCode() {
        return localCourseCode;
    }

    /**
     * Sets the value of the localCourseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalCourseCode(String value) {
        this.localCourseCode = value;
    }

    /**
     * Gets the value of the localCourseTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalCourseTitle() {
        return localCourseTitle;
    }

    /**
     * Sets the value of the localCourseTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalCourseTitle(String value) {
        this.localCourseTitle = value;
    }

    /**
     * Gets the value of the schoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getSchoolReference() {
        return schoolReference;
    }

    /**
     * Sets the value of the schoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setSchoolReference(SLCEducationalOrgReferenceType value) {
        this.schoolReference = value;
    }

    /**
     * Gets the value of the sessionReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCSessionReferenceType }
     *     
     */
    public SLCSessionReferenceType getSessionReference() {
        return sessionReference;
    }

    /**
     * Sets the value of the sessionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCSessionReferenceType }
     *     
     */
    public void setSessionReference(SLCSessionReferenceType value) {
        this.sessionReference = value;
    }

    /**
     * Gets the value of the courseReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCCourseReferenceType }
     *     
     */
    public SLCCourseReferenceType getCourseReference() {
        return courseReference;
    }

    /**
     * Sets the value of the courseReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCCourseReferenceType }
     *     
     */
    public void setCourseReference(SLCCourseReferenceType value) {
        this.courseReference = value;
    }

}
