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


package org.slc.sli.api.security.context.resolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
/**
 * Resolves which cohorts a given staff is allowed to see
 *
 * @author vmcglaughlin
 *
 */
//@Component
public class StaffCohortResolver implements EntityContextResolver {

    @Autowired
    private ResolveCreatorsEntitiesHelper creatorResolverHelper;

    @Autowired
    private AssociativeContextHelper helper;

    @Autowired
    private Repository<Entity> repository;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return (EntityNames.STAFF.equals(fromEntityType) || EntityNames.TEACHER.equals(fromEntityType)) && EntityNames.COHORT.equals(toEntityType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAccessible(Entity principal) {

        NeutralQuery neutralQuery = new NeutralQuery();

        List<String> referenceIds = new ArrayList<String>();
        referenceIds.add(principal.getEntityId());

        List<String> references = helper.findEntitiesContainingReference(EntityNames.STAFF_COHORT_ASSOCIATION, "staffId", referenceIds);
        List<String> createdContext = creatorResolverHelper.getAllowedForCreator(EntityNames.COHORT);

        if (references.isEmpty() && createdContext.isEmpty()) {
            return new ArrayList<String>();
        }

        neutralQuery.addCriteria(new NeutralCriteria("_id", "in", references));

        Iterable<Entity> staffCohortAssociations = repository.findAll(EntityNames.STAFF_COHORT_ASSOCIATION, neutralQuery);
        Set<String> cohortIds = new HashSet<String>();

        for (Entity staffCohortAssociation : staffCohortAssociations) {
            for (String cohortId : (List<String>) staffCohortAssociation.getBody().get("cohortId")) {
                if (cohortId != null) {
                    cohortIds.add(cohortId);
                }
            }
        }

        cohortIds.addAll(createdContext);
        return new ArrayList<String>(cohortIds);
    }
}
