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
 * <p>Java class for AdditionalCreditType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AdditionalCreditType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="AP"/>
 *     &lt;enumeration value="Dual credit"/>
 *     &lt;enumeration value="IB"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Vocational"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AdditionalCreditType")
@XmlEnum
public enum AdditionalCreditType {

    AP("AP"),
    @XmlEnumValue("Dual credit")
    DUAL_CREDIT("Dual credit"),
    IB("IB"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Vocational")
    VOCATIONAL("Vocational");
    private final String value;

    AdditionalCreditType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AdditionalCreditType fromValue(String v) {
        for (AdditionalCreditType c: AdditionalCreditType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
