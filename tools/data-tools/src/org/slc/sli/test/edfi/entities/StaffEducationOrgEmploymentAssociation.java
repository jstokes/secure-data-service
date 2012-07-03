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
 * This association indicates the education organization an employee, contractor, volunteer or other service provider is formally associated with, typically indicated by which organization the staff member has a services contract with or receives their compensation.   
 * 
 * <p>Java class for StaffEducationOrgEmploymentAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StaffEducationOrgEmploymentAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StaffReference" type="{http://ed-fi.org/0100}StaffReferenceType"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="EmploymentStatus" type="{http://ed-fi.org/0100}EmploymentStatusType"/>
 *         &lt;element name="EmploymentPeriod" type="{http://ed-fi.org/0100}EmploymentPeriod"/>
 *         &lt;element name="Department" type="{http://ed-fi.org/0100}Department" minOccurs="0"/>
 *         &lt;element name="FullTimeEquivalency" type="{http://ed-fi.org/0100}percent" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StaffEducationOrgEmploymentAssociation", propOrder = {
    "staffReference",
    "educationOrganizationReference",
    "employmentStatus",
    "employmentPeriod",
    "department",
    "fullTimeEquivalency"
})
public class StaffEducationOrgEmploymentAssociation {

    @XmlElement(name = "StaffReference", required = true)
    protected StaffReferenceType staffReference;
    @XmlElement(name = "EducationOrganizationReference", required = true)
    protected EducationalOrgReferenceType educationOrganizationReference;
    @XmlElement(name = "EmploymentStatus", required = true)
    protected EmploymentStatusType employmentStatus;
    @XmlElement(name = "EmploymentPeriod", required = true)
    protected EmploymentPeriod employmentPeriod;
    @XmlElement(name = "Department")
    protected String department;
    @XmlElement(name = "FullTimeEquivalency")
    protected Integer fullTimeEquivalency;

    /**
     * Gets the value of the staffReference property.
     * 
     * @return
     *     possible object is
     *     {@link StaffReferenceType }
     *     
     */
    public StaffReferenceType getStaffReference() {
        return staffReference;
    }

    /**
     * Sets the value of the staffReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaffReferenceType }
     *     
     */
    public void setStaffReference(StaffReferenceType value) {
        this.staffReference = value;
    }

    /**
     * Gets the value of the educationOrganizationReference property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public EducationalOrgReferenceType getEducationOrganizationReference() {
        return educationOrganizationReference;
    }

    /**
     * Sets the value of the educationOrganizationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *     
     */
    public void setEducationOrganizationReference(EducationalOrgReferenceType value) {
        this.educationOrganizationReference = value;
    }

    /**
     * Gets the value of the employmentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link EmploymentStatusType }
     *     
     */
    public EmploymentStatusType getEmploymentStatus() {
        return employmentStatus;
    }

    /**
     * Sets the value of the employmentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmploymentStatusType }
     *     
     */
    public void setEmploymentStatus(EmploymentStatusType value) {
        this.employmentStatus = value;
    }

    /**
     * Gets the value of the employmentPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link EmploymentPeriod }
     *     
     */
    public EmploymentPeriod getEmploymentPeriod() {
        return employmentPeriod;
    }

    /**
     * Sets the value of the employmentPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmploymentPeriod }
     *     
     */
    public void setEmploymentPeriod(EmploymentPeriod value) {
        this.employmentPeriod = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the fullTimeEquivalency property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFullTimeEquivalency() {
        return fullTimeEquivalency;
    }

    /**
     * Sets the value of the fullTimeEquivalency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFullTimeEquivalency(Integer value) {
        this.fullTimeEquivalency = value;
    }

}
