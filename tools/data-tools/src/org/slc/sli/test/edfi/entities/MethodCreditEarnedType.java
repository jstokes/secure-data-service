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
 * <p>Java class for MethodCreditEarnedType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MethodCreditEarnedType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Adult education credit"/>
 *     &lt;enumeration value="Classroom credit"/>
 *     &lt;enumeration value="Converted occupational experience credit"/>
 *     &lt;enumeration value="Correspondence credit"/>
 *     &lt;enumeration value="Credit by examination"/>
 *     &lt;enumeration value="Credit recovery"/>
 *     &lt;enumeration value="Online credit"/>
 *     &lt;enumeration value="Transfer credit"/>
 *     &lt;enumeration value="Vocational credit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MethodCreditEarnedType")
@XmlEnum
public enum MethodCreditEarnedType {

    @XmlEnumValue("Adult education credit")
    ADULT_EDUCATION_CREDIT("Adult education credit"),
    @XmlEnumValue("Classroom credit")
    CLASSROOM_CREDIT("Classroom credit"),
    @XmlEnumValue("Converted occupational experience credit")
    CONVERTED_OCCUPATIONAL_EXPERIENCE_CREDIT("Converted occupational experience credit"),
    @XmlEnumValue("Correspondence credit")
    CORRESPONDENCE_CREDIT("Correspondence credit"),
    @XmlEnumValue("Credit by examination")
    CREDIT_BY_EXAMINATION("Credit by examination"),
    @XmlEnumValue("Credit recovery")
    CREDIT_RECOVERY("Credit recovery"),
    @XmlEnumValue("Online credit")
    ONLINE_CREDIT("Online credit"),
    @XmlEnumValue("Transfer credit")
    TRANSFER_CREDIT("Transfer credit"),
    @XmlEnumValue("Vocational credit")
    VOCATIONAL_CREDIT("Vocational credit");
    private final String value;

    MethodCreditEarnedType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MethodCreditEarnedType fromValue(String v) {
        for (MethodCreditEarnedType c: MethodCreditEarnedType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
