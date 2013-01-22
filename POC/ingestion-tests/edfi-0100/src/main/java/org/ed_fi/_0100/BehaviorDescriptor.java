//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This descriptor holds the categories of behavior describing a discipline incident.
 * 
 * <p>Java class for BehaviorDescriptor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BehaviorDescriptor">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="CodeValue" type="{http://ed-fi.org/0100}CodeValue"/>
 *         &lt;element name="ShortDescription" type="{http://ed-fi.org/0100}ShortDescription"/>
 *         &lt;element name="Description" type="{http://ed-fi.org/0100}Description" minOccurs="0"/>
 *         &lt;element name="BehaviorCategory" type="{http://ed-fi.org/0100}BehaviorCategoryType"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BehaviorDescriptor", propOrder = {
    "codeValue",
    "shortDescription",
    "description",
    "behaviorCategory",
    "educationOrganizationReference"
})
public class BehaviorDescriptor
    extends ComplexObjectType
{

    @XmlElement(name = "CodeValue", required = true)
    protected String codeValue;
    @XmlElement(name = "ShortDescription", required = true)
    protected String shortDescription;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "BehaviorCategory", required = true)
    protected BehaviorCategoryType behaviorCategory;
    @XmlElement(name = "EducationOrganizationReference", required = true)
    protected List<EducationalOrgReferenceType> educationOrganizationReference;

    /**
     * Gets the value of the codeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * Sets the value of the codeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeValue(String value) {
        this.codeValue = value;
    }

    /**
     * Gets the value of the shortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the behaviorCategory property.
     * 
     * @return
     *     possible object is
     *     {@link BehaviorCategoryType }
     *     
     */
    public BehaviorCategoryType getBehaviorCategory() {
        return behaviorCategory;
    }

    /**
     * Sets the value of the behaviorCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link BehaviorCategoryType }
     *     
     */
    public void setBehaviorCategory(BehaviorCategoryType value) {
        this.behaviorCategory = value;
    }

    /**
     * Gets the value of the educationOrganizationReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationOrganizationReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationOrganizationReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EducationalOrgReferenceType }
     * 
     * 
     */
    public List<EducationalOrgReferenceType> getEducationOrganizationReference() {
        if (educationOrganizationReference == null) {
            educationOrganizationReference = new ArrayList<EducationalOrgReferenceType>();
        }
        return this.educationOrganizationReference;
    }

}
