package org.slc.sli.api.security;

import java.security.Principal;
import java.util.List;

/**
 * Attribute holder for SLI Principal
 * 
 * @author dkornishev
 *
 */
public class SLIPrincipal implements Principal {
    
    private String id;
    private String name;
    private String state;
    private List<String> theirRoles;

    @Override
    public String getName() {
        return this.name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setTheirRoles(List<String> theirRole) {
        this.theirRoles = theirRole;
    }

    public List<String> getTheirRoles() {
        return theirRoles;
    }
}
