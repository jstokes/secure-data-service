//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 02:49:01 PM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for assessmentCategoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="assessmentCategoryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Achievement test"/>
 *     &lt;enumeration value="Advanced Placement"/>
 *     &lt;enumeration value="International Baccalaureate"/>
 *     &lt;enumeration value="Aptitude test"/>
 *     &lt;enumeration value="Attitudinal test"/>
 *     &lt;enumeration value="Benchmark test"/>
 *     &lt;enumeration value="Class test"/>
 *     &lt;enumeration value="class quiz"/>
 *     &lt;enumeration value="College entrance exam"/>
 *     &lt;enumeration value="Cognitive and perceptual skills test"/>
 *     &lt;enumeration value="Developmental observation"/>
 *     &lt;enumeration value="English proficiency screening test"/>
 *     &lt;enumeration value="Foreign language proficiency test"/>
 *     &lt;enumeration value="Interest inventory"/>
 *     &lt;enumeration value="Manual dexterity test"/>
 *     &lt;enumeration value="Mental ability (intelligence) test"/>
 *     &lt;enumeration value="Performance assessment"/>
 *     &lt;enumeration value="Personality test"/>
 *     &lt;enumeration value="Portfolio assessment"/>
 *     &lt;enumeration value="Psychological test"/>
 *     &lt;enumeration value="Psychomotor test"/>
 *     &lt;enumeration value="Reading readiness test"/>
 *     &lt;enumeration value="State summative assessment 3-8 general"/>
 *     &lt;enumeration value="State high school subject assessment"/>
 *     &lt;enumeration value="State high school course assessment"/>
 *     &lt;enumeration value="State alternative assessment/grade-level standards"/>
 *     &lt;enumeration value="State alternative assessment/modified standards"/>
 *     &lt;enumeration value="State alternate assessment/ELL"/>
 *     &lt;enumeration value="State English proficiency test"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "assessmentCategoryType")
@XmlEnum
public enum AssessmentCategoryType {

    @XmlEnumValue("Achievement test")
    ACHIEVEMENT_TEST("Achievement test"),
    @XmlEnumValue("Advanced Placement")
    ADVANCED_PLACEMENT("Advanced Placement"),
    @XmlEnumValue("International Baccalaureate")
    INTERNATIONAL_BACCALAUREATE("International Baccalaureate"),
    @XmlEnumValue("Aptitude test")
    APTITUDE_TEST("Aptitude test"),
    @XmlEnumValue("Attitudinal test")
    ATTITUDINAL_TEST("Attitudinal test"),
    @XmlEnumValue("Benchmark test")
    BENCHMARK_TEST("Benchmark test"),
    @XmlEnumValue("Class test")
    CLASS_TEST("Class test"),
    @XmlEnumValue("class quiz")
    CLASS_QUIZ("class quiz"),
    @XmlEnumValue("College entrance exam")
    COLLEGE_ENTRANCE_EXAM("College entrance exam"),
    @XmlEnumValue("Cognitive and perceptual skills test")
    COGNITIVE_AND_PERCEPTUAL_SKILLS_TEST("Cognitive and perceptual skills test"),
    @XmlEnumValue("Developmental observation")
    DEVELOPMENTAL_OBSERVATION("Developmental observation"),
    @XmlEnumValue("English proficiency screening test")
    ENGLISH_PROFICIENCY_SCREENING_TEST("English proficiency screening test"),
    @XmlEnumValue("Foreign language proficiency test")
    FOREIGN_LANGUAGE_PROFICIENCY_TEST("Foreign language proficiency test"),
    @XmlEnumValue("Interest inventory")
    INTEREST_INVENTORY("Interest inventory"),
    @XmlEnumValue("Manual dexterity test")
    MANUAL_DEXTERITY_TEST("Manual dexterity test"),
    @XmlEnumValue("Mental ability (intelligence) test")
    MENTAL_ABILITY_INTELLIGENCE_TEST("Mental ability (intelligence) test"),
    @XmlEnumValue("Performance assessment")
    PERFORMANCE_ASSESSMENT("Performance assessment"),
    @XmlEnumValue("Personality test")
    PERSONALITY_TEST("Personality test"),
    @XmlEnumValue("Portfolio assessment")
    PORTFOLIO_ASSESSMENT("Portfolio assessment"),
    @XmlEnumValue("Psychological test")
    PSYCHOLOGICAL_TEST("Psychological test"),
    @XmlEnumValue("Psychomotor test")
    PSYCHOMOTOR_TEST("Psychomotor test"),
    @XmlEnumValue("Reading readiness test")
    READING_READINESS_TEST("Reading readiness test"),
    @XmlEnumValue("State summative assessment 3-8 general")
    STATE_SUMMATIVE_ASSESSMENT_3_8_GENERAL("State summative assessment 3-8 general"),
    @XmlEnumValue("State high school subject assessment")
    STATE_HIGH_SCHOOL_SUBJECT_ASSESSMENT("State high school subject assessment"),
    @XmlEnumValue("State high school course assessment")
    STATE_HIGH_SCHOOL_COURSE_ASSESSMENT("State high school course assessment"),
    @XmlEnumValue("State alternative assessment/grade-level standards")
    STATE_ALTERNATIVE_ASSESSMENT_GRADE_LEVEL_STANDARDS("State alternative assessment/grade-level standards"),
    @XmlEnumValue("State alternative assessment/modified standards")
    STATE_ALTERNATIVE_ASSESSMENT_MODIFIED_STANDARDS("State alternative assessment/modified standards"),
    @XmlEnumValue("State alternate assessment/ELL")
    STATE_ALTERNATE_ASSESSMENT_ELL("State alternate assessment/ELL"),
    @XmlEnumValue("State English proficiency test")
    STATE_ENGLISH_PROFICIENCY_TEST("State English proficiency test"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    AssessmentCategoryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AssessmentCategoryType fromValue(String v) {
        for (AssessmentCategoryType c: AssessmentCategoryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
