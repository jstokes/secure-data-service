//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents an offering of a course by school during a session, representing the course catalog of available courses.
 * 
 * <p>Java class for CourseOffering complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CourseOffering">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="LocalCourseCode" type="{http://ed-fi.org/0100}LocalCourseCode"/>
 *         &lt;element name="LocalCourseTitle" type="{http://ed-fi.org/0100}CourseTitle" minOccurs="0"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="SessionReference" type="{http://ed-fi.org/0100}SessionReferenceType"/>
 *         &lt;element name="CourseReference" type="{http://ed-fi.org/0100}CourseReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourseOffering", propOrder = {
    "localCourseCode",
    "localCourseTitle",
    "schoolReference",
    "sessionReference",
    "courseReference"
})
public class CourseOffering
    extends ComplexObjectType
{

    @XmlElement(name = "LocalCourseCode", required = true)
    protected String localCourseCode;
    @XmlElement(name = "LocalCourseTitle")
    protected String localCourseTitle;
    @XmlElement(name = "SchoolReference", required = true)
    protected EducationalOrgReferenceType schoolReference;
    @XmlElement(name = "SessionReference", required = true)
    protected SessionReferenceType sessionReference;
    @XmlElement(name = "CourseReference", required = true)
    protected CourseReferenceType courseReference;

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
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getSchoolReference() {
        return schoolReference;
    }

    /**
     * Sets the value of the schoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setSchoolReference(EducationalOrgReferenceType value) {
        this.schoolReference = value;
    }

    /**
     * Gets the value of the sessionReference property.
     * 
     * @return
     *     possible object is
     *     {@link SessionReferenceType }
     *     
     */
    public SessionReferenceType getSessionReference() {
        return sessionReference;
    }

    /**
     * Sets the value of the sessionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SessionReferenceType }
     *     
     */
    public void setSessionReference(SessionReferenceType value) {
        this.sessionReference = value;
    }

    /**
     * Gets the value of the courseReference property.
     * 
     * @return
     *     possible object is
     *     {@link CourseReferenceType }
     *     
     */
    public CourseReferenceType getCourseReference() {
        return courseReference;
    }

    /**
     * Sets the value of the courseReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseReferenceType }
     *     
     */
    public void setCourseReference(CourseReferenceType value) {
        this.courseReference = value;
    }

}
