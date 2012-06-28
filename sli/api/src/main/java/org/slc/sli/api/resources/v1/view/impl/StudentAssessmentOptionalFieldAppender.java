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


package org.slc.sli.api.resources.v1.view.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slc.sli.api.client.constants.ResourceNames;
import org.slc.sli.api.client.constants.v1.ParameterConstants;
import org.slc.sli.api.client.constants.v1.PathConstants;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.view.OptionalFieldAppender;
import org.slc.sli.api.resources.v1.view.OptionalFieldAppenderHelper;

/**
 * Provides data about students and assessments to construct the custom
 * views returned by the api
 * @author srupasinghe
 *
 */
@Component
public class StudentAssessmentOptionalFieldAppender implements OptionalFieldAppender {

    @Autowired
    private OptionalFieldAppenderHelper optionalFieldAppenderHelper;

    public StudentAssessmentOptionalFieldAppender() {
    }

    @Override
    public List<EntityBody> applyOptionalField(List<EntityBody> entities, String parameters) {

        //get the student Ids
        List<String> studentIds = optionalFieldAppenderHelper.getIdList(entities, "id");
        //get the student assessment associations for the students
        List<EntityBody> studentAssessmentAssociations = optionalFieldAppenderHelper.queryEntities(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS,
                ParameterConstants.STUDENT_ID, studentIds);

        //get the assessment ids from the associations
        List<String> assessmentIds = optionalFieldAppenderHelper.getIdList(studentAssessmentAssociations, ParameterConstants.ASSESSMENT_ID);
        //get a list of assessments
        List<EntityBody> assessments = optionalFieldAppenderHelper.queryEntities(ResourceNames.ASSESSMENTS, "_id", assessmentIds);

        for (EntityBody student : entities) {
            //get the student assessment associations for the given student
            List<EntityBody> studentAssessmentAssociationsForStudent = optionalFieldAppenderHelper.getEntitySubList(studentAssessmentAssociations, ParameterConstants.STUDENT_ID,
                    (String) student.get("id"));

            for (EntityBody studentAssessmentAssociation : studentAssessmentAssociationsForStudent) {
                //get the assessment
                EntityBody assessment = optionalFieldAppenderHelper.getEntityFromList(assessments, "id",
                        (String) studentAssessmentAssociation.get(ParameterConstants.ASSESSMENT_ID));

                studentAssessmentAssociation.put(PathConstants.ASSESSMENTS, assessment);
            }

            //add the body to the student
            student.put(PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS, studentAssessmentAssociationsForStudent);
        }

        return entities;
    }


}
