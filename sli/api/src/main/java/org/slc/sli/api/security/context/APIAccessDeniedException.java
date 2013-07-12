/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
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

package org.slc.sli.api.security.context;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.domain.Entity;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

/**
 * Represents the error detected by the API when access to an entity is denied.
 * 
 * @author bsuzuki
 *
 */
public class APIAccessDeniedException extends AccessDeniedException {

    private Set<String> targetEdOrgIds;

    private String entityType;                // type of the entities to which access was attempted
    private Set<Entity> entities;             // entities to which access was attempted
    private Set<String> entityIds;            // ids of the entities to which access was attempted

    private static final long serialVersionUID = 23498349L;

    public APIAccessDeniedException(String msg) {
        super(msg);
    }

    /**
     * inherit targetEdOrg info from the passed exception and set cause
     * - used to catch and rethrow with cause
     * @param msg   explanation of the exception
     * @param e     the exception that caused this one
     */
    public APIAccessDeniedException(String msg, APIAccessDeniedException e) {
        super(msg);
        this.targetEdOrgIds = e.getTargetEdOrgIds();
        this.entityType = e.getEntityType();
        this.entities = e.getEntities();
        this.entityIds = e.getEntityIds();
        this.initCause(e);
    }

    /**
     * @param msg           explanation of the exception
     * @param targetEdOrgId   database id of the education organization to which access was denied
     */
    public APIAccessDeniedException(String msg, String targetEdOrgId) {
        super(msg);
        this.targetEdOrgIds = new HashSet<String>();
        this.targetEdOrgIds.add(targetEdOrgId);
    }

    /**
     * @param msg           explanation of the exception
     * @param targetEdOrgIds  database ids of the education organizations to which access was denied
     */
    public APIAccessDeniedException(String msg, Collection<String> targetEdOrgIds) {
        super(msg);
        this.targetEdOrgIds = new HashSet<String>(targetEdOrgIds);
    }

    /**
     * Creates an APIAccessDeniedException with targetEdOrgs set based on the provided entity
     * @param msg       explanation of the exception
     * @param entity    entity to which access was denied
     */
    public APIAccessDeniedException(String msg, Entity entity) {
        super(msg);
        if (entity != null) {
            this.entityType = entity.getType();
            this.entities = new HashSet<Entity>();
            this.entities.add(entity);
        }
    }

    /**
     * Creates an APIAccessDeniedException with targetEdOrgs set based on the entity
     *  identified by entityType and entityId
     * @param msg           explanation of the exception
     * @param entityType    type of the entity to which access was denied
     * @param entityId      id of the entity to which access was denied
     */
    public APIAccessDeniedException(String msg, String entityType, String entityId) {
        super(msg);

        this.entityType = entityType;
        this.entityIds = new HashSet<String>();
        this.entityIds.add(entityId);
    }

    /**
     * Creates an APIAccessDeniedException with targetEdOrgs set based on the entities
     *  identified by entityType and entityIds
     * @param msg           explanation of the exception
     * @param entityType    type of the entity to which access was denied
     * @param entityIds      id of the entity to which access was denied
     */
    public APIAccessDeniedException(String msg, String entityType, Collection<String> entityIds) {
        super(msg);

        this.entityType = entityType;
        this.entityIds = new HashSet<String>(entityIds);
    }

    public void setTargetEdOrgIds(Set<String> targetEdOrgIds) {
        this.targetEdOrgIds = targetEdOrgIds;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }

    public void setEntityIds(Set<String> entityIds) {
        this.entityIds = entityIds;
    }

    public Set<String> getTargetEdOrgIds() {
        return targetEdOrgIds;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public String getEntityType() {
        return entityType;
    }

    public Set<String> getEntityIds() {
        return entityIds;
    }

}
