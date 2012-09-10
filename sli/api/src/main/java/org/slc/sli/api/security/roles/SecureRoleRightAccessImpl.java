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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;

/**
 * A basic implementation of RoleRightAccess
 * 
 * @author rlatta
 */
@Component
public class SecureRoleRightAccessImpl implements RoleRightAccess {

    @Value("${sli.sandbox.enabled}")
    protected boolean isSandboxEnabled;
    
    @Resource(name = "validationRepo")
    private Repository<Entity> repo;
    
    public static final String EDUCATOR = "Educator";
    public static final String LEADER = "Leader";
    public static final String AGGREGATOR = "Aggregate Viewer";
    public static final String IT_ADMINISTRATOR = "IT Administrator";
    
    public static final String LEA_ADMINISTRATOR = "LEA Administrator";
    public static final String SEA_ADMINISTRATOR = "SEA Administrator";
    public static final String APP_DEVELOPER = "Application Developer";
    public static final String SLC_OPERATOR = "SLC Operator";
    public static final String REALM_ADMINISTRATOR = "Realm Administrator";
    public static final String INGESTION_USER = "Ingestion User";
    public static final String SANDBOX_SLC_OPERATOR = "Sandbox SLC Operator";
    public static final String SANDBOX_ADMINISTRATOR = "Sandbox Administrator";
    
    private final Map<String, Role> adminRoles = new HashMap<String, Role>();
    
    @PostConstruct
    public void init() {
        
        adminRoles.put(
                LEA_ADMINISTRATOR,
                RoleBuilder
                        .makeRole(LEA_ADMINISTRATOR)
                        .addRights(
                                new Right[] { Right.ADMIN_ACCESS, Right.EDORG_APP_AUTHZ, Right.READ_PUBLIC,
                                        Right.CRUD_LEA_ADMIN, Right.ADMIN_APPS }).setAdmin(true).build());
        adminRoles.put(
                SEA_ADMINISTRATOR,
                RoleBuilder
                        .makeRole(SEA_ADMINISTRATOR)
                        .addRights(
                                new Right[] { Right.ADMIN_ACCESS, Right.EDORG_DELEGATE, Right.READ_PUBLIC,
                                        Right.CRUD_SEA_ADMIN, Right.CRUD_LEA_ADMIN, Right.ADMIN_APPS }).setAdmin(true).build());
        adminRoles.put(
                SANDBOX_SLC_OPERATOR,
                RoleBuilder
                        .makeRole(SANDBOX_SLC_OPERATOR)
                        .addRights(
                                new Right[] { Right.ADMIN_ACCESS, Right.CRUD_SANDBOX_SLC_OPERATOR,
                                        Right.CRUD_SANDBOX_ADMIN, Right.ADMIN_APPS }).setAdmin(true).build());
        adminRoles
                .put(SANDBOX_ADMINISTRATOR,
                        RoleBuilder.makeRole(SANDBOX_ADMINISTRATOR)
                                .addRights(new Right[] { Right.ADMIN_ACCESS, Right.CRUD_SANDBOX_ADMIN, Right.ADMIN_APPS }).setAdmin(true)
                                .build());
        adminRoles.put(
                REALM_ADMINISTRATOR,
                RoleBuilder
                        .makeRole(REALM_ADMINISTRATOR)
                        .addRights(
                                new Right[] { Right.ADMIN_ACCESS, Right.READ_GENERAL, Right.CRUD_REALM,
                                        Right.READ_PUBLIC, Right.CRUD_ROLE, Right.ADMIN_APPS }).setAdmin(true).build());
        
        Right[] appDevRights = null;
        if (isSandboxEnabled) {
            appDevRights = new Right[] { Right.ADMIN_ACCESS, Right.DEV_APP_CRUD, Right.READ_GENERAL, Right.READ_PUBLIC,
                    Right.CRUD_ROLE, Right.ADMIN_APPS, Right.INGEST_DATA };
        } else {
            appDevRights = new Right[] { Right.ADMIN_ACCESS, Right.DEV_APP_CRUD, Right.READ_GENERAL, Right.READ_PUBLIC, Right.ADMIN_APPS };
        }
        adminRoles.put(APP_DEVELOPER, RoleBuilder.makeRole(APP_DEVELOPER).addRights(appDevRights).setAdmin(true)
                .build());
        
        adminRoles.put(INGESTION_USER,
                RoleBuilder.makeRole(INGESTION_USER).addRights(new Right[] { Right.INGEST_DATA, Right.ADMIN_ACCESS, Right.ADMIN_APPS })
                        .setAdmin(true).build());
        adminRoles.put(
                SLC_OPERATOR,
                RoleBuilder
                        .makeRole(SLC_OPERATOR)
                        .addRights(
                                new Right[] { Right.ADMIN_ACCESS, Right.SLC_APP_APPROVE, Right.READ_GENERAL,
                                        Right.READ_PUBLIC, Right.CRUD_SLC_OPERATOR, Right.CRUD_SEA_ADMIN,
                                        Right.CRUD_LEA_ADMIN, Right.ADMIN_APPS }).setAdmin(true).build());
        
    }
    
    @Override
    public List<Role> findAdminRoles(List<String> roleNames) {
        List<Role> roles = new ArrayList<Role>();
        
        for (String roleName : roleNames) {
            if (adminRoles.containsKey(roleName)) {
                roles.add(adminRoles.get(roleName));
            }
        }
        
        return roles;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Role> findRoles(String tenantId, String realmId, List<String> roleNames) {
        List<Role> roles = new ArrayList<Role>();
        
        if (roleNames != null) {
            final NeutralQuery neutralQuery = new NeutralQuery();
            neutralQuery.addCriteria(new NeutralCriteria("metaData.tenantId", "=", tenantId, false));
            neutralQuery.addCriteria(new NeutralCriteria("realmId", "=", realmId));
            
            Entity doc = SecurityUtil.runWithAllTenants(new SecurityTask<Entity>() {
                
                @Override
                public Entity execute() {
                    return repo.findOne("customRole", neutralQuery);
                }
            });
            
            if (doc != null) {
                Map<String, Object> roleDefs = doc.getBody();
                
                if (roleDefs != null) {
                    List<Map<String, Object>> roleData = (List<Map<String, Object>>) roleDefs.get("roles");
                    
                    for (Map<String, Object> role : roleData) {
                        List<String> names = (List<String>) role.get("names");
                        
                        for (String roleName : names) {
                            if (roleNames.contains(roleName)) {
                                List<String> rights = (List<String>) role.get("rights");
                                roles.add(RoleBuilder.makeRole(roleName).addGrantedAuthorities(rights).build());
                                roles.add(RoleBuilder.makeRole((String) role.get("groupTitle")).build());
                            }
                        }
                    }
                }
            } else {
                debug("No custom roles defined for realm {} in tenant {}.", realmId, tenantId);
            }
        }
        return roles;
    }
    
}
