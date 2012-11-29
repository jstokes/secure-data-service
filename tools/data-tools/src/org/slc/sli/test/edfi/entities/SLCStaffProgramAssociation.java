//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.11.16 at 01:39:34 PM EST
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This association indicates the staff associated with a program.
 *
 * <p>Java class for SLC-StaffProgramAssociation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SLC-StaffProgramAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StaffReference" type="{http://ed-fi.org/0100}SLC-StaffReferenceType"/>
 *         &lt;element name="ProgramReference" type="{http://ed-fi.org/0100}SLC-ProgramReferenceType"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="StudentRecordAccess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StaffProgramAssociation", propOrder = {
    "staffReference",
    "programReference",
    "beginDate",
    "endDate",
    "studentRecordAccess"
})

@XmlRootElement(name = "StaffProgramAssociation")
public class SLCStaffProgramAssociation {

    @XmlElement(name = "StaffReference", required = true)
    protected SLCStaffReferenceType staffReference;
    @XmlElement(name = "ProgramReference", required = true)
    protected SLCProgramReferenceType programReference;
    @XmlElement(name = "BeginDate", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String beginDate;
    @XmlElement(name = "EndDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String endDate;
    @XmlElement(name = "StudentRecordAccess")
    protected Boolean studentRecordAccess;

    /**
     * Gets the value of the staffReference property.
     *
     * @return
     *     possible object is
     *     {@link SLCStaffReferenceType }
     *
     */
    public SLCStaffReferenceType getStaffReference() {
        return staffReference;
    }

    /**
     * Sets the value of the staffReference property.
     *
     * @param value
     *     allowed object is
     *     {@link SLCStaffReferenceType }
     *
     */
    public void setStaffReference(SLCStaffReferenceType value) {
        this.staffReference = value;
    }

    /**
     * Gets the value of the programReference property.
     *
     * @return
     *     possible object is
     *     {@link SLCProgramReferenceType }
     *
     */
    public SLCProgramReferenceType getProgramReference() {
        return programReference;
    }

    /**
     * Sets the value of the programReference property.
     *
     * @param value
     *     allowed object is
     *     {@link SLCProgramReferenceType }
     *
     */
    public void setProgramReference(SLCProgramReferenceType value) {
        this.programReference = value;
    }

    /**
     * Gets the value of the beginDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBeginDate(String value) {
        this.beginDate = value;
    }

    /**
     * Gets the value of the endDate property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the studentRecordAccess property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isStudentRecordAccess() {
        return studentRecordAccess;
    }

    /**
     * Sets the value of the studentRecordAccess property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setStudentRecordAccess(Boolean value) {
        this.studentRecordAccess = value;
    }

}
