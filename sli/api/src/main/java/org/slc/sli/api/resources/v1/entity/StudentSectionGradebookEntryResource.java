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
 * StudentSectionGradebookEntryResource
 *
 * This entity holds a student's grade or competency level for a GradeBookEntry.
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_SECTION_GRADEBOOK_ENTRIES)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
public class StudentSectionGradebookEntryResource extends DefaultCrudEndpoint {

    @Autowired
    public StudentSectionGradebookEntryResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_SECTION_GRADEBOOK_ENTRIES);
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
     * @return all $$studentSectionGradebookEntries$$ the user has context to view
     *
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
     * create
     *
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *              URI information including path and query parameters
     * @return A 201 response on successfully created entity with the ID of the entity
     */
    @Override
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * read
     *
     * @param studentSectionGradebookEntryId
     *            The id (or list of ids) of the $$studentSectionGradebookEntries$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A list of entities matching the list of ids queried for
     *
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.STUDENT_SECTION_GRADEBOOK_ENTRY_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON, MediaType.APPLICATION_XML })
    public Response read(@PathParam(ParameterConstants.STUDENT_SECTION_GRADEBOOK_ENTRY_ID) final String studentSectionGradebookEntryId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(studentSectionGradebookEntryId, headers, uriInfo);
    }

    /**
     * delete
     *
     * @param studentSectionGradebookEntryId
     *            The Id of the $$studentSectionGradebookEntries$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.STUDENT_SECTION_GRADEBOOK_ENTRY_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STUDENT_SECTION_GRADEBOOK_ENTRY_ID) final String studentSectionGradebookEntryId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(studentSectionGradebookEntryId, headers, uriInfo);
    }

    /**
     * update
     *
     * @param studentSectionGradebookEntryId
     *            The id of the $$studentSectionGradebookEntries$$.
     * @param newEntityBody
     *            entity data
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Response with a NOT_CONTENT status code
     */
    @Override
    @PUT
    @Path("{" + ParameterConstants.STUDENT_SECTION_GRADEBOOK_ENTRY_ID + "}")
    public Response update(@PathParam(ParameterConstants.STUDENT_SECTION_GRADEBOOK_ENTRY_ID) final String studentSectionGradebookEntryId,
            final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(studentSectionGradebookEntryId, newEntityBody, headers, uriInfo);
    }
}
