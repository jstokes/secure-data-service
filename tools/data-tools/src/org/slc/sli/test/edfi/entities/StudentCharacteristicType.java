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
 * <p>Java class for StudentCharacteristicType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StudentCharacteristicType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Displaced Homemaker"/>
 *     &lt;enumeration value="Foster Care"/>
 *     &lt;enumeration value="Homeless"/>
 *     &lt;enumeration value="Immigrant"/>
 *     &lt;enumeration value="Migratory"/>
 *     &lt;enumeration value="Parent in Military"/>
 *     &lt;enumeration value="Pregnant"/>
 *     &lt;enumeration value="Section 504 Handicapped"/>
 *     &lt;enumeration value="Single Parent"/>
 *     &lt;enumeration value="Unaccompanied Youth"/>
 *     &lt;enumeration value="Unschooled Asylee"/>
 *     &lt;enumeration value="Unschooled Refugee"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StudentCharacteristicType")
@XmlEnum
public enum StudentCharacteristicType {

    @XmlEnumValue("Displaced Homemaker")
    DISPLACED_HOMEMAKER("Displaced Homemaker"),
    @XmlEnumValue("Foster Care")
    FOSTER_CARE("Foster Care"),
    @XmlEnumValue("Homeless")
    HOMELESS("Homeless"),
    @XmlEnumValue("Immigrant")
    IMMIGRANT("Immigrant"),
    @XmlEnumValue("Migratory")
    MIGRATORY("Migratory"),
    @XmlEnumValue("Parent in Military")
    PARENT_IN_MILITARY("Parent in Military"),
    @XmlEnumValue("Pregnant")
    PREGNANT("Pregnant"),
    @XmlEnumValue("Section 504 Handicapped")
    SECTION_504_HANDICAPPED("Section 504 Handicapped"),
    @XmlEnumValue("Single Parent")
    SINGLE_PARENT("Single Parent"),
    @XmlEnumValue("Unaccompanied Youth")
    UNACCOMPANIED_YOUTH("Unaccompanied Youth"),
    @XmlEnumValue("Unschooled Asylee")
    UNSCHOOLED_ASYLEE("Unschooled Asylee"),
    @XmlEnumValue("Unschooled Refugee")
    UNSCHOOLED_REFUGEE("Unschooled Refugee");
    private final String value;

    StudentCharacteristicType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StudentCharacteristicType fromValue(String v) {
        for (StudentCharacteristicType c: StudentCharacteristicType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
