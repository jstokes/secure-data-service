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


package org.slc.sli.api.config;

import org.springframework.stereotype.Component;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.service.Treatment;
import org.slc.sli.domain.Entity;

import java.util.List;

/**
 * Add the entity ID to the response body
 *
 * @author nbrown
 *
 */
@Component
public class IdTreatment implements Treatment {
    private static final String ID_STRING = "id";

    @Override
    public List<EntityBody> toStored(List<EntityBody> exposed, EntityDefinition defn) {
        for (EntityBody body : exposed) {
            body.remove(ID_STRING);
        }
        return exposed;
    }

    @Override
    public EntityBody toExposed(EntityBody stored, EntityDefinition defn, Entity entity) {
        stored.put(ID_STRING, entity.getEntityId());
        return stored;
    }

}
