package org.slc.sli.modeling.xmi;

/**
 * Symbolic constants used for element names in XMI.
 */
public enum XmiElementName {
    /**
     * 
     */
    ASSOCIATION("Association"),
    /**
     * 
     */
    ASSOCIATION_DOT_CONNECTION("Association.connection"),
    /**
     * 
     */
    ASSOCIATION_END("AssociationEnd"),
    /**
     * 
     */
    ASSOCIATION_END_DOT_MULTIPLICITY("AssociationEnd.multiplicity"),
    /**
     * 
     */
    ASSOCIATION_END_DOT_PARTICIPANT("AssociationEnd.participant"),
    /**
     * 
     */
    ATTRIBUTE("Attribute"),
    /**
     * 
     */
    OPERATION("Operation"),
    /**
     * 
     */
    CLASS("Class"),
    /**
     * 
     */
    CLASSIFIER_DOT_FEATURE("Classifier.feature"),
    /**
     * 
     */
    DATA_TYPE("DataType"),
    /**
     * 
     */
    ENUMERATION("Enumeration"),
    /**
     * 
     */
    ENUMERATION_LITERAL("EnumerationLiteral"),
    /**
     * The enumeration literal group has the localName "Enumeration.literal".
     */
    ENUMERATION_LITERAL_GROUP("Enumeration.literal"),
    /**
     * 
     */
    MODEL_ELEMENT_DOT_TAGGED_VALUE("ModelElement.taggedValue"),
    /**
     * 
     */
    MULTIPLICITY("Multiplicity"),
    /**
     * 
     */
    MULTIPLICITY_DOT_RANGE("Multiplicity.range"),
    /**
     * 
     */
    MULTIPLICITY_RANGE("MultiplicityRange"),
    /**
     * 
     */
    NAMESPACE_DOT_OWNED_ELEMENT("Namespace.ownedElement"),
    /**
     * 
     */
    PACKAGE("Package"),
    /**
     * 
     */
    STRUCTURAL_FEATURE_DOT_MULTIPLICITY("StructuralFeature.multiplicity"),
    /**
     * 
     */
    STRUCTURAL_FEATURE_DOT_TYPE("StructuralFeature.type"),
    /**
     * 
     */
    TAG_DEFINITION("TagDefinition"),
    /**
     * 
     */
    TAG_DEFINITION_DOT_MULTIPLICITY("TagDefinition.multiplicity"),
    /**
     * 
     */
    TAGGED_VALUE("TaggedValue"),
    /**
     * 
     */
    TAGGED_VALUE_DOT_DATA_VALUE("TaggedValue.dataValue"),
    /**
     * 
     */
    TAGGED_VALUE_DOT_TYPE("TaggedValue.type");
    
    private final String localName;
    
    XmiElementName(final String localName) {
        if (localName == null) {
            throw new NullPointerException("localName");
        }
        this.localName = localName;
    }
    
    public String getLocalName() {
        return localName;
    }
    
    public static final XmiElementName getElementName(final String localName) {
        for (final XmiElementName name : values()) {
            if (name.localName.equals(localName)) {
                return name;
            }
        }
        return null;
    }
}
