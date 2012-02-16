//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents one of many single measures that make up an assessment. 
 * 
 * <p>Java class for AssessmentItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssessmentItem">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="IdentificationCode" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *         &lt;element name="ItemCategory" type="{http://ed-fi.org/0100}ItemCategoryType"/>
 *         &lt;element name="MaxRawScore" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CorrectResponse" type="{http://ed-fi.org/0100}CorrectResponse"/>
 *         &lt;element name="LearningStandardReference" type="{http://ed-fi.org/0100}LearningStandardReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Nomenclature" type="{http://ed-fi.org/0100}Nomenclature" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssessmentItem", propOrder = {
    "identificationCode",
    "itemCategory",
    "maxRawScore",
    "correctResponse",
    "learningStandardReference",
    "nomenclature"
})
public class AssessmentItem
    extends ComplexObjectType
{

    @XmlElement(name = "IdentificationCode", required = true)
    protected String identificationCode;
    @XmlElement(name = "ItemCategory", required = true)
    protected ItemCategoryType itemCategory;
    @XmlElement(name = "MaxRawScore")
    protected int maxRawScore;
    @XmlElement(name = "CorrectResponse", required = true)
    protected String correctResponse;
    @XmlElement(name = "LearningStandardReference")
    protected List<LearningStandardReferenceType> learningStandardReference;
    @XmlElement(name = "Nomenclature")
    protected String nomenclature;

    /**
     * Gets the value of the identificationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificationCode() {
        return identificationCode;
    }

    /**
     * Sets the value of the identificationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificationCode(String value) {
        this.identificationCode = value;
    }

    /**
     * Gets the value of the itemCategory property.
     * 
     * @return
     *     possible object is
     *     {@link ItemCategoryType }
     *     
     */
    public ItemCategoryType getItemCategory() {
        return itemCategory;
    }

    /**
     * Sets the value of the itemCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemCategoryType }
     *     
     */
    public void setItemCategory(ItemCategoryType value) {
        this.itemCategory = value;
    }

    /**
     * Gets the value of the maxRawScore property.
     * 
     */
    public int getMaxRawScore() {
        return maxRawScore;
    }

    /**
     * Sets the value of the maxRawScore property.
     * 
     */
    public void setMaxRawScore(int value) {
        this.maxRawScore = value;
    }

    /**
     * Gets the value of the correctResponse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrectResponse() {
        return correctResponse;
    }

    /**
     * Sets the value of the correctResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrectResponse(String value) {
        this.correctResponse = value;
    }

    /**
     * Gets the value of the learningStandardReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningStandardReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningStandardReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LearningStandardReferenceType }
     * 
     * 
     */
    public List<LearningStandardReferenceType> getLearningStandardReference() {
        if (learningStandardReference == null) {
            learningStandardReference = new ArrayList<LearningStandardReferenceType>();
        }
        return this.learningStandardReference;
    }

    /**
     * Gets the value of the nomenclature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomenclature() {
        return nomenclature;
    }

    /**
     * Sets the value of the nomenclature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomenclature(String value) {
        this.nomenclature = value;
    }

}
