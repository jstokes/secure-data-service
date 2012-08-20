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
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents one of many single measures
 * 				that make up an assessment.
 * 			
 * 
 * <p>Java class for assessmentItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="assessmentItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificationCode" type="{}identificationCode"/>
 *         &lt;element name="itemCategory" type="{}itemCategoryType"/>
 *         &lt;element name="maxRawScore" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="correctResponse" type="{}correctResponse"/>
 *         &lt;element name="learningStandards" type="{}reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nomenclature" type="{}nomenclature" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "assessmentItem", propOrder = {
    "identificationCode",
    "itemCategory",
    "maxRawScore",
    "correctResponse",
    "learningStandards",
    "nomenclature"
})
public class AssessmentItem {

    @XmlElement(required = true)
    protected String identificationCode;
    @XmlElement(required = true)
    protected ItemCategoryType itemCategory;
    protected int maxRawScore;
    @XmlElement(required = true)
    protected String correctResponse;
    protected List<String> learningStandards;
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
     * Gets the value of the learningStandards property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningStandards property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningStandards().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLearningStandards() {
        if (learningStandards == null) {
            learningStandards = new ArrayList<String>();
        }
        return this.learningStandards;
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
