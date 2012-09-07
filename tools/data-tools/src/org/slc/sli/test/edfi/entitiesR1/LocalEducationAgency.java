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
 * This entity represents an administrative unit at the
 * 			local level which exists primarily to operate schools or to contract
 * 			for educational services.  It includes school districts, charter schools,
 * 			charter management organizations, or other local administrative organizations.
 * 			
 * 
 * <p>Java class for localEducationAgency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="localEducationAgency">
 *   &lt;complexContent>
 *     &lt;extension base="{http://slc-sli/ed-org/0.1}educationOrganization">
 *       &lt;sequence>
 *         &lt;element name="LEACategory" type="{http://slc-sli/ed-org/0.1}LEACategoryType"/>
 *         &lt;element name="charterStatus" type="{http://slc-sli/ed-org/0.1}charterStatusType" minOccurs="0"/>
 *         &lt;element name="localEducationAgencyReference" type="{http://slc-sli/ed-org/0.1}reference" minOccurs="0"/>
 *         &lt;element name="educationServiceCenterReference" type="{http://slc-sli/ed-org/0.1}reference" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "localEducationAgency", propOrder = {
    "leaCategory",
    "charterStatus",
    "localEducationAgencyReference",
    "educationServiceCenterReference"
})
public class LocalEducationAgency
    extends EducationOrganization
{

    @XmlElement(name = "LEACategory", required = true)
    protected LEACategoryType leaCategory;
    protected CharterStatusType charterStatus;
    protected String localEducationAgencyReference;
    protected String educationServiceCenterReference;

    /**
     * Gets the value of the leaCategory property.
     * 
     * @return
     *     possible object is
     *     {@link LEACategoryType }
     *     
     */
    public LEACategoryType getLEACategory() {
        return leaCategory;
    }

    /**
     * Sets the value of the leaCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link LEACategoryType }
     *     
     */
    public void setLEACategory(LEACategoryType value) {
        this.leaCategory = value;
    }

    /**
     * Gets the value of the charterStatus property.
     * 
     * @return
     *     possible object is
     *     {@link CharterStatusType }
     *     
     */
    public CharterStatusType getCharterStatus() {
        return charterStatus;
    }

    /**
     * Sets the value of the charterStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link CharterStatusType }
     *     
     */
    public void setCharterStatus(CharterStatusType value) {
        this.charterStatus = value;
    }

    /**
     * Gets the value of the localEducationAgencyReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalEducationAgencyReference() {
        return localEducationAgencyReference;
    }

    /**
     * Sets the value of the localEducationAgencyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalEducationAgencyReference(String value) {
        this.localEducationAgencyReference = value;
    }

    /**
     * Gets the value of the educationServiceCenterReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducationServiceCenterReference() {
        return educationServiceCenterReference;
    }

    /**
     * Sets the value of the educationServiceCenterReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducationServiceCenterReference(String value) {
        this.educationServiceCenterReference = value;
    }

}
