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

package org.slc.sli.api.security.context.validator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.stereotype.Component;

@Component
public class StaffToCohortValidator extends AbstractContextValidator {
    
    @Override
    public boolean canValidate(String entityType, boolean isTransitive) {
        return isStaff() && EntityNames.COHORT.equals(entityType);
    }
    
    /**
     * The rule is you can see cohorts at and beneath you in the edorg heirarchy as
     * well as the ones you're directly associated with.
     */
    @Override
    public boolean validate(String entityType, Set<String> ids) {
        boolean match = false;
        Set<String> myCohortIds = new HashSet<String>();
        // Get the one's I'm associated to.
        NeutralQuery basicQuery = new NeutralQuery(new NeutralCriteria(ParameterConstants.STAFF_ID,
                NeutralCriteria.OPERATOR_EQUAL, SecurityUtil.getSLIPrincipal().getEntity().getEntityId()));
        Iterable<Entity> scas = getRepo().findAll(EntityNames.STAFF_COHORT_ASSOCIATION, basicQuery);
        for (Entity sca : scas) {
            Map<String, Object> body = sca.getBody();
            if(!body.containsKey(ParameterConstants.STUDENT_RECORD_ACCESS)) {
                continue;
            } else if (isFieldExpired(body, ParameterConstants.END_DATE)) {
                continue;
            } else if ((Boolean) body.get(ParameterConstants.STUDENT_RECORD_ACCESS) == false) {
                continue;
            } else {
                myCohortIds.add((String) body.get(ParameterConstants.COHORT_ID));
            }
        }
        
        // Get the one's beneath me
        basicQuery = new NeutralQuery(new NeutralCriteria("educationOrgId", NeutralCriteria.CRITERIA_IN,
                getStaffEdorgLineage()));
        Iterable<Entity> cohorts = getRepo().findAll(EntityNames.COHORT, basicQuery);
        for(Entity cohort : cohorts) {
            myCohortIds.add(cohort.getEntityId());
        }

        for(String id : ids) {
            if(!myCohortIds.contains(id)) {
                return false;
            } else {
                match = true;
            }
        }
        return match;
    }
    
}
