/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.api.security.roles;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A basic implementation of RoleRightAccess
 * 
 * @author rlatta
 */
@Component
public class SecureRoleRightAccessImpl implements RoleRightAccess {
    @Autowired
    private EntityDefinitionStore store;
    
    private EntityService service;
    
    public static final String EDUCATOR = "Educator";
    public static final String LEADER = "Leader";
    public static final String AGGREGATOR = "Aggregate Viewer";
    public static final String IT_ADMINISTRATOR = "IT Administrator";
    
    @PostConstruct
    private void init() {
        EntityDefinition def = store.lookupByResourceName("roles");
        setService(def.getService());
    }
    
    @Override
    public Role findRoleByName(String name) {
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(100);
        
        // TODO find a way to "findAll" from entity service
        Iterable<String> ids = service.listIds(neutralQuery);
        for (String id : ids) {
            EntityBody body;
            body = service.get(id); // FIXME massive hack for roles disappearing sporadically
            
            if (body.get("name").equals(name)) {
                return getRoleWithBodyAndID(id, body);
            }
        }
        return null;
    }
    
    @Override
    public Role findRoleBySpringName(String springName) {
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(100);
        
        Iterable<String> ids = service.listIds(neutralQuery);
        for (String id : ids) {
            EntityBody body = service.get(id);
            Role tempRole = getRoleWithBodyAndID(id, body);
            if (tempRole.getSpringRoleName().equals(springName)) {
                return tempRole;
            }
        }
        return null;
    }
    
    private Role getRoleWithBodyAndID(String id, EntityBody body) {
        return RoleBuilder.makeRole(body).addId(id).build();
    }
    
    @Override
    public List<Role> fetchAllRoles() {
        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(100);
        
        List<Role> roles = new ArrayList<Role>();
        Iterable<String> ids = service.listIds(neutralQuery);
        for (String id : ids) {
            EntityBody body = service.get(id);
            roles.add(getRoleWithBodyAndID(id, body));
        }
        return roles;
    }
    
    @Override
    public boolean addRole(Role role) {
        return service.create(role.getRoleAsEntityBody()) != null;
    }
    
    @Override
    public boolean deleteRole(Role role) {
        if (role.getId().length() > 0) {
            try {
                service.delete(role.getId());
                return true;
            } catch (Exception e) {
                return false;
            }
            
        }
        return false;
    }
    
    @Override
    public boolean updateRole(Role role) {
        if (role.getId().length() > 0) {
            try {
                service.update(role.getId(), role.getRoleAsEntityBody());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    
    // Injection method.
    public void setStore(EntityDefinitionStore store) {
        this.store = store;
    }
    
    // Injection method.
    public void setService(EntityService service) {
        this.service = service;
    }
    
    @Override
    public Role getDefaultRole(String name) {
        return findRoleByName(name);
    }
    
}
