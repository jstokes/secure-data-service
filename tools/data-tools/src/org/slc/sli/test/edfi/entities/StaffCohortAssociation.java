//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

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
 * This association indicates the staff associated with a cohort of students.
 * 
 * <p>Java class for StaffCohortAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StaffCohortAssociation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StaffReference" type="{http://ed-fi.org/0100}StaffReferenceType" maxOccurs="unbounded"/>
 *         &lt;element name="CohortReference" type="{http://ed-fi.org/0100}CohortReferenceType" maxOccurs="unbounded"/>
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
@XmlType(name = "StaffCohortAssociation", propOrder = {
    "staffReference",
    "cohortReference",
    "beginDate",
    "endDate",
    "studentRecordAccess"
})
public class StaffCohortAssociation {

    @XmlElement(name = "StaffReference", required = true)
    protected List<StaffReferenceType> staffReference;
    @XmlElement(name = "CohortReference", required = true)
    protected List<CohortReferenceType> cohortReference;
    @XmlElement(name = "BeginDate", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar beginDate;
    @XmlElement(name = "EndDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar endDate;
    @XmlElement(name = "StudentRecordAccess")
    protected Boolean studentRecordAccess;

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
     * Gets the value of the cohortReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cohortReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCohortReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CohortReferenceType }
     * 
     * 
     */
    public List<CohortReferenceType> getCohortReference() {
        if (cohortReference == null) {
            cohortReference = new ArrayList<CohortReferenceType>();
        }
        return this.cohortReference;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getBeginDate() {
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
    public void setBeginDate(Calendar value) {
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
    public Calendar getEndDate() {
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
    public void setEndDate(Calendar value) {
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
