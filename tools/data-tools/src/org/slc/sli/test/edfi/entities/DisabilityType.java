//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 01:12:00 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DisabilityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DisabilityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Autistic/Autism"/>
 *     &lt;enumeration value="Deaf-Blindness"/>
 *     &lt;enumeration value="Deafness"/>
 *     &lt;enumeration value="Developmental Delay"/>
 *     &lt;enumeration value="Emotional Disturbance"/>
 *     &lt;enumeration value="Hearing/Auditory Impairment"/>
 *     &lt;enumeration value="Infants and Toddlers with Disabilities"/>
 *     &lt;enumeration value="Mental Retardation"/>
 *     &lt;enumeration value="Multiple Disabilities"/>
 *     &lt;enumeration value="Orthopedic Impairment"/>
 *     &lt;enumeration value="Other Health Impairment"/>
 *     &lt;enumeration value="Speech or Language Impairment"/>
 *     &lt;enumeration value="Specific Learning Disability"/>
 *     &lt;enumeration value="Traumatic Brain Delay"/>
 *     &lt;enumeration value="Visual Impairment"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DisabilityType")
@XmlEnum
public enum DisabilityType {

    @XmlEnumValue("Autistic/Autism")
    AUTISTIC_AUTISM("Autistic/Autism"),
    @XmlEnumValue("Deaf-Blindness")
    DEAF_BLINDNESS("Deaf-Blindness"),
    @XmlEnumValue("Deafness")
    DEAFNESS("Deafness"),
    @XmlEnumValue("Developmental Delay")
    DEVELOPMENTAL_DELAY("Developmental Delay"),
    @XmlEnumValue("Emotional Disturbance")
    EMOTIONAL_DISTURBANCE("Emotional Disturbance"),
    @XmlEnumValue("Hearing/Auditory Impairment")
    HEARING_AUDITORY_IMPAIRMENT("Hearing/Auditory Impairment"),
    @XmlEnumValue("Infants and Toddlers with Disabilities")
    INFANTS_AND_TODDLERS_WITH_DISABILITIES("Infants and Toddlers with Disabilities"),
    @XmlEnumValue("Mental Retardation")
    MENTAL_RETARDATION("Mental Retardation"),
    @XmlEnumValue("Multiple Disabilities")
    MULTIPLE_DISABILITIES("Multiple Disabilities"),
    @XmlEnumValue("Orthopedic Impairment")
    ORTHOPEDIC_IMPAIRMENT("Orthopedic Impairment"),
    @XmlEnumValue("Other Health Impairment")
    OTHER_HEALTH_IMPAIRMENT("Other Health Impairment"),
    @XmlEnumValue("Speech or Language Impairment")
    SPEECH_OR_LANGUAGE_IMPAIRMENT("Speech or Language Impairment"),
    @XmlEnumValue("Specific Learning Disability")
    SPECIFIC_LEARNING_DISABILITY("Specific Learning Disability"),
    @XmlEnumValue("Traumatic Brain Delay")
    TRAUMATIC_BRAIN_DELAY("Traumatic Brain Delay"),
    @XmlEnumValue("Visual Impairment")
    VISUAL_IMPAIRMENT("Visual Impairment");
    private final String value;

    DisabilityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DisabilityType fromValue(String v) {
        for (DisabilityType c: DisabilityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
