//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reporterDescriptionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="reporterDescriptionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Parent"/>
 *     &lt;enumeration value="Staff"/>
 *     &lt;enumeration value="Student"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "reporterDescriptionType")
@XmlEnum
public enum ReporterDescriptionType {

    @XmlEnumValue("Parent")
    PARENT("Parent"),
    @XmlEnumValue("Staff")
    STAFF("Staff"),
    @XmlEnumValue("Student")
    STUDENT("Student"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    ReporterDescriptionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReporterDescriptionType fromValue(String v) {
        for (ReporterDescriptionType c: ReporterDescriptionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
