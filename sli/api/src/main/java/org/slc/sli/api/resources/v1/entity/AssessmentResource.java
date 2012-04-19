package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.common.constants.ResourceNames;
import org.slc.sli.common.constants.v1.ParameterConstants;
import org.slc.sli.common.constants.v1.PathConstants;

/**
 * Resource for obtaining assessment information
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.ASSESSMENTS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class AssessmentResource extends DefaultCrudEndpoint {

    @Autowired
    public AssessmentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.ASSESSMENTS);
    }

    /**
     * Returns all $$assessments$$ entities for which the logged in user has permission to see.
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @GET
    public Response readAll(
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * Create a new $$assessments$$ entity.
     *
     * @param newEntityBody
     *            assessment data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     * @response.param {@name Location} {@style header} {@type
     *                 {http://www.w3.org/2001/XMLSchema}anyURI} {@doc The URI where the created
     *                 item is accessible.}
     */
    @Override
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$assessments$$ entity
     *
     * @param assessmentId
     *            The comma separated list of ids of $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single assessment entity
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(assessmentId, headers, uriInfo);
    }

    /**
     * Delete an $$assessments$$
     *
     * @param assessmentId
     *            The id of the $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(assessmentId, headers, uriInfo);
    }

    /**
     * Update an existing $$assessments$$
     *
     * @param assessmentId
     *            The id of the $$assessments$$
     * @param newEntityBody
     *            assessment data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @PUT
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}")
    public Response update(@PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId,
            final EntityBody newEntityBody, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(assessmentId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns all the $$studentAssessmentAssociations$$ for the given $$assessments$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$ entities
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS)
    public Response getStudentAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super
                .read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ entities associated to the given assessment through
     * a $$studentAssessmentAssociations$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.STUDENTS)
    public Response getStudentAssessmentAssociationsStudents(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, "studentId",
                ResourceNames.STUDENTS, headers, uriInfo);
    }

    /**
     * Returns each $$sectionAssessmentAssociations$$ entities that
     * references the given $$assessments$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS)
    public Response getSectionAssessmentAssociations(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super
                .read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, headers, uriInfo);
    }

    /**
     * Returns each $$sections$$ entity associated to the given $$assessments$$ through
     * a $$sectionAssessmentAssociations$$
     *
     * @param assessmentId
     *            Comma separated list of ids of the $$assessments$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.SECTION_ASSESSMENT_ASSOCIATIONS + "/"
            + PathConstants.SECTIONS)
    public Response getSectionAssessmentAssociationsSections(
            @PathParam(ParameterConstants.ASSESSMENT_ID) final String assessmentId, @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.SECTION_ASSESSMENT_ASSOCIATIONS, "assessmentId", assessmentId, "sectionId",
                ResourceNames.SECTIONS, headers, uriInfo);
    }

    /**
     * Get a map of the objective assessment ids to $$learningStandards$$ entities for the given $$assessments$$.
     *
     * @param id
     *            the id of the assessment
     * @return the learning standards
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.LEARNING_STANDARDS)
    public Response getLearningStandards(@PathParam(ParameterConstants.ASSESSMENT_ID) final String id) {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Get a map of the objective assessment ids and assessmentItem ids to $$learningObjectives$$ entities for the given $$assessments$$.
     *
     * @param id
     *            the id of the assessment
     * @return the learning objectives
     */
    @GET
    @Path("{" + ParameterConstants.ASSESSMENT_ID + "}" + "/" + PathConstants.LEARNING_OBJECTIVES)
    public Response getLearningObjectives(@PathParam(ParameterConstants.ASSESSMENT_ID) final String id) {
        return Response.status(Status.NOT_FOUND).build();
    }



}
