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
 * <p>Java class for CourseGPAApplicabilityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CourseGPAApplicabilityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Applicable"/>
 *     &lt;enumeration value="Not Applicable"/>
 *     &lt;enumeration value="Weighted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CourseGPAApplicabilityType")
@XmlEnum
public enum CourseGPAApplicabilityType {

    @XmlEnumValue("Applicable")
    APPLICABLE("Applicable"),
    @XmlEnumValue("Not Applicable")
    NOT_APPLICABLE("Not Applicable"),
    @XmlEnumValue("Weighted")
    WEIGHTED("Weighted");
    private final String value;

    CourseGPAApplicabilityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CourseGPAApplicabilityType fromValue(String v) {
        for (CourseGPAApplicabilityType c: CourseGPAApplicabilityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
