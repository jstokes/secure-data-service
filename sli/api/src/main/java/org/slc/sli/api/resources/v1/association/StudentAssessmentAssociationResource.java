package org.slc.sli.api.resources.v1.association;

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
 * Prototype new api end points and versioning
 *
 * @author wscott
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_ASSESSMENT_ASSOCIATIONS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class StudentAssessmentAssociationResource extends DefaultCrudEndpoint {

    @Autowired
    public StudentAssessmentAssociationResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS);
    }

    /**
     * Returns all $$studentAssessmentAssociations$$ entities for which the logged in User has permission and context.
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
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * Create a new $$studentAssessmentAssociations$$ entity.
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
     *                 item is accessable.}
     */
    @Override
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON  })
    public Response create(final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$studentAssessmentAssociations$$ entity
     *
     * @param studentAssessmentAssociationId
     *            The Id of the $$studentAssessmentAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single $$studentAssessmentAssociations$$ entity
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID) final String studentAssessmentAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(studentAssessmentAssociationId, headers, uriInfo);
    }

    /**
     * Delete a $$studentAssessmentAssociations$$ entity
     *
     * @param studentAssessmentAssociationId
     *            The Id of the $$studentAssessmentAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID) final String studentAssessmentAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(studentAssessmentAssociationId, headers, uriInfo);
    }

    /**
     * Update an existing $$studentAssessmentAssociations$$ entity.
     *
     * @param studentAssessmentAssociationId
     *            The id of the $$studentAssessmentAssociations$$.
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
    @Path("{" + ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID + "}")
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response update(@PathParam(ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID) final String studentAssessmentAssociationId,
            final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(studentAssessmentAssociationId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ that
     * references the given $$studentAssessmentAssociations$$
     *
     * @param studentAssessmentAssociationId
     *            The Id of the studentAssessmentAssociationId.
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
     * @return
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID + "}" + "/" + PathConstants.STUDENTS)
    public Response getStudentsForAssociation(@PathParam(ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID) final String studentAssessmentAssociationId,
                                              @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
                                              @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
                                              @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "_id", studentAssessmentAssociationId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }

    /**
     * Returns each $$assessments$$ that
     * references the given $$studentAssessmentAssociations$$
     *
     * @param studentAssessmentAssociationId
     *            The Id of the studentAssessmentAssociationId.
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
     * @return
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID + "}" + "/" + PathConstants.ASSESSMENTS)
    public Response getAssessmentsForAssociation(@PathParam(ParameterConstants.STUDENT_ASSESSMENT_ASSOCIATION_ID) final String studentAssessmentAssociationId,
                                                 @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
                                                 @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
                                                 @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_ASSESSMENT_ASSOCIATIONS, "_id", studentAssessmentAssociationId, "assessmentId", ResourceNames.ASSESSMENTS, headers, uriInfo);
    }

}
