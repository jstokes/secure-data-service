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
import javax.xml.bind.annotation.XmlType;


/**
 * Changed to use a required SLC identity type.
 * 
 * <p>Java class for SLC-CourseReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-CourseReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CourseIdentity" type="{http://ed-fi.org/0100}SLC-CourseIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-CourseReferenceType", propOrder = {
    "courseIdentity"
})
public class SLCCourseReferenceType {

    @XmlElement(name = "CourseIdentity", required = true)
    protected SLCCourseIdentityType courseIdentity;

    /**
     * Gets the value of the courseIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCCourseIdentityType }
     *     
     */
    public SLCCourseIdentityType getCourseIdentity() {
        return courseIdentity;
    }

    /**
     * Sets the value of the courseIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCCourseIdentityType }
     *     
     */
    public void setCourseIdentity(SLCCourseIdentityType value) {
        this.courseIdentity = value;
    }

}
