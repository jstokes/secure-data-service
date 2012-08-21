//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 02:49:01 PM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity is the final record of a student's
 * 				performance in their courses at the end of semester or school year.
 * 			
 * 
 * <p>Java class for courseTranscript complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="courseTranscript">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courseAttemptResult" type="{}courseAttemptResultType"/>
 *         &lt;element name="creditsAttempted" type="{}credits" minOccurs="0"/>
 *         &lt;element name="creditsEarned" type="{}credits"/>
 *         &lt;element name="additionalCreditsEarned" type="{}additionalCredits" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="gradeLevelWhenTaken" type="{}gradeLevelType" minOccurs="0"/>
 *         &lt;element name="methodCreditEarned" type="{}methodCreditEarnedType" minOccurs="0"/>
 *         &lt;element name="finalLetterGradeEarned" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{}gradeEarned">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="finalNumericGradeEarned" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="courseRepeatCode" type="{}courseRepeatCodeType" minOccurs="0"/>
 *         &lt;element name="diagnosticStatement" type="{}text" minOccurs="0"/>
 *         &lt;element name="gradeType" type="{}gradeType"/>
 *         &lt;element name="performanceBaseConversion" type="{}performanceBaseType" minOccurs="0"/>
 *         &lt;element name="courseId" type="{}reference"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "courseTranscript", propOrder = {
    "courseAttemptResult",
    "creditsAttempted",
    "creditsEarned",
    "additionalCreditsEarned",
    "gradeLevelWhenTaken",
    "methodCreditEarned",
    "finalLetterGradeEarned",
    "finalNumericGradeEarned",
    "courseRepeatCode",
    "diagnosticStatement",
    "gradeType",
    "performanceBaseConversion",
    "courseId"
})
public class CourseTranscript {

    @XmlElement(required = true)
    protected CourseAttemptResultType courseAttemptResult;
    protected Credits creditsAttempted;
    @XmlElement(required = true)
    protected Credits creditsEarned;
    protected List<AdditionalCredits> additionalCreditsEarned;
    protected GradeLevelType gradeLevelWhenTaken;
    protected MethodCreditEarnedType methodCreditEarned;
    protected String finalLetterGradeEarned;
    protected BigInteger finalNumericGradeEarned;
    protected CourseRepeatCodeType courseRepeatCode;
    protected String diagnosticStatement;
    @XmlElement(required = true)
    protected GradeType gradeType;
    protected PerformanceBaseType performanceBaseConversion;
    @XmlElement(required = true)
    protected String courseId;

    /**
     * Gets the value of the courseAttemptResult property.
     * 
     * @return
     *     possible object is
     *     {@link CourseAttemptResultType }
     *     
     */
    public CourseAttemptResultType getCourseAttemptResult() {
        return courseAttemptResult;
    }

    /**
     * Sets the value of the courseAttemptResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseAttemptResultType }
     *     
     */
    public void setCourseAttemptResult(CourseAttemptResultType value) {
        this.courseAttemptResult = value;
    }

    /**
     * Gets the value of the creditsAttempted property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getCreditsAttempted() {
        return creditsAttempted;
    }

    /**
     * Sets the value of the creditsAttempted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setCreditsAttempted(Credits value) {
        this.creditsAttempted = value;
    }

    /**
     * Gets the value of the creditsEarned property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getCreditsEarned() {
        return creditsEarned;
    }

    /**
     * Sets the value of the creditsEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setCreditsEarned(Credits value) {
        this.creditsEarned = value;
    }

    /**
     * Gets the value of the additionalCreditsEarned property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalCreditsEarned property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalCreditsEarned().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdditionalCredits }
     * 
     * 
     */
    public List<AdditionalCredits> getAdditionalCreditsEarned() {
        if (additionalCreditsEarned == null) {
            additionalCreditsEarned = new ArrayList<AdditionalCredits>();
        }
        return this.additionalCreditsEarned;
    }

    /**
     * Gets the value of the gradeLevelWhenTaken property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelType }
     *     
     */
    public GradeLevelType getGradeLevelWhenTaken() {
        return gradeLevelWhenTaken;
    }

    /**
     * Sets the value of the gradeLevelWhenTaken property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelType }
     *     
     */
    public void setGradeLevelWhenTaken(GradeLevelType value) {
        this.gradeLevelWhenTaken = value;
    }

    /**
     * Gets the value of the methodCreditEarned property.
     * 
     * @return
     *     possible object is
     *     {@link MethodCreditEarnedType }
     *     
     */
    public MethodCreditEarnedType getMethodCreditEarned() {
        return methodCreditEarned;
    }

    /**
     * Sets the value of the methodCreditEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link MethodCreditEarnedType }
     *     
     */
    public void setMethodCreditEarned(MethodCreditEarnedType value) {
        this.methodCreditEarned = value;
    }

    /**
     * Gets the value of the finalLetterGradeEarned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalLetterGradeEarned() {
        return finalLetterGradeEarned;
    }

    /**
     * Sets the value of the finalLetterGradeEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalLetterGradeEarned(String value) {
        this.finalLetterGradeEarned = value;
    }

    /**
     * Gets the value of the finalNumericGradeEarned property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFinalNumericGradeEarned() {
        return finalNumericGradeEarned;
    }

    /**
     * Sets the value of the finalNumericGradeEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFinalNumericGradeEarned(BigInteger value) {
        this.finalNumericGradeEarned = value;
    }

    /**
     * Gets the value of the courseRepeatCode property.
     * 
     * @return
     *     possible object is
     *     {@link CourseRepeatCodeType }
     *     
     */
    public CourseRepeatCodeType getCourseRepeatCode() {
        return courseRepeatCode;
    }

    /**
     * Sets the value of the courseRepeatCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseRepeatCodeType }
     *     
     */
    public void setCourseRepeatCode(CourseRepeatCodeType value) {
        this.courseRepeatCode = value;
    }

    /**
     * Gets the value of the diagnosticStatement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosticStatement() {
        return diagnosticStatement;
    }

    /**
     * Sets the value of the diagnosticStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosticStatement(String value) {
        this.diagnosticStatement = value;
    }

    /**
     * Gets the value of the gradeType property.
     * 
     * @return
     *     possible object is
     *     {@link GradeType }
     *     
     */
    public GradeType getGradeType() {
        return gradeType;
    }

    /**
     * Sets the value of the gradeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeType }
     *     
     */
    public void setGradeType(GradeType value) {
        this.gradeType = value;
    }

    /**
     * Gets the value of the performanceBaseConversion property.
     * 
     * @return
     *     possible object is
     *     {@link PerformanceBaseType }
     *     
     */
    public PerformanceBaseType getPerformanceBaseConversion() {
        return performanceBaseConversion;
    }

    /**
     * Sets the value of the performanceBaseConversion property.
     * 
     * @param value
     *     allowed object is
     *     {@link PerformanceBaseType }
     *     
     */
    public void setPerformanceBaseConversion(PerformanceBaseType value) {
        this.performanceBaseConversion = value;
    }

    /**
     * Gets the value of the courseId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Sets the value of the courseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseId(String value) {
        this.courseId = value;
    }

}
