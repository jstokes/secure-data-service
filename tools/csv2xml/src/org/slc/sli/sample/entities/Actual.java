/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

import java.math.BigDecimal;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This financial entity represents the sum of the financial transactions to date relating to a specific account.
 * 
 * <p>Java class for Actual complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Actual">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="AmountToDate" type="{http://ed-fi.org/0100}Currency"/>
 *         &lt;element name="AsOfDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="AccountReference" type="{http://ed-fi.org/0100}AccountReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Actual", propOrder = {
    "amountToDate",
    "asOfDate",
    "accountReference"
})
public class Actual
    extends ComplexObjectType
{

    @XmlElement(name = "AmountToDate", required = true)
    protected BigDecimal amountToDate;
    @XmlElement(name = "AsOfDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar asOfDate;
    @XmlElement(name = "AccountReference", required = true)
    protected AccountReferenceType accountReference;

    /**
     * Gets the value of the amountToDate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountToDate() {
        return amountToDate;
    }

    /**
     * Sets the value of the amountToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountToDate(BigDecimal value) {
        this.amountToDate = value;
    }

    /**
     * Gets the value of the asOfDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getAsOfDate() {
        return asOfDate;
    }

    /**
     * Sets the value of the asOfDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsOfDate(Calendar value) {
        this.asOfDate = value;
    }

    /**
     * Gets the value of the accountReference property.
     * 
     * @return
     *     possible object is
     *     {@link AccountReferenceType }
     *     
     */
    public AccountReferenceType getAccountReference() {
        return accountReference;
    }

    /**
     * Sets the value of the accountReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountReferenceType }
     *     
     */
    public void setAccountReference(AccountReferenceType value) {
        this.accountReference = value;
    }

}
