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
 * <p>Java class for ResponseIndicatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResponseIndicatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Nonscorable response"/>
 *     &lt;enumeration value="Ineffective response"/>
 *     &lt;enumeration value="Effective response"/>
 *     &lt;enumeration value="Partial response"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResponseIndicatorType")
@XmlEnum
public enum ResponseIndicatorType {

    @XmlEnumValue("Nonscorable response")
    NONSCORABLE_RESPONSE("Nonscorable response"),
    @XmlEnumValue("Ineffective response")
    INEFFECTIVE_RESPONSE("Ineffective response"),
    @XmlEnumValue("Effective response")
    EFFECTIVE_RESPONSE("Effective response"),
    @XmlEnumValue("Partial response")
    PARTIAL_RESPONSE("Partial response");
    private final String value;

    ResponseIndicatorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResponseIndicatorType fromValue(String v) {
        for (ResponseIndicatorType c: ResponseIndicatorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
