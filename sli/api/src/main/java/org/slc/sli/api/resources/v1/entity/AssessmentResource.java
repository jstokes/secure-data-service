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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.resources.v1.DefaultCrudResource;

/**
 * Represents a tool, instrument, process, or exhibition composed of a systematic sampling of
 * behavior for measuring a student's competence, knowledge, skills or behavior. An assessment can
 * be used to measure differences in individuals or groups and changes in performance from one
 * occasion to the next.
 *
 * For more information, see the schema for $$Assessment$$ resources.
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.ASSESSMENTS)
@Component
@Scope("request")
public class AssessmentResource extends DefaultCrudResource {

    @Autowired
    public AssessmentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.ASSESSMENTS);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified
     * resource.
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS)
    public Response getStudentAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super
                .read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified
     * resource.
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.STUDENTS)
    public Response getStudentAssessmentAssociationsStudents(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, "studentId",
                ResourceNames.STUDENTS, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified
     * resource.
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.LEARNING_STANDARDS)
    public Response getLearningStandards(@PathParam(ParameterConstants.ASSESSMENT_ID) final String id) {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Returns the requested collection of resources that are associated with the specified
     * resource.
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.LEARNING_OBJECTIVES)
    public Response getLearningObjectives(@PathParam(ParameterConstants.ASSESSMENT_ID) final String id) {
        return Response.status(Status.NOT_FOUND).build();
    }

}
