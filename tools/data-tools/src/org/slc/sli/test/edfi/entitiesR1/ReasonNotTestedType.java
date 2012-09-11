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
 * <p>Java class for reasonNotTestedType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="reasonNotTestedType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Absent"/>
 *     &lt;enumeration value="LEP exempt"/>
 *     &lt;enumeration value="LEP postponement"/>
 *     &lt;enumeration value="Not appropriate (ARD decision)"/>
 *     &lt;enumeration value="Not tested (ARD decision)"/>
 *     &lt;enumeration value="Alternate assessment administered"/>
 *     &lt;enumeration value="Parental waiver"/>
 *     &lt;enumeration value="Foreign exchange student waiver"/>
 *     &lt;enumeration value="Refusal by parent"/>
 *     &lt;enumeration value="Refusal by student"/>
 *     &lt;enumeration value="Medical waiver"/>
 *     &lt;enumeration value="Disruptive behavior"/>
 *     &lt;enumeration value="Previously passed the examination"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "reasonNotTestedType")
@XmlEnum
public enum ReasonNotTestedType {

    @XmlEnumValue("Absent")
    ABSENT("Absent"),
    @XmlEnumValue("LEP exempt")
    LEP_EXEMPT("LEP exempt"),
    @XmlEnumValue("LEP postponement")
    LEP_POSTPONEMENT("LEP postponement"),
    @XmlEnumValue("Not appropriate (ARD decision)")
    NOT_APPROPRIATE_ARD_DECISION("Not appropriate (ARD decision)"),
    @XmlEnumValue("Not tested (ARD decision)")
    NOT_TESTED_ARD_DECISION("Not tested (ARD decision)"),
    @XmlEnumValue("Alternate assessment administered")
    ALTERNATE_ASSESSMENT_ADMINISTERED("Alternate assessment administered"),
    @XmlEnumValue("Parental waiver")
    PARENTAL_WAIVER("Parental waiver"),
    @XmlEnumValue("Foreign exchange student waiver")
    FOREIGN_EXCHANGE_STUDENT_WAIVER("Foreign exchange student waiver"),
    @XmlEnumValue("Refusal by parent")
    REFUSAL_BY_PARENT("Refusal by parent"),
    @XmlEnumValue("Refusal by student")
    REFUSAL_BY_STUDENT("Refusal by student"),
    @XmlEnumValue("Medical waiver")
    MEDICAL_WAIVER("Medical waiver"),
    @XmlEnumValue("Disruptive behavior")
    DISRUPTIVE_BEHAVIOR("Disruptive behavior"),
    @XmlEnumValue("Previously passed the examination")
    PREVIOUSLY_PASSED_THE_EXAMINATION("Previously passed the examination"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    ReasonNotTestedType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReasonNotTestedType fromValue(String v) {
        for (ReasonNotTestedType c: ReasonNotTestedType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
