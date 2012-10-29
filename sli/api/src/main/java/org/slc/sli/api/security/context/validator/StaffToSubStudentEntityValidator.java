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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;

/**
 * Validates a staff accessing a set of entities that are directly associated to a student.
 * Currently supported entities are:
 * attendance,
 * course transcript,
 * discipline action,
 * student academic record,
 * student assessment association,
 * student discipline incident association,
 * student grade book entry,
 * student section association,
 * student school association.
 *
 * @author shalka
 */
@Component
public class StaffToSubStudentEntityValidator extends AbstractContextValidator {

    @Autowired
    private PagingRepositoryDelegate<Entity> repo;

    @Autowired
    private StaffToStudentValidator validator;

    /**
     * Determines if the entity type is a sub-entity of student.
     */
    @Override
    public boolean canValidate(String entityType, boolean through) {
        return SecurityUtil.getSLIPrincipal().getEntity().getType().equals(EntityNames.STAFF)
                && isSubEntityOfStudent(entityType);
    }

    /**
     * Determines if the staff can see the set of entities specified by 'ids'.
     */
    @Override
    public boolean validate(String entityType, Set<String> ids) {
        Set<String> students = new HashSet<String>();
        NeutralQuery query = new NeutralQuery(new NeutralCriteria(ParameterConstants.ID,
                NeutralCriteria.OPERATOR_EQUAL, new ArrayList<String>(ids)));
        Iterable<Entity> entities = repo.findAll(entityType, query);
        if (entities != null) {
            for (Entity entity : entities) {
                Map<String, Object> body = entity.getBody();
                if (entityType.equals(EntityNames.STUDENT_SCHOOL_ASSOCIATION) && body.containsKey("exitWithdrawDate")) {
                    if (isLhsBeforeRhs(getNowMinusGracePeriod(), getDateTime((String) body.get("exitWithdrawDate")))) {
                        students.add((String) body.get(ParameterConstants.STUDENT_ID));
                    }
                } else if (entityType.equals(EntityNames.STUDENT_SECTION_ASSOCIATION) && body.containsKey("endDate")) {
                    if (isLhsBeforeRhs(getNowMinusGracePeriod(), getDateTime((String) body.get("endDate")))) {
                        students.add((String) body.get(ParameterConstants.STUDENT_ID));
                    }
                } else {
                    Object studentInfo = body.get(ParameterConstants.STUDENT_ID);
                    if (studentInfo instanceof BasicDBList) {
                        BasicDBList studentList = (BasicDBList) studentInfo;
                        for (int i = 0; i < studentList.size(); i++) {
                            students.add((String) studentList.get(i));
                        }
                    } else if (studentInfo instanceof String) {
                        students.add((String) studentInfo);
                    }
                }
            }
        }

        if (students.isEmpty()) {
            return false;
        }

        return validator.validate(EntityNames.STUDENT, students);
    }

    /**
     * Sets the paging repository delegate (for unit testing).
     *
     * @param repo
     *            Paging Delete Repository to use.
     */
    public void setRepo(PagingRepositoryDelegate<Entity> repo) {
        this.repo = repo;
    }

    /**
     * Sets the staff to student validator (for unit testing).
     *
     * @param staffToStudentValidator
     *            Staff To Student Validator.
     */
    public void setStaffToStudentValidator(StaffToStudentValidator staffToStudentValidator) {
        this.validator = staffToStudentValidator;
    }
}
