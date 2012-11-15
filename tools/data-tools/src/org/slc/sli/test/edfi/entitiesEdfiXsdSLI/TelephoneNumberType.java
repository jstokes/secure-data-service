//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TelephoneNumberType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TelephoneNumberType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Fax"/>
 *     &lt;enumeration value="Emergency 1"/>
 *     &lt;enumeration value="Emergency 2"/>
 *     &lt;enumeration value="Home"/>
 *     &lt;enumeration value="Mobile"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Unlisted"/>
 *     &lt;enumeration value="Work"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TelephoneNumberType")
@XmlEnum
public enum TelephoneNumberType {

    @XmlEnumValue("Fax")
    FAX("Fax"),
    @XmlEnumValue("Emergency 1")
    EMERGENCY_1("Emergency 1"),
    @XmlEnumValue("Emergency 2")
    EMERGENCY_2("Emergency 2"),
    @XmlEnumValue("Home")
    HOME("Home"),
    @XmlEnumValue("Mobile")
    MOBILE("Mobile"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Unlisted")
    UNLISTED("Unlisted"),
    @XmlEnumValue("Work")
    WORK("Work");
    private final String value;

    TelephoneNumberType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TelephoneNumberType fromValue(String v) {
        for (TelephoneNumberType c: TelephoneNumberType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
