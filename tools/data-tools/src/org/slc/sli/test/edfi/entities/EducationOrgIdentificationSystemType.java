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
 * <p>Java class for EducationOrgIdentificationSystemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EducationOrgIdentificationSystemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="School"/>
 *     &lt;enumeration value="ACT"/>
 *     &lt;enumeration value="LEA"/>
 *     &lt;enumeration value="SEA"/>
 *     &lt;enumeration value="NCES"/>
 *     &lt;enumeration value="Federal"/>
 *     &lt;enumeration value="DUNS"/>
 *     &lt;enumeration value="Other Federal"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EducationOrgIdentificationSystemType")
@XmlEnum
public enum EducationOrgIdentificationSystemType {

    @XmlEnumValue("School")
    SCHOOL("School"),
    ACT("ACT"),
    LEA("LEA"),
    SEA("SEA"),
    NCES("NCES"),
    @XmlEnumValue("Federal")
    FEDERAL("Federal"),
    DUNS("DUNS"),
    @XmlEnumValue("Other Federal")
    OTHER_FEDERAL("Other Federal"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    EducationOrgIdentificationSystemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EducationOrgIdentificationSystemType fromValue(String v) {
        for (EducationOrgIdentificationSystemType c: EducationOrgIdentificationSystemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
