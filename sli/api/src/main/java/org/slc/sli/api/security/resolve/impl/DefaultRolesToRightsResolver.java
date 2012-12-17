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

package org.slc.sli.api.security.resolve.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.api.security.roles.Role;
import org.slc.sli.api.security.roles.RoleRightAccess;
import org.slc.sli.api.security.roles.SecureRoleRightAccessImpl;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 *
 * @author dkornishev
 *
 */
@Component
public class DefaultRolesToRightsResolver implements RolesToRightsResolver {

    @Autowired
    private RoleRightAccess roleRightAccess;

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repo;

    @Override
    public Set<GrantedAuthority> resolveRoles(String tenantId, String realmId, List<String> roleNames) {
        Set<GrantedAuthority> auths = null;
        
        Collection<Role> roles = mapRoles(tenantId, realmId, roleNames);
        
        Entity realm = findRealm(realmId);
        for (Role role : roles) {
            if (auths ==  null) {
                auths = new HashSet<GrantedAuthority>(role.getRights());
            } else {
                if (isAdminRealm(realm) || isDeveloperRealm(realm)) {
                    auths.addAll(role.getRights());
                } else {
                    //When the user is coming from a federated realm this prevents the user from getting
                    //a right they shouldn't.  If the user is in more than one district with different roles
                    //they should get only the rights that are in all their roles. This could prevent the user from
                    //getting a right that they need but more importantly it keeps them from having a right that
                    //they shouldn't have. - DE1679
                    auths.retainAll(role.getRights());
                }
            }
        }
        debug("Final auth list {}", auths);
        if (auths == null) {
            return new HashSet<GrantedAuthority>();
        }
        return auths;
    }
    
    private boolean isDeveloperRealm(final Entity realm) {
        return valueOf(realm, "developer");
    }
    
    private boolean isAdminRealm(final Entity realm) {
        return valueOf(realm, "admin");
    }

    private boolean valueOf(final Entity realm, final String field) {
        
        if (realm != null && realm.getBody() != null) {
            Boolean value = (Boolean) realm.getBody().get(field);
            return value != null ? value : false;
        }
        return false;
    }
    
    private Entity findRealm(final String realmId) {
        
        return SecurityUtil.runWithAllTenants(new SecurityTask<Entity>() {
            @Override
            public Entity execute() {
                return repo.findById("realm", realmId);
            }
        });
        
    }

    @Override
    public Set<Role> mapRoles(String tenantId, String realmId, List<String> roleNames) {
        Set<Role> roles = new HashSet<Role>();
        
        Entity realm = findRealm(realmId);
        if (isAdminRealm(realm)) {
            roles.addAll(roleRightAccess.findAdminRoles(roleNames));
            debug("Mapped admin roles {} to {}.", roleNames, roles);
        } else if (isDeveloperRealm(realm)) {
            roles.addAll(roleRightAccess.findAdminRoles(roleNames));
            boolean canLoginAsDeveloper = false;
            for (Role role : roles) {
                if (role.hasRight(Right.PRODUCTION_LOGIN)) {
                    canLoginAsDeveloper = true;
                    break;
                }
            }
            
            roles = new HashSet<Role>();
            if (canLoginAsDeveloper) {
                roles.addAll(roleRightAccess.findAdminRoles(Arrays.asList(SecureRoleRightAccessImpl.APP_DEVELOPER, SecureRoleRightAccessImpl.PROD_LOGIN_USER)));
                debug("Mapped admin role with production login right, converted {} to {}.", roleNames, roles);
            }
        } else {
            roles.addAll(roleRightAccess.findRoles(tenantId, realmId, roleNames));
            debug("Mapped user roles {} to {}.", roleNames, roles);
        }
        return roles;
    }

}
