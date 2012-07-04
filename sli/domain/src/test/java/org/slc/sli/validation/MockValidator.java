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


package org.slc.sli.validation;

import java.util.ArrayList;

import org.slc.sli.domain.Entity;

/**
 * Mock validator for the dal
 */
public class MockValidator implements EntityValidator {

    @Override
    public boolean validate(Entity entity) throws EntityValidationException {
        if (entity.getBody().containsKey("bad-entity")) {
            throw new EntityValidationException(entity.getEntityId(), entity.getType(), new ArrayList<ValidationError>());
        }
        return true;
    }

    @Override
    public void setReferenceCheck(String referenceCheck) {
    }
}
