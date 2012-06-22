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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The legal document or authorization giving authorization to perform teaching assignment services.
 * 
 * <p>Java class for Credential complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Credential">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CredentialType" type="{http://ed-fi.org/0100}CredentialType"/>
 *         &lt;element name="CredentialField" type="{http://ed-fi.org/0100}CredentialFieldDescriptorType"/>
 *         &lt;element name="Level" type="{http://ed-fi.org/0100}LevelType"/>
 *         &lt;element name="TeachingCredentialType" type="{http://ed-fi.org/0100}TeachingCredentialType"/>
 *         &lt;element name="CredentialIssuanceDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="CredentialExpirationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="TeachingCredentialBasis" type="{http://ed-fi.org/0100}TeachingCredentialBasisType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Credential", propOrder = {
    "credentialType",
    "credentialField",
    "level",
    "teachingCredentialType",
    "credentialIssuanceDate",
    "credentialExpirationDate",
    "teachingCredentialBasis"
})
public class Credential {

    @XmlElement(name = "CredentialType", required = true)
    protected CredentialType credentialType;
    @XmlElement(name = "CredentialField", required = true)
    protected CredentialFieldDescriptorType credentialField;
    @XmlElement(name = "Level", required = true)
    protected LevelType level;
    @XmlElement(name = "TeachingCredentialType", required = true)
    protected TeachingCredentialType teachingCredentialType;
    @XmlElement(name = "CredentialIssuanceDate", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String credentialIssuanceDate;
    @XmlElement(name = "CredentialExpirationDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String credentialExpirationDate;
    @XmlElement(name = "TeachingCredentialBasis")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String teachingCredentialBasis;

    /**
     * Gets the value of the credentialType property.
     * 
     * @return
     *     possible object is
     *     {@link CredentialType }
     *     
     */
    public CredentialType getCredentialType() {
        return credentialType;
    }

    /**
     * Sets the value of the credentialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CredentialType }
     *     
     */
    public void setCredentialType(CredentialType value) {
        this.credentialType = value;
    }

    /**
     * Gets the value of the credentialField property.
     * 
     * @return
     *     possible object is
     *     {@link CredentialFieldDescriptorType }
     *     
     */
    public CredentialFieldDescriptorType getCredentialField() {
        return credentialField;
    }

    /**
     * Sets the value of the credentialField property.
     * 
     * @param value
     *     allowed object is
     *     {@link CredentialFieldDescriptorType }
     *     
     */
    public void setCredentialField(CredentialFieldDescriptorType value) {
        this.credentialField = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link LevelType }
     *     
     */
    public LevelType getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelType }
     *     
     */
    public void setLevel(LevelType value) {
        this.level = value;
    }

    /**
     * Gets the value of the teachingCredentialType property.
     * 
     * @return
     *     possible object is
     *     {@link TeachingCredentialType }
     *     
     */
    public TeachingCredentialType getTeachingCredentialType() {
        return teachingCredentialType;
    }

    /**
     * Sets the value of the teachingCredentialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TeachingCredentialType }
     *     
     */
    public void setTeachingCredentialType(TeachingCredentialType value) {
        this.teachingCredentialType = value;
    }

    /**
     * Gets the value of the credentialIssuanceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredentialIssuanceDate() {
        return credentialIssuanceDate;
    }

    /**
     * Sets the value of the credentialIssuanceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredentialIssuanceDate(String value) {
        this.credentialIssuanceDate = value;
    }

    /**
     * Gets the value of the credentialExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredentialExpirationDate() {
        return credentialExpirationDate;
    }

    /**
     * Sets the value of the credentialExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredentialExpirationDate(String value) {
        this.credentialExpirationDate = value;
    }

    /**
     * Gets the value of the teachingCredentialBasis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeachingCredentialBasis() {
        return teachingCredentialBasis;
    }

    /**
     * Sets the value of the teachingCredentialBasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeachingCredentialBasis(String value) {
        this.teachingCredentialBasis = value;
    }

}
