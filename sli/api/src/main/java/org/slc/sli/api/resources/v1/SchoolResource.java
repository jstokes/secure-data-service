package org.slc.sli.api.resources.v1;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.representation.EntityBody;

/**
 * Prototype new api end points and versioning
 * 
 * @author srupasinghe
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.SCHOOLS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class SchoolResource {
    
    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolResource.class);
    
    /*
     * Interface capable of performing CRUD operations.
     */
    private final CrudEndpoint crudDelegate;

    @Autowired
    public SchoolResource(EntityDefinitionStore entityDefs) {
        this.crudDelegate = new DefaultCrudEndpoint(entityDefs, LOGGER);
    }

    /**
     * Returns all $$schools$$ entities for which the logged in User has permission and context.
     * 
     * @param offset
     *            starting position in results to return to user
     * @param limit
     *            maximum number of results to return to user (starting from offset)
     * @param uriInfo
     *            URI information including path and query parameters
     * @return result of CRUD operation
     */
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @GET
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.readAll(ResourceNames.SCHOOLS, offset, limit, headers, uriInfo);
    }

    /**
     * Create a new $$schools$$ entity.
     * 
     * @param newEntityBody
     *            entity data
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
        return this.crudDelegate.create(ResourceNames.SCHOOLS, newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$schools$$ entity
     * 
     * @param schoolId
     *            The Id of the $$schools$$.
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single school entity
     */
    @GET
    @Path("{" + ParameterConstants.SCHOOL_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.SCHOOLS, schoolId, headers, uriInfo);
    }

    /**
     * Delete a $$schools$$ entity
     * 
     * @param schoolId
     *            The Id of the $$schools$$.
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.SCHOOL_ID + "}")
    public Response delete(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.delete(ResourceNames.SCHOOLS, schoolId, headers, uriInfo);
    }

    /**
     * Update an existing $$schools$$ entity.
     * 
     * @param schoolId
     *            The id of the $$schools$$.
     * @param newEntityBody
     *            entity data
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @PUT
    @Path("{" + ParameterConstants.SCHOOL_ID + "}")
    public Response update(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.update(ResourceNames.SCHOOLS, schoolId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$studentSchoolAssociations$$ that
     * references the given $$schools$$
     * 
     * @param schoolId
     *            The Id of the School.
     * @param offset
     *            Index of the first result to return
     * @param limit
     *            Maximum number of results to return.
     * @param expandDepth
     *            Number of hops (associations) for which to expand entities.
     * @return       
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    @Path("{" + ParameterConstants.SCHOOL_ID + "}" + "/" + PathConstants.STUDENT_SCHOOL_ASSOCIATIONS)
    public Response getStudentSchoolAssociations(@PathParam(ParameterConstants.SCHOOL_ID) final String schoolId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit) {
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
}
