//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.30 at 01:48:06 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DiplomaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DiplomaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Regular diploma"/>
 *     &lt;enumeration value="Endorsed/advanced diploma"/>
 *     &lt;enumeration value="Regents diploma"/>
 *     &lt;enumeration value="International Baccalaureate"/>
 *     &lt;enumeration value="Modified diploma"/>
 *     &lt;enumeration value="Other diploma"/>
 *     &lt;enumeration value="Alternative credential"/>
 *     &lt;enumeration value="Certificate of attendance"/>
 *     &lt;enumeration value="Certificate of completion"/>
 *     &lt;enumeration value="High school equivalency credential, other than GED"/>
 *     &lt;enumeration value="General Educational Development (GED) credential"/>
 *     &lt;enumeration value="Post graduate certificate (grade 13)"/>
 *     &lt;enumeration value="Vocational certificate"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DiplomaType")
@XmlEnum
public enum DiplomaType {

    @XmlEnumValue("Regular diploma")
    REGULAR_DIPLOMA("Regular diploma"),
    @XmlEnumValue("Endorsed/advanced diploma")
    ENDORSED_ADVANCED_DIPLOMA("Endorsed/advanced diploma"),
    @XmlEnumValue("Regents diploma")
    REGENTS_DIPLOMA("Regents diploma"),
    @XmlEnumValue("International Baccalaureate")
    INTERNATIONAL_BACCALAUREATE("International Baccalaureate"),
    @XmlEnumValue("Modified diploma")
    MODIFIED_DIPLOMA("Modified diploma"),
    @XmlEnumValue("Other diploma")
    OTHER_DIPLOMA("Other diploma"),
    @XmlEnumValue("Alternative credential")
    ALTERNATIVE_CREDENTIAL("Alternative credential"),
    @XmlEnumValue("Certificate of attendance")
    CERTIFICATE_OF_ATTENDANCE("Certificate of attendance"),
    @XmlEnumValue("Certificate of completion")
    CERTIFICATE_OF_COMPLETION("Certificate of completion"),
    @XmlEnumValue("High school equivalency credential, other than GED")
    HIGH_SCHOOL_EQUIVALENCY_CREDENTIAL_OTHER_THAN_GED("High school equivalency credential, other than GED"),
    @XmlEnumValue("General Educational Development (GED) credential")
    GENERAL_EDUCATIONAL_DEVELOPMENT_GED_CREDENTIAL("General Educational Development (GED) credential"),
    @XmlEnumValue("Post graduate certificate (grade 13)")
    POST_GRADUATE_CERTIFICATE_GRADE_13("Post graduate certificate (grade 13)"),
    @XmlEnumValue("Vocational certificate")
    VOCATIONAL_CERTIFICATE("Vocational certificate"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    DiplomaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DiplomaType fromValue(String v) {
        for (DiplomaType c: DiplomaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
