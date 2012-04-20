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
 * @author jstokes
 */
@Path(PathConstants.V1 + "/" + PathConstants.PARENTS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
public class ParentResource extends DefaultCrudEndpoint {

    @Autowired
    public ParentResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.PARENTS);
    }

    /**
     * Returns all $$parents$$ entities for which the logged in User has permission and context.
     *
     * @param offset  starting position in results to return to user
     * @param limit   maximum number of results to return to user (starting from offset)
     * @param headers HTTP Request Headers
     * @param uriInfo URI information including path and query parameters
     * @return result of CRUD operation
     */
    @Override
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    @GET
    public Response readAll(@QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
                            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
                            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * Create a new $$parents$$ entity.
     *
     * @param newEntityBody entity data
     * @param headers       HTTP Request Headers
     * @param uriInfo       URI information including path and query parameters
     * @return result of CRUD operation
     * @response.param {@name Location} {@style header} {@type
     * {http://www.w3.org/2001/XMLSchema}anyURI} {@doc The URI where the created
     * item is accessible.}
     */
    @Override
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody,
                           @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$parents$$ entity
     *
     * @param parentId The Id of the $$parents$$.
     * @param headers  HTTP Request Headers
     * @param uriInfo  URI information including path and query parameters
     * @return A single parent entity
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.PARENT_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    public Response read(@PathParam(ParameterConstants.PARENT_ID) final String parentId,
                         @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(parentId, headers, uriInfo);
    }

    /**
     * Delete a $$parents$$ entity
     *
     * @param parentId The Id of the $$parents$$.
     * @param headers  HTTP Request Headers
     * @param uriInfo  URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.PARENT_ID + "}")
    public Response delete(@PathParam(ParameterConstants.PARENT_ID) final String parentId,
                           @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(parentId, headers, uriInfo);
    }

    /**
     * Update an existing $$parents$$ entity.
     *
     * @param parentId      The id of the $$parents$$.
     * @param newEntityBody entity data
     * @param headers       HTTP Request Headers
     * @param uriInfo       URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @PUT
    @Path("{" + ParameterConstants.PARENT_ID + "}")
    public Response update(@PathParam(ParameterConstants.PARENT_ID) final String parentId,
                           final EntityBody newEntityBody,
                           @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(parentId, newEntityBody, headers, uriInfo);
    }


    /**
     * Returns each $$studentParentAssociations$$ that
     * references the given $$students$$
     *
     * @param parentId The Id of the parent.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    @Path("{" + ParameterConstants.PARENT_ID + "}" + "/" + PathConstants.STUDENT_PARENT_ASSOCIATIONS)
    public Response getStudentParentAssociations(@PathParam(ParameterConstants.PARENT_ID) final String parentId,
                                                 @Context HttpHeaders headers,
                                                 @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PARENT_ASSOCIATIONS, "parentId", parentId, headers, uriInfo);
    }


    /**
     * $$studentParentAssociations$$ - student lookup
     *
     * @param parentId The Id of the Parent.
     * @param headers   HTTP Request Headers
     * @param uriInfo   URI information including path and query parameters
     * @return result of CRUD operation
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    @Path("{" + ParameterConstants.PARENT_ID + "}" + "/" + PathConstants.STUDENT_PARENT_ASSOCIATIONS + "/" + PathConstants.STUDENTS)
    public Response getStudentParentAssociationCourses(@PathParam(ParameterConstants.PARENT_ID) final String parentId,
                                                       @Context HttpHeaders headers,
                                                       @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_PARENT_ASSOCIATIONS, "parentId", parentId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }

}
