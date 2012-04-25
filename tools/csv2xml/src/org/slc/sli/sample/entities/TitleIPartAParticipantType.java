//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TitleIPartAParticipantType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TitleIPartAParticipantType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Public Targeted Assistance Program"/>
 *     &lt;enumeration value="Public Schoolwide Program"/>
 *     &lt;enumeration value="Private school students participating"/>
 *     &lt;enumeration value="Local Neglected Program"/>
 *     &lt;enumeration value=" Was not served"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TitleIPartAParticipantType")
@XmlEnum
public enum TitleIPartAParticipantType {

    @XmlEnumValue("Public Targeted Assistance Program")
    PUBLIC_TARGETED_ASSISTANCE_PROGRAM("Public Targeted Assistance Program"),
    @XmlEnumValue("Public Schoolwide Program")
    PUBLIC_SCHOOLWIDE_PROGRAM("Public Schoolwide Program"),
    @XmlEnumValue("Private school students participating")
    PRIVATE_SCHOOL_STUDENTS_PARTICIPATING("Private school students participating"),
    @XmlEnumValue("Local Neglected Program")
    LOCAL_NEGLECTED_PROGRAM("Local Neglected Program"),
    @XmlEnumValue(" Was not served")
    WAS_NOT_SERVED(" Was not served");
    private final String value;

    TitleIPartAParticipantType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TitleIPartAParticipantType fromValue(String v) {
        for (TitleIPartAParticipantType c: TitleIPartAParticipantType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
