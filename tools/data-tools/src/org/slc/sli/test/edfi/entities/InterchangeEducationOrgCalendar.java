//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.30 at 01:48:06 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Session" type="{http://ed-fi.org/0100}Session"/>
 *         &lt;element name="GradingPeriod" type="{http://ed-fi.org/0100}GradingPeriod"/>
 *         &lt;element name="CalendarDate" type="{http://ed-fi.org/0100}CalendarDate"/>
 *         &lt;element name="AcademicWeek" type="{http://ed-fi.org/0100}AcademicWeek"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sessionOrGradingPeriodOrCalendarDate"
})
@XmlRootElement(name = "InterchangeEducationOrgCalendar")
public class InterchangeEducationOrgCalendar {

    @XmlElements({
        @XmlElement(name = "AcademicWeek", type = AcademicWeek.class),
        @XmlElement(name = "Session", type = Session.class),
        @XmlElement(name = "GradingPeriod", type = GradingPeriod.class),
        @XmlElement(name = "CalendarDate", type = CalendarDate.class)
    })
    protected List<ComplexObjectType> sessionOrGradingPeriodOrCalendarDate;

    /**
     * Gets the value of the sessionOrGradingPeriodOrCalendarDate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sessionOrGradingPeriodOrCalendarDate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSessionOrGradingPeriodOrCalendarDate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcademicWeek }
     * {@link Session }
     * {@link GradingPeriod }
     * {@link CalendarDate }
     * 
     * 
     */
    public List<ComplexObjectType> getSessionOrGradingPeriodOrCalendarDate() {
        if (sessionOrGradingPeriodOrCalendarDate == null) {
            sessionOrGradingPeriodOrCalendarDate = new ArrayList<ComplexObjectType>();
        }
        return this.sessionOrGradingPeriodOrCalendarDate;
    }

}
