package org.slc.sli.modeling.psm;

public final class PsmCollection {
    private final String name;
    
    public PsmCollection(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
