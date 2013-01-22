//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttendanceEventType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AttendanceEventType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Daily Attendance"/>
 *     &lt;enumeration value="Section Attendance"/>
 *     &lt;enumeration value="Program Attendance"/>
 *     &lt;enumeration value="Extracurricular Attendance"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AttendanceEventType")
@XmlEnum
public enum AttendanceEventType {

    @XmlEnumValue("Daily Attendance")
    DAILY_ATTENDANCE("Daily Attendance"),
    @XmlEnumValue("Section Attendance")
    SECTION_ATTENDANCE("Section Attendance"),
    @XmlEnumValue("Program Attendance")
    PROGRAM_ATTENDANCE("Program Attendance"),
    @XmlEnumValue("Extracurricular Attendance")
    EXTRACURRICULAR_ATTENDANCE("Extracurricular Attendance");
    private final String value;

    AttendanceEventType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AttendanceEventType fromValue(String v) {
        for (AttendanceEventType c: AttendanceEventType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
