//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CredentialType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CredentialType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Certification"/>
 *     &lt;enumeration value="Endorsement"/>
 *     &lt;enumeration value="Licensure"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Registration"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CredentialType")
@XmlEnum
public enum CredentialType {

    @XmlEnumValue("Certification")
    CERTIFICATION("Certification"),
    @XmlEnumValue("Endorsement")
    ENDORSEMENT("Endorsement"),
    @XmlEnumValue("Licensure")
    LICENSURE("Licensure"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Registration")
    REGISTRATION("Registration");
    private final String value;

    CredentialType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CredentialType fromValue(String v) {
        for (CredentialType c: CredentialType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
