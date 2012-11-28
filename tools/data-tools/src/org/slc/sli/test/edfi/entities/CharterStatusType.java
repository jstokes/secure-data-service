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
 * <p>Java class for CharterStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CharterStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="School Charter"/>
 *     &lt;enumeration value="College/University Charter"/>
 *     &lt;enumeration value="Open Enrollment"/>
 *     &lt;enumeration value="Not a Charter School"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CharterStatusType")
@XmlEnum
public enum CharterStatusType {

    @XmlEnumValue("School Charter")
    SCHOOL_CHARTER("School Charter"),
    @XmlEnumValue("College/University Charter")
    COLLEGE_UNIVERSITY_CHARTER("College/University Charter"),
    @XmlEnumValue("Open Enrollment")
    OPEN_ENROLLMENT("Open Enrollment"),
    @XmlEnumValue("Not a Charter School")
    NOT_A_CHARTER_SCHOOL("Not a Charter School");
    private final String value;

    CharterStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CharterStatusType fromValue(String v) {
        for (CharterStatusType c: CharterStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
