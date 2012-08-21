//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 02:49:01 PM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This event entity represents the instances where a
 * 				special education student was physically or mechanically restrained
 * 				due to imminent serious physical harm to themselves or others,
 * 				imminent serious property destruction, or a combination of both
 * 				imminent serious physical harm to themselves or others and imminent
 * 				serious property destruction.
 * 			
 * 
 * <p>Java class for restraintEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="restraintEvent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="restraintEventIdentifier" type="{}restraintEventIdentifier"/>
 *         &lt;element name="eventDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="educationalEnvironment" type="{}educationalEnvironmentType" minOccurs="0"/>
 *         &lt;element name="restraintEventReasons" type="{}restraintEventReasonsType"/>
 *         &lt;element name="studentReference" type="{}reference"/>
 *         &lt;element name="programReference" type="{}reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SchoolReference" type="{}reference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restraintEvent", propOrder = {
    "restraintEventIdentifier",
    "eventDate",
    "educationalEnvironment",
    "restraintEventReasons",
    "studentReference",
    "programReference",
    "schoolReference"
})
public class RestraintEvent {

    @XmlElement(required = true)
    protected String restraintEventIdentifier;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String eventDate;
    protected EducationalEnvironmentType educationalEnvironment;
    @XmlElement(required = true)
    protected RestraintEventReasonsType restraintEventReasons;
    @XmlElement(required = true)
    protected String studentReference;
    protected List<String> programReference;
    @XmlElement(name = "SchoolReference", required = true)
    protected String schoolReference;

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
    public String getEventDate() {
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
    public void setEventDate(String value) {
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
     *     {@link String }
     *     
     */
    public String getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentReference(String value) {
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
     * {@link String }
     * 
     * 
     */
    public List<String> getProgramReference() {
        if (programReference == null) {
            programReference = new ArrayList<String>();
        }
        return this.programReference;
    }

    /**
     * Gets the value of the schoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchoolReference() {
        return schoolReference;
    }

    /**
     * Sets the value of the schoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchoolReference(String value) {
        this.schoolReference = value;
    }

}
