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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;


/**
 * Encapsulates the possible attributes that can be used to lookup the student-section association - a composite of the student reference type and the section reference type.
 * 
 * <p>Java class for StudentSectionAssociationIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentSectionAssociationIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentIdentity" type="{http://ed-fi.org/0100}StudentIdentityType"/>
 *         &lt;element name="SectionIdentity" type="{http://ed-fi.org/0100}SectionIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentSectionAssociationIdentityType", propOrder = {
    "studentIdentity",
    "sectionIdentity"
})
public class StudentSectionAssociationIdentityType
    implements Locatable
{

    @XmlElement(name = "StudentIdentity", required = true)
    protected StudentIdentityType studentIdentity;
    @XmlElement(name = "SectionIdentity", required = true)
    protected SectionIdentityType sectionIdentity;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    /**
     * Gets the value of the studentIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link StudentIdentityType }
     *     
     */
    public StudentIdentityType getStudentIdentity() {
        return studentIdentity;
    }

    /**
     * Sets the value of the studentIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentIdentityType }
     *     
     */
    public void setStudentIdentity(StudentIdentityType value) {
        this.studentIdentity = value;
    }

    /**
     * Gets the value of the sectionIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SectionIdentityType }
     *     
     */
    public SectionIdentityType getSectionIdentity() {
        return sectionIdentity;
    }

    /**
     * Sets the value of the sectionIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SectionIdentityType }
     *     
     */
    public void setSectionIdentity(SectionIdentityType value) {
        this.sectionIdentity = value;
    }

    public Locator sourceLocation() {
        return locator;
    }

    public void setSourceLocation(Locator newLocator) {
        locator = newLocator;
    }

}
