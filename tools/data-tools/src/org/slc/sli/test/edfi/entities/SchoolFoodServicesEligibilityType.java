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
 * <p>Java class for SchoolFoodServicesEligibilityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SchoolFoodServicesEligibilityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Free"/>
 *     &lt;enumeration value="Full price"/>
 *     &lt;enumeration value="Reduced price"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SchoolFoodServicesEligibilityType")
@XmlEnum
public enum SchoolFoodServicesEligibilityType {

    @XmlEnumValue("Free")
    FREE("Free"),
    @XmlEnumValue("Full price")
    FULL_PRICE("Full price"),
    @XmlEnumValue("Reduced price")
    REDUCED_PRICE("Reduced price"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    SchoolFoodServicesEligibilityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SchoolFoodServicesEligibilityType fromValue(String v) {
        for (SchoolFoodServicesEligibilityType c: SchoolFoodServicesEligibilityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
