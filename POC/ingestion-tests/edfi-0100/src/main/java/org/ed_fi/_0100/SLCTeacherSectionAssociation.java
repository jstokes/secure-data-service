//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * TeacherSectionAssociation record with key fields: TeacherReference (StaffUniqueStateId) and SectionReference (StateOrganizationId, UniqueSectionCode). Changed types of TeacherReference and SectionReference to SLC reference types.
 * 
 * <p>Java class for SLC-TeacherSectionAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-TeacherSectionAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TeacherReference" type="{http://ed-fi.org/0100}SLC-StaffReferenceType"/>
 *         &lt;element name="SectionReference" type="{http://ed-fi.org/0100}SLC-SectionReferenceType"/>
 *         &lt;element name="ClassroomPosition" type="{http://ed-fi.org/0100}ClassroomPositionType"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="HighlyQualifiedTeacher" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-TeacherSectionAssociation", propOrder = {
    "teacherReference",
    "sectionReference",
    "classroomPosition",
    "beginDate",
    "endDate",
    "highlyQualifiedTeacher"
})
public class SLCTeacherSectionAssociation {

    @XmlElement(name = "TeacherReference", required = true)
    protected SLCStaffReferenceType teacherReference;
    @XmlElement(name = "SectionReference", required = true)
    protected SLCSectionReferenceType sectionReference;
    @XmlElement(name = "ClassroomPosition", required = true)
    protected ClassroomPositionType classroomPosition;
    @XmlElement(name = "BeginDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar beginDate;
    @XmlElement(name = "EndDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "HighlyQualifiedTeacher")
    protected Boolean highlyQualifiedTeacher;

    /**
     * Gets the value of the teacherReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStaffReferenceType }
     *     
     */
    public SLCStaffReferenceType getTeacherReference() {
        return teacherReference;
    }

    /**
     * Sets the value of the teacherReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStaffReferenceType }
     *     
     */
    public void setTeacherReference(SLCStaffReferenceType value) {
        this.teacherReference = value;
    }

    /**
     * Gets the value of the sectionReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCSectionReferenceType }
     *     
     */
    public SLCSectionReferenceType getSectionReference() {
        return sectionReference;
    }

    /**
     * Sets the value of the sectionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCSectionReferenceType }
     *     
     */
    public void setSectionReference(SLCSectionReferenceType value) {
        this.sectionReference = value;
    }

    /**
     * Gets the value of the classroomPosition property.
     * 
     * @return
     *     possible object is
     *     {@link ClassroomPositionType }
     *     
     */
    public ClassroomPositionType getClassroomPosition() {
        return classroomPosition;
    }

    /**
     * Sets the value of the classroomPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassroomPositionType }
     *     
     */
    public void setClassroomPosition(ClassroomPositionType value) {
        this.classroomPosition = value;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBeginDate(XMLGregorianCalendar value) {
        this.beginDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the highlyQualifiedTeacher property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHighlyQualifiedTeacher() {
        return highlyQualifiedTeacher;
    }

    /**
     * Sets the value of the highlyQualifiedTeacher property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHighlyQualifiedTeacher(Boolean value) {
        this.highlyQualifiedTeacher = value;
    }

}
