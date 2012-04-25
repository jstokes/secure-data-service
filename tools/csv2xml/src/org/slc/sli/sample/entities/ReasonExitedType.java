//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.20 at 03:09:04 PM EDT 
//


package org.slc.sli.sample.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReasonExitedType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ReasonExitedType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Transfer to regular education"/>
 *     &lt;enumeration value="Received a certificate"/>
 *     &lt;enumeration value="Reached maximum age"/>
 *     &lt;enumeration value="Died"/>
 *     &lt;enumeration value="Died or is permanently incapacitated"/>
 *     &lt;enumeration value="Discontinued schooling"/>
 *     &lt;enumeration value="Graduated with a high school diploma"/>
 *     &lt;enumeration value="Received certificate of completion, modified diploma, or finished IEP requirements"/>
 *     &lt;enumeration value="Program completion"/>
 *     &lt;enumeration value="Reached maximum age"/>
 *     &lt;enumeration value="No longer receiving special education"/>
 *     &lt;enumeration value="Refused services"/>
 *     &lt;enumeration value="Transferred to another district or school, known to be continuing in program/service"/>
 *     &lt;enumeration value="Transferred to another district or school, not known to be continuing in program/service"/>
 *     &lt;enumeration value="Transferred to another district or school, known not to be continuing in program/service"/>
 *     &lt;enumeration value="Suspended from school"/>
 *     &lt;enumeration value="Discontinued schooling, special education only"/>
 *     &lt;enumeration value="Discontinued schooling, not special education"/>
 *     &lt;enumeration value="Expulsion"/>
 *     &lt;enumeration value="Program discontinued"/>
 *     &lt;enumeration value="Completion of IFSP prior to reaching maximum age for Part C"/>
 *     &lt;enumeration value="Eligible for IDEA, Part B"/>
 *     &lt;enumeration value="Not eligible for Part B, exit with referrals to other programs"/>
 *     &lt;enumeration value="Part B eligibility not determined"/>
 *     &lt;enumeration value="Moved out of state"/>
 *     &lt;enumeration value="Withdrawal by a parent (or guardian)"/>
 *     &lt;enumeration value="Unknown reason"/>
 *     &lt;enumeration value="Not eligible for Part B, exit with no referrals"/>
 *     &lt;enumeration value="Attempts to contact the parent and/or child were unsuccessful"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ReasonExitedType")
@XmlEnum
public enum ReasonExitedType {

    @XmlEnumValue("Transfer to regular education")
    TRANSFER_TO_REGULAR_EDUCATION("Transfer to regular education"),
    @XmlEnumValue("Received a certificate")
    RECEIVED_A_CERTIFICATE("Received a certificate"),
    @XmlEnumValue("Reached maximum age")
    REACHED_MAXIMUM_AGE("Reached maximum age"),
    @XmlEnumValue("Died")
    DIED("Died"),
    @XmlEnumValue("Died or is permanently incapacitated")
    DIED_OR_IS_PERMANENTLY_INCAPACITATED("Died or is permanently incapacitated"),
    @XmlEnumValue("Discontinued schooling")
    DISCONTINUED_SCHOOLING("Discontinued schooling"),
    @XmlEnumValue("Graduated with a high school diploma")
    GRADUATED_WITH_A_HIGH_SCHOOL_DIPLOMA("Graduated with a high school diploma"),
    @XmlEnumValue("Received certificate of completion, modified diploma, or finished IEP requirements")
    RECEIVED_CERTIFICATE_OF_COMPLETION_MODIFIED_DIPLOMA_OR_FINISHED_IEP_REQUIREMENTS("Received certificate of completion, modified diploma, or finished IEP requirements"),
    @XmlEnumValue("Program completion")
    PROGRAM_COMPLETION("Program completion"),
    @XmlEnumValue("No longer receiving special education")
    NO_LONGER_RECEIVING_SPECIAL_EDUCATION("No longer receiving special education"),
    @XmlEnumValue("Refused services")
    REFUSED_SERVICES("Refused services"),
    @XmlEnumValue("Transferred to another district or school, known to be continuing in program/service")
    TRANSFERRED_TO_ANOTHER_DISTRICT_OR_SCHOOL_KNOWN_TO_BE_CONTINUING_IN_PROGRAM_SERVICE("Transferred to another district or school, known to be continuing in program/service"),
    @XmlEnumValue("Transferred to another district or school, not known to be continuing in program/service")
    TRANSFERRED_TO_ANOTHER_DISTRICT_OR_SCHOOL_NOT_KNOWN_TO_BE_CONTINUING_IN_PROGRAM_SERVICE("Transferred to another district or school, not known to be continuing in program/service"),
    @XmlEnumValue("Transferred to another district or school, known not to be continuing in program/service")
    TRANSFERRED_TO_ANOTHER_DISTRICT_OR_SCHOOL_KNOWN_NOT_TO_BE_CONTINUING_IN_PROGRAM_SERVICE("Transferred to another district or school, known not to be continuing in program/service"),
    @XmlEnumValue("Suspended from school")
    SUSPENDED_FROM_SCHOOL("Suspended from school"),
    @XmlEnumValue("Discontinued schooling, special education only")
    DISCONTINUED_SCHOOLING_SPECIAL_EDUCATION_ONLY("Discontinued schooling, special education only"),
    @XmlEnumValue("Discontinued schooling, not special education")
    DISCONTINUED_SCHOOLING_NOT_SPECIAL_EDUCATION("Discontinued schooling, not special education"),
    @XmlEnumValue("Expulsion")
    EXPULSION("Expulsion"),
    @XmlEnumValue("Program discontinued")
    PROGRAM_DISCONTINUED("Program discontinued"),
    @XmlEnumValue("Completion of IFSP prior to reaching maximum age for Part C")
    COMPLETION_OF_IFSP_PRIOR_TO_REACHING_MAXIMUM_AGE_FOR_PART_C("Completion of IFSP prior to reaching maximum age for Part C"),
    @XmlEnumValue("Eligible for IDEA, Part B")
    ELIGIBLE_FOR_IDEA_PART_B("Eligible for IDEA, Part B"),
    @XmlEnumValue("Not eligible for Part B, exit with referrals to other programs")
    NOT_ELIGIBLE_FOR_PART_B_EXIT_WITH_REFERRALS_TO_OTHER_PROGRAMS("Not eligible for Part B, exit with referrals to other programs"),
    @XmlEnumValue("Part B eligibility not determined")
    PART_B_ELIGIBILITY_NOT_DETERMINED("Part B eligibility not determined"),
    @XmlEnumValue("Moved out of state")
    MOVED_OUT_OF_STATE("Moved out of state"),
    @XmlEnumValue("Withdrawal by a parent (or guardian)")
    WITHDRAWAL_BY_A_PARENT_OR_GUARDIAN("Withdrawal by a parent (or guardian)"),
    @XmlEnumValue("Unknown reason")
    UNKNOWN_REASON("Unknown reason"),
    @XmlEnumValue("Not eligible for Part B, exit with no referrals")
    NOT_ELIGIBLE_FOR_PART_B_EXIT_WITH_NO_REFERRALS("Not eligible for Part B, exit with no referrals"),
    @XmlEnumValue("Attempts to contact the parent and/or child were unsuccessful")
    ATTEMPTS_TO_CONTACT_THE_PARENT_AND_OR_CHILD_WERE_UNSUCCESSFUL("Attempts to contact the parent and/or child were unsuccessful"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    ReasonExitedType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReasonExitedType fromValue(String v) {
        for (ReasonExitedType c: ReasonExitedType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
