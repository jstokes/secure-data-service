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
 * <p>Java class for PerformanceBaseType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PerformanceBaseType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Advanced"/>
 *     &lt;enumeration value="Proficient"/>
 *     &lt;enumeration value="Basic"/>
 *     &lt;enumeration value="Below Basic"/>
 *     &lt;enumeration value="Well Below Basic"/>
 *     &lt;enumeration value="Pass"/>
 *     &lt;enumeration value="Fail"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PerformanceBaseType")
@XmlEnum
public enum PerformanceBaseType {

    @XmlEnumValue("Advanced")
    ADVANCED("Advanced"),
    @XmlEnumValue("Proficient")
    PROFICIENT("Proficient"),
    @XmlEnumValue("Basic")
    BASIC("Basic"),
    @XmlEnumValue("Below Basic")
    BELOW_BASIC("Below Basic"),
    @XmlEnumValue("Well Below Basic")
    WELL_BELOW_BASIC("Well Below Basic"),
    @XmlEnumValue("Pass")
    PASS("Pass"),
    @XmlEnumValue("Fail")
    FAIL("Fail");
    private final String value;

    PerformanceBaseType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PerformanceBaseType fromValue(String v) {
        for (PerformanceBaseType c: PerformanceBaseType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
