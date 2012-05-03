package org.slc.sli.api.security;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import org.slc.sli.domain.Entity;

/**
 * Attribute holder for SLI Principal
 * 
 * @author dkornishev
 * @author shalka
 */
@Component
public class SLIPrincipal implements Principal, Serializable {
    
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String realm;
    private String externalId;
    private String adminRealm;
    private String edOrg;
    private String tenantId;
    private List<String> roles;
    private List<String> sliRoles;
    
    private Entity entity;
    
    public SLIPrincipal() {
        
    }

    public SLIPrincipal(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    public String getId() {
        return id;
    }
    
    /**
     * Gets the fully-qualified LDAP string generated by OpenAM.
     * 
     * @return String containing Realm information.
     */
    public String getRealm() {
        return realm;
    }
    
    public String getExternalId() {
        return externalId;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setRealm(String realm) {
        this.realm = realm;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public List<String> getSliRoles() {
        return sliRoles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public String getAdminRealm() {
        return adminRealm;
    }
    
    public void setAdminRealm(String adminRealm) {
        this.adminRealm = adminRealm;
    }
    
    /**
     * LDAP Attribute "edorg" is set to "X-DistrictY" which is the "stateOrganizationId"
     * for the District in the edorg hierarchy for the data that will be ingested
     * 
     * @return
     */
    public String getEdOrg() {
        return edOrg;
    }
    
    public void setEdOrg(String edOrg) {
        this.edOrg = edOrg;
    }
    
    public void setSliRoles(List<String> sliRoles) {
        this.sliRoles = sliRoles;
    }
    
    @Override
    public String toString() {
        return this.externalId + "@" + this.realm;
    }
    
    /**
     * Sets the tenant id of the sli principal.
     * @param newTenantId new tenant id.
     */
    public void setTenantId(String newTenantId) {
        this.tenantId = newTenantId;
    }
    
    /**
     * Gets the tenant id of the sli principal.
     * @return tenant id.
     */
    public String getTenantId() {
        return tenantId;
    }
    
    public Map<String, Object> toMap() throws Exception {
        Field[] fields = this.getClass().getFields();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Field f : fields) {
            map.put(f.getName(), f.get(this));
        }
        
        return map;
    }
}
