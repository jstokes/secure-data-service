//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * New SLC natural reference type for GradebookEntry.
 * 
 * <p>Java class for SLC-GradebookEntryReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-GradebookEntryReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GradebookEntryIdentity" type="{http://ed-fi.org/0100}SLC-GradebookEntryIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-GradebookEntryReferenceType", propOrder = {
    "gradebookEntryIdentity"
})
public class SLCGradebookEntryReferenceType {

    @XmlElement(name = "GradebookEntryIdentity", required = true)
    protected SLCGradebookEntryIdentityType gradebookEntryIdentity;

    /**
     * Gets the value of the gradebookEntryIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCGradebookEntryIdentityType }
     *     
     */
    public SLCGradebookEntryIdentityType getGradebookEntryIdentity() {
        return gradebookEntryIdentity;
    }

    /**
     * Sets the value of the gradebookEntryIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCGradebookEntryIdentityType }
     *     
     */
    public void setGradebookEntryIdentity(SLCGradebookEntryIdentityType value) {
        this.gradebookEntryIdentity = value;
    }

}
