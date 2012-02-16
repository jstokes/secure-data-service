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
 * <p>Java class for DiplomaLevelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DiplomaLevelType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Minimum"/>
 *     &lt;enumeration value="Recommended"/>
 *     &lt;enumeration value="Distinguished"/>
 *     &lt;enumeration value="Cum laude"/>
 *     &lt;enumeration value="Magna cum laude"/>
 *     &lt;enumeration value="Summa cum laude"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DiplomaLevelType")
@XmlEnum
public enum DiplomaLevelType {

    @XmlEnumValue("Minimum")
    MINIMUM("Minimum"),
    @XmlEnumValue("Recommended")
    RECOMMENDED("Recommended"),
    @XmlEnumValue("Distinguished")
    DISTINGUISHED("Distinguished"),
    @XmlEnumValue("Cum laude")
    CUM_LAUDE("Cum laude"),
    @XmlEnumValue("Magna cum laude")
    MAGNA_CUM_LAUDE("Magna cum laude"),
    @XmlEnumValue("Summa cum laude")
    SUMMA_CUM_LAUDE("Summa cum laude");
    private final String value;

    DiplomaLevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DiplomaLevelType fromValue(String v) {
        for (DiplomaLevelType c: DiplomaLevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
