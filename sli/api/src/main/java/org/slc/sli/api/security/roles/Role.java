package org.slc.sli.api.security.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.domain.enums.Right;

/**
 * A simple class to encapsulate a role
 */
public class Role {
    private String name;
    private Set<Right> rights = new HashSet<Right>();
    private String id = "";
    private Map<String, List<String>> mappings = new HashMap<String, List<String>>();
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Role(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean hasRight(Right right) {
        return rights.contains(right);
    }
    
    public Set<Right> getRights() {
        return rights;
    }
    
    public void addRight(Right right) {
        rights.add(right);
    }
    
    public String getSpringRoleName() {
        return "ROLE_" + getName().toUpperCase().replace(' ', '_');
    }
    
    public EntityBody getRoleAsEntityBody() {
        EntityBody body = new EntityBody();
        body.put("name", getName());
        List<String> rightStrings = new ArrayList<String>();
        for (Right right : rights) {
            rightStrings.add(right.toString());
        }
        body.put("rights", rightStrings);
        body.put("mappings", mappings);
        return body;
    }
    
    public Map<String, List<String>> getRealmRoleMappings() {
        return mappings;
    }
    
    public void setRealmRoleMappings(Map<String, List<String>> mappings) {
        this.mappings = mappings;
    }
}
