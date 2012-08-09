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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.Repository;

/**
 * Resolves which learningStandard a given staff is allowed to see
 *
 * @author dliu
 *
 */
//@Component
public class StaffLearningStandardResolver implements EntityContextResolver {

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repository;

    @Override
    public boolean canResolve(String fromEntityType, String toEntityType) {
        return EntityNames.STAFF.equals(fromEntityType) && EntityNames.LEARNING_STANDARD.equals(toEntityType);
    }

    @Override
    public List<String> findAccessible(Entity principal) {

        // TODO need to figure out business logic to determine which learning Standard is allow for
        // given staff, allow access to all learning standards temporarily
        List<String> ids = new ArrayList<String>();
        Iterable<String> it = this.repository.findAllIds(EntityNames.LEARNING_STANDARD, null);
        for (String id : it) {
            ids.add(id);
        }
        return ids;
    }
}
