package org.slc.sli.search.entity;

/**
 * Indexable Unit
 *
 */
public class IndexEntity {
    public enum Action {
        UPDATE("update"),
        INDEX("index");
        
        String type;
        Action(String type) {this.type = type;}
        public String getType() {
            return type;
        }
    }
    private Action action;
    private String index;
    private String type;
    private String id;
    private String body;
    
    public IndexEntity(Action action, String index, String type, String id, String body) {
        this.action = action;
        this.index = index;
        this.type = type;
        this.id = id;
        this.body = body;
    }
    
    public IndexEntity(String index, String type, String id, String body) {
        this(Action.INDEX, index, type, id, body);
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
    
    public String getActionValue() {
        return action.getType();
    }
    
    @Override
    public String toString() {
        return action.getType() + ": {index:" + index + ", type:" + type + ", id:" + id + ", body:" + body + "}";
    }
}
