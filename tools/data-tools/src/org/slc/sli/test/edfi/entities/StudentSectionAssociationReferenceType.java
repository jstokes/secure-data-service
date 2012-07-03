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
 * Provides alternative references for the Student-section association. Use XML IDREF to reference a program record that is included in the interchange
 * 
 * <p>Java class for StudentSectionAssociationReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentSectionAssociationReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="StudentSectionAssociationIdentity" type="{http://ed-fi.org/0100}StudentSectionAssociationIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentSectionAssociationReferenceType", propOrder = {
    "studentSectionAssociationIdentity"
})
public class StudentSectionAssociationReferenceType
    extends ReferenceType
{

    @XmlElement(name = "StudentSectionAssociationIdentity")
    protected StudentSectionAssociationIdentityType studentSectionAssociationIdentity;

    /**
     * Gets the value of the studentSectionAssociationIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link StudentSectionAssociationIdentityType }
     *     
     */
    public StudentSectionAssociationIdentityType getStudentSectionAssociationIdentity() {
        return studentSectionAssociationIdentity;
    }

    /**
     * Sets the value of the studentSectionAssociationIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentSectionAssociationIdentityType }
     *     
     */
    public void setStudentSectionAssociationIdentity(StudentSectionAssociationIdentityType value) {
        this.studentSectionAssociationIdentity = value;
    }

}
