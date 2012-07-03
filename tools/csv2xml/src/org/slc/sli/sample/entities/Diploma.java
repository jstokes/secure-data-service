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
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This educational entity represents the conferring or certification by an educational organization that the student has successfully completed a particular course of study.  It represents the electronic version of its physical document counterpart.
 * 
 * <p>Java class for Diploma complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Diploma">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="DiplomaAwardDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DiplomaLevel" type="{http://ed-fi.org/0100}DiplomaLevelType" minOccurs="0"/>
 *         &lt;element name="DiplomaType" type="{http://ed-fi.org/0100}DiplomaType"/>
 *         &lt;element name="CTECompleter" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AcademicHonors" type="{http://ed-fi.org/0100}AcademicHonor" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Recognitions" type="{http://ed-fi.org/0100}Recognition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Diploma", propOrder = {
    "diplomaAwardDate",
    "diplomaLevel",
    "diplomaType",
    "cteCompleter",
    "academicHonors",
    "recognitions",
    "schoolReference"
})
public class Diploma
    extends ComplexObjectType
{

    @XmlElement(name = "DiplomaAwardDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar diplomaAwardDate;
    @XmlElement(name = "DiplomaLevel")
    protected DiplomaLevelType diplomaLevel;
    @XmlElement(name = "DiplomaType", required = true)
    protected DiplomaType diplomaType;
    @XmlElement(name = "CTECompleter")
    protected Boolean cteCompleter;
    @XmlElement(name = "AcademicHonors")
    protected List<AcademicHonor> academicHonors;
    @XmlElement(name = "Recognitions")
    protected List<Recognition> recognitions;
    @XmlElement(name = "SchoolReference", required = true)
    protected EducationalOrgReferenceType schoolReference;

    /**
     * Gets the value of the diplomaAwardDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getDiplomaAwardDate() {
        return diplomaAwardDate;
    }

    /**
     * Sets the value of the diplomaAwardDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiplomaAwardDate(Calendar value) {
        this.diplomaAwardDate = value;
    }

    /**
     * Gets the value of the diplomaLevel property.
     * 
     * @return
     *     possible object is
     *     {@link DiplomaLevelType }
     *     
     */
    public DiplomaLevelType getDiplomaLevel() {
        return diplomaLevel;
    }

    /**
     * Sets the value of the diplomaLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiplomaLevelType }
     *     
     */
    public void setDiplomaLevel(DiplomaLevelType value) {
        this.diplomaLevel = value;
    }

    /**
     * Gets the value of the diplomaType property.
     * 
     * @return
     *     possible object is
     *     {@link DiplomaType }
     *     
     */
    public DiplomaType getDiplomaType() {
        return diplomaType;
    }

    /**
     * Sets the value of the diplomaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiplomaType }
     *     
     */
    public void setDiplomaType(DiplomaType value) {
        this.diplomaType = value;
    }

    /**
     * Gets the value of the cteCompleter property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCTECompleter() {
        return cteCompleter;
    }

    /**
     * Sets the value of the cteCompleter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCTECompleter(Boolean value) {
        this.cteCompleter = value;
    }

    /**
     * Gets the value of the academicHonors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the academicHonors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcademicHonors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcademicHonor }
     * 
     * 
     */
    public List<AcademicHonor> getAcademicHonors() {
        if (academicHonors == null) {
            academicHonors = new ArrayList<AcademicHonor>();
        }
        return this.academicHonors;
    }

    /**
     * Gets the value of the recognitions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recognitions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecognitions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Recognition }
     * 
     * 
     */
    public List<Recognition> getRecognitions() {
        if (recognitions == null) {
            recognitions = new ArrayList<Recognition>();
        }
        return this.recognitions;
    }

    /**
     * Gets the value of the schoolReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getSchoolReference() {
        return schoolReference;
    }

    /**
     * Sets the value of the schoolReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setSchoolReference(EducationalOrgReferenceType value) {
        this.schoolReference = value;
    }

}
