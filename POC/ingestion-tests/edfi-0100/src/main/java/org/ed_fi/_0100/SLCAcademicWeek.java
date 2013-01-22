//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Changed type of CalendarDateReference to SLC reference type.
 * 
 * <p>Java class for SLC-AcademicWeek complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-AcademicWeek">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="WeekIdentifier" type="{http://ed-fi.org/0100}WeekIdentifier"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="TotalInstructionalDays" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CalendarDateReference" type="{http://ed-fi.org/0100}SLC-CalendarDateReferenceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-AcademicWeek", propOrder = {
    "weekIdentifier",
    "beginDate",
    "endDate",
    "totalInstructionalDays",
    "calendarDateReference"
})
public class SLCAcademicWeek
    extends ComplexObjectType
{

    @XmlElement(name = "WeekIdentifier", required = true)
    protected String weekIdentifier;
    @XmlElement(name = "BeginDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar beginDate;
    @XmlElement(name = "EndDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "TotalInstructionalDays")
    protected int totalInstructionalDays;
    @XmlElement(name = "CalendarDateReference", required = true)
    protected List<SLCCalendarDateReferenceType> calendarDateReference;

    /**
     * Gets the value of the weekIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeekIdentifier() {
        return weekIdentifier;
    }

    /**
     * Sets the value of the weekIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeekIdentifier(String value) {
        this.weekIdentifier = value;
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

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the totalInstructionalDays property.
     * 
     */
    public int getTotalInstructionalDays() {
        return totalInstructionalDays;
    }

    /**
     * Sets the value of the totalInstructionalDays property.
     * 
     */
    public void setTotalInstructionalDays(int value) {
        this.totalInstructionalDays = value;
    }

    /**
     * Gets the value of the calendarDateReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the calendarDateReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCalendarDateReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCCalendarDateReferenceType }
     * 
     * 
     */
    public List<SLCCalendarDateReferenceType> getCalendarDateReference() {
        if (calendarDateReference == null) {
            calendarDateReference = new ArrayList<SLCCalendarDateReferenceType>();
        }
        return this.calendarDateReference;
    }

}
