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
// Generated on: 2012.05.31 at 09:35:49 AM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TitleIPartAParticipantType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TitleIPartAParticipantType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Public Targeted Assistance Program"/>
 *     &lt;enumeration value="Public Schoolwide Program"/>
 *     &lt;enumeration value="Private school students participating"/>
 *     &lt;enumeration value="Local Neglected Program"/>
 *     &lt;enumeration value="Was not served"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TitleIPartAParticipantType")
@XmlEnum
public enum TitleIPartAParticipantType {

    @XmlEnumValue("Public Targeted Assistance Program")
    PUBLIC_TARGETED_ASSISTANCE_PROGRAM("Public Targeted Assistance Program"),
    @XmlEnumValue("Public Schoolwide Program")
    PUBLIC_SCHOOLWIDE_PROGRAM("Public Schoolwide Program"),
    @XmlEnumValue("Private school students participating")
    PRIVATE_SCHOOL_STUDENTS_PARTICIPATING("Private school students participating"),
    @XmlEnumValue("Local Neglected Program")
    LOCAL_NEGLECTED_PROGRAM("Local Neglected Program"),
    @XmlEnumValue("Was not served")
    WAS_NOT_SERVED("Was not served");
    private final String value;

    TitleIPartAParticipantType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TitleIPartAParticipantType fromValue(String v) {
        for (TitleIPartAParticipantType c: TitleIPartAParticipantType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
