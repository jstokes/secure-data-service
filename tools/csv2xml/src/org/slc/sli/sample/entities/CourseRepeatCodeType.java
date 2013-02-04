/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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
 * <p>Java class for CourseRepeatCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CourseRepeatCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="RepeatCounted"/>
 *     &lt;enumeration value="RepeatNotCounted"/>
 *     &lt;enumeration value="ReplacementCounted"/>
 *     &lt;enumeration value="ReplacedNotCounted"/>
 *     &lt;enumeration value="RepeatOtherInstitution"/>
 *     &lt;enumeration value="NotCountedOther"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CourseRepeatCodeType")
@XmlEnum
public enum CourseRepeatCodeType {

    @XmlEnumValue("RepeatCounted")
    REPEAT_COUNTED("RepeatCounted"),
    @XmlEnumValue("RepeatNotCounted")
    REPEAT_NOT_COUNTED("RepeatNotCounted"),
    @XmlEnumValue("ReplacementCounted")
    REPLACEMENT_COUNTED("ReplacementCounted"),
    @XmlEnumValue("ReplacedNotCounted")
    REPLACED_NOT_COUNTED("ReplacedNotCounted"),
    @XmlEnumValue("RepeatOtherInstitution")
    REPEAT_OTHER_INSTITUTION("RepeatOtherInstitution"),
    @XmlEnumValue("NotCountedOther")
    NOT_COUNTED_OTHER("NotCountedOther");
    private final String value;

    CourseRepeatCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CourseRepeatCodeType fromValue(String v) {
        for (CourseRepeatCodeType c: CourseRepeatCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
