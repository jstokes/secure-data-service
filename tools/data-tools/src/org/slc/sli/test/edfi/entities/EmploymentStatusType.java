//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EmploymentStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EmploymentStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Probationary"/>
 *     &lt;enumeration value="Contractual"/>
 *     &lt;enumeration value="Substitute/temporary"/>
 *     &lt;enumeration value="Tenured or permanent"/>
 *     &lt;enumeration value="Volunteer/no contract"/>
 *     &lt;enumeration value="Employed or affiliated with outside organization"/>
 *     &lt;enumeration value="Contingent upon funding"/>
 *     &lt;enumeration value="Non-contractual"/>
 *     &lt;enumeration value="Self-employed part-time"/>
 *     &lt;enumeration value="Employed or affiliated with outside agency part-time"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EmploymentStatusType")
@XmlEnum
public enum EmploymentStatusType {

    @XmlEnumValue("Probationary")
    PROBATIONARY("Probationary"),
    @XmlEnumValue("Contractual")
    CONTRACTUAL("Contractual"),
    @XmlEnumValue("Substitute/temporary")
    SUBSTITUTE_TEMPORARY("Substitute/temporary"),
    @XmlEnumValue("Tenured or permanent")
    TENURED_OR_PERMANENT("Tenured or permanent"),
    @XmlEnumValue("Volunteer/no contract")
    VOLUNTEER_NO_CONTRACT("Volunteer/no contract"),
    @XmlEnumValue("Employed or affiliated with outside organization")
    EMPLOYED_OR_AFFILIATED_WITH_OUTSIDE_ORGANIZATION("Employed or affiliated with outside organization"),
    @XmlEnumValue("Contingent upon funding")
    CONTINGENT_UPON_FUNDING("Contingent upon funding"),
    @XmlEnumValue("Non-contractual")
    NON_CONTRACTUAL("Non-contractual"),
    @XmlEnumValue("Self-employed part-time")
    SELF_EMPLOYED_PART_TIME("Self-employed part-time"),
    @XmlEnumValue("Employed or affiliated with outside agency part-time")
    EMPLOYED_OR_AFFILIATED_WITH_OUTSIDE_AGENCY_PART_TIME("Employed or affiliated with outside agency part-time"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    EmploymentStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EmploymentStatusType fromValue(String v) {
        for (EmploymentStatusType c: EmploymentStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
