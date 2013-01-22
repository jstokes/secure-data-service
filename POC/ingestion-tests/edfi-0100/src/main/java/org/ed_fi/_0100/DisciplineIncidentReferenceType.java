//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides alternative references for DisciplineIncident reference during interchange. Use XML IDREF to reference a DisciplineMetric record that is included in the interchange
 * 
 * <p>Java class for DisciplineIncidentReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DisciplineIncidentReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="DisciplineIncidentIdentity" type="{http://ed-fi.org/0100}DisciplineIncidentIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisciplineIncidentReferenceType", propOrder = {
    "disciplineIncidentIdentity"
})
public class DisciplineIncidentReferenceType
    extends ReferenceType
{

    @XmlElement(name = "DisciplineIncidentIdentity")
    protected DisciplineIncidentIdentityType disciplineIncidentIdentity;

    /**
     * Gets the value of the disciplineIncidentIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link DisciplineIncidentIdentityType }
     *     
     */
    public DisciplineIncidentIdentityType getDisciplineIncidentIdentity() {
        return disciplineIncidentIdentity;
    }

    /**
     * Sets the value of the disciplineIncidentIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisciplineIncidentIdentityType }
     *     
     */
    public void setDisciplineIncidentIdentity(DisciplineIncidentIdentityType value) {
        this.disciplineIncidentIdentity = value;
    }

}
