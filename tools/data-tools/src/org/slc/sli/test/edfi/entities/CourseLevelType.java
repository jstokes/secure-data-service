//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.30 at 01:48:06 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CourseLevelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CourseLevelType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Basic or remedial"/>
 *     &lt;enumeration value="Enriched or advanced"/>
 *     &lt;enumeration value="General or regular"/>
 *     &lt;enumeration value="Honors"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CourseLevelType")
@XmlEnum
public enum CourseLevelType {

    @XmlEnumValue("Basic or remedial")
    BASIC_OR_REMEDIAL("Basic or remedial"),
    @XmlEnumValue("Enriched or advanced")
    ENRICHED_OR_ADVANCED("Enriched or advanced"),
    @XmlEnumValue("General or regular")
    GENERAL_OR_REGULAR("General or regular"),
    @XmlEnumValue("Honors")
    HONORS("Honors");
    private final String value;

    CourseLevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CourseLevelType fromValue(String v) {
        for (CourseLevelType c: CourseLevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
