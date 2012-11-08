/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.31 at 09:35:49 AM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Encapsulates the possible attributes that can be used to lookup the identity of discipline
 * incidents.
 * 
 * <p>
 * Java class for DisciplineIncidentIdentityType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DisciplineIncidentIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IncidentIdentifier" type="{http://ed-fi.org/0100}IncidentIdentifier"/>
 *         &lt;element name="EducationalOrgReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReference"/>
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
 "educationalOrgReference"
})
public class DisciplineIncidentIdentityType {

    @XmlElement(name = "IncidentIdentifier", required = true)
    protected String incidentIdentifier;
    @XmlElement(name = "EducationalOrgReference", required = true)
    protected EducationalOrgReferenceType educationalOrgReference;

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
     * Gets the value of the educationalOrgReference property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public EducationalOrgReferenceType getEducationalOrgReference() {
        return educationalOrgReference;
    }

    /**
     * Sets the value of the educationalOrgReference property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEducationalOrgReference(EducationalOrgReferenceType value) {
        this.educationalOrgReference = value;
    }


}
