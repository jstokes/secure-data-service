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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.ParameterConstants;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 * SectionResource
 *
 * A Resource class for accessing a Section entity and other entities associated with Section.
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.SECTIONS)
@Component
@Scope("request")
public class SectionResource extends DefaultCrudEndpoint {

    @Autowired
    public SectionResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.SECTIONS);
    }

    /**
     * readAll
     *
     * Returns all $$sections$$ entities for which the logged in User has permission and context.
     *
     * @param offset
     *            starting position in results to return to user
     * @param limit
     *            maximum number of results to return to user (starting from offset)
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @Override
    @GET
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * create
     *
     * Create a new $$sections$$ entity.
     *
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *              URI information including path and query parameters
     * @return result of CRUD operation
     * @response.param {@name Location} {@style header} {@type
     *                 {http://www.w3.org/2001/XMLSchema}anyURI} {@doc The URI where the created
     *                 item is accessible.}
     */
    @Override
    @POST
    public Response create(final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * read
     *
     * Get a single $$sections$$ entity
     *
     * @param sectionId
     *            The Id of the $$sections$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single section entity
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}")
    public Response read(@PathParam(ParameterConstants.SECTION_ID) final String sectionId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(sectionId, headers, uriInfo);
    }

    /**
     * delete
     *
     * Delete a $$sections$$ entity
     *
     * @param sectionId
     *            The Id of the $$sections$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.SECTION_ID + "}")
    public Response delete(@PathParam(ParameterConstants.SECTION_ID) final String sectionId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(sectionId, headers, uriInfo);
    }

    /**
     * update
     *
     * Update an existing $$sections$$ entity.
     *
     * @param sectionId
     *            The id of the $$sections$$.
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @PUT
    @Path("{" + ParameterConstants.SECTION_ID + "}")
    public Response update(@PathParam(ParameterConstants.SECTION_ID) final String sectionId,
            final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(sectionId, newEntityBody, headers, uriInfo);
    }

    /**
     * getStudentSectionAssociations
     *
     * Returns each $$studentSectionAssociations$$ that
     * references the given $$sections$$
     *
     * @param sectionId
     *            The id of the $$students$$.
     * @param offset
     *            Index of the first result to return
     * @param limit
     *            Maximum number of results to return.
     * @param expandDepth
     *            Number of hops (associations) for which to expand entities.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}" + "/" + PathConstants.STUDENT_SECTION_ASSOCIATIONS)
    public Response getStudentSectionAssociations(@PathParam(ParameterConstants.SECTION_ID) final String sectionId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SECTION_ASSOCIATIONS, "sectionId", sectionId, headers,
                uriInfo);
    }

    /**
     * getStudentSectionAssociationStudents
     *
     * Returns each $$students$$ associated to the given section through
     * a $$studentSectionAssociations$$
     *
     * @param sectionId
     *            The id of the $$sections$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}" + "/" + PathConstants.STUDENT_SECTION_ASSOCIATIONS + "/"
            + PathConstants.STUDENTS)
    public Response getStudentSectionAssociationStudents(
            @PathParam(ParameterConstants.SECTION_ID) final String sectionId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        
        // add optional field to the query params. always include the student's grade level.
        uriInfo.getQueryParameters(true).add(ParameterConstants.OPTIONAL_FIELDS, "gradeLevel");

        return super.read(ResourceNames.STUDENT_SECTION_ASSOCIATIONS, "sectionId", sectionId, "studentId",
                ResourceNames.STUDENTS, headers, uriInfo);
    }

    /**
     * getTeacherSectionAssociations
     *
     * Returns each $$teacherSectionAssociations$$ that
     * references the given $$sections$$
     *
     * @param sectionId
     *            The id of the $$students$$.
     * @param offset
     *            Index of the first result to return
     * @param limit
     *            Maximum number of results to return.
     * @param expandDepth
     *            Number of hops (associations) for which to expand entities.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}" + "/" + PathConstants.TEACHER_SECTION_ASSOCIATIONS)
    public Response getTeacherSectionAssociations(@PathParam(ParameterConstants.SECTION_ID) final String sectionId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SECTION_ASSOCIATIONS, "sectionId", sectionId, headers,
                uriInfo);
    }

    /**
     * getTeacherSectionAssociationTeachers
     *
     * Returns each $$teachers$$ associated to the given section through
     * a $$teacherSectionAssociations$$
     *
     * @param sectionId
     *            The id of the $$sections$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}" + "/" + PathConstants.TEACHER_SECTION_ASSOCIATIONS + "/"
            + PathConstants.TEACHERS)
    public Response getTeacherSectionAssociationTeachers(
            @PathParam(ParameterConstants.SECTION_ID) final String sectionId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.TEACHER_SECTION_ASSOCIATIONS, "sectionId", sectionId, "teacherId",
                ResourceNames.TEACHERS, headers, uriInfo);
    }

    /**
     * getSectionAssessmentAssociations
     *
     * Returns each $$sectionAssessmentAssociations$$ that
     * references the given $$sections$$
     *
     * @param sectionId
     *            The id of the $$students$$.
     * @param offset
     *            Index of the first result to return
     * @param limit
     *            Maximum number of results to return.
     * @param expandDepth
     *            Number of hops (associations) for which to expand entities.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS)
    public Response getSectionAssessmentAssociations(@PathParam(ParameterConstants.SECTION_ID) final String sectionId,
                                                  @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "sectionId", sectionId, headers,
                uriInfo);
    }

    /**
     * getSectionAssessmentAssociationAssessments
     *
     * Returns each $$assessments$$ associated to the given section through
     * a $$sectionAssessmentAssociations$$
     *
     * @param sectionId
     *            The id of the $$sections$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Path("{" + ParameterConstants.SECTION_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.ASSESSMENTS)
    public Response getSectionAssessmentAssociationAssessments(
            @PathParam(ParameterConstants.SECTION_ID) final String sectionId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "sectionId", sectionId, "assessmentId",
                ResourceNames.ASSESSMENTS, headers, uriInfo);
    }
}
