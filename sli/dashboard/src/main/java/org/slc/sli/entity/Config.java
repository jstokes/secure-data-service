package org.slc.sli.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Main config object for dashboard components
 * 
 * @author agrebneva
 * 
 */
public class Config {
    /**
     * Type of components
     * 
     * @author agrebneva
     * 
     */
    public enum Type {
        LAYOUT(true), PANEL(true), GRID(true), TAB(false), WIDGET(true), FIELD(false);
        
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
     * 
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
        protected String sorter;
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
        
        public String getSorter() {
            return sorter;
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
     * 
     * @author agrebneva
     * 
     */
    public static class Data {
        protected String entity;
        protected String alias;
        protected Map<String, Object> params;
        
        public Data() {
        }
        
        public Data(String entity, String alias, Map<String, Object> params) {
            this.entity = entity;
            this.alias = alias;
            this.params = params;
        }
        
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
     * 
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
    protected Type type = Type.FIELD;
    protected Condition condition;
    protected Data data;
    protected Item[] items;
    protected String root;
    
    public Config(String id, String name, Type type, Condition condition, Data data, Item[] items, String root) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.condition = condition;
        this.data = data;
        this.items = items;
        this.root = root;
    }
    
    public Config() {
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getRoot() {
        return root;
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
    
    public Config cloneWithItems(Item[] items) {
        return new Config(id, name, type, condition, data, items, root);
    }
    
    /**
     * use this method if Config object is required to have a duplicate copy. Config object should
     * be immutable in order to avoid confusions.
     * It creates a cloned (deep copy) Config object except Config.Data.entity and Config.Data.param
     * (these values are overwritten by Config object an input param)
     * 
     * @param customConfig
     *            Config.Data.entity and Config.Data.param are used to overwrite to a cloned Config
     *            object
     * @return cloned Config obejct merged with customConfig
     */
    public Config merge(Config customConfig) {
        Config config = new Config(this.id, this.name, this.type, this.condition, new Data(this.data.entity,
                this.data.alias, customConfig.data.params == null ? null
                        : Collections.unmodifiableMap(new HashMap<String, Object>(customConfig.data.params))),
                customConfig.items, this.root);
        return config;
    }
}