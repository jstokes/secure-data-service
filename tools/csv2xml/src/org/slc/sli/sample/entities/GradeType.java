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
 * <p>Java class for GradeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GradeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Conduct"/>
 *     &lt;enumeration value="Exam"/>
 *     &lt;enumeration value="Final"/>
 *     &lt;enumeration value="Grading Period"/>
 *     &lt;enumeration value="Mid-Term Grade"/>
 *     &lt;enumeration value="Progress Report"/>
 *     &lt;enumeration value="Semester"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GradeType")
@XmlEnum
public enum GradeType {

    @XmlEnumValue("Conduct")
    CONDUCT("Conduct"),
    @XmlEnumValue("Exam")
    EXAM("Exam"),
    @XmlEnumValue("Final")
    FINAL("Final"),
    @XmlEnumValue("Grading Period")
    GRADING_PERIOD("Grading Period"),
    @XmlEnumValue("Mid-Term Grade")
    MID_TERM_GRADE("Mid-Term Grade"),
    @XmlEnumValue("Progress Report")
    PROGRESS_REPORT("Progress Report"),
    @XmlEnumValue("Semester")
    SEMESTER("Semester");
    private final String value;

    GradeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GradeType fromValue(String v) {
        for (GradeType c: GradeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
