//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.06 at 10:00:50 AM EST 
//


package org.slc.sli.test.edfi.entities;import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This association represents the career and technical education (CTE) program that a student participates in.  The association is an extension of the StudentProgramAssociation particular for CTE programs.
 * 
 * <p>Java class for StudentCTEProgramAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentCTEProgramAssociation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}StudentProgramAssociation">
 *       &lt;sequence>
 *         &lt;element name="CTEProgram" type="{http://ed-fi.org/0100}CTEProgram" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentCTEProgramAssociation", propOrder = {
    "cteProgram"
})
@XmlRootElement public class StudentCTEProgramAssociation
    extends StudentProgramAssociation
{

    @XmlElement(name = "CTEProgram", required = true)
    protected List<CTEProgram> cteProgram;

    /**
     * Gets the value of the cteProgram property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cteProgram property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCTEProgram().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CTEProgram }
     * 
     * 
     */
    public List<CTEProgram> getCTEProgram() {
        if (cteProgram == null) {
            cteProgram = new ArrayList<CTEProgram>();
        }
        return this.cteProgram;
    }

}
