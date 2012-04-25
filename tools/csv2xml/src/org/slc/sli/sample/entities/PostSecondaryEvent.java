//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity captures significant postsecondary events during a student's high school tenure (e.g., FASFSA application, or college application, acceptance, and enrollment).
 * 
 * <p>Java class for PostSecondaryEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PostSecondaryEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="EventDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="PostSecondaryEventCategory" type="{http://ed-fi.org/0100}PostSecondaryEventCategoryType"/>
 *         &lt;element name="NameOfInstitution" type="{http://ed-fi.org/0100}NameOfInstitution" minOccurs="0"/>
 *         &lt;element name="InstitutionId" type="{http://ed-fi.org/0100}InstitutionId" minOccurs="0"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PostSecondaryEvent", propOrder = {
    "eventDate",
    "postSecondaryEventCategory",
    "nameOfInstitution",
    "institutionId",
    "studentReference"
})
public class PostSecondaryEvent
    extends ComplexObjectType
{

    @XmlElement(name = "EventDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar eventDate;
    @XmlElement(name = "PostSecondaryEventCategory", required = true)
    protected PostSecondaryEventCategoryType postSecondaryEventCategory;
    @XmlElement(name = "NameOfInstitution")
    protected String nameOfInstitution;
    @XmlElement(name = "InstitutionId")
    protected String institutionId;
    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDate(Calendar value) {
        this.eventDate = value;
    }

    /**
     * Gets the value of the postSecondaryEventCategory property.
     * 
     * @return
     *     possible object is
     *     {@link PostSecondaryEventCategoryType }
     *     
     */
    public PostSecondaryEventCategoryType getPostSecondaryEventCategory() {
        return postSecondaryEventCategory;
    }

    /**
     * Sets the value of the postSecondaryEventCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostSecondaryEventCategoryType }
     *     
     */
    public void setPostSecondaryEventCategory(PostSecondaryEventCategoryType value) {
        this.postSecondaryEventCategory = value;
    }

    /**
     * Gets the value of the nameOfInstitution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfInstitution() {
        return nameOfInstitution;
    }

    /**
     * Sets the value of the nameOfInstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfInstitution(String value) {
        this.nameOfInstitution = value;
    }

    /**
     * Gets the value of the institutionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitutionId() {
        return institutionId;
    }

    /**
     * Sets the value of the institutionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitutionId(String value) {
        this.institutionId = value;
    }

    /**
     * Gets the value of the studentReference property.
     * 
     * @return
     *     possible object is
     *     {@link StudentReferenceType }
     *     
     */
    public StudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentReferenceType }
     *     
     */
    public void setStudentReference(StudentReferenceType value) {
        this.studentReference = value;
    }

}
