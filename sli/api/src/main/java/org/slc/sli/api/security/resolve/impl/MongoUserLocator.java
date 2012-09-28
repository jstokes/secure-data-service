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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.resolve.UserLocator;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.MongoEntity;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;

/**
 * Attempts to locate a user in SLI mongo data-store
 *
 * @author dkornishev
 */
@Component
public class MongoUserLocator implements UserLocator {

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repo;

    @Override
    public SLIPrincipal locate(String tenantId, String externalUserId) {
        info("Locating user {}@{}", externalUserId, tenantId);
        SLIPrincipal user = new SLIPrincipal(externalUserId + "@" + tenantId);
        user.setExternalId(externalUserId);
        user.setTenantId(tenantId);

        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(1);
        Map<String, String> paths = new HashMap<String, String>();
        paths.put("metaData.tenantId", tenantId);
        paths.put("body.staffUniqueStateId", externalUserId);

        Iterable<Entity> staff = repo.findAllByPaths(EntityNames.STAFF, paths, neutralQuery);

        if (staff != null && staff.iterator().hasNext()) {
            Entity entity = staff.iterator().next();
            info("Matched user: {}@{} -> {}", new Object[] { externalUserId, tenantId, entity.getEntityId() });
            user.setEntity(entity);
        }

        if (user.getEntity() == null) {
            warn("Failed to locate user {} in the datastore", user.getId());
            Entity entity = new MongoEntity("user", "-133", new HashMap<String, Object>(),
                    new HashMap<String, Object>());
            user.setEntity(entity);
        }

        return user;
    }

    /**
     * Used by auto-wiring to set the entity repository.
     *
     * @param repo
     *            repository to be used by mongo user locator.
     */
    public void setRepo(Repository<Entity> repo) {
        this.repo = repo;
    }
}
