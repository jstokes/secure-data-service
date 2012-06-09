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
 * <p>Java class for RelationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RelationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Adopted daughter"/>
 *     &lt;enumeration value="Adopted son"/>
 *     &lt;enumeration value="Adoptive parents"/>
 *     &lt;enumeration value="Advisor"/>
 *     &lt;enumeration value="Agency representative"/>
 *     &lt;enumeration value="Aunt"/>
 *     &lt;enumeration value="Brother, half"/>
 *     &lt;enumeration value="Brother, natural/adoptive"/>
 *     &lt;enumeration value="Brother, step"/>
 *     &lt;enumeration value="Brother-in-law"/>
 *     &lt;enumeration value="Case Worker, CPS"/>
 *     &lt;enumeration value="Court appointed guardian"/>
 *     &lt;enumeration value="Cousin"/>
 *     &lt;enumeration value="Daughter"/>
 *     &lt;enumeration value="Daughter-in-law"/>
 *     &lt;enumeration value="Dependent"/>
 *     &lt;enumeration value="Doctor"/>
 *     &lt;enumeration value="Employer"/>
 *     &lt;enumeration value="Emergency Contact"/>
 *     &lt;enumeration value="Family member"/>
 *     &lt;enumeration value="Father's significant other"/>
 *     &lt;enumeration value="Father, foster"/>
 *     &lt;enumeration value="Father"/>
 *     &lt;enumeration value="Father, step"/>
 *     &lt;enumeration value="Father-in-law"/>
 *     &lt;enumeration value="Fiance"/>
 *     &lt;enumeration value="Fiancee"/>
 *     &lt;enumeration value="Former husband"/>
 *     &lt;enumeration value="Former wife"/>
 *     &lt;enumeration value="Foster daughter"/>
 *     &lt;enumeration value="Foster parent"/>
 *     &lt;enumeration value="Foster son"/>
 *     &lt;enumeration value="Friend"/>
 *     &lt;enumeration value="Granddaughter"/>
 *     &lt;enumeration value="Grandparent"/>
 *     &lt;enumeration value="Great Grandparent"/>
 *     &lt;enumeration value="Grandson"/>
 *     &lt;enumeration value="Great aunt"/>
 *     &lt;enumeration value="Great uncle"/>
 *     &lt;enumeration value="Guardian"/>
 *     &lt;enumeration value="Husband"/>
 *     &lt;enumeration value="Life partner"/>
 *     &lt;enumeration value="Life partner of parent"/>
 *     &lt;enumeration value="Minister or priest"/>
 *     &lt;enumeration value="Mother's significant other"/>
 *     &lt;enumeration value="Mother, foster"/>
 *     &lt;enumeration value="Mother"/>
 *     &lt;enumeration value="Mother, step"/>
 *     &lt;enumeration value="Mother-in-law"/>
 *     &lt;enumeration value="Nephew"/>
 *     &lt;enumeration value="Niece"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Parent"/>
 *     &lt;enumeration value="Partner"/>
 *     &lt;enumeration value="Partner of parent"/>
 *     &lt;enumeration value="Probation officer"/>
 *     &lt;enumeration value="Sibling"/>
 *     &lt;enumeration value="Sister, half"/>
 *     &lt;enumeration value="Sister, natural/adoptive"/>
 *     &lt;enumeration value="Sister, step"/>
 *     &lt;enumeration value="Sister-in-law"/>
 *     &lt;enumeration value="Son"/>
 *     &lt;enumeration value="Son-in-law"/>
 *     &lt;enumeration value="Spouse"/>
 *     &lt;enumeration value="Stepdaughter"/>
 *     &lt;enumeration value="Stepson"/>
 *     &lt;enumeration value="Stepsibling"/>
 *     &lt;enumeration value="Uncle"/>
 *     &lt;enumeration value="Ward"/>
 *     &lt;enumeration value="Wife"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RelationType")
@XmlEnum
public enum RelationType {

    @XmlEnumValue("Adopted daughter")
    ADOPTED_DAUGHTER("Adopted daughter"),
    @XmlEnumValue("Adopted son")
    ADOPTED_SON("Adopted son"),
    @XmlEnumValue("Adoptive parents")
    ADOPTIVE_PARENTS("Adoptive parents"),
    @XmlEnumValue("Advisor")
    ADVISOR("Advisor"),
    @XmlEnumValue("Agency representative")
    AGENCY_REPRESENTATIVE("Agency representative"),
    @XmlEnumValue("Aunt")
    AUNT("Aunt"),
    @XmlEnumValue("Brother, half")
    BROTHER_HALF("Brother, half"),
    @XmlEnumValue("Brother, natural/adoptive")
    BROTHER_NATURAL_ADOPTIVE("Brother, natural/adoptive"),
    @XmlEnumValue("Brother, step")
    BROTHER_STEP("Brother, step"),
    @XmlEnumValue("Brother-in-law")
    BROTHER_IN_LAW("Brother-in-law"),
    @XmlEnumValue("Case Worker, CPS")
    CASE_WORKER_CPS("Case Worker, CPS"),
    @XmlEnumValue("Court appointed guardian")
    COURT_APPOINTED_GUARDIAN("Court appointed guardian"),
    @XmlEnumValue("Cousin")
    COUSIN("Cousin"),
    @XmlEnumValue("Daughter")
    DAUGHTER("Daughter"),
    @XmlEnumValue("Daughter-in-law")
    DAUGHTER_IN_LAW("Daughter-in-law"),
    @XmlEnumValue("Dependent")
    DEPENDENT("Dependent"),
    @XmlEnumValue("Doctor")
    DOCTOR("Doctor"),
    @XmlEnumValue("Employer")
    EMPLOYER("Employer"),
    @XmlEnumValue("Emergency Contact")
    EMERGENCY_CONTACT("Emergency Contact"),
    @XmlEnumValue("Family member")
    FAMILY_MEMBER("Family member"),
    @XmlEnumValue("Father's significant other")
    FATHER_S_SIGNIFICANT_OTHER("Father's significant other"),
    @XmlEnumValue("Father, foster")
    FATHER_FOSTER("Father, foster"),
    @XmlEnumValue("Father")
    FATHER("Father"),
    @XmlEnumValue("Father, step")
    FATHER_STEP("Father, step"),
    @XmlEnumValue("Father-in-law")
    FATHER_IN_LAW("Father-in-law"),
    @XmlEnumValue("Fiance")
    FIANCE("Fiance"),
    @XmlEnumValue("Fiancee")
    FIANCEE("Fiancee"),
    @XmlEnumValue("Former husband")
    FORMER_HUSBAND("Former husband"),
    @XmlEnumValue("Former wife")
    FORMER_WIFE("Former wife"),
    @XmlEnumValue("Foster daughter")
    FOSTER_DAUGHTER("Foster daughter"),
    @XmlEnumValue("Foster parent")
    FOSTER_PARENT("Foster parent"),
    @XmlEnumValue("Foster son")
    FOSTER_SON("Foster son"),
    @XmlEnumValue("Friend")
    FRIEND("Friend"),
    @XmlEnumValue("Granddaughter")
    GRANDDAUGHTER("Granddaughter"),
    @XmlEnumValue("Grandparent")
    GRANDPARENT("Grandparent"),
    @XmlEnumValue("Great Grandparent")
    GREAT_GRANDPARENT("Great Grandparent"),
    @XmlEnumValue("Grandson")
    GRANDSON("Grandson"),
    @XmlEnumValue("Great aunt")
    GREAT_AUNT("Great aunt"),
    @XmlEnumValue("Great uncle")
    GREAT_UNCLE("Great uncle"),
    @XmlEnumValue("Guardian")
    GUARDIAN("Guardian"),
    @XmlEnumValue("Husband")
    HUSBAND("Husband"),
    @XmlEnumValue("Life partner")
    LIFE_PARTNER("Life partner"),
    @XmlEnumValue("Life partner of parent")
    LIFE_PARTNER_OF_PARENT("Life partner of parent"),
    @XmlEnumValue("Minister or priest")
    MINISTER_OR_PRIEST("Minister or priest"),
    @XmlEnumValue("Mother's significant other")
    MOTHER_S_SIGNIFICANT_OTHER("Mother's significant other"),
    @XmlEnumValue("Mother, foster")
    MOTHER_FOSTER("Mother, foster"),
    @XmlEnumValue("Mother")
    MOTHER("Mother"),
    @XmlEnumValue("Mother, step")
    MOTHER_STEP("Mother, step"),
    @XmlEnumValue("Mother-in-law")
    MOTHER_IN_LAW("Mother-in-law"),
    @XmlEnumValue("Nephew")
    NEPHEW("Nephew"),
    @XmlEnumValue("Niece")
    NIECE("Niece"),
    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Parent")
    PARENT("Parent"),
    @XmlEnumValue("Partner")
    PARTNER("Partner"),
    @XmlEnumValue("Partner of parent")
    PARTNER_OF_PARENT("Partner of parent"),
    @XmlEnumValue("Probation officer")
    PROBATION_OFFICER("Probation officer"),
    @XmlEnumValue("Sibling")
    SIBLING("Sibling"),
    @XmlEnumValue("Sister, half")
    SISTER_HALF("Sister, half"),
    @XmlEnumValue("Sister, natural/adoptive")
    SISTER_NATURAL_ADOPTIVE("Sister, natural/adoptive"),
    @XmlEnumValue("Sister, step")
    SISTER_STEP("Sister, step"),
    @XmlEnumValue("Sister-in-law")
    SISTER_IN_LAW("Sister-in-law"),
    @XmlEnumValue("Son")
    SON("Son"),
    @XmlEnumValue("Son-in-law")
    SON_IN_LAW("Son-in-law"),
    @XmlEnumValue("Spouse")
    SPOUSE("Spouse"),
    @XmlEnumValue("Stepdaughter")
    STEPDAUGHTER("Stepdaughter"),
    @XmlEnumValue("Stepson")
    STEPSON("Stepson"),
    @XmlEnumValue("Stepsibling")
    STEPSIBLING("Stepsibling"),
    @XmlEnumValue("Uncle")
    UNCLE("Uncle"),
    @XmlEnumValue("Ward")
    WARD("Ward"),
    @XmlEnumValue("Wife")
    WIFE("Wife");
    private final String value;

    RelationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelationType fromValue(String v) {
        for (RelationType c: RelationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
