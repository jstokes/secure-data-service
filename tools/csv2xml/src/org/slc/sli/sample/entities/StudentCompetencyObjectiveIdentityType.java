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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * Encapsulates the possible attributes that can be used to lookup the identity of the Student Competency Objectives.
 * 
 * <p>Java class for StudentCompetencyObjectiveIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentCompetencyObjectiveIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="StudentCompetencyObjectiveId" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *         &lt;element name="Objective" type="{http://ed-fi.org/0100}Objective"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentCompetencyObjectiveIdentityType", propOrder = {
    "studentCompetencyObjectiveIdOrObjective"
})
public class StudentCompetencyObjectiveIdentityType {

    @XmlElementRefs({
        @XmlElementRef(name = "Objective", namespace = "http://ed-fi.org/0100", type = JAXBElement.class),
        @XmlElementRef(name = "StudentCompetencyObjectiveId", namespace = "http://ed-fi.org/0100", type = JAXBElement.class)
    })
    protected List<JAXBElement<String>> studentCompetencyObjectiveIdOrObjective;

    /**
     * Gets the value of the studentCompetencyObjectiveIdOrObjective property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studentCompetencyObjectiveIdOrObjective property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudentCompetencyObjectiveIdOrObjective().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getStudentCompetencyObjectiveIdOrObjective() {
        if (studentCompetencyObjectiveIdOrObjective == null) {
            studentCompetencyObjectiveIdOrObjective = new ArrayList<JAXBElement<String>>();
        }
        return this.studentCompetencyObjectiveIdOrObjective;
    }

}
