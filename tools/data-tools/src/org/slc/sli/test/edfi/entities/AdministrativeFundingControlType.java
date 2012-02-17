//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.02.12 at 04:54:37 PM EST
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdministrativeFundingControlType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdministrativeFundingControlType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Public School"/>
 *     &lt;enumeration value="Private School"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "AdministrativeFundingControlType")
@XmlEnum
public enum AdministrativeFundingControlType {

    @XmlEnumValue("Public School")
    PUBLIC_SCHOOL("Public School"),
    @XmlEnumValue("Private School")
    PRIVATE_SCHOOL("Private School"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AdministrativeFundingControlType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AdministrativeFundingControlType fromValue(String v) {
        for (AdministrativeFundingControlType c: AdministrativeFundingControlType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
