//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This is the base type for association references.
 * 
 * <p>Java class for ReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceType")
@XmlSeeAlso({
    StudentReferenceType.class,
    AssessmentReferenceType.class,
    ParentReferenceType.class,
    LearningObjectiveReferenceType.class,
    CalendarDateReferenceType.class,
    DisciplineIncidentReferenceType.class,
    StudentCompetencyObjectiveReferenceType.class,
    EducationalOrgReferenceType.class,
    StudentSectionAssociationReferenceType.class,
    GradingPeriodReferenceType.class,
    SessionReferenceType.class,
    BellScheduleReferenceType.class,
    ObjectiveAssessmentReferenceType.class,
    AssessmentPeriodDescriptorType.class,
    StaffReferenceType.class,
    AccountReferenceType.class,
    LocationReferenceType.class,
    ProgramReferenceType.class,
    CourseOfferingReferenceType.class,
    CourseReferenceType.class,
    ClassPeriodReferenceType.class,
    CredentialFieldDescriptorType.class,
    SectionReferenceType.class,
    PerformanceLevelDescriptorType.class,
    LearningStandardReferenceType.class,
    AccountCodeDescriptorType.class,
    CohortReferenceType.class,
    DisciplineDescriptorType.class,
    AssessmentFamilyReferenceType.class,
    AssessmentItemReferenceType.class,
    BehaviorDescriptorType.class,
    CompetencyLevelDescriptorType.class,
    ServiceDescriptorType.class
})
public class ReferenceType {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ref;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRef(Object value) {
        this.ref = value;
    }

}
