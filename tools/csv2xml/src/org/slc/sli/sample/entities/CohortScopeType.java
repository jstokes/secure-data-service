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
 * <p>Java class for CohortScopeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CohortScopeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="District"/>
 *     &lt;enumeration value="School"/>
 *     &lt;enumeration value="Classroom"/>
 *     &lt;enumeration value="Teacher"/>
 *     &lt;enumeration value="Principal"/>
 *     &lt;enumeration value="Counselor"/>
 *     &lt;enumeration value="Statewide"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CohortScopeType")
@XmlEnum
public enum CohortScopeType {

    @XmlEnumValue("District")
    DISTRICT("District"),
    @XmlEnumValue("School")
    SCHOOL("School"),
    @XmlEnumValue("Classroom")
    CLASSROOM("Classroom"),
    @XmlEnumValue("Teacher")
    TEACHER("Teacher"),
    @XmlEnumValue("Principal")
    PRINCIPAL("Principal"),
    @XmlEnumValue("Counselor")
    COUNSELOR("Counselor"),
    @XmlEnumValue("Statewide")
    STATEWIDE("Statewide");
    private final String value;

    CohortScopeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CohortScopeType fromValue(String v) {
        for (CohortScopeType c: CohortScopeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
