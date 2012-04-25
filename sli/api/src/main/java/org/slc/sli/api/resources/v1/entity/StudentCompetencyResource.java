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
 * Resource handler for StudentCompetency entity.
 * Stubbed out for documentation
 *
 * @author chung
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_COMPETENCIES)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
public class StudentCompetencyResource extends DefaultCrudEndpoint {

    @Autowired
    public StudentCompetencyResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_COMPETENCIES);
    }

    /**
     * Returns all $$studentCompetencies$$ entities for which the logged in user has permission to see.
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    @GET
    public Response readAll(
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Create a new $$studentCompetencies$$ entity.
     *
     * @param newEntityBody
     *            studentCompetency data
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
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Get a single $$studentCompetencies$$ entity
     *
     * @param studentCompetencyId
     *            The comma separated list of ids of $$studentCompetencies$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single studentCompetency entity
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.STUDENT_COMPETENCY_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    public Response read(@PathParam(ParameterConstants.STUDENT_COMPETENCY_ID) final String studentCompetencyId,
                         @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Delete an $$studentCompetencies$$
     *
     * @param studentCompetencyId
     *            The id of the $$studentCompetencies$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.STUDENT_COMPETENCY_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STUDENT_COMPETENCY_ID) final String studentCompetencyId,
                           @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return Response.status(Status.NOT_FOUND).build();
    }

    /**
     * Update an existing $$studentCompetencies$$
     *
     * @param studentCompetencyId
     *            The id of the $$studentCompetencies$$
     * @param newEntityBody
     *            studentCompetency data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @PUT
    @Path("{" + ParameterConstants.STUDENT_COMPETENCY_ID + "}")
    public Response update(@PathParam(ParameterConstants.STUDENT_COMPETENCY_ID) final String studentCompetencyId,
                           final EntityBody newEntityBody, @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return Response.status(Status.NOT_FOUND).build();
    }
}
