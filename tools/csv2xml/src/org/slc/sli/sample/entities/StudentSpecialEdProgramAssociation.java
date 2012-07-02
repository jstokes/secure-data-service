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

import java.math.BigDecimal;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This association represents the special education program(s) that a student participates in or receives services from.  The association is an extension of the StudentProgramAssociation particular for special education programs.
 * 
 * <p>Java class for StudentSpecialEdProgramAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentSpecialEdProgramAssociation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}StudentProgramAssociation">
 *       &lt;sequence>
 *         &lt;element name="IdeaEligibility" type="{http://ed-fi.org/0100}IdeaEligibilityType"/>
 *         &lt;element name="EducationalEnvironment" type="{http://ed-fi.org/0100}EducationalEnvironmentType" minOccurs="0"/>
 *         &lt;element name="SpecialEducationHoursPerWeek" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="MultiplyDisabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="MedicallyFragile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="LastEvaluationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="IEPReviewDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="IEPBeginDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="IEPEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentSpecialEdProgramAssociation", propOrder = {
    "ideaEligibility",
    "educationalEnvironment",
    "specialEducationHoursPerWeek",
    "multiplyDisabled",
    "medicallyFragile",
    "lastEvaluationDate",
    "iepReviewDate",
    "iepBeginDate",
    "iepEndDate"
})
public class StudentSpecialEdProgramAssociation
    extends StudentProgramAssociation
{

    @XmlElement(name = "IdeaEligibility", required = true)
    protected IdeaEligibilityType ideaEligibility;
    @XmlElement(name = "EducationalEnvironment")
    protected EducationalEnvironmentType educationalEnvironment;
    @XmlElement(name = "SpecialEducationHoursPerWeek")
    protected BigDecimal specialEducationHoursPerWeek;
    @XmlElement(name = "MultiplyDisabled")
    protected Boolean multiplyDisabled;
    @XmlElement(name = "MedicallyFragile")
    protected Boolean medicallyFragile;
    @XmlElement(name = "LastEvaluationDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar lastEvaluationDate;
    @XmlElement(name = "IEPReviewDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar iepReviewDate;
    @XmlElement(name = "IEPBeginDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar iepBeginDate;
    @XmlElement(name = "IEPEndDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar iepEndDate;

    /**
     * Gets the value of the ideaEligibility property.
     * 
     * @return
     *     possible object is
     *     {@link IdeaEligibilityType }
     *     
     */
    public IdeaEligibilityType getIdeaEligibility() {
        return ideaEligibility;
    }

    /**
     * Sets the value of the ideaEligibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdeaEligibilityType }
     *     
     */
    public void setIdeaEligibility(IdeaEligibilityType value) {
        this.ideaEligibility = value;
    }

    /**
     * Gets the value of the educationalEnvironment property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalEnvironmentType }
     *     
     */
    public EducationalEnvironmentType getEducationalEnvironment() {
        return educationalEnvironment;
    }

    /**
     * Sets the value of the educationalEnvironment property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalEnvironmentType }
     *     
     */
    public void setEducationalEnvironment(EducationalEnvironmentType value) {
        this.educationalEnvironment = value;
    }

    /**
     * Gets the value of the specialEducationHoursPerWeek property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSpecialEducationHoursPerWeek() {
        return specialEducationHoursPerWeek;
    }

    /**
     * Sets the value of the specialEducationHoursPerWeek property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSpecialEducationHoursPerWeek(BigDecimal value) {
        this.specialEducationHoursPerWeek = value;
    }

    /**
     * Gets the value of the multiplyDisabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMultiplyDisabled() {
        return multiplyDisabled;
    }

    /**
     * Sets the value of the multiplyDisabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMultiplyDisabled(Boolean value) {
        this.multiplyDisabled = value;
    }

    /**
     * Gets the value of the medicallyFragile property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMedicallyFragile() {
        return medicallyFragile;
    }

    /**
     * Sets the value of the medicallyFragile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMedicallyFragile(Boolean value) {
        this.medicallyFragile = value;
    }

    /**
     * Gets the value of the lastEvaluationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getLastEvaluationDate() {
        return lastEvaluationDate;
    }

    /**
     * Sets the value of the lastEvaluationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastEvaluationDate(Calendar value) {
        this.lastEvaluationDate = value;
    }

    /**
     * Gets the value of the iepReviewDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getIEPReviewDate() {
        return iepReviewDate;
    }

    /**
     * Sets the value of the iepReviewDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIEPReviewDate(Calendar value) {
        this.iepReviewDate = value;
    }

    /**
     * Gets the value of the iepBeginDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getIEPBeginDate() {
        return iepBeginDate;
    }

    /**
     * Sets the value of the iepBeginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIEPBeginDate(Calendar value) {
        this.iepBeginDate = value;
    }

    /**
     * Gets the value of the iepEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getIEPEndDate() {
        return iepEndDate;
    }

    /**
     * Sets the value of the iepEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIEPEndDate(Calendar value) {
        this.iepEndDate = value;
    }

}
