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
package org.slc.sli.api.search.service;

import java.net.URI;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.generic.representation.Resource;
import org.slc.sli.api.resources.generic.representation.ServiceResponse;
import org.slc.sli.api.resources.generic.service.DefaultResourceService;
import org.slc.sli.api.resources.generic.util.ResourceHelper;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.service.query.ApiQuery;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Search service
 * 
 */

@Component
public class SearchResourceService {

    @Autowired
    DefaultResourceService defaultResourceService;

    @Autowired
    private ResourceHelper resourceHelper;

    @Autowired
    private EdOrgHelper edOrgHelper;

    //keep parameters for ElasticSearch
    //q, size, offset
    //offset is filtered ApiQuery, but accessible by getOffset()
    private static final List<String> whilteListParameters = Arrays.asList(new String[] { "q", "size" });

    public ServiceResponse list(Resource resource, URI queryUri) {

        List<EntityBody> entityBodies = null;
        SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Entity prinipalEntity = principal.getEntity();
        // Temporary until teacher security is in place
        // If teacher, return unauthorized error
        if (isTeacher(prinipalEntity)) {
            throw new AccessDeniedException("Search currently available only for staff.");
        }

        // Call BasicService to query the elastic search repo
        final EntityDefinition definition = resourceHelper.getEntityDefinition(resource);
        ApiQuery apiQuery = new ApiQuery(queryUri);

        // keep only whitelist parameters
        List<NeutralCriteria> criterias = apiQuery.getCriteria();
        if (criterias != null) {
            //use iterator to remove Object on the fly
            Iterator<NeutralCriteria> iteratorCriteria=criterias.iterator();
            while(iteratorCriteria.hasNext()){
                NeutralCriteria criteria = iteratorCriteria.next();
                if (!whilteListParameters.contains(criteria.getKey())) {
                    iteratorCriteria.remove();
                }
            }
        }

        // get allSchools for staff
        List<String> schoolIds = this.edOrgHelper.getUserSchools(prinipalEntity);

        apiQuery.addCriteria(new NeutralCriteria("context.schoolId", NeutralCriteria.CRITERIA_IN, schoolIds));

        entityBodies = (List<EntityBody>) definition.getService().list(apiQuery);

        // return results
        return new ServiceResponse(entityBodies, entityBodies.size());
    }

    private boolean isTeacher(Entity prinipalEntity) {

        String type = prinipalEntity != null ? prinipalEntity.getType() : null;
        return (type != null && type.equals(EntityNames.TEACHER));
    }

}
