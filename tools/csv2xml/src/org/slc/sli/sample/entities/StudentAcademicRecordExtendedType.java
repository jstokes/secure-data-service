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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StudentAcademicRecordExtendedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentAcademicRecordExtendedType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}StudentAcademicRecord">
 *       &lt;sequence>
 *         &lt;element name="SubmissionCertification" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentAcademicRecordExtendedType", propOrder = {
    "submissionCertification"
})
public class StudentAcademicRecordExtendedType
    extends StudentAcademicRecord
{

    @XmlElement(name = "SubmissionCertification", required = true)
    protected Object submissionCertification;

    /**
     * Gets the value of the submissionCertification property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSubmissionCertification() {
        return submissionCertification;
    }

    /**
     * Sets the value of the submissionCertification property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSubmissionCertification(Object value) {
        this.submissionCertification = value;
    }

}
