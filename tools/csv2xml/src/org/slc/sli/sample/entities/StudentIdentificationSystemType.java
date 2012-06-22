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
 * <p>Java class for StudentIdentificationSystemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StudentIdentificationSystemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Canadian SIN"/>
 *     &lt;enumeration value="District"/>
 *     &lt;enumeration value="Family"/>
 *     &lt;enumeration value="Federal"/>
 *     &lt;enumeration value="Local"/>
 *     &lt;enumeration value="National Migrant"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="School"/>
 *     &lt;enumeration value="SSN"/>
 *     &lt;enumeration value="State"/>
 *     &lt;enumeration value="State Migrant"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StudentIdentificationSystemType")
@XmlEnum
public enum StudentIdentificationSystemType {

    @XmlEnumValue("Canadian SIN")
    CANADIAN_SIN("Canadian SIN"),
    @XmlEnumValue("District")
    DISTRICT("District"),
    @XmlEnumValue("Family")
    FAMILY("Family"),
    @XmlEnumValue("Federal")
    FEDERAL("Federal"),
    @XmlEnumValue("Local")
    LOCAL("Local"),
    @XmlEnumValue("National Migrant")
    NATIONAL_MIGRANT("National Migrant"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("School")
    SCHOOL("School"),
    SSN("SSN"),
    @XmlEnumValue("State")
    STATE("State"),
    @XmlEnumValue("State Migrant")
    STATE_MIGRANT("State Migrant");
    private final String value;

    StudentIdentificationSystemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StudentIdentificationSystemType fromValue(String v) {
        for (StudentIdentificationSystemType c: StudentIdentificationSystemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
