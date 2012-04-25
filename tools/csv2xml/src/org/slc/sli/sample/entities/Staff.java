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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity represents an individual who performs specified activities for any public or private education institution or agency that provides instructional and/or support services to students or staff at the early childhood level through high school completion. For example, this includes: 
 *  1) an "employee" who performs services under the direction of the employing institution or agency, is compensated for such services by the employer, and is eligible for employee benefits and wage or salary tax withholdings; 
 *  2) a "contractor" or "consultant" who performs services for an agreed upon fee, or an employee of a management service contracted to work on site; 
 *  3) a "volunteer" who performs services on a voluntary and uncompensated basis; 
 *  4) an in kind service provider; or 
 *  5) an independent contractor or businessperson working at a school site.
 * 
 * <p>Java class for Staff complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Staff">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="StaffUniqueStateId" type="{http://ed-fi.org/0100}UniqueStateIdentifier"/>
 *         &lt;element name="StaffIdentificationCode" type="{http://ed-fi.org/0100}StaffIdentificationCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://ed-fi.org/0100}Name"/>
 *         &lt;element name="OtherName" type="{http://ed-fi.org/0100}OtherName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Sex" type="{http://ed-fi.org/0100}SexType"/>
 *         &lt;element name="BirthDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://ed-fi.org/0100}Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Telephone" type="{http://ed-fi.org/0100}Telephone" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ElectronicMail" type="{http://ed-fi.org/0100}ElectronicMail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="HispanicLatinoEthnicity" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OldEthnicity" type="{http://ed-fi.org/0100}OldEthnicityType" minOccurs="0"/>
 *         &lt;element name="Race" type="{http://ed-fi.org/0100}RaceType"/>
 *         &lt;element name="HighestLevelOfEducationCompleted" type="{http://ed-fi.org/0100}LevelOfEducationType"/>
 *         &lt;element name="YearsOfPriorProfessionalExperience" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="YearsOfPriorTeachingExperience" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Credentials" type="{http://ed-fi.org/0100}Credential" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LoginId" type="{http://ed-fi.org/0100}IdentificationCode" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Staff", propOrder = {
    "staffUniqueStateId",
    "staffIdentificationCode",
    "name",
    "otherName",
    "sex",
    "birthDate",
    "address",
    "telephone",
    "electronicMail",
    "hispanicLatinoEthnicity",
    "oldEthnicity",
    "race",
    "highestLevelOfEducationCompleted",
    "yearsOfPriorProfessionalExperience",
    "yearsOfPriorTeachingExperience",
    "credentials",
    "loginId"
})
@XmlSeeAlso({
    Teacher.class
})
public class Staff
    extends ComplexObjectType
{

    @XmlElement(name = "StaffUniqueStateId", required = true)
    protected String staffUniqueStateId;
    @XmlElement(name = "StaffIdentificationCode")
    protected List<StaffIdentificationCode> staffIdentificationCode;
    @XmlElement(name = "Name", required = true)
    protected Name name;
    @XmlElement(name = "OtherName")
    protected List<OtherName> otherName;
    @XmlElement(name = "Sex", required = true)
    protected SexType sex;
    @XmlElement(name = "BirthDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar birthDate;
    @XmlElement(name = "Address")
    protected List<Address> address;
    @XmlElement(name = "Telephone")
    protected List<Telephone> telephone;
    @XmlElement(name = "ElectronicMail")
    protected List<ElectronicMail> electronicMail;
    @XmlElement(name = "HispanicLatinoEthnicity", required = true, type = Boolean.class, nillable = true)
    protected Boolean hispanicLatinoEthnicity;
    @XmlElement(name = "OldEthnicity")
    protected OldEthnicityType oldEthnicity;
    @XmlElement(name = "Race", required = true)
    protected RaceType race;
    @XmlElement(name = "HighestLevelOfEducationCompleted", required = true)
    protected LevelOfEducationType highestLevelOfEducationCompleted;
    @XmlElement(name = "YearsOfPriorProfessionalExperience")
    protected Integer yearsOfPriorProfessionalExperience;
    @XmlElement(name = "YearsOfPriorTeachingExperience")
    protected Integer yearsOfPriorTeachingExperience;
    @XmlElement(name = "Credentials")
    protected List<Credential> credentials;
    @XmlElement(name = "LoginId")
    protected String loginId;

    /**
     * Gets the value of the staffUniqueStateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStaffUniqueStateId() {
        return staffUniqueStateId;
    }

    /**
     * Sets the value of the staffUniqueStateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStaffUniqueStateId(String value) {
        this.staffUniqueStateId = value;
    }

    /**
     * Gets the value of the staffIdentificationCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the staffIdentificationCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStaffIdentificationCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StaffIdentificationCode }
     * 
     * 
     */
    public List<StaffIdentificationCode> getStaffIdentificationCode() {
        if (staffIdentificationCode == null) {
            staffIdentificationCode = new ArrayList<StaffIdentificationCode>();
        }
        return this.staffIdentificationCode;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link Name }
     *     
     */
    public Name getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link Name }
     *     
     */
    public void setName(Name value) {
        this.name = value;
    }

    /**
     * Gets the value of the otherName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherName }
     * 
     * 
     */
    public List<OtherName> getOtherName() {
        if (otherName == null) {
            otherName = new ArrayList<OtherName>();
        }
        return this.otherName;
    }

    /**
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link SexType }
     *     
     */
    public SexType getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link SexType }
     *     
     */
    public void setSex(SexType value) {
        this.sex = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(Calendar value) {
        this.birthDate = value;
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
     * {@link Telephone }
     * 
     * 
     */
    public List<Telephone> getTelephone() {
        if (telephone == null) {
            telephone = new ArrayList<Telephone>();
        }
        return this.telephone;
    }

    /**
     * Gets the value of the electronicMail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the electronicMail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElectronicMail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ElectronicMail }
     * 
     * 
     */
    public List<ElectronicMail> getElectronicMail() {
        if (electronicMail == null) {
            electronicMail = new ArrayList<ElectronicMail>();
        }
        return this.electronicMail;
    }

    /**
     * Gets the value of the hispanicLatinoEthnicity property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHispanicLatinoEthnicity() {
        return hispanicLatinoEthnicity;
    }

    /**
     * Sets the value of the hispanicLatinoEthnicity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHispanicLatinoEthnicity(Boolean value) {
        this.hispanicLatinoEthnicity = value;
    }

    /**
     * Gets the value of the oldEthnicity property.
     * 
     * @return
     *     possible object is
     *     {@link OldEthnicityType }
     *     
     */
    public OldEthnicityType getOldEthnicity() {
        return oldEthnicity;
    }

    /**
     * Sets the value of the oldEthnicity property.
     * 
     * @param value
     *     allowed object is
     *     {@link OldEthnicityType }
     *     
     */
    public void setOldEthnicity(OldEthnicityType value) {
        this.oldEthnicity = value;
    }

    /**
     * Gets the value of the race property.
     * 
     * @return
     *     possible object is
     *     {@link RaceType }
     *     
     */
    public RaceType getRace() {
        return race;
    }

    /**
     * Sets the value of the race property.
     * 
     * @param value
     *     allowed object is
     *     {@link RaceType }
     *     
     */
    public void setRace(RaceType value) {
        this.race = value;
    }

    /**
     * Gets the value of the highestLevelOfEducationCompleted property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfEducationType }
     *     
     */
    public LevelOfEducationType getHighestLevelOfEducationCompleted() {
        return highestLevelOfEducationCompleted;
    }

    /**
     * Sets the value of the highestLevelOfEducationCompleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfEducationType }
     *     
     */
    public void setHighestLevelOfEducationCompleted(LevelOfEducationType value) {
        this.highestLevelOfEducationCompleted = value;
    }

    /**
     * Gets the value of the yearsOfPriorProfessionalExperience property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearsOfPriorProfessionalExperience() {
        return yearsOfPriorProfessionalExperience;
    }

    /**
     * Sets the value of the yearsOfPriorProfessionalExperience property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearsOfPriorProfessionalExperience(Integer value) {
        this.yearsOfPriorProfessionalExperience = value;
    }

    /**
     * Gets the value of the yearsOfPriorTeachingExperience property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getYearsOfPriorTeachingExperience() {
        return yearsOfPriorTeachingExperience;
    }

    /**
     * Sets the value of the yearsOfPriorTeachingExperience property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setYearsOfPriorTeachingExperience(Integer value) {
        this.yearsOfPriorTeachingExperience = value;
    }

    /**
     * Gets the value of the credentials property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the credentials property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCredentials().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Credential }
     * 
     * 
     */
    public List<Credential> getCredentials() {
        if (credentials == null) {
            credentials = new ArrayList<Credential>();
        }
        return this.credentials;
    }

    /**
     * Gets the value of the loginId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * Sets the value of the loginId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginId(String value) {
        this.loginId = value;
    }

}
