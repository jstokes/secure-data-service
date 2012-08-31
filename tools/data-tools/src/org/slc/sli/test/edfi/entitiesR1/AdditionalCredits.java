//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Additional credits or units of value awarded for
 * 				the completion of a course (e.g., AP, IB, Dual credits)
 * 			
 * 
 * <p>Java class for additionalCredits complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="additionalCredits">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credit">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="additionalCreditType" type="{http://slc-sli/ed-org/0.1}additionalCreditType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "additionalCredits", propOrder = {
    "credit",
    "additionalCreditType"
})
public class AdditionalCredits {

    protected double credit;
    @XmlElement(required = true)
    protected AdditionalCreditType additionalCreditType;

    /**
     * Gets the value of the credit property.
     * 
     */
    public double getCredit() {
        return credit;
    }

    /**
     * Sets the value of the credit property.
     * 
     */
    public void setCredit(double value) {
        this.credit = value;
    }

    /**
     * Gets the value of the additionalCreditType property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalCreditType }
     *     
     */
    public AdditionalCreditType getAdditionalCreditType() {
        return additionalCreditType;
    }

    /**
     * Sets the value of the additionalCreditType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalCreditType }
     *     
     */
    public void setAdditionalCreditType(AdditionalCreditType value) {
        this.additionalCreditType = value;
    }

}
