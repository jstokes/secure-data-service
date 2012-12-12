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
 *         &lt;element name="StateEducationAgency" type="{http://ed-fi.org/0100}SLC-StateEducationAgency"/>
 *         &lt;element name="EducationServiceCenter" type="{http://ed-fi.org/0100}SLC-EducationServiceCenter"/>
 *         &lt;element name="FeederSchoolAssociation" type="{http://ed-fi.org/0100}FeederSchoolAssociation"/>
 *         &lt;element name="LocalEducationAgency" type="{http://ed-fi.org/0100}SLC-LocalEducationAgency"/>
 *         &lt;element name="School" type="{http://ed-fi.org/0100}SLC-School"/>
 *         &lt;element name="Location" type="{http://ed-fi.org/0100}Location"/>
 *         &lt;element name="ClassPeriod" type="{http://ed-fi.org/0100}ClassPeriod"/>
 *         &lt;element name="Course" type="{http://ed-fi.org/0100}SLC-Course"/>
 *         &lt;element name="CompetencyLevelDescriptor" type="{http://ed-fi.org/0100}CompetencyLevelDescriptor"/>
 *         &lt;element name="Program" type="{http://ed-fi.org/0100}SLC-Program"/>
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
    "stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation"
})
@XmlRootElement(name = "InterchangeEducationOrganization")
public class InterchangeEducationOrganization {

    @XmlElements({
        @XmlElement(name = "Location", type = Location.class),
        @XmlElement(name = "Program", type = SLCProgram.class),
        @XmlElement(name = "ClassPeriod", type = ClassPeriod.class),
        @XmlElement(name = "LocalEducationAgency", type = SLCLocalEducationAgency.class),
        @XmlElement(name = "EducationServiceCenter", type = SLCEducationServiceCenter.class),
        @XmlElement(name = "FeederSchoolAssociation", type = FeederSchoolAssociation.class),
        @XmlElement(name = "Course", type = SLCCourse.class),
        @XmlElement(name = "CompetencyLevelDescriptor", type = CompetencyLevelDescriptor.class),
        @XmlElement(name = "School", type = SLCSchool.class),
        @XmlElement(name = "StateEducationAgency", type = SLCStateEducationAgency.class)
    })
    protected List<Object> stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation;

    /**
     * Gets the value of the stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Location }
     * {@link SLCProgram }
     * {@link ClassPeriod }
     * {@link SLCLocalEducationAgency }
     * {@link SLCEducationServiceCenter }
     * {@link FeederSchoolAssociation }
     * {@link SLCCourse }
     * {@link CompetencyLevelDescriptor }
     * {@link SLCSchool }
     * {@link SLCStateEducationAgency }
     * 
     * 
     */
    public List<Object> getStateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation() {
        if (stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation == null) {
            stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation = new ArrayList<Object>();
        }
        return this.stateEducationAgencyOrEducationServiceCenterOrFeederSchoolAssociation;
    }

}
