//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.31 at 09:35:49 AM EDT 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Indication of the nature and difficulty of instruction: remedial, basic, honors, Ap, IB, Dual Credit, CTE. etc. 
 * 
 * <p>Java class for CourseLevelCharacteristicsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CourseLevelCharacteristicsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CourseLevelCharacteristic" type="{http://ed-fi.org/0100}CourseLevelCharacteristicItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourseLevelCharacteristicsType", propOrder = {
    "courseLevelCharacteristic"
})
public class CourseLevelCharacteristicsType {

    @XmlElement(name = "CourseLevelCharacteristic")
    protected List<CourseLevelCharacteristicItemType> courseLevelCharacteristic;

    /**
     * Gets the value of the courseLevelCharacteristic property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseLevelCharacteristic property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseLevelCharacteristic().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseLevelCharacteristicItemType }
     * 
     * 
     */
    public List<CourseLevelCharacteristicItemType> getCourseLevelCharacteristic() {
        if (courseLevelCharacteristic == null) {
            courseLevelCharacteristic = new ArrayList<CourseLevelCharacteristicItemType>();
        }
        return this.courseLevelCharacteristic;
    }

}
