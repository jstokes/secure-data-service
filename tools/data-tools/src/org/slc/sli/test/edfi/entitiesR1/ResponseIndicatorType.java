//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 02:49:01 PM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for responseIndicatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="responseIndicatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Nonscorable response"/>
 *     &lt;enumeration value="Ineffective response"/>
 *     &lt;enumeration value="Effective response"/>
 *     &lt;enumeration value="Partial response"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "responseIndicatorType")
@XmlEnum
public enum ResponseIndicatorType {

    @XmlEnumValue("Nonscorable response")
    NONSCORABLE_RESPONSE("Nonscorable response"),
    @XmlEnumValue("Ineffective response")
    INEFFECTIVE_RESPONSE("Ineffective response"),
    @XmlEnumValue("Effective response")
    EFFECTIVE_RESPONSE("Effective response"),
    @XmlEnumValue("Partial response")
    PARTIAL_RESPONSE("Partial response");
    private final String value;

    ResponseIndicatorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResponseIndicatorType fromValue(String v) {
        for (ResponseIndicatorType c: ResponseIndicatorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
