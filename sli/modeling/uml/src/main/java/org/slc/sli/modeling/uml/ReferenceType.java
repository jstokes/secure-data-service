package org.slc.sli.modeling.uml;

/**
 * Describes the type of an identifier so that polymorphic lookups can be performed.
 */
public enum ReferenceType {
    /**
     * 
     */
    ASSOCIATION,
    /**
     * 
     */
    ASSOCIATION_END,
    /**
     * 
     */
    ATTRIBUTE,
    /**
     * 
     */
    CLASS_TYPE,
    /**
     * 
     */
    DATA_TYPE,
    /**
     * 
     */
    ENUM_TYPE,
    /**
     * 
     */
    ENUM_LITERAL,
    /**
     * 
     */
    GENERALIZATION,
    /**
     * 
     */
    MULTIPLICITY,
    /**
     * 
     */
    PACKAGE,
    /**
     * 
     */
    RANGE,
    /**
     * 
     */
    TAG_DEFINITION,
    /**
     * 
     */
    TAGGED_VALUE,
    /**
     * The type cannot be determined (usually due to lazy loading).
     */
    UNKNOWN_TYPE;
}
