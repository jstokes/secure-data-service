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
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.api.resources.v1.PathConstants;

/**
 * Prototype new api end points and versioning
 * 
 * @author jstokes
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.PROGRAMS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class ProgramResource extends DefaultCrudEndpoint {
    
    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramResource.class);
    
    @Autowired
    public ProgramResource(EntityDefinitionStore entityDefs) {
        super(entityDefs);
    }

    /**
     * Returns all $$programs$$ entities for which the logged in User has permission and context.
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
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @GET
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.LIMIT, limit);
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.OFFSET, offset);
        return super.readAll(ResourceNames.PROGRAMS, headers, uriInfo);
    }

    /**
     * Create a new $$programs$$ entity.
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
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(ResourceNames.PROGRAMS, newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$programs$$ entity
     * 
     * @param programId
     *            The Id of the $$programs$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single program entity
     */
    @GET
    @Path("{" + ParameterConstants.PROGRAM_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.PROGRAM_ID) final String programId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.PROGRAMS, programId, headers, uriInfo);
    }

    /**
     * Delete a $$programs$$ entity
     * 
     * @param programId
     *            The Id of the $$programs$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.PROGRAM_ID + "}")
    public Response delete(@PathParam(ParameterConstants.PROGRAM_ID) final String programId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(ResourceNames.PROGRAMS, programId, headers, uriInfo);
    }

    /**
     * Update an existing $$programs$$ entity.
     * 
     * @param programId
     *            The id of the $$programs$$.
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @PUT
    @Path("{" + ParameterConstants.PROGRAM_ID + "}")
    public Response update(@PathParam(ParameterConstants.PROGRAM_ID) final String programId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(ResourceNames.PROGRAMS, programId, newEntityBody, headers, uriInfo);
    }
}
