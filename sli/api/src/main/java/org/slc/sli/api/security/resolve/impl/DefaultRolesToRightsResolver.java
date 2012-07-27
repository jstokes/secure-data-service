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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.resolve.RolesToRightsResolver;
import org.slc.sli.api.security.roles.Role;
import org.slc.sli.api.security.roles.RoleRightAccess;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;

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
        Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
        List<Role> roles = new ArrayList<Role>();

        if (isAdminRealm(realmId)) {
            roles.addAll(roleRightAccess.findAdminRoles(roleNames));
        } else {
            roles.addAll(roleRightAccess.findRoles(tenantId, realmId, roleNames));
        }

        for (Role role : roles) {
            auths.addAll(role.getRights());
        }

        return auths;
    }

    private boolean isAdminRealm(final String realmId) {

        Entity entity = SecurityUtil.runWithAllTenants(new SecurityTask<Entity>() {

            @Override
            public Entity execute() {
                return repo.findById("realm", realmId);
            }
        });

        Boolean admin = (Boolean) entity.getBody().get("admin");
        return admin != null ? admin : false;
    }

    private Role findRole(final String roleName) {
        return SecurityUtil.sudoRun(new SecurityTask<Role>() {

            @Override
            public Role execute() {
                return roleRightAccess.getDefaultRole(roleName);
            }
        });

    }

    public void setRoleRightAccess(RoleRightAccess roleRightAccess) {
        this.roleRightAccess = roleRightAccess;
    }
}
