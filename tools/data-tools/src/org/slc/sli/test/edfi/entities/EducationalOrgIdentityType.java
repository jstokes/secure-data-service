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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Encapsulates the possible attributes that can be used to lookup the identity of educational organizations, especially ESCs, LEAs (districts), and schools.
 * 
 * <p>Java class for EducationalOrgIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EducationalOrgIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="StateOrganizationId" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *         &lt;element name="EducationOrgIdentificationCode" type="{http://ed-fi.org/0100}EducationOrgIdentificationCode" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EducationalOrgIdentityType", propOrder = {
    "stateOrganizationIdOrEducationOrgIdentificationCode"
})
public class EducationalOrgIdentityType {

    @XmlElements({
        @XmlElement(name = "EducationOrgIdentificationCode", type = EducationOrgIdentificationCode.class),
        @XmlElement(name = "StateOrganizationId", type = String.class)
    })
    protected List<Object> stateOrganizationIdOrEducationOrgIdentificationCode;

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
     * {@link EducationOrgIdentificationCode }
     * {@link String }
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
