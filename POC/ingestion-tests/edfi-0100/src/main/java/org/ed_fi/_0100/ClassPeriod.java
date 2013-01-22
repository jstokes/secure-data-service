//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents the designation of regularly scheduled series of class meetings at designated times and days of the week.
 * 
 * 
 * <p>Java class for ClassPeriod complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassPeriod">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="ClassPeriodName" type="{http://ed-fi.org/0100}ClassPeriodNameType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassPeriod", propOrder = {
    "classPeriodName"
})
public class ClassPeriod
    extends ComplexObjectType
{

    @XmlElement(name = "ClassPeriodName", required = true)
    protected String classPeriodName;

    /**
     * Gets the value of the classPeriodName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassPeriodName() {
        return classPeriodName;
    }

    /**
     * Sets the value of the classPeriodName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassPeriodName(String value) {
        this.classPeriodName = value;
    }

}
