package org.slc.sli.api.client.security;

import java.security.Principal;


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
    
    public SLIPrincipal() {
        
    }
    
    public SLIPrincipal(String id, String name, String state) {
        this.id = id;
        this.name = name;
        this.state = state;
    }
    
    @Override
    public String getName() {
        return name;
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
    
}
