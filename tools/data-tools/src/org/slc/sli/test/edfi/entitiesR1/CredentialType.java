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
 * <p>Java class for credentialType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="credentialType">
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
@XmlType(name = "credentialType")
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
