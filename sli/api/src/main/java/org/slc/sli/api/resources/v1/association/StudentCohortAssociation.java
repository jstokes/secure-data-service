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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.config.ResourceNames;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.DefaultCrudEndpoint;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.resources.v1.ParameterConstants;
import org.slc.sli.api.resources.v1.PathConstants;

/**
 * Prototype new api end points and versioning
 * 
 * @author kmyers
 * 
 */
@Path(PathConstants.V1 + "/" + PathConstants.STUDENT_COHORT_ASSOCIATIONS)
@Component
@Scope("request")
@Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
public class StudentCohortAssociation extends DefaultCrudEndpoint {
 
    public static final String BEGIN_DATE = "beginDate";

    /**
     * Logging utility.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentCohortAssociation.class);
    
    @Autowired
    public StudentCohortAssociation(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.STUDENT_COHORT_ASSOCIATIONS);
        LOGGER.debug("New resource handler created: {}", this);
    }

    /**
     * Returns all $$studentCohortAssociations$$ entities for which the logged in User has permission and context.
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
        return super.readAll(offset, limit, headers, uriInfo);
    }

    /**
     * Create a new $$studentCohortAssociations$$ entity.
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
     *                 item is accessible.}
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response create(final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.create(newEntityBody, headers, uriInfo);
    }

    /**
     * Get a single $$studentCohortAssociations$$ entity
     * 
     * @param studentCohortAssociationId
     *            The Id of the $$studentCohortAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return A single $$schools$$ entity
     */
    @GET
    @Path("{" + ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID + "}")
    @Produces({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response read(@PathParam(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID) final String studentCohortAssociationId,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(studentCohortAssociationId, headers, uriInfo);
    }

    /**
     * Delete a $$studentCohortAssociations$$ entity
     * 
     * @param studentCohortAssociationId
     *            The Id of the $$studentCohortAssociations$$.
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return Returns a NOT_CONTENT status code
     * @response.representation.204.mediaType HTTP headers with a Not-Content status code.
     */
    @DELETE
    @Path("{" + ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID + "}")
    public Response delete(@PathParam(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID) final String studentCohortAssociationId, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.delete(studentCohortAssociationId, headers, uriInfo);
    }

    /**
     * Update an existing $$studentCohortAssociations$$ entity.
     * 
     * @param studentCohortAssociationId
     *            The id of the $$studentCohortAssociations$$.
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
    @Path("{" + ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID + "}")
    @Consumes({ MediaType.APPLICATION_JSON, HypermediaType.VENDOR_SLC_JSON })
    public Response update(@PathParam(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID) final String studentCohortAssociationId,
            final EntityBody newEntityBody, 
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.update(studentCohortAssociationId, newEntityBody, headers, uriInfo);
    }

    /**
     * Returns each $$students$$ that
     * references the given $$studentCohortAssociations$$
     * 
     * @param studentCohortAssociationId
     *            The Id of the studentCohortAssociation.
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
    @Path("{" + ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID + "}" + "/" + PathConstants.STUDENTS)
    public Response getStudentCohortAssocationStaff(@PathParam(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID) final String studentCohortAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
       return super.read(ResourceNames.STUDENT_COHORT_ASSOCIATIONS, "_id", studentCohortAssociationId, 
               ParameterConstants.STUDENT_ID, ResourceNames.STUDENTS, headers, uriInfo);
    }
    
    /**
     * Returns each $$cohorts$$ that
     * references the given $$studentCohortAssociations$$
     * 
     * @param studentCohortAssociationId
     *            The Id of the $$studentCohortAssociation$$.
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
    @Path("{" + ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID + "}" + "/" + PathConstants.COHORTS)
    public Response getStudentCohortAssocationCohorts(@PathParam(ParameterConstants.STUDENT_COHORT_ASSOCIATION_ID) final String studentCohortAssociationId,
            @QueryParam(ParameterConstants.OFFSET) @DefaultValue(ParameterConstants.DEFAULT_OFFSET) final int offset,
            @QueryParam(ParameterConstants.LIMIT) @DefaultValue(ParameterConstants.DEFAULT_LIMIT) final int limit,
            @Context HttpHeaders headers, @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.STUDENT_COHORT_ASSOCIATIONS, "_id", studentCohortAssociationId, 
                ParameterConstants.COHORT_ID, ResourceNames.COHORTS, headers, uriInfo);
    }
}