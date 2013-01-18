/*
 *
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
 *
 */

package org.slc.sli.api.criteriaGenerator;

import org.apache.commons.lang3.StringUtils;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.model.ModelProvider;
import org.slc.sli.modeling.uml.AssociationEnd;
import org.slc.sli.modeling.uml.ClassType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pghosh
 * Date: 1/15/13
 */

@Component
public class EntityIdentifier {

    @Autowired
    private ModelProvider modelProvider;

    @Autowired
    private EntityDefinitionStore entityDefinitionStore;



    // [JS] findXXXX methods shouldn't be void
    public EntityFilterInfo findEntity(String request) {
        EntityFilterInfo entityFilterInfo = new EntityFilterInfo();
        List<String> resources = Arrays.asList(request.split("/"));
        String resource = resources.get(resources.size() - 1);
        EntityDefinition definition = entityDefinitionStore.lookupByResourceName(resource);
        ClassType entityType = modelProvider.getClassType(StringUtils.capitalize(definition.getType()));
        if (populateDateAttributes(entityFilterInfo, entityType) || populateSessionAttribute(entityFilterInfo, entityType)) {
            entityFilterInfo.setEntityName(resource);
        }
        else {

            List<String> associations = modelProvider.getAssociatedDatedEntities(entityType);
            for (String association: associations) {
                // [JS] revisit this
                if (request.toLowerCase().contains(association.toLowerCase())) {
                    populateDateAttributes(entityFilterInfo, modelProvider.getClassType(association));
                    entityFilterInfo.setEntityName(request.substring(request.toLowerCase().indexOf(association.toLowerCase())).split("/")[0]);
                    break;
                }
            }
        }
        //Enable this once all entities have stamped in ComplextTypes.xsd
//        if (entityName.isEmpty() || beginDateAttribute.isEmpty() || endDateAttribute.isEmpty()) {
//            throw new IllegalArgumentException("Cannot Identify execution path for uri " + request);
//        }
        return entityFilterInfo;
    }


    private boolean populateSessionAttribute(EntityFilterInfo entityFilterInfo, ClassType entityType) {
        for (AssociationEnd assoc : modelProvider.getAssociationEnds(entityType.getId())) {
            if (assoc.getName().equals("session")) {
                entityFilterInfo.setSessionAttribute( assoc.getAssociatedAttributeName());
            }
        }
        return !(entityFilterInfo.getSessionAttribute()!=null && entityFilterInfo.getSessionAttribute().isEmpty());
    }


    // [JS] Can one of these fail and the other succeed? If so, we may end up with an object in inconsistent state,
    // if beginDateAttribute && endDateAttribute are required
    private boolean populateDateAttributes(EntityFilterInfo entityFilterInfo, ClassType entityType) {

        entityFilterInfo.setBeginDateAttribute((entityType.getBeginDateAttribute() != null) ? entityType.getBeginDateAttribute().getName() : "");
        entityFilterInfo.setEndDateAttribute((entityType.getEndDateAttribute() != null) ? entityType.getEndDateAttribute().getName() : "");

        return !(entityFilterInfo.getBeginDateAttribute()!= null && entityFilterInfo.getBeginDateAttribute().isEmpty()
                && entityFilterInfo.getEndDateAttribute()!= null && entityFilterInfo.getEndDateAttribute().isEmpty());
    }
}
