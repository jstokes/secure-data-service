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
 * <p>Java class for AssessmentIdentificationSystemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AssessmentIdentificationSystemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="School"/>
 *     &lt;enumeration value="District"/>
 *     &lt;enumeration value="State"/>
 *     &lt;enumeration value="Federal"/>
 *     &lt;enumeration value="Other Federal"/>
 *     &lt;enumeration value="Test Contractor"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AssessmentIdentificationSystemType")
@XmlEnum
public enum AssessmentIdentificationSystemType {

    @XmlEnumValue("School")
    SCHOOL("School"),
    @XmlEnumValue("District")
    DISTRICT("District"),
    @XmlEnumValue("State")
    STATE("State"),
    @XmlEnumValue("Federal")
    FEDERAL("Federal"),
    @XmlEnumValue("Other Federal")
    OTHER_FEDERAL("Other Federal"),
    @XmlEnumValue("Test Contractor")
    TEST_CONTRACTOR("Test Contractor"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AssessmentIdentificationSystemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AssessmentIdentificationSystemType fromValue(String v) {
        for (AssessmentIdentificationSystemType c: AssessmentIdentificationSystemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
