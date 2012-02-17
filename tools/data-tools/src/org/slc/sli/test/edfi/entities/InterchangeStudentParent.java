//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.02.12 at 04:54:37 PM EST
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="Student" type="{http://ed-fi.org/0100}Student"/>
 *         &lt;element name="Parent" type="{http://ed-fi.org/0100}Parent"/>
 *         &lt;element name="StudentParentAssociation" type="{http://ed-fi.org/0100}StudentParentAssociation"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "studentOrParentOrStudentParentAssociation"
})
@XmlRootElement(name = "InterchangeStudentParent")
public class InterchangeStudentParent {

    @XmlElements({
        @XmlElement(name = "Student", type = Student.class),
        @XmlElement(name = "StudentParentAssociation", type = StudentParentAssociation.class),
        @XmlElement(name = "Parent", type = Parent.class)
    })
    protected List<Object> studentOrParentOrStudentParentAssociation;

    /**
     * Gets the value of the studentOrParentOrStudentParentAssociation property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the studentOrParentOrStudentParentAssociation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudentOrParentOrStudentParentAssociation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Student }
     * {@link StudentParentAssociation }
     * {@link Parent }
     *
     *
     */
    public List<Object> getStudentOrParentOrStudentParentAssociation() {
        if (studentOrParentOrStudentParentAssociation == null) {
            studentOrParentOrStudentParentAssociation = new ArrayList<Object>();
        }
        return this.studentOrParentOrStudentParentAssociation;
    }

}
