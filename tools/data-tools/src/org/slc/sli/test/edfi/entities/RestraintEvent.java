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
 * This event entity represents the instances where a special education student was physically or mechanically restrained due to imminent serious physical harm to themselves or others, imminent serious property destruction, or a combination of both imminent serious physical harm to themselves or others and imminent serious property destruction.
 * 
 * 
 * <p>Java class for RestraintEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RestraintEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="RestraintEventIdentifier" type="{http://ed-fi.org/0100}RestraintEventIdentifier"/>
 *         &lt;element name="EventDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EducationalEnvironment" type="{http://ed-fi.org/0100}EducationalEnvironmentType" minOccurs="0"/>
 *         &lt;element name="RestraintEventReasons" type="{http://ed-fi.org/0100}RestraintEventReasonsType"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *         &lt;element name="ProgramReference" type="{http://ed-fi.org/0100}ProgramReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RestraintEvent", propOrder = {
    "restraintEventIdentifier",
    "eventDate",
    "educationalEnvironment",
    "restraintEventReasons",
    "studentReference",
    "programReference",
    "schoolReference"
})
public class RestraintEvent
    extends ComplexObjectType
{

    @XmlElement(name = "RestraintEventIdentifier", required = true)
    protected String restraintEventIdentifier;
    @XmlElement(name = "EventDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar eventDate;
    @XmlElement(name = "EducationalEnvironment")
    protected EducationalEnvironmentType educationalEnvironment;
    @XmlElement(name = "RestraintEventReasons", required = true)
    protected RestraintEventReasonsType restraintEventReasons;
    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;
    @XmlElement(name = "ProgramReference")
    protected List<ProgramReferenceType> programReference;
    @XmlElement(name = "SchoolReference", required = true)
    protected EducationalOrgReferenceType schoolReference;

    /**
     * Gets the value of the restraintEventIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestraintEventIdentifier() {
        return restraintEventIdentifier;
    }

    /**
     * Sets the value of the restraintEventIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestraintEventIdentifier(String value) {
        this.restraintEventIdentifier = value;
    }

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
     * Gets the value of the restraintEventReasons property.
     * 
     * @return
     *     possible object is
     *     {@link RestraintEventReasonsType }
     *     
     */
    public RestraintEventReasonsType getRestraintEventReasons() {
        return restraintEventReasons;
    }

    /**
     * Sets the value of the restraintEventReasons property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestraintEventReasonsType }
     *     
     */
    public void setRestraintEventReasons(RestraintEventReasonsType value) {
        this.restraintEventReasons = value;
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

    /**
     * Gets the value of the programReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProgramReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProgramReferenceType }
     * 
     * 
     */
    public List<ProgramReferenceType> getProgramReference() {
        if (programReference == null) {
            programReference = new ArrayList<ProgramReferenceType>();
        }
        return this.programReference;
    }

    /**
     * Gets the value of the schoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getSchoolReference() {
        return schoolReference;
    }

    /**
     * Sets the value of the schoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setSchoolReference(EducationalOrgReferenceType value) {
        this.schoolReference = value;
    }

}
