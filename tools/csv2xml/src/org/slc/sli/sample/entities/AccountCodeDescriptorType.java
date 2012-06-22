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
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * Provides references for account code descriptors during interchange. Use XML IDREF to reference a course record that is included in the interchange.  To lookup when already loaded, specify either CodeValue OR Description OR ShortDescription plus the account code type (fund, function, etc.).
 * 
 * <p>Java class for AccountCodeDescriptorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountCodeDescriptorType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="AccountCodeType" type="{http://ed-fi.org/0100}AccountCodeType" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="CodeValue" type="{http://ed-fi.org/0100}CodeValue"/>
 *           &lt;element name="ShortDescription" type="{http://ed-fi.org/0100}ShortDescription"/>
 *           &lt;element name="Description" type="{http://ed-fi.org/0100}Description"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountCodeDescriptorType", propOrder = {
    "accountCodeType",
    "codeValueOrShortDescriptionOrDescription"
})
public class AccountCodeDescriptorType
    extends ReferenceType
{

    @XmlElement(name = "AccountCodeType")
    protected String accountCodeType;
    @XmlElementRefs({
        @XmlElementRef(name = "ShortDescription", namespace = "http://ed-fi.org/0100", type = JAXBElement.class),
        @XmlElementRef(name = "CodeValue", namespace = "http://ed-fi.org/0100", type = JAXBElement.class),
        @XmlElementRef(name = "Description", namespace = "http://ed-fi.org/0100", type = JAXBElement.class)
    })
    protected List<JAXBElement<String>> codeValueOrShortDescriptionOrDescription;

    /**
     * Gets the value of the accountCodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountCodeType() {
        return accountCodeType;
    }

    /**
     * Sets the value of the accountCodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountCodeType(String value) {
        this.accountCodeType = value;
    }

    /**
     * Gets the value of the codeValueOrShortDescriptionOrDescription property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeValueOrShortDescriptionOrDescription property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeValueOrShortDescriptionOrDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getCodeValueOrShortDescriptionOrDescription() {
        if (codeValueOrShortDescriptionOrDescription == null) {
            codeValueOrShortDescriptionOrDescription = new ArrayList<JAXBElement<String>>();
        }
        return this.codeValueOrShortDescriptionOrDescription;
    }

}
