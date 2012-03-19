package org.slc.sli.entity;

import java.util.Arrays;
import java.util.Map;

/**
 * Main config object for dashboard components
 * @author agrebneva
 *
 */
public class Config {
    /**
     * Type of components
     * @author agrebneva
     *
     */
    public enum Type {
        LAYOUT(true),
        PANEL(true),
        GRID(true),
        TAB(false),
        WIDGET(true),
        FIELD(false);
        
        private boolean hasOwnConfig;
        
        private Type(boolean hasOwnConfig) {
            this.hasOwnConfig = hasOwnConfig;
        }
        
        public boolean hasOwnConfig() {
            return hasOwnConfig;
        }
        
        public boolean isLayoutItem() {
            return this == LAYOUT;
        }
    }
    
    /**
     * Subcomponent of the config
     * @author agrebneva
     *
     */
    public static class Item extends Config {
        protected String description;
        protected String field;
        protected String value;
        protected String width;
        protected String datatype;
        protected String color;
        protected String style;
        protected String formatter;
        protected Map<String, Object> params;
        
        public String getDescription() {
            return description;
        }
        public String getField() {
            return field;
        }
        public String getValue() {
            return value;
        }
        public String getWidth() {
            return width;
        }
        public String getColor() {
            return color;
        }
        public String getStyle() {
            return style;
        }
        public String getFormatter() {
            return formatter;
        }
        public Map<String, Object> getParams() {
            return params;
        }
        public String getDatatype() {
            return datatype;
        }
        @Override
        public String toString() {
            return "ViewItem [width=" + width + ", type=" + datatype + ", color=" + color + ", style=" + style
                    + ", formatter=" + formatter + ", params=" + params + "]";
        }
    }
    
    /**
     * Data component of the config
     * @author agrebneva
     *
     */
    public static class Data {
        protected String entity;
        protected String alias;
        protected Map<String, Object> params;
        public String getEntityRef() {
            return entity;
        }
        public String getAlias() {
            return alias;
        }
        public Map<String, Object> getParams() {
            return params;
        }
        @Override
        public String toString() {
            return "Data [entityRef=" + entity + ", entityAlias=" + alias + ", params=" + params + "]";
        }
    }
    
    /**
     * Data-related condition on the item
     * @author agrebneva
     *
     */
    public static class Condition {
        protected String field;
        protected Object[] value;
        public String getField() {
            return field;
        }
        public Object[] getValue() {
            return value;
        }
        @Override
        public String toString() {
            return "Condition [field=" + field + ", value=" + Arrays.toString(value) + "]";
        }
    }

    protected String id;
    protected String name;
    protected Type type;
    protected Condition condition;
    protected Data data;
    protected Item[] items;
    protected String root;
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getRoot() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Condition getCondition() {
        return condition;
    }

    public Data getData() {
        return data;
    }

    public Item[] getItems() {
        return items;
    }
}