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
 * <p>Java class for LimitedEnglishProficiencyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LimitedEnglishProficiencyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="NotLimited"/>
 *     &lt;enumeration value="Limited"/>
 *     &lt;enumeration value="Limited Monitored 1"/>
 *     &lt;enumeration value="Limited Monitored 2"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LimitedEnglishProficiencyType")
@XmlEnum
public enum LimitedEnglishProficiencyType {

    @XmlEnumValue("NotLimited")
    NOT_LIMITED("NotLimited"),
    @XmlEnumValue("Limited")
    LIMITED("Limited"),
    @XmlEnumValue("Limited Monitored 1")
    LIMITED_MONITORED_1("Limited Monitored 1"),
    @XmlEnumValue("Limited Monitored 2")
    LIMITED_MONITORED_2("Limited Monitored 2");
    private final String value;

    LimitedEnglishProficiencyType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LimitedEnglishProficiencyType fromValue(String v) {
        for (LimitedEnglishProficiencyType c: LimitedEnglishProficiencyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
