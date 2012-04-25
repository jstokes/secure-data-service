//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity represents a parent or guardian of a student, such as mother, father, or caretaker.
 * 
 * <p>Java class for Parent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Parent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="ParentUniqueStateId" type="{http://ed-fi.org/0100}UniqueStateIdentifier"/>
 *         &lt;element name="Name" type="{http://ed-fi.org/0100}Name"/>
 *         &lt;element name="OtherName" type="{http://ed-fi.org/0100}OtherName" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Sex" type="{http://ed-fi.org/0100}SexType" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://ed-fi.org/0100}Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Telephone" type="{http://ed-fi.org/0100}Telephone" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ElectronicMail" type="{http://ed-fi.org/0100}ElectronicMail" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "Parent", propOrder = {
    "parentUniqueStateId",
    "name",
    "otherName",
    "sex",
    "address",
    "telephone",
    "electronicMail",
    "loginId"
})
public class Parent
    extends ComplexObjectType
{

    @XmlElement(name = "ParentUniqueStateId", required = true)
    protected String parentUniqueStateId;
    @XmlElement(name = "Name", required = true)
    protected Name name;
    @XmlElement(name = "OtherName")
    protected List<OtherName> otherName;
    @XmlElement(name = "Sex")
    protected SexType sex;
    @XmlElement(name = "Address")
    protected List<Address> address;
    @XmlElement(name = "Telephone")
    protected List<Telephone> telephone;
    @XmlElement(name = "ElectronicMail")
    protected List<ElectronicMail> electronicMail;
    @XmlElement(name = "LoginId")
    protected String loginId;

    /**
     * Gets the value of the parentUniqueStateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentUniqueStateId() {
        return parentUniqueStateId;
    }

    /**
     * Sets the value of the parentUniqueStateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentUniqueStateId(String value) {
        this.parentUniqueStateId = value;
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
