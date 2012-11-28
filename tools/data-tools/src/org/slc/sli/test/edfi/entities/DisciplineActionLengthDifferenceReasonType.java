//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DisciplineActionLengthDifferenceReasonType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DisciplineActionLengthDifferenceReasonType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="No Difference"/>
 *     &lt;enumeration value="Term Modified By District"/>
 *     &lt;enumeration value="Term Modified By Court Order"/>
 *     &lt;enumeration value="Term Modified By Mutual Agreement"/>
 *     &lt;enumeration value="Student Completed Term Requirements Sooner Than Expected"/>
 *     &lt;enumeration value="Student Incarcerated"/>
 *     &lt;enumeration value="Term Decreased Due To Extenuating Health-Related Circumstances"/>
 *     &lt;enumeration value="Student Withdrew From School"/>
 *     &lt;enumeration value="School Year Ended"/>
 *     &lt;enumeration value="Continuation Of Previous Year�s Disciplinary Action Assignment"/>
 *     &lt;enumeration value="Term Modified By Placement Program Due To Student Behavior While In The Placement"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DisciplineActionLengthDifferenceReasonType")
@XmlEnum
public enum DisciplineActionLengthDifferenceReasonType {

    @XmlEnumValue("No Difference")
    NO_DIFFERENCE("No Difference"),
    @XmlEnumValue("Term Modified By District")
    TERM_MODIFIED_BY_DISTRICT("Term Modified By District"),
    @XmlEnumValue("Term Modified By Court Order")
    TERM_MODIFIED_BY_COURT_ORDER("Term Modified By Court Order"),
    @XmlEnumValue("Term Modified By Mutual Agreement")
    TERM_MODIFIED_BY_MUTUAL_AGREEMENT("Term Modified By Mutual Agreement"),
    @XmlEnumValue("Student Completed Term Requirements Sooner Than Expected")
    STUDENT_COMPLETED_TERM_REQUIREMENTS_SOONER_THAN_EXPECTED("Student Completed Term Requirements Sooner Than Expected"),
    @XmlEnumValue("Student Incarcerated")
    STUDENT_INCARCERATED("Student Incarcerated"),
    @XmlEnumValue("Term Decreased Due To Extenuating Health-Related Circumstances")
    TERM_DECREASED_DUE_TO_EXTENUATING_HEALTH_RELATED_CIRCUMSTANCES("Term Decreased Due To Extenuating Health-Related Circumstances"),
    @XmlEnumValue("Student Withdrew From School")
    STUDENT_WITHDREW_FROM_SCHOOL("Student Withdrew From School"),
    @XmlEnumValue("School Year Ended")
    SCHOOL_YEAR_ENDED("School Year Ended"),
    @XmlEnumValue("Continuation Of Previous Year\u2019s Disciplinary Action Assignment")
    CONTINUATION_OF_PREVIOUS_YEAR_S_DISCIPLINARY_ACTION_ASSIGNMENT("Continuation Of Previous Year\u2019s Disciplinary Action Assignment"),
    @XmlEnumValue("Term Modified By Placement Program Due To Student Behavior While In The Placement")
    TERM_MODIFIED_BY_PLACEMENT_PROGRAM_DUE_TO_STUDENT_BEHAVIOR_WHILE_IN_THE_PLACEMENT("Term Modified By Placement Program Due To Student Behavior While In The Placement"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    DisciplineActionLengthDifferenceReasonType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DisciplineActionLengthDifferenceReasonType fromValue(String v) {
        for (DisciplineActionLengthDifferenceReasonType c: DisciplineActionLengthDifferenceReasonType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
