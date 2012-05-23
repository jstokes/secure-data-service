package org.slc.sli.api.resources.v1.association;

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
import org.slc.sli.client.constants.ResourceNames;
import org.slc.sli.client.constants.v1.ParameterConstants;
import org.slc.sli.client.constants.v1.PathConstants;

/**
 * Prototype new api end points and versioning
 *
 * @author kmyers
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_SECTION_ASSOCIATIONS)
@Component
@Scope("request")
public class StudentSectionAssociationResource extends DefaultCrudEndpoint {
    /**
     * Logging utility.
     */
//    private static final Logger LOGGER = LoggerFactory.getLogger(StudentSectionAssociationResource.class);

    @Autowired
    public StudentSectionAssociationResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_SECTION_ASSOCIATIONS);
//        DE260 - Logging of possibly sensitive data
//        LOGGER.debug("New resource handler created {}", this);
    }

    /**
     * Returns all $$studentSectionAssociations$$ entities for which the logged in User has permission and context.
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
     * Create a new $$studentSectionAssociations$$ entity.
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
    public Response create(final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$studentSectionAssociations$$ entity
     *
     * @param studentSectionAssociationId
     *            The Id of the $$studentSectionAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single $$studentSectionAssociations$$ entity
     */
    @Override
    @GET
    @Path("{" + ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID + "}")
    public Response read(@PathParam(ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID) final String studentSectionAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(studentSectionAssociationId, headers, uriInfo);
    }

    /**
     * Delete a $$studentSectionAssociations$$ entity
     *
     * @param studentSectionAssociationId
     *            The Id of the $$studentSectionAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @Override
    @DELETE
    @Path("{" + ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID) final String studentSectionAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(studentSectionAssociationId, headers, uriInfo);
    }

    /**
     * Update an existing $$studentSectionAssociations$$ entity.
     *
     * @param studentSectionAssociationId
     *            The id of the $$studentSectionAssociations$$.
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
    @Path("{" + ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID + "}")
    public Response update(@PathParam(ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID) final String studentSectionAssociationId,
            final EntityBody newEntityBody,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(studentSectionAssociationId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ that
     * references the given $$studentSectionAssociations$$
     *
     * @param studentSectionAssociationId
     *            The Id of the studentSectionAssociationId.
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
    @Path("{" + ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID + "}" + "/" + PathConstants.STUDENTS)
    public Response getStudents(@PathParam(ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID) final String studentSectionAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
       return super.read(ResourceNames.STUDENT_SECTION_ASSOCIATIONS, "_id", studentSectionAssociationId, "studentId", ResourceNames.STUDENTS, headers, uriInfo);
    }

    /**
     * Returns each $$sections$$ that
     * references the given $$studentSectionAssociations$$
     *
     * @param studentSectionAssociationId
     *            The Id of the studentSectionAssociationId.
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
    @Path("{" + ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID + "}" + "/" + PathConstants.SECTIONS)
    public Response getSections(@PathParam(ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID) final String studentSectionAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_SECTION_ASSOCIATIONS, "_id", studentSectionAssociationId, "sectionId", ResourceNames.SECTIONS, headers, uriInfo);
    }

    /**
     * getStudentCompetencies
     *
     * @param studentSectionAssociationId
     *            The Id of the studentSectionAssociationId.
     * @param offset
     *            Index of the first result to return
     * @param limit
     *            Maximum number of results to return.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return each $$studentCompetencies$$ that references the given $$studentSectionAssociations$$
     */
    @GET
    @Path("{" + ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID + "}" + "/" + PathConstants.STUDENT_COMPETENCIES)
    public Response getStudentCompetencies(@PathParam(ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID) final String studentSectionAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_COMPETENCIES, ParameterConstants.STUDENT_SECTION_ASSOCIATION_ID,
                studentSectionAssociationId, headers, uriInfo);
    }
}
