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
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.util.ResourceUtil;
import org.slc.sli.api.resources.v1.CrudEndpoint;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.api.resources.v1.PathConstants;

/**
 * Prototype new api end points and versioning
 * 
 * @author jstokes
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.TEACHERS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class TeacherResource {
    
    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherResource.class);
    
    /*
     * Interface capable of performing CRUD operations.
     */
    private final CrudEndpoint crudDelegate;

    @Autowired
    public TeacherResource(CrudEndpoint crudDelegate) {
        this.crudDelegate = crudDelegate;
    }

    /**
     * Returns all $$teachers$$ entities for which the logged in User has permission and context.
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
        return this.crudDelegate.readAll(ResourceNames.TEACHERS, headers, uriInfo);
    }

    /**
     * Create a new $$teachers$$ entity.
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
        return this.crudDelegate.create(ResourceNames.TEACHERS, newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$teachers$$ entity
     * 
     * @param teacherId
     *            The Id of the $$teachers$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single teacher entity
     */
    @GET
    @Path("{" + ParameterConstants.TEACHER_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.TEACHER_ID) final String teacherId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.read(ResourceNames.TEACHERS, teacherId, headers, uriInfo);
    }

    /**
     * Delete a $$teachers$$ entity
     * 
     * @param teacherId
     *            The Id of the $$teachers$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.TEACHER_ID + "}")
    public Response delete(@PathParam(ParameterConstants.TEACHER_ID) final String teacherId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.delete(ResourceNames.TEACHERS, teacherId, headers, uriInfo);
    }

    /**
     * Update an existing $$teachers$$ entity.
     * 
     * @param teacherId
     *            The id of the $$teachers$$.
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
    @Path("{" + ParameterConstants.TEACHER_ID + "}")
    public Response update(@PathParam(ParameterConstants.TEACHER_ID) final String teacherId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return this.crudDelegate.update(ResourceNames.TEACHERS, teacherId, newEntityBody, headers, uriInfo);
    }
    
    /**
     * @param teacherSectionAssociationId
     *            The id of the $$teacherSectionAssociations$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo 
     *            URI information including path and query parameters
     */
    @GET
    @Path("{" + ParameterConstants.TEACHER_ID + "}" + "/" + PathConstants.TEACHER_SECTION_ASSOCIATIONS + "/" + PathConstants.SECTIONS)
    public Response getSections(@PathParam(ParameterConstants.TEACHER_SECTION_ASSOCIATION_ID) final String teacherSectionAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return Response.status(Status.SERVICE_UNAVAILABLE).build();
    }
}
