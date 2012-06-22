/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
 * <p>Java class for EntryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Transfer from a public school in the same local education agency"/>
 *     &lt;enumeration value="Transfer from a public school in a different local education agency in the same state"/>
 *     &lt;enumeration value="Transfer from a public school in a different state"/>
 *     &lt;enumeration value="Transfer from a private, non-religiously-affiliated school in the same local education agency"/>
 *     &lt;enumeration value="Transfer from a private, non-religiously-affiliated school in a different local education agency in the same state"/>
 *     &lt;enumeration value="Transfer from a private, non-religiously-affiliated school in a different state"/>
 *     &lt;enumeration value="Transfer from a private, religiously-affiliated school in the same local education agency"/>
 *     &lt;enumeration value="Transfer from a private, religiously-affiliated school in a different local education agency in the same state"/>
 *     &lt;enumeration value="Transfer from a private, religiously-affiliated school in a different state"/>
 *     &lt;enumeration value="Transfer from a school outside of the country"/>
 *     &lt;enumeration value="Transfer from an institution"/>
 *     &lt;enumeration value="Transfer from a charter school"/>
 *     &lt;enumeration value="Transfer from home schooling"/>
 *     &lt;enumeration value="Re-entry from the same school with no interruption of schooling"/>
 *     &lt;enumeration value="Re-entry after a voluntary withdrawal"/>
 *     &lt;enumeration value="Re-entry after an involuntary withdrawal"/>
 *     &lt;enumeration value="Original entry into a United States school"/>
 *     &lt;enumeration value="Original entry into a United States school from a foreign country with no interruption in schooling"/>
 *     &lt;enumeration value="Original entry into a United States school from a foreign country with an interruption in schooling"/>
 *     &lt;enumeration value="Next year school"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntryType")
@XmlEnum
public enum EntryType {

    @XmlEnumValue("Transfer from a public school in the same local education agency")
    TRANSFER_FROM_A_PUBLIC_SCHOOL_IN_THE_SAME_LOCAL_EDUCATION_AGENCY("Transfer from a public school in the same local education agency"),
    @XmlEnumValue("Transfer from a public school in a different local education agency in the same state")
    TRANSFER_FROM_A_PUBLIC_SCHOOL_IN_A_DIFFERENT_LOCAL_EDUCATION_AGENCY_IN_THE_SAME_STATE("Transfer from a public school in a different local education agency in the same state"),
    @XmlEnumValue("Transfer from a public school in a different state")
    TRANSFER_FROM_A_PUBLIC_SCHOOL_IN_A_DIFFERENT_STATE("Transfer from a public school in a different state"),
    @XmlEnumValue("Transfer from a private, non-religiously-affiliated school in the same local education agency")
    TRANSFER_FROM_A_PRIVATE_NON_RELIGIOUSLY_AFFILIATED_SCHOOL_IN_THE_SAME_LOCAL_EDUCATION_AGENCY("Transfer from a private, non-religiously-affiliated school in the same local education agency"),
    @XmlEnumValue("Transfer from a private, non-religiously-affiliated school in a different local education agency in the same state")
    TRANSFER_FROM_A_PRIVATE_NON_RELIGIOUSLY_AFFILIATED_SCHOOL_IN_A_DIFFERENT_LOCAL_EDUCATION_AGENCY_IN_THE_SAME_STATE("Transfer from a private, non-religiously-affiliated school in a different local education agency in the same state"),
    @XmlEnumValue("Transfer from a private, non-religiously-affiliated school in a different state")
    TRANSFER_FROM_A_PRIVATE_NON_RELIGIOUSLY_AFFILIATED_SCHOOL_IN_A_DIFFERENT_STATE("Transfer from a private, non-religiously-affiliated school in a different state"),
    @XmlEnumValue("Transfer from a private, religiously-affiliated school in the same local education agency")
    TRANSFER_FROM_A_PRIVATE_RELIGIOUSLY_AFFILIATED_SCHOOL_IN_THE_SAME_LOCAL_EDUCATION_AGENCY("Transfer from a private, religiously-affiliated school in the same local education agency"),
    @XmlEnumValue("Transfer from a private, religiously-affiliated school in a different local education agency in the same state")
    TRANSFER_FROM_A_PRIVATE_RELIGIOUSLY_AFFILIATED_SCHOOL_IN_A_DIFFERENT_LOCAL_EDUCATION_AGENCY_IN_THE_SAME_STATE("Transfer from a private, religiously-affiliated school in a different local education agency in the same state"),
    @XmlEnumValue("Transfer from a private, religiously-affiliated school in a different state")
    TRANSFER_FROM_A_PRIVATE_RELIGIOUSLY_AFFILIATED_SCHOOL_IN_A_DIFFERENT_STATE("Transfer from a private, religiously-affiliated school in a different state"),
    @XmlEnumValue("Transfer from a school outside of the country")
    TRANSFER_FROM_A_SCHOOL_OUTSIDE_OF_THE_COUNTRY("Transfer from a school outside of the country"),
    @XmlEnumValue("Transfer from an institution")
    TRANSFER_FROM_AN_INSTITUTION("Transfer from an institution"),
    @XmlEnumValue("Transfer from a charter school")
    TRANSFER_FROM_A_CHARTER_SCHOOL("Transfer from a charter school"),
    @XmlEnumValue("Transfer from home schooling")
    TRANSFER_FROM_HOME_SCHOOLING("Transfer from home schooling"),
    @XmlEnumValue("Re-entry from the same school with no interruption of schooling")
    RE_ENTRY_FROM_THE_SAME_SCHOOL_WITH_NO_INTERRUPTION_OF_SCHOOLING("Re-entry from the same school with no interruption of schooling"),
    @XmlEnumValue("Re-entry after a voluntary withdrawal")
    RE_ENTRY_AFTER_A_VOLUNTARY_WITHDRAWAL("Re-entry after a voluntary withdrawal"),
    @XmlEnumValue("Re-entry after an involuntary withdrawal")
    RE_ENTRY_AFTER_AN_INVOLUNTARY_WITHDRAWAL("Re-entry after an involuntary withdrawal"),
    @XmlEnumValue("Original entry into a United States school")
    ORIGINAL_ENTRY_INTO_A_UNITED_STATES_SCHOOL("Original entry into a United States school"),
    @XmlEnumValue("Original entry into a United States school from a foreign country with no interruption in schooling")
    ORIGINAL_ENTRY_INTO_A_UNITED_STATES_SCHOOL_FROM_A_FOREIGN_COUNTRY_WITH_NO_INTERRUPTION_IN_SCHOOLING("Original entry into a United States school from a foreign country with no interruption in schooling"),
    @XmlEnumValue("Original entry into a United States school from a foreign country with an interruption in schooling")
    ORIGINAL_ENTRY_INTO_A_UNITED_STATES_SCHOOL_FROM_A_FOREIGN_COUNTRY_WITH_AN_INTERRUPTION_IN_SCHOOLING("Original entry into a United States school from a foreign country with an interruption in schooling"),
    @XmlEnumValue("Next year school")
    NEXT_YEAR_SCHOOL("Next year school"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    EntryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EntryType fromValue(String v) {
        for (EntryType c: EntryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
