//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides alternative references for objective assessments during interchange. Use XML IDREF to reference a course record that is included in the interchange
 * 
 * <p>Java class for ObjectiveAssessmentReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectiveAssessmentReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="ObjectiveAssessmentIdentity" type="{http://ed-fi.org/0100}ObjectiveAssessmentIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectiveAssessmentReferenceType", propOrder = {
    "objectiveAssessmentIdentity"
})
public class ObjectiveAssessmentReferenceType
    extends ReferenceType
{

    @XmlElement(name = "ObjectiveAssessmentIdentity")
    protected ObjectiveAssessmentIdentityType objectiveAssessmentIdentity;

    /**
     * Gets the value of the objectiveAssessmentIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectiveAssessmentIdentityType }
     *     
     */
    public ObjectiveAssessmentIdentityType getObjectiveAssessmentIdentity() {
        return objectiveAssessmentIdentity;
    }

    /**
     * Sets the value of the objectiveAssessmentIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectiveAssessmentIdentityType }
     *     
     */
    public void setObjectiveAssessmentIdentity(ObjectiveAssessmentIdentityType value) {
        this.objectiveAssessmentIdentity = value;
    }

}
