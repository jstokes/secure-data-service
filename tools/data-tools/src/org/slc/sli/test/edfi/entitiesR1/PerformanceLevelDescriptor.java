//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * This descriptor defines various levels or
 * 				thresholds for performance on the assessment.
 * 			
 * 
 * <p>Java class for performanceLevelDescriptor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="performanceLevelDescriptor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="codeValue" type="{http://slc-sli/ed-org/0.1}codeValue"/>
 *         &lt;element name="description" type="{http://slc-sli/ed-org/0.1}description"/>
 *         &lt;element name="performanceBaseConversion" type="{http://slc-sli/ed-org/0.1}performanceBaseType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "performanceLevelDescriptor", propOrder = {
    "codeValueOrDescriptionOrPerformanceBaseConversion"
})
public class PerformanceLevelDescriptor {

    @XmlElementRefs({
        @XmlElementRef(name = "codeValue", namespace = "http://slc-sli/ed-org/0.1", type = JAXBElement.class),
        @XmlElementRef(name = "performanceBaseConversion", namespace = "http://slc-sli/ed-org/0.1", type = JAXBElement.class),
        @XmlElementRef(name = "description", namespace = "http://slc-sli/ed-org/0.1", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> codeValueOrDescriptionOrPerformanceBaseConversion;

    /**
     * Gets the value of the codeValueOrDescriptionOrPerformanceBaseConversion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeValueOrDescriptionOrPerformanceBaseConversion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeValueOrDescriptionOrPerformanceBaseConversion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link PerformanceBaseType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getCodeValueOrDescriptionOrPerformanceBaseConversion() {
        if (codeValueOrDescriptionOrPerformanceBaseConversion == null) {
            codeValueOrDescriptionOrPerformanceBaseConversion = new ArrayList<JAXBElement<?>>();
        }
        return this.codeValueOrDescriptionOrPerformanceBaseConversion;
    }

}
