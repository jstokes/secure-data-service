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
import org.slc.sli.modeling.uml.Attribute;
import org.slc.sli.modeling.uml.ClassType;
import org.slc.sli.modeling.uml.TaggedValue;
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
    ModelProvider modelProvider;

    @Autowired
    private EntityDefinitionStore entityDefinitionStore;

    private String entityName;
    private String beginDateAttribute;

    public String getSessionAttribute() {
        return sessionAttribute;
    }

    private String sessionAttribute;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getBeginDateAttribute() {
        return beginDateAttribute;
    }

    public void setBeginDateAttribute(String beginDateAttribute) {
        this.beginDateAttribute = beginDateAttribute;
    }

    public String getEndDateAttribute() {
        return endDateAttribute;
    }

    public void setEndDateAttribute(String endDateAttribute) {
        this.endDateAttribute = endDateAttribute;
    }

    private String endDateAttribute;

    public void findEntity(String request) {
        List<String> resources = Arrays.asList(request.split("/"));
        String resource = resources.get(resources.size() - 1);
        EntityDefinition definition = entityDefinitionStore.lookupByResourceName(resource);
        ClassType entityType = modelProvider.getClassType(StringUtils.capitalize(definition.getType()));
        if (populateDateAttributes(entityType)) {
            entityName = resource;
        } else if (populateSessionAttribute(entityType)) {

        }
        else {
            List<String> associations = modelProvider.getAssociatedDatedEntities(entityType);
            for(String association: associations) {
               if(request.toLowerCase().contains(association.toLowerCase())) {
                   populateDateAttributes(modelProvider.getClassType(association));
                   entityName = request.substring(request.toLowerCase().indexOf(association.toLowerCase())).split("/")[0];
               }
            }
        }
        //Enable this once all entities have stamped in ComplextTypes.xsd
//        if (entityName.isEmpty() || beginDateAttribute.isEmpty() || endDateAttribute.isEmpty()) {
//            throw new IllegalArgumentException("Cannot Identify execution path for uri " + request);
//        }
    }

    private boolean populateSessionAttribute(ClassType entityType) {
       for( AssociationEnd assoc : modelProvider.getAssociationEnds(entityType.getId())) {
           if(assoc.getName().equals("session")) {
               sessionAttribute = assoc.getAssociatedAttributeName();
           }
       }
        return !sessionAttribute.isEmpty();
    }


    private boolean populateDateAttributes(ClassType entityType) {

        beginDateAttribute = (entityType.getBeginDateAttribute()!=null)?entityType.getBeginDateAttribute().getName():"";
        endDateAttribute = (entityType.getEndDateAttribute()!=null)?entityType.getEndDateAttribute().getName():"";

        return !(beginDateAttribute.isEmpty() && endDateAttribute.isEmpty());
    }
}
