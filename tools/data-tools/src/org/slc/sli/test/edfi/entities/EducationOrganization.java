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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents any public or private institution, organization, or agency that provides instructional or support services to students or staff at any level. 
 * 
 * <p>Java class for EducationOrganization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EducationOrganization">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="StateOrganizationId" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *         &lt;element name="EducationOrgIdentificationCode" type="{http://ed-fi.org/0100}EducationOrgIdentificationCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="NameOfInstitution" type="{http://ed-fi.org/0100}NameOfInstitution"/>
 *         &lt;element name="ShortNameOfInstitution" type="{http://ed-fi.org/0100}NameOfInstitution" minOccurs="0"/>
 *         &lt;element name="OrganizationCategories" type="{http://ed-fi.org/0100}EducationOrganizationCategoriesType"/>
 *         &lt;element name="Address" type="{http://ed-fi.org/0100}Address" maxOccurs="unbounded"/>
 *         &lt;element name="Telephone" type="{http://ed-fi.org/0100}InstitutionTelephone" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="WebSite" type="{http://ed-fi.org/0100}WebSite" minOccurs="0"/>
 *         &lt;element name="OperationalStatus" type="{http://ed-fi.org/0100}OperationalStatusType" minOccurs="0"/>
 *         &lt;element name="AccountabilityRatings" type="{http://ed-fi.org/0100}AccountabilityRating" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProgramReference" type="{http://ed-fi.org/0100}ProgramReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="EducationOrganizationPeerReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EducationOrganization", propOrder = {
    "stateOrganizationId",
    "educationOrgIdentificationCode",
    "nameOfInstitution",
    "shortNameOfInstitution",
    "organizationCategories",
    "address",
    "telephone",
    "webSite",
    "operationalStatus",
    "accountabilityRatings",
    "programReference",
    "educationOrganizationPeerReference"
})
@XmlSeeAlso({
    School.class,
    StateEducationAgency.class,
    EducationServiceCenter.class,
    LocalEducationAgency.class
})
public abstract class EducationOrganization
    extends ComplexObjectType
{

    @XmlElement(name = "StateOrganizationId", required = true)
    protected String stateOrganizationId;
    @XmlElement(name = "EducationOrgIdentificationCode")
    protected List<EducationOrgIdentificationCode> educationOrgIdentificationCode;
    @XmlElement(name = "NameOfInstitution", required = true)
    protected String nameOfInstitution;
    @XmlElement(name = "ShortNameOfInstitution")
    protected String shortNameOfInstitution;
    @XmlElement(name = "OrganizationCategories", required = true)
    protected EducationOrganizationCategoriesType organizationCategories;
    @XmlElement(name = "Address", required = true)
    protected List<Address> address;
    @XmlElement(name = "Telephone")
    protected List<InstitutionTelephone> telephone;
    @XmlElement(name = "WebSite")
    protected String webSite;
    @XmlElement(name = "OperationalStatus")
    protected OperationalStatusType operationalStatus;
    @XmlElement(name = "AccountabilityRatings")
    protected List<AccountabilityRating> accountabilityRatings;
    @XmlElement(name = "ProgramReference")
    protected List<ProgramReferenceType> programReference;
    @XmlElement(name = "EducationOrganizationPeerReference")
    protected List<EducationalOrgReferenceType> educationOrganizationPeerReference;

    /**
     * Gets the value of the stateOrganizationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateOrganizationId() {
        return stateOrganizationId;
    }

    /**
     * Sets the value of the stateOrganizationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateOrganizationId(String value) {
        this.stateOrganizationId = value;
    }

    /**
     * Gets the value of the educationOrgIdentificationCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationOrgIdentificationCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationOrgIdentificationCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EducationOrgIdentificationCode }
     * 
     * 
     */
    public List<EducationOrgIdentificationCode> getEducationOrgIdentificationCode() {
        if (educationOrgIdentificationCode == null) {
            educationOrgIdentificationCode = new ArrayList<EducationOrgIdentificationCode>();
        }
        return this.educationOrgIdentificationCode;
    }

    /**
     * Gets the value of the nameOfInstitution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfInstitution() {
        return nameOfInstitution;
    }

    /**
     * Sets the value of the nameOfInstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfInstitution(String value) {
        this.nameOfInstitution = value;
    }

    /**
     * Gets the value of the shortNameOfInstitution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortNameOfInstitution() {
        return shortNameOfInstitution;
    }

    /**
     * Sets the value of the shortNameOfInstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortNameOfInstitution(String value) {
        this.shortNameOfInstitution = value;
    }

    /**
     * Gets the value of the organizationCategories property.
     * 
     * @return
     *     possible object is
     *     {@link EducationOrganizationCategoriesType }
     *     
     */
    public EducationOrganizationCategoriesType getOrganizationCategories() {
        return organizationCategories;
    }

    /**
     * Sets the value of the organizationCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationOrganizationCategoriesType }
     *     
     */
    public void setOrganizationCategories(EducationOrganizationCategoriesType value) {
        this.organizationCategories = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the address property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Address }
     * 
     * 
     */
    public List<Address> getAddress() {
        if (address == null) {
            address = new ArrayList<Address>();
        }
        return this.address;
    }

    /**
     * Gets the value of the telephone property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telephone property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelephone().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InstitutionTelephone }
     * 
     * 
     */
    public List<InstitutionTelephone> getTelephone() {
        if (telephone == null) {
            telephone = new ArrayList<InstitutionTelephone>();
        }
        return this.telephone;
    }

    /**
     * Gets the value of the webSite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * Sets the value of the webSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebSite(String value) {
        this.webSite = value;
    }

    /**
     * Gets the value of the operationalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link OperationalStatusType }
     *     
     */
    public OperationalStatusType getOperationalStatus() {
        return operationalStatus;
    }

    /**
     * Sets the value of the operationalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationalStatusType }
     *     
     */
    public void setOperationalStatus(OperationalStatusType value) {
        this.operationalStatus = value;
    }

    /**
     * Gets the value of the accountabilityRatings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accountabilityRatings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccountabilityRatings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountabilityRating }
     * 
     * 
     */
    public List<AccountabilityRating> getAccountabilityRatings() {
        if (accountabilityRatings == null) {
            accountabilityRatings = new ArrayList<AccountabilityRating>();
        }
        return this.accountabilityRatings;
    }

    /**
     * Gets the value of the programReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProgramReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProgramReferenceType }
     * 
     * 
     */
    public List<ProgramReferenceType> getProgramReference() {
        if (programReference == null) {
            programReference = new ArrayList<ProgramReferenceType>();
        }
        return this.programReference;
    }

    /**
     * Gets the value of the educationOrganizationPeerReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationOrganizationPeerReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationOrganizationPeerReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EducationalOrgReferenceType }
     * 
     * 
     */
    public List<EducationalOrgReferenceType> getEducationOrganizationPeerReference() {
        if (educationOrganizationPeerReference == null) {
            educationOrganizationPeerReference = new ArrayList<EducationalOrgReferenceType>();
        }
        return this.educationOrganizationPeerReference;
    }

}
