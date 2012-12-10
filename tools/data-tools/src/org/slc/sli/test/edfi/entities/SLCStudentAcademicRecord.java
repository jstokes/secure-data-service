//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;
import javax.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * StudentAcademicRecord record with key fields: StudentReference and SessionReference. Changed types of StudentReference, SessionReference, ReportCardReference and DiplomaReference to SLC reference types.
 * 
 * <p>Java class for SLC-StudentAcademicRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentAcademicRecord">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="CumulativeCreditsEarned" type="{http://ed-fi.org/0100}Credits" minOccurs="0"/>
 *         &lt;element name="CumulativeCreditsAttempted" type="{http://ed-fi.org/0100}Credits" minOccurs="0"/>
 *         &lt;element name="CumulativeGradePointsEarned" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="CumulativeGradePointAverage" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="GradeValueQualifier" type="{http://ed-fi.org/0100}GradeValueQualifier" minOccurs="0"/>
 *         &lt;element name="ClassRanking" type="{http://ed-fi.org/0100}ClassRanking" minOccurs="0"/>
 *         &lt;element name="AcademicHonors" type="{http://ed-fi.org/0100}AcademicHonor" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Recognitions" type="{http://ed-fi.org/0100}Recognition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProjectedGraduationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="SessionCreditsEarned" type="{http://ed-fi.org/0100}Credits" minOccurs="0"/>
 *         &lt;element name="SessionCreditsAttempted" type="{http://ed-fi.org/0100}Credits" minOccurs="0"/>
 *         &lt;element name="SessionGradePointsEarned" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="SessionGradePointAverage" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="StudentReference" type="{http://ed-fi.org/0100}SLC-StudentReferenceType"/>
 *         &lt;element name="SessionReference" type="{http://ed-fi.org/0100}SLC-SessionReferenceType"/>
 *         &lt;element name="ReportCardReference" type="{http://ed-fi.org/0100}SLC-ReportCardReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DiplomaReference" type="{http://ed-fi.org/0100}SLC-DiplomaReferenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentAcademicRecord", propOrder = {
    "cumulativeCreditsEarned",
    "cumulativeCreditsAttempted",
    "cumulativeGradePointsEarned",
    "cumulativeGradePointAverage",
    "gradeValueQualifier",
    "classRanking",
    "academicHonors",
    "recognitions",
    "projectedGraduationDate",
    "sessionCreditsEarned",
    "sessionCreditsAttempted",
    "sessionGradePointsEarned",
    "sessionGradePointAverage",
    "studentReference",
    "sessionReference",
    "reportCardReference",
    "diplomaReference"
})
@XmlSeeAlso({
    StudentAcademicRecordExtendedType.class
})
@XmlRootElement(name = "StudentAcademicRecord") 
public class SLCStudentAcademicRecord
    extends ComplexObjectType
{

    @XmlElement(name = "CumulativeCreditsEarned")
    protected Credits cumulativeCreditsEarned;
    @XmlElement(name = "CumulativeCreditsAttempted")
    protected Credits cumulativeCreditsAttempted;
    @XmlElement(name = "CumulativeGradePointsEarned")
    protected BigDecimal cumulativeGradePointsEarned;
    @XmlElement(name = "CumulativeGradePointAverage")
    protected BigDecimal cumulativeGradePointAverage;
    @XmlElement(name = "GradeValueQualifier")
    protected String gradeValueQualifier;
    @XmlElement(name = "ClassRanking")
    protected ClassRanking classRanking;
    @XmlElement(name = "AcademicHonors")
    protected List<AcademicHonor> academicHonors;
    @XmlElement(name = "Recognitions")
    protected List<Recognition> recognitions;
    @XmlElement(name = "ProjectedGraduationDate")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String projectedGraduationDate;
    @XmlElement(name = "SessionCreditsEarned")
    protected Credits sessionCreditsEarned;
    @XmlElement(name = "SessionCreditsAttempted")
    protected Credits sessionCreditsAttempted;
    @XmlElement(name = "SessionGradePointsEarned")
    protected BigDecimal sessionGradePointsEarned;
    @XmlElement(name = "SessionGradePointAverage")
    protected BigDecimal sessionGradePointAverage;
    @XmlElement(name = "StudentReference", required = true)
    protected SLCStudentReferenceType studentReference;
    @XmlElement(name = "SessionReference", required = true)
    protected SLCSessionReferenceType sessionReference;
    @XmlElement(name = "ReportCardReference")
    protected List<SLCReportCardReferenceType> reportCardReference;
    @XmlElement(name = "DiplomaReference")
    protected SLCDiplomaReferenceType diplomaReference;

    /**
     * Gets the value of the cumulativeCreditsEarned property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getCumulativeCreditsEarned() {
        return cumulativeCreditsEarned;
    }

    /**
     * Sets the value of the cumulativeCreditsEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setCumulativeCreditsEarned(Credits value) {
        this.cumulativeCreditsEarned = value;
    }

    /**
     * Gets the value of the cumulativeCreditsAttempted property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getCumulativeCreditsAttempted() {
        return cumulativeCreditsAttempted;
    }

    /**
     * Sets the value of the cumulativeCreditsAttempted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setCumulativeCreditsAttempted(Credits value) {
        this.cumulativeCreditsAttempted = value;
    }

    /**
     * Gets the value of the cumulativeGradePointsEarned property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCumulativeGradePointsEarned() {
        return cumulativeGradePointsEarned;
    }

    /**
     * Sets the value of the cumulativeGradePointsEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCumulativeGradePointsEarned(BigDecimal value) {
        this.cumulativeGradePointsEarned = value;
    }

    /**
     * Gets the value of the cumulativeGradePointAverage property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCumulativeGradePointAverage() {
        return cumulativeGradePointAverage;
    }

    /**
     * Sets the value of the cumulativeGradePointAverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCumulativeGradePointAverage(BigDecimal value) {
        this.cumulativeGradePointAverage = value;
    }

    /**
     * Gets the value of the gradeValueQualifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGradeValueQualifier() {
        return gradeValueQualifier;
    }

    /**
     * Sets the value of the gradeValueQualifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGradeValueQualifier(String value) {
        this.gradeValueQualifier = value;
    }

    /**
     * Gets the value of the classRanking property.
     * 
     * @return
     *     possible object is
     *     {@link ClassRanking }
     *     
     */
    public ClassRanking getClassRanking() {
        return classRanking;
    }

    /**
     * Sets the value of the classRanking property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassRanking }
     *     
     */
    public void setClassRanking(ClassRanking value) {
        this.classRanking = value;
    }

    /**
     * Gets the value of the academicHonors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the academicHonors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcademicHonors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AcademicHonor }
     * 
     * 
     */
    public List<AcademicHonor> getAcademicHonors() {
        if (academicHonors == null) {
            academicHonors = new ArrayList<AcademicHonor>();
        }
        return this.academicHonors;
    }

    /**
     * Gets the value of the recognitions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recognitions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecognitions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Recognition }
     * 
     * 
     */
    public List<Recognition> getRecognitions() {
        if (recognitions == null) {
            recognitions = new ArrayList<Recognition>();
        }
        return this.recognitions;
    }

    /**
     * Gets the value of the projectedGraduationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectedGraduationDate() {
        return projectedGraduationDate;
    }

    /**
     * Sets the value of the projectedGraduationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectedGraduationDate(String value) {
        this.projectedGraduationDate = value;
    }

    /**
     * Gets the value of the sessionCreditsEarned property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getSessionCreditsEarned() {
        return sessionCreditsEarned;
    }

    /**
     * Sets the value of the sessionCreditsEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setSessionCreditsEarned(Credits value) {
        this.sessionCreditsEarned = value;
    }

    /**
     * Gets the value of the sessionCreditsAttempted property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getSessionCreditsAttempted() {
        return sessionCreditsAttempted;
    }

    /**
     * Sets the value of the sessionCreditsAttempted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setSessionCreditsAttempted(Credits value) {
        this.sessionCreditsAttempted = value;
    }

    /**
     * Gets the value of the sessionGradePointsEarned property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSessionGradePointsEarned() {
        return sessionGradePointsEarned;
    }

    /**
     * Sets the value of the sessionGradePointsEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSessionGradePointsEarned(BigDecimal value) {
        this.sessionGradePointsEarned = value;
    }

    /**
     * Gets the value of the sessionGradePointAverage property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSessionGradePointAverage() {
        return sessionGradePointAverage;
    }

    /**
     * Sets the value of the sessionGradePointAverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSessionGradePointAverage(BigDecimal value) {
        this.sessionGradePointAverage = value;
    }

    /**
     * Gets the value of the studentReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentReferenceType }
     *     
     */
    public SLCStudentReferenceType getStudentReference() {
        return studentReference;
    }

    /**
     * Sets the value of the studentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentReferenceType }
     *     
     */
    public void setStudentReference(SLCStudentReferenceType value) {
        this.studentReference = value;
    }

    /**
     * Gets the value of the sessionReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCSessionReferenceType }
     *     
     */
    public SLCSessionReferenceType getSessionReference() {
        return sessionReference;
    }

    /**
     * Sets the value of the sessionReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCSessionReferenceType }
     *     
     */
    public void setSessionReference(SLCSessionReferenceType value) {
        this.sessionReference = value;
    }

    /**
     * Gets the value of the reportCardReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportCardReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportCardReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCReportCardReferenceType }
     * 
     * 
     */
    public List<SLCReportCardReferenceType> getReportCardReference() {
        if (reportCardReference == null) {
            reportCardReference = new ArrayList<SLCReportCardReferenceType>();
        }
        return this.reportCardReference;
    }

    /**
     * Gets the value of the diplomaReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCDiplomaReferenceType }
     *     
     */
    public SLCDiplomaReferenceType getDiplomaReference() {
        return diplomaReference;
    }

    /**
     * Sets the value of the diplomaReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCDiplomaReferenceType }
     *     
     */
    public void setDiplomaReference(SLCDiplomaReferenceType value) {
        this.diplomaReference = value;
    }

}
