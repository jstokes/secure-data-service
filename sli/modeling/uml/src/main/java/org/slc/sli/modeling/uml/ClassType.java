package org.slc.sli.modeling.uml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * The meta-data for a class.
 */
public final class ClassType extends AbstractModelElementWithLookup implements Type {
    /**
     * The name of the class.
     */
    private final QName name;
    /**
     * Determines whether the class can be instantiated.
     */
    private final boolean isAbstract;
    /**
     * The attributes of this class.
     */
    private final List<Attribute> attributes;
    
    public ClassType(final Identifier id, final QName name, final boolean isAbstract, final List<Attribute> attributes,
            final List<TaggedValue> taggedValues, final LazyLookup lookup) {
        super(id, ReferenceType.CLASS_TYPE, taggedValues, lookup);
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (attributes == null) {
            throw new NullPointerException("attributes");
        }
        this.name = name;
        this.isAbstract = isAbstract;
        this.attributes = Collections.unmodifiableList(new ArrayList<Attribute>(attributes));
    }
    
    @Override
    public QName getName() {
        return name;
    }
    
    public boolean isAbstract() {
        return isAbstract;
    }
    
    public List<Attribute> getAttributes() {
        // We've already made defensive copy in initializer, and have made immutable.
        return attributes;
    }
    
    @Override
    public List<Generalization> getGeneralizationBase() {
        return lookup.getGeneralizationBase(this);
    }
    
    @Override
    public List<Generalization> getGeneralizationDerived() {
        return lookup.getGeneralizationDerived(this);
    }
    
    @Override
    public List<AssociationEnd> getAssociationEnds() {
        return lookup.getAssociationEnds(this);
    }
    
    @Override
    public int hashCode() {
        return getId().hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ClassType) {
            final ClassType that = (ClassType) obj;
            return this.getId().equals(that.getId());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        // sb.append("id: " + id).append(", ");
        sb.append("name: " + name).append(", ");
        sb.append("attributes: " + attributes);
        if (!getTaggedValues().isEmpty()) {
            sb.append(", ");
            sb.append("taggedValues: " + getTaggedValues());
        }
        sb.append("}");
        return sb.toString();
    }
}
