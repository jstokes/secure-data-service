//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The enumerated collection for specifying one or more grade levels.
 * 
 * <p>Java class for GradeLevelsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GradeLevelsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GradeLevel" type="{http://ed-fi.org/0100}GradeLevelType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GradeLevelsType", propOrder = {
    "gradeLevel"
})
public class GradeLevelsType {

    @XmlElement(name = "GradeLevel")
    protected List<GradeLevelType> gradeLevel;

    /**
     * Gets the value of the gradeLevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gradeLevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGradeLevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GradeLevelType }
     * 
     * 
     */
    public List<GradeLevelType> getGradeLevel() {
        if (gradeLevel == null) {
            gradeLevel = new ArrayList<GradeLevelType>();
        }
        return this.gradeLevel;
    }

}
