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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The student's relative preference to visual, auditory and tactile learning expressed as percentages.
 * 
 * <p>Java class for LearningStyles complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LearningStyles">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VisualLearning" type="{http://ed-fi.org/0100}percent"/>
 *         &lt;element name="AuditoryLearning" type="{http://ed-fi.org/0100}percent"/>
 *         &lt;element name="TactileLearning" type="{http://ed-fi.org/0100}percent"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LearningStyles", propOrder = {
    "visualLearning",
    "auditoryLearning",
    "tactileLearning"
})
public class LearningStyles {

    @XmlElement(name = "VisualLearning")
    protected int visualLearning;
    @XmlElement(name = "AuditoryLearning")
    protected int auditoryLearning;
    @XmlElement(name = "TactileLearning")
    protected int tactileLearning;

    /**
     * Gets the value of the visualLearning property.
     * 
     */
    public int getVisualLearning() {
        return visualLearning;
    }

    /**
     * Sets the value of the visualLearning property.
     * 
     */
    public void setVisualLearning(int value) {
        this.visualLearning = value;
    }

    /**
     * Gets the value of the auditoryLearning property.
     * 
     */
    public int getAuditoryLearning() {
        return auditoryLearning;
    }

    /**
     * Sets the value of the auditoryLearning property.
     * 
     */
    public void setAuditoryLearning(int value) {
        this.auditoryLearning = value;
    }

    /**
     * Gets the value of the tactileLearning property.
     * 
     */
    public int getTactileLearning() {
        return tactileLearning;
    }

    /**
     * Sets the value of the tactileLearning property.
     * 
     */
    public void setTactileLearning(int value) {
        this.tactileLearning = value;
    }

}
