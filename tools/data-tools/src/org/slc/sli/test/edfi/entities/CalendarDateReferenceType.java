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
 * Provides alternative references for calendar dates during interchange. Use XML IDREF to reference a course record that is included in the interchange
 * 
 * <p>Java class for CalendarDateReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CalendarDateReferenceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ReferenceType">
 *       &lt;sequence>
 *         &lt;element name="CalendarDateIdentity" type="{http://ed-fi.org/0100}CalendarDateIdentityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CalendarDateReferenceType", propOrder = {
    "calendarDateIdentity"
})
public class CalendarDateReferenceType
    extends ReferenceType
{

    @XmlElement(name = "CalendarDateIdentity")
    protected CalendarDateIdentityType calendarDateIdentity;

    /**
     * Gets the value of the calendarDateIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link CalendarDateIdentityType }
     *     
     */
    public CalendarDateIdentityType getCalendarDateIdentity() {
        return calendarDateIdentity;
    }

    /**
     * Sets the value of the calendarDateIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalendarDateIdentityType }
     *     
     */
    public void setCalendarDateIdentity(CalendarDateIdentityType value) {
        this.calendarDateIdentity = value;
    }

}
