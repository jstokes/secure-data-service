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
 * <p>Java class for creditType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="creditType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Carnegie unit"/>
 *     &lt;enumeration value="Semester hour credit"/>
 *     &lt;enumeration value="Trimester hour credit"/>
 *     &lt;enumeration value="Quarter hour credit"/>
 *     &lt;enumeration value="Nine month year hour credit"/>
 *     &lt;enumeration value="Twelve month year hour credit"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "creditType")
@XmlEnum
public enum CreditType {

    @XmlEnumValue("Carnegie unit")
    CARNEGIE_UNIT("Carnegie unit"),
    @XmlEnumValue("Semester hour credit")
    SEMESTER_HOUR_CREDIT("Semester hour credit"),
    @XmlEnumValue("Trimester hour credit")
    TRIMESTER_HOUR_CREDIT("Trimester hour credit"),
    @XmlEnumValue("Quarter hour credit")
    QUARTER_HOUR_CREDIT("Quarter hour credit"),
    @XmlEnumValue("Nine month year hour credit")
    NINE_MONTH_YEAR_HOUR_CREDIT("Nine month year hour credit"),
    @XmlEnumValue("Twelve month year hour credit")
    TWELVE_MONTH_YEAR_HOUR_CREDIT("Twelve month year hour credit"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    CreditType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CreditType fromValue(String v) {
        for (CreditType c: CreditType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
