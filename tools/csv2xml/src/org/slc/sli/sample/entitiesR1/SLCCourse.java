//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Course record with key fields: UniqueCourseId and EducationOrganizationReference. Changed types of EducationOrganizationReference, LearningStandardReference and LearningObjectiveReference to SLC reference types.
 * 
 * <p>Java class for SLC-Course complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-Course">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="CourseTitle" type="{http://ed-fi.org/0100}CourseTitle"/>
 *         &lt;element name="NumberOfParts">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CourseCode" type="{http://ed-fi.org/0100}CourseCode" maxOccurs="unbounded"/>
 *         &lt;element name="CourseLevel" type="{http://ed-fi.org/0100}CourseLevelType" minOccurs="0"/>
 *         &lt;element name="CourseLevelCharacteristics" type="{http://ed-fi.org/0100}CourseLevelCharacteristicsType" minOccurs="0"/>
 *         &lt;element name="GradesOffered" type="{http://ed-fi.org/0100}GradeLevelsType" minOccurs="0"/>
 *         &lt;element name="SubjectArea" type="{http://ed-fi.org/0100}AcademicSubjectType" minOccurs="0"/>
 *         &lt;element name="CourseDescription" type="{http://ed-fi.org/0100}Description" minOccurs="0"/>
 *         &lt;element name="DateCourseAdopted" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="HighSchoolCourseRequirement" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CourseGPAApplicability" type="{http://ed-fi.org/0100}CourseGPAApplicabilityType" minOccurs="0"/>
 *         &lt;element name="CourseDefinedBy" type="{http://ed-fi.org/0100}CourseDefinedByType" minOccurs="0"/>
 *         &lt;element name="MinimumAvailableCredit" type="{http://ed-fi.org/0100}Credits" minOccurs="0"/>
 *         &lt;element name="MaximumAvailableCredit" type="{http://ed-fi.org/0100}Credits" minOccurs="0"/>
 *         &lt;element name="CareerPathway" type="{http://ed-fi.org/0100}CareerPathwayType" minOccurs="0"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *         &lt;element name="LearningStandardReference" type="{http://ed-fi.org/0100}SLC-LearningStandardReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="LearningObjectiveReference" type="{http://ed-fi.org/0100}SLC-LearningObjectiveReferenceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CompetencyLevels" type="{http://ed-fi.org/0100}CompetencyLevelDescriptorType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="UniqueCourseId" type="{http://ed-fi.org/0100}SLC-UniqueCourseId"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-Course", propOrder = {
    "courseTitle",
    "numberOfParts",
    "courseCode",
    "courseLevel",
    "courseLevelCharacteristics",
    "gradesOffered",
    "subjectArea",
    "courseDescription",
    "dateCourseAdopted",
    "highSchoolCourseRequirement",
    "courseGPAApplicability",
    "courseDefinedBy",
    "minimumAvailableCredit",
    "maximumAvailableCredit",
    "careerPathway",
    "educationOrganizationReference",
    "learningStandardReference",
    "learningObjectiveReference",
    "competencyLevels",
    "uniqueCourseId"
})
public class SLCCourse
    extends ComplexObjectType
{

    @XmlElement(name = "CourseTitle", required = true)
    protected String courseTitle;
    @XmlElement(name = "NumberOfParts")
    protected int numberOfParts;
    @XmlElement(name = "CourseCode", required = true)
    protected List<CourseCode> courseCode;
    @XmlElement(name = "CourseLevel")
    protected CourseLevelType courseLevel;
    @XmlElement(name = "CourseLevelCharacteristics")
    protected CourseLevelCharacteristicsType courseLevelCharacteristics;
    @XmlElement(name = "GradesOffered")
    protected GradeLevelsType gradesOffered;
    @XmlElement(name = "SubjectArea")
    protected AcademicSubjectType subjectArea;
    @XmlElement(name = "CourseDescription")
    protected String courseDescription;
    @XmlElement(name = "DateCourseAdopted")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateCourseAdopted;
    @XmlElement(name = "HighSchoolCourseRequirement")
    protected Boolean highSchoolCourseRequirement;
    @XmlElement(name = "CourseGPAApplicability")
    protected CourseGPAApplicabilityType courseGPAApplicability;
    @XmlElement(name = "CourseDefinedBy")
    protected CourseDefinedByType courseDefinedBy;
    @XmlElement(name = "MinimumAvailableCredit")
    protected Credits minimumAvailableCredit;
    @XmlElement(name = "MaximumAvailableCredit")
    protected Credits maximumAvailableCredit;
    @XmlElement(name = "CareerPathway")
    protected CareerPathwayType careerPathway;
    @XmlElement(name = "EducationOrganizationReference", required = true)
    protected SLCEducationalOrgReferenceType educationOrganizationReference;
    @XmlElement(name = "LearningStandardReference")
    protected List<SLCLearningStandardReferenceType> learningStandardReference;
    @XmlElement(name = "LearningObjectiveReference")
    protected List<SLCLearningObjectiveReferenceType> learningObjectiveReference;
    @XmlElement(name = "CompetencyLevels")
    protected List<CompetencyLevelDescriptorType> competencyLevels;
    @XmlElement(name = "UniqueCourseId", required = true)
    protected String uniqueCourseId;

    /**
     * Gets the value of the courseTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * Sets the value of the courseTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseTitle(String value) {
        this.courseTitle = value;
    }

    /**
     * Gets the value of the numberOfParts property.
     * 
     */
    public int getNumberOfParts() {
        return numberOfParts;
    }

    /**
     * Sets the value of the numberOfParts property.
     * 
     */
    public void setNumberOfParts(int value) {
        this.numberOfParts = value;
    }

    /**
     * Gets the value of the courseCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseCode }
     * 
     * 
     */
    public List<CourseCode> getCourseCode() {
        if (courseCode == null) {
            courseCode = new ArrayList<CourseCode>();
        }
        return this.courseCode;
    }

    /**
     * Gets the value of the courseLevel property.
     * 
     * @return
     *     possible object is
     *     {@link CourseLevelType }
     *     
     */
    public CourseLevelType getCourseLevel() {
        return courseLevel;
    }

    /**
     * Sets the value of the courseLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseLevelType }
     *     
     */
    public void setCourseLevel(CourseLevelType value) {
        this.courseLevel = value;
    }

    /**
     * Gets the value of the courseLevelCharacteristics property.
     * 
     * @return
     *     possible object is
     *     {@link CourseLevelCharacteristicsType }
     *     
     */
    public CourseLevelCharacteristicsType getCourseLevelCharacteristics() {
        return courseLevelCharacteristics;
    }

    /**
     * Sets the value of the courseLevelCharacteristics property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseLevelCharacteristicsType }
     *     
     */
    public void setCourseLevelCharacteristics(CourseLevelCharacteristicsType value) {
        this.courseLevelCharacteristics = value;
    }

    /**
     * Gets the value of the gradesOffered property.
     * 
     * @return
     *     possible object is
     *     {@link GradeLevelsType }
     *     
     */
    public GradeLevelsType getGradesOffered() {
        return gradesOffered;
    }

    /**
     * Sets the value of the gradesOffered property.
     * 
     * @param value
     *     allowed object is
     *     {@link GradeLevelsType }
     *     
     */
    public void setGradesOffered(GradeLevelsType value) {
        this.gradesOffered = value;
    }

    /**
     * Gets the value of the subjectArea property.
     * 
     * @return
     *     possible object is
     *     {@link AcademicSubjectType }
     *     
     */
    public AcademicSubjectType getSubjectArea() {
        return subjectArea;
    }

    /**
     * Sets the value of the subjectArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcademicSubjectType }
     *     
     */
    public void setSubjectArea(AcademicSubjectType value) {
        this.subjectArea = value;
    }

    /**
     * Gets the value of the courseDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Sets the value of the courseDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseDescription(String value) {
        this.courseDescription = value;
    }

    /**
     * Gets the value of the dateCourseAdopted property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCourseAdopted() {
        return dateCourseAdopted;
    }

    /**
     * Sets the value of the dateCourseAdopted property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCourseAdopted(XMLGregorianCalendar value) {
        this.dateCourseAdopted = value;
    }

    /**
     * Gets the value of the highSchoolCourseRequirement property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHighSchoolCourseRequirement() {
        return highSchoolCourseRequirement;
    }

    /**
     * Sets the value of the highSchoolCourseRequirement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHighSchoolCourseRequirement(Boolean value) {
        this.highSchoolCourseRequirement = value;
    }

    /**
     * Gets the value of the courseGPAApplicability property.
     * 
     * @return
     *     possible object is
     *     {@link CourseGPAApplicabilityType }
     *     
     */
    public CourseGPAApplicabilityType getCourseGPAApplicability() {
        return courseGPAApplicability;
    }

    /**
     * Sets the value of the courseGPAApplicability property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseGPAApplicabilityType }
     *     
     */
    public void setCourseGPAApplicability(CourseGPAApplicabilityType value) {
        this.courseGPAApplicability = value;
    }

    /**
     * Gets the value of the courseDefinedBy property.
     * 
     * @return
     *     possible object is
     *     {@link CourseDefinedByType }
     *     
     */
    public CourseDefinedByType getCourseDefinedBy() {
        return courseDefinedBy;
    }

    /**
     * Sets the value of the courseDefinedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourseDefinedByType }
     *     
     */
    public void setCourseDefinedBy(CourseDefinedByType value) {
        this.courseDefinedBy = value;
    }

    /**
     * Gets the value of the minimumAvailableCredit property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getMinimumAvailableCredit() {
        return minimumAvailableCredit;
    }

    /**
     * Sets the value of the minimumAvailableCredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setMinimumAvailableCredit(Credits value) {
        this.minimumAvailableCredit = value;
    }

    /**
     * Gets the value of the maximumAvailableCredit property.
     * 
     * @return
     *     possible object is
     *     {@link Credits }
     *     
     */
    public Credits getMaximumAvailableCredit() {
        return maximumAvailableCredit;
    }

    /**
     * Sets the value of the maximumAvailableCredit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credits }
     *     
     */
    public void setMaximumAvailableCredit(Credits value) {
        this.maximumAvailableCredit = value;
    }

    /**
     * Gets the value of the careerPathway property.
     * 
     * @return
     *     possible object is
     *     {@link CareerPathwayType }
     *     
     */
    public CareerPathwayType getCareerPathway() {
        return careerPathway;
    }

    /**
     * Sets the value of the careerPathway property.
     * 
     * @param value
     *     allowed object is
     *     {@link CareerPathwayType }
     *     
     */
    public void setCareerPathway(CareerPathwayType value) {
        this.careerPathway = value;
    }

    /**
     * Gets the value of the educationOrganizationReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getEducationOrganizationReference() {
        return educationOrganizationReference;
    }

    /**
     * Sets the value of the educationOrganizationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setEducationOrganizationReference(SLCEducationalOrgReferenceType value) {
        this.educationOrganizationReference = value;
    }

    /**
     * Gets the value of the learningStandardReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningStandardReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningStandardReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCLearningStandardReferenceType }
     * 
     * 
     */
    public List<SLCLearningStandardReferenceType> getLearningStandardReference() {
        if (learningStandardReference == null) {
            learningStandardReference = new ArrayList<SLCLearningStandardReferenceType>();
        }
        return this.learningStandardReference;
    }

    /**
     * Gets the value of the learningObjectiveReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the learningObjectiveReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLearningObjectiveReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SLCLearningObjectiveReferenceType }
     * 
     * 
     */
    public List<SLCLearningObjectiveReferenceType> getLearningObjectiveReference() {
        if (learningObjectiveReference == null) {
            learningObjectiveReference = new ArrayList<SLCLearningObjectiveReferenceType>();
        }
        return this.learningObjectiveReference;
    }

    /**
     * Gets the value of the competencyLevels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the competencyLevels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompetencyLevels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompetencyLevelDescriptorType }
     * 
     * 
     */
    public List<CompetencyLevelDescriptorType> getCompetencyLevels() {
        if (competencyLevels == null) {
            competencyLevels = new ArrayList<CompetencyLevelDescriptorType>();
        }
        return this.competencyLevels;
    }

    /**
     * Gets the value of the uniqueCourseId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueCourseId() {
        return uniqueCourseId;
    }

    /**
     * Sets the value of the uniqueCourseId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueCourseId(String value) {
        this.uniqueCourseId = value;
    }

}
