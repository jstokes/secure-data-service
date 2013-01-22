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
 * AttendanceEvent record with key fields: StudentReference (StudentUniqueStateId), SchoolReference (StateOrganizationId) and EventDate. Made SchoolReference required. Changed types of StudentReference, SchoolReference, SessionReference and SectionReference to SLC reference types.
 * 
 * <p>Java class for SLC-AttendanceEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-AttendanceEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="EventDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="AttendanceEventType" type="{http://ed-fi.org/0100}AttendanceEventType" minOccurs="0"/>
 *         &lt;element name="AttendanceEventCategory" type="{http://ed-fi.org/0100}AttendanceEventCategoryType"/>
 *         &lt;element name="AttendanceEventReason" type="{http://ed-fi.org/0100}AttendanceEventReason" minOccurs="0"/>
 *         &lt;element name="EducationalEnvironment" type="{http://ed-fi.org/0100}EducationalEnvironmentType" minOccurs="0"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}SLC-StudentReferenceType"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *         &lt;element name="SessionReference" type="{http://ed-fi.org/0100}SLC-SessionReferenceType" minOccurs="0"/>
 *         &lt;element name="SectionReference" type="{http://ed-fi.org/0100}SLC-SectionReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-AttendanceEvent", propOrder = {
    "eventDate",
    "attendanceEventType",
    "attendanceEventCategory",
    "attendanceEventReason",
    "educationalEnvironment",
    "studentReference",
    "schoolReference",
    "sessionReference",
    "sectionReference"
})
public class SLCAttendanceEvent
    extends ComplexObjectType
{

    @XmlElement(name = "EventDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar eventDate;
    @XmlElement(name = "AttendanceEventType")
    protected AttendanceEventType attendanceEventType;
    @XmlElement(name = "AttendanceEventCategory", required = true)
    protected AttendanceEventCategoryType attendanceEventCategory;
    @XmlElement(name = "AttendanceEventReason")
    protected String attendanceEventReason;
    @XmlElement(name = "EducationalEnvironment")
    protected EducationalEnvironmentType educationalEnvironment;
    @XmlElement(name = "StudentReference", required = true)
    protected SLCStudentReferenceType studentReference;
    @XmlElement(name = "SchoolReference", required = true)
    protected SLCEducationalOrgReferenceType schoolReference;
    @XmlElement(name = "SessionReference")
    protected SLCSessionReferenceType sessionReference;
    @XmlElement(name = "SectionReference")
    protected SLCSectionReferenceType sectionReference;

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventDate(XMLGregorianCalendar value) {
        this.eventDate = value;
    }

    /**
     * Gets the value of the attendanceEventType property.
     * 
     * @return
     *     possible object is
     *     {@link AttendanceEventType }
     *     
     */
    public AttendanceEventType getAttendanceEventType() {
        return attendanceEventType;
    }

    /**
     * Sets the value of the attendanceEventType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttendanceEventType }
     *     
     */
    public void setAttendanceEventType(AttendanceEventType value) {
        this.attendanceEventType = value;
    }

    /**
     * Gets the value of the attendanceEventCategory property.
     * 
     * @return
     *     possible object is
     *     {@link AttendanceEventCategoryType }
     *     
     */
    public AttendanceEventCategoryType getAttendanceEventCategory() {
        return attendanceEventCategory;
    }

    /**
     * Sets the value of the attendanceEventCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttendanceEventCategoryType }
     *     
     */
    public void setAttendanceEventCategory(AttendanceEventCategoryType value) {
        this.attendanceEventCategory = value;
    }

    /**
     * Gets the value of the attendanceEventReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttendanceEventReason() {
        return attendanceEventReason;
    }

    /**
     * Sets the value of the attendanceEventReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttendanceEventReason(String value) {
        this.attendanceEventReason = value;
    }

    /**
     * Gets the value of the educationalEnvironment property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalEnvironmentType }
     *     
     */
    public EducationalEnvironmentType getEducationalEnvironment() {
        return educationalEnvironment;
    }

    /**
     * Sets the value of the educationalEnvironment property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalEnvironmentType }
     *     
     */
    public void setEducationalEnvironment(EducationalEnvironmentType value) {
        this.educationalEnvironment = value;
    }

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

}
