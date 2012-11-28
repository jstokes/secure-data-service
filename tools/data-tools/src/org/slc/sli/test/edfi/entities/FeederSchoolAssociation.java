//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The association from feeder school to the receiving school.
 * 
 * <p>Java class for FeederSchoolAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FeederSchoolAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FeederSchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="ReceivingSchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="FeederRelationshipDescription" type="{http://ed-fi.org/0100}Description" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FeederSchoolAssociation", propOrder = {
    "feederSchoolReference",
    "receivingSchoolReference",
    "beginDate",
    "endDate",
    "feederRelationshipDescription"
})
@XmlRootElement(name = "FeederSchoolAssociation")
public class FeederSchoolAssociation {

    @XmlElement(name = "FeederSchoolReference", required = true)
    protected EducationalOrgReferenceType feederSchoolReference;
    @XmlElement(name = "ReceivingSchoolReference", required = true)
    protected EducationalOrgReferenceType receivingSchoolReference;
    @XmlElement(name = "BeginDate", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String beginDate;
    @XmlElement(name = "EndDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String endDate;
    @XmlElement(name = "FeederRelationshipDescription")
    protected String feederRelationshipDescription;

    /**
     * Gets the value of the feederSchoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getFeederSchoolReference() {
        return feederSchoolReference;
    }

    /**
     * Sets the value of the feederSchoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setFeederSchoolReference(EducationalOrgReferenceType value) {
        this.feederSchoolReference = value;
    }

    /**
     * Gets the value of the receivingSchoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getReceivingSchoolReference() {
        return receivingSchoolReference;
    }

    /**
     * Sets the value of the receivingSchoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setReceivingSchoolReference(EducationalOrgReferenceType value) {
        this.receivingSchoolReference = value;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeginDate() {
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
    public void setBeginDate(String value) {
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
    public String getEndDate() {
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
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the feederRelationshipDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFeederRelationshipDescription() {
        return feederRelationshipDescription;
    }

    /**
     * Sets the value of the feederRelationshipDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFeederRelationshipDescription(String value) {
        this.feederRelationshipDescription = value;
    }

}
