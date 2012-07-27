package org.slc.sli.api.selectors.doc;

/**
 * Helper container class
 *
 * @author srupasinghe
 *
 */
public class Constraint {
    private String key;
    private Object value;

    public Constraint() {  }

    public Constraint(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
