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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Student" type="{http://ed-fi.org/0100}Student"/>
 *         &lt;element name="StudentSchoolAssociation" type="{http://ed-fi.org/0100}StudentSchoolAssociation"/>
 *         &lt;element name="StudentAcademicRecord" type="{http://ed-fi.org/0100}StudentAcademicRecordExtendedType"/>
 *         &lt;element name="CourseTranscript" type="{http://ed-fi.org/0100}CourseTranscript"/>
 *         &lt;element name="ReportCard" type="{http://ed-fi.org/0100}ReportCard"/>
 *         &lt;element name="Grade" type="{http://ed-fi.org/0100}Grade"/>
 *         &lt;element name="StudentSectionAssociation" type="{http://ed-fi.org/0100}StudentSectionAssociation"/>
 *         &lt;element name="Section" type="{http://ed-fi.org/0100}Section"/>
 *         &lt;element name="CourseOffering" type="{http://ed-fi.org/0100}CourseOffering"/>
 *         &lt;element name="Course" type="{http://ed-fi.org/0100}Course"/>
 *         &lt;element name="Diploma" type="{http://ed-fi.org/0100}Diploma"/>
 *         &lt;element name="StudentAssessment" type="{http://ed-fi.org/0100}StudentAssessment"/>
 *         &lt;element name="School" type="{http://ed-fi.org/0100}School"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "studentOrStudentSchoolAssociationOrStudentAcademicRecord"
})
@XmlRootElement(name = "InterchangeHSGeneratedStudentTranscript")
public class InterchangeHSGeneratedStudentTranscript {

    @XmlElements({
        @XmlElement(name = "School", type = School.class),
        @XmlElement(name = "Student", type = Student.class),
        @XmlElement(name = "CourseTranscript", type = CourseTranscript.class),
        @XmlElement(name = "ReportCard", type = ReportCard.class),
        @XmlElement(name = "StudentAcademicRecord", type = StudentAcademicRecordExtendedType.class),
        @XmlElement(name = "Diploma", type = Diploma.class),
        @XmlElement(name = "StudentSectionAssociation", type = StudentSectionAssociation.class),
        @XmlElement(name = "Grade", type = Grade.class),
        @XmlElement(name = "StudentAssessment", type = StudentAssessment.class),
        @XmlElement(name = "CourseOffering", type = CourseOffering.class),
        @XmlElement(name = "Course", type = Course.class),
        @XmlElement(name = "Section", type = Section.class),
        @XmlElement(name = "StudentSchoolAssociation", type = StudentSchoolAssociation.class)
    })
    protected List<Object> studentOrStudentSchoolAssociationOrStudentAcademicRecord;

    /**
     * Gets the value of the studentOrStudentSchoolAssociationOrStudentAcademicRecord property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studentOrStudentSchoolAssociationOrStudentAcademicRecord property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudentOrStudentSchoolAssociationOrStudentAcademicRecord().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link School }
     * {@link Student }
     * {@link CourseTranscript }
     * {@link ReportCard }
     * {@link StudentAcademicRecordExtendedType }
     * {@link Diploma }
     * {@link StudentSectionAssociation }
     * {@link Grade }
     * {@link StudentAssessment }
     * {@link CourseOffering }
     * {@link Course }
     * {@link Section }
     * {@link StudentSchoolAssociation }
     * 
     * 
     */
    public List<Object> getStudentOrStudentSchoolAssociationOrStudentAcademicRecord() {
        if (studentOrStudentSchoolAssociationOrStudentAcademicRecord == null) {
            studentOrStudentSchoolAssociationOrStudentAcademicRecord = new ArrayList<Object>();
        }
        return this.studentOrStudentSchoolAssociationOrStudentAcademicRecord;
    }

}
