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
 * <p>Java class for CareerPathwayType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CareerPathwayType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Agriculture, Food and Natural Resources"/>
 *     &lt;enumeration value="Architecture and Construction"/>
 *     &lt;enumeration value="Arts, A/V Technology and Communications"/>
 *     &lt;enumeration value="Business, Management and Administration"/>
 *     &lt;enumeration value="Education and Training"/>
 *     &lt;enumeration value="Finance"/>
 *     &lt;enumeration value="Government and Public Administration"/>
 *     &lt;enumeration value="Health Science"/>
 *     &lt;enumeration value="Hospitality and Tourism"/>
 *     &lt;enumeration value="Human Services"/>
 *     &lt;enumeration value="Information Technology"/>
 *     &lt;enumeration value="Law, Public Safety, Corrections and Security"/>
 *     &lt;enumeration value="Manufacturing"/>
 *     &lt;enumeration value="Marketing, Sales and Service"/>
 *     &lt;enumeration value="Science, Technology, Engineering and Mathematics"/>
 *     &lt;enumeration value="Transportation, Distribution and Logistics"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CareerPathwayType")
@XmlEnum
public enum CareerPathwayType {

    @XmlEnumValue("Agriculture, Food and Natural Resources")
    AGRICULTURE_FOOD_AND_NATURAL_RESOURCES("Agriculture, Food and Natural Resources"),
    @XmlEnumValue("Architecture and Construction")
    ARCHITECTURE_AND_CONSTRUCTION("Architecture and Construction"),
    @XmlEnumValue("Arts, A/V Technology and Communications")
    ARTS_A_V_TECHNOLOGY_AND_COMMUNICATIONS("Arts, A/V Technology and Communications"),
    @XmlEnumValue("Business, Management and Administration")
    BUSINESS_MANAGEMENT_AND_ADMINISTRATION("Business, Management and Administration"),
    @XmlEnumValue("Education and Training")
    EDUCATION_AND_TRAINING("Education and Training"),
    @XmlEnumValue("Finance")
    FINANCE("Finance"),
    @XmlEnumValue("Government and Public Administration")
    GOVERNMENT_AND_PUBLIC_ADMINISTRATION("Government and Public Administration"),
    @XmlEnumValue("Health Science")
    HEALTH_SCIENCE("Health Science"),
    @XmlEnumValue("Hospitality and Tourism")
    HOSPITALITY_AND_TOURISM("Hospitality and Tourism"),
    @XmlEnumValue("Human Services")
    HUMAN_SERVICES("Human Services"),
    @XmlEnumValue("Information Technology")
    INFORMATION_TECHNOLOGY("Information Technology"),
    @XmlEnumValue("Law, Public Safety, Corrections and Security")
    LAW_PUBLIC_SAFETY_CORRECTIONS_AND_SECURITY("Law, Public Safety, Corrections and Security"),
    @XmlEnumValue("Manufacturing")
    MANUFACTURING("Manufacturing"),
    @XmlEnumValue("Marketing, Sales and Service")
    MARKETING_SALES_AND_SERVICE("Marketing, Sales and Service"),
    @XmlEnumValue("Science, Technology, Engineering and Mathematics")
    SCIENCE_TECHNOLOGY_ENGINEERING_AND_MATHEMATICS("Science, Technology, Engineering and Mathematics"),
    @XmlEnumValue("Transportation, Distribution and Logistics")
    TRANSPORTATION_DISTRIBUTION_AND_LOGISTICS("Transportation, Distribution and Logistics");
    private final String value;

    CareerPathwayType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CareerPathwayType fromValue(String v) {
        for (CareerPathwayType c: CareerPathwayType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
