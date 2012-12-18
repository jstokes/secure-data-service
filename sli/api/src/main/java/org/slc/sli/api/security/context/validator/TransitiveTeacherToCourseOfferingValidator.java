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
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slc.sli.api.constants.EntityNames;
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
        
        List<String> students = helpme.getStudentIds();
        NeutralQuery nq = new NeutralQuery(new NeutralCriteria("courseOfferingId", "in", ids));
        nq.addCriteria(new NeutralCriteria("studentSectionAssociation.body.studentId", "in", students,false));
        Iterable<Entity> results = repo.findAll(EntityNames.SECTION, nq);
        
        Set<String> fin = new HashSet<String>(ids);
        for(Entity e : results) {
        	fin.remove(e.getBody().get("courseOfferingId"));
        }
        
        return fin.isEmpty();
    }
}
