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
 * <p>Java class for LinguisticAccommodationItemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LinguisticAccommodationItemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Bilingual Dictionary"/>
 *     &lt;enumeration value="English Dictionary"/>
 *     &lt;enumeration value="Reading Aloud - Word or Phrase"/>
 *     &lt;enumeration value="Reading Aloud - Entire Test Item"/>
 *     &lt;enumeration value="Oral Translation - Word or Phrase"/>
 *     &lt;enumeration value="Clarification - Word or Phrase"/>
 *     &lt;enumeration value="Linguistic Accommodations allowed but not used"/>
 *     &lt;enumeration value="Linguistic Simplification"/>
 *     &lt;enumeration value="Reading Assistance"/>
 *     &lt;enumeration value="Bilingual Glossary"/>
 *     &lt;enumeration value="English and Spanish tests side-by-side"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LinguisticAccommodationItemType")
@XmlEnum
public enum LinguisticAccommodationItemType {

    @XmlEnumValue("Bilingual Dictionary")
    BILINGUAL_DICTIONARY("Bilingual Dictionary"),
    @XmlEnumValue("English Dictionary")
    ENGLISH_DICTIONARY("English Dictionary"),
    @XmlEnumValue("Reading Aloud - Word or Phrase")
    READING_ALOUD_WORD_OR_PHRASE("Reading Aloud - Word or Phrase"),
    @XmlEnumValue("Reading Aloud - Entire Test Item")
    READING_ALOUD_ENTIRE_TEST_ITEM("Reading Aloud - Entire Test Item"),
    @XmlEnumValue("Oral Translation - Word or Phrase")
    ORAL_TRANSLATION_WORD_OR_PHRASE("Oral Translation - Word or Phrase"),
    @XmlEnumValue("Clarification - Word or Phrase")
    CLARIFICATION_WORD_OR_PHRASE("Clarification - Word or Phrase"),
    @XmlEnumValue("Linguistic Accommodations allowed but not used")
    LINGUISTIC_ACCOMMODATIONS_ALLOWED_BUT_NOT_USED("Linguistic Accommodations allowed but not used"),
    @XmlEnumValue("Linguistic Simplification")
    LINGUISTIC_SIMPLIFICATION("Linguistic Simplification"),
    @XmlEnumValue("Reading Assistance")
    READING_ASSISTANCE("Reading Assistance"),
    @XmlEnumValue("Bilingual Glossary")
    BILINGUAL_GLOSSARY("Bilingual Glossary"),
    @XmlEnumValue("English and Spanish tests side-by-side")
    ENGLISH_AND_SPANISH_TESTS_SIDE_BY_SIDE("English and Spanish tests side-by-side");
    private final String value;

    LinguisticAccommodationItemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LinguisticAccommodationItemType fromValue(String v) {
        for (LinguisticAccommodationItemType c: LinguisticAccommodationItemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
