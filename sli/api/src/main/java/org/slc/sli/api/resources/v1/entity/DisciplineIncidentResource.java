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

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.ParameterConstants;
import org.slc.sli.api.constants.PathConstants;
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;

/**
 * Represents the definition of a discipline incident.  A discipline incident
 * is defined as a single occurrence of an infraction.
 *
 * Discipline incidents are events classified as warranting discipline action.
 *
 * For detailed information, see the schema for $$DisciplineIncident$$ resources.
 *
 * @author jstokes
 * @author slee
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.DISCIPLINE_INCIDENTS)
@Component
@Scope("request")
public class DisciplineIncidentResource extends DefaultCrudResource {

    @Autowired
    public DisciplineIncidentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.DISCIPLINE_INCIDENTS);
    }

    /**
     * Returns the specified resource representation(s).
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}")
    public Response read(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(disciplineIncidentId, headers, uriInfo);
    }

    /**
     * Deletes the specified resource.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(disciplineIncidentId, headers, uriInfo);
    }

    /**
     * Updates the specified resource using the given resource data.
     */
    @Override
    @PUT
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}")
    public Response update(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
            final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(disciplineIncidentId, newEntityBody, headers, uriInfo);
    }


    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}" + "/" + PathConstants.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS)
    public Response getStudentDisciplineIncidentAssociations(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
                                                             @Context HttpHeaders headers,
                                                             @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS, "disciplineIncidentId", disciplineIncidentId, headers, uriInfo);
    }


    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.DISCIPLINE_INCIDENT_ID + "}" + "/" + PathConstants.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS + "/" + PathConstants.STUDENTS)
    public Response getStudentDisciplineIncidentAssociationStudents(@PathParam(ParameterConstants.DISCIPLINE_INCIDENT_ID) final String disciplineIncidentId,
                                                                    @Context HttpHeaders headers,
                                                                    @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_DISCIPLINE_INCIDENT_ASSOCIATIONS, "disciplineIncidentId", disciplineIncidentId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }
}
