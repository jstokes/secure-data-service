//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Encapsulates the possible attributes that can be used to lookup the identity of discipline incidents.
 * 
 * <p>Java class for DisciplineIncidentIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DisciplineIncidentIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IncidentIdentifier" type="{http://ed-fi.org/0100}IncidentIdentifier"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="StateOrganizationId" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *           &lt;element name="EducationOrgIdentificationCode" type="{http://ed-fi.org/0100}EducationOrgIdentificationCode" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisciplineIncidentIdentityType", propOrder = {
    "incidentIdentifier",
    "stateOrganizationIdOrEducationOrgIdentificationCode"
})
public class DisciplineIncidentIdentityType {

    @XmlElement(name = "IncidentIdentifier", required = true)
    protected String incidentIdentifier;
    @XmlElements({
        @XmlElement(name = "StateOrganizationId", type = String.class),
        @XmlElement(name = "EducationOrgIdentificationCode", type = EducationOrgIdentificationCode.class)
    })
    protected List<Object> stateOrganizationIdOrEducationOrgIdentificationCode;

    /**
     * Gets the value of the incidentIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncidentIdentifier() {
        return incidentIdentifier;
    }

    /**
     * Sets the value of the incidentIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncidentIdentifier(String value) {
        this.incidentIdentifier = value;
    }

    /**
     * Gets the value of the stateOrganizationIdOrEducationOrgIdentificationCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stateOrganizationIdOrEducationOrgIdentificationCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStateOrganizationIdOrEducationOrgIdentificationCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link EducationOrgIdentificationCode }
     * 
     * 
     */
    public List<Object> getStateOrganizationIdOrEducationOrgIdentificationCode() {
        if (stateOrganizationIdOrEducationOrgIdentificationCode == null) {
            stateOrganizationIdOrEducationOrgIdentificationCode = new ArrayList<Object>();
        }
        return this.stateOrganizationIdOrEducationOrgIdentificationCode;
    }

}
