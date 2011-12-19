package org.slc.sli.domain.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * NOTE: These strongly typed domain classes are deprecated and should no longer be used.
 * Please use see the Entity interface for their replacement.
 * 
 * Whether or not this institution is public or private
 */
@Deprecated
@XmlType(name = "AdministrativeFundingControlType")
@XmlEnum
public enum AdministrativeFundingControlType {
    
    // @XmlEnumValue("Public School")
    PUBLIC_SCHOOL("Public School"),
    // @XmlEnumValue("Private School")
    PRIVATE_SCHOOL("Private School"),
    // @XmlEnumValue("Other")
    OTHER("Other"),
    // @XmlEnumValue("Unknown")
    NULL("Unknown");
    
    private final String value;
    
    AdministrativeFundingControlType(String v) {
        value = v;
    }
    
    public String value() {
        return value;
    }
    
    public static AdministrativeFundingControlType fromValue(String v) {
        for (AdministrativeFundingControlType c : AdministrativeFundingControlType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    
    @Override
    public String toString() {
        return value();
    }
}
