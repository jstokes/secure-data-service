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
 * Represents a person that is employed by an LEA or other educational unit engaged in student
 * instruction. These persons are instructional-type staff members. In the data model, a teacher
 * entity is a staff member with additional properties. For more information, see the schema for
 * $$Teacher$$ resources.
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.TEACHERS)
@Component
@Scope("request")
public class TeacherResource extends DefaultCrudResource {

    @Autowired
    public TeacherResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.TEACHERS);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.TEACHER_ID + "}" + "/" + PathConstants.TEACHER_SECTION_ASSOCIATIONS)
    public Response getTeacherSectionAssociations(@PathParam(ParameterConstants.TEACHER_ID) final String teacherId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SECTION_ASSOCIATIONS, "teacherId", teacherId, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.TEACHER_ID + "}" + "/" + PathConstants.TEACHER_SECTION_ASSOCIATIONS + "/"
            + PathConstants.SECTIONS)
    public Response getTeacherSectionAssociationsSections(
            @PathParam(ParameterConstants.TEACHER_ID) final String teacherId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SECTION_ASSOCIATIONS, "teacherId", teacherId, "sectionId",
                ResourceNames.SECTIONS, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.TEACHER_ID + "}" + "/" + PathConstants.TEACHER_SCHOOL_ASSOCIATIONS)
    public Response getTeacherSchoolAssociations(@PathParam(ParameterConstants.TEACHER_ID) final String teacherId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS, "teacherId", teacherId, headers, uriInfo);
    }

    /**
     * Returns the requested collection of resources that are associated with the specified resource.
     */
    @GET
    @Path("{" + ParameterConstants.TEACHER_ID + "}" + "/" + PathConstants.TEACHER_SCHOOL_ASSOCIATIONS + "/"
            + PathConstants.SCHOOLS)
    public Response getTeacherSchoolAssociationsSchools(
            @PathParam(ParameterConstants.TEACHER_ID) final String teacherId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SCHOOL_ASSOCIATIONS, "teacherId", teacherId, "schoolId",
                ResourceNames.SCHOOLS, headers, uriInfo);
    }
}
