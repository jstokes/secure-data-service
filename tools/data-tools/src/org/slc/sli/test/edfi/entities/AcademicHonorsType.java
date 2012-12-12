//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AcademicHonorsType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AcademicHonorsType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Honor roll"/>
 *     &lt;enumeration value="Honor society"/>
 *     &lt;enumeration value="Honorable mention"/>
 *     &lt;enumeration value="Honors program"/>
 *     &lt;enumeration value="Prize awards"/>
 *     &lt;enumeration value="Scholarships"/>
 *     &lt;enumeration value="Awarding of units of value"/>
 *     &lt;enumeration value="Citizenship award/recognition"/>
 *     &lt;enumeration value="Completion of requirement, but no units of value awarded"/>
 *     &lt;enumeration value="Attendance award"/>
 *     &lt;enumeration value="Certificate"/>
 *     &lt;enumeration value="Honor award"/>
 *     &lt;enumeration value="Letter of student commendation"/>
 *     &lt;enumeration value="Medals"/>
 *     &lt;enumeration value="National Merit scholar"/>
 *     &lt;enumeration value="Points"/>
 *     &lt;enumeration value="Promotion or advancement"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AcademicHonorsType")
@XmlEnum
public enum AcademicHonorsType {

    @XmlEnumValue("Honor roll")
    HONOR_ROLL("Honor roll"),
    @XmlEnumValue("Honor society")
    HONOR_SOCIETY("Honor society"),
    @XmlEnumValue("Honorable mention")
    HONORABLE_MENTION("Honorable mention"),
    @XmlEnumValue("Honors program")
    HONORS_PROGRAM("Honors program"),
    @XmlEnumValue("Prize awards")
    PRIZE_AWARDS("Prize awards"),
    @XmlEnumValue("Scholarships")
    SCHOLARSHIPS("Scholarships"),
    @XmlEnumValue("Awarding of units of value")
    AWARDING_OF_UNITS_OF_VALUE("Awarding of units of value"),
    @XmlEnumValue("Citizenship award/recognition")
    CITIZENSHIP_AWARD_RECOGNITION("Citizenship award/recognition"),
    @XmlEnumValue("Completion of requirement, but no units of value awarded")
    COMPLETION_OF_REQUIREMENT_BUT_NO_UNITS_OF_VALUE_AWARDED("Completion of requirement, but no units of value awarded"),
    @XmlEnumValue("Attendance award")
    ATTENDANCE_AWARD("Attendance award"),
    @XmlEnumValue("Certificate")
    CERTIFICATE("Certificate"),
    @XmlEnumValue("Honor award")
    HONOR_AWARD("Honor award"),
    @XmlEnumValue("Letter of student commendation")
    LETTER_OF_STUDENT_COMMENDATION("Letter of student commendation"),
    @XmlEnumValue("Medals")
    MEDALS("Medals"),
    @XmlEnumValue("National Merit scholar")
    NATIONAL_MERIT_SCHOLAR("National Merit scholar"),
    @XmlEnumValue("Points")
    POINTS("Points"),
    @XmlEnumValue("Promotion or advancement")
    PROMOTION_OR_ADVANCEMENT("Promotion or advancement"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AcademicHonorsType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AcademicHonorsType fromValue(String v) {
        for (AcademicHonorsType c: AcademicHonorsType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
