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
 * <p>Java class for WeaponItemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="WeaponItemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Firearm"/>
 *     &lt;enumeration value="Illegal Knife"/>
 *     &lt;enumeration value="Non-Illegal Knife"/>
 *     &lt;enumeration value="Club"/>
 *     &lt;enumeration value="Other Sharp Objects"/>
 *     &lt;enumeration value="Other Object"/>
 *     &lt;enumeration value="Substance Used as Weapon"/>
 *     &lt;enumeration value="Knife"/>
 *     &lt;enumeration value="Unknown"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "WeaponItemType")
@XmlEnum
public enum WeaponItemType {

    @XmlEnumValue("Firearm")
    FIREARM("Firearm"),
    @XmlEnumValue("Illegal Knife")
    ILLEGAL_KNIFE("Illegal Knife"),
    @XmlEnumValue("Non-Illegal Knife")
    NON_ILLEGAL_KNIFE("Non-Illegal Knife"),
    @XmlEnumValue("Club")
    CLUB("Club"),
    @XmlEnumValue("Other Sharp Objects")
    OTHER_SHARP_OBJECTS("Other Sharp Objects"),
    @XmlEnumValue("Other Object")
    OTHER_OBJECT("Other Object"),
    @XmlEnumValue("Substance Used as Weapon")
    SUBSTANCE_USED_AS_WEAPON("Substance Used as Weapon"),
    @XmlEnumValue("Knife")
    KNIFE("Knife"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    WeaponItemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WeaponItemType fromValue(String v) {
        for (WeaponItemType c: WeaponItemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
