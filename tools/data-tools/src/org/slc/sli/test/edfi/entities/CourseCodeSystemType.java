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
 * <p>Java class for CourseCodeSystemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CourseCodeSystemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="CSSC course code"/>
 *     &lt;enumeration value="Intermediate agency course code"/>
 *     &lt;enumeration value="LEA course code"/>
 *     &lt;enumeration value="NCES Pilot SNCCS course code"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="SCED course code"/>
 *     &lt;enumeration value="School course code"/>
 *     &lt;enumeration value="State course code"/>
 *     &lt;enumeration value="University course code"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CourseCodeSystemType")
@XmlEnum
public enum CourseCodeSystemType {

    @XmlEnumValue("CSSC course code")
    CSSC_COURSE_CODE("CSSC course code"),
    @XmlEnumValue("Intermediate agency course code")
    INTERMEDIATE_AGENCY_COURSE_CODE("Intermediate agency course code"),
    @XmlEnumValue("LEA course code")
    LEA_COURSE_CODE("LEA course code"),
    @XmlEnumValue("NCES Pilot SNCCS course code")
    NCES_PILOT_SNCCS_COURSE_CODE("NCES Pilot SNCCS course code"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("SCED course code")
    SCED_COURSE_CODE("SCED course code"),
    @XmlEnumValue("School course code")
    SCHOOL_COURSE_CODE("School course code"),
    @XmlEnumValue("State course code")
    STATE_COURSE_CODE("State course code"),
    @XmlEnumValue("University course code")
    UNIVERSITY_COURSE_CODE("University course code");
    private final String value;

    CourseCodeSystemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CourseCodeSystemType fromValue(String v) {
        for (CourseCodeSystemType c: CourseCodeSystemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
