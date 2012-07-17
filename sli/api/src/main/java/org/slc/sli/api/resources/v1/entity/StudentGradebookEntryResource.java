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


package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.resources.v1.DefaultCrudResource;

/**
 * Represents a student's grade or competency level for a $$GradebookEntry$$.
 *
 * For detailed information, see the schema for $$StudentGradebookEntry$$ resources.
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_GRADEBOOK_ENTRIES)
@Component
@Scope("request")
public class StudentGradebookEntryResource extends DefaultCrudResource {

    @Autowired
    public StudentGradebookEntryResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_GRADEBOOK_ENTRIES);
    }

}
