//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.02.12 at 04:54:37 PM EST
//


package org.slc.sli.test.edfi.entities;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents the set of elements defining the meeting time for a class period for a bell schedule.
 *
 * <p>Java class for MeetingTime complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MeetingTime">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="ClassPeriodReference" type="{http://ed-fi.org/0100}ClassPeriodReferenceType"/>
 *         &lt;element name="WeekNumber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="52"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MeetingDays" type="{http://ed-fi.org/0100}MeetingDaysType"/>
 *         &lt;element name="AlternateDayName" type="{http://ed-fi.org/0100}AlternateDayName" minOccurs="0"/>
 *         &lt;element name="StartTime" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="EndTime" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="OfficialAttendancePeriod" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeetingTime", propOrder = {
    "classPeriodReference",
    "weekNumber",
    "meetingDays",
    "alternateDayName",
    "startTime",
    "endTime",
    "officialAttendancePeriod"
})
public class MeetingTime
    extends ComplexObjectType
{

    @XmlElement(name = "ClassPeriodReference", required = true)
    protected ClassPeriodReferenceType classPeriodReference;
    @XmlElement(name = "WeekNumber")
    protected int weekNumber;
    @XmlElement(name = "MeetingDays", required = true)
    protected MeetingDaysType meetingDays;
    @XmlElement(name = "AlternateDayName")
    protected String alternateDayName;
    @XmlElement(name = "StartTime", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "time")
    protected Calendar startTime;
    @XmlElement(name = "EndTime", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "time")
    protected Calendar endTime;
    @XmlElement(name = "OfficialAttendancePeriod")
    protected Boolean officialAttendancePeriod;

    /**
     * Gets the value of the classPeriodReference property.
     *
     * @return
     *     possible object is
     *     {@link ClassPeriodReferenceType }
     *
     */
    public ClassPeriodReferenceType getClassPeriodReference() {
        return classPeriodReference;
    }

    /**
     * Sets the value of the classPeriodReference property.
     *
     * @param value
     *     allowed object is
     *     {@link ClassPeriodReferenceType }
     *
     */
    public void setClassPeriodReference(ClassPeriodReferenceType value) {
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

    /**
     * Gets the value of the meetingDays property.
     *
     * @return
     *     possible object is
     *     {@link MeetingDaysType }
     *
     */
    public MeetingDaysType getMeetingDays() {
        return meetingDays;
    }

    /**
     * Sets the value of the meetingDays property.
     *
     * @param value
     *     allowed object is
     *     {@link MeetingDaysType }
     *
     */
    public void setMeetingDays(MeetingDaysType value) {
        this.meetingDays = value;
    }

    /**
     * Gets the value of the alternateDayName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAlternateDayName() {
        return alternateDayName;
    }

    /**
     * Sets the value of the alternateDayName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAlternateDayName(String value) {
        this.alternateDayName = value;
    }

    /**
     * Gets the value of the startTime property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Calendar getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStartTime(Calendar value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the endTime property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Calendar getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEndTime(Calendar value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the officialAttendancePeriod property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isOfficialAttendancePeriod() {
        return officialAttendancePeriod;
    }

    /**
     * Sets the value of the officialAttendancePeriod property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setOfficialAttendancePeriod(Boolean value) {
        this.officialAttendancePeriod = value;
    }

}
