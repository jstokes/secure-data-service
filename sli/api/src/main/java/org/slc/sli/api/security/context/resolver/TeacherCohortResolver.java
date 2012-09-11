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

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.security.context.AssociativeContextHelper;
import org.slc.sli.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Resolves teacher's access and security context to cohort records.
 * Uses staffCohortAssociation and filters on the association end date.
 */
@Component
public class TeacherCohortResolver implements EntityContextResolver {

    @Autowired
    private AssociativeContextHelper helper;

    @Autowired
    private StudentSectionAssociationEndDateFilter dateFilter;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return false;
        //return EntityNames.TEACHER.equals(fromEntityType) && EntityNames.COHORT.equals(toEntityType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findAccessible(Entity principal) {

        // teacher -> staffCohortAssociation
        Iterable<Entity> staffCohortAssociations = helper.getReferenceEntities(EntityNames.STAFF_COHORT_ASSOCIATION, ParameterConstants.STAFF_ID, Arrays.asList(principal.getEntityId()));

        // filter on end_date to get list of cohortIds
        List<String> cohortIds = new ArrayList<String>();
        final String currentDate = dateFilter.getCurrentDate();
        for (Entity assoc : staffCohortAssociations) {
            String endDate = (String) assoc.getBody().get(ParameterConstants.END_DATE);
            if (endDate == null || endDate.isEmpty() || dateFilter.isFirstDateBeforeSecondDate(currentDate, endDate)) {
                cohortIds.addAll((List<String>) assoc.getBody().get(ParameterConstants.COHORT_ID));
            }
        }

        return cohortIds;
    }

}

