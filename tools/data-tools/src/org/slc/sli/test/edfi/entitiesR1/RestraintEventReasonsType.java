//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * The categorization of the circumstances or reason
 *                 for the restraint.
 *             
 * 
 * <p>Java class for restraintEventReasonsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="restraintEventReasonsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="restraintEventReason" type="{http://slc-sli/ed-org/0.1}restraintEventReasonItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restraintEventReasonsType", propOrder = {
    "restraintEventReason"
})
public class RestraintEventReasonsType {

    protected List<RestraintEventReasonItemType> restraintEventReason;

    /**
     * Gets the value of the restraintEventReason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the restraintEventReason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRestraintEventReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RestraintEventReasonItemType }
     * 
     * 
     */
    public List<RestraintEventReasonItemType> getRestraintEventReason() {
        if (restraintEventReason == null) {
            restraintEventReason = new ArrayList<RestraintEventReasonItemType>();
        }
        return this.restraintEventReason;
    }

}
