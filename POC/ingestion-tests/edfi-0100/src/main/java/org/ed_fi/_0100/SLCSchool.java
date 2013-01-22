//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * School record with key field: StateOrganizationId. Changed type of LocalEducationAgencyReference to SLC reference type.
 * 
 * <p>Java class for SLC-School complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-School">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}SLC-EducationOrganization">
 *       &lt;sequence>
 *         &lt;element name="GradesOffered" type="{http://ed-fi.org/0100}GradeLevelsType"/>
 *         &lt;element name="SchoolCategories" type="{http://ed-fi.org/0100}SchoolCategoriesType"/>
 *         &lt;element name="SchoolType" type="{http://ed-fi.org/0100}SchoolType" minOccurs="0"/>
 *         &lt;element name="CharterStatus" type="{http://ed-fi.org/0100}CharterStatusType" minOccurs="0"/>
 *         &lt;element name="TitleIPartASchoolDesignation" type="{http://ed-fi.org/0100}TitleIPartASchoolDesignationType" minOccurs="0"/>
 *         &lt;element name="MagnetSpecialProgramEmphasisSchool" type="{http://ed-fi.org/0100}MagnetSpecialProgramEmphasisSchoolType" minOccurs="0"/>
 *         &lt;element name="AdministrativeFundingControl" type="{http://ed-fi.org/0100}AdministrativeFundingControlType" minOccurs="0"/>
 *         &lt;element name="LocalEducationAgencyReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType" minOccurs="0"/>
 *         &lt;element name="ClassPeriodReference" type="{http://ed-fi.org/0100}ReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LocationReference" type="{http://ed-fi.org/0100}ReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-School", propOrder = {
    "gradesOffered",
    "schoolCategories",
    "schoolType",
    "charterStatus",
    "titleIPartASchoolDesignation",
    "magnetSpecialProgramEmphasisSchool",
    "administrativeFundingControl",
    "localEducationAgencyReference",
    "classPeriodReference",
    "locationReference"
})
public class SLCSchool
    extends SLCEducationOrganization
{

    @XmlElement(name = "GradesOffered", required = true)
    protected GradeLevelsType gradesOffered;
    @XmlElement(name = "SchoolCategories", required = true)
    protected SchoolCategoriesType schoolCategories;
    @XmlElement(name = "SchoolType")
    protected SchoolType schoolType;
    @XmlElement(name = "CharterStatus")
    protected CharterStatusType charterStatus;
    @XmlElement(name = "TitleIPartASchoolDesignation")
    protected TitleIPartASchoolDesignationType titleIPartASchoolDesignation;
    @XmlElement(name = "MagnetSpecialProgramEmphasisSchool")
    protected MagnetSpecialProgramEmphasisSchoolType magnetSpecialProgramEmphasisSchool;
    @XmlElement(name = "AdministrativeFundingControl")
    protected AdministrativeFundingControlType administrativeFundingControl;
    @XmlElement(name = "LocalEducationAgencyReference")
    protected SLCEducationalOrgReferenceType localEducationAgencyReference;
    @XmlElement(name = "ClassPeriodReference")
    protected List<ReferenceType> classPeriodReference;
    @XmlElement(name = "LocationReference")
    protected List<ReferenceType> locationReference;

    /**
     * Gets the value of the gradesOffered property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelsType }
     *     
     */
    public GradeLevelsType getGradesOffered() {
        return gradesOffered;
    }

    /**
     * Sets the value of the gradesOffered property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelsType }
     *     
     */
    public void setGradesOffered(GradeLevelsType value) {
        this.gradesOffered = value;
    }

    /**
     * Gets the value of the schoolCategories property.
     * 
     * @return
     *     possible object is
     *     {@link SchoolCategoriesType }
     *     
     */
    public SchoolCategoriesType getSchoolCategories() {
        return schoolCategories;
    }

    /**
     * Sets the value of the schoolCategories property.
     * 
     * @param value
     *     allowed object is
     *     {@link SchoolCategoriesType }
     *     
     */
    public void setSchoolCategories(SchoolCategoriesType value) {
        this.schoolCategories = value;
    }

    /**
     * Gets the value of the schoolType property.
     * 
     * @return
     *     possible object is
     *     {@link SchoolType }
     *     
     */
    public SchoolType getSchoolType() {
        return schoolType;
    }

    /**
     * Sets the value of the schoolType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SchoolType }
     *     
     */
    public void setSchoolType(SchoolType value) {
        this.schoolType = value;
    }

    /**
     * Gets the value of the charterStatus property.
     * 
     * @return
     *     possible object is
     *     {@link CharterStatusType }
     *     
     */
    public CharterStatusType getCharterStatus() {
        return charterStatus;
    }

    /**
     * Sets the value of the charterStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link CharterStatusType }
     *     
     */
    public void setCharterStatus(CharterStatusType value) {
        this.charterStatus = value;
    }

    /**
     * Gets the value of the titleIPartASchoolDesignation property.
     * 
     * @return
     *     possible object is
     *     {@link TitleIPartASchoolDesignationType }
     *     
     */
    public TitleIPartASchoolDesignationType getTitleIPartASchoolDesignation() {
        return titleIPartASchoolDesignation;
    }

    /**
     * Sets the value of the titleIPartASchoolDesignation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TitleIPartASchoolDesignationType }
     *     
     */
    public void setTitleIPartASchoolDesignation(TitleIPartASchoolDesignationType value) {
        this.titleIPartASchoolDesignation = value;
    }

    /**
     * Gets the value of the magnetSpecialProgramEmphasisSchool property.
     * 
     * @return
     *     possible object is
     *     {@link MagnetSpecialProgramEmphasisSchoolType }
     *     
     */
    public MagnetSpecialProgramEmphasisSchoolType getMagnetSpecialProgramEmphasisSchool() {
        return magnetSpecialProgramEmphasisSchool;
    }

    /**
     * Sets the value of the magnetSpecialProgramEmphasisSchool property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagnetSpecialProgramEmphasisSchoolType }
     *     
     */
    public void setMagnetSpecialProgramEmphasisSchool(MagnetSpecialProgramEmphasisSchoolType value) {
        this.magnetSpecialProgramEmphasisSchool = value;
    }

    /**
     * Gets the value of the administrativeFundingControl property.
     * 
     * @return
     *     possible object is
     *     {@link AdministrativeFundingControlType }
     *     
     */
    public AdministrativeFundingControlType getAdministrativeFundingControl() {
        return administrativeFundingControl;
    }

    /**
     * Sets the value of the administrativeFundingControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdministrativeFundingControlType }
     *     
     */
    public void setAdministrativeFundingControl(AdministrativeFundingControlType value) {
        this.administrativeFundingControl = value;
    }

    /**
     * Gets the value of the localEducationAgencyReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getLocalEducationAgencyReference() {
        return localEducationAgencyReference;
    }

    /**
     * Sets the value of the localEducationAgencyReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setLocalEducationAgencyReference(SLCEducationalOrgReferenceType value) {
        this.localEducationAgencyReference = value;
    }

    /**
     * Gets the value of the classPeriodReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classPeriodReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassPeriodReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     * 
     * 
     */
    public List<ReferenceType> getClassPeriodReference() {
        if (classPeriodReference == null) {
            classPeriodReference = new ArrayList<ReferenceType>();
        }
        return this.classPeriodReference;
    }

    /**
     * Gets the value of the locationReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReferenceType }
     * 
     * 
     */
    public List<ReferenceType> getLocationReference() {
        if (locationReference == null) {
            locationReference = new ArrayList<ReferenceType>();
        }
        return this.locationReference;
    }

}
