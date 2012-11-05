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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaffToStudentCohortAssociationValidator extends AbstractContextValidator {
    
    @Autowired
    private PagingRepositoryDelegate<Entity> repo;
    
    @Autowired
    private StaffToStudentValidator studentValidator;
    
    @Autowired
    private StaffToCohortValidator cohortValidator;

    @Override
    public boolean canValidate(String entityType, boolean isTransitive) {
        return !isTransitive && EntityNames.STUDENT_COHORT_ASSOCIATION.equals(entityType) && isStaff();
    }
    
    @Override
    public boolean validate(String entityType, Set<String> ids) {
        boolean match = false;
        Set<String> cohortIds = new HashSet<String>();
        // See the cohort && see the student
        NeutralQuery basicQuery = new NeutralQuery(new NeutralCriteria(ParameterConstants.ID,
                NeutralCriteria.CRITERIA_IN, ids));
        Iterable<Entity> scas = getRepo().findAll(EntityNames.STUDENT_COHORT_ASSOCIATION, basicQuery);
        for (Entity sca : scas) {
            String studentId = (String) sca.getBody().get(ParameterConstants.STUDENT_ID);
            String cohortId = (String) sca.getBody().get(ParameterConstants.COHORT_ID);
            boolean validByStudent = studentValidator.validate(EntityNames.STUDENT,
                    new HashSet<String>(Arrays.asList(studentId)));
            boolean validByCohort = cohortValidator.validate(EntityNames.COHORT,
                    new HashSet<String>(Arrays.asList(cohortId)));
            if (!(validByStudent && validByCohort) || isFieldExpired(sca.getBody(), ParameterConstants.END_DATE)) {
                return false;
            } else {
                match = true;
            }
        }
        return match;
    }
    
    /**
     * @param studentValidator
     *            the studentValidator to set
     */
    public void setStudentValidator(StaffToStudentValidator studentValidator) {
        this.studentValidator = studentValidator;
    }
    
    /**
     * @param cohortValidator
     *            the cohortValidator to set
     */
    public void setCohortValidator(StaffToCohortValidator cohortValidator) {
        this.cohortValidator = cohortValidator;
    }
    
}
