//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.15 at 05:29:39 PM EST 
//


package org.slc.sli.test.edfi.entitiesEdfiXsdSLI;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TeachingCredentialType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TeachingCredentialType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Emergency"/>
 *     &lt;enumeration value="Emergency Certified"/>
 *     &lt;enumeration value="Emergency Non-Certified"/>
 *     &lt;enumeration value="Emergency Teaching"/>
 *     &lt;enumeration value="Intern"/>
 *     &lt;enumeration value="Master"/>
 *     &lt;enumeration value="Nonrenewable"/>
 *     &lt;enumeration value="One Year"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Paraprofessional"/>
 *     &lt;enumeration value="Professional"/>
 *     &lt;enumeration value="Probationary"/>
 *     &lt;enumeration value="Provisional"/>
 *     &lt;enumeration value="Regular"/>
 *     &lt;enumeration value="Retired"/>
 *     &lt;enumeration value="Specialist"/>
 *     &lt;enumeration value="Substitute"/>
 *     &lt;enumeration value="TeacherAssistant"/>
 *     &lt;enumeration value="Temporary"/>
 *     &lt;enumeration value="Special Assignment"/>
 *     &lt;enumeration value="Standard"/>
 *     &lt;enumeration value="Standard Professional"/>
 *     &lt;enumeration value="Temporary Classroom"/>
 *     &lt;enumeration value="Temporary Exemption"/>
 *     &lt;enumeration value="Unknown"/>
 *     &lt;enumeration value="Unknown Permit"/>
 *     &lt;enumeration value="Vocational"/>
 *     &lt;enumeration value="Standard Paraprofessional"/>
 *     &lt;enumeration value="Probationary Extension"/>
 *     &lt;enumeration value="Probationary Second Extension"/>
 *     &lt;enumeration value="Visiting International Teacher"/>
 *     &lt;enumeration value="District Local"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TeachingCredentialType")
@XmlEnum
public enum TeachingCredentialType {

    @XmlEnumValue("Emergency")
    EMERGENCY("Emergency"),
    @XmlEnumValue("Emergency Certified")
    EMERGENCY_CERTIFIED("Emergency Certified"),
    @XmlEnumValue("Emergency Non-Certified")
    EMERGENCY_NON_CERTIFIED("Emergency Non-Certified"),
    @XmlEnumValue("Emergency Teaching")
    EMERGENCY_TEACHING("Emergency Teaching"),
    @XmlEnumValue("Intern")
    INTERN("Intern"),
    @XmlEnumValue("Master")
    MASTER("Master"),
    @XmlEnumValue("Nonrenewable")
    NONRENEWABLE("Nonrenewable"),
    @XmlEnumValue("One Year")
    ONE_YEAR("One Year"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Paraprofessional")
    PARAPROFESSIONAL("Paraprofessional"),
    @XmlEnumValue("Professional")
    PROFESSIONAL("Professional"),
    @XmlEnumValue("Probationary")
    PROBATIONARY("Probationary"),
    @XmlEnumValue("Provisional")
    PROVISIONAL("Provisional"),
    @XmlEnumValue("Regular")
    REGULAR("Regular"),
    @XmlEnumValue("Retired")
    RETIRED("Retired"),
    @XmlEnumValue("Specialist")
    SPECIALIST("Specialist"),
    @XmlEnumValue("Substitute")
    SUBSTITUTE("Substitute"),
    @XmlEnumValue("TeacherAssistant")
    TEACHER_ASSISTANT("TeacherAssistant"),
    @XmlEnumValue("Temporary")
    TEMPORARY("Temporary"),
    @XmlEnumValue("Special Assignment")
    SPECIAL_ASSIGNMENT("Special Assignment"),
    @XmlEnumValue("Standard")
    STANDARD("Standard"),
    @XmlEnumValue("Standard Professional")
    STANDARD_PROFESSIONAL("Standard Professional"),
    @XmlEnumValue("Temporary Classroom")
    TEMPORARY_CLASSROOM("Temporary Classroom"),
    @XmlEnumValue("Temporary Exemption")
    TEMPORARY_EXEMPTION("Temporary Exemption"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Unknown Permit")
    UNKNOWN_PERMIT("Unknown Permit"),
    @XmlEnumValue("Vocational")
    VOCATIONAL("Vocational"),
    @XmlEnumValue("Standard Paraprofessional")
    STANDARD_PARAPROFESSIONAL("Standard Paraprofessional"),
    @XmlEnumValue("Probationary Extension")
    PROBATIONARY_EXTENSION("Probationary Extension"),
    @XmlEnumValue("Probationary Second Extension")
    PROBATIONARY_SECOND_EXTENSION("Probationary Second Extension"),
    @XmlEnumValue("Visiting International Teacher")
    VISITING_INTERNATIONAL_TEACHER("Visiting International Teacher"),
    @XmlEnumValue("District Local")
    DISTRICT_LOCAL("District Local");
    private final String value;

    TeachingCredentialType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TeachingCredentialType fromValue(String v) {
        for (TeachingCredentialType c: TeachingCredentialType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
