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
 * <p>Java class for Section504DisabilityItemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Section504DisabilityItemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Attention Deficit Hyperactivity Disorder (ADHD)"/>
 *     &lt;enumeration value="Medical Condition"/>
 *     &lt;enumeration value="Motor Impairment"/>
 *     &lt;enumeration value="Sensory Impairment"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Section504DisabilityItemType")
@XmlEnum
public enum Section504DisabilityItemType {

    @XmlEnumValue("Attention Deficit Hyperactivity Disorder (ADHD)")
    ATTENTION_DEFICIT_HYPERACTIVITY_DISORDER_ADHD("Attention Deficit Hyperactivity Disorder (ADHD)"),
    @XmlEnumValue("Medical Condition")
    MEDICAL_CONDITION("Medical Condition"),
    @XmlEnumValue("Motor Impairment")
    MOTOR_IMPAIRMENT("Motor Impairment"),
    @XmlEnumValue("Sensory Impairment")
    SENSORY_IMPAIRMENT("Sensory Impairment"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    Section504DisabilityItemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Section504DisabilityItemType fromValue(String v) {
        for (Section504DisabilityItemType c: Section504DisabilityItemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
