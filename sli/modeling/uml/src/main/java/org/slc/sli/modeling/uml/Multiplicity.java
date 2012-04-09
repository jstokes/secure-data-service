package org.slc.sli.modeling.uml;

import java.util.List;

/**
 * This multiplicity element is supported distinct from {@link Range} to maintain round-trip
 * fidelity.
 */
public final class Multiplicity extends AbstractModelElement {
    /**
     * The lower and upper bound range.
     */
    private final Range range;
    
    public Multiplicity(final Identifier id, final List<TaggedValue> taggedValues, final Range range,
            final LazyLookup lookup) {
        super(id, taggedValues, lookup);
        if (range == null) {
            throw new NullPointerException("range");
        }
        this.range = range;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        // sb.append("id: " + id).append(", ");
        sb.append("range: " + range);
        if (!getTaggedValues().isEmpty()) {
            sb.append(", ");
            sb.append("taggedValues: " + getTaggedValues());
        }
        sb.append("}");
        return sb.toString();
    }
    
    public Range getRange() {
        return range;
    }
}
