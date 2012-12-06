//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;

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
 *         &lt;element name="StudentSchoolAssociation" type="{http://ed-fi.org/0100}SLC-StudentSchoolAssociation"/>
 *         &lt;element name="StudentAcademicRecord" type="{http://ed-fi.org/0100}StudentAcademicRecordExtendedType"/>
 *         &lt;element name="CourseTranscript" type="{http://ed-fi.org/0100}SLC-CourseTranscript"/>
 *         &lt;element name="ReportCard" type="{http://ed-fi.org/0100}SLC-ReportCard"/>
 *         &lt;element name="Grade" type="{http://ed-fi.org/0100}SLC-Grade"/>
 *         &lt;element name="StudentSectionAssociation" type="{http://ed-fi.org/0100}SLC-StudentSectionAssociation"/>
 *         &lt;element name="Section" type="{http://ed-fi.org/0100}SLC-Section"/>
 *         &lt;element name="CourseOffering" type="{http://ed-fi.org/0100}SLC-CourseOffering"/>
 *         &lt;element name="Course" type="{http://ed-fi.org/0100}SLC-Course"/>
 *         &lt;element name="Diploma" type="{http://ed-fi.org/0100}Diploma"/>
 *         &lt;element name="StudentAssessment" type="{http://ed-fi.org/0100}StudentAssessment"/>
 *         &lt;element name="School" type="{http://ed-fi.org/0100}SLC-School"/>
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
        @XmlElement(name = "ReportCard", type = SLCReportCard.class),
        @XmlElement(name = "Grade", type = SLCGrade.class),
        @XmlElement(name = "Diploma", type = Diploma.class),
        @XmlElement(name = "StudentSchoolAssociation", type = SLCStudentSchoolAssociation.class),
        @XmlElement(name = "StudentAcademicRecord", type = StudentAcademicRecordExtendedType.class),
        @XmlElement(name = "CourseOffering", type = SLCCourseOffering.class),
        @XmlElement(name = "CourseTranscript", type = SLCCourseTranscript.class),
        @XmlElement(name = "StudentAssessment", type = StudentAssessment.class),
        @XmlElement(name = "StudentSectionAssociation", type = SLCStudentSectionAssociation.class),
        @XmlElement(name = "School", type = SLCSchool.class),
        @XmlElement(name = "Student", type = Student.class),
        @XmlElement(name = "Section", type = SLCSection.class),
        @XmlElement(name = "Course", type = SLCCourse.class)
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
     * {@link SLCReportCard }
     * {@link SLCGrade }
     * {@link Diploma }
     * {@link SLCStudentSchoolAssociation }
     * {@link StudentAcademicRecordExtendedType }
     * {@link SLCCourseOffering }
     * {@link SLCCourseTranscript }
     * {@link StudentAssessment }
     * {@link SLCStudentSectionAssociation }
     * {@link SLCSchool }
     * {@link Student }
     * {@link SLCSection }
     * {@link SLCCourse }
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
