//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.18 at 04:15:23 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;


/**
 * This association represents the school to which a student is enrolled.
 * 
 * <p>Java class for StudentSchoolAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentSchoolAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType"/>
 *         &lt;element name="SchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="SchoolYear" type="{http://ed-fi.org/0100}SchoolYearType" minOccurs="0"/>
 *         &lt;element name="EntryDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EntryGradeLevel" type="{http://ed-fi.org/0100}GradeLevelType"/>
 *         &lt;element name="EntryType" type="{http://ed-fi.org/0100}EntryType" minOccurs="0"/>
 *         &lt;element name="RepeatGradeIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ClassOf" type="{http://ed-fi.org/0100}SchoolYearType" minOccurs="0"/>
 *         &lt;element name="SchoolChoiceTransfer" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ExitWithdrawDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ExitWithdrawType" type="{http://ed-fi.org/0100}ExitWithdrawType" minOccurs="0"/>
 *         &lt;element name="EducationalPlans" type="{http://ed-fi.org/0100}EducationalPlansType" minOccurs="0"/>
 *         &lt;element name="GraduationPlanReference" type="{http://ed-fi.org/0100}ReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentSchoolAssociation", propOrder = {
    "studentReference",
    "schoolReference",
    "schoolYear",
    "entryDate",
    "entryGradeLevel",
    "entryType",
    "repeatGradeIndicator",
    "classOf",
    "schoolChoiceTransfer",
    "exitWithdrawDate",
    "exitWithdrawType",
    "educationalPlans",
    "graduationPlanReference"
})
public class StudentSchoolAssociation
    implements Locatable
{

    @XmlElement(name = "StudentReference", required = true)
    protected StudentReferenceType studentReference;
    @XmlElement(name = "SchoolReference", required = true)
    protected EducationalOrgReferenceType schoolReference;
    @XmlElement(name = "SchoolYear")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String schoolYear;
    @XmlElement(name = "EntryDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar entryDate;
    @XmlElement(name = "EntryGradeLevel", required = true)
    protected GradeLevelType entryGradeLevel;
    @XmlElement(name = "EntryType")
    protected EntryType entryType;
    @XmlElement(name = "RepeatGradeIndicator")
    protected Boolean repeatGradeIndicator;
    @XmlElement(name = "ClassOf")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String classOf;
    @XmlElement(name = "SchoolChoiceTransfer")
    protected Boolean schoolChoiceTransfer;
    @XmlElement(name = "ExitWithdrawDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar exitWithdrawDate;
    @XmlElement(name = "ExitWithdrawType")
    protected ExitWithdrawType exitWithdrawType;
    @XmlElement(name = "EducationalPlans")
    protected EducationalPlansType educationalPlans;
    @XmlElement(name = "GraduationPlanReference")
    protected ReferenceType graduationPlanReference;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    /**
     * Gets the value of the studentReference property.
     * 
     * @return
     *     possible object is
     *     {@link StudentReferenceType }
     *     
     */
    public StudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentReferenceType }
     *     
     */
    public void setStudentReference(StudentReferenceType value) {
        this.studentReference = value;
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

    /**
     * Gets the value of the schoolYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchoolYear() {
        return schoolYear;
    }

    /**
     * Sets the value of the schoolYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchoolYear(String value) {
        this.schoolYear = value;
    }

    /**
     * Gets the value of the entryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEntryDate() {
        return entryDate;
    }

    /**
     * Sets the value of the entryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEntryDate(XMLGregorianCalendar value) {
        this.entryDate = value;
    }

    /**
     * Gets the value of the entryGradeLevel property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getEntryGradeLevel() {
        return entryGradeLevel;
    }

    /**
     * Sets the value of the entryGradeLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setEntryGradeLevel(GradeLevelType value) {
        this.entryGradeLevel = value;
    }

    /**
     * Gets the value of the entryType property.
     * 
     * @return
     *     possible object is
     *     {@link EntryType }
     *     
     */
    public EntryType getEntryType() {
        return entryType;
    }

    /**
     * Sets the value of the entryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryType }
     *     
     */
    public void setEntryType(EntryType value) {
        this.entryType = value;
    }

    /**
     * Gets the value of the repeatGradeIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRepeatGradeIndicator() {
        return repeatGradeIndicator;
    }

    /**
     * Sets the value of the repeatGradeIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRepeatGradeIndicator(Boolean value) {
        this.repeatGradeIndicator = value;
    }

    /**
     * Gets the value of the classOf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassOf() {
        return classOf;
    }

    /**
     * Sets the value of the classOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassOf(String value) {
        this.classOf = value;
    }

    /**
     * Gets the value of the schoolChoiceTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSchoolChoiceTransfer() {
        return schoolChoiceTransfer;
    }

    /**
     * Sets the value of the schoolChoiceTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSchoolChoiceTransfer(Boolean value) {
        this.schoolChoiceTransfer = value;
    }

    /**
     * Gets the value of the exitWithdrawDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExitWithdrawDate() {
        return exitWithdrawDate;
    }

    /**
     * Sets the value of the exitWithdrawDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExitWithdrawDate(XMLGregorianCalendar value) {
        this.exitWithdrawDate = value;
    }

    /**
     * Gets the value of the exitWithdrawType property.
     * 
     * @return
     *     possible object is
     *     {@link ExitWithdrawType }
     *     
     */
    public ExitWithdrawType getExitWithdrawType() {
        return exitWithdrawType;
    }

    /**
     * Sets the value of the exitWithdrawType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExitWithdrawType }
     *     
     */
    public void setExitWithdrawType(ExitWithdrawType value) {
        this.exitWithdrawType = value;
    }

    /**
     * Gets the value of the educationalPlans property.
     * 
     * @return
     *     possible object is
     *     {@link EducationalPlansType }
     *     
     */
    public EducationalPlansType getEducationalPlans() {
        return educationalPlans;
    }

    /**
     * Sets the value of the educationalPlans property.
     * 
     * @param value
     *     allowed object is
     *     {@link EducationalPlansType }
     *     
     */
    public void setEducationalPlans(EducationalPlansType value) {
        this.educationalPlans = value;
    }

    /**
     * Gets the value of the graduationPlanReference property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceType }
     *     
     */
    public ReferenceType getGraduationPlanReference() {
        return graduationPlanReference;
    }

    /**
     * Sets the value of the graduationPlanReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceType }
     *     
     */
    public void setGraduationPlanReference(ReferenceType value) {
        this.graduationPlanReference = value;
    }

    public Locator sourceLocation() {
        return locator;
    }

    public void setSourceLocation(Locator newLocator) {
        locator = newLocator;
    }

}
