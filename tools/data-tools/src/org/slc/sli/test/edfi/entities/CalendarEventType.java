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
 * <p>Java class for CalendarEventType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CalendarEventType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Instructional day"/>
 *     &lt;enumeration value="Teacher only day"/>
 *     &lt;enumeration value="Holiday"/>
 *     &lt;enumeration value="Make-up day"/>
 *     &lt;enumeration value="Weather day"/>
 *     &lt;enumeration value="Student late arrival/early dismissal"/>
 *     &lt;enumeration value="Emergency day"/>
 *     &lt;enumeration value="Strike"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CalendarEventType")
@XmlEnum
public enum CalendarEventType {

    @XmlEnumValue("Instructional day")
    INSTRUCTIONAL_DAY("Instructional day"),
    @XmlEnumValue("Teacher only day")
    TEACHER_ONLY_DAY("Teacher only day"),
    @XmlEnumValue("Holiday")
    HOLIDAY("Holiday"),
    @XmlEnumValue("Make-up day")
    MAKE_UP_DAY("Make-up day"),
    @XmlEnumValue("Weather day")
    WEATHER_DAY("Weather day"),
    @XmlEnumValue("Student late arrival/early dismissal")
    STUDENT_LATE_ARRIVAL_EARLY_DISMISSAL("Student late arrival/early dismissal"),
    @XmlEnumValue("Emergency day")
    EMERGENCY_DAY("Emergency day"),
    @XmlEnumValue("Strike")
    STRIKE("Strike"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    CalendarEventType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CalendarEventType fromValue(String v) {
        for (CalendarEventType c: CalendarEventType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
