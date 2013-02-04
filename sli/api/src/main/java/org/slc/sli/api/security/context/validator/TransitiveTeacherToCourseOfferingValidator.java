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

package org.slc.sli.api.security.context.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validate's teacher's context to course offering by looking your edorg lineage.
 * 
 * 
 */
@Component
public class TransitiveTeacherToCourseOfferingValidator extends AbstractContextValidator {
    
    @Autowired
    private PagingRepositoryDelegate<Entity> repo;
    
    @Resource
    private StudentValidatorHelper helpme;

    @Override
    public boolean canValidate(String entityType, boolean isTransitive) {
        return isTeacher() && EntityNames.COURSE_OFFERING.equals(entityType) && isTransitive;
    }
    
    @Override
    public boolean validate(String entityType, Set<String> ids) {
        if (!areParametersValid(EntityNames.COURSE_OFFERING, entityType, ids)) {
            return false;
        }
        
        HashSet<String> toValidate = new HashSet<String>(ids);
        
        Set<String> edorgs = getTeacherEdorgLineage();

        // first try to validate through edorg associations
        NeutralQuery basicQuery = new NeutralQuery(new NeutralCriteria(ParameterConstants.ID,
                NeutralCriteria.CRITERIA_IN, ids));
        Iterable<Entity> cos = repo.findAll(EntityNames.COURSE_OFFERING, basicQuery);
        for (Entity co : cos) {
            if (edorgs.contains((String) co.getBody().get(ParameterConstants.SCHOOL_ID))) {
                toValidate.remove(co.getEntityId());
            }
        }
        
        if (toValidate.size() == 0) {
            return true;
        }

        
        List<String> students = helpme.getStudentIds();
        NeutralQuery nq = new NeutralQuery(new NeutralCriteria(ParameterConstants.COURSE_OFFERING_ID, NeutralCriteria.CRITERIA_IN, ids));
        nq.addCriteria(new NeutralCriteria("studentSectionAssociation.body.studentId", NeutralCriteria.CRITERIA_IN, students,false));
        Iterable<Entity> results = repo.findAll(EntityNames.SECTION, nq);
        
        for(Entity e : results) {
            toValidate.remove(e.getBody().get(ParameterConstants.COURSE_OFFERING_ID));
        }
        
        return toValidate.isEmpty();
    }
}
