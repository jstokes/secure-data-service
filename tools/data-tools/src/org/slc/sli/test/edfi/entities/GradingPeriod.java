//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents the time span for which grades are reported.
 * 
 * <p>Java class for GradingPeriod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GradingPeriod">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="GradingPeriod" type="{http://ed-fi.org/0100}GradingPeriodType"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="TotalInstructionalDays">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CalendarDateReference" type="{http://ed-fi.org/0100}ReferenceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GradingPeriod", propOrder = {
    "gradingPeriod",
    "beginDate",
    "endDate",
    "totalInstructionalDays",
    "calendarDateReference"
})
public class GradingPeriod
    extends ComplexObjectType
{

    @XmlElement(name = "GradingPeriod", required = true)
    protected GradingPeriodType gradingPeriod;
    @XmlElement(name = "BeginDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar beginDate;
    @XmlElement(name = "EndDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar endDate;
    @XmlElement(name = "TotalInstructionalDays")
    protected int totalInstructionalDays;
    @XmlElement(name = "CalendarDateReference", required = true)
    protected List<ReferenceType> calendarDateReference;

    /**
     * Gets the value of the gradingPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link GradingPeriodType }
     *     
     */
    public GradingPeriodType getGradingPeriod() {
        return gradingPeriod;
    }

    /**
     * Sets the value of the gradingPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradingPeriodType }
     *     
     */
    public void setGradingPeriod(GradingPeriodType value) {
        this.gradingPeriod = value;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginDate(Calendar value) {
        this.beginDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(Calendar value) {
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
     * {@link ReferenceType }
     * 
     * 
     */
    public List<ReferenceType> getCalendarDateReference() {
        if (calendarDateReference == null) {
            calendarDateReference = new ArrayList<ReferenceType>();
        }
        return this.calendarDateReference;
    }

}
