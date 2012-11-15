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
 * This entity represents an administrative unit at the local level which exists primarily to operate schools or to contract for educational services.  It includes school districts, charter schools, charter management organizations, or other local administrative organizations.
 * 
 * 
 * <p>Java class for LocalEducationAgency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LocalEducationAgency">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}EducationOrganization">
 *       &lt;sequence>
 *         &lt;element name="LEACategory" type="{http://ed-fi.org/0100}LEACategoryType"/>
 *         &lt;element name="CharterStatus" type="{http://ed-fi.org/0100}CharterStatusType" minOccurs="0"/>
 *         &lt;element name="LocalEducationAgencyReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" minOccurs="0"/>
 *         &lt;element name="EducationServiceCenterReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" minOccurs="0"/>
 *         &lt;element name="StateEducationAgencyReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocalEducationAgency", propOrder = {
    "leaCategory",
    "charterStatus",
    "localEducationAgencyReference",
    "educationServiceCenterReference",
    "stateEducationAgencyReference"
})
public class LocalEducationAgency
    extends EducationOrganization
{

    @XmlElement(name = "LEACategory", required = true)
    protected LEACategoryType leaCategory;
    @XmlElement(name = "CharterStatus")
    protected CharterStatusType charterStatus;
    @XmlElement(name = "LocalEducationAgencyReference")
    protected EducationalOrgReferenceType localEducationAgencyReference;
    @XmlElement(name = "EducationServiceCenterReference")
    protected EducationalOrgReferenceType educationServiceCenterReference;
    @XmlElement(name = "StateEducationAgencyReference")
    protected EducationalOrgReferenceType stateEducationAgencyReference;

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
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getLocalEducationAgencyReference() {
        return localEducationAgencyReference;
    }

    /**
     * Sets the value of the localEducationAgencyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setLocalEducationAgencyReference(EducationalOrgReferenceType value) {
        this.localEducationAgencyReference = value;
    }

    /**
     * Gets the value of the educationServiceCenterReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getEducationServiceCenterReference() {
        return educationServiceCenterReference;
    }

    /**
     * Sets the value of the educationServiceCenterReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setEducationServiceCenterReference(EducationalOrgReferenceType value) {
        this.educationServiceCenterReference = value;
    }

    /**
     * Gets the value of the stateEducationAgencyReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getStateEducationAgencyReference() {
        return stateEducationAgencyReference;
    }

    /**
     * Sets the value of the stateEducationAgencyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setStateEducationAgencyReference(EducationalOrgReferenceType value) {
        this.stateEducationAgencyReference = value;
    }

}
