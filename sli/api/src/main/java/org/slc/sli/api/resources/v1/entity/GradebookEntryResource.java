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
 * GradebookEntryResource
 * 
 * This resource responds to create, read one, read all, update, and delete operations for gradebook entries. 
 * 
 * If you're looking for grades on a specific gradebook entry, use StudentSectionGradebookEntryResource instead.
 * 
 * Limitations: None
 * 
 * @author kmyers
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.GRADEBOOK_ENTRIES)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class GradebookEntryResource extends DefaultCrudEndpoint {
    
    /**
     * Logging utility.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GradebookEntryResource.class);
    
    @Autowired
    public GradebookEntryResource(EntityDefinitionStore entityDefs) {
        super(entityDefs);
        String debugMessage = "Initialized a new " + GradebookEntryResource.class;
        LOG.debug(debugMessage);
    }

    /**
     * readAll
     * 
     * @param offset
     *            starting position in results to return to user
     * @param limit
     *            maximum number of results to return to user (starting from offset)
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return all $$gradebookEntries$$ entities for which the logged in User has permission and context
     * @response.representation.200.mediaType HTTP headers with an OK status code.
     * @response.representation $$gradebookEntries$$
     */
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @GET
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.LIMIT, limit);
        ResourceUtil.putValue(headers.getRequestHeaders(), ParameterConstants.OFFSET, offset);
        return super.readAll(ResourceNames.GRADEBOOK_ENTRIES, headers, uriInfo);
    }

    /**
     * create
     * 
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *              URI information including path and 
     * @return response containing ID/location of newly created entity
     * @response.representation.201.mediaType HTTP headers with a CREATED status code.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(ResourceNames.GRADEBOOK_ENTRIES, newEntityBody, headers, uriInfo);
    }

    /**
     * read
     * 
     * @param gradebookEntryId
     *            The Id of the $$gradebookEntries$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return a single $$gradebookEntries$$ entity
     * @response.representation.200.mediaType HTTP headers with an OK status code.
     * @response.representation $$gradebookEntries$$
     */
    @GET
    @Path("{" + ParameterConstants.GRADEBOOK_ENTRY_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.GRADEBOOK_ENTRY_ID) final String gradebookEntryId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.GRADEBOOK_ENTRIES, gradebookEntryId, headers, uriInfo);
    }

    /**
     * delete
     * 
     * @param gradebookEntryId
     *            The Id of the $$gradebookEntries$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.GRADEBOOK_ENTRY_ID + "}")
    public Response delete(@PathParam(ParameterConstants.GRADEBOOK_ENTRY_ID) final String gradebookEntryId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(ResourceNames.GRADEBOOK_ENTRIES, gradebookEntryId, headers, uriInfo);
    }

    /**
     * update
     * 
     * Updates an existing $$gradebookEntries$$ entity.
     * 
     * @param gradebookEntryId
     *            The id of the $$gradebookEntries$$.
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
    @Path("{" + ParameterConstants.GRADEBOOK_ENTRY_ID + "}")
    public Response update(@PathParam(ParameterConstants.GRADEBOOK_ENTRY_ID) final String gradebookEntryId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(ResourceNames.GRADEBOOK_ENTRIES, gradebookEntryId, newEntityBody, headers, uriInfo);
    }
}
