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
 * <p>Java class for IncidentBehaviorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IncidentBehaviorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Alcohol"/>
 *     &lt;enumeration value="Arson"/>
 *     &lt;enumeration value="Attendance Policy Violation"/>
 *     &lt;enumeration value="Battery"/>
 *     &lt;enumeration value="Burglary/Breaking and Entering"/>
 *     &lt;enumeration value="Disorderly Conduct"/>
 *     &lt;enumeration value="Drugs Excluding Alcohol and Tobacco"/>
 *     &lt;enumeration value="Fighting"/>
 *     &lt;enumeration value="Harassment, Nonsexual"/>
 *     &lt;enumeration value="Harassment, Sexual"/>
 *     &lt;enumeration value="Homicide"/>
 *     &lt;enumeration value="Inappropriate Use of Medication"/>
 *     &lt;enumeration value="Insubordination"/>
 *     &lt;enumeration value="Kidnapping"/>
 *     &lt;enumeration value="Obscene Behavior"/>
 *     &lt;enumeration value="Physical Altercation, Minor"/>
 *     &lt;enumeration value="Robbery"/>
 *     &lt;enumeration value="School Threat"/>
 *     &lt;enumeration value="Theft"/>
 *     &lt;enumeration value="Threat/Intimidation"/>
 *     &lt;enumeration value="Tobacco Possession or Use"/>
 *     &lt;enumeration value="Trespassing"/>
 *     &lt;enumeration value="Vandalism"/>
 *     &lt;enumeration value="Violation of School Rules"/>
 *     &lt;enumeration value="Weapons Possession"/>
 *     &lt;enumeration value="Harassment or bullying on the basis of disability"/>
 *     &lt;enumeration value="Harassment or bullying on the basis of race, color, or national origin"/>
 *     &lt;enumeration value="Harassment or bullying on the basis of sex"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IncidentBehaviorType")
@XmlEnum
public enum IncidentBehaviorType {

    @XmlEnumValue("Alcohol")
    ALCOHOL("Alcohol"),
    @XmlEnumValue("Arson")
    ARSON("Arson"),
    @XmlEnumValue("Attendance Policy Violation")
    ATTENDANCE_POLICY_VIOLATION("Attendance Policy Violation"),
    @XmlEnumValue("Battery")
    BATTERY("Battery"),
    @XmlEnumValue("Burglary/Breaking and Entering")
    BURGLARY_BREAKING_AND_ENTERING("Burglary/Breaking and Entering"),
    @XmlEnumValue("Disorderly Conduct")
    DISORDERLY_CONDUCT("Disorderly Conduct"),
    @XmlEnumValue("Drugs Excluding Alcohol and Tobacco")
    DRUGS_EXCLUDING_ALCOHOL_AND_TOBACCO("Drugs Excluding Alcohol and Tobacco"),
    @XmlEnumValue("Fighting")
    FIGHTING("Fighting"),
    @XmlEnumValue("Harassment, Nonsexual")
    HARASSMENT_NONSEXUAL("Harassment, Nonsexual"),
    @XmlEnumValue("Harassment, Sexual")
    HARASSMENT_SEXUAL("Harassment, Sexual"),
    @XmlEnumValue("Homicide")
    HOMICIDE("Homicide"),
    @XmlEnumValue("Inappropriate Use of Medication")
    INAPPROPRIATE_USE_OF_MEDICATION("Inappropriate Use of Medication"),
    @XmlEnumValue("Insubordination")
    INSUBORDINATION("Insubordination"),
    @XmlEnumValue("Kidnapping")
    KIDNAPPING("Kidnapping"),
    @XmlEnumValue("Obscene Behavior")
    OBSCENE_BEHAVIOR("Obscene Behavior"),
    @XmlEnumValue("Physical Altercation, Minor")
    PHYSICAL_ALTERCATION_MINOR("Physical Altercation, Minor"),
    @XmlEnumValue("Robbery")
    ROBBERY("Robbery"),
    @XmlEnumValue("School Threat")
    SCHOOL_THREAT("School Threat"),
    @XmlEnumValue("Theft")
    THEFT("Theft"),
    @XmlEnumValue("Threat/Intimidation")
    THREAT_INTIMIDATION("Threat/Intimidation"),
    @XmlEnumValue("Tobacco Possession or Use")
    TOBACCO_POSSESSION_OR_USE("Tobacco Possession or Use"),
    @XmlEnumValue("Trespassing")
    TRESPASSING("Trespassing"),
    @XmlEnumValue("Vandalism")
    VANDALISM("Vandalism"),
    @XmlEnumValue("Violation of School Rules")
    VIOLATION_OF_SCHOOL_RULES("Violation of School Rules"),
    @XmlEnumValue("Weapons Possession")
    WEAPONS_POSSESSION("Weapons Possession"),
    @XmlEnumValue("Harassment or bullying on the basis of disability")
    HARASSMENT_OR_BULLYING_ON_THE_BASIS_OF_DISABILITY("Harassment or bullying on the basis of disability"),
    @XmlEnumValue("Harassment or bullying on the basis of race, color, or national origin")
    HARASSMENT_OR_BULLYING_ON_THE_BASIS_OF_RACE_COLOR_OR_NATIONAL_ORIGIN("Harassment or bullying on the basis of race, color, or national origin"),
    @XmlEnumValue("Harassment or bullying on the basis of sex")
    HARASSMENT_OR_BULLYING_ON_THE_BASIS_OF_SEX("Harassment or bullying on the basis of sex");
    private final String value;

    IncidentBehaviorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IncidentBehaviorType fromValue(String v) {
        for (IncidentBehaviorType c: IncidentBehaviorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
