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
 * <p>Java class for OldEthnicityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OldEthnicityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="American Indian Or Alaskan Native"/>
 *     &lt;enumeration value="Asian Or Pacific Islander"/>
 *     &lt;enumeration value="Black, Not Of Hispanic Origin"/>
 *     &lt;enumeration value="Hispanic"/>
 *     &lt;enumeration value="White, Not Of Hispanic Origin"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OldEthnicityType")
@XmlEnum
public enum OldEthnicityType {

    @XmlEnumValue("American Indian Or Alaskan Native")
    AMERICAN_INDIAN_OR_ALASKAN_NATIVE("American Indian Or Alaskan Native"),
    @XmlEnumValue("Asian Or Pacific Islander")
    ASIAN_OR_PACIFIC_ISLANDER("Asian Or Pacific Islander"),
    @XmlEnumValue("Black, Not Of Hispanic Origin")
    BLACK_NOT_OF_HISPANIC_ORIGIN("Black, Not Of Hispanic Origin"),
    @XmlEnumValue("Hispanic")
    HISPANIC("Hispanic"),
    @XmlEnumValue("White, Not Of Hispanic Origin")
    WHITE_NOT_OF_HISPANIC_ORIGIN("White, Not Of Hispanic Origin");
    private final String value;

    OldEthnicityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OldEthnicityType fromValue(String v) {
        for (OldEthnicityType c: OldEthnicityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
