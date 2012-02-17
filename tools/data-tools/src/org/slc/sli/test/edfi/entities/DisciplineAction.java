//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.02.12 at 04:54:37 PM EST
//


package org.slc.sli.test.edfi.entities;

import java.math.BigInteger;
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
 * This event entity represents actions taken by an education organization after a disruptive event that is recorded as a discipline incident.
 *
 *
 * <p>Java class for DisciplineAction complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DisciplineAction">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="DisciplineActionIdentifier" type="{http://ed-fi.org/0100}DisciplineActionIdentifier"/>
 *         &lt;element name="Disciplines" type="{http://ed-fi.org/0100}DisciplineDescriptorType" maxOccurs="unbounded"/>
 *         &lt;element name="DisciplineDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DisciplineActionLength" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="ActualDisciplineActionLength" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="DisciplineActionLengthDifferenceReason" type="{http://ed-fi.org/0100}DisciplineActionLengthDifferenceReasonType" minOccurs="0"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}StudentReferenceType" maxOccurs="unbounded"/>
 *         &lt;element name="DisciplineIncidentReference" type="{http://ed-fi.org/0100}DisciplineIncidentReferenceType" maxOccurs="unbounded"/>
 *         &lt;element name="StaffReference" type="{http://ed-fi.org/0100}StaffReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ResponsibilitySchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType"/>
 *         &lt;element name="AssignmentSchoolReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisciplineAction", propOrder = {
    "disciplineActionIdentifier",
    "disciplines",
    "disciplineDate",
    "disciplineActionLength",
    "actualDisciplineActionLength",
    "disciplineActionLengthDifferenceReason",
    "studentReference",
    "disciplineIncidentReference",
    "staffReference",
    "responsibilitySchoolReference",
    "assignmentSchoolReference"
})
public class DisciplineAction
    extends ComplexObjectType
{

    @XmlElement(name = "DisciplineActionIdentifier", required = true)
    protected String disciplineActionIdentifier;
    @XmlElement(name = "Disciplines", required = true)
    protected List<DisciplineDescriptorType> disciplines;
    @XmlElement(name = "DisciplineDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar disciplineDate;
    @XmlElement(name = "DisciplineActionLength")
    protected BigInteger disciplineActionLength;
    @XmlElement(name = "ActualDisciplineActionLength")
    protected BigInteger actualDisciplineActionLength;
    @XmlElement(name = "DisciplineActionLengthDifferenceReason")
    protected DisciplineActionLengthDifferenceReasonType disciplineActionLengthDifferenceReason;
    @XmlElement(name = "StudentReference", required = true)
    protected List<StudentReferenceType> studentReference;
    @XmlElement(name = "DisciplineIncidentReference", required = true)
    protected List<DisciplineIncidentReferenceType> disciplineIncidentReference;
    @XmlElement(name = "StaffReference")
    protected List<StaffReferenceType> staffReference;
    @XmlElement(name = "ResponsibilitySchoolReference", required = true)
    protected EducationalOrgReferenceType responsibilitySchoolReference;
    @XmlElement(name = "AssignmentSchoolReference")
    protected EducationalOrgReferenceType assignmentSchoolReference;

    /**
     * Gets the value of the disciplineActionIdentifier property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDisciplineActionIdentifier() {
        return disciplineActionIdentifier;
    }

    /**
     * Sets the value of the disciplineActionIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDisciplineActionIdentifier(String value) {
        this.disciplineActionIdentifier = value;
    }

    /**
     * Gets the value of the disciplines property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disciplines property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisciplines().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DisciplineDescriptorType }
     *
     *
     */
    public List<DisciplineDescriptorType> getDisciplines() {
        if (disciplines == null) {
            disciplines = new ArrayList<DisciplineDescriptorType>();
        }
        return this.disciplines;
    }

    /**
     * Gets the value of the disciplineDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Calendar getDisciplineDate() {
        return disciplineDate;
    }

    /**
     * Sets the value of the disciplineDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDisciplineDate(Calendar value) {
        this.disciplineDate = value;
    }

    /**
     * Gets the value of the disciplineActionLength property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getDisciplineActionLength() {
        return disciplineActionLength;
    }

    /**
     * Sets the value of the disciplineActionLength property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setDisciplineActionLength(BigInteger value) {
        this.disciplineActionLength = value;
    }

    /**
     * Gets the value of the actualDisciplineActionLength property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getActualDisciplineActionLength() {
        return actualDisciplineActionLength;
    }

    /**
     * Sets the value of the actualDisciplineActionLength property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setActualDisciplineActionLength(BigInteger value) {
        this.actualDisciplineActionLength = value;
    }

    /**
     * Gets the value of the disciplineActionLengthDifferenceReason property.
     *
     * @return
     *     possible object is
     *     {@link DisciplineActionLengthDifferenceReasonType }
     *
     */
    public DisciplineActionLengthDifferenceReasonType getDisciplineActionLengthDifferenceReason() {
        return disciplineActionLengthDifferenceReason;
    }

    /**
     * Sets the value of the disciplineActionLengthDifferenceReason property.
     *
     * @param value
     *     allowed object is
     *     {@link DisciplineActionLengthDifferenceReasonType }
     *
     */
    public void setDisciplineActionLengthDifferenceReason(DisciplineActionLengthDifferenceReasonType value) {
        this.disciplineActionLengthDifferenceReason = value;
    }

    /**
     * Gets the value of the studentReference property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studentReference property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudentReference().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StudentReferenceType }
     *
     *
     */
    public List<StudentReferenceType> getStudentReference() {
        if (studentReference == null) {
            studentReference = new ArrayList<StudentReferenceType>();
        }
        return this.studentReference;
    }

    /**
     * Gets the value of the disciplineIncidentReference property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disciplineIncidentReference property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisciplineIncidentReference().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DisciplineIncidentReferenceType }
     *
     *
     */
    public List<DisciplineIncidentReferenceType> getDisciplineIncidentReference() {
        if (disciplineIncidentReference == null) {
            disciplineIncidentReference = new ArrayList<DisciplineIncidentReferenceType>();
        }
        return this.disciplineIncidentReference;
    }

    /**
     * Gets the value of the staffReference property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the staffReference property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStaffReference().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StaffReferenceType }
     *
     *
     */
    public List<StaffReferenceType> getStaffReference() {
        if (staffReference == null) {
            staffReference = new ArrayList<StaffReferenceType>();
        }
        return this.staffReference;
    }

    /**
     * Gets the value of the responsibilitySchoolReference property.
     *
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *
     */
    public EducationalOrgReferenceType getResponsibilitySchoolReference() {
        return responsibilitySchoolReference;
    }

    /**
     * Sets the value of the responsibilitySchoolReference property.
     *
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *
     */
    public void setResponsibilitySchoolReference(EducationalOrgReferenceType value) {
        this.responsibilitySchoolReference = value;
    }

    /**
     * Gets the value of the assignmentSchoolReference property.
     *
     * @return
     *     possible object is
     *     {@link EducationalOrgReferenceType }
     *
     */
    public EducationalOrgReferenceType getAssignmentSchoolReference() {
        return assignmentSchoolReference;
    }

    /**
     * Sets the value of the assignmentSchoolReference property.
     *
     * @param value
     *     allowed object is
     *     {@link EducationalOrgReferenceType }
     *
     */
    public void setAssignmentSchoolReference(EducationalOrgReferenceType value) {
        this.assignmentSchoolReference = value;
    }

}
