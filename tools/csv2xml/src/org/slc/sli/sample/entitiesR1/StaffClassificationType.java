//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StaffClassificationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StaffClassificationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Art Therapist"/>
 *     &lt;enumeration value="Athletic Trainer"/>
 *     &lt;enumeration value="Assistant Principal"/>
 *     &lt;enumeration value="Assistant Superintendent"/>
 *     &lt;enumeration value="Certified Interpreter"/>
 *     &lt;enumeration value="Counselor"/>
 *     &lt;enumeration value="High School Counselor"/>
 *     &lt;enumeration value="Instructional Coordinator"/>
 *     &lt;enumeration value="Instructional Aide"/>
 *     &lt;enumeration value="Librarians/Media Specialists"/>
 *     &lt;enumeration value="LEA Administrator"/>
 *     &lt;enumeration value="LEA Specialist"/>
 *     &lt;enumeration value="LEA System Administrator"/>
 *     &lt;enumeration value="LEA Administrative Support Staff"/>
 *     &lt;enumeration value="Librarian"/>
 *     &lt;enumeration value="Principal"/>
 *     &lt;enumeration value="Physical Therapist"/>
 *     &lt;enumeration value="Teacher"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Superintendent"/>
 *     &lt;enumeration value="School Nurse"/>
 *     &lt;enumeration value="Specialist/Consultant"/>
 *     &lt;enumeration value="School Administrator"/>
 *     &lt;enumeration value="School Administrative Support Staff"/>
 *     &lt;enumeration value="Student Support Services Staff"/>
 *     &lt;enumeration value="School Leader"/>
 *     &lt;enumeration value="School Specialist"/>
 *     &lt;enumeration value="Substitute Teacher"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StaffClassificationType")
@XmlEnum
public enum StaffClassificationType {

    @XmlEnumValue("Art Therapist")
    ART_THERAPIST("Art Therapist"),
    @XmlEnumValue("Athletic Trainer")
    ATHLETIC_TRAINER("Athletic Trainer"),
    @XmlEnumValue("Assistant Principal")
    ASSISTANT_PRINCIPAL("Assistant Principal"),
    @XmlEnumValue("Assistant Superintendent")
    ASSISTANT_SUPERINTENDENT("Assistant Superintendent"),
    @XmlEnumValue("Certified Interpreter")
    CERTIFIED_INTERPRETER("Certified Interpreter"),
    @XmlEnumValue("Counselor")
    COUNSELOR("Counselor"),
    @XmlEnumValue("High School Counselor")
    HIGH_SCHOOL_COUNSELOR("High School Counselor"),
    @XmlEnumValue("Instructional Coordinator")
    INSTRUCTIONAL_COORDINATOR("Instructional Coordinator"),
    @XmlEnumValue("Instructional Aide")
    INSTRUCTIONAL_AIDE("Instructional Aide"),
    @XmlEnumValue("Librarians/Media Specialists")
    LIBRARIANS_MEDIA_SPECIALISTS("Librarians/Media Specialists"),
    @XmlEnumValue("LEA Administrator")
    LEA_ADMINISTRATOR("LEA Administrator"),
    @XmlEnumValue("LEA Specialist")
    LEA_SPECIALIST("LEA Specialist"),
    @XmlEnumValue("LEA System Administrator")
    LEA_SYSTEM_ADMINISTRATOR("LEA System Administrator"),
    @XmlEnumValue("LEA Administrative Support Staff")
    LEA_ADMINISTRATIVE_SUPPORT_STAFF("LEA Administrative Support Staff"),
    @XmlEnumValue("Librarian")
    LIBRARIAN("Librarian"),
    @XmlEnumValue("Principal")
    PRINCIPAL("Principal"),
    @XmlEnumValue("Physical Therapist")
    PHYSICAL_THERAPIST("Physical Therapist"),
    @XmlEnumValue("Teacher")
    TEACHER("Teacher"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Superintendent")
    SUPERINTENDENT("Superintendent"),
    @XmlEnumValue("School Nurse")
    SCHOOL_NURSE("School Nurse"),
    @XmlEnumValue("Specialist/Consultant")
    SPECIALIST_CONSULTANT("Specialist/Consultant"),
    @XmlEnumValue("School Administrator")
    SCHOOL_ADMINISTRATOR("School Administrator"),
    @XmlEnumValue("School Administrative Support Staff")
    SCHOOL_ADMINISTRATIVE_SUPPORT_STAFF("School Administrative Support Staff"),
    @XmlEnumValue("Student Support Services Staff")
    STUDENT_SUPPORT_SERVICES_STAFF("Student Support Services Staff"),
    @XmlEnumValue("School Leader")
    SCHOOL_LEADER("School Leader"),
    @XmlEnumValue("School Specialist")
    SCHOOL_SPECIALIST("School Specialist"),
    @XmlEnumValue("Substitute Teacher")
    SUBSTITUTE_TEACHER("Substitute Teacher");
    private final String value;

    StaffClassificationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StaffClassificationType fromValue(String v) {
        for (StaffClassificationType c: StaffClassificationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
