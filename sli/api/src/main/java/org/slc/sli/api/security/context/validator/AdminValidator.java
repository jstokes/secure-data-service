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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import org.slc.sli.common.constants.EntityNames;
import org.slc.sli.api.util.SecurityUtil;

/**
 * Validates context for hosted administrator, such as a developer.
 */
@Component
public class AdminValidator implements IContextValidator {

    @Override
    public boolean canValidate(String entityType, boolean through) {
        return SecurityUtil.getSLIPrincipal().isAdminRealmAuthenticated() && entityType.equals(EntityNames.EDUCATION_ORGANIZATION);
    }

    @Override
    public Set<String> validate(String entityType, Set<String> ids) throws IllegalStateException {
        /*
         * Same logic for validation should be used as canValidate. The AdminValidator is
         * being invoked when it shouldn't be, and this has been done to limit where this
         * validator is invoked.
         */
        Set<String> result = new HashSet<String>();
        if (canValidate(entityType, false)) {
            result = ids;
        }
        return result;
    }

    //TODO: implement it
    @Override
    public Set<String> getValid(String entityType, Set<String> ids) {
         if (entityType.equals(EntityNames.EDUCATION_ORGANIZATION)) {
             return ids;
         }
         
         return Collections.emptySet();
    }

    @Override
    public SecurityUtil.UserContext getContext() {
        return SecurityUtil.UserContext.NO_CONTEXT;
    }
}
