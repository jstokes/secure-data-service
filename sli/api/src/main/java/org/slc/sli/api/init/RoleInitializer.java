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

package org.slc.sli.api.init;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.roles.Role;
import org.slc.sli.api.security.roles.RoleBuilder;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;

/**
 * A simple initializing bean to initialize our Mongo instance with default roles.
 *
 * IMPORTANT: If you add new SLI Administrative roles, make sure you set the admin flag to true.
 * Failure to do so can introduce a large security hole.
 *
 * @author rlatta
 */
@Component
public class RoleInitializer {
    public static final String EDUCATOR = "Educator";
    public static final String AGGREGATE_VIEWER = "Aggregate Viewer";
    public static final String IT_ADMINISTRATOR = "IT Administrator";
    public static final String LEADER = "Leader";
    public static final String ROLES = "roles";

    public static final String LEA_ADMINISTRATOR = "LEA Administrator";
    public static final String SEA_ADMINISTRATOR = "SEA Administrator";
    public static final String APP_DEVELOPER = "Application Developer";
    public static final String SLC_OPERATOR = "SLC Operator";
    public static final String REALM_ADMINISTRATOR = "Realm Administrator";
    public static final String INGESTION_USER = "Ingestion User";
    public static final String SANDBOX_SLC_OPERATOR = "Sandbox SLC Operator";
    public static final String SANDBOX_ADMINISTRATOR = "Sandbox Administrator";

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity>    repository;
    @PostConstruct
    public void init() {
        dropRoles();
        buildRoles();
    }

    private void dropRoles() {
        repository.deleteAll(ROLES);
    }

    public int buildRoles() {
        Iterable<Entity> subset = repository.findAll(ROLES);
        Set<Role> createdRoles = new HashSet<Role>();

        boolean hasEducator = false;
        boolean hasLeader = false;
        boolean hasIT = false;
        boolean hasAggregate = false;

        for (Entity entity : subset) {
            Map<String, Object> body = entity.getBody();
            if (body.get("name").equals(EDUCATOR)) {
                hasEducator = true;
            } else if (body.get("name").equals(AGGREGATE_VIEWER)) {
                hasAggregate = true;
            } else if (body.get("name").equals(IT_ADMINISTRATOR)) {
                hasIT = true;
            } else if (body.get("name").equals(LEADER)) {
                hasLeader = true;
            }
        }
        if (!hasAggregate) {
            createdRoles.add(buildAggregate());
        }
        if (!hasLeader) {
            createdRoles.add(buildLeader());
        }
        if (!hasIT) {
            createdRoles.add(buildIT());
        }
        if (!hasEducator) {
            createdRoles.add(buildEducator());
        }

        for (Role body : createdRoles) {
            repository.create(ROLES, body.getRoleAsEntityBody());
        }
        return createdRoles.size();

    }

    private Role buildAggregate() {
        info("Building Aggregate Viewer default role.");
        return RoleBuilder.makeRole(AGGREGATE_VIEWER)
                .addRights(new Right[] { Right.READ_PUBLIC, Right.AGGREGATE_READ }).build();
    }

    private Role buildEducator() {
        info("Building Educator default role.");
        return RoleBuilder.makeRole(EDUCATOR)
                .addRights(new Right[] { Right.READ_PUBLIC, Right.AGGREGATE_READ, Right.READ_GENERAL }).build();
    }

    private Role buildLeader() {
        info("Building Leader default role.");
        return RoleBuilder
                .makeRole(LEADER)
                .addRights(
                        new Right[] { Right.READ_PUBLIC, Right.AGGREGATE_READ, Right.READ_GENERAL,
                                Right.READ_RESTRICTED }).build();
    }

    private Role buildIT() {
        info("Building IT Administrator default role.");
        return RoleBuilder
                .makeRole(IT_ADMINISTRATOR)
                .addRights(
                        new Right[] { Right.READ_PUBLIC, Right.AGGREGATE_READ, Right.READ_GENERAL,
                                Right.READ_RESTRICTED, Right.WRITE_GENERAL, Right.WRITE_RESTRICTED }).build();
    }
    public void setRepository(Repository repo) {
        repository = repo;
    }

}
