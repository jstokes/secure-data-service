//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The career cluster or pathway representing the career path of the Vocational/Career Tech concentrator.
 * 
 * <p>Java class for CTEProgram complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CTEProgram">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CareerPathway" type="{http://ed-fi.org/0100}CareerPathwayType"/>
 *         &lt;element name="CIPCode" type="{http://ed-fi.org/0100}CIPCode" minOccurs="0"/>
 *         &lt;element name="PrimaryCTEProgramIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CTEProgramCompletionIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CTEProgram", propOrder = {
    "careerPathway",
    "cipCode",
    "primaryCTEProgramIndicator",
    "cteProgramCompletionIndicator"
})
public class CTEProgram {

    @XmlElement(name = "CareerPathway", required = true)
    protected CareerPathwayType careerPathway;
    @XmlElement(name = "CIPCode")
    protected String cipCode;
    @XmlElement(name = "PrimaryCTEProgramIndicator")
    protected Boolean primaryCTEProgramIndicator;
    @XmlElement(name = "CTEProgramCompletionIndicator")
    protected Boolean cteProgramCompletionIndicator;

    /**
     * Gets the value of the careerPathway property.
     * 
     * @return
     *     possible object is
     *     {@link CareerPathwayType }
     *     
     */
    public CareerPathwayType getCareerPathway() {
        return careerPathway;
    }

    /**
     * Sets the value of the careerPathway property.
     * 
     * @param value
     *     allowed object is
     *     {@link CareerPathwayType }
     *     
     */
    public void setCareerPathway(CareerPathwayType value) {
        this.careerPathway = value;
    }

    /**
     * Gets the value of the cipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCIPCode() {
        return cipCode;
    }

    /**
     * Sets the value of the cipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCIPCode(String value) {
        this.cipCode = value;
    }

    /**
     * Gets the value of the primaryCTEProgramIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrimaryCTEProgramIndicator() {
        return primaryCTEProgramIndicator;
    }

    /**
     * Sets the value of the primaryCTEProgramIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrimaryCTEProgramIndicator(Boolean value) {
        this.primaryCTEProgramIndicator = value;
    }

    /**
     * Gets the value of the cteProgramCompletionIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCTEProgramCompletionIndicator() {
        return cteProgramCompletionIndicator;
    }

    /**
     * Sets the value of the cteProgramCompletionIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCTEProgramCompletionIndicator(Boolean value) {
        this.cteProgramCompletionIndicator = value;
    }

}
