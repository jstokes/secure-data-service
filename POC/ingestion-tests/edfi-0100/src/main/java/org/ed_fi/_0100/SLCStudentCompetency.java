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
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * StudentCompetency record with key fields: LearningObjectiveReference (Objective, AcademicSubject, ObjectiveGradeLevel), StudentCompetencyObjectiveReference (StudentCompetencyObjectiveId), CompetencyLevel (CodeValue) and StudentSectionAssociationReference (StudentUniqueStateId, StateOrganizationId, UniqueSectionCode, BeginDate). Made StudentSectionAssociationReference required. Changed types of LearningObjectiveReference, StudentCompetencyObjectiveReference and StudentSectionAssociationReference to SLC reference types.
 * 
 * <p>Java class for SLC-StudentCompetency complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-StudentCompetency">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}SLC-LearningObjectiveReferenceType"/>
 *           &lt;element name="StudentCompetencyObjectiveReference" type="{http://ed-fi.org/0100}SLC-StudentCompetencyObjectiveReferenceType"/>
 *         &lt;/choice>
 *         &lt;element name="CompetencyLevel" type="{http://ed-fi.org/0100}CompetencyLevelDescriptorType"/>
 *         &lt;element name="DiagnosticStatement" type="{http://ed-fi.org/0100}text" minOccurs="0"/>
 *         &lt;element name="StudentSectionAssociationReference" type="{http://ed-fi.org/0100}SLC-StudentSectionAssociationReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-StudentCompetency", propOrder = {
    "learningObjectiveReference",
    "studentCompetencyObjectiveReference",
    "competencyLevel",
    "diagnosticStatement",
    "studentSectionAssociationReference"
})
public class SLCStudentCompetency
    extends ComplexObjectType
{

    @XmlElement(name = "LearningObjectiveReference")
    protected SLCLearningObjectiveReferenceType learningObjectiveReference;
    @XmlElement(name = "StudentCompetencyObjectiveReference")
    protected SLCStudentCompetencyObjectiveReferenceType studentCompetencyObjectiveReference;
    @XmlElement(name = "CompetencyLevel", required = true)
    protected CompetencyLevelDescriptorType competencyLevel;
    @XmlElement(name = "DiagnosticStatement")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String diagnosticStatement;
    @XmlElement(name = "StudentSectionAssociationReference", required = true)
    protected SLCStudentSectionAssociationReferenceType studentSectionAssociationReference;

    /**
     * Gets the value of the learningObjectiveReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCLearningObjectiveReferenceType }
     *     
     */
    public SLCLearningObjectiveReferenceType getLearningObjectiveReference() {
        return learningObjectiveReference;
    }

    /**
     * Sets the value of the learningObjectiveReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCLearningObjectiveReferenceType }
     *     
     */
    public void setLearningObjectiveReference(SLCLearningObjectiveReferenceType value) {
        this.learningObjectiveReference = value;
    }

    /**
     * Gets the value of the studentCompetencyObjectiveReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentCompetencyObjectiveReferenceType }
     *     
     */
    public SLCStudentCompetencyObjectiveReferenceType getStudentCompetencyObjectiveReference() {
        return studentCompetencyObjectiveReference;
    }

    /**
     * Sets the value of the studentCompetencyObjectiveReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentCompetencyObjectiveReferenceType }
     *     
     */
    public void setStudentCompetencyObjectiveReference(SLCStudentCompetencyObjectiveReferenceType value) {
        this.studentCompetencyObjectiveReference = value;
    }

    /**
     * Gets the value of the competencyLevel property.
     * 
     * @return
     *     possible object is
     *     {@link CompetencyLevelDescriptorType }
     *     
     */
    public CompetencyLevelDescriptorType getCompetencyLevel() {
        return competencyLevel;
    }

    /**
     * Sets the value of the competencyLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompetencyLevelDescriptorType }
     *     
     */
    public void setCompetencyLevel(CompetencyLevelDescriptorType value) {
        this.competencyLevel = value;
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
     * Gets the value of the studentSectionAssociationReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCStudentSectionAssociationReferenceType }
     *     
     */
    public SLCStudentSectionAssociationReferenceType getStudentSectionAssociationReference() {
        return studentSectionAssociationReference;
    }

    /**
     * Sets the value of the studentSectionAssociationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCStudentSectionAssociationReferenceType }
     *     
     */
    public void setStudentSectionAssociationReference(SLCStudentSectionAssociationReferenceType value) {
        this.studentSectionAssociationReference = value;
    }

}
