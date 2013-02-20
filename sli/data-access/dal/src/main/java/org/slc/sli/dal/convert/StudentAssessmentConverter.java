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

package org.slc.sli.dal.convert;

import java.util.Arrays;

import org.slc.sli.api.constants.EntityNames;
import org.slc.sli.domain.Entity;
import org.springframework.stereotype.Component;

/**
 * studentAssessment converter that transform studentAssessment superdoc to sli studentAssessment
 * schema
 * 
 * @author Dong Liu dliu@wgen.net
 */
@Component
public class StudentAssessmentConverter extends GenericSuperdocConverter implements SuperdocConverter {
    
    @Override
    public void subdocToBodyField(Entity entity) {
        if (entity != null && entity.getType().equals(EntityNames.STUDENT_ASSESSMENT)) {
        	Entity assessment = retrieveAssessment(entity.getBody().get("assessmentId"));
        
        	//replace assessmentItem reference in studentAssessmentItem with actual assessmentItem entity
            referenceResolve(entity, assessment, "studentAssessmentItem", "assessmentItem");
            subdocsToBody(entity, "studentAssessmentItem", Arrays.asList("studentAssessmentId")); 
            
        	//replace objectiveAssessment reference in studentObjectiveAssessment with actual objectiveAssessment entity
            referenceResolve(entity, assessment, "studentObjectiveAssessment", "objectiveAssessment");
            subdocsToBody(entity, "studentObjectiveAssessment", Arrays.asList("studentAssessmentId")); 
            
            entity.getEmbeddedData().clear();
        }
    }
  
    /*
     * Look inside each "subentityType", use "referenceKey" to lookup the counter part in entity "assessment",
     * then replace the referenceKey with the body of the item from "assessment"
     */
    private void referenceResolve(Entity studentAssessment, Entity assessment, String subEntityType, String referenceKey) {
    	if (assessment == null) {
    		studentAssessment.getEmbeddedData().remove(subEntityType);
    	}
	}

	//TO-DO fill this in
    private Entity retrieveAssessment(Object object) {
    	if (!(object instanceof String)) {
    		return null;
    	}
    	
		return null;
	}

	@Override
    public void bodyFieldToSubdoc(Entity entity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void subdocToBodyField(Iterable<Entity> entities) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void bodyFieldToSubdoc(Iterable<Entity> entities) {
        // TODO Auto-generated method stub
        
    }
    
}
