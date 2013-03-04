//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 This entity represents the time span for which
 *                 grades are reported, extended off GradingPeriodSimple
 *                 to include calendarDates.
 *             
 * 
 * <p>Java class for gradingPeriod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gradingPeriod">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gradingPeriodSimple" type="{http://slc-sli/ed-org/0.1}gradingPeriodSimple"/>
 *         &lt;element name="calendarDateReference" type="{http://slc-sli/ed-org/0.1}reference" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gradingPeriod", propOrder = {
    "gradingPeriodSimple",
    "calendarDateReference"
})
public class GradingPeriod {

    @XmlElement(required = true)
    protected GradingPeriodSimple gradingPeriodSimple;
    protected List<String> calendarDateReference;

    /**
     * Gets the value of the gradingPeriodSimple property.
     * 
     * @return
     *     possible object is
     *     {@link GradingPeriodSimple }
     *     
     */
    public GradingPeriodSimple getGradingPeriodSimple() {
        return gradingPeriodSimple;
    }

    /**
     * Sets the value of the gradingPeriodSimple property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradingPeriodSimple }
     *     
     */
    public void setGradingPeriodSimple(GradingPeriodSimple value) {
        this.gradingPeriodSimple = value;
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
     * {@link String }
     * 
     * 
     */
    public List<String> getCalendarDateReference() {
        if (calendarDateReference == null) {
            calendarDateReference = new ArrayList<String>();
        }
        return this.calendarDateReference;
    }

}
